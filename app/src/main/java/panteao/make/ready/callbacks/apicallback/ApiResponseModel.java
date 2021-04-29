package panteao.make.ready.callbacks.apicallback;

import com.make.baseCollection.baseCategoryModel.BaseCategory;
import panteao.make.ready.networking.errormodel.ApiErrorModel;

import java.util.List;

import panteao.make.ready.networking.errormodel.ApiErrorModel;

public interface ApiResponseModel<T> {
    void onStart();

    default void onSuccess(T response){

    }
    default void onSuccess(List<BaseCategory> catList){

    }
    default void onError(ApiErrorModel apiError){

    }

    default void onFailure(ApiErrorModel httpError){

    }
}