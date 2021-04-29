
package panteao.make.ready.beanModel.purchaseModel;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Notes {

    @SerializedName("enveuSMSPlanName")
    @Expose
    private String enveuSMSPlanName;
    @SerializedName("enveuSMSOfferType")
    @Expose
    private Object enveuSMSOfferType;
    @SerializedName("enveuSMSOfferContentSKU")
    @Expose
    private Object enveuSMSOfferContentSKU;
    @SerializedName("enveuSMSSubscriptionOfferType")
    @Expose
    private String enveuSMSSubscriptionOfferType;
    @SerializedName("enveuSMSPurchaseCurrency")
    @Expose
    private String enveuSMSPurchaseCurrency;
    @SerializedName("enveuSMSPlanTitle")
    @Expose
    private String enveuSMSPlanTitle;

    public String getEnveuSMSPlanName() {
        return enveuSMSPlanName;
    }

    public void setEnveuSMSPlanName(String enveuSMSPlanName) {
        this.enveuSMSPlanName = enveuSMSPlanName;
    }

    public Object getEnveuSMSOfferType() {
        return enveuSMSOfferType;
    }

    public void setEnveuSMSOfferType(Object enveuSMSOfferType) {
        this.enveuSMSOfferType = enveuSMSOfferType;
    }

    public Object getEnveuSMSOfferContentSKU() {
        return enveuSMSOfferContentSKU;
    }

    public void setEnveuSMSOfferContentSKU(Object enveuSMSOfferContentSKU) {
        this.enveuSMSOfferContentSKU = enveuSMSOfferContentSKU;
    }

    public String getEnveuSMSSubscriptionOfferType() {
        return enveuSMSSubscriptionOfferType;
    }

    public void setEnveuSMSSubscriptionOfferType(String enveuSMSSubscriptionOfferType) {
        this.enveuSMSSubscriptionOfferType = enveuSMSSubscriptionOfferType;
    }

    public String getEnveuSMSPurchaseCurrency() {
        return enveuSMSPurchaseCurrency;
    }

    public void setEnveuSMSPurchaseCurrency(String enveuSMSPurchaseCurrency) {
        this.enveuSMSPurchaseCurrency = enveuSMSPurchaseCurrency;
    }

    public String getEnveuSMSPlanTitle() {
        return enveuSMSPlanTitle;
    }

    public void setEnveuSMSPlanTitle(String enveuSMSPlanTitle) {
        this.enveuSMSPlanTitle = enveuSMSPlanTitle;
    }

}
