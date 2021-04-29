package panteao.make.ready.beanModel.responseModels.landingTabResponses.playlistResponse;

import com.google.gson.annotations.SerializedName;

import java.util.List;


public class Data {

    @SerializedName("playlists")
    private List<PlaylistsItem> playlists;

    @SerializedName("screenType")
    private String screenType;

    @SerializedName("screenName")
    private String screenName;

    @SerializedName("noOfPlaylists")
    private int noOfPlaylists;

    @SerializedName("screenIdentifier")
    private String screenIdentifier;

    public List<PlaylistsItem> getPlaylists() {
        return playlists;
    }

    public void setPlaylists(List<PlaylistsItem> playlists) {
        this.playlists = playlists;
    }

    public String getScreenType() {
        return screenType;
    }

    public void setScreenType(String screenType) {
        this.screenType = screenType;
    }

    public String getScreenName() {
        return screenName;
    }

    public void setScreenName(String screenName) {
        this.screenName = screenName;
    }

    public int getNoOfPlaylists() {
        return noOfPlaylists;
    }

    public void setNoOfPlaylists(int noOfPlaylists) {
        this.noOfPlaylists = noOfPlaylists;
    }

    public String getScreenIdentifier() {
        return screenIdentifier;
    }

    public void setScreenIdentifier(String screenIdentifier) {
        this.screenIdentifier = screenIdentifier;
    }

    @Override
    public String toString() {
        return
                "PlayListDetailsResponse{" +
                        "playlists = '" + playlists + '\'' +
                        ",screenType = '" + screenType + '\'' +
                        ",screenName = '" + screenName + '\'' +
                        ",noOfPlaylists = '" + noOfPlaylists + '\'' +
                        ",screenIdentifier = '" + screenIdentifier + '\'' +
                        "}";
    }
}