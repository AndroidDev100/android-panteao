package panteao.make.ready.activities.purchase.ui;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.SystemClock;
import android.util.Base64;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.billingclient.api.BillingClient;
import com.android.billingclient.api.BillingResult;
import com.android.billingclient.api.Purchase;
import com.android.billingclient.api.SkuDetails;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.google.android.material.bottomsheet.BottomSheetDialog;

import panteao.make.ready.activities.membershipplans.ui.MemberShipPlanActivity;
import panteao.make.ready.activities.purchase.ui.adapter.PurchaseAdapter;
import panteao.make.ready.activities.purchase.ui.adapter.PurchaseShimmerAdapter;
import panteao.make.ready.activities.purchase.ui.viewmodel.PurchaseViewModel;
import panteao.make.ready.activities.show.ui.EpisodeActivity;
import panteao.make.ready.activities.usermanagment.ui.LoginActivity;
import panteao.make.ready.baseModels.BaseBindingActivity;
import panteao.make.ready.beanModel.cancelPurchase.ResponseCancelPurchase;
import panteao.make.ready.beanModel.entitle.EntitledAs;
import panteao.make.ready.beanModel.entitle.ResponseEntitle;
import panteao.make.ready.beanModel.membershipAndPlan.ResponseMembershipAndPlan;
import panteao.make.ready.beanModel.purchaseModel.PurchaseModel;
import panteao.make.ready.beanModel.purchaseModel.PurchaseResponseModel;
import panteao.make.ready.cms.HelpActivity;
import panteao.make.ready.fragments.dialog.AlertDialogFragment;
import panteao.make.ready.fragments.dialog.AlertDialogSingleButtonFragment;
import panteao.make.ready.R;
import panteao.make.ready.beanModelV3.uiConnectorModelV2.EnveuVideoItemBean;
import panteao.make.ready.databinding.PurchaseBinding;
import panteao.make.ready.utils.MediaTypeConstants;
import panteao.make.ready.utils.commonMethods.AppCommonMethod;
import panteao.make.ready.utils.constants.AppConstants;
import panteao.make.ready.utils.cropImage.helpers.Logger;

import panteao.make.ready.utils.cropImage.helpers.PrintLogging;
import panteao.make.ready.utils.helpers.ActivityTrackers;
import panteao.make.ready.utils.helpers.CheckInternetConnection;
import panteao.make.ready.utils.helpers.ImageHelper;
import panteao.make.ready.utils.helpers.NetworkConnectivity;
import panteao.make.ready.utils.helpers.StringUtils;
import com.google.gson.JsonObject;
import panteao.make.ready.utils.helpers.ToastHandler;
import panteao.make.ready.utils.helpers.intentlaunchers.ActivityLauncher;
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


public class PurchaseActivity extends BaseBindingActivity<PurchaseBinding> implements AlertDialogFragment.AlertDialogListener, PurchaseAdapter.OnPurchaseItemClick, InAppProcessListener {


    private static List<PurchaseModel> alPurchaseOptions;
    public boolean isAlreadySubscribed = false;
    private EnveuVideoItemBean response;
    private ResponseEntitle responseEntitlementModel;
    private PurchaseAdapter adapterPurchase;
    private BillingProcessor bp;
    private PurchaseViewModel viewModel;
    private PurchaseModel purchaseModel;
    private KsPreferenceKeys preference;
    private String strToken;
    private String purchaseId;
    private String selectedPlanName;
    private String subscribedPlanName;
    private long mLastClickTime = 0;
    private String contentType;
    private int assetId;
    private boolean isloggedout = false;
    private boolean isTVOD = false;
    private boolean isOnFreePlan = false;
    private JsonObject jsonObj;

    @Override
    public PurchaseBinding inflateBindingLayout(@NonNull LayoutInflater inflater) {
        return PurchaseBinding.inflate(inflater);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        callBinding();
    }

    private void callBinding() {
        boolean isAvailable = true;//BillingProcessor.isIabServiceAvailable(this);
        if (!isAvailable) {
            Toast.makeText(this, "Your device doesn't support IN App Billing", Toast.LENGTH_LONG).show();
            getBinding().bottomLay.setVisibility(View.GONE);
            getBinding().offerLayout.setVisibility(View.GONE);
            getBinding().noOfferLayout.setVisibility(View.VISIBLE);
            return;
        }
        //  String tempBase64 = "MIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEAk56pCLKhlJNSOVJo2dPCi4jwvmhxgS+lmFj5N/lc6SKbjH9D5vm/gRj7OgvSYN4sEWflKqZ3nD+eMfYYh8h679pzNHf8AJGxjyriZaaKprYXXsBTKRnOCEIQYzNMsZ4oLyr3sEjuR22fNb3sl2BZbM2sXK0sYFG05Dba9fHPIifYivqc5ci7QFiNJMDFL83Up4zz8jREwHPgeE6VAQvlnNn3NlSzZ1y6yx66pYN4pnqk2hzO/Wcp1ay7A5up+rU2OP4EtIeNBsfWPtZ40Bp9xEQUoeETt3+hSRMnQRlCxIyJK7AgypSAZHNHwrXi979UR7pi7NkDyNX3CTBxuP9NnwIDAQAB";

        bp = new BillingProcessor(PurchaseActivity.this,this);
        bp.initializeBillingProcessor();


        viewModel = new ViewModelProvider(this).get(PurchaseViewModel.class);
        AppCommonMethod.isPurchase = false;
        isAlreadySubscribed = false;
        isOnFreePlan = false;
        isTVOD = false;
        modelCall();
        PurchaseShimmerAdapter shimmerAdapter = new PurchaseShimmerAdapter(this, true);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
        getBinding().rvPurchase.setLayoutManager(mLayoutManager);
        getBinding().rvPurchase.setItemAnimator(new DefaultItemAnimator());
        getBinding().rvPurchase.setAdapter(shimmerAdapter);

    }

    private void modelCall() {
        getBinding().toolbar.llSearchIcon.setVisibility(View.GONE);
        getBinding().toolbar.backLayout.setVisibility(View.VISIBLE);
        getBinding().toolbar.homeIcon.setVisibility(View.GONE);
        getBinding().toolbar.titleText.setVisibility(View.VISIBLE);
        getBinding().toolbar.mediaRouteButton.setVisibility(View.GONE);
        getBinding().toolbar.backLayout.setOnClickListener(view -> onBackPressed());

        // getBinding().contactus.setOnClickListener(view -> PurchaseActivity.this.startActivity(new Intent(getApplicationContext(), ContactUsActivity.class)));


        if (getIntent().hasExtra("response")) {
            response = (EnveuVideoItemBean) getIntent().getParcelableExtra("response");
            responseEntitlementModel = (ResponseEntitle) getIntent().getSerializableExtra("responseEntitlement");
            assetId = getIntent().getIntExtra("assestId", 0);
            contentType = getIntent().getStringExtra("contentType");
        }

       /* if (!StringUtils.isNullOrEmptyOrZero(response.getSku()))
            purchaseId = convertPlayStoreSKU(response.getSku());*/

        preference = KsPreferenceKeys.getInstance();
        preference.setAppPrefGotoPurchase(false);
        strToken = preference.getAppPrefAccessToken();
        getPlans();
        loadImage();
        getBinding().tvTitle.setText("" + response.getTitle());
        getBinding().tvDescription.setText("" + response.getDescription());
        setImage(response.getPosterURL(), AppConstants.VIDEO_IMAGE_BASE_KEY, getBinding().ivMovie);
        getBinding().toolbar.screenText.setText(getResources().getString(R.string.purchase_options) + " " + response.getTitle());
        if (alPurchaseOptions!=null && alPurchaseOptions.size() > 0) {
            alPurchaseOptions = getSortedList(alPurchaseOptions);
        }
        adapterPurchase = new PurchaseAdapter(this, alPurchaseOptions, PurchaseActivity.this);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
        getBinding().rvPurchase.setLayoutManager(mLayoutManager);
        getBinding().rvPurchase.setItemAnimator(new DefaultItemAnimator());
        getBinding().rvPurchase.setAdapter(adapterPurchase);
        //  getPlayStorePlans();
        Intent serviceIntent = new Intent("com.android.vending.billing.InAppBillingService.BIND");
        serviceIntent.setPackage("com.android.vending");

        getBinding().btnBuy.setOnClickListener(v -> {
            if (NetworkConnectivity.isOnline(PurchaseActivity.this)) {
                if (SystemClock.elapsedRealtime() - mLastClickTime < 1000) {
                    return;
                }
                mLastClickTime = SystemClock.elapsedRealtime();
                purchaseTVOD();
            }else {
                new ToastHandler(PurchaseActivity.this).show(getResources().getString(R.string.no_connection));
            }

            //  buySubscription();
            // bp.purchase(PurchaseActivity.this, "vod_285ce97b_a26c_482b_b0b3_0777b411310c_tvod_price", "DEVELOPER PAYLOAD HERE");
        });

        getBinding().terms.setOnClickListener(v -> {
            if (NetworkConnectivity.isOnline(PurchaseActivity.this)) {
                Objects.requireNonNull(this).startActivity(new Intent(this, HelpActivity.class).putExtra("type", "1"));
                //  Objects.requireNonNull(this).startActivity(new Intent(this, SampleActivity.class).putExtra("type", "1").putExtra("url",getString(R.string.term_condition)));
            }else {
                new ToastHandler(PurchaseActivity.this).show(getResources().getString(R.string.no_connection));
            }
        });

        getBinding().privacy.setOnClickListener(v -> {
            if (NetworkConnectivity.isOnline(PurchaseActivity.this)) {
                Objects.requireNonNull(this).startActivity(new Intent(this, HelpActivity.class).putExtra("type", "2"));
                //  Objects.requireNonNull(this).startActivity(new Intent(this, WebViewFlutterActivity.class).putExtra("type", "2").putExtra("url",getString(R.string.privacy_policy)));
            }else {
                new ToastHandler(PurchaseActivity.this).show(getResources().getString(R.string.no_connection));
            }
        });

        getBinding().contact.setOnClickListener(v -> {
            if (NetworkConnectivity.isOnline(PurchaseActivity.this)) {
                createBottomSheet();
            }else {
                new ToastHandler(PurchaseActivity.this).show(getResources().getString(R.string.no_connection));
            }
        });

        getBinding().backLayout.setOnClickListener(view -> {
            onBackPressed();
        });


    }

    private void loadImage() {
        ImageHelper.getInstance(PurchaseActivity.this).loadIAPImage(getBinding().bgImg);
    }

    public void buySubscription() {
        if (!StringUtils.isNullOrEmptyOrZero(selectedPlanName)) {
            showHideProgress(getBinding().progressBar);
            Log.w("planDetails", selectedPlanName + " " + clickedModel.getIdentifier());
            if (selectedPlanName.equalsIgnoreCase(VodOfferType.PERPETUAL.name())) {
                if (!isAlreadySubscribed) {
                    bp.purchase(PurchaseActivity.this, clickedModel.getIdentifier(), "DEVELOPER PAYLOAD", PurchaseType.PRODUCT.name());
                } else {
                    Toast.makeText(PurchaseActivity.this, getResources().getString(R.string.already_subscriber), Toast.LENGTH_SHORT).show();
                }
            } else if (selectedPlanName.equalsIgnoreCase(VodOfferType.RENTAL.name())) {

                if (!isAlreadySubscribed) {
                    bp.purchase(PurchaseActivity.this, clickedModel.getIdentifier(), "DEVELOPER PAYLOAD", PurchaseType.PRODUCT.name());
                } else {
                    Toast.makeText(PurchaseActivity.this, getResources().getString(R.string.already_subscriber), Toast.LENGTH_SHORT).show();
                }
            } else if (selectedPlanName.equalsIgnoreCase(VodOfferType.ONE_TIME.name())) {
                if (!isAlreadySubscribed) {
                    bp.purchase(PurchaseActivity.this, clickedModel.getIdentifier(), "DEVELOPER PAYLOAD", PurchaseType.PRODUCT.name());
                }
                //  bp.subscribe(PurchaseActivity.this, clickedModel.getIdentifier(), "DEVELOPER PAYLOAD HERE");
                else {
                    Toast.makeText(PurchaseActivity.this, getResources().getString(R.string.already_subscriber), Toast.LENGTH_SHORT).show();
                }
            } else if (selectedPlanName.equalsIgnoreCase(VodOfferType.RECURRING_SUBSCRIPTION​.name())) {
                if (!isAlreadySubscribed) {
                    //   bp.purchase(PurchaseActivity.this, clickedModel.getIdentifier(), "DEVELOPER PAYLOAD HERE");
                    bp.purchase(PurchaseActivity.this, initiateSKU, "DEVELOPER PAYLOAD", PurchaseType.SUBSCRIPTION.name());
                }
                // bp.subscribe(PurchaseActivity.this, clickedModel.getIdentifier(), "DEVELOPER PAYLOAD HERE");
                else {
                    Toast.makeText(PurchaseActivity.this, getResources().getString(R.string.already_subscriber), Toast.LENGTH_SHORT).show();
                }
            }

        }
    }

    private void purchaseTVOD() {
        if (clickedModel != null) {
            if (KsPreferenceKeys.getInstance().getAppPrefLoginStatus()){
                showLoading(getBinding().progressBar, true);
                buySubscription();
                //hitApiDoPurchase();
            }else {
                ActivityTrackers.getInstance().setAction(ActivityTrackers.PURCHASE);
                new ActivityLauncher(this).loginActivity(this, LoginActivity.class);
            }
        }
    }

    public String convertPlayStoreSKU(String val) {
        StringBuilder stringBuilder = new StringBuilder();
        String[] data = val.split("_", 2);
        stringBuilder.append(data[0].toLowerCase());
        stringBuilder.append("_");
        stringBuilder.append(data[1].replace("-", "_"));
        return stringBuilder.toString();
    }

    public void resetpurchaseAdapter() {
        try {
            if (responseEntitlementModel.getData().getEntitledAs()!=null){
                for (int i = 0; i < responseEntitlementModel.getData().getEntitledAs().size(); i++) {
                    EntitledAs entitledAs=responseEntitlementModel.getData().getEntitledAs().get(i);
                    String entitledAsIdentifier=entitledAs.getIdentifier();
                    for (int j = 0; j < alPurchaseOptions.size(); j++) {
                        PurchaseModel model=alPurchaseOptions.get(j);
                        String identifier= model.getIdentifier();
                        if (identifier.equalsIgnoreCase(entitledAsIdentifier)){
                            alPurchaseOptions.get(j).setSelected(true);
                        }
                    }
                }
            }
            for (int i =0;i<alPurchaseOptions.size();i++){
               if (alPurchaseOptions.get(i).getPurchaseOptions().equalsIgnoreCase(VodOfferType.RECURRING_SUBSCRIPTION​.name())){
                   if (alPurchaseOptions.get(i).getIdentifier().contains("month_access")){
                       alPurchaseOptions.get(i).setIndex("1");
                   }else if (alPurchaseOptions.get(i).getIdentifier().contains("year_access")){
                       alPurchaseOptions.get(i).setIndex("2");
                   }

                }else if (alPurchaseOptions.get(i).getPurchaseOptions().equalsIgnoreCase(VodOfferType.RENTAL.name())){
                   if (alPurchaseOptions.get(i).getIdentifier().contains("___sd")){
                       alPurchaseOptions.get(i).setIndex("3");
                   }else  if (alPurchaseOptions.get(i).getIdentifier().contains("___hd")){
                       alPurchaseOptions.get(i).setIndex("4");
                   }
                   else  if (alPurchaseOptions.get(i).getIdentifier().contains("___uhd")){
                       alPurchaseOptions.get(i).setIndex("5");
                   }

                }
               else if (alPurchaseOptions.get(i).getPurchaseOptions().equalsIgnoreCase(VodOfferType.PERPETUAL.name())){
                   if (alPurchaseOptions.get(i).getIdentifier().contains("___sd")){
                       alPurchaseOptions.get(i).setIndex("6");
                   }else  if (alPurchaseOptions.get(i).getIdentifier().contains("___hd")){
                       alPurchaseOptions.get(i).setIndex("7");
                   }
                   else  if (alPurchaseOptions.get(i).getIdentifier().contains("___uhd")){
                       alPurchaseOptions.get(i).setIndex("8");
                   }
               }
            }

            if (alPurchaseOptions.size() > 0) {
                alPurchaseOptions=getSortedList(alPurchaseOptions);
                adapterPurchase = new PurchaseAdapter(this, alPurchaseOptions, PurchaseActivity.this);
                RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
                getBinding().rvPurchase.setLayoutManager(mLayoutManager);
                getBinding().rvPurchase.setItemAnimator(new DefaultItemAnimator());
                getBinding().rvPurchase.setAdapter(adapterPurchase);
            } else {
                getBinding().bottomLay.setVisibility(View.GONE);
                getBinding().offerLayout.setVisibility(View.GONE);
                getBinding().noOfferLayout.setVisibility(View.VISIBLE);
            }

        }catch (Exception e){
          Log.w("crashOnList-->>",e.toString());
        }

    }

    int counter=0;
    List<PurchaseModel> newList=new ArrayList<>();
    private List<PurchaseModel> getSortedList(List<PurchaseModel> data) {
        Collections.sort(data, new Comparator<PurchaseModel>(){
            public int compare(PurchaseModel obj1, PurchaseModel obj2) {
                // ## Ascending order
                return obj1.getIndex().compareToIgnoreCase(obj2.getIndex()); // To compare string values

            }
        });
        return data;
    }

    List<String> subSkuList;
    List<String> productSkuList;
    @Override
    public void onBillingInitialized() {
        subSkuList = new ArrayList<>();
        productSkuList = new ArrayList<>();
        if (bp!=null) {

            if (responseEntitlementModel.getData().getPurchaseAs().size() > 0 && responseEntitlementModel.getData().getPurchaseAs() != null) {
                for (int i = 0; i < responseEntitlementModel.getData().getPurchaseAs().size(); i++) {
                    if (responseEntitlementModel.getData().getPurchaseAs()!=null && responseEntitlementModel.getData().getPurchaseAs().get(i).getOfferType()!=null && responseEntitlementModel.getData().getPurchaseAs().get(i).getOfferType().contains(VodOfferType.RECURRING_SUBSCRIPTION​.name())) {
                        String identifier=responseEntitlementModel.getData().getPurchaseAs().get(i).getIdentifier();
                        String s=identifier.replace("svod_","");
                        String v=s.replace("_",".");
                        subSkuList.add(v);
                    }else {
                        String identifier=responseEntitlementModel.getData().getPurchaseAs().get(i).getIdentifier();
                        //String s=identifier.replace("svod_","");
                        //String v=s.replace("_",".");
                        productSkuList.add(identifier);
                    }
                }


            }
            bp.getAllSkuDetails(subSkuList,productSkuList);
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
                   // handlePurchase(purchase);
                } else if (purchase.getPurchaseState() == Purchase.PurchaseState.PENDING) {
                   // PrintLogging.printLog("PurchaseActivity", "Received a pending purchase of SKU: " + purchase.getSku());
                    // handle pending purchases, e.g. confirm with users about the pending
                    // purchases, prompt them to complete it, etc.
                    // TODO: 8/24/2020 handle this in the next release.
                }
            }
        }catch (Exception ignored){

        }

    }
    String purchasedSKU="";
    private void handlePurchase(Purchase purchase) {
        try {
            // String[] strArray = new String[] {strName};
            Log.w("purchasedSKU",purchase.getSku());
            purchasedSKU= Base64.encodeToString(purchase.getSku().getBytes(),Base64.NO_WRAP);
            Log.w("purchasedSKU",purchasedSKU);

        }catch (Exception ignored){

        }

        try {
            updatePayment("","PAYMENT_DONE",purchase.getPurchaseToken(), paymentId);
        }catch (Exception ignored){

        }
    }

    @Override
    public void onListOfSKUFetched(@Nullable List<SkuDetails> purchases) {
        getPlayStorePlans();
    }

    SkuDetails skuDetails;
    public void getPlayStorePlans() {

        boolean isOneTimePurchaseSupported = true;//bp.isOneTimePurchaseSupported();
        if (!isOneTimePurchaseSupported) {
            Toast.makeText(this, "Your device doesn't support IN App Billing", Toast.LENGTH_LONG).show();
            getBinding().bottomLay.setVisibility(View.GONE);
            getBinding().offerLayout.setVisibility(View.GONE);
            getBinding().noOfferLayout.setVisibility(View.VISIBLE);
            return;
        }
        alPurchaseOptions = new ArrayList<>();

        if (responseEntitlementModel.getData().getPurchaseAs().size() > 0 && responseEntitlementModel.getData().getPurchaseAs() != null)
            for (int i = 0; i < responseEntitlementModel.getData().getPurchaseAs().size(); i++) {
                try {

                    createPlanList(i, alPurchaseOptions);
                } catch (Exception e) {
                    Logger.e(e.getMessage(), e.getLocalizedMessage());
                }
            }

        getBinding().progressBar.setVisibility(View.GONE);
        resetpurchaseAdapter();

        /*} catch (NullPointerException e) {
            getBinding().progressBar.setVisibility(View.GONE);
        }*/


    }

    private void createPlanList(int i, List<PurchaseModel> alPurchaseOptions) {
        PurchaseModel purchaseModel = new PurchaseModel();
        purchaseModel.setPurchaseOptions(responseEntitlementModel.getData().getPurchaseAs().get(i).getVoDOfferType());
        responseEntitlementModel.getData().getPurchaseAs().get(i).getVoDOfferType();
        String vodOfferType = responseEntitlementModel.getData().getPurchaseAs().get(i).getVoDOfferType();
        String subscriptionOfferPeriod = null;
        if (responseEntitlementModel.getData().getPurchaseAs().get(i).getOfferType() != null) {
            subscriptionOfferPeriod = (String) responseEntitlementModel.getData().getPurchaseAs().get(i).getOfferType();
        }

        createList(purchaseModel, i, alPurchaseOptions, vodOfferType, subscriptionOfferPeriod);
    }

    private void createList(PurchaseModel purchaseModel, int i, List<PurchaseModel> alPurchaseOptions, String vodOfferType, String subscriptionOfferPeriod) {

        try {
            if (responseEntitlementModel.getData().getPurchaseAs().get(i).getDescription()!=null)
                purchaseModel.setDescription(responseEntitlementModel.getData().getPurchaseAs().get(i).getDescription().toString().trim());
            if (subscriptionOfferPeriod != null) {
                if (responseEntitlementModel.getData().getPurchaseAs().get(i).getOfferType().contains(VodOfferType.RECURRING_SUBSCRIPTION​.name())) {
                   int index= AppCommonMethod.getIndex(subSkuList,responseEntitlementModel.getData().getPurchaseAs().get(i).getIdentifier());
                   if (index==-1 && index>=i){

                   }else {
                       createRecurringSubscriptions(purchaseModel, i, alPurchaseOptions, vodOfferType, subscriptionOfferPeriod, VodOfferType.RECURRING_SUBSCRIPTION​.name(),index);
                   }

                }  if (responseEntitlementModel.getData().getPurchaseAs().get(i).getOfferType().contains(VodOfferType.ONE_TIME.name())) {
                    createOneTimeSubscriptions(purchaseModel, i, alPurchaseOptions, vodOfferType, subscriptionOfferPeriod, VodOfferType.ONE_TIME.name());
                }

            } else if (vodOfferType != null) {
                if (vodOfferType.contains(VodOfferType.PERPETUAL.name())) {
                    SkuDetails skuDetails = null;
                    if (!StringUtils.isNullOrEmptyOrZero(responseEntitlementModel.getData().getPurchaseAs().get(i).getIdentifier())) {
                        int index= AppCommonMethod.getProductIndex(productSkuList,responseEntitlementModel.getData().getPurchaseAs().get(i).getIdentifier());
                        if (index==-1 && index>=i){

                        }else {
                            createPerpetualPlan(purchaseModel, i, alPurchaseOptions, vodOfferType, subscriptionOfferPeriod, VodOfferType.PERPETUAL.name(),index);
                        }
                       /* try {
                            // bp.consumePurchase(responseEntitlementModel.getData().getPurchaseAs().get(i).getIdentifier());
                            skuDetails = bp.getProductSkuDetail(PurchaseActivity.this,responseEntitlementModel.getData().getPurchaseAs().get(i).getIdentifier());
                            purchaseModel.setPrice("" + skuDetails.getPrice());
                            purchaseModel.setPurchaseOptions(VodOfferType.PERPETUAL.name());
                            purchaseModel.setTitle(responseEntitlementModel.getData().getPurchaseAs().get(i).getTitle());
                            purchaseModel.setIdentifier(responseEntitlementModel.getData().getPurchaseAs().get(i).getIdentifier());
                            purchaseModel.setCurrency(skuDetails.getPriceCurrencyCode());
                        } catch (Exception e) {
                            Logger.e("PurchaseModel", "purchase item not found" + "  " + e.toString());
                            purchaseModel.setPrice("" + responseEntitlementModel.getData().getPurchaseAs().get(i).getPrices().get(0).getPrice());
                            purchaseModel.setPurchaseOptions(VodOfferType.PERPETUAL.name());
                            purchaseModel.setTitle(responseEntitlementModel.getData().getPurchaseAs().get(i).getTitle());
                            purchaseModel.setIdentifier(responseEntitlementModel.getData().getPurchaseAs().get(i).getIdentifier());
                            purchaseModel.setCurrency(responseEntitlementModel.getData().getPurchaseAs().get(i).getPrices().get(0).getCurrencyCode());
                        }*/
                    }

                } else if (vodOfferType.contains(VodOfferType.RENTAL.name())) {
                    int index= AppCommonMethod.getProductIndex(productSkuList,responseEntitlementModel.getData().getPurchaseAs().get(i).getIdentifier());
                    if (index==-1 && index>=i){

                    }else {
                        createRentalPlan(purchaseModel, i, alPurchaseOptions, vodOfferType, subscriptionOfferPeriod, VodOfferType.RENTAL.name(),index);
                    }

                }
            }

        } catch (Exception ignored) {

        }

    }

    private void createPerpetualPlan(PurchaseModel purchaseModel, int i, List<PurchaseModel> alPurchaseOptions, String vodOfferType, String subscriptionOfferPeriod, String name, int planIndex) {

        try {
            skuDetails = bp.getLocalSubscriptionSkuDetail(PurchaseActivity.this,productSkuList.get(planIndex));
            purchaseModel.setPrice("" + skuDetails.getPrice());
            purchaseModel.setPurchaseOptions(VodOfferType.PERPETUAL.name());
            purchaseModel.setTitle(responseEntitlementModel.getData().getPurchaseAs().get(i).getTitle());
            purchaseModel.setIdentifier(responseEntitlementModel.getData().getPurchaseAs().get(i).getIdentifier());
            purchaseModel.setCurrency(skuDetails.getPriceCurrencyCode());
        } catch (Exception e) {
            Logger.e("PurchaseModel", "purchase item not found" + "  " + e.toString());
            purchaseModel.setPrice("" + responseEntitlementModel.getData().getPurchaseAs().get(i).getPrices().get(0).getPrice());
            purchaseModel.setPurchaseOptions(VodOfferType.PERPETUAL.name());
            purchaseModel.setTitle(responseEntitlementModel.getData().getPurchaseAs().get(i).getTitle());
            purchaseModel.setIdentifier(responseEntitlementModel.getData().getPurchaseAs().get(i).getIdentifier());
            purchaseModel.setCurrency(responseEntitlementModel.getData().getPurchaseAs().get(i).getPrices().get(0).getCurrencyCode());
        }

        if (skuDetails != null)
            alPurchaseOptions.add(purchaseModel);
    }

    private void createRentalPlan(PurchaseModel purchaseModel, int i, List<PurchaseModel> alPurchaseOptions, String vodOfferType, String subscriptionOfferPeriod, String subscriptionType,int planIndex) {
        try {
            if (responseEntitlementModel.getData().getPurchaseAs().get(i).getRentalPeriod().getPeriodType().contains(VodOfferType.DAYS.name())) {
                try {
                    skuDetails = bp.getLocalSubscriptionSkuDetail(PurchaseActivity.this,productSkuList.get(planIndex));
                    purchaseModel.setPrice("" + skuDetails.getPrice());
                    purchaseModel.setPurchaseOptions(subscriptionType);
                    purchaseModel.setOfferPeriod(VodOfferType.DAYS.name());
                    purchaseModel.setOfferPeriodDuration(responseEntitlementModel.getData().getPurchaseAs().get(i).getRentalPeriod().getPeriodLength());

                    purchaseModel.setTitle(responseEntitlementModel.getData().getPurchaseAs().get(i).getTitle());
                    purchaseModel.setIdentifier(responseEntitlementModel.getData().getPurchaseAs().get(i).getIdentifier());
                    purchaseModel.setCurrency(skuDetails.getPriceCurrencyCode());
                } catch (Exception e) {
                    // purchaseModel.setPrice("" + responseEntitlementModel.getData().getPurchaseAs().get(i).getPrice());
                    purchaseModel.setPurchaseOptions(subscriptionType);
                    purchaseModel.setOfferPeriod(VodOfferType.DAYS.name());
                    purchaseModel.setOfferPeriodDuration(responseEntitlementModel.getData().getPurchaseAs().get(i).getRentalPeriod().getPeriodLength());
                    purchaseModel.setTitle(responseEntitlementModel.getData().getPurchaseAs().get(i).getTitle());
                    purchaseModel.setIdentifier(responseEntitlementModel.getData().getPurchaseAs().get(i).getIdentifier());
                    // purchaseModel.setCurrency(responseEntitlementModel.getData().getPurchaseAs().get(i).getCurrencyCode());

                }
                if (skuDetails != null)
                    alPurchaseOptions.add(purchaseModel);
            } else if (responseEntitlementModel.getData().getPurchaseAs().get(i).getRentalPeriod().getPeriodType().contains(VodOfferType.WEEKS.name())) {
                try {
                    // skuDetails = bp.getSubscriptionListingDetails("monthly");
                    skuDetails = bp.getLocalSubscriptionSkuDetail(PurchaseActivity.this,productSkuList.get(planIndex));
                    purchaseModel.setPrice("" + skuDetails.getPrice());
                    purchaseModel.setPurchaseOptions(subscriptionType);
                    purchaseModel.setOfferPeriod(VodOfferType.WEEKS.name());
                    purchaseModel.setOfferPeriodDuration(responseEntitlementModel.getData().getPurchaseAs().get(i).getRentalPeriod().getPeriodLength());
                    purchaseModel.setTitle(responseEntitlementModel.getData().getPurchaseAs().get(i).getTitle());
                    purchaseModel.setIdentifier(responseEntitlementModel.getData().getPurchaseAs().get(i).getIdentifier());
                    purchaseModel.setCurrency(skuDetails.getPriceCurrencyCode());
                } catch (Exception e) {
                    // purchaseModel.setPrice("" + responseEntitlementModel.getData().getPurchaseAs().get(i).getPrice());
                    purchaseModel.setPurchaseOptions(subscriptionType);
                    purchaseModel.setOfferPeriod(VodOfferType.WEEKS.name());
                    purchaseModel.setOfferPeriodDuration(responseEntitlementModel.getData().getPurchaseAs().get(i).getRentalPeriod().getPeriodLength());
                    purchaseModel.setTitle(responseEntitlementModel.getData().getPurchaseAs().get(i).getTitle());
                    purchaseModel.setIdentifier(responseEntitlementModel.getData().getPurchaseAs().get(i).getIdentifier());
                    // purchaseModel.setCurrency(responseEntitlementModel.getData().getPurchaseAs().get(i).getCurrencyCode());

                }
                if (skuDetails != null)
                    alPurchaseOptions.add(purchaseModel);
            } else if (responseEntitlementModel.getData().getPurchaseAs().get(i).getRentalPeriod().getPeriodType().contains(VodOfferType.MONTHS.name())) {
                try {
                    // skuDetails = bp.getSubscriptionListingDetails("monthly");
                    skuDetails = bp.getLocalSubscriptionSkuDetail(PurchaseActivity.this,productSkuList.get(planIndex));
                    purchaseModel.setPrice("" + skuDetails.getPrice());
                    purchaseModel.setPurchaseOptions(subscriptionType);
                    purchaseModel.setOfferPeriod(VodOfferType.MONTHS.name());
                    purchaseModel.setOfferPeriodDuration(responseEntitlementModel.getData().getPurchaseAs().get(i).getRentalPeriod().getPeriodLength());
                    purchaseModel.setTitle(responseEntitlementModel.getData().getPurchaseAs().get(i).getTitle());
                    purchaseModel.setIdentifier(responseEntitlementModel.getData().getPurchaseAs().get(i).getIdentifier());
                    purchaseModel.setCurrency(skuDetails.getPriceCurrencyCode());
                } catch (Exception e) {
                    // purchaseModel.setPrice("" + responseEntitlementModel.getData().getPurchaseAs().get(i).getPrice());
                    purchaseModel.setPurchaseOptions(subscriptionType);
                    purchaseModel.setOfferPeriod(VodOfferType.MONTHS.name());
                    purchaseModel.setOfferPeriodDuration(responseEntitlementModel.getData().getPurchaseAs().get(i).getRentalPeriod().getPeriodLength());
                    purchaseModel.setTitle(responseEntitlementModel.getData().getPurchaseAs().get(i).getTitle());
                    purchaseModel.setIdentifier(responseEntitlementModel.getData().getPurchaseAs().get(i).getIdentifier());
                    // purchaseModel.setCurrency(responseEntitlementModel.getData().getPurchaseAs().get(i).getCurrencyCode());

                }
                if (skuDetails != null)
                    alPurchaseOptions.add(purchaseModel);
            } else if (responseEntitlementModel.getData().getPurchaseAs().get(i).getRentalPeriod().getPeriodType().contains(VodOfferType.MONTHS.name())) {
                try {
                    // skuDetails = bp.getSubscriptionListingDetails("monthly");
                    skuDetails = bp.getLocalSubscriptionSkuDetail(PurchaseActivity.this,productSkuList.get(planIndex));
                    purchaseModel.setPrice("" + skuDetails.getPrice());
                    purchaseModel.setPurchaseOptions(subscriptionType);
                    purchaseModel.setOfferPeriod(VodOfferType.YEARS.name());
                    purchaseModel.setOfferPeriodDuration(responseEntitlementModel.getData().getPurchaseAs().get(i).getRentalPeriod().getPeriodLength());
                    purchaseModel.setTitle(responseEntitlementModel.getData().getPurchaseAs().get(i).getTitle());
                    purchaseModel.setIdentifier(responseEntitlementModel.getData().getPurchaseAs().get(i).getIdentifier());
                    purchaseModel.setCurrency(skuDetails.getPriceCurrencyCode());
                } catch (Exception e) {
                    // purchaseModel.setPrice("" + responseEntitlementModel.getData().getPurchaseAs().get(i).getPrice());
                    purchaseModel.setPurchaseOptions(subscriptionType);
                    purchaseModel.setOfferPeriod(VodOfferType.YEARS.name());
                    purchaseModel.setOfferPeriodDuration(responseEntitlementModel.getData().getPurchaseAs().get(i).getRentalPeriod().getPeriodLength());
                    purchaseModel.setTitle(responseEntitlementModel.getData().getPurchaseAs().get(i).getTitle());
                    purchaseModel.setIdentifier(responseEntitlementModel.getData().getPurchaseAs().get(i).getIdentifier());
                    // purchaseModel.setCurrency(responseEntitlementModel.getData().getPurchaseAs().get(i).getCurrencyCode());

                }
                if (skuDetails != null)
                    alPurchaseOptions.add(purchaseModel);
            }

        } catch (Exception e) {

        }
    }

    private void createOneTimeSubscriptions(PurchaseModel purchaseModel, int i, List<PurchaseModel> alPurchaseOptions, String vodOfferType, String subscriptionOfferPeriod, String subscriptionType) {
        try {
            if (responseEntitlementModel.getData().getPurchaseAs().get(i).getOneTimeOffer().getPeriodType().contains(VodOfferType.DAYS.name())) {
                try {
                    // skuDetails = bp.getSubscriptionListingDetails("monthly");
                    skuDetails = bp.getProductSkuDetail(PurchaseActivity.this,responseEntitlementModel.getData().getPurchaseAs().get(i).getIdentifier());
                    purchaseModel.setPrice("" + skuDetails.getPrice());
                    purchaseModel.setPurchaseOptions(subscriptionType);
                    purchaseModel.setOfferPeriod(VodOfferType.DAYS.name());
                    purchaseModel.setTitle(responseEntitlementModel.getData().getPurchaseAs().get(i).getTitle());
                    purchaseModel.setIdentifier(responseEntitlementModel.getData().getPurchaseAs().get(i).getIdentifier());
                    purchaseModel.setCurrency(skuDetails.getPriceCurrencyCode());
                    purchaseModel.setOfferPeriodDuration(responseEntitlementModel.getData().getPurchaseAs().get(i).getOneTimeOffer().getPeriodLength());
                    purchaseModel.setPeriodType(responseEntitlementModel.getData().getPurchaseAs().get(i).getOneTimeOffer().getPeriodType());
                } catch (Exception e) {
                    // purchaseModel.setPrice("" + responseEntitlementModel.getData().getPurchaseAs().get(i).getPrice());
                    purchaseModel.setPurchaseOptions(subscriptionType);
                    purchaseModel.setOfferPeriod(VodOfferType.DAYS.name());
                    purchaseModel.setTitle(responseEntitlementModel.getData().getPurchaseAs().get(i).getTitle());
                    purchaseModel.setIdentifier(responseEntitlementModel.getData().getPurchaseAs().get(i).getIdentifier());
                    purchaseModel.setOfferPeriodDuration(responseEntitlementModel.getData().getPurchaseAs().get(i).getOneTimeOffer().getPeriodLength());
                    purchaseModel.setPeriodType(responseEntitlementModel.getData().getPurchaseAs().get(i).getOneTimeOffer().getPeriodType());
                    // purchaseModel.setCurrency(responseEntitlementModel.getData().getPurchaseAs().get(i).getCurrencyCode());

                }
                if (skuDetails != null)
                    alPurchaseOptions.add(purchaseModel);
            } else if (responseEntitlementModel.getData().getPurchaseAs().get(i).getOneTimeOffer().getPeriodType().contains(VodOfferType.WEEKS.name())) {
                try {
                    // skuDetails = bp.getSubscriptionListingDetails("monthly");
                    skuDetails = bp.getProductSkuDetail(PurchaseActivity.this,responseEntitlementModel.getData().getPurchaseAs().get(i).getIdentifier());
                    purchaseModel.setPrice("" + skuDetails.getPrice());
                    purchaseModel.setPurchaseOptions(subscriptionType);
                    purchaseModel.setOfferPeriod(VodOfferType.WEEKS.name());
                    purchaseModel.setTitle(responseEntitlementModel.getData().getPurchaseAs().get(i).getTitle());
                    purchaseModel.setIdentifier(responseEntitlementModel.getData().getPurchaseAs().get(i).getIdentifier());
                    purchaseModel.setCurrency(skuDetails.getPriceCurrencyCode());
                    purchaseModel.setOfferPeriodDuration(responseEntitlementModel.getData().getPurchaseAs().get(i).getOneTimeOffer().getPeriodLength());
                    purchaseModel.setPeriodType(responseEntitlementModel.getData().getPurchaseAs().get(i).getOneTimeOffer().getPeriodType());
                } catch (Exception e) {
                    // purchaseModel.setPrice("" + responseEntitlementModel.getData().getPurchaseAs().get(i).getPrice());
                    purchaseModel.setPurchaseOptions(subscriptionType);
                    purchaseModel.setOfferPeriod(VodOfferType.WEEKS.name());
                    purchaseModel.setTitle(responseEntitlementModel.getData().getPurchaseAs().get(i).getTitle());
                    purchaseModel.setIdentifier(responseEntitlementModel.getData().getPurchaseAs().get(i).getIdentifier());
                    purchaseModel.setOfferPeriodDuration(responseEntitlementModel.getData().getPurchaseAs().get(i).getOneTimeOffer().getPeriodLength());
                    purchaseModel.setPeriodType(responseEntitlementModel.getData().getPurchaseAs().get(i).getOneTimeOffer().getPeriodType());
                    // purchaseModel.setCurrency(responseEntitlementModel.getData().getPurchaseAs().get(i).getCurrencyCode());

                }
                if (skuDetails != null)
                    alPurchaseOptions.add(purchaseModel);
            } else if (responseEntitlementModel.getData().getPurchaseAs().get(i).getOneTimeOffer().getPeriodType().contains(VodOfferType.MONTHS.name())) {
                try {
                    // skuDetails = bp.getSubscriptionListingDetails("monthly");
                    skuDetails = bp.getProductSkuDetail(PurchaseActivity.this,responseEntitlementModel.getData().getPurchaseAs().get(i).getIdentifier());
                    purchaseModel.setPrice("" + skuDetails.getPrice());
                    purchaseModel.setPurchaseOptions(subscriptionType);
                    purchaseModel.setOfferPeriod(VodOfferType.MONTHS.name());
                    purchaseModel.setTitle(responseEntitlementModel.getData().getPurchaseAs().get(i).getTitle());
                    purchaseModel.setIdentifier(responseEntitlementModel.getData().getPurchaseAs().get(i).getIdentifier());
                    purchaseModel.setCurrency(skuDetails.getPriceCurrencyCode());
                    purchaseModel.setOfferPeriodDuration(responseEntitlementModel.getData().getPurchaseAs().get(i).getOneTimeOffer().getPeriodLength());
                    purchaseModel.setPeriodType(responseEntitlementModel.getData().getPurchaseAs().get(i).getOneTimeOffer().getPeriodType());
                } catch (Exception e) {
                    // purchaseModel.setPrice("" + responseEntitlementModel.getData().getPurchaseAs().get(i).getPrice());
                    purchaseModel.setPurchaseOptions(subscriptionType);
                    purchaseModel.setOfferPeriod(VodOfferType.MONTHS.name());
                    purchaseModel.setTitle(responseEntitlementModel.getData().getPurchaseAs().get(i).getTitle());
                    purchaseModel.setIdentifier(responseEntitlementModel.getData().getPurchaseAs().get(i).getIdentifier());
                    purchaseModel.setOfferPeriodDuration(responseEntitlementModel.getData().getPurchaseAs().get(i).getOneTimeOffer().getPeriodLength());
                    purchaseModel.setPeriodType(responseEntitlementModel.getData().getPurchaseAs().get(i).getOneTimeOffer().getPeriodType());
                    // purchaseModel.setCurrency(responseEntitlementModel.getData().getPurchaseAs().get(i).getCurrencyCode());

                }
                if (skuDetails != null)
                    alPurchaseOptions.add(purchaseModel);
            } else if (responseEntitlementModel.getData().getPurchaseAs().get(i).getOneTimeOffer().getPeriodType().contains(VodOfferType.YEARS.name())) {
                try {
                    // skuDetails = bp.getSubscriptionListingDetails("monthly");
                    skuDetails = bp.getProductSkuDetail(PurchaseActivity.this,responseEntitlementModel.getData().getPurchaseAs().get(i).getIdentifier());
                    purchaseModel.setPrice("" + skuDetails.getPrice());
                    purchaseModel.setPurchaseOptions(subscriptionType);
                    purchaseModel.setOfferPeriod(VodOfferType.YEARS.name());
                    purchaseModel.setTitle(responseEntitlementModel.getData().getPurchaseAs().get(i).getTitle());
                    purchaseModel.setIdentifier(responseEntitlementModel.getData().getPurchaseAs().get(i).getIdentifier());
                    purchaseModel.setCurrency(skuDetails.getPriceCurrencyCode());
                    purchaseModel.setOfferPeriodDuration(responseEntitlementModel.getData().getPurchaseAs().get(i).getOneTimeOffer().getPeriodLength());
                    purchaseModel.setPeriodType(responseEntitlementModel.getData().getPurchaseAs().get(i).getOneTimeOffer().getPeriodType());
                } catch (Exception e) {
                    // purchaseModel.setPrice("" + responseEntitlementModel.getData().getPurchaseAs().get(i).getPrice());
                    purchaseModel.setPurchaseOptions(subscriptionType);
                    purchaseModel.setOfferPeriod(VodOfferType.YEARS.name());
                    purchaseModel.setTitle(responseEntitlementModel.getData().getPurchaseAs().get(i).getTitle());
                    purchaseModel.setIdentifier(responseEntitlementModel.getData().getPurchaseAs().get(i).getIdentifier());
                    purchaseModel.setOfferPeriodDuration(responseEntitlementModel.getData().getPurchaseAs().get(i).getOneTimeOffer().getPeriodLength());
                    purchaseModel.setPeriodType(responseEntitlementModel.getData().getPurchaseAs().get(i).getOneTimeOffer().getPeriodType());
                    // purchaseModel.setCurrency(responseEntitlementModel.getData().getPurchaseAs().get(i).getCurrencyCode());

                }
                if (skuDetails != null)
                    alPurchaseOptions.add(purchaseModel);
            }

        } catch (Exception e) {

        }
    }

    private void createRecurringSubscriptions(PurchaseModel purchaseModel, int i, List<PurchaseModel> alPurchaseOptions, String vodOfferType, String subscriptionOfferPeriod, String subscriptionType,int planIndex) {
        try {
            if (responseEntitlementModel.getData().getPurchaseAs().get(i).getRecurringOffer().getOfferPeriod().contains(VodOfferType.WEEKLY.name())) {
                try {
                    // skuDetails = bp.getSubscriptionListingDetails("monthly");
                    // skuDetails = bp.getSubscriptionListingDetails("vod_285ce97b_a26c_482b_b0b3_0777b411310c_tvod_price");
                    skuDetails = bp.getLocalSubscriptionSkuDetail(PurchaseActivity.this,subSkuList.get(planIndex));
                    purchaseModel.setPrice("" + skuDetails.getPrice());
                    purchaseModel.setPurchaseOptions(subscriptionType);
                    purchaseModel.setOfferPeriod(VodOfferType.WEEKLY.name());
                    purchaseModel.setTitle(responseEntitlementModel.getData().getPurchaseAs().get(i).getTitle());
                    purchaseModel.setIdentifier(responseEntitlementModel.getData().getPurchaseAs().get(i).getIdentifier());
                    purchaseModel.setCurrency(skuDetails.getPriceCurrencyCode());
                } catch (Exception e) {
                    // purchaseModel.setPrice("" + responseEntitlementModel.getData().getPurchaseAs().get(i).getPrice());
                    purchaseModel.setPurchaseOptions(subscriptionType);
                    purchaseModel.setOfferPeriod(VodOfferType.WEEKLY.name());
                    purchaseModel.setTitle(responseEntitlementModel.getData().getPurchaseAs().get(i).getTitle());
                    purchaseModel.setIdentifier(responseEntitlementModel.getData().getPurchaseAs().get(i).getIdentifier());
                    // purchaseModel.setCurrency(responseEntitlementModel.getData().getPurchaseAs().get(i).getCurrencyCode());

                }
                if (skuDetails != null)
                    alPurchaseOptions.add(purchaseModel);
            } else if (responseEntitlementModel.getData().getPurchaseAs().get(i).getRecurringOffer().getOfferPeriod().contains(VodOfferType.MONTHLY.name())) {
                try {
                    // skuDetails = bp.getSubscriptionListingDetails("monthly");
                    // skuDetails = bp.getSubscriptionListingDetails("svod_my_monthly_pack_with_trail");
                    skuDetails = bp.getLocalSubscriptionSkuDetail(PurchaseActivity.this,subSkuList.get(planIndex));
                    purchaseModel.setPrice("" + skuDetails.getPrice());
                    purchaseModel.setPurchaseOptions(subscriptionType);
                    purchaseModel.setOfferPeriod(VodOfferType.MONTHLY.name());
                    purchaseModel.setTitle(responseEntitlementModel.getData().getPurchaseAs().get(i).getTitle());
                    purchaseModel.setIdentifier(responseEntitlementModel.getData().getPurchaseAs().get(i).getIdentifier());
                    purchaseModel.setCurrency(skuDetails.getPriceCurrencyCode());
                } catch (Exception e) {
                    // purchaseModel.setPrice("" + responseEntitlementModel.getData().getPurchaseAs().get(i).getPrice());
                    purchaseModel.setPurchaseOptions(subscriptionType);
                    purchaseModel.setOfferPeriod(VodOfferType.MONTHLY.name());
                    purchaseModel.setTitle(responseEntitlementModel.getData().getPurchaseAs().get(i).getTitle());
                    purchaseModel.setIdentifier(responseEntitlementModel.getData().getPurchaseAs().get(i).getIdentifier());
                    // purchaseModel.setCurrency(responseEntitlementModel.getData().getPurchaseAs().get(i).getCurrencyCode());

                }
                if (skuDetails != null)
                    alPurchaseOptions.add(purchaseModel);
            } else if (responseEntitlementModel.getData().getPurchaseAs().get(i).getRecurringOffer().getOfferPeriod().contains(VodOfferType.QUARTERLY.name())) {
                try {
                    // skuDetails = bp.getSubscriptionListingDetails("monthly");
                    //skuDetails = bp.getSubscriptionListingDetails("svod_my_quaterly_pack_recurring");
                    skuDetails = bp.getLocalSubscriptionSkuDetail(PurchaseActivity.this,subSkuList.get(planIndex));
                    purchaseModel.setPrice("" + skuDetails.getPrice());
                    purchaseModel.setPurchaseOptions(subscriptionType);
                    purchaseModel.setOfferPeriod(VodOfferType.QUARTERLY.name());
                    purchaseModel.setTitle(responseEntitlementModel.getData().getPurchaseAs().get(i).getTitle());
                    purchaseModel.setIdentifier(responseEntitlementModel.getData().getPurchaseAs().get(i).getIdentifier());
                    purchaseModel.setCurrency(skuDetails.getPriceCurrencyCode());
                } catch (Exception e) {
                    // purchaseModel.setPrice("" + responseEntitlementModel.getData().getPurchaseAs().get(i).getPrice());
                    purchaseModel.setPurchaseOptions(subscriptionType);
                    purchaseModel.setOfferPeriod(VodOfferType.QUARTERLY.name());
                    purchaseModel.setTitle(responseEntitlementModel.getData().getPurchaseAs().get(i).getTitle());
                    purchaseModel.setIdentifier(responseEntitlementModel.getData().getPurchaseAs().get(i).getIdentifier());
                    // purchaseModel.setCurrency(responseEntitlementModel.getData().getPurchaseAs().get(i).getCurrencyCode());

                }
                if (skuDetails != null)
                    alPurchaseOptions.add(purchaseModel);
            } else if (responseEntitlementModel.getData().getPurchaseAs().get(i).getRecurringOffer().getOfferPeriod().contains(VodOfferType.HALF_YEARLY.name())) {
                try {
                    // skuDetails = bp.getSubscriptionListingDetails("monthly");
                    skuDetails = bp.getLocalSubscriptionSkuDetail(PurchaseActivity.this,subSkuList.get(planIndex));
                    purchaseModel.setPrice("" + skuDetails.getPrice());
                    purchaseModel.setPurchaseOptions(subscriptionType);
                    purchaseModel.setOfferPeriod(VodOfferType.HALF_YEARLY.name());
                    purchaseModel.setTitle(responseEntitlementModel.getData().getPurchaseAs().get(i).getTitle());
                    purchaseModel.setIdentifier(responseEntitlementModel.getData().getPurchaseAs().get(i).getIdentifier());
                    purchaseModel.setCurrency(skuDetails.getPriceCurrencyCode());
                } catch (Exception e) {
                    // purchaseModel.setPrice("" + responseEntitlementModel.getData().getPurchaseAs().get(i).getPrice());
                    purchaseModel.setPurchaseOptions(subscriptionType);
                    purchaseModel.setOfferPeriod(VodOfferType.HALF_YEARLY.name());
                    purchaseModel.setTitle(responseEntitlementModel.getData().getPurchaseAs().get(i).getTitle());
                    purchaseModel.setIdentifier(responseEntitlementModel.getData().getPurchaseAs().get(i).getIdentifier());
                    // purchaseModel.setCurrency(responseEntitlementModel.getData().getPurchaseAs().get(i).getCurrencyCode());

                }
                if (skuDetails != null)
                    alPurchaseOptions.add(purchaseModel);
            } else if (responseEntitlementModel.getData().getPurchaseAs().get(i).getRecurringOffer().getOfferPeriod().contains(VodOfferType.ANNUAL.name())) {
                try {
                    // skuDetails = bp.getSubscriptionListingDetails("monthly");
                    skuDetails = bp.getLocalSubscriptionSkuDetail(PurchaseActivity.this,subSkuList.get(planIndex));
                    purchaseModel.setPrice("" + skuDetails.getPrice());
                    purchaseModel.setPurchaseOptions(subscriptionType);
                    purchaseModel.setOfferPeriod(VodOfferType.ANNUAL.name());
                    purchaseModel.setTitle(responseEntitlementModel.getData().getPurchaseAs().get(i).getTitle());
                    purchaseModel.setIdentifier(responseEntitlementModel.getData().getPurchaseAs().get(i).getIdentifier());
                    purchaseModel.setCurrency(skuDetails.getPriceCurrencyCode());
                } catch (Exception e) {
                    // purchaseModel.setPrice("" + responseEntitlementModel.getData().getPurchaseAs().get(i).getPrice());
                    purchaseModel.setPurchaseOptions(subscriptionType);
                    purchaseModel.setOfferPeriod(VodOfferType.ANNUAL.name());
                    purchaseModel.setTitle(responseEntitlementModel.getData().getPurchaseAs().get(i).getTitle());
                    purchaseModel.setIdentifier(responseEntitlementModel.getData().getPurchaseAs().get(i).getIdentifier());
                    // purchaseModel.setCurrency(responseEntitlementModel.getData().getPurchaseAs().get(i).getCurrencyCode());

                }
                if (skuDetails != null)
                    alPurchaseOptions.add(purchaseModel);
            }


        } catch (Exception ignored) {

        }
    }

   /* @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (!bp.handleActivityResult(requestCode, resultCode, data)) {
            super.onActivityResult(requestCode, resultCode, data);
            showHideProgress(getBinding().progressBar);
            //  Toast.makeText(PurchaseActivity.this, "" + data.toString(), Toast.LENGTH_SHORT).show();
        }
    }*/

    private void showDialog(String title, String message) {
        FragmentManager fm = getSupportFragmentManager();
        AlertDialogSingleButtonFragment alertDialog = AlertDialogSingleButtonFragment.newInstance(title, message, getResources().getString(R.string.ok));
        alertDialog.setCancelable(false);
        alertDialog.setAlertDialogCallBack(this);
        alertDialog.show(fm, "fragment_alert");
    }


    private void updatePayment(String billingError,String paymentStatus,String purchaseToken, String paymentId) {
        viewModel.updatePurchase(billingError,paymentStatus,strToken, purchaseToken, paymentId, orderId, clickedModel,purchasedSKU).observe(PurchaseActivity.this, new Observer<PurchaseResponseModel>() {
            @Override
            public void onChanged(@Nullable PurchaseResponseModel responseCancelPurchase) {
                showLoading(getBinding().progressBar, false);
                if (responseCancelPurchase.getStatus()) {
                    if (responseCancelPurchase.getData().getOrderStatus() != null) {
                        if (responseCancelPurchase.getData().getOrderStatus().equalsIgnoreCase("COMPLETED")) {
                            AppCommonMethod.isPurchase = true;
                            Toast.makeText(PurchaseActivity.this, PurchaseActivity.this.getResources().getString(R.string.purchased_successfully), Toast.LENGTH_SHORT).show();
                            finish();
                        } else {
                            dismissLoading(getBinding().progressBar);
                            showDialog(PurchaseActivity.this.getResources().getString(R.string.error), getResources().getString(R.string.payment_error) + " " + "info@panteaoproductions.com");
                        }

                    }
                    // hitApiDoPurchase();
                } else if (responseCancelPurchase.getResponseCode() == 4302) {
                    isloggedout = true;
                    dismissLoading(getBinding().progressBar);
                    showDialog(PurchaseActivity.this.getResources().getString(R.string.logged_out), responseCancelPurchase.getDebugMessage() == null ? "" : responseCancelPurchase.getDebugMessage().toString());

                }else if (responseCancelPurchase.getResponseCode() == 4011) {
                    dismissLoading(getBinding().progressBar);
                }
                else {
                    dismissLoading(getBinding().progressBar);
                    showDialog(PurchaseActivity.this.getResources().getString(R.string.error), responseCancelPurchase.getDebugMessage() == null ? "" : responseCancelPurchase.getDebugMessage().toString());
                }

            }
        });

    }


    public void hitCancelSubscription() {

        viewModel.cancelPlan(strToken).observe(PurchaseActivity.this, new Observer<ResponseCancelPurchase>() {
            @Override
            public void onChanged(@Nullable ResponseCancelPurchase responseCancelPurchase) {
                if (responseCancelPurchase.isStatus()) {
                    hitApiDoPurchase();
                } else {
                    if (responseCancelPurchase.getResponseCode() == 401) {
                        isloggedout = true;
                        showDialog(PurchaseActivity.this.getResources().getString(R.string.logged_out), getResources().getString(R.string.you_are_logged_out));
                    }
                }
            }
        });


    }

    String orderId;
    String assetSKU;

    public void hitApiDoPurchase() {
        if (responseEntitlementModel != null && responseEntitlementModel.getData() != null && responseEntitlementModel.getData().getSku() != null) {
            assetSKU = responseEntitlementModel.getData().getSku();
        }

        viewModel.createNewPurchaseRequest(strToken, jsonObj, clickedModel, assetSKU).observe(PurchaseActivity.this, purchaseResponseModel -> {
            try {
                if (purchaseResponseModel.getStatus()) {
                    if (purchaseResponseModel.getData().getOrderId() != null && !purchaseResponseModel.getData().getOrderId().equalsIgnoreCase("")) {
                        orderId = purchaseResponseModel.getData().getOrderId();
                        Log.w("orderIdOf", orderId);
                        callInitiatePaymentApi(orderId);
                    }
                } else if (purchaseResponseModel.getResponseCode() == 4302) {
                    isloggedout = true;
                    dismissLoading(getBinding().progressBar);
                    showDialog(PurchaseActivity.this.getResources().getString(R.string.logged_out), purchaseResponseModel.getDebugMessage() == null ? "" : purchaseResponseModel.getDebugMessage().toString());

                } else {
                    dismissLoading(getBinding().progressBar);
                    showDialog(PurchaseActivity.this.getResources().getString(R.string.error), purchaseResponseModel.getDebugMessage() == null ? "" : purchaseResponseModel.getDebugMessage().toString());
                }
            } catch (Exception e) {

            }


        });


    }

    String paymentId;

    private void callInitiatePaymentApi(String orderId) {
        viewModel.callInitiatePaymet(strToken, orderId).observe(PurchaseActivity.this, purchaseResponseModel -> {
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
                    showDialog(PurchaseActivity.this.getResources().getString(R.string.logged_out), purchaseResponseModel.getDebugMessage() == null ? "" : purchaseResponseModel.getDebugMessage().toString());

                } else {
                    dismissLoading(getBinding().progressBar);
                    showDialog(PurchaseActivity.this.getResources().getString(R.string.error), purchaseResponseModel.getDebugMessage() == null ? "" : purchaseResponseModel.getDebugMessage().toString());
                }
            } catch (Exception e) {

            }


        });

    }


    private void getPlans() {
        showLoading(getBinding().progressBar, false);
        String token = preference.getAppPrefAccessToken();
        if (!StringUtils.isNullOrEmptyOrZero(token)) {
            viewModel.getPlans("").observe(PurchaseActivity.this, new Observer<ResponseMembershipAndPlan>() {
                @Override
                public void onChanged(@Nullable ResponseMembershipAndPlan responseMembershipAndPlan) {
                    dismissLoading(getBinding().progressBar);
                    Logger.e("MemberShipPlanActivity", "ResponseMembershipAndPlan" + responseMembershipAndPlan.toString());
                    if (responseMembershipAndPlan.isStatus()) {
                        if (responseMembershipAndPlan.getData().size() > 0) {
                            Logger.e("", "responseMembershipAndPlan" + responseMembershipAndPlan.getData().toString());
                            alPurchaseOptions = new ArrayList<>();
                            for (int i = 0; i < responseMembershipAndPlan.getData().size(); i++) {
                                if (responseMembershipAndPlan.getData().get(i).getTitle().equalsIgnoreCase("weekly") ||
                                        responseMembershipAndPlan.getData().get(i).getTitle().equalsIgnoreCase("monthly")) {

                                    if (responseMembershipAndPlan.getData().get(i).getEntitlementState()) {
                                        isAlreadySubscribed = true;
                                        subscribedPlanName = responseMembershipAndPlan.getData().get(i).getTitle();
                                    }
                                }
                            }
                        }

                    } else {
                        if (responseMembershipAndPlan.getResponseCode() == 401) {
                            showDialog(PurchaseActivity.this.getResources().getString(R.string.logged_out), getResources().getString(R.string.you_are_logged_out));
                        }
                    }

                }
            });

        } else {
            dismissLoading(getBinding().progressBar);
            Logger.e("MemberShipPlanActivity", "Token is empty/null");
        }
    }

    String billingError="";
    @Override
    public void onBillingError(@Nullable BillingResult error) {
        try {
            if (KsPreferenceKeys.getInstance().getAppPrefLoginStatus()){
                if (error != null && error.getDebugMessage() != null) {
                    billingError=error.getDebugMessage();
                    Log.w("billingError", error.getDebugMessage());
                }
                updatePayment(billingError,"FAILED","",paymentId);
            }

        }catch (Exception ignored){
            updatePayment(billingError,"FAILED","",paymentId);
        }

    }


    @Override
    public void onDestroy() {
        if (bp!=null && bp.isReady()) {
            bp.endConnection();
        }

        purchaseModel = null;
        responseEntitlementModel = null;
        response = null;
        dismissLoading(getBinding().progressBar);
        super.onDestroy();
    }

    public void setImage(String imageKey, String imageUrl, ImageView view) {
        try {

            String url1 = preference.getAppPrefCfep();
            if (StringUtils.isNullOrEmpty(url1)) {
                url1 = AppCommonMethod.urlPoints;
                preference.setAppPrefCfep(url1);
            }
            StringBuilder stringBuilder = new StringBuilder(imageKey);
            Logger.e("", "" + stringBuilder.toString());
            int itemHeight;
            DisplayMetrics displaymetrics = new DisplayMetrics();
            (PurchaseActivity.this).getWindowManager().getDefaultDisplay().getMetrics(displaymetrics);
            //if you need three fix imageview in width
            itemHeight = (int) (displaymetrics.heightPixels) / 3;
            boolean tabletSize = PurchaseActivity.this.getResources().getBoolean(R.bool.isTablet);
            if (tabletSize) {
                //landscape
                if (PurchaseActivity.this.getResources().getConfiguration().orientation == 2)
                    itemHeight = itemHeight + (itemHeight / 2);
            }
            view.getLayoutParams().height = itemHeight;


            Glide.with(PurchaseActivity.this)
                    .asBitmap()
                    .load(stringBuilder.toString())
                    .listener(new RequestListener<Bitmap>() {
                        @Override
                        public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Bitmap> target, boolean isFirstResource) {
                            return false;
                        }

                        @Override
                        public boolean onResourceReady(Bitmap resource, Object model, Target<Bitmap> target, com.bumptech.glide.load.DataSource dataSource, boolean isFirstResource) {
                            return false;
                        }
                    })
                    .apply(AppCommonMethod.optionsPlayer)
                    .into(view);


        } catch (Exception e) {
            Logger.e("", "" + e.toString());
        }

    }

    @Override
    public void onFinishDialog() {
        if (isloggedout) {
            if (CheckInternetConnection.isOnline(Objects.requireNonNull(PurchaseActivity.this))) {
                clearCredientials(preference);
                hitApiLogout(PurchaseActivity.this, preference.getAppPrefAccessToken());
            }
        }
    }


    PurchaseModel clickedModel;
    String initiateSKU="";
    @Override
    public void onPurchaseCardClick(boolean click, PurchaseModel model) {
        try {
        if (click) {
            String selectedPlanName = model.getPurchaseOptions();
            String idSKU=model.getIdentifier();
            if(model.getPurchaseOptions().equalsIgnoreCase(VodOfferType.RECURRING_SUBSCRIPTION​.name())){
                String s=idSKU.replace("svod_","");
                initiateSKU=s.replace("_",".");
            }else {
                initiateSKU=idSKU;
            }

            clickedModel = model;
            if (isAlreadySubscribed) {
                if (selectedPlanName.equalsIgnoreCase(VodOfferType.PERPETUAL.name())) {
                    getBinding().llNote.setVisibility(View.GONE);
                    getBinding().llUnsubscribe.setVisibility(View.GONE);
                    this.selectedPlanName = selectedPlanName;
                    final int sdk = android.os.Build.VERSION.SDK_INT;
                    if (sdk < android.os.Build.VERSION_CODES.JELLY_BEAN) {
                        getBinding().btnBuy.setBackgroundDrawable(ContextCompat.getDrawable(this, R.drawable.rounded_button));
                    } else {
                        getBinding().btnBuy.setBackground(ContextCompat.getDrawable(this, R.drawable.rounded_button));
                    }
                    //getBinding().btnBuy.setBackgroundColor(getApplicationContext().getResources().getColor(R.color.themeColorDark));
                    getBinding().btnBuy.setEnabled(true);
                    getBinding().btnBuy.setClickable(true);

                } else {
                    getBinding().llNote.setVisibility(View.VISIBLE);
                    getBinding().llUnsubscribe.setVisibility(View.VISIBLE);
                    StringBuilder subscribeText = new StringBuilder();
                    subscribeText.append(PurchaseActivity.this.getResources().getString(R.string.already_subscriber));
                    subscribeText.append(" " + subscribedPlanName);
                    subscribeText.append(" plan.");
                    getBinding().note1.setText(subscribeText);
                    getBinding().btnBuy.setBackgroundColor(getApplicationContext().getResources().getColor(R.color.disable_button_color));
                    getBinding().btnBuy.setEnabled(false);
                    getBinding().btnBuy.setClickable(false);
                }

            } else {
                this.selectedPlanName = selectedPlanName;
                final int sdk = android.os.Build.VERSION.SDK_INT;
                if (sdk < android.os.Build.VERSION_CODES.JELLY_BEAN) {
                    getBinding().btnBuy.setBackgroundDrawable(ContextCompat.getDrawable(this, R.drawable.rounded_button));
                } else {
                    getBinding().btnBuy.setBackground(ContextCompat.getDrawable(this, R.drawable.rounded_button));
                }
                // getBinding().btnBuy.setBackgroundColor(getApplicationContext().getResources().getColor(R.color.themeColorDark));
                getBinding().btnBuy.setEnabled(true);
                getBinding().btnBuy.setClickable(true);

            }
        } else {
            getBinding().llNote.setVisibility(View.GONE);
            getBinding().llUnsubscribe.setVisibility(View.GONE);
            final int sdk = android.os.Build.VERSION.SDK_INT;
            if (sdk < android.os.Build.VERSION_CODES.JELLY_BEAN) {
                getBinding().btnBuy.setBackgroundDrawable(ContextCompat.getDrawable(this, R.drawable.rounded_button));
            } else {
                getBinding().btnBuy.setBackground(ContextCompat.getDrawable(this, R.drawable.rounded_button));
            }
            // getBinding().btnBuy.setBackgroundColor(getApplicationContext().getResources().getColor(R.color.disable_button_color));
            getBinding().btnBuy.setEnabled(false);
            getBinding().btnBuy.setClickable(false);
        }
        }catch (Exception ignored){

        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        try {
            if (KsPreferenceKeys.getInstance().getAppPrefLoginStatus()){
                if (ActivityTrackers.getInstance().action.equalsIgnoreCase(ActivityTrackers.PURCHASE)){
                    ActivityTrackers.getInstance().setAction("");
                    if (contentType!=null && !contentType.equalsIgnoreCase("")){
                        if (contentType.equalsIgnoreCase(MediaTypeConstants.getInstance().getSeries()) || contentType.equalsIgnoreCase(MediaTypeConstants.getInstance().getTutorial())){
                            if (response.getSku()!=null && !response.getSku().equalsIgnoreCase("")){
                                hitApiEntitlement(response.getSku());
                            }
                        }else {
                            if (response.getSeriesSku()!=null && !response.getSeriesSku().equalsIgnoreCase("")){
                                hitApiEntitlement(response.getSeriesSku());
                            }
                        }
                    }

                }
            }
        }catch (Exception e){

        }

    }

    @Override
    protected void onPause() {
        super.onPause();
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
          Intent intentEmail= new Intent(Intent.ACTION_SENDTO);
            intentEmail.setData(Uri.parse("mailto:info@panteaoproductions.com"));
            startActivity(intentEmail);

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

    public void hitApiEntitlement(String sku) {
        try {
            String token = KsPreferenceKeys.getInstance().getAppPrefAccessToken();
            if (token != null && !token.equalsIgnoreCase("")) {
                viewModel.hitApiEntitlement(token, sku).observe(PurchaseActivity.this, responseEntitlement -> {
                    responseEntitlementModel = responseEntitlement;
                    if (responseEntitlement.getStatus()) {
                        if (responseEntitlement.getData().getEntitledAs() != null) {
                            AppCommonMethod.isPurchase=true;
                            onBackPressed();
                        }else {
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    Intent intent = new Intent(PurchaseActivity.this, PurchaseActivity.class);
                                    intent.putExtra("response", response);
                                    intent.putExtra("assestId", assetId);
                                    intent.putExtra("contentType", contentType);
                                    intent.putExtra("responseEntitlement", responseEntitlementModel);
                                    if (responseEntitlementModel != null) {
                                        startActivity(intent);
                                    }
                                    finish();
                                }
                            });

                        }
                    } else {
                        if (responseEntitlementModel != null && responseEntitlementModel.getResponseCode() != null && responseEntitlementModel.getResponseCode() > 0 && responseEntitlementModel.getResponseCode() == 4302) {
                            isloggedout = true;
                            // logoutUser();
                            showDialog(PurchaseActivity.this.getResources().getString(R.string.logged_out), responseEntitlementModel.getDebugMessage() == null ? "" : responseEntitlementModel.getDebugMessage().toString());
                        }
                    }
                });

            } else {
                onBackPressed();
            }
        }catch (Exception ignored){

        }
    }

}
