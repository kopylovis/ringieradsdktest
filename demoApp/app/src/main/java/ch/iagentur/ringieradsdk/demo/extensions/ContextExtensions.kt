package ch.iagentur.ringieradsdk.demo.extensions

import android.app.AlertDialog
import android.content.Context
import android.graphics.Color
import android.text.InputType
import android.util.TypedValue
import android.view.*
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import android.widget.FrameLayout
import ch.iagentur.ringieradsdk.demo.DemoAppConfig.EditTextDialog.XANDR_TITLE
import ch.iagentur.ringieradsdk.demo.DemoAppConfig.Pins.DEFAULT_PIN
import ch.iagentur.ringieradsdk.demo.DemoAppConfig.Pins.NOT_PASSED
import ch.iagentur.ringieradsdk.demo.DemoAppConfig.Pins.PASSED
import com.jakewharton.processphoenix.ProcessPhoenix

import android.widget.ProgressBar
import android.widget.LinearLayout

import android.widget.TextView


fun Context.showAlertDialog(
    title: String,
    message: String?,
    buttonText: String,
    listener: (() -> Unit)
) {
    showAlertDialog(title, message ?: "loading...", buttonText, "Cancel", listener)
}

fun Context.showAlertDialog(
    title: String,
    message: String,
    buttonText: String,
    buttonNegativeText: String,
    listener: (() -> Unit)
) {
    AlertDialog.Builder(this).apply {
        setTitle(title)
        setMessage(message)
        setPositiveButton(buttonText) { _, _ ->
            listener.invoke()
        }
        setNegativeButton(buttonNegativeText) { _, _ -> }
        create()
        show()
    }
}

fun Context.showProgressDialog(message: String) {
    val layoutParam = LinearLayout.LayoutParams(
        LinearLayout.LayoutParams.WRAP_CONTENT,
        LinearLayout.LayoutParams.WRAP_CONTENT
    ).apply {
        gravity = Gravity.CENTER
    }
    val linearLayout = LinearLayout(this).apply {
        orientation = LinearLayout.HORIZONTAL
        setPadding(30, 30, 30, 30)
        gravity = Gravity.CENTER
        layoutParams = layoutParam
    }
    val progressBar = ProgressBar(this).apply {
        isIndeterminate = true
        layoutParams = layoutParam
        setPadding(0, 0, 0, 0)
    }
    val text = TextView(this).apply {
        text = message
        setTextColor(Color.BLACK)
        textSize = 18f
        layoutParams = layoutParam
    }
    linearLayout.addView(progressBar)
    linearLayout.addView(text)
    val builder = AlertDialog.Builder(this)
    builder.setView(linearLayout)
    val dialog = builder.create().apply {
        setCancelable(false)
        show()
    }
    val window = dialog.window
    if (window != null) {
        val layoutParams = WindowManager.LayoutParams().apply {
            copyFrom(window.attributes)
            width = LinearLayout.LayoutParams.WRAP_CONTENT
            height = LinearLayout.LayoutParams.WRAP_CONTENT
        }
        window.attributes = layoutParams
    }
}

fun Context.showRestartAlertDialog() {
    AlertDialog.Builder(this).apply {
        setTitle("Warning")
        setMessage("You need to restart the application to apply the settings. Restart now?")
        setPositiveButton("Restart") { _, _ ->
            showProgressDialog("Restarting")
            ProcessPhoenix.triggerRebirth(this@showRestartAlertDialog)
        }
        setNegativeButton("Cancel") { _, _ -> }
        create()
        show()
    }
}

fun Context.showEditTextDialog(title: String, listener: (String) -> Unit) {
    val editText = EditText(this).apply {
        hint = "Please enter $title"
        if (title == XANDR_TITLE) {
            inputType = InputType.TYPE_CLASS_NUMBER
        }
    }
    val alert: androidx.appcompat.app.AlertDialog.Builder = androidx.appcompat.app.AlertDialog.Builder(this)
    alert.setTitle(title)
    val layout = FrameLayout(this)
    layout.setPadding(dpsToIntPixels(20f), dpsToIntPixels(4f), dpsToIntPixels(20f), 0)
    layout.addView(editText)
    alert.setView(layout)
    alert.setNegativeButton("Cancel") { _, _ ->
    }
    alert.setPositiveButton("OK") { _, _ ->
        val value = editText.text.toString()
        listener.invoke(value)
    }
    alert.show()
    showKeyboard(editText, true)
}

fun Context.showPinDialog(pin: String, listener: (String) -> Unit) {
    val editText = EditText(this).apply {
        hint = "Please Enter PIN"
        inputType = InputType.TYPE_CLASS_NUMBER or InputType.TYPE_NUMBER_VARIATION_PASSWORD
    }
    val alert: androidx.appcompat.app.AlertDialog.Builder = androidx.appcompat.app.AlertDialog.Builder(this)
    alert.setTitle("PIN")
    val defaultPin = DEFAULT_PIN
    if (defaultPin.isNotEmpty()) {
        editText.setText(defaultPin)
    }
    val layout = FrameLayout(this)
    layout.setPadding(dpsToIntPixels(20f), dpsToIntPixels(4f), dpsToIntPixels(20f), 0)
    layout.addView(editText)
    alert.setView(layout)
    alert.setNegativeButton("Cancel") { _, _ ->
    }
    alert.setPositiveButton("OK") { _, _ ->
        val value = editText.text.toString()
        if (value == pin) {
            listener.invoke(PASSED)
        } else {
            listener.invoke(NOT_PASSED)
        }
    }
    alert.show()
    showKeyboard(editText, true)
}

fun Context.pixelsToFloatDps(float: Float): Float {
    return float / getDensity(this)
}

fun Context.pixelsToIntDps(float: Float): Int {
    return (this.pixelsToFloatDps(float) + 0.5f).toInt()
}

fun Context.dpsToFloatPixels(float: Float): Float {
    return float * getDensity(this)
}

fun Context.dpsToIntPixels(float: Float): Int {
    return (this.dpsToFloatPixels(float) + 0.5f).toInt()
}

fun Context.screenWidthAsIntDps(): Int {
    return pixelsToIntDps(this.resources.displayMetrics.widthPixels.toFloat())
}

fun Context.screenHeightAsIntDps(): Int {
    return pixelsToIntDps(this.resources.displayMetrics.heightPixels.toFloat())
}

fun Context.asFloatPixels(dps: Float): Float {
    val displayMetrics = this.resources.displayMetrics
    return TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dps, displayMetrics)
}

fun Context.asIntPixels(dps: Float): Int {
    return (asFloatPixels(dps) + 0.5f).toInt()
}


private fun getDensity(context: Context): Float {
    return context.resources.displayMetrics.density
}

fun Context.showKeyboard(view: View, requestFocus: Boolean = true) {
    doActionOnInputManager {
        view.postDelayed({
            if (requestFocus) {
                view.requestFocus()
            }
            it.showSoftInput(view, 0)
        }, 100) // 100 ms delay is usually enough to get a focus for View
    }
}

private fun Context.doActionOnInputManager(action: (InputMethodManager) -> Unit) {
    val inputManager = getSystemService(Context.INPUT_METHOD_SERVICE) as? InputMethodManager
    inputManager?.apply {
        action(this)
    }
}