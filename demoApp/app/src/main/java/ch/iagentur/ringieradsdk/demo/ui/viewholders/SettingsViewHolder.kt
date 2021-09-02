package ch.iagentur.ringieradsdk.demo.ui.viewholders

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import ch.iagentur.ringieradsdk.demo.databinding.ItemSettingsRecyclerviewBinding
import ch.iagentur.ringieradsdk.external.models.RingierAdUrlModel

class SettingsViewHolder(private val binding: ItemSettingsRecyclerviewBinding) :
    RecyclerView.ViewHolder(binding.root) {

    var switchCallback: ((position: Int, url: RingierAdUrlModel) -> Unit)? = null
    var removeCallback: ((position: Int) -> Unit)? = null

    fun bindView(urlModel: RingierAdUrlModel) {
        binding.apply {
            if (!urlModel.isRemovable) {
                isrRemoveImageView.visibility = View.GONE
            }
            isrSwitchCompat.isChecked = urlModel.isActivated
            isrUrlTextView.text = urlModel.url
            isrTitleTextView.text = urlModel.title
            isrSwitchCompat.setOnClickListener {
                if (isrSwitchCompat.isChecked) {
                    switchCallback?.invoke(
                        adapterPosition,
                        RingierAdUrlModel(urlModel.title, urlModel.url, true, urlModel.isRemovable)
                    )
                } else {
                    switchCallback?.invoke(
                        adapterPosition,
                        RingierAdUrlModel(urlModel.title, urlModel.url, false, urlModel.isRemovable)
                    )
                }
            }
            isrRemoveImageView.setOnClickListener {
                removeCallback?.invoke(adapterPosition)
            }
        }
    }
}