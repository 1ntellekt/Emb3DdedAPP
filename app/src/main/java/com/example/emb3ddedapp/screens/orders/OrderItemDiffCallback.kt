package com.example.emb3ddedapp.screens.orders

import androidx.recyclerview.widget.DiffUtil
import com.example.emb3ddedapp.models.Order

class OrderItemDiffCallback : DiffUtil.ItemCallback<Order>() {
    override fun areItemsTheSame(oldItem: Order, newItem: Order): Boolean {
        return oldItem.id == newItem.id
    }
    override fun areContentsTheSame(oldItem: Order, newItem: Order): Boolean {
        return oldItem == newItem
    }
}