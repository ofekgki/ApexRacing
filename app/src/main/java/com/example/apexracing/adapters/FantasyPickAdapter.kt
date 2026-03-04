package com.example.apexracing.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.apexracing.R
import com.example.apexracing.databinding.ItemFantasyBinding
import com.example.apexracing.models.FantasyItem
import com.google.firebase.storage.FirebaseStorage

class FantasyPickAdapter(
    private var items: List<FantasyItem> = emptyList(),
    private val onAddClicked: (FantasyItem) -> Unit
) : RecyclerView.Adapter<FantasyPickAdapter.VH>() {

    class VH(val binding: ItemFantasyBinding) : RecyclerView.ViewHolder(binding.root)

    private val storageRef = FirebaseStorage.getInstance().reference


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
            itemPoints.text = "%d Pts".format(item.points)
            itemPrice.text = "$%.1fM".format(item.price)

            val path = item.imageUrl.trim().removePrefix("/")
            if (path.isBlank()) {
                itemImage.setImageResource(R.drawable.thestig)
            } else {
                storageRef.child(path).downloadUrl
                    .addOnSuccessListener { uri ->
                        Glide.with(holder.binding.root)
                            .load(uri)
                            .placeholder(R.drawable.thestig)
                            .error(R.drawable.thestig)
                            .into(itemImage)
                    }
                    .addOnFailureListener {
                        itemImage.setImageResource(R.drawable.thestig)
                    }
            }
            addButton.setOnClickListener { onAddClicked(item) }
        }
    }

    override fun getItemCount() = items.size

    fun submitList(newItems: List<FantasyItem>) {
        items = newItems
        notifyDataSetChanged()
    }
}

