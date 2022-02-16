package panteao.make.ready.baseModels;

import android.app.Activity;
import android.os.Handler;
import android.view.View;
import android.view.WindowManager;
import android.widget.ProgressBar;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.make.baseCollection.baseCategoryServices.BaseCategoryServices;
import panteao.make.ready.PanteaoApplication;
import panteao.make.ready.utils.commonMethods.AppCommonMethod;
import panteao.make.ready.utils.constants.AppConstants;
import panteao.make.ready.utils.helpers.ksPreferenceKeys.KsPreferenceKeys;
import com.make.userManagement.callBacks.LogoutCallBack;
import com.facebook.login.LoginManager;
import com.google.gson.JsonObject;

import java.util.Objects;

import retrofit2.Response;

public class BaseFragment extends Fragment {

    @Nullable
    protected BaseBindingActivity getBaseActivity() {
        return (BaseBindingActivity) getActivity();
    }

    public void showHideProgress(ProgressBar progressBar, Activity context) {
        showLoading(progressBar, context);
        Handler mHandler = new Handler();
        mHandler.postDelayed(new Runnable() {
            @Override
            public void run() {
                dismissLoading(progressBar, context);
            }
        }, 3000);

    }

    protected void showLoading(ProgressBar progressBar, Activity context) {
        if (false) {
            progressBar.setVisibility(View.VISIBLE);
            progressBar.bringToFront();
        }
        context.getWindow().setFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE,
                WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
    }

    public void clearCredientials(KsPreferenceKeys preference) {
        try {
            String isFacebook = preference.getAppPrefLoginType();
            if (isFacebook.equalsIgnoreCase(AppConstants.UserLoginType.FbLogin.toString())) {
                LoginManager.getInstance().logOut();
            }
            String strCurrentTheme = KsPreferenceKeys.getInstance().getCurrentTheme();
            String strCurrentLanguage = KsPreferenceKeys.getInstance().getAppLanguage();
            preference.clear();
            KsPreferenceKeys.getInstance().setCurrentTheme(strCurrentTheme);
            KsPreferenceKeys.getInstance().setAppLanguage(strCurrentLanguage);
            if (strCurrentLanguage.equalsIgnoreCase("Thai") || KsPreferenceKeys.getInstance().getAppLanguage().equalsIgnoreCase("हिंदी") ){
                AppCommonMethod.updateLanguage("th", PanteaoApplication.getInstance());
            } else if (strCurrentLanguage.equalsIgnoreCase("English")){
                AppCommonMethod.updateLanguage("en", PanteaoApplication.getInstance());
            }
            //AppCommonMethod.updateLanguage(strCurrentLanguage,getBaseActivity());
           // new ActivityLauncher(getBaseActivity()).homeScreen(getBaseActivity(), HomeActivity.class);
        }catch (Exception e){
            //new ActivityLauncher(getBaseActivity()).homeScreen(getBaseActivity(), HomeActivity.class);

        }
    }
    public void hitApiLogout(String token) {

        String isFacebook = KsPreferenceKeys.getInstance().getAppPrefLoginType();

        if (isFacebook.equalsIgnoreCase(AppConstants.UserLoginType.FbLogin.toString())) {
            LoginManager.getInstance().logOut();
        }
        /*hitApiConfig(context);
        ApiInterface endpoint = RequestConfig.getClientInterceptor(token).create(ApiInterface.class);

        Call<JsonObject> call = endpoint.getLogout(false);
        call.enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(@NonNull Call<JsonObject> call, @NonNull retrofit2.Response<JsonObject> response) {
                if (response.code() == 404) {
                    Logger.e("BaseActivity", "404 Error");
                } else if (response.code() == 200 || response.code() == 401) {
                    KsPreferenceKeys.getInstance().clear();
                    logoutBackpress = true;
                    hitApiConfig(context);
                }
            }

            @Override
            public void onFailure(@NonNull Call<JsonObject> call, @NonNull Throwable t) {
                //pDialog.call();
            }
        });*/

        BaseCategoryServices.Companion.getInstance().logoutService(token, new LogoutCallBack() {
            @Override
            public void failure(boolean status, int errorCode, String message) {
                JsonObject jsonObject = new JsonObject();
                jsonObject.addProperty(AppConstants.API_RESPONSE_CODE, 500);

            }

            @Override
            public void success(boolean status, Response<JsonObject> response) {
                if (status){
                    try {
                        if (response.code() == 404) {
                            JsonObject jsonObject = new JsonObject();
                            jsonObject.addProperty(AppConstants.API_RESPONSE_CODE, response.code());

                        }if (response.code() == 403) {
                            JsonObject jsonObject = new JsonObject();
                            jsonObject.addProperty(AppConstants.API_RESPONSE_CODE, response.code());

                        }
                        else if (response.code() == 200) {
                            Objects.requireNonNull(response.body()).addProperty(AppConstants.API_RESPONSE_CODE, response.code());

                        } else if (response.code() == 401) {
                            JsonObject jsonObject = new JsonObject();
                            jsonObject.addProperty(AppConstants.API_RESPONSE_CODE, response.code());

                        } else if (response.code() == 500) {
                            JsonObject jsonObject = new JsonObject();
                            jsonObject.addProperty(AppConstants.API_RESPONSE_CODE, response.code());

                        }
                    }catch (Exception e){
                        JsonObject jsonObject = new JsonObject();
                        jsonObject.addProperty(AppConstants.API_RESPONSE_CODE, response.code());

                    }

                }
            }
        });



    }

    protected void dismissLoading(ProgressBar progressBar, Activity context) {
        if (progressBar != null) {
            progressBar.setVisibility(View.INVISIBLE);
            context.getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
        }
    }


    public enum FragmentTransactionType {
        ADD, REPLACE, ADD_TO_BACK_STACK_AND_ADD, ADD_TO_BACK_STACK_AND_REPLACE, POP_BACK_STACK_AND_REPLACE, CLEAR_BACK_STACK_AND_REPLACE
    }
/*
    @Override
    public void onNetworkConnectionChanged(boolean isConnected) {

    }*/
}
