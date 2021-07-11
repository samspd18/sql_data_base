package com.example.sql

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {


    private var adapter : StudentAdapter? = null
    private lateinit var sqLiteHelper: SQLiteHelper
    private var stdList : ArrayList<StudentModel> = ArrayList()

    private var std : StudentModel? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        sqLiteHelper = SQLiteHelper(this)
        initRecyclerView()

        add.setOnClickListener { addStudent()
            stdList = sqLiteHelper.getAllStudents()
            adapter?.addItems(stdList)}
        view.setOnClickListener {
            getStudents()
        }
        update.setOnClickListener {
            updateStudent()
        }

        adapter?.setOnClickItems {
            Toast.makeText(this, it.name, Toast.LENGTH_SHORT).show()
            //update
            etName.setText(it.name)
            etEmail.setText(it.email)
            etRoll.setText(it.roll)
            std = it
        }

        adapter?.setOnClickDeleteItems {
            deleteStudent(it.id)
            std = it
        }

    }

    private fun deleteStudent(id:Int){
        val builder = AlertDialog.Builder(this)
        builder.setMessage("Are you want to delete the item ?")
        builder.setCancelable(true)
        builder.setPositiveButton("Yes"){ dialog, _ ->
            sqLiteHelper.deleteStudentById(id)
            getStudents()
            dialog.dismiss()
        }
        builder.setNegativeButton("No"){ dialog, _ ->
            dialog.dismiss()
        }

        val alert = builder.create()
        alert.show()
    }
    private fun updateStudent() {
        val name = etName.text.toString()
        val email = etEmail.text.toString()
        val roll = etRoll.text.toString()

        //check record not change
        if(name == std?.name && email == std?.email && roll == std?.roll){
            Toast.makeText(this, "Record not changed....", Toast.LENGTH_SHORT).show()
        }

        if(std == null){
            return
        }

        val std = StudentModel(id = std!!.id,name = name,email = email,roll=roll)
        val status = sqLiteHelper.updateStudent(std)
        if(status > -1){
            Toast.makeText(this, "Updated", Toast.LENGTH_SHORT).show()
            clearTextField()
            getStudents()
        }else{
            Toast.makeText(this, "Update Failed", Toast.LENGTH_SHORT).show()
            getStudents()
        }
    }

    private fun initRecyclerView() {

        recyclerView.layoutManager = LinearLayoutManager(this)
        adapter = StudentAdapter(this)
        recyclerView.adapter = adapter
    }

    private fun getStudents() {
        stdList = sqLiteHelper.getAllStudents()
        adapter?.addItems(stdList)
    }

    private fun addStudent() {
        if(etName.text.toString().isEmpty() || etEmail.text.toString().isEmpty()){
            Toast.makeText(this, "please enter the required field", Toast.LENGTH_SHORT).show()
        }else{
            val std = StudentModel(name = etName.text.toString(),email = etEmail.text.toString(),roll = etRoll.text.toString())
            val status = sqLiteHelper.insertStudent(std)
            //check insert or not
            if(status > -1){
                Toast.makeText(this, "Student Added", Toast.LENGTH_SHORT).show()
                clearTextField()
                getStudents()
            }else{
                Toast.makeText(this, "Record Not saved", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun clearTextField() {
        etName.setText("")
        etEmail.setText("")
        etRoll.setText("")
        etName.requestFocus()
    }


}