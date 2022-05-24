package com.example.emb3ddedapp.screens.mainfrag

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.emb3ddedapp.cache.database.LocalDB
import com.example.emb3ddedapp.cache.repository.UserLocalRepository
import com.example.emb3ddedapp.utils.IS_CONNECT_INTERNET
import com.example.emb3ddedapp.utils.REPOSITORY
import com.example.emb3ddedapp.utils.showToast
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class MainViewModel(application: Application) : AndroidViewModel(application) {

    fun signOut(onSuccess:()->Unit){
        if (IS_CONNECT_INTERNET){
            REPOSITORY.signOut({
                clearLocal()
                onSuccess()
          },{ showToast(it)})
        } else {
            onSuccess()
        }
    }

    private val repository:UserLocalRepository

    init {
        val userDao = LocalDB.getLocalDB(application).userDao()
        repository = UserLocalRepository(userDao)
    }

    private fun clearLocal(){
        viewModelScope.launch(Dispatchers.IO) {
            repository.deleteAll()
        }
    }

}