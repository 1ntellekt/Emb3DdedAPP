package com.example.emb3ddedapp.screens.orders.allorders.adapter

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.emb3ddedapp.R
import com.example.emb3ddedapp.models.Order
import com.example.emb3ddedapp.screens.listeners.AdapterListeners
import com.google.android.material.imageview.ShapeableImageView
import java.text.DateFormat
import java.text.SimpleDateFormat
import java.util.*

class AllOrdersAdapter(private val onItemListener:AdapterListeners.OnItemClick):RecyclerView.Adapter<AllOrdersAdapter.OrderHolder>() {

    private val ordersList = mutableListOf<Order>()

    @SuppressLint("NotifyDataSetChanged")
    fun setData(list: List<Order>){
        ordersList.clear()
        ordersList.addAll(list)
        notifyDataSetChanged()
    }

    class OrderHolder(itemView: View) :RecyclerView.ViewHolder(itemView){
        val imgOrder:ShapeableImageView = itemView.findViewById(R.id.imgOrder)
        val tvShortDescription:TextView = itemView.findViewById(R.id.tvShortDescription)
        val tvAuthor:TextView = itemView.findViewById(R.id.tvAuthor)
        val tvDateTime:TextView = itemView.findViewById(R.id.tvDateTime)


            fun getDataTimeWithFormat(dateTime:String):String{
                val dateFormat: DateFormat = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'",Locale.getDefault())
                val date: Date = dateFormat.parse(dateTime)!!
                val formatter: DateFormat = SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault())
                return formatter.format(date)
            }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): OrderHolder {
        return OrderHolder(LayoutInflater.from(parent.context).inflate(R.layout.order_item_layout_1,parent,false))
    }

    @SuppressLint("SimpleDateFormat")
    override fun onBindViewHolder(holder: OrderHolder, position: Int) {
        val order = ordersList[position]
        holder.apply {
            tvAuthor.text = "Order author: ${ order.user!!.login }"
            tvShortDescription.text = "${order.description.take(120)}...."
            order.img_url?.let { url->
                Glide.with(imgOrder.context).load(url).into(imgOrder)
            }
            tvDateTime.text = holder.getDataTimeWithFormat(order.created_at!!)
        }
    }

    override fun getItemCount(): Int {
        return ordersList.size
    }

    override fun onViewAttachedToWindow(holder: OrderHolder) {
        super.onViewAttachedToWindow(holder)
        holder.itemView.setOnClickListener {
            val pos = holder.adapterPosition
            if (pos != RecyclerView.NO_POSITION){
                onItemListener.onItemClick(pos)
            }
        }
    }

    override fun onViewDetachedFromWindow(holder: OrderHolder) {
        super.onViewDetachedFromWindow(holder)
        holder.itemView.setOnClickListener(null)
    }

}