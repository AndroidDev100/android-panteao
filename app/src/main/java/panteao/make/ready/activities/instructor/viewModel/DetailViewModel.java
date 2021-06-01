package panteao.make.ready.activities.instructor.viewModel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;

import com.make.baseCollection.baseCategoryModel.BaseCategory;
import com.make.bookmarking.bean.GetBookmarkResponse;
import panteao.make.ready.activities.layers.EntitlementLayer;
import panteao.make.ready.beanModel.AssetHistoryContinueWatching.ResponseAssetHistory;
import panteao.make.ready.beanModel.addComment.ResponseAddComment;
import panteao.make.ready.beanModel.allComments.ResponseAllComments;
import panteao.make.ready.beanModel.deleteComment.ResponseDeleteComment;
import panteao.make.ready.beanModel.emptyResponse.ResponseEmpty;
import panteao.make.ready.beanModel.entitle.ResponseEntitle;
import panteao.make.ready.beanModel.isLike.ResponseIsLike;
import panteao.make.ready.beanModel.isWatchList.ResponseContentInWatchlist;
import panteao.make.ready.beanModel.like.ResponseAddLike;
import panteao.make.ready.beanModel.responseModels.detailPlayer.ResponseDetailPlayer;
import panteao.make.ready.beanModel.responseModels.landingTabResponses.CommonRailData;
import panteao.make.ready.beanModel.responseModels.series.SeriesResponse;
import panteao.make.ready.beanModel.responseModels.series.season.SeasonResponse;
import panteao.make.ready.beanModel.watchList.ResponseWatchList;
import panteao.make.ready.repository.bookmarking.BookmarkingRepository;
import panteao.make.ready.repository.detail.DetailRepository;
import panteao.make.ready.repository.home.HomeFragmentRepository;
import panteao.make.ready.utils.constants.AppConstants;
import com.google.gson.JsonObject;

import java.util.List;

public class DetailViewModel extends DetailBaseViewModel {
    // private Context context;
    final DetailRepository detailRepository;


    public DetailViewModel(@NonNull Application application) {
        super(application);
        detailRepository = DetailRepository.getInstance();
    }

    @Override
    public LiveData<List<BaseCategory>> getAllCategories() {

        return HomeFragmentRepository.getInstance().getCategories(AppConstants.HOME_ENVEU);
    }

    public LiveData<ResponseDetailPlayer> hitApiDetailPlayer(boolean check, String token, int videoId) {
        return detailRepository.hitApiDetailPlayer(check, token, videoId);
    }

    public LiveData<List<CommonRailData>> hitApiYouMayLike(int videoId, int page, int size) {
        return detailRepository.hitApiYouMayLike(videoId, page, size);
    }

    public LiveData<ResponseWatchList> hitApiAddWatchList(String token, JsonObject data) {
        return detailRepository.hitApiAddToWatchList(token, data);
    }


    public LiveData<ResponseEmpty> hitApiRemoveWatchList(String token, String data) {
        return detailRepository.hitApiRemoveFromWatchList(token, data);
    }


    public LiveData<ResponseContentInWatchlist> hitApiIsWatchList(String token, JsonObject data) {
        return detailRepository.hitApiIsToWatchList(token, data);
    }


    public LiveData<ResponseIsLike> hitApiIsLike(String token, JsonObject data) {
        return detailRepository.hitApiIsLike(token, data);
    }

    public LiveData<ResponseAddLike> hitApiAddLike(String token, JsonObject data) {
        return detailRepository.hitApiAddLike(token, data);
    }

    public LiveData<ResponseEmpty> hitApiUnLike(String token, JsonObject data) {
        return detailRepository.hitApiUnLike(token, data);
    }

    public LiveData<ResponseEntitle> hitApiEntitlement(String token, String sku) {
        return EntitlementLayer.getInstance().hitApiEntitlement(token, sku);
    }


    public LiveData<ResponseAllComments> hitApiAllComents(String size, int page, JsonObject data) {
        return detailRepository.hitApiAllComment(size, page, data);
    }

    public LiveData<ResponseAddComment> hitApiAddComment(String token, JsonObject data) {
        return detailRepository.hitApiAddComment(token, data);
    }

    public LiveData<ResponseDeleteComment> hitApiDeleteComment(String token, String data) {
        return detailRepository.hitApiDeleteComment(token, data);
    }


    public LiveData<JsonObject> hitLogout(boolean session, String token) {
        return detailRepository.hitApiLogout(session, token);
    }

    public LiveData<SeriesResponse> getSeriesDetail(int seriesId) {
        return detailRepository.getSeriesDetail(seriesId);
    }

    public LiveData<SeasonResponse> getVOD(int seriesID, int pageNo, int length) {
        return detailRepository.getVOD(seriesID, pageNo, length);
    }

    public LiveData<List<SeasonResponse>> hitMultiRequestSeries(int size, SeriesResponse data, int railSize) {
        return detailRepository.multiRequestSeries(size, data, railSize);
    }


    public LiveData<SeasonResponse> singleRequestSeries(int id, int page, int size) {
        return detailRepository.singleRequestSeries(id, page, size);
    }

    public LiveData<ResponseEmpty> heartBeatApi(JsonObject assetRequest, String token) {
        return detailRepository.heartBeatApi(assetRequest, token);
    }

    public LiveData<ResponseAssetHistory> getMultiAssetHistory(String token, JsonObject data) {
        return detailRepository.getMultiAssetHistory(token, data);
    }

    @Override
    public void resetObject() {

    }

    public LiveData<GetBookmarkResponse> getBookMarkByVideoId(String token,int videoId) {
    return BookmarkingRepository.getInstance().getBookmarkByVideoId(token,videoId);
    }
}
