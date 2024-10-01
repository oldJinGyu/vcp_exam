package com.example.vcp_exam

import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper

class ExamDataBase private constructor(context: Context) : SQLiteOpenHelper(context.applicationContext, DATABASE_NAME, null, DATABASE_VERSION) {

    companion object {
        private const val DATABASE_VERSION = 1
        private const val DATABASE_NAME = "exam.db"
        private const val TABLE_NAME = "exam_info"
        private const val COLUMN_NUM = "number"
        private const val COLUMN_IMG = "image"
        private const val COLUMN_ANSWER = "answer"

        @Volatile
        private var instance: ExamDataBase?= null

        fun getInstance(context: Context)=
            instance ?: synchronized(ExamDataBase::class.java){
                instance ?: ExamDataBase(context).also{
                    instance =it
                }
            }
    }

    override fun onCreate(db: SQLiteDatabase) {
        val createTableQuery = "CREATE TABLE $TABLE_NAME ($COLUMN_NUM TEXT PRIMARY KEY, $COLUMN_IMG BLOB, $COLUMN_ANSWER TEXT)"
        db.execSQL(createTableQuery)
    }
    //거리는 m단위 날짜는 xxxx.xx.xx 시간은 초단위

    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {
        // 데이터베이스 업그레이드 처리
    }

    fun insertData(num: String, img: ByteArray, answer: String) {
        val db = this.writableDatabase
        val contentValues = ContentValues().apply{
            put(COLUMN_NUM, num)
            put(COLUMN_IMG, img)
            put(COLUMN_ANSWER, answer)
        }
        db.insert(TABLE_NAME, null, contentValues)
        db.close()
    }

    fun getAll(): MutableList<ExamInfo> {
        val db = this.readableDatabase
        val cursor = db.rawQuery("SELECT * FROM $TABLE_NAME", null)

        var examInfo: MutableList<ExamInfo> = mutableListOf()

        cursor.use {
            while (it.moveToNext()) {
                val num = it.getString(0)
                val img = it.getBlob(1)
                val answer = it.getString(2)
                examInfo.add(ExamInfo(num,img,answer))
            }
        }
        db.close()
        return examInfo
    }
}