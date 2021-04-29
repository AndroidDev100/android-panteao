package panteao.make.ready.beanModel.responseModels.landingTabResponses.playlistResponse;

import com.google.gson.annotations.SerializedName;

public class PlaylistsItem {

    @SerializedName("name")
    private String name;

    @SerializedName("index")
    private int index;

    @SerializedName("id")
    private int id;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getIndex() {
        return index;
    }

    public void setIndex(int index) {
        this.index = index;
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
                "PlaylistsItem{" +
                        "name = '" + name + '\'' +
                        ",index = '" + index + '\'' +
                        ",id = '" + id + '\'' +
                        "}";
    }
}