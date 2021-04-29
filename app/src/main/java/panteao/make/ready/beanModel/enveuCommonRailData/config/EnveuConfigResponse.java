package panteao.make.ready.beanModel.enveuCommonRailData.config;

import com.google.gson.annotations.Expose;

import java.util.Observable;

public class EnveuConfigResponse extends Observable {
    @Expose
    private Data data;
    @Expose
    private Object debugMessage;
    @Expose
    private Long responseCode;

    public Data getData() {
        return data;
    }

    public void setData(Data data) {
        this.data = data;
    }

    public Object getDebugMessage() {
        return debugMessage;
    }

    public void setDebugMessage(Object debugMessage) {
        this.debugMessage = debugMessage;
    }

    public Long getResponseCode() {
        return responseCode;
    }

    public void setResponseCode(Long responseCode) {
        this.responseCode = responseCode;
    }

}
