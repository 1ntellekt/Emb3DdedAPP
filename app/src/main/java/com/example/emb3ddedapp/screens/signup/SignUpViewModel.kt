package com.example.emb3ddedapp.screens.signup

import android.app.Application
import android.app.ProgressDialog
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.ViewModel
import com.example.emb3ddedapp.utils.REPOSITORY
import com.example.emb3ddedapp.utils.closeProgressDialog
import com.example.emb3ddedapp.utils.showProgressDialog
import com.example.emb3ddedapp.utils.showToast
import java.lang.Appendable

class SignUpViewModel(application: Application) : AndroidViewModel(application) {

//    fun signUpGoogle(token:String,onSuccess:()->Unit){
//        showProgressDialog("Google Sign Up....")
//        REPOSITORY.signUpLogInGoogle(true,token,{
//            closeProgressDialog()
//            onSuccess()},
//            { showToast(it) })
//    }
//
//    fun signUpEmail(email:String,password:String,onSuccess:()->Unit){
//        showProgressDialog("Email Sign Up....")
//        REPOSITORY.singUpEmail(email,password,{
//            closeProgressDialog()
//            onSuccess()},
//            { showToast(it) })
//    }

}