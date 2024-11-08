package com.live.quickscores.adapters

import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter

class MatchDetailsViewPagerAdapter(fragmentActivity: FragmentActivity): FragmentStateAdapter(fragmentActivity){
    private val fragmentList = mutableListOf<Fragment>()

    fun addFragment(fragment: Fragment,bundle: Bundle) {
        fragment.arguments = bundle
        fragmentList.add(fragment)
    }


    override fun getItemCount(): Int = fragmentList.size

    override fun createFragment(position: Int): Fragment =fragmentList[position]
}