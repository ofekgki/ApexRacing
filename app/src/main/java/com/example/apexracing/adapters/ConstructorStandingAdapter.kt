package com.example.apexracing.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.graphics.toColorInt
import androidx.recyclerview.widget.RecyclerView
import com.example.apexracing.databinding.ItemPositionConstructorBinding
import com.example.apexracing.models.FlatConstructorStanding

class ConstructorStandingAdapter : RecyclerView.Adapter<ConstructorStandingAdapter.VH>() {

    private var constructors: List<FlatConstructorStanding> = emptyList()

    class VH(val binding: ItemPositionConstructorBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VH {
        val binding = ItemPositionConstructorBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return VH(binding)
    }

    override fun onBindViewHolder(holder: VH, position: Int) {
        val constructor = constructors[position]

        // Position
        holder.binding.itemPosition.text = constructor.position.toString()

        // Team Name
        holder.binding.itemName.text = constructor.name

        // Points
        holder.binding.itemPoints.text = constructor.points.toString()

        // Color
        holder.binding.itemColorLine.setBackgroundColor(constructor.color.toColorInt())
    }

    override fun getItemCount() = constructors.size

    fun submitList(list: List<FlatConstructorStanding>) {
        constructors = list
        notifyDataSetChanged()
    }
}
