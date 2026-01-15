package com.example.apexracing.ui.calendar

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.apexracing.databinding.FragmentCalendarMainBinding


class CalendarMain : Fragment() {

    private lateinit var binding: FragmentCalendarMainBinding

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?
                              , savedInstanceState: Bundle?): View
    {
        binding = FragmentCalendarMainBinding.inflate(inflater, container, false)
        return binding.root
    }

}