package panteao.make.ready.beanModel.enveuCommonRailData.enveuBeans.seriessearch;

import panteao.make.ready.beanModel.enveuCommonRailData.SeriesItem;

import java.util.List;

public class Data {
    private PageInfo pageInfo;
    private List<SeriesItem> items;

    public PageInfo getPageInfo() {
        return pageInfo;
    }

    public void setPageInfo(PageInfo pageInfo) {
        this.pageInfo = pageInfo;
    }

    public List<SeriesItem> getItems() {
        return items;
    }

    public void setItems(List<SeriesItem> items) {
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