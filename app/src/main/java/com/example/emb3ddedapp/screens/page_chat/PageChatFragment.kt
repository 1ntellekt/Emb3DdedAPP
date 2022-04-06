package com.example.emb3ddedapp.screens.page_chat

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.bumptech.glide.Glide
import com.example.emb3ddedapp.R
import com.example.emb3ddedapp.databinding.PageChatFragmentBinding
import com.example.emb3ddedapp.models.Chat
import com.example.emb3ddedapp.models.CurrUser
import com.example.emb3ddedapp.utils.APP

class PageChatFragment : Fragment() {

    private lateinit var viewModel: PageChatViewModel
    private var _binding:PageChatFragmentBinding? = null
    private val binding:PageChatFragmentBinding
    get() = _binding!!

    private var currChat: Chat? = null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        _binding = PageChatFragmentBinding.inflate(inflater,container,false)
        return binding.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProvider(this).get(PageChatViewModel::class.java)
        // TODO: Use the ViewModel
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        currChat = arguments?.getSerializable("created_chat") as? Chat
        binding.apply {
            currChat?.let { chat ->
                if (CurrUser.id == chat.user_first!!.id){
                    tvLoginUser.text = chat.user_second!!.login
                    tvStatusUser.text = chat.user_second.status
                    chat.user_second.url_profile?.let { url->
                        Glide.with(requireContext()).load(url).into(imgUser)
                    }
                } else {
                    tvLoginUser.text = chat.user_first.login
                    tvStatusUser.text = chat.user_first.status
                    chat.user_first.url_profile?.let { url->
                        Glide.with(requireContext()).load(url).into(imgUser)
                    }
                }
                btnCallPhone.setOnClickListener {
                    //call to recipient user
                }
                btnPopMenu.setOnClickListener {  }
                btnSendMsg.setOnClickListener {  }
                btnTakeFile.setOnClickListener {  }
                btnTakePhoto.setOnClickListener {  }
            }
            btnBack.setOnClickListener {
                APP.mNavController.navigate(R.id.action_pageChatFragment_to_mainFragment)
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