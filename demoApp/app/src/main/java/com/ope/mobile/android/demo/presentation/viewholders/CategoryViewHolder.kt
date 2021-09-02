package com.ope.mobile.android.demo.presentation.viewholders

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.ope.mobile.android.demo.R
import com.ope.mobile.android.demo.model.Category

class CategoryViewHolder(private val context: Context, private val parent: ViewGroup) :
    RecyclerView.ViewHolder(LayoutInflater.from(context).inflate(R.layout.row_category, parent, false)) {

    private val titleTextView = itemView.findViewById<TextView>(R.id.rcTitleTextView)

    fun bindView(category: Category) {
        titleTextView.text = category.name
    }

}