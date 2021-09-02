package com.ope.mobile.android.demo.model

import java.io.Serializable

data class Article(val id: String,
                   val title: String,
                   val category: String): Serializable {
}