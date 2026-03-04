package com.example.apexracing.ui.calendar

import android.os.Bundle
import android.os.Handler
import android.os.Looper.getMainLooper
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.bumptech.glide.Glide
import com.example.apexracing.adapters.CircuitAdapter
import com.example.apexracing.databinding.FragmentCalendarMainBinding
import com.example.apexracing.models.Circuit
import com.example.apexracing.utilities.Constants
import com.google.firebase.Firebase
import com.google.firebase.firestore.firestore
import com.google.firebase.storage.FirebaseStorage
import java.util.Calendar
import java.util.Date


class CalendarMain : Fragment() {

    private var countdownRunnable: Runnable? = null


    private val handler = Handler(getMainLooper())
    private lateinit var binding: FragmentCalendarMainBinding
    private val db = Firebase.firestore
    private val circuitAdapter = CircuitAdapter(emptyList())


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        binding = FragmentCalendarMainBinding.inflate(inflater, container, false)
        binding.calendarRV.adapter = circuitAdapter
        loadDataFromFirestore()
        return binding.root
    }


    private fun loadDataFromFirestore() {

        val circuitCollectionRef =
            db.collection(Constants.FIRESTORE.SEASONS)
                .document("2026")
                .collection(Constants.FIRESTORE.CIRCUITS)
                .orderBy("round")

        circuitCollectionRef.get()
            .addOnSuccessListener { result ->

                val list = mutableListOf<Circuit>()

                for (document in result) {
                    val circuit = document.toObject(Circuit::class.java)
                    circuit.id = document.id
                    list.add(circuit)
                }

                circuitAdapter.races = list
                circuitAdapter.notifyDataSetChanged()

                val next = findNextRace(list)
                if (next != null) {
                    bindHero(next)
                    startCountdown(next.startTime)
                }
            }
            .addOnFailureListener { exception ->
                Log.w("Error", "Error getting Document.", exception)
            }
    }

    private fun findNextRace(list: List<Circuit>): Circuit? {
        val now = Date()
        return list.firstOrNull { it.startTime.after(now) } ?: list.lastOrNull()
    }

    private fun bindHero(c: Circuit) {
        binding.calendarLBLRound.text = "Round %02d".format(c.round)
        binding.calendarLBLTitle.text = c.displayName
        binding.calendarLBLLocation.text = c.city

        binding.calendarLBLDates.text = formatWeekendRange(c.startTime)
        val path = c.layoutRef
        if (!path.isNullOrBlank()) {
            val ref = FirebaseStorage.getInstance().reference.child(path)
            ref.downloadUrl
                .addOnSuccessListener { uri ->
                    Glide.with(binding.root)
                        .load(uri)
                        .into(binding.calendarTrack3D)
                }

        }
    }

    private fun formatWeekendRange(raceDay: Date): String {
        val calStart =
            Calendar.getInstance().apply { time = raceDay; add(Calendar.DAY_OF_MONTH, -2) }
        val calEnd = Calendar.getInstance().apply { time = raceDay }

        val fmtDay = java.text.SimpleDateFormat("dd", java.util.Locale.US)
        val fmtMon = java.text.SimpleDateFormat("MMM", java.util.Locale.US)

        val startDay = fmtDay.format(calStart.time)
        val endDay = fmtDay.format(calEnd.time)
        val month = fmtMon.format(calEnd.time)

        return "$startDay - $endDay $month"
    }

    private fun startCountdown(target: Date) {
        countdownRunnable?.let { handler.removeCallbacks(it) }

        countdownRunnable = object : Runnable {
            override fun run() {
                val diff = target.time - System.currentTimeMillis()

                if (diff <= 0) {
                    binding.calendarTXTDays.text = "00"
                    binding.calendarTXTHours.text = "00"
                    binding.calendarTXTMins.text = "00"
                    return
                }

                val totalMinutes = diff / (60_000)
                val days = totalMinutes / (60 * 24)
                val hours = (totalMinutes % (60 * 24)) / 60
                val mins = totalMinutes % 60

                binding.calendarTXTDays.text = "%02d".format(days)
                binding.calendarTXTHours.text = "%02d".format(hours)
                binding.calendarTXTMins.text = "%02d".format(mins)

                handler.postDelayed(this, 60_000)
            }
        }

        handler.post(countdownRunnable!!)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        countdownRunnable?.let { handler.removeCallbacks(it) }
        countdownRunnable = null
    }


}



