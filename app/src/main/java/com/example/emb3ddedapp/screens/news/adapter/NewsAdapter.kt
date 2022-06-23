package com.example.emb3ddedapp.screens.news.adapter

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.emb3ddedapp.R
import com.example.emb3ddedapp.models.NewsItem
import com.example.emb3ddedapp.screens.listeners.AdapterListeners
import com.example.emb3ddedapp.utils.getDataTimeWithFormat
import com.google.android.material.imageview.ShapeableImageView

class NewsAdapter(
    private val showDelEdit:Boolean,
    private val onItemListener:AdapterListeners.OnItemClick?,
    private val onEditListener:AdapterListeners.OnEditClick?,
    private val onDeleteListener:AdapterListeners.OnDeleteClick?,
):ListAdapter<NewsItem,NewsAdapter.NewsHolder>(NewsItemDiffCallback()) {

    class NewsHolder(itemView: View) :RecyclerView.ViewHolder(itemView){
        val tvShortDescription:TextView = itemView.findViewById(R.id.tvShortDescription)
        val tvTag:TextView = itemView.findViewById(R.id.tvTag)
        val tvTitle:TextView = itemView.findViewById(R.id.tvTitle)
        val tvAuthor:TextView = itemView.findViewById(R.id.tvAuthor)
        val imgNews:ShapeableImageView = itemView.findViewById(R.id.imgNews)
        val btnEdit:ImageButton = itemView.findViewById(R.id.btnEdit)
        val btnDel:ImageButton = itemView.findViewById(R.id.btnDel)
        val ratPanel:LinearLayout = itemView.findViewById(R.id.ratPanel)
        val tvRating:TextView = itemView.findViewById(R.id.tvRating)
        val tvDateTime:TextView = itemView.findViewById(R.id.tvDateTime)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NewsHolder {
        return NewsHolder(LayoutInflater.from(parent.context).inflate(R.layout.new_item_layout,parent,false))
    }

    override fun onBindViewHolder(holder: NewsHolder, position: Int) {
        val newsItem = getItem(position)
        holder.apply {
            tvAuthor.text = newsItem.user!!.login
            tvTag.text = newsItem.tag

            if (newsItem.img_url == null){
                imgNews.setImageResource(R.drawable.news_img)
            }else {
                Glide.with(imgNews.context).load(newsItem.img_url).into(imgNews)
            }

            tvDateTime.text = getDataTimeWithFormat(newsItem.created_at!!)

            tvTitle.text = "${newsItem.title.take(20)}...."
            tvShortDescription.text = "${newsItem.description.take(200)}...."

            if (showDelEdit){
                btnDel.visibility = View.VISIBLE
                btnEdit.visibility = View.VISIBLE
                ratPanel.visibility = View.GONE
            } else {
                ratPanel.visibility = View.VISIBLE
//               tvRating.text = if (newsItem.avgMark == null){
//                    "0.0"
//                }else {
//                    "${newsItem.avgMark}"
//                }
                tvRating.text = "${newsItem.avgMark}"
            }
        }
    }

    override fun onViewAttachedToWindow(holder: NewsHolder) {
        super.onViewAttachedToWindow(holder)
        holder.apply {

            if (onItemListener != null){
                itemView.setOnClickListener {
                    val pos = holder.adapterPosition
                    if (pos != RecyclerView.NO_POSITION){
                        onItemListener.onItemClick(pos)
                    }
                }
            }

            if (onDeleteListener != null){
                btnDel.setOnClickListener {
                    val pos = holder.adapterPosition
                    if (pos != RecyclerView.NO_POSITION){
                        onDeleteListener.onDeleteClick(pos)
                    }
                }
            }

            if (onEditListener != null){
                btnEdit.setOnClickListener {
                    val pos = holder.adapterPosition
                    if (pos != RecyclerView.NO_POSITION){
                        onEditListener.onEditClick(pos)
                    }
                }
            }

        }
    }

    override fun onViewDetachedFromWindow(holder: NewsHolder) {
        super.onViewDetachedFromWindow(holder)
        holder.itemView.setOnClickListener(null)
        holder.btnDel.setOnClickListener(null)
        holder.btnEdit.setOnClickListener(null)
    }

}