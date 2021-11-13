package panteao.make.ready.utils.inAppBilling;

import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.MutableLiveData;

import com.android.billingclient.api.BillingClient;
import com.android.billingclient.api.BillingResult;
import com.android.billingclient.api.Purchase;
import com.android.billingclient.api.SkuDetails;
import com.android.billingclient.api.SkuDetailsParams;
import com.android.billingclient.api.SkuDetailsResponseListener;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import panteao.make.ready.PanteaoApplication;
import panteao.make.ready.R;
import panteao.make.ready.SDKConfig;
import panteao.make.ready.activities.purchase.ui.VodOfferType;
import panteao.make.ready.beanModel.membershipAndPlan.DataItem;
import panteao.make.ready.beanModel.membershipAndPlan.ResponseMembershipAndPlan;
import panteao.make.ready.beanModel.purchaseModel.PurchaseModel;
import panteao.make.ready.beanModel.purchaseModel.PurchaseResponseModel;
import panteao.make.ready.networking.apiendpoints.RequestConfig;
import panteao.make.ready.networking.detailPlayer.APIDetails;
import panteao.make.ready.networking.intercepter.ErrorCodesIntercepter;
import panteao.make.ready.utils.helpers.ksPreferenceKeys.KsPreferenceKeys;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class PurchaseHandler {
    private static PurchaseHandler projectRepository;
    BillingClient myBillingClient;
    RestoreSubscriptionCallback callback;
    boolean getPlans=false;
    public synchronized static PurchaseHandler getInstance() {
        if (projectRepository == null) {
            projectRepository = new PurchaseHandler();
        }
        return projectRepository;
    }

    public void checkPurchaseHistory(List<Purchase> purchases, BillingClient billingClient,RestoreSubscriptionCallback calling) {
        myBillingClient=billingClient;
        this.callback=calling;
        if (purchases!=null && purchases.size()>0){
            try {
                if (purchases.get(0).getPurchaseState()==Purchase.PurchaseState.PURCHASED){
                    JSONObject jsonObject=new JSONObject(purchases.get(0).getOriginalJson());
                    if (jsonObject.has("purchaseToken")){
                        Log.w("purchaseToken--2",jsonObject.getString("purchaseToken"));
                        Log.w("purchaseToken--2",purchases.get(0).getSku());
                        callGetPlansAPI(purchases,callback);
                    }
                }else {
                    callback.subscriptionStatus(false,"No Subscription found");
                }

            }catch (Exception ignored){

            }
        }else {
            callback.subscriptionStatus(false,"No Subscription found");
        }
    }

    private void callGetPlansAPI(List<Purchase> purchases, RestoreSubscriptionCallback callback) {
        try {
            APIDetails endpoint = RequestConfig.getUserInteration(KsPreferenceKeys.getInstance().getAppPrefAccessToken()).create(APIDetails.class);
            Call<ResponseMembershipAndPlan> call = endpoint.getPlans("RECURRING_SUBSCRIPTION",true);
            call.enqueue(new Callback<ResponseMembershipAndPlan>() {
                @Override
                public void onResponse(Call<ResponseMembershipAndPlan> call, Response<ResponseMembershipAndPlan> response) {
                    Log.w("getPlansRes","in");
                    ResponseMembershipAndPlan purchaseResponseModel = new ResponseMembershipAndPlan();
                    if (response.code() == 200) {
                        purchaseResponseModel.setStatus(true);
                        purchaseResponseModel.setData(response.body().getData());
                        checkEntitlementState(purchaseResponseModel,purchases,callback);
                    } else {
                        purchaseResponseModel.setStatus(false);
                        callback.subscriptionStatus(false,"No Subscription found");
                    }

                }

                @Override
                public void onFailure(Call<ResponseMembershipAndPlan> call, Throwable t) {
                    ResponseMembershipAndPlan purchaseResponseModel = new ResponseMembershipAndPlan();
                    purchaseResponseModel.setStatus(false);
                    // liveDataPurchaseResponse.postValue(purchaseResponseModel);

                }
            });

        }catch (Exception ignored){
            getPlans=false;
        }


    }

    int count=0;
    private void checkEntitlementState(ResponseMembershipAndPlan purchaseResponseModel, List<Purchase> purchases, RestoreSubscriptionCallback callback) {
        try {
            if (purchaseResponseModel!=null && purchaseResponseModel.getData().size()>0 && purchaseResponseModel.isStatus()){
                for (int i = 0; i < purchaseResponseModel.getData().size(); i++) {
                    if (!purchaseResponseModel.getData().get(i).getEntitlementState()) {
                        Purchase purchase=purchases.get(count);
                        String sku = purchase.getSku();
                        String newSKU=sku.replace(".","_");
                        String finalSKU="svod_"+newSKU;
                        Log.w("finalSKU",finalSKU);
                        if (finalSKU.equalsIgnoreCase(purchaseResponseModel.getData().get(i).getIdentifier())){
                            Log.w("identifiers",purchaseResponseModel.getData().get(i).getIdentifier());
                            Log.w("identifiers",purchase.getSku());
                            initiatePurchaseFlow(purchase,purchaseResponseModel.getData().get(i),callback);
                            break;
                        }else {
                            getPlans=false;
                            callback.subscriptionStatus(false,"No Subscription found");
                        }
                    }
                }
            }else {
                getPlans=false;
            }
        }catch (Exception ignored){
            getPlans=false;
        }

    }

    private void initiatePurchaseFlow(Purchase purchase, DataItem dataItem, RestoreSubscriptionCallback callback) {
        try {
            PurchaseModel purchaseModel=new PurchaseModel();
            List<String> subSkuList=new ArrayList<>();
            subSkuList.add(purchase.getSku());
            SkuDetailsParams.Builder params = SkuDetailsParams.newBuilder();
            params.setSkusList(subSkuList).setType(BillingClient.SkuType.SUBS);
            myBillingClient.querySkuDetailsAsync(params.build(),
                    new SkuDetailsResponseListener() {
                        @Override
                        public void onSkuDetailsResponse(BillingResult billingResult,
                                                         List<SkuDetails> skuDetailsList) {
                            if (skuDetailsList != null && skuDetailsList.size() > 0) {
                                for (SkuDetails skuDetails : skuDetailsList) {
                                    purchaseModel.setTitle("" + dataItem.getTitle());
                                    purchaseModel.setPurchaseOptions(VodOfferType.RECURRING_SUBSCRIPTION​.name());
                                    purchaseModel.setIdentifier(dataItem.getIdentifier());
                                    purchaseModel.setCurrency(skuDetails.getPriceCurrencyCode());
                                }
                                callCreateNewPurchase(purchaseModel,purchase,dataItem,callback);
                            }else {
                                getPlans=false;
                                callback.subscriptionStatus(false,"No Subscription found");
                            }
                        }
                    });

        }catch (Exception ignored){
            Log.w("billingProcess",ignored.toString());
            getPlans=false;
        }
    }

    private void callCreateNewPurchase(PurchaseModel model, Purchase purchase, DataItem dataItem, RestoreSubscriptionCallback callback) {
        try {
            JSONObject jsonObject=new JSONObject();
            JSONObject jsonObject1=new JSONObject();

            try {
                if (model.getPurchaseOptions().contains(VodOfferType.PERPETUAL.name()) || model.getPurchaseOptions().contains(VodOfferType.RENTAL.name())){
                    jsonObject1.put("enveuSMSPlanName", model.getIdentifier());
                    jsonObject1.put("enveuSMSPlanTitle", model.getTitle());
                    jsonObject1.put("enveuSMSOfferType", model.getPurchaseOptions());
                    jsonObject1.put("enveuSMSPurchaseCurrency", model.getCurrency());
                    jsonObject1.put("enveuSMSOfferContentSKU", purchase.getSku());
                }else {
                    jsonObject1.put("enveuSMSPlanName", model.getIdentifier());
                    jsonObject1.put("enveuSMSPlanTitle", model.getTitle());
                    jsonObject1.put("enveuSMSSubscriptionOfferType", model.getPurchaseOptions());
                    jsonObject1.put("enveuSMSPurchaseCurrency", model.getCurrency());
                }


                jsonObject.put("notes",jsonObject1);
            }catch (Exception ignored){

            }

            JsonParser jsonParser = new JsonParser();
            JsonObject gsonObject = (JsonObject)jsonParser.parse(jsonObject.toString());


            String planName=model.getIdentifier()+"_PLAN/";
            String paymentURL= SDKConfig.getInstance().getPAYMENT_BASE_URL()+"v1/offer/"+planName;
            APIDetails endpoint = RequestConfig.paymentClient(KsPreferenceKeys.getInstance().getAppPrefAccessToken(),paymentURL).create(APIDetails.class);
            Call<PurchaseResponseModel> call = endpoint.getCreateNewPurchase(gsonObject);
            call.enqueue(new Callback<PurchaseResponseModel>() {
                @Override
                public void onResponse(@NonNull Call<PurchaseResponseModel> call, @NonNull Response<PurchaseResponseModel> response) {

                    PurchaseResponseModel purchaseResponseModel = new PurchaseResponseModel();
                    if (response.code() == 200) {
                        purchaseResponseModel.setStatus(true);
                        purchaseResponseModel.setData(response.body().getData());
                        String orderId = purchaseResponseModel.getData().getOrderId();
                        callInitiatePaymentApi(orderId,purchase,dataItem,model,callback);
                    } else {

                        PurchaseResponseModel purchaseResponseModel2 = ErrorCodesIntercepter.getInstance().createNewOrder(response);
                        getPlans=false;
                        callback.subscriptionStatus(false,"No Subscription found");
                    }

                }

                @Override
                public void onFailure(@NonNull Call<PurchaseResponseModel> call, @NonNull Throwable t) {
                    PurchaseResponseModel purchaseResponseModel = new PurchaseResponseModel();
                    purchaseResponseModel.setStatus(false);
                    purchaseResponseModel.setResponseCode(500);
                    purchaseResponseModel.setDebugMessage(PanteaoApplication.getInstance().getResources().getString(R.string.something_went_wrong_at_our_end));
                    getPlans=false;
                    callback.subscriptionStatus(false,"No Subscription found");
                }
            });
        }catch (Exception ignored){
            getPlans=false;
            callback.subscriptionStatus(false,"No Subscription found");
        }

    }

    private void callInitiatePaymentApi(String orderID, Purchase purchase, DataItem dataItem, PurchaseModel model, RestoreSubscriptionCallback callback) {

        try {
            JSONObject jsonObject=new JSONObject();
            try {
                jsonObject.put("paymentProvider","GOOGLE_IAP");
            }catch (Exception ignored){

            }

            JsonParser jsonParser = new JsonParser();
            JsonObject gsonObject = (JsonObject)jsonParser.parse(jsonObject.toString());

            String initiateURL= SDKConfig.getInstance().getPAYMENT_BASE_URL()+"v1/order/"+orderID+"/";

            APIDetails endpoint = RequestConfig.paymentClient(KsPreferenceKeys.getInstance().getAppPrefAccessToken(),initiateURL).create(APIDetails.class);
            Call<PurchaseResponseModel> call = endpoint.initiatePurchase(gsonObject);
            call.enqueue(new Callback<PurchaseResponseModel>() {
                @Override
                public void onResponse(@NonNull Call<PurchaseResponseModel> call, @NonNull Response<PurchaseResponseModel> response) {

                    PurchaseResponseModel purchaseResponseModel = new PurchaseResponseModel();
                    if (response.code() == 200) {
                        purchaseResponseModel.setStatus(true);
                        purchaseResponseModel.setData(response.body().getData());
                        String paymentId = purchaseResponseModel.getData().getPaymentId().toString();
                        updatePayment("","PAYMENT_DONE",purchase.getPurchaseToken(), paymentId,purchase.getOrderId(),purchase,model,orderID,callback);
                    } else {
                        PurchaseResponseModel purchaseResponseModel2 = ErrorCodesIntercepter.getInstance().initiateOrder(response);
                        getPlans=false;
                        callback.subscriptionStatus(false,"No Subscription found");
                    }

                }

                @Override
                public void onFailure(@NonNull Call<PurchaseResponseModel> call, @NonNull Throwable t) {
                    PurchaseResponseModel purchaseResponseModel = new PurchaseResponseModel();
                    purchaseResponseModel.setStatus(false);
                    purchaseResponseModel.setResponseCode(500);
                    purchaseResponseModel.setDebugMessage(PanteaoApplication.getInstance().getResources().getString(R.string.something_went_wrong_at_our_end));
                    getPlans=false;
                    callback.subscriptionStatus(false,"No Subscription found");
                }
            });
        }catch (Exception ignored){
            getPlans=false;
            callback.subscriptionStatus(false,"No Subscription found");
        }


    }

    private void updatePayment(String billingError, String paymentStatus, String purchaseToken, String paymentId, String playstoreOrderId, Purchase purchase, PurchaseModel purchaseModel, String orderId, RestoreSubscriptionCallback callback) {
        try {
            JSONObject jsonObject=new JSONObject();
            JSONObject jsonObject1=new JSONObject();

            try {
                /* "purchasePrice": "20",    "purchaseCurrency": "INR"*/
                if (paymentStatus.equalsIgnoreCase("FAILED")){
                    jsonObject1.put("googleIAPPurchaseToken", "Could not connect to the play store");
                    jsonObject1.put("exception",billingError);
                }else {
                    jsonObject1.put("googleIAPPurchaseToken", purchaseToken);
                    jsonObject1.put("googleIAPPurchaseOrderId",playstoreOrderId);
                }

                if (purchaseModel!=null){
                    jsonObject1.put("purchasePrice", purchaseModel.getPrice());
                    jsonObject1.put("purchaseCurrency", purchaseModel.getCurrency());
                }else {
                    jsonObject1.put("purchasePrice", "");
                    jsonObject1.put("purchaseCurrency", "");
                }
                jsonObject.put("paymentStatus", paymentStatus);
                jsonObject.put("notes",jsonObject1);
            }catch (Exception ignored){

            }

            JsonParser jsonParser = new JsonParser();
            JsonObject gsonObject = (JsonObject)jsonParser.parse(jsonObject.toString());


            String initiateURL= SDKConfig.getInstance().getPAYMENT_BASE_URL()+"v1/order/"+orderId+"/";

            APIDetails endpoint = RequestConfig.paymentClient(KsPreferenceKeys.getInstance().getAppPrefAccessToken(),initiateURL).create(APIDetails.class);
            Call<PurchaseResponseModel> call = endpoint.updatePurchase(paymentId,gsonObject);
            call.enqueue(new Callback<PurchaseResponseModel>() {
                @Override
                public void onResponse(@NonNull Call<PurchaseResponseModel> call, @NonNull Response<PurchaseResponseModel> response) {

                    PurchaseResponseModel purchaseResponseModel = new PurchaseResponseModel();
                    if (response.code() == 200) {
                        purchaseResponseModel.setStatus(true);
                        purchaseResponseModel.setData(response.body().getData());
                        callback.subscriptionStatus(true,"Subscriptions Restored");
                        getPlans=false;
                    } else {
                        PurchaseResponseModel purchaseResponseModel2 = ErrorCodesIntercepter.getInstance().updateOrder(response);
                        getPlans=false;
                        callback.subscriptionStatus(false,"No Subscription found");
                    }


                }

                @Override
                public void onFailure(@NonNull Call<PurchaseResponseModel> call, @NonNull Throwable t) {
                    PurchaseResponseModel purchaseResponseModel = new PurchaseResponseModel();
                    purchaseResponseModel.setStatus(false);
                    purchaseResponseModel.setResponseCode(500);
                    purchaseResponseModel.setDebugMessage(PanteaoApplication.getInstance().getResources().getString(R.string.something_went_wrong_at_our_end));
                    //liveDataPurchaseResponse.postValue(purchaseResponseModel);
                    getPlans=false;
                    callback.subscriptionStatus(false,"No Subscription found");
                }
            });
        }catch (Exception ignored){
            getPlans=false;
            callback.subscriptionStatus(false,"No Subscription found");
        }

    }

}

