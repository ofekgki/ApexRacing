package com.example.apexracing.ui.profile

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import com.bumptech.glide.Glide
import com.example.apexracing.R
import com.example.apexracing.activities.WelcomeActivity
import com.example.apexracing.databinding.FragmentProfileMainBinding
import com.example.apexracing.models.UserViewModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.storage.FirebaseStorage
import kotlinx.coroutines.launch

class ProfileMain : Fragment() {

    private lateinit var binding: FragmentProfileMainBinding
    private val userVM: UserViewModel by activityViewModels()
    private var currentUserId: String? = null

    private lateinit var pickImageLauncher : ActivityResultLauncher<String>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setGallery()
    }


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

                currentUserId = s.uid

                binding.profileLBLName.text = "${user.firstName} ${user.lastName}"
                binding.profileLBLUsername.text = "@${user.username}"

                binding.profileCHPFavDriver.text = user.favoriteDriver?.getFullName()
                binding.profileCHPFavTeam.text = user.favoriteTeam?.name

                binding.profileCHPFantasyScore.text = "Fantasy Score: ${user.fantasyPoints}"

                loadProfileImage(s.uid)

                binding.profileBTNPicture.setOnClickListener {
                    pickImageLauncher.launch("image/*")


                }
            }

        }

        binding.profileCHPLogout.setOnClickListener {
            logout()
        }


    }

    private fun setGallery() {
        pickImageLauncher =
            registerForActivityResult(ActivityResultContracts.GetContent()) { uri ->


                val userId = currentUserId ?: return@registerForActivityResult

                if (uri != null) {

                    Glide.with(binding.root)
                        .load(uri)
                        .into(binding.profileIMGProfilePicture)

                    uploadProfileImage(userId, uri)                }
            }
    }

    private fun uploadProfileImage(UserID: String, uri: Uri) {

        val storageRef = FirebaseStorage.getInstance()
            .reference
            .child("ProfileImages/$UserID/profile_image.jpg")

        storageRef.putFile(uri)
            .addOnSuccessListener {
                val imgRef = storageRef.path

                FirebaseDatabase.getInstance()
                    .reference
                    .child("users")
                    .child(UserID)
                    .child("imgRef")
                    .setValue(imgRef)

                Toast.makeText(requireContext(), "Profile image updated", Toast.LENGTH_SHORT).show()

            }
            .addOnFailureListener { e ->
                Log.e("UploadImage", "Upload failed", e)
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

    private fun loadProfileImage(userId: String) {
        FirebaseDatabase.getInstance()
            .reference
            .child("users")
            .child(userId)
            .child("imgRef")
            .get()
            .addOnSuccessListener { snapshot ->

                val imgRef = snapshot.getValue(String::class.java)

                if (!imgRef.isNullOrEmpty()) {
                    FirebaseStorage.getInstance()
                        .reference
                        .child(imgRef)
                        .downloadUrl
                        .addOnSuccessListener { uri ->
                            Glide.with(binding.root)
                                .load(uri)
                                .into(binding.profileIMGProfilePicture)
                        }
                }
            }
            .addOnFailureListener { e ->
                binding.profileIMGProfilePicture.setImageResource(R.drawable.user_profile_blank)
                Log.e("LoadImage", "Failed to load profile image", e)
            }
    }

}