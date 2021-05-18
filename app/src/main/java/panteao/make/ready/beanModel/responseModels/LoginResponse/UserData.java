package panteao.make.ready.beanModel.responseModels.LoginResponse;

import com.google.gson.annotations.SerializedName;

public class UserData {

    @SerializedName("profileStep")
    private String profileStep;

    @SerializedName("gender")
    private String gender;

    @SerializedName("verified")
    private boolean verified;

    @SerializedName("dateOfBirth")
    private long dateOfBirth;

    @SerializedName("verificationDate")
    private Object verificationDate;

    @SerializedName("expiryDate")
    private long expiryDate;

    @SerializedName("password")
    private Object password;

    @SerializedName("phoneNumber")
    private String phoneNumber;

    @SerializedName("profilePicURL")
    private String profilePicURL;

    @SerializedName("name")
    private String name;

    @SerializedName("id")
    private int id;

    @SerializedName("email")
    private String email;

    @SerializedName("status")
    private String status;

    public String getProfileStep() {
        return profileStep;
    }

    public void setProfileStep(String profileStep) {
        this.profileStep = profileStep;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public boolean isVerified() {
        return verified;
    }

    public void setVerified(boolean verified) {
        this.verified = verified;
    }

    public long getDateOfBirth() {
        return dateOfBirth;
    }

    public void setDateOfBirth(long dateOfBirth) {
        this.dateOfBirth = dateOfBirth;
    }

    public Object getVerificationDate() {
        return verificationDate;
    }

    public void setVerificationDate(Object verificationDate) {
        this.verificationDate = verificationDate;
    }

    public long getExpiryDate() {
        return expiryDate;
    }

    public void setExpiryDate(long expiryDate) {
        this.expiryDate = expiryDate;
    }

    public Object getPassword() {
        return password;
    }

    public void setPassword(Object password) {
        this.password = password;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getProfilePicURL() {
        return profilePicURL;
    }

    public void setProfilePicURL(String profilePicURL) {
        this.profilePicURL = profilePicURL;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    @Override
    public String toString() {
        return
                "DataResponseRegister{" +
                        "profileStep = '" + profileStep + '\'' +
                        ",gender = '" + gender + '\'' +
                        ",verified = '" + verified + '\'' +
                        ",dateOfBirth = '" + dateOfBirth + '\'' +
                        ",verificationDate = '" + verificationDate + '\'' +
                        ",expiryDate = '" + expiryDate + '\'' +
                        ",password = '" + password + '\'' +
                        ",phoneNumber = '" + phoneNumber + '\'' +
                        ",profilePicURL = '" + profilePicURL + '\'' +
                        ",name = '" + name + '\'' +
                        ",id = '" + id + '\'' +
                        ",email = '" + email + '\'' +
                        ",status = '" + status + '\'' +
                        "}";
    }
}