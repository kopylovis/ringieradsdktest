package ch.iagentur.ringieradsdk.demo.extensions

import androidx.appcompat.widget.SwitchCompat

fun SwitchCompat.beActivatedIf(isActivated: Boolean) {
    isChecked = isActivated
}