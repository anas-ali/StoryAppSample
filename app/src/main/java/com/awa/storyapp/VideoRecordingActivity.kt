package com.awa.storyapp

import android.os.Bundle
import android.util.Log
import android.view.Window
import android.widget.VideoView
import androidx.appcompat.app.AppCompatActivity
import com.awa.storyapp.views.MultiProgressBar
import java.io.File

class VideoRecordingActivity  : AppCompatActivity() {

    private lateinit var videoView : VideoView
    private lateinit var progresView: MultiProgressBar
    private lateinit var filePath: String
    private lateinit var files: Array<File>
    private var count = 0

    val data = "/storage/emulated/0/Android/data/com.awa.storyapp/files/Movies/"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        window.requestFeature(Window.FEATURE_NO_TITLE)
        setContentView(R.layout.activity_video)
        initViews()
        readFileContent()
        readVideoPathFromExtras()


    }

    private fun initViews() {
        videoView = findViewById(R.id.VideoView)
        progresView = findViewById(R.id.mpb_main)
    }

    private fun readFileContent() {
        val file = File("/storage/emulated/0/Android/data/com.awa.storyapp/files/Movies")
        files = file.listFiles()
        count = files.size
    }

    private fun readVideoPathFromExtras() {
        intent.extras?.getString("video")?.let {
            filePath = it
            playVideo(it)
        }
    }

    private fun playVideo(path: String) {

        if(count > 0) {
            progresView.countOfProgressSteps = files.size

            videoView.setVideoPath(path);
            videoView.setOnPreparedListener {
                progresView.singleDisplayedTime = (it.duration.toLong() / 1000).toFloat()
                progresView.start()
            }
            videoView.setOnCompletionListener {
                playVideo(files[count].path)
            }
            videoView.start();

            count--
        }
    }
}