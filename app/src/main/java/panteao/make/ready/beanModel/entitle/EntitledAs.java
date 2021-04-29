package panteao.make.ready.beanModel.entitle;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.List;

public class EntitledAs implements Serializable {

    @SerializedName("title")
    @Expose
    private String title;
    @SerializedName("description")
    @Expose
    private Object description;
    @SerializedName("identifier")
    @Expose
    private String identifier;
    @SerializedName("entitlementState")
    @Expose
    private Object entitlementState;
    @SerializedName("id")
    @Expose
    private Integer id;
    @SerializedName("voDOfferType")
    @Expose
    private String voDOfferType;
    @SerializedName("rentalPeriod")
    @Expose
    private RentalPeriod rentalPeriod;
    @SerializedName("prices")
    @Expose
    private List<Price> prices = null;
    @SerializedName("offerStatus")
    @Expose
    private String offerStatus;
    @SerializedName("linkedContentSKUs")
    @Expose
    private Object linkedContentSKUs;
    @SerializedName("offerType")
    @Expose
    private String offerType;
    @SerializedName("subscriptionOrder")
    @Expose
    private Long subscriptionOrder;
    @SerializedName("oneTimeOffer")
    @Expose
    private OneTimeOffer oneTimeOffer;
    @SerializedName("recurringOffer")
    @Expose
    private RecurringOffer recurringOffer;
    @SerializedName("isDefault")
    @Expose
    private Object isDefault;

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getIdentifier() {
        return identifier;
    }

    public void setIdentifier(String identifier) {
        this.identifier = identifier;
    }

    public Object getEntitlementState() {
        return entitlementState;
    }

    public void setEntitlementState(Object entitlementState) {
        this.entitlementState = entitlementState;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getVoDOfferType() {
        return voDOfferType;
    }

    public void setVoDOfferType(String voDOfferType) {
        this.voDOfferType = voDOfferType;
    }

    public RentalPeriod getRentalPeriod() {
        return rentalPeriod;
    }

    public void setRentalPeriod(RentalPeriod rentalPeriod) {
        this.rentalPeriod = rentalPeriod;
    }

    public List<Price> getPrices() {
        return prices;
    }

    public void setPrices(List<Price> prices) {
        this.prices = prices;
    }

    public String getOfferStatus() {
        return offerStatus;
    }

    public void setOfferStatus(String offerStatus) {
        this.offerStatus = offerStatus;
    }

    public Object getLinkedContentSKUs() {
        return linkedContentSKUs;
    }

    public void setLinkedContentSKUs(Object linkedContentSKUs) {
        this.linkedContentSKUs = linkedContentSKUs;
    }

    public String getOfferType() {
        return offerType;
    }

    public void setOfferType(String offerType) {
        this.offerType = offerType;
    }

    public Long getSubscriptionOrder() {
        return subscriptionOrder;
    }

    public void setSubscriptionOrder(Long subscriptionOrder) {
        this.subscriptionOrder = subscriptionOrder;
    }

    public OneTimeOffer getOneTimeOffer() {
        return oneTimeOffer;
    }

    public void setOneTimeOffer(OneTimeOffer oneTimeOffer) {
        this.oneTimeOffer = oneTimeOffer;
    }

    public RecurringOffer getRecurringOffer() {
        return recurringOffer;
    }

    public void setRecurringOffer(RecurringOffer recurringOffer) {
        this.recurringOffer = recurringOffer;
    }

    public Object getIsDefault() {
        return isDefault;
    }

    public void setIsDefault(Object isDefault) {
        this.isDefault = isDefault;
    }

    public Object getDescription() {
        return description;
    }

    public void setDescription(Object description) {
        this.description = description;
    }

}
