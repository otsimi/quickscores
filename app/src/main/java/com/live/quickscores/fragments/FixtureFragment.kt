package com.live.quickscores.fragments

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.viewpager2.widget.ViewPager2
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator
import com.live.quickscores.MainActivity
import com.live.quickscores.MatchDetailsAdapter
import com.live.quickscores.databinding.FragmentFixtureBinding
import com.squareup.picasso.Picasso

class FixtureFragment : Fragment() {
    private lateinit var viewPager: ViewPager2
    private lateinit var tabLayout: TabLayout
    private var fixtureBinding: FragmentFixtureBinding? = null
    private val binding get() = fixtureBinding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        fixtureBinding = FragmentFixtureBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.back.setOnClickListener {
            val intent = Intent(context, MainActivity::class.java)
            startActivity(intent)
        }

        viewPager = binding.viewPager
        tabLayout = binding.tabLayout

        val adapter = MatchDetailsAdapter(requireActivity())

        val bundle = Bundle().apply {
            arguments?.let {
                putString("matchId", it.getString("matchId"))
                putString("homeTeam", it.getString("homeTeam"))
                putString("awayTeam", it.getString("awayTeam"))
                putString("homeTeamLogoUrl", it.getString("homeTeamLogoUrl"))
                putString("awayTeamLogoUrl", it.getString("awayTeamLogoUrl"))
                putString("leagueName", it.getString("leagueName"))
                putString("venue", it.getString("venue"))
                putString("date", it.getString("date"))
                putString("referee",it.getString("referee"))
                putString("country",it.getString("country"))

            }
        }
        adapter.addFragment(MatchInfoFragment(), bundle)
        adapter.addFragment(StatsFragment(), bundle)
        adapter.addFragment(UpcomingFixturesFragment(), bundle)
        adapter.addFragment(LineupsFragment(), bundle)
        adapter.addFragment(H2HFragment(), bundle)
        adapter.addFragment(LeagueTableFragment(), bundle)

        viewPager.adapter = adapter

        TabLayoutMediator(tabLayout, viewPager) { tab, position ->
            tab.text = when (position) {
                0 -> "Info"
                1 -> "Stats"
                2 -> "Fixtures"
                3 -> "Lineups"
                4 -> "H2H"
                5 -> "Table"
                else -> null
            }
        }.attach()

        arguments?.let {
            binding.homeTeamName.text = it.getString("homeTeam")
            binding.awayTeamName.text = it.getString("awayTeam")
            binding.leagueName.text = it.getString("leagueName")
            binding.venue.text = it.getString("venue")
            binding.matchDate.text = it.getString("date")
            Picasso.get().load(it.getString("homeTeamLogoUrl")).into(binding.homeTeamLogo)
            Picasso.get().load(it.getString("awayTeamLogoUrl")).into(binding.awayTeamLogo)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        fixtureBinding = null
    }

    companion object {
        fun newInstance(matchId: String) =
            FixtureFragment().apply {
                arguments = Bundle().apply {
                    putString("matchId", matchId)
                }
            }
    }
}
