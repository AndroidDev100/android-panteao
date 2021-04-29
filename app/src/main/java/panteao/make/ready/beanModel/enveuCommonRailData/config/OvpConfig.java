package panteao.make.ready.beanModel.enveuCommonRailData.config;

import com.google.gson.annotations.Expose;

public class OvpConfig {

    @Expose
    private ImageTypeMappings imageTypeMappings;

    public ImageTypeMappings getImageTypeMappings() {
        return imageTypeMappings;
    }

    public void setImageTypeMappings(ImageTypeMappings imageTypeMappings) {
        this.imageTypeMappings = imageTypeMappings;
    }

}
