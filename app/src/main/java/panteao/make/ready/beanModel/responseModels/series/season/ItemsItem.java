package panteao.make.ready.beanModel.responseModels.series.season;

import com.google.gson.annotations.SerializedName;

import java.util.List;


public class ItemsItem {

    @SerializedName("contentThumbnailUrl")
    private Object contentThumbnailUrl;

    @SerializedName("videoType")
    private String videoType;

    @SerializedName("assetLink")
    private Object assetLink;

    @SerializedName("description")
    private String description;

    @SerializedName("assetKeywords")
    private Object assetKeywords;

    @SerializedName("contentTitle")
    private String contentTitle;

    @SerializedName("transcodingJobId")
    private Object transcodingJobId;

    @SerializedName("playUrl")
    private Object playUrl;

    @SerializedName("duration")
    private Object duration;

    @SerializedName("contentProvider")
    private Object contentProvider;

    @SerializedName("landscapeImage")
    private String landscapeImage;

    @SerializedName("premium")
    private boolean premium;

    @SerializedName("casts")
    private List<String> casts;

    @SerializedName("genres")
    private List<String> genres;

    @SerializedName("series")
    private Object series;

    @SerializedName("season")
    private Object season;

    @SerializedName("id")
    private int id;

    @SerializedName("portraitImage")
    private String portraitImage;

    @SerializedName("contentType")
    private String contentType;

    @SerializedName("status")
    private String status;

    @SerializedName("episodeNo")
    private Object episodeNo;

    private boolean isContinueWatching;
    private int continueDuration;
    private int position;
    private boolean finishedWatching;
    @SerializedName("isNew")
    private boolean isNew;

    public boolean isContinueWatching() {
        return isContinueWatching;
    }

    public void setContinueWatching(boolean continueWatching) {
        isContinueWatching = continueWatching;
    }

    public int getContinueDuration() {
        return continueDuration;
    }

    public void setContinueDuration(int continueDuration) {
        this.continueDuration = continueDuration;
    }

    public int getPosition() {
        return position;
    }

    public void setPosition(int position) {
        this.position = position;
    }

    public boolean isFinishedWatching() {
        return finishedWatching;
    }

    public void setFinishedWatching(boolean finishedWatching) {
        this.finishedWatching = finishedWatching;
    }

    public boolean isNew() {
        return isNew;
    }

    public void setNew(boolean aNew) {
        isNew = aNew;
    }

    public Object getEpisodeNo() {
        return episodeNo;
    }

    public void setEpisodeNo(Object episodeNo) {
        this.episodeNo = episodeNo;
    }

    public Object getContentThumbnailUrl() {
        return contentThumbnailUrl;
    }

    public void setContentThumbnailUrl(Object contentThumbnailUrl) {
        this.contentThumbnailUrl = contentThumbnailUrl;
    }

    public String getVideoType() {
        return videoType;
    }

    public void setVideoType(String videoType) {
        this.videoType = videoType;
    }

    public Object getAssetLink() {
        return assetLink;
    }

    public void setAssetLink(Object assetLink) {
        this.assetLink = assetLink;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Object getAssetKeywords() {
        return assetKeywords;
    }

    public void setAssetKeywords(Object assetKeywords) {
        this.assetKeywords = assetKeywords;
    }

    public String getContentTitle() {
        return contentTitle;
    }

    public void setContentTitle(String contentTitle) {
        this.contentTitle = contentTitle;
    }

    public Object getTranscodingJobId() {
        return transcodingJobId;
    }

    public void setTranscodingJobId(Object transcodingJobId) {
        this.transcodingJobId = transcodingJobId;
    }

    public Object getPlayUrl() {
        return playUrl;
    }

    public void setPlayUrl(Object playUrl) {
        this.playUrl = playUrl;
    }

    public Object getDuration() {
        return duration;
    }

    public void setDuration(Object duration) {
        this.duration = duration;
    }

    public Object getContentProvider() {
        return contentProvider;
    }

    public void setContentProvider(Object contentProvider) {
        this.contentProvider = contentProvider;
    }

    public String getLandscapeImage() {
        return landscapeImage;
    }

    public void setLandscapeImage(String landscapeImage) {
        this.landscapeImage = landscapeImage;
    }

    public boolean isPremium() {
        return premium;
    }

    public void setPremium(boolean premium) {
        this.premium = premium;
    }

    public List<String> getCasts() {
        return casts;
    }

    public void setCasts(List<String> casts) {
        this.casts = casts;
    }

    public List<String> getGenres() {
        return genres;
    }

    public void setGenres(List<String> genres) {
        this.genres = genres;
    }

    public Object getSeries() {
        return series;
    }

    public void setSeries(Object series) {
        this.series = series;
    }

    public Object getSeason() {
        return season;
    }

    public void setSeason(Object season) {
        this.season = season;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getPortraitImage() {
        return portraitImage;
    }

    public void setPortraitImage(String portraitImage) {
        this.portraitImage = portraitImage;
    }

    public String getContentType() {
        return contentType;
    }

    public void setContentType(String contentType) {
        this.contentType = contentType;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    @Override
    public String toString() {
        return
                "ItemsItem{" +
                        "contentThumbnailUrl = '" + contentThumbnailUrl + '\'' +
                        ",videoType = '" + videoType + '\'' +
                        ",assetLink = '" + assetLink + '\'' +
                        ",description = '" + description + '\'' +
                        ",assetKeywords = '" + assetKeywords + '\'' +
                        ",contentTitle = '" + contentTitle + '\'' +
                        ",transcodingJobId = '" + transcodingJobId + '\'' +
                        ",playUrl = '" + playUrl + '\'' +
                        ",duration = '" + duration + '\'' +
                        ",contentProvider = '" + contentProvider + '\'' +
                        ",landscapeImage = '" + landscapeImage + '\'' +
                        ",premium = '" + premium + '\'' +
                        ",casts = '" + casts + '\'' +
                        ",genres = '" + genres + '\'' +
                        ",series = '" + series + '\'' +
                        ",season = '" + season + '\'' +
                        ",id = '" + id + '\'' +
                        ",portraitImage = '" + portraitImage + '\'' +
                        ",contentType = '" + contentType + '\'' +
                        ",status = '" + status + '\'' +
                        "}";
    }
}