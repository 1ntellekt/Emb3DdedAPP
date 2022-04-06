package com.example.emb3ddedapp.screens.chats

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
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.example.emb3ddedapp.R
import com.example.emb3ddedapp.databinding.ChatsFragmentBinding
import com.example.emb3ddedapp.models.Chat
import com.example.emb3ddedapp.models.CurrUser
import com.example.emb3ddedapp.notification.FireServices
import com.example.emb3ddedapp.screens.chats.adapter.ChatAdapter

class ChatsFragment : Fragment() {

    private lateinit var viewModel: ChatsViewModel
    private var _binding:ChatsFragmentBinding? = null
    private val binding:ChatsFragmentBinding
    get() = _binding!!

    private lateinit var adapter:ChatAdapter
    private lateinit var mObserver: Observer<List<Chat>?>
    private val chatList = mutableListOf<Chat>()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        _binding = ChatsFragmentBinding.inflate(inflater,container,false)
        return binding.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProvider(this).get(ChatsViewModel::class.java)
        // TODO: Use the ViewModel
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        adapter = ChatAdapter(CurrUser.id){ idItem-> }
        binding.apply {
            val decoration = DividerItemDecoration(requireContext(), DividerItemDecoration.VERTICAL)
            recyclerView.setHasFixedSize(true)
            recyclerView.addItemDecoration(decoration)
            recyclerView.adapter = adapter
            refLayout.setOnRefreshListener(refLayListener)
        }
        mObserver = Observer { list->
            list?.let {
                chatList.clear()
                chatList.addAll(it)
                adapter.setData(it)
            }
        }
    }

    override fun onStart() {
        super.onStart()
        viewModel.getChatsByUser(CurrUser.id)
        viewModel.chatsList.observe(this,mObserver)

        val intentFilter = IntentFilter()
        intentFilter.addAction(FireServices.PUSH_TAG)
        requireActivity().registerReceiver(broadcastReceiver,intentFilter)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        viewModel.chatsList.removeObserver(mObserver)
        requireActivity().unregisterReceiver(broadcastReceiver)
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

}