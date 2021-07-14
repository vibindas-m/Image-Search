package com.example.imagesearch.ui.main

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.navigation.findNavController
import com.example.imagesearch.R
import com.example.imagesearch.domain.model.Event
import com.example.imagesearch.domain.model.ImageSearchResultModel
import kotlinx.android.synthetic.main.fragment_image_search.*
import org.koin.androidx.viewmodel.ext.android.sharedViewModel
import com.example.imagesearch.domain.model.Result

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
        bindUI()
    }

    private fun bindUI() {
        viewModel.imageSearchEventTrigger.postValue(Event(Unit))
        viewModel.imageSearchEvent.observe(viewLifecycleOwner, imageSearchObserver)
    }

    private val imageSearchObserver = Observer<Result<ImageSearchResultModel>> {
        if (it is Result.Loading) {
            progressBar.visibility = View.VISIBLE
        } else {
            progressBar.visibility = View.GONE
            if (it is Result.Success) {
                viewModel.updateImageSearchResult(it.data)
            }
            if (it is Result.Failure) {
                showError(it.errorMsg)
            }
        }
    }

    private fun showError(errorMsg: String) {
        errorText.visibility = View.VISIBLE
        errorText.text = errorMsg
    }

}