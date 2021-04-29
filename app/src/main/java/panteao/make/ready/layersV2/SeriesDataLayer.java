package panteao.make.ready.layersV2;

import panteao.make.ready.callbacks.apicallback.ApiResponseModel;
import panteao.make.ready.networking.apiendpoints.ApiInterface;
import panteao.make.ready.networking.apiendpoints.RequestConfig;
import panteao.make.ready.networking.servicelayer.APIServiceLayer;

public class SeriesDataLayer {

    private static SeriesDataLayer seriesDataLayerInstance;
    private static ApiInterface endpoint;
    ApiResponseModel callBack;

    private SeriesDataLayer() {

    }

    public static SeriesDataLayer getInstance() {
        if (seriesDataLayerInstance == null) {
            endpoint = RequestConfig.getEnveuClient().create(ApiInterface.class);
            seriesDataLayerInstance = new SeriesDataLayer();
        }
        return (seriesDataLayerInstance);
    }


    public void getSeriesData(String assetID, ApiResponseModel listener) {
        APIServiceLayer.getInstance().getSeriesData(assetID, listener);
    }
}
