package com.example.emb3ddedapp.cache.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "Users")
data class UserEntity(
    @PrimaryKey
    var id:Int=0,
    @ColumnInfo(name = "login")
    var login: String,
    @ColumnInfo(name = "email")
    var email: String,
    @ColumnInfo(name = "uid")
    var uid:String = "",
    @ColumnInfo(name = "number")
    var number:String = "",
    @ColumnInfo(name = "status")
    var status:String ="",
    @ColumnInfo(name = "url_profile")
    var url_profile:String? = null
)
