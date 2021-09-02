package ch.iagentur.ringieradsdk.demo.ui.viewholders

import androidx.recyclerview.widget.RecyclerView
import ch.iagentur.ringieradsdk.demo.databinding.ItemTypeBinding
import ch.iagentur.ringieradsdk.demo.ui.models.RecyclerItemModel

class TypesViewHolder(private val binding: ItemTypeBinding) :
    RecyclerView.ViewHolder(binding.root) {

    var clickListener: ((Int) -> Unit)? = null

    fun bindView(item: RecyclerItemModel) {
        item.image?.let { binding.itLogo.setImageResource(it) }
        binding.itTitle.text = item.text
        itemView.setOnClickListener {
            clickListener?.invoke(adapterPosition)
        }
    }
}