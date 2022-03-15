package com.example.emb3ddedapp.screens.signup

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.widget.doBeforeTextChanged
import androidx.core.widget.doOnTextChanged
import com.example.emb3ddedapp.R
import com.example.emb3ddedapp.databinding.SignInFragmentBinding
import com.example.emb3ddedapp.databinding.SignUpFragmentBinding
import com.example.emb3ddedapp.models.CurrUser
import com.example.emb3ddedapp.utils.APP
import com.example.emb3ddedapp.utils.getSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.common.api.ApiException

class SignUpFragment : Fragment() {

    private var _binding: SignUpFragmentBinding? = null
    private val binding: SignUpFragmentBinding
        get() = _binding!!
    
    companion object {
        fun newInstance() = SignUpFragment()
    }

    private lateinit var viewModel: SignUpViewModel

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        _binding = SignUpFragmentBinding.inflate(inflater,container,false)
        return binding.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProvider(this).get(SignUpViewModel::class.java)
        // TODO: Use the ViewModel
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onStart() {
        super.onStart()

//        binding.apply {
//
//            edLogin.doOnTextChanged { text, start, before, count ->
//                text?.let { txt->
//                    if (txt.toString().length <= 6) {
//                        edLoginLayout.error = "More than 6 characters"
//                    } else if (txt.toString().length >= 20){
//                        edLoginLayout.error = "Less than 20 characters"
//                    } else {
//                        edLoginLayout.error = null
//                    }
//                }
//            }
//
//            edPassword.doOnTextChanged { text, start, before, count ->
//                text?.let { txt->
//                    if (txt.toString().length <= 7) {
//                        edPasswordLayout.error = "More than 7 characters"
//                    } else {
//                        edPasswordLayout.error = null
//                    }
//                }
//            }
//
//            edPasswordConfirm.doOnTextChanged { text, start, before, count ->
//                text?.let { txt->
//                    if (txt.toString().length <= 7) {
//                        edPasswordLayoutConfirm.error = "More than 7 characters"
//                    } else {
//                        edPasswordLayoutConfirm.error = null
//                    }
//                }
//            }
//
//            edNum.doOnTextChanged { text, start, before, count -> edNumLayout.error = null }
//            edEmail.doOnTextChanged { text, start, before, count -> edEmailLayout.error = null }
//
//            btnLogInEmail.setOnClickListener {
//                if (edEmail.text.toString().isEmpty()){
//                    edEmailLayout.error = "Input email address is empty!"
//                }else if (edLogin.text.toString().isEmpty()){
//                    edLoginLayout.error = "Input login is empty!"
//                } else if (edNum.text.toString().isEmpty()){
//                    edNumLayout.error = "Input phone number is empty!"
//                } else if (edPassword.text.toString().isEmpty()){
//                    edPasswordLayout.error = "Input password is empty!"
//                } else if (edPasswordConfirm.text.toString().isEmpty()){
//                    edPasswordLayoutConfirm.error = "input confirm password is empty!"
//                } else if (edPassword.text.toString() != edPasswordConfirm.text.toString()){
//                    edPasswordLayout.error = "Different passwords"
//                    edPasswordLayoutConfirm.error = "Different passwords"
//                } else if (edPasswordLayout.error.isNullOrEmpty() && edPasswordLayoutConfirm.error.isNullOrEmpty()
//                    && edEmailLayout.error.isNullOrEmpty() && edLoginLayout.error.isNullOrEmpty() && edNumLayout.error.isNullOrEmpty()) {
//                        CurrUser.password = edPassword.text.toString()
//                        CurrUser.profileUrlPhoto = null
//                        CurrUser.email = edEmail.text.toString()
//                        CurrUser.login = edLogin.text.toString()
//                        CurrUser.status = "Connected APP"
//                        CurrUser.telNumber = edNum.text.toString()
//
//                    viewModel.signUpEmail(edEmail.text.toString(),edPassword.text.toString()){
//                            APP.mNavController.navigate(R.id.action_signUpFragment_to_signInFragment)
//                    }
//                }
//            }
//
//            btnLogInGoogle.setOnClickListener {
//                if (edEmail.text.toString().isEmpty()){
//                    edEmailLayout.error = "Input email address is empty!"
//                }else if (edLogin.text.toString().isEmpty()){
//                    edLoginLayout.error = "Input login is empty!"
//                } else if (edNum.text.toString().isEmpty()){
//                    edNumLayout.error = "Input phone number is empty!"
//                } else if (edPassword.text.toString().isEmpty()){
//                    edPasswordLayout.error = "Input password is empty!"
//                } else if (edPasswordConfirm.text.toString().isEmpty()){
//                    edPasswordLayoutConfirm.error = "input confirm password is empty!"
//                } else if (edPassword.text.toString() != edPasswordConfirm.text.toString()){
//                    edPasswordLayout.error = "Different passwords"
//                    edPasswordLayoutConfirm.error = "Different passwords"
//                } else if (edPasswordLayout.error.isNullOrEmpty() && edPasswordLayoutConfirm.error.isNullOrEmpty()
//                    && edEmailLayout.error.isNullOrEmpty() && edLoginLayout.error.isNullOrEmpty() && edNumLayout.error.isNullOrEmpty()) {
//                    CurrUser.password = edPassword.text.toString()
//                    CurrUser.profileUrlPhoto = null
//                    CurrUser.email = edEmail.text.toString()
//                    CurrUser.login = edLogin.text.toString()
//                    CurrUser.status = "Connected APP"
//                    CurrUser.telNumber = edNum.text.toString()
//
//                    getSignInClient().signOut()
//                    takeGoogleAcc.launch(getSignInClient().signInIntent)
//                }
//            }
//
//        }
        
    }


    private val takeGoogleAcc = registerForActivityResult(ActivityResultContracts.StartActivityForResult()){
        val task = GoogleSignIn.getSignedInAccountFromIntent(it.data!!)
        try {
            val account = task.getResult(ApiException::class.java)
            account?.let { acc->
//                viewModel.signUpGoogle(acc.idToken!!){
//                    APP.mNavController.navigate(R.id.action_signUpFragment_to_mainFragment)
//                }
            }
        }catch (e: ApiException){
            Log.e("tag", "error sign Google: ${e.message.toString()}")
        }
    }

}