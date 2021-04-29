package panteao.make.ready.beanModel.configBean;

import java.util.List;

@SuppressWarnings("ALL")
public class Data {
    private UpdateInfo updateInfo;
    private String cloudFrontEndpoint;
    private String cloudFrontVideoEndpoint;
    private double configVersion;
    private String serverBaseURL;
    private List<NavScreensItem> navScreens;
    private String imageTransformationEndpoint;

    public String getImageTransformationEndpoint() {
        return imageTransformationEndpoint;
    }

    public void setImageTransformationEndpoint(String imageTransformationEndpoint) {
        this.imageTransformationEndpoint = imageTransformationEndpoint;
    }

    public UpdateInfo getUpdateInfo() {
        return updateInfo;
    }

    public void setUpdateInfo(UpdateInfo updateInfo) {
        this.updateInfo = updateInfo;
    }

    public String getCloudFrontEndpoint() {
        return cloudFrontEndpoint;
    }

    public void setCloudFrontEndpoint(String cloudFrontEndpoint) {
        this.cloudFrontEndpoint = cloudFrontEndpoint;
    }

    public String getCloudFrontVideoEndpoint() {
        return cloudFrontVideoEndpoint;
    }

    public void setCloudFrontVideoEndpoint(String cloudFrontVideoEndpoint) {
        this.cloudFrontVideoEndpoint = cloudFrontVideoEndpoint;
    }

    public double getConfigVersion() {
        return configVersion;
    }

    public void setConfigVersion(double configVersion) {
        this.configVersion = configVersion;
    }

    public String getServerBaseURL() {
        return serverBaseURL;
    }

    public void setServerBaseURL(String serverBaseURL) {
        this.serverBaseURL = serverBaseURL;
    }

    public List<NavScreensItem> getNavScreens() {
        return navScreens;
    }

    public void setNavScreens(List<NavScreensItem> navScreens) {
        this.navScreens = navScreens;
    }

    @Override
    public String toString() {
        return
                "PlayListDetailsResponse{" +
                        "updateInfo = '" + updateInfo + '\'' +
                        ",cloudFrontEndpoint = '" + cloudFrontEndpoint + '\'' +
                        ",cloudFrontVideoEndpoint = '" + cloudFrontVideoEndpoint + '\'' +
                        ",configVersion = '" + configVersion + '\'' +
                        ",serverBaseURL = '" + serverBaseURL + '\'' +
                        ",navScreens = '" + navScreens + '\'' +
                        "}";
    }
}