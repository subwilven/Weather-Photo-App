package com.example.photoweatherapp.ui.history

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.photoweatherapp.R
import com.example.photoweatherapp.ui.save_image.SaveImageFragment
import com.example.photoweatherapp.ui.save_image.SaveImageFragment.Companion.BUNDLE_FILE_PATH
import com.example.photoweatherapp.utils.ImagePicker
import kotlinx.android.synthetic.main.fragment_history_list.*
import org.koin.androidx.viewmodel.ext.android.viewModel
import java.io.File

/**
 * A simple [Fragment] subclass as the default destination in the navigation.
 */
class HistoryListFragment : Fragment() {

    private val mViewModel: HistoryViewModel by viewModel()
    private val mAdapter = ImagesAdapter()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_history_list, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        Toast.makeText(requireContext(), "Please allow permissions manually", Toast.LENGTH_LONG)
            .show()
        setUpObservers()
        initViews()
    }

    private fun setUpObservers() {
        mViewModel.imagesList.observe(viewLifecycleOwner, Observer {
            mAdapter.setData(it)
        })
        mViewModel.onImageAdded.observe(viewLifecycleOwner, Observer {
            mAdapter.notifyItemInserted(it)
        })
    }

    private fun initViews() {
        recyclerView.layoutManager = LinearLayoutManager(requireContext())
        recyclerView.adapter = mAdapter

        fab.setOnClickListener {
            ImagePicker.pickImage(this) { imageFile ->
                navigateToSaveImageFragment(imageFile)
            }
        }
    }

    fun navigateToSaveImageFragment(imageFile: File) {
        mViewModel.getWeatherDataInfo()?.let { weatherDetails ->
            val fragment = SaveImageFragment.newInstance(weatherDetails, imageFile.path)

            activity?.supportFragmentManager?.beginTransaction()?.addToBackStack(null)
                ?.add(R.id.fragment_container_view, fragment)?.commit()

            activity?.supportFragmentManager?.setFragmentResultListener(
                "imageSaved",
                viewLifecycleOwner
            )
            { key, bundle ->
                if (key == "imageSaved") {
                    val filePath = bundle.getString(BUNDLE_FILE_PATH)?:""
                    mViewModel.addImageItem(File(filePath))
                }
            }
        }
    }
}