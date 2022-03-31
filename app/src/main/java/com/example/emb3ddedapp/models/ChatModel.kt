package com.example.emb3ddedapp.models

data class Chat(
    val download_first: Int = 0,
    val download_second: Int = 0,
    val id: Int = 0,
    val user_id_first: Int,
    val user_id_second: Int,
    val user_first:User? = null,
    val user_second:User? = null,
    val last_message:Message? = null
)

data class ChatDefault(
    val download_first: Int,
    val download_second: Int,
    val id: Int = 0,
    val user_id_first: Int,
    val user_id_second: Int
)

data class ChatsByUserResponse(val status:Boolean, val message:String,val chats:List<Chat>?)

data class ChatDefaultResponse(val status:Boolean, val message:String, val chat: Chat)

data class ChatMessagesByChatResponse(val status:Boolean, val message:String,val chat:ChatDefault, val messages:List<Message>?)

