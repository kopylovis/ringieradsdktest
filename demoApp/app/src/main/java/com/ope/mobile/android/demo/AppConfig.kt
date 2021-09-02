package com.ope.mobile.android.demo

object AppConfig {

    const val REMOTE_CONFIG_URL = "https://xupload.streamboatserver.ch/adhocs/1plusX/config.json"

    object IntentExtras {
        const val ARTICLE = "ARTICLE"
        const val ARTICLE_INDEX = "ARTICLE_INDEX"
    }

    object PreferenceKeys {
        const val PREFERENCE_KEY_MAX_QUEUE_SIZE = "maxQueueSize"
        const val PREFERENCE_KEY_DISPATCH_TIME_SECONDS = "dispatchTimeSeconds"
    }

    object PreferenceDefaultValues {
        const val MAX_QUEUE_SIZE = 2L
        const val DISPATCH_TIMER_SECONDS = 5L
        const val LOGIN_SPACE_ID = "test"
    }

}