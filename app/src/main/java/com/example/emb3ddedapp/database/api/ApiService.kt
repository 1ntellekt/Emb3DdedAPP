package com.example.emb3ddedapp.database.api

import com.example.emb3ddedapp.models.*
import retrofit2.Call
import retrofit2.http.*

interface ApiService {

    //users
    @GET("users")
    @Headers("Accept:application/json", "Content-Type:application/json", "X-Requested-With:XMLHttpRequest","Authorization: Bearer cGx82W6TXYbnlkQGyZxAp3ZBjC7rJwdTUfsXSPiY")
    fun getAllUsers():Call<UsersResponse>

    @GET("users/{id}")
    @Headers("Accept:application/json", "Content-Type:application/json", "X-Requested-With:XMLHttpRequest","Authorization: Bearer cGx82W6TXYbnlkQGyZxAp3ZBjC7rJwdTUfsXSPiY")
    fun getUserById(@Path("id") id:Int):Call<UserDefaultResponse>

    @POST("register")
    @Headers("Accept:application/json", "Content-Type:application/json", "X-Requested-With:XMLHttpRequest")
    fun register(@Body params: User):Call<UserAuthResponse>

    @POST("login")
    @Headers("Accept:application/json", "Content-Type:application/json", "X-Requested-With:XMLHttpRequest")
    fun login(@Query("uid") uid:String, @Query("password") password:String):Call<UserAuthResponse>

    @POST("logout")
    @Headers("Accept:application/json", "Content-Type:application/json", "X-Requested-With:XMLHttpRequest","Authorization: Bearer cGx82W6TXYbnlkQGyZxAp3ZBjC7rJwdTUfsXSPiY")
    fun logout():Call<StatusMsgResponse>

    @POST("users/{id}?_method=PUT")
    @Headers("Accept:application/json", "Content-Type:application/json", "X-Requested-With:XMLHttpRequest","Authorization: Bearer cGx82W6TXYbnlkQGyZxAp3ZBjC7rJwdTUfsXSPiY")
    fun updateUser(@Path("id") id:Int, @Body params: User):Call<StatusMsgResponse>



    //orders
    @GET("orders")
    @Headers("Accept:application/json", "Content-Type:application/json", "X-Requested-With:XMLHttpRequest","Authorization: Bearer cGx82W6TXYbnlkQGyZxAp3ZBjC7rJwdTUfsXSPiY")
    fun getOrdersByUser(@Query("user_id") user_id:Int):Call<OrdersByUserResponse>

    @GET("orders/{id}")
    @Headers("Accept:application/json", "Content-Type:application/json", "X-Requested-With:XMLHttpRequest","Authorization: Bearer cGx82W6TXYbnlkQGyZxAp3ZBjC7rJwdTUfsXSPiY")
    fun getOrderById(@Path("id") id:Int):Call<OrderDefaultResponse>

    @POST("orders")
    @Headers("Accept:application/json", "Content-Type:application/json", "X-Requested-With:XMLHttpRequest","Authorization: Bearer cGx82W6TXYbnlkQGyZxAp3ZBjC7rJwdTUfsXSPiY")
    fun addOrder(@Body params:Order):Call<OrderDefaultResponse>

    @POST("orders/{id}?_method=PUT")
    @Headers("Accept:application/json", "Content-Type:application/json", "X-Requested-With:XMLHttpRequest","Authorization: Bearer cGx82W6TXYbnlkQGyZxAp3ZBjC7rJwdTUfsXSPiY")
    fun updateOrder(@Path("id") id: Int, @Body params: Order):Call<StatusMsgResponse>

    @DELETE("orders/{id}")
    @Headers("Accept:application/json", "Content-Type:application/json", "X-Requested-With:XMLHttpRequest","Authorization: Bearer cGx82W6TXYbnlkQGyZxAp3ZBjC7rJwdTUfsXSPiY")
    fun deleteOrder(@Path("id") id: Int):Call<StatusMsgResponse>

    @GET("filter/orders")
    @Headers("Accept:application/json", "Content-Type:application/json", "X-Requested-With:XMLHttpRequest","Authorization: Bearer cGx82W6TXYbnlkQGyZxAp3ZBjC7rJwdTUfsXSPiY")
    fun filterOrdersByTitle(@Query("title") title:String):Call<OrdersList>

    @GET("filter/orders")
    @Headers("Accept:application/json", "Content-Type:application/json", "X-Requested-With:XMLHttpRequest","Authorization: Bearer cGx82W6TXYbnlkQGyZxAp3ZBjC7rJwdTUfsXSPiY")
    fun filterOrdersByAuthorName(@Query("author") title:String):Call<OrdersList>



    //devices
    @POST("devices")
    @Headers("Accept:application/json", "Content-Type:application/json", "X-Requested-With:XMLHttpRequest","Authorization: Bearer cGx82W6TXYbnlkQGyZxAp3ZBjC7rJwdTUfsXSPiY")
    fun addDevice(@Body params:Device):Call<DeviceDefaultResponse>

    @POST("deldevices")
    @Headers("Accept:application/json", "Content-Type:application/json", "X-Requested-With:XMLHttpRequest","Authorization: Bearer cGx82W6TXYbnlkQGyZxAp3ZBjC7rJwdTUfsXSPiY")
    fun deleteDeviceByToken(@Query("token") token:String):Call<StatusMsgResponse>


    //news
    @GET("news")
    @Headers("Accept:application/json", "Content-Type:application/json", "X-Requested-With:XMLHttpRequest","Authorization: Bearer cGx82W6TXYbnlkQGyZxAp3ZBjC7rJwdTUfsXSPiY")
    fun getNewsByUser(@Query("user_id") user_id:Int):Call<NewsByUserResponse>

    @GET("news/{id}")
    @Headers("Accept:application/json", "Content-Type:application/json", "X-Requested-With:XMLHttpRequest","Authorization: Bearer cGx82W6TXYbnlkQGyZxAp3ZBjC7rJwdTUfsXSPiY")
    fun getNewsItemById(@Path("id") id:Int):Call<NewsDefaultResponse>

    @POST("news")
    @Headers("Accept:application/json", "Content-Type:application/json", "X-Requested-With:XMLHttpRequest","Authorization: Bearer cGx82W6TXYbnlkQGyZxAp3ZBjC7rJwdTUfsXSPiY")
    fun addNewsItem(@Body params:NewsItem):Call<NewsDefaultResponse>

    @POST("news/{id}?_method=PUT")
    @Headers("Accept:application/json", "Content-Type:application/json", "X-Requested-With:XMLHttpRequest","Authorization: Bearer cGx82W6TXYbnlkQGyZxAp3ZBjC7rJwdTUfsXSPiY")
    fun updateNewsItem(@Path("id") id: Int, @Body params: NewsItem):Call<StatusMsgResponse>

    @DELETE("news/{id}")
    @Headers("Accept:application/json", "Content-Type:application/json", "X-Requested-With:XMLHttpRequest","Authorization: Bearer cGx82W6TXYbnlkQGyZxAp3ZBjC7rJwdTUfsXSPiY")
    fun deleteNews(@Path("id") id: Int):Call<StatusMsgResponse>

    @GET("filter/news")
    @Headers("Accept:application/json", "Content-Type:application/json", "X-Requested-With:XMLHttpRequest","Authorization: Bearer cGx82W6TXYbnlkQGyZxAp3ZBjC7rJwdTUfsXSPiY")
    fun filterNewsByStr(@Query("txt") txt:String):Call<NewsList>


}