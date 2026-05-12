package com.example.apexracing.activities

import android.content.Intent
import android.os.Bundle
import android.util.Patterns
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.apexracing.databinding.ActivityLoginBinding
import com.example.apexracing.utilities.ValidationUtils
import com.google.firebase.auth.FirebaseAuth

class LoginActivity : AppCompatActivity() {

    private lateinit var binding: ActivityLoginBinding
    private val auth = FirebaseAuth.getInstance()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Login
        binding.loginBTNLogin.setOnClickListener { doLogin() }

        // Back -> Welcome
        binding.loginLBLBack.setOnClickListener {
            startActivity(Intent(this, WelcomeActivity::class.java))
            finish()
        }

        // Forgot password
        binding.loginLBLForgot.setOnClickListener { forgotPassword() }
    }

    override fun onStart() {
        super.onStart()
        // If already logged in -> go main
        if (auth.currentUser != null) {
            Toast.makeText(this, "Welcome back", Toast.LENGTH_SHORT).show()
            goToMain()
        }
    }

    private fun doLogin() {
        val email = binding.loginEDTEmail.text?.toString()?.trim().orEmpty()
        val pass = binding.loginEDTPassword.text?.toString().orEmpty()

        val isEmailValid = Patterns.EMAIL_ADDRESS.matcher(email).matches()
        val isPasswordValid = ValidationUtils.validatePassword(pass)

        if (!isEmailValid || !isPasswordValid) {
            toast("Email or Password are invalid")
            return
        }

        setUiEnabled(false)

        auth.signInWithEmailAndPassword(email, pass)
            .addOnSuccessListener {
                goToMain()
            }
            .addOnFailureListener { e ->
                setUiEnabled(true)
                toast("Login failed: ${e.message}")
            }
    }

    private fun forgotPassword() {
        val email = binding.loginEDTEmail.text?.toString()?.trim().orEmpty()

        if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            toast("Enter your email first")
            return
        }

        setUiEnabled(false)

        auth.sendPasswordResetEmail(email)
            .addOnSuccessListener {
                setUiEnabled(true)
                toast("Reset link sent to your email")
            }
            .addOnFailureListener { e ->
                setUiEnabled(true)
                toast("Reset failed: ${e.message}")
            }
    }

    private fun goToMain() {
        startActivity(Intent(this, MainActivity::class.java))
        finish()
    }

    private fun setUiEnabled(enabled: Boolean) {
        binding.loginBTNLogin.isEnabled = enabled
        binding.loginEDTEmail.isEnabled = enabled
        binding.loginEDTPassword.isEnabled = enabled
        binding.loginLBLBack.isEnabled = enabled
        binding.loginLBLForgot.isEnabled = enabled
    }

    private fun toast(msg: String) {
        Toast.makeText(this, msg, Toast.LENGTH_SHORT).show()
    }
}