package com.humayoun.imagesearch.ui.main

import androidx.lifecycle.ViewModelProviders
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import android.widget.Toast
import androidx.lifecycle.Observer
import androidx.navigation.NavController
import androidx.navigation.Navigation
import com.bumptech.glide.Glide

import com.humayoun.imagesearch.R
import kotlinx.android.synthetic.main.image_details_fragment.*

class ImageDetailsFragment : Fragment() {

    private lateinit var viewModel: MainViewModel
    private lateinit var navController: NavController

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        init()
        goFullScreen()

        return inflater.inflate(R.layout.image_details_fragment, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initUI()
    }

    fun init() {
        viewModel = ViewModelProviders.of(requireActivity()).get(MainViewModel::class.java)
        navController = Navigation.findNavController(requireActivity(), R.id.nav_host_fragment)

        viewModel.selectedImage.observe(viewLifecycleOwner, Observer {
            Log.i("imageDetails", it.toString())
            Glide.with(requireActivity())
                .load(it.link)
                .into(photoView)
        })
    }

    fun initUI() {
        photoView.setOnPhotoTapListener { _, _, _ ->
            Toast.makeText(requireContext(), viewModel.selectedImage.value?.name, Toast.LENGTH_SHORT).show()
        }

    }

    override fun onDestroy() {
        super.onDestroy()
        undoFullScreen()
    }

    private fun goFullScreen() {
        requireActivity().getWindow().clearFlags(WindowManager.LayoutParams.FLAG_FORCE_NOT_FULLSCREEN);
        requireActivity().getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
    }


    private fun undoFullScreen() {
        requireActivity().getWindow().clearFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
        requireActivity().getWindow().addFlags(WindowManager.LayoutParams.FLAG_FORCE_NOT_FULLSCREEN);
    }
}
