package com.example.apexracing.ui.Cummunity

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.apexracing.R
import com.example.apexracing.databinding.FragmentCommunityMainBinding

class CommunityMain : Fragment() {
    private lateinit var binding: FragmentCommunityMainBinding

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?
                              , savedInstanceState: Bundle?): View
    {
        binding = FragmentCommunityMainBinding.inflate(inflater, container, false)
        return binding.root
    }

}