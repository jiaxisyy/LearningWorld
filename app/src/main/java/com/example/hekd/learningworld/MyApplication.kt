package com.example.hekd.learningworld

import android.app.Application
import com.example.hekd.learningworld.bean.greendao.DaoMaster
import com.example.hekd.learningworld.bean.greendao.DaoSession
import com.zhy.autolayout.config.AutoLayoutConifg

/**
 * Created by hekd on 2017/10/19.
 */
class MyApplication : Application() {
    companion object {
        var session: DaoSession? = null
    }

    override fun onCreate() {
        super.onCreate()
        val devOpenHelper = DaoMaster.DevOpenHelper(this, getString(R.string.db_videos_name), null)
        val daoMaster = DaoMaster(devOpenHelper.writableDatabase)
        session = daoMaster.newSession()
        AutoLayoutConifg.getInstance().useDeviceSize()
    }


    fun getSession(): DaoSession {
        return session!!
    }
}