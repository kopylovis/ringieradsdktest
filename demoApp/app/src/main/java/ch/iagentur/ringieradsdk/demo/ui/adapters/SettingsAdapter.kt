package ch.iagentur.ringieradsdk.demo.ui.adapters

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import ch.iagentur.ringieradsdk.demo.databinding.ItemSettingsRecyclerviewBinding
import ch.iagentur.ringieradsdk.demo.ui.viewholders.SettingsViewHolder
import ch.iagentur.ringieradsdk.external.models.RingierAdUrlModel

class SettingsAdapter(private val items: List<RingierAdUrlModel>) :
    RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private var selectedPosition = -1

    @SuppressLint("NotifyDataSetChanged")
    private var clickListener = { position: Int, urlModel: RingierAdUrlModel ->
        switchCallback?.invoke(position, urlModel)
        selectedPosition = position
        notifyDataSetChanged()
    }

    var removeCallback: ((position: Int) -> Unit)? = null
    var switchCallback: ((position: Int, urlModel: RingierAdUrlModel) -> Unit)? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return SettingsViewHolder(ItemSettingsRecyclerviewBinding.inflate(LayoutInflater.from(parent.context), parent, false))
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val isSelected = selectedPosition == position
        if (holder is SettingsViewHolder) {
            holder.bindView(items[position])
            holder.switchCallback = clickListener
            holder.removeCallback = removeCallback
        }
    }

    override fun getItemCount(): Int {
        return items.size
    }
}