package com.example.emb3ddedapp.screens.news

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.example.emb3ddedapp.screens.news.allnews.AllNewsFragment
import com.example.emb3ddedapp.screens.news.mynews.MyNewsFragment

class NewsPagerAdapter(fragmentActivity: FragmentActivity):FragmentStateAdapter(fragmentActivity) {

    override fun getItemCount(): Int {
        return 2
    }

    override fun createFragment(position: Int): Fragment {
       return when(position){
           0 -> AllNewsFragment()
           else -> MyNewsFragment()
       }
    }


}