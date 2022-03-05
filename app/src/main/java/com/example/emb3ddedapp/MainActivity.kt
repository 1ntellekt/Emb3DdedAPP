package com.example.emb3ddedapp

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.navigation.NavController
import androidx.navigation.Navigation
import com.example.emb3ddedapp.databinding.ActivityMainBinding
import com.example.emb3ddedapp.utils.APP

class MainActivity : AppCompatActivity() {

    private var binding: ActivityMainBinding?=null
    lateinit var mNavController:NavController

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding?.root)
        mNavController = Navigation.findNavController(this, R.id.fragmentNavHost)
        APP = this
    }

    override fun onDestroy() {
        super.onDestroy()
        binding = null
    }

}