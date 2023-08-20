package com.example.grandpa

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide

class ShowRoomAdapter(val roomList: ArrayList<room_data>, var m2setting : Boolean, private val context: Context) : RecyclerView.Adapter<ShowRoomAdapter.RoomViewHolder>() {
    inner class RoomViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val room_image = itemView.findViewById<ImageView>(R.id.item_roomImage) //방사진
        val building_type = itemView.findViewById<TextView>(R.id.item_buildingType) //건물 유형
        val room_size = itemView.findViewById<TextView>(R.id.item_roomSize) //방크기
        val size_unit = itemView.findViewById<TextView>(R.id.item_sizeUnit) //크기 단위 평
        val size_unitm2 = itemView.findViewById<TextView>(R.id.item_sizeUnit) //크기 단위 m2
        val building_height: TextView = itemView.findViewById<TextView>(R.id.item_buildingHeight) //층수
        val room_deposit = itemView.findViewById<TextView>(R.id.item_roomDeposit) //보증금
        val room_price = itemView.findViewById<TextView>(R.id.item_roomPrice) //월세
        val building_place = itemView.findViewById<TextView>(R.id.item_address) //위치
        val room_intro = itemView.findViewById<TextView>(R.id.item_roomIntro) //상세 정보
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RoomViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.room_item, parent, false)
        return RoomViewHolder(itemView).apply{ //itemView 넘겨주기
            itemView.setOnClickListener{
                val position = adapterPosition
                if(position != RecyclerView.NO_POSITION){
                    val clickedItem = roomList[position]
                    val intent = Intent(parent.context, RoomDetailActivity::class.java)
                    intent.putExtra("room_id", clickedItem.id)
                    parent.context.startActivity(intent)
                }
            }
        }
    }

    override fun onBindViewHolder(holder: RoomViewHolder, position: Int) {
        val urlString = roomList[position].imageUrl
        holder.apply{ //사진 로딩 라이브러리
            Glide.with(context).load(urlString).into(room_image)
        }
        holder.building_type.text = roomList[position].buildingType
        holder.room_size.text = roomList[position].roomSizeType
        holder.building_height.text = roomList[position].roomFloor.toString()
        holder.room_deposit.text = roomList[position].deposit.toString()
        holder.room_price.text = roomList[position].monthlyRent.toString()
        holder.building_place.text = roomList[position].address
        holder.room_intro.text = roomList[position].title

        if(m2setting){
            //true면 m2으로
            val roomSize = roomList[position].roomSize
            holder.size_unitm2.text = String.format("%.1f", roomSize) + "㎡"
        }else{
            //false면 평으로
            val roomSize = roomList[position].roomSize
            holder.size_unit.text = String.format("%.1f", roomSize / 3.3) + "평"
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