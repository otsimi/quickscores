package com.live.quickscores.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import com.live.quickscores.LeagueTableRepository
import com.live.quickscores.LeagueTableViewModel
import com.live.quickscores.LeagueTableViewModelFactory
import com.live.quickscores.R
import com.live.quickscores.databinding.FragmentLeagueTableBinding

class LeagueTableFragment : Fragment() {
    private var tableBinding:FragmentLeagueTableBinding?=null
    private val binding get() = tableBinding!!
    private val viewModel:LeagueTableViewModel by viewModels(){
        LeagueTableViewModelFactory(LeagueTableRepository())
    }
    private var leagueName: String? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val leagueId= arguments?.getString("leagueId")
        leagueName=arguments?.getString("leagueName")
        leagueId?.let {
            println("${leagueId},MALENGE")
            viewModel.getTable(it)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        tableBinding=FragmentLeagueTableBinding.inflate(inflater,container,false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.leagueTitle.text=leagueName

    }

    companion object {

        fun newInstance(param1: String, param2: String) =
            LeagueTableFragment().apply {
                arguments = Bundle().apply {

                }
            }
    }
}