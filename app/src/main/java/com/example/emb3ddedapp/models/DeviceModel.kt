package com.example.emb3ddedapp.models

data class Device(
    val id: Int = 0,
    val name_device: String,
    val token: String,
    val user_id: Int,
    //val user:User?=null
)

  data class DeviceDefaultResponse(val status:Boolean, val message:String, val device: Device)