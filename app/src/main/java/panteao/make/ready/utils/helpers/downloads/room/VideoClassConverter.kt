package panteao.make.ready.utils.helpers.downloads.room

import androidx.room.TypeConverter
import com.brightcove.player.model.Video
import com.google.gson.Gson

class VideoClassConverter {
    @TypeConverter
    fun storeVideoToDatabase(video: Video): String {
        return Gson().toJson(video)
    }

    @TypeConverter
    fun getVideoFromDatabase(video: String): Video {
        return Gson().fromJson(video, Video::class.java)
    }
}