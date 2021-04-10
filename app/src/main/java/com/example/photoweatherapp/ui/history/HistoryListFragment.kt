package com.example.photoweatherapp.ui.history

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.photoweatherapp.R
import com.example.photoweatherapp.ui.full_screen_image.FullScreenImageFragment
import com.example.photoweatherapp.ui.save_image.SaveImageFragment
import com.example.photoweatherapp.ui.save_image.SaveImageFragment.Companion.BUNDLE_FILE_PATH
import com.example.photoweatherapp.ui.save_image.SaveImageFragment.Companion.RESULT_IMAGE_SAVED
import com.example.photoweatherapp.utils.ImagePicker
import com.example.photoweatherapp.utils.LocationUtils
import com.example.photoweatherapp.utils.PermissionsManager
import com.example.photoweatherapp.utils.shareImage
import kotlinx.android.synthetic.main.fragment_history_list.*
import org.koin.androidx.viewmodel.ext.android.viewModel
import java.io.File

/**
 * A simple [Fragment] subclass as the default destination in the navigation.
 */
class HistoryListFragment : Fragment() {

    private val mViewModel: HistoryViewModel by viewModel()
    private lateinit var mAdapter : ImagesAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_history_list, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        mAdapter = ImagesAdapter(mViewModel)
        requestLocationPermission()
        requestCameraAndStoragePermission()
        setUpObservers()
        initViews()
    }

    private fun requestLocationPermission() {
        LocationUtils.instance?.getUserLocationSingle(this,onFailed = {
            Toast.makeText(requireContext(),"Failed to get ur location",Toast.LENGTH_LONG)
        }){
            mViewModel.fetchWeatherData(it)
        }
    }

    private fun setUpObservers() {
        mViewModel.imagesList.observe(viewLifecycleOwner, {
            mAdapter.setData(it)
        })
        mViewModel.onImageAdded.observe(viewLifecycleOwner, {
            mAdapter.notifyItemInserted(it)
        })

        mViewModel.onImageClicked.observe(viewLifecycleOwner,{
            shareImage(it,requireContext())
        })

        mViewModel.navigateToFullScreen.observe(viewLifecycleOwner,{
            navigateToFullScreen(it)
        })

        mViewModel.showToast.observe(viewLifecycleOwner,{
            Toast.makeText(requireContext(),it,Toast.LENGTH_LONG).show()
        })
    }

    private fun initViews() {
        recyclerView.layoutManager = GridLayoutManager(requireContext(),2)
        recyclerView.adapter = mAdapter

        fab.setOnClickListener {
            showPickImageDialog()
        }
    }

    fun requestCameraAndStoragePermission(onGranted: (() -> Unit)? = null) {
        PermissionsManager.requestPermission(
            this,
            PermissionsManager.CAMERA,
            PermissionsManager.READ_EXTERNAL_STORAGE,
            PermissionsManager.WRITE_EXTERNAL_STORAGE
        ) {
            onGranted?.invoke()
        }
    }

    fun showPickImageDialog() {
        requestCameraAndStoragePermission {
            ImagePicker.pickImage(this) { imageFile ->
                navigateToSaveImageFragment(imageFile)
            }
        }
    }

    fun navigateToFullScreen(imageFile: File){
        val fragment = FullScreenImageFragment.newInstance(imageFile.path)
        activity?.supportFragmentManager?.beginTransaction()?.addToBackStack(null)
            ?.replace(R.id.fragment_container_view, fragment)?.commit()
    }


    fun navigateToSaveImageFragment(imageFile: File) {
        mViewModel.getWeatherDataInfo()?.let { weatherDetails ->

            val fragment = SaveImageFragment.newInstance(weatherDetails, imageFile.path)

            activity?.supportFragmentManager?.beginTransaction()?.addToBackStack(null)
                ?.add(R.id.fragment_container_view, fragment)?.commit()

            activity?.supportFragmentManager?.setFragmentResultListener(
                RESULT_IMAGE_SAVED,
                viewLifecycleOwner
            ) { _, bundle ->
                val filePath = bundle.getString(BUNDLE_FILE_PATH) ?: ""
                mViewModel.addImageItem(File(filePath))
            }
        }
    }
}