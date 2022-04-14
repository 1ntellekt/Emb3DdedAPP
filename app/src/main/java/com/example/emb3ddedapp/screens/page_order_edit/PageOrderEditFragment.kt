package com.example.emb3ddedapp.screens.page_order_edit

import android.app.Activity
import android.content.Intent
import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.widget.doOnTextChanged
import com.bumptech.glide.Glide
import com.example.emb3ddedapp.R
import com.example.emb3ddedapp.databinding.PageOrderEditFragmentBinding
import com.example.emb3ddedapp.models.CurrUser
import com.example.emb3ddedapp.models.Order
import com.example.emb3ddedapp.utils.*
import com.google.android.material.appbar.AppBarLayout
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.io.File
import java.io.FileNotFoundException
import java.text.SimpleDateFormat
import java.util.*

class PageOrderEditFragment : Fragment() {

    private var _binding: PageOrderEditFragmentBinding? = null
    private val binding: PageOrderEditFragmentBinding
        get() = _binding!!

    private lateinit var viewModel: PageOrderEditViewModel

    private var curOrder:Order? = null
    private var selectedImgCurrOrder:String? = null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        _binding = PageOrderEditFragmentBinding.inflate(inflater,container,false)
        return binding.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProvider(this).get(PageOrderEditViewModel::class.java)
        // TODO: Use the ViewModel
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.apply {
            appbar.addOnOffsetChangedListener(object : AppBarLayout.OnOffsetChangedListener {
                var scrollRange = -1
                override fun onOffsetChanged(appBarLayout: AppBarLayout, verticalOffset: Int) {
                    //Initialize the size of the scroll
                    if (scrollRange == -1) {
                        scrollRange = appBarLayout.totalScrollRange
                    }
                    //Check if the view is collapsed
                    if (scrollRange + verticalOffset == 0) {
                        tvHeadTitle.visibility = View.VISIBLE
                    } else{
                        tvHeadTitle.visibility = View.INVISIBLE
                    }
                }
            })

            curOrder = arguments?.getSerializable("order") as Order?
            curOrder?.let {
                edDescription.setText(it.description)
                edTitle.setText(it.title)
                tvHeadTitle.text = it.title
                it.img_url?.let { url->
                    Glide.with(requireContext()).load(url).into(imgOrder)
                    selectedImgCurrOrder = url
                }
                //tvAuthor.text = curOrder!!.user!!.login
            }

            tvAuthor.text = CurrUser.login

            btnBack.setOnClickListener {
                APP.mNavController.navigate(R.id.action_pageOrderEditFragment_to_mainFragment)
            }

            edDescription.doOnTextChanged { text, start, before, count -> edDescriptionLayout.error = null}
            edTitle.doOnTextChanged { text, start, before, count -> edTitleLayout.error = null }

            btnPopMenu.setOnClickListener {
                if (edTitle.text.toString().isEmpty()){
                    edTitleLayout.error = "Input title!"
                } else if(edDescription.text.toString().isEmpty()){
                    edDescriptionLayout.error = "Input description"
                } else {
                    if (selectedFileNames.isNotEmpty()){
                        viewModel.uploadFile(selectedFileNames.last(),{
                            addOrEditOrder(it)
                            clearFilesDir()
                        },{
                            clearFilesDir()
                        })
                    } else {
                        addOrEditOrder(selectedImgCurrOrder)
                    }
                }
            }

            btnEditOrder.setOnClickListener {
                val intent = Intent(Intent.ACTION_GET_CONTENT)
                intent.type = "image/*"
                takeImgFile.launch(intent)
            }

        }
    }

    private fun addOrEditOrder(filePathUrl:String?){
        binding.apply {
            if (curOrder == null) {
                viewModel.addOrder(Order(title = edTitle.text.toString(),
                    description = edDescription.text.toString(), user_id = CurrUser.id, img_url = filePathUrl)
                ){ APP.mNavController.navigate(R.id.action_pageOrderEditFragment_to_mainFragment)}
            } else {
                viewModel.updateOrder(Order(id = curOrder!!.id,title = edTitle.text.toString(),
                    description = edDescription.text.toString(), user_id = CurrUser.id, img_url = filePathUrl)
                ){ APP.mNavController.navigate(R.id.action_pageOrderEditFragment_to_mainFragment)}
            }
        }
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
                    val nowDate = SimpleDateFormat(TIME_PAT, Locale.getDefault()).format(Date())
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
                binding.imgOrder.setImageURI(imgURI) // To display selected image in image view
            }
        }
    }

}