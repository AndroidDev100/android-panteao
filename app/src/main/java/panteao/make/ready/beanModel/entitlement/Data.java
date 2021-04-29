package panteao.make.ready.beanModel.entitlement;

import java.io.Serializable;
import java.util.List;

public class Data implements Serializable {
    private String videoLink;
    private List<String> purchaseOptions;
    private boolean state;
    private Object purchasedAs;


    public String getVideoLink() {
        return videoLink;
    }

    public void setVideoLink(String videoLink) {
        this.videoLink = videoLink;
    }

    public List<String> getPurchaseOptions() {
        return purchaseOptions;
    }

    public void setPurchaseOptions(List<String> purchaseOptions) {
        this.purchaseOptions = purchaseOptions;
    }

    public boolean isState() {
        return state;
    }

    public void setState(boolean state) {
        this.state = state;
    }

    public Object getPurchasedAs() {
        return purchasedAs;
    }

    public void setPurchasedAs(Object purchasedAs) {
        this.purchasedAs = purchasedAs;
    }

    @Override
    public String toString() {
        return "PlayListDetailsResponse{" +
                "videoLink='" + videoLink + '\'' +
                ", purchaseOptions=" + purchaseOptions +
                ", state=" + state +
                ", purchasedAs=" + purchasedAs +
                '}';
    }
}