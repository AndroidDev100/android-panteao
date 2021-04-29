package panteao.make.ready.utils.config;

import panteao.make.ready.beanModelV3.continueWatching.DataItem;
import panteao.make.ready.beanModelV3.playListModelV2.PlayListDetailsResponse;
import panteao.make.ready.beanModelV3.playListModelV2.VideosItem;
import panteao.make.ready.beanModelV3.searchV2.ItemsItem;
import panteao.make.ready.beanModelV3.videoDetailsV2.EnveuVideoDetails;
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


    public String getPosterImageUrl(VideosItem videoItem) {
        String finalUrl="";
        try {
        if (KsPreferenceKeys.getInstance().getAppLanguage().equalsIgnoreCase("English")) {
            if (videoItem.getImages() != null && videoItem.getImages().getPosterEn() != null && videoItem.getImages().getPosterEn().getSources() != null
                    && videoItem.getImages().getPosterEn().getSources().size() > 0) {
                finalUrl= videoItem.getImages().getPosterEn().getSources().get(0).getSrc();
            }else {
                if (videoItem.getImages() != null && videoItem.getImages().getPoster() != null && videoItem.getImages().getPoster().getSources() != null
                        && videoItem.getImages().getPoster().getSources().size() > 0) {
                    finalUrl= videoItem.getImages().getPoster().getSources().get(0).getSrc();
                }
            }
        }
        else if (KsPreferenceKeys.getInstance().getAppLanguage().equalsIgnoreCase("Thai")){
            if (videoItem.getImages() != null && videoItem.getImages().getPoster() != null && videoItem.getImages().getPoster().getSources() != null
                    && videoItem.getImages().getPoster().getSources().size() > 0) {
                finalUrl= videoItem.getImages().getPoster().getSources().get(0).getSrc();
            }
        }
        }catch (Exception ignored){

        }
        if(finalUrl.isEmpty()){
            if (videoItem.getImages() != null && videoItem.getImages().getPoster() != null && videoItem.getImages().getPoster().getSources() != null
                    && videoItem.getImages().getPoster().getSources().size() > 0) {
                finalUrl= videoItem.getImages().getPoster().getSources().get(0).getSrc();
            }
        }
        return finalUrl;
    }

    public String getPosterImageUrl(ItemsItem videoItem) {
        String finalUrl="";
        try {

        if (KsPreferenceKeys.getInstance().getAppLanguage().equalsIgnoreCase("English")) {
            if (videoItem.getImages() != null && videoItem.getImages().getPosterEn() != null && videoItem.getImages().getPosterEn().getSources() != null
                    && videoItem.getImages().getPosterEn().getSources().size() > 0) {
                finalUrl= videoItem.getImages().getPosterEn().getSources().get(0).getSrc();
            }else {
                if (videoItem.getImages() != null && videoItem.getImages().getPoster() != null && videoItem.getImages().getPoster().getSources() != null
                        && videoItem.getImages().getPoster().getSources().size() > 0) {
                    finalUrl= videoItem.getImages().getPoster().getSources().get(0).getSrc();
                }
            }
        }
        else if (KsPreferenceKeys.getInstance().getAppLanguage().equalsIgnoreCase("Thai")){
            if (videoItem.getImages() != null && videoItem.getImages().getPoster() != null && videoItem.getImages().getPoster().getSources() != null
                    && videoItem.getImages().getPoster().getSources().size() > 0) {
                finalUrl= videoItem.getImages().getPoster().getSources().get(0).getSrc();
            }
        }
        }catch (Exception ignored){

        }
        if(finalUrl.isEmpty()){
            if (videoItem.getImages() != null && videoItem.getImages().getPoster() != null && videoItem.getImages().getPoster().getSources() != null
                    && videoItem.getImages().getPoster().getSources().size() > 0) {
                finalUrl= videoItem.getImages().getPoster().getSources().get(0).getSrc();
            }
        }
        return finalUrl;
    }

    public String getPosterImageUrl(DataItem videoItem) {
        String finalUrl="";
        try {

            if (KsPreferenceKeys.getInstance().getAppLanguage().equalsIgnoreCase("English")) {
                if (videoItem.getImages() != null && videoItem.getImages().getPosterEn() != null && videoItem.getImages().getPosterEn().getSources() != null
                        && videoItem.getImages().getPosterEn().getSources().size() > 0) {
                    finalUrl= videoItem.getImages().getPosterEn().getSources().get(0).getSrc();
                }else {
                    if (videoItem.getImages() != null && videoItem.getImages().getPoster() != null && videoItem.getImages().getPoster().getSources() != null
                            && videoItem.getImages().getPoster().getSources().size() > 0) {
                        finalUrl= videoItem.getImages().getPoster().getSources().get(0).getSrc();
                    }
                }
            }
            else if (KsPreferenceKeys.getInstance().getAppLanguage().equalsIgnoreCase("Thai")){
                if (videoItem.getImages() != null && videoItem.getImages().getPoster() != null && videoItem.getImages().getPoster().getSources() != null
                        && videoItem.getImages().getPoster().getSources().size() > 0) {
                    finalUrl= videoItem.getImages().getPoster().getSources().get(0).getSrc();
                }
            }
        }catch (Exception ignored){

        }
        if(finalUrl.isEmpty()){
            if (videoItem.getImages() != null && videoItem.getImages().getPoster() != null && videoItem.getImages().getPoster().getSources() != null
                    && videoItem.getImages().getPoster().getSources().size() > 0) {
                finalUrl= videoItem.getImages().getPoster().getSources().get(0).getSrc();
            }
        }
        return finalUrl;
    }


    public String getHeroImageUrl(PlayListDetailsResponse item) {
        String finalUrl="";
        try {

        if (KsPreferenceKeys.getInstance().getAppLanguage().equalsIgnoreCase("English")) {
            if (item.getItems().get(0).getContent().getImages()!=null && item.getItems().get(0).getContent().getImages().getPosterEn()!=null && item.getItems().get(0).getContent().getImages().getPosterEn().getSources()!=null
                    && item.getItems().get(0).getContent().getImages().getPosterEn().getSources().size()>0){
                finalUrl=item.getItems().get(0).getContent().getImages().getPosterEn().getSources().get(0).getSrc();
            }else {
                if (item.getItems().get(0).getContent().getImages()!=null && item.getItems().get(0).getContent().getImages().getPoster()!=null && item.getItems().get(0).getContent().getImages().getPoster().getSources()!=null
                        && item.getItems().get(0).getContent().getImages().getPoster().getSources().size()>0){
                    finalUrl=item.getItems().get(0).getContent().getImages().getPoster().getSources().get(0).getSrc();
                }
            }
        }
        else if (KsPreferenceKeys.getInstance().getAppLanguage().equalsIgnoreCase("Thai")){
            if (item.getItems().get(0).getContent().getImages()!=null && item.getItems().get(0).getContent().getImages().getPoster()!=null && item.getItems().get(0).getContent().getImages().getPoster().getSources()!=null
                    && item.getItems().get(0).getContent().getImages().getPoster().getSources().size()>0){
                finalUrl=item.getItems().get(0).getContent().getImages().getPoster().getSources().get(0).getSrc();
            }
        }
        }catch (Exception ignored){

        }

        return finalUrl;
    }

    public String getThumbNailImageUrl(VideosItem videoItem) {
        String finalUrl="";
        try {

        if (KsPreferenceKeys.getInstance().getAppLanguage().equalsIgnoreCase("English")) {
            if (videoItem.getImages()!=null && videoItem.getImages().getThumbnailEn()!=null && videoItem.getImages().getThumbnailEn().getSources()!=null
                    && videoItem.getImages().getThumbnailEn().getSources().size()>0){
                finalUrl=videoItem.getImages().getThumbnailEn().getSources().get(0).getSrc();
            }else {
                if (videoItem.getImages()!=null && videoItem.getImages().getThumbnail()!=null && videoItem.getImages().getThumbnail().getSources()!=null
                        && videoItem.getImages().getThumbnail().getSources().size()>0){
                    finalUrl=videoItem.getImages().getThumbnail().getSources().get(0).getSrc();
                }
            }
        }
        else if (KsPreferenceKeys.getInstance().getAppLanguage().equalsIgnoreCase("Thai")){
            if (videoItem.getImages()!=null && videoItem.getImages().getThumbnail()!=null && videoItem.getImages().getThumbnail().getSources()!=null
                    && videoItem.getImages().getThumbnail().getSources().size()>0){
                finalUrl=videoItem.getImages().getThumbnail().getSources().get(0).getSrc();
            }
        }
        }catch (Exception ignored){

        }
        if(finalUrl.isEmpty()){
            if (videoItem.getImages()!=null && videoItem.getImages().getThumbnail()!=null && videoItem.getImages().getThumbnail().getSources()!=null
                    && videoItem.getImages().getThumbnail().getSources().size()>0){
                finalUrl=videoItem.getImages().getThumbnail().getSources().get(0).getSrc();
            }
        }
        return finalUrl;
    }

    public String getThumbNailImageUrl(DataItem videoItem) {
        String finalUrl="";
        try {

            if (KsPreferenceKeys.getInstance().getAppLanguage().equalsIgnoreCase("English")) {
                if (videoItem.getImages()!=null && videoItem.getImages().getThumbnailEn()!=null && videoItem.getImages().getThumbnailEn().getSources()!=null
                        && videoItem.getImages().getThumbnailEn().getSources().size()>0){
                    finalUrl=videoItem.getImages().getThumbnailEn().getSources().get(0).getSrc();
                }else {
                    if (videoItem.getImages()!=null && videoItem.getImages().getThumbnail()!=null && videoItem.getImages().getThumbnail().getSources()!=null
                            && videoItem.getImages().getThumbnail().getSources().size()>0){
                        finalUrl=videoItem.getImages().getThumbnail().getSources().get(0).getSrc();
                    }
                }
            }
            else if (KsPreferenceKeys.getInstance().getAppLanguage().equalsIgnoreCase("Thai")){
                if (videoItem.getImages()!=null && videoItem.getImages().getThumbnail()!=null && videoItem.getImages().getThumbnail().getSources()!=null
                        && videoItem.getImages().getThumbnail().getSources().size()>0){
                    finalUrl=videoItem.getImages().getThumbnail().getSources().get(0).getSrc();
                }
            }
        }catch (Exception ignored){

        }
        if(finalUrl.isEmpty()){
            if (videoItem.getImages()!=null && videoItem.getImages().getThumbnail()!=null && videoItem.getImages().getThumbnail().getSources()!=null
                    && videoItem.getImages().getThumbnail().getSources().size()>0){
                finalUrl=videoItem.getImages().getThumbnail().getSources().get(0).getSrc();
            }
        }
        return finalUrl;
    }


    public String getThumbNailImageUrl(ItemsItem videoItem) {
        String finalUrl="";
        try {

        if (KsPreferenceKeys.getInstance().getAppLanguage().equalsIgnoreCase("English")) {
            if (videoItem.getImages()!=null && videoItem.getImages().getThumbnailEn()!=null && videoItem.getImages().getThumbnailEn().getSources()!=null
                    && videoItem.getImages().getThumbnailEn().getSources().size()>0){
                finalUrl=videoItem.getImages().getThumbnailEn().getSources().get(0).getSrc();
            }else {
                if (videoItem.getImages()!=null && videoItem.getImages().getThumbnail()!=null && videoItem.getImages().getThumbnail().getSources()!=null
                        && videoItem.getImages().getThumbnail().getSources().size()>0){
                    finalUrl=videoItem.getImages().getThumbnail().getSources().get(0).getSrc();
                }
            }
        }
        else if (KsPreferenceKeys.getInstance().getAppLanguage().equalsIgnoreCase("Thai")){
            if (videoItem.getImages()!=null && videoItem.getImages().getThumbnail()!=null && videoItem.getImages().getThumbnail().getSources()!=null
                    && videoItem.getImages().getThumbnail().getSources().size()>0){
                finalUrl=videoItem.getImages().getThumbnail().getSources().get(0).getSrc();
            }
        }
        }catch (Exception ignored){

        }
        if(finalUrl.isEmpty()){
            if (videoItem.getImages()!=null && videoItem.getImages().getThumbnail()!=null && videoItem.getImages().getThumbnail().getSources()!=null
                    && videoItem.getImages().getThumbnail().getSources().size()>0){
                finalUrl=videoItem.getImages().getThumbnail().getSources().get(0).getSrc();
            }
        }
        return finalUrl;
    }

    public String getPosterImageUrl(EnveuVideoDetails videoItem) {
        String finalUrl="";
        try {

        if (KsPreferenceKeys.getInstance().getAppLanguage().equalsIgnoreCase("English")) {
            if (videoItem.getImages() != null && videoItem.getImages().getPosterEn() != null && videoItem.getImages().getPosterEn().getSources() != null
                    && videoItem.getImages().getPosterEn().getSources().size() > 0) {
                finalUrl= videoItem.getImages().getPosterEn().getSources().get(0).getSrc();
            }else {
                if (videoItem.getImages() != null && videoItem.getImages().getPoster() != null && videoItem.getImages().getPoster().getSources() != null
                        && videoItem.getImages().getPoster().getSources().size() > 0) {
                    finalUrl= videoItem.getImages().getPoster().getSources().get(0).getSrc();
                }
            }
        }
        else if (KsPreferenceKeys.getInstance().getAppLanguage().equalsIgnoreCase("Thai")){
            if (videoItem.getImages() != null && videoItem.getImages().getPoster() != null && videoItem.getImages().getPoster().getSources() != null
                    && videoItem.getImages().getPoster().getSources().size() > 0) {
                finalUrl= videoItem.getImages().getPoster().getSources().get(0).getSrc();
            }
        }
        }catch (Exception ignored){

        }
        if(finalUrl.isEmpty()){
            if (videoItem.getImages() != null && videoItem.getImages().getPoster() != null && videoItem.getImages().getPoster().getSources() != null
                    && videoItem.getImages().getPoster().getSources().size() > 0) {
                finalUrl= videoItem.getImages().getPoster().getSources().get(0).getSrc();
            }
        }
        return finalUrl;
    }
}
