
package panteao.make.ready.beanModelV3.playListModelV2;

import java.io.Serializable;
import java.util.List;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Thumbnail implements Serializable {

    @SerializedName("src")
    @Expose
    private String src;
    @SerializedName("sources")
    @Expose
    private List<Source> sources = null;

    public String getSrc() {
        return src;
    }

    public void setSrc(String src) {
        this.src = src;
    }

    public List<Source> getSources() {
        return sources;
    }

    public void setSources(List<Source> sources) {
        this.sources = sources;
    }

}
