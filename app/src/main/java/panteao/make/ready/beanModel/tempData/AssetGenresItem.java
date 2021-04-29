package panteao.make.ready.beanModel.tempData;

public class AssetGenresItem {
    private String genreKey;
    private String genreName;
    private int id;
    private Object genreState;
    private String parentGenreKey;
    private Object picture;
    private String status;

    public String getGenreKey() {
        return genreKey;
    }

    public void setGenreKey(String genreKey) {
        this.genreKey = genreKey;
    }

    public String getGenreName() {
        return genreName;
    }

    public void setGenreName(String genreName) {
        this.genreName = genreName;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Object getGenreState() {
        return genreState;
    }

    public void setGenreState(Object genreState) {
        this.genreState = genreState;
    }

    public String getParentGenreKey() {
        return parentGenreKey;
    }

    public void setParentGenreKey(String parentGenreKey) {
        this.parentGenreKey = parentGenreKey;
    }

    public Object getPicture() {
        return picture;
    }

    public void setPicture(Object picture) {
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
                "AssetGenresItem{" +
                        "genreKey = '" + genreKey + '\'' +
                        ",genreName = '" + genreName + '\'' +
                        ",id = '" + id + '\'' +
                        ",genreState = '" + genreState + '\'' +
                        ",parentGenreKey = '" + parentGenreKey + '\'' +
                        ",picture = '" + picture + '\'' +
                        ",status = '" + status + '\'' +
                        "}";
    }
}
