package panteao.make.ready.activities.purchase.planslayer;

import panteao.make.ready.activities.purchase.callBack.EntitlementStatus;
import panteao.make.ready.beanModel.membershipAndPlan.ResponseMembershipAndPlan;
import panteao.make.ready.networking.apiendpoints.RequestConfig;
import panteao.make.ready.networking.detailPlayer.APIDetails;
import panteao.make.ready.utils.helpers.ksPreferenceKeys.KsPreferenceKeys;

import panteao.make.ready.activities.purchase.callBack.EntitlementStatus;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class GetPlansLayer {

    private static GetPlansLayer plansLayerInstance;

    private GetPlansLayer() {

    }

    public static GetPlansLayer getInstance() {
        if (plansLayerInstance == null) {
            plansLayerInstance = new GetPlansLayer();
        }
        return (plansLayerInstance);
    }

    public void getEntitlementStatus(KsPreferenceKeys preferenceKeys, String token, EntitlementStatus callBack) {
        try {

            APIDetails endpoint = RequestConfig.getUserInteration(token).create(APIDetails.class);
            Call<ResponseMembershipAndPlan> call = endpoint.getPlans("RECURRING_SUBSCRIPTION",true);
            call.enqueue(new Callback<ResponseMembershipAndPlan>() {
                @Override
                public void onResponse(Call<ResponseMembershipAndPlan> call, Response<ResponseMembershipAndPlan> response) {

                    boolean entitlementState=false;
                    ResponseMembershipAndPlan purchaseResponseModel = new ResponseMembershipAndPlan();
                    if (response.code() == 200) {
                        purchaseResponseModel.setStatus(true);
//                    purchaseResponseModel.setDebugMessage(response.body().getDebugMessage());
                        purchaseResponseModel.setData(response.body().getData());
                        if (purchaseResponseModel.getData().size()>0){
                            for (int i = 0; i < purchaseResponseModel.getData().size(); i++) {
                                if (purchaseResponseModel.getData().get(i).getEntitlementState()) {
                                    entitlementState=true;
                                    preferenceKeys.setEntitlementState(true);
                                    callBack.entitlementStatus(true,true);
                                    break;
                                }else {
                                    entitlementState=false;
                                }
                            }

                            if (!entitlementState){
                                preferenceKeys.setEntitlementState(false);
                                callBack.entitlementStatus(false,false);

                            }
                        }else {
                            preferenceKeys.setEntitlementState(false);
                            callBack.entitlementStatus(false,false);
                        }
                    } else {
                        purchaseResponseModel.setStatus(false);
                        preferenceKeys.setEntitlementState(false);
                        callBack.entitlementStatus(false,false);


                    }

                }

                @Override
                public void onFailure(Call<ResponseMembershipAndPlan> call, Throwable t) {
                    ResponseMembershipAndPlan purchaseResponseModel = new ResponseMembershipAndPlan();
                    purchaseResponseModel.setStatus(false);
                    preferenceKeys.setEntitlementState(false);
                    callBack.entitlementStatus(false,false);


                }
            });

        }catch (Exception e){
            callBack.entitlementStatus(false,false);
            preferenceKeys.setEntitlementState(false);
        }

    }
}
