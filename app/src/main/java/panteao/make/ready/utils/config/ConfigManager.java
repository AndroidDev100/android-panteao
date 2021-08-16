package panteao.make.ready.utils.config;

import android.util.Log;

import com.google.gson.Gson;
import panteao.make.ready.callbacks.apicallback.ApiResponseModel;
import panteao.make.ready.networking.apiendpoints.ApiInterface;
import panteao.make.ready.networking.apiendpoints.RequestConfig;
import panteao.make.ready.networking.errormodel.ApiErrorModel;
import panteao.make.ready.utils.commonMethods.AppCommonMethod;
import panteao.make.ready.utils.config.bean.ConfigBean;
import panteao.make.ready.utils.cropImage.helpers.Logger;
import panteao.make.ready.utils.helpers.ksPreferenceKeys.KsPreferenceKeys;
import panteao.make.ready.SDKConfig;

import java.text.SimpleDateFormat;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ConfigManager {
    private static ConfigManager configManagerInstance;
    private static ApiInterface endpoint;
    private static boolean isDmsDataStored = false;
    ConfigBean configBean;
    ApiResponseModel callBack;

    private ConfigManager() {

    }

    public static ConfigManager getInstance() {
        if (configManagerInstance == null) {
            endpoint = RequestConfig.getConfigClient().create(ApiInterface.class);
            configManagerInstance = new ConfigManager();
        }
        return (configManagerInstance);
    }

    public boolean getConfig(ApiResponseModel listener) {
        this.callBack = listener;
        callBack.onStart();
        boolean _date = verifyDmsDate(KsPreferenceKeys.getInstance().getString("DMS_Date", "mDate"));
        Logger.w("configResponse", _date + "");
        if (_date) {
            endpoint.getConfig(SDKConfig.getInstance().CONFIG_VERSION).enqueue(new Callback<ConfigBean>() {
                @Override
                public void onResponse(Call<ConfigBean> call, Response<ConfigBean> response) {
                    Logger.w("configResponse", response + "");
                    if (response.isSuccessful()) {

                        Logger.w("configResponse", response.body().getData() + "");
                        Log.e("configResponse", response.body().getData() + "");

                        Gson gson = new Gson();
                        String json = gson.toJson(response.body());
                        Logger.w("configResponse", json + "");
                        KsPreferenceKeys.getInstance().setString("DMS_Response", json);
                        KsPreferenceKeys.getInstance().setString("DMS_Date", "" + System.currentTimeMillis());
                        callBack.onSuccess(response.body());
                        Log.w("redirectionss", "inone");

                    } else {

                        isDmsDataStored = checkPreviousDmsResponse();
                        if (isDmsDataStored) {
                            KsPreferenceKeys.getInstance().setString("DMS_Date", "" + System.currentTimeMillis());
                            callBack.onSuccess(configBean);
                        } else {
                            ApiErrorModel errorModel = new ApiErrorModel(response.code(), response.message());
                            callBack.onError(errorModel);
                            Log.w("redirectionss", "inthree");
                        }


                    }

                }

                @Override
                public void onFailure(Call<ConfigBean> call, Throwable t) {
                    Logger.w("configResponse--", t.getMessage() + "");
                    isDmsDataStored = checkPreviousDmsResponse();
                    if (isDmsDataStored) {
                        KsPreferenceKeys.getInstance().setString("DMS_Date", "" + System.currentTimeMillis());
                        callBack.onSuccess(configBean);
                    } else {
                        ApiErrorModel errorModel = new ApiErrorModel(500, t.getMessage());
                        callBack.onFailure(errorModel);
                    }
                }
            });
        } else {
            Log.w("redirectionss", "intwo");
            callBack.onSuccess(configBean);
        }

        return false;
    }

    private boolean checkPreviousDmsResponse() {
        if (AppCommonMethod.getConfigResponse() != null) {
            return true;
        } else {
            return false;
        }
    }

    private boolean verifyDmsDate(String storedDate) {
        boolean verifyDms = false;

        if (storedDate == null || storedDate.equalsIgnoreCase("mDate")) {
            verifyDms = true;
            return verifyDms;
        }

        String currentDate = getDateTimeStamp(System.currentTimeMillis());
        String temp = getDateTimeStamp(Long.parseLong(storedDate));
        if (currentDate.equalsIgnoreCase(temp))
            verifyDms = false;
        else
            verifyDms = true;

        return verifyDms;
    }

    private String getDateTimeStamp(Long timeStamp) {
        SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");
        return formatter.format(timeStamp);
    }

}
