package panteao.make.ready.activities.membershipplans.ui;


import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.SystemClock;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.billingclient.api.BillingClient;
import com.android.billingclient.api.BillingResult;
import com.android.billingclient.api.Purchase;
import com.android.billingclient.api.SkuDetails;
import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import panteao.make.ready.activities.membershipplans.adapter.MembershipAdapter;
import panteao.make.ready.activities.purchase.ui.VodOfferType;
import panteao.make.ready.activities.purchase.ui.adapter.PurchaseShimmerAdapter;
import panteao.make.ready.activities.purchase.ui.viewmodel.PurchaseViewModel;
import panteao.make.ready.baseModels.BaseBindingActivity;
import panteao.make.ready.beanModel.cancelPurchase.ResponseCancelPurchase;
import panteao.make.ready.beanModel.membershipAndPlan.ResponseMembershipAndPlan;
import panteao.make.ready.beanModel.purchaseModel.PurchaseModel;
import panteao.make.ready.beanModel.purchaseModel.PurchaseResponseModel;
import panteao.make.ready.cms.HelpActivity;
import panteao.make.ready.fragments.dialog.AlertDialogFragment;
import panteao.make.ready.fragments.dialog.AlertDialogSingleButtonFragment;
import panteao.make.ready.R;
import panteao.make.ready.databinding.MembershipPlanBinding;
import panteao.make.ready.utils.commonMethods.AppCommonMethod;
import panteao.make.ready.utils.constants.AppConstants;
import panteao.make.ready.utils.cropImage.helpers.Logger;
import panteao.make.ready.utils.cropImage.helpers.PrintLogging;
import panteao.make.ready.utils.helpers.ImageHelper;
import panteao.make.ready.utils.helpers.NetworkConnectivity;

import panteao.make.ready.utils.helpers.StringUtils;
import com.google.gson.JsonObject;
import panteao.make.ready.utils.helpers.ToastHandler;
import panteao.make.ready.utils.helpers.ksPreferenceKeys.KsPreferenceKeys;
import panteao.make.ready.utils.inAppBilling.BillingProcessor;
import panteao.make.ready.utils.inAppBilling.InAppProcessListener;
import panteao.make.ready.utils.inAppBilling.PurchaseType;

import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Objects;

import panteao.make.ready.activities.purchase.ui.adapter.PurchaseShimmerAdapter;
import panteao.make.ready.activities.purchase.ui.viewmodel.PurchaseViewModel;
import panteao.make.ready.baseModels.BaseBindingActivity;
import panteao.make.ready.cms.HelpActivity;
import panteao.make.ready.utils.inAppBilling.BillingProcessor;
import panteao.make.ready.utils.inAppBilling.InAppProcessListener;
import panteao.make.ready.utils.inAppBilling.PurchaseType;

public class MemberShipPlanActivity extends BaseBindingActivity<MembershipPlanBinding> implements  MembershipAdapter.OnPurchaseItemClick, AlertDialogFragment.AlertDialogListener , InAppProcessListener {
    private static String selectedPurchaseOption;
    private static JsonObject jsonObj;
    PurchaseModel model;
    private BillingProcessor bp;
    private PurchaseViewModel viewModel;
    private KsPreferenceKeys preference;
    private MembershipAdapter adapterPurchase;
    private List<PurchaseModel> alPurchaseOptions;
    private List<String> subscribedPlanName;
    private List<PurchaseModel> playstorePurchaseOptions;
    private boolean isloggedout = false;
    private boolean isCardClickable = true;
    private String strToken;
    private long mLastClickTime = 0;
    private String localeCurrency;
    String billingError = "";

    @Override
    public MembershipPlanBinding inflateBindingLayout(@NonNull LayoutInflater inflater) {
        return MembershipPlanBinding.inflate(inflater);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        preference = KsPreferenceKeys.getInstance();
        strToken = preference.getAppPrefAccessToken();
        getBinding().playerLayout.setOnClickListener(v -> {
            createBottomSheet();
            /*Intent intent = new Intent(MemberShipPlanActivity.this, ContactActivity.class);
            startActivity(intent);*/
        });
        getBinding().contact.setOnClickListener(v -> {
            if (NetworkConnectivity.isOnline(MemberShipPlanActivity.this)) {
                createBottomSheet();
            }else {
                new ToastHandler(MemberShipPlanActivity.this).show(getResources().getString(R.string.no_connection));
            }
        });
        setClicks();
        callBinding();
    }

    private void setClicks() {
        getBinding().terms.setOnClickListener(v -> {
            if (NetworkConnectivity.isOnline(MemberShipPlanActivity.this)) {
                Objects.requireNonNull(this).startActivity(new Intent(this, HelpActivity.class).putExtra("type", "1"));
                // Objects.requireNonNull(this).startActivity(new Intent(this, SampleActivity.class).putExtra("type", "1").putExtra("url",getString(R.string.term_condition)));
            }else {
                new ToastHandler(MemberShipPlanActivity.this).show(getResources().getString(R.string.no_connection));
            }
        });
        getBinding().privacy.setOnClickListener(v -> {
            if (NetworkConnectivity.isOnline(MemberShipPlanActivity.this)) {
                Objects.requireNonNull(this).startActivity(new Intent(this, HelpActivity.class).putExtra("type", "2"));
                // Objects.requireNonNull(this).startActivity(new Intent(this, WebViewFlutterActivity.class).putExtra("type", "2").putExtra("url",getString(R.string.privacy_policy)));
            }else {
                new ToastHandler(MemberShipPlanActivity.this).show(getResources().getString(R.string.no_connection));
            }
        });
    }

    private void callBinding() {

        viewModel = ViewModelProviders.of(this).get(PurchaseViewModel.class);
        modelCall();
    }

    private void modelCall() {

        getBinding().toolbar.llSearchIcon.setVisibility(View.GONE);
        getBinding().toolbar.backLayout.setVisibility(View.VISIBLE);
        getBinding().toolbar.homeIcon.setVisibility(View.GONE);
        getBinding().toolbar.mediaRouteButton.setVisibility(View.GONE);
        getBinding().toolbar.titleText.setVisibility(View.VISIBLE);

        getBinding().toolbar.screenText.setText(getResources().getString(R.string.membership_plan));
        connectionObserver();

        getBinding().backLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });


    }

    private void connectionObserver() {
        if (NetworkConnectivity.isOnline(this)) {
            connectionValidation(true);
        } else {
            connectionValidation(false);
        }
    }

    private void connectionValidation(Boolean aBoolean) {
        if (aBoolean) {
            UIinitialization();
        } else {
            noConnectionLayout();
        }
    }

    public ResponseMembershipAndPlan planResponse;

    private void UIinitialization() {
        /*getBinding().llTop.setVisibility(View.VISIBLE);
        getBinding().noConnectionLayout.setVisibility(View.GONE);*/

        PurchaseShimmerAdapter shimmerAdapter = new PurchaseShimmerAdapter(this, false);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
        getBinding().rvPurchase.setLayoutManager(mLayoutManager);
        getBinding().rvPurchase.setItemAnimator(new DefaultItemAnimator());
        getBinding().rvPurchase.setAdapter(shimmerAdapter);
        String tempBase64 = "MIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEAk56pCLKhlJNSOVJo2dPCi4jwvmhxgS+lmFj5N/lc6SKbjH9D5vm/gRj7OgvSYN4sEWflKqZ3nD+eMfYYh8h679pzNHf8AJGxjyriZaaKprYXXsBTKRnOCEIQYzNMsZ4oLyr3sEjuR22fNb3sl2BZbM2sXK0sYFG05Dba9fHPIifYivqc5ci7QFiNJMDFL83Up4zz8jREwHPgeE6VAQvlnNn3NlSzZ1y6yx66pYN4pnqk2hzO/Wcp1ay7A5up+rU2OP4EtIeNBsfWPtZ40Bp9xEQUoeETt3+hSRMnQRlCxIyJK7AgypSAZHNHwrXi979UR7pi7NkDyNX3CTBxuP9NnwIDAQAB";

        bp = new BillingProcessor(MemberShipPlanActivity.this, this);
        bp.initializeBillingProcessor();


        getBinding().btnBuy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (NetworkConnectivity.isOnline(MemberShipPlanActivity.this)) {
                    if (SystemClock.elapsedRealtime() - mLastClickTime < 1000) {
                        return;
                    }
                    mLastClickTime = SystemClock.elapsedRealtime();
                    if (model!=null){
                        purchaseTVOD();
                    }
                }else {
                    new ToastHandler(MemberShipPlanActivity.this).show(getResources().getString(R.string.no_connection));
                }
            }
        });

        loadImage();
    }

    private void loadImage() {
        ImageHelper.getInstance(MemberShipPlanActivity.this).loadIAPImage(getBinding().bgImg);
    }

    private void purchaseTVOD() {
        showLoading(getBinding().progressBar, true);
        hitApiDoPurchase();

    }

    @Override
    public void onFinishDialog() {
        if (isloggedout) {
            forceLogout();
        }
    }

    public void forceLogout() {
        if (isloggedout) {
            isloggedout = false;
            hitApiLogout(MemberShipPlanActivity.this, preference.getAppPrefAccessToken());
        }

    }

    private void showDialog(String title, String message) {
        FragmentManager fm = getSupportFragmentManager();
        AlertDialogSingleButtonFragment alertDialog = AlertDialogSingleButtonFragment.newInstance(title, message, getResources().getString(R.string.ok));
        alertDialog.setCancelable(false);
        alertDialog.setAlertDialogCallBack(this);
        alertDialog.show(fm, "fragment_alert");
    }

    public void resetAdapter(List<PurchaseModel> list, List<String> selectedPlans) {
        isCardClickable = true;
        for (int i = 0; i < selectedPlans.size(); i++) {
            if (selectedPlans.get(i).equalsIgnoreCase("weekly Pack") || selectedPlans.get(i).equalsIgnoreCase("monthly") || selectedPlans.get(i).equalsIgnoreCase("daily"))
                isCardClickable = false;
        }
        if (!isCardClickable) {
            for (int i = 0; i < list.size(); i++) {
                if (list.get(i).getPurchaseOptions().equalsIgnoreCase("free"))
                    list.get(i).setSelected(false);
            }
        }

        if (!isCardClickable) {
            getBinding().issue.setVisibility(View.VISIBLE);
        }

        adapterPurchase = new MembershipAdapter(this, list, this, isCardClickable, localeCurrency);
        getBinding().rvPurchase.setAdapter(adapterPurchase);
    }

    private void noConnectionLayout() {
        /*getBinding().llTop.setVisibility(View.GONE);
        getBinding().noConnectionLayout.setVisibility(View.VISIBLE);
        getBinding().connection.retryTxt.setOnClickListener(view -> connectionObserver());*/
    }

    public void createBottomSheet() {

        BottomSheetDialog dialog;
        RecyclerView recyclerView;
        TextView email;
        TextView line;

        View view1 = getLayoutInflater().inflate(R.layout.bottom_sheet_contact, null);
        dialog = new BottomSheetDialog(this);
        dialog.setContentView(view1);
        email=view1.findViewById(R.id.email);
        line=view1.findViewById(R.id.line);
        FrameLayout bottomSheet = (FrameLayout) dialog.findViewById(com.google.android.material.R.id.design_bottom_sheet);
        BottomSheetBehavior.from(bottomSheet).setPeekHeight(BottomSheetBehavior.PEEK_HEIGHT_AUTO);
        BottomSheetBehavior.from(bottomSheet).setState(BottomSheetBehavior.STATE_EXPANDED);
        dialog.show();

        email.setOnClickListener(v -> {
      /*      Intent intentEmail= new Intent(Intent.ACTION_SENDTO);
            intentEmail.setData(Uri.parse("mailto:info@mvhub.com"));
            startActivity(intentEmail);*/

        });
        line.setOnClickListener(v -> {
            Intent intent = new Intent();
            try {
                intent = Intent.parseUri(AppConstants.LINE_URI, Intent.URI_INTENT_SCHEME);
            } catch (URISyntaxException e) {
                e.printStackTrace();
            }
            startActivity(intent);
        });

    }

    @Override
    public void onBillingInitialized() {
        try {
            boolean isOneTimePurchaseSupported = true;bp.isOneTimePurchaseSupported();
            if (!isOneTimePurchaseSupported) {
                Toast.makeText(this, "Your device doesn't support IN App Billing", Toast.LENGTH_LONG).show();
                getBinding().offerLayout.setVisibility(View.GONE);
                getBinding().noOfferLayout.setVisibility(View.VISIBLE);
                getBinding().bottomLay.setVisibility(View.GONE);
                return;
            }

            getPlansApi();

        } catch (NullPointerException e) {
            getBinding().progressBar.setVisibility(View.GONE);
        }


    }

    @Override
    public void onPurchasesUpdated(@NonNull BillingResult billingResult, @Nullable List<Purchase> purchases) {
        if (billingResult.getResponseCode() == BillingClient.BillingResponseCode.OK && purchases != null) {
            if (purchases.get(0).getPurchaseToken() != null) {
                processPurchase(purchases);

            } else {
                updatePayment("","FAILED","inapp:com.enveu.demo:android.test.purchased", paymentId);
            }
        }
    }

    private void processPurchase(List<Purchase> purchases) {
        try {
            for (Purchase purchase : purchases) {
                if (purchase.getPurchaseState() == Purchase.PurchaseState.PURCHASED) {
                    handlePurchase(purchase);
                } else if (purchase.getPurchaseState() == Purchase.PurchaseState.PENDING) {
                    PrintLogging.printLog("PurchaseActivity", "Received a pending purchase of SKU: " + purchase.getSku());
                    // handle pending purchases, e.g. confirm with users about the pending
                    // purchases, prompt them to complete it, etc.
                    // TODO: 8/24/2020 handle this in the next release.
                }
            }
        }catch (Exception ignored){

        }

    }

    private void handlePurchase(Purchase purchase) {
        try {
            updatePayment("","PAYMENT_DONE",purchase.getPurchaseToken(), paymentId);
        }catch (Exception ignored){

        }
    }


    @Override
    public void onBillingError(@Nullable BillingResult error) {
        try {
            if (error != null && error.getDebugMessage() != null) {
                billingError=error.getDebugMessage();
                Log.w("billingError", error.getDebugMessage());
            }
            updatePayment(billingError,"FAILED","",paymentId);

        }catch (Exception ignored){
            updatePayment(billingError,"FAILED","",paymentId);
        }

    }

    private void getPlansApi() {
        String token = preference.getAppPrefAccessToken();
        if (!StringUtils.isNullOrEmptyOrZero(token)) {
            viewModel.getNewPlans(token).observe(MemberShipPlanActivity.this, new Observer<ResponseMembershipAndPlan>() {
                @Override
                public void onChanged(@Nullable ResponseMembershipAndPlan responseMembershipAndPlan) {
                    Logger.e("MemberShipPlanActivity", "ResponseMembershipAndPlan" + responseMembershipAndPlan.toString());
                    if (responseMembershipAndPlan.isStatus()) {
                        if (responseMembershipAndPlan.getData().size() > 0) {
                            planResponse = responseMembershipAndPlan;
                            Logger.e("", "responseMembershipAndPlan" + responseMembershipAndPlan.getData().toString());
                            alPurchaseOptions = new ArrayList<>();
                            subscribedPlanName = new ArrayList<>();
                            subSkuList.clear();
                            for (int i = 0; i < responseMembershipAndPlan.getData().size(); i++) {
                                if (responseMembershipAndPlan.getData().get(i).getOfferType().contains(VodOfferType.RECURRING_SUBSCRIPTION​.name())) {
                                    String identifier=responseMembershipAndPlan.getData().get(i).getIdentifier();
                                    String s=identifier.replace("svod_","");
                                    String v=s.replace("_",".");
                                    callPlaystorePlans(v);
                                }
                            }
                            if (subSkuList!=null && subSkuList.size()>0){
                                bp.getAllSkuSubscriptionDetails(subSkuList);
                            }

                            //resetAdapter(alPurchaseOptions, subscribedPlanName);
                        }else {
                            getBinding().offerLayout.setVisibility(View.GONE);
                            getBinding().noOfferLayout.setVisibility(View.VISIBLE);
                            getBinding().bottomLay.setVisibility(View.GONE);
                        }

                    } else {
                        if (responseMembershipAndPlan.getResponseCode() == 401) {
                            isloggedout = true;
                            showDialog(MemberShipPlanActivity.this.getResources().getString(R.string.logged_out), getResources().getString(R.string.you_are_logged_out));
                        } else {
                            showDialog(MemberShipPlanActivity.this.getResources().getString(R.string.error), getResources().getString(R.string.something_went_wrong_at_our_end_please_try_later));
                        }
                    }

                }
            });

        } else {
            Logger.e("MemberShipPlanActivity", "Token is empty/null");
        }
    }

    List<String> subSkuList=new ArrayList<>();
    private void callPlaystorePlans(String sku) {
        if (bp!=null) {
            subSkuList.add(sku);
        }
    }

    @Override
    public void onListOfSKUFetched(@Nullable List<SkuDetails> purchases) {
        try {
            if (purchases!=null && purchases.size()>0){
                 for (int i = 0; i < planResponse.getData().size(); i++) {
                                if (planResponse.getData().get(i).getOfferType().contains(VodOfferType.RECURRING_SUBSCRIPTION​.name())) {

                                    PurchaseModel tempModel = new PurchaseModel();
                                    try {
                                        if (planResponse.getData().get(i).getDescription()!=null){
                                            tempModel.setDescription(planResponse.getData().get(i).getDescription().toString().trim());
                                        }else {
                                            tempModel.setDescription("");
                                        }
                                    }catch (Exception e){
                                        tempModel.setDescription("");
                                    }
                                   /* if (i==1){
                                        tempModel.setExpiryDate(1608778216918L);
                                    }else {
                                        tempModel.setExpiryDate(0L);
                                        tempModel.setDescription("");
                                    }*/

                                    tempModel.setExpiryDate(AppCommonMethod.getPlanExpiry(planResponse.getData().get(i)));

                                    createRecurringSubscriptions(tempModel, i, alPurchaseOptions, "", "", VodOfferType.RECURRING_SUBSCRIPTION​.name());
                                    if (planResponse.getData().get(i).getEntitlementState()) {
                                        tempModel.setSelected(true);
                                        isCardClickable = false;
                                        getBinding().btnBuy.setVisibility(View.INVISIBLE);
                                        subscribedPlanName.add(planResponse.getData().get(i).getTitle());
                                    } else {
                                        tempModel.setSelected(false);
                                    }

                                }
                                 alPurchaseOptions=getSortedList(alPurchaseOptions);
                            }

                            if (alPurchaseOptions.size() > 0) {
                                getBinding().offerLayout.setVisibility(View.VISIBLE);
                                getBinding().noOfferLayout.setVisibility(View.GONE);
                                adapterPurchase = new MembershipAdapter(getApplicationContext(), alPurchaseOptions, MemberShipPlanActivity.this, isCardClickable, localeCurrency);
                                getBinding().rvPurchase.setAdapter(adapterPurchase);
                            } else {
                                getBinding().offerLayout.setVisibility(View.GONE);
                                getBinding().noOfferLayout.setVisibility(View.VISIBLE);
                                getBinding().bottomLay.setVisibility(View.GONE);
                            }
            }

        }catch (Exception e){

        }
    }

    private List<PurchaseModel> getSortedList(List<PurchaseModel> data) {
        Collections.sort(data, new Comparator<PurchaseModel>(){
            public int compare(PurchaseModel obj1, PurchaseModel obj2) {
                // ## Ascending order
                return obj1.getPrice().compareToIgnoreCase(obj2.getPrice()); // To compare string values

            }
        });
        return data;
    }

    SkuDetails skuDetails;

    private void createRecurringSubscriptions(PurchaseModel purchaseModel, int i, List<PurchaseModel> alPurchaseOptions, String vodOfferType, String subscriptionOfferPeriod, String subscriptionType) {
        try {
            if (planResponse.getData().get(i).getRecurringOffer().getOfferPeriod().contains(VodOfferType.WEEKLY.name())) {
                try {
                    // skuDetails = bp.getSubscriptionListingDetails("monthly");
                    // skuDetails = bp.getSubscriptionListingDetails("vod_285ce97b_a26c_482b_b0b3_0777b411310c_tvod_price");
                    skuDetails = bp.getLocalSubscriptionSkuDetail(MemberShipPlanActivity.this,subSkuList.get(i));
                    purchaseModel.setPrice("" + skuDetails.getPrice());
                    purchaseModel.setPurchaseOptions(subscriptionType);
                    purchaseModel.setOfferPeriod(VodOfferType.WEEKLY.name());
                    purchaseModel.setTitle(planResponse.getData().get(i).getTitle());
                    purchaseModel.setIdentifier(planResponse.getData().get(i).getIdentifier());
                    purchaseModel.setCurrency(skuDetails.getPriceCurrencyCode());
                } catch (Exception e) {
                    // purchaseModel.setPrice("" + planResponse.getData().get(i).getPrice());
                    purchaseModel.setPurchaseOptions(subscriptionType);
                    purchaseModel.setOfferPeriod(VodOfferType.WEEKLY.name());
                    purchaseModel.setTitle(planResponse.getData().get(i).getTitle());
                    purchaseModel.setIdentifier(planResponse.getData().get(i).getIdentifier());
                    // purchaseModel.setCurrency(planResponse.getData().get(i).getCurrencyCode());

                }
                if (skuDetails != null)
                    alPurchaseOptions.add(purchaseModel);
            } else if (planResponse.getData().get(i).getRecurringOffer().getOfferPeriod().contains(VodOfferType.MONTHLY.name())) {
                try {
                    // skuDetails = bp.getSubscriptionListingDetails("monthly");
                    // skuDetails = bp.getSubscriptionListingDetails("svod_my_monthly_pack_with_trail");
                    skuDetails = bp.getLocalSubscriptionSkuDetail(MemberShipPlanActivity.this,subSkuList.get(i));
                    purchaseModel.setPrice("" + skuDetails.getPrice());
                    Log.w("priceofPlan",skuDetails.getPrice()+"");
                    purchaseModel.setPurchaseOptions(subscriptionType);
                    purchaseModel.setOfferPeriod(VodOfferType.MONTHLY.name());
                    purchaseModel.setTitle(planResponse.getData().get(i).getTitle());
                    purchaseModel.setIdentifier(planResponse.getData().get(i).getIdentifier());
                    purchaseModel.setCurrency(skuDetails.getPriceCurrencyCode());
                } catch (Exception e) {
                    // purchaseModel.setPrice("" + planResponse.getData().get(i).getPrice());
                    purchaseModel.setPurchaseOptions(subscriptionType);
                    purchaseModel.setOfferPeriod(VodOfferType.MONTHLY.name());
                    purchaseModel.setTitle(planResponse.getData().get(i).getTitle());
                    purchaseModel.setIdentifier(planResponse.getData().get(i).getIdentifier());
                    // purchaseModel.setCurrency(planResponse.getData().get(i).getCurrencyCode());

                }
                if (skuDetails != null)
                    alPurchaseOptions.add(purchaseModel);
            } else if (planResponse.getData().get(i).getRecurringOffer().getOfferPeriod().contains(VodOfferType.QUARTERLY.name())) {
                try {
                    // skuDetails = bp.getSubscriptionListingDetails("monthly");
                    //skuDetails = bp.getSubscriptionListingDetails("svod_my_quaterly_pack_recurring");
                    skuDetails = bp.getLocalSubscriptionSkuDetail(MemberShipPlanActivity.this,subSkuList.get(i));
                    purchaseModel.setPrice("" + skuDetails.getPrice());
                    purchaseModel.setPurchaseOptions(subscriptionType);
                    purchaseModel.setOfferPeriod(VodOfferType.QUARTERLY.name());
                    purchaseModel.setTitle(planResponse.getData().get(i).getTitle());
                    purchaseModel.setIdentifier(planResponse.getData().get(i).getIdentifier());
                    purchaseModel.setCurrency(skuDetails.getPriceCurrencyCode());
                } catch (Exception e) {
                    // purchaseModel.setPrice("" + planResponse.getData().get(i).getPrice());
                    purchaseModel.setPurchaseOptions(subscriptionType);
                    purchaseModel.setOfferPeriod(VodOfferType.QUARTERLY.name());
                    purchaseModel.setTitle(planResponse.getData().get(i).getTitle());
                    purchaseModel.setIdentifier(planResponse.getData().get(i).getIdentifier());
                    // purchaseModel.setCurrency(planResponse.getData().get(i).getCurrencyCode());

                }
                if (skuDetails != null)
                    alPurchaseOptions.add(purchaseModel);
            } else if (planResponse.getData().get(i).getRecurringOffer().getOfferPeriod().contains(VodOfferType.HALF_YEARLY.name())) {
                try {
                    // skuDetails = bp.getSubscriptionListingDetails("monthly");
                    skuDetails = bp.getLocalSubscriptionSkuDetail(MemberShipPlanActivity.this,subSkuList.get(i));
                    purchaseModel.setPrice("" + skuDetails.getPrice());
                    purchaseModel.setPurchaseOptions(subscriptionType);
                    purchaseModel.setOfferPeriod(VodOfferType.HALF_YEARLY.name());
                    purchaseModel.setTitle(planResponse.getData().get(i).getTitle());
                    purchaseModel.setIdentifier(planResponse.getData().get(i).getIdentifier());
                    purchaseModel.setCurrency(skuDetails.getPriceCurrencyCode());
                } catch (Exception e) {
                    // purchaseModel.setPrice("" + planResponse.getData().get(i).getPrice());
                    purchaseModel.setPurchaseOptions(subscriptionType);
                    purchaseModel.setOfferPeriod(VodOfferType.HALF_YEARLY.name());
                    purchaseModel.setTitle(planResponse.getData().get(i).getTitle());
                    purchaseModel.setIdentifier(planResponse.getData().get(i).getIdentifier());
                    // purchaseModel.setCurrency(planResponse.getData().get(i).getCurrencyCode());

                }
                if (skuDetails != null)
                    alPurchaseOptions.add(purchaseModel);
            } else if (planResponse.getData().get(i).getRecurringOffer().getOfferPeriod().contains(VodOfferType.ANNUAL.name())) {
                try {
                    // skuDetails = bp.getSubscriptionListingDetails("monthly");
                    skuDetails = bp.getLocalSubscriptionSkuDetail(MemberShipPlanActivity.this,subSkuList.get(i));
                    purchaseModel.setPrice("" + skuDetails.getPrice());
                    purchaseModel.setPurchaseOptions(subscriptionType);
                    purchaseModel.setOfferPeriod(VodOfferType.ANNUAL.name());
                    purchaseModel.setTitle(planResponse.getData().get(i).getTitle());
                    purchaseModel.setIdentifier(planResponse.getData().get(i).getIdentifier());
                    purchaseModel.setCurrency(skuDetails.getPriceCurrencyCode());
                } catch (Exception e) {
                    // purchaseModel.setPrice("" + planResponse.getData().get(i).getPrice());
                    purchaseModel.setPurchaseOptions(subscriptionType);
                    purchaseModel.setOfferPeriod(VodOfferType.ANNUAL.name());
                    purchaseModel.setTitle(planResponse.getData().get(i).getTitle());
                    purchaseModel.setIdentifier(planResponse.getData().get(i).getIdentifier());
                    // purchaseModel.setCurrency(planResponse.getData().get(i).getCurrencyCode());

                }
                if (skuDetails != null)
                    alPurchaseOptions.add(purchaseModel);
            }


        } catch (Exception ignored) {

        }
    }




    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (bp!=null && bp.isReady()) {
            bp.endConnection();
        }

    }


    private void updatePayment(String billinhError, String paymentStatus, String purchaseToken, String paymentId) {
        viewModel.updatePurchase(billingError, paymentStatus, strToken, purchaseToken, paymentId, orderId, model).observe(MemberShipPlanActivity.this, new Observer<PurchaseResponseModel>() {
            @Override
            public void onChanged(@Nullable PurchaseResponseModel responseCancelPurchase) {
                showLoading(getBinding().progressBar, false);
                if (responseCancelPurchase.getStatus()) {
                    if (responseCancelPurchase.getData().getOrderStatus() != null) {
                        if (responseCancelPurchase.getData().getOrderStatus().equalsIgnoreCase("COMPLETED")) {
                            AppCommonMethod.isPurchase = true;
                            Toast.makeText(MemberShipPlanActivity.this, MemberShipPlanActivity.this.getResources().getString(R.string.purchased_successfully), Toast.LENGTH_SHORT).show();
                            finish();
                        } else {
                            dismissLoading(getBinding().progressBar);
                            showDialog(MemberShipPlanActivity.this.getResources().getString(R.string.error), getResources().getString(R.string.payment_error) + " " + "support@panteao.com");
                        }

                    }
                    // hitApiDoPurchase();
                } else if (responseCancelPurchase.getResponseCode() == 4302) {
                    isloggedout = true;
                    dismissLoading(getBinding().progressBar);
                    showDialog(MemberShipPlanActivity.this.getResources().getString(R.string.logged_out), responseCancelPurchase.getDebugMessage() == null ? "" : responseCancelPurchase.getDebugMessage().toString());

                } else if (responseCancelPurchase.getResponseCode() == 4011) {
                    dismissLoading(getBinding().progressBar);
                } else {
                    dismissLoading(getBinding().progressBar);
                    showDialog(MemberShipPlanActivity.this.getResources().getString(R.string.error), responseCancelPurchase.getDebugMessage() == null ? "" : responseCancelPurchase.getDebugMessage().toString());
                }

            }
        });

    }


    public void hitCancelSubscription() {

        viewModel.cancelPlan(strToken).observe(MemberShipPlanActivity.this, new Observer<ResponseCancelPurchase>() {
            @Override
            public void onChanged(@Nullable ResponseCancelPurchase responseCancelPurchase) {
                if (responseCancelPurchase.isStatus()) {
                    hitApiDoPurchase();
                } else {
                    if (responseCancelPurchase.getResponseCode() == 401) {
                        isloggedout = true;
                        showDialog(MemberShipPlanActivity.this.getResources().getString(R.string.logged_out), getResources().getString(R.string.you_are_logged_out));
                    }
                }
            }
        });


    }

    String orderId;
    String assetSKU;

    public void hitApiDoPurchase() {
        viewModel.createNewPurchaseRequest(strToken, jsonObj, model, "").observe(MemberShipPlanActivity.this, purchaseResponseModel -> {

            if (purchaseResponseModel.getStatus()) {
                if (purchaseResponseModel.getData().getOrderId() != null && !purchaseResponseModel.getData().getOrderId().equalsIgnoreCase("")) {
                    orderId = purchaseResponseModel.getData().getOrderId();
                    Log.w("orderIdOf", orderId);
                    showLoading(getBinding().progressBar, false);
                    callInitiatePaymentApi(orderId);
                }
            } else if (purchaseResponseModel.getResponseCode() == 4302) {
                isloggedout = true;
                dismissLoading(getBinding().progressBar);
                showDialog(MemberShipPlanActivity.this.getResources().getString(R.string.logged_out), purchaseResponseModel.getDebugMessage() == null ? "" : purchaseResponseModel.getDebugMessage().toString());

            } else {
                dismissLoading(getBinding().progressBar);
                showDialog(MemberShipPlanActivity.this.getResources().getString(R.string.error), purchaseResponseModel.getDebugMessage() == null ? "" : purchaseResponseModel.getDebugMessage().toString());
            }
        });

    }


    String initiateSKU="";
    @Override
    public void onPurchaseCardClick(boolean click, int poss, String planName, PurchaseModel purchaseModel) {
        if (isCardClickable) {
            String idSKU=purchaseModel.getIdentifier();
            String s=idSKU.replace("svod_","");
            initiateSKU=s.replace("_",".");
            model = purchaseModel;
            selectedPurchaseOption = planName;
            final int sdk = android.os.Build.VERSION.SDK_INT;
            if (sdk < android.os.Build.VERSION_CODES.JELLY_BEAN) {
                getBinding().btnBuy.setBackgroundDrawable(ContextCompat.getDrawable(this, R.drawable.rounded_button));
            } else {
                getBinding().btnBuy.setBackground(ContextCompat.getDrawable(this, R.drawable.rounded_button));
            }
            getBinding().btnBuy.setEnabled(true);
            getBinding().btnBuy.setClickable(true);

        } else {
            getBinding().btnBuy.setBackgroundColor(getApplicationContext().getResources().getColor(R.color.disable_button_color));
            getBinding().btnBuy.setEnabled(false);
            getBinding().btnBuy.setClickable(false);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    String paymentId;

    private void callInitiatePaymentApi(String orderId) {
        viewModel.callInitiatePaymet(strToken, orderId).observe(MemberShipPlanActivity.this, purchaseResponseModel -> {
            try {
                if (purchaseResponseModel.getStatus()) {
                    if (purchaseResponseModel.getData().getPaymentId() != null && !purchaseResponseModel.getData().getPaymentId().toString().equalsIgnoreCase("")) {
                        paymentId = purchaseResponseModel.getData().getPaymentId().toString();
                        Log.w("orderIdOf", paymentId);
                        //updatePayment("sample_token",paymentId);
                        buySubscription();
                    }

                    // hitInitiatePayment();product
                    // finish();
                    //  new ActivityLauncher(PurchaseActivity.this).detailScreen(PurchaseActivity.this, DetailActivity.class, assetId, "0", true);
                } else if (purchaseResponseModel.getResponseCode() == 4302) {
                    isloggedout = true;
                    dismissLoading(getBinding().progressBar);
                    showDialog(MemberShipPlanActivity.this.getResources().getString(R.string.logged_out), purchaseResponseModel.getDebugMessage() == null ? "" : purchaseResponseModel.getDebugMessage().toString());

                } else {
                    dismissLoading(getBinding().progressBar);
                    showDialog(MemberShipPlanActivity.this.getResources().getString(R.string.error), purchaseResponseModel.getDebugMessage() == null ? "" : purchaseResponseModel.getDebugMessage().toString());
                }
            } catch (Exception e) {

            }


        });

    }

    public void buySubscription() {
        if (!StringUtils.isNullOrEmptyOrZero(model.getPurchaseOptions())) {
            showHideProgress(getBinding().progressBar);

            if (model.getPurchaseOptions().equalsIgnoreCase(VodOfferType.RECURRING_SUBSCRIPTION​.name())) {
                if (model.isSelected()) {
                   // bp.subscribe(MemberShipPlanActivity.this, model.getIdentifier(), "DEVELOPER PAYLOAD HERE");
                    bp.purchase(MemberShipPlanActivity.this, initiateSKU, "DEVELOPER PAYLOAD", PurchaseType.SUBSCRIPTION.name());
                } else {
                    Toast.makeText(MemberShipPlanActivity.this, getResources().getString(R.string.already_subscriber), Toast.LENGTH_SHORT).show();
                }
            }

        }
    }


}

