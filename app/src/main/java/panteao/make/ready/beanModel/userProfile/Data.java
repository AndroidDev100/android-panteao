package panteao.make.ready.beanModel.userProfile;

import java.util.List;

public class Data{
	private boolean manualLinked;
	private Object profileStep;
	private boolean isFbLinked;
	private Object gender;
	private boolean verified;
	private List<Object> appUserPlans;
	private Object dateOfBirth;
	private long verificationDate;
	private Object expiryDate;
	private Object accountId;
	private Object password;
	private Object phoneNumber;
	private Object profilePicURL;
	private String name;
	private int id;
	private String email;
	private String status;
	private boolean gplusLinked;

	public void setManualLinked(boolean manualLinked){
		this.manualLinked = manualLinked;
	}

	public boolean isManualLinked(){
		return manualLinked;
	}

	public void setProfileStep(Object profileStep){
		this.profileStep = profileStep;
	}

	public Object getProfileStep(){
		return profileStep;
	}

	public void setIsFbLinked(boolean isFbLinked){
		this.isFbLinked = isFbLinked;
	}

	public boolean isIsFbLinked(){
		return isFbLinked;
	}

	public void setGender(Object gender){
		this.gender = gender;
	}

	public Object getGender(){
		return gender;
	}

	public void setVerified(boolean verified){
		this.verified = verified;
	}

	public boolean isVerified(){
		return verified;
	}

	public void setAppUserPlans(List<Object> appUserPlans){
		this.appUserPlans = appUserPlans;
	}

	public List<Object> getAppUserPlans(){
		return appUserPlans;
	}

	public void setDateOfBirth(Object dateOfBirth){
		this.dateOfBirth = dateOfBirth;
	}

	public Object getDateOfBirth(){
		return dateOfBirth;
	}

	public void setVerificationDate(long verificationDate){
		this.verificationDate = verificationDate;
	}

	public long getVerificationDate(){
		return verificationDate;
	}

	public void setExpiryDate(Object expiryDate){
		this.expiryDate = expiryDate;
	}

	public Object getExpiryDate(){
		return expiryDate;
	}

	public void setAccountId(Object accountId){
		this.accountId = accountId;
	}

	public Object getAccountId(){
		return accountId;
	}

	public void setPassword(Object password){
		this.password = password;
	}

	public Object getPassword(){
		return password;
	}

	public void setPhoneNumber(Object phoneNumber){
		this.phoneNumber = phoneNumber;
	}

	public Object getPhoneNumber(){
		return phoneNumber;
	}

	public void setProfilePicURL(Object profilePicURL){
		this.profilePicURL = profilePicURL;
	}

	public Object getProfilePicURL(){
		return profilePicURL;
	}

	public void setName(String name){
		this.name = name;
	}

	public String getName(){
		return name;
	}

	public void setId(int id){
		this.id = id;
	}

	public int getId(){
		return id;
	}

	public void setEmail(String email){
		this.email = email;
	}

	public String getEmail(){
		return email;
	}

	public void setStatus(String status){
		this.status = status;
	}

	public String getStatus(){
		return status;
	}

	public void setGplusLinked(boolean gplusLinked){
		this.gplusLinked = gplusLinked;
	}

	public boolean isGplusLinked(){
		return gplusLinked;
	}

	@Override
 	public String toString(){
		return 
			"Data{" + 
			"manualLinked = '" + manualLinked + '\'' + 
			",profileStep = '" + profileStep + '\'' + 
			",isFbLinked = '" + isFbLinked + '\'' + 
			",gender = '" + gender + '\'' + 
			",verified = '" + verified + '\'' + 
			",appUserPlans = '" + appUserPlans + '\'' + 
			",dateOfBirth = '" + dateOfBirth + '\'' + 
			",verificationDate = '" + verificationDate + '\'' + 
			",expiryDate = '" + expiryDate + '\'' + 
			",accountId = '" + accountId + '\'' + 
			",password = '" + password + '\'' + 
			",phoneNumber = '" + phoneNumber + '\'' + 
			",profilePicURL = '" + profilePicURL + '\'' + 
			",name = '" + name + '\'' + 
			",id = '" + id + '\'' + 
			",email = '" + email + '\'' + 
			",status = '" + status + '\'' + 
			",gplusLinked = '" + gplusLinked + '\'' + 
			"}";
		}
}