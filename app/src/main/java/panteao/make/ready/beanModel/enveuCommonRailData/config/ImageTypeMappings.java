package panteao.make.ready.beanModel.enveuCommonRailData.config;

import com.google.gson.annotations.SerializedName;

public class ImageTypeMappings {

    @SerializedName("CIR")
    private String circle;
    @SerializedName("CST")
    private String custom;
    @SerializedName("LDS")
    private String lanscape;
    @SerializedName("LDS2")
    private String landscape2;
    @SerializedName("PR1")
    private String potrait;
    @SerializedName("PR2")
    private String potrait2;
    @SerializedName("SQR")
    private String square;

    public String getCircle() {
        return circle;
    }

    public void setCircle(String circle) {
        this.circle = circle;
    }

    public String getCustom() {
        return custom;
    }

    public void setCustom(String custom) {
        this.custom = custom;
    }

    public String getLanscape() {
        return lanscape;
    }

    public void setLanscape(String lanscape) {
        this.lanscape = lanscape;
    }

    public String getLandscape2() {
        return landscape2;
    }

    public void setLandscape2(String landscape2) {
        this.landscape2 = landscape2;
    }

    public String getPotrait() {
        return potrait;
    }

    public void setPotrait(String potrait) {
        this.potrait = potrait;
    }

    public String getPotrait2() {
        return potrait2;
    }

    public void setPotrait2(String potrait2) {
        this.potrait2 = potrait2;
    }

    public String getSquare() {
        return square;
    }

    public void setSquare(String square) {
        this.square = square;
    }
}
