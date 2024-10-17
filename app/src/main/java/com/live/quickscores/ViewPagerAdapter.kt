package com.live.quickscores

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter

class ViewPagerAdapter(activity: FragmentActivity, private val dates: List<String>): FragmentStateAdapter(activity) {

    override fun getItemCount(): Int {
        return dates.size
    }

    override fun createFragment(position: Int): Fragment {
        return MatchFragment.newInstance(dates[position])
    }


}