package com.ope.mobile.android.demo.presentation.ui

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem
import androidx.appcompat.widget.Toolbar
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.ope.mobile.android.demo.AppConfig
import com.ope.mobile.android.demo.model.Article
import com.ope.mobile.android.demo.model.Category
import com.ope.mobile.android.demo.presentation.adapters.ArticleRvAdapter
import com.microsoft.appcenter.AppCenter
import com.microsoft.appcenter.analytics.Analytics
import com.microsoft.appcenter.crashes.Crashes
import com.microsoft.appcenter.distribute.Distribute
import com.microsoft.appcenter.distribute.UpdateTrack
import com.ope.mobile.android.OnePlusX
import com.ope.mobile.android.demo.BuildConfig
import com.ope.mobile.android.demo.R
import java.util.*

class MainActivity : AppCompatActivity() {

    private var isAfterClosingDetails = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val toolbar = findViewById<Toolbar>(R.id.amToolbar)
        toolbar.title = "Articles"
        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setDisplayShowHomeEnabled(true)
        setupArticles()
        setupAppCenter()
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == android.R.id.home) {
            finish()
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onResume() {
        super.onResume()
        OnePlusX.trackPageView(eventType = "articles", customPayload = mapOf(
            "is_after_closing_details" to isAfterClosingDetails
        ))
    }

    private fun setupAppCenter() {
        if (BuildConfig.DEBUG) {
            Distribute.setEnabledForDebuggableBuild(true)
            Distribute.setUpdateTrack(UpdateTrack.PRIVATE)
            AppCenter.start(application, "b54cc216-9989-47ac-b0af-57cc0e6aa3c9",
                Analytics::class.java,
                Crashes::class.java,
                Distribute::class.java)
        }
    }

    private fun setupArticles() {
        val recyclerView = findViewById<RecyclerView>(R.id.amRecyclerView)
        recyclerView.layoutManager = LinearLayoutManager(this)
        val articles = createArticles()
        val adapter = ArticleRvAdapter(createArticles())
        adapter.openDetailsCallback = { article ->
            isAfterClosingDetails = true
            val intent = Intent(this, DetailsActivity::class.java)
            intent.putExtra(AppConfig.IntentExtras.ARTICLE, article)
            val index = articles.indexOf(article)
            intent.putExtra(AppConfig.IntentExtras.ARTICLE_INDEX, index)
            startActivity(intent)
        }
        recyclerView.adapter = adapter
    }

    private fun createArticles(): List<Any> {
        val items = mutableListOf<Any>()
        var articlesInCategory = 1
        for (i in 0..3) {
            val categoryTitle = "Category $i"
            items.add(Category(categoryTitle))

            for (j in 0..articlesInCategory) {
                val title = "Title $j"
                val id = UUID.randomUUID().toString()
                val article = Article(id, title, categoryTitle)
                items.add(article)
            }
            articlesInCategory++
        }
        return items
    }

}