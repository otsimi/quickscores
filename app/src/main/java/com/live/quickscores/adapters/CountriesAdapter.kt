package com.live.quickscores.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.live.quickscores.countriesresponse.Response
import com.live.quickscores.databinding.CountriesItemsBinding
import com.squareup.picasso.Picasso

class CountriesAdapter(private val countryLists: List<Response>):RecyclerView.Adapter<CountriesAdapter.CountriesViewHolder>() {

    class CountriesViewHolder(val binding: CountriesItemsBinding):RecyclerView.ViewHolder(binding.root){
        val countryNam:TextView=binding.countryName
        val countryFla:ImageView=binding.countryFlag
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CountriesViewHolder {
       val binding=CountriesItemsBinding.inflate(LayoutInflater.from(parent.context), parent, false)

        return CountriesViewHolder(binding)
    }

    override fun onBindViewHolder(holder: CountriesViewHolder, position: Int) {
       val countries=countryLists[position]
        holder.countryNam.text = countries.name ?: "Unknown Country"
        if (countries.flag.isNotEmpty()){
            Picasso.get().load("${countries.flag}.svg").into(holder.countryFla)
            println("Loaded Image from URL: ${countries.flag}")
        }
    }

    override fun getItemCount(): Int {
       return countryLists.size
    }

}