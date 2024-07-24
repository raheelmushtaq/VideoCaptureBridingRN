package com.videocapturebridingapp.videoCapture

import androidx.core.content.ContextCompat
import com.facebook.react.bridge.ActivityEventListener
import com.facebook.react.bridge.Promise
import com.facebook.react.bridge.ReactApplicationContext
import com.facebook.react.bridge.ReactContextBaseJavaModule
import com.facebook.react.bridge.ReactMethod

import android.Manifest
import android.app.Activity
import android.content.ContentValues
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.provider.MediaStore
import androidx.core.app.ActivityCompat

class VideoCapturingModule(reactContext: ReactApplicationContext) :
    ReactContextBaseJavaModule(reactContext), ActivityEventListener {

    private val VIDEO_CAPTURE_REQUEST_CODE = 1
    private var promise: Promise? = null

    init {
        reactContext.addActivityEventListener(this)
    }

    override fun getName(): String {
        return "VideoCaptureModule"
    }

    @ReactMethod
    fun captureVideo(promise: Promise) {
        this.promise = promise

        val currentActivity = currentActivity

        if (currentActivity == null) {
            promise.reject("Activity doesn't exist")
            return
        }

        if (ContextCompat.checkSelfPermission(currentActivity, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED ||
            ContextCompat.checkSelfPermission(currentActivity, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(currentActivity, arrayOf(Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE), VIDEO_CAPTURE_REQUEST_CODE)
            return
        }

        startVideoCapture()
    }

    private fun startVideoCapture() {
        val currentActivity = currentActivity ?: return

        val videoIntent = Intent(MediaStore.ACTION_VIDEO_CAPTURE)
        videoIntent.putExtra(MediaStore.EXTRA_DURATION_LIMIT, 20)
        currentActivity.startActivityForResult(videoIntent, VIDEO_CAPTURE_REQUEST_CODE)
    }

    override fun onActivityResult(activity: Activity?, requestCode: Int, resultCode: Int, data: Intent?) {
        if (requestCode == VIDEO_CAPTURE_REQUEST_CODE) {
            if (resultCode == Activity.RESULT_OK) {
                val videoUri: Uri? = data?.data
                videoUri?.let {
                    saveVideoToGallery(it)
                    promise?.resolve(it.toString())
                } ?: promise?.reject("Video capture failed")
            } else {
                promise?.reject("Video capture failed")
            }
        }
    }

    private fun saveVideoToGallery(uri: Uri) {
        val currentActivity = currentActivity ?: return

        val contentValues = ContentValues().apply {
            put(MediaStore.MediaColumns.DISPLAY_NAME, "video_" + System.currentTimeMillis())
            put(MediaStore.MediaColumns.MIME_TYPE, "video/mp4")
            put(MediaStore.Video.Media.RELATIVE_PATH, "Movies/")
        }

        val resolver = currentActivity.contentResolver
        resolver.insert(MediaStore.Video.Media.EXTERNAL_CONTENT_URI, contentValues)?.let { newUri ->
            resolver.openInputStream(uri)?.use { inputStream ->
                resolver.openOutputStream(newUri)?.use { outputStream ->
                    inputStream.copyTo(outputStream)
                }
            }
        }
    }

    override fun onNewIntent(intent: Intent?) {
        // Not needed for this example
    }
}