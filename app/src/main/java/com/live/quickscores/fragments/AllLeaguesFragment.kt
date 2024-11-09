package com.live.quickscores.fragments

import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.live.quickscores.AllLeaguesRepo
import com.live.quickscores.AllLeaguesViewModel
import com.live.quickscores.AllLeaguesViewModelFactoryProvider
import com.live.quickscores.R
import com.live.quickscores.adapters.LeaguesAdapter
import com.live.quickscores.databinding.FragmentAllLeaguesBinding
import com.live.quickscores.fragments.LeaguesFragment.OnLeagueClicked
import com.live.quickscores.leagueResponse.Response


class AllLeaguesFragment : Fragment(),LeaguesAdapter.OnLeagueClickListener {
    private var _binding: FragmentAllLeaguesBinding?=null
    private val binding get() = _binding!!
    private var leagueClickListener: OnLeagueClicked?=null
    private lateinit var leaguesAdapter: LeaguesAdapter
    private val viewModel:AllLeaguesViewModel by viewModels(){
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
        setUpRecyclerView()
        observeLeagueData()
    }
    private fun setUpRecyclerView(){
        leaguesAdapter= LeaguesAdapter(emptyList(),this)
        binding.RecyclerView.apply {
            layoutManager= LinearLayoutManager(requireContext())
            binding.RecyclerView.adapter = leaguesAdapter
        }
    }

    companion object {
        fun newInstance(param1: String, param2: String) =
            AllLeaguesFragment().apply {
                arguments = Bundle().apply {

                }
            }
    }

    override fun onLeagueClick(league: Response) {
        TODO("Not yet implemented")
    }
}