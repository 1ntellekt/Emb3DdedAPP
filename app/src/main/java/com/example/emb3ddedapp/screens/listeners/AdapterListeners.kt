package com.example.emb3ddedapp.screens.listeners

interface AdapterListeners {

    fun interface OnItemClick {
       fun onItemClick(position:Int)
    }

    fun interface OnDeleteClick {
        fun onDeleteClick(position: Int)
    }

    fun interface OnEditClick {
        fun onEditClick(position: Int)
    }

    fun interface OnDoneClick {
        fun onDoneClick(position: Int)
    }

    fun interface On3dFile{
        fun on3dFileClick(position: Int)
    }

    fun interface OnFile{
        fun onFileClick(position: Int)
    }

}