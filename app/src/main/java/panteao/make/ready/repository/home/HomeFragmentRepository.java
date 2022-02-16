package panteao.make.ready.repository.home;

import android.app.Activity;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.make.baseCollection.baseCategoryModel.BaseCategory;
import com.make.baseCollection.baseCategoryServices.BaseCategoryServices;
import com.make.callBacks.EnveuCallBacks;
import com.make.watchHistory.beans.ResponseWatchHistoryAssetList;
import panteao.make.ready.beanModelV3.videoDetailsV2.EnveuVideoDetailsBean;
import panteao.make.ready.userAssetList.ResponseUserAssetList;
import panteao.make.ready.beanModel.AssetHistoryContinueWatching.ResponseAssetHistory;
import panteao.make.ready.beanModel.emptyResponse.ResponseEmpty;
import panteao.make.ready.beanModel.enveuCommonRailData.RailCommonData;
import panteao.make.ready.beanModel.mobileAds.DataItem;
import panteao.make.ready.beanModel.mobileAds.ResponseMobileAds;
import panteao.make.ready.beanModel.responseGetWatchlist.ResponseGetIsWatchlist;
import panteao.make.ready.beanModel.responseIsLike.ResponseIsLike;
import panteao.make.ready.beanModel.responseModels.landingTabResponses.CommonRailData;
import panteao.make.ready.beanModel.responseModels.landingTabResponses.playlistResponse.PlaylistResponses;
import panteao.make.ready.beanModel.responseModels.landingTabResponses.playlistResponse.PlaylistsItem;
import panteao.make.ready.beanModel.responseModels.landingTabResponses.railData.PlaylistRailData;
import panteao.make.ready.callbacks.apicallback.ApiResponseModel;
import panteao.make.ready.callbacks.commonCallbacks.CommonApiCallBack;
import panteao.make.ready.networking.errormodel.ApiErrorModel;
import panteao.make.ready.networking.apiendpoints.ApiInterface;
import panteao.make.ready.networking.apiendpoints.RequestConfig;
import panteao.make.ready.utils.config.LanguageLayer;
import panteao.make.ready.utils.constants.AppConstants;
import panteao.make.ready.utils.cropImage.helpers.Logger;
import panteao.make.ready.utils.cropImage.helpers.PrintLogging;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonNull;
import com.google.gson.JsonObject;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.ListIterator;
import java.util.Objects;

import io.reactivex.Observable;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Function;
import io.reactivex.schedulers.Schedulers;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class HomeFragmentRepository {
    private static HomeFragmentRepository projectRepository;
    private static ApiInterface endpoint;
    ListIterator<PlaylistRailData> lir;
    private Activity activity;
    private List<PlaylistsItem> playlist;
    private int count;
    private List<PlaylistRailData> commonRailData;
    private List<RailCommonData> mModel;
    private ArrayList<PlaylistRailData> railData;
    private ResponseMobileAds adsResponse;

    public synchronized static HomeFragmentRepository getInstance() {
        if (projectRepository == null) {
            endpoint = RequestConfig.getEnveuClient().create(ApiInterface.class);
            projectRepository = new HomeFragmentRepository();
        }
        return projectRepository;
    }


    public LiveData<List<BaseCategory>> getCategories(String screenId) {
        MutableLiveData<List<BaseCategory>> liveData = new MutableLiveData<>();
        BaseCategoryServices.Companion.getInstance().categoryService(screenId, new EnveuCallBacks() {
            @Override
            public void success(boolean status, List<BaseCategory> categoryList) {
                if (status) {
                    Collections.sort(categoryList, new Comparator<BaseCategory>() {
                        @Override
                        public int compare(BaseCategory o1, BaseCategory o2) {
                            return o1.getDisplayOrder().compareTo(o2.getDisplayOrder());
                        }
                    });
                    liveData.postValue(categoryList);
                }
            }

            @Override
            public void failure(boolean status, int errorCode, String errorMessage) {
                liveData.postValue(new ArrayList<>());
            }
        });
        return liveData;
    }


    public LiveData<List<CommonRailData>> getPlaylist(int id) {
        count = 0;
        MutableLiveData<List<CommonRailData>> playlistResponsesMutableLiveData = new MutableLiveData<>();
        Call<PlaylistResponses> call = endpoint.getPlaylists(id);
        call.enqueue(new Callback<PlaylistResponses>() {
            @Override
            public void onResponse(@NonNull Call<PlaylistResponses> call, @NonNull Response<PlaylistResponses> response) {
                if (response.body() != null) {
                    playlist = response.body().getData().getPlaylists();
                    commonRailData = new ArrayList<>();
                    if (playlist.size() > 0)
                        getRailData(playlist);
                    else
                        playlistResponsesMutableLiveData.postValue(new ArrayList<>());
                } else {
                    playlistResponsesMutableLiveData.postValue(new ArrayList<>());

                }
                //playlistResponsesMutableLiveData.postValue(commonRailDataList);
            }

            @Override
            public void onFailure(@NonNull Call<PlaylistResponses> call, @NonNull Throwable t) {
                PrintLogging.printLog(t.toString());
                playlistResponsesMutableLiveData.postValue(new ArrayList<>());

            }
        });
        return playlistResponsesMutableLiveData;
    }

    private void getRailData
            (List<PlaylistsItem> playlist) {
        if (count != playlist.size()) {
            multiRequestHome(playlist.size(), playlist);
        }
    }

    public LiveData<ResponseMobileAds> getAdData(int screenId) {
        MutableLiveData<ResponseMobileAds> ads = new MutableLiveData<>();
        ApiInterface endpoint = RequestConfig.getClientHeader().create(ApiInterface.class);
        Call<ResponseMobileAds> call = endpoint.getAdsData(screenId, AppConstants.PLATFORM);
        call.enqueue(new Callback<ResponseMobileAds>() {
            @Override
            public void onResponse(@NonNull Call<ResponseMobileAds> call, @NonNull Response<ResponseMobileAds> response) {

                if (response.code() == 200) {
                    adsResponse = new ResponseMobileAds();
                    adsResponse.setResponseCode(response.code());
                    adsResponse.setStatus(true);
                    adsResponse.setData(Objects.requireNonNull(response.body()).getData());
                    ads.postValue(adsResponse);
                } else {
                    adsResponse = new ResponseMobileAds();
                    adsResponse.setResponseCode(response.code());
                    adsResponse.setStatus(false);
                    ArrayList<DataItem> dataItem = new ArrayList<>();
                    adsResponse.setData(dataItem);
                    ads.postValue(adsResponse);
                }
            }

            @Override
            public void onFailure(@NonNull Call<ResponseMobileAds> call, @NonNull Throwable t) {
                adsResponse = new ResponseMobileAds();
                adsResponse.setStatus(false);
                ads.postValue(adsResponse);
            }
        });


        return ads;
    }

    public void multiRequestHome(int size, List<
            PlaylistsItem> playlist) {

        MutableLiveData<List<PlaylistRailData>> responseHome;
        railData = new ArrayList<>();
        lir = railData.listIterator();
        ApiInterface endpoint = RequestConfig.getClientHeader().create(ApiInterface.class);
        List<Observable<?>> requests = new ArrayList<>();
        for (int i = 0; i < size; i++) {
            requests.add(endpoint.getPlaylistsDataHome(playlist.get(i).getId(), 0, 10));
        }
        responseHome = new MutableLiveData<>();
        Observable.zip(requests, (Function<Object[], Object>) objects -> {
                    for (Object object : objects) {
                        PlaylistRailData tempData = (PlaylistRailData) object;
                        railData.add(tempData);
                    }

                    return railData;
                }

        ).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<Object>() {
                    @Override
                    public void onSubscribe(Disposable d) {
                    }

                    @Override
                    public void onNext(Object o) {
//                        Logger.e("", o.toString());
                        // mModel = new ArrayList<>();
                        // mModel.addAll(data);
                        // if (railData.size() == ((ArrayList) o).size())
                        try {
                            responseHome.postValue(railData);
                        } catch (Exception e) {
                            Logger.e("HomeFragRepo", "" + e.toString());

                        }
                    }

                    @Override
                    public void onError(Throwable e) {

                    }

                    @Override
                    public void onComplete() {

                    }
                });
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
                } else {
                    ResponseAssetHistory responseAssetHistory = new ResponseAssetHistory();
                    responseAssetHistory.setStatus(false);
                    if (response.code() == 401) {
                        responseAssetHistory.setResponseCode(401);
                    } else if (response.code() == 500) {
                        String debugMessage = "";
                        try {
                            JSONObject jObjError = new JSONObject(response.errorBody().string());
                            debugMessage = jObjError.getString("debugMessage");
                            Logger.e("", "" + jObjError.getString("debugMessage"));
                        } catch (Exception e) {
                            Logger.e("HomeRepos", "" + e.toString());
                        }
                        responseAssetHistory.setErrorMsg(debugMessage);
                    }
                    assetContinue.postValue(responseAssetHistory);
                }
            }

            @Override
            public void onFailure(Call<ResponseAssetHistory> call, Throwable t) {
                assetContinue.postValue(new ResponseAssetHistory());
            }
        });
        return assetContinue;
    }


    public LiveData<ResponseUserAssetList> getAssetList(String token, ResponseAssetHistory list,
                                                        boolean isFetchData) {
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
                    if (response.code() == 401) {
                        responseFail.setResponseCode(401);
                    } else if (response.code() == 500) {
                        String debugMessage = "";
                        try {
                            JSONObject jObjError = new JSONObject(response.errorBody().string());
                            debugMessage = jObjError.getString("debugMessage");
                            Logger.e("", "" + jObjError.getString("debugMessage"));
                        } catch (Exception e) {
                            Logger.e("HomeRepos", "" + e.toString());
                        }
                        responseFail.setErrorMsg(debugMessage);
                    }

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

    public LiveData<JsonObject> hitApiLogout(boolean session, String token) {
        final MutableLiveData<JsonObject> responseApi;
        {
            responseApi = new MutableLiveData<>();
            ApiInterface endpoint = RequestConfig.getClientInterceptor(token).create(ApiInterface.class);

            Call<JsonObject> call = endpoint.getLogout(session);
            call.enqueue(new Callback<JsonObject>() {
                @Override
                public void onResponse(@NonNull Call<JsonObject> call, @NonNull Response<JsonObject> response) {

                    if (response.code() == 404) {
                        Objects.requireNonNull(response.body()).addProperty(AppConstants.API_RESPONSE_CODE, response.code());
                        responseApi.postValue(response.body());
                    } else if (response.code() == 200) {
                        Objects.requireNonNull(response.body()).addProperty(AppConstants.API_RESPONSE_CODE, response.code());
                        responseApi.postValue(response.body());
                    } else if (response.code() == 401) {
                        JsonObject jsonObject = new JsonObject();
                        jsonObject.addProperty(AppConstants.API_RESPONSE_CODE, response.code());
                        responseApi.postValue(jsonObject);
                    } else if (response.code() == 500) {
                        JsonObject jsonObject = new JsonObject();
                        jsonObject.addProperty(AppConstants.API_RESPONSE_CODE, response.code());
                        responseApi.postValue(jsonObject);
                    }
                }

                @Override
                public void onFailure(@NonNull Call<JsonObject> call, @NonNull Throwable t) {
                    Logger.e("", "Error" + "hitApiLogout");

                }
            });
        }

        return responseApi;
    }

    String languageCode;
    public void getContinueWatchingVideos(String manualImageAssetId, CommonApiCallBack commonApiCallBack) {
        languageCode= LanguageLayer.getCurrentLanguageCode();
        endpoint.getVideoDetails(manualImageAssetId,languageCode).enqueue(new Callback<EnveuVideoDetailsBean>() {
            @Override
            public void onResponse(Call<EnveuVideoDetailsBean> call, Response<EnveuVideoDetailsBean> response) {
               /* if (response.isSuccessful()) {
                    ArrayList<EnveuVideoDetails> enveuVideoDetailsArrayList = new ArrayList<>();
                    ArrayList<EnveuVideoDetails> enveuVideoDetails = (ArrayList<EnveuVideoDetails>) response.body().getData();

                    for (ContinueWatchingBookmark continueWatchingBookmark :
                            continueWatchingBookmarkList) {
                        for (EnveuVideoDetails enveuVideoDetail :
                                enveuVideoDetails) {
                            if (continueWatchingBookmark.getAssetId() == enveuVideoDetail.getId()) {
                                if (continueWatchingBookmark.getPosition() != null)
                                    enveuVideoDetail.setmVideoPosition(continueWatchingBookmark.getPosition());
                                enveuVideoDetailsArrayList.add(enveuVideoDetail);
                            }
                        }
                    }
                    commonApiCallBack.onSuccess(enveuVideoDetailsArrayList);
                } else {
                    commonApiCallBack.onFailure(new Throwable("Details Not Found"));

                }*/
            }

            @Override
            public void onFailure(Call<EnveuVideoDetailsBean> call, Throwable t) {
                commonApiCallBack.onFailure();

            }
        });
    }

    public void getWatchHistoryVideos(String manualImageAssetId, CommonApiCallBack commonApiCallBack) {
        languageCode= LanguageLayer.getCurrentLanguageCode();
        endpoint.getVideoDetails(manualImageAssetId,languageCode).enqueue(new Callback<EnveuVideoDetailsBean>() {
            @Override
            public void onResponse(Call<EnveuVideoDetailsBean> call, Response<EnveuVideoDetailsBean> response) {
                if (response.body() != null && response.isSuccessful()) {
                    Logger.e("WATCH RESPONSE", new Gson().toJson(response.isSuccessful()));
                   /* ArrayList<EnveuVideoDetails> enveuVideoDetailsArrayList = new ArrayList<>();
                    ArrayList<EnveuVideoDetails> enveuVideoDetails = (ArrayList<EnveuVideoDetails>) response.body().getData();
                    for (com.enveu.watchHistory.beans.ItemsItem historyItem : continueWatchingBookmarkList
                    ) {
                        for (EnveuVideoDetails enveuVideoDetail :
                                enveuVideoDetails) {
                            if (historyItem.getAssetId() == enveuVideoDetail.getId()) {
                                enveuVideoDetailsArrayList.add(enveuVideoDetail);
                            }
                        }
                    }
                    commonApiCallBack.onSuccess(enveuVideoDetailsArrayList);*/
                } else {
                    commonApiCallBack.onFailure();

                }
            }

            @Override
            public void onFailure(Call<EnveuVideoDetailsBean> call, Throwable t) {
                commonApiCallBack.onFailure();

            }
        });
    }



    public LiveData<ResponseGetIsWatchlist> hitApiIsToWatchList(String token, int seriesId) {
        MutableLiveData<ResponseGetIsWatchlist> responseData = new MutableLiveData<>();
        ApiInterface endpoint = RequestConfig.getUserInteration(token).create(ApiInterface.class);
        endpoint.getIsWatchList(seriesId).enqueue(new Callback<ResponseGetIsWatchlist>() {
            @Override
            public void onResponse(Call<ResponseGetIsWatchlist> call, Response<ResponseGetIsWatchlist> response) {
                try {
                    if (response.code() == 200) {
                        response.body().setStatus(true);
                        responseData.postValue(response.body());
                    } else {
                        ResponseGetIsWatchlist empty = new ResponseGetIsWatchlist();
                        empty.setStatus(false);
                        empty.setResponseCode(response.code());
                        String debugMessage = "";
                        try {
                            JSONObject jObjError = new JSONObject(response.errorBody().string());
                            debugMessage = jObjError.getString("debugMessage");
                            Logger.e("", "" + jObjError.getString("debugMessage"));
                        } catch (Exception e) {
                            Logger.e("RegistrationLoginRepo", "" + e.toString());
                        }
                        empty.setDebugMessage(debugMessage);

                        responseData.postValue(empty);
                    }

                } catch (Exception e) {

                }

            }

            @Override
            public void onFailure(Call<ResponseGetIsWatchlist> call, Throwable t) {
                try {
                    ResponseGetIsWatchlist empty = new ResponseGetIsWatchlist();
                    empty.setStatus(false);
                    empty.setResponseCode(AppConstants.RESPONSE_CODE_ERROR);
                    empty.setDebugMessage(t.getMessage());
                    responseData.postValue(empty);
                } catch (Exception e) {

                }
            }
        });


        return responseData;
    }


    public LiveData<ResponseEmpty> hitApiAddToWatchList(String token, int seriesId) {
        MutableLiveData<ResponseEmpty> responseData = new MutableLiveData<>();
        ApiInterface endpoint = RequestConfig.getUserInteration(token).create(ApiInterface.class);
        endpoint.addToWatchList(seriesId).enqueue(new Callback<ResponseEmpty>() {
            @Override
            public void onResponse(Call<ResponseEmpty> call, Response<ResponseEmpty> response) {
                try {
                    if (response.code() == 200) {
                        response.body().setStatus(true);
                        responseData.postValue(response.body());
                    } else {
                        ResponseEmpty empty = new ResponseEmpty();
                        empty.setStatus(false);
                        empty.setResponseCode(response.code());
                        String debugMessage = "";
                        try {
                            JSONObject jObjError = new JSONObject(response.errorBody().string());
                            debugMessage = jObjError.getString("debugMessage");
                            Logger.e("", "" + jObjError.getString("debugMessage"));
                        } catch (Exception e) {
                            Logger.e("RegistrationLoginRepo", "" + e.toString());
                        }
                        empty.setDebugMessage(debugMessage);

                        responseData.postValue(empty);
                    }

                } catch (Exception e) {

                }


            }

            @Override
            public void onFailure(Call<ResponseEmpty> call, Throwable t) {
                try {
                    ResponseEmpty empty = new ResponseEmpty();
                    empty.setStatus(false);
                    empty.setResponseCode(AppConstants.RESPONSE_CODE_ERROR);
                    empty.setDebugMessage(t.getMessage());
                    responseData.postValue(empty);
                } catch (Exception e) {

                }
            }
        });


        return responseData;
    }

    public LiveData<ResponseEmpty> hitApiAddToWatchHistory(String token, int seriesId) {
        MutableLiveData<ResponseEmpty> responseData = new MutableLiveData<>();
        endpoint = RequestConfig.getClientInterceptor(token).create(ApiInterface.class);
        endpoint.addToWatchHistory(seriesId).enqueue(new Callback<ResponseEmpty>() {
            @Override
            public void onResponse(Call<ResponseEmpty> call, Response<ResponseEmpty> response) {
                if (response.code() == 200) {
                    response.body().setStatus(true);
                    responseData.postValue(response.body());
                } else {
                    ResponseEmpty empty = new ResponseEmpty();
                    empty.setStatus(false);
                    empty.setResponseCode(response.code());
                    responseData.postValue(empty);
                }


            }

            @Override
            public void onFailure(Call<ResponseEmpty> call, Throwable t) {
                ResponseEmpty empty = new ResponseEmpty();
                empty.setStatus(false);
                empty.setResponseCode(AppConstants.RESPONSE_CODE_ERROR);
                responseData.postValue(empty);
            }
        });


        return responseData;
    }


    public LiveData<ResponseUserAssetList> getAssetList(String token, ResponseWatchHistoryAssetList list, boolean isFetchData) {
        ApiInterface endpoint = RequestConfig.getClientInterceptor(token).create(ApiInterface.class);
        MutableLiveData<ResponseUserAssetList> mModel = new MutableLiveData<>();
        JsonObject jsonObj = new JsonObject();
        JsonArray jsonArr = new JsonArray();
        try {
            for (int i = 0; i < list.getData().getItems().size(); i++) {
                JsonObject pnObj = new JsonObject();
                pnObj.addProperty(AppConstants.API_PARAM_ID, list.getData().getItems().get(i).getId());
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


    public LiveData<ResponseIsLike> hitApiIsLike(String token, int seriesId) {
        MutableLiveData<ResponseIsLike> responseData = new MutableLiveData<>();


        ApiInterface endpoint = RequestConfig.getUserInteration(token).create(ApiInterface.class);
        endpoint.getIsLikeVod(seriesId).enqueue(new Callback<ResponseIsLike>() {
            @Override
            public void onResponse(Call<ResponseIsLike> call, Response<ResponseIsLike> response) {
                try {
                    if (response.code() == 200) {
                        response.body().setStatus(true);
                        responseData.postValue(response.body());
                    } else {
                        ResponseIsLike empty = new ResponseIsLike();
                        empty.setStatus(false);
                        empty.setResponseCode(response.code());
                        String debugMessage = "";
                        try {
                            JSONObject jObjError = new JSONObject(response.errorBody().string());
                            debugMessage = jObjError.getString("debugMessage");
                            Logger.e("", "" + jObjError.getString("debugMessage"));
                        } catch (Exception e) {
                            Logger.e("RegistrationLoginRepo", "" + e.toString());
                        }
                        empty.setDebugMessage(debugMessage);

                        responseData.postValue(empty);
                    }

                } catch (Exception e) {

                }


            }

            @Override
            public void onFailure(Call<ResponseIsLike> call, Throwable t) {
                try {
                    ResponseIsLike empty = new ResponseIsLike();
                    empty.setStatus(false);
                    empty.setResponseCode(AppConstants.RESPONSE_CODE_ERROR);
                    empty.setDebugMessage(t.getMessage());
                    responseData.postValue(empty);
                } catch (Exception e) {

                }
            }
        });


        return responseData;
    }

    public LiveData<ResponseEmpty> hitApiDeleteLike(String token, int seriesId) {
        MutableLiveData<ResponseEmpty> responseData = new MutableLiveData<>();
        ApiInterface endpoint = RequestConfig.getUserInteration(token).create(ApiInterface.class);
        endpoint.deleteLike(seriesId).enqueue(new Callback<ResponseEmpty>() {
            @Override
            public void onResponse(Call<ResponseEmpty> call, Response<ResponseEmpty> response) {
                try {
                    if (response.code() == 200) {
                        response.body().setStatus(true);
                        responseData.postValue(response.body());
                    } else {
                        ResponseEmpty empty = new ResponseEmpty();
                        empty.setStatus(false);
                        empty.setResponseCode(response.code());
                        String debugMessage = "";
                        try {
                            JSONObject jObjError = new JSONObject(response.errorBody().string());
                            debugMessage = jObjError.getString("debugMessage");
                            Logger.e("", "" + jObjError.getString("debugMessage"));
                        } catch (Exception e) {
                            Logger.e("RegistrationLoginRepo", "" + e.toString());
                        }
                        empty.setDebugMessage(debugMessage);

                        responseData.postValue(empty);
                    }

                } catch (Exception e) {

                }


            }

            @Override
            public void onFailure(Call<ResponseEmpty> call, Throwable t) {
                try {
                    ResponseEmpty empty = new ResponseEmpty();
                    empty.setStatus(false);
                    empty.setResponseCode(AppConstants.RESPONSE_CODE_ERROR);
                    empty.setDebugMessage(t.getMessage());
                    responseData.postValue(empty);
                } catch (Exception e) {

                }
            }
        });


        return responseData;
    }

    public LiveData<ResponseEmpty> hitRemoveWatchlist(String token, int seriesId) {
        MutableLiveData<ResponseEmpty> responseData = new MutableLiveData<>();
        ApiInterface endpoint = RequestConfig.getUserInteration(token).create(ApiInterface.class);
        endpoint.removeWatchlist(seriesId).enqueue(new Callback<ResponseEmpty>() {
            @Override
            public void onResponse(Call<ResponseEmpty> call, Response<ResponseEmpty> response) {
                try {
                    if (response.code() == 200) {
                        response.body().setStatus(true);
                        responseData.postValue(response.body());
                    } else {
                        ResponseEmpty empty = new ResponseEmpty();
                        empty.setStatus(false);
                        empty.setResponseCode(response.code());
                        String debugMessage = "";
                        try {
                            JSONObject jObjError = new JSONObject(response.errorBody().string());
                            debugMessage = jObjError.getString("debugMessage");
                            Logger.e("", "" + jObjError.getString("debugMessage"));
                        } catch (Exception e) {
                            Logger.e("RegistrationLoginRepo", "" + e.toString());
                        }
                        empty.setDebugMessage(debugMessage);

                        responseData.postValue(empty);
                    }

                } catch (Exception e) {

                }


            }

            @Override
            public void onFailure(Call<ResponseEmpty> call, Throwable t) {
                try {
                    ResponseEmpty empty = new ResponseEmpty();
                    empty.setStatus(false);
                    empty.setResponseCode(AppConstants.RESPONSE_CODE_ERROR);
                    empty.setDebugMessage(t.getMessage());
                    responseData.postValue(empty);
                } catch (Exception e) {

                }
            }
        });


        return responseData;
    }


    public LiveData<ResponseEmpty> hitApiAddLike(String token, int seriesId) {
        MutableLiveData<ResponseEmpty> responseData = new MutableLiveData<>();
        ApiInterface endpoint = RequestConfig.getUserInteration(token).create(ApiInterface.class);
        endpoint.addToLikeVod(seriesId).enqueue(new Callback<ResponseEmpty>() {
            @Override
            public void onResponse(Call<ResponseEmpty> call, Response<ResponseEmpty> response) {
                try {
                    if (response.code() == 200) {
                        response.body().setStatus(true);
                        responseData.postValue(response.body());
                    } else {
                        ResponseEmpty empty = new ResponseEmpty();
                        empty.setStatus(false);
                        empty.setResponseCode(response.code());
                        String debugMessage = "";
                        try {
                            JSONObject jObjError = new JSONObject(response.errorBody().string());
                            debugMessage = jObjError.getString("debugMessage");
                            Logger.e("", "" + jObjError.getString("debugMessage"));
                        } catch (Exception e) {
                            Logger.e("RegistrationLoginRepo", "" + e.toString());
                        }
                        empty.setDebugMessage(debugMessage);

                        responseData.postValue(empty);
                    }

                } catch (Exception e) {

                }
            }

            @Override
            public void onFailure(Call<ResponseEmpty> call, Throwable t) {
                try {
                    ResponseEmpty empty = new ResponseEmpty();
                    empty.setStatus(false);
                    empty.setResponseCode(AppConstants.RESPONSE_CODE_ERROR);
                    empty.setDebugMessage(t.getMessage());
                    responseData.postValue(empty);
                } catch (Exception e) {

                }
            }
        });


        return responseData;
    }


    public void getWatchListVideos(String videoIds, CommonApiCallBack commonApiCallBack) {
        languageCode= LanguageLayer.getCurrentLanguageCode();
        endpoint.getVideoDetails(videoIds,languageCode).enqueue(new Callback<EnveuVideoDetailsBean>() {
            @Override
            public void onResponse(Call<EnveuVideoDetailsBean> call, Response<EnveuVideoDetailsBean> response) {
                if (response.isSuccessful()) {
                   /* ArrayList<EnveuVideoDetails> enveuVideoDetailsArrayList = new ArrayList<>();
                    ArrayList<EnveuVideoDetails> enveuVideoDetails = (ArrayList<EnveuVideoDetails>) response.body().getData();
                    for (ItemsItem item :
                            continueWatchingBookmarkList) {
                        for (EnveuVideoDetails enveuVideoDetail :
                                enveuVideoDetails) {
                            if (item.getAssetId() == enveuVideoDetail.getId()) {
                                enveuVideoDetailsArrayList.add(enveuVideoDetail);
                            }
                        }
                    }
                    commonApiCallBack.onSuccess(enveuVideoDetailsArrayList);*/
                } else {
                    commonApiCallBack.onFailure();

                }
            }

            @Override
            public void onFailure(Call<EnveuVideoDetailsBean> call, Throwable t) {
                commonApiCallBack.onFailure();

            }
        });
    }

    ApiResponseModel apiResponseModel;
    public void getCat(String screenId, ApiResponseModel callBak) {
        apiResponseModel=callBak;
        apiResponseModel.onStart();
        BaseCategoryServices.Companion.getInstance().categoryService(screenId, new EnveuCallBacks() {
            @Override
            public void success(boolean status, List<BaseCategory> categoryList) {
                if (status) {
                    Collections.sort(categoryList, new Comparator<BaseCategory>() {
                        @Override
                        public int compare(BaseCategory o1, BaseCategory o2) {
                            return o1.getDisplayOrder().compareTo(o2.getDisplayOrder());
                        }
                    });
                    apiResponseModel.onSuccess(categoryList);
                }
            }

            @Override
            public void failure(boolean status, int errorCode, String errorMessage) {
                ApiErrorModel errorModel=new ApiErrorModel(errorCode,errorMessage);
                apiResponseModel.onFailure(errorModel);
            }
        });
    }


}