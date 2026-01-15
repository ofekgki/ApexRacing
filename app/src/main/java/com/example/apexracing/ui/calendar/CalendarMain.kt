package com.example.apexracing.ui.calendar

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import com.example.apexracing.databinding.FragmentCalendarMainBinding


class CalendarMain : Fragment() {

    private var binding: FragmentCalendarMainBinding? = null

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentCalendarMainBinding.bind(view)

    }


}