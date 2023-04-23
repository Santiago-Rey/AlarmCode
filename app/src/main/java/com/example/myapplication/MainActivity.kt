package com.example.myapplication

import android.hardware.camera2.CameraManager
import android.media.AudioManager
import android.media.MediaPlayer
import android.net.Uri
import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.Spinner
import androidx.appcompat.app.AppCompatActivity
import java.util.*

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val play = findViewById<Button>(R.id.playb)
        val stop = findViewById<Button>(R.id.stopb)
        val selectMusicSpinner = findViewById<Spinner>(R.id.selectMusic)
        val mediaPlayer = MediaPlayer()
        //UpMaxVolume()
        //flashOn()

        val audioResources = mapOf(
            "alarma 1" to R.raw.alarm_one,
            "alarma 2" to R.raw.alarm_two,
            "alarma 3" to R.raw.alarm_three
        )

        val adapter = ArrayAdapter(
            this,
            android.R.layout.simple_spinner_item,
           listOf("Predeterminado")+audioResources.map { it.key })
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        selectMusicSpinner.adapter = adapter
        selectMusicSpinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                parent: AdapterView<*>,
                view: View?,
                position: Int,
                id: Long
            ) {
                if (position != 0) {
                    val audioName = parent.getItemAtPosition(position) as String
                    val audioResource = audioResources[audioName]
                    val audioUri = Uri.parse("android.resource://${packageName}/${audioResource}")
                    mediaPlayer.apply {
                        reset()
                        setDataSource(applicationContext, audioUri)
                        prepare()
                        start()
                    }
                }
            }

            override fun onNothingSelected(parent: AdapterView<*>) {}
        }



        play.setOnClickListener {
            mediaPlayer.setOnCompletionListener {
                // Reiniciamos la reproducción
                mediaPlayer.seekTo(0)
                mediaPlayer.start()
            }
        }

        stop.setOnClickListener {
            mediaPlayer.pause()
        }
    }

    private fun flashOn() {
        var isFlashOn = false

        val cameraManager = getSystemService(CAMERA_SERVICE) as CameraManager
        val cameraId = cameraManager.cameraIdList[0]
        val timer = Timer()

        timer.schedule(object : TimerTask() {
            override fun run() {
                isFlashOn = !isFlashOn
                cameraManager.setTorchMode(cameraId, isFlashOn)
            }
        }, 0, 500)
    }

    private fun UpMaxVolume() {
        val audioManager = getSystemService(AUDIO_SERVICE) as AudioManager
        val maxVolume = audioManager.getStreamMaxVolume(AudioManager.STREAM_MUSIC)

        // Aumentamos el volumen al máximo permitido
        audioManager.setStreamVolume(AudioManager.STREAM_MUSIC, maxVolume, 0)
    }
}