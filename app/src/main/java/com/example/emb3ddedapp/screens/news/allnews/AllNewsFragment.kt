package com.example.emb3ddedapp.screens.news.allnews

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.emb3ddedapp.R

class AllNewsFragment : Fragment() {

    companion object {
        fun newInstance() = AllNewsFragment()
    }

    private lateinit var viewModel: AllNewsViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.all_news_fragment, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProvider(this).get(AllNewsViewModel::class.java)
        // TODO: Use the ViewModel
    }

}