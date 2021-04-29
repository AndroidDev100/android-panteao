package panteao.make.ready.layersV2;

import com.make.bookmarking.bean.continuewatching.ContinueWatchingBookmark;
import com.make.watchHistory.beans.ItemsItem;
import panteao.make.ready.callbacks.apicallback.ApiResponseModel;
import panteao.make.ready.callbacks.commonCallbacks.CommonApiCallBack;
import panteao.make.ready.networking.apiendpoints.ApiInterface;
import panteao.make.ready.networking.apiendpoints.RequestConfig;
import panteao.make.ready.networking.servicelayer.APIServiceLayer;

import java.util.List;

public class ContinueWatchingLayer {

    private static ContinueWatchingLayer videoDetailLayerInstance;
    private static ApiInterface endpoint;
    ApiResponseModel callBack;

    private ContinueWatchingLayer() {

    }

    public static ContinueWatchingLayer getInstance() {
        if (videoDetailLayerInstance == null) {
            endpoint = RequestConfig.getEnveuClient().create(ApiInterface.class);
            videoDetailLayerInstance = new ContinueWatchingLayer();
        }
        return (videoDetailLayerInstance);
    }

    public void getContinueWatchingVideos(List<ContinueWatchingBookmark> continueWatchingBookmarkList, String manualImageAssetId, CommonApiCallBack commonApiCallBack) {
        APIServiceLayer.getInstance().getContinueWatchingVideos(continueWatchingBookmarkList, manualImageAssetId, commonApiCallBack);
    }

    public void getWatchHistoryVideos(List<ItemsItem> continueWatchingBookmarkList, String manualImageAssetId, CommonApiCallBack commonApiCallBack) {
        APIServiceLayer.getInstance().getWatchListVideos(continueWatchingBookmarkList, manualImageAssetId, commonApiCallBack);
    }

}
