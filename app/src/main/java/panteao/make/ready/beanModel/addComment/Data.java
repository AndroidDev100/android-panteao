package panteao.make.ready.beanModel.addComment;

@SuppressWarnings("unused")
public class Data {
    private Object lastUpdated;
    private long dateCreated;
    private int videoId;
    private String id;
    private String commentText;
    private int userId;
    private Object seriesId;
    private boolean status;
    private String image;


    @SuppressWarnings("unused")
    public String getImage() {
        return image;
    }

    @SuppressWarnings("unused")
    public void setImage(String image) {
        this.image = image;
    }

    @SuppressWarnings("unused")
    public Object getLastUpdated() {
        return lastUpdated;
    }

    @SuppressWarnings("unused")
    public void setLastUpdated(Object lastUpdated) {
        this.lastUpdated = lastUpdated;
    }

    public long getDateCreated() {
        return dateCreated;
    }

    @SuppressWarnings("unused")
    public void setDateCreated(long dateCreated) {
        this.dateCreated = dateCreated;
    }

    public int getVideoId() {
        return videoId;
    }

    @SuppressWarnings("unused")
    public void setVideoId(int videoId) {
        this.videoId = videoId;
    }

    public String getId() {
        return id;
    }

    @SuppressWarnings("unused")
    public void setId(String id) {
        this.id = id;
    }

    public String getCommentText() {
        return commentText;
    }

    @SuppressWarnings("unused")
    public void setCommentText(String commentText) {
        this.commentText = commentText;
    }

    public int getUserId() {
        return userId;
    }

    @SuppressWarnings("unused")
    public void setUserId(int userId) {
        this.userId = userId;
    }

    public Object getSeriesId() {
        return seriesId;
    }

    @SuppressWarnings("unused")
    public void setSeriesId(Object seriesId) {
        this.seriesId = seriesId;
    }

    @SuppressWarnings("unused")
    public boolean isStatus() {
        return status;
    }

    @SuppressWarnings("unused")
    public void setStatus(boolean status) {
        this.status = status;
    }

    @Override
    public String toString() {
        return
                "PlayListDetailsResponse{" +
                        "lastUpdated = '" + lastUpdated + '\'' +
                        ",dateCreated = '" + dateCreated + '\'' +
                        ",videoId = '" + videoId + '\'' +
                        ",id = '" + id + '\'' +
                        ",commentText = '" + commentText + '\'' +
                        ",userId = '" + userId + '\'' +
                        ",seriesId = '" + seriesId + '\'' +
                        ",status = '" + status + '\'' +
                        "}";
    }
}
