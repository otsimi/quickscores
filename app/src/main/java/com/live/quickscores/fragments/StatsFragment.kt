package com.live.quickscores.fragments

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.live.quickscores.repositories.StatisticsRepository
import com.live.quickscores.viewmodelclasses.StatisticsViewModel
import com.live.quickscores.viewmodelclasses.StatisticsViewModelFactory
import com.live.quickscores.databinding.FragmentStatsBinding

class StatsFragment : Fragment() {

    private var matchId: String? = null
    private var statsBinding:FragmentStatsBinding?=null
    private val binding get() = statsBinding!!
    private val viewModel: StatisticsViewModel by viewModels {
        StatisticsViewModelFactory(StatisticsRepository())
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        arguments?.let {
            matchId = it.getString("matchId")
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        statsBinding = FragmentStatsBinding.inflate(inflater,container,false)

        matchId?.let { fixtureId ->
            fetchMatchStatistics(fixtureId)
        }

        return binding.root
    }

    private fun fetchMatchStatistics(fixtureId: String) {
        viewModel.fetchStats(fixtureId)
        println("${fixtureId},Malenge")

        viewModel.stats.observe(viewLifecycleOwner, Observer { response ->
            response?.let {
                if (it.isSuccessful) {
                    it.body()?.let { statsResponse ->
                        Log.d("StatsFragment", "Statistics fetched successfully,Malenge: ${statsResponse}")
//                        binding.homeTeam.text=
                    } ?: run {
                        Log.e("StatsFragment", "Empty statistics response body")
                    }
                } else {
                    Log.e("StatsFragment", "Error: ${it.message()}")
                }
            } ?: run {
                Log.e("StatsFragment", "Statistics response is null")
            }
        })
    }

    companion object {
        fun newInstance(matchId: String) =
            StatsFragment().apply {
                arguments = Bundle().apply {
                    putString("matchId", matchId)
                }
            }
    }
}
