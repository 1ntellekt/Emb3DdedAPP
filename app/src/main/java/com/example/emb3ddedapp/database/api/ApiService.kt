package com.example.emb3ddedapp.database.api

import com.example.emb3ddedapp.models.*
import com.example.emb3ddedapp.utils.getTokenAccess
import com.squareup.okhttp.ResponseBody
import okhttp3.MultipartBody
import retrofit2.Call
import retrofit2.http.*

interface ApiService {

    //users
    @GET("users")
    //@Headers("Accept:application/json", "Content-Type:application/json", "X-Requested-With:XMLHttpRequest","Authorization: Bearer cGx82W6TXYbnlkQGyZxAp3ZBjC7rJwdTUfsXSPiY")
    fun getAllUsers():Call<UsersResponse>

    @GET("users/{id}")
    //@Headers("Accept:application/json", "Content-Type:application/json", "X-Requested-With:XMLHttpRequest")
    fun getUserById(@Path("id") id:Int):Call<UserDefaultResponse>

    @POST("register")
    //@Headers("Accept:application/json", "Content-Type:application/json", "X-Requested-With:XMLHttpRequest")
    fun register(@Body params: User, @Query("password") password: String):Call<UserAuthResponse>

    @POST("login")
    //@Headers("Accept:application/json", "Content-Type:application/json", "X-Requested-With:XMLHttpRequest")
    fun login(@Body params: LoginModel):Call<UserAuthResponse>

    @POST("logout")
    //@Headers("Accept:application/json", "Content-Type:application/json", "X-Requested-With:XMLHttpRequest")
    fun logout():Call<StatusMsgResponse>

    @POST("users/{id}?_method=PUT")
    //@Headers("Accept:application/json", "Content-Type:application/json", "X-Requested-With:XMLHttpRequest")
    fun updateUser(@Path("id") id:Int, @Body params: User?, @Query("old_password") old_password:String?,@Query("password") password:String?):Call<StatusMsgResponse>

    @POST("reset?_method=PUT")
    //@Headers("Accept:application/json", "Content-Type:application/json", "X-Requested-With:XMLHttpRequest")
    fun resetPassword(@Query("uid") uid: String, @Query("reset_password") reset_password:String):Call<StatusMsgResponse>

    //orders
    @GET("orders")
    //@Headers("Accept:application/json", "Content-Type:application/json", "X-Requested-With:XMLHttpRequest","Authorization: Bearer cGx82W6TXYbnlkQGyZxAp3ZBjC7rJwdTUfsXSPiY")
    fun getOrdersByUser(@Query("user_id") user_id:Int):Call<OrdersByUserResponse>

    @GET("orders/{id}")
    //@Headers("Accept:application/json", "Content-Type:application/json", "X-Requested-With:XMLHttpRequest","Authorization: Bearer cGx82W6TXYbnlkQGyZxAp3ZBjC7rJwdTUfsXSPiY")
    fun getOrderById(@Path("id") id:Int):Call<OrderDefaultResponse>

    @POST("orders")
    //@Headers("Accept:application/json", "Content-Type:application/json", "X-Requested-With:XMLHttpRequest","Authorization: Bearer cGx82W6TXYbnlkQGyZxAp3ZBjC7rJwdTUfsXSPiY")
    fun addOrder(@Body params:Order):Call<OrderDefaultResponse>

    @POST("orders/{id}?_method=PUT")
    //@Headers("Accept:application/json", "Content-Type:application/json", "X-Requested-With:XMLHttpRequest","Authorization: Bearer cGx82W6TXYbnlkQGyZxAp3ZBjC7rJwdTUfsXSPiY")
    fun updateOrder(@Path("id") id: Int, @Body params: Order):Call<StatusMsgResponse>

    @POST("orders/{id}?_method=DELETE")
    @Headers("X-Requested-With:XMLHttpRequest")
    fun deleteOrder(@Path("id") id: Int):Call<StatusMsgResponse>

    @GET("filter/orders")
    //@Headers("Accept:application/json", "Content-Type:application/json", "X-Requested-With:XMLHttpRequest","Authorization: Bearer cGx82W6TXYbnlkQGyZxAp3ZBjC7rJwdTUfsXSPiY")
    fun filterOrdersByTitle(@Query("title") title:String):Call<OrdersList>

    @GET("filter/orders")
   // @Headers("Accept:application/json", "Content-Type:application/json", "X-Requested-With:XMLHttpRequest","Authorization: Bearer cGx82W6TXYbnlkQGyZxAp3ZBjC7rJwdTUfsXSPiY")
    fun filterOrdersByAuthorName(@Query("author") title:String):Call<OrdersList>

    @GET("all/orders")
    fun getAllOrders():Call<OrdersByUserResponse>



    //devices
    @POST("devices")
    //@Headers("Accept:application/json", "Content-Type:application/json", "X-Requested-With:XMLHttpRequest")
    fun addDevice(@Body params:Device):Call<DeviceDefaultResponse>

    @POST("deldevices")
    //@Headers("Accept:application/json", "Content-Type:application/json", "X-Requested-With:XMLHttpRequest")
    fun deleteDeviceByToken(@Query("token") token:String):Call<StatusMsgResponse>


    //news
    @GET("news")
    //@Headers("Accept:application/json", "Content-Type:application/json", "X-Requested-With:XMLHttpRequest","Authorization: Bearer cGx82W6TXYbnlkQGyZxAp3ZBjC7rJwdTUfsXSPiY")
    fun getNewsByUser(@Query("user_id") user_id:Int):Call<NewsByUserResponse>

    @GET("news/{id}")
    //@Headers("Accept:application/json", "Content-Type:application/json", "X-Requested-With:XMLHttpRequest","Authorization: Bearer cGx82W6TXYbnlkQGyZxAp3ZBjC7rJwdTUfsXSPiY")
    fun getNewsItemById(@Path("id") id:Int):Call<NewsDefaultResponse>

    @POST("news")
   // @Headers("Accept:application/json", "Content-Type:application/json", "X-Requested-With:XMLHttpRequest","Authorization: Bearer cGx82W6TXYbnlkQGyZxAp3ZBjC7rJwdTUfsXSPiY")
    fun addNewsItem(@Body params:NewsItem):Call<NewsDefaultResponse>

    @POST("news/{id}?_method=PUT")
   // @Headers("Accept:application/json", "Content-Type:application/json", "X-Requested-With:XMLHttpRequest","Authorization: Bearer cGx82W6TXYbnlkQGyZxAp3ZBjC7rJwdTUfsXSPiY")
    fun updateNewsItem(@Path("id") id: Int, @Body params: NewsItem):Call<StatusMsgResponse>

    @POST("news/{id}?_method=DELETE")
    //@Headers("Accept:application/json", "Content-Type:application/json", "X-Requested-With:XMLHttpRequest","Authorization: Bearer cGx82W6TXYbnlkQGyZxAp3ZBjC7rJwdTUfsXSPiY")
    fun deleteNews(@Path("id") id: Int):Call<StatusMsgResponse>

    @GET("filter/news")
   // @Headers("Accept:application/json", "Content-Type:application/json", "X-Requested-With:XMLHttpRequest","Authorization: Bearer cGx82W6TXYbnlkQGyZxAp3ZBjC7rJwdTUfsXSPiY")
    fun filterNewsByStr(@Query("txt") txt:String):Call<NewsList>

    @GET("all/news")
    fun getAllNews():Call<NewsByUserResponse>


    //chats
    @GET("chats")
   // @Headers("Accept:application/json", "Content-Type:application/json", "X-Requested-With:XMLHttpRequest","Authorization: Bearer cGx82W6TXYbnlkQGyZxAp3ZBjC7rJwdTUfsXSPiY")
    fun getChatsByUser(@Query("user_id") user_id:Int):Call<ChatsByUserResponse>

    @GET("chats/{id}")
   // @Headers("Accept:application/json", "Content-Type:application/json", "X-Requested-With:XMLHttpRequest","Authorization: Bearer cGx82W6TXYbnlkQGyZxAp3ZBjC7rJwdTUfsXSPiY")
    fun getChatMessagesById(@Path("id") id:Int):Call<ChatMessagesByChatResponse>

    @POST("chats")
  //  @Headers("Accept:application/json", "Content-Type:application/json", "X-Requested-With:XMLHttpRequest","Authorization: Bearer cGx82W6TXYbnlkQGyZxAp3ZBjC7rJwdTUfsXSPiY")
    fun addChat(@Query("user_id_first") user_id_first:Int, @Query("user_id_second") user_id_second:Int):Call<ChatDefaultResponse>

    @POST("chats/{id}?_method=PUT")
   // @Headers("Accept:application/json", "Content-Type:application/json", "X-Requested-With:XMLHttpRequest","Authorization: Bearer cGx82W6TXYbnlkQGyZxAp3ZBjC7rJwdTUfsXSPiY")
    fun updateChat(@Path("id") id: Int, @Query("download_first") download_first:Int?, @Query("download_second") download_second:Int?):Call<StatusMsgResponse>


    //messages //send
    @POST("messages")
   // @Headers("Accept:application/json", "Content-Type:application/json", "X-Requested-With:XMLHttpRequest","Authorization: Bearer cGx82W6TXYbnlkQGyZxAp3ZBjC7rJwdTUfsXSPiY")
    fun addMessage(@Body params:Message):Call<MessageDefaultResponse>

    //files
    //@Multipart
    //@POST("putfile")
    //fun uploadFile(@Part part:MultipartBody.Part):Call<ResponseBody>
}