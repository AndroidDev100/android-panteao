
package panteao.make.ready.utils.config.bean;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Version {

    @SerializedName("forceUpdate")
    @Expose
    private Boolean forceUpdate;
    @SerializedName("updatedVersion")
    @Expose
    private String updatedVersion;
    @SerializedName("recommendedUpdate")
    @Expose
    private Boolean recommendedUpdate;

    public Boolean getForceUpdate() {
        return forceUpdate;
    }

    public void setForceUpdate(Boolean forceUpdate) {
        this.forceUpdate = forceUpdate;
    }

    public String getUpdatedVersion() {
        return updatedVersion;
    }

    public void setUpdatedVersion(String updatedVersion) {
        this.updatedVersion = updatedVersion;
    }

    public Boolean getRecommendedUpdate() {
        return recommendedUpdate;
    }

    public void setRecommendedUpdate(Boolean recommendedUpdate) {
        this.recommendedUpdate = recommendedUpdate;
    }

}
