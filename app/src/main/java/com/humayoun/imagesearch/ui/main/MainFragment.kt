package com.humayoun.imagesearch.ui.main

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders.of
import androidx.lifecycle.lifecycleScope
import androidx.navigation.NavController
import androidx.navigation.Navigation
import androidx.paging.LoadState
import com.humayoun.imagesearch.R
import com.humayoun.imagesearch.data.models.ImageItem
import com.humayoun.imagesearch.utils.hideKeyboard
import com.humayoun.imagesearch.utils.onDone
import kotlinx.android.synthetic.main.main_fragment.*
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

class MainFragment : Fragment(), ImageAdapter.OnClick {

    private lateinit var viewModel: MainViewModel
    private lateinit var navController: NavController
    private lateinit var adapter: ImageAdapter

    // to keep track of search job and cancel previous job
    private var searchJob: Job? = null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View {


        viewModel = of(requireActivity()).get(MainViewModel::class.java)
        navController = Navigation.findNavController(requireActivity(), R.id.nav_host_fragment)


//        viewModel.getImages("flowers")
//        viewModel.imageData.observe(viewLifecycleOwner, Observer {
//            val imageAdapter = ImageAdapter(requireContext(), it, this )
//            rvImages.adapter = imageAdapter
//
//            for(image in it) {
//                Log.i("test", image.toString())
//            }
//        })

        return inflater.inflate(R.layout.main_fragment, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        adapter = ImageAdapter(requireContext(), this)
        rvImages.adapter = adapter.withLoadStateHeaderAndFooter(
            header = GalleryLoadStateAdapter { adapter.retry() },
            footer =  GalleryLoadStateAdapter { adapter.retry() }
        )

        adapter.addLoadStateListener { loadState ->
            // Only show the list if refresh succeeds.
            rvImages.visibility = if(loadState.source.refresh is LoadState.NotLoading) View.VISIBLE else View.GONE
            // Show loading spinner during initial load or refresh.
            pbSearching.visibility = if(loadState.source.refresh is LoadState.Loading) View.VISIBLE else View.GONE
            // Show the retry state if initial load or refresh fails.
            btnRetrySearch.visibility = if(loadState.source.refresh is LoadState.Error) View.VISIBLE else View.GONE

            // Toast on any error, regardless of whether it came from RemoteMediator or PagingSource
            val errorState = loadState.source.append as? LoadState.Error
                ?: loadState.source.prepend as? LoadState.Error
                ?: loadState.append as? LoadState.Error
                ?: loadState.prepend as? LoadState.Error
            errorState?.let {
                Toast.makeText(
                    requireContext(),
                    "\uD83D\uDE28 Wooops ${it.error}",
                    Toast.LENGTH_LONG
                ).show()
            }
        }

        btnRetrySearch.setOnClickListener{ adapter.retry() }
        setupUI()

        searchJ("balls")


        lifecycleScope.launch {
            viewModel.currentSearchResults?.let {
                it.collect {
                    adapter.submitData(it)

                }
            }
        }

    }

    private fun setupUI() {

        btnSearch.setOnClickListener {
            search()
        }

        etSearch.onDone { search() }

    }

    private fun search() {
        val searchQuery = etSearch.text.toString()
        //viewModel.getImages(searchQuery)
        searchJ(searchQuery)
        requireActivity().hideKeyboard()
    }

    private fun searchJ(query: String) {
        searchJob?.cancel()
        searchJob = lifecycleScope.launch {
            viewModel.searchForImages(query).collectLatest {
                adapter.submitData(it)
            }
        }
    }

    override fun onItemClick(item: ImageItem) {
        viewModel.selectedImage.value = item
        navController.navigate(R.id.action_mainFragment_to_imageDetailsFragment)
    }


}
