package panteao.make.ready.utils.helpers.downloads.db;

import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

@Entity(tableName = "assetdownload")
public class DownloadItemEntity {

    @PrimaryKey(autoGenerate = true)
    int id;
    String name;
    String assetType;
    boolean isSeries;
    String downloadSize;
    String expiryDate;
    String entryId;
    String seasonNumber;
    String seriesId;
    String episodeNumber;

    @Ignore
    public DownloadItemEntity(String name, String assetType, boolean isSeries, String downloadSize, String expiryDate,String entryId,
                              String seasonNumber,String seriesId,String episodeNumber) {
        this.name = name;
        this.assetType = assetType;
        this.isSeries = isSeries;
        this.downloadSize = downloadSize;
        this.expiryDate = expiryDate;
        this.entryId=entryId;
        this.seriesId=seriesId;
    }

    public DownloadItemEntity(int id, String name, String assetType, boolean isSeries, String downloadSize, String expiryDate,String entryId,
    String seasonNumber,String seriesId,String episodeNumber) {
        this.id = id;
        this.name = name;
        this.assetType = assetType;
        this.isSeries = isSeries;
        this.downloadSize = downloadSize;
        this.expiryDate = expiryDate;
        this.entryId=entryId;
        this.seriesId=seriesId;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAssetType() {
        return assetType;
    }

    public void setAssetType(String assetType) {
        this.assetType = assetType;
    }

    public boolean isSeries() {
        return isSeries;
    }

    public void setSeries(boolean isSeries) {
        this.isSeries = isSeries;
    }

    public String getDownloadSize() {
        return downloadSize;
    }

    public void setDownloadSize(String downloadSize) {
        this.downloadSize = downloadSize;
    }

    public String getExpiryDate() {
        return expiryDate;
    }

    public void setExpiryDate(String expiryDate) {
        this.expiryDate = expiryDate;
    }

    public String getEntryId() {
        return entryId;
    }

    public void setEntryId(String entryId) {
        this.entryId = entryId;
    }

    public void setSeriesId(String seriesId) {
        this.seriesId = seriesId;
    }

    public String getSeriesId() {
        return seriesId;
    }
}
