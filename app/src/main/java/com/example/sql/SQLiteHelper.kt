package com.example.sql

import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import android.widget.Toast
import java.lang.Exception

class SQLiteHelper(context : Context) : SQLiteOpenHelper(context ,DATABASE_NAME , null , DATABASE_VERSION) {

    companion object{
        private val DATABASE_VERSION = 1
        private val DATABASE_NAME = "student.db"
        private val TABLE_NAME = "tbl_student"
        private val ID = "id"
        private val NAME = "name"
        private val EMAIL = "email"
        private val ROLLNO = "roll"
    }

    override fun onCreate(db: SQLiteDatabase?) {
        val table = "CREATE TABLE " + TABLE_NAME +
                " (" + ID + " INTEGER PRIMARY KEY, " +
                NAME + " TEXT, " +
                EMAIL + " TEXT, " +
                ROLLNO + " TEXT );";
        db?.execSQL(table)
    }

    override fun onUpgrade(db: SQLiteDatabase?, p1: Int, p2: Int) {
        db!!.execSQL("DROP TABLE IF EXISTS $TABLE_NAME")
        onCreate(db)
    }

    fun insertStudent(std:StudentModel): Long{
        val db = this.writableDatabase

        val cv = ContentValues()
        cv.put(ID,std.id)
        cv.put(NAME,std.name)
        cv.put(EMAIL,std.email)
        cv.put(ROLLNO,std.roll)

        val sucess = db.insert(TABLE_NAME,null,cv)
        db.close()
        return sucess
    }

    fun getAllStudents() : ArrayList<StudentModel>{
        val stdList : ArrayList<StudentModel> = ArrayList()
        val query = "SELECT * FROM $TABLE_NAME"
        val db = this.readableDatabase

        var cursor : Cursor? = null

        try {
            cursor = db.rawQuery(query, null)
        }catch (e : Exception) {
            e.printStackTrace()
            db.execSQL(query)
            return ArrayList()
        }

        var id : Int
        var name : String
        var email : String
        var roll : String

        if(cursor.moveToFirst()){
            do{
                id = cursor.getInt(cursor.getColumnIndex("id"))
                name = cursor.getString(cursor.getColumnIndex("name"))
                email = cursor.getString(cursor.getColumnIndex("email"))
                roll = cursor.getString(cursor.getColumnIndex("roll"))

                val std = StudentModel(id = id , name = name,email = email,roll = roll)
                stdList.add(std)
            }while (cursor.moveToNext())
        }
        return stdList
    }

    fun updateStudent(std : StudentModel) : Int {
        val db = this.writableDatabase
        val cv = ContentValues()

        cv.put(ID,std.id)
        cv.put(NAME,std.name)
        cv.put(EMAIL,std.email)
        cv.put(ROLLNO,std.roll)

        val success = db.update(TABLE_NAME,cv,"id=" + std.id,null)
        db.close()
        return success
    }

    fun deleteStudentById(id:Int) : Int {
        val db = this.writableDatabase
        val cv = ContentValues()

        cv.put(ID,id)

        val sucess = db.delete(TABLE_NAME, "id=$id",null)
        db.close()
        return sucess
    }

}