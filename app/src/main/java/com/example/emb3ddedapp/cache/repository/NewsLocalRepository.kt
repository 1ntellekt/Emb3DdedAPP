package com.example.emb3ddedapp.cache.repository

import androidx.lifecycle.LiveData
import com.example.emb3ddedapp.cache.dao.NewsDao
import com.example.emb3ddedapp.cache.entities.NewsEntity

class NewsLocalRepository(private val newsDao: NewsDao) {

    suspend fun addNews(newsEntity: NewsEntity){
        newsDao.addNewsItem(newsEntity)
    }

    suspend fun deleteNews(newsEntity: NewsEntity){
        newsDao.deleteNewsItem(newsEntity)
    }

    suspend fun getNewsById(id:Int): LiveData<NewsEntity?> {
        return newsDao.getNewsItemById(id)
    }

    suspend fun getAllNews(): LiveData<List<NewsEntity>?> {
        return newsDao.getAllNews()
    }

}