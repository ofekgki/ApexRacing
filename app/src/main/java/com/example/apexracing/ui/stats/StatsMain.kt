package com.example.apexracing.ui.stats

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.apexracing.databinding.FragmentStatsMainBinding

class StatsMain : Fragment() {

    private lateinit var binding: FragmentStatsMainBinding

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?
                              , savedInstanceState: Bundle?): View
    {
        binding = FragmentStatsMainBinding.inflate(inflater, container, false)
        return binding.root
    }
}