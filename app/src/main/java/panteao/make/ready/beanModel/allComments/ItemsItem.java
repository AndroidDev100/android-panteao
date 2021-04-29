package panteao.make.ready.beanModel.allComments;

public class ItemsItem {
    private String image;
    private long dateCreated;
    private String name;
    private int videoId;
    private String id;
    private String commentText;
    private int userId;
    private Object seriesId;
    private boolean status;

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public long getDateCreated() {
        return dateCreated;
    }

    public void setDateCreated(long dateCreated) {
        this.dateCreated = dateCreated;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getVideoId() {
        return videoId;
    }

    public void setVideoId(int videoId) {
        this.videoId = videoId;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getCommentText() {
        return commentText;
    }

    public void setCommentText(String commentText) {
        this.commentText = commentText;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public Object getSeriesId() {
        return seriesId;
    }

    public void setSeriesId(Object seriesId) {
        this.seriesId = seriesId;
    }

    public boolean isStatus() {
        return status;
    }

    public void setStatus(boolean status) {
        this.status = status;
    }

    @Override
    public String toString() {
        return
                "ItemsItem{" +
                        "image = '" + image + '\'' +
                        ",dateCreated = '" + dateCreated + '\'' +
                        ",name = '" + name + '\'' +
                        ",videoId = '" + videoId + '\'' +
                        ",id = '" + id + '\'' +
                        ",commentText = '" + commentText + '\'' +
                        ",userId = '" + userId + '\'' +
                        ",seriesId = '" + seriesId + '\'' +
                        ",status = '" + status + '\'' +
                        "}";
    }
}
