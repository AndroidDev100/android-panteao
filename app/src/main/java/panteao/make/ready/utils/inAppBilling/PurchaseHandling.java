package panteao.make.ready.utils.inAppBilling;

import android.util.Base64;
import android.util.Log;

import androidx.annotation.NonNull;

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

public class PurchaseHandling {

    private static PurchaseHandling projectRepository;
    BillingClient myBillingClient;
    boolean getPlans=false;
    public synchronized static PurchaseHandling getInstance() {
        if (projectRepository == null) {
            projectRepository = new PurchaseHandling();
        }
        return projectRepository;
    }

    public void checkPurchaseHistory(List<Purchase> purchases, BillingClient billingClient) {
        if (purchases!=null && purchases.size()>0){
            try {
                myBillingClient=billingClient;
                if (purchases.get(0).getPurchaseState()==Purchase.PurchaseState.PURCHASED){
                    JSONObject jsonObject=new JSONObject(purchases.get(0).getOriginalJson());
                    if (jsonObject.has("purchaseToken")){
                        Log.w("purchaseToken--2",jsonObject.getString("purchaseToken"));
                        Log.w("purchaseToken--2",purchases.get(0).getSku());
                        if (jsonObject.has("obfuscatedAccountId")){
                            Log.w("purchaseToken--2",jsonObject.getString("obfuscatedAccountId"));
                            Log.w("purchaseToken--2",KsPreferenceKeys.getInstance().getAppPrefUserId());
                            if (jsonObject.getString("obfuscatedAccountId").equalsIgnoreCase(KsPreferenceKeys.getInstance().getAppPrefUserId())){
                                Log.w("purchaseToken--2","conditionTrue");
                                callGetPlansAPI(purchases);
                            }
                        }else {
                            callGetPlansAPI(purchases);
                        }

                    }
                }

            }catch (Exception ignored){

            }
        }else {
            Log.w("purchaseToken--2","no purchase available");
        }
    }

    private void callGetPlansAPI(List<Purchase> purchases) {
        if (getPlans==true){
            return;
        }
        try {
            getPlans=true;
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
                        checkEntitlementState(purchaseResponseModel,purchases);
                    } else {
                        purchaseResponseModel.setStatus(false);
                        getPlans=false;
                    }

                }

                @Override
                public void onFailure(Call<ResponseMembershipAndPlan> call, Throwable t) {
                    ResponseMembershipAndPlan purchaseResponseModel = new ResponseMembershipAndPlan();
                    purchaseResponseModel.setStatus(false);
                    getPlans=false;
                    // liveDataPurchaseResponse.postValue(purchaseResponseModel);

                }
            });

        }catch (Exception ignored){
            getPlans=false;
        }


    }

    int count=0;
    private void checkEntitlementState(ResponseMembershipAndPlan purchaseResponseModel, List<Purchase> purchases) {
        try {
            if (purchaseResponseModel!=null && purchaseResponseModel.getData().size()>0 && purchaseResponseModel.isStatus()){
                for (int i = 0; i < purchaseResponseModel.getData().size(); i++) {
                    if (!purchaseResponseModel.getData().get(i).getEntitlementState()) {
                        Purchase purchase=purchases.get(count);
                        Log.w("identifiers",purchase.getSku());
                        Log.w("identifiers",purchaseResponseModel.getData().get(i).getIdentifier());
                        String s=purchaseResponseModel.getData().get(i).getIdentifier().replace("svod_","");
                        String initiateSKU=s.replace("_",".");
                        Log.w("identifiers",initiateSKU);
                        if (purchase.getSku().equalsIgnoreCase(initiateSKU)){
                            Log.w("identifiers",purchaseResponseModel.getData().get(i).getIdentifier());
                            Log.w("identifiers",purchase.getSku());
                            Log.w("identifiers","initiatePurchaseflow");
                            initiatePurchaseFlow(purchase,purchaseResponseModel.getData().get(i));
                            break;
                        }else {
                            getPlans=false;
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

    private void initiatePurchaseFlow(Purchase purchase, DataItem dataItem) {
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
                                callCreateNewPurchase(purchaseModel,purchase,dataItem);
                            }else {
                                getPlans=false;
                            }
                        }
                    });

        }catch (Exception ignored){
            Log.w("billingProcess",ignored.toString());
            getPlans=false;
        }
    }

    private void callCreateNewPurchase(PurchaseModel model, Purchase purchase, DataItem dataItem) {
        try {
            JSONObject jsonObject=new JSONObject();
            JSONObject jsonObject1=new JSONObject();

            try {
                if (model.getPurchaseOptions().contains(VodOfferType.PERPETUAL.name()) || model.getPurchaseOptions().contains(VodOfferType.RENTAL.name())){
                    jsonObject1.put("enveuSMSPlanName", model.getIdentifier());
                    jsonObject1.put("enveuSMSPlanTitle", model.getTitle());
                    jsonObject1.put("enveuSMSOfferType", model.getPurchaseOptions());
                    jsonObject1.put("enveuSMSPurchaseCurrency", "USD");
                    jsonObject1.put("enveuSMSOfferContentSKU", purchase.getSku());
                }else {
                    jsonObject1.put("enveuSMSPlanName", model.getIdentifier());
                    jsonObject1.put("enveuSMSPlanTitle", model.getTitle());
                    jsonObject1.put("enveuSMSSubscriptionOfferType", model.getPurchaseOptions());
                    jsonObject1.put("enveuSMSPurchaseCurrency", "USD");
                }


                jsonObject.put("notes",jsonObject1);
            }catch (Exception ignored){

            }

            JsonParser jsonParser = new JsonParser();
            JsonObject gsonObject = (JsonObject)jsonParser.parse(jsonObject.toString());


            String planName=model.getIdentifier()+"_PLAN/";
            String paymentURL= SDKConfig.getInstance().getPAYMENT_BASE_URL()+"v2/offer/"+planName;
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
                        callInitiatePaymentApi(orderId,purchase,dataItem,model);
                    } else {

                        PurchaseResponseModel purchaseResponseModel2 = ErrorCodesIntercepter.getInstance().createNewOrder(response);
                        getPlans=false;
                    }

                }

                @Override
                public void onFailure(@NonNull Call<PurchaseResponseModel> call, @NonNull Throwable t) {
                    PurchaseResponseModel purchaseResponseModel = new PurchaseResponseModel();
                    purchaseResponseModel.setStatus(false);
                    purchaseResponseModel.setResponseCode(500);
                    purchaseResponseModel.setDebugMessage(PanteaoApplication.getInstance().getResources().getString(R.string.something_went_wrong_at_our_end));
                    getPlans=false;
                }
            });
        }catch (Exception ignored){
            getPlans=false;
        }

    }

    private void callInitiatePaymentApi(String orderID, Purchase purchase, DataItem dataItem, PurchaseModel model) {

        try {
            JSONObject jsonObject=new JSONObject();
            try {
                jsonObject.put("paymentProvider","GOOGLE_IAP");
            }catch (Exception ignored){

            }

            JsonParser jsonParser = new JsonParser();
            JsonObject gsonObject = (JsonObject)jsonParser.parse(jsonObject.toString());

            String initiateURL= SDKConfig.getInstance().getPAYMENT_BASE_URL()+"v2/order/"+orderID+"/";

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
                        updatePayment("","PAYMENT_DONE",purchase.getPurchaseToken(), paymentId,purchase.getOrderId(),purchase,model,orderID);
                    } else {
                        PurchaseResponseModel purchaseResponseModel2 = ErrorCodesIntercepter.getInstance().initiateOrder(response);
                        getPlans=false;
                    }

                }

                @Override
                public void onFailure(@NonNull Call<PurchaseResponseModel> call, @NonNull Throwable t) {
                    PurchaseResponseModel purchaseResponseModel = new PurchaseResponseModel();
                    purchaseResponseModel.setStatus(false);
                    purchaseResponseModel.setResponseCode(500);
                    purchaseResponseModel.setDebugMessage(PanteaoApplication.getInstance().getResources().getString(R.string.something_went_wrong_at_our_end));
                    getPlans=false;
                }
            });
        }catch (Exception ignored){
            getPlans=false;
        }


    }

    private void updatePayment(String billingError, String paymentStatus, String purchaseToken, String paymentId, String playstoreOrderId, Purchase purchase, PurchaseModel purchaseModel, String orderId) {
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
                    jsonObject1.put("playStoreProductId", Base64.encodeToString(purchase.getSku().getBytes(),Base64.NO_WRAP));
                }

                if (purchaseModel!=null){
                    jsonObject1.put("purchasePrice", purchaseModel.getPrice());
                    jsonObject1.put("purchaseCurrency", "USD");
                }else {
                    jsonObject1.put("purchasePrice", "");
                    jsonObject1.put("purchaseCurrency", "");
                }
                jsonObject.put("paymentStatus", paymentStatus);
                jsonObject.put("notes",jsonObject1);
            }catch (Exception ignored){
                Log.w("purchasedSKU",paymentStatus);
            }

            JsonParser jsonParser = new JsonParser();
            JsonObject gsonObject = (JsonObject)jsonParser.parse(jsonObject.toString());


            String initiateURL= SDKConfig.getInstance().getPAYMENT_BASE_URL()+"v2/order/"+orderId+"/";

            APIDetails endpoint = RequestConfig.paymentClient(KsPreferenceKeys.getInstance().getAppPrefAccessToken(),initiateURL).create(APIDetails.class);
            Call<PurchaseResponseModel> call = endpoint.updatePurchase(paymentId,gsonObject);
            call.enqueue(new Callback<PurchaseResponseModel>() {
                @Override
                public void onResponse(@NonNull Call<PurchaseResponseModel> call, @NonNull Response<PurchaseResponseModel> response) {

                    PurchaseResponseModel purchaseResponseModel = new PurchaseResponseModel();
                    if (response.code() == 200) {
                        purchaseResponseModel.setStatus(true);
                        purchaseResponseModel.setData(response.body().getData());
                        getPlans=false;
                    } else {
                        PurchaseResponseModel purchaseResponseModel2 = ErrorCodesIntercepter.getInstance().updateOrder(response);
                        getPlans=false;
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
                }
            });
        }catch (Exception ignored){
            getPlans=false;
        }

    }

}