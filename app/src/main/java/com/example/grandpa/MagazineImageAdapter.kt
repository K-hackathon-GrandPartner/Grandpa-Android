package com.example.grandpa

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide


class MagazineImageAdapter(private val magazineInfoList: List<MagazineData>, private val context: Context) : RecyclerView.Adapter<MagazineImageAdapter.ViewPagerViewHolder>() {
    inner class ViewPagerViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val magazineView = itemView.findViewById<ImageView>(R.id.magazine_view)
    }
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewPagerViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.magazine_item, parent, false)
        return ViewPagerViewHolder(itemView).apply {
            itemView.setOnClickListener{
                val position = adapterPosition
                if(position != RecyclerView.NO_POSITION){
                    val magazineId = magazineInfoList[position% magazineInfoList.size].id
                    val intent = Intent(parent.context, MagazineDetailActivity::class.java)
                    intent.putExtra("magazineId", magazineId)
                    parent.context.startActivity(intent)
                }
            }
        }
    }
    override fun onBindViewHolder(holder: ViewPagerViewHolder, position: Int) {
        val index: Int = position % magazineInfoList.size
        val item: MagazineData = magazineInfoList[index]
        Glide.with(context).load(item.imageUrl).into(holder.magazineView)

    }
    override fun getItemCount(): Int {
        return if (magazineInfoList.isEmpty()) {
            0
        } else {
            Int.MAX_VALUE
        }
    }
}