package com.live.quickscores

import MatchFragment
import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter

class ViewPagerAdapter(activity: FragmentActivity,private val dates: List<String>): FragmentStateAdapter(activity) {


    override fun getItemCount(): Int=dates.size



    override fun createFragment(position: Int): Fragment {
        val fragment = MatchFragment()
        val bundle = Bundle()
        bundle.putString("selected_date", dates[position])
        fragment.arguments = bundle
        return fragment

    }

}