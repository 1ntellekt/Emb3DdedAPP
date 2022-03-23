package com.example.emb3ddedapp.screens.signin

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.widget.doAfterTextChanged
import androidx.core.widget.doBeforeTextChanged
import androidx.core.widget.doOnTextChanged
import com.example.emb3ddedapp.R
import com.example.emb3ddedapp.databinding.SignInFragmentBinding
import com.example.emb3ddedapp.utils.APP
import com.example.emb3ddedapp.utils.getSignInClient
import com.example.emb3ddedapp.utils.showToast
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.common.api.ApiException

class SignInFragment : Fragment() {

    private var _binding:SignInFragmentBinding? = null
    private val binding:SignInFragmentBinding
    get() = _binding!!

    companion object {
        fun newInstance() = SignInFragment()
    }

    private lateinit var viewModel: SignInViewModel

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        _binding = SignInFragmentBinding.inflate(inflater,container,false)
        return binding.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProvider(this).get(SignInViewModel::class.java)
        // TODO: Use the ViewModel
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onStart() {
        super.onStart()
        binding.apply {

            toggleSignUp.setOnClickListener {
            APP.mNavController.navigate(R.id.action_signInFragment_to_signUpFragment)
            }

            edPassword.doOnTextChanged { text, start, count, after ->
                text?.let { txt->
                    if (txt.toString().length <=7){
                        edPasswordLayout.error = "More than 7 characters"
                    } else {
                        edPasswordLayout.error = null
                    }
                }
            }

            edEmail.doOnTextChanged { text, start, before, count -> edEmailLayout.error = null }


            btnLogInEmail.setOnClickListener {
                if (edPasswordLayout.error.isNullOrEmpty() && edEmail.text.toString().isNotEmpty() && edPassword.text.toString().isNotEmpty()){
                    //log in email
                    /*viewModel.logInEmail(edEmail.text.toString(),edPassword.text.toString()) {
                        APP.mNavController.navigate(R.id.action_signInFragment_to_mainFragment)
                    }*/
                } else if (edEmail.text.toString().isEmpty()){
                    edEmailLayout.error = "Input email address!"
                } else if (edPassword.text.toString().isEmpty()){
                    edPasswordLayout.error = "Input password!"
                }
            }

            btnLogInGoogle.setOnClickListener {
                getSignInClient().signOut()
                takeGoogleAcc.launch(getSignInClient().signInIntent)
            }

        }
    }

    private val takeGoogleAcc = registerForActivityResult(ActivityResultContracts.StartActivityForResult()){
        val task = GoogleSignIn.getSignedInAccountFromIntent(it.data!!)
        try {
            val account = task.getResult(ApiException::class.java)
            account?.let { acc->
//                viewModel.logInGoogle(acc.idToken!!){
//                    APP.mNavController.navigate(R.id.action_signInFragment_to_mainFragment)
//                }
            }
        }catch (e: ApiException){
            Log.e("tag", "error sign Google: ${e.message.toString()}")
        }
    }

}