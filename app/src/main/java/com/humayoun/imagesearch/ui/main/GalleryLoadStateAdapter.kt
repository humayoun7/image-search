package com.humayoun.imagesearch.ui.main

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ProgressBar
import android.widget.TextView
import androidx.paging.LoadState
import androidx.paging.LoadStateAdapter
import androidx.recyclerview.widget.RecyclerView
import com.humayoun.imagesearch.R


class GalleryLoadStateAdapter(
    private val retry: () -> Unit
): LoadStateAdapter<GalleryLoadStateAdapter.ViewHolder>() {

    class ViewHolder(val view: View): RecyclerView.ViewHolder(view) {
        val txtError: TextView = view.findViewById(R.id.txtError)
        val btnRetry: Button = view.findViewById(R.id.btnRetry)
        val pbLoading: ProgressBar = view.findViewById(R.id.pbLoading)
    }

    override fun onCreateViewHolder(parent: ViewGroup, loadState: LoadState): ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val view = inflater.inflate(R.layout.gallery_load_state_footer, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, loadState: LoadState) {

        with(holder) {
            if (loadState is LoadState.Error) {
                txtError.text = loadState.error.localizedMessage
            }

            pbLoading.visibility = if (loadState is  LoadState.Loading ) View.VISIBLE else View.GONE
            btnRetry.visibility = if (loadState !is LoadState.Loading ) View.VISIBLE else View.GONE
            txtError.visibility = if (loadState !is  LoadState.Loading ) View.VISIBLE else View.GONE

            btnRetry.setOnClickListener{ retry() }
        }

    }

}