package com.example.apexracing.ui.calendar

import androidx.lifecycle.*
import com.example.apexracing.api.RetrofitClient
import com.example.apexracing.models.*
import com.example.apexracing.utilities.*
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.time.ZonedDateTime

class CalendarViewModel : ViewModel() {

    private val api = RetrofitClient.api

    private val _races = MutableLiveData<List<Race>>(emptyList())
    val races: LiveData<List<Race>> = _races

    private val _nextRace = MutableLiveData<Race?>(null)
    val nextRace: LiveData<Race?> = _nextRace

    private val _countdownText = MutableLiveData<String>("")
    val countdownText: LiveData<String> = _countdownText

    private val _error = MutableLiveData<String?>(null)
    val error: LiveData<String?> = _error

    private var countdownJob: Job? = null

    fun loadCalendar(season: String = "current") {
        viewModelScope.launch {
            try {
                val res = api.(season)
                val list = res.mrData.raceTable.races.map { it.toDomain() }
                    .sortedBy { it.toStartDateTimeUtc()?.toInstant()?.epochSecond ?: Long.MAX_VALUE }

                _races.value = list
                _error.value = null

                val now = ZonedDateTime.now(java.time.ZoneId.of("Asia/Jerusalem"))
                val upcoming = list.firstOrNull { race ->
                    val start = race.toStartDateTimeLocal()
                    start != null && start.isAfter(now)
                }

                _nextRace.value = upcoming
                startCountdown(upcoming)
            } catch (e: Exception) {
                _error.value = e.message ?: "Unknown error"
            }
        }
    }

    private fun startCountdown(race: Race?) {
        countdownJob?.cancel()
        if (race == null) {
            _countdownText.value = ""
            return
        }

        val target = race.toStartDateTimeLocal() ?: run {
            _countdownText.value = ""
            return
        }

        countdownJob = viewModelScope.launch {
            while (true) {
                val c = countdownTo(target)
                _countdownText.value = formatCountdown(c)
                if (c.days == 0L && c.hours == 0L && c.minutes == 0L && c.seconds == 0L) break
                delay(1000)
            }
        }
    }
}
