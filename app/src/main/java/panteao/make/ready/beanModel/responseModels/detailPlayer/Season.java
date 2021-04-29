package panteao.make.ready.beanModel.responseModels.detailPlayer;

import java.io.Serializable;

public class Season implements Serializable {
    private Object name;
    private Object id;
    private Object picture;
    private Object status;

    public Object getName() {
        return name;
    }

    public void setName(Object name) {
        this.name = name;
    }

    public Object getId() {
        return id;
    }

    public void setId(Object id) {
        this.id = id;
    }

    public Object getPicture() {
        return picture;
    }

    public void setPicture(Object picture) {
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
                "Season{" +
                        "name = '" + name + '\'' +
                        ",id = '" + id + '\'' +
                        ",picture = '" + picture + '\'' +
                        ",status = '" + status + '\'' +
                        "}";
    }
}
