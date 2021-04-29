package panteao.make.ready.utils.helpers.downloads.room

import androidx.annotation.NonNull
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "downloadedEpisodes")
data class DownloadedEpisodes (
        @PrimaryKey
        @NonNull
        val videoId: String,
        val seasonNumber: String,
        val episodeNumber:String,
        val seriesId: String
)