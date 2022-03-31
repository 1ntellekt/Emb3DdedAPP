package com.example.emb3ddedapp.models

data class NewsItem(
    val id: Int = 0,
    val title: String,
    val description: String,
    val user_id: Int,
    val img_url: String? = null,
    val tag: String,
    val created_at: String,
    val user: User? = null
)

data class NewsList(val news:List<NewsItem>?)

data class NewsByUserResponse(val status:Boolean, val message:String, val news:List<NewsItem>?)

data class NewsDefaultResponse(val status:Boolean, val message:String, val news_item:NewsItem)
