package com.example.emb3ddedapp.cache.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.emb3ddedapp.cache.dao.UserDao
import com.example.emb3ddedapp.cache.entities.UserEntity

@Database(entities = [UserEntity::class], exportSchema = false, version = 1)
abstract class LocalDB:RoomDatabase() {

    companion object{
        private var INSTANCE:LocalDB? = null

        @Synchronized
        fun getLocalDB(context: Context):LocalDB {
            if (INSTANCE == null){
               INSTANCE = Room.databaseBuilder(context, LocalDB::class.java,"LocalDB.db").build()
            }
            return INSTANCE!!
        }
    }

    abstract fun userDao():UserDao

}