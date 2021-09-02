package ch.iagentur.ringieradsdk.demo.ui.activities

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import androidx.recyclerview.widget.LinearLayoutManager
import ch.iagentur.ringieradsdk.demo.DemoAppConfig.ActivitiesIds.CATEGORY_ID
import ch.iagentur.ringieradsdk.demo.DemoAppConfig.ScreensIds.AUTO
import ch.iagentur.ringieradsdk.demo.DemoAppConfig.ScreensIds.BLICKTV
import ch.iagentur.ringieradsdk.demo.DemoAppConfig.ScreensIds.DIGITAL
import ch.iagentur.ringieradsdk.demo.DemoAppConfig.ScreensIds.HOME
import ch.iagentur.ringieradsdk.demo.DemoAppConfig.ScreensIds.LEBEN
import ch.iagentur.ringieradsdk.demo.DemoAppConfig.ScreensIds.MEINUNG
import ch.iagentur.ringieradsdk.demo.DemoAppConfig.ScreensIds.NEWS
import ch.iagentur.ringieradsdk.demo.DemoAppConfig.ScreensIds.PEOPLE
import ch.iagentur.ringieradsdk.demo.DemoAppConfig.ScreensIds.POLITIK
import ch.iagentur.ringieradsdk.demo.DemoAppConfig.ScreensIds.ROS
import ch.iagentur.ringieradsdk.demo.DemoAppConfig.ScreensIds.SPORT
import ch.iagentur.ringieradsdk.demo.DemoAppConfig.ScreensIds.VIDEO
import ch.iagentur.ringieradsdk.demo.DemoAppConfig.ScreensIds.WIRTSCHAFT
import ch.iagentur.ringieradsdk.demo.databinding.ActivityArticlesBinding
import ch.iagentur.ringieradsdk.demo.ui.ViewBindingBaseActivity
import ch.iagentur.ringieradsdk.demo.ui.adapters.ArticlesAdapter

class ArticlesActivity : ViewBindingBaseActivity<ActivityArticlesBinding>() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        title = "Articles"
        binding?.acRecyclerView?.layoutManager = LinearLayoutManager(this)
        initCategoriesType()
    }

    private fun initCategoriesType() {
        val categoriesList = listOf(
            HOME,
            NEWS,
            SPORT,
            MEINUNG,
            POLITIK,
            WIRTSCHAFT,
            PEOPLE,
            LEBEN,
            DIGITAL,
            AUTO,
            VIDEO,
            ROS,
            BLICKTV
        )
        val adapter = ArticlesAdapter(categoriesList)
        adapter.clickListener = { position ->
            val category = categoriesList[position]
            val intent = Intent(this, TypesActivity::class.java)
            intent.putExtra(CATEGORY_ID, category)
            startActivity(intent)
        }
        binding?.acRecyclerView?.adapter = adapter
    }

    override val bindingInflater: (LayoutInflater) -> ActivityArticlesBinding
        get() = ActivityArticlesBinding::inflate

}