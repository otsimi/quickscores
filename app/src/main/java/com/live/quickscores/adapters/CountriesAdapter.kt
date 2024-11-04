package com.live.quickscores.adapters

import android.net.Uri
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.live.quickscores.countriesresponse.Response
import com.live.quickscores.databinding.CountriesItemsBinding

class CountriesAdapter(private val countryLists: List<Response>, private val countryClickListener:OnCountryClickListener) : RecyclerView.Adapter<CountriesAdapter.CountriesViewHolder>() {

    interface OnCountryClickListener {
        fun onCountryClick(country:Response)
    }

    inner class CountriesViewHolder(val binding: CountriesItemsBinding) :
        RecyclerView.ViewHolder(binding.root) {
        val countryName: TextView = binding.countryName
        val countryFlag: ImageView = binding.countryFlag

        init {
            binding.root.setOnClickListener {
                val position = adapterPosition
                if (position != RecyclerView.NO_POSITION) {
                    countryClickListener.onCountryClick(countryLists[position])
                }
            }
        }
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CountriesViewHolder {
        val binding =
            CountriesItemsBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return CountriesViewHolder(binding)
    }

    override fun onBindViewHolder(holder: CountriesViewHolder, position: Int) {
        val countries = countryLists[position]
        holder.countryName.text = countries.name ?: "Unknown Country"

        if (countries.flag.isNotEmpty()) {
            val svgUrl = countries.flag
            println("Loading SVG from URL: $svgUrl")

            Glide.with(holder.itemView.context)
                .load(Uri.parse(svgUrl))
                .into(holder.countryFlag)
        }
    }

    override fun getItemCount(): Int {
        return countryLists.size
    }
}






