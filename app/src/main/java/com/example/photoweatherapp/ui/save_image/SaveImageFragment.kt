package com.example.photoweatherapp.ui.save_image

import android.graphics.Bitmap
import android.graphics.Canvas
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.setFragmentResult
import androidx.lifecycle.lifecycleScope
import com.example.photoweatherapp.R
import com.example.photoweatherapp.model.WeatherModel
import com.example.photoweatherapp.utils.saveImageToStorage
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.fragment_save_image.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.io.File

class SaveImageFragment : Fragment() {

    companion object {

        private const val BUNDLE_WEATHER_DATA = "weather-data"
        const val BUNDLE_FILE_PATH = "file-data"
        const val RESULT_IMAGE_SAVED = "imageSaved"

        fun newInstance(weatherModel: WeatherModel, filePath: String): SaveImageFragment {
            val fragment = SaveImageFragment()
            val bundle = Bundle()
            bundle.putParcelable(BUNDLE_WEATHER_DATA, weatherModel)
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
        return inflater.inflate(R.layout.fragment_save_image, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val weatherModel = arguments?.getParcelable<WeatherModel>(BUNDLE_WEATHER_DATA)
        val filePath = arguments?.getString(BUNDLE_FILE_PATH) ?: ""


        bindData(weatherModel, filePath)
        btn_save.setOnClickListener {
            saveImage()
        }
        btn_cancel.setOnClickListener {
            activity?.onBackPressed()
        }
    }

    private fun bindData(weatherModel: WeatherModel?, filePath: String) {
        weatherModel.apply {
            tv_status.text = this?.weather?.first()?.main
            tv_max.text = this?.main?.tempMax.toString()+" /"
            tv_min.text = this?.main?.tempMin.toString()
            tv_pressure.text = getString(R.string.pressure,this?.main?.pressure.toString())
            tv_humidity.text = getString(R.string.humidity,this?.main?.humidity.toString())
            tv_wind_speed.text = getString(R.string.wind_speed,this?.wind?.speed.toString())
            tv_address.text = this?.name
            Picasso.get().load(File(filePath)).into(imageView2)
        }
    }


    fun saveImage() {
        lifecycleScope.launch(Dispatchers.Default) {

            val imageBitmap = Bitmap.createBitmap(
                image_Details.width,
                image_Details.height,
                Bitmap.Config.ARGB_8888
            )
            val canvas = Canvas(imageBitmap)
            image_Details?.draw(canvas)
            saveImageToStorage(requireContext(), imageBitmap) {
                lifecycleScope.launch(Dispatchers.Main){
                    val bundle  = Bundle()
                    bundle.putString(BUNDLE_FILE_PATH,it.path)
                    setFragmentResult(RESULT_IMAGE_SAVED, bundle)
                    activity?.onBackPressed()
                }
            }
        }
    }
}