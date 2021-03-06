package panteao.make.ready.utils.inAppBilling;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.android.billingclient.api.AcknowledgePurchaseParams;
import com.android.billingclient.api.AcknowledgePurchaseResponseListener;
import com.android.billingclient.api.BillingClient;
import com.android.billingclient.api.BillingClientStateListener;
import com.android.billingclient.api.BillingFlowParams;
import com.android.billingclient.api.BillingResult;
import com.android.billingclient.api.ConsumeParams;
import com.android.billingclient.api.ConsumeResponseListener;
import com.android.billingclient.api.Purchase;
import com.android.billingclient.api.PurchasesUpdatedListener;
import com.android.billingclient.api.SkuDetails;
import com.android.billingclient.api.SkuDetailsParams;
import com.android.billingclient.api.SkuDetailsResponseListener;

import panteao.make.ready.activities.purchaseNew.BillingConstants;
import panteao.make.ready.utils.cropImage.helpers.PrintLogging;
import panteao.make.ready.utils.helpers.ksPreferenceKeys.KsPreferenceKeys;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class BillingProcessor implements PurchasesUpdatedListener {
    private static WeakReference<Activity> mActivity;
    private BillingClient myBillingClient=null;
    private final InAppProcessListener inAppProcessListener;

    public BillingProcessor(Activity activity,InAppProcessListener billingCallBacks) {
        mActivity = new WeakReference<>(activity);
        inAppProcessListener=billingCallBacks;
    }

    public static boolean isIabServiceAvailable(Activity context) {
        final PackageManager packageManager = context.getPackageManager();
        List<ResolveInfo> list = packageManager.queryIntentServices(getBindServiceIntent(), 0);
        return list != null && list.size() > 0;
    }

    private static Intent getBindServiceIntent()
    {
        Intent intent = new Intent("com.android.vending.billing.InAppBillingService.BIND");
        intent.setPackage("com.android.vending");
        return intent;
    }

    /** A reference to BillingClient */
    public void initializeBillingProcessor() {
        myBillingClient =
                BillingClient.newBuilder(mActivity.get())
                        .enablePendingPurchases()
                        .setListener(this)
                        .build();
        // clears billing manager when the jvm exits or gets terminated.
        Runtime.getRuntime().addShutdownHook(new Thread(this::destroy));
        // starts play billing service connection
        connectToPlayBillingService();
    }

    /** Initiates Google Play Billing Service. */
    private void connectToPlayBillingService() {
        PrintLogging.printLog("connectToPlayBillingService");
        if (!myBillingClient.isReady()) {
            startServiceConnection(
                    () -> {
                        // IAB is fully set up. Now, let's get an inventory of stuff we own.
                        PrintLogging.printLog("Setup successful. Querying inventory.");
                        if (inAppProcessListener!=null)
                        inAppProcessListener.onBillingInitialized();
                       // querySkuDetails();
                        // queryPurchaseHistoryAsync();
                        // queryPurchasesLocally();
                    });
        }
    }

    private void querySkuDetails() {
        Map<String, SkuDetails> skuResultMap = new HashMap<>();
        List<String> subscriptionSkuList = BillingConstants.getSkuList(BillingClient.SkuType.SUBS);
        SkuDetailsParams.Builder params = SkuDetailsParams.newBuilder();
        params.setSkusList(subscriptionSkuList).setType(BillingClient.SkuType.SUBS);
        querySkuDetailsAsync(
                skuResultMap,
                params,
                BillingClient.SkuType.SUBS,
                () -> {
                    List<String> inAppSkuList = BillingConstants.getSkuList(BillingClient.SkuType.INAPP);
                    SkuDetailsParams.Builder params1 = SkuDetailsParams.newBuilder();
                    params1.setSkusList(inAppSkuList).setType(BillingClient.SkuType.INAPP);
                    querySkuDetailsAsync(skuResultMap, params1, BillingClient.SkuType.INAPP, null);
                });
    }

    /**
     * Makes connection with BillingClient.
     *
     * @param executeOnSuccess A runnable implementation.
     */
    private void startServiceConnection(Runnable executeOnSuccess) {
        myBillingClient.startConnection(
                new BillingClientStateListener() {
                    @Override
                    public void onBillingSetupFinished(@NonNull BillingResult billingResult) {
                        // The billing client is ready. You can query purchases here.
                        PrintLogging.printLog("Setup finished");
                        if (billingResult.getResponseCode() == BillingClient.BillingResponseCode.OK) {
                            if (executeOnSuccess != null) {
                                executeOnSuccess.run();
                            }
                        }
                        logErrorType(billingResult);
                    }

                    @Override
                    public void onBillingServiceDisconnected() {
                        // Try to restart the connection on the next request to
                        // Google Play by calling the startConnection() method.
                    }
                });
    }

    /**
     * Logs Billing Client Success, Failure and error responses.
     *
     * @param billingResult to identify the states of Billing Client Responses.
     * @see <a
     *     href="https://developer.android.com/google/play/billing/billing_reference.html">Google
     *     Play InApp Purchase Response Types Guide</a>
     */
    public static final String TAG = BillingProcessor.class.getName();
    private void logErrorType(BillingResult billingResult) {
        switch (billingResult.getResponseCode()) {
            case BillingClient.BillingResponseCode.DEVELOPER_ERROR:
                inAppProcessListener.onBillingError();
            case BillingClient.BillingResponseCode.BILLING_UNAVAILABLE:
                PrintLogging.printLog(
                        "Billing unavailable. Make sure your Google Play app is setup correctly");
                inAppProcessListener.onBillingError();
                break;
            case BillingClient.BillingResponseCode.SERVICE_DISCONNECTED:
                //notifyBillingError(R.string.err_service_disconnected);
               // connectToPlayBillingService();
                inAppProcessListener.onBillingError();
                break;
            case BillingClient.BillingResponseCode.OK:
                PrintLogging.printLog("Setup successful!");
                break;
            case BillingClient.BillingResponseCode.USER_CANCELED:
                PrintLogging.printLog("User has cancelled Purchase!");
                inAppProcessListener.onBillingError();
                break;
            case BillingClient.BillingResponseCode.SERVICE_UNAVAILABLE:
                //notifyBillingError(R.string.err_no_internet);
                inAppProcessListener.onBillingError();
                break;
            case BillingClient.BillingResponseCode.ITEM_UNAVAILABLE:
                PrintLogging.printLog("Product is not available for purchase");
                inAppProcessListener.onBillingError();
                break;
            case BillingClient.BillingResponseCode.ERROR:
                PrintLogging.printLog("fatal error during API action");
                inAppProcessListener.onBillingError();
                break;
            case BillingClient.BillingResponseCode.ITEM_ALREADY_OWNED:
                PrintLogging.printLog("Failure to purchase since item is already owned");
                inAppProcessListener.onBillingError();
                // queryPurchasesLocally();
                break;
            case BillingClient.BillingResponseCode.ITEM_NOT_OWNED:
                PrintLogging.printLog("Failure to consume since item is not owned");
                inAppProcessListener.onBillingError();
                break;
            case BillingClient.BillingResponseCode.FEATURE_NOT_SUPPORTED:
                PrintLogging.printLog("Billing feature is not supported on your device");
                inAppProcessListener.onBillingError();
                break;
            case BillingClient.BillingResponseCode.SERVICE_TIMEOUT:
                PrintLogging.printLog("Billing service timeout occurred");
                inAppProcessListener.onBillingError();
                break;
            default:
                PrintLogging.printLog("Billing unavailable. Please check your device");
                inAppProcessListener.onBillingError();
                break;
        }
    }

    private void destroy() {
        PrintLogging.printLog("Destroying the billing manager.");
        if (myBillingClient.isReady()) {
            myBillingClient.endConnection();
        }
        // networkManager.removeCallback(this);
    }

    @Override
    public void onPurchasesUpdated(@NonNull BillingResult billingResult, @Nullable List<Purchase> purchases) {
       // PrintLogging.printLog(TAG, "onPurchasesUpdate() responseCode: " + billingResult.getResponseCode());
        String purchaseType=PurchaseType.SUBSCRIPTION.name();
        if (billingResult.getResponseCode() == BillingClient.BillingResponseCode.OK && purchases != null) {
            if (inAppProcessListener!=null){
                try {
                    if (purchases.get(0).getPurchaseToken() != null) {
                        for (Purchase purchase : purchases) {
                            if (purchase.getPurchaseState() == Purchase.PurchaseState.PURCHASED) {
                                Log.w("productType",productType);
                                if (productType!=null && !productType.equalsIgnoreCase("") &productType.equalsIgnoreCase(PurchaseType.PRODUCT.name())){
                                   handleConsumablePurchasesAsync(purchase);
                                }
                            }
                        }
                    }

                    }catch (Exception e){
                    Log.w("productType",e.toString());
                }
               /* try {
                    queryPurchasesForConsume();
                }catch (Exception e){

                }*/
                inAppProcessListener.onPurchasesUpdated();
            }
          //  processPurchases(purchases);


            try {
                if (purchaseType!=null && !purchaseType.equalsIgnoreCase("") && purchaseType.equalsIgnoreCase(PurchaseType.SUBSCRIPTION.name())){
                    if (myBillingClient!=null && myBillingClient.isReady()){
                        if (billingResult.getResponseCode() == BillingClient.BillingResponseCode.OK && purchases != null) {
                            if (purchases.get(0).getPurchaseToken() != null) {
                                for (Purchase purchase : purchases) {
                                    if (purchase.getPurchaseState() == Purchase.PurchaseState.PURCHASED) {
                                        acknowledgeNonConsumablePurchasesAsync(purchase);
                                    }
                                }
                            }
                        }

                    }
                }
            }catch (Exception e){

            }


        } else {
            // Handle any other error codes.
            logErrorType(billingResult);
        }
    }

    private void handleConsumablePurchasesAsync(Purchase purchase) {
        try {
            // Generating Consume Response listener
            final ConsumeResponseListener listener =
                    (billingResult, purchaseToken) -> {
                        // If billing service was disconnected, we try to reconnect 1 time
                        // (feel free to introduce your retry policy here).
                        try {
                            Log.w("tvod",billingResult.getDebugMessage()+" "+billingResult.getResponseCode());
                            if (billingResult.getResponseCode() == BillingClient.BillingResponseCode.OK) {
                                Log.w("tvod","consumed");
                            } else {
                                Log.w("tvod","failed");
                            }
                        }catch (Exception e){

                        }

                    };
            // Consume the purchase async
            final ConsumeParams consumeParams =
                    ConsumeParams.newBuilder().setPurchaseToken(purchase.getPurchaseToken()).build();
            // Creating a runnable from the request to use it inside our connection retry policy below
            executeServiceRequest(() -> myBillingClient.consumeAsync(consumeParams, listener));
        }catch (Exception e){

        }
    }


    /**
     * Queries SKU Details from Google Play Remote Server of SKU Types (InApp and Subscription).
     *
     * @param skuResultLMap contains SKU ID and Price Details returned by the sku details query.
     * @param params contains list of SKU IDs and SKU Type (InApp or Subscription).
     * @param billingType InApp or Subscription.
     * @param executeWhenFinished contains query for InApp SKU Details that will be run after
     */
    SkuDetails skuDetails=null;
    private void querySkuDetailsAsync(
            Map<String, SkuDetails> skuResultLMap,
            SkuDetailsParams.Builder params,
            @BillingClient.SkuType String billingType,
            Runnable executeWhenFinished) {
        final SkuDetailsResponseListener listener =
                (billingResult, skuDetailsList) -> {
                    // Process the result.
                    if (billingResult.getResponseCode() != BillingClient.BillingResponseCode.OK) {
                        PrintLogging.printLog(
                                "Unsuccessful query for type: "
                                        + billingType
                                        + ". Error code: "
                                        + billingResult.getResponseCode());
                    } else if (skuDetailsList != null && skuDetailsList.size() > 0) {
                        for (SkuDetails skuDetails : skuDetailsList) {
                            Log.w("skuDetails",skuDetails.getPrice()+"-->>"+skuDetails.getPriceCurrencyCode());
                            this.skuDetails=skuDetails;
                            skuResultLMap.put(skuDetails.getSku(), skuDetails);
                        }
                    }
                    if (executeWhenFinished != null) {
                        executeWhenFinished.run();
                        return;
                    }
                    if (skuResultLMap.size() == 0) {
                        PrintLogging.printLog(
                                "sku error: " + "nosku");
                    } else {
                        PrintLogging.printLog("storing sku list locally");
                        //storeSkuDetailsLocally(skuResultLMap);
                    }
                };
        // Creating a runnable from the request to use it inside our connection retry policy below
        executeServiceRequest(() -> myBillingClient.querySkuDetailsAsync(params.build(), listener));
    }

    private void executeServiceRequest(Runnable runnable) {
        if (myBillingClient.isReady()) {
            runnable.run();
        } else {
            // If billing service was disconnected, we try to reconnect 1 time.
            // (feel free to introduce your retry policy here).
            startServiceConnection(runnable);
        }
    }



    /**
     * Stores Purchased Items, consumes consumable items, acknowledges non-consumable items.
     *
     * @param purchases list of Purchase Details returned from the queries.
     */
    private void processPurchases(@NonNull List<Purchase> purchases) {
        if (purchases.size() > 0) {
            PrintLogging.printLog("purchase list size: " + purchases.size());
        }
        for (Purchase purchase : purchases) {
            if (purchase.getPurchaseState() == Purchase.PurchaseState.PURCHASED) {
                Log.w("purchaseToken-->>>",purchase.getPurchaseToken());
                // handlePurchase(purchase);
            } else if (purchase.getPurchaseState() == Purchase.PurchaseState.PENDING) {
                PrintLogging.printLog(
                        "Received a pending purchase of SKU: " + purchase.getSku());
                // handle pending purchases, e.g. confirm with users about the pending
                // purchases, prompt them to complete it, etc.
                // TODO: 8/24/2020 handle this in the next release.
            }
        }
        //storePurchaseResultsLocally(myPurchasesResultList);
        for (Purchase purchase : purchases) {
            if (purchase.getSku().equals(BillingConstants.SKU_BUY_APPLE)) {
                // handleConsumablePurchasesAsync(purchase);
            } else {
                 acknowledgeNonConsumablePurchasesAsync(purchase);
            }
        }
    }

    public void acknowledgeNonConsumablePurchasesAsync(Purchase purchase) {
        final AcknowledgePurchaseParams params =
                AcknowledgePurchaseParams.newBuilder()
                        .setPurchaseToken(purchase.getPurchaseToken())
                        .build();
        final AcknowledgePurchaseResponseListener listener =
                billingResult -> {
                    if (billingResult.getResponseCode() == BillingClient.BillingResponseCode.OK) {
                        PrintLogging.printLog(
                                "onAcknowledgePurchaseResponse: "
                                        + billingResult.getResponseCode());
                    } else {
                        PrintLogging.printLog(
                                "onAcknowledgePurchaseResponse: "
                                        + billingResult.getDebugMessage());
                    }
                };
        executeServiceRequest(() -> myBillingClient.acknowledgePurchase(params, listener));
    }


    BillingFlowParams purchaseParams;
    public void initiatePurchaseFlow(@NonNull Activity activity, @NonNull SkuDetails skuDetails) {
        try {
            if (skuDetails.getType().equals(BillingClient.SkuType.SUBS) && areSubscriptionsSupported()
                    || skuDetails.getType().equals(BillingClient.SkuType.INAPP)) {
                if (KsPreferenceKeys.getInstance().getAppPrefUserId()!=null && !KsPreferenceKeys.getInstance().getAppPrefUserId().equalsIgnoreCase("")){
                    purchaseParams =
                            BillingFlowParams.newBuilder().setSkuDetails(skuDetails).setObfuscatedAccountId(KsPreferenceKeys.getInstance().getAppPrefUserId()).build();
                }else {
                    purchaseParams  =
                            BillingFlowParams.newBuilder().setSkuDetails(skuDetails).build();
                }

                executeServiceRequest(
                        () -> {
                            PrintLogging.printLog("Launching in-app purchase flow.");
                            myBillingClient.launchBillingFlow(activity, purchaseParams);
                        });
            }

        }catch (Exception e){

        }
    }

    private boolean areSubscriptionsSupported() {
        final BillingResult billingResult =
                myBillingClient.isFeatureSupported(BillingClient.FeatureType.SUBSCRIPTIONS);
        if (billingResult.getResponseCode() != BillingClient.BillingResponseCode.OK) {
            PrintLogging.printLog(
                    "areSubscriptionsSupported() got an error response: "
                            + billingResult.getResponseCode());
            if (inAppProcessListener!=null){
                inAppProcessListener.onBillingError();
            }
            //  notifyBillingError(R.string.err_subscription_not_supported);
        }
        return billingResult.getResponseCode() == BillingClient.BillingResponseCode.OK;
    }

    String purchaseType="";
    String productType="";
    public void purchase(Activity activity, String sku, String purchaseType) {
        this.purchaseType="";
        this.productType="";
        if (purchaseType.equalsIgnoreCase(PurchaseType.PRODUCT.name())){
            this.purchaseType=PurchaseType.PRODUCT.name();
            this.productType=PurchaseType.PRODUCT.name();
            if (myBillingClient!=null && myBillingClient.isReady()){
                getProductSkuDetails(activity,sku);
            }
        }else {
            if (myBillingClient!=null && myBillingClient.isReady()){
                this.productType=PurchaseType.SUBSCRIPTION.name();
                getSubscriptionSkuDetails(activity,sku);
            }
        }
    }

    public void getProductSkuDetails(Activity activity, String sku) {
        List<String> skuList = new ArrayList<>();
        skuList.add(sku);
        SkuDetailsParams.Builder params = SkuDetailsParams.newBuilder();
        params.setSkusList(skuList).setType(BillingClient.SkuType.INAPP);
        myBillingClient.querySkuDetailsAsync(params.build(),
                new SkuDetailsResponseListener() {
                    @Override
                    public void onSkuDetailsResponse(BillingResult billingResult,
                                                     List<SkuDetails> skuDetailsList) {
                        if (skuDetailsList != null && skuDetailsList.size() > 0) {
                            for (SkuDetails skuDetails : skuDetailsList) {
                                Log.w("skuDetails", skuDetails.getPrice() + "-->>" + skuDetails.getPriceCurrencyCode());
                                if (skuDetails.getSku().equalsIgnoreCase(sku)){
                                    initiatePurchaseFlow(activity,skuDetails);
                                }
                            }
                        }
                    }
                });
    }

    public void getSubscriptionSkuDetails(Activity activity, String sku) {
        List<String> skuList = new ArrayList<>();
        skuList.add(sku);
        SkuDetailsParams.Builder params = SkuDetailsParams.newBuilder();
        params.setSkusList(skuList).setType(BillingClient.SkuType.SUBS);
        myBillingClient.querySkuDetailsAsync(params.build(),
                new SkuDetailsResponseListener() {
                    @Override
                    public void onSkuDetailsResponse(BillingResult billingResult,
                                                     List<SkuDetails> skuDetailsList) {
                        if (skuDetailsList != null && skuDetailsList.size() > 0) {
                            for (SkuDetails skuDetails : skuDetailsList) {
                                Log.w("skuDetails", skuDetails.getPrice() + "-->>" + skuDetails.getPriceCurrencyCode());
                                if (skuDetails.getSku().equalsIgnoreCase(sku)){
                                    initiatePurchaseFlow(activity,skuDetails);
                                }
                            }
                        }
                    }
                });
    }

    public SkuDetails getProductSkuDetail(String sku) {
        final SkuDetails[] skuDetail=new SkuDetails[1];
        List<String> skuList = new ArrayList<>();
        skuList.add(sku);
        SkuDetailsParams.Builder params = SkuDetailsParams.newBuilder();
        params.setSkusList(skuList).setType(BillingClient.SkuType.INAPP);
        myBillingClient.querySkuDetailsAsync(params.build(),
                new SkuDetailsResponseListener() {
                    @Override
                    public void onSkuDetailsResponse(BillingResult billingResult,
                                                     List<SkuDetails> skuDetailsList) {
                        if (skuDetailsList != null && skuDetailsList.size() > 0) {
                            for (SkuDetails skuDetails : skuDetailsList) {
                                Log.w("skuDetails", skuDetails.getPrice() + "-->>" + skuDetails.getPriceCurrencyCode());
                                if (skuDetails.getSku().equalsIgnoreCase(sku)){
                                   // initiatePurchaseFlow(activity,skuDetails);
                                    skuDetail[0]=skuDetails;
                                }
                            }
                        }
                    }
                });
        return skuDetails;
    }


    public SkuDetails getSubscriptionSkuDetail(String sku) {
        final SkuDetails[] skuDetail=new SkuDetails[1];
        List<String> skuList = new ArrayList<>();
        skuList.add(sku);
        SkuDetailsParams.Builder params = SkuDetailsParams.newBuilder();
        params.setSkusList(skuList).setType(BillingClient.SkuType.SUBS);
        myBillingClient.querySkuDetailsAsync(params.build(),
                new SkuDetailsResponseListener() {
                    @Override
                    public void onSkuDetailsResponse(BillingResult billingResult,
                                                     List<SkuDetails> skuDetailsList) {
                        if (skuDetailsList != null && skuDetailsList.size() > 0) {
                            for (SkuDetails skuDetails : skuDetailsList) {
                                Log.w("skuDetails", skuDetails.getPrice() + "-->>" + skuDetails.getPriceCurrencyCode());
                                Log.w("skuDetails",  sku+"-->>" + skuDetails.getSku());
                                if (skuDetails.getSku().equalsIgnoreCase(sku)){
                                    // initiatePurchaseFlow(activity,skuDetails);
                                    skuDetail[0]=skuDetails;
                                }
                            }
                        }
                    }
                });
       return skuDetails;
    }


    public boolean isReady() {
        if (myBillingClient!=null){
            return myBillingClient.isReady();
        }else {
            return false;
        }

    }

    public void endConnection() {
        if (myBillingClient!=null && myBillingClient.isReady()){
            myBillingClient.endConnection();
        }
    }
    List<SkuDetails> listOfllSkus;
    public void getAllSkuDetails(List<String> subSkuList, List<String> productSkuList) {
        listOfllSkus=new ArrayList<>();
        SkuDetailsParams.Builder params = SkuDetailsParams.newBuilder();
        params.setSkusList(subSkuList).setType(BillingClient.SkuType.SUBS);
        myBillingClient.querySkuDetailsAsync(params.build(),
                new SkuDetailsResponseListener() {
                    @Override
                    public void onSkuDetailsResponse(BillingResult billingResult,
                                                     List<SkuDetails> skuDetailsList) {
                        if (skuDetailsList != null && skuDetailsList.size() > 0) {
                            for (SkuDetails skuDetails : skuDetailsList) {
                                 Log.w("skuDetails", skuDetails.getPrice() + "-->>" + skuDetails.getPriceCurrencyCode());
                                listOfllSkus.add(skuDetails);
                            }
                            fetchAllProducts(productSkuList,listOfllSkus);
                        }else {
                            fetchAllProducts(productSkuList,listOfllSkus);
                        }
                    }
                });

    }

    private void fetchAllProducts(List<String> productSkuList, List<SkuDetails> listOfllSkus) {
        SkuDetailsParams.Builder params = SkuDetailsParams.newBuilder();
        params.setSkusList(productSkuList).setType(BillingClient.SkuType.INAPP);
        myBillingClient.querySkuDetailsAsync(params.build(),
                new SkuDetailsResponseListener() {
                    @Override
                    public void onSkuDetailsResponse(BillingResult billingResult,
                                                     List<SkuDetails> skuDetailsList) {
                        if (skuDetailsList != null && skuDetailsList.size() > 0) {
                            for (SkuDetails skuDetails : skuDetailsList) {
                                Log.w("skuDetails", skuDetails.getPrice() + "-->>" + skuDetails.getPriceCurrencyCode());
                                listOfllSkus.add(skuDetails);
                                inAppProcessListener.onListOfSKUFetched();
                            }
                        }else {
                            inAppProcessListener.onListOfSKUFetched();
                        }
                    }
                });
    }

    public List<SkuDetails> getListOfllSkus() {
        return listOfllSkus;
    }

    public SkuDetails getLocalSubscriptionSkuDetail(String identifier) {
        try {
            if (getListOfllSkus()!=null && getListOfllSkus().size()>0){
                for (int i=0;i<getListOfllSkus().size();i++){
                    if (identifier.equalsIgnoreCase(getListOfllSkus().get(i).getSku())){
                        return getListOfllSkus().get(i);
                    }
                }
            }
        }catch (Exception ignored){

        }

        return null;
    }

    public void getAllSkuSubscriptionDetails(List<String> subSkuList) {
        listOfllSkus=new ArrayList<>();
        SkuDetailsParams.Builder params = SkuDetailsParams.newBuilder();
        params.setSkusList(subSkuList).setType(BillingClient.SkuType.SUBS);
        myBillingClient.querySkuDetailsAsync(params.build(),
                new SkuDetailsResponseListener() {
                    @Override
                    public void onSkuDetailsResponse(BillingResult billingResult,
                                                     List<SkuDetails> skuDetailsList) {
                        if (skuDetailsList != null && skuDetailsList.size() > 0) {
                            for (SkuDetails skuDetails : skuDetailsList) {
                                Log.w("skuDetails", skuDetails.getPrice() + "-->>" + skuDetails.getPriceCurrencyCode());
                                listOfllSkus.add(skuDetails);
                            }
                        }
                        inAppProcessListener.onListOfSKUFetched();
                    }
                });
    }

    public boolean isOneTimePurchaseSupported() {
        final BillingResult billingResult= myBillingClient.isFeatureSupported(BillingClient.FeatureType.SUBSCRIPTIONS);
        return billingResult.getResponseCode() != BillingClient.BillingResponseCode.OK;
    }


    String purchasedSKU="";
    String purchasedToken="";
    RestoreSubscriptionCallback callback;
    public void queryPurchases(RestoreSubscriptionCallback calling) {
        try {
            this.callback=calling;
            if (KsPreferenceKeys.getInstance().getAppPrefLoginStatus()) {
                if (myBillingClient!=null){
                    final Purchase.PurchasesResult purchasesResult =
                            myBillingClient.queryPurchases(BillingClient.SkuType.SUBS);

                    final List<Purchase> purchases = new ArrayList<>();
                    if (purchasesResult.getPurchasesList() != null) {
                        purchases.addAll(purchasesResult.getPurchasesList());
                    }
                    if (purchases.size()>0){
                        purchasedSKU=purchases.get(0).getSku();
                        purchasedToken=purchases.get(0).getPurchaseToken();
                    }

                  //  Log.w("purchasedata",purchases.get(0).getOriginalJson());

                    PurchaseHandler.getInstance().checkPurchaseHistory(purchases,myBillingClient,callback);
                }
            }
        }catch (Exception ignored){
            Log.w("crashHap",ignored.toString());
        }


    }

    public void queryPurchasesForConsume() {
        try {
            if (KsPreferenceKeys.getInstance().getAppPrefLoginStatus()) {
                if (!KsPreferenceKeys.getInstance().getConsumeProduct()){
                    Log.w("consumeCall",KsPreferenceKeys.getInstance().getConsumeProduct()+"");
                    if (myBillingClient!=null){
                        final Purchase.PurchasesResult purchasesResult =
                                myBillingClient.queryPurchases(BillingClient.SkuType.INAPP);

                        if (purchasesResult.getPurchasesList() != null && purchasesResult.getPurchasesList().size()>0) {
                            for (Purchase purchase : purchasesResult.getPurchasesList()) {
                                handleConsumablePurchasesAsync(purchase);
                            }
                            KsPreferenceKeys.getInstance().setConsumeProduct(true);
                        }
                    }
                }
            }
        }catch (Exception ignored){
            Log.w("crashHap",ignored.toString());
        }
    }

    public void queryPurchases() {
        try {
            if (KsPreferenceKeys.getInstance().getAppPrefLoginStatus()) {
                if (myBillingClient!=null){
                    final Purchase.PurchasesResult purchasesResult =
                            myBillingClient.queryPurchases(BillingClient.SkuType.SUBS);

                    final List<Purchase> purchases = new ArrayList<>();
                    if (purchasesResult.getPurchasesList() != null) {
                        purchases.addAll(purchasesResult.getPurchasesList());
                    }
                    if (purchases.size()>0){
                        purchasedSKU=purchases.get(0).getSku();
                        purchasedToken=purchases.get(0).getPurchaseToken();
                    }

                    PurchaseHandling.getInstance().checkPurchaseHistory(purchases,myBillingClient);
                }
            }
        }catch (Exception ignored){
            Log.w("crashHap",ignored.toString());
        }
    }

}
