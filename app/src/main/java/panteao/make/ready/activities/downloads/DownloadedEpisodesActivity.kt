package panteao.make.ready.activities.downloads

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.LayoutInflater
import android.view.View
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.LinearLayoutManager
import com.brightcove.player.model.Video
import com.brightcove.player.network.DownloadStatus
import com.brightcove.player.offline.MediaDownloadable
import com.mmtv.utils.helpers.downloads.DownloadHelper
import panteao.make.ready.databinding.ActivityDownloadedEpisodesBinding
import panteao.make.ready.utils.cropImage.helpers.Logger
import panteao.make.ready.utils.helpers.downloads.room.DownloadedEpisodes
import kotlinx.android.synthetic.main.activity_my_downloads.*
import panteao.make.ready.baseModels.BaseBindingActivity
import panteao.make.ready.utils.helpers.downloads.VideoListListener
import java.io.Serializable
import java.util.ArrayList

class DownloadedEpisodesActivity() : BaseBindingActivity<ActivityDownloadedEpisodesBinding>(), MediaDownloadable.DownloadEventListener, VideoListListener {
    override fun inflateBindingLayout(inflater: LayoutInflater): ActivityDownloadedEpisodesBinding {
        return ActivityDownloadedEpisodesBinding.inflate(inflater)
    }

    private lateinit var downloadsAdapter: DownloadedEpisodesAdapter
    private lateinit var seriesId: String
    private lateinit var seriesName: String
    private lateinit var seasonNumber: String
    private lateinit var downloadHelper: DownloadHelper
    private var index: Int = -1
    private val TAG = "DownloadedEpisodeActivity"
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        this.seriesId = intent?.getStringExtra("SeriesId")!!
        this.seriesName = intent?.getStringExtra("SeriesName")!!
        this.seasonNumber = intent?.getStringExtra("SeasonNumber")!!
        this.index = intent?.getIntExtra("index",-1)!!

        setupToolbar(seriesName)
        downloadHelper = DownloadHelper(this, this)
        downloadHelper.getAllEpisodesOfSeries(seriesId, seasonNumber).observe(this, Observer {
            checkOffline(it)

        })
    }

    private fun checkOffline(it: ArrayList<DownloadedEpisodes>?) {
        if (it!!.size>0){
            downloadsAdapter = DownloadedEpisodesAdapter(this@DownloadedEpisodesActivity, it, index)
            downloaded_recycler_view.layoutManager = LinearLayoutManager(this)
            downloaded_recycler_view.setHasFixedSize(true)
            downloaded_recycler_view.itemAnimator = DefaultItemAnimator()
            downloaded_recycler_view.adapter = downloadsAdapter
        }else{
            nodatafounmd.visibility = View.VISIBLE
            Logger.e(TAG, "blankActivity")
        }
    }


    private fun setupToolbar(seriesName: String) {
        binding.toolbar.llSearchIcon.visibility = View.GONE
        binding.toolbar.backLayout.visibility = View.VISIBLE
        binding.toolbar.homeIcon.visibility = View.GONE
        binding.toolbar.titleText.visibility = View.VISIBLE
        binding.toolbar.screenText.text = seriesName
        binding.toolbar.backLayout.setOnClickListener { onBackPressed() }
    }

    override fun alreadyDownloaded(video: Video) {
    }

    override fun downloadedVideos(p0: MutableList<out Video>?) {
    }

    override fun videoFound(video: Video?) {
    }


    override fun downloadStatus(videoId: String, downloadStatus: DownloadStatus?) {
        Logger.e(TAG, downloadStatus?.code.toString())
        downloadHelper.updateVideoStatus(downloadStatus!!.code, videoId)
        downloadsAdapter?.notifyVideoChanged(videoId, downloadStatus)

    }
    override fun onDownloadRequested(video: Video) {
        Logger.e(TAG, String.format(
                "Starting to process '%s' video download request", video.name))
    }

    override fun onDownloadStarted(video: Video, l: Long, map: Map<String, Serializable>) {
        Logger.e(TAG, "onDownloadStarted" + video.name)
        downloadHelper.updateVideoStatus(DownloadStatus.STATUS_DOWNLOADING, video.id)
    }

    override fun onDownloadProgress(video: Video, downloadStatus: com.brightcove.player.network.DownloadStatus) {
        Logger.e(TAG, "onDownloadProgress" + downloadStatus.progress)
        downloadsAdapter?.notifyVideoChanged(video.id, downloadStatus)
    }

    override fun onDownloadPaused(video: Video, downloadStatus: com.brightcove.player.network.DownloadStatus) {
       Logger.e(TAG, "onDownloadPaused")
    }

    override fun onDownloadCompleted(video: Video, downloadStatus: com.brightcove.player.network.DownloadStatus) {
        downloadHelper.updateVideoStatus(DownloadStatus.STATUS_COMPLETE, video.id)
        downloadsAdapter?.notifyVideoChanged(video.id, downloadStatus)
    }

    override fun onDownloadCanceled(video: Video) {
        Logger.e(TAG, "onDownloadCanceled")
        Logger.e(TAG, "onDownloadCanceled")
        if (downloadHelper!=null){
            Handler(Looper.getMainLooper()).postDelayed({
                downloadHelper.getAllEpisodesOfSeries(seriesId, seasonNumber).observe(this, Observer {
                    checkOffline(it)
                })
            }, 300)

        }


    }

    override fun onDownloadDeleted(video: Video) {
        Logger.e(TAG, "onDownloadDeleted")
    }

    override fun onDownloadFailed(video: Video, downloadStatus: com.brightcove.player.network.DownloadStatus) {
        Logger.e(TAG, "onDownloadFailed")

    }

    override fun downloadVideo(video: Video) {

    }

    override fun pauseVideoDownload(video: Video) {
        Logger.e(TAG, "pauseVideoDownload")
    }

    override fun resumeVideoDownload(video: Video) {
    }

    override fun deleteVideo(video: Video) {

    }
}
