package panteao.make.ready.baseModels;


import android.app.TaskStackBuilder;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.provider.Settings;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ProgressBar;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import com.android.billingclient.api.BillingResult;
import com.android.billingclient.api.Purchase;
import com.android.billingclient.api.SkuDetails;
import com.make.baseCollection.baseCategoryServices.BaseCategoryServices;
import com.make.userManagement.callBacks.LogoutCallBack;
import com.facebook.login.LoginManager;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.play.core.install.model.InstallStatus;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import panteao.make.ready.R;
import panteao.make.ready.activities.homeactivity.ui.HomeActivity;
import panteao.make.ready.beanModel.configBean.ResponseConfig;
import panteao.make.ready.networking.apiendpoints.ApiInterface;
import panteao.make.ready.networking.apiendpoints.RequestConfig;
import panteao.make.ready.utils.BaseActivityAlertDialog;
import panteao.make.ready.utils.commonMethods.AppCommonMethod;
import panteao.make.ready.utils.constants.AppConstants;
import panteao.make.ready.utils.helpers.ksPreferenceKeys.KsPreferenceKeys;
import panteao.make.ready.utils.inAppBilling.BillingProcessor;
import panteao.make.ready.utils.inAppBilling.InAppProcessListener;
import panteao.make.ready.utils.inAppUpdate.ApplicationUpdateManager;

import java.util.List;
import java.util.Objects;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class BaseActivity extends AppCompatActivity implements BaseActivityAlertDialog.AlertDialogListener {

    boolean tabletSize;
    boolean logoutBackpress = false;
    String currentLanguage="";
    private String strCurrentTheme = "";

    public void showPopup() {
        AlertDialog.Builder builder=new AlertDialog.Builder(BaseActivity.this);
    }

    public void hideSoftKeyboard(View view) {
        InputMethodManager imm = (InputMethodManager) view.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
        Objects.requireNonNull(imm).hideSoftInputFromWindow(view.getWindowToken(), 0);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }


    private void showDialog(String title, String message) {
        FragmentManager fm = getSupportFragmentManager();
        BaseActivityAlertDialog alertDialog = BaseActivityAlertDialog.newInstance(title, message, getResources().getString(R.string.ok));
        alertDialog.setCancelable(false);
        alertDialog.setAlertDialogCallBack(this);
        alertDialog.show(fm, "fragment_alert");

    }

    public void hitApiLogout(Context context, String token) {

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
            AppCommonMethod.updateLanguage(strCurrentLanguage,this);
           // new ActivityLauncher(this).homeScreen(this, HomeActivity.class);
        }catch (Exception e){
           // new ActivityLauncher(this).homeScreen(this, HomeActivity.class);

        }
    }


    public void hitApiConfig(Context context) {
        ApiInterface endpoint = RequestConfig.getClient().create(ApiInterface.class);

        Call<ResponseConfig> call = endpoint.getConfiguration("true");
        call.enqueue(new Callback<ResponseConfig>() {
            @Override
            public void onResponse(@NonNull Call<ResponseConfig> call, @NonNull retrofit2.Response<ResponseConfig> response) {
                if (response.body() != null) {
                    AppCommonMethod.urlPoints = response.body().getData().getImageTransformationEndpoint();
                    ResponseConfig cl = response.body();

                    Gson gson = new Gson();
                    String json = gson.toJson(cl);
                    AppCommonMethod.urlPoints = /*AppConstants.PROFILE_URL +*/ response.body().getData().getImageTransformationEndpoint();

                    KsPreferenceKeys ksPreferenceKeys = KsPreferenceKeys.getInstance();

                    ksPreferenceKeys.setAppPrefLastConfigHit(String.valueOf(System.currentTimeMillis()));
                    ksPreferenceKeys.setAppPrefLoginStatus(false);
                    ksPreferenceKeys.setAppPrefAccessToken("");
                    ksPreferenceKeys.setAppPrefConfigResponse(json);
                    ksPreferenceKeys.setAppPrefVideoUrl(response.body().getData().getCloudFrontVideoEndpoint());
                    ksPreferenceKeys.setAppPrefAvailableVersion(response.body().getData().getUpdateInfo().getAvailableVersion());
                    ksPreferenceKeys.setAppPrefCfep(AppCommonMethod.urlPoints);
                    ksPreferenceKeys.setAppPrefConfigVersion(String.valueOf(response.body().getData().getConfigVersion()));
                    ksPreferenceKeys.setAppPrefServerBaseUrl(response.body().getData().getServerBaseURL());





//                    sharedPrefHelper.setString(AppConstants.APP_PREF_LOGIN_STATUS, AppConstants.UserStatus.Logout.toString());
//                    sharedPrefHelper.setString(AppConstants.APP_PREF_ACCESS_TOKEN, "");
//
//                    sharedPrefHelper.setString(AppConstants.APP_PREF_CONFIG_RESPONSE, json);
//                    sharedPrefHelper.setString(AppConstants.APP_PREF_LAST_CONFIG_HIT, String.valueOf(System.currentTimeMillis()));
//                    sharedPrefHelper.setString(AppConstants.APP_PREF_VIDEO_URL, response.body().getData().getCloudFrontVideoEndpoint());
//                    sharedPrefHelper.setString(AppConstants.APP_PREF_AVAILABLE_VERSION, response.body().getData().getUpdateInfo().getAvailableVersion());
//                    sharedPrefHelper.setString(AppConstants.APP_PREF_CFEP, AppCommonMethod.urlPoints);
//                    sharedPrefHelper.setString(AppConstants.APP_PREF_CONFIG_VERSION, String.valueOf(response.body().getData().getConfigVersion()));
//                    sharedPrefHelper.setString(AppConstants.APP_PREF_SERVER_BASE_URL, response.body().getData().getServerBaseURL());
//                    preference.setString(AppConstants.APP_PREF_LOGIN_STATUS, AppConstants.UserStatus.Logout.toString());
                    //  Toast.makeText(getApplicationContext(), getResources().getString(R.string.you_are_logged_out), Toast.LENGTH_LONG).show();
                    if (logoutBackpress) {
                        logoutBackpress = false;
                        Intent intent = new Intent(context, HomeActivity.class);
                        TaskStackBuilder.create(context).addNextIntentWithParentStack(intent).startActivities();
                    }

                }
            }

            @Override
            public void onFailure(@NonNull Call<ResponseConfig> call, @NonNull Throwable t) {

            }
        });

    }


    public void showHideProgress(ProgressBar progressBar) {
        showLoading(progressBar, false);
        Handler mHandler = new Handler();
        mHandler.postDelayed(new Runnable() {
            @Override
            public void run() {
                dismissLoading(progressBar);
            }
        }, 3000);

    }

    protected void showLoading(ProgressBar progressBar, boolean val) {
        if (val) {
            progressBar.setVisibility(View.VISIBLE);
            progressBar.bringToFront();
        }
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE,
                WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
    }


    public void dismissLoading(ProgressBar progressBar) {
        if (progressBar != null) {
            progressBar.setVisibility(View.INVISIBLE);
            getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
        }
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
       /* tabletSize = getResources().getBoolean(R.bool.isTablet);
        if (tabletSize) {
            checkAutoRotation();
        } else {
            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        }*/

        tabletSize = getResources().getBoolean(R.bool.isTablet);
        if (tabletSize) {
            // checkAutoRotation();
        } else {
            if (android.os.Build.VERSION.SDK_INT != Build.VERSION_CODES.O) {
            }
            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        }

        strCurrentTheme = KsPreferenceKeys.getInstance().getCurrentTheme();
        if (KsPreferenceKeys.getInstance().getCurrentTheme().equalsIgnoreCase(AppConstants.LIGHT_THEME)) {
            setTheme(R.style.MyMaterialTheme_Base_Light);
        } else {
            setTheme(R.style.MyMaterialTheme_Base_Dark);

        }
        currentLanguage = KsPreferenceKeys.getInstance().getAppLanguage();
        updateLanguage(currentLanguage);


    }

    public void updateLanguage(String currentLanguage) {
        try {
            // String language=AppCommonMethod.getSystemLanguage();
            if (currentLanguage.equalsIgnoreCase("")){
                KsPreferenceKeys.getInstance().setAppLanguage("English");
                KsPreferenceKeys.getInstance().setAppPrefLanguagePos(0);
                AppCommonMethod.updateLanguage("en", this);
            }else {
                if (KsPreferenceKeys.getInstance().getAppLanguage().equalsIgnoreCase("Thai") || KsPreferenceKeys.getInstance().getAppLanguage().equalsIgnoreCase("हिंदी") ){
                    AppCommonMethod.updateLanguage("en", this);
                } else if (KsPreferenceKeys.getInstance().getAppLanguage().equalsIgnoreCase("English")){
                    AppCommonMethod.updateLanguage("en", this);
                }
            }

            //Logger.e("currentLanguage", "" + currentLanguage);

        }catch (Exception e){

        }
    }

    private void checkAutoRotation() {
        if (android.provider.Settings.System.getInt(getApplicationContext().getContentResolver(), Settings.System.ACCELEROMETER_ROTATION, 0) == 1) {
            this.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_SENSOR_LANDSCAPE);
        } else {
            this.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
        }
    }
   /* public void loadFragment(int fragmentContainerId, BaseFragment fragment, @Nullable String tag,
                             int enterAnimId, int exitAnimId,
                             BaseFragment.FragmentTransactionType fragmentTransactionType) {

        performFragmentTranscation(fragmentContainerId, fragment, tag,
                enterAnimId,
                exitAnimId,
                fragmentTransactionType);
    }*/

    private void performFragmentTranscation(int fragmentContainerId,
                                            Fragment fragment, String tag,
                                            int enterAnimId, int exitAnimId,
                                            BaseFragment.FragmentTransactionType fragmentTransactionType) {
        switch (fragmentTransactionType) {
            case ADD:
                addFragment(fragmentContainerId, fragment, tag, enterAnimId, exitAnimId);
                break;
            case REPLACE:
                replaceFragment(fragmentContainerId, fragment, tag, enterAnimId, exitAnimId);
                break;
            case ADD_TO_BACK_STACK_AND_ADD:
                addToBackStackAndAdd(fragmentContainerId, fragment, tag, enterAnimId, exitAnimId);
                break;
            case ADD_TO_BACK_STACK_AND_REPLACE:
                addToBackStackAndReplace(fragmentContainerId, fragment, tag, enterAnimId, exitAnimId);
                break;
            case POP_BACK_STACK_AND_REPLACE:
                popBackStackAndReplace(fragmentContainerId, fragment, tag, enterAnimId, exitAnimId);
                break;
            case CLEAR_BACK_STACK_AND_REPLACE:
                clearBackStackAndReplace(fragmentContainerId, fragment, tag, enterAnimId, exitAnimId);
                break;
            default:
                replaceFragment(fragmentContainerId, fragment, tag, enterAnimId, exitAnimId);
        }

    }


    private void addToBackStackAndAdd(int fragmentContainerId, Fragment fragment, String tag, int enterAnimId, int exitAnimId) {
        getSupportFragmentManager().beginTransaction()
                .setCustomAnimations(enterAnimId, 0, 0, exitAnimId)
                .add(fragmentContainerId, fragment, tag)
                .addToBackStack(tag)
                .commit();
    }

    private void addFragment(int fragmentContainerId, Fragment fragment, String tag, int enterAnimId, int exitAnimId) {
        getSupportFragmentManager().beginTransaction()
                .setCustomAnimations(enterAnimId, exitAnimId)
                .add(fragmentContainerId, fragment, tag)
                .commit();
    }

    private void replaceFragment(int fragmentContainerId, Fragment fragment, @Nullable String tag,
                                 int enterAnimId, int exitAnimId) {
        getSupportFragmentManager().beginTransaction()
                .setCustomAnimations(enterAnimId, exitAnimId)
                .replace(fragmentContainerId, fragment, tag).commit();
    }

    private void popBackStackAndReplace(int fragmentContainerId, Fragment fragment,
                                        @Nullable String tag, int enterAnimId, int exitAnimId) {
        getSupportFragmentManager().popBackStack(null, FragmentManager.POP_BACK_STACK_INCLUSIVE);
        getSupportFragmentManager().beginTransaction()
                .setCustomAnimations(enterAnimId, exitAnimId)
                .replace(fragmentContainerId, fragment, tag).commit();
    }

    private void addToBackStackAndReplace(int fragmentContainerId, Fragment fragment,
                                          @Nullable String tag, int enterAnimId, int exitAnimId) {
        getSupportFragmentManager().beginTransaction().addToBackStack(null)
                .setCustomAnimations(enterAnimId, 0, 0, exitAnimId)
                .replace(fragmentContainerId, fragment, tag).commit();
    }

    private void clearBackStackAndReplace(int fragmentContainerId, Fragment fragment,
                                          @Nullable String tag, int enterAnimId, int exitAnimId) {
        clearBackStack();
        getSupportFragmentManager().beginTransaction()
                .setCustomAnimations(enterAnimId, exitAnimId)
                .replace(fragmentContainerId, fragment, tag).commit();
    }

    private void clearBackStack() {
        FragmentManager fm = getSupportFragmentManager();

        if (fm.getBackStackEntryCount() > 0) {
            FragmentManager.BackStackEntry first = fm.getBackStackEntryAt(0);
            fm.popBackStack(first.getId(), FragmentManager.POP_BACK_STACK_INCLUSIVE);
        }
    }

    private boolean isOnline(Context activity) {

        ConnectivityManager cm = (ConnectivityManager) activity
                .getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo;

        try {
            netInfo = Objects.requireNonNull(cm).getActiveNetworkInfo();
            if (netInfo != null && netInfo.isConnectedOrConnecting()) {
                return true;
            }
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        return false;
    }

    protected void setupUI(View view) {

        // Set up touch listener for non-text box views to hide keyboard.
        if (!(view instanceof EditText)) {
            view.setOnTouchListener((v, event) -> {
                hideSoftKeyboard(view);
                return false;
            });
        }
        //If a layout container, iterate over children and seed recursion.
        if (view instanceof ViewGroup) {
            for (int i = 0; i < ((ViewGroup) view).getChildCount(); i++) {
                View innerView = ((ViewGroup) view).getChildAt(i);
                setupUI(innerView);
            }
        }


    }

    public void showSoftKeyboard(View view) {
        if (view.requestFocus()) {
            InputMethodManager imm = (InputMethodManager)
                    getSystemService(Context.INPUT_METHOD_SERVICE);
            Objects.requireNonNull(imm).showSoftInput(view, InputMethodManager.SHOW_IMPLICIT);
        }
    }

    @Override
    public void onDialogFinished() {
        String token = KsPreferenceKeys.getInstance().getAppPrefAccessToken();
        hitApiLogout(getApplicationContext(), token);
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (!currentLanguage.equalsIgnoreCase(KsPreferenceKeys.getInstance().getAppLanguage())) {
//          Recreate needs to be invoked to recreate the activity so that the new theme can be applied on the current screen.
        }

        if (!strCurrentTheme.equalsIgnoreCase(KsPreferenceKeys.getInstance().getCurrentTheme())) {

//          Recreate needs to be invoked to recreate the activity so that the new theme can be applied on the current screen.
            recreate();
        }

        ApplicationUpdateManager.getInstance(getApplicationContext()).getAppUpdateManager().getAppUpdateInfo().addOnSuccessListener(appUpdateInfo -> {
            if (appUpdateInfo.installStatus() == InstallStatus.DOWNLOADED) {
                // If the update is downloaded but not installed,
                // notify the user to complete the update.
                popupSnackbarForCompleteUpdate();
            }
        });
        initializeBillingProcessor();
    }

    @Override
    protected void onStart() {
        super.onStart();
//        if(DetailActivity.isBackStacklost){
            /*getApplicationContext().startActivity(new Intent(this, getClass())
                    .addFlags(Intent.FLAG_ACTIVITY_NEW_TASK));*/
//        }
    }

    /* Displays the snackbar notification and call to action. */
    private void popupSnackbarForCompleteUpdate() {
        Snackbar snackbar =
                Snackbar.make(findViewById(android.R.id.content), getResources().getString(R.string.update_has_downloaded), Snackbar.LENGTH_INDEFINITE);
        snackbar.setAction(getResources().getString(R.string.restart), view -> ApplicationUpdateManager.getInstance(getApplicationContext()).getAppUpdateManager().completeUpdate());
        snackbar.setActionTextColor(
                getResources().getColor(R.color.moretitlecolor));
        snackbar.show();
    }


    BillingProcessor bp;
    private void initializeBillingProcessor() {
        try {
            if (bp==null){
                bp = new BillingProcessor(this, new InAppProcessListener() {
                    @Override
                    public void onBillingInitialized() {
                        if (bp!=null && bp.isReady()){
                            // if (BaseActivity.this instanceof PurchaseActivity || BaseActivity.this instanceof MemberShipPlanActivity){
                            Log.w("BaseActivityClass-->>","in");
                            // }
                            bp.queryPurchasesForConsume();
                            bp.queryPurchases();
                        }
                    }

                    @Override
                    public void onPurchasesUpdated(@NonNull BillingResult billingResult, @Nullable List<Purchase> purchases) {

                    }

                    @Override
                    public void onListOfSKUFetched(@Nullable List<SkuDetails> purchases) {

                    }

                    @Override
                    public void onBillingError(@Nullable BillingResult error) {

                    }
                });
                bp.initializeBillingProcessor();
            }

        }catch (Exception ignored){

        }
    }

}
