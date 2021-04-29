package panteao.make.ready.beanModel.enveuCommonRailData.enveuBeans;

import panteao.make.ready.beanModelV3.playListModelV2.VideosItem;

import java.util.List;

public class PlayListDetails {
    private int viewType;
    private String identifier;
    private String brightcovePlaylistId;
    private int maxContent;
    private String displayName;
    private String playlistType;
    private List<VideosItem> videos;
    private long id;

    public int getViewType() {
        return viewType;
    }

    public void setViewType(int viewType) {
        this.viewType = viewType;
    }

    public String getIdentifier() {
        return identifier;
    }

    public void setIdentifier(String identifier) {
        this.identifier = identifier;
    }

    public String getBrightcovePlaylistId() {
        return brightcovePlaylistId;
    }

    public void setBrightcovePlaylistId(String brightcovePlaylistId) {
        this.brightcovePlaylistId = brightcovePlaylistId;
    }

    public int getMaxContent() {
        return maxContent;
    }

    public void setMaxContent(int maxContent) {
        this.maxContent = maxContent;
    }

    public String getDisplayName() {
        return displayName;
    }

    public void setDisplayName(String displayName) {
        this.displayName = displayName;
    }

    public String getPlaylistType() {
        return playlistType;
    }

    public void setPlaylistType(String playlistType) {
        this.playlistType = playlistType;
    }

    public List<VideosItem> getVideos() {
        return videos;
    }

    public void setVideos(List<VideosItem> videos) {
        this.videos = videos;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    @Override
    public String toString() {
        return
                "PlayListDetailsResponse{" +
                        "identifier = '" + identifier + '\'' +
                        ",brightcovePlaylistId = '" + brightcovePlaylistId + '\'' +
                        ",maxContent = '" + maxContent + '\'' +
                        ",displayName = '" + displayName + '\'' +
                        ",playlistType = '" + playlistType + '\'' +
                        ",videos = '" + videos + '\'' +
                        ",id = '" + id + '\'' +
                        "}";
    }
}
