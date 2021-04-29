package panteao.make.ready.beanModel.popularSearch;

public class ItemsItem {
    private int seasonCount;
    private String name;
    private long publishDate;
    private int episodesCount;
    private int id;
    private String type;
    private String picture;
    private String status;

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

    public long getPublishDate() {
        return publishDate;
    }

    public void setPublishDate(long publishDate) {
        this.publishDate = publishDate;
    }

    public int getEpisodesCount() {
        return episodesCount;
    }

    public void setEpisodesCount(int episodesCount) {
        this.episodesCount = episodesCount;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
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
                "ItemsItem{" +
                        "seasonCount = '" + seasonCount + '\'' +
                        ",name = '" + name + '\'' +
                        ",publishDate = '" + publishDate + '\'' +
                        ",episodesCount = '" + episodesCount + '\'' +
                        ",id = '" + id + '\'' +
                        ",type = '" + type + '\'' +
                        ",picture = '" + picture + '\'' +
                        ",status = '" + status + '\'' +
                        "}";
    }
}
