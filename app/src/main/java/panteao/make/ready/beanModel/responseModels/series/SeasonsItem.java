package panteao.make.ready.beanModel.responseModels.series;

import com.google.gson.annotations.SerializedName;


public class SeasonsItem {

    @SerializedName("name")
    private String name;

    @SerializedName("id")
    private int id;


    @SerializedName("seasonNo")
    private int seasonNo;

    public int getSeasonNo() {
        return seasonNo;
    }

    public void setSeasonNo(int seasonNo) {
        this.seasonNo = seasonNo;
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

    @Override
    public String toString() {
        return
                "SeasonsItem{" +
                        "name = '" + name + '\'' +
                        ",id = '" + id + '\'' +
                        "}";
    }
}