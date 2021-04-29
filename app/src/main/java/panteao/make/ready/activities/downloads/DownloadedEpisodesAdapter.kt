package panteao.make.ready.activities.downloads

import android.app.Activity
import android.content.Intent
import android.os.Parcelable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.PopupMenu
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.brightcove.player.edge.OfflineCallback
import com.brightcove.player.model.Video
import com.brightcove.player.network.DownloadStatus
import com.brightcove.player.offline.MediaDownloadable
import com.mmtv.utils.helpers.downloads.DownloadHelper
import panteao.make.ready.R
import panteao.make.ready.databinding.ListDownloadItemBinding
import panteao.make.ready.utils.commonMethods.AppCommonMethod
import panteao.make.ready.utils.helpers.downloads.DownloadedVideoActivity
import panteao.make.ready.utils.helpers.downloads.room.DownloadedEpisodes
import panteao.make.ready.utils.cropImage.helpers.Logger
import panteao.make.ready.utils.helpers.downloads.VideoListListener
import java.io.Serializable
import java.text.DecimalFormat
import java.util.HashMap
import kotlin.math.log10
import kotlin.math.pow

class DownloadedEpisodesAdapter : RecyclerView.Adapter<RecyclerView.ViewHolder>, MediaDownloadable.DownloadEventListener, VideoListListener {
    private fun deleteVideo(view: View, downloadedVideo: DownloadedEpisodes, position: Int) {
        val popup = PopupMenu(context, view)
        popup.setOnMenuItemClickListener { item ->
            when (item.itemId) {
                R.id.delete_download -> {
                    downloadHelper.deleteVideo(downloadedVideo.videoId)
                    downloadedVideos.remove(downloadedVideo)
                    notifyItemRemoved(position)
                    notifyItemRangeChanged(position, downloadedVideos.size)
                    buildIndexMap()
                    if (downloadedVideos.size>0){

                    }else{
                        AppCommonMethod.isDownloadDeleted=true
                        if (index>-1){
                            AppCommonMethod.isDownloadIndex=index
                        }
                        context!!.onBackPressed()
                    }
                }
                R.id.my_Download -> {

                }
            }
            false
        }
        popup.inflate(R.menu.my_downloads_menu)
        popup.show()
    }

    private fun showPauseCancelPopUpMenu(view: View, video: DownloadedEpisodes, position: Int) {
        val popup = PopupMenu(context, view)
        popup.setOnMenuItemClickListener { item ->
            when (item.itemId) {
                R.id.cancel_download -> {
                    downloadHelper.cancelVideo(video.videoId)
                    downloadedVideos.remove(video)
                    notifyItemRemoved(position)
                    notifyItemRangeChanged(position, downloadedVideos.size)
                    buildIndexMap()
                    if (downloadedVideos.size>0){

                    }else{
                        AppCommonMethod.isDownloadDeleted=true
                        if (index>-1){
                            AppCommonMethod.isDownloadIndex=index
                        }
                        context!!.onBackPressed()
                    }
                }
                R.id.pause_download -> {
                    downloadHelper.pauseVideo(video.videoId)
                    buildIndexMap()
                }//downloadHelper.pauseVideo(video.videoId)
            }
            false
        }
        popup.inflate(R.menu.download_menu)
        popup.show()
    }

    private lateinit var video: Video
    private var viewHolder: LandscapeItemRowHolder? = null
    private val TAG = this.javaClass.simpleName
    private var context: Activity
    private val downloadedVideos = ArrayList<DownloadedEpisodes>()
    private val indexMap = HashMap<String, Int>()
    private val downloadHelper: DownloadHelper
    private var downloadStatus: Int=0
    private var index: Int=-1

    constructor(activity: Activity, downloadedEpisodes: ArrayList<DownloadedEpisodes>,inde : Int) {
        context = activity
        index=inde
        downloadHelper = DownloadHelper(activity, this)
        downloadedEpisodes.forEachIndexed { index, downloadedEpisode ->
            downloadHelper.findOfflineVideoById(downloadedEpisode.videoId, object : OfflineCallback<Video> {
                override fun onSuccess(video: Video) {
                    if (!video.isClearContent) {
                        if (video.licenseExpiryDate!!.time >= System.currentTimeMillis()) {
                            Logger.e("License", "Expiry" + video.licenseExpiryDate)
                            downloadedVideos.add(downloadedEpisode)
                            if (index == downloadedEpisodes.size - 1) {
                                buildIndexMap()
                            }
                        } else {
                            downloadHelper.deleteVideo(video)
                            if (index == downloadedEpisodes.size - 1) {
                                buildIndexMap()
                            }
                        }
                    } else {
                        downloadedVideos.add(downloadedEpisode)
                        if (index == downloadedEpisodes.size - 1) {
                            buildIndexMap()
                        }
                    }
                }

                override fun onFailure(p0: Throwable?) {
                    downloadHelper.deleteVideo(downloadedEpisode.videoId)
                    if (index == downloadedEpisodes.size - 1) {
                        buildIndexMap()
                    }
                }
            })
        }
    }

    private fun buildIndexMap() {
        indexMap.clear()
        if (downloadedVideos != null) {
            var index = 0
            for (video in downloadedVideos) {
                indexMap[video.videoId] = index++
            }
        }
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val listLdsItemBinding: ListDownloadItemBinding = DataBindingUtil.inflate(
                LayoutInflater.from(viewGroup.context),
                R.layout.list_download_item, viewGroup, false)
        viewHolder = LandscapeItemRowHolder(listLdsItemBinding)
        return viewHolder as LandscapeItemRowHolder
    }

    override fun getItemCount(): Int {
        return downloadedVideos.size
    }

    override fun onBindViewHolder(viewHolder: RecyclerView.ViewHolder, position: Int) {
        setLandscapeData(viewHolder as LandscapeItemRowHolder, position)
    }

    private fun setLandscapeData(viewHolder: LandscapeItemRowHolder, position: Int) {
        val currentVideoItem = downloadedVideos[position]
        downloadHelper.getDownloadStatus(currentVideoItem.videoId)
        downloadHelper.findOfflineVideoById(currentVideoItem.videoId, object : OfflineCallback<Video> {
            override fun onSuccess(p0: Video?) {
                viewHolder.itemBinding.root.tag = position
                viewHolder.itemBinding.videoItem = p0
                viewHolder.itemBinding.tvTitle.text = p0?.name
            }

            override fun onFailure(p0: Throwable?) {
            }
        })
//        if (currentVideoItem.downloadType == AppConstants.ContentType.SERIES.name) {
//            viewHolder.itemBinding.flDeleteWatchlist.visibility = View.GONE
//        }
        updateDownloadStatus(viewHolder, currentVideoItem, position)
        viewHolder.itemBinding.videoDownloaded.setOnClickListener { v -> deleteVideo(v!!, currentVideoItem, position) }
        viewHolder.itemBinding.videoDownloading.setOnClickListener { v ->
            showPauseCancelPopUpMenu(v!!, currentVideoItem, position)
        }
        viewHolder.itemBinding.pauseDownload.setOnClickListener {
            viewHolder.itemBinding.downloadStatus = panteao.make.ready.enums.DownloadStatus.REQUESTED
           // viewHolder.itemBinding.
            downloadHelper.resumeDownload(currentVideoItem.videoId)
        }
        viewHolder.itemBinding.root.setOnClickListener {
            val downloadedVideoIntent = Intent(context, DownloadedVideoActivity::class.java)
            downloadHelper.findOfflineVideoById(currentVideoItem.videoId, object : OfflineCallback<Video> {
                override fun onSuccess(p0: Video?) {
                    val status=getDownloadStatus(currentVideoItem)
                    if (status==1){
                        Logger.e("isParcelable", (p0 is Parcelable).toString())
                        downloadedVideoIntent.putExtra("DownloadedVideoId", p0 as Parcelable)
                        context.startActivity(downloadedVideoIntent)
                    }

                }

                override fun onFailure(p0: Throwable?) {
                }
            })
        }
    }

    private fun getDownloadStatus(p0: DownloadedEpisodes): Any {
        downloadHelper.getCatalog().getVideoDownloadStatus(p0.videoId,
                object : OfflineCallback<DownloadStatus> {
                    override fun onSuccess(p0: DownloadStatus?) {
                        Logger.e("DownloadStatus", p0?.code.toString());
                        when (p0?.code) {
                            DownloadStatus.STATUS_COMPLETE -> {
                                downloadStatus=1;
                            }
                            DownloadStatus.STATUS_DOWNLOADING -> {
                                downloadStatus=0;
                            }
                            DownloadStatus.STATUS_PAUSED -> {
                                downloadStatus=0;
                            }
                            DownloadStatus.STATUS_NOT_QUEUED->{
                                downloadStatus=0;
                            }
                        }
                    }

                    override fun onFailure(p0: Throwable?) {
                    }
                })
         return downloadStatus
    }

    private fun updateDownloadStatus(viewHolder: LandscapeItemRowHolder, currentVideoItem: DownloadedEpisodes, position: Int) {
        downloadHelper.getCatalog().getVideoDownloadStatus(currentVideoItem.videoId,
                object : OfflineCallback<DownloadStatus> {
                    override fun onSuccess(p0: DownloadStatus?) {
                        Logger.e("DownloadStatus", p0?.code.toString());
                        when (p0?.code) {
                            DownloadStatus.STATUS_COMPLETE -> {
                                viewHolder.itemBinding.downloadStatus = panteao.make.ready.enums.DownloadStatus.DOWNLOADED
                                viewHolder.itemBinding.descriptionTxt.text = getFileSize(p0.actualSize)
                            }
                            DownloadStatus.STATUS_DOWNLOADING -> {
                                viewHolder.itemBinding.downloadStatus = panteao.make.ready.enums.DownloadStatus.DOWNLOADING
                                viewHolder.itemBinding.videoDownloading.progress = p0.progress.toFloat()
                                viewHolder.itemBinding.descriptionTxt.text = context.getString(R.string.Downloading)
                            }
                            DownloadStatus.STATUS_PAUSED -> {
                                viewHolder.itemBinding.downloadStatus = panteao.make.ready.enums.DownloadStatus.PAUSE
                                viewHolder.itemBinding.descriptionTxt.text = context.getString(R.string.Paused)
                            }
                            DownloadStatus.STATUS_PENDING -> {
                                viewHolder.itemBinding.downloadStatus = panteao.make.ready.enums.DownloadStatus.DOWNLOADING
                                viewHolder.itemBinding.descriptionTxt.text = context.getString(R.string.Downloading)
                            }
                            DownloadStatus.PAUSED_WAITING_TO_RETRY -> {
                                viewHolder.itemBinding.downloadStatus = panteao.make.ready.enums.DownloadStatus.DOWNLOADING
                                viewHolder.itemBinding.descriptionTxt.text = context.getString(R.string.Downloading)
                            }
                            DownloadStatus.STATUS_NOT_QUEUED->{
                                downloadedVideos.remove(currentVideoItem)
                                notifyItemRemoved(position)
                                notifyItemRangeChanged(position, downloadedVideos.size)
                                buildIndexMap()
                            }
                        }
                    }

                    override fun onFailure(p0: Throwable?) {
                    }
                })
    }

    fun getFileSize(size: Long): String {
        if (size <= 0)
            return "0"

        val units = arrayOf("B", "KB", "MB", "GB", "TB")
        val digitGroups = (log10(size.toDouble()) / log10(1024.0)).toInt()

        return DecimalFormat("#,##0.#").format(size / 1024.0.pow(digitGroups.toDouble())) + " " + units[digitGroups]
    }

    internal fun notifyVideoChanged(videoId: String, status: DownloadStatus) {
        if (indexMap.containsKey(videoId)) {
            val index = indexMap[videoId]!!
            for (video in downloadedVideos) {
                if (videoId == video.videoId) {
                    downloadedVideos[index] = video
                    notifyItemChanged(index, status)
                }
            }
        }
    }

    override fun onDownloadStarted(p0: Video, p1: Long, p2: MutableMap<String, Serializable>) {
    }

    override fun onDownloadPaused(p0: Video, p1: DownloadStatus) {
    }

    override fun onDownloadFailed(p0: Video, p1: DownloadStatus) {
    }

    override fun onDownloadRequested(p0: Video) {
    }

    override fun onDownloadCanceled(p0: Video) {
        Logger.e("DownloadCancelled", "True")
    }

    override fun onDownloadDeleted(p0: Video) {

    }

    override fun onDownloadCompleted(p0: Video, p1: DownloadStatus) {
        notifyVideoChanged(p0.id, p1)
    }

    override fun onDownloadProgress(p0: Video, p1: DownloadStatus) {
        Logger.e(TAG, "onDownloadProgress" + p1.progress)
        notifyVideoChanged(p0.id, p1)
    }

    override fun downloadVideo(video: Video) {

    }

    override fun pauseVideoDownload(video: Video) {
    }

    override fun resumeVideoDownload(video: Video) {
    }

    override fun deleteVideo(video: Video) {
    }

    override fun alreadyDownloaded(video: Video) {
    }

    override fun downloadedVideos(p0: MutableList<out Video>?) {
    }

    override fun videoFound(video: Video?) {

    }

    override fun downloadStatus(videoId: String, downloadStatus: DownloadStatus?) {
        Logger.e(TAG, "downloadStatus" + downloadStatus?.progress)
    }

    inner class LandscapeItemRowHolder(internal val itemBinding: ListDownloadItemBinding) : RecyclerView.ViewHolder(itemBinding.root)

}
