package panteao.make.ready.beanModel.responseModels.RegisterSignUpModels;

public class ResponseRegisteredSignup {
    private DataResponseRegister data;
    private int responseCode;
    private String debugMessage;
    private boolean status;


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

    public DataResponseRegister getData() {
        return data;
    }

    public void setData(DataResponseRegister dataResponseRegister) {
        this.data = dataResponseRegister;
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
                "ResponseRegisteredSignup{" +
                        "data = '" + data + '\'' +
                        ",responseCode = '" + responseCode + '\'' +
                        "}";
    }
}
