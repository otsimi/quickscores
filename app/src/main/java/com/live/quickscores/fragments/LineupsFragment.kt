package com.live.quickscores.fragments

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import com.live.quickscores.LineupsRepository
import com.live.quickscores.LineupsViewModel
import com.live.quickscores.LineupsViewModelFactory
import com.live.quickscores.R
import com.live.quickscores.databinding.FragmentLineupsBinding
import com.live.quickscores.lineupresponse.LineupsResponse
import com.live.quickscores.lineupresponse.Response
import com.live.quickscores.utils.LOGO_URL
import com.squareup.picasso.Picasso


class LineupsFragment : Fragment() {
    private var lineupsBinding:FragmentLineupsBinding?=null
    private val binding get() =lineupsBinding!!
    private val viewModel:LineupsViewModel by viewModels{
        LineupsViewModelFactory(LineupsRepository())
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val fixtureId = arguments?.getString("matchId")
        if (fixtureId != null) {
            viewModel.fetchLineups(fixtureId)
            println("${fixtureId},maleeenge")
        }
        arguments?.let {

        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        lineupsBinding = FragmentLineupsBinding.inflate(inflater, container, false)
        return binding.root
    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel.lineups.observe(viewLifecycleOwner) { response ->
            val lineupsBody = response.body()
            if (lineupsBody!=null) {
                println("Lineups data: ${lineupsBody.response}")
      lineupsBody.response.forEachIndexed { index, response ->
         if (index==0){
        binding.homeManagerName.text=response.coach.name
             binding.homeTeamFormation.text=response.formation
             if (response.coach.photo.isNotEmpty()){
                 Picasso.get().load("$LOGO_URL${response.coach.photo}.png").into(binding.homeManagerImage)
             }
             println("${response.startXI[0]},malemgee")
             binding.homeGoalkeeperName.text=response.startXI[0].player.name
             binding.homeGoalkeeperNumber.text=response.startXI[0].player.number.toString()
             binding.homeLeftBackName.text=response.startXI[1].player.name
             binding.homeLeftBackNumber.text=response.startXI[1].player.number.toString()
             binding.homeRightBackName.text=response.startXI[2].player.name
             binding.homeRightBackNumber.text=response.startXI[2].player.number.toString()
             binding.homeCentreBack1Name.text=response.startXI[3].player.name
             binding.homeCentreBack1Number.text=response.startXI[3].player.number.toString()
             binding.homeCentreBack2Name.text=response.startXI[4].player.name
             binding.homeCentreBack2Number.text=response.startXI[4].player.number.toString()
             binding.homeMid1Name.text=response.startXI[5].player.name
             binding.homeMid1Number.text=response.startXI[5].player.number.toString()
             binding.homeMid2Name.text=response.startXI[6].player.name
             binding.homeMid2Number.text=response.startXI[6].player.number.toString()
             binding.homeWinger1Name.text=response.startXI[7].player.name
             binding.homeWinger1Number.text=response.startXI[7].player.number.toString()
             binding.homeWinger2Name.text=response.startXI[8].player.name
             binding.homeWinger2Number.text=response.startXI[8].player.number.toString()
             binding.homeForward1Name.text=response.startXI[9].player.name
             binding.homeForward1Number.text=response.startXI[9].player.number.toString()
             binding.homeForward2Name.text=response.startXI[10].player.name
             binding.homeForward2Number.text=response.startXI[10].player.number.toString()

             binding.homeSub1Name.text=response.substitutes[0].player.name
             binding.homeSub1Number.text=response.substitutes[0].player.number.toString()
             binding.homeSub2Name.text=response.substitutes[1].player.name
             binding.homeSub2Number.text=response.substitutes[1].player.number.toString()
             binding.homeSub3Name.text=response.substitutes[2].player.name
             binding.homeSub3Number.text=response.substitutes[2].player.number.toString()
             binding.homeSub4Name.text=response.substitutes[3].player.name
             binding.homeSub4Number.text=response.substitutes[3].player.number.toString()
             binding.homeSub5Name.text=response.substitutes[4].player.name
             binding.homeSub5Number.text=response.substitutes[4].player.number.toString()
             binding.homeSub6Name.text=response.substitutes[5].player.name
             binding.homeSub6Number.text=response.substitutes[5].player.number.toString()

         }else if (index==1){
             binding.awayManagerName.text=response.coach.name
             if (response.coach.photo.isNotEmpty()){
                 Picasso.get().load("$LOGO_URL${response.coach.photo}.png").into(binding.awayManagerImage)
             }
             binding.awayTeamFormation.text=response.formation
             binding.awayGoalkeeperName.text=response.startXI[0].player.name
             binding.awayGoalkeeperNumber.text=response.startXI[0].player.number.toString()
             binding.awayLeftBackName.text=response.startXI[1].player.name
             binding.awayLeftBackNumber.text=response.startXI[1].player.number.toString()
             binding.awayRightBackName.text=response.startXI[2].player.name
             binding.awayRightBackNumber.text=response.startXI[2].player.number.toString()
             binding.awayCentreBackName.text=response.startXI[3].player.name
             binding.awayCentreBackNumber.text=response.startXI[3].player.number.toString()
             binding.awayCentreBack2Name.text=response.startXI[4].player.name
             binding.awayCentreBack2Number.text=response.startXI[4].player.number.toString()
             binding.awayMid1Name.text=response.startXI[5].player.name
             binding.awayMid1Number.text=response.startXI[5].player.number.toString()
             binding.awayMid2Name.text=response.startXI[6].player.name
             binding.awayMid2Number.text=response.startXI[6].player.number.toString()
             binding.awayWinger1Name.text=response.startXI[7].player.name
             binding.awayWinger1Number.text=response.startXI[7].player.number.toString()
             binding.awayWinger2Name.text=response.startXI[8].player.name
             binding.awayWinger2Number.text=response.startXI[8].player.number.toString()
             binding.awayForward1Name.text=response.startXI[9].player.name
             binding.awayForward1Number.text=response.startXI[9].player.number.toString()
             binding.awayForward2Name.text=response.startXI[10].player.name
             binding.awayForward2Number.text=response.startXI[10].player.number.toString()


             binding.awaySub1Name.text=response.substitutes[0].player.name
             binding.awaySub1Number.text=response.substitutes[0].player.number.toString()
             binding.awaySub2Name.text=response.substitutes[1].player.name
             binding.awaySub2Number.text=response.substitutes[1].player.number.toString()
             binding.awaySub3Name.text=response.substitutes[2].player.name
             binding.awaySub3Number.text=response.substitutes[2].player.number.toString()
             binding.awaySub4Name.text=response.substitutes[3].player.name
             binding.awaySub4Number.text=response.substitutes[3].player.number.toString()
             binding.awaySub5Name.text=response.substitutes[4].player.name
             binding.awaySub5Number.text=response.substitutes[4].player.number.toString()
             binding.awaySub6Name.text=response.substitutes[5].player.name
             binding.awaySub6Number.text=response.substitutes[5].player.number.toString()




         }
}

            } else {
                Log.d("error occurred","Malenge")
            }
        }
    }

    companion object {

        fun newInstance(param1: String, param2: String) =
            LineupsFragment().apply {
                arguments = Bundle().apply {

                }
            }
    }
}