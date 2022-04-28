package com.example.emb3ddedapp.screens.page_news

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.emb3ddedapp.models.MarksByNewsItemResponse
import com.example.emb3ddedapp.models.Rating
import com.example.emb3ddedapp.models.RatingL
import com.example.emb3ddedapp.utils.REPOSITORY
import com.example.emb3ddedapp.utils.showToast
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class PageNewsViewModel(application: Application) : AndroidViewModel(application) {

    val ratingList:MutableLiveData<List<RatingL>?> = MutableLiveData()

    fun addMark(rating:Rating, onSuccess:()->Unit){
        REPOSITORY.addUppMark(rating, {
            onSuccess()
        },
        {
            showToast(it)
        })
    }

    fun getMark(newsItemId:Int, onSuccess: (Rating) -> Unit){
        REPOSITORY.getUserMark(news_items_id = newsItemId,
            {
               onSuccess(it)
            },
            {
               // showToast(it)
            })
    }

    fun getMarksByNews(newsItemId: Int){
        REPOSITORY.getMarksByNewsId(newsItemId).enqueue(object : Callback<MarksByNewsItemResponse>{
            override fun onResponse(call: Call<MarksByNewsItemResponse>, response: Response<MarksByNewsItemResponse>) {
                if (response.isSuccessful){
                    response.body()?.let { body->
                        ratingList.postValue(body.marks)
                    }
                } else {
                    ratingList.postValue(null)
                    Log.i("tagAPI","Error get marks by newsId: ${response.code()}")
                }
            }
            override fun onFailure(call: Call<MarksByNewsItemResponse>, t: Throwable) {
                ratingList.postValue(null)
                Log.i("tagAPI","Error get marks by newsId: ${t.message.toString()}")
            }
        })
    }

}