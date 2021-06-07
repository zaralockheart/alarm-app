package com.example.alarmapp.ui

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.SpannableStringBuilder
import android.text.TextWatcher
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.ui.AppBarConfiguration
import com.example.alarmapp.databinding.ActivityShakeSetupBinding
import com.google.android.material.dialog.MaterialAlertDialogBuilder

class ShakeSetupActivity : AppCompatActivity(), TextWatcher {

    private lateinit var appBarConfiguration: AppBarConfiguration
    private lateinit var binding: ActivityShakeSetupBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityShakeSetupBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.textInputTimes.addTextChangedListener(this)
        binding.buttonOk.setOnClickListener {
            if (binding.textInputTimes.text.isNullOrEmpty() || binding.textInputTimes.text.toString() == "0") {
                MaterialAlertDialogBuilder(this)
                    .setMessage("Tetapkan goncangan lebih dari 0")
                    .setPositiveButton("OK") { dialog, which ->
                        dialog.dismiss()
                    }
                    .show()
                return@setOnClickListener
            }
            val data = Intent()
            data.putExtra("shake_count", binding.textInputTimes.text.toString().toInt())

            setResult(Activity.RESULT_OK, data)
            finish()
        }

        binding.buttonCancel.setOnClickListener { finish() }
    }

    override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
    }

    override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
    }

    override fun afterTextChanged(p0: Editable?) {
        if ((p0?.length ?: 0) > 0) {
            binding.textInputTimesInformer.text = SpannableStringBuilder(
                "Goncang ${
                    binding.textInputTimes.text.toString().toInt()
                } kali"
            )
        }
    }
}