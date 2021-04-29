package panteao.make.ready.beanModel.watchHistory;

import java.util.List;

public class ItemsItem {
    private String imageLandscape;
    private List<String> genres;
    private int id;
    private String title;
    private String status;
    private String videoType;

    public String getVideoType() {
        return videoType;
    }

    public void setVideoType(String videoType) {
        this.videoType = videoType;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getImageLandscape() {
        return imageLandscape;
    }

    public void setImageLandscape(String imageLandscape) {
        this.imageLandscape = imageLandscape;
    }

    public List<String> getGenres() {
        return genres;
    }

    public void setGenres(List<String> genres) {
        this.genres = genres;
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

    @Override
    public String toString() {
        return
                "ItemsItem{" +
                        "imageLandscape = '" + imageLandscape + '\'' +
                        ",genres = '" + genres + '\'' +
                        ",id = '" + id + '\'' +
                        ",title = '" + title + '\'' +
                        "}";
    }
}