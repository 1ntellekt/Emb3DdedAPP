package com.example.emb3ddedapp.screens.profile

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.bumptech.glide.Glide
import com.example.emb3ddedapp.R
import com.example.emb3ddedapp.databinding.ProfileFragmentBinding
import com.example.emb3ddedapp.models.CurrUser

class ProfileFragment : Fragment() {

    private lateinit var viewModel: ProfileViewModel
    private var _binding:ProfileFragmentBinding? = null
    private val binding:ProfileFragmentBinding
    get() = _binding!!

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        _binding = ProfileFragmentBinding.inflate(inflater,container,false)
        return binding.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProvider(this).get(ProfileViewModel::class.java)
        // TODO: Use the ViewModel
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.apply {
            tvEmail.text = CurrUser.email
            edLogin.setText(CurrUser.login)
            edNumber.setText(CurrUser.number)
            edStatus.setText(CurrUser.status)
            CurrUser.url_profile?.let {
                Glide.with(this@ProfileFragment).load(it).into(imgProfile)
            }
        }

    }

    override fun onStart() {
        super.onStart()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}