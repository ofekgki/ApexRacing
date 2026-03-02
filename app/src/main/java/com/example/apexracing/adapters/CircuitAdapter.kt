package com.example.apexracing.adapters

import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.apexracing.databinding.ItemCalenderBinding
import com.example.apexracing.models.*
import com.google.firebase.Firebase
import com.google.firebase.storage.FirebaseStorage

class CircuitAdapter(var races: List<Circuit> = emptyList())
    : RecyclerView.Adapter<CircuitAdapter.CircuitViewHolder>() {

    val storageRef = FirebaseStorage.getInstance().reference

    override fun onCreateViewHolder(
            parent: ViewGroup,
            viewType: Int
        ): CircuitViewHolder {
            val binding = ItemCalenderBinding
                .inflate(
                    LayoutInflater.from(parent.context),
                    parent,
                    false
                )
            return CircuitViewHolder(binding)
        }

    override fun onBindViewHolder(holder: CircuitViewHolder, position: Int) {
        val circuit = getItem(position)

        holder.binding.itemRaceRowTitle.text = circuit.displayName
        holder.binding.itemRaceRowMonth.text = circuit.getMonth()
        val dayStr = java.text.SimpleDateFormat("dd", java.util.Locale.US).format(circuit.startTime)
        holder.binding.itemRaceRowDay.text = dayStr
        holder.binding.itemRaceRowMeta.text = "R %02d • %s".format(circuit.round, circuit.city)

        val path = circuit.layoutRef
        Log.d("LAYOUT_PATH", "circuit=${circuit.circuitName} path=$path")
        if (!path.isNullOrBlank()) {
            storageRef.child(path).downloadUrl
                .addOnSuccessListener { uri ->
                    Glide.with(holder.itemView)
                        .load(uri)
                        .into(holder.binding.itemRaceRowTrackIcon)
                }
        }
    }

    override fun getItemCount(): Int {
        return races.size
    }

    fun getItem(position: Int): Circuit = races[position]
        class CircuitViewHolder(val binding: ItemCalenderBinding):
            RecyclerView.ViewHolder(binding.root)


    }
