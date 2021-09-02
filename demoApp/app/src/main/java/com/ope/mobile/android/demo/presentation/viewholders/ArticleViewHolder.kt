package com.ope.mobile.android.demo.presentation.viewholders

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.ope.mobile.android.demo.R
import com.ope.mobile.android.demo.model.Article

class ArticleViewHolder(private val context: Context, private val parent: ViewGroup) :
    RecyclerView.ViewHolder(LayoutInflater.from(context).inflate(R.layout.row_article, parent, false)) {

    private val titleTextView = itemView.findViewById<TextView>(R.id.raTitleTextView)
    private val subTitleTextView = itemView.findViewById<TextView>(R.id.raSubTitleTextView)

    var openDetailsCallback: ((Article) -> Unit)? = null

    fun bindView(article: Article) {
        titleTextView.text = article.title
        subTitleTextView.text = article.id
        itemView.setOnClickListener {
            openDetailsCallback?.invoke(article)
        }
    }

}