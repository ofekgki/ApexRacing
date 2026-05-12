package com.example.apexracing.ui.profile

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import com.example.apexracing.adapters.FavoriteConstructorAdapter
import com.example.apexracing.adapters.FavoriteDriverAdapter
import com.example.apexracing.databinding.FragmentNewFavoritesBinding
import com.example.apexracing.models.User.UserViewModel
import com.example.apexracing.utilities.DBData
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase

class NewFavorites : Fragment() {

    data class FavoriteSelection(val driverId: String, val teamId: String)

    private val userVM: UserViewModel by activityViewModels()

    private lateinit var binding: FragmentNewFavoritesBinding
    private lateinit var driversAdapter: FavoriteDriverAdapter
    private lateinit var teamsAdapter: FavoriteConstructorAdapter

    private var selectedDriverId: String? = null
    private var selectedTeamId: String? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        binding = FragmentNewFavoritesBinding.inflate(inflater, container, false)
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

        binding.newFavBTNSave.setOnClickListener {

            val selection = getSelectionOrNull() ?: return@setOnClickListener

            val uid = FirebaseAuth.getInstance().currentUser?.uid ?: return@setOnClickListener

            val updates = mapOf(
                "favoriteDriver" to selection.driverId,
                "favoriteTeam" to selection.teamId
            )

            FirebaseDatabase.getInstance()
                .reference
                .child("users")
                .child(uid)
                .updateChildren(updates)
                .addOnSuccessListener {

                    userVM.reloadUser(uid)

                    Toast.makeText(
                        requireContext(),
                        "Favorites updated",
                        Toast.LENGTH_SHORT
                    ).show()

                    findNavController().popBackStack()
                }
                .addOnFailureListener { e ->

                    Toast.makeText(
                        requireContext(),
                        "Failed to update favorites",
                        Toast.LENGTH_SHORT
                    ).show()

                    Log.e("Favorites", "Update failed", e)
                }
        }

        binding.newFavBTNBack.setOnClickListener {
            findNavController().popBackStack()
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