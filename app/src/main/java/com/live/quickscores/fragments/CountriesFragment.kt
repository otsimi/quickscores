package com.live.quickscores

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.live.quickscores.adapters.CountriesAdapter
import com.live.quickscores.countriesresponse.CountriesResponse
import com.live.quickscores.databinding.FragmentCountriesBinding

class CountriesFragment : Fragment() {

    private var _binding: FragmentCountriesBinding? = null
    private val binding get() = _binding!!

    private lateinit var countriesAdapter: CountriesAdapter
    private val viewModel: CountriesViewModel by viewModels {
        CountriesViewModelFactory(CountriesRepository()) // replace with actual repository instance
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

        // Trigger the fetching of countries
        viewModel.fetchCountries()
    }

    private fun setupRecyclerView() {
        countriesAdapter = CountriesAdapter(emptyList())
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
        countriesAdapter = CountriesAdapter(countriesResponse.response) // update with actual list
        binding.RecyclerView.adapter = countriesAdapter
        countriesAdapter.notifyDataSetChanged()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
