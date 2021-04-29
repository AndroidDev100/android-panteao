package panteao.make.ready.userAssetList;

import java.util.List;

public class Data {

    private int type;

    private List<ContentsItem> contents;

    public List<ContentsItem> getContents() {
        return contents;
    }

    public void setContents(List<ContentsItem> contents) {
        this.contents = contents;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    @Override
    public String toString() {
        return
                "PlayListDetailsResponse{" +
                        "contents = '" + contents + '\'' +
                        "}";
    }
}