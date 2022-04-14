package com.example.emb3ddedapp.screens.profile

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.ViewModel
import com.example.emb3ddedapp.models.CurrUser
import com.example.emb3ddedapp.models.User
import com.example.emb3ddedapp.utils.*
import java.io.File

class ProfileViewModel(application: Application) : AndroidViewModel(application) {

    fun signOut(onSuccess:()->Unit){
        REPOSITORY.signOut({
             onSuccess()
        },{
            showToast(it)
        })
    }

    fun updateUserProfile(old_pass:String?=null,new_pass:String?=null, user: User, onSuccess: (User) -> Unit){
        if (old_pass == null && new_pass == null){
            REPOSITORY.editCurrentUser(oldPassword = old_pass, password = new_pass, user,
                {
                    onSuccess(user)
                },
                {
                    showToast(it)
                })
        } else if (old_pass != null && new_pass != null){
            REPOSITORY.reAuthenticate(CurrUser.email, new_pass, old_pass, {

            REPOSITORY.editCurrentUser(oldPassword = old_pass, password = new_pass, user,
            {
                onSuccess(user)
            },
            {
               showToast(it)
            })

            }, { showToast(it)})
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