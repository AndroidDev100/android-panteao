package panteao.make.ready.beanModel.allWatchList;

public class ResponseAllWatchList {
    private Data data;
    private int responseCode;
    private boolean status;
    private String debugMessage;

    public boolean isStatus() {
        return status;
    }

    public void setStatus(boolean status) {
        this.status = status;
    }

    public Object getDebugMessage() {
        return debugMessage;
    }

    public void setDebugMessage(String debugMessage) {
        this.debugMessage = debugMessage;
    }

    public Data getData() {
        return data;
    }

    public void setData(Data data) {
        this.data = data;
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
                "ResponseAllWatchList{" +
                        "data = '" + data + '\'' +
                        ",debugMessage = '" + debugMessage + '\'' +
                        ",responseCode = '" + responseCode + '\'' +
                        "}";
    }
}
