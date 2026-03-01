package com.example.apexracing.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.apexracing.databinding.ItemCalenderBinding
import com.example.apexracing.models.*

class CircuitAdapter(var races: List<Circuit> = listOf(Circuit.Builder()
    .circuitName("No Data...").build())) : RecyclerView.Adapter<CircuitAdapter.CircuitViewHolder>() {


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

        override fun onBindViewHolder(
            holder: CircuitViewHolder,
            position: Int
        ) {
            with(holder) {
                with(getItem(position)) {
                    binding.itemRaceRowTitle.text = displayName
                    binding.itemRaceRowMonth.text = getMonth()
                    binding.itemRaceRowDay.text = "%02d".format(startTime.day)
                    binding.itemRaceRowMeta.text = "R %02d • %s".format(round, city)


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
