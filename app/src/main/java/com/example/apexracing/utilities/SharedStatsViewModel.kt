package com.example.apexracing.utilities

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class SharedStatsViewModel : ViewModel() {

    private val _selectedYear = MutableLiveData("2026")
    val selectedYear: LiveData<String> = _selectedYear

    fun updateYear(year: String) {
        _selectedYear.value = year
    }
}