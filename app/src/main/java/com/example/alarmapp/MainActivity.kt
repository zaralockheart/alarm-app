package com.example.alarmapp

import android.content.Intent
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.Settings
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.findNavController
import androidx.navigation.ui.setupWithNavController
import com.example.alarmapp.databinding.ActivityMainBinding

/**
 * Application entry point.
 */
class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        checkAndroidQOverlayPermission()
        val navController = findNavController(R.id.nav_host_fragment)
        binding.navView.setupWithNavController(navController)
    }

    /**
     * Required for Android Q and above to launch activity from background.
     *
     * reference:
     * https://www.androidpolice.com/2020/02/19/granting-overlay-permissions-in-android-11-takes-one-more-tap/
     * https://developer.android.com/guide/components/activities/background-starts
     */
    private fun checkAndroidQOverlayPermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q &&
            !Settings.canDrawOverlays(this)
        ) {
            requestPermission()
        }
    }

    /**
     * Request runtime permission. Runtime permission start from Android M
     *
     * Reference:
     * https://developer.android.com/guide/topics/permissions/overview#runtime
     * https://developer.android.com/training/permissions/usage-notes#version_specific_details_permissions_in_m
     */
    private fun requestPermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            val intent = Intent(
                Settings.ACTION_MANAGE_OVERLAY_PERMISSION,
                Uri.parse("package:$packageName")
            )
            val resultLauncher =
                registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {}
            resultLauncher.launch(intent)
        }
    }
}