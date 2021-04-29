package panteao.make.ready.beanModel.enveuCommonRailData.enveuBeans.seriessearch;

public class ItemsItem {
    private int vodCount;
    private String posterURL;
    private int seasonCount;
    private String brightcoveSeriesId;
    private Object description;
    private int id;
    private String title;
    private String thumbnailURL;
    private String status;

    public int getVodCount() {
        return vodCount;
    }

    public void setVodCount(int vodCount) {
        this.vodCount = vodCount;
    }

    public String getPosterURL() {
        return posterURL;
    }

    public void setPosterURL(String posterURL) {
        this.posterURL = posterURL;
    }

    public int getSeasonCount() {
        return seasonCount;
    }

    public void setSeasonCount(int seasonCount) {
        this.seasonCount = seasonCount;
    }

    public String getBrightcoveSeriesId() {
        return brightcoveSeriesId;
    }

    public void setBrightcoveSeriesId(String brightcoveSeriesId) {
        this.brightcoveSeriesId = brightcoveSeriesId;
    }

    public Object getDescription() {
        return description;
    }

    public void setDescription(Object description) {
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

    public String getThumbnailURL() {
        return thumbnailURL;
    }

    public void setThumbnailURL(String thumbnailURL) {
        this.thumbnailURL = thumbnailURL;
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
                        "vodCount = '" + vodCount + '\'' +
                        ",posterURL = '" + posterURL + '\'' +
                        ",seasonCount = '" + seasonCount + '\'' +
                        ",brightcoveSeriesId = '" + brightcoveSeriesId + '\'' +
                        ",description = '" + description + '\'' +
                        ",id = '" + id + '\'' +
                        ",title = '" + title + '\'' +
                        ",thumbnailURL = '" + thumbnailURL + '\'' +
                        ",status = '" + status + '\'' +
                        "}";
    }
}
