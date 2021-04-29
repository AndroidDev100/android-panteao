package panteao.make.ready.utils.helpers.downloads.room;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;

@Dao
public interface DownloadVideoDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertVideo(DownloadedVideo downloadVideo);

    @Query("SELECT * from downloadedVideos ORDER BY created_at DESC")
    List<DownloadedVideo> getDownloadedVideos();

    @Query("DELETE FROM downloadedVideos WHERE videoId=:videoId")
    void deleteVideo(@NotNull String videoId);

    @Query("UPDATE downloadedVideos set downloadStatus=:code WHERE videoId = :videoId")
    void updateStatus(@NotNull String videoId, @Nullable Integer code);

    @Query("DELETE FROM downloadedVideos")
    void deleteAllVideos();
}
