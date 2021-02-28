package com.greppy.app

import com.jakewharton.threetenabp.AndroidThreeTen
import app.baseappsetup.MApplicationController

class ApplicationController : MApplicationController() {

    companion object {
        lateinit var instance: ApplicationController
    }

    override fun onCreate() {
        super.onCreate()
        instance = this
        AndroidThreeTen.init(this)
    }
}