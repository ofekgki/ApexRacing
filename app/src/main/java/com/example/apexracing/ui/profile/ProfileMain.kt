package com.example.apexracing.ui.profile

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import com.example.apexracing.activities.WelcomeActivity
import com.example.apexracing.databinding.FragmentProfileMainBinding
import com.example.apexracing.models.UserViewModel
import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.launch

class ProfileMain : Fragment() {

    private lateinit var binding: FragmentProfileMainBinding
    private val userVM: UserViewModel by activityViewModels()


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        binding = FragmentProfileMainBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewLifecycleOwner.lifecycleScope.launch {
            userVM.state.collect { s ->
                if (s !is UserViewModel.UserUiState.Ready) return@collect
                val user = s.user

                binding.profileLBLName.text = "${user.firstName} ${user.lastName}"
                binding.profileLBLUsername.text = "@${user.username}"

                binding.profileCHPFavDriver.text = user.favoriteDriver?.getFullName()
                binding.profileCHPFavTeam.text = user.favoriteTeam?.name

                binding.profileCHPFantasyScore.text = "Fantasy Score: ${user.fantasyPoints}"


            }

        }

        binding.profileCHPLogout.setOnClickListener {
            logout()
        }
    }


    private fun logout() {
        binding.profileCHPLogout.setOnClickListener {

            FirebaseAuth.getInstance().signOut()

            val intent = Intent(requireContext(), WelcomeActivity::class.java)
            intent.flags =
                Intent.FLAG_ACTIVITY_NEW_TASK or
                        Intent.FLAG_ACTIVITY_CLEAR_TASK

            startActivity(intent)
        }
    }


}