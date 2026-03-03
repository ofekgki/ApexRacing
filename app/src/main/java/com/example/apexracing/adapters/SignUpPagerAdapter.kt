package com.example.apexracing.adapters

import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter

class SignUpPagerAdapter(activity: AppCompatActivity,
    private val step1: Fragment,
    private val step2: Fragment
        ) : FragmentStateAdapter(activity) {

    override fun getItemCount() = 2

    override fun createFragment(position: Int): Fragment {
        return if (position == 0) step1 else step2
    }
}