package com.example.emb3ddedapp.database.repository

import com.example.emb3ddedapp.models.*
import com.google.firebase.auth.FirebaseUser
import retrofit2.Call

interface DataRepository {

    //auth

//    fun initDatabase()
//
    fun logInEmail(email:String,password:String,onSuccess:()->Unit,onFail:(String)->Unit)
    fun singUpEmail(email:String,password:String,onSuccess:()->Unit,onFail:(String)->Unit)
    fun sendVerifyEmail(user: FirebaseUser,password: String,onSuccess: () -> Unit, onFail: (String) -> Unit)
    fun resetPasswordEmail(email: String, onSuccess: () -> Unit, onFail: (String) -> Unit)

    //fun linkEmailToGoogle(email: String,password: String,onSuccess: () -> Unit,onFail: (String) -> Unit)
    //fun signUpLogInGoogle(isSignUp:Boolean,token:String,onSuccess: () -> Unit,onFail: (String) -> Unit)

    fun signOut(onSuccess: () -> Unit, onFail: (String) -> Unit)

    //user
   fun loginUser(uid:String,password: String,onSuccess:()->Unit,onFail:(String)->Unit)
   fun registerUser(uid:String,password: String,onSuccess:()->Unit,onFail:(String)->Unit)
    fun getCurrUser(onSuccess: () -> Unit, onFail: (String) -> Unit)
    fun resetPassword(uid: String, password: String, onSuccess: () -> Unit, onFail: (String) -> Unit)
    fun editCurrentUser(oldPassword:String?, password: String?, user: User?, onSuccess:()->Unit, onFail:(String)->Unit)

    //device
    fun addDevice(user_id:Int, nameDevice:String,onSuccess:()->Unit,onFail:(String)->Unit)
    fun deleteDevice(token:String,onSuccess:()->Unit,onFail:(String)->Unit)

    //orders
    fun addOrder(order:Order,onSuccess: () -> Unit, onFail: (String) -> Unit)
    fun updateOrder(id:Int, order: Order, onSuccess: () -> Unit, onFail: (String) -> Unit)
    fun deleteOrder(id: Int,onSuccess: () -> Unit,onFail: (String) -> Unit)
    fun getOrdersByUserId(user_id: Int):Call<OrdersByUserResponse>
    fun getOrderById(id: Int):Call<OrderDefaultResponse>
    fun getAllOrders():Call<OrdersByUserResponse>

    //news
    fun addNewsItem(newsItem: NewsItem,onSuccess: () -> Unit, onFail: (String) -> Unit)
    fun updateNewsItem(id: Int,newsItem: NewsItem,onSuccess: () -> Unit, onFail: (String) -> Unit)
    fun deleteNewsItem(id: Int, onSuccess: () -> Unit, onFail: (String) -> Unit)
    fun getNewsByUserId(user_id: Int):Call<NewsByUserResponse>
    fun getNewsItemById(id: Int):Call<NewsDefaultResponse>
    fun getAllNews():Call<NewsByUserResponse>

    //chats
    fun addNewChat(user_id_first:Int, user_id_second:Int, onSuccess: (Chat) -> Unit, onFail: (String) -> Unit)
    fun updateChat(id: Int, download_first:Int?, download_second:Int?,onSuccess: () -> Unit, onFail: (String) -> Unit)
    fun getChatsByUserId(user_id: Int):Call<ChatsByUserResponse>
    fun getMessagesByChatId(id: Int):Call<ChatMessagesByChatResponse>

    //message
    fun addMessage(msg:Message, onSuccess: () -> Unit, onFail: (String) -> Unit)

}