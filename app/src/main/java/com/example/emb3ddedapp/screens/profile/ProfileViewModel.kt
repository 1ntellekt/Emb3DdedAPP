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
        showProgressDialog("Exit account to sign pages")
        REPOSITORY.signOut({
             closeProgressDialog()
             onSuccess()
        },{
            closeProgressDialog()
            showToast(it)
        })
    }

    fun updateUserProfile(old_pass:String?=null,new_pass:String?=null, user: User, onSuccess: (User) -> Unit){
        showProgressDialog("Update user profile....")
        if (old_pass == null && new_pass == null){
            REPOSITORY.editCurrentUser(oldPassword = old_pass, password = new_pass, user,
                {
                    closeProgressDialog()
                    onSuccess(user)
                },
                {
                    closeProgressDialog()
                    showToast(it)
                })
        } else if (old_pass != null && new_pass != null){
            REPOSITORY.reAuthenticate(CurrUser.email, new_pass, old_pass, {

            REPOSITORY.editCurrentUser(oldPassword = old_pass, password = new_pass, user,
            {
                closeProgressDialog()
                onSuccess(user)
            },
            {
               closeProgressDialog()
               showToast(it)
            })

            }, { showToast(it)})
        }
    }

    fun uploadFile(file: File, onSuccess: (String) -> Unit, onFail:()->Unit){
        showProgressDialog("Uploading image...")
        REPOSITORY.uploadFile(file,"profile_img",
            {
                closeProgressDialog()
                onSuccess("$CONTENT_FILE_URL$it")
            },
            {
                closeProgressDialog()
                showToast(it)
                onFail()
            })
    }

}