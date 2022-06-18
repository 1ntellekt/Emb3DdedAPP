package com.example.emb3ddedapp.screens.news.mynews

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.example.emb3ddedapp.R
import com.example.emb3ddedapp.databinding.MyNewsFragmentBinding
import com.example.emb3ddedapp.models.CurrUser
import com.example.emb3ddedapp.models.NewsItem
import com.example.emb3ddedapp.notification.FireServices
import com.example.emb3ddedapp.screens.news.adapter.NewsAdapter
import com.example.emb3ddedapp.utils.APP

class MyNewsFragment : Fragment() {

    private lateinit var viewModel: MyNewsViewModel
    private var _binding:MyNewsFragmentBinding? = null
    private val binding:MyNewsFragmentBinding
    get() = _binding!!

    private lateinit var adapter:NewsAdapter
    private lateinit var mObserver:Observer<List<NewsItem>?>

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        _binding = MyNewsFragmentBinding.inflate(inflater,container,false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel = ViewModelProvider(this)[MyNewsViewModel::class.java]
        adapter = NewsAdapter(true,null,
        { editId->
            val newsItem = adapter.currentList[editId]
            val args = Bundle().also { it.putSerializable("news_item",newsItem) }
            APP.mNavController.navigate(R.id.action_mainFragment_to_pageNewsEditFragment,args)
        },
        { delId->
            viewModel.deleteNewsItem(adapter.currentList[delId].id)
        })
        binding.apply {
            recyclerView.setHasFixedSize(true)
            recyclerView.adapter = adapter
            btnAddNews.setOnClickListener {
                APP.mNavController.navigate(R.id.action_mainFragment_to_pageNewsEditFragment)
            }
            refLayout.setOnRefreshListener(refLayListener)
        }
        mObserver = Observer { list->
            list?.let {
                adapter.submitList(it)
                if (it.isNotEmpty()) binding.tvHint.visibility = View.GONE
                else binding.tvHint.visibility = View.VISIBLE
            }
        }
    }

    private val refLayListener = SwipeRefreshLayout.OnRefreshListener {
        viewModel.getNewsByUserId(CurrUser.id)
        binding.refLayout.isRefreshing = false
    }

    override fun onStart() {
        super.onStart()
        viewModel.myNewsList.observe(viewLifecycleOwner,mObserver)
        viewModel.getNewsByUserId(CurrUser.id)
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
        viewModel.myNewsList.removeObserver(mObserver)
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