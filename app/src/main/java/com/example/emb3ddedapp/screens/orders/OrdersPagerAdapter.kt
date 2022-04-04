package com.example.emb3ddedapp.screens.orders

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.Lifecycle
import androidx.viewpager.widget.PagerAdapter.POSITION_NONE
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.example.emb3ddedapp.screens.orders.allorders.AllOrdersFragment
import com.example.emb3ddedapp.screens.orders.myorders.MyOrdersFragment


class OrdersPagerAdapter(fragmentManager: FragmentManager,var fragments: MutableList<Fragment>,lifecycle: Lifecycle):FragmentStateAdapter(fragmentManager,lifecycle) {
    override fun getItemCount(): Int {
        return 2
    }

    override fun createFragment(position: Int): Fragment {
        return when(position){
            0 ->AllOrdersFragment()
            else -> MyOrdersFragment()
        }
    }

    fun refreshFragment(index: Int, fragment: Fragment) {
        fragments[index] = fragment
        notifyItemChanged(index)
    }

    override fun getItemId(position: Int): Long {
        return fragments[position].hashCode().toLong()
    }

    override fun containsItem(itemId: Long): Boolean {
        return fragments.find { it.hashCode().toLong() == itemId } != null
    }

}