package com.example.vcp_exam

import android.Manifest
import android.Manifest.permission.READ_MEDIA_IMAGES
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.MediaStore
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.Toast
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.bumptech.glide.Glide
import com.example.vcp_exam.databinding.ActivityMainBinding
import java.io.File

class ActivityMain : AppCompatActivity() {

    private val binding: ActivityMainBinding by lazy { ActivityMainBinding.inflate(layoutInflater) }

    private lateinit var editNum: EditText
    private lateinit var editStart: EditText
    private lateinit var editEnd: EditText
    private lateinit var editAnswer: EditText
    private lateinit var btnSave: Button
    private lateinit var btnImg: Button
    private lateinit var btnStart: Button
    private lateinit var btnStartAll: Button
    private lateinit var img: ImageView
    private lateinit var imgByteArray: ByteArray

    val pickMedia = registerForActivityResult(ActivityResultContracts.PickVisualMedia()) { uri ->
        if (uri != null) {
            Glide.with(this)
                .load(uri)
                .into(img)
            val inputStream = this.contentResolver.openInputStream(uri)
            inputStream?.use { stream ->
                imgByteArray = stream.readBytes()
            }
        }
    }

    private val databaseInfo: ExamDataBase by lazy{ ExamDataBase.getInstance(applicationContext) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        editNum = binding.mainEditNum
        editStart = binding.mainEditStart
        editEnd = binding.mainEditEnd
        editAnswer = binding.mainEditAnswer
        btnSave = binding.mainBtnSave
        btnImg = binding.mainBtnImg
        btnStart = binding.mainBtnStart
        btnStartAll = binding.mainBtnStartAll
        img = binding.mainImg

        btnImg.setOnClickListener {
            when {
                Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU -> {
                    // Android 13 이상
                    if (ContextCompat.checkSelfPermission(this, READ_MEDIA_IMAGES) == PackageManager.PERMISSION_GRANTED) {
                        pickMedia.launch(PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly))
                    } else {
                        requestPermissions(arrayOf(READ_MEDIA_IMAGES), 1000)
                    }
                }
                else -> {
                    // Android 12 이하
                    if (ContextCompat.checkSelfPermission(this,
                            Manifest.permission.READ_EXTERNAL_STORAGE
                        ) == PackageManager.PERMISSION_GRANTED) {
                        pickMedia.launch(PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly))
                    } else {
                        requestPermissions(arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE), 1000)
                    }
                }
            }
        }

        btnSave.setOnClickListener {
            if(editNum.text.isEmpty() || editAnswer.text.isEmpty()){
                Toast.makeText(this, "입력해",Toast.LENGTH_SHORT).show()
            }else{
                databaseInfo.insertData(editNum.text.toString(), imgByteArray, editAnswer.text.toString().uppercase())
                editNum.text.clear()
                editAnswer.text.clear()
                Toast.makeText(this, "저장 완료",Toast.LENGTH_SHORT).show()
            }
        }

        btnStart.setOnClickListener {
            if(editStart.text.isEmpty() || editEnd.text.isEmpty()){
                Toast.makeText(this, "입력해",Toast.LENGTH_SHORT).show()
            }else{
                val intent = Intent(this, ActivityExam::class.java)
                intent.putExtra("type", false)
                intent.putExtra("start", editStart.text.toString())
                intent.putExtra("end", editEnd.text.toString())
                startActivity(intent)
                finish()
            }
        }

        btnStartAll.setOnClickListener {
            val intent = Intent(this, ActivityExam::class.java)
            intent.putExtra("type", true)
            startActivity(intent)
            finish()
        }
    }
}