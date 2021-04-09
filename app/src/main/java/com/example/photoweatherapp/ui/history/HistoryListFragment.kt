package com.example.photoweatherapp.ui.history

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.photoweatherapp.R
import com.example.photoweatherapp.utils.ImagePicker
import kotlinx.android.synthetic.main.fragment_history_list.*
import org.koin.androidx.viewmodel.ext.android.viewModel

/**
 * A simple [Fragment] subclass as the default destination in the navigation.
 */
class HistoryListFragment : Fragment() {

    private val mViewModel: HistoryViewModel by viewModel()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_history_list, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        mViewModel.text()
        initViews()
    }


    private fun initViews() {

        fab.setOnClickListener {
            ImagePicker.pickImage(this) {

            }
        }
    }
}