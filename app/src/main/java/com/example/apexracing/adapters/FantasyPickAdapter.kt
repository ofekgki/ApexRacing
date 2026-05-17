package com.example.apexracing.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.apexracing.R
import com.example.apexracing.databinding.ItemFantasyBinding
import com.example.apexracing.models.FantasyItem
import com.example.apexracing.models.User.UserViewModel
import com.google.android.material.imageview.ShapeableImageView
import com.google.firebase.storage.FirebaseStorage

class FantasyPickAdapter(
    private var items: List<FantasyItem> = emptyList(),
    private val onAddClicked: (FantasyItem) -> Unit
) : RecyclerView.Adapter<FantasyPickAdapter.VH>() {

    class VH(val binding: ItemFantasyBinding) : RecyclerView.ViewHolder(binding.root)

    private val storageRef = FirebaseStorage.getInstance().reference

    private var selectedDriverIds: Set<String> = emptySet()
    private var selectedConstructorIds: Set<String> = emptySet()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VH {
        val binding = ItemFantasyBinding.inflate(
            LayoutInflater.from(parent.context), parent, false
        )
        return VH(binding)
    }

    override fun onBindViewHolder(holder: VH, position: Int) {
        val item = items[position]

        with(holder.binding) {
            itemName.text = item.name
            itemPoints.text = "${item.points} Pts"
            itemPrice.text = "$%.1fM".format(item.price ?: 0f)

            val isSelected = when (item.type) {
                UserViewModel.PickType.DRIVER -> selectedDriverIds.contains(item.id)
                UserViewModel.PickType.CONSTRUCTOR -> selectedConstructorIds.contains(item.id)
            }

            addButton.setImageResource(
                if (isSelected) R.drawable.ic_remove else R.drawable.ic_add
            )

            bindImage(
                imageView = itemImage,
                path = item.imageUrl,
                isDriver = item.type == UserViewModel.PickType.DRIVER
            )

            addButton.setOnClickListener {
                onAddClicked(item)
            }
        }
    }

    override fun getItemCount(): Int = items.size

    fun submitList(newItems: List<FantasyItem>) {
        items = newItems
        notifyDataSetChanged()
    }

    fun setSelectedItems(
        driverIds: List<String>,
        constructorIds: List<String>
    ) {
        selectedDriverIds = driverIds.toSet()
        selectedConstructorIds = constructorIds.toSet()
        notifyDataSetChanged()
    }

    private fun bindImage(imageView: ShapeableImageView, path: String?, isDriver: Boolean) {
        imageView.scaleType = if (isDriver) {
            ImageView.ScaleType.CENTER_CROP
        } else {
            ImageView.ScaleType.FIT_CENTER
        }

        val cleanPath = path?.trim()?.removePrefix("/")

        if (cleanPath.isNullOrBlank()) {
            imageView.setImageResource(R.drawable.thestig)
            return
        }

        storageRef.child(cleanPath).downloadUrl
            .addOnSuccessListener { uri ->
                val request = Glide.with(imageView)
                    .load(uri)
                    .placeholder( if(isDriver) R.drawable.thestig else R.drawable.f1logo)
                    .error(if(isDriver) R.drawable.thestig else R.drawable.f1logo)

                request.into(imageView)
            }
            .addOnFailureListener {
                imageView.setImageResource(if(isDriver) R.drawable.thestig else R.drawable.f1logo)
            }
    }
}