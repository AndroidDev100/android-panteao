package panteao.make.ready.utils.helpers.carousel.entity;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class UserData {

    @SerializedName("id")
    @Expose
    private String id;
    @SerializedName("name")
    @Expose
    private String name;

    @SerializedName("emailId")
    @Expose
    private String email;

    @SerializedName("userType")
    @Expose
    private String userType;

    @SerializedName("vioName")
    @Expose
    private String vioName;

    @SerializedName("profilePicUrl")
    @Expose
    private String profilePicture;

    @SerializedName("registrationType")
    @Expose
    private String registrationType;
    @SerializedName("loginType")
    @Expose
    private String loginType;
    @SerializedName("bio")
    @Expose
    private String bio;
    @SerializedName("socialAccounts")
    @Expose
    private List<SocialData> socialAccounts = null;
    @SerializedName("status")
    @Expose
    private String status;
    @SerializedName("verified")
    @Expose
    private boolean verified;
    @SerializedName("canGoLive")
    @Expose
    private String canGoLive;
    @SerializedName("dob")
    @Expose
    private String dob;
    @SerializedName("verify")
    @Expose
    private String verify;
    @SerializedName("followersCount")
    @Expose
    private Integer followersCount;
    @SerializedName("followingCount")
    @Expose
    private Integer followingCount;
    @SerializedName("gender")
    @Expose
    private String gender;
    @SerializedName("goLiveStatus")
    @Expose
    private String goLiveStatus;
    private boolean showNotification;
    private Integer notificationCount;

    public String getLoginType() {
        return loginType;
    }

    public void setLoginType(String loginType) {
        this.loginType = loginType;
    }

    public String getBio() {
        return bio;
    }

    public void setBio(String bio) {
        this.bio = bio;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public boolean getVerified() {
        return verified;
    }

    public String getCanGoLive() {
        return canGoLive;
    }

    public void setCanGoLive(String canGoLive) {
        this.canGoLive = canGoLive;
    }

    public String getVerify() {
        return verify;
    }

    public void setVerify(String verify) {
        this.verify = verify;
    }

    public boolean isVerified() {
        return verified;
    }

    public void setVerified(boolean verified) {
        this.verified = verified;
    }

    public Integer getFollowersCount() {
        return followersCount;
    }

    public void setFollowersCount(Integer followersCount) {
        this.followersCount = followersCount;
    }

    public Integer getFollowingCount() {
        return followingCount;
    }

    public void setFollowingCount(Integer followingCount) {
        this.followingCount = followingCount;
    }

    public String getDob() {
        return dob;
    }

    public void setDob(String dob) {
        this.dob = dob;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getUserType() {
        return userType;
    }

    public void setUserType(String userType) {
        this.userType = userType;
    }

    public String getGoLiveStatus() {
        return goLiveStatus;
    }

    public void setGoLiveStatus(String goLiveStatus) {
        this.goLiveStatus = goLiveStatus;
    }

    public boolean isShowNotification() {
        return showNotification;
    }

    public void setShowNotification(boolean showNotification) {
        this.showNotification = showNotification;
    }

    public Integer getNotificationCount() {
        return notificationCount;
    }

    public void setNotificationCount(Integer notificationCount) {
        this.notificationCount = notificationCount;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getVioName() {
        return vioName;
    }

    public void setVioName(String vioName) {
        this.vioName = vioName;
    }

    public String getProfilePicture() {
        return profilePicture;
    }

    public void setProfilePicture(String profilePicture) {
        this.profilePicture = profilePicture;
    }

    public String getRegistrationType() {
        return registrationType;
    }

    public void setRegistrationType(String registrationType) {
        this.registrationType = registrationType;
    }

    public List<SocialData> getSocialAccounts() {
        return socialAccounts;
    }

    public void setSocialAccounts(List<SocialData> socialAccounts) {
        this.socialAccounts = socialAccounts;
    }
}
