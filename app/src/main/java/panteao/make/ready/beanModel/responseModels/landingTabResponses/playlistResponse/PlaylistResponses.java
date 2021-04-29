package panteao.make.ready.beanModel.responseModels.landingTabResponses.playlistResponse;

import com.google.gson.annotations.SerializedName;


public class PlaylistResponses {

    public boolean status;
    @SerializedName("data")
    private Data data;
    @SerializedName("debugMessage")
    private Object debugMessage;
    @SerializedName("responseCode")
    private int responseCode;

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
                "PlaylistResponses{" +
                        "data = '" + data + '\'' +
                        ",debugMessage = '" + debugMessage + '\'' +
                        ",responseCode = '" + responseCode + '\'' +
                        "}";
    }
}