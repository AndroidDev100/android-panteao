package panteao.make.ready.beanModel.membershipAndPlan;

import com.google.gson.annotations.SerializedName;

import java.util.List;


public class ResponseMembershipAndPlan{

	@SerializedName("data")
	private List<DataItem> data;

	private boolean status;

	@SerializedName("debugMessage")
	private Object debugMessage;

	@SerializedName("responseCode")
	private int responseCode;

	public void setData(List<DataItem> data){
		this.data = data;
	}

	public List<DataItem> getData(){
		return data;
	}

	public void setDebugMessage(Object debugMessage){
		this.debugMessage = debugMessage;
	}

	public Object getDebugMessage(){
		return debugMessage;
	}

	public void setStatus(boolean status) {
		this.status = status;
	}

	public boolean isStatus() {
		return status;
	}

	public void setResponseCode(int responseCode){
		this.responseCode = responseCode;
	}

	public int getResponseCode(){
		return responseCode;
	}

	@Override
 	public String toString(){
		return 
			"ResponseMembershipAndPlan{" + 
			"data = '" + data + '\'' + 
			",debugMessage = '" + debugMessage + '\'' + 
			",responseCode = '" + responseCode + '\'' + 
			"}";
		}
}