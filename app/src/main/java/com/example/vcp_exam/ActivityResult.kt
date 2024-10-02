package com.example.vcp_exam

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import com.example.vcp_exam.databinding.ActivityResultBinding

class ActivityResult : AppCompatActivity() {

    private val binding: ActivityResultBinding by lazy { ActivityResultBinding.inflate(layoutInflater) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        val total = intent.getStringExtra("total")!!.toInt()
        val point = intent.getStringExtra("point")!!.toInt()

        binding.resultText.text = "$point/$total  점수: ${(point*100/total)}"
        binding.resultBtn.setOnClickListener {
            startActivity(Intent(this, ActivityMain::class.java))
            finish()
        }
    }
}