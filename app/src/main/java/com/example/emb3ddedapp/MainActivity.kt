package com.example.emb3ddedapp


import android.content.IntentFilter
import android.net.ConnectivityManager
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.NavController
import androidx.navigation.Navigation
import com.example.emb3ddedapp.database.repository.Repository
import com.example.emb3ddedapp.databinding.ActivityMainBinding
import com.example.emb3ddedapp.utils.APP
import com.example.emb3ddedapp.utils.REPOSITORY
import com.example.emb3ddedapp.utils.connection.ConnectionReceiver

class MainActivity : AppCompatActivity() {

    private var binding: ActivityMainBinding? = null
    lateinit var mNavController: NavController

    private lateinit var broadcastReceiver:ConnectionReceiver
    private lateinit var intentFilter:IntentFilter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding?.root)
        mNavController = Navigation.findNavController(this, R.id.fragmentNavHost)
        APP = this
        REPOSITORY = Repository()

        broadcastReceiver = ConnectionReceiver()
         intentFilter = IntentFilter()
         intentFilter.addAction(ConnectivityManager.CONNECTIVITY_ACTION)
         //registerReceiver(broadcastReceiver,intentFilter)
    }

    override fun onResume() {
        super.onResume()
        registerReceiver(broadcastReceiver,intentFilter)
    }

    override fun onPause() {
        super.onPause()
        unregisterReceiver(broadcastReceiver)
    }

    override fun onDestroy() {
        super.onDestroy()
        //unregisterReceiver(broadcastReceiver)
        binding = null
    }

}