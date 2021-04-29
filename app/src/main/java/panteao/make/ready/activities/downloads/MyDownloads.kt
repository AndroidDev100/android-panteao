package panteao.make.ready.activities.downloads

import android.content.Intent
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
import com.google.gson.Gson
import com.mmtv.utils.helpers.downloads.DownloadHelper
import panteao.make.ready.R
import panteao.make.ready.databinding.ActivityMyDownloadsBinding
import panteao.make.ready.utils.MediaTypeConstants
import panteao.make.ready.utils.commonMethods.AppCommonMethod
import panteao.make.ready.utils.cropImage.helpers.Logger
import panteao.make.ready.utils.helpers.downloads.room.DownloadModel
import kotlinx.android.synthetic.main.activity_my_downloads.*
import panteao.make.ready.baseModels.BaseBindingActivity
import panteao.make.ready.utils.helpers.downloads.VideoListListener
import java.io.Serializable

class MyDownloads : BaseBindingActivity<ActivityMyDownloadsBinding>(), MediaDownloadable.DownloadEventListener, VideoListListener, NoDataCallBack {
    override fun inflateBindingLayout(inflater: LayoutInflater): ActivityMyDownloadsBinding {
        return ActivityMyDownloadsBinding.inflate(inflater)
    }

    override fun downloadStatus(videoId: String, downloadStatus: DownloadStatus?) {
        Logger.e(TAG, downloadStatus?.code.toString())
        downloadHelper.updateVideoStatus(downloadStatus!!.code, videoId)
        downloadsAdapter?.notifyVideoChanged(videoId, downloadStatus)

    }

    private lateinit var downloadHelper: DownloadHelper
    private var downloadsAdapter: DownloadsAdapter? = null
    val TAG = this.javaClass.simpleName
    override fun alreadyDownloaded(video: Video) {
    }

    override fun downloadedVideos(p0: MutableList<out Video>?) {
    }

    override fun videoFound(video: Video?) {
    }

    override fun onDownloadRequested(video: Video) {
        Logger.e(TAG, String.format(
                "Starting to process '%s' video download request", video.name))
    }

    override fun onDownloadStarted(video: Video, l: Long, map: Map<String, Serializable>) {
        Logger.e(TAG, "onDownloadStarted" + video.name)
        downloadHelper.updateVideoStatus(DownloadStatus.STATUS_DOWNLOADING, video.id)
    }

    override fun onDownloadProgress(video: Video, downloadStatus: DownloadStatus) {
        Logger.e(TAG, "onDownloadProgress" + downloadStatus.progress)
        downloadsAdapter?.notifyVideoChanged(video.id, downloadStatus)
    }

    override fun onDownloadPaused(video: Video, downloadStatus: DownloadStatus) {
        Logger.e(TAG, "onDownloadPaused")
    }

    override fun onDownloadCompleted(video: Video, downloadStatus: DownloadStatus) {
        downloadHelper.updateVideoStatus(DownloadStatus.STATUS_COMPLETE, video.id)
        downloadsAdapter?.notifyVideoChanged(video.id, downloadStatus)
    }

    override fun onDownloadCanceled(video: Video) {
        Logger.e(TAG, "onDownloadCanceled")
        if (downloadHelper!=null){
            Handler(Looper.getMainLooper()).postDelayed({
                downloadHelper.getAllVideosFromDatabase().observe(this, Observer {

                    var downloadedVideos = AppCommonMethod.removeDownloadedSeries(it.downloadVideos, downloadHelper)
                    checkOffline(it)


                })
            }, 300)

        }

    }

    override fun onDownloadDeleted(video: Video) {
        Logger.e(TAG, "onDownloadDeleted")
    }

    override fun onDownloadFailed(video: Video, downloadStatus: DownloadStatus) {
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
        Logger.e(TAG, "deleteVideo")
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        var counter = 0
        downloadHelper = DownloadHelper(this, this)
        downloadHelper.getAllVideosFromDatabase().observe(this, Observer {

            var downloadedVideos = AppCommonMethod.removeDownloadedSeries(it.downloadVideos, downloadHelper)
            checkOffline(it)

        })
        setupToolbar()
    }

    private fun checkOffline(it: DownloadModel?) {
        downloadHelper.getAllVideosFromDatabase().observe(this, Observer {
            if (it.downloadVideos.size>0){
                downloadsAdapter = DownloadsAdapter(this, it, this)
                downloaded_recycler_view.layoutManager = LinearLayoutManager(this)
                downloaded_recycler_view.setHasFixedSize(true)
                downloaded_recycler_view.itemAnimator = DefaultItemAnimator()
                downloaded_recycler_view.adapter = downloadsAdapter
                nodatafounmd.visibility = View.GONE
                downloaded_recycler_view.visibility = View.VISIBLE
                rec_layout.visibility = View.VISIBLE
                progress_bar.visibility = View.GONE
            }else{
                nodatafounmd.visibility = View.VISIBLE
                downloaded_recycler_view.visibility = View.GONE
                rec_layout.visibility = View.GONE
                progress_bar.visibility = View.GONE
                Logger.e(TAG, "blankActivity")
            }

        })


    }


    private fun setupToolbar() {
        binding.toolbar.llSearchIcon.visibility = View.GONE
        binding.toolbar.backLayout.visibility = View.VISIBLE
        binding.toolbar.homeIcon.visibility = View.GONE
        binding.toolbar.titleText.visibility = View.VISIBLE
        binding.toolbar.screenText.text = resources.getString(R.string.my_downloads)
        binding.toolbar.backLayout.setOnClickListener { onBackPressed() }
    }

    private fun setData(it: DownloadModel?) {
        Logger.e(TAG, "Videos: => " + Gson().toJson(it))
    }

    override fun dataNotAvailable() {
        /* if (downloadHelper!=null){
             Handler(Looper.getMainLooper()).postDelayed({
                 downloadHelper.getAllVideosFromDatabase().observe(this, Observer {
                     checkOffline(it)
                 })
             }, 300)

         }*/
        /*if (downloadsAdapter!=null){
            var size=downloadsAdapter!!.itemCount
            if (size>0){

            }else{
                downloaded_recycler_view.visibility= View.GONE

            }
        }*/
        Logger.e(TAG, "dataNotAvailable")
        nodatafounmd.visibility = View.VISIBLE
        downloaded_recycler_view.visibility = View.GONE
        rec_layout.visibility = View.GONE
        progress_bar.visibility = View.GONE
        // val downloadedVideoIntent = Intent(this, MyDownloads::class.java)
        //startActivity(downloadedVideoIntent)

    }
    var dataCount: Int = 1
    override fun onStart() {
        super.onStart()
        try {
            Logger.w("sdva", AppCommonMethod.isDownloadDeleted.toString())
            if (AppCommonMethod.isDownloadDeleted){

                Handler(Looper.getMainLooper()).postDelayed({
                    downloadHelper.getAllVideosFromDatabase().observe(this, Observer {
                        if (it.downloadVideos.size>0){

                            it.downloadVideos.forEachIndexed { index, downloadedVideo ->
                                val b = downloadedVideo.downloadType == MediaTypeConstants.getInstance().series || downloadedVideo.downloadType ==  MediaTypeConstants.getInstance().episode
                                Logger.e("inCondition", b.toString() +" "+Integer.parseInt(downloadedVideo.episodeCount))
                                if (b) {
                                    if (Integer.parseInt(downloadedVideo.episodeCount) != 0){

                                    }else{
                                        Logger.e("inCondition", downloadedVideo.videoId)
                                        downloadHelper.deleteVideo(downloadedVideo.videoId)
                                    }
                                }
                            }




                        }
                    })
                }, 1000)

                progress_bar.visibility = View.VISIBLE
                AppCommonMethod.isDownloadDeleted=false;
                Handler(Looper.getMainLooper()).postDelayed({
                    val downloadedVideoIntent = Intent(this, MyDownloads::class.java)
                    startActivity(downloadedVideoIntent)
                    finish()

                }, 2500)


            }
            /* downloadHelper.getAllVideosFromDatabase().observe(this, Observer {
                 var downloadedVideos = AppCommonMethod.removeDownloadedSeries(it.downloadVideos, downloadHelper)

             })*/

        }catch (exception:Exception){
            Logger.e("inCondition", exception.message)
        }

    }
}