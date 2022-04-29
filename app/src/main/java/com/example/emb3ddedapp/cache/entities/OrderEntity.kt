package com.example.emb3ddedapp.cache.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import java.io.Serializable

@Entity(tableName = "Orders")
data class OrderEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    @ColumnInfo(name = "title")
    val title: String,
    @ColumnInfo(name = "description")
    val description: String,
    @ColumnInfo(name = "user_id")
    val user_id: Int,
    @ColumnInfo(name = "img_url")
    val img_url: String? = null,
    @ColumnInfo(name = "status")
    val status: Int = 0,
    @ColumnInfo(name = "created_at")
    val created_at: String? = null
):Serializable
