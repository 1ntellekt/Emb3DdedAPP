package com.example.emb3ddedapp.screens.page_chat

import android.app.Application
import android.net.DnsResolver
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.emb3ddedapp.models.ChatDefault
import com.example.emb3ddedapp.models.ChatMessagesByChatResponse
import com.example.emb3ddedapp.models.CurrUser
import com.example.emb3ddedapp.models.Message
import com.example.emb3ddedapp.utils.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.File

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

    fun sendFileMsg(file: File, chatId: Int, recipientId:Int, onSuccess:(String)->Unit, onFail:()->Unit){
            showProgressDialog("Uploading file...")
            REPOSITORY.uploadFile(file,"chat_file",
                { pathFile->

                    REPOSITORY.addMessage(Message(chat_id = chatId, file_msg = "$DOWNLOAD_FILE_URL$pathFile", user_id_sender = CurrUser.id, user_id_recepient = recipientId),{
                        closeProgressDialog()
                        onSuccess("$DOWNLOAD_FILE_URL$pathFile")
                    },{
                        closeProgressDialog()
                        showToast(it)
                    })

                },
                {
                    closeProgressDialog()
                    showToast(it)
                    onFail()
                })
    }

    fun send3dFile(file: File, chatId: Int, recipientId:Int, onSuccess:(String)->Unit, onFail:()->Unit) {
        showProgressDialog("Uploading 3d file...")
        REPOSITORY.uploadFile(file,"chat_3d_file",
            { pathFile->

                REPOSITORY.addMessage(Message(chat_id = chatId, file_3d_msg = "$DOWNLOAD_FILE_URL$pathFile", user_id_sender = CurrUser.id, user_id_recepient = recipientId),{
                    closeProgressDialog()
                    onSuccess("$DOWNLOAD_FILE_URL$pathFile")
                },{
                    closeProgressDialog()
                    showToast(it)
                })

            },
            {
                closeProgressDialog()
                showToast(it)
                onFail()
            })
    }

    fun sendImage(file: File, chatId: Int, recipientId:Int, onSuccess:(String)->Unit, onFail:()->Unit) {
        showProgressDialog("Uploading image...")
        REPOSITORY.uploadFile(file,"chat_img",
            { pathFile->

                REPOSITORY.addMessage(Message(chat_id = chatId, img_msg = "$CONTENT_FILE_URL$pathFile", user_id_sender = CurrUser.id, user_id_recepient = recipientId),{
                    closeProgressDialog()
                    onSuccess("$DOWNLOAD_FILE_URL$pathFile")
                },{
                    closeProgressDialog()
                    showToast(it)
                })

            },
            {
                closeProgressDialog()
                showToast(it)
                onFail()
            })
    }

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