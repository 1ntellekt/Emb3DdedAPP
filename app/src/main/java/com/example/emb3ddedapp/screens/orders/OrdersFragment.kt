package com.example.emb3ddedapp.screens.orders

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.emb3ddedapp.databinding.OrdersFragmentBinding
import com.google.android.material.tabs.TabLayoutMediator

class OrdersFragment : Fragment() {

    private var _binding:OrdersFragmentBinding? = null
    private val binding:OrdersFragmentBinding
    get() = _binding!!

    private lateinit var adapter: OrdersPagerAdapter

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        _binding = OrdersFragmentBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.apply {
           adapter =  OrdersPagerAdapter(requireActivity().supportFragmentManager,lifecycle)
            viewPager.adapter = adapter
            tabLayout.tabIconTint = null
            TabLayoutMediator(tabLayout, viewPager){ tab, pos ->
                when(pos){
                    0-> {tab.text = "All orders"}
                    1->{tab.text = "My orders"}
                }
            }.attach()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}