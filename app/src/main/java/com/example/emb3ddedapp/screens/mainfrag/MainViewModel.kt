package com.example.emb3ddedapp.screens.mainfrag

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.ViewModel
import com.example.emb3ddedapp.utils.REPOSITORY
import com.example.emb3ddedapp.utils.showToast

class MainViewModel(application: Application) : AndroidViewModel(application) {

    fun signOut(onSuccess:()->Unit){
        REPOSITORY.signOut({onSuccess()},{ showToast(it)})
    }

}