package panteao.make.ready.beanModel.allComments;

import java.util.List;

@SuppressWarnings("ALL")
public class Data {
    private PageInfo pageInfo;
    private List<ItemsItem> items;

    public PageInfo getPageInfo() {
        return pageInfo;
    }

    public void setPageInfo(PageInfo pageInfo) {
        this.pageInfo = pageInfo;
    }

    public List<ItemsItem> getItems() {
        return items;
    }

    public void setItems(List<ItemsItem> items) {
        this.items = items;
    }

    @Override
    public String toString() {
        return
                "PlayListDetailsResponse{" +
                        "pageInfo = '" + pageInfo + '\'' +
                        ",items = '" + items + '\'' +
                        "}";
    }
}