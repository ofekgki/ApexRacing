package com.example.apexracing.ui.community

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import com.example.apexracing.adapters.PostAdapter
import com.example.apexracing.databinding.FragmentCommunityMainBinding
import com.example.apexracing.models.Post
import com.example.apexracing.models.UserViewModel
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import kotlin.getValue

class CommunityMain : Fragment() {
    private lateinit var binding: FragmentCommunityMainBinding

    private val db = FirebaseFirestore.getInstance()

    private val userVM: UserViewModel by activityViewModels()

    private val rtdb = FirebaseDatabase.getInstance()

    private val storageRef = FirebaseStorage.getInstance().reference

    private val postAdapter = PostAdapter(emptyList())



    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        binding = FragmentCommunityMainBinding.inflate(inflater,
            container, false)
        binding.communityRVPosts.adapter = postAdapter
        loadPostsFromFirebase()
        return binding.root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewLifecycleOwner.lifecycleScope.launch {
            userVM.state.collect { s ->
                if (s !is UserViewModel.UserUiState.Ready) return@collect
                val user = s.user




            }

        }
    }


    private fun loadPostsFromFirebase() {

        suspend {
            val postsCollection = rtdb.getReference("posts").get().await()

            val posts = mutableListOf<Post>()
            for (postSnapshot in postsCollection.children) {
                val post = postSnapshot.getValue(Post::class.java)
                post?.let { posts.add(it) }
            }

            postAdapter.posts = posts
            postAdapter.notifyDataSetChanged()

        }
    }

}



