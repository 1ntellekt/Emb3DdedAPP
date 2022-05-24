package com.example.emb3ddedapp.screens.model_webviewer

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.emb3ddedapp.databinding.FragmentWebViewBinding


class WebViewFragment : Fragment() {

    private var _binding:FragmentWebViewBinding? = null
    private val binding:FragmentWebViewBinding
    get() = _binding!!


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        // Inflate the layout for this fragment
        _binding = FragmentWebViewBinding.inflate(inflater,container,false)
        return binding.root
    }


    @SuppressLint("SetJavaScriptEnabled")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.apply {
//            btnBack.setOnClickListener {
//                APP.mNavController.navigate(R.id.action_webViewFragment_to_pageChatFragment)
//            }
            val urlModel = arguments?.getString("urlModel")
            urlModel?.let { url->
                with(webView){
                    settings.apply {
                        javaScriptEnabled = true
                        loadWithOverviewMode = true
                    }
                    loadUrl(url)
                }
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}