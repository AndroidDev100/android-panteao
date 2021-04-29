
package panteao.make.ready.beanModel.entitle;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class TrialPeriod implements Serializable {

    @SerializedName("trialType")
    @Expose
    private String trialType;
    @SerializedName("trialDuration")
    @Expose
    private Integer trialDuration;

    public String getTrialType() {
        return trialType;
    }

    public void setTrialType(String trialType) {
        this.trialType = trialType;
    }

    public Integer getTrialDuration() {
        return trialDuration;
    }

    public void setTrialDuration(Integer trialDuration) {
        this.trialDuration = trialDuration;
    }

}
