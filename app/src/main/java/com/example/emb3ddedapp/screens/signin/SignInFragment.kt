package com.example.emb3ddedapp.screens.signin

import android.app.AlertDialog
import android.app.Dialog
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
import com.example.emb3ddedapp.progressdialog.MyProgressDialog
import com.example.emb3ddedapp.utils.APP
import com.example.emb3ddedapp.utils.showToast

class SignInFragment : Fragment() {

    private var _binding:SignInFragmentBinding? = null
    private val binding:SignInFragmentBinding
    get() = _binding!!

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

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

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
            edEmail.doOnTextChanged { text, start, before, count -> edEmailLayout.error = null }

            alertDialog.setPositiveButton("Ok"){ dialog, which ->

                if (edEmail.text.toString().isNotEmpty()){
                    viewModel.resetPassword(email = edEmail.text.toString())
                } else {
                    showToast("Input was empty!")
                }

            }.setNegativeButton("Cancel"){dialog, which ->
                dialog.dismiss()
            }

        }
        alertDialog.create().show()
    }

    override fun onStart() {
        super.onStart()
    }

/*    private val takeGoogleAcc = registerForActivityResult(ActivityResultContracts.StartActivityForResult()){
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
    }*/

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}