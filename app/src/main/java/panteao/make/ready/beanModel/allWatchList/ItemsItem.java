package panteao.make.ready.beanModel.allWatchList;

import java.util.List;

public class ItemsItem {
    private List<String> genres;
    private String imageUrl;
    private int contentId;
    private int id;
    private String title;
    private String contentType;
    private String status;
    private String assetType;


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

    public List<String> getGenres() {
        return genres;
    }

    public void setGenres(List<String> genres) {
        this.genres = genres;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public int getContentId() {
        return contentId;
    }

    public void setContentId(int contentId) {
        this.contentId = contentId;
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

    public String getContentType() {
        return contentType;
    }

    public void setContentType(String contentType) {
        this.contentType = contentType;
    }

    @Override
    public String toString() {
        return
                "ItemsItem{" +
                        "genres = '" + genres + '\'' +
                        ",imageUrl = '" + imageUrl + '\'' +
                        ",contentId = '" + contentId + '\'' +
                        ",id = '" + id + '\'' +
                        ",title = '" + title + '\'' +
                        ",contentType = '" + contentType + '\'' +
                        "}";
    }
}