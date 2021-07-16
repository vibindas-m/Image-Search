package com.example.imagesearch.ui.main

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.SearchView
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.navigation.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.imagesearch.R
import com.example.imagesearch.adapter.ImageSearchListAdapter
import com.example.imagesearch.domain.model.Event
import com.example.imagesearch.domain.model.ImageSearchModel
import com.example.imagesearch.domain.model.ImageSearchResultModel
import kotlinx.android.synthetic.main.fragment_image_search.*
import org.koin.androidx.viewmodel.ext.android.sharedViewModel
import com.example.imagesearch.domain.model.Result
import com.example.imagesearch.domain.room.ImageSearchRoomData

class ImageSearchFragment : Fragment() {

    private val viewModel: MainViewModel by sharedViewModel()
    private var searchKeyword: String = ""
    lateinit var linearLayoutManager: LinearLayoutManager
    private val imageSearchListAdapter = ImageSearchListAdapter()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return inflater.inflate(R.layout.fragment_image_search, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        bindUI()
    }

    private fun bindUI() {
        viewModel.imageSearchEvent.observe(viewLifecycleOwner, imageSearchObserver)
        viewModel.saveImageSearchDataStorageEvent.observe(
            viewLifecycleOwner,
            saveImageSearchObserver
        )
        viewModel.getImageSearchFromStorageEvent.observe(
            viewLifecycleOwner,
            getImageSearchFromStorageObserver
        )
        viewModel.imageSearchList.observe(viewLifecycleOwner, Observer {
            it?.let {
                loadImageSearchList(it)
            }
        })
        enablePagination()
        searchBtn.setOnClickListener {
            if (searchKeyword.isNotEmpty()) {
                searchImageOnStorage()
            }
        }
        search?.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(p0: String?): Boolean {
                searchKeyword = p0 ?: ""
                searchImageOnStorage()
                return false
            }

            override fun onQueryTextChange(p0: String?): Boolean {
                searchKeyword = p0 ?: ""
                return false
            }
        })
    }

    private fun searchImageOnStorage() {
        viewModel.resetFetchedList()
        viewModel.getImageSearchFromStorageTrigger.postValue(Event(searchKeyword))
    }

    private fun getImageSearchFromServer() {
        viewModel.imageSearchEventTrigger.postValue(Event(searchKeyword))
    }

    private fun loadImageSearchList(list: List<ImageSearchModel>) {
        errorText.visibility = View.GONE
        if (recyclerView.adapter == null) {
            linearLayoutManager = LinearLayoutManager(activity)
            imageSearchListAdapter.setOnItemClickListener(object :
                ImageSearchListAdapter.OnItemClickListener {
                override fun onClick(view: View, data: ImageSearchModel) {
                    viewModel.updateSelectedImage(data)
                    view.findNavController()
                        .navigate(R.id.action_imageSearchFragment_to_imageDetailFragment)
                }
            })
            recyclerView.layoutManager = LinearLayoutManager(activity)
            recyclerView.adapter = imageSearchListAdapter
        }
        imageSearchListAdapter.setData(list)
    }

    private val getImageSearchFromStorageObserver = Observer<Result<ImageSearchRoomData>> {
        if (it is Result.Loading) {
            progressBar.visibility = View.VISIBLE
        } else {
            if (it is Result.Success) {
                progressBar.visibility = View.GONE
                viewModel.updateImageSearchFromStorage(it.data)
            }
            if (it is Result.Failure) {
                getImageSearchFromServer()
            }
        }
    }

    private val saveImageSearchObserver = Observer<Result<Boolean>> {
        if (it is Result.Loading) {
            progressBar.visibility = View.VISIBLE
        } else {
            progressBar.visibility = View.GONE
        }
    }

    private val imageSearchObserver = Observer<Result<ImageSearchResultModel>> {
        if (it is Result.Loading) {
            progressBar.visibility = View.VISIBLE
        } else {
            progressBar.visibility = View.GONE
            if (it is Result.Success) {
                viewModel.getAndUpdateImageSearchRoomData(it.data, searchKeyword)?.let { imgSearchRoomDate ->
                    viewModel.saveImageSearchDataStorageTrigger.postValue(Event(imgSearchRoomDate))
                }
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

    private fun enablePagination() {
        recyclerView.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
                super.onScrollStateChanged(recyclerView, newState)
                if (newState == RecyclerView.SCROLL_STATE_IDLE && viewModel.imageSearchList.value?.isNullOrEmpty() == false) {
                    val item = linearLayoutManager.findLastCompletelyVisibleItemPosition()
                    if (item == linearLayoutManager.itemCount - 1 && viewModel.getHasNext()) {
                        getImageSearchFromServer()
                    }
                }
            }
        })
    }
}