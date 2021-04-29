package panteao.make.ready.beanModel.mobileAds;

public class DataItem {
    private String adClient;
    private int id;
    private String adSlot;

    public String getAdClient() {
        return adClient;
    }

    public void setAdClient(String adClient) {
        this.adClient = adClient;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getAdSlot() {
        return adSlot;
    }

    public void setAdSlot(String adSlot) {
        this.adSlot = adSlot;
    }

    @Override
    public String toString() {
        return
                "DataItem{" +
                        "adClient = '" + adClient + '\'' +
                        ",id = '" + id + '\'' +
                        ",adSlot = '" + adSlot + '\'' +
                        "}";
    }
}
