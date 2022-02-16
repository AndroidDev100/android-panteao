package panteao.make.ready.utils.helpers.downloads.downloadListing

import android.app.Activity
import android.content.Context
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
import panteao.make.ready.utils.commonMethods.AppCommonMethod
import panteao.make.ready.utils.helpers.ImageHelper
import panteao.make.ready.utils.helpers.downloads.KTDownloadEvents
import panteao.make.ready.utils.helpers.downloads.KTDownloadHelper
import panteao.make.ready.utils.helpers.downloads.db.DownloadItemEntity
import panteao.make.ready.utils.helpers.intentlaunchers.ActivityLauncher
import panteao.make.ready.utils.helpers.ksPreferenceKeys.KsPreferenceKeys
import java.util.*

class MyDownloadsFragmentAdapter : RecyclerView.Adapter<RecyclerView.ViewHolder>, KTDownloadEvents {
    private lateinit var context : Activity
    private lateinit var downloadHelper: KTDownloadHelper
    private lateinit var itemList: ArrayList<DownloadItemEntity>
    private lateinit var listener: KTDownloadEvents
    private var viewHolder: MyDownloadsFragmentAdapter.LandscapeItemRowHolder? = null

    constructor(activity: Activity, downloadsList: ArrayList<DownloadItemEntity>, ktDownloadEvents: KTDownloadEvents) {
        context = activity
        downloadHelper = KTDownloadHelper(activity, this)
        itemList=downloadsList
        listener=ktDownloadEvents
    }

    override fun setDownloadProgressListener(progress: Float, assetId: String?) {
        android.util.Log.w("activityProgress 2",progress.toString())
        if (::listener.isInitialized){
            listener.setDownloadProgressListener(progress,assetId)
        }
    }

    override fun onDownloadPaused(assetId: String) {

    }

    override fun initialStatus() {

    }

    override fun onStateChanged(state: OfflineManager.AssetDownloadState,assetId: String?) {

    }

    override fun onAssetDownloadComplete(assetId: String) {
        if (::listener.isInitialized){
            listener.onAssetDownloadComplete(assetId)
        }
    }

    override fun onAssetDownloadFailed() {

    }

    override fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val listLdsItemBinding: ListDownloadItemBinding = DataBindingUtil.inflate(
            LayoutInflater.from(viewGroup.context),
            R.layout.list_download_item, viewGroup, false)
        viewHolder = LandscapeItemRowHolder(listLdsItemBinding)
        return viewHolder as MyDownloadsFragmentAdapter.LandscapeItemRowHolder
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
                    setLandscapeDataNew(holder as MyDownloadsFragmentAdapter.LandscapeItemRowHolder, position,itemList.get(position))
                }else if(pay.equals(PAY2)){
                    setLandscapeDataNew2(holder as MyDownloadsFragmentAdapter.LandscapeItemRowHolder, position,itemList.get(position))
                }
                else if(pay.equals(PAY3)){
                    setLandscapeDataNew3(holder as MyDownloadsFragmentAdapter.LandscapeItemRowHolder, position,itemList.get(position))
                }
            }
        }else{
            super.onBindViewHolder(holder, position, payloads)
        }

    }

    private fun setLandscapeDataNew3(itemBinding: MyDownloadsFragmentAdapter.LandscapeItemRowHolder,
                                     position: Int,
                                     currentVideoItem: DownloadItemEntity) {
        Log.d("checkingPayload 1","payload2")
        itemBinding.itemBinding.downloadStatus = DownloadStatus.PAUSE
        itemBinding.itemBinding.descriptionTxt.text = DownloadStatus.PAUSED.name
    }


    private fun setLandscapeDataNew2(itemBinding: MyDownloadsFragmentAdapter.LandscapeItemRowHolder,
                                     position: Int,
                                     currentVideoItem: DownloadItemEntity) {
        Log.d("downloadStatus 1","payload2")
        downloadHelper.getAssetDownloadState(currentVideoItem.entryId,downloadHelper,
            object : DownloadStateListener {
                override fun downloadState(name: String?, percentage: Float,downloadSize: String?) {
                    Log.d("downloadStatus 2",name!!)
                    // Log.d("stateOfAsset 2",currentVideoItem.name!!)
                    //Log.d("stateOfAsset 2",percentage.toString())
                    if (name == null) {
                        itemBinding.itemBinding.flDeleteWatchlist.visibility = View.GONE
                    }
                    else if (name.equals("paused", ignoreCase = true)){
                        itemBinding.itemBinding.downloadStatus = DownloadStatus.PAUSE
                        itemBinding.itemBinding.descriptionTxt.text = DownloadStatus.PAUSED.name
                    }
                    else if (name.equals("started", ignoreCase = true)){
                        // itemBinding?.flDeleteWatchlist?.visibility = View.VISIBLE
                        // itemBinding?.videoDownloading?.visibility = View.VISIBLE
                        itemBinding.itemBinding.downloadStatus = DownloadStatus.DOWNLOADING
                        itemBinding.itemBinding.videoDownloading.progress = percentage
                        // itemBinding?.videoDownloaded?.visibility = View.GONE
                        // itemBinding?.pauseDownload?.visibility = View.GONE
                        // itemBinding?.loadingDownload?.visibility = View.GONE
                    }
                    else if (name.equals("completed", ignoreCase = true)){
                        itemBinding.itemBinding.downloadStatus = DownloadStatus.DOWNLOADED
                        itemBinding.itemBinding.descriptionTxt.text = downloadSize
                        itemBinding.itemBinding.tvGenre.visibility = View.VISIBLE
                        itemBinding.itemBinding.tvGenre.text = DownloadStatus.Completed.name

                        itemBinding.itemBinding.tvGenre.setTextColor(
                            context.resources.getColor(R.color.more_text_color_dark)
                        )
                        itemBinding.itemBinding.descriptionTxt.setTextColor(
                            context.resources.getColor(R.color.more_text_color_dark)
                        )
                    }else{

                    }
                }
            })

    }


    private fun setLandscapeDataNew(itemBinding: MyDownloadsFragmentAdapter.LandscapeItemRowHolder,
                                    position: Int,
                                    currentVideoItem: DownloadItemEntity) {
        if(currentVideoItem.isSeries){
            return
        }
        downloadHelper.getAssetDownloadState(currentVideoItem.entryId,downloadHelper,
            object : DownloadStateListener {
                override fun downloadState(name: String?, percentage: Float,downloadSize: String?) {
                    Log.d("stateOfAsset 2",name!!)
                    Log.d("stateOfAsset 2",currentVideoItem.name!!)
                    Log.d("stateOfAsset 2",percentage.toString())
                    if (name == null) {
                        itemBinding.itemBinding.flDeleteWatchlist.visibility = View.GONE
                    }
                    else if (name.equals("paused", ignoreCase = true)){
                        itemBinding.itemBinding.downloadStatus = DownloadStatus.PAUSE
                        itemBinding.itemBinding.descriptionTxt.text = DownloadStatus.PAUSED.name
                    }
                    else if (name.equals("started", ignoreCase = true)){
                        // itemBinding?.flDeleteWatchlist?.visibility = View.VISIBLE
                        // itemBinding?.videoDownloading?.visibility = View.VISIBLE
                        itemBinding.itemBinding.downloadStatus = DownloadStatus.DOWNLOADING
                        itemBinding.itemBinding.videoDownloading.progress = percentage
                        // itemBinding?.videoDownloaded?.visibility = View.GONE
                        // itemBinding?.pauseDownload?.visibility = View.GONE
                        // itemBinding?.loadingDownload?.visibility = View.GONE
                    }
                    else if (name.equals("completed", ignoreCase = true)){
                        itemBinding.itemBinding.downloadStatus = DownloadStatus.DOWNLOADED
                        itemBinding.itemBinding.descriptionTxt.text = downloadSize
                        itemBinding.itemBinding.tvGenre.visibility = View.VISIBLE
                        itemBinding.itemBinding.tvGenre.text = DownloadStatus.Completed.name

                        itemBinding.itemBinding.tvGenre.setTextColor(
                            context.resources.getColor(R.color.more_text_color_dark)
                        )
                        itemBinding.itemBinding.descriptionTxt.setTextColor(
                            context.resources.getColor(R.color.more_text_color_dark)
                        )
                    }else{

                    }
                }
            })

    }



    override fun onBindViewHolder(viewGroup: RecyclerView.ViewHolder, position: Int) {
        val currentVideoItem = itemList[position]
        try {
            Log.d("imageURL",currentVideoItem.imageURL)
            viewHolder?.itemBinding?.tvGenre?.visibility = View.GONE
            if (!currentVideoItem.isSeries){
                updatedStatus(viewHolder?.itemBinding,currentVideoItem,position,context)
            }

            if (currentVideoItem.isSeries){
                if (currentVideoItem.seriesImageUrl!=null && !currentVideoItem.seriesImageUrl.equals("", ignoreCase = true)){
                    ImageHelper.getInstance(context).loadListImage(viewHolder?.itemBinding?.itemImage, AppCommonMethod.getListLDownloadImage(currentVideoItem.seriesImageUrl, context))
                    /*ImageHelper.getInstance(context)
                        .loadListImage(viewHolder?.itemBinding?.itemImage, currentVideoItem.seriesImageUrl)*/
                }
                viewHolder?.itemBinding?.flDeleteWatchlist?.visibility = View.VISIBLE
                viewHolder?.itemBinding?.arrowIcon?.visibility = View.VISIBLE
                viewHolder?.itemBinding?.tvTitle?.text = currentVideoItem.seriesName
                if (!currentVideoItem.assetType.equals("chapter", ignoreCase = true)){
                    var builder=StringBuilder()
                    builder.append("Season ")
                    if (currentVideoItem.seasonNumber==0){
                        builder.append("1")
                    }else{
                        builder.append(currentVideoItem.seasonNumber)
                    }
                    builder.append(" | ")
                    if (currentVideoItem.episodesCount==0){
                        builder.append("1")
                    }else{
                        var occurance=accuranceOfEpisodes(AppCommonMethod.occuranceList,currentVideoItem.seriesIdWithNumber)
                        Log.w("occurance-->",occurance.toString()+"")
                        builder.append(occurance.toString())
                    }
                    if (currentVideoItem.episodesCount>1){
                        builder.append(" Episodes")
                    }else{
                        builder.append(" Episode")
                    }

                    viewHolder?.itemBinding?.descriptionTxt?.text = builder.toString()
                }else{
                    var builder=StringBuilder()
                    builder.append("Tutorial")
                    builder.append(" | ")
                    if (currentVideoItem.episodesCount==0){
                        builder.append("1")
                    }else{
                        builder.append(currentVideoItem.episodesCount.toString())
                    }
                    builder.append(" Chapters")
                    viewHolder?.itemBinding?.descriptionTxt?.text = builder.toString()
                }

            }else{
                //ImageHelper.getInstance(context).loadListImage(viewHolder?.itemBinding?.itemImage, currentVideoItem.imageURL) ImageLayer.getInstance().getFilteredImage(crousalImages, imageType, dpToPx(128f).toInt(), dpToPx(72f).toInt())
                ImageHelper.getInstance(context).loadListImage(viewHolder?.itemBinding?.itemImage, AppCommonMethod.getListLDownloadImage(currentVideoItem.imageURL, context))
                viewHolder?.itemBinding?.loadingDownload?.visibility = View.GONE
                viewHolder?.itemBinding?.arrowIcon?.visibility = View.GONE
                viewHolder?.itemBinding?.tvTitle?.text = currentVideoItem.name
            }
            
            viewHolder?.itemBinding?.clRoot?.setOnClickListener { v ->
                if (currentVideoItem.isSeries){
                    ActivityLauncher(context).launchMyDownloads(currentVideoItem.seriesId,currentVideoItem.seasonNumber,viewHolder?.itemBinding?.tvTitle?.text.toString())
                }else{
                    downloadHelper.getAssetDownloadState(currentVideoItem.entryId,downloadHelper,
                        object : DownloadStateListener {
                            override fun downloadState(name: String?, percentage: Float,downloadSize: String?) {
                                if (name.equals("completed", ignoreCase = true)){
                                    ActivityLauncher(context).launchOfflinePlayer(currentVideoItem.entryId)
                                }else if(name.equals("failed", ignoreCase = true)){
                                    failedDownloadPopUpMenu(v!!, currentVideoItem, position)
                                }
                            }
                        })
                }
            }

        }catch (exception : Exception){
            Log.d("imageURL",exception.toString())
        }

    }

    private fun accuranceOfEpisodes(occuranceList: java.util.ArrayList<String>?,seriesId:String) : Int{
        val occurrences = Collections.frequency(occuranceList, seriesId)
        return occurrences
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
                        itemBinding?.descriptionTxt?.text = DownloadStatus.PAUSED.name
                    }
                    else if (name.equals("started", ignoreCase = true)){
                        // itemBinding?.flDeleteWatchlist?.visibility = View.VISIBLE
                        // itemBinding?.videoDownloading?.visibility = View.VISIBLE
                        itemBinding?.downloadStatus = DownloadStatus.DOWNLOADING
                        itemBinding?.videoDownloading?.progress = percentage
                        itemBinding?.descriptionTxt?.text = DownloadStatus.DOWNLOADING.name
                        // itemBinding?.videoDownloaded?.visibility = View.GONE
                        // itemBinding?.pauseDownload?.visibility = View.GONE
                        // itemBinding?.loadingDownload?.visibility = View.GONE
                    }
                    else if (name.equals("completed", ignoreCase = true)){
                        itemBinding?.downloadStatus = DownloadStatus.DOWNLOADED
                        itemBinding?.descriptionTxt?.text = downloadSize
                        itemBinding?.tvGenre?.visibility = View.VISIBLE
                        itemBinding?.tvGenre?.text = DownloadStatus.Completed.name

                        itemBinding?.tvGenre?.setTextColor(
                            context.resources.getColor(R.color.more_text_color_dark)
                        )
                        itemBinding?.descriptionTxt?.setTextColor(
                            context.resources.getColor(R.color.more_text_color_dark)
                        )
                    }else{

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
                    //notifyAdapterByPosition(position)
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
                var entity=itemList.get(index)
                if (entity.entryId.equals(assetId,ignoreCase = true)){
                    Log.w("sameid",itemList.get(index).entryId)
                    Log.w("sameid",index.toString())
                    itemList[index] = value
                    notifyItemChanged(index,PAY)
                    break
                }
            }
        }
    }

    private fun notifyItemWhenResumed(assetId: String?) {
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