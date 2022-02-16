package panteao.make.ready.utils.helpers.downloads.downloadListing

import android.content.IntentFilter
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.view.View
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.LinearLayoutManager
import com.kaltura.tvplayer.OfflineManager
import kotlinx.android.synthetic.main.activity_my_downloads.*
import panteao.make.ready.R
import panteao.make.ready.baseModels.BaseBindingActivity
import panteao.make.ready.callbacks.commonCallbacks.NetworkChangeReceiver
import panteao.make.ready.callbacks.commonCallbacks.NetworkChangeReceiver.ConnectivityReceiverListener
import panteao.make.ready.databinding.ActivityMyDownloadsBinding
import panteao.make.ready.utils.commonMethods.AppCommonMethod
import panteao.make.ready.utils.constants.AppConstants
import panteao.make.ready.utils.helpers.downloads.KTDownloadEvents
import panteao.make.ready.utils.helpers.downloads.KTDownloadHelper
import panteao.make.ready.utils.helpers.downloads.db.DownloadItemEntity
import panteao.make.ready.utils.helpers.ksPreferenceKeys.KsPreferenceKeys

class MyDownloadsNewActivity : BaseBindingActivity<ActivityMyDownloadsBinding>(), KTDownloadEvents,
    ConnectivityReceiverListener{

    private lateinit var downloadHelper:KTDownloadHelper
    private lateinit var downloadsAdapter:MyDownloadsNewAdapter
    private var seriesID : String? = ""
    private var title : String? = ""
    private var sesonNumber : Int? = 0
    override fun inflateBindingLayout(): ActivityMyDownloadsBinding {
        return ActivityMyDownloadsBinding.inflate(inflater)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        title=intent.getStringExtra("title")
        seriesID=intent.getStringExtra("series_id")
        sesonNumber=intent.getIntExtra("season_number",0)
        setupToolBar()
        fetchdataBaseValues()
    }

    private fun fetchdataBaseValues() {
        downloadHelper= KTDownloadHelper(this,this)
        progress_bar.visibility = View.VISIBLE
        downloadHelper.getAllEpisodesFromDB(seriesID,sesonNumber!!).observe(this, Observer {
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
        if (it.size>0){
            if (!it.get(0).assetType.equals("chapter", ignoreCase = true)){
                var listNew=AppCommonMethod.getEpisodesSortedList(it)
                populateAdapter(listNew)
                try {
                    if (downloadHelper!=null){
                        if (downloadHelper.manager!=null){
                            if (listNew.size>0){
                                downloadHelper.updateDownload(listNew)
                            }
                        }
                    }
                }catch (ignored : Exception){

                }
            }else{
                var listNew=AppCommonMethod.getSortChapters(ArrayList<DownloadItemEntity>(it))
                populateAdapter(listNew)
                try {
                    if (downloadHelper!=null){
                        if (downloadHelper.manager!=null){
                            if (listNew.size>0){
                                downloadHelper.updateDownload(listNew)
                            }
                        }
                    }
                }catch (ignored : Exception){

                }
            }

        }

    }

    private fun populateAdapter(it: ArrayList<DownloadItemEntity>) {
        downloadsAdapter = MyDownloadsNewAdapter(this, it, this)
        downloaded_recycler_view.layoutManager = LinearLayoutManager(this)
        downloaded_recycler_view.setHasFixedSize(true)
        //(binding.downloadedRecyclerView.getItemAnimator() as SimpleItemAnimator).supportsChangeAnimations = false
       // downloaded_recycler_view.getItemAnimator().setSupportsChangeAnimations(false);
       // downloaded_recycler_view.itemAnimator = null
        downloaded_recycler_view.itemAnimator = DefaultItemAnimator()
        downloaded_recycler_view.itemAnimator?.changeDuration = 0
        downloaded_recycler_view.adapter = downloadsAdapter
        nodatafounmd.visibility = View.GONE
        downloaded_recycler_view.visibility = View.VISIBLE
        rec_layout.visibility = View.VISIBLE
        progress_bar.visibility = View.GONE
    }

    private fun setupToolBar() {
            if (KsPreferenceKeys.getInstance().currentTheme == (AppConstants.LIGHT_THEME)) {
                binding.noData.setBackgroundResource(R.drawable.ic_no_data)
            } else {
                binding.noData.setBackgroundResource(R.drawable.ic_no_data)
            }
            binding.toolbar.llSearchIcon.visibility = View.GONE
            binding.toolbar.backLayout.visibility = View.VISIBLE
            binding.toolbar.homeIcon.visibility = View.GONE
            binding.toolbar.titleText.visibility = View.VISIBLE
            if (title!=null && !title.equals("")){
                binding.toolbar.screenText.text = title
            }else{
                binding.toolbar.screenText.text = resources.getString(R.string.my_downloads)
            }

            binding.toolbar.backLayout.setOnClickListener { onBackPressed() }
        }

    override fun setDownloadProgressListener(progress: Float, assetId: String?) {
            Log.e("activityProgress new",progress.toString())
            if(::downloadsAdapter.isInitialized){
                binding.downloadedRecyclerView.post(Runnable { downloadsAdapter.notifyItemChanged(assetId) })

            }
    }

    override fun onDownloadPaused(assetId: String) {

    }

    override fun initialStatus() {

    }

    override fun onStateChanged(state: OfflineManager.AssetDownloadState,assetId: String) {

    }

    override fun onAssetDownloadComplete(assetId: String) {
        if(::downloadsAdapter.isInitialized){
            binding.downloadedRecyclerView.post(Runnable { downloadsAdapter.notifyItemWhenResumed(assetId) })
        }
    }

    override fun onAssetDownloadFailed() {

    }

    override fun updateAdapter(type : Int) {
        progress_bar.visibility = View.VISIBLE
        if (type==1){
            binding.progressBar.visibility = View.VISIBLE
            Handler(Looper.getMainLooper()).postDelayed({
                                                        fetchdataBaseValues()
                /*downloadHelper.getAllAssetFromDB().observe(this, Observer {
                    binding.progressBar.visibility = View.GONE
                    if(it!==null && it.size>0){
                        noDownloadedData(2)
                        KsPreferenceKeys.getInstance().videoDownloadAction = 3
                        createUniqueList(it)
                    }else{
                        noDownloadedData(1)
                    }
                })*/
            }, 1500)
        }else{
            noDownloadedData(1)
        }

    }

    override fun onResume() {
        super.onResume()
        setBroadcast()
    }
    private var receiver: NetworkChangeReceiver? = null
    private fun setBroadcast() {
            receiver = NetworkChangeReceiver()
            val filter = IntentFilter()
            filter.addAction("android.net.conn.CONNECTIVITY_CHANGE")
            filter.addAction("android.net.wifi.WIFI_STATE_CHANGED")
            filter.addAction("android.net.wifi.STATE_CHANGE")
            this@MyDownloadsNewActivity.registerReceiver(receiver, filter)
            setConnectivityListener(this)
    }

    fun setConnectivityListener(listener: ConnectivityReceiverListener?) {
        NetworkChangeReceiver.connectivityReceiverListener = listener
    }

    override fun onNetworkConnectionChanged(isConnected: Boolean) {
        Log.d("onNetworkConnection",isConnected.toString())
        if (!isConnected){
            if (downloadHelper!=null && downloadHelper.manager!=null) {
                downloadHelper.manager.pauseDownloads()
            }
        }
    }

    override fun onPause() {
        super.onPause()
        if (receiver != null) {
            unregisterReceiver(receiver)
            if (NetworkChangeReceiver.connectivityReceiverListener != null) NetworkChangeReceiver.connectivityReceiverListener = null
        }
    }

}