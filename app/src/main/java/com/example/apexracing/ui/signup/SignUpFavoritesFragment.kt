package com.example.apexracing.ui.signup

import androidx.fragment.app.Fragment
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.apexracing.adapters.FavoriteConstructorAdapter
import com.example.apexracing.adapters.FavoriteDriverAdapter
import com.example.apexracing.databinding.FragmentSignUpFavoritesBinding
import com.example.apexracing.models.Driver
import com.google.firebase.firestore.FirebaseFirestore

data class FavoriteSelection(
        val driverId: String,
        val teamId: String
    )
class SignUpFavoritesFragment : Fragment() {

    private var _binding: FragmentSignUpFavoritesBinding? = null
    private val binding get() = _binding!!

    private lateinit var driversAdapter: FavoriteDriverAdapter
    private lateinit var teamsAdapter: FavoriteConstructorAdapter

    private var selectedDriverId: String? = null
    private var selectedTeamId: String? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentSignUpFavoritesBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupDriversRecycler()
        //setupTeamsRecycler()

    }

    fun getSelectionOrNull(): FavoriteSelection? {
        val d = selectedDriverId
        val t = selectedTeamId

        if (d == null) {
            binding.signup2LBLHint.text = "Pick a driver to continue."
            return null
        }
        if (t == null) {
            binding.signup2LBLHint.text = "Pick a team to continue."
            return null
        }
        return FavoriteSelection(d, t)
    }

    private fun setupDriversRecycler() {
        driversAdapter = FavoriteDriverAdapter { driver ->
            selectedDriverId = driver.id
        }

        binding.signup2RVDrivers.apply {
            adapter = driversAdapter
            setHasFixedSize(true)
        }
    }
}