package ch.iagentur.ringieradsdk.demo.ui.activities

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import ch.iagentur.ringieradsdk.demo.DemoAppConfig
import ch.iagentur.ringieradsdk.demo.DemoAppConfig.PlacementIds.MMR_1
import ch.iagentur.ringieradsdk.demo.DemoAppConfig.PlacementIds.MMR_5
import ch.iagentur.ringieradsdk.demo.DemoAppConfig.PlacementIds.MPA_2
import ch.iagentur.ringieradsdk.demo.databinding.ActivityBannerBinding
import ch.iagentur.ringieradsdk.demo.extensions.*
import ch.iagentur.ringieradsdk.demo.ui.ViewBindingBaseActivity
import ch.iagentur.ringieradsdk.external.ad.RingierAd
import ch.iagentur.ringieradsdk.external.error.RingierAdError
import ch.iagentur.ringieradsdk.external.models.RingierAdSize

class BannerActivity : ViewBindingBaseActivity<ActivityBannerBinding>() {

    private lateinit var category: String
    private lateinit var type: String
    private val mapOfSizes = mutableMapOf<Int, List<String>>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setupLoadingAds()
        setupToolbar()
        setupPlaceholderIds()
        setupButtonInfo()
        setupAdsLoadListeners()
        setupAdsSizesListeners()
    }

    private fun setupAdsSizesListeners() {
        binding?.apply {
            abBannerOne.adSizeListener = object : RingierAd.AdSizeListener {
                override fun adSize(size: RingierAdSize) {
                }

                override fun adSizes(sizes: List<String>) {
                    if (mapOfSizes[0] != null) {
                        mapOfSizes.remove(0)
                    }
                    mapOfSizes[0] = sizes
                    abBannerOneInfo.visibility = View.VISIBLE
                }
            }
            abBannerTwo.adSizeListener = object : RingierAd.AdSizeListener {
                override fun adSize(size: RingierAdSize) {

                }
                override fun adSizes(sizes: List<String>) {
                    if (mapOfSizes[1] != null) {
                        mapOfSizes.remove(1)
                    }
                    mapOfSizes[1] = sizes
                    abBannerTwoInfo.visibility = View.VISIBLE
                }
            }
            abBannerThree.adSizeListener = object : RingierAd.AdSizeListener {
                override fun adSize(size: RingierAdSize) {

                }
                override fun adSizes(sizes: List<String>) {
                    if (mapOfSizes[2] != null) {
                        mapOfSizes.remove(2)
                    }
                    mapOfSizes[2] = sizes
                    abBannerThreeInfo.visibility = View.VISIBLE
                }
            }
        }
    }

    private fun setupAdsLoadListeners() {
        binding?.apply {
            abBannerOne.adLoadListener = object : RingierAd.AdLoadListener {
                override fun adLoaded() {
                    abLoadAdOneButton.applyRefreshState()
                }

                override fun adFailed(error: RingierAdError) {
                    abLoadAdOneButton.applyErrorState()
                }
            }
            abBannerTwo.adLoadListener = object : RingierAd.AdLoadListener {
                override fun adLoaded() {
                    abLoadAdTwoButton.applyRefreshState()
                }

                override fun adFailed(error: RingierAdError) {
                    abLoadAdTwoButton.applyErrorState()
                }
            }
            abBannerThree.adLoadListener = object : RingierAd.AdLoadListener {
                override fun adLoaded() {
                    abLoadAdThreeButton.applyRefreshState()
                }

                override fun adFailed(error: RingierAdError) {
                    abLoadAdThreeButton.applyErrorState()
                }
            }
        }
    }


    private fun setupLoadingAds() {
        binding?.apply {
            abLoadAdOneButton.setOnClickListener {
                abBannerOne.loadAd(MMR_1, category)
                abLoadAdOneButton.applyLoadingState()
            }
            abLoadAdTwoButton.setOnClickListener {
                abBannerTwo.loadAd(MPA_2, category)
                abLoadAdTwoButton.applyLoadingState()
            }
            abLoadAdThreeButton.setOnClickListener {
                abBannerThree.loadAd(MMR_5, category)
                abLoadAdThreeButton.applyLoadingState()
            }
        }
    }

    private fun setupPlaceholderIds() {
        binding?.apply {
            abPlaceholderIdOne.text = (abPlaceholderIdOne.text.toString() + MMR_1)
            abPlaceholderIdTwo.text = (abPlaceholderIdTwo.text.toString() + MPA_2)
            abPlaceholderIdThree.text = (abPlaceholderIdThree.text.toString() + MMR_5)
        }
    }

    private fun setupToolbar() {
        category = intent.getStringExtra(DemoAppConfig.ActivitiesIds.CATEGORY_ID) ?: ""
        type = intent.getStringExtra(DemoAppConfig.ActivitiesIds.TYPE_ID) ?: ""
        title = ("$category with $type")
    }

    private fun setupButtonInfo() {
        binding?.apply {
            abBannerOneInfo.setOnClickListener {
                showAlertDialog("$MMR_1 settings:", mapOfSizes[0]?.toStringWithNewLine(), "OK") {}
            }
            abBannerTwoInfo.setOnClickListener {
                showAlertDialog("$MPA_2 settings:", mapOfSizes[1]?.toStringWithNewLine(), "OK") {}
            }
            abBannerThreeInfo.setOnClickListener {
                showAlertDialog("$MMR_5 settings:", mapOfSizes[2]?.toStringWithNewLine(), "OK") {}
            }
        }
    }

    override fun onResume() {
        super.onResume()
        binding?.apply {
            abBannerOne.resume()
            abBannerTwo.resume()
            abBannerThree.resume()
        }
    }

    override fun onPause() {
        super.onPause()
        binding?.apply {
            abBannerOne.pause()
            abBannerTwo.pause()
            abBannerThree.pause()
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        binding?.apply {
            abBannerOne.destroy()
            abBannerTwo.destroy()
            abBannerThree.destroy()
        }
    }

    override val bindingInflater: (LayoutInflater) -> ActivityBannerBinding
        get() = ActivityBannerBinding::inflate

}