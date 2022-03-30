package com.example.emb3ddedapp.screens.mainfrag

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import android.util.Log
import android.view.*
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.NavUtils
import androidx.fragment.app.Fragment
import androidx.navigation.NavController
import androidx.navigation.Navigation
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.NavigationUI
import com.example.emb3ddedapp.R
import com.example.emb3ddedapp.database.api.RetrofitInstance
import com.example.emb3ddedapp.databinding.MainFragmentBinding
import com.example.emb3ddedapp.utils.APP
import com.google.firebase.messaging.FirebaseMessaging
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MainFragment : Fragment() {

    private var _binding:MainFragmentBinding? = null
    private val binding:MainFragmentBinding
    get() = _binding!!

    private lateinit var navController:NavController

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
    }

    private lateinit var viewModel: MainViewModel

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        _binding = MainFragmentBinding.inflate(inflater, container,false)
         (requireActivity() as AppCompatActivity).setSupportActionBar(binding.toolbar.also { it.title="" })
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        navController = (childFragmentManager.findFragmentById(R.id.fragMenuNavHost) as NavHostFragment).navController
        NavigationUI.setupWithNavController(binding.bottomNavMenu, navController)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProvider(this).get(MainViewModel::class.java)
        // TODO: Use the ViewModel
    }

    override fun onStart() {
        super.onStart()
/*        binding.tvTest.text = "${CurrUser.id} ${CurrUser.email} ${CurrUser.login} ${CurrUser.password} " +
                "${CurrUser.profileUrlPhoto} ${CurrUser.status} ${CurrUser.telNumber} ${CurrUser.tokenMsg}"*/
        FirebaseMessaging.getInstance().token
            .addOnSuccessListener { Log.i("tagDevice","generated token: $it") }
            .addOnFailureListener {  Log.e("tagDevice","Error generated token: ${it.message.toString()}") }
        Log.i("tagDevice", android.os.Build.MODEL)
        //FirebaseMessaging.getInstance().deleteToken()

//        RetrofitInstance.api.getOrdersByUser(2).enqueue(object : Callback<OrdersByUserResponse>{
//            override fun onResponse(call: Call<OrdersByUserResponse>, response: Response<OrdersByUserResponse>) {
//                if (response.isSuccessful){
//                    Log.i("tagAPI", "${response.body()}")
//                } else {
//                    Log.i("tagAPI", "${response.code()} | ${response.message()}")
//                }
//            }
//
//            override fun onFailure(call: Call<OrdersByUserResponse>, t: Throwable) {
//                Log.i("tagAPI", "message error: ${t.message}")
//            }
//
//        })

//        RetrofitInstance.api.login("sdhjvgsdfjvhgsdjfvhvbjk","12345678").enqueue(object : Callback<UserAuthResponse>{
//            override fun onResponse(call: Call<UserAuthResponse>, response: Response<UserAuthResponse>) {
//                if (response.isSuccessful){
//                   Log.i("tagAPI", "${response.body()}")
//                } else {
//                  Log.i("tagAPI", "${response.code()} | ${response.message()}")
//                }
//            }
//            override fun onFailure(call: Call<UserAuthResponse>, t: Throwable) {
//                Log.i("tagAPI", "message error: ${t.message}")
//            }
//        })

    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.app_main_menu_tool,menu)
        super.onCreateOptionsMenu(menu, inflater)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {

        when(item.itemId){
            R.id.idExit -> {
                //viewModel.signOutAccount()
                APP.mNavController.navigate(R.id.action_mainFragment_to_signInFragment)
            }
        }
        return true
    }



}
