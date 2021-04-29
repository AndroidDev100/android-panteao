package panteao.make.ready.beanModel.responseModels.SignUp;

import android.os.Parcel;
import android.os.Parcelable;

public class DataModel implements Parcelable {

    public static final Creator<DataModel> CREATOR = new Creator<DataModel>() {
        @Override
        public DataModel createFromParcel(Parcel source) {
            return new DataModel(source);
        }

        @Override
        public DataModel[] newArray(int size) {
            return new DataModel[size];
        }
    };
    private String id;
    private String email;
    private String password;
    private String name;
    private String verificationDate;
    private String profilePicURL;
    private String dateOfBirth;
    private String gender;
    private String phoneNumber;
    private String status;
    private String expiryDate;
    private String profileStep;
    private String verified;

    public DataModel() {
    }

    DataModel(Parcel in) {
        this.id = in.readString();
        this.email = in.readString();
        this.password = in.readString();
        this.name = in.readString();
        this.verificationDate = in.readString();
        this.profilePicURL = in.readString();
        this.dateOfBirth = in.readString();
        this.gender = in.readString();
        this.phoneNumber = in.readString();
        this.status = in.readString();
        this.expiryDate = in.readString();
        this.profileStep = in.readString();
        this.verified = in.readString();
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

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getVerificationDate() {
        return verificationDate;
    }

    public void setVerificationDate(String verificationDate) {
        this.verificationDate = verificationDate;
    }

    public String getProfilePicURL() {
        return profilePicURL;
    }

    public void setProfilePicURL(String profilePicURL) {
        this.profilePicURL = profilePicURL;
    }

    public String getDateOfBirth() {
        return dateOfBirth;
    }

    public void setDateOfBirth(String dateOfBirth) {
        this.dateOfBirth = dateOfBirth;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getExpiryDate() {
        return expiryDate;
    }

    public void setExpiryDate(String expiryDate) {
        this.expiryDate = expiryDate;
    }

    public String getProfileStep() {
        return profileStep;
    }

    public void setProfileStep(String profileStep) {
        this.profileStep = profileStep;
    }

    public String getVerified() {
        return verified;
    }

    public void setVerified(String verified) {
        this.verified = verified;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.id);
        dest.writeString(this.email);
        dest.writeString(this.password);
        dest.writeString(this.name);
        dest.writeString(this.verificationDate);
        dest.writeString(this.profilePicURL);
        dest.writeString(this.dateOfBirth);
        dest.writeString(this.gender);
        dest.writeString(this.phoneNumber);
        dest.writeString(this.status);
        dest.writeString(this.expiryDate);
        dest.writeString(this.profileStep);
        dest.writeString(this.verified);
    }
}
