package com.example.hekd.learningworld.adapter

import android.support.v7.widget.RecyclerView
import android.view.View
import android.view.ViewGroup

/**
 * Created by hekd on 2017/10/11.
 */
class RvAdapter(list:List<String>) : RecyclerView.Adapter<RvAdapter.MyRvViewHolder>() {


    override fun onCreateViewHolder(parent: ViewGroup?, viewType: Int): MyRvViewHolder {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun getItemCount(): Int {

        return 5
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun onBindViewHolder(holder: MyRvViewHolder?, position: Int) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }


    class MyRvViewHolder(itemView: View?) : RecyclerView.ViewHolder(itemView){

    }

}