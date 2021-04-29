package panteao.make.ready.beanModel.responseModels.landingTabResponses.railData;

import java.util.List;

public class Data {
    private String identifier;
    private String verticalGridSize;
    private Pagination pagination;
    private int maxContent;
    private String displayName;
    private String contentImageType;
    private String defaultDetailView;
    private String playlistContentAlgorithm;
    private List<ContentsItem> contents;
    private String playlistType;
    private String layoutType;
    private String contentType;
    private int index;
    private boolean isNew;

    private int id;


    public boolean isNew() {
        return isNew;
    }

    public void setNew(boolean aNew) {
        isNew = aNew;
    }


    public int getIndex() {
        return index;
    }

    public void setIndex(int index) {
        this.index = index;
    }

    public String getIdentifier() {
        return identifier;
    }

    public void setIdentifier(String identifier) {
        this.identifier = identifier;
    }

    public String getVerticalGridSize() {
        return verticalGridSize;
    }

    public void setVerticalGridSize(String verticalGridSize) {
        this.verticalGridSize = verticalGridSize;
    }

    public Pagination getPagination() {
        return pagination;
    }

    public void setPagination(Pagination pagination) {
        this.pagination = pagination;
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

    public String getContentImageType() {
        return contentImageType;
    }

    public void setContentImageType(String contentImageType) {
        this.contentImageType = contentImageType;
    }

    public String getDefaultDetailView() {
        return defaultDetailView;
    }

    public void setDefaultDetailView(String defaultDetailView) {
        this.defaultDetailView = defaultDetailView;
    }

    public String getPlaylistContentAlgorithm() {
        return playlistContentAlgorithm;
    }

    public void setPlaylistContentAlgorithm(String playlistContentAlgorithm) {
        this.playlistContentAlgorithm = playlistContentAlgorithm;
    }

    public List<ContentsItem> getContents() {
        return contents;
    }

    public void setContents(List<ContentsItem> contents) {
        this.contents = contents;
    }

    public String getPlaylistType() {
        return playlistType;
    }

    public void setPlaylistType(String playlistType) {
        this.playlistType = playlistType;
    }

    public String getLayoutType() {
        return layoutType;
    }

    public void setLayoutType(String layoutType) {
        this.layoutType = layoutType;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
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
                "PlayListDetailsResponse{" +
                        "identifier = '" + identifier + '\'' +
                        ",verticalGridSize = '" + verticalGridSize + '\'' +
                        ",pagination = '" + pagination + '\'' +
                        ",maxContent = '" + maxContent + '\'' +
                        ",displayName = '" + displayName + '\'' +
                        ",contentImageType = '" + contentImageType + '\'' +
                        ",defaultDetailView = '" + defaultDetailView + '\'' +
                        ",playlistContentAlgorithm = '" + playlistContentAlgorithm + '\'' +
                        ",contents = '" + contents + '\'' +
                        ",playlistType = '" + playlistType + '\'' +
                        ",layoutType = '" + layoutType + '\'' +
                        ",id = '" + id + '\'' +
                        ",contentType = '" + contentType + '\'' +
                        "}";
    }
}