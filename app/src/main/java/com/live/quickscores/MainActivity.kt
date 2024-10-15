import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.drawerlayout.widget.DrawerLayout
import androidx.viewpager2.widget.ViewPager2
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.AdView
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator
import com.live.quickscores.R
import com.live.quickscores.ViewPagerAdapter
import com.live.quickscores.databinding.ActivityMainBinding
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale

class MainActivity : AppCompatActivity() {
    private lateinit var adView: AdView
    private lateinit var adRequest: AdRequest
    private lateinit var drawerLayout: DrawerLayout
    private lateinit var toolbar: Toolbar
    private lateinit var binding: ActivityMainBinding
    private lateinit var dates: List<String>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        dates = generateDates(1000)
        Log.d("MainActivity", "Generated Dates: $dates")

        toolbar = findViewById(R.id.toolbar)
        drawerLayout = findViewById(R.id.drawer_layout)



        setUpToolBar()
        initializeViews()
        loadBannerAd()

        val viewPager = findViewById<ViewPager2>(R.id.view_pager)
        val tabLayout = findViewById<TabLayout>(R.id.tab_layout)

        setUpViewPager(viewPager, tabLayout)

        setSupportActionBar(toolbar)
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

    private fun initializeViews() {
        adView = findViewById(R.id.BannerAdView)
    }

    private fun loadBannerAd() {
        adRequest = AdRequest.Builder().build()
        adView.loadAd(adRequest)
    }

    private fun setUpViewPager(viewPager: ViewPager2, tabLayout: TabLayout) {
        val adapter = ViewPagerAdapter(this, dates)
        viewPager.adapter = adapter

        TabLayoutMediator(tabLayout, viewPager) { tab, position ->
            tab.text = getTabTitle(dates[position])
        }.attach()
    }

    private fun getTabTitle(dateString: String): String {
        val dateFormat = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
        val date = dateFormat.parse(dateString)

        if (date == null) {
            Log.e("MainActivity", "Date parsing failed for: $dateString")
            return dateString
        }
        val calendar = Calendar.getInstance()
        val today = Calendar.getInstance()

        val yesterday = Calendar.getInstance().apply {
            add(Calendar.DATE, -1)
        }

        val tomorrow = Calendar.getInstance().apply {
            add(Calendar.DATE, 1)
        }

        return when {
            today.get(Calendar.YEAR) == date.year + 1900 &&
                    calendar.get(Calendar.DAY_OF_YEAR) == date.day + 1 -> "Today"

            yesterday.get(Calendar.YEAR) == date.year + 1900 &&
                    yesterday.get(Calendar.DAY_OF_YEAR) == date.day + 1 -> "Yesterday"

            tomorrow.get(Calendar.YEAR) == date.year + 1900 &&
                    tomorrow.get(Calendar.DAY_OF_YEAR) == date.day + 1 -> "Tomorrow"

            else -> SimpleDateFormat("MMM dd", Locale.getDefault()).format(date)
        }
    }

    private fun generateDates(totalDays: Int): List<String> {
        val dateList = mutableListOf<String>()
        val dateFormat = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
        val calendar = Calendar.getInstance()

        for (i in (totalDays / 2) downTo 1) {
            calendar.add(Calendar.DATE, -1)
            dateList.add(dateFormat.format(calendar.time))
        }

        calendar.time = Date()
        dateList.add(dateFormat.format(calendar.time))

        for (i in 1..(totalDays / 2)) {
            calendar.add(Calendar.DATE, 1)
            dateList.add(dateFormat.format(calendar.time))
        }

        return dateList
    }
}
