package com.example.emb3ddedapp.screens.load

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.emb3ddedapp.R
import com.example.emb3ddedapp.databinding.LoadFragmentBinding
import com.example.emb3ddedapp.utils.APP
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class LoadFragment : Fragment() {

    private lateinit var binding:LoadFragmentBinding

    companion object {
        fun newInstance() = LoadFragment()
    }

    private lateinit var viewModel: LoadViewModel

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = LoadFragmentBinding.inflate(inflater,container,false)
        return binding.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProvider(this).get(LoadViewModel::class.java)
        // TODO: Use the ViewModel
    }

    override fun onStart() {
        super.onStart()

//        if (getInitUser()){
//            viewModel.getDataForCurrentUser {
//                CoroutineScope(Dispatchers.Main).launch {
//                    binding.logoLoad.animate().apply {
//                        duration = 1000
//                        alpha(.5f)
//                        rotation(.5f)
//                        scaleXBy(.5f)
//                        scaleYBy(.5f)
//                        rotationYBy(360f)
//                        translationYBy(200f)
//                    }.withEndAction {
//                        binding.logoLoad.animate().apply {
//                            duration = 1000
//                            alpha(1f)
//                            scaleXBy(-.5f)
//                            scaleYBy(-.5f)
//                            rotationXBy(360f)
//                            translationYBy(-200f)
//                        }
//                    }.start()
//                    delay(2500)
//                    APP.mNavController.navigate(R.id.action_loadFragment_to_mainFragment)
//                }
//            }
//        } else {
//            CoroutineScope(Dispatchers.Main).launch {
//                binding.logoLoad.animate().apply {
//                    duration = 1000
//                    alpha(.5f)
//                    rotation(.5f)
//                    scaleXBy(.5f)
//                    scaleYBy(.5f)
//                    rotationYBy(360f)
//                    translationYBy(200f)
//                }.withEndAction {
//                    binding.logoLoad.animate().apply {
//                        duration = 1000
//                        alpha(1f)
//                        scaleXBy(-.5f)
//                        scaleYBy(-.5f)
//                        rotationXBy(360f)
//                        translationYBy(-200f)
//                    }
//                }.start()
//                delay(2500)
//                APP.mNavController.navigate(R.id.action_loadFragment_to_signInFragment)
//            }
//        }
    }

}