package panteao.make.ready.utils.inAppBilling;

public interface InAppProcessListener {
    void onBillingInitialized();
    void onPurchasesUpdated();
    void onListOfSKUFetched();
    void onBillingError();
}
