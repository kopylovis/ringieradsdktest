package ch.iagentur.ringieradsdk.demo.ui.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import ch.iagentur.ringieradsdk.demo.databinding.RowArticleBinding
import ch.iagentur.ringieradsdk.demo.ui.viewholders.ArticlesViewHolder

class ArticlesAdapter(private val items: List<String>) :
    RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    var clickListener: ((Int) -> Unit)? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return ArticlesViewHolder(
            RowArticleBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        if (holder is ArticlesViewHolder) {
            holder.clickListener = clickListener
            holder.bindView(items[position])
        }
    }

    override fun getItemCount(): Int {
        return items.size
    }

}