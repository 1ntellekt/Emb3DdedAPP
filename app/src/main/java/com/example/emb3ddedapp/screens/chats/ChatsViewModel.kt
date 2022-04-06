package com.example.emb3ddedapp.screens.chats

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.emb3ddedapp.models.Chat
import com.example.emb3ddedapp.models.ChatsByUserResponse
import com.example.emb3ddedapp.utils.REPOSITORY
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class ChatsViewModel(application: Application) : AndroidViewModel(application) {

        val chatsList:MutableLiveData<List<Chat>?> = MutableLiveData()

        fun getChatsByUser(user_id:Int) {
            REPOSITORY.getChatsByUserId(user_id)
            .enqueue(object : Callback<ChatsByUserResponse>{
                override fun onResponse(call: Call<ChatsByUserResponse>, response: Response<ChatsByUserResponse>) {
                    if (response.isSuccessful){
                        response.body()?.let {
                            chatsList.postValue(it.chats)
                        }
                    } else {
                        chatsList.postValue(null)
                        Log.i("tagAPI","Error get user chats: ${response.code()}")
                    }
                }
                override fun onFailure(call: Call<ChatsByUserResponse>, t: Throwable) {
                    chatsList.postValue(null)
                    Log.i("tagAPI","Error get user chats: ${t.message.toString()}")
                }
            })
        }

}