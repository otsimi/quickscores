package com.live.quickscores.fragments

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.live.quickscores.R
import com.live.quickscores.UpcomingFixturesRepository
import com.live.quickscores.UpcomingFixturesViewModel
import com.live.quickscores.UpcomingFixturesViewModelFactory
import com.live.quickscores.adapters.UpcomingFixturesAdapter
import com.live.quickscores.databinding.FragmentUpcomingFixturesBinding


class UpcomingFixturesFragment : Fragment() {
    private var binding: FragmentUpcomingFixturesBinding? = null
    private val viewModel: UpcomingFixturesViewModel by viewModels {
        UpcomingFixturesViewModelFactory(UpcomingFixturesRepository())
    }

    private lateinit var homeAdapter: UpcomingFixturesAdapter
    private lateinit var awayAdapter: UpcomingFixturesAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val fragmentBinding = FragmentUpcomingFixturesBinding.inflate(inflater, container, false)
        binding = fragmentBinding
        return fragmentBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val homeTeamName = arguments?.getString("homeTeam") ?: "Home Team"
        val awayTeamName = arguments?.getString("awayTeam") ?: "Away Team"
        val homeTeamId = arguments?.getString("homeTeamId") ?: ""
        val awayTeamId = arguments?.getString("awayTeamId") ?: ""

        binding?.homeTeamNextFixturesTitle?.text = homeTeamName
        binding?.awayTeamNextFixturesTitle?.text = awayTeamName


        homeAdapter = UpcomingFixturesAdapter(emptyList())
        awayAdapter = UpcomingFixturesAdapter(emptyList())

        setupHomeTeamRecyclerView()
        setupAwayTeamRecyclerView()

        if (homeTeamId.isNotEmpty()) {
            println("${homeTeamId},Mlima")
            viewModel.getUpcomingHomeFixtures(homeTeamId)
            observeHomeTeamFixtures()
        } else {
            Log.e("UpcomingFixturesFragment", "Invalid home team ID")
        }

        if (awayTeamId.isNotEmpty()) {
            println("${awayTeamId},Mrima")
            viewModel.getUpcomingAwayFixtures(awayTeamId)
            observeAwayTeamFixtures()
        } else {
            Log.e("UpcomingFixturesFragment", "Invalid away team ID")
        }
    }

    private fun setupHomeTeamRecyclerView() {
        binding?.homeFixturesRecyclerView?.apply {
            layoutManager = LinearLayoutManager(context)
            adapter = homeAdapter
        }
    }

    private fun setupAwayTeamRecyclerView() {
        binding?.awayFixturesRecyclerView?.apply {
            layoutManager = LinearLayoutManager(context)
            adapter = awayAdapter
        }
    }

    private fun observeHomeTeamFixtures() {
        viewModel.upcomingHomeFixtures.observe(viewLifecycleOwner) { response ->
            println("${response},Malengeeee")
            response?.body()?.let { fixtures ->
                Log.d("UpcomingFixturesFragment", "Home fixtures fetched: ${fixtures.response}")
                homeAdapter.updateFixtures(fixtures.response)
            } ?: Log.e("UpcomingFixturesFragment", "No data available for home fixtures")
        }
    }

    private fun observeAwayTeamFixtures() {
        viewModel.upcomingAwayFixtures.observe(viewLifecycleOwner) { response ->
            response?.body()?.let { fixtures ->
                Log.d("UpcomingFixturesFragment", "Away fixtures fetched: ${fixtures.response}")
                awayAdapter.updateFixtures(fixtures.response)
            } ?: Log.e("UpcomingFixturesFragment", "No data available for away fixtures")
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        binding = null
    }
}
