package com.example.emb3ddedapp.cache.repository

import androidx.lifecycle.LiveData
import com.example.emb3ddedapp.cache.dao.UserDao
import com.example.emb3ddedapp.cache.entities.UserEntity

class UserLocalRepository(private val userDao: UserDao) {

    suspend fun addUser(userEntity: UserEntity){
        userDao.addUser(userEntity)
    }

    suspend fun deleteUser(userEntity: UserEntity){
        userDao.deleteUser(userEntity)
    }

    suspend fun getUserById(id:Int):LiveData<UserEntity?>{
        return userDao.getUserById(id)
    }

    suspend fun getAllUsers():LiveData<List<UserEntity>?>{
        return userDao.getAllUsers()
    }

}