package com.example.apexracing.ui.explore

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.example.apexracing.R
import com.example.apexracing.databinding.FragmentExploreMainBinding
import com.example.apexracing.ui.Cummunity.CommunityMain
import com.example.apexracing.ui.stats.StatsMain
import com.google.android.material.tabs.TabLayoutMediator

class ExploreMain : Fragment(R.layout.fragment_explore_main) {

    private var binding: FragmentExploreMainBinding? = null

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentExploreMainBinding.bind(view)

        binding!!.explorePAGER.adapter = object : FragmentStateAdapter(this) {
            override fun getItemCount() = 2
            override fun createFragment(position: Int): Fragment =
                if (position == 0) CommunityMain() else StatsMain()
        }

        TabLayoutMediator(binding!!.exploreTAB, binding!!.explorePAGER) { tab, position ->
            tab.text = if (position == 0) "Community" else "Stats"
        }.attach()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        binding = null
    }
}
