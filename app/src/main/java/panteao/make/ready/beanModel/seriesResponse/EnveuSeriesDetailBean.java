package panteao.make.ready.beanModel.seriesResponse;

import java.util.List;

public class EnveuSeriesDetailBean {
    private List<Data> data;
    private Object debugMessage;
    private int responseCode;

    public List<Data>  getData() {
        return data;
    }

    public void setData(List<Data> data) {
        this.data = data;
    }

    public Object getDebugMessage() {
        return debugMessage;
    }

    public void setDebugMessage(Object debugMessage) {
        this.debugMessage = debugMessage;
    }

    public int getResponseCode() {
        return responseCode;
    }

    public void setResponseCode(int responseCode) {
        this.responseCode = responseCode;
    }

    @Override
    public String toString() {
        return
                "EnveuSeriesDetailBean{" +
                        "data = '" + data + '\'' +
                        ",debugMessage = '" + debugMessage + '\'' +
                        ",responseCode = '" + responseCode + '\'' +
                        "}";
    }
}
