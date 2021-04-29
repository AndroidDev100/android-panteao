package panteao.make.ready.beanModel.enveuCommonRailData.enveuBeans.seriessearch;

public class PageInfo {
    private int perpage;
    private int total;
    private int pages;
    private Object field;
    private Object page;
    private Object sort;

    public int getPerpage() {
        return perpage;
    }

    public void setPerpage(int perpage) {
        this.perpage = perpage;
    }

    public int getTotal() {
        return total;
    }

    public void setTotal(int total) {
        this.total = total;
    }

    public int getPages() {
        return pages;
    }

    public void setPages(int pages) {
        this.pages = pages;
    }

    public Object getField() {
        return field;
    }

    public void setField(Object field) {
        this.field = field;
    }

    public Object getPage() {
        return page;
    }

    public void setPage(Object page) {
        this.page = page;
    }

    public Object getSort() {
        return sort;
    }

    public void setSort(Object sort) {
        this.sort = sort;
    }

    @Override
    public String toString() {
        return
                "PageInfo{" +
                        "perpage = '" + perpage + '\'' +
                        ",total = '" + total + '\'' +
                        ",pages = '" + pages + '\'' +
                        ",field = '" + field + '\'' +
                        ",page = '" + page + '\'' +
                        ",sort = '" + sort + '\'' +
                        "}";
    }
}
