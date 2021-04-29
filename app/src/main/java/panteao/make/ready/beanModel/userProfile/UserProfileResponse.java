package panteao.make.ready.beanModel.userProfile;

public class UserProfileResponse{
	private Data data;
	private Object debugMessage;
	private int responseCode;
	boolean status;

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

	public void setStatus(boolean status) {
		this.status = status;
	}

	public boolean getStatus(){
		return status;
	}

	@Override
 	public String toString(){
		return 
			"UserProfileResponse{" + 
			"data = '" + data + '\'' + 
			",debugMessage = '" + debugMessage + '\'' + 
			",responseCode = '" + responseCode + '\'' + 
			"}";
		}
}
