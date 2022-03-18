package com.example.emb3ddedapp.screens.orders.myorders

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.emb3ddedapp.R

class MyOrdersFragment : Fragment() {

    companion object {
        fun newInstance() = MyOrdersFragment()
    }

    private lateinit var viewModel: MyOrdersViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.my_orders_fragment, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProvider(this).get(MyOrdersViewModel::class.java)
        // TODO: Use the ViewModel
    }

}