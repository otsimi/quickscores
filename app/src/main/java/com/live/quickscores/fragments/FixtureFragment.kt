package com.live.quickscores.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.viewpager2.widget.ViewPager2
import com.google.android.material.tabs.TabLayout
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
        arguments?.let {
            val matchId = it.getString("matchId")
            val homeTeam = it.getString("homeTeam")
            val awayTeam = it.getString("awayTeam")
            val homeTeamLogoUrl = it.getString("homeTeamLogoUrl")
            val awayTeamLogoUrl = it.getString("awayTeamLogoUrl")
            val leagueName=it.getString("leagueName")
            val venue=it.getString("venue")
            val date=it.getString("date")


            binding.homeTeamName.text = homeTeam
            binding.awayTeamName.text = awayTeam
            binding.leagueName.text=leagueName
            binding.venue.text=venue
            binding.matchDate.text=date
            Picasso.get().load(homeTeamLogoUrl).into(binding.homeTeamLogo)
            Picasso.get().load(awayTeamLogoUrl).into(binding.awayTeamLogo)

        }

    }

    override fun onDestroyView() {
        super.onDestroyView()
        fixtureBinding=null

    }
    private fun fetchMatchDetails(matchId: String){

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