package com.live.quickscores.fragments

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.live.quickscores.repositories.CountriesRepository
import com.live.quickscores.viewmodelclasses.CountriesViewModel
import com.live.quickscores.viewmodelclasses.CountriesViewModelFactory
import com.live.quickscores.adapters.CountriesAdapter
import com.live.quickscores.countriesresponse.CountriesResponse
import com.live.quickscores.countriesresponse.Response
import com.live.quickscores.databinding.FragmentCountriesBinding

class CountriesFragment : Fragment(), CountriesAdapter.OnCountryClickListener {

    private var _binding: FragmentCountriesBinding? = null
    private val binding get() = _binding!!
    private var countryClickListener: OnCountryClicked? = null
    private lateinit var countriesAdapter: CountriesAdapter
    private val viewModel: CountriesViewModel by viewModels {
        CountriesViewModelFactory(CountriesRepository())
    }


    interface OnCountryClicked {
        fun onCountryClicked(
            countryName: String,
            countryCode:String
        )
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        if (context is OnCountryClicked) {
            countryClickListener = context
        } else {
            throw RuntimeException("$context must implement OnCountryClicked")
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentCountriesBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupRecyclerView()
        observeCountriesData()
        viewModel.fetchCountries()
    }

    private fun setupRecyclerView() {
        countriesAdapter = CountriesAdapter(emptyList(), this)
        binding.RecyclerView.apply {
            layoutManager = LinearLayoutManager(requireContext())
            adapter = countriesAdapter
        }
    }

    private fun observeCountriesData() {
        viewModel.allCountries.observe(viewLifecycleOwner) { response ->
            if (response.isSuccessful) {
                response.body()?.let {
                    updateAdapterData(it)
                }
            } else {
                Toast.makeText(requireContext(), "Failed to fetch data", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun updateAdapterData(countriesResponse: CountriesResponse) {
        countriesAdapter = CountriesAdapter(countriesResponse.response, this)
        binding.RecyclerView.adapter = countriesAdapter
        countriesAdapter.notifyDataSetChanged()
    }

    override fun onCountryClick(country: Response) {
        val countryName = country.name ?: "Unknown Country"
        val countryCode = country.code
        countryClickListener?.onCountryClicked(countryName,countryCode)
        println("${countryName},Malenge,countryName")
        println("${countryCode},Malenge")
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onDetach() {
        super.onDetach()
        countryClickListener = null
    }
}
