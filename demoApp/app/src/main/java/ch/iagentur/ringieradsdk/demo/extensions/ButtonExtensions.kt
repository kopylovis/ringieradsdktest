package ch.iagentur.ringieradsdk.demo.extensions

import android.widget.Button
import androidx.core.content.ContextCompat
import ch.iagentur.ringieradsdk.demo.R

fun Button.applyLoadingState() {
    isEnabled = false
    text = ("Loading...")
    backgroundTintList = ContextCompat.getColorStateList(context, R.color.gray)
}

fun Button.applyRefreshState() {
    isEnabled = true
    text = ("Refresh Ad")
    backgroundTintList = ContextCompat.getColorStateList(context, R.color.design_default_color_primary)
}

fun Button.applyErrorState() {
    isEnabled = true
    text = ("Error")
    backgroundTintList = ContextCompat.getColorStateList(context, R.color.light_red)
}