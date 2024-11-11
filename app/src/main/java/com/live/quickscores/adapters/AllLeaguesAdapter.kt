package com.live.quickscores.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.live.quickscores.R
import com.live.quickscores.allleaguesresponse.Response
import com.live.quickscores.databinding.LeaguesItemsBinding
import com.live.quickscores.fragments.AllLeaguesFragment
import com.squareup.picasso.Picasso

class AllLeaguesAdapter(private val leagueLists:List<Response>,private val leagueClickListener:AllLeaguesFragment):RecyclerView.Adapter<AllLeaguesAdapter.AllLeaguesViewHolder>() {

    interface OnLeagueClickListener{
        fun onLeagueClick(league:Response)
    }
    inner class AllLeaguesViewHolder(val binding: LeaguesItemsBinding):RecyclerView.ViewHolder(binding.root){
        val leagueName: TextView =binding.leagueName
        val leagueLogo: ImageView =binding.leagueLogo

        init {
            binding.root.setOnClickListener{
                val position=adapterPosition
                if (position!=RecyclerView.NO_POSITION){
                    leagueClickListener.onLeagueClick(leagueLists[position])
                }
            }
        }
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): AllLeaguesAdapter.AllLeaguesViewHolder {
        val binding=LeaguesItemsBinding.inflate(LayoutInflater.from(parent.context),parent,false)
        return AllLeaguesViewHolder(binding)
    }


    override fun onBindViewHolder(holder: AllLeaguesAdapter.AllLeaguesViewHolder, position: Int) {
        val leagues=leagueLists[position]
        holder.leagueName.text=leagues.league.name
        if (leagues.league.logo.isNotEmpty()){
            Picasso.get().load(leagues.league.logo).into(holder.leagueLogo)
        }else{
            holder.leagueLogo.setImageResource(R.drawable.imageholder)
        }
    }

    override fun getItemCount(): Int {
        return leagueLists.size
    }
}