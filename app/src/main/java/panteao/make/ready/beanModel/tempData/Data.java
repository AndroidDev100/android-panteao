package panteao.make.ready.beanModel.tempData;

import java.util.List;

public class Data {
    private Pagination pagination;
    private List<ContentsItem> contents;

    public Pagination getPagination() {
        return pagination;
    }

    public void setPagination(Pagination pagination) {
        this.pagination = pagination;
    }

    public List<ContentsItem> getContents() {
        return contents;
    }

    public void setContents(List<ContentsItem> contents) {
        this.contents = contents;
    }

    @Override
    public String toString() {
        return
                "PlayListDetailsResponse{" +
                        "pagination = '" + pagination + '\'' +
                        ",contents = '" + contents + '\'' +
                        "}";
    }
}