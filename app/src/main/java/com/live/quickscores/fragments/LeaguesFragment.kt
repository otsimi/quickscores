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
import androidx.recyclerview.widget.LinearLayoutManager
import com.live.quickscores.LeaguesRepository
import com.live.quickscores.LeaguesViewModel
import com.live.quickscores.LeaguesViewModelFactoryProvider
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
    private val viewModel:LeaguesViewModel by viewModels(){
        LeaguesViewModelFactoryProvider(LeaguesRepository())
    }
    private var countryCode: String? = null
    private var season: String? = null

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
        val leagueId=league.league.id
        val season=league.seasons
        val name=league.league.name
        val leagueLogo=league.league.logo
        val leagueCountryName=league.country
        leagueClickListener?.onLeagueClicked(leagueId.toString(),season.toString(),name,leagueLogo,
            leagueCountryName.toString()
        )
    }
}