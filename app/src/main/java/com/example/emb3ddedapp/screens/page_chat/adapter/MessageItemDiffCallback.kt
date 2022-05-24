package com.example.emb3ddedapp.screens.page_chat.adapter

import androidx.recyclerview.widget.DiffUtil
import com.example.emb3ddedapp.models.Message

class MessageItemDiffCallback : DiffUtil.ItemCallback<Message>() {
    override fun areItemsTheSame(oldItem: Message, newItem: Message): Boolean {
        return oldItem.id == newItem.id
    }
    override fun areContentsTheSame(oldItem: Message, newItem: Message): Boolean {
        return oldItem == newItem
    }
}