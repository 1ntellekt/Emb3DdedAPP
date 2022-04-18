package com.example.emb3ddedapp.screens.page_chat.adapter

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.emb3ddedapp.R
import com.example.emb3ddedapp.models.Message
import com.example.emb3ddedapp.screens.listeners.AdapterListeners

class MessageAdapter(
    private val user_id:Int,
    private val onItem3dFile: AdapterListeners.OnItemFile,
    private val onItemFile: AdapterListeners.OnItemFile,
    private val onImage:AdapterListeners.OnImage,
    private val onFile:AdapterListeners.OnFile,
    private val on3dFile: AdapterListeners.On3dFile,
    private val onTextMsg: AdapterListeners.OnItemTextMsg
):RecyclerView.Adapter<MessageAdapter.MessageHolder>() {

    private val messageList = mutableListOf<Message>()

    @SuppressLint("NotifyDataSetChanged")
    fun setData(list: List<Message>){
        messageList.clear()
        //val list1 = list.sortedBy { getDataTimeWithFormat(it.created_at!!) }
        messageList.addAll(list)
        //Log.i("messAdapter", "setData(): ${list.map { it.id }}" )
        notifyDataSetChanged()
        //notifyItemRangeChanged(0, itemCount)
    }

//    @SuppressLint("NotifyDataSetChanged")
//    fun addItem(msg:Message){
//        messageList.add(msg)
//        notifyDataSetChanged()
//    }

     class MessageHolder(itemView: View) :RecyclerView.ViewHolder(itemView){

        val layMsgMy:ConstraintLayout = itemView.findViewById(R.id.layMsgMy)

        val tvMsgMy:TextView = itemView.findViewById(R.id.tvMsgMy)
        val imgMsgMy:ImageView = itemView.findViewById(R.id.imgMsgMy)

        val panel3dFileMy:LinearLayout = itemView.findViewById(R.id.panel3dFileMy)
        val tvFilename3dMy:TextView = itemView.findViewById(R.id.tvFilename3dMy)
        val btn3dDownloadMy:ImageButton = itemView.findViewById(R.id.btn3dDownloadMy)

        val panelFileMy:LinearLayout = itemView.findViewById(R.id.panelFileMy)
        val tvFilenameMy:TextView = itemView.findViewById(R.id.tvFilenameMy)
        val btnDownloadFileMy:ImageButton = itemView.findViewById(R.id.btnDownloadFileMy)


        val layMsgPartner:ConstraintLayout = itemView.findViewById(R.id.layMsgPartner)

        val tvMsgPartner:TextView = itemView.findViewById(R.id.tvMsgPartner)
        val imgMsgPartner:ImageView = itemView.findViewById(R.id.imgMsgPartner)

        val panel3dFilePartner:LinearLayout = itemView.findViewById(R.id.panel3dFilePartner)
        val tvFilename3dPartner:TextView = itemView.findViewById(R.id.tvFilename3dPartner)
        val btn3dDownloadPartner:ImageButton = itemView.findViewById(R.id.btn3dDownloadPartner)

        val panelFilePartner:LinearLayout = itemView.findViewById(R.id.panelFilePartner)
        val tvFilenamePartner:TextView = itemView.findViewById(R.id.tvFilenamePartner)
        val btnDownloadFilePartner:ImageButton = itemView.findViewById(R.id.btnDownloadFilePartner)

        /*fun bind(message: Message){
            if (message.user_id_sender == user_id){
                // i'm sender
                layMsgPartner.visibility = View.INVISIBLE
                layMsgMy.visibility = View.VISIBLE
                bindMsg(true, message)
            } else if (message.user_id_recepient == user_id){
                //message sends to me
                layMsgMy.visibility = View.INVISIBLE
                layMsgPartner.visibility = View.VISIBLE
                bindMsg(false, message)
            }
        }

        private fun bindMsg(isMy:Boolean, message: Message){
            Log.i("messAdapter", "id: ${message.id} pos:$adapterPosition" )
            if (message.text_msg!=null){

               if (isMy){
                   tvMsgMy.visibility = View.VISIBLE
                   tvMsgMy.text = message.text_msg
               }
               else{
                   tvMsgPartner.visibility = View.VISIBLE
                   tvMsgPartner.text = message.text_msg
               }

            }
            else if (message.img_msg != null) {

                if (isMy){
                    imgMsgMy.visibility = View.VISIBLE
                    Glide.with(imgMsgMy.context).load(message.img_msg).into(imgMsgMy)
                } else {
                    imgMsgPartner.visibility = View.VISIBLE
                    Glide.with(imgMsgPartner.context).load(message.img_msg).into(imgMsgPartner)
                }

            }
            else if (message.file_msg != null){

               if (isMy){
                  panelFileMy.visibility = View.VISIBLE
                  tvFilenameMy.text = message.file_msg
               } else {
                   panelFilePartner.visibility = View.VISIBLE
                   tvFilenamePartner.text = message.file_msg
               }

            }
            else if (message.file_3d_msg != null){

                if (isMy){
                    panel3dFileMy.visibility = View.VISIBLE
                    tvFilename3dMy.text = message.file_3d_msg
                } else {
                    panel3dFilePartner.visibility = View.VISIBLE
                    tvFilename3dPartner.text = message.file_3d_msg
                }

            }
        }
       */

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MessageHolder {
        return MessageHolder(LayoutInflater.from(parent.context).inflate(R.layout.message_item,parent,false))
    }

    override fun onBindViewHolder(holder: MessageHolder, position: Int) {
        val message = messageList[position]
        //holder.bind(message = message)
        holder.apply {
            message.apply {
                if (message.user_id_sender == user_id){
                    // i'm sender
                    layMsgPartner.visibility = View.GONE
                    layMsgMy.visibility = View.VISIBLE

                    when {
                        text_msg != null -> {
                            tvMsgMy.visibility = View.VISIBLE
                            imgMsgMy.visibility = View.GONE
                            panelFileMy.visibility = View.GONE
                            panel3dFileMy.visibility = View.GONE
                            layMsgMy.setOnLongClickListener {
                                val pos = holder.adapterPosition
                                if (pos != RecyclerView.NO_POSITION){
                                    onTextMsg.onItemTextMsgClick(pos, layMsgMy)
                                }
                                true
                            }
                            tvMsgMy.text = text_msg
                        }
                        img_msg != null -> {
                            imgMsgMy.visibility = View.VISIBLE
                            tvMsgMy.visibility = View.GONE
                            panelFileMy.visibility = View.GONE
                            panel3dFileMy.visibility = View.GONE
                            Glide.with(imgMsgMy.context).load(img_msg).into(imgMsgMy)
                        }
                        file_msg != null -> {
                            panelFileMy.visibility = View.VISIBLE
                            imgMsgMy.visibility = View.GONE
                            tvMsgMy.visibility = View.GONE
                            panel3dFileMy.visibility = View.GONE
                            tvFilenameMy.text = file_msg
                        }
                        else -> {
                            panel3dFileMy.visibility = View.VISIBLE
                            panelFileMy.visibility = View.GONE
                            imgMsgMy.visibility = View.GONE
                            tvMsgMy.visibility = View.GONE
                            tvFilename3dMy.text = file_3d_msg
                        }
                    }

                } else if (message.user_id_recepient == user_id){
                    //message sends to me
                    layMsgMy.visibility = View.GONE
                    layMsgPartner.visibility = View.VISIBLE

                    when {
                        text_msg != null -> {
                            tvMsgPartner.visibility = View.VISIBLE
                            imgMsgPartner.visibility = View.GONE
                            panelFilePartner.visibility = View.GONE
                            panel3dFilePartner.visibility = View.GONE
                            layMsgPartner.setOnLongClickListener {
                                val pos = holder.adapterPosition
                                if (pos != RecyclerView.NO_POSITION){
                                    onTextMsg.onItemTextMsgClick(pos, layMsgPartner)
                                }
                                true
                            }
                            tvMsgPartner.text = text_msg
                        }
                        img_msg != null -> {
                            imgMsgPartner.visibility = View.VISIBLE
                            tvMsgPartner.visibility = View.GONE
                            panelFilePartner.visibility = View.GONE
                            panel3dFilePartner.visibility = View.GONE
                            Glide.with(imgMsgPartner.context).load(img_msg).into(imgMsgPartner)
                        }
                        file_msg != null -> {
                            panelFilePartner.visibility = View.VISIBLE
                            imgMsgPartner.visibility = View.GONE
                            tvMsgPartner.visibility = View.GONE
                            panel3dFilePartner.visibility = View.GONE
                            tvFilenamePartner.text = file_msg
                        }
                        else -> {
                            panel3dFilePartner.visibility = View.VISIBLE
                            panelFilePartner.visibility = View.GONE
                            imgMsgPartner.visibility = View.GONE
                            tvMsgPartner.visibility = View.GONE
                            tvFilename3dPartner.text = file_3d_msg
                        }
                    }

                }
            }
        }
    }

    override fun onViewAttachedToWindow(holder: MessageHolder) {
        super.onViewAttachedToWindow(holder)
        holder.apply {
            btnDownloadFilePartner.setOnClickListener {
                val pos = holder.adapterPosition
                if (pos != RecyclerView.NO_POSITION){
                    onFile.onFileClick(pos)
                }
            }
            btnDownloadFileMy.setOnClickListener {
                val pos = holder.adapterPosition
                if (pos != RecyclerView.NO_POSITION){
                    onFile.onFileClick(pos)
                }
            }
            btn3dDownloadPartner.setOnClickListener {
                val pos = holder.adapterPosition
                if (pos != RecyclerView.NO_POSITION){
                    on3dFile.on3dFileClick(pos)
                }
            }
            btn3dDownloadMy.setOnClickListener {
                val pos = holder.adapterPosition
                if (pos != RecyclerView.NO_POSITION){
                    on3dFile.on3dFileClick(pos)
                }
            }
            panel3dFileMy.setOnLongClickListener {
                val pos = holder.adapterPosition
                if (pos != RecyclerView.NO_POSITION){
                    onItem3dFile.onItemFileClick(pos, holder.panel3dFileMy)
                }
                true
            }
            panel3dFilePartner.setOnLongClickListener {
                val pos = holder.adapterPosition
                if (pos != RecyclerView.NO_POSITION){
                    onItem3dFile.onItemFileClick(pos,holder.panel3dFilePartner)
                }
                true
            }
            panelFileMy.setOnLongClickListener {
                val pos = holder.adapterPosition
                if (pos != RecyclerView.NO_POSITION){
                    onItemFile.onItemFileClick(pos, holder.panelFileMy)
                }
                true
            }
            panelFilePartner.setOnLongClickListener {
                val pos = holder.adapterPosition
                if (pos != RecyclerView.NO_POSITION){
                    onItemFile.onItemFileClick(pos, holder.panelFilePartner)
                }
                true
            }
            imgMsgMy.setOnClickListener {
                val pos = holder.adapterPosition
                if (pos != RecyclerView.NO_POSITION){
                    onImage.onImgClick(pos)
                }
            }
            imgMsgPartner.setOnClickListener {
                val pos = holder.adapterPosition
                if (pos != RecyclerView.NO_POSITION){
                    onImage.onImgClick(pos)
                }
            }
        }
//        holder.apply {
//            panel3dFilePartner.setOnClickListener { showToast("it's panel 3d partner") }
//            panel3dFileMy.setOnClickListener { showToast("it's panel 3d my") }
//            panelFilePartner.setOnClickListener { showToast("it's panel file partner") }
//            panelFileMy.setOnClickListener { showToast("it's panel file my") }
//            tvMsgMy.setOnClickListener { showToast("it's msg my") }
//            tvMsgPartner.setOnClickListener { showToast("it's msg partner") }
//            imgMsgPartner.setOnClickListener { showToast("it's img partner") }
//            imgMsgMy.setOnClickListener { showToast("it's img my") }
//        }
    }

    override fun onViewDetachedFromWindow(holder: MessageHolder) {
        super.onViewDetachedFromWindow(holder)
        holder.apply {
            btn3dDownloadMy.setOnClickListener(null)
            btnDownloadFileMy.setOnClickListener(null)
            btn3dDownloadPartner.setOnClickListener(null)
            btnDownloadFilePartner.setOnClickListener(null)
            panel3dFileMy.setOnLongClickListener(null)
            panel3dFilePartner.setOnLongClickListener(null)
            panelFileMy.setOnLongClickListener(null)
            panelFilePartner.setOnLongClickListener(null)
            imgMsgPartner.setOnClickListener(null)
            imgMsgMy.setOnClickListener(null)
            layMsgMy.setOnLongClickListener(null)
            layMsgPartner.setOnLongClickListener(null)
        }

    }

    override fun getItemCount(): Int {
        return messageList.size
    }

}