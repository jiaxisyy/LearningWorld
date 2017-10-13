package com.example.hekd.learningworld.adapter

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.example.hekd.learningworld.R
import kotlinx.android.synthetic.main.item_lw_main.view.*


/**
 * Created by hekd on 2017/10/11.
 */
class RvAdapter(val list: List<String>,val itemClickListener: ItemClickListener) : RecyclerView.Adapter<RvAdapter.MyViewHolder>() {
    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        holder.bind(list[position])
    }

    override fun getItemCount(): Int = list.size

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_lw_main,null)
        return MyViewHolder(view,itemClickListener)
    }

    class MyViewHolder(itemView: View, val itemClickListener: ItemClickListener) : RecyclerView.ViewHolder(itemView) {

        fun bind(name: String) {

            itemView.item_tv_lw_mainName.text = name
            itemView.setOnClickListener {
                itemClickListener.itemClick(itemView, position)
            }

        }

    }


    interface ItemClickListener {
        fun itemClick(view: View, position: Int)
    }
}