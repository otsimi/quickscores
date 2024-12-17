package com.live.quickscores.fragments

import android.annotation.SuppressLint
import android.os.Build
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.live.quickscores.repositories.LeagueFixturesRepo
import com.live.quickscores.viewmodelclasses.LeagueFixturesViewModel
import com.live.quickscores.viewmodelclasses.LeagueFixturesViewModelFactoryProvider
import com.live.quickscores.LeagueIdSharedViewModel
import com.live.quickscores.R
import com.live.quickscores.adapters.LeagueFixturesAdapter
import com.live.quickscores.databinding.FragmentResultsBinding
import com.live.quickscores.fixtureresponse.Response
import java.text.SimpleDateFormat
import java.time.OffsetDateTime
import java.time.ZonedDateTime
import java.time.format.DateTimeFormatter
import java.time.format.DateTimeParseException
import java.util.Calendar

class ResultsFragment : Fragment(),LeagueFixturesAdapter.OnFixtureClickListener {
    private var _binding:FragmentResultsBinding?=null
    private val binding get() = _binding!!
    private lateinit var fixturesAdapter: LeagueFixturesAdapter
    private val viewModel: LeagueFixturesViewModel by viewModels {
        LeagueFixturesViewModelFactoryProvider(LeagueFixturesRepo())
    }
    private val sharedViewModel: LeagueIdSharedViewModel by activityViewModels()
    private var leagueId: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {

        }
    }
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding=FragmentResultsBinding.inflate(inflater,container,false)
        return binding.root
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        sharedViewModel.selectedLeagueId.observe(viewLifecycleOwner) { id ->
            leagueId = id
            println("$id, gottenStringBundle")
            println("${leagueId}, malengeLoadedId")

            val season = getCurrentYear().toString()
            val fromDate = getDateThreeMonthsAgo()
            val toDate = getCurrentDate()

            leagueId?.let {
                viewModel.fetchFixturesByLeagueId(it, season, fromDate, toDate)
                println("${it},${season},${fromDate},${toDate}, Malengeparams")
            } ?: run {
                println("leagueId is null, unable to fetch fixtures")
            }
        }
        viewModel.fixtures.observe(viewLifecycleOwner, Observer { response ->
            response?.body()?.let { fixturesData ->
                val groupedFixtures = groupFixturesByDate(fixturesData.response)
                println("$groupedFixtures, responseMalenge")
                setUpRecyclerView(groupedFixtures)
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

    companion object {

        fun newInstance(param1: String, param2: String) =
            ResultsFragment().apply {
                arguments = Bundle().apply {

                }
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
        val fixtureStatus=match.fixture.status.short
        val matchPeriod=match.fixture.status.elapsed

        Toast.makeText(requireContext(), "Clicked on: $homeTeam vs $awayTeam", Toast.LENGTH_SHORT).show()
        val args = Bundle().apply {
            putString("matchId", matchId ?: "")
            putString("homeTeam", homeTeam ?: "Unknown")
            putString("awayTeam", awayTeam ?: "Unknown")
            putString("homeTeamLogoUrl", homeTeamLogoUrl ?: "")
            putString("awayTeamLogoUrl", awayTeamLogoUrl ?: "")
            putString("leagueName", leagueName ?: "")
            putString("venue", venue ?: "")
            putString("date", formattedDate ?: "")
            putString("country", country ?: "")
            putString("referee", referee ?: "")
            putString("city", city ?: "")
            putString("homeTeamGoals", (homeTeamGoals ?: "").toString())
            putString("awayTeamGoals", (awayTeamGoals ?: "").toString())
            putString("homeTeamId", homeTeamId.toString())
            putString("awayTeamId", awayTeamId.toString())
            putString("leagueId", leagueId.toString())
            putString("season", season.toString())
            putString("fixtureStatus",fixtureStatus)
            putString("matchPeriod",matchPeriod.toString())
        }
        findNavController().navigate(R.id.action_leaguesFixturesFragment_to_fixtureFragment3, args)
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun groupFixturesByDate(fixtures: List<Response>): Map<String, List<Response>> {
        val dateFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd")

        val sortedFixtures = fixtures.sortedByDescending { fixture ->
            OffsetDateTime.parse(fixture.fixture.date).toLocalDate()
        }
        return sortedFixtures.groupBy { fixture ->
            OffsetDateTime.parse(fixture.fixture.date).format(dateFormatter)
        }
    }
    @RequiresApi(Build.VERSION_CODES.O)
    private fun getCurrentDate(): String {
        return ZonedDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd"))
    }
    @SuppressLint("SimpleDateFormat")
    fun getDateThreeMonthsAgo(): String {
        val calendar = Calendar.getInstance()
        calendar.add(Calendar.MONTH, -3)
        val formatter = SimpleDateFormat("yyyy-MM-dd")
        return formatter.format(calendar.time)
    }
    @RequiresApi(Build.VERSION_CODES.O)
    private fun getCurrentYear(): Int {
        return ZonedDateTime.now().year
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
}