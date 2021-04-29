package panteao.make.ready.beanModel.connectFb;

import java.util.List;

@SuppressWarnings("ALL")
public class Data {
    private boolean manualLinked;
    private String profileStep;
    private boolean isFbLinked;
    private Object gender;
    private boolean verified;
    private List<AppUserPlansItem> appUserPlans;
    private Object dateOfBirth;
    private Object verificationDate;
    private long expiryDate;
    private String accountId;
    private Object password;
    private Object phoneNumber;
    private String profilePicURL;
    private String name;
    private int id;
    private String email;
    private String status;
    private boolean gplusLinked;

    public boolean isManualLinked() {
        return manualLinked;
    }

    public void setManualLinked(boolean manualLinked) {
        this.manualLinked = manualLinked;
    }

    public String getProfileStep() {
        return profileStep;
    }

    public void setProfileStep(String profileStep) {
        this.profileStep = profileStep;
    }

    public boolean isIsFbLinked() {
        return isFbLinked;
    }

    public void setIsFbLinked(boolean isFbLinked) {
        this.isFbLinked = isFbLinked;
    }

    public Object getGender() {
        return gender;
    }

    public void setGender(Object gender) {
        this.gender = gender;
    }

    public boolean isVerified() {
        return verified;
    }

    public void setVerified(boolean verified) {
        this.verified = verified;
    }

    public List<AppUserPlansItem> getAppUserPlans() {
        return appUserPlans;
    }

    public void setAppUserPlans(List<AppUserPlansItem> appUserPlans) {
        this.appUserPlans = appUserPlans;
    }

    public Object getDateOfBirth() {
        return dateOfBirth;
    }

    public void setDateOfBirth(Object dateOfBirth) {
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

    public String getAccountId() {
        return accountId;
    }

    public void setAccountId(String accountId) {
        this.accountId = accountId;
    }

    public Object getPassword() {
        return password;
    }

    public void setPassword(Object password) {
        this.password = password;
    }

    public Object getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(Object phoneNumber) {
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

    public boolean isGplusLinked() {
        return gplusLinked;
    }

    public void setGplusLinked(boolean gplusLinked) {
        this.gplusLinked = gplusLinked;
    }

    @Override
    public String toString() {
        return
                "PlayListDetailsResponse{" +
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