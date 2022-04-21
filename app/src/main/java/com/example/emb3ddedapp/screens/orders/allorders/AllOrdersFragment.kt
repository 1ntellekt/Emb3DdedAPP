package com.example.emb3ddedapp.screens.orders.allorders

import android.app.Dialog
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import android.util.Log
import android.view.*
import androidx.appcompat.widget.SearchView
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.example.emb3ddedapp.R
import com.example.emb3ddedapp.databinding.AllOrdersFragmentBinding
import com.example.emb3ddedapp.databinding.FilterOrdersLayoutBinding
import com.example.emb3ddedapp.models.Order
import com.example.emb3ddedapp.notification.FireServices
import com.example.emb3ddedapp.screens.orders.allorders.adapter.AllOrdersAdapter
import com.example.emb3ddedapp.utils.*
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
            btnFilter.setOnClickListener {
                dialogFilterOrder()
            }
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

            searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener{
                override fun onQueryTextSubmit(query: String?): Boolean {
                    return false
                }
                override fun onQueryTextChange(newText: String?): Boolean {
                    filterOrders(newText)
                    return true
                }
            })

        }
        mObserver = Observer { list->
            list?.let { list1->
                val orders = if (sortByFirstNewDate){
                     list1.sortedByDescending {it.created_at!!}
                } else {
                    list1.sortedBy {it.created_at!!}
                }

                ordersList.clear()
                ordersList.addAll(orders)
                adapter.setData(orders)
            }
        }
        //Log.i("tagLife", "onViewCreated() on AllOrders")
    }

    private fun filterOrders(newText: String?) {
        if (newText != null){
            if (searchByAuthorName){
                adapter.setData(ordersList.filter { item->  item.user!!.login.contains(newText.lowercase(Locale.getDefault()), ignoreCase = true) })
            } else {
                adapter.setData(ordersList.filter { item->  item.title.contains(newText.lowercase(Locale.getDefault()), ignoreCase = true) })
            }
        } else {
            adapter.setData(ordersList)
        }
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

    //filters
    private var searchByAuthorName = true
    private var sortByFirstNewDate = true

    private fun dialogFilterOrder(){
        val dialog = context?.let { Dialog(it) }
        dialog?.let { dia->
            dia.requestWindowFeature(Window.FEATURE_NO_TITLE)
            val binding:FilterOrdersLayoutBinding = FilterOrdersLayoutBinding.inflate(dia.layoutInflater)
            dia.setContentView(binding.root)


            binding.apply {

                if (sortByFirstNewDate){
                    btnFirstNew.backgroundTintList = ContextCompat.getColorStateList(requireContext(),R.color.btn_active)
                    btnFirstOld.backgroundTintList = ContextCompat.getColorStateList(requireContext(),R.color.btn_inactive)
                } else {
                    btnFirstOld.backgroundTintList = ContextCompat.getColorStateList(requireContext(),R.color.btn_active)
                    btnFirstNew.backgroundTintList = ContextCompat.getColorStateList(requireContext(),R.color.btn_inactive)
                }

                if (searchByAuthorName){
                    btnSearchAuthor.backgroundTintList = ContextCompat.getColorStateList(requireContext(),R.color.btn_active)
                    btnSearchTitle.backgroundTintList = ContextCompat.getColorStateList(requireContext(),R.color.btn_inactive)
                } else {
                    btnSearchTitle.backgroundTintList = ContextCompat.getColorStateList(requireContext(),R.color.btn_active)
                    btnSearchAuthor.backgroundTintList = ContextCompat.getColorStateList(requireContext(),R.color.btn_inactive)
                }

                btnFirstNew.setOnClickListener {
                    if (sortByFirstNewDate) return@setOnClickListener
                    sortByFirstNewDate = true
                    btnFirstNew.backgroundTintList = ContextCompat.getColorStateList(requireContext(),R.color.btn_active)
                    btnFirstOld.backgroundTintList = ContextCompat.getColorStateList(requireContext(),R.color.btn_inactive)
                }
                btnFirstOld.setOnClickListener {
                    if (!sortByFirstNewDate) return@setOnClickListener
                    sortByFirstNewDate = false
                    btnFirstOld.backgroundTintList = ContextCompat.getColorStateList(requireContext(),R.color.btn_active)
                    btnFirstNew.backgroundTintList = ContextCompat.getColorStateList(requireContext(),R.color.btn_inactive)
                }
                btnSearchAuthor.setOnClickListener {
                    if (searchByAuthorName) return@setOnClickListener
                    searchByAuthorName = true
                    btnSearchAuthor.backgroundTintList = ContextCompat.getColorStateList(requireContext(),R.color.btn_active)
                    btnSearchTitle.backgroundTintList = ContextCompat.getColorStateList(requireContext(),R.color.btn_inactive)
                }
                btnSearchTitle.setOnClickListener {
                    if (!searchByAuthorName) return@setOnClickListener
                    searchByAuthorName = false
                    btnSearchTitle.backgroundTintList = ContextCompat.getColorStateList(requireContext(),R.color.btn_active)
                    btnSearchAuthor.backgroundTintList = ContextCompat.getColorStateList(requireContext(),R.color.btn_inactive)
                }
            }

            dia.show()
            dia.window?.setLayout(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT
            )
            dia.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
            dia.window?.attributes?.windowAnimations = R.style.DialogAnimation
            dia.window?.setGravity(Gravity.BOTTOM)
        }
    }

}