package panteao.make.ready.beanModel.enveuCommonRailData;

import android.os.Parcel;
import android.os.Parcelable;
import android.util.Log;
import android.widget.Toast;

import com.make.baseCollection.baseCategoryModel.BaseCategory;
import com.make.enums.ImageSource;
import com.make.enums.ImageType;
import com.make.enums.LandingPageType;
import com.make.enums.Layouts;
import com.make.enums.WidgetImageType;

import panteao.make.ready.beanModelV3.continueWatching.DataItem;
import panteao.make.ready.beanModelV3.playListModelV2.ItemsItem;
import panteao.make.ready.beanModelV3.playListModelV2.PlayListDetailsResponse;
import panteao.make.ready.beanModelV3.playListModelV2.VideosItem;
import panteao.make.ready.beanModelV3.videoDetailsV2.EnveuVideoDetails;
import panteao.make.ready.callbacks.commonCallbacks.CommonApiCallBack;
import panteao.make.ready.beanModelV3.uiConnectorModelV2.EnveuVideoItemBean;
import panteao.make.ready.layersV2.VideoDetailLayer;
import panteao.make.ready.utils.MediaTypeConstants;
import panteao.make.ready.utils.commonMethods.AppCommonMethod;
import panteao.make.ready.utils.config.ImageLayer;
import panteao.make.ready.utils.constants.AppConstants;

import com.google.gson.Gson;

import panteao.make.ready.utils.cropImage.helpers.Logger;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import panteao.make.ready.beanModelV3.continueWatching.DataItem;
import panteao.make.ready.beanModelV3.playListModelV2.ItemsItem;
import panteao.make.ready.beanModelV3.playListModelV2.PlayListDetailsResponse;
import panteao.make.ready.beanModelV3.playListModelV2.VideosItem;
import panteao.make.ready.beanModelV3.uiConnectorModelV2.EnveuVideoItemBean;
import panteao.make.ready.beanModelV3.videoDetailsV2.EnveuVideoDetails;
import panteao.make.ready.callbacks.commonCallbacks.CommonApiCallBack;
import panteao.make.ready.layersV2.VideoDetailLayer;
import panteao.make.ready.utils.cropImage.helpers.Logger;

public class RailCommonData implements Parcelable {

    private int maxContent;
    private String displayName;
    private String playlistType;
    private List<EnveuVideoItemBean> enveuVideoItemBeans = new ArrayList<>();
    private int seasonNumber;
    private String seasonName;
    private int railType;
    private BaseCategory screenWidget;
    private boolean status;
    private int pageTotal;
    private int layoutType = 0;
    private String searchKey;
    private int totalCount = 0;
    private String assetType;
    private boolean isSeries = false;
    private boolean isContinueWatching = false;
    private boolean isAd = false;
    private int pageNumber = 0;
    public String customeInternalPageId = "";

    public void setRailType(int railType) {
        this.railType = railType;
    }

    protected RailCommonData(Parcel in) {
        maxContent = in.readInt();
        displayName = in.readString();
        playlistType = in.readString();
        seasonNumber = in.readInt();
        seasonName = in.readString();
        railType = in.readInt();
        screenWidget = in.readParcelable(BaseCategory.class.getClassLoader());
        status = in.readByte() != 0;
        pageTotal = in.readInt();
        layoutType = in.readInt();
        searchKey = in.readString();
        totalCount = in.readInt();
        assetType = in.readString();
        isSeries = in.readByte() != 0;
        isContinueWatching = in.readByte() != 0;
        isAd = in.readByte() != 0;
        pageNumber = in.readInt();
        pageSize = in.readInt();
    }

    public static final Creator<RailCommonData> CREATOR = new Creator<RailCommonData>() {
        @Override
        public RailCommonData createFromParcel(Parcel in) {
            return new RailCommonData(in);
        }

        @Override
        public RailCommonData[] newArray(int size) {
            return new RailCommonData[size];
        }
    };

    public int getPageNumber() {
        return pageNumber;
    }

    public void setPageNumber(int pageNumber) {
        this.pageNumber = pageNumber;
    }

    public int getPageSize() {
        return pageSize;
    }

    public void setPageSize(int pageSize) {
        this.pageSize = pageSize;
    }

    private int pageSize = 0;

    // for playlist constructor
    public RailCommonData(PlayListDetailsResponse playListDetailsResponse, BaseCategory screenWidget, boolean type) {
        this.screenWidget = screenWidget;
        /*type = false calling for home playlist data -->> type = true calling from more listing*/
        if (!type) {
            setBrighcoveVideos(playListDetailsResponse.getItems(), screenWidget.getWidgetImageType());
            isSeries = false;

            setRailType(screenWidget.getLayout(), screenWidget.getContentImageType());
        } else {
            setBrighcoveVideos(playListDetailsResponse.getItems(), ImageType.LDS.name());
            isSeries = false;
        }
    }

    // for episode listing constructor
    public RailCommonData(PlayListDetailsResponse playListDetailsResponse) {
        setEpisodesList(playListDetailsResponse.getItems(), ImageType.LDS.name());
        isSeries = false;
    }

    // for related content listing constructor
    public RailCommonData(PlayListDetailsResponse playListDetailsResponse, MediaTypeConstants mediaTypeConstants) {
        setEpisodesList(playListDetailsResponse.getItems(), ImageType.LDS.name(),mediaTypeConstants);
        isSeries = false;
    }

    /*if (videoItem.getContentType()!=null && !videoItem.getContentType().equalsIgnoreCase("")){
        if (!videoItem.getContentType().equalsIgnoreCase(MediaTypeConstants.getInstance().getTrailor())){*/

    // for related content listing constructor - Instructor page
    private void setEpisodesList(List<ItemsItem> videos, String imageType, MediaTypeConstants mediaTypeConstants) {
        try {
            if (videos != null && videos.size() > 0) {
                final RailCommonData railCommonData = this;
                for (int i = 0; i < videos.size(); i++) {
                    VideosItem videoItem = videos.get(i).getContent();
                    Log.w("mediaTypes",videoItem.getContentType()+"   "+MediaTypeConstants.getInstance().getChapter());
                    if (videoItem.getContentType()!=null && !videoItem.getContentType().equalsIgnoreCase("")){
                        if (!videoItem.getContentType().equalsIgnoreCase(MediaTypeConstants.getInstance().getTrailor()) &&
                                !videoItem.getContentType().equalsIgnoreCase(MediaTypeConstants.getInstance().getChapter()) &&
                                !videoItem.getContentType().equalsIgnoreCase(MediaTypeConstants.getInstance().getEpisode())){
                            Gson gson = new Gson();
                            String tmp = gson.toJson(videoItem);
                            EnveuVideoItemBean enveuVideoItemBean = new EnveuVideoItemBean(videoItem, videos.get(i).getContentOrder(), imageType);
                            if (videoItem != null) {
                                if (videoItem.getSeasonNumber() != null && !videoItem.getSeasonNumber().equalsIgnoreCase("")) {
                                    int seasonNumber = Integer.parseInt(videoItem.getSeasonNumber());
                                    railCommonData.setSeasonNumber(seasonNumber);
                                }
                                enveuVideoItemBean.setVodCount(videos.size());
                                if (screenWidget != null && screenWidget.getWidgetImageType() != null && screenWidget.getWidgetImageType().equalsIgnoreCase(WidgetImageType.THUMBNAIL.toString())) {
                                    Logger.e("Screen WidgetType ", screenWidget.getWidgetImageType());
                                    String imageUrl = ImageLayer.getInstance().getThumbNailImageUrl(videoItem, screenWidget.getWidgetImageType());
                                    enveuVideoItemBean.setImages(videoItem.getImages());
                                    enveuVideoItemBean.setPosterURL(imageUrl);
                                    enveuVideoItemBean.setThumbnailImage(ImageLayer.getInstance().getPosterImageUrl(videoItem, screenWidget.getWidgetImageType()));

                                } else {
                                    String imageUrl = ImageLayer.getInstance().getPosterImageUrl(videoItem, ImageType.LDS.name());
                                    enveuVideoItemBean.setImages(videoItem.getImages());
                                    enveuVideoItemBean.setPosterURL(imageUrl);
                                    enveuVideoItemBean.setThumbnailImage(ImageLayer.getInstance().getThumbNailImageUrl(videoItem, ImageType.LDS.name()));
                                }


                                enveuVideoItemBeans.add(enveuVideoItemBean);
                            }
                        }
                    }
                }

                try {
                    if (enveuVideoItemBeans != null && enveuVideoItemBeans.size() > 0) {
                        Collections.sort(enveuVideoItemBeans, new Comparator<EnveuVideoItemBean>() {
                            public int compare(EnveuVideoItemBean o1, EnveuVideoItemBean o2) {
                                if (o1.getEpisodeNo() != null && o1.getEpisodeNo() instanceof Double) {
                                    return Double.compare((Double) o1.getEpisodeNo(), (Double) o2.getEpisodeNo());
                                } else {
                                    return 0;
                                }
                            }
                        });
                    }
                } catch (Exception ignored) {

                }

            }
        }catch (Exception ignored){

        }

    }

    public RailCommonData(BaseCategory screenWidget) {
        this.screenWidget = screenWidget;
        setRailType(screenWidget.getLayout(), screenWidget.getContentImageType());
    }


    public RailCommonData() {

    }

    public void getHeroRailCommonData(BaseCategory screenWidget, CommonApiCallBack commonApiCallBack) {
        EnveuVideoItemBean enveuVideoItemBean = new EnveuVideoItemBean();
        this.screenWidget = screenWidget;
        setRailType(screenWidget.getLayout(), screenWidget.getContentImageType());
        if (screenWidget.getImageSource() != null && screenWidget.getImageSource().equalsIgnoreCase(ImageSource.MNL.name())) {
            setManualTypeHero(enveuVideoItemBean, commonApiCallBack);
        } else {
            setAssetTypeHero(enveuVideoItemBean, commonApiCallBack);
        }
    }

    private void setManualTypeHero(EnveuVideoItemBean enveuVideoItemBean, CommonApiCallBack commonApiCallBack) {
        enveuVideoItemBean.setPosterURL(screenWidget.getImageURL());
        enveuVideoItemBean.setThumbnailImage(screenWidget.getImageURL());
        setRailType(screenWidget.getLayout(), screenWidget.getContentImageType());
        if (screenWidget.getLandingPageType().equals(LandingPageType.PDF.name()) || screenWidget.getLandingPageType().equals(LandingPageType.PLT.name()) || screenWidget.getLandingPageType().equals(LandingPageType.HTM.name())) {
            enveuVideoItemBeans.add(enveuVideoItemBean);
            commonApiCallBack.onSuccess(this);
        } else {
            VideoDetailLayer.getInstance().getAssetTypeHero(screenWidget.getLandingPageAssetId(), new CommonApiCallBack() {
                @Override
                public void onSuccess(Object item) {
                    if (item instanceof EnveuVideoDetails) {
                        EnveuVideoDetails enveuVideoDetails = (EnveuVideoDetails) item;
                        AppCommonMethod.createManualHeroItem(enveuVideoItemBean, enveuVideoDetails);
                    }
                    enveuVideoItemBeans.add(enveuVideoItemBean);
                    commonApiCallBack.onSuccess(RailCommonData.this);
                }

                @Override
                public void onFailure(Throwable throwable) {
                    commonApiCallBack.onFailure(new Throwable("HERO ASSET NOT FOUND"));
                }

                @Override
                public void onFinish() {

                }


            });
        }


    }

    private void setAssetTypeHero(EnveuVideoItemBean enveuVideoItemBean, CommonApiCallBack commonApiCallBack) {
        final RailCommonData railCommonData = this;
        screenWidget.setLandingPageAssetId(screenWidget.getManualImageAssetId());
        VideoDetailLayer.getInstance().getAssetTypeHero(screenWidget.getManualImageAssetId(), new CommonApiCallBack() {
            @Override
            public void onSuccess(Object item) {
                if (item instanceof EnveuVideoDetails) {
                    EnveuVideoDetails enveuVideoDetails = (EnveuVideoDetails) item;
                    AppCommonMethod.createAssetHeroItem(enveuVideoItemBean, enveuVideoDetails, screenWidget);
                    enveuVideoItemBeans.add(enveuVideoItemBean);
                    railCommonData.setEnveuVideoItemBeans(enveuVideoItemBeans);
                    commonApiCallBack.onSuccess(railCommonData);
                }
            }

            @Override
            public void onFailure(Throwable throwable) {
                commonApiCallBack.onFailure(new Throwable("HERO ASSET NOT FOUND"));

            }

            @Override
            public void onFinish() {

            }
        });
    }

    private void setBrighcoveVideos(List<ItemsItem> videos, String imageType) {
        if (videos != null && videos.size() > 0) {
            final RailCommonData railCommonData = this;
            for (int i = 0; i < videos.size(); i++) {
                VideosItem videoItem = videos.get(i).getContent();
                Gson gson = new Gson();
                EnveuVideoItemBean enveuVideoItemBean = new EnveuVideoItemBean(videoItem, videos.get(i).getContentOrder(), imageType);
                if (enveuVideoItemBean.getId() == 128982) {
                    Logger.e("EXTERNAL_ASSET", new Gson().toJson(videos.get(0)));
                }
                enveuVideoItemBean.setImages(videoItem.getImages());
                if (videoItem != null) {
                    if (videoItem.getSeasonNumber() != null && !videoItem.getSeasonNumber().equalsIgnoreCase("")) {
                        int seasonNumber = Integer.parseInt(videoItem.getSeasonNumber());
                        railCommonData.setSeasonNumber(seasonNumber);
                    }
                    enveuVideoItemBean.setImages(videoItem.getImages());
                    if (screenWidget != null && screenWidget.getWidgetImageType() != null && screenWidget.getWidgetImageType().equalsIgnoreCase(WidgetImageType.THUMBNAIL.toString())) {
                        String imageUrl = ImageLayer.getInstance().getThumbNailImageUrl(videoItem, screenWidget.getWidgetImageType());
                        enveuVideoItemBean.setImages(videoItem.getImages());
                        enveuVideoItemBean.setPosterURL(imageUrl);
                        enveuVideoItemBean.setThumbnailImage(ImageLayer.getInstance().getPosterImageUrl(videoItem, screenWidget.getWidgetImageType()));
                    } else {
                        String imageUrl = ImageLayer.getInstance().getPosterImageUrl(videoItem, imageType);
                        enveuVideoItemBean.setImages(videoItem.getImages());
                        enveuVideoItemBean.setPosterURL(imageUrl);
                        enveuVideoItemBean.setThumbnailImage(ImageLayer.getInstance().getThumbNailImageUrl(videoItem, imageType));
                    }
                    enveuVideoItemBeans.add(enveuVideoItemBean);
                }

            }

            if (enveuVideoItemBeans != null && enveuVideoItemBeans.size() > 0) {
                Collections.sort(enveuVideoItemBeans, new Comparator<EnveuVideoItemBean>() {
                    public int compare(EnveuVideoItemBean o1, EnveuVideoItemBean o2) {
                        return Integer.compare(o1.getContentOrder(), o2.getContentOrder());
                    }
                });
            }
        }
    }

    private void setEpisodesList(List<ItemsItem> videos, String imageType) {
        if (videos != null && videos.size() > 0) {
            final RailCommonData railCommonData = this;
            for (int i = 0; i < videos.size(); i++) {
                VideosItem videoItem = videos.get(i).getContent();
                Gson gson = new Gson();
                String tmp = gson.toJson(videoItem);
                EnveuVideoItemBean enveuVideoItemBean = new EnveuVideoItemBean(videoItem, videos.get(i).getContentOrder(), imageType);
                if (videoItem != null) {
                    if (videoItem.getSeasonNumber() != null && !videoItem.getSeasonNumber().equalsIgnoreCase("")) {
                        int seasonNumber = Integer.parseInt(videoItem.getSeasonNumber());
                        railCommonData.setSeasonNumber(seasonNumber);
                    }
                    enveuVideoItemBean.setVodCount(videos.size());
                    if (screenWidget != null && screenWidget.getWidgetImageType() != null && screenWidget.getWidgetImageType().equalsIgnoreCase(WidgetImageType.THUMBNAIL.toString())) {
                        Logger.e("Screen WidgetType ", screenWidget.getWidgetImageType());
                        String imageUrl = ImageLayer.getInstance().getThumbNailImageUrl(videoItem, screenWidget.getWidgetImageType());
                        enveuVideoItemBean.setImages(videoItem.getImages());
                        enveuVideoItemBean.setPosterURL(imageUrl);
                        enveuVideoItemBean.setThumbnailImage(ImageLayer.getInstance().getPosterImageUrl(videoItem, screenWidget.getWidgetImageType()));

                    } else {
                        String imageUrl = ImageLayer.getInstance().getPosterImageUrl(videoItem, ImageType.LDS.name());
                        enveuVideoItemBean.setImages(videoItem.getImages());
                        enveuVideoItemBean.setPosterURL(imageUrl);
                        enveuVideoItemBean.setThumbnailImage(ImageLayer.getInstance().getThumbNailImageUrl(videoItem, ImageType.LDS.name()));
                    }


                    enveuVideoItemBeans.add(enveuVideoItemBean);
                }

            }

            try {
                if (enveuVideoItemBeans != null && enveuVideoItemBeans.size() > 0) {
                    Collections.sort(enveuVideoItemBeans, new Comparator<EnveuVideoItemBean>() {
                        public int compare(EnveuVideoItemBean o1, EnveuVideoItemBean o2) {
                            if (o1.getEpisodeNo() != null && o1.getEpisodeNo() instanceof Double) {
                                return Double.compare((Double) o1.getEpisodeNo(), (Double) o2.getEpisodeNo());
                            } else {
                                return 0;
                            }
                        }
                    });
                }
            } catch (Exception ignored) {

            }

        }
    }


    public void setBrightCoveSeries(List<SeriesItem> seriesItems, String name) {
        for (SeriesItem seriesItem :
                seriesItems) {
            Gson gson = new Gson();
            String tmp = gson.toJson(seriesItem);
            EnveuVideoItemBean enveuVideoItemBean = gson.fromJson(tmp, EnveuVideoItemBean.class);

            if (screenWidget != null && screenWidget.getWidgetImageType() != null && screenWidget.getWidgetImageType().equalsIgnoreCase(WidgetImageType.THUMBNAIL.toString())) {
                enveuVideoItemBean.setPosterURL(seriesItem.getThumbnailImage());
                enveuVideoItemBean.setThumbnailImage(seriesItem.getPosterImage());
            } else {
                enveuVideoItemBean.setPosterURL(seriesItem.getPosterImage());
                enveuVideoItemBean.setThumbnailImage(seriesItem.getPosterImage());
            }
            enveuVideoItemBean.setTitle(seriesItem.getName());
            Log.d("testseries", seriesItem.getName());
            enveuVideoItemBean.setBrightcoveVideoId(seriesItem.getBrightcoveSeriesId());
            enveuVideoItemBeans.add(enveuVideoItemBean);
        }
    }


    private void setRailType(String layoutType, String layoutImageType) {
        if (layoutType.equalsIgnoreCase(Layouts.CAR.name())) {
            if (layoutImageType.equalsIgnoreCase(ImageType.LDS.name())) {
                railType = AppConstants.CAROUSEL_LDS_LANDSCAPE;
            } else if (layoutImageType.equalsIgnoreCase(ImageType.PR1.name())) {
                railType = AppConstants.CAROUSEL_PR_POTRAIT;
            } else if (layoutImageType.equalsIgnoreCase(ImageType.SQR.name())) {
                railType = AppConstants.CAROUSEL_SQR_SQUARE;
            } else if (layoutImageType.equalsIgnoreCase(ImageType.CIR.name())) {
                railType = AppConstants.CAROUSEL_CIR_CIRCLE;
            }
        } else if (layoutType.equalsIgnoreCase(Layouts.HOR.name())) {
            if (layoutImageType.equalsIgnoreCase(ImageType.LDS.name())) {
                railType = AppConstants.HORIZONTAL_LDS_LANDSCAPE;
            } else if (layoutImageType.equalsIgnoreCase(ImageType.PR1.name())) {
                railType = AppConstants.HORIZONTAL_PR_POTRAIT;
            } else if (layoutImageType.equalsIgnoreCase(ImageType.PR2.name())) {
                railType = AppConstants.HORIZONTAL_PR_POSTER;
            } else if (layoutImageType.equalsIgnoreCase(ImageType.SQR.name())) {
                railType = AppConstants.HORIZONTAL_SQR_SQUARE;
            } else if (layoutImageType.equalsIgnoreCase(ImageType.CIR.name())) {
                railType = AppConstants.HORIZONTAL_CIR_CIRCLE;
            }
        } else if (layoutType.equalsIgnoreCase(Layouts.HRO.name())) {
            if (layoutImageType.equalsIgnoreCase(ImageType.LDS.name())) {
                railType = AppConstants.HERO_LDS_LANDSCAPE;
            } else if (layoutImageType.equalsIgnoreCase(ImageType.PR1.name())) {
                railType = AppConstants.HERO_PR_POTRAIT;
            } else if (layoutImageType.equalsIgnoreCase(ImageType.PR2.name())) {
                railType = AppConstants.HERO_PR_POSTER;
            } else if (layoutImageType.equalsIgnoreCase(ImageType.SQR.name())) {
                railType = AppConstants.HERO_SQR_SQUARE;
            } else if (layoutImageType.equalsIgnoreCase(ImageType.CIR.name())) {
                railType = AppConstants.HERO_CIR_CIRCLE;
            }
        } else if (layoutType.equalsIgnoreCase(Layouts.BAN.name())) {
            railType = AppConstants.ADS_BANNER;

        } else if (layoutType.equalsIgnoreCase(Layouts.MRC.name())) {
            railType = AppConstants.ADS_MREC;
        } else if (layoutType.equalsIgnoreCase(Layouts.CUS.name())) {
            railType = AppConstants.ADS_CUS;
        }
    }

    public void setContinueWatchingData(BaseCategory screenWidget, ArrayList<DataItem> enveuVideoDetails, CommonApiCallBack commonApiCallBack) {
        // this.identifier = screenWidget.getReferenceName();
        this.displayName = (String) screenWidget.getName();
        this.screenWidget = screenWidget;
        final RailCommonData railCommonData = this;
        if (enveuVideoDetails != null && enveuVideoDetails.size() > 0) {
            for (DataItem enveuVideoDetails1 : enveuVideoDetails) {
                Gson gson = new Gson();
                String tmp = gson.toJson(enveuVideoDetails1);
                EnveuVideoItemBean enveuVideoItemBean = new EnveuVideoItemBean(enveuVideoDetails1, screenWidget.getWidgetImageType());
                enveuVideoItemBean.setImages(enveuVideoDetails1.getImages());

                if (this.screenWidget != null && this.screenWidget.getWidgetImageType() != null && this.screenWidget.getWidgetImageType().equalsIgnoreCase(WidgetImageType.THUMBNAIL.toString())) {
                    String imageUrl = ImageLayer.getInstance().getThumbNailImageUrl(enveuVideoDetails1, screenWidget.getWidgetImageType());
                    enveuVideoItemBean.setPosterURL(imageUrl);
                    enveuVideoItemBean.setThumbnailImage(ImageLayer.getInstance().getPosterImageUrl(enveuVideoDetails1, screenWidget.getWidgetImageType()));

                } else {
                    String imageUrl = ImageLayer.getInstance().getPosterImageUrl(enveuVideoDetails1, screenWidget.getWidgetImageType());
                    enveuVideoItemBean.setPosterURL(imageUrl);
                    //enveuVideoItemBean.setPosterURL(screenWidget.getImageURL());
                    enveuVideoItemBean.setThumbnailImage(ImageLayer.getInstance().getThumbNailImageUrl(enveuVideoDetails1, screenWidget.getWidgetImageType()));

                }

                enveuVideoItemBeans.add(enveuVideoItemBean);
            }
            setRailType(Layouts.HOR.name(), ImageType.LDS.name());
            railCommonData.setIsContinueWatching(true);
            commonApiCallBack.onSuccess(RailCommonData.this);
        } else {
            commonApiCallBack.onFailure(new Throwable("No Data Found"));
        }

    }

    public void setWatchHistoryData(ArrayList<DataItem> enveuVideoDetails, CommonApiCallBack commonApiCallBack) {

        if (enveuVideoDetails != null && enveuVideoDetails.size() > 0) {
            for (DataItem enveuVideoDetails1 : enveuVideoDetails) {
                Gson gson = new Gson();
                String tmp = gson.toJson(enveuVideoDetails1);
                EnveuVideoItemBean enveuVideoItemBean = null;
                if (screenWidget != null && screenWidget.getWidgetImageType() != null) {
                    enveuVideoItemBean = new EnveuVideoItemBean(enveuVideoDetails1, screenWidget.getWidgetImageType());
                } else {
                    enveuVideoItemBean = new EnveuVideoItemBean(enveuVideoDetails1, null);
                }
                enveuVideoItemBean.setImages(enveuVideoDetails1.getImages());

                if (this.screenWidget != null && this.screenWidget.getWidgetImageType() != null && this.screenWidget.getWidgetImageType().equalsIgnoreCase(WidgetImageType.THUMBNAIL.toString())) {
                    Logger.e("Screen WidgetType ", screenWidget.getWidgetImageType());
                    String imageUrl = ImageLayer.getInstance().getThumbNailImageUrl(enveuVideoDetails1, screenWidget.getWidgetImageType());
                    enveuVideoItemBean.setPosterURL(imageUrl);
                    enveuVideoItemBean.setThumbnailImage(ImageLayer.getInstance().getPosterImageUrl(enveuVideoDetails1, screenWidget.getWidgetImageType()));

                } else {
                    String imageUrl = ImageLayer.getInstance().getPosterImageUrl(enveuVideoDetails1, null);
                    enveuVideoItemBean.setPosterURL(imageUrl);
                    enveuVideoItemBean.setThumbnailImage(ImageLayer.getInstance().getThumbNailImageUrl(enveuVideoDetails1, null));
                }
                enveuVideoItemBeans.add(enveuVideoItemBean);
            }
            setRailType(Layouts.HOR.name(), ImageType.LDS.name());
            commonApiCallBack.onSuccess(RailCommonData.this);
        } else {
            commonApiCallBack.onFailure(new Throwable("No Data Found"));
        }

        /* if (enveuVideoDetails != null && enveuVideoDetails.size() > 0) {
            for (EnveuVideoDetails enveuVideoDetails1 :
                    enveuVideoDetails) {
                Gson gson = new Gson();
                String tmp = gson.toJson(enveuVideoDetails1);
                EnveuVideoItemBean enveuVideoItemBean = gson.fromJson(tmp, EnveuVideoItemBean.class);

                if (this.screenWidget != null && this.screenWidget.getWidgetImageType() != null && this.screenWidget.getWidgetImageType().equalsIgnoreCase(WidgetImageType.THUMBNAIL.toString()))
                    enveuVideoItemBean.setPosterURL(enveuVideoDetails1.getThumbnailImage());
                else
                    enveuVideoItemBean.setPosterURL(enveuVideoDetails1.getPosterImage());

                enveuVideoItemBean.setVideoPosition(0);
                enveuVideoItemBeans.add(enveuVideoItemBean);
            }
            setRailType(Layouts.HOR.name(), ImageType.LDS.name());
            commonApiCallBack.onSuccess(RailCommonData.this);
        } else {
            commonApiCallBack.onFailure(new Throwable("No Data Found"));
        }*/

    }
   /* public String getIdentifier() {
        return identifier;
    }*/

   /* public String getBrightcovePlaylistId() {
        return brightcovePlaylistId;
    }*/

    public void setIsContinueWatching(boolean continueWatching) {
        isContinueWatching = continueWatching;
    }

    public boolean isContinueWatching() {
        return isContinueWatching;
    }

    public void setIsAd(boolean ad) {
        isAd = ad;
    }

    public boolean isAd() {
        return isAd;
    }

    public int getMaxContent() {
        return maxContent;
    }

    public String getDisplayName() {
        return displayName;
    }

    public String getPlaylistType() {
        return playlistType;
    }

    public List<EnveuVideoItemBean> getEnveuVideoItemBeans() {
        return enveuVideoItemBeans;
    }

    public void setEnveuVideoItemBeans(List<EnveuVideoItemBean> enveuVideoItemBeans) {
        this.enveuVideoItemBeans = enveuVideoItemBeans;
    }

   /* public long getId() {
        return id;
    }*/

    public int getRailType() {
        return railType;
    }

    public BaseCategory getScreenWidget() {
        return screenWidget;
    }

    public boolean getStatus() {
        return status;
    }

    public void setStatus(boolean status) {
        this.status = status;
    }

    public int getPageTotal() {
        return pageTotal;
    }

    public void setPageTotal(int page) {
        this.pageTotal = page;
    }

    public int getLayoutType() {
        return layoutType;
    }

    public void setLayoutType(int layoutType) {
        this.layoutType = layoutType;
    }

    public String getSearchKey() {
        return searchKey;
    }

    public void setSearchKey(String searchKey) {
        this.searchKey = searchKey;
    }

    public int getTotalCount() {
        return totalCount;
    }

    public void setTotalCount(int totalCount) {
        this.totalCount = totalCount;
    }

    public String getAssetType() {
        return assetType;
    }

    public void setAssetType(String assetType) {
        this.assetType = assetType;
    }

    public boolean isSeries() {
        return isSeries;
    }

    public void setSeries(boolean series) {
        isSeries = series;
    }

    public int getSeasonNumber() {
        return seasonNumber;
    }

    public void setSeasonNumber(int seasonNumber) {
        this.seasonNumber = seasonNumber;
    }

    public String getSeasonName() {
        return seasonName;
    }

    public void setSeasonName(String seasonName) {
        this.seasonName = seasonName;
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(maxContent);
        dest.writeString(displayName);
        dest.writeString(playlistType);
        dest.writeInt(seasonNumber);
        dest.writeString(seasonName);
        dest.writeInt(railType);
        dest.writeParcelable(screenWidget, flags);
        dest.writeByte((byte) (status ? 1 : 0));
        dest.writeInt(pageTotal);
        dest.writeInt(layoutType);
        dest.writeString(searchKey);
        dest.writeInt(totalCount);
        dest.writeString(assetType);
        dest.writeByte((byte) (isSeries ? 1 : 0));
        dest.writeByte((byte) (isContinueWatching ? 1 : 0));
        dest.writeByte((byte) (isAd ? 1 : 0));
        dest.writeInt(pageNumber);
        dest.writeInt(pageSize);
    }

    public String setCustomeInternalPageId(BaseCategory screenWidget) {
        customeInternalPageId = "";
        if (screenWidget!=null && screenWidget.getLandingPageAssetId()!=null && !screenWidget.getLandingPageAssetId().equalsIgnoreCase("")){
            customeInternalPageId=screenWidget.getLandingPageAssetId();
        }
        return customeInternalPageId;
    }
}
