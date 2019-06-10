package com.github.farzadfarazmand.remindme.core

import android.app.Application
import com.github.farzadfarazmand.remindme.BuildConfig
import com.orhanobut.logger.AndroidLogAdapter
import com.orhanobut.logger.Logger
import com.orhanobut.logger.PrettyFormatStrategy


class RemindMeApp : Application() {

    override fun onCreate() {
        super.onCreate()

        val formatStrategy = PrettyFormatStrategy.newBuilder()
            .showThreadInfo(false)
            .tag("RemindMe!")
            .build()

        Logger.addLogAdapter(object : AndroidLogAdapter(formatStrategy) {
            override fun isLoggable(priority: Int, tag: String?): Boolean {
                //show log only in debug mode
                return BuildConfig.DEBUG
            }
        })

    }

}