
package panteao.make.ready.beanModelV3.videoDetailsV2;

public class EnveuVideoDetailsBean {

    private EnveuVideoDetails data;
    private Object debugMessage;
    private int responseCode;

    public void setData(EnveuVideoDetails data){
        this.data = data;
    }

    public EnveuVideoDetails getData(){
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
