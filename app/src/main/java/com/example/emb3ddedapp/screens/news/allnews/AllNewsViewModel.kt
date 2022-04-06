package com.example.emb3ddedapp.screens.news.allnews

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.emb3ddedapp.models.NewsByUserResponse
import com.example.emb3ddedapp.models.NewsItem
import com.example.emb3ddedapp.utils.REPOSITORY
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class AllNewsViewModel(application: Application) : AndroidViewModel(application) {

    val allNewsList:MutableLiveData<List<NewsItem>?> = MutableLiveData()

    fun getAllNews() {
        REPOSITORY.getAllNews().enqueue(object : Callback<NewsByUserResponse> {
            override fun onResponse(call: Call<NewsByUserResponse>, response: Response<NewsByUserResponse>) {
                if (response.isSuccessful){
                    response.body()?.let {
                        allNewsList.postValue(it.news)
                    }
                } else {
                    allNewsList.postValue(null)
                    Log.i("tagAPI","Error all news: ${response.code()}")
                }
            }
            override fun onFailure(call: Call<NewsByUserResponse>, t: Throwable) {
                allNewsList.postValue(null)
                Log.i("tagAPI","Error all news: ${t.message.toString()}")
            }
        })
    }

}