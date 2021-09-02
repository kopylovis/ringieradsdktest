package com.ope.mobile.android.demo.presentation.ui

import android.os.Bundle
import android.view.MenuItem
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.ope.mobile.android.demo.AppConfig

import com.ope.mobile.android.demo.model.Article
import com.ope.mobile.android.OnePlusX
import com.ope.mobile.android.demo.R
import com.ope.mobile.android.public.OpeKey

class DetailsActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_details)
        val titleTextView = findViewById<TextView>(R.id.adTitleTextView)
        val subTitleTextView = findViewById<TextView>(R.id.adSubTitleTextView)
        val article = intent.extras?.get(AppConfig.IntentExtras.ARTICLE) as? Article
        val articleIndex = intent.extras?.get(AppConfig.IntentExtras.ARTICLE_INDEX) as? Int ?: 0
        if (article != null) {
            titleTextView.text = article.title
            subTitleTextView.text = article.id
        }

        val opePayload = mapOf(OpeKey.ITEM_URI to OnePlusX.buildIdentifier("demo-article", "$articleIndex"))
        findViewById<Button>(R.id.adCustomEventButton).setOnClickListener {
            val customPayload = createArticlePayload("custom_data", article)
            OnePlusX.trackEvent("article", opePayload = opePayload, customPayload = customPayload)
        }

        val customPayload = createArticlePayload("article", article)
        OnePlusX.trackPageView(eventType = "article_detail", opePayload = opePayload, customPayload = customPayload, sendImmediately = false)

        setSupportActionBar(findViewById(R.id.adToolbar))
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setDisplayShowHomeEnabled(true)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == android.R.id.home) {
            finish()
        }
        return super.onOptionsItemSelected(item)
    }

    private fun createArticlePayload(key: String, article: Article?): Map<String, Any> {
        val customPayload = mutableMapOf<String, Any>()
        val articlePayload = mutableMapOf<String, Any?>()
        articlePayload["category"] = article?.category
        articlePayload["id"] = article?.id
        articlePayload["name"] = article?.title
        customPayload[key] = articlePayload
        return customPayload
    }

}