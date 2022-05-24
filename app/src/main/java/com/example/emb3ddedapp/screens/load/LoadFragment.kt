package com.example.emb3ddedapp.screens.load

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import com.example.emb3ddedapp.R
import com.example.emb3ddedapp.cache.entities.UserEntity
import com.example.emb3ddedapp.database.api.RetrofitInstance
import com.example.emb3ddedapp.databinding.LoadFragmentBinding
import com.example.emb3ddedapp.models.CurrUser
import com.example.emb3ddedapp.utils.APP
import com.example.emb3ddedapp.utils.getInitUserId
import com.example.emb3ddedapp.utils.getTokenAccess
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class LoadFragment : Fragment() {

    private lateinit var binding:LoadFragmentBinding

    private lateinit var mObserver: Observer<UserEntity>

    private lateinit var viewModel: LoadViewModel

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = LoadFragmentBinding.inflate(inflater,container,false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel = ViewModelProvider(this)[LoadViewModel::class.java]
        mObserver = Observer { userEntity ->
                CurrUser.status = userEntity.status
                CurrUser.email = userEntity.email
                CurrUser.login = userEntity.login
                CurrUser.number = userEntity.number
                CurrUser.uid = userEntity.uid
                CurrUser.url_profile = userEntity.url_profile
                APP.mNavController.navigate(R.id.action_loadFragment_to_mainFragment)
        }
    }

    override fun onStart() {
        super.onStart()
        if (getInitUserId() > 0){
            RetrofitInstance.setAuthorizationBearer(getTokenAccess()!!)
                CoroutineScope(Dispatchers.Main).launch {
                    binding.logoLoad.animate().apply {
                        duration = 1000
                        alpha(.5f)
                        rotation(.5f)
                        scaleXBy(.5f)
                        scaleYBy(.5f)
                        rotationYBy(360f)
                        translationYBy(200f)
                    }.withEndAction {
                        binding.logoLoad.animate().apply {
                            duration = 1000
                            alpha(1f)
                            scaleXBy(-.5f)
                            scaleYBy(-.5f)
                            rotationXBy(360f)
                            translationYBy(-200f)
                        }
                    }.start()
                    delay(2500)
                    viewModel.getDataForCurrentUser({
                        APP.mNavController.navigate(R.id.action_loadFragment_to_mainFragment)
                    }) {
                        viewModel.userLive.observe(viewLifecycleOwner,mObserver)
                    }
                }
        } else {
            CoroutineScope(Dispatchers.Main).launch {
                binding.logoLoad.animate().apply {
                    duration = 1000
                    alpha(.5f)
                    rotation(.5f)
                    scaleXBy(.5f)
                    scaleYBy(.5f)
                    rotationYBy(360f)
                    translationYBy(200f)
                }.withEndAction {
                    binding.logoLoad.animate().apply {
                        duration = 1000
                        alpha(1f)
                        scaleXBy(-.5f)
                        scaleYBy(-.5f)
                        rotationXBy(360f)
                        translationYBy(-200f)
                    }
                }.start()
                delay(2500)
                APP.mNavController.navigate(R.id.action_loadFragment_to_signInFragment)
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        viewModel.userLive.removeObserver(mObserver)
    }

}