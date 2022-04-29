package com.example.emb3ddedapp.cache.dao

import androidx.lifecycle.LiveData
import androidx.room.*
import com.example.emb3ddedapp.cache.entities.NewsEntity

@Dao
interface NewsDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun addNewsItem(newsEntity: NewsEntity)

    @Query("SELECT * FROM News")
    suspend fun getAllNews():LiveData<List<NewsEntity>?>

    @Query("SELECT * FROM News Where id = :id")
    suspend fun getNewsItemById(id:Int):LiveData<NewsEntity?>

    @Delete
    suspend fun deleteNewsItem(newsEntity: NewsEntity)

}