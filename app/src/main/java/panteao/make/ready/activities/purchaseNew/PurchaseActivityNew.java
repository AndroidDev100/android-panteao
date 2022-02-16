package panteao.make.ready.activities.purchaseNew;

import android.os.Bundle;
import android.util.Log;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.android.billingclient.api.BillingClient;
import com.android.billingclient.api.BillingClientStateListener;
import com.android.billingclient.api.BillingFlowParams;
import com.android.billingclient.api.BillingResult;
import com.android.billingclient.api.Purchase;
import com.android.billingclient.api.PurchasesUpdatedListener;
import com.android.billingclient.api.SkuDetails;
import com.android.billingclient.api.SkuDetailsParams;
import com.android.billingclient.api.SkuDetailsResponseListener;

import panteao.make.ready.baseModels.BaseBindingActivity;
import panteao.make.ready.databinding.PurchaseBinding;
import panteao.make.ready.utils.cropImage.helpers.PrintLogging;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class PurchaseActivityNew extends BaseBindingActivity<PurchaseBinding> implements PurchasesUpdatedListener{


    @Override
    public PurchaseBinding inflateBindingLayout() {
        return PurchaseBinding.inflate(inflater);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getBinding().btnBuy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (skuDetails!=null){
                    initiatePurchaseFlow();
                }
            }
        });
        initializeBillingProcessor();
    }

    /** A reference to BillingClient */
    private BillingClient myBillingClient=null;
    private void initializeBillingProcessor() {
        myBillingClient =
                BillingClient.newBuilder(PurchaseActivityNew.this)
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
            );
        }
    }

    private void querySkuDetails() {
        Map<String, SkuDetails> skuResultMap = new HashMap<>();
        List<String> subscriptionSkuList = BillingConstants.getSkuList(BillingClient.SkuType.SUBS);
        SkuDetailsParams.Builder params = SkuDetailsParams.newBuilder();
        params.setSkusList(subscriptionSkuList).setType(BillingClient.SkuType.SUBS);
        querySkuDetailsAsync(
        );
    }

    /**
     * Makes connection with BillingClient.
     *
     */
    private void startServiceConnection() {
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
                        logErrorType();
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
    public static final String TAG = PurchaseActivityNew.class.getName();
    private void logErrorType() {
        switch (billingResult.getResponseCode()) {
            case BillingClient.BillingResponseCode.DEVELOPER_ERROR:
            case BillingClient.BillingResponseCode.BILLING_UNAVAILABLE:
                PrintLogging.printLog(
                        "Billing unavailable. Make sure your Google Play app is setup correctly");
                break;
            case BillingClient.BillingResponseCode.SERVICE_DISCONNECTED:
                //notifyBillingError(R.string.err_service_disconnected);
                connectToPlayBillingService();
                break;
            case BillingClient.BillingResponseCode.OK:
                PrintLogging.printLog("Setup successful!");
                break;
            case BillingClient.BillingResponseCode.USER_CANCELED:
                PrintLogging.printLog("User has cancelled Purchase!");
                break;
            case BillingClient.BillingResponseCode.SERVICE_UNAVAILABLE:
                //notifyBillingError(R.string.err_no_internet);
                break;
            case BillingClient.BillingResponseCode.ITEM_UNAVAILABLE:
                PrintLogging.printLog("Product is not available for purchase");
                break;
            case BillingClient.BillingResponseCode.ERROR:
                PrintLogging.printLog("fatal error during API action");
                break;
            case BillingClient.BillingResponseCode.ITEM_ALREADY_OWNED:
                PrintLogging.printLog("Failure to purchase since item is already owned");
               // queryPurchasesLocally();
                break;
            case BillingClient.BillingResponseCode.ITEM_NOT_OWNED:
                PrintLogging.printLog("Failure to consume since item is not owned");
                break;
            case BillingClient.BillingResponseCode.FEATURE_NOT_SUPPORTED:
                PrintLogging.printLog("Billing feature is not supported on your device");
                break;
            case BillingClient.BillingResponseCode.SERVICE_TIMEOUT:
                PrintLogging.printLog("Billing service timeout occurred");
                break;
            default:
                PrintLogging.printLog("Billing unavailable. Please check your device");
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
        PrintLogging.printLog("onPurchasesUpdate() responseCode: " + billingResult.getResponseCode());
        if (billingResult.getResponseCode() == BillingClient.BillingResponseCode.OK && purchases != null) {
            processPurchases();
        } else {
            // Handle any other error codes.
            logErrorType();
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
    private void querySkuDetailsAsync() {
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
        executeServiceRequest();
    }

    private void executeServiceRequest() {
        if (myBillingClient.isReady()) {
            runnable.run();
        } else {
            // If billing service was disconnected, we try to reconnect 1 time.
            // (feel free to introduce your retry policy here).
            startServiceConnection();
        }
    }



    /**
     * Stores Purchased Items, consumes consumable items, acknowledges non-consumable items.
     *
     */
    private void processPurchases() {
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
               // acknowledgeNonConsumablePurchasesAsync(purchase);
            }
        }
    }

    public void initiatePurchaseFlow() {
        if (skuDetails.getType().equals(BillingClient.SkuType.SUBS) && areSubscriptionsSupported()
                || skuDetails.getType().equals(BillingClient.SkuType.INAPP)) {
            final BillingFlowParams purchaseParams =
                    BillingFlowParams.newBuilder().setSkuDetails(skuDetails).build();
            executeServiceRequest(
            );
        }
    }

    private boolean areSubscriptionsSupported() {
        final BillingResult billingResult =
                myBillingClient.isFeatureSupported(BillingClient.FeatureType.SUBSCRIPTIONS);
        if (billingResult.getResponseCode() != BillingClient.BillingResponseCode.OK) {
            PrintLogging.printLog(
                    "areSubscriptionsSupported() got an error response: "
                            + billingResult.getResponseCode());
          //  notifyBillingError(R.string.err_subscription_not_supported);
        }
        return billingResult.getResponseCode() == BillingClient.BillingResponseCode.OK;
    }


}
