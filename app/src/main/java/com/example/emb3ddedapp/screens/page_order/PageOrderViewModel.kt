package com.example.emb3ddedapp.screens.page_order

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.ViewModel
import com.example.emb3ddedapp.models.Chat
import com.example.emb3ddedapp.utils.REPOSITORY
import com.example.emb3ddedapp.utils.showToast

class PageOrderViewModel(application: Application) : AndroidViewModel(application) {

    fun createChat(user_id_first:Int, user_id_second:Int, onSuccess:(Chat)->Unit) {
        REPOSITORY.addNewChat(user_id_first = user_id_first, user_id_second = user_id_second, {onSuccess(it)}, { showToast(it)})
    }

}