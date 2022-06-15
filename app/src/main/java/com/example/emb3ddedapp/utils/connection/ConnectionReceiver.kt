package com.example.emb3ddedapp.utils.connection

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.net.ConnectivityManager
import com.example.emb3ddedapp.utils.IS_CONNECT_INTERNET
import com.example.emb3ddedapp.utils.showToast

class ConnectionReceiver : BroadcastReceiver() {

    override fun onReceive(context: Context?, intent: Intent?) {
        intent?.let {
            if (ConnectivityManager.CONNECTIVITY_ACTION == intent.action){
                 val isConnect = intent.getBooleanExtra(ConnectivityManager.EXTRA_NO_CONNECTIVITY, false)
                IS_CONNECT_INTERNET = !isConnect
                /*context?.let {
                    showToast("Connection: $IS_CONNECT_INTERNET")
                }*/
            }
        }
    }

}