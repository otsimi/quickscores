package com.live.quickscores.fragments

import android.content.Context
import android.os.Build
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.live.quickscores.LeagueFixturesRepo
import com.live.quickscores.LeagueFixturesViewModel
import com.live.quickscores.LeagueFixturesViewModelFactoryProvider
import com.live.quickscores.adapters.LeagueFixturesAdapter
import com.live.quickscores.databinding.FragmentLeaguesFixturesBinding
import com.live.quickscores.fixturesresponse.FixturesResponse
import com.live.quickscores.fixturesresponse.Response
import java.time.OffsetDateTime
import java.time.ZonedDateTime
import java.time.format.DateTimeFormatter
import java.time.format.DateTimeParseException

class LeaguesFixturesFragment : Fragment(),LeagueFixturesAdapter.OnFixtureClickListener {
    private lateinit var leagueId:String
    private var leagueName: String? = null
    private var countryName:String?=null
//    private var season:String?=null
    private var fixtureClickListener: OnFixtureClickListener? = null
    private var _binding:FragmentLeaguesFixturesBinding?=null
    private val binding get() = _binding!!
    private lateinit var fixturesAdapter:LeagueFixturesAdapter
    private val viewModel:LeagueFixturesViewModel by viewModels {
        LeagueFixturesViewModelFactoryProvider(LeagueFixturesRepo())
    }
    interface OnFixtureClickListener{
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
    override fun onAttach(context: Context) {
        super.onAttach(context)
        if (context is OnFixtureClickListener) {
            fixtureClickListener = context
        } else {
            throw RuntimeException("$context must implement OnFixtureClickListener")
        }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        arguments?.let {
            leagueName = it.getString("name") ?: leagueName
            countryName=it.getString("country")?:countryName
            leagueId=it.getString("leagueId")?:leagueId
//            season = it.getString("season") ?: getCurrentYear().toString()

        }

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding=FragmentLeaguesFixturesBinding.inflate(inflater,container,false)
        return binding.root
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
//        binding.leagueName.text = leagueName
//        binding.countryName.text = countryName
        val fromDate = getCurrentDate()
        val toDate=getDateInThreeMonths()
        val season = getCurrentYear().toString()
        println("${season},leagueSeasonPrintout")
        viewModel.fetchFixturesByLeagueId(leagueId,season,fromDate,toDate)
        println("${leagueId},${season},${fromDate},${toDate}Malengeprinted")
        viewModel.fixtures.observe(viewLifecycleOwner, Observer { response ->
            response?.let{
                if (it.isSuccessful) {
                    println("Response received successfully: ${response.body()}")
                    it.body()?.let { fixturesData  ->
                        val groupedFixtures = groupFixturesByDate(fixturesData.response)
                        setUpRecyclerView(groupedFixtures)
                    } ?: run {
                        Log.e("LeagueFixturesFragment", "Empty response body")
                    }
                } else {
                    println("Error response code: ${response.code()}")
                    println("Error response body: ${response.errorBody()?.string()}")
                    Toast.makeText(requireContext(), "Failed to load fixtures", Toast.LENGTH_SHORT)
                        .show()
                }
            }

        })
    }
    private fun setUpRecyclerView(groupedFixtures: Map<String, List<Response>>) {
        fixturesAdapter = LeagueFixturesAdapter(groupedFixtures, this)
        binding.RecyclerView.apply {
            layoutManager = LinearLayoutManager(requireContext())
            adapter = fixturesAdapter
        }
    }
    @RequiresApi(Build.VERSION_CODES.O)
    private fun groupFixturesByDate(fixtures: List<Response>): Map<String, List<Response>> {
        val dateFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd")
        return fixtures.groupBy { fixture ->
            OffsetDateTime.parse(fixture.fixture.date).format(dateFormatter)
        }
    }



    companion object {
        fun newInstance(leagueName: String) = LeaguesFixturesFragment().apply {
            arguments = Bundle().apply {
                putString("leagueName", leagueName)
            }
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
    @RequiresApi(Build.VERSION_CODES.O)
    private fun getCurrentDate(): String {
        return ZonedDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd"))
    }
    @RequiresApi(Build.VERSION_CODES.O)
    private fun getDateInThreeMonths(): String {
        val currentDate = ZonedDateTime.now().toLocalDate()
        val dateInThreeMonths = currentDate.plusMonths(3)
        return dateInThreeMonths.format(DateTimeFormatter.ofPattern("yyyy-MM-dd"))
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun getCurrentYear(): Int {
        return ZonedDateTime.now().year

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
        fixtureClickListener?.onFixtureClicked(matchId, homeTeam, awayTeam, homeTeamLogoUrl, awayTeamLogoUrl,leagueName,venue,formattedDate,country,city,referee,homeTeamGoals,awayTeamGoals,
            homeTeamId.toString(),awayTeamId.toString(),leagueId.toString(),season.toString())
        println("${referee},Malenge")
    }


}