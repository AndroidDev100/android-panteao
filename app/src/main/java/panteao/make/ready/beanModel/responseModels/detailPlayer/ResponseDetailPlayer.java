package panteao.make.ready.beanModel.responseModels.detailPlayer;

import java.io.Serializable;

public class ResponseDetailPlayer implements Serializable {
    private Data data;
    private Object debugMessage;
    private int responseCode;
    private boolean status;

    public boolean isStatus() {
        return status;
    }

    public void setStatus(boolean status) {
        this.status = status;
    }

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

    public int getResponseCode() {
        return responseCode;
    }

    public void setResponseCode(int responseCode) {
        this.responseCode = responseCode;
    }

    @Override
    public String toString() {
        return
                "ResponseDetailPlayer{" +
                        "data = '" + data + '\'' +
                        ",debugMessage = '" + debugMessage + '\'' +
                        ",responseCode = '" + responseCode + '\'' +
                        "}";
    }
}
