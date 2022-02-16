package panteao.make.ready.repository.detail;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import panteao.make.ready.beanModel.AssetHistoryContinueWatching.ResponseAssetHistory;
import panteao.make.ready.beanModel.addComment.ResponseAddComment;
import panteao.make.ready.beanModel.allComments.ResponseAllComments;
import panteao.make.ready.beanModel.deleteComment.ResponseDeleteComment;
import panteao.make.ready.beanModel.emptyResponse.ResponseEmpty;
import panteao.make.ready.beanModel.entitlement.ResponseEntitlement;
import panteao.make.ready.beanModel.isLike.ResponseIsLike;
import panteao.make.ready.beanModel.isWatchList.ResponseContentInWatchlist;
import panteao.make.ready.beanModel.like.ResponseAddLike;
import panteao.make.ready.beanModel.responseModels.detailPlayer.ResponseDetailPlayer;
import panteao.make.ready.beanModel.responseModels.landingTabResponses.CommonRailData;
import panteao.make.ready.beanModel.responseModels.series.SeriesResponse;
import panteao.make.ready.beanModel.responseModels.series.season.SeasonResponse;
import panteao.make.ready.beanModel.watchList.ResponseWatchList;
import panteao.make.ready.networking.detailPlayer.APIDetails;
import panteao.make.ready.networking.apiendpoints.ApiInterface;
import panteao.make.ready.networking.apiendpoints.RequestConfig;
import panteao.make.ready.utils.constants.AppConstants;
import panteao.make.ready.utils.cropImage.helpers.Logger;
import panteao.make.ready.utils.cropImage.helpers.PrintLogging;
import com.google.gson.JsonObject;

import java.util.ArrayList;
import java.util.List;
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

public class DetailRepository {
    private static DetailRepository detailRepository;
    List<SeasonResponse> railData;


    public synchronized static DetailRepository getInstance() {
        if (detailRepository == null) {
            detailRepository = new DetailRepository();
        }
        return detailRepository;
    }


    public LiveData<ResponseDetailPlayer> hitApiDetailPlayer(boolean check, String token, int videoId) {
        MutableLiveData<ResponseDetailPlayer> responsePlayer = new MutableLiveData<>();
        APIDetails endpoint;
        if (check) {
            endpoint = RequestConfig.getClientInterceptor(token).create(APIDetails.class);
        } else {
            endpoint = RequestConfig.getClientHeader().create(APIDetails.class);
        }


        Call<ResponseDetailPlayer> call = endpoint.getPlayerDetails(videoId);
        call.enqueue(new Callback<ResponseDetailPlayer>() {
            @Override
            public void onResponse(@NonNull Call<ResponseDetailPlayer> call, @NonNull Response<ResponseDetailPlayer> response) {
                if (response.body() != null) {
                    ResponseDetailPlayer playerResponse = new ResponseDetailPlayer();
                    playerResponse = response.body();
                    playerResponse.setStatus(true);
                    responsePlayer.postValue(playerResponse);
                } else {
                    ResponseDetailPlayer playerResponse = new ResponseDetailPlayer();
                    playerResponse.setResponseCode(response.code());
                    playerResponse.setStatus(false);
                    responsePlayer.postValue(playerResponse);
                }

            }

            @Override
            public void onFailure(@NonNull Call<ResponseDetailPlayer> call, @NonNull Throwable t) {
                ResponseDetailPlayer playerResponse = new ResponseDetailPlayer();

                playerResponse.setResponseCode(600);
                playerResponse.setStatus(false);
                responsePlayer.postValue(playerResponse);
            }
        });

        return responsePlayer;
    }

    public LiveData<List<CommonRailData>> hitApiYouMayLike(int id, int pageNumber, int pageSize) {
        MutableLiveData<List<CommonRailData>> responsePlayer = new MutableLiveData<>();
        List<CommonRailData> commonRailDataList = new ArrayList<>();
        APIDetails endpoint = RequestConfig.getClientHeader().create(APIDetails.class);
        Call<SeasonResponse> call = endpoint.getYouMayLike(id, pageNumber, pageSize);
        call.enqueue(new Callback<SeasonResponse>() {
            @Override
            public void onResponse(@NonNull Call<SeasonResponse> call, @NonNull Response<SeasonResponse> response) {
                if (response.body() != null) {
                    ResponseDetailPlayer playerResponse = new ResponseDetailPlayer();

                    playerResponse.setStatus(true);
                    CommonRailData temp = new CommonRailData();
                    temp.setSeasonResponse(response.body());
                    temp.setContentType(AppConstants.VOD);
                    temp.setType(3);
                    commonRailDataList.add(temp);
                    responsePlayer.postValue(commonRailDataList);

                } else {
                    ResponseDetailPlayer playerResponse = new ResponseDetailPlayer();

                    playerResponse.setResponseCode(response.code());
                    playerResponse.setStatus(false);
                    responsePlayer.postValue(commonRailDataList);
                }

            }

            @Override
            public void onFailure(@NonNull Call<SeasonResponse> call, @NonNull Throwable t) {

                ResponseDetailPlayer playerResponse = new ResponseDetailPlayer();
                playerResponse.setResponseCode(600);
                playerResponse.setStatus(false);
            }
        });

        return responsePlayer;
    }


    public LiveData<ResponseWatchList> hitApiAddToWatchList(String token, JsonObject data) {
        MutableLiveData<ResponseWatchList> responsePlayer = new MutableLiveData<>();

        APIDetails endpoint = RequestConfig.getClientInterceptor(token).create(APIDetails.class);
        Call<ResponseWatchList> call = endpoint.getAddToWatchList(data);
        call.enqueue(new Callback<ResponseWatchList>() {
            @Override
            public void onResponse(@NonNull Call<ResponseWatchList> call, @NonNull Response<ResponseWatchList> response) {
                Logger.e("", "" + response.body());
                ResponseWatchList responseWatchList = new ResponseWatchList();

                if (response.code() == 200) {
                    responseWatchList.setStatus(true);
                    responseWatchList.setData(Objects.requireNonNull(response.body()).getData());
                    responsePlayer.postValue(responseWatchList);
                } else {

                    responseWatchList.setResponseCode(response.code());
                    responseWatchList.setStatus(false);
                    responsePlayer.postValue(responseWatchList);
                }
            }

            @Override
            public void onFailure(@NonNull Call<ResponseWatchList> call, @NonNull Throwable t) {
                ResponseWatchList responseWatchList = new ResponseWatchList();
                responseWatchList.setStatus(false);
                responsePlayer.postValue(responseWatchList);
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

                    responseEmpty.setResponseCode(response.body().getResponseCode());
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

    public LiveData<ResponseContentInWatchlist> hitApiIsToWatchList(String token, JsonObject data) {
        MutableLiveData<ResponseContentInWatchlist> responsePlayer = new MutableLiveData<>();

        APIDetails endpoint = RequestConfig.getClientInterceptor(token).create(APIDetails.class);
        Call<ResponseContentInWatchlist> call = endpoint.getIsContentWatchList(data);
        call.enqueue(new Callback<ResponseContentInWatchlist>() {
            @Override
            public void onResponse(@NonNull Call<ResponseContentInWatchlist> call, @NonNull Response<ResponseContentInWatchlist> response) {
                //   Logger.e("", "" + response.body());
                ResponseContentInWatchlist responseWatchList = new ResponseContentInWatchlist();

                if (response.code() == 200) {
                    responseWatchList.setStatus(true);
                    responseWatchList.setData(Objects.requireNonNull(response.body()).getData());
                    responsePlayer.postValue(responseWatchList);
                } else {
                    responseWatchList.setResponseCode(response.code());
                    responseWatchList.setStatus(false);
                    responsePlayer.postValue(responseWatchList);
                }


            }

            @Override
            public void onFailure(@NonNull Call<ResponseContentInWatchlist> call, @NonNull Throwable t) {
                ResponseContentInWatchlist responseWatchList = new ResponseContentInWatchlist();
                responseWatchList.setStatus(false);
                responsePlayer.postValue(responseWatchList);
            }
        });

        return responsePlayer;
    }


    public LiveData<ResponseIsLike> hitApiIsLike(String token, JsonObject data) {
        MutableLiveData<ResponseIsLike> responsePlayer = new MutableLiveData<>();

        APIDetails endpoint = RequestConfig.getClientInterceptor(token).create(APIDetails.class);
        Call<ResponseIsLike> call = endpoint.getIsLike(data);
        call.enqueue(new Callback<ResponseIsLike>() {
            @Override
            public void onResponse(@NonNull Call<ResponseIsLike> call, @NonNull Response<ResponseIsLike> response) {
                Logger.e("", "" + response.body());
                ResponseIsLike responseWatchList = new ResponseIsLike();
                if (response.code() == 200) {
                    responseWatchList.setStatus(true);
                    responseWatchList.setResponseCode(response.code());
                    responseWatchList.setData(Objects.requireNonNull(response.body()).getData());
                    responsePlayer.postValue(responseWatchList);
                } else {
                    responseWatchList.setResponseCode(response.code());
                    responseWatchList.setStatus(false);
                    responsePlayer.postValue(responseWatchList);
                }


            }

            @Override
            public void onFailure(@NonNull Call<ResponseIsLike> call, @NonNull Throwable t) {
                ResponseIsLike responseWatchList = new ResponseIsLike();
                responseWatchList.setStatus(false);
                responsePlayer.postValue(responseWatchList);
            }
        });

        return responsePlayer;
    }

    public LiveData<ResponseAddLike> hitApiAddLike(String token, JsonObject data) {
        MutableLiveData<ResponseAddLike> responsePlayer = new MutableLiveData<>();

        APIDetails endpoint = RequestConfig.getClientInterceptor(token).create(APIDetails.class);
        Call<ResponseAddLike> call = endpoint.getAddLike(data);
        call.enqueue(new Callback<ResponseAddLike>() {
            @Override
            public void onResponse(@NonNull Call<ResponseAddLike> call, @NonNull Response<ResponseAddLike> response) {
                Logger.e("", "" + response.body());
                ResponseAddLike responseWatchList = new ResponseAddLike();

                if (response.code() == 200) {
                    responseWatchList.setStatus(true);
                    responsePlayer.postValue(responseWatchList);
                } else {
                    responseWatchList.setResponseCode(response.code());
                    responseWatchList.setStatus(false);
                    responsePlayer.postValue(responseWatchList);
                }
            }

            @Override
            public void onFailure(@NonNull Call<ResponseAddLike> call, @NonNull Throwable t) {
                ResponseAddLike responseWatchList = new ResponseAddLike();
                responseWatchList.setStatus(false);
                responsePlayer.postValue(responseWatchList);
            }
        });

        return responsePlayer;
    }

    public LiveData<ResponseEmpty> hitApiUnLike(String token, JsonObject data) {
        MutableLiveData<ResponseEmpty> responsePlayer = new MutableLiveData<>();

        APIDetails endpoint = RequestConfig.getClientInterceptor(token).create(APIDetails.class);
        Call<ResponseEmpty> call = endpoint.getUnLike(data);
        call.enqueue(new Callback<ResponseEmpty>() {
            @Override
            public void onResponse(@NonNull Call<ResponseEmpty> call, @NonNull Response<ResponseEmpty> response) {
                Logger.e("", "" + response.body());
                ResponseEmpty responseWatchList = new ResponseEmpty();

                if (response.code() == 200) {
                    responseWatchList.setStatus(true);
                    responsePlayer.postValue(responseWatchList);
                } else {
                    responseWatchList.setResponseCode(response.code());
                    responseWatchList.setStatus(false);
                    responsePlayer.postValue(responseWatchList);
                }


            }

            @Override
            public void onFailure(@NonNull Call<ResponseEmpty> call, @NonNull Throwable t) {
                ResponseEmpty responseWatchList = new ResponseEmpty();
                responseWatchList.setStatus(false);
                responsePlayer.postValue(responseWatchList);
            }
        });

        return responsePlayer;
    }


    public LiveData<ResponseEntitlement> hitApiEntitlement() {
        MutableLiveData<ResponseEntitlement> responseOutput = new MutableLiveData<>();

        /*APIDetails endpoint = RequestConfig.getClientInterceptor(token).create(APIDetails.class);
        Call<ResponseEntitlement> call = endpoint.getSku(sku);
        call.enqueue(new Callback<ResponseEntitlement>() {
            @Override
            public void onResponse(@NonNull Call<ResponseEntitlement> call, @NonNull ContinueWatchingModel<ResponseEntitlement> response) {
                if (response.code() == 200) {
                    ResponseEntitlement responseEntitlement = new ResponseEntitlement();

                    responseEntitlement.setResponseCode(response.code());
                    responseEntitlement.setStatus(true);
                    responseEntitlement.setData(Objects.requireNonNull(response.body()).getData());
                    responseOutput.postValue(responseEntitlement);
                } else {
                    ResponseEntitlement responseEntitlement = new ResponseEntitlement();

                    responseEntitlement.setResponseCode(Objects.requireNonNull(response.code()));
                    responseEntitlement.setStatus(false);
                    responseOutput.postValue(responseEntitlement);
                }
            }

            @Override
            public void onFailure(@NonNull Call<ResponseEntitlement> call, @NonNull Throwable t) {
                ResponseEntitlement responseEntitlement = new ResponseEntitlement();
                responseEntitlement.setStatus(false);
                responseOutput.postValue(responseEntitlement);


            }
        });*/
        return responseOutput;
    }


    public LiveData<ResponseAllComments> hitApiAllComment(String size, int page, JsonObject obj) {
        MutableLiveData<ResponseAllComments> responseOutput = new MutableLiveData<>();
        APIDetails endpoint = RequestConfig.getClientHeader().create(APIDetails.class);

        Call<ResponseAllComments> call = endpoint.getAllComments(size, page, obj);
        call.enqueue(new Callback<ResponseAllComments>() {
            @Override
            public void onResponse(@NonNull Call<ResponseAllComments> call, @NonNull Response<ResponseAllComments> response) {
                ResponseAllComments responseEntitlement = new ResponseAllComments();
                if (response.code() == 200) {
                    responseEntitlement.setResponseCode(response.code());
                    responseEntitlement.setStatus(true);
                    responseEntitlement.setData(Objects.requireNonNull(response.body()).getData());
                    responseOutput.postValue(responseEntitlement);
                } else {
                    responseEntitlement.setResponseCode(response.code());
                    responseEntitlement.setStatus(false);
                    responseOutput.postValue(responseEntitlement);
                }
            }

            @Override
            public void onFailure(@NonNull Call<ResponseAllComments> call, @NonNull Throwable t) {
                ResponseAllComments responseEntitlement = new ResponseAllComments();
                responseEntitlement.setStatus(false);
                responseOutput.postValue(responseEntitlement);

            }
        });

        return responseOutput;
    }


    public LiveData<ResponseAddComment> hitApiAddComment(String token, JsonObject data) {
        MutableLiveData<ResponseAddComment> responseMutable = new MutableLiveData<>();

        APIDetails endpoint = RequestConfig.getClientInterceptor(token).create(APIDetails.class);
        Call<ResponseAddComment> call = endpoint.getAddComments(data);
        call.enqueue(new Callback<ResponseAddComment>() {
            @Override
            public void onResponse(@NonNull Call<ResponseAddComment> call, @NonNull Response<ResponseAddComment> response) {
                ResponseAddComment responseAddComment = new ResponseAddComment();
                if (response.code() == 200) {
                    responseAddComment.setStatus(true);
                    responseAddComment.setData(Objects.requireNonNull(response.body()).getData());
                    responseMutable.postValue(responseAddComment);
                } else {

                    responseAddComment.setResponseCode(response.code());
                    responseAddComment.setStatus(false);
                    responseMutable.postValue(responseAddComment);
                }


            }

            @Override
            public void onFailure(@NonNull Call<ResponseAddComment> call, @NonNull Throwable t) {
                ResponseAddComment responseAddComment = new ResponseAddComment();
                responseAddComment.setStatus(false);
                responseMutable.postValue(responseAddComment);
            }
        });
        return responseMutable;
    }


    public LiveData<ResponseDeleteComment> hitApiDeleteComment(String token, String contentId) {
        MutableLiveData<ResponseDeleteComment> responseMutable = new MutableLiveData<>();

        APIDetails endpoint = RequestConfig.getClientInterceptor(token).create(APIDetails.class);
        Call<ResponseDeleteComment> call = endpoint.getDeleteComment(contentId);
        call.enqueue(new Callback<ResponseDeleteComment>() {
            @Override
            public void onResponse(@NonNull Call<ResponseDeleteComment> call, @NonNull Response<ResponseDeleteComment> response) {
                ResponseDeleteComment model = new ResponseDeleteComment();
                if (response.code() == 200) {
                    model.setStatus(true);
                    responseMutable.postValue(model);
                } else {
                    model.setResponseCode(response.code());
                    model.setStatus(false);
                    responseMutable.postValue(model);
                }
            }

            @Override
            public void onFailure(@NonNull Call<ResponseDeleteComment> call, @NonNull Throwable t) {
                ResponseDeleteComment model = new ResponseDeleteComment();
                model.setStatus(false);
                responseMutable.postValue(model);

            }
        });
        return responseMutable;
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

    public LiveData<SeriesResponse> getSeriesDetail(int seriesId) {

        MutableLiveData<SeriesResponse> mutableLiveData = new MutableLiveData<>();
        APIDetails endpoint = RequestConfig.getClientHeader().create(APIDetails.class);
        Call<SeriesResponse> call = endpoint.getSeriesDetails(seriesId);
        call.enqueue(new Callback<SeriesResponse>() {
            @Override
            public void onResponse(@NonNull Call<SeriesResponse> call, @NonNull Response<SeriesResponse> response) {
                if (response.code() == 200) {
                    PrintLogging.printLog(response.body() + "getSeriesDetail");
                    mutableLiveData.postValue(response.body());
                }
            }

            @Override
            public void onFailure(@NonNull Call<SeriesResponse> call, @NonNull Throwable t) {
            }
        });

        return mutableLiveData;
    }


    public LiveData<SeasonResponse> getVOD(int seriedId, int pageNo, int length) {
        MutableLiveData<SeasonResponse> mutableLiveData = new MutableLiveData<>();
        APIDetails endpoint = RequestConfig.getClientHeader().create(APIDetails.class);
        Call<SeasonResponse> call = endpoint.getVOD(seriedId, pageNo, length);
        call.enqueue(new Callback<SeasonResponse>() {
            @Override
            public void onResponse(@NonNull Call<SeasonResponse> call, @NonNull Response<SeasonResponse> response) {
                PrintLogging.printLog("getVOD");

                mutableLiveData.postValue(response.body());
            }

            @Override
            public void onFailure(@NonNull Call<SeasonResponse> call, @NonNull Throwable t) {

            }
        });
        return mutableLiveData;
    }


    public LiveData<SeasonResponse> singleRequestSeries(int id, int page, int size) {
        MutableLiveData<SeasonResponse> responseHome = new MutableLiveData<>();
        railData = new ArrayList<>();
        APIDetails endpoint = RequestConfig.getClientHeader().create(APIDetails.class);
        Call<SeasonResponse> responseCall = endpoint.getSeasonEpisodeSingle(id, page, size);

        responseCall.enqueue(new Callback<SeasonResponse>() {
            @Override
            public void onResponse(Call<SeasonResponse> call, Response<SeasonResponse> response) {
                responseHome.postValue(response.body());

            }

            @Override
            public void onFailure(Call<SeasonResponse> call, Throwable t) {

            }
        });


        return responseHome;
    }


    public LiveData<List<SeasonResponse>> multiRequestSeries(int size, SeriesResponse playlist, int railLength) {
        MutableLiveData<List<SeasonResponse>> responseHome = new MutableLiveData<>();
        railData = new ArrayList<>();
        APIDetails endpoint = RequestConfig.getClientHeader().create(APIDetails.class);

        List<Observable<?>> requests = new ArrayList<>();
        for (int i = 0; i < size; i++) {
            requests.add(endpoint.getSeasonEpisodeMulti(playlist.getData().getSeasons().get(i).getId(), 0, railLength));
        }
        Observable.zip(requests, (Function<Object[], Object>) objects -> {
                    for (Object object : objects) {
                        SeasonResponse tempData = (SeasonResponse) object;
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
                        try {
                            responseHome.postValue(railData);
                        } catch (Exception e) {
                            Logger.e("SeriesRepo", "" + e.toString());
                        }
                    }

                    @Override
                    public void onError(Throwable e) {

                    }

                    @Override
                    public void onComplete() {

                    }
                });
        return responseHome;
    }

    public LiveData<ResponseAssetHistory> getMultiAssetHistory(String token, JsonObject requestParam) {
        MutableLiveData<ResponseAssetHistory> data = new MutableLiveData<>();

        APIDetails endpoint = RequestConfig.getClientInterceptor(token).create(APIDetails.class);
        Call<ResponseAssetHistory> call = endpoint.getAssetHistory(requestParam);


        call.enqueue(new Callback<ResponseAssetHistory>() {
            @Override
            public void onResponse(Call<ResponseAssetHistory> call, Response<ResponseAssetHistory> response) {
                Logger.e("", "");
                if (response.code() == 200) {
                    response.body().setStatus(true);
                    data.postValue(response.body());
                }

            }

            @Override
            public void onFailure(Call<ResponseAssetHistory> call, Throwable t) {
            }
        });
        return data;

    }

    public LiveData<ResponseEmpty> heartBeatApi(JsonObject object, String token) {
        MutableLiveData<ResponseEmpty> val = new MutableLiveData<>();
        APIDetails endpoint = RequestConfig.getClientInterceptor(token).create(APIDetails.class);
        Call<ResponseEmpty> call = endpoint.getBookMark(object);
        call.enqueue(new Callback<ResponseEmpty>() {
            @Override
            public void onResponse(Call<ResponseEmpty> call, Response<ResponseEmpty> response) {
                if (response.code() == 200) {
                    response.body().setStatus(true);
                    val.postValue(response.body());
                } else {
                    ResponseEmpty responseEmpty = new ResponseEmpty();
                    responseEmpty.setStatus(false);
                    val.postValue(responseEmpty);
                }
            }

            @Override
            public void onFailure(Call<ResponseEmpty> call, Throwable t) {
                ResponseEmpty responseEmpty = new ResponseEmpty();
                responseEmpty.setStatus(false);
                val.postValue(responseEmpty);
            }
        });
        return val;
    }


}
