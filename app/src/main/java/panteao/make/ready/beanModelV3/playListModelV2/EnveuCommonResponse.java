package panteao.make.ready.beanModelV3.playListModelV2;

import com.google.gson.annotations.SerializedName;


public class EnveuCommonResponse {
    @SerializedName("data")
    private PlayListDetailsResponse data;
    private Object debugMessage;
    private int responseCode;

    public void setData(PlayListDetailsResponse data){
        this.data = data;
    }

    public PlayListDetailsResponse getData(){
        return data;
    }

    public void setDebugMessage(Object debugMessage){
        this.debugMessage = debugMessage;
    }

    public Object getDebugMessage(){
        return debugMessage;
    }

    public void setResponseCode(int responseCode){
        this.responseCode = responseCode;
    }

    public int getResponseCode(){
        return responseCode;
    }

    @Override
    public String toString(){
        return
                "ContinueWatchingModel{" +
                        "data = '" + data + '\'' +
                        ",debugMessage = '" + debugMessage + '\'' +
                        ",responseCode = '" + responseCode + '\'' +
                        "}";
    }
}
