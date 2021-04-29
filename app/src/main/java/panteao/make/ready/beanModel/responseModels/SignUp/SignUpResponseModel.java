package panteao.make.ready.beanModel.responseModels.SignUp;

import android.os.Parcel;
import android.os.Parcelable;

public class SignUpResponseModel implements Parcelable {

    public static final Creator<SignUpResponseModel> CREATOR = new Creator<SignUpResponseModel>() {
        @Override
        public SignUpResponseModel createFromParcel(Parcel in) {
            return new SignUpResponseModel(in);
        }

        @Override
        public SignUpResponseModel[] newArray(int size) {
            return new SignUpResponseModel[size];
        }
    };
    private DataModel data;
    private int responseCode;

    private SignUpResponseModel(Parcel in) {
        data = in.readParcelable(DataModel.class.getClassLoader());
        responseCode = in.readInt();
    }

    public SignUpResponseModel() {
    }

    public DataModel getData() {
        return data;
    }

    public void setData(DataModel data) {
        this.data = data;
    }

    public int getResponseCode() {
        return responseCode;
    }

    public void setResponseCode(int responseCode) {
        this.responseCode = responseCode;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeParcelable(data, i);
        parcel.writeInt(responseCode);
    }
}
