package panteao.make.ready.utils.helpers.downloads.downloadListing

import android.app.Activity
import android.content.Context
import android.os.Handler
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.PopupMenu
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.kaltura.android.exoplayer2.util.Log
import com.kaltura.tvplayer.OfflineManager
import panteao.make.ready.R
import panteao.make.ready.databinding.ListDownloadItemBinding
import panteao.make.ready.enums.DownloadStatus
import panteao.make.ready.utils.helpers.ImageHelper
import panteao.make.ready.utils.helpers.NetworkConnectivity
import panteao.make.ready.utils.helpers.downloads.KTDownloadEvents
import panteao.make.ready.utils.helpers.downloads.KTDownloadHelper
import panteao.make.ready.utils.helpers.downloads.db.DownloadItemEntity
import panteao.make.ready.utils.helpers.intentlaunchers.ActivityLauncher
import panteao.make.ready.utils.helpers.ksPreferenceKeys.KsPreferenceKeys

class MyDownloadsNewAdapter : RecyclerView.Adapter<RecyclerView.ViewHolder>,KTDownloadEvents {
    private lateinit var context : Activity
    private lateinit var downloadHelper:KTDownloadHelper
    private lateinit var itemList: ArrayList<DownloadItemEntity>
    private lateinit var listener: KTDownloadEvents
    private var viewHolder: MyDownloadsNewAdapter.LandscapeItemRowHolder? = null

    constructor(activity: Activity, downloadsList: ArrayList<DownloadItemEntity>, ktDownloadEvents: KTDownloadEvents) {
        context = activity
        downloadHelper = KTDownloadHelper(activity, this)
        itemList=downloadsList;
        listener=ktDownloadEvents;
    }

    override fun setDownloadProgressListener(progress: Float, assetId: String?) {
        android.util.Log.w("activityProgress 2",progress.toString())
        if (::listener.isInitialized){
            listener.setDownloadProgressListener(progress,assetId)
        }
    }

    override fun onDownloadPaused(assetId: String) {
        android.util.Log.w("adapterCallBack 1","paused")
    }

    override fun initialStatus(state: OfflineManager.AssetDownloadState) {

    }

    override fun onStateChanged(state: OfflineManager.AssetDownloadState) {
        android.util.Log.w("adapterCallBack 2","onStateChanged")
        if (context!=null && !NetworkConnectivity.isOnline(context)){
            if (downloadHelper!=null && downloadHelper.manager!=null) {
              //  downloadHelper.manager.pauseDownloads()
            }
        }
    }

    override fun onAssetDownloadComplete(assetId: String) {
        android.util.Log.w("adapterCallBack 2","onAssetDownloadComplete")
        if (::listener.isInitialized){
            listener.onAssetDownloadComplete(assetId)
        }
    }

    override fun onAssetDownloadFailed(assetId: String, e: Exception?) {

    }

    override fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val listLdsItemBinding: ListDownloadItemBinding = DataBindingUtil.inflate(
            LayoutInflater.from(viewGroup.context),
            R.layout.list_download_item, viewGroup, false)
        viewHolder = LandscapeItemRowHolder(listLdsItemBinding)
        return viewHolder as MyDownloadsNewAdapter.LandscapeItemRowHolder
    }

    var PAY = "payload"
    var PAY2 = "payload2"
    var PAY3 = "payload3"
    override fun onBindViewHolder(
        holder: RecyclerView.ViewHolder,
        position: Int,
        payloads: MutableList<Any>
    ) {
        if (payloads!==null && payloads.size>0){
            for (pay in payloads){
                if (pay.equals(PAY)){
                    setLandscapeDataNew(holder as MyDownloadsNewAdapter.LandscapeItemRowHolder, position,itemList.get(position))
                }else if(pay.equals(PAY2)){
                    setLandscapeDataNew2(holder as MyDownloadsNewAdapter.LandscapeItemRowHolder, position,itemList.get(position))
                }
                else if(pay.equals(PAY3)){
                    setLandscapeDataNew3(holder as MyDownloadsNewAdapter.LandscapeItemRowHolder, position,itemList.get(position))
                }
            }
        }else{
            super.onBindViewHolder(holder, position, payloads)
        }

    }

    private fun setLandscapeDataNew3(itemBinding: MyDownloadsNewAdapter.LandscapeItemRowHolder,
                                     position: Int,
                                     currentVideoItem: DownloadItemEntity) {
        Log.d("checkingPayload 1","payload2")
        itemBinding?.itemBinding?.downloadStatus = DownloadStatus.PAUSE
        itemBinding?.itemBinding?.descriptionTxt?.text = DownloadStatus.PAUSED.name
    }

    private fun setLandscapeDataNew2(itemBinding: MyDownloadsNewAdapter.LandscapeItemRowHolder,
                                    position: Int,
                                    currentVideoItem: DownloadItemEntity) {
        Log.d("checkingPayload 1","payload2")
        downloadHelper.getAssetDownloadState(currentVideoItem.entryId,downloadHelper,
            object : DownloadStateListener {
                override fun downloadState(name: String?, percentage: Float,downloadSize: String?) {
                    Log.d("checkingPayload 2",name!!)
                    // Log.d("stateOfAsset 2",currentVideoItem.name!!)
                    //Log.d("stateOfAsset 2",percentage.toString())
                    if (name == null) {
                        itemBinding?.itemBinding.flDeleteWatchlist?.visibility = View.GONE
                    }
                    else if (name.equals("paused", ignoreCase = true)){
                        itemBinding?.itemBinding?.downloadStatus = DownloadStatus.PAUSE
                        itemBinding?.itemBinding?.descriptionTxt?.text = DownloadStatus.PAUSED.name
                    }
                    else if (name.equals("started", ignoreCase = true)){
                        // itemBinding?.flDeleteWatchlist?.visibility = View.VISIBLE
                        // itemBinding?.videoDownloading?.visibility = View.VISIBLE
                        itemBinding?.itemBinding?.downloadStatus = DownloadStatus.DOWNLOADING
                        itemBinding?.itemBinding?.videoDownloading?.progress = percentage
                        itemBinding?.itemBinding?.descriptionTxt?.text = DownloadStatus.DOWNLOADING.name
                        // itemBinding?.videoDownloaded?.visibility = View.GONE
                        // itemBinding?.pauseDownload?.visibility = View.GONE
                        // itemBinding?.loadingDownload?.visibility = View.GONE
                    }
                    else if (name.equals("completed", ignoreCase = true)){
                        itemBinding?.itemBinding?.downloadStatus = DownloadStatus.DOWNLOADED
                        itemBinding?.itemBinding?.descriptionTxt?.text = downloadSize
                    }else{

                    }
                }
            })

    }


    private fun setLandscapeDataNew(itemBinding: MyDownloadsNewAdapter.LandscapeItemRowHolder,
        position: Int,
        currentVideoItem: DownloadItemEntity) {
        Log.d("downloadStatus 1","payload")
        downloadHelper.getAssetDownloadState(currentVideoItem.entryId,downloadHelper,
            object : DownloadStateListener {
                override fun downloadState(name: String?, percentage: Float,downloadSize: String?) {
                    Log.d("downloadStatus 2",name!!)
                   // Log.d("stateOfAsset 2",currentVideoItem.name!!)
                    //Log.d("stateOfAsset 2",percentage.toString())
                    if (name == null) {
                        itemBinding?.itemBinding.flDeleteWatchlist?.visibility = View.GONE
                    }
                    else if (name.equals("paused", ignoreCase = true)){
                        itemBinding?.itemBinding?.downloadStatus = DownloadStatus.PAUSE
                        itemBinding?.itemBinding?.descriptionTxt?.text = DownloadStatus.PAUSED.name
                    }
                    else if (name.equals("started", ignoreCase = true)){
                        // itemBinding?.flDeleteWatchlist?.visibility = View.VISIBLE
                        // itemBinding?.videoDownloading?.visibility = View.VISIBLE
                        itemBinding?.itemBinding?.downloadStatus = DownloadStatus.DOWNLOADING
                        itemBinding?.itemBinding?.videoDownloading?.progress = percentage
                        itemBinding?.itemBinding?.descriptionTxt?.text = DownloadStatus.DOWNLOADING.name
                        // itemBinding?.videoDownloaded?.visibility = View.GONE
                        // itemBinding?.pauseDownload?.visibility = View.GONE
                        // itemBinding?.loadingDownload?.visibility = View.GONE
                    }
                    else if (name.equals("completed", ignoreCase = true)){
                        itemBinding?.itemBinding?.downloadStatus = DownloadStatus.DOWNLOADED
                        itemBinding?.itemBinding?.descriptionTxt?.text = downloadSize
                    }else{

                    }
                }
            })

    }

    override fun onBindViewHolder(viewGroup: RecyclerView.ViewHolder, position: Int) {
        setLandscapeData(viewHolder as MyDownloadsNewAdapter.LandscapeItemRowHolder, position)
    }

    private fun setLandscapeData(viewHolder: MyDownloadsNewAdapter.LandscapeItemRowHolder, position: Int) {
        val currentVideoItem = itemList[position]
        try {
            Log.d("imageURL",currentVideoItem.imageURL)
            updatedStatus(viewHolder?.itemBinding,currentVideoItem,position,context)
            ImageHelper.getInstance(context)
                .loadListImage(viewHolder?.itemBinding?.itemImage, currentVideoItem.imageURL)
            viewHolder?.itemBinding?.tvGenre?.visibility = View.GONE
            if (currentVideoItem.isSeries){
              //  viewHolder?.itemBinding?.loadingDownload?.visibility = View.GONE
                viewHolder?.itemBinding?.tvTitle?.text = currentVideoItem.name
            }else{
              //  viewHolder?.itemBinding?.loadingDownload?.visibility = View.GONE
                viewHolder?.itemBinding?.tvTitle?.text = currentVideoItem.name
            }

            viewHolder?.itemBinding?.clRoot?.setOnClickListener { v ->
                downloadHelper.getAssetDownloadState(currentVideoItem.entryId,downloadHelper,
                    object : DownloadStateListener {
                        override fun downloadState(name: String?, percentage: Float,downloadSize: String?) {
                            if (name.equals("completed", ignoreCase = true)){
                                ActivityLauncher(context!!).launchOfflinePlayer(currentVideoItem.entryId)
                            }else if(name.equals("failed", ignoreCase = true)){
                               failedDownloadPopUpMenu(v!!, currentVideoItem, position)
                            }
                        }
                    })

            }


        }catch (exception : Exception){
            Log.d("imageURL",exception.toString())
        }
    }

    private fun updatedStatus(itemBinding: ListDownloadItemBinding?, currentVideoItem: DownloadItemEntity, position: Int, context: Context) {
        downloadHelper.getAssetDownloadState(currentVideoItem.entryId,downloadHelper,
            object : DownloadStateListener {
                override fun downloadState(name: String?, percentage: Float,downloadSize: String?) {
                   Log.d("stateOfAsset 2",name!!)
                    Log.d("stateOfAsset 2",currentVideoItem.name!!)
                    Log.d("stateOfAsset 2",percentage.toString())
                    if (name == null) {
                        itemBinding?.flDeleteWatchlist?.visibility = View.GONE
                    }
                    else if (name.equals("paused", ignoreCase = true)){
                        itemBinding?.downloadStatus = DownloadStatus.PAUSE
                        viewHolder?.itemBinding?.descriptionTxt?.text = DownloadStatus.PAUSED.name
                    }
                    else if (name.equals("started", ignoreCase = true)){
                       // itemBinding?.flDeleteWatchlist?.visibility = View.VISIBLE
                       // itemBinding?.videoDownloading?.visibility = View.VISIBLE
                         itemBinding?.downloadStatus = DownloadStatus.DOWNLOADING
                        itemBinding?.videoDownloading?.progress = percentage
                        viewHolder?.itemBinding?.descriptionTxt?.text = DownloadStatus.DOWNLOADING.name
                       // itemBinding?.videoDownloaded?.visibility = View.GONE
                       // itemBinding?.pauseDownload?.visibility = View.GONE
                       // itemBinding?.loadingDownload?.visibility = View.GONE
                    }
                    else if (name.equals("completed", ignoreCase = true)){
                        itemBinding?.downloadStatus = DownloadStatus.DOWNLOADED
                        itemBinding?.descriptionTxt?.text = downloadSize
                    }
                    else if (name.equals("failed", ignoreCase = true)){
                        itemBinding?.downloadStatus = DownloadStatus.FAILED
                        itemBinding?.descriptionTxt?.text = DownloadStatus.FAILED.name
                    }
                    else{

                    }
                }
            })

        itemBinding?.videoDownloading?.setOnClickListener { v ->
            downloadHelper.getAssetDownloadState(currentVideoItem.entryId,downloadHelper,
                object : DownloadStateListener {
                    override fun downloadState(name: String?, percentage: Float,downloadSize: String?) {
                        if (name.equals("paused", ignoreCase = true)){
                            downloadHelper.resumeDownload(currentVideoItem.entryId)
                        }else{
                            showPauseCancelPopUpMenu(v!!, currentVideoItem, position)
                        }
                    }
                })
        }

        itemBinding?.pauseDownload?.setOnClickListener { v ->
            downloadHelper.getAssetDownloadState(currentVideoItem.entryId,downloadHelper,
                object : DownloadStateListener {
                    override fun downloadState(name: String?, percentage: Float,downloadSize: String?) {
                        if (name.equals("paused", ignoreCase = true)){
                            downloadHelper.resumeDownload(currentVideoItem.entryId)
                            notifyItemWhenResumed(currentVideoItem.entryId)
                        }
                    }
                })
        }


        itemBinding?.videoDownloaded?.setOnClickListener { v ->
            downloadHelper.getAssetDownloadState(currentVideoItem.entryId,downloadHelper,
                object : DownloadStateListener {
                    override fun downloadState(name: String?, percentage: Float,downloadSize: String?) {
                        if (name.equals("completed", ignoreCase = true)){
                            deleteVideo(v!!, currentVideoItem, position)
                        }
                    }
                })
        }


    }

    private fun failedDownloadPopUpMenu(view: View, video: DownloadItemEntity, position: Int) {
        val popup = PopupMenu(context, view)
        popup.setOnMenuItemClickListener { item ->
            when (item.itemId) {
                R.id.delete_download -> {
                    downloadHelper.cancelVideo(video.entryId)
                    itemList.remove(video)
                    notifyItemRemoved(position)
                    notifyItemRangeChanged(position, itemList.size)
                    if (itemList.size>0){
                        listener.updateAdapter(1)
                    }else{
                        KsPreferenceKeys.getInstance().videoDownloadAction = 3
                        listener.updateAdapter(2)
                    }
                }
            }
            false
        }
        popup.inflate(R.menu.failed_download)
        popup.show()
    }


    private fun showPauseCancelPopUpMenu(view: View, video: DownloadItemEntity, position: Int) {
        val popup = PopupMenu(context, view)
        popup.setOnMenuItemClickListener { item ->
            when (item.itemId) {
                R.id.cancel_download -> {
                    downloadHelper.cancelVideo(video.entryId)
                    itemList.remove(video)
                    notifyItemRemoved(position)
                    notifyItemRangeChanged(position, itemList.size)
                    if (itemList.size>0){
                        listener.updateAdapter(1)
                    }else{
                        KsPreferenceKeys.getInstance().videoDownloadAction = 3
                        listener.updateAdapter(2)
                    }
                }
                R.id.pause_download -> {
                    downloadHelper.pauseVideo(video.entryId)
                    notifyItemWhenPaused(video.entryId)
                }
            }
            false
        }
        popup.inflate(R.menu.download_menu)
        popup.show()
    }

    private fun deleteVideo(view: View, video: DownloadItemEntity, position: Int) {
        val popup = PopupMenu(context, view)
        popup.setOnMenuItemClickListener { item ->
            when (item.itemId) {
                R.id.delete_download -> {
                    downloadHelper.cancelVideo(video.entryId)
                    itemList.remove(video)
                    notifyItemRemoved(position)
                    notifyItemRangeChanged(position, itemList.size)
                    if (itemList.size>0){
                        listener.updateAdapter(1)
                    }else{
                        KsPreferenceKeys.getInstance().videoDownloadAction = 3
                        listener.updateAdapter(2)
                    }
                }
            }
            false
        }

        popup.inflate(R.menu.my_downloads_menu)
        popup.show()
    }


    private fun notifyAdapterByPosition(position: Int) {
        notifyItemChanged(position)
    }

    override fun getItemCount(): Int {
       return itemList.size
    }

    fun notifyItemChanged(assetId: String?) {
        if (itemList!==null && itemList.size>0){
            for ((index,value ) in itemList.withIndex()){
                Log.w("sameid -1",value.name!!)
                Log.w("sameid 0",assetId!!)
                Log.w("sameid 1",value.entryId)
                Log.w("sameid 2",index.toString())
                var entity=itemList.get(index)
                if (entity.entryId.equals(assetId,ignoreCase = true)){
                    Log.w("sameid 3",value.entryId)
                    Log.w("sameid 4",index.toString())
                    itemList[index] = value
                    notifyItemChanged(index,PAY)
                   // notifyItemRangeChanged(index,itemList.size)
                    //break;
                }
            }
        }
    }

    public fun notifyItemWhenResumed(assetId: String?) {
        Log.w("notifyWhenResume -11","")
        if (itemList!==null && itemList.size>0){
            for ((index,value ) in itemList.withIndex()){
                Log.w("notifyWhenResume -1",value.name!!)
                Log.w("notifyWhenResume 0",assetId!!)
                Log.w("notifyWhenResume 1",value.entryId)
                Log.w("notifyWhenResume 2",index.toString())
                var entity=itemList.get(index)
                if (entity.entryId.equals(assetId,ignoreCase = true)){
                    Log.w("notifyWhenResume 3",value.entryId)
                    Log.w("notifyWhenResume 4",index.toString())
                    itemList[index] = value
                    notifyItemChanged(index,PAY2)
                    // notifyItemRangeChanged(index,itemList.size)
                    //break;
                }
            }
        }
    }

    private fun notifyItemWhenPaused(assetId: String?) {
        Log.w("notifyWhenResume -11","")
        if (itemList!==null && itemList.size>0){
            for ((index,value ) in itemList.withIndex()){
                Log.w("notifyWhenResume -1",value.name!!)
                Log.w("notifyWhenResume 0",assetId!!)
                Log.w("notifyWhenResume 1",value.entryId)
                Log.w("notifyWhenResume 2",index.toString())
                var entity=itemList.get(index)
                if (entity.entryId.equals(assetId,ignoreCase = true)){
                    Log.w("notifyWhenResume 3",value.entryId)
                    Log.w("notifyWhenResume 4",index.toString())
                    itemList[index] = value
                    notifyItemChanged(index,PAY3)
                    // notifyItemRangeChanged(index,itemList.size)
                    //break;
                }
            }
        }
    }

    inner class LandscapeItemRowHolder(internal val itemBinding: ListDownloadItemBinding) : RecyclerView.ViewHolder(itemBinding.root)
}