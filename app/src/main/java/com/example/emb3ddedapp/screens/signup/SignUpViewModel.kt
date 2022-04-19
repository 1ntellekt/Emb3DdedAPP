package com.example.emb3ddedapp.screens.signup

import android.app.Application
import android.app.ProgressDialog
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.emb3ddedapp.utils.REPOSITORY
import com.example.emb3ddedapp.utils.showToast
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.lang.Appendable

class SignUpViewModel(application: Application) : AndroidViewModel(application) {

/*    fun signUpGoogle(token:String,onSuccess:()->Unit){
        showProgressDialog("Google Sign Up....")
        REPOSITORY.signUpLogInGoogle(true,token,{
            closeProgressDialog()
            onSuccess()},
            { showToast(it) })
    }*/

    fun signUpEmail(email:String,password:String,onSuccess:()->Unit, onFail: () -> Unit){
            REPOSITORY.singUpEmail(email,password,
                {
                    onSuccess()
                },
                {
                    showToast(it)
                    onFail()
                })
    }

}