package ch.iagentur.ringieradsdk.demo.ui.activities

import android.os.Bundle
import android.view.LayoutInflater
import androidx.recyclerview.widget.LinearLayoutManager
import ch.iagentur.ringieradsdk.demo.DemoAppConfig
import ch.iagentur.ringieradsdk.demo.DemoAppConfig.PlacementIds.MMR_1
import ch.iagentur.ringieradsdk.demo.DemoAppConfig.PlacementIds.MMR_3
import ch.iagentur.ringieradsdk.demo.DemoAppConfig.PlacementIds.MPA_2
import ch.iagentur.ringieradsdk.demo.DemoAppConfig.PlacementIds.PREROLL_1
import ch.iagentur.ringieradsdk.demo.databinding.ActivityRecyclerViewBinding
import ch.iagentur.ringieradsdk.demo.ui.ViewBindingBaseActivity
import ch.iagentur.ringieradsdk.demo.ui.adapters.RecyclerActivityAdapter
import ch.iagentur.ringieradsdk.demo.ui.models.RecyclerItemModel
import ch.iagentur.ringieradsdk.demo.ui.models.RingierAdItemModel

class RecyclerViewActivity : ViewBindingBaseActivity<ActivityRecyclerViewBinding>() {

    private lateinit var category: String
    private lateinit var type: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        category = intent.getStringExtra(DemoAppConfig.ActivitiesIds.CATEGORY_ID) ?: ""
        type = intent.getStringExtra(DemoAppConfig.ActivitiesIds.TYPE_ID) ?: ""
        title = ("$category with $type")
        binding?.arvRecyclerView?.layoutManager = LinearLayoutManager(this)
        initRecyclerView()
    }

    private fun initRecyclerView() {
        val itemsList = mutableListOf<Any>()
        for (i in 0..15) {
            when (i) {
                1 -> {
                    itemsList.add(RingierAdItemModel(MMR_1, category))
                }
                3 -> {
                    itemsList.add(RingierAdItemModel(MPA_2, category))
                }
                10 -> {
                    itemsList.add(RingierAdItemModel(MMR_3, category))
                }
                15 -> {
                    itemsList.add(RingierAdItemModel(PREROLL_1, category))
                }
                else -> {
                    itemsList.add(RecyclerItemModel(i.toString()))
                }
            }
        }
        val recyclerAdapter = RecyclerActivityAdapter(itemsList)
        binding?.arvRecyclerView?.adapter = recyclerAdapter
    }

    override val bindingInflater: (LayoutInflater) -> ActivityRecyclerViewBinding
        get() = ActivityRecyclerViewBinding::inflate
}