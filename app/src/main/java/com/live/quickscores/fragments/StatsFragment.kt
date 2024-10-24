package com.live.quickscores.fragments

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.live.quickscores.R
import com.live.quickscores.RAPID_API_KEY
import com.live.quickscores.RetrofitClient
import com.live.quickscores.dataclasses.StatisticsResponse
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class StatsFragment : Fragment() {

    private var matchId: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        arguments?.let {
            matchId = it.getString("matchId")
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        matchId?.let { fixtureId ->
            getMatchStatistics(fixtureId)
        }

        return inflater.inflate(R.layout.fragment_stats, container, false)
    }

    private fun getMatchStatistics(fixtureId: String) {
        println("${fixtureId},murima")
        val apiKey = RAPID_API_KEY
        RetrofitClient.apiService.fetchFixtureStatistics(apiKey, fixtureId).enqueue(object :
            Callback<StatisticsResponse> {
            override fun onResponse(call: Call<StatisticsResponse>, response: Response<StatisticsResponse>) {
             println("${apiKey},${fixtureId},Malenge")
                if (response.isSuccessful) {

                    Log.d("StatsFragment", "Response malenge: ${response.message()}")
                    val statsResponseBody = response.body()
                    statsResponseBody?.let {
                        println("${fixtureId},Malenge murima")
                    }
                } else {

                    Log.e("StatsFragment", "Error: ${response.message()}")
                }
            }

            override fun onFailure(call: Call<StatisticsResponse>, t: Throwable) {
                Log.e("StatsFragment", "API Failure: ${t.message ?: "Error"}")
            }
        })
    }

    companion object {
        fun newInstance(matchId: String) =
            StatsFragment().apply {
                arguments = Bundle().apply {
                    putString("matchId", matchId)
                }
            }
    }
}
