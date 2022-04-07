package com.example.emb3ddedapp.screens.page_news

import android.opengl.Visibility
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.bumptech.glide.Glide
import com.example.emb3ddedapp.R
import com.example.emb3ddedapp.databinding.PageNewsFragmentBinding
import com.example.emb3ddedapp.models.NewsItem
import com.example.emb3ddedapp.utils.APP
import com.example.emb3ddedapp.utils.getDataTimeWithFormat
import com.google.android.material.appbar.AppBarLayout


class PageNewsFragment : Fragment() {

    private var _binding:PageNewsFragmentBinding? = null
    private val binding:PageNewsFragmentBinding
    get() = _binding!!

    private lateinit var viewModel: PageNewsViewModel

    private var currNewsItem:NewsItem? = null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        _binding = PageNewsFragmentBinding.inflate(inflater,container,false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        currNewsItem = arguments?.getSerializable("news_item") as? NewsItem
        binding.apply {
            appbar.addOnOffsetChangedListener(object : AppBarLayout.OnOffsetChangedListener {
                var scrollRange = -1
                override fun onOffsetChanged(appBarLayout: AppBarLayout, verticalOffset: Int) {
                    //Initialize the size of the scroll
                    if (scrollRange == -1) {
                        scrollRange = appBarLayout.totalScrollRange
                    }
                    //Check if the view is collapsed
                    if (scrollRange + verticalOffset == 0) {
                        tvHeadTitle.visibility = View.VISIBLE
                    } else{
                        tvHeadTitle.visibility = View.INVISIBLE
                    }
                }
            })
            currNewsItem?.let { newsItem ->
                newsItem.img_url?.let { url->
                    Glide.with(requireContext()).load(url).into(imgNews)
                }
                tvDescription.text = newsItem.description
                tvHeadTitle.text = newsItem.title
                tvTag.text = newsItem.tag
                tvTitle.text = newsItem.title
                tvAuthor.text = newsItem.user!!.login
                tvDateTime.text = getDataTimeWithFormat(newsItem.created_at)
            }
            btnBack.setOnClickListener { APP.mNavController.navigate(R.id.action_pageNewsFragment_to_mainFragment)}
        }
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProvider(this).get(PageNewsViewModel::class.java)
    }

    override fun onStart() {
        super.onStart()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}