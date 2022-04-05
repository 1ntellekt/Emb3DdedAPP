package com.example.emb3ddedapp.screens.orders.myorders

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.example.emb3ddedapp.R
import com.example.emb3ddedapp.databinding.AllOrdersFragmentBinding
import com.example.emb3ddedapp.databinding.MyOrdersFragmentBinding
import com.example.emb3ddedapp.models.CurrUser
import com.example.emb3ddedapp.models.Order
import com.example.emb3ddedapp.notification.FireServices
import com.example.emb3ddedapp.screens.orders.myorders.adapter.MyOrdersAdapter
import com.example.emb3ddedapp.utils.APP
import com.example.emb3ddedapp.utils.showToast

class MyOrdersFragment : Fragment() {

    private lateinit var viewModel: MyOrdersViewModel
    private var _binding: MyOrdersFragmentBinding? = null
    private val binding: MyOrdersFragmentBinding
    get() = _binding!!

    private lateinit var adapter:MyOrdersAdapter
    private val ordersList = mutableListOf<Order>()
    private lateinit var mObserver:Observer<List<Order>?>

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        _binding = MyOrdersFragmentBinding.inflate(inflater,container,false)
        return binding.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProvider(this).get(MyOrdersViewModel::class.java)
        // TODO: Use the ViewModel
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        adapter = MyOrdersAdapter({deleteId->
            viewModel.deleteOrder(ordersList[deleteId].id)
        },{editId->
            val order = ordersList[editId]
            val args = Bundle().also { it.putSerializable("order",order) }
            APP.mNavController.navigate(R.id.action_mainFragment_to_pageOrderEditFragment,args)
        },{doneId->
            val order = ordersList[doneId]
            viewModel.updateOrder(order.copy(status = 1)){}
        })
        binding.apply {
            recyclerView.setHasFixedSize(true)
            recyclerView.adapter = adapter
            btnAddOrders.setOnClickListener {
                APP.mNavController.navigate(R.id.action_mainFragment_to_pageOrderEditFragment)
            }
        }
        mObserver = Observer { list->
            list?.let {
                ordersList.clear()
                ordersList.addAll(it)
                adapter.setData(it)
                if (it.isNotEmpty()) binding.tvHint.visibility = View.GONE
                else binding.tvHint.visibility = View.VISIBLE
            }
        }
        Log.i("tagLife", "onViewCreated() on MyOrders")
    }

    private val refLayListener = SwipeRefreshLayout.OnRefreshListener {
        viewModel.getUserOrders(CurrUser.id)
        binding.refLayout.isRefreshing = false
    }

    override fun onStart() {
        super.onStart()
        viewModel.getUserOrders(CurrUser.id)
        viewModel.myOrdersList.observe(this,mObserver)
        //showToast("MYOrders")
       // Log.i("tagLife", "onStart() on MyOrders")
        binding.refLayout.setOnRefreshListener(refLayListener)


        val intentFilter = IntentFilter()
        intentFilter.addAction(FireServices.PUSH_TAG)
        requireActivity().registerReceiver(broadcastReceiver,intentFilter)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        viewModel.myOrdersList.removeObserver(mObserver)
        requireActivity().unregisterReceiver(broadcastReceiver)
        _binding = null
    }

    private val broadcastReceiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context?, intent: Intent?) {
            if (intent?.extras!=null){
                val extras = intent.extras
                Log.d("msg","Message received!")
                extras?.keySet()?.firstOrNull{it == FireServices.KEY_ACTION}?.let { key->
                    Log.d("msg","key: $key")
                    when(extras.getString(key)){
                        FireServices.ACTION_ORDER ->{
                            refLayListener.onRefresh()
                        }
                        else ->{
                            Log.d("msg","key error not found")}
                    }
                }
            }
        }
    }

}