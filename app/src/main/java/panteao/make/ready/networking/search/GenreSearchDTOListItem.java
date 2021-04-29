package panteao.make.ready.networking.search;

public class GenreSearchDTOListItem {
    private String genreKey;
    private String genreName;
    private int id;

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

    @Override
    public String toString() {
        return
                "GenreSearchDTOListItem{" +
                        "genreKey = '" + genreKey + '\'' +
                        ",genreName = '" + genreName + '\'' +
                        ",id = '" + id + '\'' +
                        "}";
    }
}
