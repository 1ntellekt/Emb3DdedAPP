package com.example.emb3ddedapp.models

import java.io.Serializable

data class Order(
    val id: Int = 0,
    val title: String,
    val description: String,
    val user_id: Int,
    val img_url: String? = null,
    val status: Int = 0,
    val created_at: String?,
    val user: User? = null
):Serializable

data class OrdersList(val orders:List<Order>?)

//get all orders by user_id
data class OrdersByUserResponse(val status: Boolean, val message:String,val orders: List<Order>?)

// get order by id
data class OrderDefaultResponse(val status: Boolean, val message:String, val order: Order)

