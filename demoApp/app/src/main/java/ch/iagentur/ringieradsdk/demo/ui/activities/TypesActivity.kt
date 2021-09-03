package ch.iagentur.ringieradsdk.demo.ui.activities

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import androidx.recyclerview.widget.GridLayoutManager
import ch.iagentur.ringieradsdk.demo.DemoAppConfig
import ch.iagentur.ringieradsdk.demo.R
import ch.iagentur.ringieradsdk.demo.databinding.ActivityTypesBinding
import ch.iagentur.ringieradsdk.demo.ui.ViewBindingBaseActivity
import ch.iagentur.ringieradsdk.demo.ui.adapters.TypesAdapter
import ch.iagentur.ringieradsdk.demo.ui.models.RecyclerItemModel

class TypesActivity : ViewBindingBaseActivity<ActivityTypesBinding>() {

    private lateinit var category: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setupToolbar()
        setupRecyclerView()
    }

    private fun setupToolbar() {
        category = intent.getStringExtra(DemoAppConfig.ActivitiesIds.CATEGORY_ID) ?: ""
        title = "Choose a type for $category screen"
    }

    private fun setupRecyclerView() {
        binding?.apply {
            atRecyclerView.layoutManager = GridLayoutManager(this@TypesActivity, 2)
            val itemList = setupItemsList()
            val adapter = TypesAdapter(itemList)
            atRecyclerView.adapter = adapter
            adapter.clickListener = { position ->
                val typeName = itemList[position].text
                val intent = when (position) {
                    0 -> Intent(this@TypesActivity, ScrollViewActivity::class.java)
                    1 -> Intent(this@TypesActivity, RecyclerViewActivity::class.java)
                    else -> Intent(this@TypesActivity, BannerActivity::class.java)
                }
                intent.putExtra(DemoAppConfig.ActivitiesIds.CATEGORY_ID, category)
                intent.putExtra(DemoAppConfig.ActivitiesIds.TYPE_ID, typeName)
                startActivity(intent)
            }
        }
    }

    private fun setupItemsList(): List<RecyclerItemModel> {
        val itemsList = mutableListOf<RecyclerItemModel>()
        itemsList.add(RecyclerItemModel("Scroll View", R.drawable.ic_scrollview))
        itemsList.add(RecyclerItemModel("Recycler View", R.drawable.ic_recyclerview))
        itemsList.add(RecyclerItemModel("Banner View", R.drawable.ic_bannerscreen))
        return itemsList
    }

    override val bindingInflater: (LayoutInflater) -> ActivityTypesBinding
        get() = ActivityTypesBinding::inflate
}