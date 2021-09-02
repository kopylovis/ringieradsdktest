package ch.iagentur.ringieradsdk.demo.ui.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import ch.iagentur.ringieradsdk.demo.databinding.ItemTypeBinding
import ch.iagentur.ringieradsdk.demo.ui.models.RecyclerItemModel
import ch.iagentur.ringieradsdk.demo.ui.viewholders.TypesViewHolder

class TypesAdapter(private val items: List<RecyclerItemModel>) :
    RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    var clickListener: ((Int) -> Unit)? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return TypesViewHolder(
            ItemTypeBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        if (holder is TypesViewHolder) {
            holder.clickListener = clickListener
            holder.bindView(items[position])
        }
    }

    override fun getItemCount(): Int {
        return items.size
    }

}