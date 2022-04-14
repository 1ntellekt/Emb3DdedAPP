package com.example.emb3ddedapp.screens.page_news_edit

import android.app.Activity.RESULT_OK
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.widget.doOnTextChanged
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.bumptech.glide.Glide
import com.example.emb3ddedapp.R
import com.example.emb3ddedapp.databinding.PageNewsEditFragmentBinding
import com.example.emb3ddedapp.models.CurrUser
import com.example.emb3ddedapp.models.NewsItem
import com.example.emb3ddedapp.utils.*
import com.google.android.material.appbar.AppBarLayout
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.io.File
import java.io.FileNotFoundException
import java.text.SimpleDateFormat
import java.util.*


class PageNewsEditFragment : Fragment() {

    private var _binding:PageNewsEditFragmentBinding? = null
    private val binding:PageNewsEditFragmentBinding
    get() = _binding!!

    private lateinit var viewModel: PageNewsEditViewModel

    private var curNewsItem: NewsItem? = null
    private var selectedImgCurrNewsItem:String? = null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        _binding = PageNewsEditFragmentBinding.inflate(inflater,container,false)
        return binding.root
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

            curNewsItem = arguments?.getSerializable("news_item") as? NewsItem

            curNewsItem?.let {
                edDescription.setText(it.description)
                edTitle.setText(it.title)
                edTag.setText(it.tag.substringAfter("#"))
                tvHeadTitle.text = it.title
                it.img_url?.let { url->
                    Glide.with(requireContext()).load(url).into(imgNews)
                    selectedImgCurrNewsItem = url
                }
               // tvAuthor.text = curNewsItem!!.user!!.login
            }

            tvAuthor.text = CurrUser.login

            edTitle.doOnTextChanged { text, start, before, count -> edTitleLayout.error = null }
            edDescription.doOnTextChanged { text, start, before, count -> edDescriptionLayout.error = null}
            edTag.doOnTextChanged { text, start, before, count -> edTagLayout.error = null }

            btnPopMenu.setOnClickListener {
                if (edTitle.text.toString().isEmpty()){
                    edTitleLayout.error = "Input title"
                } else if (edTag.text.toString().isEmpty()){
                    edTagLayout.error = "Input tag"
                } else if (edDescription.text.toString().isEmpty()){
                    edDescriptionLayout.error = "Input description"
                } else {
                    if (selectedFileNames.isNotEmpty()){
                        viewModel.uploadFile(selectedFileNames.last(),{
                            addOrEditNewsItem(it)
                            clearFilesDir()
                        },{
                            clearFilesDir()
                        })
                    } else {
                        addOrEditNewsItem(selectedImgCurrNewsItem)
                    }
                }
            }
            btnEditNews.setOnClickListener {
                val intent = Intent(Intent.ACTION_GET_CONTENT)
                intent.type = "image/*"
                takeImgFile.launch(intent)
            }
            btnBack.setOnClickListener { APP.mNavController.navigate(R.id.action_pageNewsEditFragment_to_mainFragment) }
        }
    }

    private fun addOrEditNewsItem(filePathUrl:String?){
        binding.apply {
            if (curNewsItem == null) {
                viewModel.addNewsItem(NewsItem(title = edTitle.text.toString(), description = edDescription.text.toString(),
                    img_url = filePathUrl, tag = "#${edTag.text.toString()}", user_id = CurrUser.id))
                { APP.mNavController.navigate(R.id.action_pageNewsEditFragment_to_mainFragment)}
            } else {
                viewModel.editNewsItem(NewsItem(id = curNewsItem!!.id,title = edTitle.text.toString(), description = edDescription.text.toString(),
                    img_url = filePathUrl, tag = edTag.text.toString(), user_id = CurrUser.id))
                { APP.mNavController.navigate(R.id.action_pageNewsEditFragment_to_mainFragment)}
            }
        }
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProvider(this).get(PageNewsEditViewModel::class.java)
        // TODO: Use the ViewModel
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
        if(result.data!=null && result.resultCode == RESULT_OK){
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
                binding.imgNews.setImageURI(imgURI) // To display selected image in image view
            }
        }
    }

}