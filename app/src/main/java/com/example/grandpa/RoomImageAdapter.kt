package com.example.grandpa

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide

class RoomImageAdapter(private val roomImageList: List<String>, private val context: Context) : RecyclerView.Adapter<RoomImageAdapter.ViewPagerViewHolder>() {
    inner class ViewPagerViewHolder(parent: ViewGroup) : RecyclerView.ViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.detail_item, parent, false)){
        val room_image = itemView.findViewById<ImageView>(R.id.detailItemPager)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int)= ViewPagerViewHolder((parent))

    override fun onBindViewHolder(holder: ViewPagerViewHolder, position: Int) {
        val urlString = roomImageList[position]
        Glide.with(context).load(urlString).into(holder.room_image)
    }

    override fun getItemCount(): Int {
        return roomImageList.size
    }
}