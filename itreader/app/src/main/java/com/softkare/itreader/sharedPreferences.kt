package com.softkare.itreader

import android.app.Application
import com.softkare.itreader.backend.Preferencias

class sharedPreferences : Application(){

    companion object{
        lateinit var prefs:Preferencias
    }

    override fun onCreate() {
        super.onCreate()
        prefs = Preferencias(applicationContext)
    }
}