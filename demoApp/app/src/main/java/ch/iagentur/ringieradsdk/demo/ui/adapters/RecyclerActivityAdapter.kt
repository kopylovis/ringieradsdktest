package ch.iagentur.ringieradsdk.demo.ui.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import ch.iagentur.ringieradsdk.demo.databinding.ItemRecyclerviewBinding
import ch.iagentur.ringieradsdk.demo.databinding.RowRingieradBinding
import ch.iagentur.ringieradsdk.demo.ui.models.RingierAdItemModel
import ch.iagentur.ringieradsdk.demo.ui.viewholders.RecyclerViewHolder
import ch.iagentur.ringieradsdk.demo.ui.viewholders.RingierAdViewHolder

class RecyclerActivityAdapter(private val items: List<Any>) :
    RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    var clickListener: (() -> Unit)? = null

    companion object {
        private const val TYPE_BANNER_AD = 0
        private const val TYPE_ITEM = 1
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return when (viewType) {
            TYPE_BANNER_AD -> RingierAdViewHolder(
                RowRingieradBinding.inflate(LayoutInflater.from(parent.context), parent, false)
            )
            else -> RecyclerViewHolder(
                ItemRecyclerviewBinding.inflate(
                    LayoutInflater.from(parent.context),
                    parent,
                    false
                )
            )
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when (getItemViewType(position)) {
            TYPE_BANNER_AD -> {
                if (holder is RingierAdViewHolder) {
                    holder.bindView(items[position] as RingierAdItemModel)
                }
            }
            else -> if (holder is RecyclerViewHolder) {
                holder.clickListener = clickListener
                holder.bindView()
            }
        }
    }

    override fun getItemCount(): Int {
        return items.size
    }

    override fun getItemViewType(position: Int): Int {
        return when (items[position]) {
            is RingierAdItemModel -> {
                TYPE_BANNER_AD
            }
            else -> TYPE_ITEM
        }
    }

}