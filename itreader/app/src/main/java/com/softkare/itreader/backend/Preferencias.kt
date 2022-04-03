package com.softkare.itreader.backend

import android.content.Context

class Preferencias(val context:Context) {

    val SHARED_CTX = "Nydtb"

    val SHARED_USERNAME = "username"

    val SHARED_EMAIL = "email"

    val SHARE_NAME = "name"

    val storage = context.getSharedPreferences(SHARED_CTX,0)

    fun saveName(name_key:String){
        storage.edit().putString(SHARE_NAME,name_key).apply()
    }

    fun saveUsername(username_key:String){
        storage.edit().putString(SHARED_USERNAME,username_key).apply()
    }

    fun saveEmail(email_key:String){
        storage.edit().putString(SHARED_EMAIL,email_key).apply()
    }

    fun getName():String{
        return storage.getString(SHARE_NAME,"")!!
    }

    fun getUsername():String{
        return storage.getString(SHARED_USERNAME,"")!!
    }

    fun getEmail():String{
        return storage.getString(SHARED_EMAIL,"")!!
    }

}