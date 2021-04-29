package panteao.make.ready.beanModel.tempData;

public class PlansItem {
    private String duration;
    private int noOfSubscribers;
    private int noOfAssets;
    private Object purchased;
    private String billingPeriod;
    private int price;
    private String name;
    private String currency;
    private int id;

    public String getDuration() {
        return duration;
    }

    public void setDuration(String duration) {
        this.duration = duration;
    }

    public int getNoOfSubscribers() {
        return noOfSubscribers;
    }

    public void setNoOfSubscribers(int noOfSubscribers) {
        this.noOfSubscribers = noOfSubscribers;
    }

    public int getNoOfAssets() {
        return noOfAssets;
    }

    public void setNoOfAssets(int noOfAssets) {
        this.noOfAssets = noOfAssets;
    }

    public Object getPurchased() {
        return purchased;
    }

    public void setPurchased(Object purchased) {
        this.purchased = purchased;
    }

    public String getBillingPeriod() {
        return billingPeriod;
    }

    public void setBillingPeriod(String billingPeriod) {
        this.billingPeriod = billingPeriod;
    }

    public int getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCurrency() {
        return currency;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    @Override
    public String toString() {
        return
                "PlansItem{" +
                        "duration = '" + duration + '\'' +
                        ",noOfSubscribers = '" + noOfSubscribers + '\'' +
                        ",noOfAssets = '" + noOfAssets + '\'' +
                        ",purchased = '" + purchased + '\'' +
                        ",billingPeriod = '" + billingPeriod + '\'' +
                        ",price = '" + price + '\'' +
                        ",name = '" + name + '\'' +
                        ",currency = '" + currency + '\'' +
                        ",id = '" + id + '\'' +
                        "}";
    }
}
