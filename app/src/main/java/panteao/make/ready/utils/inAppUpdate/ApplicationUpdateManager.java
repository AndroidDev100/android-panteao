package panteao.make.ready.utils.inAppUpdate;

import android.content.Context;
import android.content.IntentSender;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.play.core.appupdate.AppUpdateInfo;
import com.google.android.play.core.appupdate.AppUpdateManager;
import com.google.android.play.core.appupdate.AppUpdateManagerFactory;
import com.google.android.play.core.install.model.AppUpdateType;
import com.google.android.play.core.install.model.UpdateAvailability;
import com.google.android.play.core.tasks.Task;
import panteao.make.ready.utils.cropImage.helpers.Logger;

public class ApplicationUpdateManager {

    private static ApplicationUpdateManager applicationUpdateManager;

    //    private boolean isUpdateAvailable = false;
    private final AppUpdateManager appUpdateManager;
    private AppUpdateInfo appUpdateInfoObject;

    public static final int APP_UPDATE_REQUEST_CODE = 101;
    private AppUpdateCallBack appUpdateCallBack;

    private ApplicationUpdateManager(Context context) {

        appUpdateManager = AppUpdateManagerFactory.create(context);

    }


    public static ApplicationUpdateManager getInstance(Context context) {

        if (applicationUpdateManager == null) {
            synchronized (ApplicationUpdateManager.class) {

                if (applicationUpdateManager == null) {
                    applicationUpdateManager = new ApplicationUpdateManager(context);
                }
            }
        }

        return applicationUpdateManager;
    }


    public AppUpdateManager getAppUpdateManager() {
        return appUpdateManager;
    }

    /**
     * Check for app update availability
     */

    public void isUpdateAvailable() {


        // Creates instance of the manager.
//        AppUpdateManager appUpdateManager = AppUpdateManagerFactory.create(context);

// Returns an intent object that you use to check for an update.
        Task<AppUpdateInfo> appUpdateInfoTask = appUpdateManager.getAppUpdateInfo();

// Checks that the platform will allow the specified type of update.
        appUpdateInfoTask.addOnSuccessListener(appUpdateInfo -> {
            if (appUpdateInfo.updateAvailability() == UpdateAvailability.UPDATE_AVAILABLE
                    && appUpdateInfo.isUpdateTypeAllowed(AppUpdateType.IMMEDIATE)) {
                // Request the update.

//                isUpdateAvailable = true;
//                appUpdateInfoObject = appUpdateInfo;

                appUpdateCallBack.getAppUpdateCallBack();

            } else if (appUpdateInfo.updateAvailability() == UpdateAvailability.UPDATE_AVAILABLE
                    // For a flexible update, use AppUpdateType.FLEXIBLE
                    && appUpdateInfo.isUpdateTypeAllowed(AppUpdateType.FLEXIBLE)) {
                // Request the update.

//                isUpdateAvailable = true;

//                appUpdateInfoObject = appUpdateInfo;


                appUpdateCallBack.getAppUpdateCallBack();
            } else {
                Logger.w("inapp update", "Update availability " + appUpdateInfo.updateAvailability());
            }
        });

//        return isUpdateAvailable;

//        return appUpdateInfoObject;
    }


    /**
     * if update available start update
     */

    public void startUpdate(AppUpdateInfo appUpdateInfo, int updateType, Context context, int requestCode) {


        try {
            appUpdateManager.startUpdateFlowForResult(
                    // Pass the intent that is returned by 'getAppUpdateInfo()'.
                    appUpdateInfo,
                    // Or 'AppUpdateType.FLEXIBLE' for flexible updates.
                    updateType,
                    // The current activity making the update request.
                    ((AppCompatActivity) context),
                    // Include a request code to later monitor this update request.
                    requestCode);
//            ApplicationUpdateManager.getInstance(getApplicationContext()).getAppUpdateManager().startUpdateFlowForResult(appUpdateInfo, AppUpdateType.IMMEDIATE, this, APP_UPDATE_REQUEST_CODE);
        } catch (IntentSender.SendIntentException e) {
//            e.printStackTrace();

            Logger.w("Appupdate exception", "" + e);
        } catch (Exception e) {

            Logger.w("Appupdate exception", "" + e);
        }
    }


    public void setAppUpdateCallBack(AppUpdateCallBack appUpdateCallBack) {
        this.appUpdateCallBack = appUpdateCallBack;
    }


}
