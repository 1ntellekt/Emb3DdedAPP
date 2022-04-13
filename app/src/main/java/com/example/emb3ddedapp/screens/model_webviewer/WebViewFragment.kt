package com.example.emb3ddedapp.screens.model_webviewer

import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.emb3ddedapp.R
import com.example.emb3ddedapp.databinding.FragmentWebViewBinding


class WebViewFragment : Fragment() {

    private var _binding:FragmentWebViewBinding? = null
    private val binding:FragmentWebViewBinding
    get() = _binding!!


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        _binding = FragmentWebViewBinding.inflate(inflater,container,false)
        return binding.root
    }


    @SuppressLint("SetJavaScriptEnabled")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.apply {
            val urlModel = arguments?.getString("urlModel")
            urlModel?.let { url->
                with(webView){

                    //val summary =""

                val summary = "<html>\n" +
                            "    <body>\n" +
                            "<script type=\"module\" src=\"https://unpkg.com/@google/model-viewer/dist/model-viewer.js\"></script>\n" +
                        "<script nomodule src=\"https://unpkg.com/@google/model-viewer/dist/model-viewer-legacy.js\"></script>"+
                            "<model-viewer ar src=\"$url\" auto-rotate camera-controls alt=\"Chair\" background-color=\"#455A64\"></model-viewer>"+
                            "    </body>\n" +
                            "</html>"

                    settings.apply {
                        javaScriptEnabled = true
                        loadWithOverviewMode = true
                    }
                    Log.i("tagWeb", summary)
                    loadData(summary,"text/html; charset=utf-8", "UTF-8")
                }
            }

        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}