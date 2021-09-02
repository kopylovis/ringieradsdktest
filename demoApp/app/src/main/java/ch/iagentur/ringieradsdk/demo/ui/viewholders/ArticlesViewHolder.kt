package ch.iagentur.ringieradsdk.demo.ui.viewholders

import androidx.recyclerview.widget.RecyclerView
import ch.iagentur.ringieradsdk.demo.databinding.RowArticleBinding

class ArticlesViewHolder(private val binding: RowArticleBinding) :
    RecyclerView.ViewHolder(binding.root) {

    var clickListener: ((Int) -> Unit)? = null

    fun bindView(category: String) {
        binding.rcNameTextView.text = category
        itemView.setOnClickListener {
            clickListener?.invoke(adapterPosition)
        }
    }

}