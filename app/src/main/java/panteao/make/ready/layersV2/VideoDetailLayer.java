package panteao.make.ready.layersV2;

import com.google.gson.Gson;

import panteao.make.ready.beanModel.enveuCommonRailData.RailCommonData;
import panteao.make.ready.beanModelV3.videoDetailsV2.EnveuVideoDetails;
import panteao.make.ready.beanModelV3.videoDetailsV2.EnveuVideoDetailsBean;
import panteao.make.ready.callbacks.apicallback.ApiResponseModel;
import panteao.make.ready.callbacks.commonCallbacks.CommonApiCallBack;
import panteao.make.ready.networking.apiendpoints.ApiInterface;
import panteao.make.ready.networking.apiendpoints.RequestConfig;
import panteao.make.ready.networking.errormodel.ApiErrorModel;
import panteao.make.ready.networking.servicelayer.APIServiceLayer;
import panteao.make.ready.utils.commonMethods.AppCommonMethod;
import panteao.make.ready.utils.config.LanguageLayer;

import panteao.make.ready.utils.cropImage.helpers.Logger;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class VideoDetailLayer {

    private static VideoDetailLayer videoDetailLayerInstance;
    private static ApiInterface endpoint;
    ApiResponseModel callBack;

    private VideoDetailLayer() {

    }

    public static VideoDetailLayer getInstance() {
        if (videoDetailLayerInstance == null) {
            endpoint = RequestConfig.getEnveuClient().create(ApiInterface.class);
            videoDetailLayerInstance = new VideoDetailLayer();
        }
        return (videoDetailLayerInstance);
    }

    String languageCode;
    public void getVideoDetails(String manualImageAssetId, ApiResponseModel listener) {
        this.callBack = listener;
        callBack.onStart();
        languageCode= LanguageLayer.getCurrentLanguageCode();
        endpoint.getVideoDetails(manualImageAssetId,languageCode).enqueue(new Callback<EnveuVideoDetailsBean>() {
            @Override
            public void onResponse(Call<EnveuVideoDetailsBean> call, Response<EnveuVideoDetailsBean> response) {
                if (response.isSuccessful()) {
                    if (response.body().getData() != null) {
                        RailCommonData railCommonData = new RailCommonData();
                        AppCommonMethod.getAssetDetail(railCommonData, response);
                        Logger.e("ASSET_DETAILS", new Gson().toJson(railCommonData));
                        callBack.onSuccess(railCommonData);
                    }

                } else {
                    ApiErrorModel errorModel = new ApiErrorModel(response.code(), response.message());
                    callBack.onError(errorModel);
                }

            }

            @Override
            public void onFailure(Call<EnveuVideoDetailsBean> call, Throwable t) {
                ApiErrorModel errorModel = new ApiErrorModel(500, t.getMessage());
                callBack.onFailure(errorModel);
            }
        });
    }


    public void getAssetTypeHero(String manualImageAssetId, CommonApiCallBack commonApiCallBack) {
        APIServiceLayer.getInstance().getAssetTypeHero(manualImageAssetId, commonApiCallBack);
    }


}