package com.humayoun.imagesearch.ui.main

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.navigation.Navigation
import androidx.paging.LoadState
import com.humayoun.imagesearch.R
import com.humayoun.imagesearch.data.models.GalleryItem
import com.humayoun.imagesearch.Constants
import com.humayoun.imagesearch.utils.hideKeyboard
import com.humayoun.imagesearch.utils.onSearch
import kotlinx.android.synthetic.main.main_fragment.*
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch


class MainFragment : Fragment(), GalleryAdapter.OnClick {

    private lateinit var viewModel: MainViewModel
    private lateinit var adapter: GalleryAdapter

    // to keep track of search job and cancel previous job
    private var searchJob: Job? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        init()
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View {
        return inflater.inflate(R.layout.main_fragment, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initUI()
        checkForInitialSearch()

    }

    private fun init() {
        viewModel = ViewModelProvider(requireActivity()).get(MainViewModel::class.java)
        adapter = GalleryAdapter(requireContext(), this)
    }

    private fun initUI() {
        btnSearch.setOnClickListener { search() }
        etSearch.onSearch { search() }
        btnRetrySearch.setOnClickListener{ adapter.retry() }
        setAdapter()
    }

    private fun checkForInitialSearch() {
        if(viewModel.currentlySearchingFor == null) {
            search(Constants.INITIAL_QUERY)
        }
    }

    private fun search() {
        val searchQuery = etSearch.text.toString()
        search(searchQuery)
    }

    private fun search(query: String) {
        searchJob?.cancel()
        searchJob = lifecycleScope.launch {
            viewModel.searchForImages(query).collectLatest {
                adapter.submitData(it)
            }
        }

        requireActivity().hideKeyboard()
    }

    override fun onItemClick(item: GalleryItem) {
        viewModel.selectedImage.value = item
        Navigation
            .findNavController(requireActivity(), R.id.nav_host_fragment)
            .navigate(R.id.action_mainFragment_to_imageDetailsFragment)

    }

    private fun setAdapter() {
        rvImages.adapter = adapter.withLoadStateHeaderAndFooter(
            header = GalleryLoadStateAdapter { adapter.retry() },
            footer =  GalleryLoadStateAdapter { adapter.retry() }
        )

        adapter.addLoadStateListener { loadState ->

            rvImages?.let {
                it.visibility =
                    if (loadState.source.refresh is LoadState.NotLoading) View.VISIBLE else View.GONE
            }

            pbSearching?.let {
                it?.visibility =
                    if (loadState.source.refresh is LoadState.Loading) View.VISIBLE else View.GONE
            }

            btnRetrySearch?.let {
                it.visibility =
                    if (loadState.source.refresh is LoadState.Error) View.VISIBLE else View.GONE
            }


            // Toast on any error, regardless of whether it came from RemoteMediator or PagingSource
            val errorState = loadState.source.append as? LoadState.Error
                ?: loadState.source.prepend as? LoadState.Error
                ?: loadState.append as? LoadState.Error
                ?: loadState.prepend as? LoadState.Error

            val errorMessage = getString(R.string.something_went_wrong)

            errorState?.let {
                Toast.makeText(
                    requireContext(),
                    "$errorMessage ${it.error}",
                    Toast.LENGTH_LONG
                ).show()
            }
        }
    }
}
