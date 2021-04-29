package panteao.make.ready.beanModel.configBean;

public class UpdateInfo {
    private boolean recommendedUpdate;
    private String availableVersion;
    private boolean forceUpdate;

    public boolean isRecommendedUpdate() {
        return recommendedUpdate;
    }

    public void setRecommendedUpdate(boolean recommendedUpdate) {
        this.recommendedUpdate = recommendedUpdate;
    }

    public String getAvailableVersion() {
        return availableVersion;
    }

    public void setAvailableVersion(String availableVersion) {
        this.availableVersion = availableVersion;
    }

    public boolean isForceUpdate() {
        return forceUpdate;
    }

    public void setForceUpdate(boolean forceUpdate) {
        this.forceUpdate = forceUpdate;
    }

    @Override
    public String toString() {
        return
                "UpdateInfo{" +
                        "recommendedUpdate = '" + recommendedUpdate + '\'' +
                        ",availableVersion = '" + availableVersion + '\'' +
                        ",forceUpdate = '" + forceUpdate + '\'' +
                        "}";
    }
}
