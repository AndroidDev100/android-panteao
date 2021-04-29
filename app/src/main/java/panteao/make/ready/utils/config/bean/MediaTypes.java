
package panteao.make.ready.utils.config.bean;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class MediaTypes {

    @SerializedName("movie")
    @Expose
    private String movie;
    @SerializedName("live")
    @Expose
    private String live;
    @SerializedName("show")
    @Expose
    private String show;
    @SerializedName("series")
    @Expose
    private String series;
    @SerializedName("episode")
    @Expose
    private String episode;

    public String getMovie() {
        return movie;
    }

    public void setMovie(String movie) {
        this.movie = movie;
    }

    public String getLive() {
        return live;
    }

    public void setLive(String live) {
        this.live = live;
    }

    public String getShow() {
        return show;
    }

    public void setShow(String show) {
        this.show = show;
    }

    public String getSeries() {
        return series;
    }

    public void setSeries(String series) {
        this.series = series;
    }

    public String getEpisode() {
        return episode;
    }

    public void setEpisode(String episode) {
        this.episode = episode;
    }

}
