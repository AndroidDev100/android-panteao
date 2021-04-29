package panteao.make.ready.beanModel.responseModels.RegisterSignUpModels;

public class DataResponseRegister {
    private String profileStep;
    private String gender;
    private boolean verified;
    private String dateOfBirth;
    private String verificationDate;
    private String expiryDate;
    private String password;
    private String phoneNumber;
    private String profilePicURL;
    private String name;
    private String id;
    private String email;
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

    public String getDateOfBirth() {
        return dateOfBirth;
    }

    public void setDateOfBirth(String dateOfBirth) {
        this.dateOfBirth = dateOfBirth;
    }

    public String getVerificationDate() {
        return verificationDate;
    }

    public void setVerificationDate(String verificationDate) {
        this.verificationDate = verificationDate;
    }

    public String getExpiryDate() {
        return expiryDate;
    }

    public void setExpiryDate(String expiryDate) {
        this.expiryDate = expiryDate;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
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

    public String getId() {
        return id;
    }

    public void setId(String id) {
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
        return "DataResponseRegister{" +
                "profileStep='" + profileStep + '\'' +
                ", gender='" + gender + '\'' +
                ", verified=" + verified +
                ", dateOfBirth='" + dateOfBirth + '\'' +
                ", verificationDate='" + verificationDate + '\'' +
                ", expiryDate='" + expiryDate + '\'' +
                ", password='" + password + '\'' +
                ", phoneNumber='" + phoneNumber + '\'' +
                ", profilePicURL='" + profilePicURL + '\'' +
                ", name='" + name + '\'' +
                ", id=" + id +
                ", email='" + email + '\'' +
                ", status='" + status + '\'' +
                '}';
    }
}
