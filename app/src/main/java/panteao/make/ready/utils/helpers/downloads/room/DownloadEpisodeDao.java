package panteao.make.ready.utils.helpers.downloads.room;

import androidx.annotation.NonNull;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import org.jetbrains.annotations.NotNull;

import java.util.List;

@Dao
public interface DownloadEpisodeDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertEpisodes(DownloadedEpisodes downloadedEpisodes);

    @Query("SELECT * FROM downloadedEpisodes where seriesId=:seriesId and seasonNumber=:seasonNumber")
    List<DownloadedEpisodes> getEpisodesList(@NotNull String seriesId, @NonNull String seasonNumber);

    @Query("SELECT * FROM downloadedEpisodes where videoId=:episodeId")
    List<DownloadedEpisodes> getEpisodes(@NotNull String episodeId);

    @Query("SELECT * FROM downloadedEpisodes")
    List<DownloadedEpisodes> getAllEpisodes();

    @Query("DELETE  FROM downloadedEpisodes")
    void deleteAllVideos();

    @Query("DELETE  FROM downloadedEpisodes where videoId=:videoId")
    void deleteVideo(@NotNull String videoId);
}
