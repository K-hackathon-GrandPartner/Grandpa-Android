package com.example.grandpa

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class RuleAdapter(private val ruleListData: List<String>): RecyclerView.Adapter<RuleAdapter.RuleViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RuleViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.rule_item, parent, false)
        return RuleViewHolder(itemView)
    }

    inner class RuleViewHolder(itemView: View): RecyclerView.ViewHolder(itemView) {
        val ruleText: TextView = itemView.findViewById(R.id.ruleText)
    }

    override fun getItemCount(): Int {
        return ruleListData.size
    }

    override fun onBindViewHolder(holder: RuleAdapter.RuleViewHolder, position: Int) {
        holder.ruleText.text = ruleListData[position]
    }

}