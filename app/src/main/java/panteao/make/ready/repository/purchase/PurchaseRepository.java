package panteao.make.ready.repository.purchase;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import panteao.make.ready.PanteaoApplication;
import panteao.make.ready.R;
import panteao.make.ready.SDKConfig;
import panteao.make.ready.beanModel.cancelPurchase.ResponseCancelPurchase;
import panteao.make.ready.beanModel.membershipAndPlan.ResponseMembershipAndPlan;
import panteao.make.ready.beanModel.purchaseModel.PurchaseModel;
import panteao.make.ready.beanModel.purchaseModel.PurchaseResponseModel;
import panteao.make.ready.networking.detailPlayer.APIDetails;
import panteao.make.ready.activities.purchase.ui.VodOfferType;
import panteao.make.ready.networking.apiendpoints.RequestConfig;
import panteao.make.ready.networking.intercepter.ErrorCodesIntercepter;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import org.json.JSONObject;

import java.util.Objects;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class PurchaseRepository {

    private static PurchaseRepository purchaseRepository;

    private PurchaseRepository() {
    }

    public static synchronized PurchaseRepository getInstance() {
        if (purchaseRepository == null) {
            purchaseRepository = new PurchaseRepository();
        }

        return purchaseRepository;
    }

    public LiveData<PurchaseResponseModel> createNewPurchaseRequest(String token, JsonObject data, PurchaseModel model,String sku) {

        MutableLiveData<PurchaseResponseModel> liveDataPurchaseResponse = new MutableLiveData<>();

        JSONObject jsonObject=new JSONObject();
        JSONObject jsonObject1=new JSONObject();

        try {
            if (model.getPurchaseOptions().contains(VodOfferType.PERPETUAL.name()) || model.getPurchaseOptions().contains(VodOfferType.RENTAL.name())){
                jsonObject1.put("enveuSMSPlanName", model.getIdentifier());
                jsonObject1.put("enveuSMSPlanTitle", model.getTitle());
                jsonObject1.put("enveuSMSOfferType", model.getPurchaseOptions());
                jsonObject1.put("enveuSMSPurchaseCurrency", "USD");
                jsonObject1.put("enveuSMSOfferContentSKU", sku);
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
        String paymentURL=SDKConfig.getInstance().getPAYMENT_BASE_URL()+"v2/offer/"+planName;
        APIDetails endpoint = RequestConfig.paymentClient(token,paymentURL).create(APIDetails.class);
        Call<PurchaseResponseModel> call = endpoint.getCreateNewPurchase(gsonObject);
        call.enqueue(new Callback<PurchaseResponseModel>() {
            @Override
            public void onResponse(@NonNull Call<PurchaseResponseModel> call, @NonNull Response<PurchaseResponseModel> response) {

                PurchaseResponseModel purchaseResponseModel = new PurchaseResponseModel();
                if (response.code() == 200) {
                    purchaseResponseModel.setStatus(true);
                    purchaseResponseModel.setData(response.body().getData());
                    liveDataPurchaseResponse.postValue(purchaseResponseModel);
                } else {

                    PurchaseResponseModel purchaseResponseModel2 = ErrorCodesIntercepter.getInstance().createNewOrder(response);
                    liveDataPurchaseResponse.postValue(purchaseResponseModel2);

                }


            }

            @Override
            public void onFailure(@NonNull Call<PurchaseResponseModel> call, @NonNull Throwable t) {
                PurchaseResponseModel purchaseResponseModel = new PurchaseResponseModel();
                purchaseResponseModel.setStatus(false);
                purchaseResponseModel.setResponseCode(500);
                purchaseResponseModel.setDebugMessage(PanteaoApplication.getInstance().getResources().getString(R.string.something_went_wrong_at_our_end));
                liveDataPurchaseResponse.postValue(purchaseResponseModel);
            }
        });

        return liveDataPurchaseResponse;
    }

    public LiveData<PurchaseResponseModel> callInitiatePaymet(String token, String orderID) {

        MutableLiveData<PurchaseResponseModel> liveDataPurchaseResponse = new MutableLiveData<>();
        JSONObject jsonObject=new JSONObject();


        try {
            jsonObject.put("paymentProvider","GOOGLE_IAP");
        }catch (Exception ignored){

        }

        JsonParser jsonParser = new JsonParser();
        JsonObject gsonObject = (JsonObject)jsonParser.parse(jsonObject.toString());

        String initiateURL= SDKConfig.getInstance().getPAYMENT_BASE_URL()+"v2/order/"+orderID+"/";

        APIDetails endpoint = RequestConfig.paymentClient(token,initiateURL).create(APIDetails.class);
        Call<PurchaseResponseModel> call = endpoint.initiatePurchase(gsonObject);
        call.enqueue(new Callback<PurchaseResponseModel>() {
            @Override
            public void onResponse(@NonNull Call<PurchaseResponseModel> call, @NonNull Response<PurchaseResponseModel> response) {

                PurchaseResponseModel purchaseResponseModel = new PurchaseResponseModel();
                if (response.code() == 200) {
                    purchaseResponseModel.setStatus(true);
                    purchaseResponseModel.setData(response.body().getData());
                    liveDataPurchaseResponse.postValue(purchaseResponseModel);
                } else {
                    PurchaseResponseModel purchaseResponseModel2 = ErrorCodesIntercepter.getInstance().initiateOrder(response);
                    liveDataPurchaseResponse.postValue(purchaseResponseModel2);
                }


            }

            @Override
            public void onFailure(@NonNull Call<PurchaseResponseModel> call, @NonNull Throwable t) {
                PurchaseResponseModel purchaseResponseModel = new PurchaseResponseModel();
                purchaseResponseModel.setStatus(false);
                purchaseResponseModel.setResponseCode(500);
                purchaseResponseModel.setDebugMessage(PanteaoApplication.getInstance().getResources().getString(R.string.something_went_wrong_at_our_end));
                liveDataPurchaseResponse.postValue(purchaseResponseModel);
            }
        });

        return liveDataPurchaseResponse;
    }



    public LiveData<ResponseMembershipAndPlan> getPlans(String token) {

        MutableLiveData<ResponseMembershipAndPlan> liveDataPurchaseResponse = new MutableLiveData<>();
        APIDetails endpoint = RequestConfig.getUserInteration(token).create(APIDetails.class);
        Call<ResponseMembershipAndPlan> call = endpoint.getPlans("RECURRING_SUBSCRIPTION",true);
        call.enqueue(new Callback<ResponseMembershipAndPlan>() {
            @Override
            public void onResponse(Call<ResponseMembershipAndPlan> call, Response<ResponseMembershipAndPlan> response) {


                ResponseMembershipAndPlan purchaseResponseModel = new ResponseMembershipAndPlan();
                if (response.code() == 200) {
                    purchaseResponseModel.setStatus(true);
   //                 purchaseResponseModel.setDebugMessage(response.body().getDebugMessage());
                    purchaseResponseModel.setData(response.body().getData());
                    liveDataPurchaseResponse.postValue(purchaseResponseModel);
                } else {
                    purchaseResponseModel.setStatus(false);
                    liveDataPurchaseResponse.postValue(purchaseResponseModel);
                }

            }

            @Override
            public void onFailure(Call<ResponseMembershipAndPlan> call, Throwable t) {
                ResponseMembershipAndPlan purchaseResponseModel = new ResponseMembershipAndPlan();
                purchaseResponseModel.setStatus(false);
                liveDataPurchaseResponse.postValue(purchaseResponseModel);

            }
        });


        return liveDataPurchaseResponse;
    }

    public LiveData<ResponseMembershipAndPlan> getNewPlans(String token) {

        MutableLiveData<ResponseMembershipAndPlan> liveDataPurchaseResponse = new MutableLiveData<>();
        APIDetails endpoint = RequestConfig.getUserInteration(token).create(APIDetails.class);
        Call<ResponseMembershipAndPlan> call = endpoint.getPlans("RECURRING_SUBSCRIPTION",true);
        call.enqueue(new Callback<ResponseMembershipAndPlan>() {
            @Override
            public void onResponse(Call<ResponseMembershipAndPlan> call, Response<ResponseMembershipAndPlan> response) {


                ResponseMembershipAndPlan purchaseResponseModel = new ResponseMembershipAndPlan();
                if (response.code() == 200) {
                    purchaseResponseModel.setStatus(true);
                  //purchaseResponseModel.setDebugMessage(response.body().getDebugMessage());
                    purchaseResponseModel.setData(response.body().getData());
                    liveDataPurchaseResponse.postValue(purchaseResponseModel);
                } else {
                    purchaseResponseModel.setStatus(false);
                    liveDataPurchaseResponse.postValue(purchaseResponseModel);
                }

            }

            @Override
            public void onFailure(Call<ResponseMembershipAndPlan> call, Throwable t) {
                ResponseMembershipAndPlan purchaseResponseModel = new ResponseMembershipAndPlan();
                purchaseResponseModel.setStatus(false);
                liveDataPurchaseResponse.postValue(purchaseResponseModel);

            }
        });


        return liveDataPurchaseResponse;
    }



    public LiveData<ResponseCancelPurchase> cancelPlan(String token) {

        MutableLiveData<ResponseCancelPurchase> liveDataPurchaseResponse = new MutableLiveData<>();
        APIDetails endpoint = RequestConfig.getClientInterceptor(token).create(APIDetails.class);
        Call<ResponseCancelPurchase> call = endpoint.cancelPurchase();

        call.enqueue(new Callback<ResponseCancelPurchase>() {
            @Override
            public void onResponse(Call<ResponseCancelPurchase> call, Response<ResponseCancelPurchase> response) {

                ResponseCancelPurchase model = new ResponseCancelPurchase();
                if (response.code() == 200) {

                    model.setStatus(true);
                    model.setData(response.body().getData());
                    liveDataPurchaseResponse.postValue(model);
                } else {
                    model.setStatus(false);
                    if (response.code() == 500) {
                        model.setResponseCode(500);

                    } else {
                        model.setResponseCode(Objects.requireNonNull(response.body().getResponseCode()));
                    }

                    liveDataPurchaseResponse.postValue(model);
                }

            }

            @Override
            public void onFailure(Call<ResponseCancelPurchase> call, Throwable t) {
                ResponseCancelPurchase model = new ResponseCancelPurchase();
                model.setStatus(false);
                model.setResponseCode(600);
                liveDataPurchaseResponse.postValue(model);

            }
        });


        return liveDataPurchaseResponse;
    }

    public LiveData<PurchaseResponseModel> updatePurchase(String billingError,String paymentStatus,String token,String purchaseToken,String paymentId,
                                                          String orderId,PurchaseModel purchaseModel,String purchasedSKU) {

        MutableLiveData<PurchaseResponseModel> liveDataPurchaseResponse = new MutableLiveData<>();


        /*{
            "googleIAPPurchaseToken": "Could not connect to the play store",
  	"exception": "exceptionn goes here!"
        }
        }*/

        JSONObject jsonObject=new JSONObject();
        JSONObject jsonObject1=new JSONObject();

        try {
           /* "purchasePrice": "20",    "purchaseCurrency": "INR"*/
            if (paymentStatus.equalsIgnoreCase("FAILED")){
                jsonObject1.put("googleIAPPurchaseToken", "Could not connect to the play store");
                jsonObject1.put("exception",billingError);
            }else {
                jsonObject1.put("googleIAPPurchaseToken", purchaseToken);
                jsonObject1.put("playStoreProductId",purchasedSKU);
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

        }

        JsonParser jsonParser = new JsonParser();
        JsonObject gsonObject = (JsonObject)jsonParser.parse(jsonObject.toString());


        String initiateURL= SDKConfig.getInstance().getPAYMENT_BASE_URL()+"v2/order/"+orderId+"/";

        APIDetails endpoint = RequestConfig.paymentClient(token,initiateURL).create(APIDetails.class);
        Call<PurchaseResponseModel> call = endpoint.updatePurchase(paymentId,gsonObject);
        call.enqueue(new Callback<PurchaseResponseModel>() {
            @Override
            public void onResponse(@NonNull Call<PurchaseResponseModel> call, @NonNull Response<PurchaseResponseModel> response) {

                PurchaseResponseModel purchaseResponseModel = new PurchaseResponseModel();
                if (response.code() == 200) {
                    purchaseResponseModel.setStatus(true);
                    purchaseResponseModel.setData(response.body().getData());
                    liveDataPurchaseResponse.postValue(purchaseResponseModel);
                } else {
                    PurchaseResponseModel purchaseResponseModel2 = ErrorCodesIntercepter.getInstance().updateOrder(response);
                    liveDataPurchaseResponse.postValue(purchaseResponseModel2);
                }


            }

            @Override
            public void onFailure(@NonNull Call<PurchaseResponseModel> call, @NonNull Throwable t) {
                PurchaseResponseModel purchaseResponseModel = new PurchaseResponseModel();
                purchaseResponseModel.setStatus(false);
                purchaseResponseModel.setResponseCode(500);
                purchaseResponseModel.setDebugMessage(PanteaoApplication.getInstance().getResources().getString(R.string.something_went_wrong_at_our_end));
                liveDataPurchaseResponse.postValue(purchaseResponseModel);
            }
        });

        return liveDataPurchaseResponse;
    }



}
