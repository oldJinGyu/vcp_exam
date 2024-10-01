package com.example.vcp_exam

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.vcp_exam.databinding.ActivityExamBinding

class ActivityExam : AppCompatActivity() {

    private val binding: ActivityExamBinding by lazy { ActivityExamBinding.inflate(layoutInflater) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

    }
}