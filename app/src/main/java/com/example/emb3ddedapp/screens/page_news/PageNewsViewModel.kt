package com.example.emb3ddedapp.screens.page_news

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.ViewModel
import com.example.emb3ddedapp.models.Rating
import com.example.emb3ddedapp.utils.REPOSITORY
import com.example.emb3ddedapp.utils.showToast

class PageNewsViewModel(application: Application) : AndroidViewModel(application) {

    fun addMark(rating:Rating, onSuccess:()->Unit){
        REPOSITORY.addUppMark(rating, {
            onSuccess()
        },
        {
            showToast(it)
        })
    }

    fun getMark(newsItemId:Int, onSuccess: (Double) -> Unit){
        REPOSITORY.getUserMark(news_items_id = newsItemId,
            {
               onSuccess(it)
            },
            {
               // showToast(it)
            })
    }

}