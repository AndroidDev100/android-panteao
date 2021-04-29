package panteao.make.ready.activities.settings.downloadsettings.changequality.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.gson.Gson
import panteao.make.ready.R
import panteao.make.ready.activities.settings.downloadsettings.DownloadSettings.Companion.CHANGE_QUALITY_REQUEST_CODE
import panteao.make.ready.databinding.ActivityChangeDownloadQualityBinding
import panteao.make.ready.activities.settings.downloadsettings.changequality.adapter.ChangeDownloadQualityAdapter
import panteao.make.ready.baseModels.BaseBindingActivity
import panteao.make.ready.utils.cropImage.helpers.Logger
import panteao.make.ready.utils.helpers.ksPreferenceKeys.KsPreferenceKeys


class ChangeDownloadQuality : BaseBindingActivity<ActivityChangeDownloadQualityBinding>() {
    private var changeDownloadQualityAdapter: ChangeDownloadQualityAdapter? = null
    override fun inflateBindingLayout(inflater: LayoutInflater): ActivityChangeDownloadQualityBinding {
        return ActivityChangeDownloadQualityBinding.inflate(inflater)
    }

    private var preference: KsPreferenceKeys? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        preference = KsPreferenceKeys.getInstance()
        setupToolbar()
        uiInitialization()

    }

    private fun setupToolbar() {
        binding.toolbar.llSearchIcon.visibility = View.GONE
        binding.toolbar.backLayout.visibility = View.VISIBLE
        binding.toolbar.homeIcon.visibility = View.GONE
        binding.toolbar.titleText.visibility = View.VISIBLE
        binding.toolbar.screenText.text = resources.getString(R.string.download_quality)
        binding.toolbar.backLayout.setOnClickListener { onBackPressed() }
    }

    override fun onBackPressed() {
//        super.onBackPressed()
        setResult(CHANGE_QUALITY_REQUEST_CODE)
        finish()
    }

    private val downloadQualityList: ArrayList<String> = ArrayList()
    private fun setAdapter() {
        downloadQualityList.addAll(resources.getStringArray(R.array.download_quality))
        Logger.e("ChangeDownloadQuality", Gson().toJson(downloadQualityList));
        changeDownloadQualityAdapter = ChangeDownloadQualityAdapter(this@ChangeDownloadQuality, downloadQualityList)
        binding.recyclerview.adapter = changeDownloadQualityAdapter
    }

    private fun uiInitialization() {
        binding.recyclerview.hasFixedSize()
        binding.recyclerview.isNestedScrollingEnabled = false
        binding.recyclerview.layoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
        val dividerItemDecoration = DividerItemDecoration(binding.recyclerview.context,
                (binding.recyclerview.layoutManager as LinearLayoutManager).orientation)
        binding.recyclerview.addItemDecoration(dividerItemDecoration)
        setAdapter()
    }
}