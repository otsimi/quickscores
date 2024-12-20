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
import com.live.quickscores.R
import com.live.quickscores.adapters.MatchDetailsViewPagerAdapter
import com.live.quickscores.databinding.FragmentFixtureBinding
import com.squareup.picasso.Picasso

class FixtureFragment : Fragment() {
    private lateinit var viewPager: ViewPager2
    private lateinit var tabLayout: TabLayout
    private var fixtureBinding: FragmentFixtureBinding? = null
    private val binding get() = fixtureBinding!!
    private var homeTeamGoals: String? = null
    private var awayTeamGoals: String? = null



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

        val adapter = MatchDetailsViewPagerAdapter(requireActivity())

        val bundle = Bundle().apply {
            arguments?.let {
                putString("matchId", it.getString("matchId"))
                putString("homeTeam", it.getString("homeTeam"))
                putString("awayTeam", it.getString("awayTeam"))
                putString("homeTeamLogoUrl", it.getString("homeTeamLogoUrl"))
                putString("awayTeamLogoUrl", it.getString("awayTeamLogoUrl"))
                putString("leagueName", it.getString("leagueName"))
                putString("leagueId", it.getString("leagueId"))
                putString("venue", it.getString("venue"))
                putString("date", it.getString("date"))
                putString("referee",it.getString("referee"))
                putString("country",it.getString("country"))
                homeTeamGoals = arguments?.getString("homeTeamGoals")
                awayTeamGoals = arguments?.getString("awayTeamGoals")
                putString("homeTeamId",it.getString("homeTeamId"))
                putString("awayTeamId",it.getString("awayTeamId"))
                putString("season",it.getString("season"))
                putString("matchPeriod",it.getString("matchPeriod"))
                putString("fixtureStatus",it.getString("fixtureStatus"))


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
//            binding.venue.text = it.getString("venue")
//            binding.matchDate.text = it.getString("date")
            Picasso.get().load(it.getString("homeTeamLogoUrl")).into(binding.homeTeamLogo)
            Picasso.get().load(it.getString("awayTeamLogoUrl")).into(binding.awayTeamLogo)
            binding.homeTeamGoals.text = it.getString("homeTeamGoals")
            binding.awayTeamGoals.text = it.getString("awayTeamGoals")
            val fixtureStatus=it.getString("fixtureStatus")
            val matchPeriod=it.getString("matchPeriod")
            val venue=it.getString("venue")
            val date=it.getString("date")

            when(fixtureStatus){
                "NS" -> binding.matchDate.text = date
                "1H" -> binding.matchDate.text = getString(R.string.first_half)
                "2H" -> binding.matchDate.text = getString(R.string.second_half)
                "HT" -> binding.matchDate.text = getString(R.string.half_time)
                "FT" -> binding.matchDate.text = getString(R.string.full_time)
                "ET"->binding.matchDate.text=getString(R.string.extra_time)
                "AET"->binding.matchDate.text=getString(R.string.added_extra_time)
                "PEN" -> binding.matchDate.text = getString(R.string.penalties)
                "CANC"->binding.matchDate.text=getString(R.string.cancelled)
                "PST"->binding.matchDate.text=getString(R.string.postponed)
                "ABD"->binding.matchDate.text=getString(R.string.abandoned)
                "INT"->binding.matchDate.text=getString(R.string.interrupted)
                else -> binding.matchDate.text = date

            }
            if (fixtureStatus=="TBD"){
                binding.matchDate.text=date
                binding.venue.text=venue
            } else if (fixtureStatus=="NS") {
                binding.venue.text = venue
            } else if (fixtureStatus == "1H"||fixtureStatus=="2H"||fixtureStatus=="ET") {
                binding.venue.text = matchPeriod.toString()
            } else if (fixtureStatus=="HT"||fixtureStatus=="FT"||fixtureStatus=="BT"||fixtureStatus=="AET"||fixtureStatus=="PEN") {
                binding.venue.text = fixtureStatus
            }else if (fixtureStatus=="PST"||fixtureStatus=="ABD"||fixtureStatus=="INT"||fixtureStatus=="CANC"){
                binding.venue.text=venue
            }

            if (homeTeamGoals == "-" && awayTeamGoals == "-") {
                binding.homeTeamGoals.visibility = View.VISIBLE
                binding.awayTeamGoals.visibility = View.VISIBLE
                binding.matchDate.visibility = View.VISIBLE
                binding.venue.visibility = View.VISIBLE
                binding.homeTeamGoals.text = homeTeamGoals
                binding.awayTeamGoals.text = awayTeamGoals
                binding.venue.text = it.getString("venue")
                binding.matchDate.text = it.getString("date")

            } else {
                val homeGoalsInt = homeTeamGoals?.toIntOrNull() ?: 0
                val awayGoalsInt = awayTeamGoals?.toIntOrNull() ?: 0
                if (homeGoalsInt > 0 && awayGoalsInt > 0) {
                    binding.homeTeamGoals.visibility = View.VISIBLE
                    binding.awayTeamGoals.visibility = View.VISIBLE
                    binding.matchDate.visibility = View.VISIBLE
                    binding.venue.visibility = View.VISIBLE
                }
            }
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
