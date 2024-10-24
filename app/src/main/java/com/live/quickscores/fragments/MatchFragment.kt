package com.live.quickscores.fragments

import android.content.Context
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.live.quickscores.R
import com.live.quickscores.RAPID_API_KEY
import com.live.quickscores.RecyclerViewAdapter
import com.live.quickscores.Response
import com.live.quickscores.RetrofitClient
import com.live.quickscores.dataclasses.FixturesResponse
import retrofit2.Call
import retrofit2.Callback
import java.time.ZonedDateTime
import java.time.format.DateTimeFormatter
import java.time.format.DateTimeParseException
import retrofit2.Response as Response1

class MatchFragment : Fragment(), RecyclerViewAdapter.OnFixtureClickListener {

    private lateinit var recyclerViewAdapter: RecyclerViewAdapter
    private lateinit var recyclerView: RecyclerView
    private lateinit var date: String
    private var fixtureClickListener: OnFixtureClickListener? = null

    interface OnFixtureClickListener {
        fun onFixtureClicked(
            matchId: String,
            homeTeam: String,
            awayTeam: String,
            homeTeamLogoUrl: String,
            awayTeamLogoUrl: String,
            leagueName:String,
            venue:String?,
            date:String?,
            country:String?,
            referee:String?,
            city:String?


        )

    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        if (context is OnFixtureClickListener) {
            fixtureClickListener = context
        } else {
            throw RuntimeException("$context must implement OnFixtureClickListener")
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_match, container, false)
        date = arguments?.getString("date") ?: ""
        recyclerView = view.findViewById(R.id.RecyclerView)

        fetchDataForDate(date)
        return view
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onFixtureClick(match: Response) {
        val homeTeam = match.teams.home.name
        val awayTeam = match.teams.away.name
        val homeTeamLogoUrl = match.teams.home.logo
        val awayTeamLogoUrl = match.teams.away.logo
        val matchId = match.fixture.id.toString()
        val leagueName=match.league.name
        val venue=match.fixture.venue.name
        val formattedDate = formatDate(match.fixture.date)
        val country=match.league.country
        val referee=match.fixture.referee
        val city=match.fixture.venue.city


        Toast.makeText(requireContext(), "Clicked on: ${match.teams.home.name} vs ${match.teams.away.name}", Toast.LENGTH_SHORT).show()
        fixtureClickListener?.onFixtureClicked(matchId, homeTeam, awayTeam, homeTeamLogoUrl, awayTeamLogoUrl,leagueName,venue,formattedDate,country,city,referee)
        println("${referee},Malenge")
    }

    private fun fetchDataForDate(date: String) {
        val apiKey = RAPID_API_KEY
        RetrofitClient.apiService.fetchFixtures(apiKey, date).enqueue(object : Callback<FixturesResponse> {
            override fun onResponse(call: Call<FixturesResponse>, response: Response1<FixturesResponse>) {
                if (response.isSuccessful) {
                    Log.d("Malenge",response.message())
                    val fixturesResponseBody = response.body()
                    fixturesResponseBody?.let {
                        setupRecyclerView(it)
                    }
                } else {
                    Log.e("Malenge", response.message())
                }
            }

            override fun onFailure(call: Call<FixturesResponse>, t: Throwable) {
                Log.e("API Failure", t.message ?: "Error")
            }
        })
    }

    private fun setupRecyclerView(fixturesResponse: FixturesResponse) {
        val headerList = listOf(fixturesResponse)
        recyclerViewAdapter = RecyclerViewAdapter(headerList, this)
        recyclerView.apply {
            layoutManager = LinearLayoutManager(context)
            adapter = recyclerViewAdapter
        }
    }
    @RequiresApi(Build.VERSION_CODES.O)
    private fun formatDate(dateString: String): String? {
        return try {
            val zonedDateTime = ZonedDateTime.parse(dateString)
            val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd")
            zonedDateTime.format(formatter)
        } catch (e: DateTimeParseException) {
            e.printStackTrace()
            null
        }
    }

    override fun onDetach() {
        super.onDetach()
        fixtureClickListener = null
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
