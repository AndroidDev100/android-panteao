package panteao.make.ready.beanModel.responseModels.series;

import com.google.gson.annotations.SerializedName;

import java.util.List;


public class Data {

    @SerializedName("seasons")
    private List<SeasonsItem> seasons;

    @SerializedName("vodCount")
    private int vodCount;

    @SerializedName("seasonCount")
    private int seasonCount;

    @SerializedName("name")
    private String name;

    @SerializedName("description")
    private Object description;

    @SerializedName("id")
    private int id;

    @SerializedName("picture")
    private String picture;

    @SerializedName("status")
    private String status;


    @SerializedName("posterLandscapeImage")
    private String posterLandscapeImage;

    @SerializedName("posterPortraitImage")
    private String posterPortraitImage;


    @SerializedName("seriesSeasonDTOS")
    private String seriesSeasonDTOS;

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

    public String getSeriesSeasonDTOS() {
        return seriesSeasonDTOS;
    }

    public void setSeriesSeasonDTOS(String seriesSeasonDTOS) {
        this.seriesSeasonDTOS = seriesSeasonDTOS;
    }

    public List<SeasonsItem> getSeasons() {
        return seasons;
    }

    public void setSeasons(List<SeasonsItem> seasons) {
        this.seasons = seasons;
    }

    public int getVodCount() {
        return vodCount;
    }

    public void setVodCount(int vodCount) {
        this.vodCount = vodCount;
    }

    public int getSeasonCount() {
        return seasonCount;
    }

    public void setSeasonCount(int seasonCount) {
        this.seasonCount = seasonCount;
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

    @Override
    public String toString() {
        return
                "PlayListDetailsResponse{" +
                        "seasons = '" + seasons + '\'' +
                        ",vodCount = '" + vodCount + '\'' +
                        ",seasonCount = '" + seasonCount + '\'' +
                        ",name = '" + name + '\'' +
                        ",description = '" + description + '\'' +
                        ",id = '" + id + '\'' +
                        ",picture = '" + picture + '\'' +
                        ",status = '" + status + '\'' +
                        "}";
    }
}