package panteao.make.ready.layersV2;

import com.make.baseCollection.baseCategoryModel.BaseCategory;
import panteao.make.ready.callbacks.apicallback.ApiResponseModel;
import panteao.make.ready.networking.apiendpoints.ApiInterface;
import panteao.make.ready.networking.apiendpoints.RequestConfig;
import panteao.make.ready.networking.servicelayer.APIServiceLayer;

public class ListPaginationDataLayer {

    private static ListPaginationDataLayer listPaginationDataLayerInstance;
    private static ApiInterface endpoint;
    private ApiResponseModel callBack;

    private ListPaginationDataLayer() {

    }

    public static ListPaginationDataLayer getInstance() {
        if (listPaginationDataLayerInstance == null) {
            endpoint = RequestConfig.getEnveuClient().create(ApiInterface.class);
            listPaginationDataLayerInstance = new ListPaginationDataLayer();
        }
        return (listPaginationDataLayerInstance);
    }


    public void getPlayListByWithPagination(String playlistID,
                                            int pageNumber,
                                            int pageSize,
                                            BaseCategory screenWidget, ApiResponseModel listener) {
        APIServiceLayer.getInstance().getPlayListByWithPagination(playlistID, pageNumber, pageSize, screenWidget, listener);
    }
}
