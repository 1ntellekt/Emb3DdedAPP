package com.example.emb3ddedapp.screens.page_news_edit

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.widget.doOnTextChanged
import com.bumptech.glide.Glide
import com.example.emb3ddedapp.R
import com.example.emb3ddedapp.databinding.PageNewsEditFragmentBinding
import com.example.emb3ddedapp.models.CurrUser
import com.example.emb3ddedapp.models.NewsItem
import com.example.emb3ddedapp.models.Order
import com.example.emb3ddedapp.utils.APP
import com.google.android.material.appbar.AppBarLayout

class PageNewsEditFragment : Fragment() {

    private var _binding:PageNewsEditFragmentBinding? = null
    private val binding:PageNewsEditFragmentBinding
    get() = _binding!!

    private lateinit var viewModel: PageNewsEditViewModel

    private var curNewsItem: NewsItem? = null
    private var selectedImg:String? = null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        _binding = PageNewsEditFragmentBinding.inflate(inflater,container,false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
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

            curNewsItem = arguments?.getSerializable("news_item") as? NewsItem

            curNewsItem?.let {
                edDescription.setText(it.description)
                edTitle.setText(it.title)
                tvHeadTitle.text = it.title
                it.img_url?.let { url->
                    Glide.with(requireContext()).load(url).into(imgNews)
                    selectedImg = url
                }
               // tvAuthor.text = curNewsItem!!.user!!.login
            }

            tvAuthor.text = CurrUser.login

            edTitle.doOnTextChanged { text, start, before, count -> edTitleLayout.error = null }
            edDescription.doOnTextChanged { text, start, before, count -> edDescriptionLayout.error = null}
            edTag.doOnTextChanged { text, start, before, count -> edTagLayout.error = null }

            btnPopMenu.setOnClickListener {
                if (edTitle.text.toString().isEmpty()){
                    edTitleLayout.error = "Input title"
                } else if (edTag.text.toString().isEmpty()){
                    edTagLayout.error = "Input tag"
                } else if (edDescription.text.toString().isEmpty()){
                    edDescriptionLayout.error = "Input description"
                } else {
                    if (curNewsItem == null) {
                        viewModel.addNewsItem(NewsItem(title = edTitle.text.toString(), description = edDescription.text.toString(),
                            img_url = selectedImg, tag = "#${edTag.text.toString()}", user_id = CurrUser.id, created_at = ""))
                        { APP.mNavController.navigate(R.id.action_pageNewsEditFragment_to_mainFragment)}
                    } else {
                        viewModel.editNewsItem(NewsItem(id = curNewsItem!!.id,title = edTitle.text.toString(), description = edDescription.text.toString(),
                            img_url = selectedImg, tag = edTag.text.toString(), user_id = CurrUser.id, created_at = ""))
                        { APP.mNavController.navigate(R.id.action_pageNewsEditFragment_to_mainFragment)}
                    }
                }
            }
            btnBack.setOnClickListener { APP.mNavController.navigate(R.id.action_pageNewsEditFragment_to_mainFragment) }
        }
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProvider(this).get(PageNewsEditViewModel::class.java)
        // TODO: Use the ViewModel
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}