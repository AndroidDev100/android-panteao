package panteao.make.ready.activities.purchase.ui.viewmodel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import panteao.make.ready.activities.layers.EntitlementLayer;
import panteao.make.ready.beanModel.cancelPurchase.ResponseCancelPurchase;
import panteao.make.ready.beanModel.entitle.ResponseEntitle;
import panteao.make.ready.beanModel.membershipAndPlan.ResponseMembershipAndPlan;
import panteao.make.ready.beanModel.purchaseModel.PurchaseModel;
import panteao.make.ready.beanModel.purchaseModel.PurchaseResponseModel;
import panteao.make.ready.repository.purchase.PurchaseRepository;
import com.google.gson.JsonObject;


public class PurchaseViewModel extends AndroidViewModel {

    public PurchaseViewModel(@NonNull Application application) {
        super(application);
    }


    public LiveData<PurchaseResponseModel> createNewPurchaseRequest(String token, JsonObject data, PurchaseModel model,String sku) {
        return PurchaseRepository.getInstance().createNewPurchaseRequest(token, model,sku);
    }

    public LiveData<PurchaseResponseModel> callInitiatePaymet(String token, String orderId) {
        return PurchaseRepository.getInstance().callInitiatePaymet(token, orderId);
    }


    public LiveData<ResponseMembershipAndPlan> getPlans(String token) {
        return PurchaseRepository.getInstance().getPlans(token);
    }

    public LiveData<ResponseMembershipAndPlan> getNewPlans(String token) {
        return PurchaseRepository.getInstance().getNewPlans(token);
    }

    public LiveData<ResponseCancelPurchase> cancelPlan(String token) {
        return PurchaseRepository.getInstance().cancelPlan(token);
    }

    public LiveData<PurchaseResponseModel> updatePurchase(String billingError,String paymentStatus,String token,String purchaseToken,String paymentId,String orderId,PurchaseModel purchaseModel,String purchasedSKU) {
        return PurchaseRepository.getInstance().updatePurchase(billingError,paymentStatus,token,purchaseToken,paymentId,orderId,purchaseModel,purchasedSKU);
    }

    public LiveData<ResponseEntitle> hitApiEntitlement(String token, String sku) {

        return EntitlementLayer.getInstance().hitApiEntitlement(token, sku);
    }



}
