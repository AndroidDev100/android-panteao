package panteao.make.ready.beanModel.enveuCommonRailData.config;

import com.google.gson.annotations.Expose;

public class Data {

    @Expose
    private Object configVersion;
    @Expose
    private OvpConfig ovpConfig;
    @Expose
    private Object updateInfo;

    public Object getConfigVersion() {
        return configVersion;
    }

    public void setConfigVersion(Object configVersion) {
        this.configVersion = configVersion;
    }

    public OvpConfig getOvpConfig() {
        return ovpConfig;
    }

    public void setOvpConfig(OvpConfig ovpConfig) {
        this.ovpConfig = ovpConfig;
    }

    public Object getUpdateInfo() {
        return updateInfo;
    }

    public void setUpdateInfo(Object updateInfo) {
        this.updateInfo = updateInfo;
    }

}
