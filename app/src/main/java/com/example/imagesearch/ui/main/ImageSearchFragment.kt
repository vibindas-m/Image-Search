package com.example.imagesearch.ui.main

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.findNavController
import com.example.imagesearch.R
import kotlinx.android.synthetic.main.fragment_image_search.*
import org.koin.androidx.viewmodel.ext.android.sharedViewModel

class ImageSearchFragment : Fragment() {

    companion object {
        fun newInstance() = ImageSearchFragment()
    }

    private val viewModel: MainViewModel by sharedViewModel()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return inflater.inflate(R.layout.fragment_image_search, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        nextBtn.setOnClickListener {
            it?.findNavController()?.navigate(R.id.action_imageSearchFragment_to_imageDetailFragment)
        }
    }

}