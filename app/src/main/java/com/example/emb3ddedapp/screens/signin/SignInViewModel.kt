package com.example.emb3ddedapp.screens.signin

import android.app.Application
import android.app.ProgressDialog
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.emb3ddedapp.cache.database.LocalDB
import com.example.emb3ddedapp.cache.entities.UserEntity
import com.example.emb3ddedapp.cache.repository.UserLocalRepository
import com.example.emb3ddedapp.models.CurrUser
import com.example.emb3ddedapp.utils.*
import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class SignInViewModel(application: Application) : AndroidViewModel(application) {

/*
    fun logInGoogle(token:String,onSuccess:()->Unit){
        showProgressDialog("Google logIn....")
        REPOSITORY.signUpLogInGoogle(false,token,{
            //get data current user
            //REPOSITORY.getCurrentUser({
                closeProgressDialog()
                setInitUser(true)
                onSuccess()
            *//*}, {
                showToast(it)
            })*//*

        },
            {
                closeProgressDialog()
                showToast(it)
            }
        )
    }

    fun logInEmail(email:String,password:String,onSuccess:()->Unit){
        showProgressDialog("Email logIn....")
        REPOSITORY.logInEmail(email,password,{
            //closeProgressDialog()
            Log.i("tag", "${FirebaseAuth.getInstance().currentUser?.email}")
            if (FirebaseAuth.getInstance().currentUser?.isEmailVerified == true){
                //get data current user
                //REPOSITORY.getCurrentUser({
                    closeProgressDialog()
                    setInitUser(true)
                    onSuccess()
                *//*}, {
                    showToast(it)
                })*//*
            } else {
                closeProgressDialog()
                showToast("Verified email please!")
            }
        },
            {
                closeProgressDialog()
                showToast(it)
            }
        )
    }
    */

    private val repository:UserLocalRepository

    init {
        val userDao = LocalDB.getLocalDB(application).userDao()
        repository = UserLocalRepository(userDao)
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

    fun logInEmail(email:String,password:String,onSuccess:()->Unit, onFail:()->Unit){
        REPOSITORY.logInEmail(email,password, {
            addLocalUser()
            onSuccess()
        },{
            showToast(it)
            onFail()
        })
    }

    fun resetPassword(email: String){
        REPOSITORY.resetPasswordEmail(email,{ showToast("Send email message to reset user password")},{ showToast(it)})
    }

}