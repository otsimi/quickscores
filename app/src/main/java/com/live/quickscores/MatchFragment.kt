package com.live.quickscores

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response as Response1

class MatchFragment : Fragment() {

    private lateinit var recyclerViewAdapter: RecyclerViewAdapter
    private lateinit var recyclerView: RecyclerView
    private lateinit var date: String

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_match, container, false)
        date = arguments?.getString("date") ?: ""
        recyclerView = view.findViewById(R.id.RecyclerView)

        fetchDataForDate(date)

        return view
    }

    private fun fetchDataForDate(date: String) {
        val apiKey = RAPID_API_KEY
        RetrofitClient.apiService.fetchFixtures(apiKey, date).enqueue(object : Callback<FixturesResponse> {
            override fun onResponse(call: Call<FixturesResponse>, response: Response1<FixturesResponse>) {
                if (response.isSuccessful) {
                    val fixturesResponseBody = response.body()
                    Log.d("API Response", "Full Response JSON: $fixturesResponseBody")
                    fixturesResponseBody?.let {
                        setupRecyclerView(it)
                    }
                } else {
                    Log.e("Malenge", response.message())
                }
            }


            override fun onFailure(call: Call<FixturesResponse>, t: Throwable) {
                Log.e("API Failure", t.message ?: "Murima")
            }
        })
    }

    private fun setupRecyclerView(fixturesResponse: FixturesResponse) {
        val headerList = listOf(fixturesResponse)
        val competitionList = fixturesResponse.response

        recyclerViewAdapter = RecyclerViewAdapter(headerList, competitionList)
        recyclerView.apply {
            layoutManager = LinearLayoutManager(context)
            adapter = recyclerViewAdapter
        }
    }

    companion object {
        fun newInstance(date: String): MatchFragment {
            val fragment = MatchFragment()
            val args = Bundle()
            args.putString("date", date)
            fragment.arguments = args
            return fragment
        }
    }
}
