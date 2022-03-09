package com.example.emb3ddedapp.models

data class User(
    val id:String,
    val login:String,
    val telNumber:String,
    val status:String,
    val tokenMsg:String,
    val profileUrlPhoto:String?,
    val email:String,
    val password:String,
)