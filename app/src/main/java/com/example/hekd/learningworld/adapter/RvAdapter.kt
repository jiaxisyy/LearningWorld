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
class RvAdapter(val list: List<String>, val dOrF: List<Int>, val isPlayed: List<Int>, val itemClickListener: ItemClickListener) : RecyclerView.Adapter<RvAdapter.MyViewHolder>() {
    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        holder.bind(list[position], dOrF[position], isPlayed[position])
    }

    override fun getItemCount(): Int = list.size

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_lw_main, null)
        return MyViewHolder(view, itemClickListener)
    }

    class MyViewHolder(itemView: View, val itemClickListener: ItemClickListener) : RecyclerView.ViewHolder(itemView) {

        fun bind(name: String, dirOrFile: Int, isPd: Int) {
            itemView.item_tv_lw_mainName.text = name
            when (dirOrFile) {
            // 1表示文件,0表示文件夹
            // 1表示已观看,0表示未观看
                1 -> {

                    if (isPd == 1) {//已观看
                        itemView.item_iv_lw_main_more.visibility = View.INVISIBLE
                        itemView.item_tv_lw_progress.visibility = View.VISIBLE
                        itemView.item_tv_lw_progress.text = "已观看13%"// 设置已观看进度
                    } else {
                        itemView.item_iv_lw_main_more.setImageResource(R.drawable.iv_no_play)
                    }
                }
                0 -> {


                }
            }

            itemView.setOnClickListener {
                itemClickListener.itemClick(itemView, position)
            }
        }
    }

    interface ItemClickListener {
        fun itemClick(view: View, position: Int)
    }
}