package panteao.make.ready.beanModel.responseModels.VersionVerification;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class ResponceData implements Parcelable {

    public static final Creator<ResponceData> CREATOR = new Creator<ResponceData>() {
        @Override
        public ResponceData createFromParcel(Parcel source) {
            return new ResponceData(source);
        }

        @Override
        public ResponceData[] newArray(int size) {
            return new ResponceData[size];
        }
    };
    @SerializedName("RestResponse")
    @Expose
    private RestResponse restResponse;

    public ResponceData() {
    }

    protected ResponceData(Parcel in) {
        this.restResponse = in.readParcelable(RestResponse.class.getClassLoader());
    }

    public RestResponse getRestResponse() {
        return restResponse;
    }

    public void setRestResponse(RestResponse restResponse) {
        this.restResponse = restResponse;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeParcelable(this.restResponse, flags);
    }
}
