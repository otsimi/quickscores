package com.live.quickscores.fragments

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.live.quickscores.LeagueTableRepository
import com.live.quickscores.LeagueTableViewModel
import com.live.quickscores.LeagueTableViewModelFactory
import com.live.quickscores.adapters.LeagueTableAdapter
import com.live.quickscores.databinding.FragmentLeagueTableBinding
import com.live.quickscores.standingsresponse.StandingsResponse

class LeagueTableFragment : Fragment() {

    private var _binding: FragmentLeagueTableBinding? = null
    private val binding get() = _binding!!

    private val viewModel: LeagueTableViewModel by viewModels {
        LeagueTableViewModelFactory(LeagueTableRepository())
    }

    private var leagueName: String? = null
    private lateinit var adapter: LeagueTableAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        leagueName = arguments?.getString("leagueName")
        val season=arguments?.getString("season")
        val leagueId = arguments?.getString("leagueId")
//        leagueId="13"
//        season="2024"
        if (leagueId!=null && season!=null){
            println("${leagueId},${season},LeagueId,season")
            viewModel.getTable(leagueId,season)
        }else{
            Log.e("LeagueId & season Error", "LeagueId && season is null")
        }

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentLeagueTableBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.leagueTitle.text = leagueName

        adapter = LeagueTableAdapter(emptyList())
        setUpLeagueTableRecyclerView()
        observeLeagueTable()
    }

    private fun setUpLeagueTableRecyclerView() {
        binding.recyclerView.layoutManager = LinearLayoutManager(context)
        binding.recyclerView.adapter = adapter
    }

    private fun observeLeagueTable() {

        viewModel.table.observe(viewLifecycleOwner) { response ->
            if (response == null) {
                Log.e("API Error,Malwnwgww", "API response is null")
            } else {
                println("API response,Malehe: ${response.body()}")
            }
            println("observeLeagueTable called, response: $response")
            response?.body()?.let { standingsResponse ->
                updateAdapter(standingsResponse)
                println("${standingsResponse},MaLenGekubwa")

            } ?: Log.e("API Error", "Response is null")
        }
    }

    private fun updateAdapter(data: StandingsResponse) {
        println("${data},DataResponse, malenge")
        val teamStandingsList = data.response.firstOrNull()?.league?.standings ?: emptyList()
        adapter = LeagueTableAdapter(teamStandingsList)
        binding.recyclerView.adapter = adapter
        adapter.notifyDataSetChanged()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    companion object {
        fun newInstance(leagueId: String, leagueName: String, season:String) =
            LeagueTableFragment().apply {
                arguments = Bundle().apply {
                    putString("leagueId", leagueId)
                    putString("leagueName", leagueName)
                    putString("season",season)
                }
            }
    }
}
