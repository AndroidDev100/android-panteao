package panteao.make.ready.utils.config;

import com.google.gson.Gson;

import java.util.HashMap;
import java.util.Map;

import panteao.make.ready.beanModelV3.continueWatching.DataItem;
import panteao.make.ready.beanModelV3.playListModelV2.PlayListDetailsResponse;
import panteao.make.ready.beanModelV3.playListModelV2.Thumbnail;
import panteao.make.ready.beanModelV3.playListModelV2.VideosItem;
import panteao.make.ready.beanModelV3.searchV2.ItemsItem;
import panteao.make.ready.beanModelV3.videoDetailsV2.EnveuVideoDetails;
import panteao.make.ready.enums.KalturaImageType;
import panteao.make.ready.utils.Utils;
import panteao.make.ready.utils.cropImage.helpers.Logger;

public class ImageLayer {
    private static ImageLayer imageLayerInstance;

    private ImageLayer() {

    }

    public static ImageLayer getInstance() {
        if (imageLayerInstance == null) {
            imageLayerInstance = new ImageLayer();
        }
        return (imageLayerInstance);
    }


    public String getPosterImageUrl(VideosItem videoItem, String imageType) {
        String finalUrl = "";
        try {
            if (videoItem.getImages() != null && videoItem.getImages().containsKey(imageType)) {
                finalUrl = videoItem.getImages().get(imageType).getSources().get(0).getSrc();
            } else {
                Map.Entry<String, Thumbnail> entry = videoItem.getImages().entrySet().iterator().next();
                finalUrl = videoItem.getImages().get(entry.getKey()).getSources().get(0).getSrc();
            }
        } catch (Exception ignored) {

        }
        // Logger.e("IMAGE_TYPE", imageType + " " + finalUrl);
        return finalUrl;
    }

    public String getPosterImageUrl(ItemsItem videoItem, String imageType) {
        String finalUrl = "";
        try {
            if (videoItem.getImages() != null && videoItem.getImages().containsKey(imageType)) {
                finalUrl = videoItem.getImages().get(imageType).getSources().get(0).getSrc();
            } else {
                Map.Entry<String, Thumbnail> entry = videoItem.getImages().entrySet().iterator().next();
                finalUrl = videoItem.getImages().get(entry.getKey()).getSources().get(0).getSrc();
            }
        } catch (Exception ignored) {

        }
        return finalUrl;
    }

    public String getPosterImageUrl(DataItem videoItem, String imageType) {
        String finalUrl = "";
        try {
            if (videoItem.getImages() != null && videoItem.getImages().containsKey(imageType)) {
                finalUrl = videoItem.getImages().get(imageType).getSources().get(0).getSrc();
            } else {
                Map.Entry<String, Thumbnail> entry = videoItem.getImages().entrySet().iterator().next();
                finalUrl = videoItem.getImages().get(entry.getKey()).getSources().get(0).getSrc();
            }
        } catch (Exception ignored) {

        }
        // Logger.e("IMAGE_TYPE", imageType + " " + finalUrl);
        return finalUrl;
    }


    public String getHeroImageUrl(PlayListDetailsResponse item) {
        String finalUrl = "";
//        try {
//
//        if (KsPreferenceKeys.getInstance().getAppLanguage().equalsIgnoreCase("English")) {
//            if (item.getItems().get(0).getContent().getImages()!=null && item.getItems().get(0).getContent().getImages().getPosterEn()!=null && item.getItems().get(0).getContent().getImages().getPosterEn().getSources()!=null
//                    && item.getItems().get(0).getContent().getImages().getPosterEn().getSources().size()>0){
//                finalUrl=item.getItems().get(0).getContent().getImages().getPosterEn().getSources().get(0).getSrc();
//            }else {
//                if (item.getItems().get(0).getContent().getImages()!=null && item.getItems().get(0).getContent().getImages().getPoster()!=null && item.getItems().get(0).getContent().getImages().getPoster().getSources()!=null
//                        && item.getItems().get(0).getContent().getImages().getPoster().getSources().size()>0){
//                    finalUrl=item.getItems().get(0).getContent().getImages().getPoster().getSources().get(0).getSrc();
//                }
//            }
//        }
//        else if (KsPreferenceKeys.getInstance().getAppLanguage().equalsIgnoreCase("Thai")){
//            if (item.getItems().get(0).getContent().getImages()!=null && item.getItems().get(0).getContent().getImages().getPoster()!=null && item.getItems().get(0).getContent().getImages().getPoster().getSources()!=null
//                    && item.getItems().get(0).getContent().getImages().getPoster().getSources().size()>0){
//                finalUrl=item.getItems().get(0).getContent().getImages().getPoster().getSources().get(0).getSrc();
//            }
//        }
//        }catch (Exception ignored){
//
//        }
//               // Logger.e("IMAGE_TYPE", imageType + " " + finalUrl);
        return finalUrl;
    }

    public String getThumbNailImageUrl(VideosItem videoItem, String imageType) {
        String finalUrl = "";
        try {
            if (videoItem.getImages() != null && videoItem.getImages().containsKey(imageType)) {
                finalUrl = videoItem.getImages().get(imageType).getSources().get(0).getSrc();
            } else {
                Map.Entry<String, Thumbnail> entry = videoItem.getImages().entrySet().iterator().next();
                finalUrl = videoItem.getImages().get(entry.getKey()).getSources().get(0).getSrc();
            }
        } catch (Exception ignored) {

        }
        // Logger.e("IMAGE_TYPE", imageType + " " + finalUrl);
        return finalUrl;
    }

    public String getThumbNailImageUrl(DataItem videoItem, String imageType) {
        String finalUrl = "";
        try {
            if (videoItem.getImages() != null && videoItem.getImages().containsKey(imageType)) {
                finalUrl = videoItem.getImages().get(imageType).getSources().get(0).getSrc();
            } else {
                Map.Entry<String, Thumbnail> entry = videoItem.getImages().entrySet().iterator().next();
                finalUrl = videoItem.getImages().get(entry.getKey()).getSources().get(0).getSrc();
            }
        } catch (Exception ignored) {

        }
        // Logger.e("IMAGE_TYPE", imageType + " " + finalUrl);
        return finalUrl;
    }


    public String getThumbNailImageUrl(ItemsItem videoItem, String imageType) {
        String finalUrl = "";
        try {
            if (videoItem.getImages() != null && videoItem.getImages().containsKey(imageType)) {
                finalUrl = videoItem.getImages().get(imageType).getSources().get(0).getSrc();
            } else {
                Map.Entry<String, Thumbnail> entry = videoItem.getImages().entrySet().iterator().next();
                finalUrl = videoItem.getImages().get(entry.getKey()).getSources().get(0).getSrc();
            }
        } catch (Exception ignored) {

        }
        // Logger.e("IMAGE_TYPE", imageType + " " + finalUrl);
        return finalUrl;
    }

    public String getPosterImageUrl(EnveuVideoDetails videoItem, String imageType) {
        String finalUrl = "";
        try {
            if (videoItem.getImages() != null && videoItem.getImages().containsKey(imageType)) {
                finalUrl = videoItem.getImages().get(imageType).getSources().get(0).getSrc();
            } else {
                Map.Entry<String, Thumbnail> entry = videoItem.getImages().entrySet().iterator().next();
                finalUrl = videoItem.getImages().get(entry.getKey()).getSources().get(0).getSrc();
            }
        } catch (Exception ignored) {

        }
        // Logger.e("IMAGE_TYPE", imageType + " " + finalUrl);
        return finalUrl;
    }

    public String getFilteredImage(HashMap<String, Thumbnail> crousalImages, KalturaImageType imageType, int i, int i1) {
        int width = i;
        int height = i1;
        String imageUrl = "";
        if (crousalImages != null) {
            for (Map.Entry<String, Thumbnail> entry : crousalImages.entrySet()) {
                if (imageType == KalturaImageType.CAROUSAL_FULL_IMAGE) {
                    if ((entry.getValue().getSources().get(0).getWidth() / entry.getValue().getSources().get(0).getHeight()) > 3) {
                        imageUrl = entry.getValue().getSources().get(0).getSrc();
                    }
                } else if (imageType == KalturaImageType.LANDSCAPE) {
                    if ((entry.getValue().getSources().get(0).getWidth() / entry.getValue().getSources().get(0).getHeight()) >= 1 && (entry.getValue().getSources().get(0).getWidth() / entry.getValue().getSources().get(0).getHeight()) < 3) {
                        imageUrl = entry.getValue().getSources().get(0).getSrc();
                    }
                } else if (imageType == KalturaImageType.PORTRAIT) {
                    if ((entry.getValue().getSources().get(0).getWidth() / entry.getValue().getSources().get(0).getHeight()) < 1) {
                        imageUrl = entry.getValue().getSources().get(0).getSrc();
                    }
                } else if (imageType == KalturaImageType.PORTRAIT_2_3) {
                    if ((entry.getValue().getSources().get(0).getWidth() / entry.getValue().getSources().get(0).getHeight()) < 1) {
                        imageUrl = entry.getValue().getSources().get(0).getSrc();
                        width=0;
                        height=350;
                    }
                }
            }
            if (imageUrl.isEmpty()) {
                Map.Entry<String, Thumbnail> entry = crousalImages.entrySet().iterator().next();
                imageUrl = entry.getValue().getSources().get(0).getSrc();
            }
            Logger.e("RECOMMENDED_IMAGES", Utils.INSTANCE.getFilteredUrl(imageUrl, width, height));
        }
        return Utils.INSTANCE.getFilteredUrl(imageUrl, width, height);
    }

    public String getSeriesPosterImageUrl(EnveuVideoDetails videoItem, String imageType) {
        String finalUrl = "";
        try {
            if (videoItem.getLinkedContent().getImages() != null && videoItem.getLinkedContent().getImages().containsKey(imageType)) {
                finalUrl = videoItem.getLinkedContent().getImages().get(imageType).getSources().get(0).getSrc();
            } else {
                Map.Entry<String, Thumbnail> entry = videoItem.getLinkedContent().getImages().entrySet().iterator().next();
                finalUrl = videoItem.getLinkedContent().getImages().get(entry.getKey()).getSources().get(0).getSrc();
            }
        } catch (Exception ignored) {

        }
        Logger.e("IMAGE_TYPE", imageType + " " + finalUrl);
        return finalUrl;
    }

    public String getFilteredDetailImage(HashMap<String, Thumbnail> crousalImages, KalturaImageType imageType, int i, int i1) {
        int width = i;
        int height = i1;
        String imageUrl = "";
        if (crousalImages != null) {
            for (Map.Entry<String, Thumbnail> entry : crousalImages.entrySet()) {
                if (imageType == KalturaImageType.CAROUSAL_FULL_IMAGE) {
                    if ((entry.getValue().getSources().get(0).getWidth() / entry.getValue().getSources().get(0).getHeight()) > 3) {
                        imageUrl = entry.getValue().getSources().get(0).getSrc();
                    }
                } else if (imageType == KalturaImageType.LANDSCAPE) {
                    if ((entry.getValue().getSources().get(0).getWidth() / entry.getValue().getSources().get(0).getHeight()) >= 1 && (entry.getValue().getSources().get(0).getWidth() / entry.getValue().getSources().get(0).getHeight()) < 3) {
                        imageUrl = entry.getValue().getSources().get(0).getSrc();
                        width=244;
                        height=0;
                    }
                } else if (imageType == KalturaImageType.PORTRAIT) {
                    if ((entry.getValue().getSources().get(0).getWidth() / entry.getValue().getSources().get(0).getHeight()) < 1) {
                        imageUrl = entry.getValue().getSources().get(0).getSrc();
                    }
                } else if (imageType == KalturaImageType.PORTRAIT_2_3) {
                    if ((entry.getValue().getSources().get(0).getWidth() / entry.getValue().getSources().get(0).getHeight()) < 1) {
                        imageUrl = entry.getValue().getSources().get(0).getSrc();
                        width=0;
                        height=244;
                    }
                }
            }
            if (imageUrl.isEmpty()) {
                Map.Entry<String, Thumbnail> entry = crousalImages.entrySet().iterator().next();
                imageUrl = entry.getValue().getSources().get(0).getSrc();
            }
            Logger.e("RECOMMENDED_IMAGES", Utils.INSTANCE.getFilteredUrl(imageUrl, width, height));
        }
        return Utils.INSTANCE.getFilteredUrl(imageUrl, width, height);
    }


}
