package com.example.photoweatherapp.utils

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.Fragment
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleObserver
import androidx.lifecycle.OnLifecycleEvent
import com.afollestad.materialdialogs.LayoutMode
import com.afollestad.materialdialogs.MaterialDialog
import com.afollestad.materialdialogs.bottomsheets.BottomSheet
import com.example.photoweatherapp.R
import java.io.File
import java.util.*


object ImagePicker : LifecycleObserver {


    var onPicked: ((File) -> Unit)? = {}
    var onFailed: (() -> Unit)? = {}
    var onGalleryPermissionGranted: () -> Unit = {}
    var onCameraPermissionGranted: () -> Unit = {}

    private const val DIALOG_TITLE = "DIALOG_TITLE"
    private const val MESSAGE = "MESSAGE"

    fun pickImage(
        fragment: Fragment,
        onImageFailed: (() -> Unit)? = null,
        onImagePicked: (File) -> Unit
    ) {
        fragment.viewLifecycleOwner.lifecycle.addObserver(this)
        onPicked = onImagePicked
        onFailed = onImageFailed


        val imageFragment = ImagePickerFragment()
        val bundle = Bundle()
        bundle.putString(DIALOG_TITLE, fragment.getString(R.string.choose_image_picker))
        bundle.putString(MESSAGE, fragment.getString(R.string.choose_image_picker))
        imageFragment.arguments = bundle

        launchImagePickerFragment(fragment, imageFragment)
    }

    private fun launchImagePickerFragment(
        currentFragment: Fragment,
        pickerFragment: ImagePickerFragment
    ) {
        val previousFragment =
            currentFragment.childFragmentManager.findFragmentByTag(ImagePickerFragment.fragmentTag)
        if (previousFragment != null) {
            currentFragment.childFragmentManager.beginTransaction()
                .remove(previousFragment)
                .commitNow()
        }
        currentFragment.childFragmentManager.beginTransaction()
            .add(pickerFragment, ImagePickerFragment.fragmentTag)
            .commitNow()
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_DESTROY)
    fun disconnectListener() {
        onPicked = null
        onFailed = null
    }

    class ImagePickerFragment() : Fragment() {

        companion object {
            val fragmentTag = "ImagePickerFragment"
        }

        var currentPhotoPath = ""

        override fun onCreate(savedInstanceState: Bundle?) {
            super.onCreate(savedInstanceState)
            val title = arguments?.getString(DIALOG_TITLE) ?: ""
            val message = arguments?.getString(MESSAGE) ?: ""
            showImagePickerDialog(title, message)
            retainInstance = true
        }

        private fun showImagePickerDialog(title: String, message: String) {
            onGalleryPermissionGranted = {
                val galleryIntent = Intent(Intent.ACTION_PICK)
                galleryIntent.type = "image/*"
                val bundle = Bundle()
                galleryIntent.putExtras(bundle)
                galleryResultLauncher.launch(galleryIntent)
            }
            onCameraPermissionGranted = {
                val photoFile: File = createImageFile(requireContext())
                currentPhotoPath =photoFile.absolutePath
                val photoURI: Uri = getUriFromFile(requireContext(), photoFile)
                val cameraIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
                cameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI)
                cameraResultLauncher.launch(cameraIntent)
            }

            val galleryButton: ((MaterialDialog) -> Unit) = {
                it.dismiss()
                onGalleryPermissionGranted.invoke()
            }
            val cameraButton: ((MaterialDialog) -> Unit) = {
                it.dismiss()
                onCameraPermissionGranted.invoke()
            }

            val dialog = MaterialDialog(requireContext(), BottomSheet((LayoutMode.WRAP_CONTENT)))
            dialog.title(text = title)
                .message(text = message)
                .positiveButton(R.string.gallery)
                {
                    galleryButton.invoke(it)
                }
                .negativeButton(R.string.camera)
                { cameraButton.invoke(it) }
                .create()
            dialog.show()
        }


        private val cameraResultLauncher =
            registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {
                when (it.resultCode) {
                    Activity.RESULT_OK -> {
                        onPicked?.invoke(File(currentPhotoPath))
                    }
                    else -> {
                        onFailed?.invoke()
                    }
                }
                removeCurrentFragment()
            }

        private fun removeCurrentFragment() {
            parentFragment?.childFragmentManager
                ?.beginTransaction()
                ?.remove(this@ImagePickerFragment)
                ?.commitNow()
        }

        private val galleryResultLauncher =
            registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {
                when (it.resultCode) {
                    Activity.RESULT_OK -> {
                        val uri = it.data?.data ?: Uri.EMPTY
                        when {
                            uri != Uri.EMPTY -> {
                                val filePath = uri.getFilePathFromURI(requireContext())
                                if (filePath == null) {
                                    onFailed?.invoke()
                                } else {
                                    onPicked?.invoke(File(filePath))
                                }
                            }
                            else -> {
                                onFailed?.invoke()
                            }
                        }
                    }
                    else -> {
                        onFailed?.invoke()
                    }
                }
                removeCurrentFragment()
            }

    }


}

