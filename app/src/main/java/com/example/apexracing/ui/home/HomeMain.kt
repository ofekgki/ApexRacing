package com.example.apexracing.ui.home

import android.os.Bundle
import android.os.CountDownTimer
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.core.graphics.toColorInt
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.bumptech.glide.Glide
import com.example.apexracing.R
import com.example.apexracing.databinding.FragmentHomeMainBinding
import com.example.apexracing.models.Circuit
import com.example.apexracing.models.User.User
import com.example.apexracing.models.User.UserViewModel
import com.example.apexracing.utilities.DBData
import com.example.apexracing.utilities.DBData.getNextRace
import com.example.apexracing.utilities.UtilitiesFunctions
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.storage.FirebaseStorage
import kotlinx.coroutines.launch
import java.util.Date
import java.util.Locale.getDefault
import kotlin.getValue


class HomeMain : Fragment() {
    private lateinit var binding: FragmentHomeMainBinding
    private var timer: CountDownTimer? = null


    private val userVM: UserViewModel by activityViewModels()

    private val storageRef = FirebaseStorage.getInstance().reference


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        binding = FragmentHomeMainBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                userVM.state.collect { s ->
                    val ready = s as? UserViewModel.UserUiState.Ready ?: return@collect
                    val user = ready.user

                    loadProfileImage(s.uid)

                    binding.homeTXTName.text = user.firstName.replaceFirstChar {
                        if (it.isLowerCase()) it.titlecase(getDefault()) else it.toString()
                    }

                    val nextRace = getNextRace()
                    nextRace?.let { race ->
                        binding.homeBADGERound.text = "Round %02d".format(race.round)
                        binding.homeTXTRaceName.text = race.displayName
                        binding.homeTXTTrack.text = "📍%s".format(race.circuitName)
                        startCountdown(race.startTime)
                        bindPhoto(race, user)
                    } ?: run {
                        binding.homeBADGERound.text = "Round 00"
                        binding.homeTXTRaceName.text = " Season Finished"
                        binding.homeTXTTrack.text = "📍Space"
                    }

                    binding.homeTXTPointsValue.text = user.fantasyPoints.toString()

                    DBData.getUserFantasyRank(
                        onResult = { rank, total ->
                            binding.homeTXTRankValue.text = "$rank" + UtilitiesFunctions()
                                .getNumFollowing(rank)

                            val topPercent =
                                ((rank.toFloat() - 1f) / total.toFloat()) * 100f

                            val displayPercent =
                                maxOf(1, topPercent.toInt())

                            binding.homeBADGERankDelta.text =
                                "Top $displayPercent%"
                        },

                        onError = {
                            Log.e("Fantasy Rank", "Failed to get rank", it)
                        }
                    )


                    user.favoriteDriver?.let { driver ->
                        driver.constructor?.get()?.addOnSuccessListener { team ->
                            binding.homeTXTTeam.text = team.getString("name")
                        }
                        binding.homeTXTDriverName.text = driver.getFullName()
                        binding.homeTXTSeasonPts.text = "%3d Pts".format(driver.points)
                        binding.homeTXTSeasonRankVal.text = "P%2d".format(driver.position)
                        driver.constructor?.get()
                            ?.addOnSuccessListener { document ->

                                val teamName = document.id
                                val driverColor = UtilitiesFunctions()
                                    .getTeamColor(teamName)
                                binding.homeTXTDriverNumber.setTextColor(driverColor.toColorInt())

                            }
                        binding.homeTXTDriverNumber.text = driver.permanentNumber.toString()

                    }


                }
            }
        }
    }

    private fun loadImage(path: String, imageView: ImageView) {
        val cleanPath = path.trim().removePrefix("/")

        storageRef.child(cleanPath).downloadUrl.addOnSuccessListener { uri ->
            Glide.with(this@HomeMain)
                .load(uri)
                .placeholder(R.drawable.user_profile_blank)
                .error(R.drawable.user_profile_blank)
                .into(imageView)
        }
    }

    private fun bindPhoto(race: Circuit, user: User) {

        loadImage(race.flagRef, binding.homeIMGFlag)
        loadImage(race.skylineRef, binding.homeIMGRace)

        user.favoriteDriver?.imgRef?.let {
            loadImage(it, binding.homeIMGDriver)
        }
        user.favoriteDriver?.constructor?.get()?.addOnSuccessListener { doc ->
            val imgRef = doc.getString("imgRef") ?: return@addOnSuccessListener

            loadImage(imgRef, binding.homeIMGTeam)

        }
    }

    private fun startCountdown(raceTime: Date) {
        val now = System.currentTimeMillis()
        val raceMillis = raceTime.time
        val diff = raceMillis - now

        if (diff <= 0) return

        timer?.cancel()

        timer = object : CountDownTimer(diff, 1000) {
            override fun onTick(millisUntilFinished: Long) {

                val seconds = millisUntilFinished / 1000

                val days = seconds / (24 * 3600)
                val hours = (seconds % (24 * 3600)) / 3600
                val minutes = (seconds % 3600) / 60
                val secs = seconds % 60

                binding.homeTXTDays.text = "%02d".format(days)
                binding.homeTXTHours.text = "%02d".format(hours)
                binding.homeTXTMins.text = "%02d".format(minutes)
                binding.homeTXTSecs.text = "%02d".format(secs)
            }

            override fun onFinish() {
                binding.homeTXTDays.text = "00"
                binding.homeTXTHours.text = "00"
                binding.homeTXTMins.text = "00"
                binding.homeTXTSecs.text = "00"
            }
        }.start()
    }
    private fun loadProfileImage(userId: String) {
        FirebaseDatabase.getInstance()
            .reference
            .child("users")
            .child(userId)
            .child("imgRef")
            .get()
            .addOnSuccessListener { snapshot ->

                val imgRef = snapshot.getValue(String::class.java)

                Log.d("IMG_REF", imgRef.toString())

                if (!imgRef.isNullOrEmpty()) {
                    FirebaseStorage.getInstance()
                        .getReference(imgRef)
                        .downloadUrl
                        .addOnSuccessListener { uri ->
                            Glide.with(binding.root)
                                .load(uri)
                                .into(binding.homeIMGAvatar)
                        }

                }
            }
            .addOnFailureListener { e ->
                binding.homeIMGAvatar.setImageResource(R.drawable.user_profile_blank)
                Log.e("LoadImage", "Failed to load profile image", e)
            }
    }

    override fun onDestroyView() {
        timer?.cancel()
        timer = null
        super.onDestroyView()
    }


}