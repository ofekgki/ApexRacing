package com.example.apexracing.activities

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.viewpager2.widget.ViewPager2
import com.example.apexracing.adapters.SignUpPagerAdapter
import com.example.apexracing.databinding.ActivitySignupBinding
import com.example.apexracing.ui.signup.SignUpCredentialsFragment
import com.example.apexracing.ui.signup.SignUpFavoritesFragment
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase

class SignUpActivity : AppCompatActivity() {

    private lateinit var binding: ActivitySignupBinding

    private lateinit var pagerAdapter: SignUpPagerAdapter
    private lateinit var credentialsFragment: SignUpCredentialsFragment
    private lateinit var favoritesFragment: SignUpFavoritesFragment

    private val auth = FirebaseAuth.getInstance()

    private val rtdb = FirebaseDatabase.getInstance().reference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySignupBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupPager()
        setupButtons()
        updateStepUI(0)
    }

    private fun setupPager() {
        credentialsFragment = SignUpCredentialsFragment()
        favoritesFragment = SignUpFavoritesFragment()

        pagerAdapter = SignUpPagerAdapter(this, credentialsFragment, favoritesFragment)
        binding.signupVP.adapter = pagerAdapter

        // Switch enabler
        binding.signupVP.isUserInputEnabled = false

        binding.signupVP.registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback() {
            override fun onPageSelected(position: Int) {
                updateStepUI(position)
            }
        })
    }

    private fun setupButtons() {
        binding.signupBTNBack.setOnClickListener {
            val p = binding.signupVP.currentItem
            if (p > 0) binding.signupVP.currentItem = p - 1 else {
                val intent = Intent(this, WelcomeActivity::class.java)
                startActivity(intent)
                finish()
            }
        }

        binding.signupBTNNext.setOnClickListener {
            when (binding.signupVP.currentItem) {
                0 -> goToStep2IfValid()
                1 -> submitSignup()
            }
        }
    }

    private fun updateStepUI(position: Int) {
        binding.signupLBLSubtitle.text = if (position == 0) "Step 1 of 2" else "Step 2 of 2"
        binding.signupBTNBack.text = if (position == 0) "Cancel" else "Back"
        binding.signupBTNNext.text = if (position == 0) "Next" else "Create Account"
    }

    private fun goToStep2IfValid() {
        val data = credentialsFragment.getDataOrNull() ?: return
        binding.signupVP.currentItem = 1
    }

    private fun submitSignup() {
        val cred = credentialsFragment.getDataOrNull() ?: return
        val fav = favoritesFragment.getSelectionOrNull() ?: return

        binding.signupBTNNext.isEnabled = false
        binding.signupBTNBack.isEnabled = false

        auth.createUserWithEmailAndPassword(cred.email, cred.password)
            .addOnSuccessListener { res ->
                val uid = res.user?.uid ?: return@addOnSuccessListener fail("No uid")

                val userMap = hashMapOf<String, Any>(
                    "username" to cred.username,
                    "firstName" to cred.firstName,
                    "lastName" to cred.lastName,
                    "email" to cred.email,
                    "favoriteDriver" to fav.driverId,
                    "favoriteTeam" to fav.teamId,
                    "fantasyPoints" to 0,
                    "imgRef" to "",
                    "createdAt" to System.currentTimeMillis()
                )

                rtdb.child("users").child(uid)
                    .setValue(userMap)
                    .addOnSuccessListener {
                        Toast.makeText(this, "Account created!", Toast.LENGTH_SHORT).show()
                        startActivity(Intent(this, WelcomeActivity::class.java))
                        finish()
                    }
                    .addOnFailureListener { e ->
                        fail("RTDB error: ${e.message}")
                    }
            }
            .addOnFailureListener { e ->
                fail("Auth error: ${e.message}")
            }
    }

    private fun fail(msg: String) {
        Toast.makeText(this, msg, Toast.LENGTH_LONG).show()
        binding.signupBTNNext.isEnabled = true
        binding.signupBTNBack.isEnabled = true
    }
}