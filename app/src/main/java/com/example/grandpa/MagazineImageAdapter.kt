package com.example.grandpa

import android.content.Context
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
        val magazineTag = itemView.findViewById<TextView>(R.id.magazine_tag)
        val magazineTitle = itemView.findViewById<TextView>(R.id.magazine_title)
        val magazineName = itemView.findViewById<TextView>(R.id.magazine_name)
    }
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewPagerViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.magazine_item, parent, false)
        return ViewPagerViewHolder(itemView)
    }
    override fun onBindViewHolder(holder: ViewPagerViewHolder, position: Int) {
        val index: Int = position % magazineInfoList.size
        val item: MagazineData = magazineInfoList[index]

        Glide.with(context).load(item.imageUrl).into(holder.magazineView)
        holder.magazineTag.text = item.tag
        holder.magazineTitle.text = item.title
        holder.magazineName.text = item.names

    }
    override fun getItemCount(): Int {
        return Integer.MAX_VALUE;
    }
}