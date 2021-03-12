package com.example.gameoflife

import android.os.Bundle
import android.os.Handler
import android.widget.Button
import android.widget.LinearLayout
import androidx.appcompat.app.AppCompatActivity
import java.util.*

@Suppress("DEPRECATION")
class PixelGridActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.pixel_grid)

        lateinit var timer: Timer
        val cellMap: LinearLayout = findViewById(R.id.pixel_grid)
        val clearButton: Button = findViewById(R.id.clear_button)
        val playButton: Button = findViewById(R.id.play_button)
        val randomButton: Button = findViewById(R.id.random_button)
        val stopButton: Button = findViewById(R.id.stop_button)
        val pixelGrid = PixelGrid(this)

        cellMap.addView(pixelGrid)
        clearButton.setOnClickListener { pixelGrid.clearGrid() }
        randomButton.setOnClickListener { pixelGrid.generateRandom() }
        playButton.setOnClickListener {
            playButton.isEnabled = false
            stopButton.isEnabled = true
            val handler = Handler()
            timer = Timer()
            val timerTask = object : TimerTask() {
                override fun run() {
                    handler.post {
                        pixelGrid.gameOfLifeRules()
                    }
                }
            }
            timer.schedule(timerTask, 0, 100)
        }

        stopButton.setOnClickListener {
            stopButton.isEnabled = false
            playButton.isEnabled = true
            timer.cancel()
        }
    }
}