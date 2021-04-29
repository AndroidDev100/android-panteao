package panteao.make.ready;

import android.app.Application;
import android.content.Context;
import android.content.pm.PackageManager;
import android.os.AsyncTask;

import androidx.appcompat.app.AppCompatDelegate;
import androidx.multidex.BuildConfig;
import androidx.multidex.MultiDex;
import androidx.multidex.MultiDexApplication;

import com.make.baseClient.BaseDeviceType;
import com.google.firebase.analytics.FirebaseAnalytics;
import com.google.firebase.crashlytics.FirebaseCrashlytics;

import panteao.make.ready.dependencies.DaggerEnveuComponent;
import panteao.make.ready.dependencies.EnveuComponent;
import panteao.make.ready.dependencies.modules.UserPreferencesModule;
import panteao.make.ready.utils.TrackerUtil.TrackerUtil;
import panteao.make.ready.utils.constants.AppConstants;
import panteao.make.ready.utils.helpers.ksPreferenceKeys.KsPreferenceKeys;
import com.google.android.gms.analytics.GoogleAnalytics;
import com.google.android.gms.analytics.Tracker;
import com.google.firebase.FirebaseApp;

import io.branch.referral.Branch;


/**
 * This is a subclass of {@link Application} used to provide shared objects for this app, such as
 * the {@link Tracker}.
 */
public class PanteaoApplication extends MultiDexApplication {
    private EnveuComponent enveuComponent;
    private static GoogleAnalytics sAnalytics;
    private static Tracker sTracker;
    private static PanteaoApplication panteaoApplication;
    private FirebaseAnalytics mFirebaseAnalytics;


    public static PanteaoApplication getInstance() {
        return panteaoApplication;
    }

    private void setupBaseClient() {
        boolean isTablet = getResources().getBoolean(R.bool.isTablet);
        String API_KEY = "";
        String DEVICE_TYPE = "";
        if (isTablet) {
            API_KEY = SDKConfig.API_KEY_TAB;
            DEVICE_TYPE = BaseDeviceType.tablet.name();
        } else {
            API_KEY = SDKConfig.API_KEY_MOB;
            DEVICE_TYPE = BaseDeviceType.mobile.name();
        }

        // BaseClient client = new BaseClient(this, BaseGateway.ENVEU, SDKConfig.BASE_URL, SDKConfig.SUBSCRIPTION_BASE_URL, DEVICE_TYPE, BasePlatform.android.name(), isTablet, AppCommonMethod.getDeviceId(getContentResolver()));
        //BaseConfiguration.Companion.getInstance().clientSetup(client);
    }

    @Override
    public void onCreate() {
        super.onCreate();
        AppCompatDelegate.setCompatVectorFromResourcesEnabled(true);
        panteaoApplication = this;
        KsPreferenceKeys.getInstance();
        MultiDex.install(this);
        if (BuildConfig.FLAVOR.equals("dev"))
            Branch.enableTestMode();
        AsyncTask.execute(new Runnable() {
            @Override
            public void run() {
                TrackerUtil.getInstance(getApplicationContext());
                FirebaseApp.initializeApp(getApplicationContext());
            }
        });

        Branch.getAutoInstance(this);
        Branch.enableDebugMode();
        //  setupBaseClient();

        // Obtain the FirebaseAnalytics instance.
        mFirebaseAnalytics = FirebaseAnalytics.getInstance(this);
        firebaseCrashlyticSetup();

        /*flutterEngine = new FlutterEngine(this);
        // Configure an initial route.
        flutterEngine.getNavigationChannel().setInitialRoute("/");
        // Start executing Dart code to pre-warm the FlutterEngine.
        flutterEngine.getDartExecutor().executeDartEntrypoint(
                DartExecutor.DartEntrypoint.createDefault()
        );
        // Cache the FlutterEngine to be used by FlutterActivity or FlutterFragment.
        FlutterEngineCache
                .getInstance()
                .put("my_engine_id", flutterEngine);*/

    }

    private void firebaseCrashlyticSetup() {

        if (KsPreferenceKeys.getInstance().getAppPrefLoginStatus().equalsIgnoreCase(AppConstants.UserStatus.Login.toString())) {
            String userId = KsPreferenceKeys.getInstance().getAppPrefUserId();
            FirebaseCrashlytics.getInstance().setUserId(userId);
        }
    }


    /**
     * Gets the default {@link Tracker} for this {@link Application}.
     *
     * @return tracker
     */
    synchronized public Tracker getDefaultTracker() {
        // To enable debug logging use: adb shell setprop log.tag.GAv4 DEBUG
        try {
            if (sTracker == null) {
                sTracker = TrackerUtil.getAnalyticsInstance().newTracker(R.xml.global_tracker);

//            sTracker = TrackerUtil.getAnalyticsInstance().newTracker(R.xml.global_tracker);
            }
        } catch (Exception ignored) {

        }

        return sTracker;
    }

    public int getVersion() {
        int v = 0;
        try {
            v = getPackageManager().getPackageInfo(getPackageName(), 0).versionCode;
        } catch (PackageManager.NameNotFoundException e) {
            // Huh? Really?
        }
        return v;
    }

    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
        MultiDex.install(this);
    }

    public static PanteaoApplication getApplicationContext(Context context) {
        return (PanteaoApplication) context.getApplicationContext();
    }

    public EnveuComponent getEnveuComponent() {
        if (this.enveuComponent == null) {
            this.enveuComponent = DaggerEnveuComponent.builder()
                    .userPreferencesModule(new UserPreferencesModule(this))
                    .build();
        }
        return this.enveuComponent;
    }

    public String getVersionName() {
        String v = "";
        try {
            v = getPackageManager().getPackageInfo(getPackageName(), 0).versionName;
        } catch (PackageManager.NameNotFoundException e) {
            // Huh? Really?
        }
        return v;
    }
}
