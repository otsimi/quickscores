package com.live.quickscores.fragments

import android.content.Context
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.live.quickscores.repositories.AllLeaguesRepo
import com.live.quickscores.viewmodelclasses.AllLeaguesViewModel
import com.live.quickscores.viewmodelclasses.AllLeaguesViewModelFactoryProvider
import com.live.quickscores.LeagueIdSharedViewModel
import com.live.quickscores.adapters.AllLeaguesAdapter
import com.live.quickscores.allleaguesresponse.AllLeaguesReponse
import com.live.quickscores.databinding.FragmentAllLeaguesBinding



class AllLeaguesFragment : Fragment(), AllLeaguesAdapter.OnLeagueClickListener {
    private var _binding: FragmentAllLeaguesBinding?=null
    private val binding get() = _binding!!
    private var leagueClickListener: OnLeagueClicked?=null
    private lateinit var leaguesAdapter: AllLeaguesAdapter
    private lateinit var sharedViewModel: LeagueIdSharedViewModel
    private val viewModel: AllLeaguesViewModel by viewModels{
        AllLeaguesViewModelFactoryProvider(AllLeaguesRepo())
    }
    interface OnLeagueClicked{
        fun onLeagueClicked(
            leagueId:String,
            season: String,
            name:String,
            leagueLogo:String,
            leagueCountryName:String
        )

    }
    override fun onAttach(context: Context) {
        super.onAttach(context)
        if (context is OnLeagueClicked){
            leagueClickListener=context
        }else{
            throw RuntimeException("$context must implement OnLeagueClicked")
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
        _binding=FragmentAllLeaguesBinding.inflate(inflater,container,false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        sharedViewModel = ViewModelProvider(requireActivity()).get(LeagueIdSharedViewModel::class.java)
        setUpRecyclerView()
        println("Fetching leagues data...")
        observeLeagueData()
        viewModel.fetchAllLeagues()
        println("leaguesFteched")
    }
    private fun setUpRecyclerView(){
        leaguesAdapter= AllLeaguesAdapter(emptyList(),this)
        binding.RecyclerView.apply {
            layoutManager= LinearLayoutManager(requireContext())
            binding.RecyclerView.adapter = leaguesAdapter
        }
    }
    private fun observeLeagueData(){
        viewModel.leagues.observe(viewLifecycleOwner) { response ->
            if (response.isSuccessful) {
                println("${response.body()},responseBodyM")
                response.body()?.let {
                    updateAdapterData(it)
                } ?: run {
                    Log.d("No data available","No data to fetch")
                    Toast.makeText(requireContext(), "No data available", Toast.LENGTH_SHORT).show()
                }
            } else {
                Log.e("Error","Failed to fetch leagues")
                Toast.makeText(requireContext(), "Failed to fetch leagues", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun updateAdapterData(response: AllLeaguesReponse){
        leaguesAdapter= AllLeaguesAdapter(response.response,this)
        binding.RecyclerView.adapter=leaguesAdapter
    }


    companion object {
        fun newInstance(param1: String, param2: String) =
            AllLeaguesFragment().apply {
                arguments = Bundle().apply {

                }
            }
    }

    override fun onLeagueClick(league: com.live.quickscores.allleaguesresponse.Response) {
        val leagueId=league.league.id.toString()
        sharedViewModel.setSelectedLeagueId(leagueId)
        val season=league.seasons.toString()
        val name=league.league.name
        val leagueLogo=league.league.logo
        val leagueCountryName=league.country.name
        leagueClickListener?.onLeagueClicked(leagueId,season,name,leagueLogo,
            leagueCountryName)
        println("${leagueId},${season},${name},${leagueLogo},${leagueCountryName},AllLeagues Malenge")
    }
    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
    override fun onDetach() {
        super.onDetach()
        leagueClickListener=null
    }
}