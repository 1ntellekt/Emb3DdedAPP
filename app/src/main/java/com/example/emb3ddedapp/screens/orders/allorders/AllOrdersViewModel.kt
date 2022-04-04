package com.example.emb3ddedapp.screens.orders.allorders

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.emb3ddedapp.models.Order
import com.example.emb3ddedapp.models.OrdersByUserResponse
import com.example.emb3ddedapp.utils.REPOSITORY
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class AllOrdersViewModel(application: Application) : AndroidViewModel(application) {

    val allOrdersList:MutableLiveData<List<Order>?> = MutableLiveData()

    fun getAllOrders(){
        REPOSITORY.getAllOrders().enqueue(object : Callback<OrdersByUserResponse>{
            override fun onResponse(call: Call<OrdersByUserResponse>, response: Response<OrdersByUserResponse>) {
                if (response.isSuccessful){
                    response.body()?.let { body->
                        allOrdersList.postValue(body.orders)
                    }
                } else {
                    allOrdersList.postValue(null)
                    Log.i("tagAPI","Error get all orders: ${response.code()}")
                }
            }
            override fun onFailure(call: Call<OrdersByUserResponse>, t: Throwable) {
                allOrdersList.postValue(null)
                Log.i("tagAPI","Error all orders: ${t.message.toString()}")
            }
        })
    }

}