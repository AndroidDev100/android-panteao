package panteao.make.ready.beanModel.responseModels.LoginResponse;

public class LoginResponseModel {

    //@SerializedName("data")
    private Data data;

    //@SerializedName("responseCode")
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
                "LoginResponseModel{" +
                        "data = '" + data + '\'' +
                        ",responseCode = '" + responseCode + '\'' +
                        "}";
    }
}