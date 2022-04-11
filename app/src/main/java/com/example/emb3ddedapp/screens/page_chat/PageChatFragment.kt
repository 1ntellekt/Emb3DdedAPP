package com.example.emb3ddedapp.screens.page_chat

import android.app.Activity
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.graphics.Bitmap
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.example.emb3ddedapp.R
import com.example.emb3ddedapp.databinding.PageChatFragmentBinding
import com.example.emb3ddedapp.models.*
import com.example.emb3ddedapp.notification.FireServices
import com.example.emb3ddedapp.screens.page_chat.adapter.MessageAdapter
import com.example.emb3ddedapp.utils.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.io.File
import java.io.FileNotFoundException
import java.io.FileOutputStream
import java.text.SimpleDateFormat
import java.util.*


class PageChatFragment : Fragment() {

    private lateinit var viewModel: PageChatViewModel
    private var _binding:PageChatFragmentBinding? = null
    private val binding:PageChatFragmentBinding
    get() = _binding!!

    private var chatId:Int? = null
    private lateinit var mObserver: Observer<List<Message>?>
    private lateinit var cObserver: Observer<ChatDefault?>
    private lateinit var adapter:MessageAdapter
    private val messageList = mutableListOf<Message>()
    private var chatDefault:ChatDefault? = null

    private var myRecipientId:Int = 0
    private var recipientUser: User? = null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        _binding = PageChatFragmentBinding.inflate(inflater,container,false)
        return binding.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProvider(this).get(PageChatViewModel::class.java)
        // TODO: Use the ViewModel
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        chatId = arguments?.getInt("id_chat")
        recipientUser = arguments?.getSerializable("recipientUser") as? User
        adapter = MessageAdapter(CurrUser.id,{},{},{})
        binding.apply {
            btnBack.setOnClickListener {
                APP.mNavController.navigate(R.id.action_pageChatFragment_to_mainFragment)
            }
            //recyclerView.setHasFixedSize(true)
            recyclerView.adapter = adapter
            refLayout.isEnabled = false
            refLayout.setOnRefreshListener(refLayListener)
            btnSendMsg.setOnClickListener {
                if (edMessage.text.toString().isNotEmpty()){
                    chatDefault?.let {
                        viewModel.sendTextMsg(Message(chat_id = it.id, file_3d_msg = null,
                            file_msg = null, img_msg = null, user_id_recepient = myRecipientId, user_id_sender = CurrUser.id, text_msg = edMessage.text.toString()))
                        edMessage.setText("")
                    }
                }
            }
            btnTakePhoto.setOnClickListener {
                val intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
                takePhoto.launch(intent)
            }
            btnTakeFile.setOnClickListener {
                val intent = Intent(Intent.ACTION_GET_CONTENT)
                intent.type = "*/*"
                takeFile.launch(intent)
            }
            btnCallPhone.setOnClickListener {

            }
            recipientUser?.let {
                tvLoginUser.text = it.login
                tvStatusUser.text = it.status
            }
        }
        mObserver = Observer { messages->
            messages?.let { list->
                messageList.clear()
                messageList.addAll(list)
                adapter.setData(list)
            }
        }
        cObserver = Observer {
            it?.let { chatDef ->
                chatDefault = chatDef
                myRecipientId = if (chatDef.user_id_first == CurrUser.id){
                    chatDef.user_id_second
                } else {
                    chatDef.user_id_first
                }
            }
        }
    }

    private val refLayListener = SwipeRefreshLayout.OnRefreshListener {
        chatId?.let { viewModel.getMessagesByChatId(it) }
        binding.refLayout.isRefreshing = false
        binding.refLayout.isEnabled = false
    }

    override fun onStart() {
        super.onStart()
        viewModel.messagesList.observe(this,mObserver)
        viewModel.chatDef.observe(this,cObserver)
        chatId?.let {
            viewModel.getMessagesByChatId(it)
        }



        val intentFilter = IntentFilter()
        intentFilter.addAction(FireServices.PUSH_TAG)
        requireActivity().registerReceiver(broadcastReceiver,intentFilter)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        viewModel.messagesList.removeObserver(mObserver)
        viewModel.chatDef.removeObserver(cObserver)
        requireActivity().unregisterReceiver(broadcastReceiver)
        _binding = null
    }

    private val broadcastReceiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context?, intent: Intent?) {
            if (intent?.extras!=null){
                val extras = intent.extras
                //Log.d("msg","Message received!")
                extras?.keySet()?.firstOrNull{it == FireServices.KEY_ACTION}?.let { key->
                    //Log.d("msg","key: $key")
                    when(extras.getString(key)){
                        FireServices.ACTION_SHOW_MESSAGE -> {
                            binding.refLayout.isEnabled = true
                            refLayListener.onRefresh()
                            //viewModel.getMessagesByChatId(chatId!!)
                        }
                        else ->{
                            Log.d("msg","key error not found")}
                    }
                }
            }
        }
    }


    private var currentSelectedFile: File? = null
    //private val selectedFileNames = mutableListOf<File>()

    private fun clearFilesDir(){
//        if (selectedFileNames.isNotEmpty()){
//            selectedFileNames.forEach {
//                it.delete()
//            }
//            selectedFileNames.clear()
//        }
        //selectedFileNames.clear()
        CoroutineScope(Dispatchers.Default).launch {
            requireActivity().filesDir.listFiles()?.forEach { file ->
                if (file.isFile && !file.isHidden &&
                    (
                            file.name.startsWith("file-", ignoreCase = true)
                            || file.name.startsWith("img-", ignoreCase = true)
                            || file.name.startsWith("3dfile-", ignoreCase = true)
                    )
                ){
                    file.delete()
                }
            }
        }
    }

    private val takeFile = registerForActivityResult(ActivityResultContracts.StartActivityForResult()){ result->
        if(result.data!=null && result.resultCode == Activity.RESULT_OK){
            if(result.data!!.data != null){
                val fileURI = result.data!!.data!!
                try {
                    val nowDate = SimpleDateFormat("HH:mm:ss", Locale.getDefault()).format(Date())
                    var fileName = "$nowDate-${fileURI.getName()}"
                    fileName = if (fileName.endsWith(".glb") || fileName.endsWith(".gltf")){
                        "3dfile-$fileName"
                    } else {
                        "file-$fileName"
                    }
                    val fullFileName = "${requireActivity().filesDir.path}/$fileName"
                    currentSelectedFile = getFileFromInput(fullFileName,APP.contentResolver.openInputStream(fileURI))
                    if (currentSelectedFile == null){
                        showToast("File image not selected!")
                    } else {
                        //selectedFileNames.add(currentSelectedFile!!)
                        if (fileName.startsWith("file-")){
                            viewModel.sendFileMsg(currentSelectedFile!!,chatId!!, recipientUser!!.id,{clearFilesDir()},{clearFilesDir()})
                        } else if (fileName.startsWith("3dfile-")){
                            viewModel.send3dFile(currentSelectedFile!!,chatId!!, recipientUser!!.id,{clearFilesDir()},{clearFilesDir()})
                        }
                        currentSelectedFile = null
                    }
                } catch (e: FileNotFoundException) {
                    e.printStackTrace()
                }
                //val bmp = BitmapFactory.decodeStream(imageStream)
            }
        }
    }

    private val takePhoto  = registerForActivityResult(ActivityResultContracts.StartActivityForResult()){result->
        if(result.resultCode== AppCompatActivity.RESULT_OK &&result.data!=null){
            val bundle:Bundle = result.data!!.extras!!
            val nowDate = SimpleDateFormat("HH:mm:ss", Locale.getDefault()).format(Date())
            val bmp: Bitmap = bundle.get("data") as Bitmap
            val file = File(requireActivity().filesDir.path,"img-$nowDate.jpg")
            val fOut = FileOutputStream(file)
            bmp.compress(Bitmap.CompressFormat.JPEG, 100, fOut)
            fOut.flush()
            fOut.close()
            //selectedFileNames.add(file)
            viewModel.sendImage(file, chatId!!, recipientUser!!.id,{clearFilesDir()},{clearFilesDir()})
        }
    }

}