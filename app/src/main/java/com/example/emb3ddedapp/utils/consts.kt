package com.example.emb3ddedapp.utils

import android.content.Context.MODE_PRIVATE
import android.widget.Toast
import com.example.emb3ddedapp.MainActivity

lateinit var APP:MainActivity

const val dataName = "data"

fun showToast(msg:String){
    Toast.makeText(APP,msg,Toast.LENGTH_SHORT).show()
}

fun setInitUser(init:Boolean){
    val pref = APP.getSharedPreferences(dataName, MODE_PRIVATE).edit()
    pref.putBoolean("user_init",init)
    pref.apply()
}

fun getInitUser():Boolean = APP.getSharedPreferences(dataName, MODE_PRIVATE).getBoolean("user_init", false)