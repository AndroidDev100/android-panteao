package panteao.make.ready.redeemcoupon;

import com.google.gson.annotations.SerializedName;

public class Data{

	@SerializedName("accountId")
	private String accountId;

	@SerializedName("entitlementState")
	private String entitlementState;

	@SerializedName("billingPeriod")
	private String billingPeriod;

	@SerializedName("billingCycleDay")
	private int billingCycleDay;

	@SerializedName("billingEndDate")
	private Object billingEndDate;

	@SerializedName("planName")
	private String planName;

	@SerializedName("subscriptionID")
	private String subscriptionID;

	@SerializedName("productName")
	private String productName;

	@SerializedName("billingStartDate")
	private long billingStartDate;

	@SerializedName("cancelationDate")
	private Object cancelationDate;

	public void setAccountId(String accountId){
		this.accountId = accountId;
	}

	public String getAccountId(){
		return accountId;
	}

	public void setEntitlementState(String entitlementState){
		this.entitlementState = entitlementState;
	}

	public String getEntitlementState(){
		return entitlementState;
	}

	public void setBillingPeriod(String billingPeriod){
		this.billingPeriod = billingPeriod;
	}

	public String getBillingPeriod(){
		return billingPeriod;
	}

	public void setBillingCycleDay(int billingCycleDay){
		this.billingCycleDay = billingCycleDay;
	}

	public int getBillingCycleDay(){
		return billingCycleDay;
	}

	public void setBillingEndDate(Object billingEndDate){
		this.billingEndDate = billingEndDate;
	}

	public Object getBillingEndDate(){
		return billingEndDate;
	}

	public void setPlanName(String planName){
		this.planName = planName;
	}

	public String getPlanName(){
		return planName;
	}

	public void setSubscriptionID(String subscriptionID){
		this.subscriptionID = subscriptionID;
	}

	public String getSubscriptionID(){
		return subscriptionID;
	}

	public void setProductName(String productName){
		this.productName = productName;
	}

	public String getProductName(){
		return productName;
	}

	public void setBillingStartDate(long billingStartDate){
		this.billingStartDate = billingStartDate;
	}

	public long getBillingStartDate(){
		return billingStartDate;
	}

	public void setCancelationDate(Object cancelationDate){
		this.cancelationDate = cancelationDate;
	}

	public Object getCancelationDate(){
		return cancelationDate;
	}
}