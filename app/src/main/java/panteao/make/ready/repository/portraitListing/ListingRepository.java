package panteao.make.ready.repository.portraitListing;

import android.app.Activity;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import panteao.make.ready.beanModelV3.playListModelV2.EnveuCommonResponse;
import panteao.make.ready.beanModel.responseModels.landingTabResponses.railData.PlaylistRailData;
import panteao.make.ready.networking.apiendpoints.ApiInterface;
import panteao.make.ready.networking.apiendpoints.RequestConfig;
import panteao.make.ready.utils.config.LanguageLayer;

import panteao.make.ready.beanModel.responseModels.landingTabResponses.railData.PlaylistRailData;
import panteao.make.ready.beanModelV3.playListModelV2.EnveuCommonResponse;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ListingRepository {

    private static ListingRepository projectRepository;
    Activity activity;

    public synchronized static ListingRepository getInstance() {
        if (projectRepository == null) {
            projectRepository = new ListingRepository();
        }
        return projectRepository;
    }


    String languageCode;
    public LiveData<PlaylistRailData> getRailData(int playlistId, int pageNo, int length) {
        MutableLiveData<PlaylistRailData> mutableLiveData = new MutableLiveData<>();
        ApiInterface endpoint = RequestConfig.getEnveuClient().create(ApiInterface.class);
        languageCode= LanguageLayer.getCurrentLanguageCode();
        Call<EnveuCommonResponse> call = endpoint.getPlaylistDetailsById("6056171168001",languageCode, pageNo, length);
        call.enqueue(new Callback<EnveuCommonResponse>() {
            @Override
            public void onResponse(Call<EnveuCommonResponse> call, Response<EnveuCommonResponse> response) {
                if (response.body() != null && response.body().getData() != null) {

                }
            }

            @Override
            public void onFailure(Call<EnveuCommonResponse> call, Throwable t) {

            }
        });
        /*call.enqueue(new Callback<PlaylistRailData>() {
            @Override
            public void onResponse(@NonNull Call<PlaylistRailData> call, @NonNull SeriesSearchResponse<PlaylistRailData> response) {
                mutableLiveData.postValue(response.body());
            }

            @Override
            public void onFailure(@NonNull Call<PlaylistRailData> call, @NonNull Throwable t) {

            }
        });*/

        return mutableLiveData;
    }


}

