
package panteao.make.ready.activities.splash.ui;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.Signature;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.SystemClock;
import android.text.TextUtils;
import android.util.Base64;
import android.util.Log;
import android.view.DisplayCutout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowInsets;
import android.view.WindowInsetsController;
import android.view.WindowManager;
import android.view.animation.Animation;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatDelegate;

import com.google.android.gms.common.GoogleApiAvailability;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.analytics.FirebaseAnalytics;
import com.google.firebase.dynamiclinks.FirebaseDynamicLinks;
import com.google.firebase.dynamiclinks.PendingDynamicLinkData;
import com.make.baseClient.BaseClient;
import com.make.baseClient.BaseConfiguration;
import com.make.baseClient.BaseDeviceType;
import com.make.baseClient.BaseGateway;
import com.make.baseClient.BasePlatform;
import com.google.android.gms.common.GooglePlayServicesNotAvailableException;
import com.google.android.gms.common.GooglePlayServicesRepairableException;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.security.ProviderInstaller;
import com.google.gson.Gson;
import panteao.make.ready.PanteaoApplication;
import panteao.make.ready.activities.article.ArticleActivity;
import panteao.make.ready.activities.instructor.ui.InstructorActivity;
import panteao.make.ready.activities.show.ui.EpisodeActivity;
import panteao.make.ready.activities.homeactivity.ui.HomeActivity;
import panteao.make.ready.activities.live.LiveActivity;
import panteao.make.ready.activities.show.ui.ShowActivity;
import panteao.make.ready.activities.splash.dialog.ConfigFailDialog;
import panteao.make.ready.activities.tutorial.ui.ChapterActivity;
import panteao.make.ready.activities.tutorial.ui.TutorialActivity;
import panteao.make.ready.baseModels.BaseBindingActivity;
import panteao.make.ready.callbacks.commonCallbacks.DialogInterface;
import panteao.make.ready.dependencies.providers.DTGPrefrencesProvider;
import panteao.make.ready.BuildConfig;
import panteao.make.ready.R;
import panteao.make.ready.SDKConfig;
import panteao.make.ready.activities.series.ui.SeriesDetailActivity;
import panteao.make.ready.callbacks.apicallback.ApiResponseModel;
import panteao.make.ready.callbacks.commonCallbacks.VersionValidator;
import panteao.make.ready.databinding.ActivitySplashBinding;
import panteao.make.ready.fragments.dialog.AlertDialogFragment;
import panteao.make.ready.networking.errormodel.ApiErrorModel;
import panteao.make.ready.utils.MediaTypeConstants;
import panteao.make.ready.utils.commonMethods.AppCommonMethod;
import panteao.make.ready.utils.config.ConfigManager;
import panteao.make.ready.utils.config.bean.ConfigBean;
import panteao.make.ready.utils.constants.AppConstants;
import panteao.make.ready.utils.cropImage.helpers.Logger;
import panteao.make.ready.utils.cropImage.helpers.PrintLogging;
import panteao.make.ready.utils.helpers.AnalyticsController;
import panteao.make.ready.utils.helpers.ForceUpdateHandler;
import panteao.make.ready.utils.helpers.NetworkConnectivity;

import panteao.make.ready.utils.helpers.downloads.KTDownloadHelper;
import panteao.make.ready.utils.helpers.downloads.ManagerStart;
import panteao.make.ready.utils.helpers.intentlaunchers.ActivityLauncher;
import panteao.make.ready.utils.helpers.ksPreferenceKeys.KsPreferenceKeys;

import org.json.JSONObject;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import javax.inject.Inject;

public class ActivitySplash extends BaseBindingActivity<ActivitySplashBinding> implements AlertDialogFragment.AlertDialogListener, ManagerStart {
    private final String TAG = this.getClass().getSimpleName();
    @Inject
    DTGPrefrencesProvider dtgPrefrencesProvider;
    KTDownloadHelper downloadHelper;
    private ForceUpdateHandler forceUpdateHandler;
    private KsPreferenceKeys session;
    private PanteaoApplication appState;
    private boolean viaIntent;
    private long mLastClickTime = 0;
    private String currentLanguage;
    private ConfigBean configBean;
    private int configCall = 1;
    private int count = 0;
    int clapanimation = 1;
    private String notid = "";
    private String notAssetType = "";
    private int notificationAssetId = 0;
    private Animation zoomInAnimation;
    private Animation rotateAnimation;
    private Animation translateAnimation;
    JSONObject deepLinkObject=null;

    @Override
    public ActivitySplashBinding inflateBindingLayout(@NonNull LayoutInflater inflater) {
        return ActivitySplashBinding.inflate(inflater);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//setFullScreen();
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {


        AppCompatDelegate.setCompatVectorFromResourcesEnabled(true);

        if (TextUtils.isEmpty(KsPreferenceKeys.getInstance().getQualityName())) {
            KsPreferenceKeys.getInstance().setQualityName(getApplicationContext().getResources().getString(R.string.auto));
            KsPreferenceKeys.getInstance().setQualityPosition(0);
        }
        if ((!KsPreferenceKeys.getInstance().getAppPrefUserId().equalsIgnoreCase("")) && (KsPreferenceKeys.getInstance().getAppPrefUserId() != null)) {
            FirebaseAnalytics mFirebaseAnalytics = FirebaseAnalytics.getInstance(getApplicationContext());
            mFirebaseAnalytics.setUserId(KsPreferenceKeys.getInstance().getAppPrefUserId());
            mFirebaseAnalytics.setUserProperty("username", KsPreferenceKeys.getInstance().getAppPrefUserName());
        } else {
            Log.d("userid", "userlogin");
        }
        session = KsPreferenceKeys.getInstance();
        // session.setDownloadOverWifi(1);
        AppCommonMethod.getPushToken(ActivitySplash.this);
        updateAndroidSecurityProvider(ActivitySplash.this);

        new AnalyticsController(ActivitySplash.this).callAnalytics("splash_screen", "Action", "Launch");
        // MobileAds.initialize(this, "ca-app-pub-3940256099942544~3347511713");

        currentLanguage = KsPreferenceKeys.getInstance().getAppLanguage();

        PanteaoApplication.getApplicationContext(ActivitySplash.this).getEnveuComponent().inject(ActivitySplash.this);
        dtgPrefrencesProvider.saveExpiryDays(3);
        downloadHelper = new KTDownloadHelper(ActivitySplash.this,ActivitySplash.this);

        notificationCheck();

        connectionObserver();
        getBinding().noConnectionLayout.retryTxt.setOnClickListener(view -> connectionObserver());
        //getBinding().noConnectionLayout.btnMyDownloads.setOnClickListener(view -> new ActivityLauncher(this).launchMyDownloads());
        Logger.e("IntentData", new Gson().toJson(ActivitySplash.this.getIntent().getData()));
        printHashKey();
            }
        },100);
        //6273664
    }

    @Override
    public void managerStarted() {
        if (downloadHelper!=null){
            downloadHelper.deleteAllExpiredVideos();
        }
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


    private void setFullScreen() {
        Log.e("Tag", "Inset: " + Build.VERSION.SDK_INT);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            final WindowInsetsController insetsController = getWindow().getInsetsController();
            if (insetsController != null) {
                insetsController.hide(WindowInsets.Type.statusBars());
            }
            WindowManager.LayoutParams attribs = getWindow().getAttributes();
            attribs.layoutInDisplayCutoutMode = WindowManager.LayoutParams.LAYOUT_IN_DISPLAY_CUTOUT_MODE_ALWAYS;
            getWindow().getDecorView().setOnApplyWindowInsetsListener((view, windowInsets) -> {
                        DisplayCutout inset = windowInsets.getDisplayCutout();
                        Log.d("Tag", "Inset: " + inset);
                        return windowInsets;
                    }
            );
        } else {
            getWindow().setFlags(
                    WindowManager.LayoutParams.FLAG_FULLSCREEN,
                    WindowManager.LayoutParams.FLAG_FULLSCREEN
            );
        }

    }


    private void callConfig(JSONObject jsonObject, String updateType) {
        ConfigManager.getInstance().getConfig(new ApiResponseModel() {
            @Override
            public void onStart() {

            }

            @Override
            public void onSuccess(Object response) {
                Logger.e("Animation End", "Config Call Started");
                boolean isTablet = getResources().getBoolean(R.bool.isTablet);
                configBean = AppCommonMethod.getConfigResponse();
                Gson gson = new Gson();
                String json = gson.toJson(configBean);
                Logger.w("configResponse fromshared", json + "");
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

                BaseClient client = new BaseClient(ActivitySplash.this, BaseGateway.ENVEU, SDKConfig.getInstance().getBASE_URL(), SDKConfig.getInstance().getSUBSCRIPTION_BASE_URL(), DEVICE_TYPE, API_KEY, BasePlatform.android.name(), isTablet, AppCommonMethod.getDeviceId(getContentResolver()));
                BaseConfiguration.Companion.getInstance().clientSetup(client);
                updateLanguage(configBean.getData().getAppConfig().getPrimaryLanguage());
                KsPreferenceKeys.getInstance().setOVPBASEURL(SDKConfig.getInstance().getOVP_BASE_URL());

                if (configBean != null) {
                    startClapAnimation(jsonObject, updateType);

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

    private void startClapAnimation(JSONObject jsonObject, String updateType) {
 {
        if (jsonObject != null) {

            if (updateType != null && updateType.equalsIgnoreCase(ForceUpdateHandler.RECOMMENDED)) {
                brachRedirections(jsonObject);

            } else {
                boolean updateValue = getForceUpdateValue(jsonObject, 1);
                if (!updateValue) {
                    brachRedirections(jsonObject);

                }
            }
        } else {
            if (updateType != null && updateType.equalsIgnoreCase(ForceUpdateHandler.RECOMMENDED)) {
                Log.w("cickc", "-->>config" + "");


                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        Log.w("branchRedirectors", "-->>non" + "");
                        new ActivityLauncher(ActivitySplash.this).homeScreen(ActivitySplash.this, HomeActivity.class);
                        finish();
                    }
                }, 1000);
            } else {
                boolean updateValue = getForceUpdateValue(null, 3);

                if (!updateValue) {
                    Log.w("branchRedirectors", "-->>config" + "");
                    // homeRedirection();
                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            Log.w("branchRedirectors", "-->>non" + "");
                            if(deepLinkObject==null){
                                new ActivityLauncher(ActivitySplash.this).homeScreen(ActivitySplash.this, HomeActivity.class);
                                finish();
                            }

                        }
                    }, 1000);
                }
            }

        }

    }

}


    private void notificationCheck() {
        Logger.w("notificationCheck","in");
        if (getIntent() != null) {
            Logger.w("notificationCheck","notnull");
            if (getIntent().getExtras() != null) {
                Logger.w("notificationCheck","extra");
                Bundle bundle = getIntent().getExtras();
                if (bundle != null) {
                    notid = bundle.getString("id");
                    if (notid != null && !notid.equalsIgnoreCase("")) {
                        notAssetType = bundle.getString("contentType");
                        if (notAssetType != null && !notAssetType.equalsIgnoreCase("")) {
                            parseNotification(notid,notAssetType);
                        }else {
                            onNewIntent(getIntent());
                        }

                    } else {
                        Log.d ("myApplication--->>>", getIntent().toString());
                        onNewIntent(getIntent());
                    }

                } else {
                    onNewIntent(getIntent());
                }

            } else {
                Logger.w("notificationCheck","nonextra");
                onNewIntent(getIntent());
            }
        }else {
            Logger.w("notificationCheck","null");
        }

    }

    private void loadAnimations() {
//        zoomInAnimation = AnimationUtils.loadAnimation(this, R.anim.splash_zoom_in);
//        rotateAnimation = AnimationUtils.loadAnimation(this, R.anim.splash_rotate_animation);
//        translateAnimation = AnimationUtils.loadAnimation(this, R.anim.splash_zoom_out);
//        rotateAnimation.setAnimationListener(new Animation.AnimationListener() {
//            @Override
//            public void onAnimationStart(Animation animation) {
//
//            }
//
//            @Override
//            public void onAnimationEnd(Animation animation) {
//                Log.w("branchRedirectors", "onAnimationEnd2");
////                getBinding().flapView1.setVisibility(View.VISIBLE);
////                getBinding().flapView1.startAnimation(translateAnimation);
//            }
//
//            @Override
//            public void onAnimationRepeat(Animation animation) {
//
//            }
//        });
//        zoomInAnimation.setAnimationListener(new Animation.AnimationListener() {
//            @Override
//            public void onAnimationStart(Animation animation) {
//
//            }
//
//            @Override
//            public void onAnimationEnd(Animation animation) {
                if (viaIntent) {
                    String notiVAlues = KsPreferenceKeys.getInstance().getNotificationPayload(notificationAssetId + "");
                    try {
                        Logger.e("Animation End","Config Call");
                        JSONObject jsonObject = new JSONObject(notiVAlues);
                        redirections(jsonObject);
                    } catch (Exception e) {
                        if (notificationObject!=null){
                            redirections(notificationObject);
                        }else {
                            redirections(null);
                        }
                    }
                }else{
//                    new Handler().postDelayed(new Runnable() {
//                        @Override
//                        public void run() {
//                            Branch.getInstance().initSession(branchReferralInitListener, ActivitySplash.this.getIntent().getData(), ActivitySplash.this);
//                        }
//                    },3000);
                    redirectToHome();

                }
//            }
//
//            @Override
//            public void onAnimationRepeat(Animation animation) {
//
//            }
//        });
//        new Handler().postDelayed(() -> {
//            getBinding().imgLogo.setVisibility(View.VISIBLE);
//            getBinding().imgLogo.startAnimation(zoomInAnimation);
//        }, 100);
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
            GoogleApiAvailability apiAvailability = GoogleApiAvailability.getInstance();
            apiAvailability.getErrorDialog(callingActivity,0,0);
        } catch (GooglePlayServicesNotAvailableException e) {
            Logger.e("SecurityException", "Google Play Services not available.");
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        try {
            boolean isTablet = ActivitySplash.this.getResources().getBoolean(R.bool.isTablet);
            if (!isTablet)
                getBinding().buildNumber.setText(getResources().getString(R.string.app_name) + "  v" + BuildConfig.VERSION_NAME);
        }catch (Exception e){

        }

    }

    @Override
    protected void onStart() {
        super.onStart();
       // Branch.getInstance().initSession(branchReferralInitListener, ActivitySplash.this.getIntent().getData(), ActivitySplash.this);
        //Branch.sessionBuilder(this).withCallback(branchReferralInitListener).withData(getIntent() != null ? getIntent().getData() : null).init();
    }

/*   private Branch.BranchReferralInitListener branchReferralInitListener = new Branch.BranchReferralInitListener() {
        @Override
        public void onInitFinished(@Nullable JSONObject referringParams, @Nullable BranchError error) {
            if (error == null) {
                Log.i("returnedObject er", referringParams + "");
                if (referringParams != null) {
                    Logger.e("BranchCall", String.valueOf(referringParams));
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
                    }
                } else {
                    redirectToHome();

                }
            } else {
                redirectToHome();
                Log.i("returnedObject er", error.getMessage());
            }
        }
    };*/

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
        new ConfigFailDialog(ActivitySplash.this).showDialog(new DialogInterface() {
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




        /*BottomSheetDialog mBottomSheetDialog = new BottomSheetDialog(ActivitySplash.this);
        View sheetView = getLayoutInflater().inflate(R.layout.config_failure, null);
        mBottomSheetDialog.setContentView(sheetView);

        Button retrybutton = (Button) sheetView.findViewById(R.id.retryBtn);
        Button cancelbutton = (Button) sheetView.findViewById(R.id.cancenBtn);

        retrybutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                KsPreferenceKeys.getInstance().setString("DMS_Date", "mDate");
                mBottomSheetDialog.dismiss();
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        getBinding().progressBar.setVisibility(View.VISIBLE);
                        configRetry = true;
                        callConfig(null);
                    }
                }, 200);

            }
        });

        cancelbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getBinding().progressBar.setVisibility(View.GONE);
                mBottomSheetDialog.dismiss();
                finish();
            }
        });
        mBottomSheetDialog.show();*/
    }


    private void  redirections(JSONObject jsonObject) {
        try {
            callConfig(jsonObject, null);
        } catch (Exception e) {

        }
    }

    private void brachRedirections(JSONObject jsonObject) {
        try {
            Log.e("branchRedirectors", new Gson().toJson(jsonObject));
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
                        new ActivityLauncher(ActivitySplash.this).seriesDetailScreen(ActivitySplash.this, SeriesDetailActivity.class, assestId);
                        //finish();


                    }  else if (contentType.equalsIgnoreCase(MediaTypeConstants.getInstance().getShow())) {
                        //new ActivityLauncher(ActivitySplash.this).articleScreen(ActivitySplash.this, ArticleActivity.class, assestId, "0", false);
                        new ActivityLauncher(this).homeScreen(this, HomeActivity.class);
                        new ActivityLauncher(ActivitySplash.this).showScreen(ActivitySplash.this, ShowActivity.class, assestId, "0", false);


                    }  else if (contentType.equalsIgnoreCase(MediaTypeConstants.getInstance().getTutorial())) {
                        mLastClickTime = SystemClock.elapsedRealtime();
                        //new ActivityLauncher(ActivitySplash.this).articleScreen(ActivitySplash.this, ArticleActivity.class, assestId, "0", false);
                        new ActivityLauncher(this).homeScreen(this, HomeActivity.class);
                        new ActivityLauncher(ActivitySplash.this).tutorialDetailScreen(ActivitySplash.this, TutorialActivity.class, assestId);


                    }  else if (contentType.equalsIgnoreCase(MediaTypeConstants.getInstance().getInstructor())) {
                        //new ActivityLauncher(ActivitySplash.this).articleScreen(ActivitySplash.this, ArticleActivity.class, assestId, "0", false);
                        new ActivityLauncher(this).homeScreen(this, HomeActivity.class);
                        new ActivityLauncher(ActivitySplash.this).detailScreen(ActivitySplash.this, InstructorActivity.class, assestId, "0", false);

                    }  else if (contentType.equalsIgnoreCase(MediaTypeConstants.getInstance().getChapter())) {
                        //new ActivityLauncher(ActivitySplash.this).articleScreen(ActivitySplash.this, ArticleActivity.class, assestId, "0", false);
                        new ActivityLauncher(this).homeScreen(this, HomeActivity.class);
                        new ActivityLauncher(ActivitySplash.this).chapterScreen(ActivitySplash.this, ChapterActivity.class, assestId, "0", false);



                        // finish();
                    } else if (contentType.equalsIgnoreCase(MediaTypeConstants.getInstance().getLive())) {
                        if (SystemClock.elapsedRealtime() - mLastClickTime < 1200) {
                            return;
                        }
                        mLastClickTime = SystemClock.elapsedRealtime();
                        //new ActivityLauncher(ActivitySplash.this).articleScreen(ActivitySplash.this, ArticleActivity.class, assestId, "0", false);
                        new ActivityLauncher(this).homeScreen(this, HomeActivity.class);
                        new ActivityLauncher(ActivitySplash.this).liveScreenBrightCove(ActivitySplash.this, LiveActivity.class, 0l, assestId, "0", false, SDKConfig.getInstance().getLiveDetailId());
                       // finish();
                    } else if (contentType.equalsIgnoreCase(MediaTypeConstants.getInstance().getEpisode())) {
                        if (SystemClock.elapsedRealtime() - mLastClickTime < 1200) {
                            return;
                        }
                        mLastClickTime = SystemClock.elapsedRealtime();
                        new ActivityLauncher(this).homeScreen(this, HomeActivity.class);
                        new ActivityLauncher(ActivitySplash.this).episodeScreen(ActivitySplash.this, EpisodeActivity.class, assestId, "0", false);
                       // finish();
                    } else if (contentType.equalsIgnoreCase(AppConstants.ContentType.ARTICLE.toString())) {
                        if (SystemClock.elapsedRealtime() - mLastClickTime < 1200) {
                            return;
                        }
                        mLastClickTime = SystemClock.elapsedRealtime();
                        new ActivityLauncher(this).homeScreen(this, HomeActivity.class);
                        new ActivityLauncher(ActivitySplash.this).articleScreen(ActivitySplash.this, ArticleActivity.class, assestId, "0", false);
                       // finish();
                    }


                } else {
                    new ActivityLauncher(this).homeScreen(this, HomeActivity.class);
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
            try {

            }catch (Exception ignored){
                getBinding().noConnectionLayout.btnMyDownloads.setVisibility(View.GONE);
            }
        }
    }

    private void connectionValidation(Boolean aBoolean) {
        if (aBoolean) {
            getBinding().noConnectionLayout.noConnectionLayout.setVisibility(View.GONE);
            loadAnimations();
        } else {
            try {
                getBinding().noConnectionLayout.noConnectionLayout.setVisibility(View.VISIBLE);
                getBinding().noConnectionLayout.noConnectionLayout.bringToFront();
                boolean isTablet = getResources().getBoolean(R.bool.isTablet);
                configBean=AppCommonMethod.getConfigResponse();
                if (configBean!=null){
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
                    BaseClient client = new BaseClient(ActivitySplash.this, BaseGateway.ENVEU, SDKConfig.getInstance().getBASE_URL(), SDKConfig.getInstance().getSUBSCRIPTION_BASE_URL(), DEVICE_TYPE, API_KEY, BasePlatform.android.name(), isTablet, AppCommonMethod.getDeviceId(getContentResolver()));
                    BaseConfiguration.Companion.getInstance().clientSetup(client);
                    getBinding().noConnectionLayout.btnMyDownloads.setVisibility(View.VISIBLE);

                    getBinding().noConnectionLayout.btnMyDownloads.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            KsPreferenceKeys.getInstance().setFromOfflineClick(2);
                            new ActivityLauncher(ActivitySplash.this).homeScreen(ActivitySplash.this, HomeActivity.class);
                            finish();
                        }
                    });

                }
            }catch (Exception ignored){

            }
        }
    }

    @Override
    public void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        this.setIntent(intent);
        try {
            notid = intent.getStringExtra("assetId");
            notAssetType=intent.getStringExtra("assetType");
            parseNotification(notid,notAssetType);

        } catch (Exception e) {

        }

    }

    JSONObject notificationObject=null;
    private void parseNotification(String notid,String assetType) {
        if (notid != null && !assetType.equalsIgnoreCase("")) {
            notificationAssetId = Integer.parseInt(notid);
            if (notificationAssetId > 0 && assetType!=null && !assetType.equalsIgnoreCase("")) {
                Logger.w("FCM_Payload_final --", notificationAssetId + "");
                notificationObject=AppCommonMethod.createNotificationObject(notid,assetType);
                viaIntent = true;
            }
        } else {
         //   Branch.getInstance().reInitSession(this, branchReferralInitListener);
            setBranchInIt();
        }
    }

    private void setBranchInIt() {

        FirebaseDynamicLinks.getInstance().getDynamicLink(getIntent()).addOnSuccessListener(new OnSuccessListener<PendingDynamicLinkData>() {
            @Override
            public void onSuccess(@NonNull PendingDynamicLinkData pendingDynamicLinkData) {
                try {
                        Uri deepLink=null;
                    if (pendingDynamicLinkData != null) {
                           deepLink = pendingDynamicLinkData.getLink();
                        Log.e("deepLink","in2"+pendingDynamicLinkData.getLink()+" "+deepLink.getQuery());

                        if (deepLink!=null){

                            Uri uri = Uri.parse(String.valueOf(deepLink));
                            String id = null;
                            String mediaType = null;
                            try {
                                 id = uri.getQueryParameter("id");
                                 mediaType = uri.getQueryParameter("mediaType");
                                 Log.w("programeID",id);
                                 Log.w("programeMediaType",mediaType);
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                            try {
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

                            }catch (Exception e){
                                e.printStackTrace();
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
                }catch (Exception e){
                    Logger.e("Catch", String.valueOf(e));
                }



            }

        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                e.printStackTrace();

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
        forceUpdateHandler = new ForceUpdateHandler(ActivitySplash.this, configBean);
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
                            }/*else {

                            }
                            if (type == 1) {
                                forceUpdateHandler.hideDialog();
                                brachRedirections(jsonObject);
                            } else {
                                Log.w("branchRedirectors", "-->>force" + "");
                                homeRedirection();
                            }*/

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