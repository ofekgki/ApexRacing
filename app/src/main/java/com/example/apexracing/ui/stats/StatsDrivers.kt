package com.example.apexracing.ui.stats

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.apexracing.adapters.DriverStandingAdapter
import com.example.apexracing.api.RetrofitClient
import com.example.apexracing.databinding.FragmentStatsDriversBinding
import com.example.apexracing.utilities.SharedStatsViewModel
import kotlinx.coroutines.launch

class StatsDrivers : Fragment() {
    private var binding: FragmentStatsDriversBinding? = null
    private lateinit var adapter: DriverStandingAdapter

    private val sharedViewModel: SharedStatsViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return FragmentStatsDriversBinding.inflate(inflater,
            container, false).also {
            binding = it
        }.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupRecyclerView()
        sharedViewModel.selectedYear.observe(viewLifecycleOwner) { year ->
            loadDriverStandings(year)
        }
    }

    private fun setupRecyclerView() {
        adapter = DriverStandingAdapter()
        binding?.standingRecyclerView?.apply {
            layoutManager = LinearLayoutManager(requireContext())
            adapter = this.adapter
            setHasFixedSize(true)
        }
    }

    private fun loadDriverStandings(year: String) {
        viewLifecycleOwner.lifecycleScope.launch {
            try {
                val drivers = RetrofitClient.driversApiService.getDriverStandings(year)
                println("Fetched ${drivers.size} drivers for year $year")
                adapter.submitList(drivers)
            }
            catch (e: Exception) {
                println("Error fetching data: ${e.message}")
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        binding = null
    }

}