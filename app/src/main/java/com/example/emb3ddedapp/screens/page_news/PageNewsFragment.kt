package com.example.emb3ddedapp.screens.page_news

import android.app.Dialog
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.*
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.DividerItemDecoration
import com.bumptech.glide.Glide
import com.example.emb3ddedapp.R
import com.example.emb3ddedapp.databinding.DialogRatingSetLayoutBinding
import com.example.emb3ddedapp.databinding.PageNewsFragmentBinding
import com.example.emb3ddedapp.models.CurrUser
import com.example.emb3ddedapp.models.NewsItem
import com.example.emb3ddedapp.models.Rating
import com.example.emb3ddedapp.models.RatingL
import com.example.emb3ddedapp.screens.page_news.adapter.CommentAdapter
import com.example.emb3ddedapp.utils.APP
import com.example.emb3ddedapp.utils.getDataTimeWithFormat
import com.google.android.material.appbar.AppBarLayout


class PageNewsFragment : Fragment() {

    private var _binding:PageNewsFragmentBinding? = null
    private val binding:PageNewsFragmentBinding
    get() = _binding!!

    private lateinit var adapter:CommentAdapter
    private lateinit var mObserver:Observer<List<RatingL>?>
    private lateinit var viewModel: PageNewsViewModel

    private var currNewsItem:NewsItem? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        currNewsItem = arguments?.getSerializable("news_item") as? NewsItem
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        _binding = PageNewsFragmentBinding.inflate(inflater,container,false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel = ViewModelProvider(this)[PageNewsViewModel::class.java]
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
            currNewsItem?.let { newsItem ->
                newsItem.img_url?.let { url->
                    Glide.with(requireContext()).load(url).into(imgNews)
                }

                tvRating.text = newsItem.avgMark.toString()

                tvDescription.text = newsItem.description
                tvHeadTitle.text = newsItem.title
                tvTag.text = newsItem.tag
                tvTitle.text = newsItem.title
                tvAuthor.text = newsItem.user!!.login
                tvDateTime.text = getDataTimeWithFormat(newsItem.created_at!!)
                newsItem.user.url_profile?.let { url->
                    Glide.with(imgPerson.context).load(url).into(imgPerson)
                }
            }
            btnBack.setOnClickListener { APP.mNavController.navigate(R.id.action_pageNewsFragment_to_mainFragment, Bundle().also { it.putString("nav", "news") })}
            btnSetRating.setOnClickListener {
                dialogRating()
            }
            adapter = CommentAdapter()
            val decoration = DividerItemDecoration(requireContext(), DividerItemDecoration.VERTICAL)
            recyclerView.setHasFixedSize(true)
            recyclerView.addItemDecoration(decoration)
            recyclerView.adapter = adapter
            mObserver = Observer { listComment->
                listComment?.let { list ->
                    adapter.setData(list)
                }
            }
        }
    }

    private fun dialogRating() {
        val dialog = context?.let { Dialog(it) }
        dialog?.let { dia->
            dia.requestWindowFeature(Window.FEATURE_NO_TITLE)
            val binding: DialogRatingSetLayoutBinding = DialogRatingSetLayoutBinding.inflate(dia.layoutInflater)
            dia.setContentView(binding.root)

            viewModel.getMark(currNewsItem!!.id){
                binding.tvRating.visibility = View.VISIBLE
                binding.ratingBar.rating = it.mark.toFloat()
                binding.edComment.setText(it.comment)
            }

            binding.apply {
                btnOk.setOnClickListener {
                    if (edComment.text.isNullOrEmpty()){
                        viewModel.addMark(Rating(user_id = CurrUser.id,news_items_id = currNewsItem!!.id, mark = ratingBar.rating.toDouble())){
                            dia.dismiss()
                        }
                    } else {
                        viewModel.addMark(Rating(user_id = CurrUser.id, comment = edComment.text.toString(),news_items_id = currNewsItem!!.id, mark = ratingBar.rating.toDouble())){
                            dia.dismiss()
                        }
                    }
                }
            }

            dia.show()
            dia.window?.setLayout(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT
            )
            dia.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
            dia.window?.attributes?.windowAnimations = R.style.DialogAnimation
            dia.window?.setGravity(Gravity.BOTTOM)
        }
    }

    override fun onStart() {
        super.onStart()
        viewModel.ratingList.observe(viewLifecycleOwner,mObserver)
        currNewsItem?.let { newsItem ->
           viewModel.getMarksByNews(newsItemId = newsItem.id)
       }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        viewModel.ratingList.removeObserver(mObserver)
        _binding = null
    }

}