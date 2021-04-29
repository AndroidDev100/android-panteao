package panteao.make.ready.utils.helpers.carousel.entity;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class SocialData {
    @SerializedName("platform")
    @Expose
    private String platform;
    @SerializedName("userId")
    @Expose
    private String userId;

    public String getPlatform() {
        return platform;
    }

    public void setPlatform(String platform) {
        this.platform = platform;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }
}
