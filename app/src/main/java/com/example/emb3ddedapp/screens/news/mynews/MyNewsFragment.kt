package com.example.emb3ddedapp.screens.news.mynews

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.emb3ddedapp.R

class MyNewsFragment : Fragment() {

    companion object {
        fun newInstance() = MyNewsFragment()
    }

    private lateinit var viewModel: MyNewsViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.my_news_fragment, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProvider(this).get(MyNewsViewModel::class.java)
        // TODO: Use the ViewModel
    }

}