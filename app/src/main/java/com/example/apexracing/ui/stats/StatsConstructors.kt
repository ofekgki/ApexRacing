package com.example.apexracing.ui.stats

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.apexracing.adapters.ConstructorStandingAdapter
import com.example.apexracing.api.RetrofitClient
import com.example.apexracing.databinding.FragmentStatsConstructorsBinding
import com.example.apexracing.utilities.SharedStatsViewModel
import kotlinx.coroutines.launch


class StatsConstructors : Fragment() {

    private var binding: FragmentStatsConstructorsBinding? = null
    private lateinit var adapter: ConstructorStandingAdapter

    private val sharedViewModel: SharedStatsViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return FragmentStatsConstructorsBinding.inflate(inflater,
            container, false).also {
            binding = it
        }.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupRecyclerView()

        sharedViewModel.selectedYear.observe(viewLifecycleOwner) { year ->
            loadConstructorStandings(year)
        }
    }

    private fun setupRecyclerView() {
        adapter = ConstructorStandingAdapter()
        binding?.standingRecyclerView?.apply {
            layoutManager = LinearLayoutManager(requireContext())
            adapter = this.adapter
            setHasFixedSize(true)
        }
    }

    private fun loadConstructorStandings(year: String) {
        viewLifecycleOwner.lifecycleScope.launch {
            try {
                val constructors = RetrofitClient.constructorsApiService.getConstructorStandings(year)
                println("Fetched ${constructors.size} constructors for year $year")
                adapter.submitList(constructors)
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