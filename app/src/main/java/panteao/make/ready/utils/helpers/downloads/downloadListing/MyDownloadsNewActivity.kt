package panteao.make.ready.utils.helpers.downloads.downloadListing

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.SimpleItemAnimator
import com.kaltura.tvplayer.OfflineManager
import kotlinx.android.synthetic.main.activity_my_downloads.*
import panteao.make.ready.R
import panteao.make.ready.baseModels.BaseBindingActivity
import panteao.make.ready.databinding.ActivityMyDownloadsBinding
import panteao.make.ready.utils.commonMethods.AppCommonMethod
import panteao.make.ready.utils.constants.AppConstants
import panteao.make.ready.utils.helpers.downloads.KTDownloadEvents
import panteao.make.ready.utils.helpers.downloads.KTDownloadHelper
import panteao.make.ready.utils.helpers.downloads.db.DownloadItemEntity
import panteao.make.ready.utils.helpers.ksPreferenceKeys.KsPreferenceKeys

class MyDownloadsNewActivity : BaseBindingActivity<ActivityMyDownloadsBinding>(), KTDownloadEvents {

    private lateinit var downloadHelper:KTDownloadHelper
    private lateinit var downloadsAdapter:MyDownloadsNewAdapter
    override fun inflateBindingLayout(inflater: LayoutInflater): ActivityMyDownloadsBinding {
        return ActivityMyDownloadsBinding.inflate(inflater)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setupToolBar();
        fetchdataBaseValues();
    }

    private fun fetchdataBaseValues() {
        downloadHelper= KTDownloadHelper(this,this)
        progress_bar.visibility = View.VISIBLE
        downloadHelper.getAllAssetFromDB().observe(this, Observer {
            if(it!==null && it.size>0){
                noDownloadedData(2)
                createUniqueList(it)
            }else{
                noDownloadedData(1)
            }
        })
    }

    private fun noDownloadedData(noData : Int) {
        if (noData==1){
            nodatafounmd.visibility = View.VISIBLE
            rec_layout.visibility = View.GONE
            progress_bar.visibility = View.GONE
        }else{
            nodatafounmd.visibility = View.GONE
            rec_layout.visibility = View.VISIBLE
            progress_bar.visibility = View.GONE
        }

    }

    var included : Boolean?=false
    private fun createUniqueList(it: List<DownloadItemEntity>) {
        var listNew=AppCommonMethod.getSortedList(it)
        populateAdapter(listNew)
    }

    private fun populateAdapter(it: ArrayList<DownloadItemEntity>) {
        downloadsAdapter = MyDownloadsNewAdapter(this, it, this)
        downloaded_recycler_view.layoutManager = LinearLayoutManager(this)
        downloaded_recycler_view.setHasFixedSize(true)
        (binding.downloadedRecyclerView.getItemAnimator() as SimpleItemAnimator).supportsChangeAnimations = false
        downloaded_recycler_view.adapter = downloadsAdapter
        nodatafounmd.visibility = View.GONE
        downloaded_recycler_view.visibility = View.VISIBLE
        rec_layout.visibility = View.VISIBLE
        progress_bar.visibility = View.GONE
    }

    private fun setupToolBar() {
            if (KsPreferenceKeys.getInstance().getCurrentTheme() == (AppConstants.LIGHT_THEME)) {
                binding.noData.setBackgroundResource(R.drawable.ic_no_data);
            } else {
                binding.noData.setBackgroundResource(R.drawable.ic_no_data);
            }
            binding.toolbar.llSearchIcon.visibility = View.GONE
            binding.toolbar.backLayout.visibility = View.VISIBLE
            binding.toolbar.homeIcon.visibility = View.GONE
            binding.toolbar.titleText.visibility = View.VISIBLE
            binding.toolbar.screenText.text = resources.getString(R.string.my_downloads)
            binding.toolbar.backLayout.setOnClickListener { onBackPressed() }
        }

    override fun setDownloadProgressListener(progress: Float, assetId: String?) {
            Log.w("activityProgress 1",progress.toString())
            if(::downloadsAdapter.isInitialized){
                binding.downloadedRecyclerView.post(Runnable { downloadsAdapter?.notifyItemChanged(assetId) })

            }
    }

    override fun onDownloadPaused(assetId: String) {

    }

    override fun initialStatus(state: OfflineManager.AssetDownloadState) {

    }

    override fun onStateChanged(state: OfflineManager.AssetDownloadState) {

    }

    override fun onAssetDownloadComplete(assetId: String) {

    }

    override fun onAssetDownloadFailed(assetId: String, e: Exception?) {

    }

    override fun updateAdapter(type : Int) {
        progress_bar.visibility = View.VISIBLE
        if (type==1){
            binding.progressBar.visibility = View.VISIBLE
            Handler(Looper.getMainLooper()).postDelayed({
                downloadHelper.getAllAssetFromDB().observe(this, Observer {
                    binding.progressBar.visibility = View.GONE
                    if(it!==null && it.size>0){
                        noDownloadedData(2)
                        createUniqueList(it)
                    }else{
                        noDownloadedData(1)
                    }
                })
            }, 1500)
        }else{
            noDownloadedData(1)
        }

    }

}