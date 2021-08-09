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
import panteao.make.ready.utils.helpers.ImageHelper
import panteao.make.ready.utils.helpers.downloads.KTDownloadEvents
import panteao.make.ready.utils.helpers.downloads.KTDownloadHelper
import panteao.make.ready.utils.helpers.downloads.db.DownloadItemEntity

class MyDownloadsNewAdapter : RecyclerView.Adapter<RecyclerView.ViewHolder>,KTDownloadEvents {
    private lateinit var context : Context
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

    }

    override fun initialStatus(state: OfflineManager.AssetDownloadState) {

    }

    override fun onStateChanged(state: OfflineManager.AssetDownloadState) {

    }

    override fun onAssetDownloadComplete(assetId: String) {

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

    override fun onBindViewHolder(viewGroup: RecyclerView.ViewHolder, position: Int) {
        val currentVideoItem = itemList[position]
        try {
            Log.d("imageURL",currentVideoItem.imageURL)
            updatedStatus(viewHolder?.itemBinding,currentVideoItem,position,context)
            ImageHelper.getInstance(context)
                .loadListImage(viewHolder?.itemBinding?.itemImage, currentVideoItem.imageURL)
            if (currentVideoItem.isSeries){
                viewHolder?.itemBinding?.loadingDownload?.visibility = View.GONE
                viewHolder?.itemBinding?.tvTitle?.text = currentVideoItem.seriesName
            }else{
                viewHolder?.itemBinding?.loadingDownload?.visibility = View.GONE
                viewHolder?.itemBinding?.tvTitle?.text = currentVideoItem.name
            }

        }catch (exception : Exception){
            Log.d("imageURL",exception.toString())
        }

    }

    private fun updatedStatus(itemBinding: ListDownloadItemBinding?, currentVideoItem: DownloadItemEntity, position: Int, context: Context) {
        downloadHelper.getAssetDownloadState(currentVideoItem.entryId,downloadHelper,
            object : DownloadStateListener {
                override fun downloadState(name: String?, percentage: Float) {
                   Log.d("stateOfAsset 2",name!!)
                    Log.d("stateOfAsset 2",currentVideoItem.name!!)
                    Log.d("stateOfAsset 2",percentage.toString())
                    if (name == null) {
                        itemBinding?.flDeleteWatchlist?.visibility = View.GONE
                    }
                    else if (name.equals("paused", ignoreCase = true)){
                        itemBinding?.flDeleteWatchlist?.visibility = View.VISIBLE
                        itemBinding?.videoDownloading?.visibility = View.GONE
                        itemBinding?.videoDownloaded?.visibility = View.GONE
                        itemBinding?.pauseDownload?.visibility = View.VISIBLE
                        itemBinding?.loadingDownload?.visibility = View.GONE
                    }
                    else if (name.equals("started", ignoreCase = true)){
                        itemBinding?.flDeleteWatchlist?.visibility = View.VISIBLE
                        itemBinding?.videoDownloading?.visibility = View.VISIBLE
                        itemBinding?.videoDownloading?.progress = percentage
                        itemBinding?.videoDownloaded?.visibility = View.GONE
                        itemBinding?.pauseDownload?.visibility = View.GONE
                        itemBinding?.loadingDownload?.visibility = View.GONE
                    }
                    else if (name.equals("completed", ignoreCase = true)){
                        itemBinding?.flDeleteWatchlist?.visibility = View.VISIBLE
                        itemBinding?.videoDownloading?.visibility = View.VISIBLE
                        itemBinding?.videoDownloading?.visibility = View.GONE
                        itemBinding?.videoDownloaded?.visibility = View.VISIBLE
                        itemBinding?.pauseDownload?.visibility = View.GONE
                        itemBinding?.loadingDownload?.visibility = View.GONE
                    }else{

                    }
                }
            })

        itemBinding?.videoDownloading?.setOnClickListener { v ->
            downloadHelper.getAssetDownloadState(currentVideoItem.entryId,downloadHelper,
                object : DownloadStateListener {
                    override fun downloadState(name: String?, percentage: Float) {
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
                    override fun downloadState(name: String?, percentage: Float) {
                        if (name.equals("paused", ignoreCase = true)){
                            downloadHelper.resumeDownload(currentVideoItem.entryId)
                        }
                    }
                })
        }

        itemBinding?.pauseDownload?.setOnClickListener { v ->
            downloadHelper.getAssetDownloadState(currentVideoItem.entryId,downloadHelper,
                object : DownloadStateListener {
                    override fun downloadState(name: String?, percentage: Float) {
                        if (name.equals("paused", ignoreCase = true)){
                            downloadHelper.resumeDownload(currentVideoItem.entryId)
                        }
                    }
                })
        }


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
                        listener.updateAdapter(2)
                    }
                }
                R.id.pause_download -> {
                    downloadHelper.pauseVideo(video.entryId)
                    notifyAdapterByPosition(position)
                }
            }
            false
        }
        popup.inflate(R.menu.download_menu)
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
                var entity=itemList.get(index)
                if (entity.entryId.equals(assetId,ignoreCase = true)){
                    Log.w("sameid",itemList.get(index).entryId)
                    notifyItemChanged(index)
                    break;
                }
            }
        }
    }

    inner class LandscapeItemRowHolder(internal val itemBinding: ListDownloadItemBinding) : RecyclerView.ViewHolder(itemBinding.root)
}