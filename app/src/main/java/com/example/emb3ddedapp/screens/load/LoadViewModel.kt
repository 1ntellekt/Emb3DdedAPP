package com.example.emb3ddedapp.screens.load

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.emb3ddedapp.cache.database.LocalDB
import com.example.emb3ddedapp.cache.entities.UserEntity
import com.example.emb3ddedapp.cache.repository.UserLocalRepository
import com.example.emb3ddedapp.models.CurrUser
import com.example.emb3ddedapp.utils.IS_CONNECT_INTERNET
import com.example.emb3ddedapp.utils.REPOSITORY
import com.example.emb3ddedapp.utils.getInitUserId
import com.example.emb3ddedapp.utils.showToast
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class LoadViewModel(application: Application) : AndroidViewModel(application) {

    private val repository:UserLocalRepository
    val userLive:LiveData<UserEntity>

    fun getDataForCurrentUser(onSuccess:()->Unit, onLocalSuccess:()->Unit){
        if (IS_CONNECT_INTERNET){
            REPOSITORY.getCurrUser({
                addLocalUser()
                onSuccess()
            },{ showToast(it)})
        } else {
            onLocalSuccess()
        }
    }

    init {
        val userDao = LocalDB.getLocalDB(application).userDao()
        repository = UserLocalRepository(userDao)
        userLive = repository.getUserById(getInitUserId())
    }

    private fun addLocalUser(){
        viewModelScope.launch(Dispatchers.IO) {
            repository.addUser(userEntity = UserEntity(
                id = getInitUserId(),
                login = CurrUser.login, email = CurrUser.email,
                uid = CurrUser.uid, number = CurrUser.number,
                status = CurrUser.status, url_profile = CurrUser.url_profile)
            )
        }
    }

}