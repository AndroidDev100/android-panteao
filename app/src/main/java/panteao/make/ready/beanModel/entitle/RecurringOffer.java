
package panteao.make.ready.beanModel.entitle;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class RecurringOffer implements Serializable {

    @SerializedName("offerPeriod")
    @Expose
    private String offerPeriod;
    @SerializedName("trialPeriod")
    @Expose
    private TrialPeriod trialPeriod;

    public String getOfferPeriod() {
        return offerPeriod;
    }

    public void setOfferPeriod(String offerPeriod) {
        this.offerPeriod = offerPeriod;
    }

    public TrialPeriod getTrialPeriod() {
        return trialPeriod;
    }

    public void setTrialPeriod(TrialPeriod trialPeriod) {
        this.trialPeriod = trialPeriod;
    }

}
