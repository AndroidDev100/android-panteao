package panteao.make.ready.utils.config;

import java.util.Map;

import panteao.make.ready.beanModelV3.continueWatching.DataItem;
import panteao.make.ready.beanModelV3.playListModelV2.PlayListDetailsResponse;
import panteao.make.ready.beanModelV3.playListModelV2.Thumbnail;
import panteao.make.ready.beanModelV3.playListModelV2.VideosItem;
import panteao.make.ready.beanModelV3.searchV2.ItemsItem;
import panteao.make.ready.beanModelV3.videoDetailsV2.EnveuVideoDetails;
import panteao.make.ready.utils.cropImage.helpers.Logger;
import panteao.make.ready.utils.helpers.ksPreferenceKeys.KsPreferenceKeys;
import panteao.make.ready.beanModelV3.continueWatching.DataItem;
import panteao.make.ready.beanModelV3.playListModelV2.PlayListDetailsResponse;
import panteao.make.ready.beanModelV3.playListModelV2.VideosItem;
import panteao.make.ready.beanModelV3.searchV2.ItemsItem;
import panteao.make.ready.beanModelV3.videoDetailsV2.EnveuVideoDetails;
import panteao.make.ready.utils.helpers.ksPreferenceKeys.KsPreferenceKeys;

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


    public String getPosterImageUrl(VideosItem videoItem,String imageType) {
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
        Logger.e("IMAGE_TYPE", imageType + " " + finalUrl);
        return finalUrl;
    }

    public String getPosterImageUrl(ItemsItem videoItem,String imageType) {
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
        Logger.e("IMAGE_TYPE", imageType + " " + finalUrl);
        return finalUrl;
    }

    public String getPosterImageUrl(DataItem videoItem,String imageType) {
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
        Logger.e("IMAGE_TYPE", imageType + " " + finalUrl);
        return finalUrl;
    }


    public String getHeroImageUrl(PlayListDetailsResponse item) {
        String finalUrl="";
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
//        Logger.e("IMAGE_TYPE", imageType + " " + finalUrl);
        return finalUrl;
    }

    public String getThumbNailImageUrl(VideosItem videoItem,String imageType) {
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
        Logger.e("IMAGE_TYPE", imageType + " " + finalUrl);
        return finalUrl;
    }

    public String getThumbNailImageUrl(DataItem videoItem,String imageType) {
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
        Logger.e("IMAGE_TYPE", imageType + " " + finalUrl);
        return finalUrl;
    }


    public String getThumbNailImageUrl(ItemsItem videoItem,String imageType) {
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
        Logger.e("IMAGE_TYPE", imageType + " " + finalUrl);
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
        Logger.e("IMAGE_TYPE", imageType + " " + finalUrl);
        return finalUrl;
    }
}
