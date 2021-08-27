package panteao.make.ready.beanModel.purchaseModel;

import java.io.Serializable;
import java.util.Objects;

@SuppressWarnings("EqualsWhichDoesntCheckParameterClass")
public class PurchaseModel implements Serializable {

    private String purchaseOptions;
    private String price;
    private boolean isSelected;
    private String subscribedText;
    private String currency;
    private String identifier;
    private String title;
    private String offerPeriod;
    private int offerPeriodDuration;
    private String periodType;
    private double expiryDate;
    private String description;
    private boolean fromClick;

    public PurchaseModel() {
    }

    public PurchaseModel(String purchaseOptions, String price, boolean isSelected) {
        this.purchaseOptions = purchaseOptions;
        this.price = price;
        this.isSelected = isSelected;
    }

    public String getCurrency() {
        return currency;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }

    public String getSubscribedText() {
        return subscribedText;
    }

    public void setSubscribedText(String subscribedText) {
        this.subscribedText = subscribedText;
    }

    public String getPurchaseOptions() {
        return purchaseOptions;
    }

    public void setPurchaseOptions(String purchaseOptions) {
        this.purchaseOptions = purchaseOptions;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public boolean isSelected() {
        return isSelected;
    }

    public void setSelected(boolean selected) {
        isSelected = selected;
    }

    public void setIdentifier(String identifier) {
        this.identifier = identifier;
    }

    public String getIdentifier() {
        return identifier;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getTitle() {
        return title;
    }

    public void setOfferPeriod(String offerPeriod) {
        this.offerPeriod = offerPeriod;
    }

    public String getOfferPeriod() {
        return offerPeriod;
    }

    public int getOfferPeriodDuration() {
        return offerPeriodDuration;
    }

    public void setOfferPeriodDuration(int offerPeriodDuration) {
        this.offerPeriodDuration = offerPeriodDuration;
    }

    public String getPeriodType() {
        return periodType;
    }

    public void setPeriodType(String periodType) {
        this.periodType = periodType;
    }

    public void setExpiryDate(double expiryDate) {
        this.expiryDate = expiryDate;
    }

    public double getExpiryDate() {
        return expiryDate;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getDescription() {
        return description;
    }

    public void setFromClick(boolean fromClick) {
        this.fromClick = fromClick;
    }

    public boolean isFromClick() {
        return fromClick;
    }

    @Override
    public boolean equals(Object obj) {
        PurchaseModel purchaseModel = (PurchaseModel) obj;
        return (this.isSelected == purchaseModel.isSelected());
    }

    @Override
    public int hashCode() {
//        return super.hashCode();

        return Objects.hash(purchaseOptions, price, isSelected);
    }
}
