package panteao.make.ready.beanModel.responseModels.series.season;

import com.google.gson.annotations.SerializedName;

public class SeasonResponse {

    @SerializedName("data")
    private Data data;

    @SerializedName("debugMessage")
    private Object debugMessage;

    @SerializedName("responseCode")
    private int responseCode;

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
                "SeasonResponse{" +
                        "data = '" + data + '\'' +
                        ",debugMessage = '" + debugMessage + '\'' +
                        ",responseCode = '" + responseCode + '\'' +
                        "}";
    }
}