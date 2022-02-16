package panteao.make.ready.repository.series;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import panteao.make.ready.BuildConfig;
import panteao.make.ready.beanModel.AssetHistoryContinueWatching.ResponseAssetHistory;
import panteao.make.ready.beanModel.addComment.ResponseAddComment;
import panteao.make.ready.beanModel.allComments.ResponseAllComments;
import panteao.make.ready.beanModel.deleteComment.ResponseDeleteComment;
import panteao.make.ready.beanModel.emptyResponse.ResponseEmpty;
import panteao.make.ready.beanModel.isLike.ResponseIsLike;
import panteao.make.ready.beanModel.isWatchList.ResponseContentInWatchlist;
import panteao.make.ready.beanModel.like.ResponseAddLike;
import panteao.make.ready.beanModel.responseModels.series.SeriesResponse;
import panteao.make.ready.beanModel.responseModels.series.season.SeasonResponse;
import panteao.make.ready.beanModel.tempData.ResponseContinueList;
import panteao.make.ready.beanModel.watchList.ResponseWatchList;
import panteao.make.ready.networking.detailPlayer.APIDetails;
import panteao.make.ready.networking.apiendpoints.APIInterFaceContinue;
import panteao.make.ready.networking.apiendpoints.ApiInterface;
import panteao.make.ready.networking.apiendpoints.RequestConfig;
import panteao.make.ready.networking.profiler.HttpProfiler;
import panteao.make.ready.utils.constants.AppConstants;
import panteao.make.ready.utils.cropImage.helpers.Logger;
import panteao.make.ready.utils.cropImage.helpers.PrintLogging;
import com.google.gson.JsonObject;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import io.reactivex.Observable;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Function;
import io.reactivex.schedulers.Schedulers;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class SeriesRepository {

    private static SeriesRepository seriesRepository;
    List<SeasonResponse> railData;
    List<ResponseAssetHistory> listData;
    private Retrofit retrofit2 = null;

    public static SeriesRepository getInstance() {
        if (seriesRepository == null) {
            seriesRepository = new SeriesRepository();
        }
        return seriesRepository;
    }

    public LiveData<SeriesResponse> getSeriesDetail(int seriesId) {

        MutableLiveData<SeriesResponse> mutableLiveData = new MutableLiveData<>();
        APIDetails endpoint = RequestConfig.getClientHeader().create(APIDetails.class);
        Call<SeriesResponse> call = endpoint.getSeriesDetails(seriesId);
        call.enqueue(new Callback<SeriesResponse>() {
            @Override
            public void onResponse(@NonNull Call<SeriesResponse> call, @NonNull Response<SeriesResponse> response) {

                PrintLogging.printLog(response.body() + "getSeriesDetail");
                mutableLiveData.postValue(response.body());
            }

            @Override
            public void onFailure(@NonNull Call<SeriesResponse> call, @NonNull Throwable t) {
            }
        });

        return mutableLiveData;
    }

    public LiveData<SeasonResponse> getSeasonDetail(int seasonId, int pageNo, int length) {
        MutableLiveData<SeasonResponse> mutableLiveData = new MutableLiveData<>();
        APIDetails endpoint = RequestConfig.getClientHeader().create(APIDetails.class);
        Call<SeasonResponse> call = endpoint.getSeasonEpisode(seasonId, pageNo, length);
        call.enqueue(new Callback<SeasonResponse>() {
            @Override
            public void onResponse(@NonNull Call<SeasonResponse> call, @NonNull Response<SeasonResponse> response) {
                if (response.body() != null)
                    mutableLiveData.postValue(response.body());
                else {
                    mutableLiveData.postValue(new SeasonResponse());
                }
            }

            @Override
            public void onFailure(@NonNull Call<SeasonResponse> call, @NonNull Throwable t) {
                mutableLiveData.postValue(new SeasonResponse());
            }
        });
        return mutableLiveData;
    }

    public LiveData<List<SeasonResponse>> multiRequestSeries(int size, SeriesResponse playlist) {
        MutableLiveData<List<SeasonResponse>> responseHome = new MutableLiveData<>();
        railData = new ArrayList<>();
        APIDetails endpoint = RequestConfig.getClientHeader().create(APIDetails.class);

        List<Observable<?>> requests = new ArrayList<>();
        for (int i = 0; i < size; i++) {
            requests.add(endpoint.getSeasonEpisodeMulti(playlist.getData().getSeasons().get(i).getId(), 0, 20));
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
                    data.postValue(response.body());
                }

            }

            @Override
            public void onFailure(Call<ResponseAssetHistory> call, Throwable t) {
            }
        });
        return data;

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
                    responseWatchList.setData(response.body().getData());
                    responsePlayer.postValue(responseWatchList);
                } else if (response.code() == 400 || response.code() == 404) {
                    responseWatchList.setStatus(false);
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

    public LiveData<ResponseContentInWatchlist> hitApiIsToWatchList(String token, JsonObject data) {
        MutableLiveData<ResponseContentInWatchlist> responsePlayer = new MutableLiveData<>();

        APIDetails endpoint = RequestConfig.getClientInterceptor(token).create(APIDetails.class);
        Call<ResponseContentInWatchlist> call = endpoint.getIsContentWatchList(data);
        call.enqueue(new Callback<ResponseContentInWatchlist>() {
            @Override
            public void onResponse(@NonNull Call<ResponseContentInWatchlist> call, @NonNull Response<ResponseContentInWatchlist> response) {
                Logger.e("", "" + response.body());
                ResponseContentInWatchlist responseWatchList = new ResponseContentInWatchlist();

                if (response.code() == 200) {
                    responseWatchList.setStatus(true);
                    responseWatchList.setData(response.body().getData());
                    responsePlayer.postValue(responseWatchList);
                } else {
                    responseWatchList.setStatus(false);
                    responseWatchList.setResponseCode(response.code());
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
                    responseEntitlement.setData(response.body().getData());
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
                    responseAddComment.setData(response.body().getData());
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

    public LiveData<ResponseIsLike> hitApiIsLike(String token, JsonObject data) {
        MutableLiveData<ResponseIsLike> responsePlayer = new MutableLiveData<>();

        APIDetails endpoint = RequestConfig.getClientInterceptor(token).create(APIDetails.class);
        Call<ResponseIsLike> call = endpoint.getIsLike(data);
        call.enqueue(new Callback<ResponseIsLike>() {
            @Override
            public void onResponse(@NonNull Call<ResponseIsLike> call, @NonNull Response<ResponseIsLike> response) {
                //  Logger.e("", "" + response.body());
                ResponseIsLike responseWatchList = new ResponseIsLike();
                if (response.code() == 200) {
                    responseWatchList.setStatus(true);
                    responseWatchList.setData(response.body().getData());
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

    public LiveData<ResponseContinueList> getAssetList(String token, ResponseAssetHistory list) {
        APIInterFaceContinue endpoint = getClientForContinue("http://demo4120540.mockable.io/", token).create(APIInterFaceContinue.class);

        MutableLiveData<ResponseContinueList> responseModel = new MutableLiveData<>();


        JSONObject jsonObj = new JSONObject();
        JSONArray jsonArr = new JSONArray();
        try {
            for (int i = 0; i < list.getData().getItems().size(); i++) {
                JSONObject pnObj = new JSONObject();
                pnObj.put("id", list.getData().getItems().get(i).getId());
                pnObj.put("type", list.getData().getItems().get(i).getType());
                jsonArr.put(pnObj);

            }

            jsonObj.put("", jsonArr);
        } catch (Exception e) {

        }

        Call<ResponseContinueList> call = endpoint.getAssetList(jsonObj);

        call.enqueue(new Callback<ResponseContinueList>() {
            @Override
            public void onResponse(Call<ResponseContinueList> call, Response<ResponseContinueList> response) {
                if (response.code() == 200) {
                    responseModel.postValue(response.body());
                }

            }

            @Override
            public void onFailure(Call<ResponseContinueList> call, Throwable t) {

            }
        });

        return responseModel;


    }

    public Retrofit getClientForContinue(String baseURl, String token) {

        Logger.e("TOKEN", token);
        HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor();
        interceptor.setLevel(HttpLoggingInterceptor.Level.BODY);

        OkHttpClient.Builder httpClient = new OkHttpClient.Builder();
        httpClient.addInterceptor(interceptor);

        if(HttpProfiler.getInstance().needHttpProfiler()){
            if (BuildConfig.DEBUG) {
                httpClient.addInterceptor(HttpProfiler.getInstance().getOkHttpProfilerInterceptor());
            }
        }

        httpClient.addInterceptor(chain -> {
            Request original = chain.request();
            // Request customization: add request headers
            Request.Builder requestBuilder = original.newBuilder()
                    .addHeader("Content-Type", " application/json")
                    .addHeader("x-platform", " android")
                    .addHeader("x-auth-token", " " + token);

            Request request = requestBuilder.build();
            return chain.proceed(request);
        });

        OkHttpClient client = httpClient.build();


        retrofit2 = new Retrofit.Builder()
                .baseUrl(baseURl)
                .addConverterFactory(GsonConverterFactory.create())
                .client(client)
                .build();

        return retrofit2;
    }


}
