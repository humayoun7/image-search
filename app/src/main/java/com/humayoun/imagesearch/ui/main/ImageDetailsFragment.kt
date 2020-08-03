package com.humayoun.imagesearch.ui.main

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.bumptech.glide.Glide
import com.humayoun.imagesearch.R
import kotlinx.android.synthetic.main.image_details_fragment.*

class ImageDetailsFragment : Fragment() {

    private lateinit var viewModel: MainViewModel


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        init()
        goFullScreen()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.image_details_fragment, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initUI()
    }

    fun init() {
        viewModel = ViewModelProvider(requireActivity()).get(MainViewModel::class.java)
    }

    fun initUI() {
        viewModel.selectedImage.observe(viewLifecycleOwner, Observer {
            Glide.with(requireActivity())
                .load(it.link)
                .into(photoView)
        })

        // showing image description on tap
        photoView.setOnPhotoTapListener { _, _, _ ->
            viewModel.selectedImage.value?.description?.let {
                Toast.makeText(requireContext(), it, Toast.LENGTH_SHORT).show()
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()

        exitFullScreen()
    }

    private fun goFullScreen() {
        requireActivity().window.clearFlags(WindowManager.LayoutParams.FLAG_FORCE_NOT_FULLSCREEN);
        requireActivity().window.addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
    }

    private fun exitFullScreen() {
        requireActivity().window.clearFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
        requireActivity().window.addFlags(WindowManager.LayoutParams.FLAG_FORCE_NOT_FULLSCREEN);
    }
}
