package com.example.emb3ddedapp.models

import java.io.Serializable

data class User(
    val id:Int,
    val login:String,
    val email:String,
    val uid:String,
    val number:String,
    val status:String,
    val url_profile:String?,
):Serializable

//data class UserList(val users:List<User>)

//all users
data class UsersResponse(val status: Boolean, val message:String,val users: List<User>?)

//get user by id
data class UserDefaultResponse(val status: Boolean, val message:String,val user: User)

// login // register
data class UserAuthResponse(val status: Boolean, val message:String,val user: User,val token:String)

//update user (profile) //logout --StatusMessageResponse
//data class UserResponse(val status: Boolean, val message:String)