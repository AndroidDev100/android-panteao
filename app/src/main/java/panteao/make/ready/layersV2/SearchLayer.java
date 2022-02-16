package panteao.make.ready.layersV2;

import androidx.lifecycle.LiveData;

import panteao.make.ready.beanModel.enveuCommonRailData.RailCommonData;
import panteao.make.ready.callbacks.apicallback.ApiResponseModel;
import panteao.make.ready.networking.apiendpoints.ApiInterface;
import panteao.make.ready.networking.apiendpoints.RequestConfig;
import panteao.make.ready.networking.servicelayer.APIServiceLayer;

import java.util.List;

public class SearchLayer {

    private static SearchLayer searchLayerInstance;
    private static ApiInterface endpoint;
    ApiResponseModel callBack;

    private SearchLayer() {

    }

    public static SearchLayer getInstance() {
        if (searchLayerInstance == null) {
            endpoint = RequestConfig.getEnveuClient().create(ApiInterface.class);
            searchLayerInstance = new SearchLayer();
        }
        return (searchLayerInstance);
    }


    public LiveData<List<RailCommonData>> getSearchData(String type, String keyword, int size, int page) {
        return APIServiceLayer.getInstance().getSearchData(keyword,size,page);
    }

    public LiveData<RailCommonData> getSingleCategorySearch(String type, String keyword, int size, int page) {
        return APIServiceLayer.getInstance().getSingleCategorySearch(type,keyword,size,page);
    }


}
