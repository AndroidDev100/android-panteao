package panteao.make.ready.utils.helpers.downloads.db;

import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.Index;
import androidx.room.PrimaryKey;

@Entity(tableName = "assetdownload", indices = @Index(value = {"entryId"}, unique = true))
public class DownloadItemEntity {

    @PrimaryKey(autoGenerate = true)
    int id;
    String name;
    String assetType;
    boolean isSeries;
    String downloadSize;
    String expiryDate;
    String entryId;
    int seasonNumber;
    String seriesId;
    String episodeNumber;
    String seriesName;
    String imageURL;
    long timeStamp;
    String seriesImageUrl;
    int episodesCount;
    String seriesIdWithNumber;

    @Ignore
    public DownloadItemEntity(String name, String assetType, boolean isSeries, String downloadSize, String expiryDate,String entryId,
                              int seasonNumber,String seriesId,String episodeNumber,String seriesName,String imageURL,long timeStamp,String seriesImageUrl,int episodesCount) {
        this.name = name;
        this.assetType = assetType;
        this.isSeries = isSeries;
        this.downloadSize = downloadSize;
        this.expiryDate = expiryDate;
        this.entryId=entryId;
        this.seriesId=seriesId;
        this.seriesName=seriesName;
        this.imageURL=imageURL;
        this.timeStamp=timeStamp;
        this.seasonNumber=seasonNumber;
        this.episodeNumber=episodeNumber;
        this.seriesImageUrl=seriesImageUrl;
        this.episodesCount=episodesCount;
        this.seriesIdWithNumber=seriesId+seasonNumber;
    }

    public DownloadItemEntity(int id, String name, String assetType, boolean isSeries, String downloadSize, String expiryDate,String entryId,
    int seasonNumber,String seriesId,String episodeNumber,String seriesName,String imageURL,long timeStamp,String seriesImageUrl,int episodesCount) {
        this.id = id;
        this.name = name;
        this.assetType = assetType;
        this.isSeries = isSeries;
        this.downloadSize = downloadSize;
        this.expiryDate = expiryDate;
        this.entryId=entryId;
        this.seriesId=seriesId;
        this.seriesName=seriesName;
        this.imageURL=imageURL;
        this.timeStamp=timeStamp;
        this.seasonNumber=seasonNumber;
        this.episodeNumber=episodeNumber;
        this.seriesImageUrl=seriesImageUrl;
        this.episodesCount=episodesCount;
        this.seriesIdWithNumber=seriesId+seasonNumber;
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

    public String getSeriesName() {
        return seriesName;
    }

    public void setSeriesName(String seriesName) {
        this.seriesName = seriesName;
    }

    public void setImageURL(String imageURL) {
        this.imageURL = imageURL;
    }

    public String getImageURL() {
        return imageURL;
    }

    public long getTimeStamp() {
        return timeStamp;
    }

    public void setTimeStamp(long timeStamp) {
        this.timeStamp = timeStamp;
    }

    public int getSeasonNumber() {
        return seasonNumber;
    }

    public void setSeasonNumber(int seasonNumber) {
        this.seasonNumber = seasonNumber;
    }

    public String getEpisodeNumber() {
        return episodeNumber;
    }

    public void setEpisodeNumber(String episodeNumber) {
        this.episodeNumber = episodeNumber;
    }

    public void setSeriesImageUrl(String seriesImageUrl) {
        this.seriesImageUrl = seriesImageUrl;
    }

    public String getSeriesImageUrl() {
        return seriesImageUrl;
    }

    public void setEpisodesCount(int episodesCount) {
        this.episodesCount = episodesCount;
    }

    public int getEpisodesCount() {
        return episodesCount;
    }

    public String getSeriesIdWithNumber() {
        return seriesIdWithNumber;
    }

    public void setSeriesIdWithNumber(String seriesIdWithNumber) {
        this.seriesIdWithNumber = seriesIdWithNumber;
    }
}
