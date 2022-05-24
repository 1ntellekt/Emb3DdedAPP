package com.example.emb3ddedapp.screens.signup

import android.os.Bundle
import android.telephony.PhoneNumberFormattingTextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.widget.doOnTextChanged
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.example.emb3ddedapp.R
import com.example.emb3ddedapp.databinding.SignUpFragmentBinding
import com.example.emb3ddedapp.models.CurrUser
import com.example.emb3ddedapp.utils.progressdialog.MyProgressDialog
import com.example.emb3ddedapp.utils.APP


class SignUpFragment : Fragment() {

    private var _binding: SignUpFragmentBinding? = null
    private val binding: SignUpFragmentBinding
        get() = _binding!!

    private lateinit var viewModel: SignUpViewModel

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        _binding = SignUpFragmentBinding.inflate(inflater,container,false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel = ViewModelProvider(this)[SignUpViewModel::class.java]
        binding.apply {

            edNum.addTextChangedListener(PhoneNumberFormattingTextWatcher("US"))

            edLogin.doOnTextChanged { text, _, _, _ ->
                text?.let { txt->
                    if (txt.toString().length <= 6) {
                        edLoginLayout.error = "More than 6 characters"
                    } else if (txt.toString().length >= 20){
                        edLoginLayout.error = "Less than 20 characters"
                    } else {
                        edLoginLayout.error = null
                    }
                }
            }

            edPassword.doOnTextChanged { text, _, _, _ ->
                text?.let { txt->
                    if (txt.toString().length <= 7) {
                        edPasswordLayout.error = "More than 7 characters"
                    } else {
                        edPasswordLayout.error = null
                    }
                }
            }

            edPasswordConfirm.doOnTextChanged { text, _, _, _ ->
                text?.let { txt->
                    if (txt.toString().length <= 7) {
                        edPasswordLayoutConfirm.error = "More than 7 characters"
                    } else {
                        edPasswordLayoutConfirm.error = null
                    }
                }
            }

            edNum.doOnTextChanged { text, _, _, _ ->
                text?.let { txt->
                    if (txt.toString().length != 13) {
                        edNumLayout.error = "Number format not access!"
                    } else {
                        edNumLayout.error = null
                    }
                }
            }

            edEmail.doOnTextChanged { _, _, _, _ -> edEmailLayout.error = null }

            btnLogInEmail.setOnClickListener {
                if (edEmail.text.toString().isEmpty()){
                    edEmailLayout.error = "Input email address is empty!"
                    edEmail.requestFocus()
                }else if (edLogin.text.toString().isEmpty()){
                    edLoginLayout.error = "Input login is empty!"
                    edLogin.requestFocus()
                } else if (edNum.text.toString().isEmpty()){
                    edNumLayout.error = "Input phone number is empty!"
                    edNum.requestFocus()
                } else if (edPassword.text.toString().isEmpty()){
                    edPasswordLayout.error = "Input password is empty!"
                    edPassword.requestFocus()
                } else if (edPasswordConfirm.text.toString().isEmpty()){
                    edPasswordLayoutConfirm.error = "input confirm password is empty!"
                } else if (edPassword.text.toString() != edPasswordConfirm.text.toString()){
                    edPasswordLayout.error = "Different passwords"
                    edPasswordLayoutConfirm.error = "Different passwords"
                    edPassword.requestFocus()
                } else if (edPasswordLayout.error.isNullOrEmpty() && edPasswordLayoutConfirm.error.isNullOrEmpty()
                    && edEmailLayout.error.isNullOrEmpty() && edLoginLayout.error.isNullOrEmpty() && edNumLayout.error.isNullOrEmpty()) {
                    CurrUser.email = edEmail.text.toString()
                    CurrUser.login = edLogin.text.toString()
                    CurrUser.number = edNum.text.toString()
                    val myProgress = MyProgressDialog(requireContext())
                    myProgress.load("Sign up email....")
                    viewModel.signUpEmail(edEmail.text.toString(),edPassword.text.toString(),{
                        myProgress.dismiss()
                        APP.mNavController.navigate(R.id.action_signUpFragment_to_signInFragment)
                    },{
                        myProgress.dismiss()
                    })
                }
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}