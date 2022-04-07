package com.example.emb3ddedapp.screens.page_chat

import android.app.Application
import android.net.DnsResolver
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.emb3ddedapp.models.ChatDefault
import com.example.emb3ddedapp.models.ChatMessagesByChatResponse
import com.example.emb3ddedapp.models.Message
import com.example.emb3ddedapp.utils.REPOSITORY
import com.example.emb3ddedapp.utils.showToast
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class PageChatViewModel(application: Application) : AndroidViewModel(application) {

    val messagesList:MutableLiveData<List<Message>?> = MutableLiveData()
    val chatDef:MutableLiveData<ChatDefault?> = MutableLiveData()

    fun getMessagesByChatId(chatId:Int){
        REPOSITORY.getMessagesByChatId(chatId)
         .enqueue(object: Callback<ChatMessagesByChatResponse>{
           override fun onResponse(call: Call<ChatMessagesByChatResponse>, response: Response<ChatMessagesByChatResponse>) {
               if (response.isSuccessful){
                   response.body()?.let {
                       messagesList.postValue(it.messages)
                       chatDef.postValue(it.chat)
                   }
               } else {
                   messagesList.postValue(null)
                   chatDef.postValue(null)
                   Log.i("tagAPI","Error get messages by chatId: ${response.code()}")
               }
           }
           override fun onFailure(call: Call<ChatMessagesByChatResponse>, t: Throwable) {
               messagesList.postValue(null)
               chatDef.postValue(null)
               Log.i("tagAPI","Error get messages by chatId: ${t.message.toString()}")
           }
         })
    }

    fun sendTextMsg(msg:Message){
        REPOSITORY.addMessage(msg,{},{ showToast(it)})
    }
//
//    fun sendFileMsg(){
//
//    }
//
//    fun send3dFile(){
//
//    }
//
//    fun getAccessToDownload3dFile(){
//
//    }
//
//    fun downloadFile(){
//
//    }
//
//    fun download3dFile(){
//
//    }

}