package com.example.ez_tour

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.viewholder.view.*

class StarAdapter(val items: MutableList<StarData>,val context: Context) :
    RecyclerView.Adapter<RecyclerAdapter.ViewHolder>() {

    private var List = mutableListOf<StarData>()
    init {
        List = items
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerAdapter.ViewHolder {
        val inflatedView =
            LayoutInflater.from(parent.context).inflate(R.layout.viewholder, parent, false)
        return RecyclerAdapter.ViewHolder(inflatedView)
    }

    override fun onBindViewHolder(holder: RecyclerAdapter.ViewHolder, position: Int) {
        holder.itemView.setOnClickListener {
            val intent = Intent(holder.itemView.context,InformationActivity::class.java)
            intent.putExtra("name",holder.itemView.text_rename.text.toString())
            intent.putExtra("tag",List.get(position).imageTag)
            ContextCompat.startActivity(holder.itemView.context,intent,null)
        }
        val image = holder.itemView.findViewById<ImageView>(R.id.view_reimage)
        image.setImageBitmap(List.get(position).bitmap)
        val name = holder.itemView.findViewById<TextView>(R.id.text_rename)
        name.text = "${List.get(position).strName}"
        val tag = holder.itemView.findViewById<ImageView>(R.id.view_retag)
        tag.setImageResource(
            when (List.get(position).imageTag) {
                "일반 충전소"-> R.drawable.map_image_0005_tag_1
                "숙소" -> R.drawable.map_image_0004_tag_2
                "카페" -> R.drawable.map_image_0003_tag_3
                "관광명소" -> R.drawable.map_image_0002_tag_4
                "음식점" -> R.drawable.map_image_0001_tag_5
                "상점" -> R.drawable.map_image_0000_tag_6
                else -> R.drawable.map_image_0005_tag_1
            }
        )
    }

    override fun getItemCount(): Int {
        return List.size
    }
    class ViewHolder(v: View) : RecyclerView.ViewHolder(v) {
        private var view: View = v


    }
}