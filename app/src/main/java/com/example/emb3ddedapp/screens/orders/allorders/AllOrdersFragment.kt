package com.example.emb3ddedapp.screens.orders.allorders

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
import androidx.core.content.ContextCompat
import androidx.lifecycle.Observer
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.example.emb3ddedapp.R
import com.example.emb3ddedapp.databinding.AllOrdersFragmentBinding
import com.example.emb3ddedapp.models.Order
import com.example.emb3ddedapp.notification.FireServices
import com.example.emb3ddedapp.screens.orders.allorders.adapter.AllOrdersAdapter
import com.example.emb3ddedapp.utils.*
import java.text.SimpleDateFormat
import java.util.*

class AllOrdersFragment : Fragment() {

    private lateinit var viewModel: AllOrdersViewModel
    private var _binding:AllOrdersFragmentBinding? = null
    private val binding:AllOrdersFragmentBinding
    get() = _binding!!

    private val ordersList = mutableListOf<Order>()
    private lateinit var mObserver:Observer<List<Order>?>
    private lateinit var adapter:AllOrdersAdapter

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        _binding = AllOrdersFragmentBinding.inflate(inflater,container,false)
        return binding.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProvider(this).get(AllOrdersViewModel::class.java)
        // TODO: Use the ViewModel
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        adapter = AllOrdersAdapter { posItem ->
            val order = ordersList[posItem]
            val args = Bundle().also { it.putSerializable("order", order) }
            APP.mNavController.navigate(R.id.action_mainFragment_to_pageOrderFragment,args)
        }
        binding.apply {
            recyclerView.setHasFixedSize(true)
            recyclerView.adapter = adapter
            btnFilter.setOnClickListener {  }
/*            btnNewFirst.setOnClickListener {
                val list = ordersList.sortedByDescending { getDataTimeWithFormat(it.created_at!!)}
                adapter.setData(list)
                btnOldFirst.backgroundTintList = ContextCompat.getColorStateList(requireContext(),R.color.btn_inactive)
                btnNewFirst.backgroundTintList = ContextCompat.getColorStateList(requireContext(),R.color.btn_active)
            }
            btnOldFirst.setOnClickListener {
                val list = ordersList.sortedBy {getDataTimeWithFormat(it.created_at!!)}
                adapter.setData(list)
                btnOldFirst.backgroundTintList = ContextCompat.getColorStateList(requireContext(),R.color.btn_active)
                btnNewFirst.backgroundTintList = ContextCompat.getColorStateList(requireContext(),R.color.btn_inactive)
            }*/
            refLayout.setOnRefreshListener(refLayListener)
        }
        mObserver = Observer { list->
            list?.let {
                ordersList.clear()
                ordersList.addAll(it)
                adapter.setData(it)
            }
        }
        //Log.i("tagLife", "onViewCreated() on AllOrders")
    }

    private val refLayListener = SwipeRefreshLayout.OnRefreshListener {
        viewModel.getAllOrders()
        binding.refLayout.isRefreshing = false
    }

    override fun onStart() {
        super.onStart()
        viewModel.getAllOrders()
        viewModel.allOrdersList.observe(this,mObserver)
        //showToast("ALLOrders")
        //Log.i("tagLife", "onStart() on AllOrders")


        val intentFilter = IntentFilter()
         intentFilter.addAction(FireServices.PUSH_TAG)
         requireActivity().registerReceiver(broadcastReceiver,intentFilter)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        viewModel.allOrdersList.removeObserver(mObserver)
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