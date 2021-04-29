package panteao.make.ready.beanModel.responseModels.SignUp;

import panteao.make.ready.beanModel.responseModels.LoginResponse.LoginResponseModel;
import panteao.make.ready.beanModel.responseModels.LoginResponse.LoginResponseModel;

public class SignupResponseAccessToken {

    public int code;
    LoginResponseModel responseModel;
    String accessToken;
    private String debugMessage;
    private boolean status;

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

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

    public LoginResponseModel getResponseModel() {
        return responseModel;
    }

    public void setResponseModel(LoginResponseModel responseModel) {
        this.responseModel = responseModel;
    }

    public String getAccessToken() {
        return accessToken;
    }

    public void setAccessToken(String accessToken) {
        this.accessToken = accessToken;
    }
}
