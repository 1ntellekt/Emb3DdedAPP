package com.example.emb3ddedapp


import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.net.ConnectivityManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.navigation.NavController
import androidx.navigation.Navigation
import com.example.emb3ddedapp.database.repository.Repository
import com.example.emb3ddedapp.databinding.ActivityMainBinding
import com.example.emb3ddedapp.notification.FireServices
import com.example.emb3ddedapp.utils.APP
import com.example.emb3ddedapp.utils.REPOSITORY
import com.example.emb3ddedapp.utils.connection.ConnectionReceiver

class MainActivity : AppCompatActivity() {

    private var binding: ActivityMainBinding? = null
    lateinit var mNavController: NavController

    private lateinit var broadcastReceiver:ConnectionReceiver

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding?.root)
        mNavController = Navigation.findNavController(this, R.id.fragmentNavHost)
        APP = this
        REPOSITORY = Repository()

        broadcastReceiver = ConnectionReceiver()
        val intentFilter = IntentFilter()
         intentFilter.addAction(ConnectivityManager.CONNECTIVITY_ACTION)
         registerReceiver(broadcastReceiver,intentFilter)
    }

    override fun onDestroy() {
        super.onDestroy()
        binding = null
    }

    override fun onStop() {
        super.onStop()
        unregisterReceiver(broadcastReceiver)
    }

/*    private val broadcastReceiver = object : BroadcastReceiver(){
        override fun onReceive(context: Context?, intent: Intent?) {
            if (intent?.extras!=null){
                val extras = intent.extras
                Log.d("msg","Message received!")
//                extras?.keySet()?.firstOrNull{it == FireServices.KEY_ACTION}?.let { key->
//                    Log.d("msg","key: $key")
//                    when(extras.getString(key)){
//                        PushService.ACTION_SHOW_MESSAGE ->{
//                            val userSender = User(id = extras.getString(PushService.KEY_ID)!!,
//                                email = extras.getString(PushService.KEY_EMAIL)!!,
//                                nick = extras.getString(PushService.KEY_NICK)!!,
//                                status = extras.getString(PushService.KEY_STATUS)!!,
//                                token = extras.getString(PushService.KEY_TOKEN)!!)
//                            createNotification(userSender,extras.getString(PushService.KEY_MESSAGE)!!)
//                        }
//                        else ->{
//                            Log.d("msg","key error not found")}
//                    }
//                }
            }
        }
    }*/

}