package com.example.emb3ddedapp.cache.dao

import androidx.lifecycle.LiveData
import androidx.room.*
import com.example.emb3ddedapp.cache.entities.NewsEntity
import com.example.emb3ddedapp.cache.entities.OrderEntity

@Dao
interface OrderDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun addOrder(orderEntity: OrderEntity)

    @Query("SELECT * FROM Orders")
    suspend fun getAllOrders(): LiveData<List<OrderEntity>?>

    @Query("SELECT * FROM Orders Where id = :id")
    suspend fun getOrderById(id:Int): LiveData<OrderEntity?>

    @Delete
    suspend fun deleteOrder(orderEntity: OrderEntity)

}