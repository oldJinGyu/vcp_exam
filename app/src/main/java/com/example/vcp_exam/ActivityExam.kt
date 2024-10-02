package com.example.vcp_exam

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.example.vcp_exam.databinding.ActivityExamBinding
import org.w3c.dom.Text
import kotlin.random.Random

class ActivityExam : AppCompatActivity() {

    private val binding: ActivityExamBinding by lazy { ActivityExamBinding.inflate(layoutInflater) }

    private val databaseInfo: ExamDataBase by lazy{ ExamDataBase.getInstance(applicationContext) }
    private lateinit var exam: MutableList<ExamInfo>
    private lateinit var randomExam: MutableList<ExamInfo>
    private lateinit var textNum: TextView
    private lateinit var textAnswer: TextView
    private lateinit var img: ImageView
    private lateinit var btnNext: Button
    private lateinit var btnNext2: Button
    private lateinit var btnEnd: Button
    private lateinit var editAnswer: EditText
    private lateinit var textNumber: TextView
    private var showNum = false
    private var point = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        textNumber = binding.examTextNumber
        textNum = binding.examTextNum
        textAnswer = binding.examTextAnswer
        img = binding.examImg
        btnNext = binding.examBtnNext
        btnNext2 = binding.examBtnNext2
        btnEnd = binding.examBtnEnd
        editAnswer = binding.examEditAnswer

        val seed = System.currentTimeMillis()
        val random = Random(seed)

        if(intent.getBooleanExtra("type", false) == true){
            exam = databaseInfo.getAll()
            randomExam = exam.shuffled(random).toMutableList()
        }else {
            val start = intent.getStringExtra("start")!!.toInt()
            val end = intent.getStringExtra("end")!!.toInt()
            exam = databaseInfo.getAll().subList(start-1, end)
            randomExam = exam.shuffled(random).toMutableList()
        }

        var start = 0
        val total = randomExam.size
        var exam = randomExam[start]
        textNumber.text = exam.num

        textNum.text = "${start+1}/$total"
        Glide.with(this)
            .load(exam.img)
            .into(img)

        btnNext.setOnClickListener {
            if(editAnswer.text.toString().uppercase() == exam.answer){
                editAnswer.text.clear()
                point+=1
                start+=1
                if(start == total){
                    btnNext.visibility = View.GONE
                    btnNext2.visibility = View.GONE
                    btnEnd.visibility = View.VISIBLE
                }else{
                    textNum.text = "${start+1}/$total"
                    exam = randomExam[start]
                    textNumber.text = exam.num
                    Glide.with(this)
                        .load(exam.img)
                        .into(img)
                }
            }else{
                editAnswer.text.clear()
                start+=1
                textAnswer.text = "정답: ${exam.answer}"
                textAnswer.visibility = View.VISIBLE
                btnNext.visibility = View.GONE
                btnNext2.visibility = View.VISIBLE
                if(start == total){
                    btnNext.visibility = View.GONE
                    btnNext2.visibility = View.GONE
                    btnEnd.visibility = View.VISIBLE
                }
            }
        }

        btnNext2.setOnClickListener {
            textNum.text = "${start+1}/$total"
            exam = randomExam[start]
            textNumber.text = exam.num
            Glide.with(this)
                .load(exam.img)
                .into(img)
            btnNext2.visibility = View.GONE
            btnNext.visibility = View.VISIBLE
            textAnswer.visibility = View.INVISIBLE
        }

        btnEnd.setOnClickListener {
            val intent = Intent(this, ActivityResult::class.java)
            intent.putExtra("point", point.toString())
            intent.putExtra("total", total.toString())
            startActivity(intent)
            finish()
        }

        binding.examBtnNum.setOnClickListener {
            if(showNum){
                showNum = false
                textNumber.visibility = View.INVISIBLE
            }else{
                showNum = true
                textNumber.visibility = View.VISIBLE
            }
        }
    }
}