package com.example.apexracing.ui.fantasy

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.apexracing.databinding.FragmentFantasyMainBinding

class FantasyMain : Fragment() {
    private lateinit var binding: FragmentFantasyMainBinding

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?
                              , savedInstanceState: Bundle?): View
    {
        binding = FragmentFantasyMainBinding.inflate(inflater, container, false)
        return binding.root
    }

}