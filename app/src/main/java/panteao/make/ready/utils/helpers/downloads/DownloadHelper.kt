package com.mmtv.utils.helpers.downloads

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.os.AsyncTask
import android.os.Bundle
import android.os.Environment.getExternalStorageDirectory
import android.os.StatFs
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.MutableLiveData
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.room.Room
import com.brightcove.player.edge.OfflineCallback
import com.brightcove.player.edge.OfflineCatalog
import com.brightcove.player.edge.VideoListener
import com.brightcove.player.event.Event
import com.brightcove.player.event.EventEmitter
import com.brightcove.player.event.EventListener
import com.brightcove.player.event.EventType
import com.brightcove.player.model.MediaFormat
import com.brightcove.player.model.Video
import com.brightcove.player.network.DownloadStatus
import com.brightcove.player.network.HttpRequestConfig
import com.brightcove.player.offline.MediaDownloadable
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.gson.Gson
import panteao.make.ready.R
import panteao.make.ready.SDKConfig
import panteao.make.ready.callbacks.commonCallbacks.CommonApiCallBack
import panteao.make.ready.databinding.LayoutDownloadQualityBottomSheetBinding
import panteao.make.ready.databinding.WifiDialogBinding
import panteao.make.ready.utils.MediaTypeConstants
import panteao.make.ready.utils.commonMethods.AppCommonMethod
import panteao.make.ready.utils.constants.SharedPrefesConstants
import panteao.make.ready.utils.cropImage.helpers.Logger
import panteao.make.ready.utils.helpers.downloads.BrightcoveDownloadUtil
import panteao.make.ready.utils.helpers.downloads.ImageDownloadHelper
import panteao.make.ready.utils.helpers.downloads.room.DownloadDatabase
import panteao.make.ready.utils.helpers.downloads.room.DownloadModel
import panteao.make.ready.utils.helpers.downloads.room.DownloadedEpisodes
import panteao.make.ready.utils.helpers.downloads.room.DownloadedVideo
import panteao.make.ready.MvHubPlusApplication
import panteao.make.ready.activities.downloads.SelectDownloadQualityAdapter
import panteao.make.ready.activities.downloads.VideoQualitySelectedListener
import panteao.make.ready.activities.downloads.WifiPreferenceListener
import panteao.make.ready.beanModelV3.uiConnectorModelV2.EnveuVideoItemBean
import panteao.make.ready.utils.helpers.SharedPrefHelper
import panteao.make.ready.utils.helpers.downloads.VideoListListener
import panteao.make.ready.utils.helpers.ksPreferenceKeys.KsPreferenceKeys
import java.text.DecimalFormat
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList

class DownloadHelper() {
    private lateinit var httpRequestConfig: HttpRequestConfig
    private var video: Video? = null
    private var TAG = this.javaClass.simpleName
    private lateinit var catalog: OfflineCatalog
    private lateinit var videoListListener: VideoListListener
    private lateinit var activity: Activity
    private lateinit var videoListener: MediaDownloadable.DownloadEventListener
    private lateinit var db: DownloadDatabase
    private var assetType = "VIDEO"
    private lateinit var enveuVideoItemBean: EnveuVideoItemBean
    private var seriesId = ""
    private var seriesName = ""
    private var seasonNumber: String? = null
    private var episodeNumber: String? = null
    private var mobileDownloadAllwed: Int? = 1
    fun setAssetType(assetType: String) {
        this.assetType = assetType
    }

    fun setSeriesName(seriesName: String) {
        this.seriesName = seriesName
    }

    constructor(activity: Activity) : this() {
        this.activity = activity
        db = Room.databaseBuilder(
                MvHubPlusApplication.getApplicationContext(activity),
                DownloadDatabase::class.java, "enveu.db").build()
        init(activity)
    }

    constructor(activity: Activity, videoListener: MediaDownloadable.DownloadEventListener) : this() {
        this.videoListListener = videoListener as VideoListListener
        this.videoListener = videoListener
        this.activity = activity
        db = Room.databaseBuilder(
                MvHubPlusApplication.getApplicationContext(activity),
                DownloadDatabase::class.java, "enveu.db").build()
        init(activity)
    }

    constructor(activity: Activity, videoListener: MediaDownloadable.DownloadEventListener, assetType: String) : this(activity, videoListener) {
        this.assetType = assetType
        this.videoListener = videoListener
        this.activity = activity
    }

    constructor(activity: Activity, videoListener: MediaDownloadable.DownloadEventListener, seriesId: String, seriesName: String, assetType: String, enveuVideoItemBean: EnveuVideoItemBean) : this(activity, videoListener) {
        this.assetType = assetType
        this.enveuVideoItemBean = enveuVideoItemBean
        this.seriesId = seriesId
        this.seriesName = seriesName
        this.seasonNumber = enveuVideoItemBean.season
        if (enveuVideoItemBean.episodeNo != null)
            this.episodeNumber = enveuVideoItemBean.episodeNo.toString()
    }

    private fun init(activity: Activity) {
        catalog = OfflineCatalog(activity, object : EventEmitter {
            override fun on(p0: String?, p1: EventListener?): Int {
                return 0
            }

            override fun once(p0: String?, p1: EventListener?): Int {
                return 0//To change body of created functions use File | Settings | File Templates.
            }

            override fun enable() {
            }

            override fun off() {
            }

            override fun off(p0: String?, p1: Int) {
            }

            override fun respond(p0: MutableMap<String, Any>?) {
            }

            override fun respond(p0: Event?) {
            }

            override fun request(p0: String?, p1: EventListener?) {
            }

            override fun request(p0: String?, p1: MutableMap<String, Any>?, p2: EventListener?) {
            }

            override fun disable() {
            }

            override fun emit(p0: String?) {
            }

            override fun emit(p0: String?, p1: MutableMap<String, Any>?) {
            }

        }, ACCOUNT_ID, POLICY_KEY)
        if (::videoListener.isInitialized) {
            catalog.addDownloadEventListener(videoListener)
            allowedMobileDownload()
            getAllVideosFromDatabase()
        }
    }

    fun allowedMobileDownload() {
        mobileDownloadAllwed = KsPreferenceKeys.getInstance().downloadOverWifi
        catalog.isMobileDownloadAllowed = mobileDownloadAllwed==0
    }

    fun findVideo(videoId: String): Video? {
        Logger.e(TAG, "VideoId$videoId")
        catalog.findVideoByID(videoId, object : VideoListener() {
            override fun onVideo(video: Video) {
                this@DownloadHelper.video = video
                checkDownloadStatus()
                videoListListener.videoFound(video)
            }

            override fun onError(error: String) {
                Logger.e(TAG, error)
            }
        })
        return video
    }

    fun findVideo(videoId: String, videoListener: VideoListener) {
        catalog.findVideoByID(videoId, object : VideoListener() {
            override fun onVideo(video: Video) {
                videoListener.onVideo(video)
            }

            override fun onError(error: String) {
                videoListener.onError(error)
            }
        })
    }

    fun findOfflineVideoById(videoId: String, videoListener: OfflineCallback<Video>) {
        catalog.findOfflineVideoById(videoId, object : OfflineCallback<Video> {
            override fun onSuccess(p0: Video?) {
                videoListener.onSuccess(p0)
            }

            override fun onFailure(p0: Throwable?) {
                videoListener.onFailure(p0)
            }

        })
    }

    companion object {
        private const val BEST_BIT_RATE = 2000000
        private const val BETTER_BIT_RATE = 1500000
        private const val GOOD_BIT_RATE = 1000000
        private const val DATA_SAVER_BIT_RATE = 500000
        private const val MEGABYTE_IN_BYTES = (1024 * 1024).toLong()
        private const val BUFFER_MB = 500
      //  private const val ACCOUNT_ID = "5854923532001"
       // private const val POLICY_KEY = "BCpkADawqM1eQgw1AYFQOUXoNSPw_rzWhyBPlA-s-FXA5HLM5PxfY7B5JA-fgES-HUj3a4WafkLDIiQYmRZMrpe_oOfuY_KGj1EGRz9e-v7a8LI6sKhZmd3s3CAY1VlLNP9eCQz1OAMHAfUi"
        private const val ACCOUNT_ID = "6093072334001"
        private const val POLICY_KEY = "BCpkADawqM116o-lW2kfupCrzeL-MF0Sf6we0Gvw2Ecdt7QkXNJl09dNhCvYIx-S83OJOSDMzDgv6gS3vSVGbK4Ctp9PuotvCcPHmApyXjR_8FcX5-aNMjJKfnYPGCKr0uUI7qNc0kbDnOC9";
    }

    fun pauseAllVideos() {
        val videos = ArrayList<Video>()
        videos.addAll(catalog.findAllVideoDownload(DownloadStatus.STATUS_PENDING))
        videos.addAll(catalog.findAllVideoDownload(DownloadStatus.STATUS_DOWNLOADING))
        videos.addAll(catalog.findAllQueuedVideoDownload())
        for (video in videos) {
            catalog.findVideoByID(video?.id, object : VideoListener() {
                override fun onVideo(p0: Video?) {
                    p0?.let {
                        pauseVideo(it.id)
                    }
                }
            })
        }
    }

    @SuppressLint("WrongConstant")
    fun deleteAllVideos(activity: Activity) {
        try {
            if (!::catalog.isInitialized) {
                init(activity)
            }
            if (!::db.isInitialized) {
                db = Room.databaseBuilder(
                        MvHubPlusApplication.getApplicationContext(activity),
                        DownloadDatabase::class.java, "enveu.db").build()
            }
            val videosList = ArrayList<Video>()
            videosList.addAll(catalog.findAllVideoDownload(DownloadStatus.STATUS_COMPLETE))
            videosList.addAll(catalog.findAllVideoDownload(DownloadStatus.STATUS_PENDING))
            videosList.addAll(catalog.findAllVideoDownload(DownloadStatus.STATUS_PAUSED))
            videosList.addAll(catalog.findAllVideoDownload(DownloadStatus.STATUS_DOWNLOADING))
            videosList.addAll(catalog.findAllVideoDownload(DownloadStatus.PAUSED_WAITING_TO_RETRY))
            videosList.addAll(catalog.findAllQueuedVideoDownload())

            for (video in videosList) {
                catalog.findVideoByID(video.id, object : VideoListener() {
                    override fun onVideo(p0: Video?) {
                        p0?.let {
                            deleteVideo(it)
                        }
                    }
                })
            }
            deleteAllVideosFromDatabase()
        }catch (ignored : Exception){

        }
    }

    fun startVideoDownload(video: Video, videoQuality: Int) {
        if (video.isClearContent) {
            downloadVideo(video, videoQuality)
        } else {
            acquireLicense(video, object : CommonApiCallBack {
                override fun onSuccess(item: Any?) {
                    downloadVideo(item as Video, videoQuality)
                }

                override fun onFailure(throwable: Throwable?) {
                    Logger.e(TAG, throwable?.message)
                }

                override fun onFinish() {
                }
            })
        }
    }

    fun startEpisodeDownload(video: Video, seriesId: String, seasonNumber: Int, episodeNumber: String, videoQuality: Int) {
        assetType = MediaTypeConstants.getInstance().episode
        this.seriesId = seriesId
        this.seasonNumber = seasonNumber.toString()
        this.episodeNumber = episodeNumber
        if (video.isClearContent) {
            downloadVideo(video, videoQuality)
        } else {
            acquireLicense(video, object : CommonApiCallBack {
                override fun onSuccess(item: Any?) {
                    downloadVideo(item as Video, videoQuality)
                }

                override fun onFailure(throwable: Throwable?) {
                   Logger.e(TAG, throwable?.message)
                }

                override fun onFinish() {
                }
            })
        }
    }

    @SuppressLint("SimpleDateFormat")
    private fun acquireLicense(video: Video, param: CommonApiCallBack) {
        var playDuration = (video.duration.plus(10000)).toLong()
        Logger.e(TAG, playDuration.toString())
        val sdf = SimpleDateFormat("dd/M/yyyy HH:mm:ss")
        val currentDate = sdf.format(Date())
        val calender = Calendar.getInstance()
        calender.time = sdf.parse(currentDate)
        calender.add(Calendar.DATE, 3)
        val httpRequestConfigBuilder = HttpRequestConfig.Builder()
        httpRequestConfigBuilder.setBrightcoveAuthorizationToken("AEnTxTh1tfn_PqonTvZXtqKaLZbZeAM3wLlN2OIPcKXOYKFhlHISivLU98g0R5fOoMddEIzPnC9bm9BK7X27H3nNcJHDtMgCMw2fwJpyWxxwfvzJhTd3Npk")
        httpRequestConfig = httpRequestConfigBuilder.build()
        catalog.requestRentalLicense(video, calender.time, calender.timeInMillis, { event ->
            when (event.type) {
                EventType.ODRM_LICENSE_ACQUIRED -> {
                    Logger.i(TAG, "ODRM_LICENSE_ACQUIRED" + Gson().toJson(event))
                    param.onSuccess(event.properties[Event.VIDEO] as Video)
                }
                EventType.ODRM_PLAYBACK_NOT_ALLOWED, EventType.ODRM_SOURCE_NOT_FOUND -> {
                    Logger.w(TAG, "ODRM_PLAYBACK_NOT_ALLOWED")
                    param.onFailure(Throwable("ODRM_PLAYBACK_NOT_ALLOWED"))
                }
                EventType.ODRM_LICENSE_ERROR -> {
                    Logger.e(TAG, "ODRM_LICENSE_ERROR" + event.properties[Event.ERROR] as Throwable?)
                    param.onFailure(Throwable("ODRM_LICENSE_ERROR"))
                }
            }
        }, httpRequestConfig)
    }

    fun checkDownloadStatus() {
        video?.let {
            catalog.getVideoDownloadStatus(it, object : OfflineCallback<DownloadStatus> {
                override fun onSuccess(downloadStatus: DownloadStatus?) {
                    updateDownloadStatus(downloadStatus)

                }

                override fun onFailure(throwable: Throwable?) {
                    Logger.e(TAG, throwable?.message)
                }
            })
        }
    }

    fun checkDownloadStatus(video: Video) {
        video.let {
            catalog.getVideoDownloadStatus(it, object : OfflineCallback<DownloadStatus> {
                override fun onSuccess(downloadStatus: DownloadStatus?) {
                    updateDownloadStatus(downloadStatus)

                }

                override fun onFailure(throwable: Throwable?) {
                    Logger.e(TAG, throwable?.message)
                }
            })
        }
    }

    fun getDownloadStatus(videoId: String) {
        catalog.getVideoDownloadStatus(videoId, object : OfflineCallback<DownloadStatus> {
            override fun onSuccess(downloadStatus: DownloadStatus?) {
                updateDownloadStatus(downloadStatus)
            }

            override fun onFailure(throwable: Throwable?) {
                Logger.e(TAG, throwable?.message)
            }
        })
    }

    fun getDownloadStatus(videoId: String, callback: OfflineCallback<DownloadStatus>) {
        catalog.getVideoDownloadStatus(videoId, object : OfflineCallback<DownloadStatus> {
            override fun onSuccess(downloadStatus: DownloadStatus?) {
//                updateDownloadStatus(downloadStatus)
                callback.onSuccess(downloadStatus)
            }

            override fun onFailure(throwable: Throwable?) {
                Logger.e(TAG, throwable?.message)
                callback.onFailure(throwable)
            }
        })
    }

    private fun updateDownloadStatus(downloadStatus: DownloadStatus?) {
        Logger.e(TAG, downloadStatus?.code.toString())
        video?.let {
            updateVideoStatus(downloadStatus!!.code, it.id)
            videoListListener.downloadStatus(it.id, downloadStatus)

            when (downloadStatus.code) {
                DownloadStatus.STATUS_COMPLETE -> {
                    video?.let {
                        videoListListener.alreadyDownloaded(it)
                    }
                }
                DownloadStatus.STATUS_PAUSED -> {
                    videoListListener.resumeVideoDownload(it)
                }
                DownloadStatus.STATUS_DOWNLOADING -> {
//                cancelVideo(videoId)
                }
                DownloadStatus.STATUS_DELETING -> {

                }
                DownloadStatus.STATUS_CANCELLING -> {

                }
                DownloadStatus.STATUS_QUEUEING -> {
                }
                DownloadStatus.STATUS_PENDING -> {
                    resumeDownload(it.id)
                }
                DownloadStatus.PAUSED_WAITING_TO_RETRY,
                DownloadStatus.PAUSED_WAITING_FOR_NETWORK -> {
                    videoListListener.resumeVideoDownload(it)
                }
                else -> {

                }
            }
        }

    }

    fun deleteVideo(video: Video) {
        catalog.deleteVideo(video, object : OfflineCallback<Boolean> {
            override fun onSuccess(aBoolean: Boolean?) {
                Logger.e(TAG, aBoolean!!.toString())
                if (aBoolean!!) {
                    checkDownloadStatus()
                    deleteVideoFromDatabase(video.id)
                }else{
                    deleteVideoFromDatabase(video.id)
                }
            }
            override fun onFailure(throwable: Throwable) {
                deleteVideoFromDatabase(video.id)
            }
        })
    }

    fun deleteVideo(videoId: String) {
        catalog.deleteVideo(videoId, object : OfflineCallback<Boolean> {
            override fun onSuccess(aBoolean: Boolean?) {
                if (aBoolean!!) {
                    checkDownloadStatus()
                    deleteVideoFromDatabase(videoId)
                }else{
                    deleteVideoFromDatabase(videoId)
                }
            }

            override fun onFailure(throwable: Throwable) {
                deleteVideoFromDatabase(videoId)
            }
        })
    }

    private fun downloadVideo(video: Video, videoQuality: Int) {
       // addVideotoDatabase(video, seriesId)

        when (videoQuality) {
            0 -> {
                Logger.e("Download", BEST_BIT_RATE.toString())
                catalog.setVideoBitrate(BEST_BIT_RATE)
            }
            1 -> {
                Logger.e("Download", BETTER_BIT_RATE.toString())
                catalog.setVideoBitrate(BETTER_BIT_RATE)
            }
            2 -> {
                Logger.e("Download", GOOD_BIT_RATE.toString())
                catalog.setVideoBitrate(GOOD_BIT_RATE)
            }
            3 -> {
                Logger.e("Download", DATA_SAVER_BIT_RATE.toString())
                catalog.setVideoBitrate(DATA_SAVER_BIT_RATE)
            }
        }
        if (assetType != MediaTypeConstants.getInstance().series) {
            catalog.getMediaFormatTracksAvailable(video) { mediaDownloadable, bundle ->
                BrightcoveDownloadUtil.selectMediaFormatTracksAvailable(mediaDownloadable, bundle)
                when (videoQuality) {
                    0 -> {
                        Logger.e("Download", BEST_BIT_RATE.toString())
                        mediaDownloadable.setVideoBitrate(BEST_BIT_RATE)
                    }
                    1 -> {
                        Logger.e("Download", BETTER_BIT_RATE.toString())
                        mediaDownloadable.setVideoBitrate(BETTER_BIT_RATE)
                    }
                    2 -> {
                        Logger.e("Download", GOOD_BIT_RATE.toString())
                        mediaDownloadable.setVideoBitrate(GOOD_BIT_RATE)
                    }
                    3 -> {
                        Logger.e("Download", DATA_SAVER_BIT_RATE.toString())
                        mediaDownloadable.setVideoBitrate(DATA_SAVER_BIT_RATE)
                    }
                }
                val videos = ArrayList<MediaFormat>()
                val audios = ArrayList<MediaFormat>()
                val roles = ArrayList<String>()
                val captions = ArrayList<MediaFormat>()
                if (bundle.containsKey(MediaDownloadable.VIDEO_RENDITIONS)) {
                    videos.addAll(bundle.getParcelableArrayList(MediaDownloadable.VIDEO_RENDITIONS)!!)
                }
                if (bundle.containsKey(MediaDownloadable.AUDIO_LANGUAGES)) {
                    audios.addAll(bundle.getParcelableArrayList(MediaDownloadable.AUDIO_LANGUAGES)!!)
                }
                if(bundle.containsKey(MediaDownloadable.AUDIO_LANGUAGE_ROLES)) {
                    roles.addAll(bundle.getStringArrayList(MediaDownloadable.AUDIO_LANGUAGE_ROLES)!!)
                }
                if(bundle.containsKey(MediaDownloadable.CAPTIONS)) {
                    captions.addAll(bundle.getParcelableArrayList(MediaDownloadable.CAPTIONS)!!)
                }
                val filteredBundle = Bundle()
                filteredBundle.putParcelableArrayList(MediaDownloadable.VIDEO_RENDITIONS,videos)
                filteredBundle.putParcelableArrayList(MediaDownloadable.AUDIO_LANGUAGES,audios)
                filteredBundle.putStringArrayList(MediaDownloadable.AUDIO_LANGUAGE_ROLES,roles)
                filteredBundle.putParcelableArrayList(MediaDownloadable.CAPTIONS,captions)
                mediaDownloadable.configurationBundle = filteredBundle
                Logger.e("Bundle", Gson().toJson(filteredBundle))
                catalog.downloadVideo(video, object : OfflineCallback<DownloadStatus> {
                    override fun onSuccess(downloadStatus: DownloadStatus) {
                        addVideotoDatabase(video, seriesId)
                        updateDownloadStatus(downloadStatus)
                    }

                    override fun onFailure(throwable: Throwable) {
                        Logger.e(TAG, "onFailure====>" + throwable.message)
                    }
                })
            }
        }
    }


    private fun addVideotoDatabase(video: Video, seriesId: String) {
        if (assetType == MediaTypeConstants.getInstance().series || assetType == MediaTypeConstants.getInstance().episode) {

            Logger.e("addVideoData 2", assetType)
            var downloadedVideo = DownloadedVideo(video!!.id, MediaTypeConstants.getInstance().series, seriesId)
            downloadedVideo.seriesName = seriesName
            downloadedVideo.seasonNumber = seasonNumber!!
            val downloadedEpisodes = DownloadedEpisodes(video.id, seasonNumber!!, episodeNumber!!, seriesId)
            ImageDownloadHelper(activity as Context, seriesId, object : CommonApiCallBack {
                override fun onSuccess(item: Any?) {
                    Logger.e("addVideoData 4", item.toString())
                    insertVideo(downloadedVideo, downloadedEpisodes)
                }

                override fun onFailure(throwable: Throwable?) {
                    Logger.e(TAG, "Unable to save image")
                    Logger.e("addVideoData 3", throwable!!.message)
                    insertVideo(downloadedVideo, downloadedEpisodes)

                }

                override fun onFinish() {
                }

            }).execute(video.posterImage.toString())
            /* findVideo(seriesId, object : VideoListener() {
                 override fun onVideo(p: Video?) {

                 }

                 override fun onError(error: String) {
                     super.onError(error)
                     Logger.e(TAG, error)
                 }
             })*/

        } else {
            var downloadedVideo = DownloadedVideo(video.id, assetType, seriesId, "", "", video.name, AppCommonMethod.expiryDate(SDKConfig.DOWNLOAD_EXPIRY_DAYS))
            insertVideo(downloadedVideo, null)
        }

        if (assetType == MediaTypeConstants.getInstance().episode || assetType == MediaTypeConstants.getInstance().series) {
            try {
                AsyncTask.execute {
                    val downloadedEpisodes = DownloadedEpisodes(video.id, seasonNumber!!, episodeNumber!!, seriesId)
                    db.downloadEpisodeDao().insertEpisodes(downloadedEpisodes)
                }
            } catch (ex: Exception) {
                Logger.e(TAG, ex.message)
            }
        }
    }

    private fun insertVideo(downloadedVideo: DownloadedVideo, downloadedEpisodes: DownloadedEpisodes?) {
        db = Room.databaseBuilder(
                MvHubPlusApplication.getApplicationContext(activity),
                DownloadDatabase::class.java, "enveu.db").build()
        try {
            AsyncTask.execute {
                db.downloadVideoDao().insertVideo(downloadedVideo)
                if (downloadedEpisodes != null)
                    db.downloadEpisodeDao().insertEpisodes(downloadedEpisodes)

            }
        } catch (ex: Exception) {
            Logger.e(TAG, ex.message)
        }
    }

    private fun deleteVideoFromDatabase(videoId: String) {
        Logger.e("DeleteVideo",videoId)
        try {
            AsyncTask.execute {
                db.downloadVideoDao().deleteVideo(videoId)
            }
            AsyncTask.execute {
                db.downloadEpisodeDao().deleteVideo(videoId)
            }
        } catch (ex: Exception) {
            Logger.e(TAG, ex.message)
        }
    }

    fun deleteAllVideosFromDatabase() {
        try {
            AsyncTask.execute {
                db.downloadVideoDao().deleteAllVideos()
                db.downloadEpisodeDao().deleteAllVideos()
            }
        } catch (ex: Exception) {
            Logger.e(TAG, ex.message)
        }
    }

    fun getAllVideosFromDatabase(): MutableLiveData<DownloadModel> {
        val mutableLiveData = MutableLiveData<DownloadModel>()
        val downloadTask = DownloadTask()
        downloadTask.delegate = object : AsyncResponse<DownloadModel> {
            override fun processFinish(result: DownloadModel) {
                mutableLiveData.value = result
            }
        }
        downloadTask.execute(db)
        return mutableLiveData
    }

    fun getAllEpisodesOfSeries(seriesId: String, seasonNumber: String): MutableLiveData<ArrayList<DownloadedEpisodes>> {
        val downloadTask = DownloadEpisodesTask(seriesId, seasonNumber)
        val mutableLiveData = MutableLiveData<ArrayList<DownloadedEpisodes>>()
        downloadTask.delegate = object : AsyncResponse<List<DownloadedEpisodes>> {
            override fun processFinish(result: List<DownloadedEpisodes>) {
                mutableLiveData.value = result as ArrayList<DownloadedEpisodes>
            }
        }
        downloadTask.execute(db)
        return mutableLiveData
    }

    fun getAllEpisodes(): MutableLiveData<ArrayList<DownloadedEpisodes>> {
        val downloadTask = DownloadEpisodesTask(null, null)
        val mutableLiveData = MutableLiveData<ArrayList<DownloadedEpisodes>>()
        downloadTask.delegate = object : AsyncResponse<List<DownloadedEpisodes>> {
            override fun processFinish(result: List<DownloadedEpisodes>) {
                mutableLiveData.value = result as ArrayList<DownloadedEpisodes>
            }
        }
        downloadTask.execute(db)
        return mutableLiveData
    }

    fun updateVideoStatus(status: Int, videoId: String) {
        AsyncTask.execute {
            db.downloadVideoDao().updateStatus(videoId, status)
        }
    }

    private fun getAvailableFreeSpace(): Long {
        val stat = StatFs(getExternalStorageDirectory().path)
        var bytesAvailable: Long
        bytesAvailable = stat.blockSizeLong * stat.availableBlocksLong
        return (bytesAvailable / (MEGABYTE_IN_BYTES))
    }

    private fun getFreeSpaceString(): String {
        val availableSize = getAvailableFreeSpace()
        if (availableSize < 1024) {
            return "$availableSize MB"
        } else {
            val size = (availableSize / 1024.0)
            val value = DecimalFormat("#.#")
            return value.format(size) + " GB"
        }
    }

    fun pauseVideo() {
        video?.id?.let {
            catalog.pauseVideoDownload(it, object : OfflineCallback<Int> {
                override fun onSuccess(p0: Int?) {
                    Logger.e(TAG, "pauseDownload$p0")
                }

                override fun onFailure(p0: Throwable?) {
                }
            })
        }
    }

    fun pauseVideo(videoId: String) {
        catalog.pauseVideoDownload(videoId, object : OfflineCallback<Int> {
            override fun onSuccess(p0: Int?) {
                Logger.e(TAG, "pauseDownload$p0")
            }

            override fun onFailure(p0: Throwable?) {
            }
        })
    }

    fun cancelVideo(videoId: String) {
        catalog.cancelVideoDownload(videoId, object : OfflineCallback<Boolean> {
            override fun onSuccess(cancelled: Boolean) {
                AsyncTask.execute {
                    db.downloadVideoDao().deleteVideo(videoId)
                    db.downloadEpisodeDao().deleteVideo(videoId)
                }
            }

            override fun onFailure(p0: Throwable?) {
            }
        })
    }

    fun resumeDownload(videoId: String) {
        findVideo(videoId, object : VideoListener() {
            override fun onVideo(p0: Video?) {
                if (p0?.isClearContent!!) {
                    videoDownloadResume(p0)
                } else {
                    acquireLicense(p0, object : CommonApiCallBack {
                        override fun onSuccess(item: Any?) {
                            videoDownloadResume(item as Video)
                        }

                        override fun onFailure(throwable: Throwable?) {
                        }

                        override fun onFinish() {
                        }
                    })
                }
            }
        })
    }

    fun videoDownloadResume(video: Video) {
        catalog.resumeVideoDownload(video, object : OfflineCallback<Int> {
            override fun onSuccess(p0: Int?) {
                videoListListener.pauseVideoDownload(video)
            }

            override fun onFailure(p0: Throwable?) {
            }
        })
    }

    fun getDownloadedVideos() {
        catalog.findAllVideoDownload(DownloadStatus.STATUS_COMPLETE, object : OfflineCallback<List<Video>> {
            override fun onSuccess(p0: List<Video>?) {
                Logger.e(TAG, Gson().toJson(p0?.get(1)))
                videoListListener.downloadedVideos(p0)
            }

            override fun onFailure(p0: Throwable?) {

            }
        })
    }

    fun getCatalog(): OfflineCatalog {
        return catalog
    }

    fun startSeriesDownload(seriesId: String, selectedSeason: Int, seasonEpisodes: MutableList<out EnveuVideoItemBean>, videoQuality: Int) {
        for (enveuVideoItem in seasonEpisodes) {
            enveuVideoItemBean = enveuVideoItem
            this.seriesId = seriesId
            this.seasonNumber = enveuVideoItem.season.toString()
            this.episodeNumber = enveuVideoItem.episodeNo.toString()
            findVideo(enveuVideoItem.brightcoveVideoId, object : VideoListener() {
                override fun onVideo(video: Video?) {
                    if (video?.isOfflinePlaybackAllowed!!) {
                        assetType = MediaTypeConstants.getInstance().episode
                        startVideoDownload(video, videoQuality)
                    }
                }
            })
        }
    }

    fun selectVideoQuality(videoQualitySelectedListener: VideoQualitySelectedListener) {
        val binding = DataBindingUtil.inflate<LayoutDownloadQualityBottomSheetBinding>(activity.layoutInflater, R.layout.layout_download_quality_bottom_sheet, null, false)
        val dialog = BottomSheetDialog(activity)
        dialog.setContentView(binding.root)
        binding.availableSpace.append(getFreeSpaceString())

        binding.recyclerView.hasFixedSize()
        binding.recyclerView.isNestedScrollingEnabled = false
        binding.recyclerView.layoutManager = LinearLayoutManager(activity, LinearLayoutManager.VERTICAL, false)
        val dividerItemDecoration = DividerItemDecoration(binding.recyclerView.context,
                (binding.recyclerView.layoutManager as LinearLayoutManager).orientation)
        binding.recyclerView.addItemDecoration(dividerItemDecoration)


        val downloadQualityList = ArrayList<String>()
        downloadQualityList.addAll(activity.resources.getStringArray(R.array.download_quality))
        downloadQualityList.removeAt(4)

        var selectedVideoQualityPosition = 0
        val changeDownloadQualityAdapter = SelectDownloadQualityAdapter(activity, downloadQualityList, object : VideoQualitySelectedListener {
            override fun videoQualitySelected(position: Int) {
                selectedVideoQualityPosition = position
            }
        })
        binding.btnStartDownload.setOnClickListener {
            if (binding.checkboxMakeDefault.isChecked) {
                SharedPrefHelper(activity).setInt(SharedPrefesConstants.DOWNLOAD_QUALITY_INDEX, selectedVideoQualityPosition)
            }
            videoQualitySelectedListener.videoQualitySelected(selectedVideoQualityPosition)
            dialog.dismiss()
        }
        binding.recyclerView.adapter = changeDownloadQualityAdapter
        dialog.show()
    }

    fun changeWifiSetting(videoQualitySelectedListener: WifiPreferenceListener) {
        val binding = DataBindingUtil.inflate<WifiDialogBinding>(activity.layoutInflater, R.layout.wifi_dialog, null, false)
        val dialog = BottomSheetDialog(activity)
        dialog.setContentView(binding.root)

        val downloadQualityList = ArrayList<String>()
        downloadQualityList.addAll(activity.resources.getStringArray(R.array.download_quality))
        downloadQualityList.removeAt(4)
        binding.switchDownload.isChecked= KsPreferenceKeys.getInstance().downloadOverWifi == 1

        binding.btnStartDownload.setOnClickListener {
            if (binding.switchDownload.isChecked) {
                KsPreferenceKeys.getInstance().downloadOverWifi = 1
                //SharedPrefHelper(activity).setInt(SharedPrefesConstants.DOWNLOAD_QUALITY_INDEX, selectedVideoQualityPosition)
            }else{
                KsPreferenceKeys.getInstance().downloadOverWifi=0
            }
            Logger.e("DownloadedVideo1", Gson().toJson(KsPreferenceKeys.getInstance().downloadOverWifi))
            videoQualitySelectedListener.actionP(KsPreferenceKeys.getInstance().downloadOverWifi)
            dialog.dismiss()
        }

        dialog.show()
    }


    fun deleteAllExpiredVideos() {
        //Delete Expired Videos
        getAllVideosFromDatabase().observe(activity as LifecycleOwner, androidx.lifecycle.Observer {
            it.downloadVideos.forEach { downloadedVideo ->
                Logger.e("DownloadedVideo1", Gson().toJson(downloadedVideo))
                if (downloadedVideo.downloadType.equals(MediaTypeConstants.getInstance().series,ignoreCase = true)) {
                    findOfflineVideoById(downloadedVideo.videoId, object : OfflineCallback<Video> {
                        override fun onSuccess(video: Video?) {
                            if (video != null) {
                                if (!video.isClearContent) {
                                    if (video.licenseExpiryDate!!.time >= System.currentTimeMillis()) {
                                        Logger.e("License", "Expiry" + video.licenseExpiryDate)
                                    } else {
                                        deleteVideo(video)
                                    }
                                }else{
                                    deleteExpireVideoFromDB(downloadedVideo)
                                  //  Logger.e("License", "Expiry" + video.licenseExpiryDate)
                                }
                            } else {
                                deleteVideoFromDatabase(downloadedVideo.videoId)
                            }
                        }

                        override fun onFailure(p0: Throwable?) {
                        }
                    })
                }
            }
        })
        //Delete Expired Episodes
        getAllEpisodes().observe(activity as LifecycleOwner, androidx.lifecycle.Observer { downloadedEpisodes ->
            downloadedEpisodes.forEach { downloadedVideo ->
                Logger.e("DownloadedVideo1", Gson().toJson(downloadedVideo))
                findOfflineVideoById(downloadedVideo.videoId, object : OfflineCallback<Video> {
                    override fun onSuccess(video: Video?) {
                        if (video != null) {
                            if (!video.isClearContent) {
                                if (video.licenseExpiryDate!!.time >= System.currentTimeMillis()) {
                                    Logger.e("License", "Expiry" + video.licenseExpiryDate)
                                } else {
                                    deleteVideo(video)
                                }
                            }
                        } else {
                            deleteVideoFromDatabase(downloadedVideo.videoId)
                        }
                    }

                    override fun onFailure(p0: Throwable?) {
                    }
                })
            }
        })
    }

    private fun deleteExpireVideoFromDB(videoId: DownloadedVideo) {
        val diff: Int = AppCommonMethod.getTodaysDifference(videoId.expiryDate)
        if (diff == 1) {
            try {
                catalog.deleteVideo(videoId.videoId, object : OfflineCallback<Boolean> {
                    override fun onSuccess(aBoolean: Boolean?) {
                        if (aBoolean!!) {
                            checkDownloadStatus()
                            deleteVideoFromDatabase(videoId.videoId)
                        }
                    }

                    override fun onFailure(throwable: Throwable) {

                    }
                })

            } catch (ex: Exception) {
                Logger.e(TAG, ex.message)
            }
        }

    }

    private class DownloadTask : AsyncTask<DownloadDatabase, Any, DownloadModel>() {
        var delegate: AsyncResponse<DownloadModel>? = null
        override fun doInBackground(vararg params: DownloadDatabase): DownloadModel {
            val seriesId = params[0].downloadVideoDao().downloadedVideos
            val downloadModel = DownloadModel()
            downloadModel.downloadVideos = seriesId as ArrayList<DownloadedVideo>
            seriesId.forEachIndexed { index, downloadVideo ->
                val downloadedEpisodes = params[0].downloadEpisodeDao().getEpisodesList(downloadVideo.seriesId, downloadVideo.seasonNumber)
                downloadVideo.episodeCount = downloadedEpisodes.size.toString()
                if (downloadVideo.downloadType == MediaTypeConstants.getInstance().series && downloadedEpisodes.size > 0) {
                    downloadVideo.seasonNumber = downloadedEpisodes[0].seasonNumber
                    downloadModel.episodeMap[downloadVideo.seriesId + "_" + downloadedEpisodes[0].seasonNumber] = downloadedEpisodes as java.util.ArrayList<DownloadedEpisodes>
                }
            }
            return downloadModel
        }

        override fun onPostExecute(result: DownloadModel) {
            super.onPostExecute(result)
            delegate?.processFinish(result)
//            val downloadEpisodeTask = DownloadEpisodeTask(result as List<DownloadedVideo>)
//            downloadEpisodeTask.delegate = delegate
        }
    }

    private class DownloadEpisodesTask(seriesId: String?, seasonNumber: String?) : AsyncTask<DownloadDatabase, Any, MutableList<DownloadedEpisodes>>() {
        var seriesId = seriesId
        var seasonNumber = seasonNumber
        var delegate: AsyncResponse<List<DownloadedEpisodes>>? = null

        override fun doInBackground(vararg params: DownloadDatabase): MutableList<DownloadedEpisodes> {
            if (seriesId != null && seasonNumber != null) {
                return params[0].downloadEpisodeDao().getEpisodesList(seriesId!!, seasonNumber!!)
            } else {
                return params[0].downloadEpisodeDao().allEpisodes
            }
        }

        override fun onPostExecute(result: MutableList<DownloadedEpisodes>) {
            super.onPostExecute(result)
            delegate?.processFinish(result)
        }
    }

    interface AsyncResponse<T> {
        fun processFinish(result: T)
    }
}