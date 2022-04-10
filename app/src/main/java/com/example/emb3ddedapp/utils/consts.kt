package com.example.emb3ddedapp.utils

import android.app.ProgressDialog
import android.content.Context.MODE_PRIVATE
import android.net.Uri
import android.provider.OpenableColumns
import android.widget.Toast
import com.example.emb3ddedapp.MainActivity
import com.example.emb3ddedapp.R
import com.example.emb3ddedapp.database.repository.DataRepository
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import java.io.File
import java.io.FileOutputStream
import java.io.IOException
import java.io.InputStream
import java.text.DateFormat
import java.text.SimpleDateFormat
import java.util.*

lateinit var APP:MainActivity
lateinit var REPOSITORY:DataRepository
lateinit var progressDialog:ProgressDialog
lateinit var BEARER_TOKEN:String

const val dataName = "data"
//const val TIME_PAT = "HH:mm:ss"
//const val DATE_PAT = "yyyy-MM-dd"
const val DATE_TIME_PATTERN = "yyyy-MM-dd HH:mm:ss"
const val DB_DATE_TIME_PATTERN = "yyyy-MM-dd'T'HH:mm:ss.SSS'Z'"
const val CONTENT_FILE_URL = "https://www.emb3ded.kz/api/getContent?file="

fun showProgressDialog(title:String) {
    progressDialog.setTitle(title)
    progressDialog.show()
}

fun closeProgressDialog() {
    if (progressDialog.isShowing)
        progressDialog.dismiss()
}

fun getDataTimeWithFormat(dateTime:String):String{
    val dateFormat: DateFormat = SimpleDateFormat(DB_DATE_TIME_PATTERN, Locale.getDefault())
    val date: Date = dateFormat.parse(dateTime)!!
    val formatter: DateFormat = SimpleDateFormat(DATE_TIME_PATTERN, Locale.getDefault())
    return formatter.format(date)
}

/*//Google sign in
fun getSignInClient(): GoogleSignInClient {
    val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN).requestIdToken(
        APP.getString(com.firebase.ui.auth.R.string.default_web_client_id)).requestEmail().build()
    return GoogleSignIn.getClient(APP,gso)
}*/

fun showToast(msg:String){
    Toast.makeText(APP,msg,Toast.LENGTH_LONG).show()
}

//prefs
fun setInitUserId(id:Int){
    val pref = APP.getSharedPreferences(dataName, MODE_PRIVATE).edit()
    pref.putInt("user_init_id",id)
    pref.apply()
}

fun getInitUserId():Int = APP.getSharedPreferences(dataName, MODE_PRIVATE).getInt("user_init_id", 0)

fun setInitTokenDevice(init:Boolean){
    val pref = APP.getSharedPreferences(dataName, MODE_PRIVATE).edit()
    pref.putBoolean("user_init_device",init)
    pref.apply()
}

fun getInitTokenDevice():Boolean = APP.getSharedPreferences(dataName, MODE_PRIVATE).getBoolean("user_init_device", false)

fun setInitResetPassword(isResetPasswordEmail:String,init:Boolean){
    val pref = APP.getSharedPreferences(dataName, MODE_PRIVATE).edit()
    pref.putBoolean("reset_email_$isResetPasswordEmail", init)
    pref.apply()
}

fun getInitResetPassword(isResetPasswordEmail:String):Boolean =
    APP.getSharedPreferences(dataName, MODE_PRIVATE).getBoolean("reset_email_$isResetPasswordEmail",false)

fun setAccessToken(bearerToken : String?){
    val pref = APP.getSharedPreferences(dataName, MODE_PRIVATE).edit()
    pref.putString("bearer_token",bearerToken)
    pref.apply()
}

fun getTokenAccess():String? = APP.getSharedPreferences(dataName, MODE_PRIVATE).getString("bearer_token","")

//Uri gen
fun Uri.getName():String?{
    val cursor = APP.contentResolver.query(this,null,null,null,null)
    val nameIndex = cursor?.getColumnIndex(OpenableColumns.DISPLAY_NAME)
    cursor?.moveToFirst()
    val filename = nameIndex?.let { cursor.getString(it) }
    cursor?.close()
    return filename
}

//get File From resolver

fun getFileFromInput(filename: String, inputStream: InputStream?): File?{

    inputStream?.let { input->
        try {
            val  file = File(filename)
            val outputStream = FileOutputStream(file)

            var len = 0
            val buffer = ByteArray(1024)

            while (input.read(buffer).also { len = it }!=-1){
                outputStream.write(buffer,0,len)
            }
            outputStream.flush()
            outputStream.close()
            input.close()

            return file

        }catch (e: IOException){
            e.printStackTrace()
            input.close()
        }
    }
    return null
}
