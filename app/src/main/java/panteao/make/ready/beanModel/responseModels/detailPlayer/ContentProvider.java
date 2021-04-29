package panteao.make.ready.beanModel.responseModels.detailPlayer;

import java.io.Serializable;

public class ContentProvider implements Serializable {
    private String name;
    private int id;
    private Object status;

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

    public Object getStatus() {
        return status;
    }

    public void setStatus(Object status) {
        this.status = status;
    }

    @Override
    public String toString() {
        return
                "ContentProvider{" +
                        "name = '" + name + '\'' +
                        ",id = '" + id + '\'' +
                        ",status = '" + status + '\'' +
                        "}";
    }
}
