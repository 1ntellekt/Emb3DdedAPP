package com.example.emb3ddedapp.screens.mainfrag

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import android.view.*
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.example.emb3ddedapp.R
import com.example.emb3ddedapp.databinding.MainFragmentBinding
import com.example.emb3ddedapp.models.CurrUser
import com.example.emb3ddedapp.utils.APP

class MainFragment : Fragment() {

    private var _binding:MainFragmentBinding? = null
    private val binding:MainFragmentBinding
    get() = _binding!!

    companion object {
        fun newInstance() = MainFragment()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
    }

    private lateinit var viewModel: MainViewModel

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        _binding = MainFragmentBinding.inflate(inflater, container,false)
         (requireActivity() as AppCompatActivity).setSupportActionBar(binding.toolbar)
        return binding.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProvider(this).get(MainViewModel::class.java)
        // TODO: Use the ViewModel
    }

    override fun onStart() {
        super.onStart()
        binding.tvTest.text = "${CurrUser.id} ${CurrUser.email} ${CurrUser.login} ${CurrUser.password} " +
                "${CurrUser.profileUrlPhoto} ${CurrUser.status} ${CurrUser.telNumber} ${CurrUser.tokenMsg}"
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.app_main_menu,menu)
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
