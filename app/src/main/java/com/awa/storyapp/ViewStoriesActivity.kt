package com.awa.storyapp

import android.os.Bundle
import android.util.Log
import android.view.Window
import android.widget.VideoView
import androidx.appcompat.app.AppCompatActivity
import com.awa.storyapp.views.MultiProgressBar
import java.io.File

class ViewStoriesActivity : AppCompatActivity() {

    private lateinit var videoView: VideoView
    private lateinit var progresView: MultiProgressBar
    private lateinit var files: Array<File>
    private var count = 0
    private var videoCount = 0


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        window.requestFeature(Window.FEATURE_NO_TITLE)
        setContentView(R.layout.activity_video)
        initViews()
        readFileContent()
    }

    private fun initViews() {
        videoView = findViewById(R.id.VideoView)
        progresView = findViewById(R.id.mpb_main)
    }

    private fun readFileContent() {
        val file = File("/storage/emulated/0/Android/data/com.awa.storyapp/files/Movies")
        files = file.listFiles()
        Log.e("uuu 39", files.toString())
        count = files.size
        playVideo(files[videoCount++].path)
    }

    private fun playVideo(path: String) {
        progresView.countOfProgressSteps = files.size
        videoView.setVideoPath(path)
        videoView.setOnPreparedListener {
            val videoLength = (it.duration.toLong() / 1000).toFloat()
            Log.e("uuu 46", " " + videoLength)
            progresView.singleDisplayedTime = videoLength
            progresView.start()
        }
        videoView.setOnCompletionListener {
            if (videoCount < count)
                playVideo(files[videoCount++].path)
        }
        videoView.start();
    }
}