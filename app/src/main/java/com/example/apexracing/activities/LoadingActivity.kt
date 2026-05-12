package com.example.apexracing.activities

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.animation.DecelerateInterpolator
import androidx.appcompat.app.AppCompatActivity
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import com.example.apexracing.databinding.ActivityLoadingBinding
import com.example.apexracing.utilities.DBData

class LoadingActivity : AppCompatActivity() {

    private lateinit var binding: ActivityLoadingBinding

    override fun onCreate(savedInstanceState: Bundle?) {

        installSplashScreen()
        super.onCreate(savedInstanceState)
        binding = ActivityLoadingBinding.inflate(layoutInflater)
        setContentView(binding.root)

        startLogoAnimation()

        DBData.preloadAndWait()
            .addOnSuccessListener { goNext() }
            .addOnFailureListener { e ->
                Log.d("LoadingActivity", "Error getting documents: ${e.message}")
                finish()
            }
    }

    private fun startLogoAnimation() {
        binding.loadingIMGLogo.apply {
            alpha = 0f
            scaleX = 0.85f
            scaleY = 0.85f
            translationY = 80f

            animate()
                .alpha(1f)
                .scaleX(1f)
                .scaleY(1f)
                .translationY(0f)
                .setDuration(900)
                .setInterpolator(DecelerateInterpolator())
                .start()
        }
    }

    private fun goNext() {
        startActivity(Intent(this, WelcomeActivity::class.java))
        finish()
    }
}