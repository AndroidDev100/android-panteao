package panteao.make.ready.activities.usermanagment.viewmodel;

import android.app.Application;
import android.content.Context;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import panteao.make.ready.beanModel.connectFb.ResponseConnectFb;
import panteao.make.ready.beanModel.forgotPassword.CommonResponse;
import panteao.make.ready.beanModel.requestParamModel.RequestParamRegisterUser;
import panteao.make.ready.beanModel.responseModels.LoginResponse.LoginResponseModel;
import panteao.make.ready.beanModel.responseModels.RegisterSignUpModels.ResponseRegisteredSignup;
import panteao.make.ready.beanModel.responseModels.SignUp.SignupResponseAccessToken;
import panteao.make.ready.beanModel.userProfile.UserProfileResponse;
import panteao.make.ready.repository.userManagement.RegistrationLoginRepository;
import com.google.gson.JsonObject;


public class RegistrationLoginViewModel extends AndroidViewModel {

    final RegistrationLoginRepository loginRepository;

    public RegistrationLoginViewModel(@NonNull Application application) {
        super(application);
        loginRepository = RegistrationLoginRepository.getInstance();
    }

    public LiveData<LoginResponseModel> hitLoginAPI(Context context, String userName, String pwd) {
        return loginRepository.getLoginAPIResponse(context, userName, pwd);
    }

    public LiveData<SignupResponseAccessToken> hitSignUpAPI(String name, String userName, String pwd) {
        return loginRepository.getSignupAPIResponse(name, userName, pwd);
    }

    public LiveData<ResponseRegisteredSignup> hitRegisterSignUpAPI(Context context, RequestParamRegisterUser userDetails) {
        return loginRepository.getSignupAPIResponse(context, userDetails);
    }

    public LiveData<CommonResponse> hitForgotPasswordApi(String email) {
        return loginRepository.getForgotPasswordAPIResponse(email);
    }

    public LiveData<LoginResponseModel> hitApiChangePwd(String email, String token,Context context) {
        return loginRepository.getChangePwdAPIResponse(email, token,context);
    }


    public LiveData<LoginResponseModel> hitFbLogin(Context context, String email, String fbToken, String name, String fbId,String pic, boolean isEmail) {
        return loginRepository.getFbLogin(context, email, fbToken, name, fbId,pic, isEmail);
    }

    public LiveData<LoginResponseModel> hitApiForceFbLogin(Context context, String email, String fbToken, String name, String fbId, String profilePic, boolean isEmail) {
        return loginRepository.getForceFbLogin(context, email, fbToken, name, fbId, profilePic, isEmail);
    }


    public LiveData<ResponseConnectFb> hitApiConnectFb(Context context, String token, JsonObject requestParam) {
        return loginRepository.getConnectFb(context, token, requestParam);
    }

    public LiveData<JsonObject> hitLogout(boolean session, String token) {
        return loginRepository.hitApiLogout(session, token);
    }

    public LiveData<UserProfileResponse> hitUserProfile(Context context,String token) {
        return loginRepository.getUserProfile(context,token);
    }

    public LiveData<UserProfileResponse> hitUpdateProfile(Context context,String token,String name) {
        return loginRepository.getUpdateProfile(context,token,name);
    }


}
