package com.example.emb3ddedapp.screens.chats.adapter

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.emb3ddedapp.R
import com.example.emb3ddedapp.models.Chat
import com.example.emb3ddedapp.screens.listeners.AdapterListeners
import com.example.emb3ddedapp.utils.APP
import com.example.emb3ddedapp.utils.getDataTimeWithFormat
import de.hdodenhof.circleimageview.CircleImageView

class ChatAdapter(
    private val user_id:Int,
    private val onItemListener: AdapterListeners.OnItemClick
):ListAdapter<Chat,ChatAdapter.ChatHolder>(ChatDiffCallback()) {

    class ChatHolder(itemView: View):RecyclerView.ViewHolder(itemView){
        val imgChatPerson:CircleImageView = itemView.findViewById(R.id.imgChatPerson)
        val tvLogin:TextView = itemView.findViewById(R.id.tvLogin)
        val tvLastMessage:TextView = itemView.findViewById(R.id.tvLastMessage)
        val tvTime:TextView = itemView.findViewById(R.id.tvTime)
        val tvDate:TextView = itemView.findViewById(R.id.tvDate)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ChatHolder {
        return ChatHolder(LayoutInflater.from(parent.context).inflate(R.layout.chat_item_layout,parent,false))
    }

    override fun onBindViewHolder(holder: ChatHolder, position: Int) {
        val chat = getItem(position)
        holder.apply {

            if (chat.user_first!!.id == user_id){
                //user second
                tvLogin.text = chat.user_second!!.login
                if (chat.user_second.url_profile == null){
                    imgChatPerson.setImageResource(R.drawable.ic_person)
                } else {
                    Glide.with(imgChatPerson.context).load(chat.user_second.url_profile).into(imgChatPerson)
                }
            } else {
                //user first
                tvLogin.text = chat.user_first.login
                if (chat.user_first.url_profile == null){
                    imgChatPerson.setImageResource(R.drawable.ic_person)
                } else {
                    Glide.with(imgChatPerson.context).load(chat.user_first.url_profile).into(imgChatPerson)
                }
            }

            if (chat.last_message == null){
                tvLastMessage.setTextColor(Color.BLACK)
                tvLastMessage.text = tvLastMessage.context.resources.getString(R.string.no_msg_txt)
            } else {
                chat.last_message.apply {
                    if (text_msg != null){
                        tvLastMessage.text = text_msg
                        tvLastMessage.setTextColor(ContextCompat.getColor(holder.itemView.context, R.color.color_grey))
                    } else if (img_msg != null) {
                        tvLastMessage.setTextColor(Color.MAGENTA)
                        tvLastMessage.text = tvLastMessage.context.resources.getString(R.string.picture_txt)
                    } else if (file_msg != null){
                        tvLastMessage.setTextColor(Color.MAGENTA)
                        tvLastMessage.text = tvLastMessage.context.resources.getString(R.string.file_txt)
                    } else if (file_3d_msg != null){
                        tvLastMessage.setTextColor(Color.MAGENTA)
                        tvLastMessage.text = tvLastMessage.context.resources.getString(R.string.d_file_txt)
                    }
                    val dateTimeStr = getDataTimeWithFormat(created_at!!)
                    val dateStr = dateTimeStr.substringBefore(" ")
                    val timeStr = dateTimeStr.substringAfter(" ")
                    tvDate.text = dateStr
                    tvTime.text = timeStr
                }
            }

        }
    }

    override fun onViewAttachedToWindow(holder: ChatHolder) {
        super.onViewAttachedToWindow(holder)
        holder.itemView.setOnClickListener {
            val pos = holder.adapterPosition
            if (pos != RecyclerView.NO_POSITION){
                onItemListener.onItemClick(pos)
            }
        }
    }

    override fun onViewDetachedFromWindow(holder: ChatHolder) {
        super.onViewDetachedFromWindow(holder)
        holder.itemView.setOnClickListener(null)
    }

}