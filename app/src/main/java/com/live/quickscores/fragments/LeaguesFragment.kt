package com.live.quickscores.fragments

import android.annotation.SuppressLint
import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.live.quickscores.LeagueIdSharedViewModel
import com.live.quickscores.repositories.LeaguesRepository
import com.live.quickscores.viewmodelclasses.LeaguesViewModel
import com.live.quickscores.viewmodelclasses.LeaguesViewModelFactoryProvider
import com.live.quickscores.adapters.LeaguesAdapter
import com.live.quickscores.databinding.FragmentLeaguesBinding
import com.live.quickscores.leagueResponse.LeaguesResponse
import com.live.quickscores.leagueResponse.Response

@SuppressLint("SuspiciousIndentation")
class LeaguesFragment : Fragment() ,LeaguesAdapter.OnLeagueClickListener{
   private var _binding:FragmentLeaguesBinding?=null
    private val binding get() = _binding!!
    private var leagueClickListener:OnLeagueClicked?=null
    private lateinit var leaguesAdapter: LeaguesAdapter
    private var countryCode: String? = null
    private var countryName:String?=null
    private var season: String? = null
    private lateinit var sharedViewModel: LeagueIdSharedViewModel
    private val viewModel: LeaguesViewModel by viewModels{
        LeaguesViewModelFactoryProvider(LeaguesRepository())
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
            countryCode = it.getString("countryCode")
            countryName=it.getString("countryName")

        }
    }

    @SuppressLint("SuspiciousIndentation")
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
      _binding=FragmentLeaguesBinding.inflate(inflater,container,false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        sharedViewModel = ViewModelProvider(requireActivity()).get(LeagueIdSharedViewModel::class.java)
        setUpRecyclerView()
        observeLeagueData()
        countryCode?.let { code ->
                viewModel.fetchLeagues(code)
            }
        }

    private fun setUpRecyclerView(){
        leaguesAdapter= LeaguesAdapter(emptyList(),this)
        binding.RecyclerView.apply {
            layoutManager=LinearLayoutManager(requireContext())
            binding.RecyclerView.adapter = leaguesAdapter
        }
    }
    private fun observeLeagueData() {
        viewModel.leagues.observe(viewLifecycleOwner) { response ->
            if (response.isSuccessful) {
                response.body()?.let {
                    updateAdapterData(it)
                }
            } else {
                Toast.makeText(requireContext(), "Failed to fetch data", Toast.LENGTH_SHORT).show()
            }

        }
    }


    private fun updateAdapterData(response: LeaguesResponse) {
        leaguesAdapter = LeaguesAdapter(response.response, this)
        binding.RecyclerView.adapter = leaguesAdapter
    }


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onDetach() {
        super.onDetach()
        leagueClickListener=null
    }

    companion object {
        @JvmStatic
        fun newInstance(countryCode: String, season: String) =
            LeaguesFragment().apply {
                arguments = Bundle().apply {
                    putString("countryCode", countryCode)
                    putString("season", season)
                }
            }
    }

    override fun onLeagueClick(league: Response) {
        val leagueId=league.league.id.toString()
        sharedViewModel.setSelectedLeagueId(leagueId)
        val season=league.seasons.toString()
        val leagueName=league.league.name
        val leagueLogo=league.league.logo
        val leagueCountryName=league.country.name
        val action=LeaguesFragmentDirections.actionLeaguesFragmentToLeaguesFixturesFragment(
            leagueId = leagueId,
            season = season,
            leagueName = leagueName,
            leagueLogo = leagueLogo,
            leagueCountryName = leagueCountryName
        )
        findNavController().navigate(action)
        println("${leagueCountryName},${leagueId},${leagueName},${leagueLogo},leaguefetchDetails")

    }
}