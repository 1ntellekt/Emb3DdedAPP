package com.example.emb3ddedapp.screens.profile

import android.app.Activity
import android.content.Intent
import android.graphics.Bitmap
import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import android.provider.MediaStore
import android.telephony.PhoneNumberFormattingTextWatcher
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.widget.PopupMenu
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.widget.doOnTextChanged
import com.bumptech.glide.Glide
import com.example.emb3ddedapp.R
import com.example.emb3ddedapp.databinding.ProfileFragmentBinding
import com.example.emb3ddedapp.models.CurrUser
import com.example.emb3ddedapp.models.User
import com.example.emb3ddedapp.utils.APP
import com.example.emb3ddedapp.utils.getFileFromInput
import com.example.emb3ddedapp.utils.getName
import com.example.emb3ddedapp.utils.showToast
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.io.File
import java.io.FileNotFoundException
import java.io.FileOutputStream
import java.text.SimpleDateFormat
import java.util.*

class ProfileFragment : Fragment() {

    private lateinit var viewModel: ProfileViewModel
    private var _binding:ProfileFragmentBinding? = null
    private val binding:ProfileFragmentBinding
    get() = _binding!!

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        _binding = ProfileFragmentBinding.inflate(inflater,container,false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel = ViewModelProvider(this)[ProfileViewModel::class.java]
        binding.apply {
            initProfileUser()

            btnExit.setOnClickListener {
                viewModel.signOut { APP.mNavController.navigate(R.id.action_mainFragment_to_signInFragment) }
            }

            edNumber.addTextChangedListener(PhoneNumberFormattingTextWatcher("US"))

            edNumber.doOnTextChanged { text, start, before, count ->
                text?.let { txt->
                    if (txt.toString().length != 13) {
                        edNumberLayout.error = "Number format not access!"
                    } else {
                        edNumberLayout.error = null
                    }
                }
            }

            edLogin.doOnTextChanged { text, start, before, count ->
                text?.let { txt->
                    when {
                        txt.toString().length <= 6 -> {
                            edLoginLayout.error = "More than 6 characters"
                        }
                        txt.toString().length >= 20 -> {
                            edLoginLayout.error = "Less than 20 characters"
                        }
                        else -> {
                            edLoginLayout.error = null
                        }
                    }
                }
            }

            edOldPassword.doOnTextChanged { text, start, before, count ->
                text?.let { txt->
                    if (txt.toString().isNotEmpty() && txt.toString().length <= 7) {
                        edOldPasswordLayout.error = "More than 7 characters"
                    } else {
                        edOldPasswordLayout.error = null
                    }
                }
            }

            edNewPassword.doOnTextChanged { text, start, before, count ->
                text?.let { txt->
                    if (txt.toString().isNotEmpty() && txt.toString().length <= 7) {
                        edNewPasswordLayout.error = "More than 7 characters"
                    } else {
                        edNewPasswordLayout.error = null
                    }
                }
            }

            btnEditProfilePhoto.setOnClickListener {
                showPopupMenu()
            }

            btnEditProfile.setOnClickListener {
                if (edLogin.text.toString().isEmpty()){
                    edLoginLayout.error = "Input login is empty!"
                    edLogin.requestFocus()
                }
                else if (edNumber.text.toString().isEmpty()){
                    edNumberLayout.error = "Input phone number is empty!"
                    edNumber.requestFocus()
                }
                else if (edStatus.text.toString().isEmpty()){
                    edStatusLayout.error = "Input status is empty"
                    edStatus.requestFocus()
                }
                else if (edOldPassword.text.toString().isNotEmpty() && edOldPasswordLayout.error != null){
                    edOldPasswordLayout.error = "Input password is empty!"
                    edOldPassword.requestFocus()
                }
                else if (edNewPassword.text.toString().isNotEmpty() && edNewPasswordLayout.error != null){
                    edNewPasswordLayout.error = "input confirm password is empty!"
                    edNewPassword.requestFocus()
                } else {
                    var new_pass_input:String? = null
                    var old_pass_input:String? = null
                    if (edNewPassword.text.toString().isNotEmpty() && edOldPassword.text.toString().isNotEmpty()){
                        new_pass_input = edNewPassword.text.toString()
                        old_pass_input = edOldPassword.text.toString()
                    }

                    if (selectedFileNames.isEmpty()){
                        viewModel.updateUserProfile(
                            old_pass = old_pass_input, new_pass = new_pass_input,
                            user = User(id = CurrUser.id, login = edLogin.text.toString(),
                                status = edStatus.text.toString(), number = edNumber.text.toString(), url_profile = CurrUser.url_profile)
                       ) { updatedUser ->
                            CurrUser.url_profile = updatedUser.url_profile
                            CurrUser.number = updatedUser.number
                            CurrUser.status = updatedUser.status
                            CurrUser.login = updatedUser.login
                            initProfileUser()
                        }
                    } else {
                        viewModel.uploadFile(selectedFileNames.last(),{ pathUrl->

                        viewModel.updateUserProfile(
                                old_pass = old_pass_input, new_pass = new_pass_input,
                                user = User(id = CurrUser.id, login = edLogin.text.toString(),
                                    status = edStatus.text.toString(), number = edNumber.text.toString(), url_profile = pathUrl)
                            ) { updatedUser ->
                                CurrUser.url_profile = updatedUser.url_profile
                                CurrUser.number = updatedUser.number
                                CurrUser.status = updatedUser.status
                                CurrUser.login = updatedUser.login
                                initProfileUser()
                            }

                            clearFilesDir()
                        }, {clearFilesDir()})
                    }

                }
            }

        }

    }

    private fun showPopupMenu() {
        val popupMenu = PopupMenu(context, binding.btnEditProfilePhoto)
        popupMenu.inflate(R.menu.profile_menu)
        popupMenu.setOnMenuItemClickListener { item -> popupMenuItemClicked(item) }
        try {
            val fieldPopup = PopupMenu::class.java.getDeclaredField("mPopup")
            fieldPopup.isAccessible = true
            val mPopup = fieldPopup.get(popupMenu)
            mPopup.javaClass.getDeclaredMethod("setForceShowIcon", Boolean::class.java).invoke(mPopup, true)
        }catch (e:Exception){
            Log.e("tag", e.message.toString())
        }finally {
            popupMenu.show()
        }
    }

    private fun popupMenuItemClicked(menuItem: MenuItem):Boolean {
        when(menuItem.itemId){
            R.id.takeImageFile ->{
                val intent = Intent(Intent.ACTION_GET_CONTENT)
                intent.type = "image/*"
                takeImgFile.launch(intent)
            }
            R.id.takePhotoCamera -> {
                val intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
                takePhoto.launch(intent)
            }
        }
        return true
    }

    private fun initProfileUser() {
        binding.apply {
            tvEmail.text = CurrUser.email
            edLogin.setText(CurrUser.login)
            edNumber.setText(CurrUser.number)
            edStatus.setText(CurrUser.status)
            CurrUser.url_profile?.let {
                Glide.with(imgProfile.context).load(it).into(imgProfile)
            }
            edNewPassword.setText("")
            edOldPassword.setText("")
        }
    }

    override fun onStart() {
        super.onStart()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private var currentSelectedFile: File? = null
    private val selectedFileNames = mutableListOf<File>()

    private fun clearFilesDir(){
//        if (selectedFileNames.isNotEmpty()){
//            selectedFileNames.forEach {
//                it.delete()
//            }
//            selectedFileNames.clear()
//        }
        selectedFileNames.clear()
        CoroutineScope(Dispatchers.Default).launch{
            requireActivity().filesDir.listFiles()?.forEach { file ->
                if (file.isFile && !file.isHidden && file.name.startsWith("img-", ignoreCase = true)){
                    file.delete()
                }
            }
        }
    }


    private val takeImgFile = registerForActivityResult(ActivityResultContracts.StartActivityForResult()){ result->
        if(result.data!=null && result.resultCode == Activity.RESULT_OK){
            if(result.data!!.data != null){
                val imgURI = result.data!!.data!!
                try {
                    val nowDate = SimpleDateFormat("HH:mm:ss", Locale.getDefault()).format(Date())
                    val fileName = "${requireActivity().filesDir.path}/img-$nowDate-${imgURI.getName()}"
                    currentSelectedFile = getFileFromInput(fileName,APP.contentResolver.openInputStream(imgURI))
                    if (currentSelectedFile == null){
                        showToast("File image not selected!")
                    } else {
                        selectedFileNames.add(currentSelectedFile!!)
                        currentSelectedFile = null
                    }
                } catch (e: FileNotFoundException) {
                    e.printStackTrace()
                }
                //val bmp = BitmapFactory.decodeStream(imageStream)
                binding.imgProfile.setImageURI(imgURI) // To display selected image in image view
            }
        }
    }


    private val takePhoto  = registerForActivityResult(ActivityResultContracts.StartActivityForResult()){ result->
        if(result.resultCode== AppCompatActivity.RESULT_OK &&result.data!=null){
            val bundle:Bundle = result.data!!.extras!!
            val nowDate = SimpleDateFormat("HH:mm:ss", Locale.getDefault()).format(Date())
            val bmp: Bitmap = bundle.get("data") as Bitmap
            val file = File(requireActivity().filesDir.path,"img-$nowDate.jpg")
            val fOut = FileOutputStream(file)
            bmp.compress(Bitmap.CompressFormat.JPEG, 100, fOut)
            fOut.flush()
            fOut.close()
            binding.imgProfile.setImageBitmap(bmp)
            selectedFileNames.add(file)
        }
    }



}