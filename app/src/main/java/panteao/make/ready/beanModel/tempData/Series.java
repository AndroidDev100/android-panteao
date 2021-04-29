package panteao.make.ready.beanModel.tempData;

public class Series {
    private Object posterPortraitImage;
    private Object vodCount;
    private Object seasonCount;
    private Object posterLandscapeImage;
    private String name;
    private Object description;
    private int id;
    private Object isNew;
    private String picture;
    private String status;
    private Object seriesSeasonDTOS;

    public Object getPosterPortraitImage() {
        return posterPortraitImage;
    }

    public void setPosterPortraitImage(Object posterPortraitImage) {
        this.posterPortraitImage = posterPortraitImage;
    }

    public Object getVodCount() {
        return vodCount;
    }

    public void setVodCount(Object vodCount) {
        this.vodCount = vodCount;
    }

    public Object getSeasonCount() {
        return seasonCount;
    }

    public void setSeasonCount(Object seasonCount) {
        this.seasonCount = seasonCount;
    }

    public Object getPosterLandscapeImage() {
        return posterLandscapeImage;
    }

    public void setPosterLandscapeImage(Object posterLandscapeImage) {
        this.posterLandscapeImage = posterLandscapeImage;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
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

    public Object getIsNew() {
        return isNew;
    }

    public void setIsNew(Object isNew) {
        this.isNew = isNew;
    }

    public String getPicture() {
        return picture;
    }

    public void setPicture(String picture) {
        this.picture = picture;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Object getSeriesSeasonDTOS() {
        return seriesSeasonDTOS;
    }

    public void setSeriesSeasonDTOS(Object seriesSeasonDTOS) {
        this.seriesSeasonDTOS = seriesSeasonDTOS;
    }

    @Override
    public String toString() {
        return
                "Series{" +
                        "posterPortraitImage = '" + posterPortraitImage + '\'' +
                        ",vodCount = '" + vodCount + '\'' +
                        ",seasonCount = '" + seasonCount + '\'' +
                        ",posterLandscapeImage = '" + posterLandscapeImage + '\'' +
                        ",name = '" + name + '\'' +
                        ",description = '" + description + '\'' +
                        ",id = '" + id + '\'' +
                        ",isNew = '" + isNew + '\'' +
                        ",picture = '" + picture + '\'' +
                        ",status = '" + status + '\'' +
                        ",seriesSeasonDTOS = '" + seriesSeasonDTOS + '\'' +
                        "}";
    }
}
