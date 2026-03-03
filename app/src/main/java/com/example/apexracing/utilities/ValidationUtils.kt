package com.example.apexracing.utilities

object ValidationUtils {

    private val EMAIL_REGEX = Regex(
        pattern = "^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$"
    )

    private val PASSWORD_REGEX = Regex(
        pattern = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[@#\$%^&+=!]).{8,64}$"
    )

    fun validateEmail(email: String): String? {

        if (email.isBlank())
            return "Email is required"

        if (email.length > 254)
            return "Email is too long"

        if (email.contains(" "))
            return "Email cannot contain spaces"

        if (!EMAIL_REGEX.matches(email))
            return "Invalid email format"

        if (email.endsWith("."))
            return "Invalid email format"

        return null
    }

    fun validatePassword(password: String): Boolean {

        if (password.isBlank())
            return false

        if (password.contains(" "))
            return false

        if (!PASSWORD_REGEX.matches(password))
            return false

        if (isCommonWeakPassword(password))
            return false

        if (isSequential(password))
            return false

        return true
    }

    private fun isCommonWeakPassword(password: String): Boolean {
        val common = listOf(
            "Password123!",
            "Qwerty123!",
            "12345678!",
            "Admin123!"
        )
        return common.contains(password)
    }

    private fun isSequential(password: String): Boolean {
        return password.contains("1234") ||
                password.contains("abcd") ||
                password.contains("0000")
    }
}