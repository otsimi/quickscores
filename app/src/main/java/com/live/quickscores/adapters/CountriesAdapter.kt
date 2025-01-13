package com.live.quickscores.adapters

import android.annotation.SuppressLint
import android.net.Uri
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.live.quickscores.R
import com.live.quickscores.countriesresponse.Response
import com.live.quickscores.databinding.CountriesItemsBinding

class CountriesAdapter(
    private val countryLists: List<Response>,
    private val countryClickListener: OnCountryClickListener
) : RecyclerView.Adapter<CountriesAdapter.CountriesViewHolder>() {

    interface OnCountryClickListener {
        fun onCountryClick(country: Response)
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
        val binding = CountriesItemsBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return CountriesViewHolder(binding)
    }

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: CountriesViewHolder, position: Int) {
        val country = countryLists[position]
        Log.d("CountriesAdapter", "Country at position $position: $country")
        if (country.name.isEmpty() || country.flag.isEmpty()) {
            Log.d("CountriesAdapter", "Skipping invalid country at position $position: $country")
            holder.countryName.text = "Invalid Country"
            holder.countryFlag.setImageResource(R.drawable.imageholder)
            return
        }
        holder.countryName.text = country.name
        val svgUrl = country.flag
        Glide.with(holder.itemView.context)
            .load(Uri.parse(svgUrl))
            .placeholder(R.drawable.imageholder)
            .error(R.drawable.imageholder)
            .into(holder.countryFlag)
    }

    override fun getItemCount(): Int {
        return countryLists.size
    }
}







