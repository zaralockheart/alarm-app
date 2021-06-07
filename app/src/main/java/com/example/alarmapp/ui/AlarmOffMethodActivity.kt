package com.example.alarmapp.ui

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import com.example.alarmapp.R
import kotlinx.android.synthetic.main.activity_alarm_off_method.*

class AlarmOffMethodActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_alarm_off_method)

        val resultLauncher =
            registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
                if (result.resultCode == Activity.RESULT_OK && result.data != null) {
                    val shakeCount = result.data?.getIntExtra("shake_count", 0)
                    if (shakeCount != null && shakeCount > 0) {
                        val data = Intent()
                        data.putExtra("method", "shake")
                        data.putExtra("shake_count", shakeCount)
                        setResult(Activity.RESULT_OK, data)
                        finish()
                    }

                }
            }

        buttonGoncang.setOnClickListener {
            val intent = Intent(this, ShakeSetupActivity::class.java)
            resultLauncher.launch(intent)
        }

        buttonEasyMath.setOnClickListener {
            val data = Intent()
            data.putExtra("method", "easyMath")
            setResult(Activity.RESULT_OK, data)
            finish()
        }

        buttonNormal.setOnClickListener {
            val data = Intent()
            data.putExtra("method", "normal")
            setResult(Activity.RESULT_OK, data)
            finish()
        }
    }
}