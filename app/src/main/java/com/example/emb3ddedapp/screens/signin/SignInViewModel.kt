package com.example.emb3ddedapp.screens.signin

import android.app.Application
import android.app.ProgressDialog
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.ViewModel
import com.example.emb3ddedapp.utils.*
import com.google.firebase.auth.FirebaseAuth

class SignInViewModel(application: Application) : AndroidViewModel(application) {

    fun logInGoogle(token:String,onSuccess:()->Unit){
        showProgressDialog("Google logIn....")
        REPOSITORY.signUpLogInGoogle(false,token,{
            //get data current user
            closeProgressDialog()
            setInitUser(true)
            onSuccess()
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
            closeProgressDialog()
            Log.i("tag", "${FirebaseAuth.getInstance().currentUser?.email}")
            if (FirebaseAuth.getInstance().currentUser?.isEmailVerified == true){
                //get data current user
                setInitUser(true)
                onSuccess()
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

}