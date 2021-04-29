
package panteao.make.ready.beanModel.purchaseModel;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Data {

    @SerializedName("dateCreated")
    @Expose
    private Long dateCreated;
    @SerializedName("lastUpdated")
    @Expose
    private Long lastUpdated;
    @SerializedName("id")
    @Expose
    private Integer id;
    @SerializedName("offerId")
    @Expose
    private String offerId;
    @SerializedName("orderId")
    @Expose
    private String orderId;
    @SerializedName("userId")
    @Expose
    private String userId;
    @SerializedName("paymentId")
    @Expose
    private Object paymentId;
    @SerializedName("orderStatus")
    @Expose
    private String orderStatus;
    @SerializedName("orderAmount")
    @Expose
    private Double orderAmount;
    @SerializedName("orderCurrency")
    @Expose
    private String orderCurrency;
    @SerializedName("paymentProvider")
    @Expose
    private Object paymentProvider;
    @SerializedName("orderExpiry")
    @Expose
    private Integer orderExpiry;
    @SerializedName("notes")
    @Expose
    private Notes notes;

    public Long getDateCreated() {
        return dateCreated;
    }

    public void setDateCreated(Long dateCreated) {
        this.dateCreated = dateCreated;
    }

    public Long getLastUpdated() {
        return lastUpdated;
    }

    public void setLastUpdated(Long lastUpdated) {
        this.lastUpdated = lastUpdated;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getOfferId() {
        return offerId;
    }

    public void setOfferId(String offerId) {
        this.offerId = offerId;
    }

    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public Object getPaymentId() {
        return paymentId;
    }

    public void setPaymentId(Object paymentId) {
        this.paymentId = paymentId;
    }

    public String getOrderStatus() {
        return orderStatus;
    }

    public void setOrderStatus(String orderStatus) {
        this.orderStatus = orderStatus;
    }

    public Double getOrderAmount() {
        return orderAmount;
    }

    public void setOrderAmount(Double orderAmount) {
        this.orderAmount = orderAmount;
    }

    public String getOrderCurrency() {
        return orderCurrency;
    }

    public void setOrderCurrency(String orderCurrency) {
        this.orderCurrency = orderCurrency;
    }

    public Object getPaymentProvider() {
        return paymentProvider;
    }

    public void setPaymentProvider(Object paymentProvider) {
        this.paymentProvider = paymentProvider;
    }

    public Integer getOrderExpiry() {
        return orderExpiry;
    }

    public void setOrderExpiry(Integer orderExpiry) {
        this.orderExpiry = orderExpiry;
    }

    public Notes getNotes() {
        return notes;
    }

    public void setNotes(Notes notes) {
        this.notes = notes;
    }

}
