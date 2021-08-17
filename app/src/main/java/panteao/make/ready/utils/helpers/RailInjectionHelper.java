package panteao.make.ready.utils.helpers;

import android.app.Activity;
import android.app.Application;
import android.content.Context;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentActivity;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModelProviders;

import com.make.baseCollection.baseCategoryModel.BaseCategory;
import com.make.bookmarking.bean.continuewatching.ContinueWatchingBookmark;
import com.make.enums.Layouts;
import com.make.watchHistory.beans.ItemsItem;
import panteao.make.ready.Bookmarking.BookmarkingViewModel;
import panteao.make.ready.beanModel.emptyResponse.ResponseEmpty;
import panteao.make.ready.beanModel.enveuCommonRailData.RailCommonData;
import panteao.make.ready.beanModel.responseGetWatchlist.ResponseGetIsWatchlist;
import panteao.make.ready.beanModel.responseIsLike.ResponseIsLike;
import panteao.make.ready.beanModelV3.continueWatching.DataItem;
import panteao.make.ready.callbacks.apicallback.ApiResponseModel;
import panteao.make.ready.callbacks.commonCallbacks.CommonApiCallBack;
import panteao.make.ready.layersV2.ContinueWatchingLayer;
import panteao.make.ready.layersV2.ListPaginationDataLayer;
import panteao.make.ready.layersV2.SearchLayer;
import panteao.make.ready.layersV2.SeasonEpisodesList;
import panteao.make.ready.layersV2.SeriesDataLayer;
import panteao.make.ready.networking.apistatus.APIStatus;
import panteao.make.ready.networking.errormodel.ApiErrorModel;
import panteao.make.ready.networking.responsehandler.ResponseModel;
import panteao.make.ready.networking.servicelayer.APIServiceLayer;
import panteao.make.ready.repository.home.HomeFragmentRepository;
import panteao.make.ready.repository.userManagement.RegistrationLoginRepository;
import panteao.make.ready.utils.constants.AppConstants;
import panteao.make.ready.utils.helpers.ksPreferenceKeys.KsPreferenceKeys;
import panteao.make.ready.ObservableRxList;
import panteao.make.ready.layersV2.VideoDetailLayer;
import com.google.gson.JsonObject;
import com.make.enums.PlaylistType;

import java.util.ArrayList;
import java.util.List;


public class RailInjectionHelper extends AndroidViewModel {
    private MutableLiveData<RailCommonData> mutableRailCommonData = new MutableLiveData<>();
    private ObservableRxList<RailCommonData> observableList = new ObservableRxList<>();
    private int i = 0;
    private List<BaseCategory> baseCategories;
    private KsPreferenceKeys preference;

    public RailInjectionHelper(@NonNull Application application) {
        super(application);
    }

    public ObservableRxList<RailCommonData> getScreenWidgets(Activity activity, String screenId, CommonApiCallBack commonApiCallBack) {
        APIServiceLayer.getInstance().getCategories(screenId).observe((LifecycleOwner) activity, baseCategoriesList -> {
            baseCategories = baseCategoriesList;
            i = 0;
            if (baseCategories.size() > 0) {
                getScreenListing(activity, commonApiCallBack);
            } else {
                commonApiCallBack.onFailure(new Throwable("No Data"));
            }
        });
        return observableList;
    }

    private void getScreenListing(Activity activity, CommonApiCallBack commonApiCallBack) {
        if (i < baseCategories.size()) {
            BaseCategory screenWidget = baseCategories.get(i);
            String type = "";
            if (screenWidget.getType() != null)
                type = screenWidget.getType();
            if (type.equalsIgnoreCase(AppConstants.WIDGET_TYPE_CONTENT)) {
                getRailDetails(activity, screenWidget, commonApiCallBack);
            } else if (type.equalsIgnoreCase(AppConstants.WIDGET_TYPE_AD)) {
                getAdsDetails(activity, screenWidget, commonApiCallBack);
            }
        } else {
            commonApiCallBack.onFinish();
        }
    }

    private void getAdsDetails(Activity activity, BaseCategory screenWidget, CommonApiCallBack commonApiCallBack) {
        if (!KsPreferenceKeys.getInstance().getEntitlementStatus()){
            RailCommonData railCommonData = new RailCommonData(screenWidget);
            railCommonData.setIsAd(true);
            commonApiCallBack.onSuccess(railCommonData);
            i++;
            getScreenListing(activity, commonApiCallBack);
        }else {
            i++;
            getScreenListing(activity, commonApiCallBack);
        }

    }

    private void getRailDetails(Activity activity, BaseCategory screenWidget, CommonApiCallBack commonApiCallBack) {
        if (screenWidget.getLayout() != null && screenWidget.getLayout().equalsIgnoreCase(Layouts.HRO.name())) {
            getHeroDetails(activity, screenWidget, commonApiCallBack);
        } else {
            if (screenWidget.getContentPlayListType() != null && (screenWidget.getContentPlayListType().equalsIgnoreCase(PlaylistType.BVC.name()) || screenWidget.getContentPlayListType().equalsIgnoreCase(PlaylistType.EN_OVP.name()))) {
                getPlaylistDetailsById(activity, screenWidget, commonApiCallBack);
            } else if (screenWidget.getContentPlayListType() != null && screenWidget.getContentPlayListType().equalsIgnoreCase(PlaylistType.K_PDF.name())) {
                //TODO: Get Playlist data from Predefined Kaltura
            } else if (screenWidget.getContentPlayListType() != null && screenWidget.getContentPlayListType().equalsIgnoreCase(PlaylistType.KTM.name())) {
                //TODO: Get Playlist data from Kaltura
            }
        }
    }

    private void getHeroDetails(Activity activity, BaseCategory screenWidget, CommonApiCallBack commonApiCallBack) {
        RailCommonData railCommonData = new RailCommonData();
        railCommonData.getHeroRailCommonData(screenWidget, new CommonApiCallBack() {
            @Override
            public void onSuccess(Object item) {
                commonApiCallBack.onSuccess(item);
                i++;
                getScreenListing(activity, commonApiCallBack);
            }

            @Override
            public void onFailure(Throwable throwable) {
                i++;
                getScreenListing(activity, commonApiCallBack);
            }

            @Override
            public void onFinish() {

            }
        });

    }


    private void getPlaylistDetailsById(Activity activity, BaseCategory screenWidget, CommonApiCallBack commonApiCallBack) {
        int contentSize = 0;
        if (screenWidget.getContentSize() != null) {
            contentSize = screenWidget.getContentSize();
        }
        if (screenWidget.getName() != null && screenWidget.getReferenceName() != null && (screenWidget.getReferenceName().equalsIgnoreCase("special_playlist") || screenWidget.getReferenceName().equalsIgnoreCase(AppConstants.ContentType.CONTINUE_WATCHING.name()))) {
            injectContinueWatchingRail(activity, contentSize, screenWidget, commonApiCallBack);
        } else if (screenWidget.getName() != null && screenWidget.getReferenceName() != null && screenWidget.getReferenceName().equalsIgnoreCase(AppConstants.ContentType.MY_WATCHLIST.name())) {
            injectWatchlistRail(activity, contentSize, screenWidget, commonApiCallBack);
        } else {
            APIServiceLayer.getInstance().getPlayListById(screenWidget.getContentID(), 0, contentSize).observe((LifecycleOwner) activity, enveuCommonResponse -> {
                if (enveuCommonResponse != null && enveuCommonResponse.getData() != null) {
                    RailCommonData railCommonData = new RailCommonData(enveuCommonResponse.getData(), screenWidget, false);
                    commonApiCallBack.onSuccess(railCommonData);
                    i++;
                    getScreenListing(activity, commonApiCallBack);
                } else {
                    i++;
                    getScreenListing(activity, commonApiCallBack);
                }
            });
        }
    }

    private void injectWatchlistRail(Activity activity, int contentSize, BaseCategory screenWidget, CommonApiCallBack commonApiCallBack) {
        if (preference == null)
            preference = KsPreferenceKeys.getInstance();
        if (preference.getAppPrefLoginStatus()) {
            String token = preference.getAppPrefAccessToken();
            BookmarkingViewModel bookmarkingViewModel = ViewModelProviders.of((FragmentActivity) activity).get(BookmarkingViewModel.class);
            bookmarkingViewModel.getMywatchListData(token, 0, contentSize).observe((LifecycleOwner) activity, getContinueWatchingBean -> {
                String videoIds = "";
                if (getContinueWatchingBean != null && getContinueWatchingBean.getData() != null) {
                    List<ItemsItem> continueWatchingBookmarkList = getContinueWatchingBean.getData().getItems();
                    for (ItemsItem continueWatchingBookmark : continueWatchingBookmarkList
                    ) {
                        videoIds = videoIds.concat(String.valueOf(continueWatchingBookmark.getAssetId())).concat(",");
                    }
                    ContinueWatchingLayer.getInstance().getWatchHistoryVideos(continueWatchingBookmarkList, videoIds, new CommonApiCallBack() {
                        @Override
                        public void onSuccess(Object item) {
                            if (item instanceof List) {
                                ArrayList<DataItem> enveuVideoDetails = (ArrayList<DataItem>) item;
                                RailCommonData railCommonData = new RailCommonData();
                                railCommonData.setContinueWatchingData(screenWidget, enveuVideoDetails, new CommonApiCallBack() {
                                    @Override
                                    public void onSuccess(Object item) {
                                        commonApiCallBack.onSuccess(railCommonData);
                                        i++;
                                        getScreenListing(activity, commonApiCallBack);
                                    }

                                    @Override
                                    public void onFailure(Throwable throwable) {
                                        i++;
                                        getScreenListing(activity, commonApiCallBack);
                                    }

                                    @Override
                                    public void onFinish() {

                                    }
                                });
                            }
                        }

                        @Override
                        public void onFailure(Throwable throwable) {
                            commonApiCallBack.onFailure(new Throwable("ASSET NOT FOUND"));
                            i++;
                            getScreenListing(activity, commonApiCallBack);
                        }

                        @Override
                        public void onFinish() {

                        }
                    });
                } else {
                    i++;
                    getScreenListing(activity, commonApiCallBack);
                }

            });
        } else {
            i++;
            getScreenListing(activity, commonApiCallBack);
        }
    }


    private void injectContinueWatchingRail(Activity activity, int contentSize, BaseCategory screenWidget, CommonApiCallBack commonApiCallBack) {
        if (preference == null)
            preference = KsPreferenceKeys.getInstance();
        if (preference.getAppPrefLoginStatus()) {
            String token = preference.getAppPrefAccessToken();
            BookmarkingViewModel bookmarkingViewModel = ViewModelProviders.of((FragmentActivity) activity).get(BookmarkingViewModel.class);
            bookmarkingViewModel.getContinueWatchingData(token, 0, contentSize).observe((LifecycleOwner) activity, getContinueWatchingBean -> {
                String videoIds = "0,";
                if (getContinueWatchingBean != null && getContinueWatchingBean.getData() != null) {
                    List<ContinueWatchingBookmark> continueWatchingBookmarkLists = getContinueWatchingBean.getData().getContinueWatchingBookmarks();
                    List<ContinueWatchingBookmark> continueWatchingBookmarkList=removeDuplicates(continueWatchingBookmarkLists);
                    for (ContinueWatchingBookmark continueWatchingBookmark : continueWatchingBookmarkList) {
                        videoIds = videoIds.concat(String.valueOf(continueWatchingBookmark.getAssetId())).concat(",");
                    }
                    ContinueWatchingLayer.getInstance().getContinueWatchingVideos(continueWatchingBookmarkList, videoIds, new CommonApiCallBack() {
                        @Override
                        public void onSuccess(Object item) {
                            if (item instanceof List) {
                                ArrayList<DataItem> enveuVideoDetails = (ArrayList<DataItem>) item;
                                RailCommonData railCommonData = new RailCommonData();
                                railCommonData.setContinueWatchingData(screenWidget, enveuVideoDetails, new CommonApiCallBack() {
                                    @Override
                                    public void onSuccess(Object item) {
                                        commonApiCallBack.onSuccess(railCommonData);
                                        i++;
                                        getScreenListing(activity, commonApiCallBack);
                                    }

                                    @Override
                                    public void onFailure(Throwable throwable) {
                                        i++;
                                        getScreenListing(activity, commonApiCallBack);
                                    }

                                    @Override
                                    public void onFinish() {

                                    }
                                });
                            }
                        }

                        @Override
                        public void onFailure(Throwable throwable) {
                            commonApiCallBack.onFailure(new Throwable("ASSET NOT FOUND"));
                            i++;
                            getScreenListing(activity, commonApiCallBack);
                        }

                        @Override
                        public void onFinish() {

                        }
                    });
                } else {
                    i++;
                    getScreenListing(activity, commonApiCallBack);
                }

            });
        } else {
            i++;
            getScreenListing(activity, commonApiCallBack);
        }
    }

    private List<ContinueWatchingBookmark> removeDuplicates(List<ContinueWatchingBookmark> continueWatchingBookmarkList) {
        List<ContinueWatchingBookmark> noRepeat = new ArrayList<ContinueWatchingBookmark>();
        try {
            for (ContinueWatchingBookmark event : continueWatchingBookmarkList) {
                boolean isFound = false;
                // check if the event name exists in noRepeat
                for (ContinueWatchingBookmark e : noRepeat) {
                    if (e.getAssetId().equals(event.getAssetId()) || (e.equals(event))) {
                        isFound = true;
                        break;
                    }
                }
                if (!isFound) noRepeat.add(event);
            }
        }catch (Exception ignored){

        }


        return noRepeat;
    }


    public MutableLiveData<RailCommonData> getPlayListDetailsWithPagination(Context
                                                                                    context, String playlistID,
                                                                            int pageNumber, int pageSize, BaseCategory screenWidget) {
        return APIServiceLayer.getInstance().getSearchPopularPlayList(playlistID, pageNumber, pageSize, screenWidget);
    }

    public LiveData<List<RailCommonData>> getSearch(String keyword, int size, int page) {
        return SearchLayer.getInstance().getSearchData("MOVIE", keyword, size, page);
    }

    public LiveData<RailCommonData> getSearchSingleCategory(String type, String keyword, int size, int page) {
        return SearchLayer.getInstance().getSingleCategorySearch(type, keyword, size, page);
    }



    public LiveData<ResponseGetIsWatchlist> hitApiIsWatchList(String token, int seriesId) {
        return HomeFragmentRepository.getInstance().hitApiIsToWatchList(token, seriesId);
    }

    public LiveData<ResponseEmpty> hitApiAddWatchList(String token, int seriesId) {
        return HomeFragmentRepository.getInstance().hitApiAddToWatchList(token, seriesId);
    }

    public LiveData<ResponseEmpty> hitApiAddWatchHistory(String token, int assetId) {
        return HomeFragmentRepository.getInstance().hitApiAddToWatchHistory(token, assetId);
    }


    public LiveData<ResponseIsLike> hitApiIsLike(String token, int assetId) {
        return HomeFragmentRepository.getInstance().hitApiIsLike(token, assetId);
    }

    public LiveData<ResponseEmpty> hitApiDeleteLike(String token, int assetId) {
        return HomeFragmentRepository.getInstance().hitApiDeleteLike(token, assetId);
    }

    public LiveData<ResponseEmpty> hitRemoveWatchlist(String token, int assetId) {
        return HomeFragmentRepository.getInstance().hitRemoveWatchlist(token, assetId);
    }

    public LiveData<ResponseEmpty> hitApiAddLike(String token, int assetId) {
        return HomeFragmentRepository.getInstance().hitApiAddLike(token, assetId);
    }


    public LiveData<RailCommonData> getWatchHistoryAssets(List<ItemsItem> watchHistoryList, String videoIds) {
        MutableLiveData<RailCommonData> railCommonDataMutableLiveData = new MutableLiveData<>();

        ContinueWatchingLayer.getInstance().getWatchHistoryVideos(watchHistoryList, videoIds, new CommonApiCallBack() {
            @Override
            public void onSuccess(Object item) {
                if (item instanceof List) {
                    ArrayList<DataItem> enveuVideoDetails = (ArrayList<DataItem>) item;
                    RailCommonData railCommonData = new RailCommonData();
                    railCommonData.setWatchHistoryData(enveuVideoDetails, new CommonApiCallBack() {
                        @Override
                        public void onSuccess(Object item) {
                            railCommonDataMutableLiveData.postValue(railCommonData);
                        }

                        @Override
                        public void onFailure(Throwable throwable) {
                            railCommonDataMutableLiveData.postValue(null);
                        }

                        @Override
                        public void onFinish() {
                        }
                    });
                } else {
                    railCommonDataMutableLiveData.postValue(null);
                }
            }

            @Override
            public void onFailure(Throwable throwable) {
                railCommonDataMutableLiveData.postValue(null);
            }

            @Override
            public void onFinish() {

            }
        });
        return railCommonDataMutableLiveData;
    }

    public LiveData<JsonObject> hitLogout(boolean session, String token) {
        return RegistrationLoginRepository.getInstance().hitApiLogout(session, token);
    }


    public LiveData<ResponseModel> getCatData(Activity activity, String screenId) {
        MutableLiveData<ResponseModel> liveData=new MutableLiveData<>();
        HomeFragmentRepository homeFragmentRepository=new HomeFragmentRepository();

        homeFragmentRepository.getCat(screenId, new ApiResponseModel<BaseCategory>() {
            @Override
            public void onStart() {
                liveData.postValue(new ResponseModel(APIStatus.START.name(),null,null));
            }

            @Override
            public void onSuccess(List<BaseCategory> t) {
                liveData.postValue(new ResponseModel(APIStatus.SUCCESS.name(),t,null));
            }

            @Override
            public void onError(ApiErrorModel apiError) {
                liveData.postValue(new ResponseModel(APIStatus.ERROR.name(),null, apiError));
            }

            @Override
            public void onFailure(ApiErrorModel httpError) {
                liveData.postValue(new ResponseModel(APIStatus.FAILURE.name(),null, httpError));
            }
        });
        return liveData;
    }

    CommonApiCallBack apiCallBack;
    public void getRailCommonData(Activity activity,List baseCategoriesList,CommonApiCallBack commonApiCallBack) {
        apiCallBack=commonApiCallBack;
        baseCategories = baseCategoriesList;
        i = 0;
        if (baseCategories.size() > 0) {
            getScreenListing(activity, commonApiCallBack);
        } else {
            commonApiCallBack.onFailure(new Throwable("No Data"));
        }
    }

    public MutableLiveData<ResponseModel> getPlayListDetailsWithPaginationV2(Context context, String playlistID,
                                                                            int pageNumber, int pageSize,
                                                                              BaseCategory screenWidget) {
        MutableLiveData liveData=new MutableLiveData();
        ListPaginationDataLayer.getInstance().getPlayListByWithPagination(playlistID, pageNumber, pageSize, screenWidget, new ApiResponseModel<RailCommonData>() {
            @Override
            public void onStart() {
                liveData.postValue(new ResponseModel(APIStatus.START.name(),null,null));
            }

            @Override
            public void onSuccess(RailCommonData response) {
                liveData.postValue(new ResponseModel(APIStatus.SUCCESS.name(),response,null));
            }

            @Override
            public void onError(ApiErrorModel apiError) {
                liveData.postValue(new ResponseModel(APIStatus.ERROR.name(),null, apiError));
            }

            @Override
            public void onFailure(ApiErrorModel httpError) {
                liveData.postValue(new ResponseModel(APIStatus.FAILURE.name(),null, httpError));
            }
        });

        return liveData;
    }

    public LiveData<ResponseModel> getSeriesDetailsV2(String asseetID) {
        MutableLiveData liveData=new MutableLiveData();
        SeriesDataLayer.getInstance().getSeriesData(asseetID, new ApiResponseModel<RailCommonData>() {
            @Override
            public void onStart() {
                liveData.postValue(new ResponseModel(APIStatus.START.name(),null,null));
            }

            @Override
            public void onSuccess(RailCommonData response) {
                liveData.postValue(new ResponseModel(APIStatus.SUCCESS.name(),response,null));
            }

            @Override
            public void onError(ApiErrorModel apiError) {
                liveData.postValue(new ResponseModel(APIStatus.ERROR.name(),null, apiError));
            }

            @Override
            public void onFailure(ApiErrorModel httpError) {
                liveData.postValue(new ResponseModel(APIStatus.FAILURE.name(),null, httpError));
            }
        });

        return liveData;
    }


    public LiveData<ResponseModel> getEpisodeNoSeasonV2(int seriesId, int pageNo, int size, int seasonNumber) {
        MutableLiveData liveData=new MutableLiveData();
        if (seasonNumber == -1) {
            SeasonEpisodesList.getInstance().getAllEpisodesV2(seriesId, pageNo, size, new ApiResponseModel<RailCommonData>() {
                @Override
                public void onStart() {
                    liveData.postValue(new ResponseModel(APIStatus.START.name(), null, null));
                }

                @Override
                public void onSuccess(RailCommonData response) {
                    liveData.postValue(new ResponseModel(APIStatus.SUCCESS.name(), response, null));
                }

                @Override
                public void onError(ApiErrorModel apiError) {
                    liveData.postValue(new ResponseModel(APIStatus.ERROR.name(), null, apiError));
                }

                @Override
                public void onFailure(ApiErrorModel httpError) {
                    liveData.postValue(new ResponseModel(APIStatus.FAILURE.name(), null, httpError));
                }
            });


            return liveData;//SeasonEpisodesList.getInstance().getAllEpisodes(seriesId, pageNo, size);

        }else {
            SeasonEpisodesList.getInstance().getSeasonEpisodesV2(seriesId, pageNo, size, seasonNumber, new ApiResponseModel<RailCommonData>() {
                @Override
                public void onStart() {
                    liveData.postValue(new ResponseModel(APIStatus.START.name(), null, null));
                }

                @Override
                public void onSuccess(RailCommonData response) {
                    liveData.postValue(new ResponseModel(APIStatus.SUCCESS.name(), response, null));
                }

                @Override
                public void onError(ApiErrorModel apiError) {
                    liveData.postValue(new ResponseModel(APIStatus.ERROR.name(), null, apiError));
                }

                @Override
                public void onFailure(ApiErrorModel httpError) {
                    liveData.postValue(new ResponseModel(APIStatus.FAILURE.name(), null, httpError));
                }
            });

            return liveData;
        }
    }

    public LiveData<ResponseModel> getInstructorRelatedContent(int seriesId, int pageNo, int size, int seasonNumber) {
        MutableLiveData liveData=new MutableLiveData();
        if (seasonNumber == -1) {

            SeasonEpisodesList.getInstance().getInstructorRelatedContent(seriesId, pageNo, size, new ApiResponseModel<RailCommonData>() {
                @Override
                public void onStart() {
                    liveData.postValue(new ResponseModel(APIStatus.START.name(), null, null));
                }

                @Override
                public void onSuccess(RailCommonData response) {
                    liveData.postValue(new ResponseModel(APIStatus.SUCCESS.name(), response, null));
                }

                @Override
                public void onError(ApiErrorModel apiError) {
                    liveData.postValue(new ResponseModel(APIStatus.ERROR.name(), null, apiError));
                }

                @Override
                public void onFailure(ApiErrorModel httpError) {
                    liveData.postValue(new ResponseModel(APIStatus.FAILURE.name(), null, httpError));
                }
            });


            return liveData;//SeasonEpisodesList.getInstance().getAllEpisodes(seriesId, pageNo, size);

        }else {
            SeasonEpisodesList.getInstance().getSeasonEpisodesV2(seriesId, pageNo, size, seasonNumber, new ApiResponseModel<RailCommonData>() {
                @Override
                public void onStart() {
                    liveData.postValue(new ResponseModel(APIStatus.START.name(), null, null));
                }

                @Override
                public void onSuccess(RailCommonData response) {
                    liveData.postValue(new ResponseModel(APIStatus.SUCCESS.name(), response, null));
                }

                @Override
                public void onError(ApiErrorModel apiError) {
                    liveData.postValue(new ResponseModel(APIStatus.ERROR.name(), null, apiError));
                }

                @Override
                public void onFailure(ApiErrorModel httpError) {
                    liveData.postValue(new ResponseModel(APIStatus.FAILURE.name(), null, httpError));
                }
            });

            return liveData;
        }
    }


    public LiveData<ResponseModel> getAssetDetailsV2(String asseetID) {
        MutableLiveData liveData=new MutableLiveData();
        VideoDetailLayer.getInstance().getVideoDetails(asseetID, new ApiResponseModel<RailCommonData>() {
            @Override
            public void onStart() {
                liveData.postValue(new ResponseModel(APIStatus.START.name(), null, null));
            }

            @Override
            public void onSuccess(RailCommonData response) {
                liveData.postValue(new ResponseModel(APIStatus.SUCCESS.name(), response, null));
            }

            @Override
            public void onError(ApiErrorModel apiError) {
                liveData.postValue(new ResponseModel(APIStatus.ERROR.name(), null, apiError));
            }

            @Override
            public void onFailure(ApiErrorModel httpError) {
                liveData.postValue(new ResponseModel(APIStatus.FAILURE.name(), null, httpError));
            }
        });


        return liveData;

    }

}
