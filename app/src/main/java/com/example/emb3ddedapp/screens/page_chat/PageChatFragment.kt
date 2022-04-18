package com.example.emb3ddedapp.screens.page_chat

import android.app.Activity
import android.app.AlertDialog
import android.app.Dialog
import android.content.*
import android.graphics.Bitmap
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.net.Uri
import android.os.Bundle
import android.os.CountDownTimer
import android.os.Environment.DIRECTORY_DOWNLOADS
import android.os.Handler
import android.os.Looper
import android.provider.MediaStore
import android.util.Log
import android.view.*
import android.widget.ImageView
import android.widget.PopupMenu
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearSmoothScroller
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.bumptech.glide.Glide
import com.example.emb3ddedapp.R
import com.example.emb3ddedapp.databinding.ChangeMsgDialogBinding
import com.example.emb3ddedapp.databinding.PageChatFragmentBinding
import com.example.emb3ddedapp.models.*
import com.example.emb3ddedapp.notification.FireServices
import com.example.emb3ddedapp.progressdialog.MyProgressDialog
import com.example.emb3ddedapp.screens.page_chat.adapter.MessageAdapter
import com.example.emb3ddedapp.utils.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.io.File
import java.io.FileNotFoundException
import java.io.FileOutputStream
import java.net.URLEncoder
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
    private var recipientUser: User? = null

    private var iScrollDown = true
    private var partnerDownload3DFiles = false
    private var iDownloadFile = false

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
        adapter = MessageAdapter(CurrUser.id,
        { idItemPanel3d, viewPanel->
            //click panel 3d
            popupMenu3Dialog(messageList[idItemPanel3d], viewPanel)
        },
        { idItemPanelFile, viewPanel->
          //click file panel
            popupMenuFileDialog(messageList[idItemPanelFile], viewPanel)
        },
        { idImgItem->
          //on image click
          popupMenuImageDialog(messageList[idImgItem])
        },
        { idFileItem->
          //file btn click
            downloadFromUrl(messageList[idFileItem].file_msg!!,"file")
        },
        { id3dFileItem->
            //file btn 3d click
            if (iDownloadFile || messageList[id3dFileItem].user_id_sender == CurrUser.id)
            downloadFromUrl(messageList[id3dFileItem].file_3d_msg!!,"3dfile")
            else showToast("No access!")
        },
        { idMsgText, viewTextMsg->
            // message click
            if (messageList[idMsgText].user_id_sender == CurrUser.id)
            popupMenuMsgTextDialog(messageList[idMsgText], viewTextMsg)
        })
        binding.apply {
            btnBack.setOnClickListener {
                APP.mNavController.navigate(R.id.action_pageChatFragment_to_mainFragment, Bundle().also { it.putString("nav", "chats") })
            }

            recyclerView.setHasFixedSize(true)
            recyclerView.adapter = adapter

            btnDownScroll.setOnClickListener {
                binding.btnDownScroll.visibility = View.GONE
                recyclerView.setOnScrollChangeListener(null)
                iScrollDown = true
                smoother.targetPosition = messageList.size-1
                binding.recyclerView.layoutManager!!.startSmoothScroll(smoother)
            }

            refLayout.isEnabled = false
            refLayout.setOnRefreshListener(refLayListener)

            btnSendMsg.setOnClickListener {
                if (edMessage.text.toString().isNotEmpty()){
                    chatDefault?.let {
                        viewModel.sendTextMsg(Message(chat_id = it.id, file_3d_msg = null,
                            file_msg = null, img_msg = null, user_id_recepient = recipientUser!!.id, user_id_sender = CurrUser.id, text_msg = edMessage.text.toString()))
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
            btnPopMenu.setOnClickListener {
                popupMenuChat(it)
            }
            recipientUser?.let {
                tvLoginUser.text = it.login
                tvStatusUser.text = it.status
                it.url_profile?.let { url->
                    Glide.with(imgUser.context).load(url).into(imgUser)
                }
            }
        }
        mObserver = Observer { messages->
            messages?.let { list->
                messageList.clear()
                messageList.addAll(list)
                adapter.setData(list)
                if (iScrollDown){
                    if (list.isNotEmpty()){
                        smoother.targetPosition = messageList.size-1
                        binding.recyclerView.layoutManager!!.startSmoothScroll(smoother)
                    }
                    Handler(Looper.getMainLooper()).postDelayed({binding.recyclerView.setOnScrollChangeListener(scrollListener)},1000)
                }
            }
        }
        cObserver = Observer {
            it?.let { chatDef ->
                chatDefault = chatDef
                setDownloadFlag(chatDef.user_id_first,chatDef.user_id_second, chatDef.download_first, chatDef.download_second)
            }
        }
    }

    private fun popupMenuMsgTextDialog(message: Message, viewTextMsg: View) {
        val popupMenu = PopupMenu(context, viewTextMsg)
        popupMenu.inflate(R.menu.msg_menu)
        popupMenu.setOnMenuItemClickListener { item -> showPopupMenuMsgClicked(item, message) }
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

    private fun showPopupMenuMsgClicked(menuItem: MenuItem, message: Message):Boolean {
        when(menuItem.itemId){
            R.id.deleteMsg -> {
                viewModel.deleteMsg(message)
            }
            R.id.editMsg -> {
                changeTextMsgDialog(message)
            }
        }
        return true
    }

    private fun changeTextMsgDialog(message: Message) {
        val alertDialogBuilder = AlertDialog.Builder(requireContext())
        val binding: ChangeMsgDialogBinding = ChangeMsgDialogBinding.inflate(LayoutInflater.from(requireContext()))
        alertDialogBuilder.setView(binding.root)
        binding.edMessage.setText(message.text_msg!!)
        alertDialogBuilder
            .setPositiveButton("Ok"){ dialog, which ->
                if (binding.edMessage.text.toString().isEmpty()){
                    showToast("Input text message is null!")
                } else {
                    viewModel.editMsg(message.copy(text_msg = binding.edMessage.text.toString()))
                }
            }
            .setNegativeButton("Cancel"){ dialog, which->
                dialog.dismiss()
            }.create().show()
    }


    private fun popupMenuFileDialog(message: Message, viewPanel: View) {
        val popupMenu = PopupMenu(context, viewPanel)
        popupMenu.inflate(R.menu.file_menu)
        popupMenu.setOnMenuItemClickListener { item -> showPopupMenuFileClicked(item, message) }
        try {
            val fieldPopup = PopupMenu::class.java.getDeclaredField("mPopup")
            fieldPopup.isAccessible = true
            val mPopup = fieldPopup.get(popupMenu)
            mPopup.javaClass.getDeclaredMethod("setForceShowIcon", Boolean::class.java).invoke(mPopup, true)
        }catch (e:Exception){
            Log.e("tag", e.message.toString())
        }finally {
            if (CurrUser.id != message.user_id_sender){
                val item = popupMenu.menu.findItem(R.id.deleteFile)
                item.isVisible = false
            }
            popupMenu.show()
        }
    }

    private fun showPopupMenuFileClicked(menuItem: MenuItem, message: Message):Boolean {
        when(menuItem.itemId){
            R.id.downloadFile -> {
                downloadFromUrl(message.file_msg!!,"file")
            }
            R.id.deleteFile -> {
                viewModel.deleteMsg(message)
            }
        }
        return true
    }

    private fun popupMenuChat(it: View) {
        val popupMenu = PopupMenu(context, it)
        popupMenu.inflate(R.menu.chat_menu)
        popupMenu.setOnMenuItemClickListener { item -> showPopupMenuChatClicked(item) }
        try {
            val fieldPopup = PopupMenu::class.java.getDeclaredField("mPopup")
            fieldPopup.isAccessible = true
            val mPopup = fieldPopup.get(popupMenu)
            mPopup.javaClass.getDeclaredMethod("setForceShowIcon", Boolean::class.java).invoke(mPopup, true)
        }catch (e:Exception){
            Log.e("tag", e.message.toString())
        }finally {
            val item: MenuItem = popupMenu.menu.findItem(R.id.setAccess)
            item.isChecked = partnerDownload3DFiles
            popupMenu.show()
        }
    }

    private fun showPopupMenuChatClicked(menuItem: MenuItem):Boolean {
        when(menuItem.itemId){
            R.id.setAccess ->{
                //menuItem.isChecked = iDownload3DFiles

                //menuItem.isChecked = !menuItem.isChecked

                partnerDownload3DFiles = !partnerDownload3DFiles

                when (CurrUser.id) {
                    chatDefault!!.user_id_first -> {
                        viewModel.updateChat(idChat = chatId!!, downloadFirst = partnerDownload3DFiles.toInt(), downloadSecond = chatDefault!!.download_second)
                    }
                    chatDefault!!.user_id_second -> {
                        viewModel.updateChat(idChat = chatId!!, downloadFirst = chatDefault!!.download_first, downloadSecond = partnerDownload3DFiles.toInt())
                    }
                }

            }
        }

        return true
    }

    private fun popupMenu3Dialog(message: Message, view: View) {
        val popupMenu = PopupMenu(context, view)
        popupMenu.inflate(R.menu.file_3d_menu)
        popupMenu.setOnMenuItemClickListener { item -> showPopupMenuItemClicked(item, message) }
        try {
            val fieldPopup = PopupMenu::class.java.getDeclaredField("mPopup")
            fieldPopup.isAccessible = true
            val mPopup = fieldPopup.get(popupMenu)
            mPopup.javaClass.getDeclaredMethod("setForceShowIcon", Boolean::class.java).invoke(mPopup, true)
        }catch (e:Exception){
            Log.e("tag", e.message.toString())
        }finally {
            if (CurrUser.id != message.user_id_sender){
                val item = popupMenu.menu.findItem(R.id.delete3d)
                item.isVisible = false
            }
            popupMenu.show()
        }
    }

    private fun showPopupMenuItemClicked(menuItem: MenuItem, message: Message):Boolean {
        when(menuItem.itemId) {
            R.id.copyLink -> {
                val clipboard = APP.getSystemService(ClipboardManager::class.java)
                val clip = ClipData.newPlainText("Copied link", "$VIEWER_WEB_PAGE${URLEncoder.encode(message.file_3d_msg,"UTF-8")}")
                clipboard.setPrimaryClip(clip)
                showToast("Url link copied!")
            }
            R.id.showViewer -> {
                val sceneViewerIntent = Intent(Intent.ACTION_VIEW)
                sceneViewerIntent.data =
                Uri.parse("$VIEW_URL_ARC${message.file_3d_msg}")
                sceneViewerIntent.setPackage(getString(R.string.scene_viewer_package))
                startActivity(sceneViewerIntent)
            }
            R.id.showOnWebView -> {
                val bundle = Bundle()
                bundle.putString("urlModel", "$VIEWER_WEB_PAGE${URLEncoder.encode(message.file_3d_msg,"UTF-8")}")
                APP.mNavController.navigate(R.id.action_pageChatFragment_to_webViewFragment, bundle)
            }
            R.id.chooserToSend -> {
                val intent = Intent(Intent.ACTION_SEND)
                val shareBody = "$VIEWER_WEB_PAGE${URLEncoder.encode(message.file_3d_msg,"UTF-8")}"
                intent.type = "text/plain"
                intent.putExtra(Intent.EXTRA_SUBJECT, "share")
                intent.putExtra(Intent.EXTRA_TEXT, shareBody)
                startActivity(Intent.createChooser(intent, "Choose APP"))
            }
            R.id.delete3d -> {
                viewModel.deleteMsg(message)
            }
            R.id.download3d -> {
                if (iDownloadFile || message.user_id_sender == CurrUser.id)
                downloadFromUrl(message.file_3d_msg!!,"3dfile")
                else showToast("No access!")
            }
        }
        return true
    }

    private fun popupMenuImageDialog(message: Message) {
        val dialog = context?.let { Dialog(it) }
        dialog?.let {
            dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
            dialog.setContentView(R.layout.img_layout)

            val imgMess = dialog.findViewById<ImageView>(R.id.imgMsg)

            //dialog.dismiss()
            Glide.with(imgMess.context).load(message.img_msg!!).into(imgMess)
            imgMess.setOnLongClickListener {
                val popupMenu = PopupMenu(imgMess.context, imgMess)
                popupMenu.inflate(R.menu.img_menu)
                popupMenu.setOnMenuItemClickListener { item -> showPopupMenuImgClicked(item,message) }
                try {
                    val fieldPopup = PopupMenu::class.java.getDeclaredField("mPopup")
                    fieldPopup.isAccessible = true
                    val mPopup = fieldPopup.get(popupMenu)
                    mPopup.javaClass.getDeclaredMethod("setForceShowIcon", Boolean::class.java).invoke(mPopup, true)
                }catch (e:Exception){
                    Log.e("tag", e.message.toString())
                }finally {
                    if (CurrUser.id != message.user_id_sender){
                        val item = popupMenu.menu.findItem(R.id.deleteImg)
                        item.isVisible = false
                    }
                    popupMenu.show()
                }
                true
            }

            dialog.show()
            dialog.window?.setLayout(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT
            )
            dialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
            dialog.window?.attributes?.windowAnimations = R.style.DialogAnimation
            dialog.window?.setGravity(Gravity.CENTER_VERTICAL)
        }
    }

    private fun showPopupMenuImgClicked(menuItem: MenuItem, message: Message):Boolean {
        when(menuItem.itemId){
            R.id.shareImg -> {
                val intent = Intent(Intent.ACTION_SEND)
                intent.type = "text/plain"
                intent.putExtra(Intent.EXTRA_SUBJECT, "share")
                intent.putExtra(Intent.EXTRA_TEXT, message.img_msg)
                startActivity(Intent.createChooser(intent, "Choose APP"))
            }
            R.id.downloadImg -> {
                downloadFromUrl(message.img_msg!!,"img")
            }
            R.id.deleteImg -> {
                viewModel.deleteMsg(message)
            }
        }
        return true
    }


    private fun downloadFromUrl(url: String, name: String) {
        val nowDate = SimpleDateFormat(TIME_PAT, Locale.getDefault()).format(Date())
        val extension = url.substringAfterLast(".")
        downloadManager("$name-$nowDate.$extension", DIRECTORY_DOWNLOADS,url)
    }


    private val scrollListener = View.OnScrollChangeListener { v, scrollX, scrollY, oldScrollX, oldScrollY ->
            if (scrollY > oldScrollY) {
                //Log.i("tag", "Scroll DOWN scrollY:$scrollY oldScrollY:$oldScrollY")
                binding.btnDownScroll.visibility = View.GONE
            }
            if (scrollY < oldScrollY) {
                //Log.i("tag", "Scroll UP scrollY:$scrollY oldScrollY:$oldScrollY")
                binding.btnDownScroll.visibility = View.VISIBLE
            }
            iScrollDown = false
            //Log.i("tag", "iScrollDown: $iScrollDown")
        }

    private val smoother = object : LinearSmoothScroller(APP.applicationContext){
        override fun getVerticalSnapPreference(): Int {
            return LinearSmoothScroller.SNAP_TO_END
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

        val myProgress = MyProgressDialog(requireContext())
        myProgress.load("Loading chat....")
        object : CountDownTimer(3000,1000){
            override fun onTick(millisUntilFinished: Long) {}
            override fun onFinish() {
                myProgress.dismiss()
                chatId?.let {
                    viewModel.getMessagesByChatId(it)
                }
            }
        }.start()

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
                            /*  val message = Message(
                                id = extras.getInt(FireServices.KEY_ID),
                                chat_id = extras.getInt(FireServices.KEY_CHAT_ID),
                                text_msg = extras.getString(FireServices.KEY_TEXT_MSG),
                                img_msg = extras.getString(FireServices.KEY_IMG_MSG),
                                file_msg = extras.getString(FireServices.KEY_FILE_MSG),
                                file_3d_msg = extras.getString(FireServices.KEY_3D_FILE_MSG),
                                created_at = extras.getString(FireServices.KEY_CREATED_AT),
                                user_id_sender = extras.getInt(FireServices.KEY_ID_SENDER),
                                user_id_recepient = extras.getInt(FireServices.KEY_ID_RECIPIENT)
                            )
                            adapter.addItem(message)
                            messageList.add(message)*/
                        }
                        FireServices.ACTION_CHAT -> {

                            val id = extras.getString(FireServices.KEY_ID)!!.toInt()
                            val downloadFirst = extras.getString(FireServices.KEY_D_FIRST)!!.toInt()
                            val downloadSecond = extras.getString(FireServices.KEY_D_SECOND)!!.toInt()
 //                           iDownloadFile = (CurrUser.id == chatDefault!!.user_id_first && downloadFirst ==1) || (CurrUser.id == chatDefault!!.user_id_second && downloadSecond == 1)
                            chatDefault?.let {
                                if (id == it.id){
                                    setDownloadFlag(it.user_id_first,it.user_id_second, downloadFirst, downloadSecond)
                                } else{
                                    Log.d("msg","chat get error")
                                }
                            }

                        }
                        else ->{
                            Log.d("msg","key error not found")}
                    }
                }
            }
        }
    }

    private fun setDownloadFlag(first_id:Int, second_id:Int, firstBool:Int, secondBool:Int){
        partnerDownload3DFiles = (CurrUser.id == first_id && firstBool == 1) || (CurrUser.id == second_id && secondBool == 1)
        //iDownloadFile = (CurrUser.id == first_id && firstBool ==1) || (CurrUser.id == second_id && secondBool == 1)
        if (recipientUser!!.id == first_id){
            iDownloadFile = (firstBool == 1)
        } else if (recipientUser!!.id == second_id) {
            iDownloadFile = (secondBool == 1)
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
                    val nowDate = SimpleDateFormat(TIME_PAT, Locale.getDefault()).format(Date())
                    var fileName = "$nowDate-${fileURI.getName()}"
                    fileName = if (fileName.endsWith(".glb") || fileName.endsWith(".gltf")){
                        "3dfile-$fileName"
                    } else if (fileName.endsWith(".jpeg") || fileName.endsWith(".jpg") || fileName.endsWith(".png")){
                        "img-$fileName"
                    } else {
                        "file-$fileName"
                    }
                    val fullFileName = "${requireActivity().filesDir.path}/$fileName"
                    currentSelectedFile = getFileFromInput(fullFileName,APP.contentResolver.openInputStream(fileURI))
                    if (currentSelectedFile == null){
                        showToast("File image not selected!")
                    } else {
                        //selectedFileNames.add(currentSelectedFile!!)
                        when {
                            fileName.startsWith("file-") -> {
                                viewModel.sendFileMsg(currentSelectedFile!!,chatId!!, recipientUser!!.id,{clearFilesDir()},{clearFilesDir()})
                            }
                            fileName.startsWith("3dfile-") -> {
                                viewModel.send3dFile(currentSelectedFile!!,chatId!!, recipientUser!!.id,{clearFilesDir()},{clearFilesDir()})
                            }
                            else -> {
                                viewModel.sendImage(currentSelectedFile!!,chatId!!,recipientUser!!.id, {clearFilesDir()}, {clearFilesDir()})
                            }
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
            val nowDate = SimpleDateFormat(TIME_PAT, Locale.getDefault()).format(Date())
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