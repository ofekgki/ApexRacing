package com.example.apexracing.ui.community

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.bumptech.glide.Glide
import com.example.apexracing.R
import com.example.apexracing.adapters.PostAdapter
import com.example.apexracing.databinding.FragmentCommunityMainBinding
import com.example.apexracing.models.Post
import com.example.apexracing.models.User.UserViewModel
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.storage.FirebaseStorage
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.mapNotNull
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await

class CommunityMain : Fragment() {
    private lateinit var binding: FragmentCommunityMainBinding

    private val userVM: UserViewModel by activityViewModels()

    private val rtdb = FirebaseDatabase.getInstance()

    private val postAdapter = PostAdapter(emptyList()) { post ->
        likePost(post)
    }


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        binding = FragmentCommunityMainBinding.inflate(
            inflater,
            container, false
        )
        binding.communityRVPosts.adapter = postAdapter
        return binding.root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        loadPostsFromFirebase()


        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                userVM.state
                    .mapNotNull { it as? UserViewModel.UserUiState.Ready }
                    .map { it.uid }
                    .distinctUntilChanged() // אוסף רק כשקורה שינוי id
                    .collect { uid ->
                        loadProfileImage(uid)
                    }
            }
        }

        binding.communityBTNPost.setOnClickListener {

            val text = binding.communityEDTPost.text.toString().trim()

            if (text.length < 10) {

                Toast.makeText(
                    requireContext(), "Post must be at least 10 characters long", Toast.LENGTH_SHORT
                ).show()

                return@setOnClickListener
            }

            viewLifecycleOwner.lifecycleScope.launch {
                val s = userVM.state.first { // לוקח מידע רק פעם אחת - כאשר לוחצים על post
                    it is UserViewModel.UserUiState.Ready
                } as UserViewModel.UserUiState.Ready
                val user = s.user



                uploadPost(
                    userId = s.uid,
                    userName = user.username,
                    postText = text
                )

                binding.communityEDTPost.text?.clear()

            }
        }

    }


private suspend fun uploadPost(userId: String, userName: String, postText: String) {

    val postRef = rtdb.getReference("posts").push()
    val postId = postRef.key ?: return

    val post = Post(
        postId = postId,
        userId = userId,
        userName = userName,
        postText = postText,
        time = System.currentTimeMillis(),
        likes = 0
    )

    postRef.setValue(post).await()

    loadPostsFromFirebase()
}

    private fun loadPostsFromFirebase() {
        viewLifecycleOwner.lifecycleScope.launch {
            try {
                val postsSnapshot = rtdb.getReference("posts").get().await()

                val posts = mutableListOf<Post>()

                for (postSnapshot in postsSnapshot.children) {
                    val post = postSnapshot.getValue(Post::class.java)
                    post?.let { posts.add(it) }
                }

                postAdapter.posts = posts.sortedByDescending { it.time }
                postAdapter.notifyDataSetChanged()

            } catch (e: Exception) {
                Log.e("CommunityMain", "Failed to load posts", e)
                Toast.makeText(requireContext(), "Failed to load posts", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun loadProfileImage(userId: String) {
        FirebaseDatabase.getInstance()
            .reference
            .child("users")
            .child(userId)
            .child("imgRef")
            .get()
            .addOnSuccessListener { snapshot ->

                val imgRef = snapshot.getValue(String::class.java)

                Log.d("IMG_REF", imgRef.toString())

                if (!imgRef.isNullOrEmpty()) {
                    FirebaseStorage.getInstance()
                        .getReference(imgRef)
                        .downloadUrl
                        .addOnSuccessListener { uri ->
                            Glide.with(binding.root)
                                .load(uri)
                                .into(binding.communityIMGAvatar)
                        }

                }
            }
            .addOnFailureListener { e ->
                binding.communityIMGAvatar.setImageResource(R.drawable.user_profile_blank)
                Log.e("LoadImage", "Failed to load profile image", e)
            }
    }

    private fun likePost(post: Post) {
        viewLifecycleOwner.lifecycleScope.launch {
            try {
                val s = userVM.state.first {
                    it is UserViewModel.UserUiState.Ready
                } as UserViewModel.UserUiState.Ready

                val currentUserId = s.uid

                val postRef = rtdb
                    .getReference("posts")
                    .child(post.postId)

                val snapshot = postRef.get().await()
                val currentPost = snapshot.getValue(Post::class.java) ?: return@launch

                if (currentPost.likedBy.containsKey(currentUserId)) {
                    Toast.makeText(requireContext(), "You already liked this post",
                        Toast.LENGTH_SHORT).show()
                    return@launch
                }

                val updates = mapOf<String, Any>(
                    "likes" to currentPost.likes + 1,
                    "likedBy/$currentUserId" to true
                )

                postRef.updateChildren(updates).await()

                loadPostsFromFirebase()

            } catch (e: Exception) {
                Log.e("CommunityMain", "Failed to like post", e)
                Toast.makeText(requireContext(), "Failed to like post",
                    Toast.LENGTH_SHORT).show()
            }
        }
    }
}



