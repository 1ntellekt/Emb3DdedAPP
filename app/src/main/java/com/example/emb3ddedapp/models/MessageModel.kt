package com.example.emb3ddedapp.models

data class Message(
    val chat_id: Int,
    val created_at: String,
    val file_3d_msg: String? = null,
    val file_msg: String? = null,
    val id: Int = 0,
    val img_msg: String? = null,
    val text_msg: String? = null,
    val user_id_recepient: Int,
    val user_id_sender: Int
)

data class MessageDefaultResponse(val status:Boolean, val message:String, val message_created: Message)