package com.example.emb3ddedapp.screens.news.allnews

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
import com.example.emb3ddedapp.R
import com.example.emb3ddedapp.databinding.AllNewsFragmentBinding
import com.example.emb3ddedapp.models.CurrUser
import com.example.emb3ddedapp.models.NewsItem
import com.example.emb3ddedapp.notification.FireServices
import com.example.emb3ddedapp.screens.news.adapter.NewsAdapter
import com.example.emb3ddedapp.utils.APP

class AllNewsFragment : Fragment() {

    private lateinit var viewModel: AllNewsViewModel
    private var _binding: AllNewsFragmentBinding? = null
    private val binding: AllNewsFragmentBinding
        get() = _binding!!

    private lateinit var adapter: NewsAdapter
    private val newsList = mutableListOf<NewsItem>()
    private lateinit var mObserver: Observer<List<NewsItem>?>


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,savedInstanceState: Bundle?): View? {
        _binding = AllNewsFragmentBinding.inflate(inflater,container,false)
        return binding.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProvider(this).get(AllNewsViewModel::class.java)
        // TODO: Use the ViewModel
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        adapter = NewsAdapter(false,{ idItem->
            val newsItem = newsList[idItem]
            val args = Bundle().also { it.putSerializable("news_item", newsItem)}
            APP.mNavController.navigate(R.id.action_mainFragment_to_pageNewsFragment, args)
        },null,null)
        binding.apply {
            recyclerView.setHasFixedSize(true)
            recyclerView.adapter = adapter
            refLayout.setOnRefreshListener(refLayListener)
        }
        mObserver = Observer { list->
            list?.let {
                newsList.clear()
                newsList.addAll(it)
                adapter.setData(it)
            }
        }
    }

    private val refLayListener = SwipeRefreshLayout.OnRefreshListener {
        viewModel.getAllNews()
        binding.refLayout.isRefreshing = false
    }

    override fun onStart() {
        super.onStart()
        viewModel.allNewsList.observe(this,mObserver)
        viewModel.getAllNews()



        val intentFilter = IntentFilter()
        intentFilter.addAction(FireServices.PUSH_TAG)
        requireActivity().registerReceiver(broadcastReceiver,intentFilter)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        viewModel.allNewsList.removeObserver(mObserver)
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
                        FireServices.ACTION_NEWS -> {
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