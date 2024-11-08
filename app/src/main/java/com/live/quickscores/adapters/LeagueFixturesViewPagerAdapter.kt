package com.live.quickscores.adapters

import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter

class LeagueFixturesViewPagerAdapter(fragmentActivity: FragmentActivity) :
    FragmentStateAdapter(fragmentActivity) {
    private val fragmentList = mutableListOf<Pair<Fragment, Bundle>>()

    override fun getItemCount(): Int {
        return fragmentList.size
    }

    override fun createFragment(position: Int): Fragment {
        return fragmentList[position].first
    }

    fun addFragment(fragment: Fragment, bundle: Bundle) {
        fragmentList.add(Pair(fragment, bundle))
        println("${bundle},bundleMalenge")
    }
}
