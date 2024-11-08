package com.live.quickscores.fragments

import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.live.quickscores.LeagueFixturesRepo
import com.live.quickscores.LeagueFixturesViewModel
import com.live.quickscores.LeagueFixturesViewModelFactoryProvider
import com.live.quickscores.LeagueIdSharedViewModel
import com.live.quickscores.adapters.LeagueFixturesAdapter
import com.live.quickscores.databinding.FragmentLeagueFixturesContentBinding
import com.live.quickscores.fixturesresponse.Response
import java.time.OffsetDateTime
import java.time.ZonedDateTime
import java.time.format.DateTimeFormatter
import java.time.format.DateTimeParseException

class LeagueFixturesContentFragment : Fragment(), LeagueFixturesAdapter.OnFixtureClickListener {
    private var _binding: FragmentLeagueFixturesContentBinding? = null
    private val binding get() = _binding!!
    private lateinit var fixturesAdapter: LeagueFixturesAdapter
    private var fixtureClickListener: OnFixtureClickListener? = null
    private val viewModel: LeagueFixturesViewModel by viewModels {
        LeagueFixturesViewModelFactoryProvider(LeagueFixturesRepo())
    }

    private val sharedViewModel: LeagueIdSharedViewModel by activityViewModels()
    private var leagueId: String? = null

    interface OnFixtureClickListener {
        fun onFixtureClicked(
            matchId: String,
            homeTeam: String,
            awayTeam: String,
            homeTeamLogoUrl: String,
            awayTeamLogoUrl: String,
            leagueName: String,
            venue: String?,
            date: String?,
            country: String?,
            referee: String?,
            city: String?,
            homeTeamGoals: String?,
            awayTeamGoals: String?,
            homeTeamId: String,
            awayTeamId: String,
            leagueId: String,
            season: String
        )
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        _binding = FragmentLeagueFixturesContentBinding.inflate(inflater, container, false)
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
            val fromDate = getCurrentDate()
            val toDate = getDateInThreeMonths()

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

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onFixtureClick(match: Response) {
        val homeTeam = match.teams.home.name
        val awayTeam = match.teams.away.name
        val homeTeamLogoUrl = match.teams.home.logo
        val awayTeamLogoUrl = match.teams.away.logo
        val matchId = match.fixture.id.toString()
        val leagueName = match.league.name
        val venue = match.fixture.venue.name
        val formattedDate = formatDate(match.fixture.date)
        val country = match.league.country
        val referee = match.fixture.referee
        val city = match.fixture.venue.city
        val homeTeamGoals = match.goals.home
        val awayTeamGoals = match.goals.away
        val homeTeamId = match.teams.home.id
        val awayTeamId = match.teams.away.id
        val leagueId = match.league.id
        val season = match.league.season

        Toast.makeText(requireContext(), "Clicked on: $homeTeam vs $awayTeam", Toast.LENGTH_SHORT).show()
        fixtureClickListener?.onFixtureClicked(
            matchId, homeTeam, awayTeam, homeTeamLogoUrl, awayTeamLogoUrl, leagueName, venue,
            formattedDate, country, city, referee, homeTeamGoals, awayTeamGoals,
            homeTeamId.toString(), awayTeamId.toString(), leagueId.toString(), season.toString()
        )
        println("$referee, Malenge")
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun groupFixturesByDate(fixtures: List<Response>): Map<String, List<Response>> {
        val dateFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd")
        return fixtures.groupBy { fixture ->
            OffsetDateTime.parse(fixture.fixture.date).format(dateFormatter)
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
