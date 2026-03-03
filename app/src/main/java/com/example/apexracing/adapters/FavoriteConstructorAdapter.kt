package com.example.apexracing.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.apexracing.R
import com.example.apexracing.databinding.ItemPickImageBinding
import com.example.apexracing.models.Constructor
import com.google.firebase.storage.FirebaseStorage

class FavoriteConstructorAdapter(
    private val onSelected: (Constructor) -> Unit
) : RecyclerView.Adapter<FavoriteConstructorAdapter.VH>() {

    private val storageRef = FirebaseStorage.getInstance().reference

    private var items: List<Constructor> = emptyList()
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
        val t = items[position]

        val isSelected = t.id == selectedId
        holder.binding.itemPickCARD.strokeWidth = if (isSelected) 6 else 2
        holder.binding.itemPickCARD.setStrokeColor(
            holder.itemView.context.getColor(
                if (isSelected) R.color.brand_red else R.color.white
            )
        )

        val path = t.imgRef.trim().removePrefix("/")
        holder.binding.itemPickIMG.setImageDrawable(null)
        holder.binding.itemPickIMG.tag = path

        if (path.isNotBlank()) {
            storageRef.child(path).downloadUrl
                .addOnSuccessListener { uri ->
                    if (holder.binding.itemPickIMG.tag == path) {
                        Glide.with(holder.itemView)
                            .load(uri)
                            .placeholder(R.drawable.f1logo)
                            .error(R.drawable.f1logo)
                            .into(holder.binding.itemPickIMG)
                    }
                }

        }

        holder.itemView.setOnClickListener {
            val old = selectedId
            selectedId = t.id
            onSelected(t)

            if (old != null) notifyItemChanged(items.indexOfFirst { it.id == old })
            notifyItemChanged(position)
        }
    }

    override fun getItemCount() = items.size

    fun submitList(list: List<Constructor>) {
        items = list
        notifyDataSetChanged()
    }
}