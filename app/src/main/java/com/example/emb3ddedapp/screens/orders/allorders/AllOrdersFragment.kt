package com.example.emb3ddedapp.screens.orders.allorders

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.emb3ddedapp.R

class AllOrdersFragment : Fragment() {

    companion object {
        fun newInstance() = AllOrdersFragment()
    }

    private lateinit var viewModel: AllOrdersViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.all_orders_fragment, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProvider(this).get(AllOrdersViewModel::class.java)
        // TODO: Use the ViewModel
    }

}