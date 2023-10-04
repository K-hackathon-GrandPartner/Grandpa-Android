package com.example.grandpa

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CheckBox
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.widget.LinearLayoutCompat.DividerMode
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.RecyclerView

class CheckAdapter(private val checklistData: List<String>) : RecyclerView.Adapter<CheckAdapter.CheckViewHolder>() {

    inner class CheckViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val checkText = itemView.findViewById<TextView>(R.id.chkText)
    }
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CheckViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.check_item, parent, false)
        return CheckViewHolder(itemView)
    }

    override fun getItemCount(): Int {
        return checklistData.size
    }

    override fun onBindViewHolder(holder: CheckViewHolder, position: Int) {
        holder.checkText.text = checklistData[position]
    }
}