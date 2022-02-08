package panteao.make.ready.activities.myPurchases.viewmodel;

import android.app.Application;
import android.content.Context;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.google.gson.JsonObject;

import panteao.make.ready.beanModel.emptyResponse.ResponseEmpty;
import panteao.make.ready.beanModel.responseModels.LoginResponse.LoginResponseModel;
import panteao.make.ready.beanModel.responseModels.MyPurchasesResponseModel;
import panteao.make.ready.beanModel.userProfile.UserProfileResponse;
import panteao.make.ready.repository.home.HomeRepository;
import panteao.make.ready.repository.purchase.MyPurchasesRepository;
import panteao.make.ready.repository.userManagement.RegistrationLoginRepository;


public class MyPurchaseViewModel extends AndroidViewModel {
    final MyPurchasesRepository myPurchasesRepository;

    public MyPurchaseViewModel(@NonNull Application application) {
        super(application);
        myPurchasesRepository = MyPurchasesRepository.Companion.getInstance();
    }
    public LiveData<MyPurchasesResponseModel> hitMyPurchasesAPI(Context context,String auth, String page, String size, @Nullable String orderStatus) {
        return myPurchasesRepository.getMyPurchasesAPIresponse(context,auth,page,size,orderStatus);
    }
}
