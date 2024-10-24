package com.live.quickscores

import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.drawerlayout.widget.DrawerLayout
import androidx.viewpager2.widget.ViewPager2
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.AdView
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator
import com.live.quickscores.databinding.ActivityMainBinding
import com.live.quickscores.fragments.FixtureFragment
import com.live.quickscores.fragments.MatchFragment
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale

class MainActivity : AppCompatActivity(), MatchFragment.OnFixtureClickListener {

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
        setSupportActionBar(toolbar)

        viewPager.registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback() {
            override fun onPageSelected(position: Int) {

            }
        })
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

    ) {
        navigateToFixtureFragment(matchId,homeTeam,awayTeam,homeTeamLogoUrl,awayTeamLogoUrl,leagueName,venue,formattedDate,city,country,referee)
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
                }
            }

            Log.d("match", "arguments passed: ${fixtureFragment.arguments}")

            hideViewPagerAndTabs()
            supportFragmentManager.beginTransaction()
                .replace(R.id.fragment_container, fixtureFragment)
                .addToBackStack(null)
                .commit()

            Log.d("FragmentTransaction", "Navigating to FixtureFragment with ID: $matchId")
        }
    }



    private fun setUpToolBar() {
        setSupportActionBar(toolbar)
        val toggle = ActionBarDrawerToggle(
            this, drawerLayout, toolbar,
            R.string.navigation_drawer_open,
            R.string.navigation_drawer_close
        )
        drawerLayout.addDrawerListener(toggle)
        toggle.syncState()
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

    fun hideViewPagerAndTabs() {
        viewPager.visibility = View.GONE
        tabLayout.visibility = View.GONE
        toolbar.visibility = View.GONE
        Log.d("Murima", "ViewPager and Tabs are now hidden")
    }

    fun showViewPagerAndTabs() {
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
