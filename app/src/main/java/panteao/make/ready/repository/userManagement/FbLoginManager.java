package panteao.make.ready.repository.userManagement;


import panteao.make.ready.beanModel.facebook.userprofileresponse.FacebookUserProfileResponse;

public class FbLoginManager {

    private static FbLoginManager mInstance;
    private FbLoginRequest mFbLoginRequest;
    private FacebookUserProfileResponse mFacebookUserProfileResponse;


    private FbLoginManager() {

    }

    public static FbLoginManager getInstance() {
        if (mInstance == null) {
            mInstance = new FbLoginManager();
        }
        return mInstance;
    }

    public FbLoginRequest getFbLoginRequest() {
        return mFbLoginRequest;
    }

    public void setFbLoginRequest(FbLoginRequest fbLoginRequest) {
        this.mFbLoginRequest = fbLoginRequest;
    }

    public FacebookUserProfileResponse getFacebookUserProfileResponse() {
        return mFacebookUserProfileResponse;
    }

    public void setFacebookUserProfileResponse(FacebookUserProfileResponse facebookUserProfileResponse) {
        this.mFacebookUserProfileResponse = facebookUserProfileResponse;
    }

    public void clear(){
        this.mFbLoginRequest = null;
        this.mFacebookUserProfileResponse = null;
    }
}
