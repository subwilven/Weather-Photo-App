package com.example.photoweatherapp.ui.full_screen_image

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.example.photoweatherapp.R
import com.example.photoweatherapp.ui.save_image.SaveImageFragment.Companion.BUNDLE_FILE_PATH
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.fragment_full_screen.*
import java.io.File

class FullScreenImageFragment :Fragment() {


    companion object {

        fun newInstance(filePath: String): FullScreenImageFragment {
            val fragment = FullScreenImageFragment()
            val bundle = Bundle()
            bundle.putString(BUNDLE_FILE_PATH, filePath)
            fragment.arguments = bundle
            return fragment
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        activity?.window?.addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
        (activity as AppCompatActivity).supportActionBar?.hide()
        return inflater.inflate(R.layout.fragment_full_screen, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val filePath = arguments?.getString(BUNDLE_FILE_PATH)?:""
        Picasso.get().load(File(filePath)).into(fullScreenImageView)
    }

    override fun onDestroyView() {
        (activity as AppCompatActivity).supportActionBar?.show()
        activity?.window?.clearFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
        super.onDestroyView()
    }
}