package panteao.make.ready.activities.purchaseNew;

import com.android.billingclient.api.BillingClient;

import java.util.Arrays;
import java.util.List;

public class BillingConstants {

    // SKUs for our managed products
    // One time purchase (valid for lifetime)
    public static final String SKU_UNLOCK_APP_FEATURES = "app_premium_feature";
    // can be purchased many times by consuming
    public static final String SKU_BUY_APPLE = "one_apple";
    // SKU for our subscription
    private static final String SKU_POPCORN_UNLIMITED_MONTHLY = "svod_monthly_recurring_subscription";
    private static final String[] IN_APP_SKU = {SKU_UNLOCK_APP_FEATURES, SKU_BUY_APPLE};
    private static final String[] SUBSCRIPTIONS_SKU = {SKU_POPCORN_UNLIMITED_MONTHLY};

    private BillingConstants() {}

    /**
     * Gives a list of SKUs based on the type of billing, InApp or Subscription.
     *
     * @param billingType the billing type, InApp or Subscription.
     * @return the list of all SKUs for the billing type specified.
     */
    static public List<String> getSkuList(@BillingClient.SkuType String billingType) {
        return (billingType.equals(BillingClient.SkuType.INAPP))
                ? Arrays.asList(IN_APP_SKU)
                : Arrays.asList(SUBSCRIPTIONS_SKU);
    }
}
