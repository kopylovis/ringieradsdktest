package ch.iagentur.ringieradsdk.demo.ui.viewholders

import androidx.recyclerview.widget.RecyclerView
import ch.iagentur.ringieradsdk.demo.DemoAppConfig.TAG
import ch.iagentur.ringieradsdk.demo.databinding.RowRingieradBinding
import ch.iagentur.ringieradsdk.demo.extensions.beInvisible
import ch.iagentur.ringieradsdk.demo.extensions.beVisible
import ch.iagentur.ringieradsdk.demo.extensions.showAlertDialog
import ch.iagentur.ringieradsdk.demo.ui.models.RingierAdItemModel
import ch.iagentur.ringieradsdk.external.ad.RingierAd
import ch.iagentur.ringieradsdk.external.error.RingierAdError
import ch.iagentur.ringieradsdk.external.models.RingierAdSize
import timber.log.Timber

class RingierAdViewHolder(private val binding: RowRingieradBinding) :
    RecyclerView.ViewHolder(binding.root) {

    private lateinit var size: RingierAdSize
    private lateinit var sizes: List<String>

    var clickListener: (() -> Unit)? = null

    fun bindView(ringierAdItemModel: RingierAdItemModel) {
        binding.apply {
            //If the banner has an advertisement, it won't send a "Load Ad" request.
            if (rrBannerAdView.childCount == 0) {
                rrBannerAdView.loadAd(ringierAdItemModel.placeholderIds, ringierAdItemModel.screenIds)
            }
            itemView.setOnClickListener {
                clickListener?.invoke()
            }
            rrButtonInfoImageView.setOnClickListener {
                itemView.context.showAlertDialog(
                    "${ringierAdItemModel.placeholderIds} settings: ",
                    size.toString(),
                    "OK"
                ) {}
            }
            rrBannerAdView.adLoadListener = object : RingierAd.AdLoadListener {
                override fun adLoaded() {
                    rrButtonInfoImageView.beVisible()
                }

                override fun adFailed(error: RingierAdError) {
                    rrButtonInfoImageView.beInvisible()
                    Timber.tag(TAG)
                        .d("${ringierAdItemModel.placeholderIds}, error = ${error.message}")
                    if (error.throwable != null) {
                        Timber.tag(TAG)
                            .e(error.throwable, "${ringierAdItemModel.placeholderIds} adFailed")
                    }
                }

            }
            rrBannerAdView.adSizeListener = object : RingierAd.AdSizeListener {
                override fun adSize(size: RingierAdSize) {
                    this@RingierAdViewHolder.size = size
                }

                override fun adSizes(sizes: List<String>) {
                    this@RingierAdViewHolder.sizes = sizes
                }
            }
        }
    }
}
