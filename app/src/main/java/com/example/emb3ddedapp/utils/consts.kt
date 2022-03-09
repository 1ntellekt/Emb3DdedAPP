package com.example.emb3ddedapp.utils

import android.app.ProgressDialog
import android.content.Context.MODE_PRIVATE
import android.widget.Toast
import com.example.emb3ddedapp.MainActivity
import com.example.emb3ddedapp.R
import com.example.emb3ddedapp.database.repository.DataRepository
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions

lateinit var APP:MainActivity
lateinit var REPOSITORY:DataRepository
lateinit var progressDialog:ProgressDialog

const val dataName = "data"
const val TIME_PAT = "HH:mm:ss"
const val DATE_PAT = "yyyy-MM-dd"

fun showProgressDialog(title:String) {
    progressDialog.setTitle(title)
    progressDialog.show()
}

fun closeProgressDialog() {
    if (progressDialog.isShowing)
        progressDialog.dismiss()
}

//Google sign in
fun getSignInClient(): GoogleSignInClient {
    val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN).requestIdToken(
        APP.getString(com.firebase.ui.auth.R.string.default_web_client_id)).requestEmail().build()
    return GoogleSignIn.getClient(APP,gso)
}

fun showToast(msg:String){
    Toast.makeText(APP,msg,Toast.LENGTH_LONG).show()
}

fun setInitUser(init:Boolean){
    val pref = APP.getSharedPreferences(dataName, MODE_PRIVATE).edit()
    pref.putBoolean("user_init",init)
    pref.apply()
}

fun getInitUser():Boolean = APP.getSharedPreferences(dataName, MODE_PRIVATE).getBoolean("user_init", false)