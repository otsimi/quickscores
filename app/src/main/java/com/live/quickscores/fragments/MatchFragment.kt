package com.live.quickscores.fragments

import android.Manifest
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.annotation.RequiresPermission
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager2.widget.ViewPager2
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator
import com.live.quickscores.LeagueIdSharedViewModel
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


class MatchFragment : Fragment(), RecyclerViewAdapter.OnFixtureClickListener,RecyclerViewAdapter.OnLeagueItemClickListener {

    private lateinit var recyclerViewAdapter: RecyclerViewAdapter
    private lateinit var recyclerView: RecyclerView
    private lateinit var viewPager: ViewPager2
    private lateinit var tabLayout: TabLayout
    private lateinit var dates: List<String>
    private lateinit var sharedViewModel: LeagueIdSharedViewModel
    private var currentPage: Int = 0
    private val viewModel: FixturesViewModel by viewModels {
        FixturesViewModelFactory(FixturesRepository())
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
        createNotificationChannel(requireContext())
        sharedViewModel = ViewModelProvider(requireActivity())[LeagueIdSharedViewModel::class.java]
        recyclerView = view.findViewById(R.id.RecyclerView)
        viewPager = view.findViewById(R.id.viewPager)
        tabLayout = view.findViewById(R.id.tab_layout)
        val todayIndex = findTodayIndex(dates)
        setUpViewPager(viewPager, tabLayout)
        viewPager.post {
            viewPager.setCurrentItem(todayIndex, false)
        }


    }
    fun createNotificationChannel(context: Context) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                "favorites_channel",
                "Favorite Matches",
                NotificationManager.IMPORTANCE_DEFAULT
            ).apply {
                description = "Notifications for favorite matches"
            }

            val notificationManager =
                context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            notificationManager.createNotificationChannel(channel)
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
        Toast.makeText(requireContext(), "Clicked on: ${match.teams.home.name} vs ${match.teams.away.name}", Toast.LENGTH_SHORT).show()
        val args = Bundle().apply {
            putString("matchId", matchId)
            putString("homeTeam", homeTeam)
            putString("awayTeam", awayTeam)
            putString("homeTeamLogoUrl", homeTeamLogoUrl)
            putString("awayTeamLogoUrl", awayTeamLogoUrl)
            putString("leagueName", leagueName)
            putString("venue", venue)
            putString("date", formattedDate ?: "")
            putString("country", country)
            putString("referee", referee)
            putString("city", city)
            putString("homeTeamGoals", homeTeamGoals.toString())
            putString("awayTeamGoals", awayTeamGoals.toString())
            putString("homeTeamId", homeTeamId.toString())
            putString("awayTeamId", awayTeamId.toString())
            putString("leagueId", leagueId.toString())
            putString("season", season.toString())
            putString("fixtureStatus",fixtureStatus)
            putString("matchPeriod",matchPeriod.toString())
        }
        println("${fixtureStatus},${matchPeriod},Malenge live match")
        findNavController().navigate(R.id.action_matchFragment_to_fixtureFragment,args)
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
                val todayDate = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(Date())
                val selectedDate = dates[position]
                if (todayDate==selectedDate){
                    currentPage=position
                    Log.d("Todays position $currentPage","Position equals selected date")
                }
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
                    val sortedFixtures = response.body()?.response
                    if (!sortedFixtures.isNullOrEmpty()){
                        setupRecyclerView(sortedFixtures)
                        println("✅ Sorted countries order: ${sortedFixtures.map { it.league.country }}")
                    } else{
                        Log.e("Match fragment","No fixtures found for this date")
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

        return when {
            calendar.get(Calendar.YEAR) == today.get(Calendar.YEAR) &&
                    calendar.get(Calendar.DAY_OF_YEAR) == today.get(Calendar.DAY_OF_YEAR) -> "Today"
            else -> SimpleDateFormat("EEE, MMM dd", Locale.getDefault()).format(date)
        }
    }
    private fun findTodayIndex(dates: List<String>): Int {
        val today = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(Date())
        return dates.indexOf(today)
    }
    @RequiresPermission(Manifest.permission.POST_NOTIFICATIONS)
    private fun setupRecyclerView(fixtureList: List<Response>) {
        Log.d("RecyclerViewSetup", "Starting RecyclerView setup")
        Log.d("RecyclerViewSetup", "Fixture list size: ${fixtureList.size}")

        recyclerViewAdapter = RecyclerViewAdapter(fixtureList, this, this)

        // 🔔 Handle favorite clicks here
        recyclerViewAdapter.onFavoriteClickListener = { match ->
            sendFavoriteNotification(
                requireContext(),
                "Match Added to Favorites",
                "${match.teams.home.name} vs ${match.teams.away.name} has been added to your favorites!"
            )

            Log.d("Favorites", "Notification sent for ${match.teams.home.name} vs ${match.teams.away.name}")
        }

        recyclerView.apply {
            layoutManager = LinearLayoutManager(context)
            adapter = recyclerViewAdapter
            Log.d("RecyclerViewSetup", "RecyclerView setup completed")
            Log.d("RecyclerViewSetup", "Number of items in adapter: ${recyclerViewAdapter.itemCount}")
        }
    }
    @RequiresPermission(Manifest.permission.POST_NOTIFICATIONS)
    private fun sendFavoriteNotification(context: Context, title: String, message: String) {
        val builder = NotificationCompat.Builder(context, "favorites_channel")
            .setSmallIcon(android.R.drawable.btn_star_big_on)

            .setContentTitle(title)
            .setContentText(message)
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)
            .setAutoCancel(true)

        val notificationManager = NotificationManagerCompat.from(context)
        notificationManager.notify(System.currentTimeMillis().toInt(), builder.build())
        Toast.makeText(context, "Notification sent: $title", Toast.LENGTH_SHORT).show()
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

    override fun onPause() {
        super.onPause()
        currentPage=viewPager.currentItem
    }

    override fun onResume() {
        super.onResume()
        if (currentPage != 0) {
            viewPager.setCurrentItem(currentPage, false)
            Log.d("ViewPager", "Restored to today's position: $currentPage")
        }
    }

    override fun onLeagueClick(leagueId: Int, leagueName: String, country: String,leagueLogo:String,season:Int) {
        println("leagueClicked,$leagueId")
        sharedViewModel.setSelectedLeagueId(leagueId.toString())
        Toast.makeText(requireContext(),"Clicked on $leagueName",Toast.LENGTH_SHORT).show()
        val args=Bundle().apply {
            putString("leagueId",leagueId.toString())
            putString("leagueName",leagueName)
            putString("season",season.toString())
            putString("leagueLogo",leagueLogo)
            putString("leagueCountryName",country)
        }
        println("${leagueName},Malenge")
        findNavController().navigate(R.id.action_matchFragment_to_leaguesFixturesFragment,args)
    }


}