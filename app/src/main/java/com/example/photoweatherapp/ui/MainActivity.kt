package com.example.photoweatherapp.ui

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.android.material.snackbar.Snackbar
import androidx.appcompat.app.AppCompatActivity
import com.example.photoweatherapp.R
import com.example.photoweatherapp.utils.LocationUtils

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setSupportActionBar(findViewById(R.id.toolbar))
    }


    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {

        if (requestCode == LocationUtils.REQUEST_CODE) {
            when (resultCode) {
                Activity.RESULT_OK -> LocationUtils.instance?.checkPermissionAndStartTrack()
                else -> LocationUtils.instance?.onFailed?.invoke()
            }
        } else {
            super.onActivityResult(requestCode, resultCode, data)
        }
    }
}