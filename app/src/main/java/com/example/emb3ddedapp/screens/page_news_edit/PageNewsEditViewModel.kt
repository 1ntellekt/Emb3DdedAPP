package com.example.emb3ddedapp.screens.page_news_edit

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import com.example.emb3ddedapp.database.api.RetrofitInstance
import com.example.emb3ddedapp.models.NewsItem
import com.example.emb3ddedapp.models.StatusMsgPath
import com.example.emb3ddedapp.utils.*
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody
import okhttp3.RequestBody.Companion.asRequestBody
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.File


class PageNewsEditViewModel(application: Application) : AndroidViewModel(application) {

    fun editNewsItem(newsItem: NewsItem, onSuccess:()->Unit){
        REPOSITORY.updateNewsItem(id = newsItem.id, newsItem = newsItem, {onSuccess()}, { showToast(it)})
    }

    fun addNewsItem(newsItem: NewsItem, onSuccess: () -> Unit){
        REPOSITORY.addNewsItem(newsItem, {onSuccess()}, { showToast(it)})
    }

    fun uploadFile(file: File, onSuccess: (String) -> Unit, onFail:()->Unit){
        REPOSITORY.uploadFile(file,"news_img",
        {
            onSuccess("$CONTENT_FILE_URL$it")
        },
        {
            showToast(it)
            onFail()
        })
    }

}