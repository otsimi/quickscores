package com.live.quickscores

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.ProgressBar
import android.widget.SearchView
import android.widget.TextView
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.live.quickscores.adapters.FavoriteSelectionAdapter
import com.live.quickscores.playersresponse.Player
import com.live.quickscores.repositories.PlayerRepository
import com.live.quickscores.repositories.TeamsRepository
import com.live.quickscores.teamsresponse.Team
import com.live.quickscores.viewmodelclasses.PlayerViewModel
import com.live.quickscores.viewmodelclasses.PlayersViewModelFactory
import com.live.quickscores.viewmodelclasses.Resource
import com.live.quickscores.viewmodelclasses.TeamsViewModel
import com.live.quickscores.viewmodelclasses.TeamsViewModelFactory
import kotlinx.coroutines.launch

class FavoriteSelectionActivity : AppCompatActivity() {
    private val teamsViewModel: TeamsViewModel by viewModels {
        TeamsViewModelFactory(TeamsRepository())
    }
    private val playersViewModel: PlayerViewModel by viewModels {
        PlayersViewModelFactory(PlayerRepository())
    }
    private lateinit var adapter: FavoriteSelectionAdapter
    private lateinit var progressBar: ProgressBar
    private var allTeams: List<Team> = emptyList()
    private var allPlayers: List<Player> = emptyList()
    private enum class TabType { ALL, TEAMS, PLAYERS }
    private var currentTab = TabType.ALL

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_favorite_selection)

        progressBar = findViewById(R.id.progressBar)

        findViewById<TextView>(R.id.skip_favorites).setOnClickListener {
            startActivity(Intent(this, MainActivity::class.java))
            finish()
        }
        setupRecyclerView()
        setupTabClicks()
        setupSearchView()
        observeTeams()
        observePlayers()
        selectAllTab()
    }
    private fun setupRecyclerView() {
        adapter = FavoriteSelectionAdapter { handleFavoriteClick(it) }

        findViewById<RecyclerView>(R.id.favoritesRecyclerView).apply {
            layoutManager = LinearLayoutManager(this@FavoriteSelectionActivity)
            adapter = this@FavoriteSelectionActivity.adapter
        }
    }
    private fun setupTabClicks() {
        findViewById<Button>(R.id.btnAll).setOnClickListener { selectAllTab() }
        findViewById<Button>(R.id.btnTeams).setOnClickListener { selectTeamsTab() }
        findViewById<Button>(R.id.btnPlayers).setOnClickListener { selectPlayersTab() }
    }
    private fun highlightSelectedTab(selectedId: Int) {
        val all = findViewById<Button>(R.id.btnAll)
        val teams = findViewById<Button>(R.id.btnTeams)
        val players = findViewById<Button>(R.id.btnPlayers)

        listOf(all, teams, players).forEach { it.isSelected = false }
        findViewById<Button>(selectedId).isSelected = true
    }
    private fun selectAllTab() {
        currentTab = TabType.ALL
        highlightSelectedTab(R.id.btnAll)

        Log.d("FavoriteSelection", "ALL tab selected")

        teamsViewModel.loadPopularTeams()
        playersViewModel.loadPopularPlayers()
    }
    private fun selectTeamsTab() {
        currentTab = TabType.TEAMS
        highlightSelectedTab(R.id.btnTeams)

        Log.d("FavoriteSelection", "TEAMS tab selected")

        teamsViewModel.loadPopularTeams()
    }
    private fun selectPlayersTab() {
        currentTab = TabType.PLAYERS
        highlightSelectedTab(R.id.btnPlayers)

        Log.d("FavoriteSelection", "PLAYERS tab selected")
        playersViewModel.loadPopularPlayers()
    }

    private fun observeTeams() {
        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                teamsViewModel.teams.collect { resource ->
                    when (resource) {
                        is Resource.Loading -> {
                            if (currentTab != TabType.PLAYERS) showLoading()
                        }
                        is Resource.Success -> {
                            allTeams = resource.data
                            progressBar.visibility = View.GONE
                            when (currentTab) {
                                TabType.TEAMS -> showTeams(allTeams)
                                TabType.ALL -> showCombined()
                                else -> Unit
                            }

                            Log.d("FavoriteSelection", "Teams loaded: ${allTeams.size}")
                        }

                        is Resource.Error -> showError(resource.message)
                    }
                }
            }
        }
    }

    private fun observePlayers() {
        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                playersViewModel.players.collect { resource ->
                    when (resource) {
                        is Resource.Loading -> {
                            if (currentTab != TabType.TEAMS) showLoading()
                        }
                        is Resource.Success -> {
                            allPlayers = resource.data
                            progressBar.visibility = View.GONE

                            when (currentTab) {
                                TabType.PLAYERS -> showPlayers(allPlayers)
                                TabType.ALL -> showCombined()
                                else -> Unit
                            }

                            Log.d("FavoriteSelection", "Players loaded: ${allPlayers.size}")
                        }

                        is Resource.Error -> showError(resource.message)
                    }
                }
            }
        }
    }

    private fun setupSearchView() {
        val searchView = findViewById<androidx.appcompat.widget.SearchView>(R.id.searchView)

        searchView.setOnQueryTextListener(
            object :androidx.appcompat.widget.SearchView.OnQueryTextListener {
                override fun onQueryTextSubmit(query: String?) = true

                override fun onQueryTextChange(newText: String?): Boolean {
                    filterResults(newText.orEmpty())
                    return true
                }
            }
        )
    }
    private fun filterResults(query: String) {
        val q = query.lowercase()

        when (currentTab) {
            TabType.ALL -> {
                val filteredTeams = allTeams.filter { it.name.lowercase().contains(q) }
                val filteredPlayers = allPlayers.filter { it.name.lowercase().contains(q) }
                showCombined(filteredTeams, filteredPlayers)
            }

            TabType.TEAMS -> {
                showTeams(allTeams.filter { it.name.lowercase().contains(q) })
            }

            TabType.PLAYERS -> {
                showPlayers(allPlayers.filter { it.name.lowercase().contains(q) })
            }
        }

        Log.d("FavoriteSelection", "Search='$query' on $currentTab")
    }
    private fun showLoading() {
        progressBar.visibility = View.VISIBLE
    }

    private fun showError(message: String) {
        progressBar.visibility = View.GONE
        Log.e("FavoriteSelection", "Error: $message")
    }

    private fun showTeams(teams: List<Team>) {
        adapter.submitList(teams.map { FavoriteItem.TeamItem(it) })
    }

    private fun showPlayers(players: List<Player>) {
        adapter.submitList(players.map { FavoriteItem.PlayerItem(it) })
    }

    private fun showCombined(
        teams: List<Team> = allTeams,
        players: List<Player> = allPlayers
    ) {
        val items = mutableListOf<FavoriteItem>()
        teams.forEach { items.add(FavoriteItem.TeamItem(it)) }
        players.forEach { items.add(FavoriteItem.PlayerItem(it)) }

        adapter.submitList(items)

        Log.d(
            "FavoriteSelection",
            "Combined list shown: teams=${teams.size}, players=${players.size}"
        )
    }

    private fun handleFavoriteClick(item: FavoriteItem) {
        when (item) {
            is FavoriteItem.TeamItem -> {
                Log.d("FavoriteSelection", "Team clicked: ${item.team.name}")
            }

            is FavoriteItem.PlayerItem -> {
                Log.d("FavoriteSelection", "Player clicked: ${item.player.name}")
            }
        }
    }
}
