package panteao.make.ready.beanModel.enveuCommonRailData.enveuBeans.newsearch;

import java.util.List;

public class Data {
    private PageInfo pageInfo;
    private List<SearchItems> items;

    public PageInfo getPageInfo() {
        return pageInfo;
    }

    public void setPageInfo(PageInfo pageInfo) {
        this.pageInfo = pageInfo;
    }

    public List<SearchItems> getItems() {
        return items;
    }

    public void setItems(List<SearchItems> items) {
        this.items = items;
    }

    @Override
    public String toString() {
        return
                "Data{" +
                        "pageInfo = '" + pageInfo + '\'' +
                        ",items = '" + items + '\'' +
                        "}";
    }
}