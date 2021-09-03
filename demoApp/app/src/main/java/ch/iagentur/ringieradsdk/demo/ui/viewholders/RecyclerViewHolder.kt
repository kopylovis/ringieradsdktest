package ch.iagentur.ringieradsdk.demo.ui.viewholders

import androidx.recyclerview.widget.RecyclerView
import ch.iagentur.ringieradsdk.demo.databinding.ItemRecyclerviewBinding

class RecyclerViewHolder(private val binding: ItemRecyclerviewBinding) :
    RecyclerView.ViewHolder(binding.root) {

    var clickListener: (() -> Unit)? = null

    fun bindView() {
        itemView.setOnClickListener {
            clickListener?.invoke()
        }
    }
}