package panteao.make.ready.Bookmarking;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.make.bookmarking.bean.BookmarkingResponse;
import com.make.bookmarking.bean.GetBookmarkResponse;
import com.make.bookmarking.bean.continuewatching.GetContinueWatchingBean;
import com.make.watchHistory.beans.ResponseWatchHistoryAssetList;
import panteao.make.ready.beanModel.emptyResponse.ResponseEmpty;
import panteao.make.ready.beanModel.responseGetWatchlist.ResponseGetIsWatchlist;
import panteao.make.ready.beanModel.responseIsLike.ResponseIsLike;
import panteao.make.ready.repository.bookmarking.BookmarkingRepository;
import panteao.make.ready.repository.userManagement.RegistrationLoginRepository;
import panteao.make.ready.repository.userinteraction.UserInterationRepository;
import com.google.gson.JsonObject;

import panteao.make.ready.beanModel.emptyResponse.ResponseEmpty;
import panteao.make.ready.beanModel.responseGetWatchlist.ResponseGetIsWatchlist;
import panteao.make.ready.beanModel.responseIsLike.ResponseIsLike;
import panteao.make.ready.repository.bookmarking.BookmarkingRepository;
import panteao.make.ready.repository.userManagement.RegistrationLoginRepository;
import panteao.make.ready.repository.userinteraction.UserInterationRepository;

public class BookmarkingViewModel extends AndroidViewModel {
    public BookmarkingViewModel(@NonNull Application application) {
        super(application);
    }

    public LiveData<BookmarkingResponse> bookmarkVideo(String token, int assestId, int currentPosition) {
        return BookmarkingRepository.getInstance().bookmarkVideo(token,assestId,currentPosition);
    }

    public MutableLiveData<GetBookmarkResponse> finishBookmark(String token, int assestId) {
        return BookmarkingRepository.getInstance().finishBookmark(token,assestId);
    }

    public MutableLiveData<GetContinueWatchingBean> getContinueWatchingData(String token, int pageNumber, int pageSize) {
        return BookmarkingRepository.getInstance().getContinueWatchingData(token,pageNumber,pageSize);
}

    public LiveData<ResponseWatchHistoryAssetList> getWatchHistory(String token,
                                                                   int page, int size) {
        return BookmarkingRepository.getInstance().hitApiGetWatchHistoryList(token, page, size);
    }

    public void addToWatchHistory(String token, int assestId) {
        BookmarkingRepository.getInstance().addToWatchHistory(token,assestId);
    }

    public LiveData<BookmarkingResponse>  deleteFromWatchHistory(String token, int assetId) {
        return BookmarkingRepository.getInstance().deleteFromWatchHistory(token,assetId);
    }

    public LiveData<ResponseGetIsWatchlist> hitApiIsWatchList(String token, int seriesId) {
        return UserInterationRepository.getInstance().hitApiIsToWatchList(token, seriesId);
    }

    public LiveData<ResponseEmpty> hitApiAddWatchList(String token, int seriesId) {
        return UserInterationRepository.getInstance().hitApiAddToWatchList(token, seriesId);
    }


    public LiveData<ResponseIsLike> hitApiIsLike(String token, int assetId) {
        return UserInterationRepository.getInstance().hitApiIsLike(token, assetId);
    }

    public LiveData<ResponseEmpty> hitApiDeleteLike(String token, int assetId) {
        return UserInterationRepository.getInstance().hitApiDeleteLike(token, assetId);
    }

    public LiveData<ResponseEmpty> hitRemoveWatchlist(String token, int assetId) {
        return UserInterationRepository.getInstance().hitRemoveWatchlist(token, assetId);
    }

    public LiveData<ResponseEmpty> hitApiAddLike(String token, int assetId) {
        return UserInterationRepository.getInstance().hitApiAddLike(token, assetId);
    }

    public LiveData<JsonObject> hitLogout(boolean session, String token) {
        return RegistrationLoginRepository.getInstance().hitApiLogout(session, token);
    }

    public LiveData<ResponseWatchHistoryAssetList> getMywatchListData(String token, int pageNumber, int pageSize) {
        return BookmarkingRepository.getInstance().getMyWatchListData(token,pageNumber,pageSize);
    }

}
