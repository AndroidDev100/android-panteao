package panteao.make.ready.redeemcoupon;

import com.google.gson.annotations.SerializedName;

public class RedeemCouponResponseModel{

	@SerializedName("data")
	private Data data;

	@SerializedName("debugMessage")
	private Object debugMessage;

	@SerializedName("responseCode")
	private int responseCode;

	public void setData(Data data){
		this.data = data;
	}

	public Data getData(){
		return data;
	}

	public void setDebugMessage(Object debugMessage){
		this.debugMessage = debugMessage;
	}

	public Object getDebugMessage(){
		return debugMessage;
	}

	public void setResponseCode(int responseCode){
		this.responseCode = responseCode;
	}

	public int getResponseCode(){
		return responseCode;
	}
}