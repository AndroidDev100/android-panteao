package panteao.make.ready.callbacks.commonCallbacks;


import panteao.make.ready.beanModel.responseModels.VersionVerification.Result;
import panteao.make.ready.beanModel.responseModels.VersionVerification.Result;

public interface CallBacks {
    void onCountryClick(Result country, int position);

    default void common(boolean status){

    }
}
