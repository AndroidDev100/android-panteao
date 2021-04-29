package panteao.make.ready.utils.config.bean;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class LanguageCodes {

    @SerializedName("english")
    @Expose
    private String english;
    @SerializedName("thai")
    @Expose
    private String thai;

    public String getEnglish() {
        return english;
    }

    public void setEnglish(String english) {
        this.english = english;
    }

    public void setThai(String thai) {
        this.thai = thai;
    }

    public String getThai() {
        return thai;
    }
}
