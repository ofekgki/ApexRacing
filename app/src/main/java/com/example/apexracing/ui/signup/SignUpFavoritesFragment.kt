package com.example.apexracing.ui.signup

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import com.example.apexracing.adapters.FavoriteConstructorAdapter
import com.example.apexracing.adapters.FavoriteDriverAdapter
import com.example.apexracing.databinding.FragmentSignUpFavoritesBinding
import com.example.apexracing.utilities.DBData

class SignUpFavoritesFragment : Fragment() {
    data class FavoriteSelection(val driverId: String, val teamId: String)

    private lateinit var binding: FragmentSignUpFavoritesBinding
    private lateinit var driversAdapter: FavoriteDriverAdapter
    private lateinit var teamsAdapter: FavoriteConstructorAdapter

    private var selectedDriverId: String? = null
    private var selectedTeamId: String? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        binding = FragmentSignUpFavoritesBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupDrivers()
        setupTeams()
        driversAdapter.submitList(DBData.drivers.sortedBy { it.position })
        teamsAdapter.submitList(DBData.teams.sortedBy { it.position })
        if (DBData.drivers.isEmpty() || DBData.teams.isEmpty()) {
            DBData.preloadAndWait().addOnSuccessListener {
                driversAdapter.submitList(DBData.drivers.sortedBy { it.position })
                teamsAdapter.submitList(DBData.teams.sortedBy { it.position })
            }
        }
    }


    private fun setupDrivers() {
        driversAdapter = FavoriteDriverAdapter { d ->
            selectedDriverId = d.id
            binding.signup2LBLHint.text = "Selected driver: ${d.givenName} ${d.familyName}"
        }

        binding.signup2RVDrivers.apply {
            layoutManager = GridLayoutManager(requireContext(), 3)
            adapter = driversAdapter
            setHasFixedSize(true)
        }
    }

    private fun setupTeams() {
        teamsAdapter = FavoriteConstructorAdapter { t ->
            selectedTeamId = t.id
            binding.signup2LBLHint.text = "Selected team: ${t.name}"
        }

        binding.signup2RVTeams.apply {
            layoutManager = GridLayoutManager(requireContext(), 3)
            adapter = teamsAdapter
            setHasFixedSize(true)
        }
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

}