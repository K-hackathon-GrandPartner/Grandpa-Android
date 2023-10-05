package com.example.grandpa

import android.annotation.SuppressLint
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

    private val checkBoxStates = mutableMapOf<Int, Boolean>()

    init {
        for(i in checklistData.indices){
            checkBoxStates[i] = false
        }
    }

    @SuppressLint("NotifyDataSetChanged")
    fun checkAllCheckboxes() {
        for (i in checklistData.indices) {
            checkBoxStates[i] = true
        }
        notifyDataSetChanged()
    }

    @SuppressLint("NotifyDataSetChanged")
    fun uncheckAllCheckboxes() {
        for (i in checklistData.indices) {
            checkBoxStates[i] = false
        }
        notifyDataSetChanged()
    }

    fun isAllChecked(): Boolean {
        return checkBoxStates.values.all { it }
    }

    fun isAnyChecked(): Boolean {
        return checkBoxStates.values.any { it }
    }


    inner class CheckViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val checkText: TextView = itemView.findViewById(R.id.chkText)
        val checkBox:CheckBox = itemView.findViewById(R.id.chkList_checkbox)
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
        holder.checkBox.isChecked = checkBoxStates.getOrDefault(position, false)

        // 체크박스 클릭 이벤트 처리
        holder.checkBox.setOnCheckedChangeListener { _, isChecked ->
            checkBoxStates[position] = isChecked
        }

    }
}