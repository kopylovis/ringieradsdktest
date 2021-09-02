package com.ope.mobile.android.demo.presentation.adapters

import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.ope.mobile.android.demo.model.Article
import com.ope.mobile.android.demo.model.Category
import com.ope.mobile.android.demo.presentation.viewholders.ArticleViewHolder
import com.ope.mobile.android.demo.presentation.viewholders.CategoryViewHolder

class ArticleRvAdapter(private val items: List<Any>): RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    companion object {
        private const val CATEGORY = 1
        private const val ARTICLE = 2
    }

    var openDetailsCallback: ((Article) -> Unit)? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val context = parent.context
        if (viewType == CATEGORY) {
            return CategoryViewHolder(context, parent)
        }
        return ArticleViewHolder(context, parent)
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        if (holder is ArticleViewHolder) {
            holder.openDetailsCallback = openDetailsCallback
            holder.bindView(items[holder.adapterPosition] as Article)
        } else if (holder is CategoryViewHolder) {
            holder.bindView(items[holder.adapterPosition] as Category)
        }
    }

    override fun getItemViewType(position: Int): Int {
        val item = items[position]
        if (item is Category) {
            return CATEGORY
        }
        return ARTICLE
    }

    override fun getItemCount(): Int {
        return items.size
    }
}