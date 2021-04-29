package panteao.make.ready.beanModel.responseModels.responseYouMayLike;

import java.util.List;

public class ItemsItem {
    private Object contentThumbnailUrl;
    private String videoType;
    private Object assetLink;
    private String description;
    private Object assetKeywords;
    private String contentTitle;
    private Object transcodingJobId;
    private Object playUrl;
    private Object duration;
    private Object contentProvider;
    private String landscapeImage;
    private boolean premium;
    private List<String> casts;
    private List<String> genres;
    private Object series;
    private Object season;
    private int id;
    private String portraitImage;
    private String contentType;
    private String status;

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