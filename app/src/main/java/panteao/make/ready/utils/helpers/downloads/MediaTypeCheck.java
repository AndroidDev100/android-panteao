package panteao.make.ready.utils.helpers.downloads;

import panteao.make.ready.utils.MediaTypeConstants;
import panteao.make.ready.utils.MediaTypeConstants;

public class MediaTypeCheck {


    public static boolean isMediaTypeSupported(String assetType) {
        boolean supported=false;
        if (MediaTypeConstants.getInstance().getMovie().equalsIgnoreCase(assetType)
            || MediaTypeConstants.getInstance().getEpisode().equalsIgnoreCase(assetType)
                || MediaTypeConstants.getInstance().getShow().equalsIgnoreCase(assetType)){
            supported=true;
        }
        return supported;
    }
}
