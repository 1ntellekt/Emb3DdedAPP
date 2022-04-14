package com.example.emb3ddedapp.progressdialog

import android.app.AlertDialog
import android.content.Context
import android.view.LayoutInflater
import com.example.emb3ddedapp.databinding.LoadingLayoutBinding

class MyProgressDialog(private val context: Context) {
    private lateinit var alertDialog:AlertDialog
     fun load(title:String){
         val alertDialogBuilder = AlertDialog.Builder(context)
         val binding: LoadingLayoutBinding = LoadingLayoutBinding.inflate(LayoutInflater.from(context))
         alertDialogBuilder.setView(binding.root)
         binding.tvTitle.text = title
         alertDialogBuilder.setCancelable(false)
          alertDialog = alertDialogBuilder.create()
          alertDialog.show()
     }
    fun dismiss(){
        if (alertDialog.isShowing)
            alertDialog.dismiss()
    }
}