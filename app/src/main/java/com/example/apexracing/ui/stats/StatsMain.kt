package com.example.apexracing.ui.stats

import android.os.Bundle
import android.view.View
import android.widget.ArrayAdapter
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.example.apexracing.databinding.FragmentStatsMainBinding
import com.example.apexracing.utilities.SharedStatsViewModel
import com.google.android.material.tabs.TabLayoutMediator

class StatsMain : Fragment() {

    private var binding: FragmentStatsMainBinding? = null

    private val sharedViewModel: SharedStatsViewModel by activityViewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentStatsMainBinding.bind(view)

        setupSeasonDropdown()
        binding!!.explorePAGER.adapter = object : FragmentStateAdapter(this) {
            override fun getItemCount() = 2
            override fun createFragment(position: Int): Fragment =
                if (position == 0) StatsDrivers() else StatsConstructors()
        }

        TabLayoutMediator(binding!!.exploreTAB, binding!!.explorePAGER) { tab, position ->
            tab.text = if (position == 0) "Drivers" else "Constructors"
        }.attach()
    }

    private fun setupSeasonDropdown() {
        val yearsList = (2026 downTo 1950).map { it.toString() }
        val adapter =
            ArrayAdapter(requireContext(), android.R.layout.simple_dropdown_item_1line, yearsList)
        binding?.statsSeasonSelector?.setAdapter(adapter)
        binding?.statsSeasonSelector?.setOnItemClickListener { parent, _, position, _ ->
            val selectedYear = parent.getItemAtPosition(position).toString()
            sharedViewModel.updateYear(selectedYear)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        binding = null
    }
}