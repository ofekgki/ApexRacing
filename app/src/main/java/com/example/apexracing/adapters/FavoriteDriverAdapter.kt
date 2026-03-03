package com.example.apexracing.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.apexracing.R
import com.example.apexracing.databinding.ItemFavoriteDriverBinding
import com.example.apexracing.models.Driver
import com.example.apexracing.utilities.DBData
import com.google.firebase.storage.FirebaseStorage

class FavoriteDriverAdapter(private val onSelected: (Driver) -> Unit)
    : RecyclerView.Adapter<FavoriteDriverAdapter.VH>() {
    class VH(val binding: ItemFavoriteDriverBinding) : RecyclerView.ViewHolder(binding.root)

    var selectedId: String? = null
        private set

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VH {
        val binding = ItemFavoriteDriverBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return VH(binding)
    }

    override fun onBindViewHolder(holder: VH, position: Int) {
        val driver = DBData.drivers[position]
        holder.binding.itemDriverLBLName.text = buildString {
            append(driver.givenName)
            append(" ")
            append(driver.familyName)
        }
        val teamName = DBData.getTeamNameForDriver(driver)
        holder.binding.itemDriverLBLMeta.text = teamName ?: ""

        // image
        val storageRef = FirebaseStorage.getInstance().reference.child(driver.imgRef)
        Glide.with(holder.itemView)
            .load(storageRef)
            .placeholder(R.drawable.user_profile_blank)
            .error(R.drawable.user_profile_blank)
            .into(holder.binding.itemDriverIMG)

        // selection UI
        val isSelected = (driver.id == selectedId)
        holder.binding.itemDriverCARD.strokeWidth = if (isSelected) 5 else 1

        holder.itemView.setOnClickListener {
            selectedId = driver.id
            notifyDataSetChanged()
            onSelected(driver)
        }
    }

    override fun getItemCount(): Int {
        return DBData.drivers.size
    }

}