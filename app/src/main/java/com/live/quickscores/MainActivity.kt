package com.live.quickscores

import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import androidx.fragment.app.Fragment
import androidx.viewpager2.widget.ViewPager2
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.AdView
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator
import com.live.quickscores.adapters.ViewPagerAdapter
import com.live.quickscores.databinding.ActivityMainBinding
import com.live.quickscores.fragments.CountriesFragment
import com.live.quickscores.fragments.FixtureFragment
import com.live.quickscores.fragments.LeagueTableFragment
import com.live.quickscores.fragments.LeaguesFixturesFragment
import com.live.quickscores.fragments.LeaguesFragment
import com.live.quickscores.fragments.MatchFragment
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale

class MainActivity : AppCompatActivity(), MatchFragment.OnFixtureClickListener,
    CountriesFragment.OnCountryClicked,LeaguesFragment.OnLeagueClicked,LeaguesFixturesFragment.OnFixtureClickListener {

    private lateinit var adView: AdView
    private lateinit var adRequest: AdRequest
    private lateinit var drawerLayout: DrawerLayout
    private lateinit var toolbar: Toolbar
    private lateinit var binding: ActivityMainBinding
    private lateinit var dates: List<String>
    private lateinit var viewPager: ViewPager2
    private lateinit var tabLayout: TabLayout

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
//        supportActionBar.setBackgroundDrawable(R.color.gray)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        dates = generateDates()

        toolbar = findViewById(R.id.toolbar)
        drawerLayout = findViewById(R.id.drawer_layout)
        adView = findViewById(R.id.BannerAdView)
        setUpToolBar()
        loadBannerAd()

        viewPager = findViewById(R.id.view_pager)
        tabLayout = findViewById(R.id.tab_layout)


        setUpViewPager(viewPager, tabLayout)


        val todayIndex = findTodayIndex(dates)
        viewPager.setCurrentItem(todayIndex, false)
//        setSupportActionBar(toolbar)

//        viewPager.registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback() {
//            override fun onPageSelected(position: Int) {
//
//            }
//        })
        val bottomNavigationView = findViewById<BottomNavigationView>(R.id.bottom_navigation)
        bottomNavigationView.setOnNavigationItemSelectedListener { menuItem ->
            handleBottomNavigationItemSelected(menuItem.itemId)
        }

    }
    private fun handleBottomNavigationItemSelected(itemId: Int): Boolean {
        return when (itemId) {
            R.id.home -> {
                val intent = Intent(this, MainActivity::class.java)
                startActivity(intent)
                true
            }
            R.id.standings -> {
                hideViewPagerAndTabs()
                navigateToFragment(LeagueTableFragment())
                true
            }
            R.id.leagues -> {
                hideViewPagerAndTabs()
                navigateToFragment(LeaguesFragment())
                true
            }
            R.id.countries -> {
                hideViewPagerAndTabs()
                navigateToFragment(CountriesFragment())
                true
            }
            else -> false
        }
    }

    private fun navigateToFragment(fragment: Fragment) {
        supportFragmentManager.beginTransaction()
            .replace(R.id.fragment_container, fragment)
            .commit()
    }

    override fun onCountryClicked(
        countryName: String,
        countryCode:String){
        navigateToLeaguesFragment(countryName,countryCode)
        hideViewPagerAndTabs()
    }
    private fun navigateToLeaguesFragment(countryName: String?,countryCode: String?){
        val existingFragment = supportFragmentManager.findFragmentById(R.id.fragment_container)
        if (existingFragment !is LeaguesFragment){
            val leaguesFragment=LeaguesFragment().apply {
                arguments=Bundle().apply {
                    putString("countryName", countryName)
                    putString("countryCode",countryCode)
                }
                println("${countryName},${countryCode},Malengekubwa")


            }
            hideViewPagerAndTabs()
            supportFragmentManager.beginTransaction()
                .replace(R.id.fragment_container, leaguesFragment)
                .addToBackStack(null)
                .commit()
        }

    }

    override fun onFixtureClicked(
        matchId: String,
        homeTeam: String,
        awayTeam: String,
        homeTeamLogoUrl: String,
        awayTeamLogoUrl: String,
        leagueName:String,
        venue:String?,
        formattedDate:String?,
        city:String?,
        country:String?,
        referee:String?,
        homeTeamGoals:String?,
        awayTeamGoals:String?,
        homeTeamId:String,
        awayTeamId:String,
        leagueId:String,
        season:String


    ) {
        navigateToFixtureFragment(matchId,homeTeam,awayTeam,homeTeamLogoUrl,awayTeamLogoUrl,leagueName,venue!!,formattedDate,city,country,referee, homeTeamGoals ?: "-",awayTeamGoals ?: "-",homeTeamId,awayTeamId,leagueId,season)
        hideViewPagerAndTabs()
    }
    private fun navigateToFixtureFragment(
        matchId: String,
        homeTeam: String,
        awayTeam: String,
        homeTeamLogoUrl: String,
        awayTeamLogoUrl: String,
        leagueName: String,
        venue:String?,
        formattedDate:String?,
        city:String?,
        country:String?,
        referee:String?,
        homeTeamGoals: String?,
        awayTeamGoals: String?,
        homeTeamId: String,
        awayTeamId: String,
        leagueId: String,
        season: String

    ) {
        val existingFragment = supportFragmentManager.findFragmentById(R.id.fragment_container)
        if (existingFragment !is FixtureFragment) {
            val fixtureFragment = FixtureFragment().apply {
                arguments = Bundle().apply {
                    putString("matchId", matchId)
                    putString("homeTeam", homeTeam)
                    putString("awayTeam", awayTeam)
                    putString("homeTeamLogoUrl", homeTeamLogoUrl)
                    putString("awayTeamLogoUrl", awayTeamLogoUrl)
                    putString("leagueName",leagueName)
                    putString("venue",venue)
                    putString("date",formattedDate)
                    putString("city",city)
                    putString("country",country)
                    putString("referee",referee)
                    putString("homeTeamGoals",homeTeamGoals)
                    putString("awayTeamGoals",awayTeamGoals)
                    putString("homeTeamId",homeTeamId)
                    putString("awayTeamId",awayTeamId)
                    putString("leagueId",leagueId)
                    putString("season",season)
                }
            }

            Log.d("match", "arguments passed: ${fixtureFragment.arguments}")
            Log.d("onFixtureClicked", "Home Goals: $homeTeamGoals, Away Goals: $awayTeamGoals")

            hideViewPagerAndTabs()
            supportFragmentManager.beginTransaction()
                .replace(R.id.fragment_container, fixtureFragment)
                .addToBackStack(null)
                .commit()

            Log.d("FragmentTransaction", "Navigating to FixtureFragment with ID: $matchId")
        }
    }
    override fun onLeagueClicked(leagueId: String, season: String, name: String, leagueLogo: String,leagueCountryName:String) {
        navigateToLeaguesFixtureFragment(leagueId, season, name, leagueLogo,leagueCountryName)
        hideViewPagerAndTabs()
    }
    private fun navigateToLeaguesFixtureFragment(leagueId: String, season: String, name: String, leagueLogo: String,leagueCountryName: String) {
        val existingFragment = supportFragmentManager.findFragmentById(R.id.fragment_container)
        if (existingFragment !is LeaguesFixturesFragment) {
            val leaguesFixturesFragment = LeaguesFixturesFragment().apply {
                arguments = Bundle().apply {
                    putString("leagueId", leagueId)
                    putString("season", season)
                    putString("name", name)
                    putString("leagueLogo", leagueLogo)
                    putString("country",leagueCountryName)
                }
                println("${leagueId},MalengeKubwaSana")
                println("${season},MalengeKubwaSana")
                println("${name},MalengeKubwaSana")
                println("${leagueCountryName},MalengeKubwaSana")

            }
            supportFragmentManager.beginTransaction()
                .replace(R.id.fragment_container, leaguesFixturesFragment)
                .addToBackStack(null)
                .commit()
        }
    }

    private fun setUpToolBar() {
        setSupportActionBar(toolbar)
        val toggle = ActionBarDrawerToggle(
            this, drawerLayout, toolbar,
            R.string.navigation_drawer_open,
            R.string.navigation_drawer_close
        ).apply {
            drawerArrowDrawable.color = Color.parseColor("#C0C0C0")
        }

        drawerLayout.addDrawerListener(toggle)
        toggle.syncState()
        toggle.toolbarNavigationClickListener = View.OnClickListener {
            if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
                drawerLayout.closeDrawer(GravityCompat.START)
            } else {
                drawerLayout.openDrawer(GravityCompat.START)
            }
        }
    }


    private fun loadBannerAd() {
        adRequest = AdRequest.Builder().build()
        adView.loadAd(adRequest)
    }

    private fun setUpViewPager(viewPager: ViewPager2, tabLayout: TabLayout) {
        val adapter = ViewPagerAdapter(this, dates)
        viewPager.adapter = adapter
        TabLayoutMediator(tabLayout, viewPager) { tab, position ->
            val date = dates[position]
            tab.text = getTabTitle(date)
        }.attach()
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

    private fun generateDates(): List<String> {
        val dateList = mutableListOf<String>()
        val dateFormat = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
        val calendar = Calendar.getInstance()

        dateList.add(dateFormat.format(calendar.time))

        calendar.set(Calendar.HOUR_OF_DAY, 0)
        calendar.set(Calendar.MINUTE, 0)
        calendar.set(Calendar.SECOND, 0)
        calendar.set(Calendar.MILLISECOND, 0)

        for (i in 1..14) {
            calendar.add(Calendar.DATE, -1)
            dateList.add(0, dateFormat.format(calendar.time))
        }

        calendar.time = Date()

        for (i in 1..15) {
            calendar.add(Calendar.DATE, 1)
            dateList.add(dateFormat.format(calendar.time))
        }

        return dateList
    }

    private fun findTodayIndex(dates: List<String>): Int {
        val today = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(Date())
        return dates.indexOf(today)
    }

    private fun hideViewPagerAndTabs() {
        viewPager.visibility = View.GONE
        tabLayout.visibility = View.GONE
        toolbar.visibility = View.GONE
        Log.d("Murima", "ViewPager and Tabs are now hidden")
    }

    private fun showViewPagerAndTabs() {
        viewPager.visibility = View.VISIBLE
        tabLayout.visibility = View.VISIBLE
        toolbar.visibility = View.VISIBLE
        Log.d("Murima", "ViewPager and Tabs are now visible")
    }


    @Deprecated("This method has been deprecated in favor of using the OnBackPressedDispatcher.")
    override fun onBackPressed() {
        val currentFragment = supportFragmentManager.findFragmentById(R.id.fragment_container)

        if (currentFragment is FixtureFragment) {
            supportFragmentManager.popBackStack()
            showViewPagerAndTabs()
        } else {
            super.onBackPressed()
        }
    }



}
