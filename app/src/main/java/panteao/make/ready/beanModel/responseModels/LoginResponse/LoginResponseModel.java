package panteao.make.ready.beanModel.responseModels.LoginResponse;

public class LoginResponseModel {

    //@SerializedName("data")
    private UserData data;

    //@SerializedName("responseCode")
    private int responseCode;
    private boolean status;
    private String debugMessage;
    private boolean isSuccessful=false;

    public boolean isSuccessful() {
        return isSuccessful;
    }

    public void setSuccessful(boolean successful) {
        isSuccessful = successful;
    }

    public boolean isStatus() {
        return status;
    }

    public void setStatus(boolean status) {
        this.status = status;
    }

    public String getDebugMessage() {
        return debugMessage;
    }

    public void setDebugMessage(String debugMessage) {
        this.debugMessage = debugMessage;
    }

    public UserData getData() {
        return data;
    }

    public void setData(UserData data) {
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