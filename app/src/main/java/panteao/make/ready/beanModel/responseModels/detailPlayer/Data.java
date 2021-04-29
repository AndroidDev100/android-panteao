package panteao.make.ready.beanModel.responseModels.detailPlayer;

import java.io.Serializable;
import java.util.List;

public class Data implements Serializable {
    private String vastTag;
    private Object contentThumbnailUrl;
    private String videoType;
    private Object episodeNo;
    private String assetLink;
    private String description;
    private List<Object> assetKeywords;
    private String contentTitle;
    private Object transcodingJobId;
    private String playUrl;
    private int duration;
    private ContentProvider contentProvider;
    private String landscapeImage;
    private boolean premium;
    private List<String> casts;
    private List<String> genres;
    private Series series;
    private String contentUniquenessType;
    private Season season;
    private int id;
    private long publishedDate;
    private String portraitImage;
    private String sku;
    private String status;

    public String getVastTag() {
        return vastTag;
    }

    public void setVastTag(String vastTag) {
        this.vastTag = vastTag;
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

    public Object getEpisodeNo() {
        return episodeNo;
    }

    public void setEpisodeNo(Object episodeNo) {
        this.episodeNo = episodeNo;
    }

    public String getAssetLink() {
        return assetLink;
    }

    public void setAssetLink(String assetLink) {
        this.assetLink = assetLink;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public List<Object> getAssetKeywords() {
        return assetKeywords;
    }

    public void setAssetKeywords(List<Object> assetKeywords) {
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

    public String getPlayUrl() {
        return playUrl;
    }

    public void setPlayUrl(String playUrl) {
        this.playUrl = playUrl;
    }

    public int getDuration() {
        return duration;
    }

    public void setDuration(int duration) {
        this.duration = duration;
    }

    public ContentProvider getContentProvider() {
        return contentProvider;
    }

    public void setContentProvider(ContentProvider contentProvider) {
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

    public Series getSeries() {
        return series;
    }

    public void setSeries(Series series) {
        this.series = series;
    }

    public String getContentUniquenessType() {
        return contentUniquenessType;
    }

    public void setContentUniquenessType(String contentUniquenessType) {
        this.contentUniquenessType = contentUniquenessType;
    }

    public Season getSeason() {
        return season;
    }

    public void setSeason(Season season) {
        this.season = season;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public long getPublishedDate() {
        return publishedDate;
    }

    public void setPublishedDate(long publishedDate) {
        this.publishedDate = publishedDate;
    }

    public String getPortraitImage() {
        return portraitImage;
    }

    public void setPortraitImage(String portraitImage) {
        this.portraitImage = portraitImage;
    }

    public String getSku() {
        return sku;
    }

    public void setSku(String sku) {
        this.sku = sku;
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
                "PlayListDetailsResponse{" +
                        "vastTag = '" + vastTag + '\'' +
                        ",contentThumbnailUrl = '" + contentThumbnailUrl + '\'' +
                        ",videoType = '" + videoType + '\'' +
                        ",episodeNo = '" + episodeNo + '\'' +
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
                        ",contentUniquenessType = '" + contentUniquenessType + '\'' +
                        ",season = '" + season + '\'' +
                        ",id = '" + id + '\'' +
                        ",publishedDate = '" + publishedDate + '\'' +
                        ",portraitImage = '" + portraitImage + '\'' +
                        ",sku = '" + sku + '\'' +
                        ",status = '" + status + '\'' +
                        "}";
    }
}