package panteao.make.ready.networking.servicelayer;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.make.baseCollection.baseCategoryModel.BaseCategory;
import com.make.baseCollection.baseCategoryServices.BaseCategoryServices;
import com.make.bookmarking.bean.continuewatching.ContinueWatchingBookmark;
import com.make.callBacks.EnveuCallBacks;
import com.make.enums.ImageType;
import com.make.watchHistory.beans.ItemsItem;
import com.google.gson.Gson;

import panteao.make.ready.beanModel.enveuCommonRailData.RailCommonData;
import panteao.make.ready.beanModelV3.continueWatching.ContinueWatchingModel;
import panteao.make.ready.beanModelV3.continueWatching.DataItem;
import panteao.make.ready.beanModelV3.playListModelV2.EnveuCommonResponse;
import panteao.make.ready.beanModelV3.searchV2.ResponseSearch;
import panteao.make.ready.beanModelV3.videoDetailsV2.EnveuVideoDetails;
import panteao.make.ready.beanModelV3.videoDetailsV2.EnveuVideoDetailsBean;
import panteao.make.ready.callbacks.apicallback.ApiResponseModel;
import panteao.make.ready.callbacks.commonCallbacks.CommonApiCallBack;
import panteao.make.ready.networking.apiendpoints.ApiInterface;
import panteao.make.ready.networking.apiendpoints.RequestConfig;
import panteao.make.ready.networking.errormodel.ApiErrorModel;
import panteao.make.ready.utils.cropImage.helpers.Logger;
import panteao.make.ready.utils.cropImage.helpers.PrintLogging;
import panteao.make.ready.beanModelV3.uiConnectorModelV2.EnveuVideoItemBean;
import panteao.make.ready.utils.MediaTypeConstants;
import panteao.make.ready.utils.commonMethods.AppCommonMethod;
import panteao.make.ready.utils.config.ImageLayer;
import panteao.make.ready.utils.config.LanguageLayer;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import io.reactivex.Observable;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import panteao.make.ready.beanModelV3.continueWatching.ContinueWatchingModel;
import panteao.make.ready.beanModelV3.continueWatching.DataItem;
import panteao.make.ready.beanModelV3.playListModelV2.EnveuCommonResponse;
import panteao.make.ready.beanModelV3.searchV2.ResponseSearch;
import panteao.make.ready.beanModelV3.uiConnectorModelV2.EnveuVideoItemBean;
import panteao.make.ready.beanModelV3.videoDetailsV2.EnveuVideoDetails;
import panteao.make.ready.beanModelV3.videoDetailsV2.EnveuVideoDetailsBean;
import panteao.make.ready.networking.apiendpoints.ApiInterface;
import panteao.make.ready.networking.apiendpoints.RequestConfig;
import panteao.make.ready.utils.MediaTypeConstants;
import panteao.make.ready.utils.cropImage.helpers.Logger;
import panteao.make.ready.utils.cropImage.helpers.PrintLogging;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class APIServiceLayer {

    private static APIServiceLayer projectRepository;
    private static ApiInterface endpoint;
    private ApiResponseModel callBack;
    private List<EnveuVideoItemBean> enveuVideoItemBeans;

    public synchronized static APIServiceLayer getInstance() {
        if (projectRepository == null) {
            endpoint = RequestConfig.getEnveuClient().create(ApiInterface.class);
            projectRepository = new APIServiceLayer();
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

    String languageCode = "";

    public MutableLiveData<EnveuCommonResponse> getPlayListById(String playListId, int pageNumber, int pageSize) {
        MutableLiveData<EnveuCommonResponse> enveuCommonResponseMutableLiveData = new MutableLiveData<>();
        languageCode = LanguageLayer.getCurrentLanguageCode();
        endpoint.getPlaylistDetailsById(playListId, languageCode, pageNumber, pageSize).enqueue(new Callback<EnveuCommonResponse>() {
            @Override
            public void onResponse(Call<EnveuCommonResponse> call, Response<EnveuCommonResponse> response) {
                if (response.isSuccessful()) {
                    if (response.body() != null && response.body().getData() != null) {
                        if (response.body().getResponseCode() == 2000)
                            enveuCommonResponseMutableLiveData.postValue(response.body());
                        else {
                            enveuCommonResponseMutableLiveData.postValue(null);
                        }
                    }
                } else {
                    enveuCommonResponseMutableLiveData.postValue(null);
                }
            }

            @Override
            public void onFailure(Call<EnveuCommonResponse> call, Throwable t) {
                Logger.e("API RESPONSE ERROR", t.getMessage());
                enveuCommonResponseMutableLiveData.postValue(null);
            }
        });
        return enveuCommonResponseMutableLiveData;
    }

    public void getAssetTypeHero(String manualImageAssetId, CommonApiCallBack commonApiCallBack) {
        languageCode = LanguageLayer.getCurrentLanguageCode();
        endpoint.getVideoDetails(manualImageAssetId, languageCode).enqueue(new Callback<EnveuVideoDetailsBean>() {
            @Override
            public void onResponse(Call<EnveuVideoDetailsBean> call, Response<EnveuVideoDetailsBean> response) {
                if (response.isSuccessful()) {
                    commonApiCallBack.onSuccess(response.body().getData());
                } else {
                    commonApiCallBack.onFailure(new Throwable("Details Not Found"));

                }
            }

            @Override
            public void onFailure(Call<EnveuVideoDetailsBean> call, Throwable t) {
                commonApiCallBack.onFailure(new Throwable("Details Not Found"));

            }
        });
    }

    public void getPlayListByWithPagination(String playlistID,
                                            int pageNumber,
                                            int pageSize,
                                            BaseCategory screenWidget, ApiResponseModel listener) {
        this.callBack = listener;
        callBack.onStart();
        languageCode = LanguageLayer.getCurrentLanguageCode();
        endpoint.getPlaylistDetailsById(playlistID, languageCode, pageNumber, pageSize).enqueue(new Callback<EnveuCommonResponse>() {
            @Override
            public void onResponse(Call<EnveuCommonResponse> call, Response<EnveuCommonResponse> response) {
                if (response.body() != null && response.body().getData() != null) {
                    RailCommonData railCommonData = new RailCommonData(response.body().getData(), screenWidget, true);
                    railCommonData.setStatus(true);
                    callBack.onSuccess(railCommonData);
                } else {
                    ApiErrorModel errorModel = new ApiErrorModel(response.code(), response.message());
                    callBack.onError(errorModel);
                }
            }

            @Override
            public void onFailure(Call<EnveuCommonResponse> call, Throwable t) {
                ApiErrorModel errorModel = new ApiErrorModel(500, t.getMessage());
                callBack.onFailure(errorModel);
            }
        });

    }


    public void getSeasonEpisodesV2(int seriesId, int pageNumber,
                                    int size, int seasonNumber, ApiResponseModel listener) {
        this.callBack = listener;
        callBack.onStart();
        languageCode = LanguageLayer.getCurrentLanguageCode();
        endpoint.getRelatedContent(seriesId, seasonNumber, pageNumber, size, languageCode).enqueue(new Callback<EnveuCommonResponse>() {
            @Override
            public void onResponse(Call<EnveuCommonResponse> call, Response<EnveuCommonResponse> response) {
                parseResponseAsRailCommonData(response);
            }

            @Override
            public void onFailure(Call<EnveuCommonResponse> call, Throwable t) {
                ApiErrorModel errorModel = new ApiErrorModel(500, t.getMessage());
                callBack.onFailure(errorModel);
            }
        });

    }

    public void getInstructorRelatedContent(int seriesId, int pageNumber,
                                            int size, ApiResponseModel listener) {
        this.callBack = listener;
        callBack.onStart();
        languageCode = LanguageLayer.getCurrentLanguageCode();
        endpoint.getInstructorRelatedContent(seriesId, pageNumber, size, languageCode).enqueue(new Callback<EnveuCommonResponse>() {
            @Override
            public void onResponse(Call<EnveuCommonResponse> call, Response<EnveuCommonResponse> response) {
                Gson gson = new Gson();
                String json = gson.toJson(response.body());
                parseResponseAsRailCommonData(response);
            }

            @Override
            public void onFailure(Call<EnveuCommonResponse> call, Throwable t) {
                ApiErrorModel errorModel = new ApiErrorModel(500, t.getMessage());
                callBack.onFailure(errorModel);
            }
        });

    }

    public void getAllEpisodesV2(int seriesId, int pageNumber,
                                 int size, ApiResponseModel listener) {
        this.callBack = listener;
        callBack.onStart();
        languageCode = LanguageLayer.getCurrentLanguageCode();
        endpoint.getRelatedContentWithoutSNo(seriesId, pageNumber, size, languageCode).enqueue(new Callback<EnveuCommonResponse>() {
            @Override
            public void onResponse(Call<EnveuCommonResponse> call, Response<EnveuCommonResponse> response) {
                parseResponseAsRailCommonData(response);
            }

            @Override
            public void onFailure(Call<EnveuCommonResponse> call, Throwable t) {
                ApiErrorModel errorModel = new ApiErrorModel(500, t.getMessage());
                callBack.onFailure(errorModel);
            }
        });

    }


    private void parseResponseAsRailCommonData(Response<EnveuCommonResponse> response) {
        if (response.body() != null && response.body().getData() != null) {
            RailCommonData railCommonData = new RailCommonData(response.body().getData());
            railCommonData.setStatus(true);
            try {
                railCommonData.setTotalCount(response.body().getData().getTotalElements());
                railCommonData.setPageTotal(response.body().getData().getTotalPages());
            } catch (Exception ignore) {

            }
            callBack.onSuccess(railCommonData);
        } else {
            ApiErrorModel errorModel = new ApiErrorModel(response.code(), response.message());
            callBack.onError(errorModel);
        }

    }

    public void getSeriesData(String assetID, ApiResponseModel listener) {
        this.callBack = listener;
        callBack.onStart();
        languageCode = LanguageLayer.getCurrentLanguageCode();
        endpoint.getVideoDetails(assetID, languageCode).enqueue(new Callback<EnveuVideoDetailsBean>() {
            @Override
            public void onResponse(Call<EnveuVideoDetailsBean> call, Response<EnveuVideoDetailsBean> response) {
                if (response.isSuccessful()) {
                    Gson gson = new Gson();
                    String json = gson.toJson(response.body());
                    if (response.body().getData() instanceof EnveuVideoDetails) {
                        RailCommonData railCommonData = new RailCommonData();
                        AppCommonMethod.getAssetDetail(railCommonData, response);
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

    public MutableLiveData<RailCommonData> getSearchPopularPlayList(String playlistID,
                                                                    int pageNumber, int pageSize, BaseCategory screenWidget) {
        MutableLiveData<RailCommonData> railCommonDataMutableLiveData = new MutableLiveData<>();
        languageCode = LanguageLayer.getCurrentLanguageCode();
        endpoint.getPlaylistDetailsById(playlistID, languageCode, pageNumber, pageSize).enqueue(new Callback<EnveuCommonResponse>() {
            @Override
            public void onResponse(Call<EnveuCommonResponse> call, Response<EnveuCommonResponse> response) {
                if (response.body() != null && response.body().getData() != null) {
                    RailCommonData railCommonData = new RailCommonData(response.body().getData(), screenWidget, true);
                    railCommonData.setStatus(true);
                    railCommonDataMutableLiveData.postValue(railCommonData);
                } else {
                    RailCommonData railCommonData = new RailCommonData();
                    railCommonData.setStatus(false);
                    railCommonDataMutableLiveData.postValue(railCommonData);
                }
            }

            @Override
            public void onFailure(Call<EnveuCommonResponse> call, Throwable t) {
                RailCommonData railCommonData = new RailCommonData();
                railCommonData.setStatus(false);
                Logger.e("APIRESPONSEERROR", t.getMessage());
                railCommonDataMutableLiveData.postValue(railCommonData);
            }
        });
        return railCommonDataMutableLiveData;

    }

    public void getContinueWatchingVideos(List<ContinueWatchingBookmark> continueWatchingBookmarkList, String manualImageAssetId, CommonApiCallBack commonApiCallBack) {
        languageCode = LanguageLayer.getCurrentLanguageCode();
        endpoint.getVideos(manualImageAssetId, languageCode).enqueue(new Callback<ContinueWatchingModel>() {
            @Override
            public void onResponse(Call<ContinueWatchingModel> call, Response<ContinueWatchingModel> response) {
                if (response.isSuccessful()) {
                    ArrayList<DataItem> enveuVideoDetailsArrayList = new ArrayList<>();
                    ArrayList<DataItem> enveuVideoDetails = (ArrayList<DataItem>) response.body().getData();

                    for (ContinueWatchingBookmark continueWatchingBookmark : continueWatchingBookmarkList) {
                        for (DataItem enveuVideoDetail : enveuVideoDetails) {

                            if (continueWatchingBookmark.getAssetId().intValue() == enveuVideoDetail.getId()) {
                                if (continueWatchingBookmark.getPosition() != null)
                                    enveuVideoDetail.setPosition(continueWatchingBookmark.getPosition());
                                enveuVideoDetailsArrayList.add(enveuVideoDetail);
                            }
                        }
                    }
                    commonApiCallBack.onSuccess(enveuVideoDetailsArrayList);
                } else {
                    commonApiCallBack.onFailure(new Throwable("Details Not Found"));

                }
            }

            @Override
            public void onFailure(Call<ContinueWatchingModel> call, Throwable t) {
                commonApiCallBack.onFailure(new Throwable("Details Not Found"));

            }
        });
    }

    public void getWatchListVideos(List<ItemsItem> continueWatchingBookmarkList, String manualImageAssetId, CommonApiCallBack commonApiCallBack) {
        languageCode = LanguageLayer.getCurrentLanguageCode();
        endpoint.getVideos(manualImageAssetId, languageCode).enqueue(new Callback<ContinueWatchingModel>() {
            @Override
            public void onResponse(Call<ContinueWatchingModel> call, Response<ContinueWatchingModel> response) {
                if (response.body() != null && response.isSuccessful()) {
                    Logger.e("WATCH RESPONSE", new Gson().toJson(response.isSuccessful()));
                    ArrayList<DataItem> enveuVideoDetailsArrayList = new ArrayList<>();
                    ArrayList<DataItem> enveuVideoDetails = (ArrayList<DataItem>) response.body().getData();

                    for (ItemsItem item :
                            continueWatchingBookmarkList) {
                        for (DataItem enveuVideoDetail :
                                enveuVideoDetails) {
                            if (item.getAssetId() == enveuVideoDetail.getId()) {
                                enveuVideoDetailsArrayList.add(enveuVideoDetail);
                            }
                        }
                    }
                    commonApiCallBack.onSuccess(enveuVideoDetailsArrayList);
                } else {
                    commonApiCallBack.onFailure(new Throwable("Details Not Found"));

                }
            }

            @Override
            public void onFailure(Call<ContinueWatchingModel> call, Throwable t) {
                commonApiCallBack.onFailure(new Throwable("Details Not Found"));

            }
        });

    }

    private List<RailCommonData> mModel;

    public LiveData<List<RailCommonData>> getSearchData(String type, String keyword, int size, int page) {
        languageCode = LanguageLayer.getCurrentLanguageCode();
        ApiInterface endpoint = RequestConfig.getClientSearch().create(ApiInterface.class);

        MutableLiveData<List<RailCommonData>> responsePopular = new MutableLiveData<>();
        {
            try {
                // keyword= URLEncoder.encode(keyword, "UTF-8");
                //String searchValue=

                Observable<ResponseSearch> call = endpoint.getSearch(keyword, MediaTypeConstants.getInstance().getInstructor(), size, page, languageCode)
                        .subscribeOn(Schedulers.io())
                        .observeOn(Schedulers.io());
                Observable<ResponseSearch> call1 = endpoint.getSearch(keyword, MediaTypeConstants.getInstance().getSeries(), size, page, languageCode)
                        .subscribeOn(Schedulers.io())
                        .observeOn(Schedulers.io());

                Observable<ResponseSearch> call2 = endpoint.getSearch(keyword, MediaTypeConstants.getInstance().getEpisode(), size, page, languageCode)
                        .subscribeOn(Schedulers.io())
                        .observeOn(Schedulers.io());

                Observable<ResponseSearch> call3 = endpoint.getSearch(keyword, MediaTypeConstants.getInstance().getTutorial(), size, page, languageCode)
                        .subscribeOn(Schedulers.io())
                        .observeOn(Schedulers.io());

                Observable<ResponseSearch> call4 = endpoint.getSearch(keyword, MediaTypeConstants.getInstance().getChapter(), size, page, languageCode)
                        .subscribeOn(Schedulers.io())
                        .observeOn(Schedulers.io());

                Observable<ResponseSearch> call5 = endpoint.getSearch(keyword, MediaTypeConstants.getInstance().getShow(), size, page, languageCode)
                        .subscribeOn(Schedulers.io())
                        .observeOn(Schedulers.io());


                Observable<List<ResponseSearch>> combined = Observable.zip(call, call1, call2, call3, call4, call5, (list, list1, list2, list3, list4, list5) -> {
                    List<ResponseSearch> mlist = new ArrayList<>();
                    mlist.add(list);
                    mlist.add(list1);
                    mlist.add(list2);
                    mlist.add(list3);
                    mlist.add(list4);
                    mlist.add(list5);
                    return mlist;
                }).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread());
                combined.subscribe(new Observer<List<ResponseSearch>>() {

                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onNext(List<ResponseSearch> data) {
                        mModel = new ArrayList<>();
                        try {
                            Logger.e("valueOfSearch", "" + data.size());
                            for (int i = 0; i < data.size(); i++) {
                                RailCommonData railCommonData = null;
                                if (data != null) {
                                    railCommonData = new RailCommonData();
                                    if (data.get(i).getData() != null && data.get(i).getData().getItems() != null) {
                                        railCommonData.setStatus(true);
                                        List<panteao.make.ready.beanModelV3.searchV2.ItemsItem> searchItems = data.get(i).getData().getItems();
                                        List<EnveuVideoItemBean> enveuVideoItemBeans = new ArrayList<>();
                                        for (panteao.make.ready.beanModelV3.searchV2.ItemsItem videoItem : searchItems) {
                                            Gson gson = new Gson();
                                            String tmp = gson.toJson(videoItem);
                                            EnveuVideoItemBean enveuVideoItemBean = new EnveuVideoItemBean(videoItem);
                                            enveuVideoItemBean.setPosterURL(ImageLayer.getInstance().getPosterImageUrl(videoItem, ImageType.LDS.name()));
                                            enveuVideoItemBean.setThumbnailImage(ImageLayer.getInstance().getThumbNailImageUrl(videoItem, ImageType.LDS.name()));
                                            enveuVideoItemBeans.add(enveuVideoItemBean);
                                        }
                                        railCommonData.setEnveuVideoItemBeans(enveuVideoItemBeans);
                                        railCommonData.setPageTotal(data.get(i).getData().getPageInfo().getTotal());
                                        railCommonData.setStatus(true);
                                    } else {
                                        railCommonData.setStatus(false);
                                    }

                                } else {
                                    railCommonData.setStatus(false);
                                }
                                mModel.add(railCommonData);
                            }

                        } catch (Exception e) {
                            RailCommonData railCommonData = new RailCommonData();
                            railCommonData.setStatus(false);
                            mModel.add(railCommonData);
                        }
                        responsePopular.postValue(mModel);
                    }

                    @Override
                    public void onError(Throwable e) {
                        responsePopular.postValue(new ArrayList<>());
                    }

                    @Override
                    public void onComplete() {

                    }
                });

            } catch (Exception e) {
                mModel = new ArrayList<>();
                RailCommonData railCommonData = new RailCommonData();
                railCommonData.setStatus(false);
                mModel.add(railCommonData);
                responsePopular.postValue(mModel);
            }

        }
        return responsePopular;
    }

    public LiveData<RailCommonData> getSingleCategorySearch(String keyword, String type, int size, int page) {
        MutableLiveData<RailCommonData> responsePopular;
        {
            languageCode = LanguageLayer.getCurrentLanguageCode();
            try {
                // keyword= URLEncoder.encode(keyword, "UTF-8");
            } catch (Exception e) {

            }
            responsePopular = new MutableLiveData<>();
            ApiInterface backendApi = RequestConfig.getClientSearch().create(ApiInterface.class);

            PrintLogging.printLog("", "SearchValues-->>" + keyword + " " + type + " " + size + " " + page);
            Call<ResponseSearch> call = backendApi.getSearchResults(keyword, type, size, page, languageCode);
            call.enqueue(new Callback<ResponseSearch>() {
                @Override
                public void onResponse(@NonNull Call<ResponseSearch> call, @NonNull Response<ResponseSearch> data) {
                    if (data.code() == 200) {
                        RailCommonData railCommonData = null;
                        if (data != null) {
                            railCommonData = new RailCommonData();
                            Logger.e("SearchData", new Gson().toJson(data.body().getData()));
                            if (data.body().getData() != null && data.body().getData().getItems() != null) {
                                railCommonData.setStatus(true);
                                List<panteao.make.ready.beanModelV3.searchV2.ItemsItem> itemsItem = data.body().getData().getItems();
                                enveuVideoItemBeans = new ArrayList<>();
                                for (panteao.make.ready.beanModelV3.searchV2.ItemsItem videoItem : itemsItem) {
                                    EnveuVideoItemBean enveuVideoItemBean = new EnveuVideoItemBean(videoItem);
                                    enveuVideoItemBean.setPosterURL(ImageLayer.getInstance().getPosterImageUrl(videoItem, ImageType.LDS.name()));
                                    if (type.equalsIgnoreCase(MediaTypeConstants.getInstance().getSeries()) && videoItem.getSeasons() != null)
                                        enveuVideoItemBean.setSeasonCount(videoItem.getSeasons().size());

                                    enveuVideoItemBeans.add(enveuVideoItemBean);
                                }

                                railCommonData.setEnveuVideoItemBeans(enveuVideoItemBeans);
                                railCommonData.setPageTotal(data.body().getData().getPageInfo().getTotal());
                                railCommonData.setStatus(true);
                                responsePopular.postValue(railCommonData);
                            } else {
                                railCommonData.setStatus(false);
                                responsePopular.postValue(railCommonData);
                            }

                        } else {
                            responsePopular.postValue(railCommonData);
                        }
                    } else {
                        responsePopular.postValue(new RailCommonData());
                    }
                }

                @Override
                public void onFailure(@NonNull Call<ResponseSearch> call, @NonNull Throwable t) {
                    responsePopular.postValue(new RailCommonData());
                }
            });

        }
        return responsePopular;
    }

}
