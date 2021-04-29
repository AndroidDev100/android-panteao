package panteao.make.ready.beanModel.configBean;

public class NavScreensItem {
    private String screenType;
    private int id;
    private String screenName;
    private String screenIdentifier;

    public String getScreenType() {
        return screenType;
    }

    public void setScreenType(String screenType) {
        this.screenType = screenType;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getScreenName() {
        return screenName;
    }

    public void setScreenName(String screenName) {
        this.screenName = screenName;
    }

    public String getScreenIdentifier() {
        return screenIdentifier;
    }

    public void setScreenIdentifier(String screenIdentifier) {
        this.screenIdentifier = screenIdentifier;
    }

    @Override
    public String toString() {
        return
                "NavScreensItem{" +
                        "screenType = '" + screenType + '\'' +
                        ",id = '" + id + '\'' +
                        ",screenName = '" + screenName + '\'' +
                        ",screenIdentifier = '" + screenIdentifier + '\'' +
                        "}";
    }
}
