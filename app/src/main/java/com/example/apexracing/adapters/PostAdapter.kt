package com.example.apexracing.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.apexracing.R
import com.example.apexracing.databinding.ItemPostBinding
import com.example.apexracing.models.Post
import com.example.apexracing.utilities.UtilitiesFunctions
import com.google.firebase.storage.FirebaseStorage

class PostAdapter(
    var posts: List<Post> = emptyList(), private val onLikeClick: (Post) -> Unit
) : RecyclerView.Adapter<PostAdapter.PostViewHolder>() {
    val storageRef = FirebaseStorage.getInstance().reference

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): PostViewHolder {
        val binding = ItemPostBinding
            .inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        return PostViewHolder(binding)
    }

    override fun onBindViewHolder(holder: PostViewHolder, position: Int) {
        val post = getItem(position)

        holder.binding.postTXTName.text = post.userName
        holder.binding.postTXTContent.text = post.postText
        holder.binding.postLBLLikesCount.text = "${post.likes} Likes"

        holder.binding.postTXTTime.text = UtilitiesFunctions().getTimeSince(post.time)

        val pathPhoto = "ProfileImages/${post.userId}/profile_image.jpg"

        if (pathPhoto.isNotBlank()) {
            storageRef.child(pathPhoto).downloadUrl
                .addOnSuccessListener { uri ->
                    Glide.with(holder.itemView)
                        .load(uri)
                        .into(holder.binding.postIMGAvatar)
                }
                .addOnFailureListener {
                    Glide.with(holder.itemView)
                        .load(R.drawable.user_profile_blank)
                        .into(holder.binding.postIMGAvatar)
                }
        }

        holder.binding.postBTNLike.setOnClickListener {
            onLikeClick(post)
        }
    }

    override fun getItemCount(): Int {
        return posts.size
    }

    fun getItem(position: Int): Post = posts[position]
    class PostViewHolder(val binding: ItemPostBinding) :
        RecyclerView.ViewHolder(binding.root)


}

