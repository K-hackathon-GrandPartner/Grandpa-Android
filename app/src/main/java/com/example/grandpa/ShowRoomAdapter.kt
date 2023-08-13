package com.example.grandpa

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class ShowRoomAdapter(val roomList: ArrayList<room_data>, var m2setting : Boolean) : RecyclerView.Adapter<ShowRoomAdapter.RoomViewHolder>() {
    inner class RoomViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val room_image = itemView.findViewById<ImageView>(R.id.item_roomImage) //방사진
        val building_type = itemView.findViewById<TextView>(R.id.item_buildingType) //건물 유형
        val room_size = itemView.findViewById<TextView>(R.id.item_roomSize) //방크기
        val size_unit = itemView.findViewById<TextView>(R.id.item_sizeUnit) //크기 단위 평
        val size_unitm2 = itemView.findViewById<TextView>(R.id.item_sizeUnit) //크기 단위 m2
        val building_height = itemView.findViewById<TextView>(R.id.item_buildingHeight) //층수
        val room_deposit = itemView.findViewById<TextView>(R.id.item_roomDeposit) //보증금
        val room_price = itemView.findViewById<TextView>(R.id.item_roomPrice) //월세
        val building_place = itemView.findViewById<TextView>(R.id.item_buildingPlace) //위치
        val room_intro = itemView.findViewById<TextView>(R.id.item_roomIntro) //상세 정보
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RoomViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.room_item, parent, false)
        return RoomViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: RoomViewHolder, position: Int) {
        holder.room_image.setImageResource(roomList[position].room_image)
        holder.building_type.text = roomList[position].building_type
        holder.room_size.text = roomList[position].room_size
        holder.building_height.text = roomList[position].building_height.toString()
        holder.room_deposit.text = roomList[position].room_deposit.toString()
        holder.room_price.text = roomList[position].room_price.toString()
        holder.building_place.text = roomList[position].building_place
        holder.room_intro.text = roomList[position].room_intro

        if(m2setting){
            //true면 평으로
            holder.size_unit.text = roomList[position].size_unit.toString()
        }else{
            //false면 m2로
            holder.size_unitm2.text = roomList[position].size_unitm2.toString()
        }
    }

    override fun getItemCount(): Int {
        return roomList.size
    }

    fun setM2Setting(m2setting: Boolean) {
        this.m2setting = m2setting
        notifyDataSetChanged()
    }
}