package com.example.emb3ddedapp.screens.page_order

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.bumptech.glide.Glide
import com.example.emb3ddedapp.R
import com.example.emb3ddedapp.databinding.PageOrderFragmentBinding
import com.example.emb3ddedapp.models.CurrUser
import com.example.emb3ddedapp.models.Order
import com.example.emb3ddedapp.utils.APP
import com.example.emb3ddedapp.utils.getDataTimeWithFormat
import com.google.android.material.appbar.AppBarLayout
import java.text.SimpleDateFormat
import java.util.*

class PageOrderFragment : Fragment() {

    private lateinit var viewModel: PageOrderViewModel
    private var _binding:PageOrderFragmentBinding? = null
    private val binding:PageOrderFragmentBinding
    get() = _binding!!

    private var curOrder:Order? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        curOrder = arguments?.getSerializable("order") as? Order
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        _binding = PageOrderFragmentBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel = ViewModelProvider(this)[PageOrderViewModel::class.java]
        binding.apply {
            appbar.addOnOffsetChangedListener(object : AppBarLayout.OnOffsetChangedListener{
                var scrollRange = -1
                override fun onOffsetChanged(appBarLayout: AppBarLayout, verticalOffset: Int) {
                    //Initialize the size of the scroll
                    if (scrollRange == -1) {
                        scrollRange = appBarLayout.totalScrollRange
                    }
                    //Check if the view is collapsed
                    if (scrollRange + verticalOffset == 0) {
                        tvHeadTitle.visibility = View.VISIBLE
                    } else{
                        tvHeadTitle.visibility = View.INVISIBLE
                    }
                }
            })

            curOrder?.let { order->
                tvDescription.text = order.description
                tvAuthor.text = order.user!!.login
                tvHeadTitle.text = order.title
                tvDateTime.text = getDataTimeWithFormat(order.created_at!!)
                tvTitle.text = order.title
                order.img_url?.let { url->
                    Glide.with(imgOrder.context).load(url).into(imgOrder)
                }
                order.user.url_profile?.let { url->
                    Glide.with(imgPerson.context).load(url).into(imgPerson)
                }
                btnCallPhone.setOnClickListener {
                    //call to order's owner
                }
                btnMsg.setOnClickListener {
                    viewModel.createChat(CurrUser.id, order.user.id){ createdChat->
                        val args = Bundle()
                        args.putInt("id_chat", createdChat.id)
                        args.putSerializable("recipientUser", curOrder!!.user)
                        APP.mNavController.navigate(R.id.action_pageOrderFragment_to_pageChatFragment, args)
                    }
                }
            }
            btnBack.setOnClickListener { APP.mNavController.navigate(R.id.action_pageOrderFragment_to_mainFragment, Bundle().also { it.putString("nav", "orders") }) }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}