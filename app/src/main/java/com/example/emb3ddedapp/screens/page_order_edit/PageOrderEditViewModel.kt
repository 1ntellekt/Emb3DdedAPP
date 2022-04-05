package com.example.emb3ddedapp.screens.page_order_edit

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.ViewModel
import com.example.emb3ddedapp.models.Order
import com.example.emb3ddedapp.utils.REPOSITORY
import com.example.emb3ddedapp.utils.showToast

class PageOrderEditViewModel(application: Application) : AndroidViewModel(application) {

    fun addOrder(order:Order,onSuccess:()->Unit){
        REPOSITORY.addOrder(order,{onSuccess()},{ showToast(it)})
    }

    fun updateOrder(order: Order,onSuccess: () -> Unit){
        REPOSITORY.updateOrder(id = order.id, order, {onSuccess()}, { showToast(it)})
    }

}