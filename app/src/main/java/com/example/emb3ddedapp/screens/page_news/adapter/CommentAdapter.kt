package com.example.emb3ddedapp.screens.page_news.adapter


import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.RatingBar
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.emb3ddedapp.R
import com.example.emb3ddedapp.models.RatingL
import de.hdodenhof.circleimageview.CircleImageView

class CommentAdapter:RecyclerView.Adapter<CommentAdapter.CommentHolder>() {

    private val commentList = mutableListOf<RatingL>()

    @SuppressLint("NotifyDataSetChanged")
    fun setData(list: List<RatingL>){
        commentList.clear()
        commentList.addAll(list)
        notifyDataSetChanged()
    }

    class CommentHolder(itemView: View) :RecyclerView.ViewHolder(itemView){
        val tvComment:TextView = itemView.findViewById(R.id.tvComment)
        val tvRating:TextView = itemView.findViewById(R.id.tvRating)
        val tvLogin:TextView = itemView.findViewById(R.id.tvLogin)
        val ratingBar:RatingBar = itemView.findViewById(R.id.ratingBar)
        val imgPerson:CircleImageView = itemView.findViewById(R.id.imgPerson)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CommentHolder {
        return CommentHolder(LayoutInflater.from(parent.context).inflate(R.layout.comment_item_layout,parent,false))
    }

    override fun onBindViewHolder(holder: CommentHolder, position: Int) {
        val comment = commentList[position]
        holder.apply {
            if (comment.comment == null){
                tvComment.visibility = View.GONE
            } else {
                tvComment.visibility = View.VISIBLE
                tvComment.text = comment.comment
            }
            ratingBar.rating = comment.mark.toFloat()
            if (comment.user.url_profile == null){
                imgPerson.setImageResource(R.drawable.ic_person)
            } else {
                Glide.with(imgPerson.context).load(comment.user.url_profile).into(imgPerson)
            }
            tvLogin.text = comment.user.login
            tvRating.text = comment.mark.toString()
        }
    }

    override fun getItemCount(): Int {
        return commentList.size
    }

}