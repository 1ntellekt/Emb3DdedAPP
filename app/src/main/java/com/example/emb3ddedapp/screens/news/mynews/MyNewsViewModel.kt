package com.example.emb3ddedapp.screens.news.mynews

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.emb3ddedapp.models.NewsByUserResponse
import com.example.emb3ddedapp.models.NewsItem
import com.example.emb3ddedapp.utils.IS_CONNECT_INTERNET
import com.example.emb3ddedapp.utils.REPOSITORY
import com.example.emb3ddedapp.utils.showToast
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MyNewsViewModel(application: Application) : AndroidViewModel(application) {

    val myNewsList:MutableLiveData<List<NewsItem>?> = MutableLiveData()

    fun getNewsByUserId(user_id:Int){
        if (IS_CONNECT_INTERNET)
        REPOSITORY.getNewsByUserId(user_id).enqueue(object : Callback<NewsByUserResponse>{
            override fun onResponse(call: Call<NewsByUserResponse>, response: Response<NewsByUserResponse>) {
                if (response.isSuccessful){
                    response.body()?.let {
                        myNewsList.postValue(it.news)
                    }
                } else {
                    myNewsList.postValue(null)
                    Log.i("tagAPI","Error get user news: ${response.code()}")
                }
            }
            override fun onFailure(call: Call<NewsByUserResponse>, t: Throwable) {
                myNewsList.postValue(null)
                Log.i("tagAPI","Error get user news: ${t.message.toString()}")
            }
        })
    }

    fun deleteNewsItem(id:Int){
        REPOSITORY.deleteNewsItem(id,{},{ showToast(it)})
    }

}