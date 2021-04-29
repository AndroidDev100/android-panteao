
package panteao.make.ready.beanModel.entitle;

import java.io.Serializable;
import java.util.List;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Data implements Serializable {

    @SerializedName("sku")
    @Expose
    private String sku;
    @SerializedName("purchaseAs")
    @Expose
    private List<PurchaseA> purchaseAs = null;
    @SerializedName("entitledAs")
    @Expose
    private List<EntitledAs> entitledAs;
    @SerializedName("brightcoveVideoId")
    @Expose
    private String brightcoveVideoId;
    @SerializedName("entitled")
    @Expose
    private Boolean entitled;

    public String getSku() {
        return sku;
    }

    public void setSku(String sku) {
        this.sku = sku;
    }

    public List<PurchaseA> getPurchaseAs() {
        return purchaseAs;
    }

    public void setPurchaseAs(List<PurchaseA> purchaseAs) {
        this.purchaseAs = purchaseAs;
    }

    public List<EntitledAs> getEntitledAs() {
        return entitledAs;
    }

    public void setEntitledAs(List<EntitledAs> entitledAs) {
        this.entitledAs = entitledAs;
    }

    public String getBrightcoveVideoId() {
        return brightcoveVideoId;
    }

    public void setBrightcoveVideoId(String brightcoveVideoId) {
        this.brightcoveVideoId = brightcoveVideoId;
    }

    public Boolean getEntitled() {
        return entitled;
    }

    public void setEntitled(Boolean entitled) {
        this.entitled = entitled;
    }

}
