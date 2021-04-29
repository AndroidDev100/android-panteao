
package panteao.make.ready.utils.config.bean;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class ConfigBean {

    @SerializedName("data")
    @Expose
    private Data data;
    @SerializedName("responseCode")
    @Expose
    private Integer responseCode;
    @SerializedName("debugMessage")
    @Expose
    private Object debugMessage;

    public Data getData() {
        return data;
    }

    public void setData(Data data) {
        this.data = data;
    }

    public Integer getResponseCode() {
        return responseCode;
    }

    public void setResponseCode(Integer responseCode) {
        this.responseCode = responseCode;
    }

    public Object getDebugMessage() {
        return debugMessage;
    }

    public void setDebugMessage(Object debugMessage) {
        this.debugMessage = debugMessage;
    }

}
