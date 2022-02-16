package panteao.make.ready.repository.watchList;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import panteao.make.ready.userAssetList.ResponseUserAssetList;
import panteao.make.ready.beanModel.AssetHistoryContinueWatching.ResponseAssetHistory;
import panteao.make.ready.beanModel.allWatchList.ResponseAllWatchList;
import panteao.make.ready.beanModel.emptyResponse.ResponseEmpty;
import panteao.make.ready.beanModel.watchHistory.ResponseWatchHistory;
import panteao.make.ready.networking.detailPlayer.APIDetails;
import panteao.make.ready.networking.apiendpoints.ApiInterface;
import panteao.make.ready.networking.apiendpoints.RequestConfig;
import panteao.make.ready.utils.constants.AppConstants;
import panteao.make.ready.utils.cropImage.helpers.Logger;
import panteao.make.ready.utils.helpers.StringUtils;
import com.google.gson.JsonArray;
import com.google.gson.JsonNull;
import com.google.gson.JsonObject;

import org.json.JSONObject;

import java.util.Objects;

import panteao.make.ready.utils.cropImage.helpers.Logger;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class WatchListRepository {


    private static WatchListRepository instance;

    private WatchListRepository() {
    }

    public static WatchListRepository getInstance() {
        if (instance == null) {
            instance = new WatchListRepository();
        }
        return (instance);
    }


    public LiveData<ResponseAllWatchList> hitApiAllWatchList(String token, int page, int length) {
        MutableLiveData<ResponseAllWatchList> responsePlayer = new MutableLiveData<>();

        APIDetails endpoint = RequestConfig.getClientInterceptor(token).create(APIDetails.class);
        Call<ResponseAllWatchList> call = endpoint.getAllWatchList(page, length);
        call.enqueue(new Callback<ResponseAllWatchList>() {
            @Override
            public void onResponse(@NonNull Call<ResponseAllWatchList> call, @NonNull Response<ResponseAllWatchList> response) {
                ResponseAllWatchList responseAllWatchList = new ResponseAllWatchList();

                if (response.code() == 200) {
                    responseAllWatchList.setStatus(true);
                    responseAllWatchList.setData(Objects.requireNonNull(response.body()).getData());
                    responsePlayer.postValue(responseAllWatchList);
                } else if (response.code() == 404) {
                    String debugMessage = "";
                    try {
                        JSONObject jObjError = new JSONObject(Objects.requireNonNull(response.errorBody()).string());
                        debugMessage = jObjError.getString("debugMessage");
                        Logger.e("", "" + jObjError.getString("debugMessage"));
                    } catch (Exception e) {
                        Logger.i("WatchListRepository", "less");
                    }

                    responseAllWatchList.setResponseCode(response.code());
                    responseAllWatchList.setStatus(false);
                    if (!StringUtils.isNullOrEmptyOrZero(debugMessage))
                        responseAllWatchList.setDebugMessage(debugMessage);
                    responsePlayer.postValue(responseAllWatchList);
                } else {
                    responseAllWatchList.setResponseCode(response.code());
                    responseAllWatchList.setStatus(false);
                    responsePlayer.postValue(responseAllWatchList);
                }


            }

            @Override
            public void onFailure(@NonNull Call<ResponseAllWatchList> call, @NonNull Throwable t) {
                ResponseAllWatchList responseAllWatchList = new ResponseAllWatchList();

                responseAllWatchList.setStatus(false);
                responsePlayer.postValue(responseAllWatchList);
            }
        });

        return responsePlayer;
    }


    public LiveData<ResponseWatchHistory> hitApiWatchHistory(String token, int page, int length) {
        MutableLiveData<ResponseWatchHistory> responsePlayer = new MutableLiveData<>();

        APIDetails endpoint = RequestConfig.getClientInterceptor(token).create(APIDetails.class);
        Call<ResponseWatchHistory> call = endpoint.getWatchHistory(page, length);
        call.enqueue(new Callback<ResponseWatchHistory>() {
            @Override
            public void onResponse(@NonNull Call<ResponseWatchHistory> call, @NonNull Response<ResponseWatchHistory> response) {
                ResponseWatchHistory responseAllWatchList = new ResponseWatchHistory();

                if (response.code() == 200) {
                    responseAllWatchList.setStatus(true);
                    responseAllWatchList.setData(response.body().getData());
                    responsePlayer.postValue(responseAllWatchList);
                    Logger.e("hitApiWatchHistory", "responseAllWatchList" + responseAllWatchList);

                } else {
                    responseAllWatchList.setResponseCode(response.code());
                    responseAllWatchList.setStatus(false);
                    Logger.e("hitApiWatchHistory", "responseAllWatchList" + responseAllWatchList);
                    responsePlayer.postValue(responseAllWatchList);
                }

            }

            @Override
            public void onFailure(@NonNull Call<ResponseWatchHistory> call, @NonNull Throwable t) {
                ResponseWatchHistory responseAllWatchList = new ResponseWatchHistory();

                responseAllWatchList.setStatus(false);
                responsePlayer.postValue(responseAllWatchList);
            }
        });

        return responsePlayer;
    }

    public LiveData<ResponseEmpty> hitApiRemoveFromWatchList(String token, String data) {
        MutableLiveData<ResponseEmpty> responsePlayer = new MutableLiveData<>();

        APIDetails endpoint = RequestConfig.getClientInterceptor(token).create(APIDetails.class);
        Call<ResponseEmpty> call = endpoint.getRemoveFromWatchList(data);
        call.enqueue(new Callback<ResponseEmpty>() {
            @Override
            public void onResponse(@NonNull Call<ResponseEmpty> call, @NonNull Response<ResponseEmpty> response) {
                //  Logger.e("", "" + response.body());
                ResponseEmpty responseEmpty = new ResponseEmpty();
                if (response.code() == 200) {
                    responseEmpty.setResponseCode(response.code());
                    responseEmpty.setStatus(true);
                    responsePlayer.postValue(responseEmpty);
                } else {

                    responseEmpty.setResponseCode(response.code());
                    responseEmpty.setStatus(false);
                    responsePlayer.postValue(responseEmpty);
                }

            }

            @Override
            public void onFailure(@NonNull Call<ResponseEmpty> call, @NonNull Throwable t) {
                ResponseEmpty responseEmpty = new ResponseEmpty();
                responseEmpty.setStatus(false);
                responsePlayer.postValue(responseEmpty);
            }
        });

        return responsePlayer;
    }


    public LiveData<ResponseAssetHistory> getAssetHistory(String token, int page, int size) {
        MutableLiveData<ResponseAssetHistory> assetContinue = new MutableLiveData<>();

        ApiInterface endpoint = RequestConfig.getClientInterceptor(token).create(ApiInterface.class);

        final JsonObject requestParam = new JsonObject();
        requestParam.addProperty(AppConstants.API_PARAM_PAGE, page);
        requestParam.addProperty(AppConstants.API_PARAM_SIZE, size);
        requestParam.add(AppConstants.API_PARAM_SERIES_ID, JsonNull.INSTANCE);
        requestParam.add(AppConstants.API_PARAM_SEASON_ID, JsonNull.INSTANCE);

        Logger.e("", "page " + page);

        Call<ResponseAssetHistory> call = endpoint.getAssetHistory(requestParam);

        call.enqueue(new Callback<ResponseAssetHistory>() {
            @Override
            public void onResponse(Call<ResponseAssetHistory> call, Response<ResponseAssetHistory> response) {
                if (response.code() == 200) {
                    response.body().setStatus(true);
                    assetContinue.postValue(response.body());
                }
            }

            @Override
            public void onFailure(Call<ResponseAssetHistory> call, Throwable t) {
                assetContinue.postValue(new ResponseAssetHistory());
            }
        });
        return assetContinue;
    }


    public LiveData<ResponseUserAssetList> getAssetList(String token, ResponseAssetHistory list, boolean isFetchData) {
        ApiInterface endpoint = RequestConfig.getClientInterceptor(token).create(ApiInterface.class);
        MutableLiveData<ResponseUserAssetList> mModel = new MutableLiveData<>();
        JsonObject jsonObj = new JsonObject();
        JsonArray jsonArr = new JsonArray();
        try {
            for (int i = 0; i < list.getData().getItems().size(); i++) {
                JsonObject pnObj = new JsonObject();
                pnObj.addProperty(AppConstants.API_PARAM_ID, list.getData().getItems().get(i).getId());
                pnObj.addProperty(AppConstants.API_PARAM_TYPE, list.getData().getItems().get(i).getType());
                jsonArr.add(pnObj);
            }
            jsonObj.addProperty(AppConstants.API_PARAM_FETCH_DATA, isFetchData);
            jsonObj.add("userAssetListDTOList", jsonArr);
        } catch (Exception e) {

        }

        Call<ResponseUserAssetList> call = endpoint.getAssetList(jsonObj);

        call.enqueue(new Callback<ResponseUserAssetList>() {
            @Override
            public void onResponse(Call<ResponseUserAssetList> call, Response<ResponseUserAssetList> response) {
                if (response.code() == 200) {
                    response.body().setStatus(true);
                    mModel.postValue(response.body());
                } else {
                    ResponseUserAssetList responseFail = new ResponseUserAssetList();
                    responseFail.setStatus(false);
                    mModel.postValue(responseFail);
                }
            }

            @Override
            public void onFailure(Call<ResponseUserAssetList> call, Throwable t) {
                ResponseUserAssetList response = new ResponseUserAssetList();
                response.setStatus(false);
                mModel.postValue(response);
            }
        });
        return mModel;
    }

}
