package panteao.make.ready.utils.helpers.downloads.room

import androidx.room.ColumnInfo
import androidx.room.Entity
import com.brightcove.player.network.DownloadStatus

@Entity(tableName = "downloadedVideos", primaryKeys = ["videoId", "seasonNumber"])
data class DownloadedVideo(
        val videoId: String,
        val downloadType: String,
        val seriesId: String,
        var seasonNumber: String = "",
        var episodeCount: String = "",
        var seriesName: String = "",
        var expiryDate: String = "") {

    var id: Long = 0
    @ColumnInfo(name = "created_at")
    var createdAt = System.currentTimeMillis()
    var downloadStatus = DownloadStatus.STATUS_QUEUEING
}