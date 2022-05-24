package com.example.emb3ddedapp.screens.signin

import android.app.AlertDialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.widget.doOnTextChanged
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.example.emb3ddedapp.R
import com.example.emb3ddedapp.databinding.DialogForgotPassLayoutBinding
import com.example.emb3ddedapp.databinding.SignInFragmentBinding
import com.example.emb3ddedapp.utils.progressdialog.MyProgressDialog
import com.example.emb3ddedapp.utils.APP
import com.example.emb3ddedapp.utils.showToast

class SignInFragment : Fragment() {

    private var _binding:SignInFragmentBinding? = null
    private val binding:SignInFragmentBinding
    get() = _binding!!

    private lateinit var viewModel: SignInViewModel

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        _binding = SignInFragmentBinding.inflate(inflater,container,false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel = ViewModelProvider(this)[SignInViewModel::class.java]
        binding.apply {

            toggleSignUp.setOnClickListener {
                APP.mNavController.navigate(R.id.action_signInFragment_to_signUpFragment)
            }

            edPassword.doOnTextChanged { text, _, _, _ ->
                text?.let { txt->
                    if (txt.toString().length <=7){
                        edPasswordLayout.error = "More than 7 characters"
                    } else {
                        edPasswordLayout.error = null
                    }
                }
            }

            edEmail.doOnTextChanged { _, _, _, _ -> edEmailLayout.error = null }


            btnLogInEmail.setOnClickListener {
                if (edPasswordLayout.error.isNullOrEmpty() && edEmail.text.toString().isNotEmpty() && edPassword.text.toString().isNotEmpty()){
                    //log in email
                    val myProgress = MyProgressDialog(requireContext())
                    myProgress.load("Log in email....")
                    viewModel.logInEmail(edEmail.text.toString(),edPassword.text.toString(),{
                        myProgress.dismiss()
                        APP.mNavController.navigate(R.id.action_signInFragment_to_mainFragment)
                    }, {
                        myProgress.dismiss()
                    })
                } else if (edEmail.text.toString().isEmpty()){
                    edEmailLayout.error = "Input email address!"
                } else if (edPassword.text.toString().isEmpty()){
                    edPasswordLayout.error = "Input password!"
                }
            }

/*            btnLogInGoogle.setOnClickListener {
                getSignInClient().signOut()
                takeGoogleAcc.launch(getSignInClient().signInIntent)
            }*/

            toggleForgotPass.setOnClickListener {
                dialogForgotPassword()
            }

        }

    }

    private fun dialogForgotPassword() {
        val alertDialog = AlertDialog.Builder(requireContext())
        val binding:DialogForgotPassLayoutBinding = DialogForgotPassLayoutBinding.inflate(LayoutInflater.from(context))
        alertDialog.setView(binding.root)

        binding.apply {
            edEmail.doOnTextChanged { _, _, _, _ -> edEmailLayout.error = null }

            alertDialog.setPositiveButton("Ok"){ _, _ ->

                if (edEmail.text.toString().isNotEmpty()){
                    viewModel.resetPassword(email = edEmail.text.toString())
                } else {
                    showToast("Input was empty!")
                }

            }.setNegativeButton("Cancel"){ dialog, _ ->
                dialog.dismiss()
            }

        }
        alertDialog.create().show()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}