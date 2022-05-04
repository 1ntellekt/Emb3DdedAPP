package com.example.emb3ddedapp.cache.dao

import androidx.lifecycle.LiveData
import androidx.room.*
import com.example.emb3ddedapp.cache.entities.UserEntity

@Dao
interface UserDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun addUser(userEntity: UserEntity)

    @Query("SELECT * FROM Users")
    fun getAllUsers():LiveData<List<UserEntity>>

    @Query("SELECT * FROM Users Where id = :id")
    fun getUserById(id:Int):LiveData<UserEntity>

    @Delete
    suspend fun deleteUser(userEntity: UserEntity)

    @Query("DELETE FROM Users")
    suspend fun deleteAll()

}