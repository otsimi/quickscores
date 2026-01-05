package com.live.quickscores

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.live.quickscores.repositories.PlayerRepository
import com.live.quickscores.repositories.TeamsRepository
import com.live.quickscores.viewmodelclasses.PlayerViewModel
import com.live.quickscores.viewmodelclasses.PlayersViewModelFactory
import com.live.quickscores.viewmodelclasses.Resource
import com.live.quickscores.viewmodelclasses.TeamsViewModel
import com.live.quickscores.viewmodelclasses.TeamsViewModelFactory

class FavoriteSelectionActivity : AppCompatActivity() {
    private val viewModel: TeamsViewModel by viewModels{
        TeamsViewModelFactory(TeamsRepository())
    }
    private val playersViewModel: PlayerViewModel by viewModels {
        PlayersViewModelFactory(PlayerRepository())
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_favorite_selection)
        findViewById<TextView>(R.id.skip_favorites).setOnClickListener {
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
            finish()
        }

        findViewById<Button>(R.id.btnTeams).setOnClickListener {
            viewModel.loadPopularTeams()
        }
        findViewById<Button>(R.id.btnPlayers).setOnClickListener {
            playersViewModel.loadPopularPlayers()
        }
        findViewById<Button>(R.id.btnAll).setOnClickListener {
            println("button all clicked")
        }

    }

}