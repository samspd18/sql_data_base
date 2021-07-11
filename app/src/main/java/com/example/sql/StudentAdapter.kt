package com.example.sql

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.rv_list.view.*

class StudentAdapter(private val context: Context) : RecyclerView.Adapter<StudentAdapter.ViewHolder>(){

    private var stdList : ArrayList<StudentModel> = ArrayList()
    private var onClickItems: ((StudentModel) -> Unit)? = null
    private var onClickDeleteItems: ((StudentModel) -> Unit)? = null

    fun addItems(items:ArrayList<StudentModel>){
        this.stdList = items
        notifyDataSetChanged()
    }
    //update functions
    fun setOnClickItems(Callback: (StudentModel) -> Unit) {
        this.onClickItems = Callback
    }
    //delete functions
    fun setOnClickDeleteItems(Callback: (StudentModel) -> Unit) {
        this.onClickDeleteItems = Callback
    }
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): StudentAdapter.ViewHolder {
        return ViewHolder(
            LayoutInflater.from(context).inflate(
                R.layout.rv_list,parent,false
            )
        )
    }
    override fun onBindViewHolder(holder: StudentAdapter.ViewHolder, position: Int) {
        val std = stdList[position]
        holder.bindView(std)
        holder.itemView.setOnClickListener{
            onClickItems?.invoke(std)
        }
        holder.delete.setOnClickListener{
            onClickDeleteItems?.invoke(std)
        }
    }

    override fun getItemCount(): Int {
        return stdList.size
    }

    class ViewHolder(v:View) : RecyclerView.ViewHolder(v) {
        var id =v.tvId
        var name = v.tvName
        var email = v.tvEmail
        var roll = v.tvRoll
        var delete = v.Delete

        fun bindView(std : StudentModel){
            id.text = std.id.toString()
            name.text = std.name
            email.text = std.email
            roll.text = std.roll
        }
    }

}