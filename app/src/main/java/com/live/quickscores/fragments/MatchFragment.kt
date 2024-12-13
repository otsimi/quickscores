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
import androidx.viewpager2.widget.ViewPager2
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator
import com.live.quickscores.repositories.FixturesRepository
import com.live.quickscores.viewmodelclasses.FixturesViewModel
import com.live.quickscores.viewmodelclasses.FixturesViewModelFactory
import com.live.quickscores.R
import com.live.quickscores.adapters.RecyclerViewAdapter
import com.live.quickscores.adapters.ViewPagerAdapter
import com.live.quickscores.fixtureresponse.Response
import java.text.SimpleDateFormat
import java.time.ZonedDateTime
import java.time.format.DateTimeFormatter
import java.time.format.DateTimeParseException
import java.util.Calendar
import java.util.Date
import java.util.Locale


class MatchFragment : Fragment(), RecyclerViewAdapter.OnFixtureClickListener {

    private lateinit var recyclerViewAdapter: RecyclerViewAdapter
    private lateinit var recyclerView: RecyclerView
    private var fixtureClickListener: OnFixtureClickListener? = null
    private lateinit var viewPager: ViewPager2
    private lateinit var tabLayout: TabLayout
    private lateinit var dates: List<String>
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
        dates = generateDates()
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        recyclerView = view.findViewById(R.id.RecyclerView)
        viewPager = view.findViewById(R.id.viewPager)
        tabLayout = view.findViewById(R.id.tab_layout)
        val todayIndex = findTodayIndex(dates)
        setUpViewPager(viewPager, tabLayout)
        viewPager.post {
            viewPager.setCurrentItem(todayIndex, false)
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
    private fun generateDates(): List<String> {
        val dateList = mutableListOf<String>()
        val dateFormat = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
        val calendar = Calendar.getInstance()

        for (i in -30..30) {
            calendar.time = Date()
            calendar.add(Calendar.DATE, i)
            dateList.add(dateFormat.format(calendar.time))
        }
        return dateList
    }
    private fun setUpViewPager(viewPager: ViewPager2, tabLayout: TabLayout) {
        val adapter = ViewPagerAdapter(requireActivity(), dates)
        viewPager.adapter = adapter
        viewPager.registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback() {
            override fun onPageSelected(position: Int) {
                super.onPageSelected(position)
                Log.d("ViewPagerSwipe", "Page swiped to position: $position")
                val selectedDate = dates[position]
                println("${selectedDate},selected date malenge")
                fetchFixturesForDate(selectedDate)
            }

            override fun onPageScrollStateChanged(state: Int) {
                super.onPageScrollStateChanged(state)
                when(state){
                    ViewPager2.SCROLL_STATE_IDLE->{
                        Log.d("ViewPagerSwipe", "Swipe ended (idle state)")
                    }
                    ViewPager2.SCROLL_STATE_DRAGGING->{
                        Log.d("ViewPagerSwipe", "Swipe started (dragging state)")
                    }
                    ViewPager2.SCROLL_STATE_SETTLING->{
                        Log.d("ViewPagerSwipe", "Swipe settling")
                    }
                }
            }
        })
        TabLayoutMediator(tabLayout, viewPager) { tab, position ->
            val date = dates[position]
            tab.text = getTabTitle(date)
        }.attach()
    }
    private fun fetchFixturesForDate(date: String) {
        println("Fetching fixtures for date: $date")
        viewModel.fetchFixtures(date)
        viewModel.fixtures.observe(viewLifecycleOwner, Observer { response ->
            response?.let {
                if (response.isSuccessful) {
                    val fixturesData = response.body()
                    fixturesData?.response?.let { fixturesList ->
                        if (fixturesList.isNotEmpty()) {
                            setupRecyclerView(fixturesList)
                        } else {
                            Log.e("MatchFragment", "No fixtures found for this date")
                        }
                    }
                }
            }
        })
    }


    private fun getTabTitle(dateString: String): String {
        val dateFormat = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
        val date = dateFormat.parse(dateString) ?: return dateString

        val calendar = Calendar.getInstance().apply { time = date }
        val today = Calendar.getInstance()
        val yesterday = Calendar.getInstance().apply { add(Calendar.DATE, -1) }
        val tomorrow = Calendar.getInstance().apply { add(Calendar.DATE, 1) }

        return when {
            calendar.get(Calendar.YEAR) == today.get(Calendar.YEAR) &&
                    calendar.get(Calendar.DAY_OF_YEAR) == today.get(Calendar.DAY_OF_YEAR) -> "Today"
            calendar.get(Calendar.YEAR) == yesterday.get(Calendar.YEAR) &&
                    calendar.get(Calendar.DAY_OF_YEAR) == yesterday.get(Calendar.DAY_OF_YEAR) -> "Yesterday"
            calendar.get(Calendar.YEAR) == tomorrow.get(Calendar.YEAR) &&
                    calendar.get(Calendar.DAY_OF_YEAR) == tomorrow.get(Calendar.DAY_OF_YEAR) -> "Tomorrow"
            else -> SimpleDateFormat("EEE, MMM dd", Locale.getDefault()).format(date)
        }
    }
    private fun findTodayIndex(dates: List<String>): Int {
        val today = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(Date())
        return dates.indexOf(today)
    }
    private fun setupRecyclerView(fixtureList: List<Response>) {
        Log.d("RecyclerViewSetup", "Starting RecyclerView setup")
        Log.d("RecyclerViewSetup", "Fixture list size: ${fixtureList.size}")
        recyclerViewAdapter = RecyclerViewAdapter(fixtureList, this)
        recyclerView.apply {
            layoutManager = LinearLayoutManager(context)
            adapter = recyclerViewAdapter
            Log.d("RecyclerViewSetup", "RecyclerView setup completed")
            Log.d("RecyclerViewSetup", "Number of items in adapter: ${recyclerViewAdapter.itemCount}")
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




}