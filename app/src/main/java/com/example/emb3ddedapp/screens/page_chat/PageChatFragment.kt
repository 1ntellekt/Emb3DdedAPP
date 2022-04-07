package com.example.emb3ddedapp.screens.page_chat

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.bumptech.glide.Glide
import com.example.emb3ddedapp.R
import com.example.emb3ddedapp.databinding.PageChatFragmentBinding
import com.example.emb3ddedapp.models.Chat
import com.example.emb3ddedapp.models.ChatDefault
import com.example.emb3ddedapp.models.CurrUser
import com.example.emb3ddedapp.models.Message
import com.example.emb3ddedapp.notification.FireServices
import com.example.emb3ddedapp.screens.page_chat.adapter.MessageAdapter
import com.example.emb3ddedapp.utils.APP
import com.google.android.gms.common.util.IOUtils

class PageChatFragment : Fragment() {

    private lateinit var viewModel: PageChatViewModel
    private var _binding:PageChatFragmentBinding? = null
    private val binding:PageChatFragmentBinding
    get() = _binding!!

    private var chatId:Int? = null
    private lateinit var mObserver: Observer<List<Message>?>
    private lateinit var cObserver: Observer<ChatDefault?>
    private lateinit var adapter:MessageAdapter
    private val messageList = mutableListOf<Message>()
    private var chatDefault:ChatDefault? = null

    private var myRecipientId:Int = 0

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
        chatId = arguments?.getInt("id_chat")
        adapter = MessageAdapter(CurrUser.id,{},{},{})
        binding.apply {
            btnBack.setOnClickListener {
                APP.mNavController.navigate(R.id.action_pageChatFragment_to_mainFragment)
            }
            recyclerView.setHasFixedSize(true)
            recyclerView.adapter = adapter
            refLayout.setOnRefreshListener(refLayListener)
            btnSendMsg.setOnClickListener {
                if (edMessage.text.toString().isNotEmpty()){
                    chatDefault?.let {
                        viewModel.sendTextMsg(Message(chat_id = it.id, created_at = "", file_3d_msg = null,
                            file_msg = null, img_msg = null, user_id_recepient = myRecipientId, user_id_sender = CurrUser.id, text_msg = edMessage.text.toString()))
                    }
                }
            }
        }
        mObserver = Observer { messages->
            messages?.let {
                messageList.clear()
                messageList.addAll(it)
                adapter.setData(it)
            }
        }
        cObserver = Observer {
            it?.let { chatDef ->
                chatDefault = chatDef
                myRecipientId = if (chatDef.user_id_first == CurrUser.id){
                    chatDef.user_id_second
                } else {
                    chatDef.user_id_first
                }
            }
        }
    }

    private val refLayListener = SwipeRefreshLayout.OnRefreshListener {
        chatId?.let { viewModel.getMessagesByChatId(it) }
        binding.refLayout.isRefreshing = false
    }

    override fun onStart() {
        super.onStart()
        viewModel.messagesList.observe(this,mObserver)
        viewModel.chatDef.observe(this,cObserver)
        chatId?.let {
            viewModel.getMessagesByChatId(it)
        }



        val intentFilter = IntentFilter()
        intentFilter.addAction(FireServices.PUSH_TAG)
        requireActivity().registerReceiver(broadcastReceiver,intentFilter)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        viewModel.messagesList.removeObserver(mObserver)
        viewModel.chatDef.removeObserver(cObserver)
        requireActivity().unregisterReceiver(broadcastReceiver)
        _binding = null
    }

    private val broadcastReceiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context?, intent: Intent?) {
            if (intent?.extras!=null){
                val extras = intent.extras
                //Log.d("msg","Message received!")
                extras?.keySet()?.firstOrNull{it == FireServices.KEY_ACTION}?.let { key->
                    //Log.d("msg","key: $key")
                    when(extras.getString(key)){
                        FireServices.ACTION_SHOW_MESSAGE -> {
                            refLayListener.onRefresh()
                        }
                        else ->{
                            Log.d("msg","key error not found")}
                    }
                }
            }
        }
    }

}