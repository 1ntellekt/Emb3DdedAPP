package com.example.emb3ddedapp.screens.signin

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.emb3ddedapp.R
import com.example.emb3ddedapp.databinding.SignInFragmentBinding
import com.example.emb3ddedapp.utils.APP

class SignInFragment : Fragment() {

    private var _binding:SignInFragmentBinding? = null
    private val binding:SignInFragmentBinding
    get() = _binding!!

    companion object {
        fun newInstance() = SignInFragment()
    }

    private lateinit var viewModel: SignInViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
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
        binding.toggleSignUp.setOnClickListener {
            APP.mNavController.navigate(R.id.action_signInFragment_to_signUpFragment)
        }
    }

}