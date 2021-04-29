package panteao.make.ready.beanModel.connectFb;

class AppUserPlansItem {
    private String duration;
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
                "AppUserPlansItem{" +
                        "duration = '" + duration + '\'' +
                        ",billingPeriod = '" + billingPeriod + '\'' +
                        ",price = '" + price + '\'' +
                        ",name = '" + name + '\'' +
                        ",currency = '" + currency + '\'' +
                        ",id = '" + id + '\'' +
                        "}";
    }
}
