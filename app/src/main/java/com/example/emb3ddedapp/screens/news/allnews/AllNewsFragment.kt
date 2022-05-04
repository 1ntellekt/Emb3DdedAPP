package com.example.emb3ddedapp.screens.news.allnews

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
import android.widget.RatingBar
import androidx.fragment.app.Fragment
import androidx.appcompat.widget.SearchView
import androidx.core.content.ContextCompat
import androidx.lifecycle.Observer
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.example.emb3ddedapp.R
import com.example.emb3ddedapp.databinding.AllNewsFragmentBinding
import com.example.emb3ddedapp.databinding.FilterNewsLayoutBinding
import com.example.emb3ddedapp.models.NewsItem
import com.example.emb3ddedapp.notification.FireServices
import com.example.emb3ddedapp.screens.news.adapter.NewsAdapter
import com.example.emb3ddedapp.utils.APP
import com.example.emb3ddedapp.utils.IS_CONNECT_INTERNET
import com.example.emb3ddedapp.utils.warning_dialog.DialogWarningConnection
import java.util.*

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
            searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener{
                override fun onQueryTextSubmit(query: String?): Boolean {
                    return false
                }
                override fun onQueryTextChange(newText: String?): Boolean {
                    filterNews(newText)
                    return true
                }
            })
            btnFilter.setOnClickListener {
                showFilterNewsDialog()
            }
        }
        mObserver = Observer { list->
            list?.let { list1->
                val news = if (sortByFirstNewDate){
                    list1.sortedByDescending {it.created_at!!}.filter { it.avgMark != null && it.avgMark >= minRating }
                } else {
                    list1.sortedBy {it.created_at!!}.filter { it.avgMark != null && it.avgMark >= minRating }
                }

                newsList.clear()
                newsList.addAll(news)
                adapter.setData(news)
            }
        }
    }

    //filters
    private var sortByFirstNewDate = true
    private var minRating = 0.0

    private fun showFilterNewsDialog() {
        val dialog = context?.let { Dialog(it) }
        dialog?.let { dia->
            dia.requestWindowFeature(Window.FEATURE_NO_TITLE)
            val binding: FilterNewsLayoutBinding = FilterNewsLayoutBinding.inflate(dia.layoutInflater)
            dia.setContentView(binding.root)

            binding.apply {
                if (sortByFirstNewDate){
                    btnFirstNew.backgroundTintList = ContextCompat.getColorStateList(requireContext(),R.color.btn_active)
                    btnFirstOld.backgroundTintList = ContextCompat.getColorStateList(requireContext(),R.color.btn_inactive)
                } else {
                    btnFirstOld.backgroundTintList = ContextCompat.getColorStateList(requireContext(),R.color.btn_active)
                    btnFirstNew.backgroundTintList = ContextCompat.getColorStateList(requireContext(),R.color.btn_inactive)
                }

                ratingBar.rating = minRating.toFloat()

                btnFirstNew.setOnClickListener {
                    if (sortByFirstNewDate) return@setOnClickListener
                    sortByFirstNewDate = true
                    btnFirstNew.backgroundTintList = ContextCompat.getColorStateList(requireContext(),R.color.btn_active)
                    btnFirstOld.backgroundTintList = ContextCompat.getColorStateList(requireContext(),R.color.btn_inactive)
                }
                btnFirstOld.setOnClickListener {
                    if (!sortByFirstNewDate) return@setOnClickListener
                    sortByFirstNewDate = false
                    btnFirstOld.backgroundTintList = ContextCompat.getColorStateList(requireContext(),R.color.btn_active)
                    btnFirstNew.backgroundTintList = ContextCompat.getColorStateList(requireContext(),R.color.btn_inactive)
                }

                ratingBar.onRatingBarChangeListener =
                    RatingBar.OnRatingBarChangeListener { ratingBar, rating, fromUser ->
                        minRating = rating.toDouble()
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

    private fun filterNews(newText: String?) {
        if (newText != null){
            adapter.setData(newsList.filter {
                it.title.contains(newText.lowercase(Locale.getDefault()), ignoreCase = true)
              ||it.tag.contains(newText.lowercase(Locale.getDefault()), ignoreCase = true)
            })
        } else {
            adapter.setData(newsList)
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

        if (!IS_CONNECT_INTERNET){
            val dialog = DialogWarningConnection(requireContext())
            dialog.showDialog()
        }

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