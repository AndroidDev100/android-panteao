package panteao.make.ready.beanModel.responseModels.VersionVerification;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

public class RestResponse implements Parcelable {

    public static final Creator<RestResponse> CREATOR = new Creator<RestResponse>() {
        @Override
        public RestResponse createFromParcel(Parcel source) {
            return new RestResponse(source);
        }

        @Override
        public RestResponse[] newArray(int size) {
            return new RestResponse[size];
        }
    };
    @SerializedName("messages")
    @Expose
    private List<String> messages = null;
    @SerializedName("result")
    @Expose
    private List<Result> result = null;

    public RestResponse() {
    }

    protected RestResponse(Parcel in) {
        this.messages = in.createStringArrayList();
        this.result = new ArrayList<>();
        in.readList(this.result, Result.class.getClassLoader());
    }

    public List<String> getMessages() {
        return messages;
    }

    public void setMessages(List<String> messages) {
        this.messages = messages;
    }

    public List<Result> getResult() {
        return result;
    }

    public void setResult(List<Result> result) {
        this.result = result;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeStringList(this.messages);
        dest.writeList(this.result);
    }
}
