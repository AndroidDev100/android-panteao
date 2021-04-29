package panteao.make.ready.utils.inAppBilling;

import com.android.billingclient.api.SkuDetails;

public interface SkuDetailsListener {
    void response(SkuDetails skuDetails);
}
