package com.example.apexracing.ui.profile

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.apexracing.databinding.FragmentProfileMainBinding

class ProfileMain : Fragment() {

    private lateinit var binding: FragmentProfileMainBinding

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?
                              , savedInstanceState: Bundle?): View
    {
        binding = FragmentProfileMainBinding.inflate(inflater, container, false)
        return binding.root
    }

}