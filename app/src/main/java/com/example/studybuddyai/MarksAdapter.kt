package com.example.studybuddyai

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class MarksAdapter(private val marksList: MutableList<Marks>) : RecyclerView.Adapter<MarksAdapter.ViewHolder>() {

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val marksTextView: TextView = itemView.findViewById(R.id.marksTextView)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_marks, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val marks = marksList[position]
        holder.marksTextView.text = marks.marks
    }

    override fun getItemCount(): Int {
        return marksList.size
    }
}
