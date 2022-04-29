package com.example.emb3ddedapp.cache.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.emb3ddedapp.cache.dao.NewsDao
import com.example.emb3ddedapp.cache.dao.OrderDao
import com.example.emb3ddedapp.cache.dao.UserDao
import com.example.emb3ddedapp.cache.entities.NewsEntity
import com.example.emb3ddedapp.cache.entities.OrderEntity
import com.example.emb3ddedapp.cache.entities.UserEntity

@Database(entities = [UserEntity::class, NewsEntity::class, OrderEntity::class], exportSchema = false, version = 1)
abstract class LocalDB:RoomDatabase() {

    companion object{
        private var INSTANCE:LocalDB? = null

        @Synchronized
        fun getLocalDB(context: Context):LocalDB? {
            if (INSTANCE == null){
               INSTANCE = Room.databaseBuilder(context, LocalDB::class.java,"LocalDB").build()
            }
            return INSTANCE
        }
    }

    abstract fun userDao():UserDao
    abstract fun newsDao():NewsDao
    abstract fun orderDao():OrderDao

}