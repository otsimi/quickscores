package com.live.quickscores.fragments

import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModel
import androidx.recyclerview.widget.RecyclerView
import com.live.quickscores.LeagueFixturesRepo
import com.live.quickscores.LeagueFixturesViewModel
import com.live.quickscores.LeagueFixturesViewModelFactoryProvider
import com.live.quickscores.R
import com.live.quickscores.adapters.RecyclerViewAdapter
import com.live.quickscores.databinding.FragmentLeaguesFixturesBinding
import com.live.quickscores.dataclasses.FixtureResponses
import com.live.quickscores.fragments.MatchFragment.OnFixtureClickListener

class LeaguesFixturesFragment : Fragment(),RecyclerViewAdapter.OnFixtureClickListener {
    private lateinit var recyclerViewAdapter: RecyclerViewAdapter
    private lateinit var recyclerView: RecyclerView
    private lateinit var date: String
    private lateinit var leagueId:String
    private var fixtureClickListener: OnFixtureClickListener? = null
    private var _binding:FragmentLeaguesFixturesBinding?=null
    private val binding get() = _binding!!
    private val viewModel:LeagueFixturesViewModel by viewModels {
        LeagueFixturesViewModelFactoryProvider(LeagueFixturesRepo())
    }
    interface OnFixtureClickListener{
        fun onFixtureClicked(
            matchId: String,
            homeTeam: String,
            awayTeam: String,
            homeTeamLogoUrl: String,
            awayTeamLogoUrl: String,
            leagueName:String,
            venue:String?,
            date:String?,
            country:String?,
            referee:String?,
            city:String?,
            homeTeamGoals:String?,
            awayTeamGoals:String?,
            homeTeamId:String,
            awayTeamId:String,
            leagueId:String,
            season:String
        )
    }
    override fun onAttach(context: Context) {
        super.onAttach(context)
        if (context is com.live.quickscores.fragments.LeaguesFixturesFragment.OnFixtureClickListener) {
            fixtureClickListener = context
        } else {
            throw RuntimeException("$context must implement OnFixtureClickListener")
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {

        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding=FragmentLeaguesFixturesBinding.inflate(inflater,container,false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
//        binding.leagueName.text=
    }

    companion object {
        fun newInstance() =
            LeaguesFixturesFragment().apply {
                arguments = Bundle().apply {

                }
            }
    }

    override fun onFixtureClick(match: FixtureResponses) {
        TODO("Not yet implemented")
    }
}