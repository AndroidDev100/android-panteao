package panteao.make.ready.utils.helpers.downloads.db;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import java.util.ArrayList;
import java.util.List;

@Dao
public interface DownloadDao {
    @Query("SELECT * FROM ASSETDOWNLOAD ORDER BY ID")
    List<DownloadItemEntity> loadAllDownloads();

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertDownloadItem(DownloadItemEntity person);

    @Update
    void updateDownloadItem(DownloadItemEntity person);

    @Delete
    void deleteDownloadItem(DownloadItemEntity person);

    @Query("SELECT * FROM ASSETDOWNLOAD WHERE id = :id")
    DownloadItemEntity loadDownloadItemById(int id);

    @Query("SELECT * FROM ASSETDOWNLOAD WHERE seriesId = :seriesId")
    List<DownloadItemEntity> loadChaptersByTID(String seriesId);

    @Query("SELECT * FROM ASSETDOWNLOAD WHERE seriesId = :seriesId AND seasonNumber = :seasonNumber")
    List<DownloadItemEntity> loadEpisodesBySeriesID(String seriesId,int seasonNumber);

    @Delete
    void deleteExpireIDs(ArrayList<DownloadItemEntity> ids);
}
