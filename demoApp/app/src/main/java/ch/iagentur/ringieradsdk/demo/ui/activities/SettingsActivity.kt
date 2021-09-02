package ch.iagentur.ringieradsdk.demo.ui.activities

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.widget.*
import androidx.recyclerview.widget.LinearLayoutManager
import ch.iagentur.ringieradsdk.demo.DemoAppConfig.EditTextDialog.KEYWORD_KEY_DEFAULT
import ch.iagentur.ringieradsdk.demo.DemoAppConfig.EditTextDialog.KEYWORD_KEY_PREF
import ch.iagentur.ringieradsdk.demo.DemoAppConfig.EditTextDialog.KEYWORD_VALUE_DEFAULT
import ch.iagentur.ringieradsdk.demo.DemoAppConfig.EditTextDialog.KEYWORD_TITLE_KEY
import ch.iagentur.ringieradsdk.demo.DemoAppConfig.EditTextDialog.KEYWORD_TITLE_VALUE
import ch.iagentur.ringieradsdk.demo.DemoAppConfig.EditTextDialog.KEYWORD_VALUE_PREF
import ch.iagentur.ringieradsdk.demo.DemoAppConfig.EditTextDialog.NOT_SET_YET
import ch.iagentur.ringieradsdk.demo.databinding.ActivitySettingsBinding
import ch.iagentur.ringieradsdk.demo.extensions.*
import ch.iagentur.ringieradsdk.demo.ui.adapters.SettingsAdapter
import ch.iagentur.ringieradsdk.demo.providers.RingierAdPreferences
import ch.iagentur.ringieradsdk.demo.ui.ViewBindingBaseActivity
import ch.iagentur.ringieradsdk.external.models.RingierAdUrlModel

class SettingsActivity : ViewBindingBaseActivity<ActivitySettingsBinding>(), View.OnClickListener {

    private lateinit var ringierAdPreferences: RingierAdPreferences
    private lateinit var urlsList: MutableList<RingierAdUrlModel>
    private lateinit var recyclerAdapter: SettingsAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        ringierAdPreferences = RingierAdPreferences(this)
        title = "Settings"
        binding?.apply {
            asetKeywordKey.text =
                if (ringierAdPreferences.keywordKeyPref.isNotEmpty()) ringierAdPreferences.keywordKeyPref else NOT_SET_YET
            asetKeywordValue.text =
                if (ringierAdPreferences.keywordValuePref.isNotEmpty()) ringierAdPreferences.keywordValuePref else NOT_SET_YET
            asetSwitchCompat.beActivatedIf(ringierAdPreferences.isTestModeEnabled)
        }
        setupClickListeners()
        setupRecyclerView()
        setupSwitchCompatTestMode()
    }

    private fun setupClickListeners() {
        binding?.apply {
            asetButtonRemoveAll.setOnClickListener(this@SettingsActivity)
            asetButtonSave.setOnClickListener(this@SettingsActivity)
            asetAdvancedSettings.setOnClickListener(this@SettingsActivity)
            asetKeywordKey.setOnClickListener(this@SettingsActivity)
            asetKeywordValue.setOnClickListener(this@SettingsActivity)
            asetAdmforceButtonDefault.setOnClickListener(this@SettingsActivity)
            asetKeywordRemove.setOnClickListener(this@SettingsActivity)
        }
    }

    private fun setupSwitchCompatTestMode() {
        binding?.apply {
            asetSwitchCompat.setOnCheckedChangeListener { _, isChecked ->
                ringierAdPreferences.isTestModeEnabled = isChecked
                showRestartAlertDialog()
            }
        }
    }

    private fun setupRecyclerView() {
        binding?.asetRecyclerView?.layoutManager = LinearLayoutManager(this@SettingsActivity)
        urlsList = ringierAdPreferences.getUrlsList().toMutableList()
        recyclerAdapter = SettingsAdapter(urlsList)
        if (urlsList.isEmpty()) {
            initDefaultConfigs()
        }
        binding?.asetRecyclerView?.adapter = recyclerAdapter
        recyclerAdapter.switchCallback = { position, urlModel ->
            urlsList.removeAt(position)
            urlsList.add(position, urlModel)
            recyclerAdapter.notifyItemChanged(position)
            ringierAdPreferences.saveUrlsList(urlsList)
            showRestartAlertDialog()
        }
        recyclerAdapter.removeCallback = { position ->
            urlsList.removeAt(position)
            recyclerAdapter.notifyItemRemoved(position)
            ringierAdPreferences.saveUrlsList(urlsList)
        }
    }

    private fun onSetDefaultButtonClicked() {
        binding?.apply {
            asetKeywordKey.text = KEYWORD_KEY_DEFAULT
            asetKeywordValue.text = KEYWORD_VALUE_DEFAULT
        }
        ringierAdPreferences.keywordKeyPref = KEYWORD_KEY_DEFAULT
        ringierAdPreferences.keywordValuePref = KEYWORD_VALUE_DEFAULT
        showRestartAlertDialog()
    }

    @SuppressLint("NotifyDataSetChanged")
    private fun onSaveButtonClicked(ringierAdUrlModel: RingierAdUrlModel) {
        val url = ringierAdUrlModel.url
        if (url.matches(Regex(""))) {
            Toast.makeText(this, "Url cannot be empty", Toast.LENGTH_SHORT).show()
        } else {
            urlsList.add(ringierAdUrlModel)
            ringierAdPreferences.saveUrlsList(urlsList)
            recyclerAdapter.notifyDataSetChanged()
        }
    }

    private fun openAdvancedSettingsActivity() {
        val intent = Intent(this, HiddenSettingsActivity::class.java)
        startActivity(intent)
    }

    override fun onClick(v: View?) {
        binding?.apply {
            when (v?.id) {
                asetButtonRemoveAll.id -> {
                    val filteredList = urlsList.filter {
                        !it.title.contains(Regex("Custom"))
                    }
                    ringierAdPreferences.saveUrlsList(filteredList)
                    setupRecyclerView()
                }
                asetButtonSave.id -> {
                    val url = asetEditTextUrl.text.toString()
                    onSaveButtonClicked(RingierAdUrlModel("Custom config url", url, false))
                    setupRecyclerView()
                }
                asetAdvancedSettings.id -> openAdvancedSettingsActivity()
                asetAdmforceButtonDefault.id -> onSetDefaultButtonClicked()
                asetKeywordKey.id -> this@SettingsActivity.showEditTextDialog(KEYWORD_TITLE_KEY) {
                    asetKeywordKey.text = it
                    ringierAdPreferences.keywordKeyPref = it
                    showRestartAlertDialog()
                }
                asetKeywordValue.id -> this@SettingsActivity.showEditTextDialog(KEYWORD_TITLE_VALUE) {
                    asetKeywordValue.text = it
                    ringierAdPreferences.keywordValuePref = it
                    showRestartAlertDialog()
                }
                asetKeywordRemove.id -> onKeywordRemovePressed()
            }
        }
    }

    private fun onKeywordRemovePressed() {
        binding?.apply {
            asetKeywordKey.text = NOT_SET_YET
            asetKeywordValue.text = NOT_SET_YET
        }
        ringierAdPreferences.removeString(KEYWORD_KEY_PREF)
        ringierAdPreferences.removeString(KEYWORD_VALUE_PREF)
        showRestartAlertDialog()
    }

    private fun initDefaultConfigs() {
        urlsList.add(
            RingierAdUrlModel(
                "Android SDK",
                "https://cdn.admeira.ch/staging/app_tagmanager/blick.ch_de/1.0.0/android.json",
                isActivated = true,
                isRemovable = false
            )
        )
        urlsList.add(
            RingierAdUrlModel(
                "Production, Blick.ch_de and version 1.0.0",
                "https://cdn.admeira.ch/prod/app_tagmanager/blick.ch_de/1.0.0/config.json",
                isActivated = false,
                isRemovable = false
            )
        )
        urlsList.add(
            RingierAdUrlModel(
                "Production, Blick.ch_de and version latest",
                "https://cdn.admeira.ch/prod/app_tagmanager/blick.ch_de/latest/config.json",
                isActivated = false,
                isRemovable = false
            )
        )
        urlsList.add(
            RingierAdUrlModel(
                "Staging, Blick.ch_de and version 1.0.0",
                "https://cdn.admeira.ch/staging/app_tagmanager/blick.ch_de/1.0.0/config.json",
                isActivated = false,
                isRemovable = false
            )
        )
        urlsList.add(
            RingierAdUrlModel(
                "Staging, Blick.ch_de and version latest",
                "https://cdn.admeira.ch/staging/app_tagmanager/blick.ch_de/latest/config.json",
                isActivated = false,
                isRemovable = false
            )
        )
    }

    override val bindingInflater: (LayoutInflater) -> ActivitySettingsBinding
        get() = ActivitySettingsBinding::inflate

}