package com.example.photoweatherapp.ui.history

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.photoweatherapp.R
import com.squareup.picasso.Picasso
import kotlinx.android.extensions.LayoutContainer
import kotlinx.android.synthetic.main.item_history.*
import java.io.File

class ImagesAdapter(val historyViewModel: HistoryViewModel) : RecyclerView.Adapter<ImagesAdapter.ImagesViewHolder>() {

    var list: MutableList<File> = mutableListOf()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ImagesViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_history, parent, false)
        return ImagesViewHolder(view)

    }

    override fun onBindViewHolder(holder: ImagesViewHolder, position: Int) {
        holder.bind(list[position])

    }

    fun addItem(file: File) {
        this.list.add(file)
    }

    override fun getItemCount(): Int {
        return list.count()
    }

    fun setData(list: MutableList<File>) {
        this.list = list
    }

    inner class ImagesViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView),
        LayoutContainer {

        init {
            itemView.setOnClickListener {
                historyViewModel.shareImage(list[adapterPosition])
            }
        }

        fun bind(file: File) {
            Picasso.get().load(file).into(imageView)
        }

        override val containerView: View
            get() = itemView
    }
}