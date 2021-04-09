package com.example.photoweatherapp.utils

import android.Manifest
import android.annotation.SuppressLint
import androidx.fragment.app.Fragment
import com.example.photoweatherapp.R
import com.gun0912.tedpermission.PermissionListener
import com.gun0912.tedpermission.TedPermission


public object PermissionsManager {

    val CAMERA = Manifest.permission.CAMERA
    val READ_EXTERNAL_STORAGE = Manifest.permission.READ_EXTERNAL_STORAGE
    val WRITE_EXTERNAL_STORAGE = Manifest.permission.WRITE_EXTERNAL_STORAGE
    val COARSE_LOCATION = Manifest.permission.ACCESS_COARSE_LOCATION
    @SuppressLint("CheckResult")
    fun requestPermission(fragment: Fragment,
                          vararg permissions: String?,
                          onDenied: (() -> Unit)? = null,
                          onGranted: (() -> Unit)? = null) {

        TedPermission.with(fragment.context)
            .setPermissionListener(object : PermissionListener {
                override fun onPermissionGranted() {
                    onGranted?.invoke()
                }

                override fun onPermissionDenied(deniedPermissions: MutableList<String>?) {
                    onDenied?.invoke()
                }

            })
            .setPermissions(*permissions)
            .check()

    }
}

