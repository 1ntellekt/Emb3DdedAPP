package com.example.emb3ddedapp.screens.chats

import android.app.Dialog
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import android.util.Log
import android.view.*
import androidx.appcompat.widget.SearchView
import androidx.fragment.app.Fragment
import androidx.core.content.ContextCompat
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.example.emb3ddedapp.R
import com.example.emb3ddedapp.databinding.ChatsFragmentBinding
import com.example.emb3ddedapp.databinding.FilterChatsLayoutBinding
import com.example.emb3ddedapp.models.Chat
import com.example.emb3ddedapp.models.CurrUser
import com.example.emb3ddedapp.notification.FireServices
import com.example.emb3ddedapp.screens.chats.adapter.ChatAdapter
import com.example.emb3ddedapp.utils.APP
import com.example.emb3ddedapp.utils.IS_CONNECT_INTERNET
import com.example.emb3ddedapp.utils.warning_dialog.DialogWarningConnection
import java.util.*

class ChatsFragment : Fragment() {

    private lateinit var viewModel: ChatsViewModel
    private var _binding:ChatsFragmentBinding? = null
    private val binding:ChatsFragmentBinding
    get() = _binding!!

    private lateinit var adapter:ChatAdapter
    private lateinit var mObserver: Observer<List<Chat>?>
    private val chatList = mutableListOf<Chat>()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        _binding = ChatsFragmentBinding.inflate(inflater,container,false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel = ViewModelProvider(this)[ChatsViewModel::class.java]
        adapter = ChatAdapter(CurrUser.id){ idItem->
            val chatId = adapter.currentList[idItem].id
            val args = Bundle()
            args.putInt("id_chat",chatId)
            if (adapter.currentList[idItem].user_first!!.id == CurrUser.id)
            args.putSerializable("recipientUser",adapter.currentList[idItem].user_second)
            else if (adapter.currentList[idItem].user_second!!.id == CurrUser.id){
                args.putSerializable("recipientUser",adapter.currentList[idItem].user_first)
            }
            APP.mNavController.navigate(R.id.action_mainFragment_to_pageChatFragment,args)
        }
        binding.apply {
            val decoration = DividerItemDecoration(requireContext(), DividerItemDecoration.VERTICAL)
            recyclerView.setHasFixedSize(true)
            recyclerView.addItemDecoration(decoration)
            recyclerView.adapter = adapter
            refLayout.setOnRefreshListener(refLayListener)
            btnFilter.setOnClickListener {
                dialogFilterChats()
            }
            searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener{
                override fun onQueryTextSubmit(query: String?): Boolean {
                    return false
                }
                override fun onQueryTextChange(newText: String?): Boolean {
                    filterChats(newText)
                    return true
                }
            })
        }
        mObserver = Observer { list->
            list?.let { list1->


               val chats = when(choose){
                    0-> {
                        if (sortByFirstNewDate) {
                            list1.sortedByDescending {it.last_message?.created_at}
                        } else {
                            list1.sortedBy {it.last_message?.created_at}
                        }
                    }
                    else -> {
                        if (sortByLoginAlpha) {
                            list1
                                .sortedBy { it.user_first?.login }
                                .sortedBy { it.user_second?.login }
                        } else {
                            list1
                                .sortedByDescending { it.user_first?.login }
                                .sortedByDescending { it.user_second?.login }
                        }
                    }
                }

                chatList.clear()
                chatList.addAll(chats)
                adapter.submitList(chats)
            }
        }
    }

    private fun filterChats(newText: String?) {
        if (newText != null){
            adapter.submitList(
                chatList
//                .filterNot { it.user_id_first == CurrUser.id }
//                .filterNot { it.user_id_second == CurrUser.id }
                .filter {
                    (it.user_id_second != CurrUser.id && it.user_id_first == CurrUser.id
                    && it.user_second?.login!!.contains(newText.lowercase(Locale.getDefault()), ignoreCase = true))
                    || (it.user_id_first != CurrUser.id && it.user_id_second == CurrUser.id
                    && it.user_first?.login!!.contains(newText.lowercase(Locale.getDefault()), ignoreCase = true))
                }
            )
        } else {
            adapter.submitList(chatList)
        }
    }

    override fun onStart() {
        super.onStart()
        viewModel.getChatsByUser(CurrUser.id)
        viewModel.chatsList.observe(viewLifecycleOwner,mObserver)

        if (!IS_CONNECT_INTERNET){
            val dialog = DialogWarningConnection(requireContext())
            dialog.showDialog()
        }
    }

    override fun onResume() {
        super.onResume()
        val intentFilter = IntentFilter()
        intentFilter.addAction(FireServices.PUSH_TAG)
        requireActivity().registerReceiver(broadcastReceiver,intentFilter)
    }

    override fun onPause() {
        super.onPause()
        requireActivity().unregisterReceiver(broadcastReceiver)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        viewModel.chatsList.removeObserver(mObserver)
        _binding = null
    }

    private val refLayListener = SwipeRefreshLayout.OnRefreshListener {
        viewModel.getChatsByUser(CurrUser.id)
        binding.refLayout.isRefreshing = false
    }

    private val broadcastReceiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context?, intent: Intent?) {
            if (intent?.extras!=null){
                val extras = intent.extras
                //Log.d("msg","Message received!")
                extras?.keySet()?.firstOrNull{it == FireServices.KEY_ACTION}?.let { key->
                    //Log.d("msg","key: $key")
                    when(extras.getString(key)){
                        FireServices.ACTION_CHAT ->{
                            refLayListener.onRefresh()
                        }
                        else ->{
                            Log.d("msg","key error not found")}
                    }
                }
            }
        }
    }

    //filters
    private var sortByLoginAlpha = true
    private var sortByFirstNewDate = true
    private var choose = 0

    private fun dialogFilterChats(){
        val dialog = context?.let { Dialog(it) }
        dialog?.let { dia->
            dia.requestWindowFeature(Window.FEATURE_NO_TITLE)
            val binding: FilterChatsLayoutBinding = FilterChatsLayoutBinding.inflate(dia.layoutInflater)
            dia.setContentView(binding.root)

            binding.apply {

                when(choose){
                    0->{
                        if (sortByFirstNewDate){
                            btnFirstNew.backgroundTintList = ContextCompat.getColorStateList(requireContext(),R.color.btn_active)
                            btnFirstOld.backgroundTintList = ContextCompat.getColorStateList(requireContext(),R.color.btn_inactive)
                            btnAlpha.backgroundTintList = ContextCompat.getColorStateList(requireContext(),R.color.btn_inactive)
                            btnReverseAlpha.backgroundTintList = ContextCompat.getColorStateList(requireContext(),R.color.btn_inactive)
                        } else {
                            btnFirstOld.backgroundTintList = ContextCompat.getColorStateList(requireContext(),R.color.btn_active)
                            btnFirstNew.backgroundTintList = ContextCompat.getColorStateList(requireContext(),R.color.btn_inactive)
                            btnAlpha.backgroundTintList = ContextCompat.getColorStateList(requireContext(),R.color.btn_inactive)
                            btnReverseAlpha.backgroundTintList = ContextCompat.getColorStateList(requireContext(),R.color.btn_inactive)
                        }
                    }
                    else -> {
                        if (sortByLoginAlpha){
                            btnAlpha.backgroundTintList = ContextCompat.getColorStateList(requireContext(),R.color.btn_active)
                            btnReverseAlpha.backgroundTintList = ContextCompat.getColorStateList(requireContext(),R.color.btn_inactive)
                            btnFirstNew.backgroundTintList = ContextCompat.getColorStateList(requireContext(),R.color.btn_inactive)
                            btnFirstOld.backgroundTintList = ContextCompat.getColorStateList(requireContext(),R.color.btn_inactive)
                        } else {
                            btnReverseAlpha.backgroundTintList = ContextCompat.getColorStateList(requireContext(),R.color.btn_active)
                            btnAlpha.backgroundTintList = ContextCompat.getColorStateList(requireContext(),R.color.btn_inactive)
                            btnFirstNew.backgroundTintList = ContextCompat.getColorStateList(requireContext(),R.color.btn_inactive)
                            btnFirstOld.backgroundTintList = ContextCompat.getColorStateList(requireContext(),R.color.btn_inactive)
                        }
                    }
                }

                btnFirstNew.setOnClickListener {
                    //if (sortByFirstNewDate) return@setOnClickListener
                    sortByFirstNewDate = true
                    choose = 0
                    btnFirstNew.backgroundTintList = ContextCompat.getColorStateList(requireContext(),R.color.btn_active)
                    btnFirstOld.backgroundTintList = ContextCompat.getColorStateList(requireContext(),R.color.btn_inactive)
                    btnAlpha.backgroundTintList = ContextCompat.getColorStateList(requireContext(),R.color.btn_inactive)
                    btnReverseAlpha.backgroundTintList = ContextCompat.getColorStateList(requireContext(),R.color.btn_inactive)
                }
                btnFirstOld.setOnClickListener {
                    //if (!sortByFirstNewDate) return@setOnClickListener
                    sortByFirstNewDate = false
                    choose = 0
                    btnFirstOld.backgroundTintList = ContextCompat.getColorStateList(requireContext(),R.color.btn_active)
                    btnFirstNew.backgroundTintList = ContextCompat.getColorStateList(requireContext(),R.color.btn_inactive)
                    btnAlpha.backgroundTintList = ContextCompat.getColorStateList(requireContext(),R.color.btn_inactive)
                    btnReverseAlpha.backgroundTintList = ContextCompat.getColorStateList(requireContext(),R.color.btn_inactive)
                }
                btnAlpha.setOnClickListener {
                    //if (sortByLoginAlpha) return@setOnClickListener
                    sortByLoginAlpha = true
                    choose = 1
                    btnAlpha.backgroundTintList = ContextCompat.getColorStateList(requireContext(),R.color.btn_active)
                    btnReverseAlpha.backgroundTintList = ContextCompat.getColorStateList(requireContext(),R.color.btn_inactive)
                    btnFirstNew.backgroundTintList = ContextCompat.getColorStateList(requireContext(),R.color.btn_inactive)
                    btnFirstOld.backgroundTintList = ContextCompat.getColorStateList(requireContext(),R.color.btn_inactive)
                }
                btnReverseAlpha.setOnClickListener {
                    //if (!sortByLoginAlpha) return@setOnClickListener
                    sortByLoginAlpha = false
                    choose = 1
                    btnReverseAlpha.backgroundTintList = ContextCompat.getColorStateList(requireContext(),R.color.btn_active)
                    btnAlpha.backgroundTintList = ContextCompat.getColorStateList(requireContext(),R.color.btn_inactive)
                    btnFirstNew.backgroundTintList = ContextCompat.getColorStateList(requireContext(),R.color.btn_inactive)
                    btnFirstOld.backgroundTintList = ContextCompat.getColorStateList(requireContext(),R.color.btn_inactive)
                }
            }

            dia.show()
            dia.window?.setLayout(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT
            )
            dia.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
            dia.window?.attributes?.windowAnimations = R.style.DialogAnimation
            dia.window?.setGravity(Gravity.BOTTOM)
        }
    }

}