package com.live.quickscores

import com.live.quickscores.fragments.LeagueFixturesContentFragment
import android.graphics.Color
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.findNavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import androidx.viewpager2.widget.ViewPager2
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.AdView
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator
import com.live.quickscores.adapters.ViewPagerAdapter
import com.live.quickscores.databinding.ActivityMainBinding
import com.live.quickscores.fragments.AllLeaguesFragment
import com.live.quickscores.fragments.CountriesFragment
import com.live.quickscores.fragments.FixtureFragment
import com.live.quickscores.fragments.LeaguesFixturesFragment
import com.live.quickscores.fragments.LeaguesFragment
import com.live.quickscores.fragments.MatchFragment
import com.live.quickscores.fragments.ResultsFragment
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale

class MainActivity : AppCompatActivity(){

    private lateinit var adView: AdView
    private lateinit var adRequest: AdRequest
    private lateinit var drawerLayout: DrawerLayout
    private lateinit var toolbar: Toolbar
    private lateinit var binding: ActivityMainBinding
    private lateinit var sharedViewModel:LeagueIdSharedViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        sharedViewModel = ViewModelProvider(this)[LeagueIdSharedViewModel::class.java]
        toolbar = findViewById(R.id.toolbar)
        setSupportActionBar(toolbar)

        val navHostFragment = supportFragmentManager.findFragmentById(R.id.nav_host_fragment) as NavHostFragment
        val navController = navHostFragment.navController

        val appBarConfiguration = AppBarConfiguration(setOf(
            R.id.matchFragment,
            R.id.countriesFragment,
            R.id.leagueTableFragment,
            R.id.allLeaguesFragment,
            R.id.leaguesFragment
        ))
        setupActionBarWithNavController(navController, appBarConfiguration)

        drawerLayout = findViewById(R.id.drawer_layout)
        adView = findViewById(R.id.BannerAdView)
        setUpToolBar()
        loadBannerAd()
        val bottomNavigationView = findViewById<BottomNavigationView>(R.id.bottom_navigation)

        bottomNavigationView.setupWithNavController(navController)
        navController.addOnDestinationChangedListener { _, destination, _ ->
            when (destination.id) {
                R.id.fixtureFragment -> binding.bottomNavigation.visibility = View.GONE
                else -> binding.bottomNavigation.visibility = View.VISIBLE
            }
        }
    }

    override fun onSupportNavigateUp(): Boolean {
        val navController = findNavController(R.id.nav_host_fragment)
        return navController.navigateUp() || super.onSupportNavigateUp()
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
    override fun onPause() {
        super.onPause()
        adView.pause()
    }
    override fun onResume() {
        super.onResume()
        adView.resume()
    }
    override fun onDestroy() {
        adView.destroy()
        super.onDestroy()
    }

}