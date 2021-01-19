package com.awa.storyapp

import android.os.Bundle
import android.view.Window
import android.widget.VideoView
import androidx.appcompat.app.AppCompatActivity
import com.awa.storyapp.views.MultiProgressBar

class VideoRecordingActivity  : AppCompatActivity() {

    private lateinit var videoView : VideoView
    private lateinit var progresView: MultiProgressBar
    private lateinit var filePath: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        window.requestFeature(Window.FEATURE_NO_TITLE)
        setContentView(R.layout.activity_video)
        initViews()
        readVideoPathFromExtras()
    }

    private fun initViews() {
        videoView = findViewById(R.id.VideoView)
        progresView = findViewById(R.id.mpb_main)
    }

    private fun readVideoPathFromExtras() {
        intent.extras?.getString("video")?.let {
            filePath = it
            playVideo(it)
        }
    }

    private fun playVideo(path: String) {
        videoView.setVideoPath(path);
        videoView.setOnPreparedListener {
            progresView.singleDisplayedTime = (it.duration.toLong()/1000).toFloat()
            progresView.start()
        }
        videoView.start();
    }
}