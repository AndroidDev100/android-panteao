
package panteao.make.ready.activities.splash.ui;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.Signature;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.SystemClock;
import android.util.Base64;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import androidx.annotation.NonNull;
import com.google.android.gms.common.GooglePlayServicesNotAvailableException;
import com.google.android.gms.common.GooglePlayServicesRepairableException;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.security.ProviderInstaller;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.dynamiclinks.FirebaseDynamicLinks;
import com.google.firebase.dynamiclinks.PendingDynamicLinkData;
import com.google.gson.Gson;
import com.make.baseClient.BaseClient;
import com.make.baseClient.BaseConfiguration;
import com.make.baseClient.BaseDeviceType;
import com.make.baseClient.BaseGateway;
import com.make.baseClient.BasePlatform;
import org.json.JSONObject;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import javax.inject.Inject;
import panteao.make.ready.BuildConfig;
import panteao.make.ready.PanteaoApplication;
import panteao.make.ready.R;
import panteao.make.ready.SDKConfig;
import panteao.make.ready.activities.article.ArticleActivity;
import panteao.make.ready.activities.detail.ui.DetailActivity;
import panteao.make.ready.activities.detail.ui.EpisodeActivity;
import panteao.make.ready.activities.homeactivity.ui.HomeActivity;
import panteao.make.ready.activities.homeactivity.ui.TVHomeActivity;
import panteao.make.ready.activities.live.LiveActivity;
import panteao.make.ready.activities.series.ui.SeriesDetailActivity;
import panteao.make.ready.activities.splash.dialog.ConfigFailDialog;
import panteao.make.ready.callbacks.apicallback.ApiResponseModel;
import panteao.make.ready.callbacks.commonCallbacks.DialogInterface;
import panteao.make.ready.callbacks.commonCallbacks.VersionValidator;
import panteao.make.ready.databinding.ActivitySplashBinding;
import panteao.make.ready.databinding.ActivityTvSplashBinding;
import panteao.make.ready.dependencies.providers.DTGPrefrencesProvider;
import panteao.make.ready.fragments.dialog.AlertDialogFragment;
import panteao.make.ready.networking.errormodel.ApiErrorModel;
import panteao.make.ready.tvBaseModels.basemodels.TvBaseBindingActivity;
import panteao.make.ready.utils.MediaTypeConstants;
import panteao.make.ready.utils.commonMethods.AppCommonMethod;
import panteao.make.ready.utils.config.ConfigManager;
import panteao.make.ready.utils.config.bean.ConfigBean;
import panteao.make.ready.utils.constants.AppConstants;
import panteao.make.ready.utils.cropImage.helpers.Logger;
import panteao.make.ready.utils.cropImage.helpers.PrintLogging;
import panteao.make.ready.utils.helpers.ForceUpdateHandler;
import panteao.make.ready.utils.helpers.NetworkConnectivity;
import panteao.make.ready.utils.helpers.intentlaunchers.ActivityLauncher;
import panteao.make.ready.utils.helpers.ksPreferenceKeys.KsPreferenceKeys;

public class TVSplashActivity extends TvBaseBindingActivity<ActivityTvSplashBinding> implements AlertDialogFragment.AlertDialogListener {
    private final String TAG = this.getClass().getSimpleName();
    @Inject
    DTGPrefrencesProvider dtgPrefrencesProvider;
    private ForceUpdateHandler forceUpdateHandler;
    private KsPreferenceKeys session;
    private PanteaoApplication appState;
    private boolean viaIntent;
    private long mLastClickTime = 0;
    private String currentLanguage;
    private ConfigBean configBean;
    private int configCall = 1;
    private int count = 0;
    private String notid = "";
    private String notAssetType = "";
    private int notificationAssetId = 0;
    JSONObject deepLinkObject = null;

    @Override
    public ActivityTvSplashBinding inflateBindingLayout(@NonNull LayoutInflater inflater) {
        return ActivityTvSplashBinding.inflate(inflater);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        DisplayMetrics displayMetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        int height = displayMetrics.heightPixels;
        int width = displayMetrics.widthPixels;
        AppCommonMethod.ScreenHeight = height;
        AppCommonMethod.ScreenWidth = width;
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
//                if (TextUtils.isEmpty(KsPreferenceKeys.getInstance().getQualityName())) {
//                    KsPreferenceKeys.getInstance().setQualityName(getApplicationContext().getResources().getString(com.make.brightcovelibrary.R.string.auto));
//                    KsPreferenceKeys.getInstance().setQualityPosition(0);
//                }
//                if ((!KsPreferenceKeys.getInstance().getAppPrefUserId().equalsIgnoreCase("")) && (KsPreferenceKeys.getInstance().getAppPrefUserId() != null)) {
//                    FirebaseAnalytics mFirebaseAnalytics = FirebaseAnalytics.getInstance(getApplicationContext());
//                    mFirebaseAnalytics.setUserId(KsPreferenceKeys.getInstance().getAppPrefUserId());
//                    mFirebaseAnalytics.setUserProperty("username", KsPreferenceKeys.getInstance().getAppPrefUserName());
//                } else {
//                    Log.d("userid", "userlogin");
//                }
                session = KsPreferenceKeys.getInstance();
                // session.setDownloadOverWifi(1);
                currentLanguage = KsPreferenceKeys.getInstance().getAppLanguage();

                connectionObserver();
//                getBinding().noConnectionLayout.retryTxt.setOnClickListener(view -> connectionObserver());
//                getBinding().noConnectionLayout.btnMyDownloads.setOnClickListener(view -> new ActivityLauncher(TVSplashActivity.this).launchMyDownloads());
                printHashKey();
            }
        }, 3000);

    }

    private void printHashKey() {
        try {
            PackageInfo info = getPackageManager().getPackageInfo(
                    "panteao.make.ready",
                    PackageManager.GET_SIGNATURES);
            for (Signature signature : info.signatures) {
                MessageDigest md = MessageDigest.getInstance("SHA");
                md.update(signature.toByteArray());
                Log.w("KeyHash:", Base64.encodeToString(md.digest(), Base64.DEFAULT));
            }
        } catch (PackageManager.NameNotFoundException e) {
            PrintLogging.printLog("Exception", "" + e);
        } catch (NoSuchAlgorithmException e) {
            PrintLogging.printLog("Exception", "" + e);
        }
    }

    private void callConfig(JSONObject jsonObject, String updateType) {
        ConfigManager.getInstance().getConfig(new ApiResponseModel() {
            @Override
            public void onStart() {

            }

            @Override
            public void onSuccess(Object response) {
                boolean isTablet = getResources().getBoolean(R.bool.isTablet);
                configBean = AppCommonMethod.getConfigResponse();
                Gson gson = new Gson();
                String json = gson.toJson(configBean);
                AppCommonMethod.setConfigConstant(configBean, isTablet);
                String API_KEY = "";
                String DEVICE_TYPE = "";
                if (isTablet) {
                    API_KEY = SDKConfig.API_KEY_TAB;
                    DEVICE_TYPE = BaseDeviceType.tablet.name();
                } else {
                    API_KEY = SDKConfig.API_KEY_MOB;
                    DEVICE_TYPE = BaseDeviceType.mobile.name();
                }
                BaseClient client = new BaseClient(TVSplashActivity.this, BaseGateway.ENVEU, SDKConfig.getInstance().getBASE_URL(), SDKConfig.getInstance().getSUBSCRIPTION_BASE_URL(), DEVICE_TYPE, API_KEY, BasePlatform.android.name(), isTablet, AppCommonMethod.getDeviceId(getContentResolver()));
                BaseConfiguration.Companion.getInstance().clientSetup(client);
                updateLanguage(configBean.getData().getAppConfig().getPrimaryLanguage());

                if (configBean != null) {
                    startActivity(new Intent(TVSplashActivity.this, TVHomeActivity.class));
                    finish();
                } else {
                    configFailPopup();
                }
            }

            @Override
            public void onError(ApiErrorModel httpError) {
                configFailPopup();
            }

            @Override
            public void onFailure(ApiErrorModel httpError) {
                configFailPopup();
            }
        });

    }
    private void callConfig() {
        ConfigManager.getInstance().getConfig(new ApiResponseModel() {
            @Override
            public void onStart() {

            }

            @Override
            public void onSuccess(Object response) {
                boolean isTablet = getResources().getBoolean(R.bool.isTablet);
                configBean = AppCommonMethod.getConfigResponse();
                Gson gson = new Gson();
                String json = gson.toJson(configBean);
                AppCommonMethod.setConfigConstant(configBean, isTablet);
                String API_KEY = "";
                String DEVICE_TYPE = "";
                if (isTablet) {
                    API_KEY = SDKConfig.API_KEY_TAB;
                    DEVICE_TYPE = BaseDeviceType.tablet.name();
                } else {
                    API_KEY = SDKConfig.API_KEY_MOB;
                    DEVICE_TYPE = BaseDeviceType.mobile.name();
                }
                BaseClient client = new BaseClient(TVSplashActivity.this, BaseGateway.ENVEU, SDKConfig.getInstance().getBASE_URL(), SDKConfig.getInstance().getSUBSCRIPTION_BASE_URL(), DEVICE_TYPE, API_KEY, BasePlatform.android.name(), isTablet, AppCommonMethod.getDeviceId(getContentResolver()));
                BaseConfiguration.Companion.getInstance().clientSetup(client);
                updateLanguage(configBean.getData().getAppConfig().getPrimaryLanguage());

                if (configBean != null) {
                    startActivity(new Intent(TVSplashActivity.this, TVHomeActivity.class));
                    finish();
                } else {
                    configFailPopup();
                }
            }

            @Override
            public void onError(ApiErrorModel httpError) {
                configFailPopup();
            }

            @Override
            public void onFailure(ApiErrorModel httpError) {
                configFailPopup();
            }
        });

    }


    private void checkLanguage() {
        if (KsPreferenceKeys.getInstance().getAppLanguage().equalsIgnoreCase("Hindi")) {
            AppCommonMethod.updateLanguage("hi", this);
        } else if (KsPreferenceKeys.getInstance().getAppLanguage().equalsIgnoreCase("English")) {
            AppCommonMethod.updateLanguage("en", this);
        } else if (KsPreferenceKeys.getInstance().getAppLanguage().equalsIgnoreCase("Indonesia")) {
            AppCommonMethod.updateLanguage("in", this);
        }
    }

    private void updateAndroidSecurityProvider(Activity callingActivity) {
        try {
            ProviderInstaller.installIfNeeded(this);
        } catch (GooglePlayServicesRepairableException e) {
            // Thrown when Google Play Services is not installed, up-to-date, or enabled
            // Show dialog to allow users to install, update, or otherwise enable Google Play services.
            GooglePlayServicesUtil.getErrorDialog(e.getConnectionStatusCode(), callingActivity, 0);
        } catch (GooglePlayServicesNotAvailableException e) {
            Logger.e("SecurityException", "Google Play Services not available.");
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        boolean isTablet = TVSplashActivity.this.getResources().getBoolean(R.bool.isTablet);
        if (!AppCommonMethod.isTV(this)) {
            getBinding().buildNumber.setText(getResources().getString(R.string.app_name) + "  V " + BuildConfig.VERSION_NAME);
        } else {
            getBinding().buildNumber.setText(getResources().getString(R.string.app_name) + "  V " + BuildConfig.VERSION_NAME);
        }
    }

    private void redirectToHome() {
        boolean updateValue = getForceUpdateValue(null, 2);
        if (!updateValue) {
            Log.w("branchRedirectors", "homeRedirection");
            homeRedirection();
        }
    }

    private void homeRedirection() {
        Log.w("branchRedirectors", configCall + "");
        if (configCall == 1) {
            Log.w("branchRedirectors", configCall + "");
            configCall = 2;
            callConfig(null, null);
        }

    }

    boolean configRetry = false;

    private void configFailPopup() {
        if (KsPreferenceKeys.getInstance().getAppLanguage().equalsIgnoreCase("Thai") || KsPreferenceKeys.getInstance().getAppLanguage().equalsIgnoreCase("हिंदी")) {
            AppCommonMethod.updateLanguage("en", PanteaoApplication.getInstance());
        } else if (KsPreferenceKeys.getInstance().getAppLanguage().equalsIgnoreCase("English")) {
            AppCommonMethod.updateLanguage("en", PanteaoApplication.getInstance());
        }
        new ConfigFailDialog(TVSplashActivity.this).showDialog(new DialogInterface() {
            @Override
            public void positiveAction() {
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        getBinding().progressBar.setVisibility(View.VISIBLE);
                        configRetry = true;
                        callConfig(null, null);
                    }
                }, 200);
            }

            @Override
            public void negativeAction() {
                getBinding().progressBar.setVisibility(View.GONE);
                finish();
            }
        });
    }


    private void redirections(JSONObject jsonObject) {
        try {
            callConfig(jsonObject, null);
        } catch (Exception e) {

        }
    }

    private void brachRedirections(JSONObject jsonObject) {
        try {
            if (jsonObject != null && jsonObject.has("contentType") && jsonObject.has("id")) {
                int assestId = 0;
                String contentType = jsonObject.getString("contentType");
                String id = jsonObject.getString("id");
                if (id != null && !id.equalsIgnoreCase("")) {
                    assestId = Integer.parseInt(id);
                    if (contentType.equalsIgnoreCase(MediaTypeConstants.getInstance().getSeries())) {
                        if (SystemClock.elapsedRealtime() - mLastClickTime < 1200) {
                            return;
                        }
                        mLastClickTime = SystemClock.elapsedRealtime();
                        launchSeriesPage(contentType, assestId);
                        new ActivityLauncher(this).homeScreen(this, HomeActivity.class);
                        new ActivityLauncher(TVSplashActivity.this).seriesDetailScreen(TVSplashActivity.this, SeriesDetailActivity.class, assestId);
                        finish();
                    } else if (contentType.equalsIgnoreCase(AppConstants.ContentType.VIDEO.name()) || contentType.equalsIgnoreCase(MediaTypeConstants.getInstance().getMovie())) {
                        if (SystemClock.elapsedRealtime() - mLastClickTime < 1200) {
                            return;
                        }
                        mLastClickTime = SystemClock.elapsedRealtime();
                        //new ActivityLauncher(ActivitySplash.this).articleScreen(ActivitySplash.this, ArticleActivity.class, assestId, "0", false);
                        new ActivityLauncher(this).homeScreen(this, HomeActivity.class);
                        new ActivityLauncher(TVSplashActivity.this).detailScreen(TVSplashActivity.this, DetailActivity.class, assestId, "0", false);
                        finish();
                    } else if (contentType.equalsIgnoreCase(MediaTypeConstants.getInstance().getShow())) {
                        if (SystemClock.elapsedRealtime() - mLastClickTime < 1200) {
                            return;
                        }
                        mLastClickTime = SystemClock.elapsedRealtime();
                        //new ActivityLauncher(ActivitySplash.this).articleScreen(ActivitySplash.this, ArticleActivity.class, assestId, "0", false);
                        new ActivityLauncher(this).homeScreen(this, HomeActivity.class);
                        new ActivityLauncher(TVSplashActivity.this).detailScreen(TVSplashActivity.this, DetailActivity.class, assestId, "0", false);
                        finish();
                    } else if (contentType.equalsIgnoreCase(MediaTypeConstants.getInstance().getLive())) {
                        if (SystemClock.elapsedRealtime() - mLastClickTime < 1200) {
                            return;
                        }
                        mLastClickTime = SystemClock.elapsedRealtime();
                        //new ActivityLauncher(ActivitySplash.this).articleScreen(ActivitySplash.this, ArticleActivity.class, assestId, "0", false);
                        new ActivityLauncher(this).homeScreen(this, HomeActivity.class);
                        new ActivityLauncher(TVSplashActivity.this).liveScreenBrightCove(TVSplashActivity.this, LiveActivity.class, 0l, assestId, "0", false, SDKConfig.getInstance().getLiveDetailId());
                        finish();
                    } else if (contentType.equalsIgnoreCase(MediaTypeConstants.getInstance().getEpisode())) {
                        if (SystemClock.elapsedRealtime() - mLastClickTime < 1200) {
                            return;
                        }
                        mLastClickTime = SystemClock.elapsedRealtime();
                        new ActivityLauncher(this).homeScreen(this, HomeActivity.class);
                        new ActivityLauncher(TVSplashActivity.this).episodeScreen(TVSplashActivity.this, EpisodeActivity.class, assestId, "0", false);
                        finish();
                    } else if (contentType.equalsIgnoreCase(AppConstants.ContentType.ARTICLE.toString())) {
                        if (SystemClock.elapsedRealtime() - mLastClickTime < 1200) {
                            return;
                        }
                        mLastClickTime = SystemClock.elapsedRealtime();
                        new ActivityLauncher(this).homeScreen(this, HomeActivity.class);
                        new ActivityLauncher(TVSplashActivity.this).articleScreen(TVSplashActivity.this, ArticleActivity.class, assestId, "0", false);
                        finish();
                    }
                } else {
                    if (!AppCommonMethod.isTV(TVSplashActivity.this)) {
                        new ActivityLauncher(TVSplashActivity.this).homeScreen(TVSplashActivity.this, HomeActivity.class);
                    } else {
                        startActivity(new Intent(TVSplashActivity.this, TVHomeActivity.class));
                    }
                    finish();
                }

            }

        } catch (Exception e) {

        }
    }

    private void launchSeriesPage(String contentType, int assestId) {

    }


    private void connectionObserver() {
        if (NetworkConnectivity.isOnline(this)) {
            connectionValidation(true);
        } else {
            connectionValidation(false);
        }
    }

    private void connectionValidation(Boolean aBoolean) {
        if (aBoolean) {
//            getBinding().noConnectionLayout.noConnectionLayout.setVisibility(View.GONE);
            callConfig();
        } else {
//            getBinding().noConnectionLayout.noConnectionLayout.setVisibility(View.VISIBLE);
//            getBinding().noConnectionLayout.noConnectionLayout.bringToFront();
            /*  showDialog(ActivitySplash.this.getResources().getString(R.string.error),getResources().getString(R.string.no_connection)); */
        }
    }

    JSONObject notificationObject = null;

    private void parseNotification(String notid, String assetType) {
        if (notid != null && !assetType.equalsIgnoreCase("")) {
            notificationAssetId = Integer.parseInt(notid);
            if (notificationAssetId > 0 && assetType != null && !assetType.equalsIgnoreCase("")) {
                Logger.w("FCM_Payload_final --", notificationAssetId + "");
                notificationObject = AppCommonMethod.createNotificationObject(notid, assetType);
                viaIntent = true;
            }
        } else {
            //   Branch.getInstance().reInitSession(this, branchReferralInitListener);
        }
    }

    private void setBranchInIt() {

        FirebaseDynamicLinks.getInstance().getDynamicLink(getIntent()).addOnSuccessListener(new OnSuccessListener<PendingDynamicLinkData>() {
            @Override
            public void onSuccess(@NonNull PendingDynamicLinkData pendingDynamicLinkData) {
                try {
                    if (pendingDynamicLinkData != null) {
                        Uri deepLink = pendingDynamicLinkData.getLink();
                        Log.e("deepLink", "in2" + pendingDynamicLinkData.getLink() + " " + deepLink.getQuery());
                        if (deepLink != null) {
                            Uri uri = Uri.parse(String.valueOf(deepLink));
                            String id = null;
                            String mediaType = null;
                            try {
                                id = uri.getQueryParameter("id");
                                mediaType = uri.getQueryParameter("mediaType");
                                Log.w("programeID", id);
                                Log.w("programeMediaType", mediaType);
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                            if (!mediaType.equalsIgnoreCase("") && !id.equalsIgnoreCase("")) {
                                //Logger.e("ASSET TYPE", String.valueOf(viaIntent));
                                KsPreferenceKeys.getInstance().setAppPrefJumpTo(mediaType);
                                KsPreferenceKeys.getInstance().setAppPrefBranchIo(true);
                                KsPreferenceKeys.getInstance().setAppPrefJumpBackId(Integer.parseInt(id));
                                deepLinkObject = AppCommonMethod.createDynamicLinkObject(id, mediaType);
                                redirections(deepLinkObject);
                                Log.w("redirectionss", "redirections");

                            } else {
                                redirectToHome();
                            }





                              /*  Logger.e("BranchCall", String.valueOf(referringParams));
                                int assestId = 0;
                                String contentType = "";
                                if (referringParams.has("id") && referringParams.has("contentType")) {
                                    try {
                                        assestId = Integer.parseInt(referringParams.getString("id"));
                                        contentType = referringParams.getString("contentType");
                                    } catch (JSONException e) {
                                        e.printStackTrace();
                                    }
                                    if (!contentType.equalsIgnoreCase("") && assestId > 0) {
                                        Logger.e("ASSET TYPE", String.valueOf(viaIntent));
                                        KsPreferenceKeys.getInstance().setAppPrefJumpTo(contentType);
                                        KsPreferenceKeys.getInstance().setAppPrefBranchIo(true);
                                        KsPreferenceKeys.getInstance().setAppPrefJumpBackId(assestId);
                                        redirections(referringParams);
                                        Log.w("redirectionss", "redirections");

                                    } else {
                                        redirectToHome();
                                    }

                                } else {
                                    redirectToHome();
                                }*/
                            // http://www.panteao.com/?id=32308&mediaType=SERIES&image=https:
                            // cf-images.ap-southeast-1.prod.boltdns.net/v1/static/5854923532001/
                            // f875275a-1a20-4022-98df-9e6562f7994d/e84c09fb-10a7-497a-9bc1-307ea942bcef/1280x720/match/image.jpg&name=Criminal+Justice&apn=panteao.make.ready.dev

                        }

                    }
                } catch (Exception e) {
                    Logger.e("Catch", String.valueOf(e));
                }


            }

        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {

            }
        });


    }

    @Override
    public void onFinishDialog() {
        connectionObserver();
    }


    @Override
    protected void onPause() {
        super.onPause();
    }

    boolean forceUpdate = false;

    private boolean getForceUpdateValue(JSONObject jsonObject, int type) {
        Log.i("branchRedirectors er", "forceupdate");
        if (KsPreferenceKeys.getInstance().getAppLanguage().equalsIgnoreCase("Thai") || KsPreferenceKeys.getInstance().getAppLanguage().equalsIgnoreCase("हिंदी")) {
            AppCommonMethod.updateLanguage("en", PanteaoApplication.getInstance());
        } else if (KsPreferenceKeys.getInstance().getAppLanguage().equalsIgnoreCase("English")) {
            AppCommonMethod.updateLanguage("en", PanteaoApplication.getInstance());
        }
        forceUpdateHandler = new ForceUpdateHandler(TVSplashActivity.this, configBean);
        forceUpdateHandler.checkCurrentVersion(new VersionValidator() {
            @Override
            public void version(boolean status, int currentVersion, int playstoreVersion, String updateType) {
                if (status) {
                    forceUpdate = true;
                    forceUpdateHandler.typeHandle(updateType, selection -> {
                        if (updateType.equals(ForceUpdateHandler.RECOMMENDED)) {
                            if (!selection) {
                                getBinding().progressBar.setVisibility(View.VISIBLE);
                                forceUpdateHandler.hideDialog();
//                                clapanimation=1;
                                callConfig(null, updateType);
                            }
                        }
                    });
                } else {
                    forceUpdate = false;
                }
            }
        });

        return forceUpdate;
    }


}