package panteao.make.ready.utils.helpers;

import android.app.Activity;

import androidx.annotation.NonNull;

import panteao.make.ready.PanteaoApplication;
import panteao.make.ready.beanModel.configBean.ResponseConfig;
import panteao.make.ready.callbacks.commonCallbacks.DialogInterface;
import panteao.make.ready.callbacks.commonCallbacks.VersionUpdateCallBack;
import panteao.make.ready.callbacks.commonCallbacks.VersionValidator;
import panteao.make.ready.networking.apiendpoints.ApiInterface;
import panteao.make.ready.networking.apiendpoints.RequestConfig;
import panteao.make.ready.utils.helpers.ksPreferenceKeys.KsPreferenceKeys;
import panteao.make.ready.utils.commonMethods.AppCommonMethod;
import com.google.gson.Gson;
import panteao.make.ready.utils.config.bean.ConfigBean;
import panteao.make.ready.utils.config.bean.Version;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ForceUpdateHandler {
    final Activity activity;
    final KsPreferenceKeys session;
    final MaterialDialog materialDialog;
    VersionValidator versionValidator;
    VersionUpdateCallBack versionUpdateCallBack;
    ConfigBean configBean;
    public static String FORCE="force";
    public static String RECOMMENDED="recommended";


    public ForceUpdateHandler(Activity context, ConfigBean configBean) {
        this.activity = context;
        session = KsPreferenceKeys.getInstance();
        materialDialog = new MaterialDialog(activity);
        this.configBean=configBean;
    }

    public void checkCurrentVersion(VersionValidator callBack) {
       // checkPlaystoreVersion(currentVersion, callBack);
        versionValidator = callBack;
        checkVersion(configBean);
    }

    PanteaoApplication application;
    private void checkVersion(ConfigBean configBean) {
        if (configBean!=null){
           application= ((PanteaoApplication) activity.getApplication());
           //configBean.getData().getAppConfig().getVersion().setForceUpdate(false);
           Version version=configBean.getData().getAppConfig().getVersion();
           if (version.getForceUpdate()){
               String appversion = application.getVersionName().replace(".", "");
               int appCurrentVersion = Integer.parseInt(appversion);
               String configVersion= version.getUpdatedVersion();
               if (!configVersion.equalsIgnoreCase("")) {
                   if (configVersion.contains(".")) {
                       configVersion = configVersion.replace(".", "");
                       if (!configVersion.equalsIgnoreCase("")) {
                           int configAppCurrentVersion = Integer.parseInt(configVersion);
                              if (appCurrentVersion<configAppCurrentVersion){
                                  versionValidator.version(false, appCurrentVersion, configAppCurrentVersion,FORCE);
                              }else {
                                  versionValidator.version(false, appCurrentVersion, configAppCurrentVersion,FORCE);
                              }
                       }else {
                           versionValidator.version(false, appCurrentVersion, 0,FORCE);
                       }
                   }
                   else {
                       versionValidator.version(false, appCurrentVersion, 0,FORCE);
                   }
               }else {
                   versionValidator.version(false, appCurrentVersion, 0,FORCE);
               }

           }else if (version.getRecommendedUpdate()){
               String appversion = application.getVersionName().replace(".", "");
               int appCurrentVersion = Integer.parseInt(appversion);
               String configVersion= version.getUpdatedVersion();
               if (!configVersion.equalsIgnoreCase("")) {
                   if (configVersion.contains(".")) {
                       configVersion = configVersion.replace(".", "");
                       if (!configVersion.equalsIgnoreCase("")) {
                           int configAppCurrentVersion = Integer.parseInt(configVersion);
                           if (appCurrentVersion < configAppCurrentVersion) {
                               versionValidator.version(true, appCurrentVersion, configAppCurrentVersion, RECOMMENDED);
                           } else {
                               versionValidator.version(false, appCurrentVersion, configAppCurrentVersion, RECOMMENDED);
                           }
                       } else {
                           versionValidator.version(false, appCurrentVersion, 0, RECOMMENDED);
                       }
                   } else {
                       versionValidator.version(false, appCurrentVersion, 0, RECOMMENDED);
                   }
               }
           }else {
               versionValidator.version(false, 0, 0, RECOMMENDED);
           }
        }
    }

    private void checkPlaystoreVersion(final int currentVersion, final VersionValidator callBack) {
        versionValidator = callBack;
        ApiInterface endpoint = RequestConfig.getClient().create(ApiInterface.class);


        Call<ResponseConfig> call = endpoint.getConfiguration("true");
        call.enqueue(new Callback<ResponseConfig>() {
            @Override
            public void onResponse(@NonNull Call<ResponseConfig> call, @NonNull Response<ResponseConfig> response) {
                if (response.body() != null) {
                    AppCommonMethod.urlPoints = response.body().getData().getImageTransformationEndpoint();
                    ResponseConfig cl = response.body();
                    KsPreferenceKeys ksPreferenceKeys = KsPreferenceKeys.getInstance();
                    Gson gson = new Gson();
                    String json = gson.toJson(cl);


                    AppCommonMethod.urlPoints = /*AppConstants.PROFILE_URL +*/ response.body().getData().getImageTransformationEndpoint();

                    ksPreferenceKeys.setAppPrefLastConfigHit(String.valueOf(System.currentTimeMillis()));
                    ksPreferenceKeys.setAppPrefConfigResponse(json);
                    ksPreferenceKeys.setAppPrefVideoUrl(response.body().getData().getCloudFrontVideoEndpoint());
                    ksPreferenceKeys.setAppPrefAvailableVersion(response.body().getData().getUpdateInfo().getAvailableVersion());
                    ksPreferenceKeys.setAppPrefCfep(AppCommonMethod.urlPoints);
                    ksPreferenceKeys.setAppPrefConfigVersion(String.valueOf(response.body().getData().getConfigVersion()));
                    ksPreferenceKeys.setAppPrefServerBaseUrl(response.body().getData().getServerBaseURL());
                  //  versionValidator.version(false, currentVersion, currentVersion);

                }
            }

            @Override
            public void onFailure(@NonNull Call<ResponseConfig> call, @NonNull Throwable t) {

            }
        });
    }

    public void hideDialog() {
        materialDialog.hide();
    }

    public void typeHandle(String type,VersionUpdateCallBack callBack) {
        versionUpdateCallBack = callBack;
        if (KsPreferenceKeys.getInstance().getAppLanguage().equalsIgnoreCase("Thai") || KsPreferenceKeys.getInstance().getAppLanguage().equalsIgnoreCase("हिंदी") ){
            AppCommonMethod.updateLanguage("th", PanteaoApplication.getInstance());
        } else if (KsPreferenceKeys.getInstance().getAppLanguage().equalsIgnoreCase("English")){
            AppCommonMethod.updateLanguage("en", PanteaoApplication.getInstance());
        }
        materialDialog.showDialog(type, "", activity, new DialogInterface() {
            @Override
            public void positiveAction() {
                versionUpdateCallBack.selection(false);
            }

            @Override
            public void negativeAction() {
                versionUpdateCallBack.selection(true);
            }
        });

    }
}
