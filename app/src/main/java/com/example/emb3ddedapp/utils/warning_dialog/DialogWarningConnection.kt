package com.example.emb3ddedapp.utils.warning_dialog

import android.app.Dialog
import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.view.Gravity
import android.view.ViewGroup
import com.example.emb3ddedapp.R

class DialogWarningConnection(context:Context) : Dialog(context) {

    fun showDialog() {
        setContentView(R.layout.dialog_cache_load)
        show()
        //setCancelable(false)
        window?.setLayout(
            ViewGroup.LayoutParams.WRAP_CONTENT,
            ViewGroup.LayoutParams.WRAP_CONTENT
        )
        window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        window?.attributes?.windowAnimations = R.style.DialogAnimation
        window?.setGravity(Gravity.CENTER)
    }

}