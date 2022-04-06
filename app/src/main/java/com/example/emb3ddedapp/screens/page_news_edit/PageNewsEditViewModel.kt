package com.example.emb3ddedapp.screens.page_news_edit

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.ViewModel
import com.example.emb3ddedapp.models.NewsItem
import com.example.emb3ddedapp.utils.REPOSITORY
import com.example.emb3ddedapp.utils.showToast

class PageNewsEditViewModel(application: Application) : AndroidViewModel(application) {

    fun editNewsItem(newsItem: NewsItem, onSuccess:()->Unit){
        REPOSITORY.updateNewsItem(id = newsItem.id, newsItem = newsItem, {onSuccess()}, { showToast(it)})
    }

    fun addNewsItem(newsItem: NewsItem, onSuccess: () -> Unit){
        REPOSITORY.addNewsItem(newsItem, {onSuccess()}, { showToast(it)})
    }

}