package ch.iagentur.ringieradsdk.demo.extensions

import android.view.View

fun View.beGone() {
    visibility = View.GONE
}

fun View.beInvisible() {
    visibility = View.INVISIBLE
}

fun View.beVisible() {
    visibility = View.VISIBLE
}

fun View.beVisibleIf(beVisible: Boolean) = if (beVisible) beVisible() else beGone()