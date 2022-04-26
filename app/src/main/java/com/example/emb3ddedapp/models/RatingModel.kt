package com.example.emb3ddedapp.models

data class Rating(
    val id:Int =0,
    val user_id:Int,
    val news_items_id:Int,
    val mark:Double
)

//get Mark by user and news id
data class RatingDefaultResponse(val status:Boolean,val message:String, val you_mark:Rating?)