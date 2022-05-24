package com.example.emb3ddedapp.screens.news

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import com.example.emb3ddedapp.databinding.NewsFragmentBinding
import com.google.android.material.tabs.TabLayoutMediator

class NewsFragment : Fragment() {

    private var _binding:NewsFragmentBinding? = null
    private val binding: NewsFragmentBinding
    get() = _binding!!

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        _binding = NewsFragmentBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.apply {
            viewPager.adapter = NewsPagerAdapter(context as FragmentActivity)
            tabLayout.tabIconTint = null
            TabLayoutMediator(tabLayout,viewPager){ tab, pos ->
                when(pos){
                    0-> {tab.text = "All news"}
                    1->{tab.text = "My news"}
                }
            }.attach()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}