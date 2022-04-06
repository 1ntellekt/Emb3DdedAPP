package com.example.emb3ddedapp.screens.page_order_edit

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.widget.doOnTextChanged
import androidx.navigation.NavOptions
import com.bumptech.glide.Glide
import com.example.emb3ddedapp.R
import com.example.emb3ddedapp.databinding.PageOrderEditFragmentBinding
import com.example.emb3ddedapp.models.CurrUser
import com.example.emb3ddedapp.models.Order
import com.example.emb3ddedapp.utils.APP
import com.example.emb3ddedapp.utils.showToast
import com.google.android.material.appbar.AppBarLayout
import java.text.SimpleDateFormat
import java.util.*

class PageOrderEditFragment : Fragment() {

    private var _binding: PageOrderEditFragmentBinding? = null
    private val binding: PageOrderEditFragmentBinding
        get() = _binding!!

    private lateinit var viewModel: PageOrderEditViewModel

    private var curOrder:Order? = null
    private var selectedImg:String? = null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        _binding = PageOrderEditFragmentBinding.inflate(inflater,container,false)
        return binding.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProvider(this).get(PageOrderEditViewModel::class.java)
        // TODO: Use the ViewModel
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.apply {
            appbar.addOnOffsetChangedListener(object : AppBarLayout.OnOffsetChangedListener {
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

            curOrder = arguments?.getSerializable("order") as Order?
            curOrder?.let {
                edDescription.setText(it.description)
                edTitle.setText(it.title)
                tvHeadTitle.text = it.title
                it.img_url?.let { url->
                    Glide.with(requireContext()).load(url).into(imgOrder)
                    selectedImg = url
                }
                //tvAuthor.text = curOrder!!.user!!.login
            }

            tvAuthor.text = CurrUser.login

            btnBack.setOnClickListener {
                APP.mNavController.navigate(R.id.action_pageOrderEditFragment_to_mainFragment)
            }

            edDescription.doOnTextChanged { text, start, before, count -> edDescriptionLayout.error = null}
            edTitle.doOnTextChanged { text, start, before, count -> edTitleLayout.error = null }

            btnPopMenu.setOnClickListener {

                if (edTitle.text.toString().isEmpty()){
                    edTitleLayout.error = "Input title!"
                } else if(edDescription.text.toString().isEmpty()){
                    edDescriptionLayout.error = "Input description"
                } else {
                    if (curOrder == null) {
                        viewModel.addOrder(Order(title = edTitle.text.toString(),
                            description = edDescription.text.toString(), user_id = CurrUser.id, created_at = null, img_url = selectedImg)
                        ){ APP.mNavController.navigate(R.id.action_pageOrderEditFragment_to_mainFragment)}
                    } else {
                        viewModel.updateOrder(Order(id = curOrder!!.id,title = edTitle.text.toString(),
                            description = edDescription.text.toString(), user_id = CurrUser.id, created_at = null, img_url = selectedImg, status = 0)
                        ){ APP.mNavController.navigate(R.id.action_pageOrderEditFragment_to_mainFragment)}
                    }
                }

            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}