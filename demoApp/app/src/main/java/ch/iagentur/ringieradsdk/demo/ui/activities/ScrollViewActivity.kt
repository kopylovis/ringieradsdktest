package ch.iagentur.ringieradsdk.demo.ui.activities

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import ch.iagentur.ringieradsdk.demo.DemoAppConfig
import ch.iagentur.ringieradsdk.demo.DemoAppConfig.PlacementIds.MMR_1
import ch.iagentur.ringieradsdk.demo.DemoAppConfig.PlacementIds.MPA_2
import ch.iagentur.ringieradsdk.demo.databinding.ActivityScrollviewBinding
import ch.iagentur.ringieradsdk.demo.extensions.beGone
import ch.iagentur.ringieradsdk.demo.extensions.beVisible
import ch.iagentur.ringieradsdk.demo.extensions.showAlertDialog
import ch.iagentur.ringieradsdk.demo.extensions.toStringWithNewLine
import ch.iagentur.ringieradsdk.demo.ui.ViewBindingBaseActivity
import ch.iagentur.ringieradsdk.external.ad.RingierAd
import ch.iagentur.ringieradsdk.external.error.RingierAdError
import ch.iagentur.ringieradsdk.external.models.RingierAdSize
import timber.log.Timber

class ScrollViewActivity : ViewBindingBaseActivity<ActivityScrollviewBinding>(),
    View.OnClickListener {

    private val mapOfSizes = mutableMapOf<Int, List<String>>()
    private lateinit var category: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setupToolbar()
        binding?.apply {
            asvOneButtonInfo.setOnClickListener(this@ScrollViewActivity)
            asvTwoButtonInfo.setOnClickListener(this@ScrollViewActivity)
            asvOneAdView.loadAd(MMR_1, category)
            asvTwoAdView.loadAd(MPA_2, category)
        }
        setupAdsLoadListeners()
        setupAdsSizesListeners()
    }

    private fun setupToolbar() {
        category = intent.getStringExtra(DemoAppConfig.ActivitiesIds.CATEGORY_ID) ?: ""
        val type = intent.getStringExtra(DemoAppConfig.ActivitiesIds.TYPE_ID) ?: ""
        title = ("$category with $type")
    }

    private fun setupAdsSizesListeners() {
        binding?.apply {
            asvOneAdView.adSizeListener = object : RingierAd.AdSizeListener {
                override fun adSize(size: RingierAdSize) {

                }

                override fun adSizes(sizes: List<String>) {
                    if (mapOfSizes[0] != null) {
                        mapOfSizes.remove(0)
                    }
                    mapOfSizes[0] = sizes
                    asvOneButtonInfo.beVisible()
                }
            }
            asvTwoAdView.adSizeListener = object : RingierAd.AdSizeListener {
                override fun adSize(size: RingierAdSize) {

                }

                override fun adSizes(sizes: List<String>) {
                    if (mapOfSizes[1] != null) {
                        mapOfSizes.remove(1)
                    }
                    mapOfSizes[1] = sizes
                    asvTwoButtonInfo.beVisible()
                }
            }
        }
    }

    private fun setupAdsLoadListeners() {
        binding?.apply {
            asvOneAdView.adLoadListener = object : RingierAd.AdLoadListener {
                override fun adLoaded() {
                    Timber.tag(DemoAppConfig.TAG).d("admobBannerView adLoaded")
                    asvOneButtonInfo.beVisible()
                }

                override fun adFailed(error: RingierAdError) {
                    asvOneButtonInfo.beGone()
                    Timber.tag(DemoAppConfig.TAG)
                        .d("admobBannerView adFailed, error = ${error.message}")
                    if (error.throwable != null) {
                        Timber.tag(DemoAppConfig.TAG).e(error.throwable, "admobBannerView adFailed")
                    }
                }
            }
            asvTwoAdView.adLoadListener = object : RingierAd.AdLoadListener {
                override fun adLoaded() {
                    Timber.tag(DemoAppConfig.TAG).d("xandrBannerView adLoaded")
                    asvTwoButtonInfo.beVisible()
                }

                override fun adFailed(error: RingierAdError) {
                    asvTwoButtonInfo.beGone()
                    Timber.tag(DemoAppConfig.TAG)
                        .d("xandrBannerView adFailed, error = ${error.message}")
                    if (error.throwable != null) {
                        Timber.tag(DemoAppConfig.TAG).e(error.throwable, "xandrBannerView adFailed")
                    }
                }
            }
        }
    }

    override fun onClick(v: View?) {
        binding?.apply {
            when (v?.id) {
                asvOneButtonInfo.id ->
                    this@ScrollViewActivity
                        .showAlertDialog("$MMR_1 settings:", mapOfSizes[0]?.toStringWithNewLine(), "OK") {}
                asvTwoButtonInfo.id ->
                    this@ScrollViewActivity
                        .showAlertDialog("$MPA_2 settings:", mapOfSizes[1]?.toStringWithNewLine(), "OK") {}
            }
        }
    }

    override fun onResume() {
        super.onResume()
        binding?.apply {
            asvOneAdView.resume()
            asvTwoAdView.resume()
        }
    }

    override fun onPause() {
        super.onPause()
        binding?.apply {
            asvOneAdView.pause()
            asvTwoAdView.pause()
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        binding?.apply {
            asvOneAdView.destroy()
            asvTwoAdView.destroy()
        }
    }

    override val bindingInflater: (LayoutInflater) -> ActivityScrollviewBinding
        get() = ActivityScrollviewBinding::inflate

}