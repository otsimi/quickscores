package com.live.quickscores.fragments

import android.os.Build
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.RequiresApi
import androidx.lifecycle.ViewModelProvider
import androidx.viewpager2.widget.ViewPager2
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator
import com.live.quickscores.LeagueIdSharedViewModel
import com.live.quickscores.adapters.LeagueFixturesViewPagerAdapter
import com.live.quickscores.databinding.FragmentLeaguesFixturesBinding

class LeaguesFixturesFragment : Fragment() {
    private var leagueId: String? = null
    private var leagueName: String? = null
    private var countryName: String? = null
    private var _binding: FragmentLeaguesFixturesBinding? = null
    private val binding get() = _binding!!
    private lateinit var viewPager: ViewPager2
    private lateinit var tabLayout: TabLayout
    private lateinit var sharedViewModel: LeagueIdSharedViewModel

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        sharedViewModel = ViewModelProvider(requireActivity())[LeagueIdSharedViewModel::class.java]

        arguments?.let {
            leagueName = it.getString("leagueName")
            countryName = it.getString("country") ?: countryName
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentLeaguesFixturesBinding.inflate(inflater, container, false)
        return binding.root
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        sharedViewModel.selectedLeagueId.observe(viewLifecycleOwner) { id ->
            leagueId = id
//            println("$id, gottenStringBundle")
            setupViewPagerAndTabs()
        }

        binding.leagueName.text = leagueName
    }

    private fun setupViewPagerAndTabs() {
        viewPager = binding.viewPager
        tabLayout = binding.tabLayout
        val adapter = LeagueFixturesViewPagerAdapter(requireActivity())

        val bundle = Bundle().apply {
            putString("leagueId", leagueId)
            putString("leagueName", leagueName)
            println("${leagueId},putStringLeagueId")
        }

        adapter.addFragment(LeagueFixturesContentFragment(), bundle)
        adapter.addFragment(ResultsFragment(), bundle)
        adapter.addFragment(PlayerStatsFragment(), bundle)

        viewPager.adapter = adapter
        TabLayoutMediator(tabLayout, viewPager) { tab, position ->
            tab.text = when (position) {
                0 -> "Fixtures"
                1 -> "Results"
                2 -> "Player's stats"
                else -> null
            }
        }.attach()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    companion object {
        @JvmStatic
        fun newInstance(countryCode: String, season: String, initialLeagueId: String?) =
            LeaguesFixturesFragment().apply {
                arguments = Bundle().apply {
                    putString("countryCode", countryCode)
                    putString("season", season)
                    putString("initialLeagueId", initialLeagueId)
                }
                println("${initialLeagueId},initialLeagueId")
            }
    }

}
