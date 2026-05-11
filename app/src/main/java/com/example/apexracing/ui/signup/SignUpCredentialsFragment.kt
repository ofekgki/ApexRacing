package com.example.apexracing.ui.signup

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.apexracing.databinding.FragmentSignUpCredentialsBinding
import com.example.apexracing.utilities.ValidationUtils

class SignUpCredentialsFragment : Fragment() {

    data class SignUpCredentials(
        val username: String,
        val email: String,
        val password: String,
        val firstName: String,
        val lastName: String
    )

    private lateinit var binding: FragmentSignUpCredentialsBinding


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentSignUpCredentialsBinding.inflate(inflater, container, false)
        return binding.root
    }

    fun getDataOrNull(): SignUpCredentials? {
        clearErrors()

        val username = binding.signup1EDTUsername.text?.toString()?.trim().orEmpty()
        val email = binding.signup1EDTEmail.text?.toString()?.trim().orEmpty()
        val password = binding.signup1EDTPassword.text?.toString().orEmpty()
        val first = binding.signup1EDTFirst.text?.toString()?.trim().orEmpty()
        val last = binding.signup1EDTLast.text?.toString()?.trim().orEmpty()

        var ok = true

        if (username.length < 3) {
            binding.signup1TILUsername.error = "Min 3 chars"; ok = false
        }

        val emailError = ValidationUtils.validateEmail(email)
        if (emailError != null) {
            binding.signup1TILEmail.error = emailError
            ok = false
        }

        val passwordError = ValidationUtils.validatePassword(password)
        if (passwordError != true) {
            binding.signup1TILPassword.error =
                """
                Password must contain:
                • 8+ characters
                • Uppercase letter
                • Lowercase letter
                • Number
                • Special character
            """.trimIndent()
            ok = false
        }

        if (first.isBlank()) {
            binding.signup1TILFirst.error = "Required"; ok = false
        }
        if (last.isBlank()) {
            binding.signup1TILLast.error = "Required"; ok = false
        }

        if (!ok) return null

        return SignUpCredentials(username, email, password, first, last)
    }

    private fun clearErrors() {
        binding.signup1TILUsername.error = null
        binding.signup1TILEmail.error = null
        binding.signup1TILPassword.error = null
        binding.signup1TILFirst.error = null
        binding.signup1TILLast.error = null
    }


}
