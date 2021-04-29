package panteao.make.ready.beanModel.responseModels.series.season;

import com.google.gson.annotations.SerializedName;


public class PageInfo {

    @SerializedName("perpage")
    private int perpage;

    @SerializedName("total")
    private int total;

    @SerializedName("pages")
    private int pages;

    @SerializedName("field")
    private Object field;

    @SerializedName("page")
    private int page;

    @SerializedName("sort")
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

    public int getPage() {
        return page;
    }

    public void setPage(int page) {
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