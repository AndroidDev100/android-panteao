package panteao.make.ready.utils.helpers.downloads.downloadUtil

import android.view.View
import com.kaltura.android.exoplayer2.util.Log
import panteao.make.ready.R
import panteao.make.ready.activities.series.adapter.SeasonAdapter
import panteao.make.ready.beanModelV3.uiConnectorModelV2.EnveuVideoItemBean
import panteao.make.ready.databinding.RowEpisodeListBinding
import panteao.make.ready.enums.DownloadStatus
import panteao.make.ready.utils.helpers.downloads.KTDownloadHelper
import panteao.make.ready.utils.helpers.downloads.downloadListing.DownloadStateListener

object DownloadUtils {
    fun setDownloadStatus(holder: RowEpisodeListBinding, position: Int, enveuVideoItemBean: EnveuVideoItemBean,downloadHelper:KTDownloadHelper) {
        downloadHelper.getAssetDownloadState(enveuVideoItemBean.getkEntryId(),downloadHelper,
            object : DownloadStateListener {
                override fun downloadState(name: String?, percentage: Float,downloadSize: String?) {
                    Log.d("downloadStatus "+position+"--->  ",name!!)
                    // Log.d("stateOfAsset 2",currentVideoItem.name!!)
                    //Log.d("stateOfAsset 2",percentage.toString())
                    if (name == null) {
                        holder.download.visibility = View.GONE
                    }
                    else if (name.equals("paused", ignoreCase = true)){
                        holder.downloadStatus = DownloadStatus.PAUSE
                        //holder.descriptionTxt?.text = DownloadStatus.PAUSED.name
                    }
                    else if (name.equals("started", ignoreCase = true)){
                        // itemBinding?.flDeleteWatchlist?.visibility = View.VISIBLE
                        // itemBinding?.videoDownloading?.visibility = View.VISIBLE
                        holder.downloadStatus = DownloadStatus.DOWNLOADING
                        holder.videoDownloading?.progress = percentage
                        // itemBinding?.videoDownloaded?.visibility = View.GONE
                        // itemBinding?.pauseDownload?.visibility = View.GONE
                        // itemBinding?.loadingDownload?.visibility = View.GONE
                    }
                    else if (name.equals("completed", ignoreCase = true)){
                        holder.downloadStatus = DownloadStatus.DOWNLOADED
                       /* holder.descriptionTxt?.text = downloadSize
                        holder.tvGenre?.visibility = View.VISIBLE
                        itemBinding?.itemBinding?.tvGenre?.text = DownloadStatus.Completed.name

                        itemBinding?.itemBinding?.tvGenre?.setTextColor(
                            context.getResources().getColor(R.color.more_text_color_dark)
                        )
                        itemBinding?.itemBinding?.descriptionTxt?.setTextColor(
                            context.getResources().getColor(R.color.more_text_color_dark)
                        )*/
                    }
                    else if (name.equals("failed", ignoreCase = true)){
                        holder.downloadStatus = DownloadStatus.FAILED
                        /* holder.descriptionTxt?.text = downloadSize
                         holder.tvGenre?.visibility = View.VISIBLE
                         itemBinding?.itemBinding?.tvGenre?.text = DownloadStatus.Completed.name

                         itemBinding?.itemBinding?.tvGenre?.setTextColor(
                             context.getResources().getColor(R.color.more_text_color_dark)
                         )
                         itemBinding?.itemBinding?.descriptionTxt?.setTextColor(
                             context.getResources().getColor(R.color.more_text_color_dark)
                         )*/
                    }
                    else if (name.equals("none", ignoreCase = true)){
                        holder.downloadStatus = DownloadStatus.START
                        /* holder.descriptionTxt?.text = downloadSize
                         holder.tvGenre?.visibility = View.VISIBLE
                         itemBinding?.itemBinding?.tvGenre?.text = DownloadStatus.Completed.name

                         itemBinding?.itemBinding?.tvGenre?.setTextColor(
                             context.getResources().getColor(R.color.more_text_color_dark)
                         )
                         itemBinding?.itemBinding?.descriptionTxt?.setTextColor(
                             context.getResources().getColor(R.color.more_text_color_dark)
                         )*/
                    }
                    else if (name.equals("asset_not_found", ignoreCase = true)){
                        holder.downloadStatus = DownloadStatus.START
                    }
                    else{

                    }
                }
            })

    }
}