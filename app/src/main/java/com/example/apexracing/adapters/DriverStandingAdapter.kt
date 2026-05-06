package com.example.apexracing.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.graphics.toColorInt
import androidx.recyclerview.widget.RecyclerView
import com.example.apexracing.databinding.ItemPositionDriverBinding
import com.example.apexracing.models.FlatDriverStanding

class DriverStandingAdapter : RecyclerView.Adapter<DriverStandingAdapter.VH>() {

    private var drivers: List<FlatDriverStanding> = emptyList()

    class VH(val binding: ItemPositionDriverBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VH {
        val binding = ItemPositionDriverBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return VH(binding)
    }

    override fun onBindViewHolder(holder: VH, position: Int) {
        val driver = drivers[position]

        // Position
        holder.binding.itemPosition.text = driver.position.toString()

        // Driver Name
        holder.binding.itemName.text = driver.fullName

        // Points
        holder.binding.itemPoints.text = driver.points.toString()

        //Color
        holder.binding.itemColorLine.setBackgroundColor(driver.color.toColorInt())

        //Team Name
        holder.binding.itemTeam.text = driver.constructorName
    }

    override fun getItemCount() = drivers.size

    fun submitList(list: List<FlatDriverStanding>) {
        drivers = list
        notifyDataSetChanged()
    }
}
