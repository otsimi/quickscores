package com.live.quickscores.fragments

import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.live.quickscores.repositories.FixturesRepository
import com.live.quickscores.viewmodelclasses.FixturesViewModel
import com.live.quickscores.viewmodelclasses.FixturesViewModelFactory
import com.live.quickscores.R
import com.live.quickscores.adapters.RecyclerViewAdapter
import com.live.quickscores.fixtureresponse.Response
import java.time.ZonedDateTime
import java.time.format.DateTimeFormatter
import java.time.format.DateTimeParseException



class MatchFragment : Fragment(), RecyclerViewAdapter.OnFixtureClickListener {

    private lateinit var recyclerViewAdapter: RecyclerViewAdapter
    private lateinit var recyclerView: RecyclerView
    private lateinit var date: String
    private var fixtureClickListener: OnFixtureClickListener? = null

    private val viewModel: FixturesViewModel by viewModels {
        FixturesViewModelFactory(FixturesRepository())
    }

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
            city:String?,
            homeTeamGoals:String?,
            awayTeamGoals:String?,
            homeTeamId:String,
            awayTeamId:String,
            leagueId:String,
            season:String


        )

    }


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_match, container, false)
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        date = arguments?.getString("date") ?: ""
        recyclerView = view.findViewById(R.id.RecyclerView)
        if (date.isNotEmpty()) {
            println("${date},Malengedate")
            viewModel.fetchFixtures(date)
            println("${date},fetchDatadate")
            viewModel.fixtures.observe(viewLifecycleOwner, Observer { response ->
                response?.let {
                    if (response.isSuccessful) {
                        println("Response Body: ${response.body()}")
                        val fixturesData = response.body()
                        fixturesData?.response?.let { fixturesList ->
                            if (fixturesList.isNotEmpty()) {
                                println("Fixtures List: $fixturesList")
                                setupRecyclerView(fixturesList)
                            } else {
                                Log.e("MatchFragment", "No fixtures found for this date")
                            }
                        } ?: run {
                            Log.e("MatchFragment", "Response body is empty")
                        }
                    }
            }
            })
        }else{
            Log.d("Date","Date is empty")
        }
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
        val homeTeamGoals=match.goals.home
        val awayTeamGoals=match.goals.away
        val homeTeamId=match.teams.home.id
        val awayTeamId=match.teams.away.id
        val leagueId=match.league.id
        val season=match.league.season


        Toast.makeText(requireContext(), "Clicked on: ${match.teams.home.name} vs ${match.teams.away.name}", Toast.LENGTH_SHORT).show()
//        fixtureClickListener?.onFixtureClicked(matchId, homeTeam, awayTeam, homeTeamLogoUrl, awayTeamLogoUrl,leagueName,venue,formattedDate,country,city,referee,homeTeamGoals,awayTeamGoals,
//            homeTeamId.toString(),awayTeamId.toString(),leagueId.toString(),season.toString())
        println("${referee},Malenge")
    }


    private fun setupRecyclerView(fixtureList: List<Response>) {
        val combinedList = recyclerViewAdapter.prepareData(fixtureList)
        recyclerViewAdapter = RecyclerViewAdapter(combinedList, this)
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