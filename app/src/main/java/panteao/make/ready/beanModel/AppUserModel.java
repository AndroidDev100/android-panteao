package panteao.make.ready.beanModel;


import android.os.Parcel;
import android.os.Parcelable;

public class AppUserModel implements Parcelable {

    public static final Parcelable.Creator<AppUserModel> CREATOR = new Parcelable.Creator<AppUserModel>() {
        @Override
        public AppUserModel createFromParcel(Parcel source) {
            return new AppUserModel(source);
        }

        @Override
        public AppUserModel[] newArray(int size) {
            return new AppUserModel[size];
        }
    };
    private static AppUserModel USER_DETAIL;
    private String __typename;
    private String id;
    private String name;
    private String email;
    private String profilePicURL;
    private String gender;
    private String dateOfBirth;
    private String phoneNumber;
    private boolean isVerified;
    private String registrationType;
    private String profileStep;
    private String status;
    private long notificationCount;
    private boolean showNotification;
    private String bio;
    private String dateCreated;
    private String lastUpdated;
    private boolean isFbLinked;
    private boolean isManualLinked;
    private boolean isGPlusLinked;

    private AppUserModel() {
    }

    protected AppUserModel(Parcel in) {
        this.__typename = in.readString();
        this.id = in.readString();
        this.name = in.readString();
        this.email = in.readString();
        this.profilePicURL = in.readString();
        this.gender = in.readString();
        this.dateOfBirth = in.readString();
        this.phoneNumber = in.readString();
        this.isVerified = in.readByte() != 0;
        this.isFbLinked = in.readByte() != 0;
        this.isManualLinked = in.readByte() != 0;
        this.isGPlusLinked = in.readByte() != 0;
        this.registrationType = in.readString();
        this.profileStep = in.readString();
        this.status = in.readString();
        this.notificationCount = in.readLong();
        this.showNotification = in.readByte() != 0;
        this.bio = in.readString();
        this.dateCreated = in.readString();
        this.lastUpdated = in.readString();
    }

    public static AppUserModel getInstance() {
        if (USER_DETAIL == null) {
            synchronized (AppUserModel.class) {
                USER_DETAIL = new AppUserModel();
            }
        }
        return USER_DETAIL;
    }

    public static void clear() {
        USER_DETAIL = null;
    }

    public boolean isFbLinked() {
        return isFbLinked;
    }

    public void setFbLinked(boolean fbLinked) {
        isFbLinked = fbLinked;
    }

    public boolean isManualLinked() {
        return isManualLinked;
    }

    public void setManualLinked(boolean manualLinked) {
        isManualLinked = manualLinked;
    }

    public boolean isGPlusLinked() {
        return isGPlusLinked;
    }

    public void setGPlusLinked(boolean GPlusLinked) {
        isGPlusLinked = GPlusLinked;
    }

    public void clearData() {
        USER_DETAIL = null;
    }

    public String get__typename() {
        return __typename;
    }

    public void set__typename(String __typename) {
        this.__typename = __typename;
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

    public String getProfilePicURL() {
        return profilePicURL;
    }

    public void setProfilePicURL(String profilePicURL) {
        this.profilePicURL = profilePicURL;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getDateOfBirth() {
        return dateOfBirth;
    }

    public void setDateOfBirth(String dateOfBirth) {
        this.dateOfBirth = dateOfBirth;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public boolean isVerified() {
        return isVerified;
    }

    public void setVerified(boolean verified) {
        isVerified = verified;
    }

    public String getRegistrationType() {
        return registrationType;
    }

    public void setRegistrationType(String registrationType) {
        this.registrationType = registrationType;
    }

    public String getProfileStep() {
        return profileStep;
    }

    public void setProfileStep(String profileStep) {
        this.profileStep = profileStep;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public long getNotificationCount() {
        return notificationCount;
    }

    public void setNotificationCount(long notificationCount) {
        this.notificationCount = notificationCount;
    }

    public boolean isShowNotification() {
        return showNotification;
    }

    public void setShowNotification(boolean showNotification) {
        this.showNotification = showNotification;
    }

    public String getBio() {
        return bio;
    }

    public void setBio(String bio) {
        this.bio = bio;
    }

    public String getDateCreated() {
        return dateCreated;
    }

    public void setDateCreated(String dateCreated) {
        this.dateCreated = dateCreated;
    }

    public String getLastUpdated() {
        return lastUpdated;
    }

    public void setLastUpdated(String lastUpdated) {
        this.lastUpdated = lastUpdated;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.__typename);
        dest.writeString(this.id);
        dest.writeString(this.name);
        dest.writeString(this.email);
        dest.writeString(this.profilePicURL);
        dest.writeString(this.gender);
        dest.writeString(this.dateOfBirth);
        dest.writeString(this.phoneNumber);
        dest.writeByte(this.isVerified ? (byte) 1 : (byte) 0);
        dest.writeString(this.registrationType);
        dest.writeString(this.profileStep);
        dest.writeString(this.status);
        dest.writeLong(this.notificationCount);
        dest.writeByte(this.showNotification ? (byte) 1 : (byte) 0);
        dest.writeString(this.bio);
        dest.writeString(this.dateCreated);
        dest.writeString(this.lastUpdated);
        dest.writeByte(this.isFbLinked ? (byte) 1 : (byte) 0);
        dest.writeByte(this.isManualLinked ? (byte) 1 : (byte) 0);
        dest.writeByte(this.isGPlusLinked ? (byte) 1 : (byte) 0);


    }


}
