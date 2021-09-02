package ch.iagentur.ringieradsdk.demo.ui.activities

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.widget.TextView
import ch.iagentur.ringieradsdk.demo.DemoAppConfig.EditTextDialog.ADMOB_TITLE
import ch.iagentur.ringieradsdk.demo.DemoAppConfig.EditTextDialog.ADUNIT_ID
import ch.iagentur.ringieradsdk.demo.DemoAppConfig.EditTextDialog.MEMBER_ID
import ch.iagentur.ringieradsdk.demo.DemoAppConfig.EditTextDialog.NOT_SET_YET
import ch.iagentur.ringieradsdk.demo.DemoAppConfig.EditTextDialog.XANDR_TITLE
import ch.iagentur.ringieradsdk.demo.databinding.ActivityHiddenSettingsBinding
import ch.iagentur.ringieradsdk.demo.extensions.showEditTextDialog
import ch.iagentur.ringieradsdk.demo.extensions.showRestartAlertDialog
import ch.iagentur.ringieradsdk.demo.providers.RingierAdPreferences
import ch.iagentur.ringieradsdk.demo.ui.ViewBindingBaseActivity

class HiddenSettingsActivity : ViewBindingBaseActivity<ActivityHiddenSettingsBinding>(),
    View.OnClickListener {

    private lateinit var ringierAdPreferences: RingierAdPreferences

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        title = "Advanced Settings"
        ringierAdPreferences = RingierAdPreferences(this)
        setupClickListeners()
        setupTextFiled()
    }

    private fun setupTextFiled() {
        binding?.apply {
            ahsXandrMemberId.text =
                if (ringierAdPreferences.memberIdPref.isNotEmpty()) ringierAdPreferences.memberIdPref else NOT_SET_YET
            ahsAdmobAdUnitId.text =
                if (ringierAdPreferences.adUnitIdPref.isNotEmpty()) ringierAdPreferences.adUnitIdPref else NOT_SET_YET
        }
    }

    private fun setupClickListeners() {
        binding?.apply {
            ahsSaveButton.setOnClickListener(this@HiddenSettingsActivity)
            ahsXandrButton.setOnClickListener(this@HiddenSettingsActivity)
            ahsAdmobButton.setOnClickListener(this@HiddenSettingsActivity)
            ahsXandrButtonRemove.setOnClickListener(this@HiddenSettingsActivity)
            ahsAdmobButtonRemove.setOnClickListener(this@HiddenSettingsActivity)
        }
    }

    private fun onSaveButtonPressed() {
        binding?.apply {
            val memberId = ahsXandrMemberId.text.toString()
            val adUnitID = ahsAdmobAdUnitId.text.toString()
            if (memberId == NOT_SET_YET) {
                ringierAdPreferences.removeString(MEMBER_ID)
            } else {
                ringierAdPreferences.memberIdPref = memberId
            }
            if (adUnitID == NOT_SET_YET) {
                ringierAdPreferences.removeString(ADUNIT_ID)
            } else {
                ringierAdPreferences.adUnitIdPref = adUnitID
            }
        }
        showRestartAlertDialog()
    }

    private fun onRemoveButtonPressed(textView: TextView) {
        binding?.apply {
            textView.text = NOT_SET_YET
        }
    }

    override val bindingInflater: (LayoutInflater) -> ActivityHiddenSettingsBinding
        get() = ActivityHiddenSettingsBinding::inflate

    override fun onClick(v: View?) {
        binding?.apply {
            when (v?.id) {
                ahsXandrButton.id -> this@HiddenSettingsActivity.showEditTextDialog(XANDR_TITLE) {
                    ahsXandrMemberId.text = it
                }
                ahsAdmobButton.id -> this@HiddenSettingsActivity.showEditTextDialog(ADMOB_TITLE) {
                    ahsAdmobAdUnitId.text = it
                }
                ahsSaveButton.id -> onSaveButtonPressed()
                ahsXandrButtonRemove.id -> onRemoveButtonPressed(ahsXandrMemberId)
                ahsAdmobButtonRemove.id -> onRemoveButtonPressed(ahsAdmobAdUnitId)
            }
        }
    }

}