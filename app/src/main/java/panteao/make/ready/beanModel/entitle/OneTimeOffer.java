package panteao.make.ready.beanModel.entitle;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class OneTimeOffer implements Serializable {

    @SerializedName("periodType")
    @Expose
    private String periodType;
    @SerializedName("periodLength")
    @Expose
    private Integer periodLength;

    public String getPeriodType() {
        return periodType;
    }

    public void setPeriodType(String periodType) {
        this.periodType = periodType;
    }

    public Integer getPeriodLength() {
        return periodLength;
    }

    public void setPeriodLength(Integer periodLength) {
        this.periodLength = periodLength;
    }

}
