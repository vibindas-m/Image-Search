package com.example.imagesearch.ui.main

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.imagesearch.R
import com.example.imagesearch.ui.extension.loadImageUrl
import kotlinx.android.synthetic.main.fragment_image_detail.*
import kotlinx.android.synthetic.main.image_search_list_item.*
import org.koin.androidx.viewmodel.ext.android.sharedViewModel

class ImageDetailFragment : Fragment() {

    private val viewModel: MainViewModel by sharedViewModel()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_image_detail, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel.getSelectedImage()?.let {
            imageNameTv.text = it.title ?: ""
            if (!it.original.isNullOrEmpty())
                fullImage.loadImageUrl(it.original)
        }


    }

}