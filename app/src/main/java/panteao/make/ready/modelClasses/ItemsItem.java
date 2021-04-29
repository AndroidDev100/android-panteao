package panteao.make.ready.modelClasses;

import panteao.make.ready.networking.search.GenreSearchDTOListItem;

import java.util.List;

import panteao.make.ready.networking.search.GenreSearchDTOListItem;

public class ItemsItem {
    private long duration;
    private String landscapeImage;
    private boolean premium;
    private List<Object> keywordSearchDTOList;
    private List<GenreSearchDTOListItem> genreSearchDTOList;
    private String description;
    private int id;
    private String title;
    private String portraitImage;
    private String assetType;
    private String status;
    private Object thumbnailURL;
    private int episodesCount;
    private int seasonCount;

    public int getEpisodesCount() {
        return episodesCount;
    }

    public void setEpisodesCount(int episodesCount) {
        this.episodesCount = episodesCount;
    }

    public int getSeasonCount() {
        return seasonCount;
    }

    public void setSeasonCount(int seasonCount) {
        this.seasonCount = seasonCount;
    }

    public long getDuration() {
        return duration;
    }

    public void setDuration(long duration) {
        this.duration = duration;
    }

    public void setDuration(int duration) {
        this.duration = duration;
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

    public List<Object> getKeywordSearchDTOList() {
        return keywordSearchDTOList;
    }

    public void setKeywordSearchDTOList(List<Object> keywordSearchDTOList) {
        this.keywordSearchDTOList = keywordSearchDTOList;
    }

    public List<GenreSearchDTOListItem> getGenreSearchDTOList() {
        return genreSearchDTOList;
    }

    public void setGenreSearchDTOList(List<GenreSearchDTOListItem> genreSearchDTOList) {
        this.genreSearchDTOList = genreSearchDTOList;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getPortraitImage() {
        return portraitImage;
    }

    public void setPortraitImage(String portraitImage) {
        this.portraitImage = portraitImage;
    }

    public String getAssetType() {
        return assetType;
    }

    public void setAssetType(String assetType) {
        this.assetType = assetType;
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
                "ItemsItem{" +
                        "duration = '" + duration + '\'' +
                        ",landscapeImage = '" + landscapeImage + '\'' +
                        ",premium = '" + premium + '\'' +
                        ",keywordSearchDTOList = '" + keywordSearchDTOList + '\'' +
                        ",genreSearchDTOList = '" + genreSearchDTOList + '\'' +
                        ",description = '" + description + '\'' +
                        ",id = '" + id + '\'' +
                        ",title = '" + title + '\'' +
                        ",portraitImage = '" + portraitImage + '\'' +
                        ",assetType = '" + assetType + '\'' +
                        ",status = '" + status + '\'' +
                        ",thumbnailURL = '" + thumbnailURL + '\'' +
                        "}";
    }
}