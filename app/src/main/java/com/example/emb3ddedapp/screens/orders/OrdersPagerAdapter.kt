package com.example.emb3ddedapp.screens.orders

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.example.emb3ddedapp.screens.orders.allorders.AllOrdersFragment
import com.example.emb3ddedapp.screens.orders.myorders.MyOrdersFragment

class OrdersPagerAdapter(fragmentActivity: FragmentActivity):FragmentStateAdapter(fragmentActivity) {
    override fun getItemCount(): Int {
        return 2
    }

    override fun createFragment(position: Int): Fragment {
        return when(position){
            0 ->AllOrdersFragment()
            else -> MyOrdersFragment()
        }
    }

}