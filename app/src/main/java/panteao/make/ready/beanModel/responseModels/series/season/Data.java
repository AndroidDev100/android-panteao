package panteao.make.ready.beanModel.responseModels.series.season;

import com.google.gson.annotations.SerializedName;

import java.util.List;


public class Data {

    @SerializedName("pageInfo")
    private PageInfo pageInfo;

    @SerializedName("name")
    private String name;

    @SerializedName("id")
    private int id;

    @SerializedName("items")
    private List<ItemsItem> items;

    @SerializedName("picture")
    private String picture;

    @SerializedName("status")
    private Object status;


    @SerializedName("seasonCount")
    private int seasonCount;

    public int getSeasonCount() {
        return seasonCount;
    }

    public void setSeasonCount(int seasonCount) {
        this.seasonCount = seasonCount;
    }

    public PageInfo getPageInfo() {
        return pageInfo;
    }

    public void setPageInfo(PageInfo pageInfo) {
        this.pageInfo = pageInfo;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public List<ItemsItem> getItems() {
        return items;
    }

    public void setItems(List<ItemsItem> items) {
        this.items = items;
    }

    public String getPicture() {
        return picture;
    }

    public void setPicture(String picture) {
        this.picture = picture;
    }

    public Object getStatus() {
        return status;
    }

    public void setStatus(Object status) {
        this.status = status;
    }

    @Override
    public String toString() {
        return
                "PlayListDetailsResponse{" +
                        "pageInfo = '" + pageInfo + '\'' +
                        ",name = '" + name + '\'' +
                        ",id = '" + id + '\'' +
                        ",items = '" + items + '\'' +
                        ",picture = '" + picture + '\'' +
                        ",status = '" + status + '\'' +
                        "}";
    }
}