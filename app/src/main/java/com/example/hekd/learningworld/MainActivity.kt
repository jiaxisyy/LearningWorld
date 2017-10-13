package com.example.hekd.learningworld

import android.content.Intent
import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.view.View
import com.example.hekd.learningworld.adapter.RvAdapter
import com.example.hekd.learningworld.ui.ChineseClassicsActivity
import com.zhy.autolayout.AutoLayoutActivity
import kotlinx.android.synthetic.main.activity_main.*


class MainActivity : AutoLayoutActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val list = listOf<String>("国学经典", "童话故事", "记忆编码", "九门功课")
        //往事如风


        rv_lw_main.adapter = RvAdapter(list, object : RvAdapter.ItemClickListener {
            override fun itemClick(view: View, position: Int) {

                when (position) {
                    0 ->   startActivity(Intent(this@MainActivity, ChineseClassicsActivity::class.java))
                }
            }
        })
        rv_lw_main.layoutManager = LinearLayoutManager(this)

    }
}
