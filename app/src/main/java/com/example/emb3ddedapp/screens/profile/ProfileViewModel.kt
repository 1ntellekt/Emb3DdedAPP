package com.example.emb3ddedapp.screens.profile

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.emb3ddedapp.cache.database.LocalDB
import com.example.emb3ddedapp.cache.repository.UserLocalRepository
import com.example.emb3ddedapp.models.CurrUser
import com.example.emb3ddedapp.models.User
import com.example.emb3ddedapp.utils.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.io.File

class ProfileViewModel(application: Application) : AndroidViewModel(application) {

    fun signOut(onSuccess:()->Unit){
        if (IS_CONNECT_INTERNET){
            REPOSITORY.signOut({
                clearLocal()
                onSuccess()
            },{showToast(it)})
        } else {
            onSuccess()
        }
    }

    private val repository: UserLocalRepository

    init {
        val userDao = LocalDB.getLocalDB(application).userDao()
        repository = UserLocalRepository(userDao)
    }

    private fun clearLocal(){
        viewModelScope.launch(Dispatchers.IO) {
            repository.deleteAll()
        }
    }


    fun updateUserProfile(old_pass:String?=null,new_pass:String?=null, user: User, onSuccess: (User) -> Unit, onFail: (String) -> Unit){
        if (old_pass == null && new_pass == null){
            REPOSITORY.editCurrentUser(oldPassword = old_pass, password = new_pass, user,
                {
                    onSuccess(user)
                },
                {
                    onFail(it)
                })
        } else if (old_pass != null && new_pass != null){
            REPOSITORY.reAuthenticate(CurrUser.email, new_pass, old_pass, {

            REPOSITORY.editCurrentUser(oldPassword = old_pass, password = new_pass, user,
            {
                onSuccess(user)
            },
            {
                onFail(it)
            })

            }, { onFail(it) })
        }
    }

    fun uploadFile(file: File, onSuccess: (String) -> Unit, onFail:()->Unit){
        REPOSITORY.uploadFile(file,"profile_img",
            {
                onSuccess("$CONTENT_FILE_URL$it")
            },
            {
                showToast(it)
                onFail()
            })
    }

}