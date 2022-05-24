package com.example.emb3ddedapp.screens.chats.adapter

import androidx.recyclerview.widget.DiffUtil
import com.example.emb3ddedapp.models.Chat

class ChatDiffCallback : DiffUtil.ItemCallback<Chat>() {
    override fun areItemsTheSame(oldItem: Chat, newItem: Chat): Boolean {
        return oldItem.id == newItem.id
    }
    override fun areContentsTheSame(oldItem: Chat, newItem: Chat): Boolean {
        return oldItem == newItem
    }
}