package com.example.emb3ddedapp.screens.page_news.adapter

import androidx.recyclerview.widget.DiffUtil
import com.example.emb3ddedapp.models.RatingL

class CommentItemDiffCallback : DiffUtil.ItemCallback<RatingL>() {
    override fun areItemsTheSame(oldItem: RatingL, newItem: RatingL): Boolean {
        return oldItem.id == newItem.id
    }
    override fun areContentsTheSame(oldItem: RatingL, newItem: RatingL): Boolean {
        return oldItem == newItem
    }
}