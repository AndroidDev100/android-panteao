package panteao.make.ready.beanModel.responseModels.landingTabResponses.railData;

import panteao.make.ready.beanModel.AssetHistoryContinueWatching.ItemsItem;

public class ContentsItem {
    private Object transcodedFlavoursPlaylistLinks;
    private Object assetLink;
    private String description;
    private Object assetKeywords;
    private Object transcodedMasterPlaylistLink;
    private String title;
    private Object transcodingJobId;
    private String assetType;
    private Object contentProvider;
    private Object duration;
    private Object assetCast;
    private String landscapeImage;
    private boolean premium;
    private Object series;
    private Object assetGenres;
    private String contentUniquenessType;
    private Object season;
    private long publishedDate;
    private String portraitImage;
    private String status;
    private Object thumbnailURL;
    private int id;
    private String name;
    private String picture;
    private boolean isNew;
    private String posterLandscapeImage;
    private String posterPortraitImage;


    private ItemsItem userAssetStatus;
    private panteao.make.ready.userAssetList.ContentsItem userAssetDetail;
    private boolean isContiue;

    public ItemsItem getUserAssetStatus() {
        return userAssetStatus;
    }

    public void setUserAssetStatus(ItemsItem userAssetStatus) {
        this.userAssetStatus = userAssetStatus;
    }

    public panteao.make.ready.userAssetList.ContentsItem getUserAssetDetail() {
        return userAssetDetail;
    }

    public void setUserAssetDetail(panteao.make.ready.userAssetList.ContentsItem userAssetDetail) {
        this.userAssetDetail = userAssetDetail;
    }

    public boolean isContiue() {
        return isContiue;
    }

    public void setContiue(boolean contiue) {
        isContiue = contiue;
    }


    public boolean isNew() {
        return isNew;
    }

    public void setNew(boolean aNew) {
        isNew = aNew;
    }

    public String getPosterLandscapeImage() {
        return posterLandscapeImage;
    }

    public void setPosterLandscapeImage(String posterLandscapeImage) {
        this.posterLandscapeImage = posterLandscapeImage;
    }

    public String getPosterPortraitImage() {
        return posterPortraitImage;
    }

    public void setPosterPortraitImage(String posterPortraitImage) {
        this.posterPortraitImage = posterPortraitImage;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPicture() {
        return picture;
    }

    public void setPicture(String picture) {
        this.picture = picture;
    }

    public Object getTranscodedFlavoursPlaylistLinks() {
        return transcodedFlavoursPlaylistLinks;
    }

    public void setTranscodedFlavoursPlaylistLinks(Object transcodedFlavoursPlaylistLinks) {
        this.transcodedFlavoursPlaylistLinks = transcodedFlavoursPlaylistLinks;
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

    public Object getTranscodedMasterPlaylistLink() {
        return transcodedMasterPlaylistLink;
    }

    public void setTranscodedMasterPlaylistLink(Object transcodedMasterPlaylistLink) {
        this.transcodedMasterPlaylistLink = transcodedMasterPlaylistLink;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Object getTranscodingJobId() {
        return transcodingJobId;
    }

    public void setTranscodingJobId(Object transcodingJobId) {
        this.transcodingJobId = transcodingJobId;
    }

    public String getAssetType() {
        return assetType;
    }

    public void setAssetType(String assetType) {
        this.assetType = assetType;
    }

    public Object getContentProvider() {
        return contentProvider;
    }

    public void setContentProvider(Object contentProvider) {
        this.contentProvider = contentProvider;
    }

    public Object getDuration() {
        return duration;
    }

    public void setDuration(Object duration) {
        this.duration = duration;
    }

    public Object getAssetCast() {
        return assetCast;
    }

    public void setAssetCast(Object assetCast) {
        this.assetCast = assetCast;
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

    public Object getSeries() {
        return series;
    }

    public void setSeries(Object series) {
        this.series = series;
    }

    public Object getAssetGenres() {
        return assetGenres;
    }

    public void setAssetGenres(Object assetGenres) {
        this.assetGenres = assetGenres;
    }

    public String getContentUniquenessType() {
        return contentUniquenessType;
    }

    public void setContentUniquenessType(String contentUniquenessType) {
        this.contentUniquenessType = contentUniquenessType;
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

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Object getThumbnailURL() {
        return thumbnailURL;
    }

    public void setThumbnailURL(Object thumbnailURL) {
        this.thumbnailURL = thumbnailURL;
    }

    @Override
    public String toString() {
        return
                "ContentsItem{" +
                        "name = '" + name + '\'' +
                        ",id = '" + id + '\'' +
                        ",picture = '" + picture + '\'' +
                        ",status = '" + status + '\'' +
                        "transcodedFlavoursPlaylistLinks = '" + transcodedFlavoursPlaylistLinks + '\'' +
                        ",assetLink = '" + assetLink + '\'' +
                        ",description = '" + description + '\'' +
                        ",assetKeywords = '" + assetKeywords + '\'' +
                        ",transcodedMasterPlaylistLink = '" + transcodedMasterPlaylistLink + '\'' +
                        ",title = '" + title + '\'' +
                        ",transcodingJobId = '" + transcodingJobId + '\'' +
                        ",assetType = '" + assetType + '\'' +
                        ",contentProvider = '" + contentProvider + '\'' +
                        ",duration = '" + duration + '\'' +
                        ",assetCast = '" + assetCast + '\'' +
                        ",landscapeImage = '" + landscapeImage + '\'' +
                        ",premium = '" + premium + '\'' +
                        ",series = '" + series + '\'' +
                        ",assetGenres = '" + assetGenres + '\'' +
                        ",contentUniquenessType = '" + contentUniquenessType + '\'' +
                        ",season = '" + season + '\'' +
                        ",id = '" + id + '\'' +
                        ",publishedDate = '" + publishedDate + '\'' +
                        ",portraitImage = '" + portraitImage + '\'' +
                        ",status = '" + status + '\'' +
                        ",thumbnailURL = '" + thumbnailURL + '\'' +
                        "}";
    }
}
