package com.example.emb3ddedapp.screens.page_news_edit

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.emb3ddedapp.R
import com.example.emb3ddedapp.databinding.PageNewsEditFragmentBinding
import com.google.android.material.appbar.AppBarLayout

class PageNewsEditFragment : Fragment() {

    private var _binding:PageNewsEditFragmentBinding? = null
    private val binding:PageNewsEditFragmentBinding
    get() = _binding!!

    private lateinit var viewModel: PageNewsEditViewModel

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