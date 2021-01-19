package com.awa.storyapp


import android.Manifest
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.os.Environment
import android.util.Log
import android.view.Window
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.camera.view.CameraView
import androidx.camera.view.video.OnVideoSavedCallback
import androidx.camera.view.video.OutputFileResults
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.hluhovskyi.camerabutton.CameraButton
import java.io.File

class CaptureStoryActivity : AppCompatActivity() {
    private lateinit var cameraView: CameraView

    private lateinit var recordFiles: Array<File>
    private lateinit var storageDirectory: File
    private val permissionsArray = arrayOf(
        Manifest.permission.WRITE_EXTERNAL_STORAGE,
        Manifest.permission.CAMERA,
        Manifest.permission.RECORD_AUDIO
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        window.requestFeature(Window.FEATURE_NO_TITLE)
        setContentView(R.layout.activity_main)
        initViews()
        setStorageDetails()
        checkPermission()
        setVideoButtonListener()
    }

    private fun initViews() {
        cameraView = findViewById(R.id.camera_view)
        recordFiles = ContextCompat.getExternalFilesDirs(this, Environment.DIRECTORY_MOVIES)
    }

    private fun setStorageDetails() {
        storageDirectory = recordFiles[0]
    }

    private fun setVideoButtonListener() {
        (findViewById<CameraButton>(R.id.camera_button)).setOnVideoEventListener(object :
            CameraButton.OnVideoEventListener {
            override fun onFinish() { cameraView.stopRecording() }
            override fun onCancel() {}
            override fun onStart() { recordVideo("${storageDirectory.absoluteFile}/${System.currentTimeMillis()}_video.mp4") }
        })
    }

    private fun checkPermission() {
        if(hasPermissions(this, permissionsArray)) {
            startCameraSession()
        }
        else {
            requestPermission()
        }
    }

    private fun startCameraSession() = with(this) {
        if (ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.CAMERA
            ) != PackageManager.PERMISSION_GRANTED
        ) {

            return
        }
        cameraView.bindToLifecycle(this)
        cameraView.captureMode = CameraView.CaptureMode.VIDEO
    }

    private fun recordVideo(videoRecordingFilePath: String) {
        Log.e("uuu u3", videoRecordingFilePath)
        cameraView.startRecording(
            File(videoRecordingFilePath),
            ContextCompat.getMainExecutor(this),
            object : OnVideoSavedCallback {
                override fun onVideoSaved(outputFileResults: OutputFileResults) {
                    val intent = Intent(this@CaptureStoryActivity, ViewStoriesActivity::class.java)
                    intent.putExtra("video", outputFileResults.savedUri?.getPath())
                    startActivity(intent)
                }
                override fun onError(videoCaptureError: Int, message: String, cause: Throwable?) {
                    Toast.makeText(
                        this@CaptureStoryActivity,
                        "Recording Error $message",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            })
    }

    private fun requestPermission() {
        ActivityCompat.requestPermissions(
            this,
            permissionsArray
            ,
            101
        )
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if(requestCode == 101 && permissions.size == permissionsArray.size) {
            startCameraSession()
        }
        else {
            requestPermission()
        }
    }

    private fun hasPermissions(context: Context, permissions: Array<String>): Boolean {
        for (permission in permissions) {
            if (ActivityCompat.checkSelfPermission(
                    context,
                    permission
                ) != PackageManager.PERMISSION_GRANTED
            ) {
                return false
            }
        }
        return true
    }


}