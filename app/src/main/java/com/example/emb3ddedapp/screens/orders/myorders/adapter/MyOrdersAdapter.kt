package com.example.emb3ddedapp.screens.orders.myorders.adapter

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.emb3ddedapp.R
import com.example.emb3ddedapp.models.Order
import com.example.emb3ddedapp.screens.listeners.AdapterListeners
import com.example.emb3ddedapp.utils.getDataTimeWithFormat
import com.google.android.material.imageview.ShapeableImageView
import java.text.DateFormat
import java.text.SimpleDateFormat
import java.util.*

class MyOrdersAdapter(
    private val onDelListen:AdapterListeners.OnDeleteClick,
    private val onEditListen:AdapterListeners.OnEditClick,
    private val onDoneListen:AdapterListeners.OnDoneClick
    ):RecyclerView.Adapter<MyOrdersAdapter.OrderHolder>() {

    private val ordersList = mutableListOf<Order>()

    @SuppressLint("NotifyDataSetChanged")
    fun setData(list: List<Order>){
        ordersList.clear()
        ordersList.addAll(list)
        notifyDataSetChanged()
    }

    fun deleteItem(position: Int){
        ordersList.removeAt(position)
        notifyItemRemoved(position)
        notifyItemRangeChanged(position,itemCount)
    }

    class OrderHolder(itemView: View) :RecyclerView.ViewHolder(itemView){
        val imgOrder: ShapeableImageView = itemView.findViewById(R.id.imgOrder)
        val imgStatusDone:ImageView = itemView.findViewById(R.id.imgStatusDone)

        val tvShortDescription: TextView = itemView.findViewById(R.id.tvShortDescription)
        val tvAuthor: TextView = itemView.findViewById(R.id.tvAuthor)
        val tvDateTime: TextView = itemView.findViewById(R.id.tvDateTime)
        val tvTitle: TextView = itemView.findViewById(R.id.tvTitle)

        val btnDone:ImageButton = itemView.findViewById(R.id.btnDone)
        val btnEdit:ImageButton = itemView.findViewById(R.id.btnEdit)
        val btnDel:ImageButton = itemView.findViewById(R.id.btnDel)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): OrderHolder {
        return OrderHolder(LayoutInflater.from(parent.context).inflate(R.layout.order_item_layout_2,parent,false))
    }

    override fun onBindViewHolder(holder: OrderHolder, position: Int) {
        val order = ordersList[position]
        holder.apply {
            tvAuthor.text = "Order author: ${ order.user!!.login }"
            tvTitle.text = "${order.title.take(30)}...."
            tvShortDescription.text = "${order.description.take(190)}...."
            order.img_url?.let { url->
                Glide.with(imgOrder.context).load(url).into(imgOrder)
            }
            tvDateTime.text = getDataTimeWithFormat(order.created_at!!)

            if (order.status>0) {
                imgStatusDone.visibility = View.VISIBLE
                btnDone.isEnabled = false
            }
        }
    }

    override fun getItemCount(): Int {
        return ordersList.size
    }

    override fun onViewAttachedToWindow(holder: OrderHolder) {
        super.onViewAttachedToWindow(holder)
        holder.apply {
            btnDel.setOnClickListener {
                val pos = holder.adapterPosition
                if (pos != RecyclerView.NO_POSITION){
                    onDelListen.onDeleteClick(pos)
                }
            }
            btnEdit.setOnClickListener{
                val pos = holder.adapterPosition
                if (pos != RecyclerView.NO_POSITION){
                    onEditListen.onEditClick(pos)
                }
            }
            btnDone.setOnClickListener {
                val pos = holder.adapterPosition
                if (pos != RecyclerView.NO_POSITION){
                    onDoneListen.onDoneClick(pos)
                }
            }
        }
    }

    override fun onViewDetachedFromWindow(holder: OrderHolder) {
        super.onViewDetachedFromWindow(holder)
        holder.apply {
            btnDel.setOnClickListener(null)
            btnDone.setOnClickListener(null)
            btnEdit.setOnClickListener(null)
        }
    }

}