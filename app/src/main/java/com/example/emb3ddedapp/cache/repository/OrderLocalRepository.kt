package com.example.emb3ddedapp.cache.repository

import androidx.lifecycle.LiveData
import com.example.emb3ddedapp.cache.dao.OrderDao
import com.example.emb3ddedapp.cache.entities.OrderEntity
import com.example.emb3ddedapp.cache.entities.UserEntity

class OrderLocalRepository(private val orderDao: OrderDao) {

    suspend fun addOrder(orderEntity: OrderEntity){
        orderDao.addOrder(orderEntity)
    }

    suspend fun deleteOrder(orderEntity: OrderEntity){
        orderDao.deleteOrder(orderEntity)
    }

    suspend fun getOrderById(id:Int): LiveData<OrderEntity?> {
        return orderDao.getOrderById(id)
    }

    suspend fun getAllUsers(): LiveData<List<OrderEntity>?> {
        return orderDao.getAllOrders()
    }

}