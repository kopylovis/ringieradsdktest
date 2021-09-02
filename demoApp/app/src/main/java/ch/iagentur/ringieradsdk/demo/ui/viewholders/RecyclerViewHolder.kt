package ch.iagentur.ringieradsdk.demo.ui.viewholders

import androidx.recyclerview.widget.RecyclerView
import ch.iagentur.ringieradsdk.demo.databinding.ItemRecyclerviewBinding
import ch.iagentur.ringieradsdk.demo.databinding.RowRingieradBinding

class RecyclerViewHolder(private val binding: ItemRecyclerviewBinding) :
    RecyclerView.ViewHolder(binding.root) {

    var clickListener: (() -> Unit)? = null

    fun bindView() {
        itemView.setOnClickListener {
            clickListener?.invoke()
        }
    }
}