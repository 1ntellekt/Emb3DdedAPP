package com.example.emb3ddedapp.screens.orders.myorders

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.emb3ddedapp.database.api.RetrofitInstance
import com.example.emb3ddedapp.models.Order
import com.example.emb3ddedapp.models.OrdersByUserResponse
import com.example.emb3ddedapp.utils.REPOSITORY
import com.example.emb3ddedapp.utils.showToast
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MyOrdersViewModel(application: Application) : AndroidViewModel(application) {

    val myOrdersList:MutableLiveData<List<Order>?> = MutableLiveData()

    fun getUserOrders(user_id:Int){
        RetrofitInstance.api.getOrdersByUser(user_id)
            .enqueue(object : Callback<OrdersByUserResponse> {
                override fun onResponse(call: Call<OrdersByUserResponse>, response: Response<OrdersByUserResponse>) {
                    if (response.isSuccessful){
                        response.body()?.let { body->
                            myOrdersList.postValue(body.orders)
                        }
                    } else {
                        myOrdersList.postValue(null)
                        Log.i("tagAPI","Error get user orders: ${response.code()}")
                    }
                }
                override fun onFailure(call: Call<OrdersByUserResponse>, t: Throwable) {
                   myOrdersList.postValue(null)
                    Log.i("tagAPI","Error get user orders: ${t.message.toString()}")
                }
            })
    }

    fun deleteOrder(id:Int){
        REPOSITORY.deleteOrder(id,{},{ showToast(it)})
    }

    fun updateOrder(order: Order,onSuccess: () -> Unit){
        REPOSITORY.updateOrder(id = order.id, order, {onSuccess()}, { showToast(it)})
    }

}