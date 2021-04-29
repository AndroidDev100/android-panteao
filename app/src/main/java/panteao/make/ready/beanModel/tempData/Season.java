package panteao.make.ready.beanModel.tempData;

public class Season {
    private String name;
    private int id;
    private String picture;
    private String status;
    private int seasonNo;

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

    public String getPicture() {
        return picture;
    }

    public void setPicture(String picture) {
        this.picture = picture;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public int getSeasonNo() {
        return seasonNo;
    }

    public void setSeasonNo(int seasonNo) {
        this.seasonNo = seasonNo;
    }

    @Override
    public String toString() {
        return
                "Season{" +
                        "name = '" + name + '\'' +
                        ",id = '" + id + '\'' +
                        ",picture = '" + picture + '\'' +
                        ",status = '" + status + '\'' +
                        ",seasonNo = '" + seasonNo + '\'' +
                        "}";
    }
}
