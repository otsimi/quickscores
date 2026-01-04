package com.live.quickscores

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity


class WelcomeActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_welcome)
        findViewById<Button>(R.id.getStartedBtn).setOnClickListener {
            startActivity(Intent(this, FavoriteSelectionActivity::class.java))
            finish()
        }
    }
}