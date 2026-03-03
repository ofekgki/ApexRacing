package com.example.apexracing.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.apexracing.R
import com.example.apexracing.databinding.ItemPickImageBinding
import com.example.apexracing.models.Driver
import com.google.firebase.storage.FirebaseStorage

class FavoriteDriverAdapter(
    private val onSelected: (Driver) -> Unit
) : RecyclerView.Adapter<FavoriteDriverAdapter.VH>() {

    private val storageRef = FirebaseStorage.getInstance().reference

    private var items: List<Driver> = emptyList()
    var selectedId: String? = null
        private set

    class VH(val binding: ItemPickImageBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VH {
        val binding = ItemPickImageBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return VH(binding)
    }

    override fun onBindViewHolder(holder: VH, position: Int) {
        val d = items[position]

        // Selection UI
        val isSelected = d.id == selectedId
        holder.binding.itemPickCARD.strokeWidth = if (isSelected) 6 else 2
        holder.binding.itemPickCARD.setStrokeColor(
            holder.itemView.context.getColor(
                if (isSelected) R.color.brand_red else R.color.white
            )
        )

        val path = d.imgRef.trim().removePrefix("/")
        holder.binding.itemPickIMG.setImageDrawable(null)
        holder.binding.itemPickIMG.tag = path

        if (path.isNotBlank()) {
            storageRef.child(path).downloadUrl
                .addOnSuccessListener { uri ->
                    if (holder.binding.itemPickIMG.tag == path) {
                        Glide.with(holder.itemView)
                            .load(uri)
                            .placeholder(R.drawable.thestig)
                            .error(R.drawable.thestig)
                            .centerCrop()
                            .into(holder.binding.itemPickIMG)
                    }
                }

        }

        holder.itemView.setOnClickListener {
            val old = selectedId
            selectedId = d.id
            onSelected(d)
            if (old != null) notifyItemChanged(items.indexOfFirst { it.id == old })
            notifyItemChanged(position)
        }
    }

    override fun getItemCount() = items.size

    fun submitList(list: List<Driver>) {
        items = list
        notifyDataSetChanged()
    }
}