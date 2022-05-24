package com.example.emb3ddedapp.screens.news.adapter

import androidx.recyclerview.widget.DiffUtil
import com.example.emb3ddedapp.models.NewsItem

class NewsItemDiffCallback : DiffUtil.ItemCallback<NewsItem>() {
    override fun areItemsTheSame(oldItem: NewsItem, newItem: NewsItem): Boolean {
        return oldItem.id == newItem.id
    }
    override fun areContentsTheSame(oldItem: NewsItem, newItem: NewsItem): Boolean {
        return oldItem == newItem
    }
}