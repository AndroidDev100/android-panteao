package panteao.make.ready.beanModel.membershipAndPlan;

import panteao.make.ready.beanModel.entitle.Price;
import panteao.make.ready.beanModel.entitle.RecurringOffer;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;


public class DataItem{

	@SerializedName("title")
	@Expose
	private String title;
	@SerializedName("identifier")
	@Expose
	private String identifier;
	@SerializedName("entitlementState")
	@Expose
	private Boolean entitlementState;
	@SerializedName("id")
	@Expose
	private Integer id;
	@SerializedName("offerType")
	@Expose
	private String offerType;
	@SerializedName("subscriptionOrder")
	@Expose
	private Long subscriptionOrder;
	@SerializedName("oneTimeOffer")
	@Expose
	private Object oneTimeOffer;
	@SerializedName("recurringOffer")
	@Expose
	private RecurringOffer recurringOffer;
	@SerializedName("prices")
	@Expose
	private List<Price> prices = null;
	@SerializedName("offerStatus")
	@Expose
	private String offerStatus;
	@SerializedName("linkedContentSKUs")
	@Expose
	private Object linkedContentSKUs;
	@SerializedName("isDefault")
	@Expose
	private Object isDefault;
	@SerializedName("expiryDate")
	@Expose
	private Object expiryDate;

	@SerializedName("description")
	@Expose
	private Object description;


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

	public Boolean getEntitlementState() {
		return entitlementState;
	}

	public void setEntitlementState(Boolean entitlementState) {
		this.entitlementState = entitlementState;
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
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

	public Object getOneTimeOffer() {
		return oneTimeOffer;
	}

	public void setOneTimeOffer(Object oneTimeOffer) {
		this.oneTimeOffer = oneTimeOffer;
	}

	public RecurringOffer getRecurringOffer() {
		return recurringOffer;
	}

	public void setRecurringOffer(RecurringOffer recurringOffer) {
		this.recurringOffer = recurringOffer;
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

	public Object getIsDefault() {
		return isDefault;
	}

	public void setIsDefault(Object isDefault) {
		this.isDefault = isDefault;
	}

	public Object getExpiryDate() {
		return expiryDate;
	}

	public void setExpiryDate(Object expiryDate) {
		this.expiryDate = expiryDate;
	}

	public Object getDescription() {
		return description;
	}

	public void setDescription(Object description) {
		this.description = description;
	}
}