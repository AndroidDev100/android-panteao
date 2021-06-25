package panteao.make.ready.beanModelV3.uiConnectorModelV2;

import android.graphics.drawable.Drawable;

import androidx.core.content.ContextCompat;

import com.google.gson.internal.LinkedTreeMap;

import panteao.make.ready.PanteaoApplication;
import panteao.make.ready.beanModelV3.continueWatching.DataItem;
import panteao.make.ready.beanModelV3.playListModelV2.VideosItem;
import panteao.make.ready.beanModelV3.searchV2.ItemsItem;
import panteao.make.ready.beanModelV3.videoDetailsV2.EnveuVideoDetailsBean;
import panteao.make.ready.utils.config.ImageLayer;
import panteao.make.ready.utils.cropImage.helpers.Logger;
import panteao.make.ready.utils.helpers.ksPreferenceKeys.KsPreferenceKeys;
import panteao.make.ready.R;
import panteao.make.ready.utils.CustomeFields;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class EnveuVideoItemBean implements Serializable {
    private ArrayList seasons;
    private String description;
    private String longDescription;
    private List<String> assetKeywords;
    private int likeCount;
    private String title;
    private Object svod;
    private String contentProvider;
    private List<String> assetCast;
    private boolean premium;
    private String posterURL;
    private Object price;
    private List<String> assetGenres;
    private String season;
    private int id;
    private String sku;
    private boolean isNew;
    private Object tvod;
    private Object episodeNo;
    private String assetType;
    private int commentCount;
    private String uploadedAssetKey;
    private String brightcoveVideoId;
    private String series;
    private String seriesId;
    private Object plans;
    private long publishedDate;
    private String status;
    private int responseCode;
    private long duration;
    private String name;
    private int vodCount;
    private int seasonCount;
    private String thumbnailImage;

    private long videoPosition;
    private int contentOrder;
    private String seasonNumber;
    private String imageType;

    private String parentalRating;
    private String widevineLicence;
    private String getWidevineURL;
    private String country;
    private String company;
    private String year;
    private String isNewS;
    private String isVIP;
    private String VastTag;
    private String islivedrm = "false";
    private String kEntryId = "";
    public boolean isContinueWatching = false;

    public String getIslivedrm() {
        return islivedrm;
    }

    public void setIslivedrm(String islivedrm) {
        this.islivedrm = islivedrm;
    }

    public void setSeasons(ArrayList seasons) {
        this.seasons = seasons;
    }

    public void setDuration(long duration) {
        this.duration = duration;
    }

    public boolean isContinueWatching() {
        return isContinueWatching;
    }

    public void setContinueWatching(boolean continueWatching) {
        isContinueWatching = continueWatching;
    }

    public EnveuVideoItemBean() {
    }

    //description page - single asset parsing
    public EnveuVideoItemBean(EnveuVideoDetailsBean details,String imageType) {
        try {
            this.title = details.getData().getTitle() == null ? "" : details.getData().getTitle();
            this.kEntryId = details.getData().getkEntryId() == null ? "" : details.getData().getkEntryId();
            this.description = details.getData().getDescription() == null ? "" : details.getData().getDescription().trim();
            this.assetGenres = details.getData().getGenres() == null ? new ArrayList<>() : details.getData().getGenres();
            this.assetCast = details.getData().getCast() == null ? new ArrayList<>() : details.getData().getCast();

            this.contentProvider = details.getData().getContentProvider() == null ? "" : details.getData().getContentProvider();
            this.premium = details.getData().getPremium();
            this.posterURL = ImageLayer.getInstance().getPosterImageUrl(details.getData(),imageType);

            this.season = "";
            if (details.getData().getLinkedContent() != null) {
                this.seriesId = String.valueOf(details.getData().getLinkedContent().getId());
            }
            this.sku = details.getData().getSku() == null ? "" : details.getData().getSku();
            this.id = details.getData().getId();
            this.isNew = false;
            this.episodeNo = details.getData().getEpisodeNumber() == null ? "" : details.getData().getEpisodeNumber();
            this.assetType = details.getData().getContentType() == null ? "" : details.getData().getContentType();
            this.brightcoveVideoId = details.getData().getBrightcoveContentId() == null ? "" : details.getData().getBrightcoveContentId();
            this.series = String.valueOf(details.getData().getId());
            this.status = details.getData().getStatus() == null ? "" : details.getData().getStatus();

            Object customeFiled = details.getData().getCustomFields();
            LinkedTreeMap<Object, Object> t = (LinkedTreeMap) customeFiled;

            if (t.containsKey(CustomeFields.WIDEVINE_LICENCE)) {
                String widevineLicence = t.get((CustomeFields.WIDEVINE_LICENCE)).toString();
                this.widevineLicence = widevineLicence;
            }
            if (t.containsKey(CustomeFields.ISLIVEDRM)) {
                String isLiveDrm = t.get((CustomeFields.ISLIVEDRM)).toString();
                this.islivedrm = isLiveDrm;
            }

            if (t.containsKey(CustomeFields.WIDEVINE_URL)) {
                String widevineURL = t.get((CustomeFields.WIDEVINE_URL)).toString();
                this.getWidevineURL = widevineURL;
            }

            if (t.containsKey(CustomeFields.parentalRating)) {
                String parentalRating = t.get((CustomeFields.parentalRating)).toString();
                this.parentalRating = parentalRating;
            }

            if (t.containsKey(CustomeFields.Country)) {
                String country = t.get((CustomeFields.Country)).toString();
                this.country = country;
            }

            if (t.containsKey(CustomeFields.company)) {
                String company = t.get((CustomeFields.company)).toString();
                this.company = company;
            }

            if (t.containsKey(CustomeFields.year)) {
                String year = t.get((CustomeFields.year)).toString();
                this.year = year;
            }

            if (t.containsKey(CustomeFields.VastTag)) {
                String year = t.get((CustomeFields.VastTag)).toString();
                this.VastTag = year;
            }


            if (t.containsKey(CustomeFields.IsVip)) {
                String vip = t.get((CustomeFields.IsVip)).toString();
                this.isVIP = vip;
            }

            if (t.containsKey(CustomeFields.IsNew)) {
                String isNew = t.get((CustomeFields.IsNew)).toString();
                this.isNewS = isNew;
            }

            this.longDescription = details.getData().getLongDescription() == null ? "" : details.getData().getLongDescription().toString().trim();


            //series realated data
            this.vodCount = 0;
            this.seasonNumber = details.getData().getSeasonNumber() == null ? "" : details.getData().getSeasonNumber().toString().replaceAll("\\.0*$", "");
            if (details.getData().getSeasons() != null) {
                ArrayList arrayList = (ArrayList) details.getData().getSeasons();
                this.seasons = arrayList;
                this.seasonCount = arrayList.size();
            }

            this.duration = details.getData().getDuration();

        } catch (Exception e) {
            Logger.e("parsing error", e.getMessage());
        }

    }

    //for asset details.......
    public EnveuVideoItemBean(VideosItem details, int contentOrder, String imageType) {

        try {

            this.title = details.getTitle() == null ? "" : details.getTitle();
            this.kEntryId = details.getkEntryId() == null ? "" : details.getkEntryId();
            this.description = details.getDescription() == null ? "" : details.getDescription().trim();
            this.assetGenres = details.getGenres() == null ? new ArrayList<>() : details.getGenres();
            this.assetCast = details.getCast() == null ? new ArrayList<>() : details.getCast();
            this.contentProvider = details.getContentProvider() == null ? "" : details.getContentProvider();
            this.assetCast = details.getCast() == null ? new ArrayList<>() : details.getCast();
            this.premium = details.getPremium();

            this.posterURL = ImageLayer.getInstance().getPosterImageUrl(details,imageType);
            this.assetGenres = details.getGenres() == null ? new ArrayList<>() : details.getGenres();
            this.season = "";
            this.id = details.getId();
            this.sku = details.getSku() == null ? "" : details.getSku();
            this.isNew = false;
            this.episodeNo = details.getEpisodeNumber() == null ? "" : details.getEpisodeNumber();
            this.assetType = details.getContentType() == null ? "" : details.getContentType();
            this.brightcoveVideoId = details.getBrightcoveContentId() == null ? "" : details.getBrightcoveContentId();

            this.series = "";
            this.status = details.getStatus() == null ? "" : details.getStatus();
            this.contentOrder = contentOrder;
            if (imageType != null) {
                this.imageType = imageType;
            } else {
                this.imageType = "";
            }

            Object customeFiled = details.getCustomFields();
            LinkedTreeMap<Object, Object> t = (LinkedTreeMap) customeFiled;

            if (KsPreferenceKeys.getInstance().getAppLanguage().equalsIgnoreCase("English")) {

                if (t.containsKey(CustomeFields.parentalRating)) {
                    String parentalRating = t.get((CustomeFields.parentalRating)).toString();
                    this.parentalRating = parentalRating;
                } else {
                    if (t.containsKey(CustomeFields.rating)) {
                        String parentalRating = t.get((CustomeFields.rating)).toString();
                        this.parentalRating = parentalRating;
                    }
                }

                if (t.containsKey(CustomeFields.Country)) {
                    String country = t.get((CustomeFields.Country)).toString();
                    this.country = country;
                }

                if (t.containsKey(CustomeFields.company)) {
                    String company = t.get((CustomeFields.company)).toString();
                    this.company = company;
                }

                if (t.containsKey(CustomeFields.year)) {
                    String year = t.get((CustomeFields.year)).toString();
                    this.year = year;
                }

                if (t.containsKey(CustomeFields.VastTag)) {
                    String year = t.get((CustomeFields.VastTag)).toString();
                    this.VastTag = year;
                }

            } else {

                if (t.containsKey(CustomeFields.rating)) {
                    String parentalRating = t.get((CustomeFields.rating)).toString();
                    this.parentalRating = parentalRating;
                }

                if (t.containsKey(CustomeFields.Country)) {
                    String country = t.get((CustomeFields.Country)).toString();
                    this.country = country;
                }

                if (t.containsKey(CustomeFields.company)) {
                    String company = t.get((CustomeFields.company)).toString();
                    this.company = company;
                }

                if (t.containsKey(CustomeFields.year)) {
                    String year = t.get((CustomeFields.year)).toString();
                    this.year = year;
                }

                if (t.containsKey(CustomeFields.VastTag)) {
                    String year = t.get((CustomeFields.VastTag)).toString();
                    this.VastTag = year;
                }
            }

            if (t.containsKey(CustomeFields.IsVip)) {
                String vip = t.get((CustomeFields.IsVip)).toString();
                this.isVIP = vip;
            }

            if (t.containsKey(CustomeFields.IsNew)) {
                String isNew = t.get((CustomeFields.IsNew)).toString();
                this.isNewS = isNew;
            }

            this.longDescription = details.getLongDescription() == null ? "" : details.getLongDescription().toString().trim();


            //series realated data
            if (details.getSeasons() != null) {
                ArrayList arrayList = (ArrayList) details.getSeasons();
                this.seasonCount = arrayList.size();
            }


            this.duration = (long) details.getDuration();

        } catch (Exception e) {

        }
    }

    //for continue watching.......
    public EnveuVideoItemBean(DataItem details,String imageType) {

        try {
            this.title = details.getTitle() == null ? "" : details.getTitle();
            this.description = details.getDescription() == null ? "" : details.getDescription().trim();
            this.assetGenres = details.getGenres() == null ? new ArrayList<>() : details.getGenres();
            this.assetCast = details.getCast() == null ? new ArrayList<>() : details.getCast();
            this.contentProvider = details.getContentProvider() == null ? "" : details.getContentProvider();
            this.assetCast = details.getCast() == null ? new ArrayList<>() : details.getCast();
            this.posterURL = ImageLayer.getInstance().getPosterImageUrl(details,imageType);
            this.assetGenres = details.getGenres() == null ? new ArrayList<>() : details.getGenres();
            this.season = "";
            this.id = details.getId();
            this.sku = details.getSku() == null ? "" : details.getSku();
            this.isNew = false;
            this.episodeNo = details.getEpisodeNumber() == null ? "" : details.getEpisodeNumber();
            this.assetType = details.getContentType() == null ? "" : details.getContentType();
            this.brightcoveVideoId = details.getBrightcoveContentId() == null ? "" : details.getBrightcoveContentId();
            this.series = "";
            this.status = details.getStatus() == null ? "" : details.getStatus();
            if (details.getPosition() != null) {
                this.videoPosition = details.getPosition();
            }

            this.contentOrder = contentOrder;
            if (imageType != null) {
                this.imageType = imageType;
            } else {
                this.imageType = "";
            }

            Object customeFiled = details.getCustomFields();
            LinkedTreeMap<Object, Object> t = (LinkedTreeMap) customeFiled;


            if (KsPreferenceKeys.getInstance().getAppLanguage().equalsIgnoreCase("English")) {


                if (t.containsKey(CustomeFields.parentalRating)) {
                    String parentalRating = t.get((CustomeFields.parentalRating)).toString();
                    this.parentalRating = parentalRating;
                } else {
                    if (t.containsKey(CustomeFields.rating)) {
                        String parentalRating = t.get((CustomeFields.rating)).toString();
                        this.parentalRating = parentalRating;
                    }
                }

                if (t.containsKey(CustomeFields.Country)) {
                    String country = t.get((CustomeFields.Country)).toString();
                    this.country = country;
                }

                if (t.containsKey(CustomeFields.company)) {
                    String company = t.get((CustomeFields.company)).toString();
                    this.company = company;
                }

                if (t.containsKey(CustomeFields.year)) {
                    String year = t.get((CustomeFields.year)).toString();
                    this.year = year;
                }

                if (t.containsKey(CustomeFields.VastTag)) {
                    String year = t.get((CustomeFields.VastTag)).toString();
                    this.VastTag = year;
                }

            } else {

                if (t.containsKey(CustomeFields.rating)) {
                    String parentalRating = t.get((CustomeFields.rating)).toString();
                    this.parentalRating = parentalRating;
                }

                if (t.containsKey(CustomeFields.Country)) {
                    String country = t.get((CustomeFields.Country)).toString();
                    this.country = country;
                }

                if (t.containsKey(CustomeFields.company)) {
                    String company = t.get((CustomeFields.company)).toString();
                    this.company = company;
                }

                if (t.containsKey(CustomeFields.year)) {
                    String year = t.get((CustomeFields.year)).toString();
                    this.year = year;
                }

                if (t.containsKey(CustomeFields.VastTag)) {
                    String year = t.get((CustomeFields.VastTag)).toString();
                    this.VastTag = year;
                }

            }

            if (t.containsKey(CustomeFields.IsVip)) {
                String vip = t.get((CustomeFields.IsVip)).toString();
                this.isVIP = vip;
            }

            if (t.containsKey(CustomeFields.IsNew)) {
                String isNew = t.get((CustomeFields.IsNew)).toString();
                this.isNewS = isNew;
            }
            this.longDescription = details.getLongDescription() == null ? "" : details.getLongDescription().toString().trim();

            if (details.getSeasons() != null) {
                ArrayList arrayList = (ArrayList) details.getSeasons();
                this.seasonCount = arrayList.size();
            }
            this.duration = (long) details.getDuration();
        } catch (Exception ignored) {
            Logger.e("ContinueWatching", ignored.getMessage());
        }

    }

    //for search data.......
    public EnveuVideoItemBean(ItemsItem details) {

        try {

            //  this.svod = details.getSvod() == null ? "" : details.getSvod();
            this.title = details.getTitle() == null ? "" : details.getTitle();
            this.description = details.getDescription() == null ? "" : details.getDescription().trim();
            this.assetGenres = details.getGenres() == null ? new ArrayList<>() : details.getGenres();
            this.assetCast = details.getCast() == null ? new ArrayList<>() : details.getCast();

            this.contentProvider = details.getContentProvider() == null ? "" : details.getContentProvider();
            this.assetCast = details.getCast() == null ? new ArrayList<>() : details.getCast();
            //  this.premium = details.isPremium();

            this.posterURL = ImageLayer.getInstance().getPosterImageUrl(details,imageType);
               /* if (details.getImages()!=null && details.getImages().getPoster()!=null && details.getImages().getPoster().getSources()!=null
                        && details.getImages().getPoster().getSources().size()>0){
                     details.getImages().getPoster().getSources().get(0).getSrc();
                }*/
            // this.posterURL = details.getImages()== null ? "" : details.getImages().getPoster().getSources().toString();
            //  this.price = details.getPrice() == null ? "" : details.getPrice();
            this.assetGenres = details.getGenres() == null ? new ArrayList<>() : details.getGenres();
            this.season = "";
            this.id = details.getId();
            this.sku = details.getSku() == null ? "" : details.getSku();
            this.isNew = false;
            //  this.tvod = details.getTvod() == null ? "" : details.getTvod();
            this.episodeNo = details.getEpisodeNumber() == null ? "" : details.getEpisodeNumber();
            this.assetType = details.getContentType() == null ? "" : details.getContentType();
            this.brightcoveVideoId = details.getBrightcoveContentId() == null ? "" : details.getBrightcoveContentId();
            this.series = "";
            this.status = details.getStatus() == null ? "" : details.getStatus();
            this.contentOrder = contentOrder;
            if (imageType != null) {
                this.imageType = imageType;
            } else {
                this.imageType = "";
            }

            Object customeFiled = details.getCustomFields();
            LinkedTreeMap<Object, Object> t = (LinkedTreeMap) customeFiled;


            if (KsPreferenceKeys.getInstance().getAppLanguage().equalsIgnoreCase("English")) {


                if (t.containsKey(CustomeFields.parentalRating)) {
                    String parentalRating = t.get((CustomeFields.parentalRating)).toString();
                    this.parentalRating = parentalRating;
                } else {
                    if (t.containsKey(CustomeFields.rating)) {
                        String parentalRating = t.get((CustomeFields.rating)).toString();
                        this.parentalRating = parentalRating;
                    }
                }

                if (t.containsKey(CustomeFields.Country)) {
                    String country = t.get((CustomeFields.Country)).toString();
                    this.country = country;
                }

                if (t.containsKey(CustomeFields.company)) {
                    String company = t.get((CustomeFields.company)).toString();
                    this.company = company;
                }

                if (t.containsKey(CustomeFields.year)) {
                    String year = t.get((CustomeFields.year)).toString();
                    this.year = year;
                }

                if (t.containsKey(CustomeFields.VastTag)) {
                    String year = t.get((CustomeFields.VastTag)).toString();
                    this.VastTag = year;
                }

            } else {

                if (t.containsKey(CustomeFields.rating)) {
                    String parentalRating = t.get((CustomeFields.rating)).toString();
                    this.parentalRating = parentalRating;
                }

                if (t.containsKey(CustomeFields.Country)) {
                    String country = t.get((CustomeFields.Country)).toString();
                    this.country = country;
                }

                if (t.containsKey(CustomeFields.company)) {
                    String company = t.get((CustomeFields.company)).toString();
                    this.company = company;
                }

                if (t.containsKey(CustomeFields.year)) {
                    String year = t.get((CustomeFields.year)).toString();
                    this.year = year;
                }

                if (t.containsKey(CustomeFields.VastTag)) {
                    String year = t.get((CustomeFields.VastTag)).toString();
                    this.VastTag = year;
                }

            }

            if (t.containsKey(CustomeFields.IsVip)) {
                String vip = t.get((CustomeFields.IsVip)).toString();
                this.isVIP = vip;
            }

            if (t.containsKey(CustomeFields.IsNew)) {
                String isNew = t.get((CustomeFields.IsNew)).toString();
                this.isNewS = isNew;
            }
            this.longDescription = details.getLongDescription() == null ? "" : details.getLongDescription().toString().trim();

            //series realated data
            if (details.getSeasons() != null) {
                ArrayList arrayList = (ArrayList) details.getSeasons();
                this.seasonCount = arrayList.size();
            }
            this.duration = (long) details.getDuration();
        } catch (Exception ignored) {

        }
    }


    public String getSeasonNumber() {
        return seasonNumber;
    }

    public void setSeasonNumber(String seasonNumber) {
        this.seasonNumber = seasonNumber;
    }

    public int getContentOrder() {
        return contentOrder;
    }

    public void setContentOrder(int contentOrder) {
        this.contentOrder = contentOrder;
    }

    public int getResponseCode() {
        return responseCode;
    }

    public void setResponseCode(int responseCode) {
        this.responseCode = responseCode;
    }

    public long getDuration() {
        return duration;
    }

    public void setDuration(Long duration) {
        this.duration = duration;
    }

    public String getName() {
        return name;
    }

    public ArrayList getSeasons() {
        return seasons;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getVodCount() {
        return vodCount;
    }

    public void setVodCount(int vodCount) {
        this.vodCount = vodCount;
    }

    public int getSeasonCount() {
        return seasonCount;
    }

    public void setSeasonCount(int seasonCount) {
        this.seasonCount = seasonCount;
    }

    public String getThumbnailImage() {
        return thumbnailImage;
    }

    public void setThumbnailImage(String thumbnailImage) {
        this.thumbnailImage = thumbnailImage;
    }

   /* public Object getVastTag() {
        return vastTag;
    }

    public void setVastTag(Object vastTag) {
        this.vastTag = vastTag;
    }*/

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getLongDescription() {
        return longDescription;
    }

    public void setLongDescription(String longDescription) {
        this.longDescription = longDescription;
    }

    public List<String> getAssetKeywords() {
        return assetKeywords;
    }

    public void setAssetKeywords(List<String> assetKeywords) {
        this.assetKeywords = assetKeywords;
    }

    public int getLikeCount() {
        return likeCount;
    }

    public void setLikeCount(int likeCount) {
        this.likeCount = likeCount;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getkEntryId() {
        return kEntryId;
    }

    public void setkEntryId(String kEntryId) {
        this.kEntryId = kEntryId;
    }

    public Object getSvod() {
        return svod;
    }

    public void setSvod(Object svod) {
        this.svod = svod;
    }

    public String getContentProvider() {
        return contentProvider;
    }

    public void setContentProvider(String contentProvider) {
        this.contentProvider = contentProvider;
    }

    public List<String> getAssetCast() {
        return assetCast;
    }

    public String getAllAssetCast() {
        StringBuilder castString = new StringBuilder();
        if (assetCast != null && assetCast.size() > 0) {
            for (int i = 0; i < assetCast.size(); i++) {
                if (i == 0 || i == assetCast.size() - 1) {
                    castString.append(assetCast.get(i));
                } else {
                    castString.append(assetCast.get(i) + ",");
                }
            }
        }
        return castString.toString();
    }

    public void setAssetCast(List<String> assetCast) {
        this.assetCast = assetCast;
    }

    public boolean isPremium() {
        return premium;
    }

    public void setPremium(boolean premium) {
        this.premium = premium;
    }

    public String getPosterURL() {
        return posterURL;
    }

    public void setPosterURL(String posterURL) {
        this.posterURL = posterURL;
    }

    public Object getPrice() {
        return price;
    }

    public void setPrice(Object price) {
        this.price = price;
    }

    public List<String> getAssetGenres() {
        return assetGenres;
    }

    public void setAssetGenres(List<String> assetGenres) {
        this.assetGenres = assetGenres;
    }

    public String getSeason() {
        return season;
    }

    public void setSeason(String season) {
        this.season = season;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getSku() {
        return sku;
    }

    public void setSku(String sku) {
        this.sku = sku;
    }

    public boolean isNew() {
        return isNew;
    }

    //	public void setThumbnailURL(String thumbnailURL){
//		this.thumbnailURL = thumbnailURL;
//	}
//
//	public String getThumbnailURL(){
//		return thumbnailURL;
//	}
//
    public void setNew(boolean aNew) {
        this.isNew = aNew;
    }

    public Object getTvod() {
        return tvod;
    }

    public void setTvod(Object tvod) {
        this.tvod = tvod;
    }

    public Object getEpisodeNo() {
        return episodeNo;
    }

    public void setEpisodeNo(Object episodeNo) {
        this.episodeNo = episodeNo;
    }

    public String getAssetType() {
        return assetType;
    }

    public void setAssetType(String assetType) {
        this.assetType = assetType;
    }

    public int getCommentCount() {
        return commentCount;
    }

    public void setCommentCount(int commentCount) {
        this.commentCount = commentCount;
    }

    public String getUploadedAssetKey() {
        return uploadedAssetKey;
    }

    public void setUploadedAssetKey(String uploadedAssetKey) {
        this.uploadedAssetKey = uploadedAssetKey;
    }

    public String getBrightcoveVideoId() {
        return brightcoveVideoId;
    }

    public void setBrightcoveVideoId(String brightcoveVideoId) {
        this.brightcoveVideoId = brightcoveVideoId;
    }

    public String getSeries() {
        return series;
    }

    public void setSeries(String series) {
        this.series = series;
    }

    public String getSeriesId() {
        return seriesId;
    }

    public void setSeriesId(String seriesId) {
        this.seriesId = seriesId;
    }

    public Object getPlans() {
        return plans;
    }

    public void setPlans(Object plans) {
        this.plans = plans;
    }

    public long getPublishedDate() {
        return publishedDate;
    }

    public void setPublishedDate(long publishedDate) {
        this.publishedDate = publishedDate;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public long getVideoPosition() {
        return videoPosition;
    }

    public void setVideoPosition(long videoPosition) {
        this.videoPosition = videoPosition;
    }

    public void setImageType(String imageType) {
        this.imageType = imageType;
    }

    public String getImageType() {
        return imageType;
    }

    public String getParentalRating() {
        return parentalRating;
    }

    public void setParentalRating(String parentalRating) {
        this.parentalRating = parentalRating;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getCompany() {
        return company;
    }

    public void setCompany(String company) {
        this.company = company;
    }

    public String getYear() {
        return year;
    }

    public void setYear(String year) {
        this.year = year;
    }

    public String getIsNewS() {
        return isNewS;
    }

    public void setIsNewS(String isNewS) {
        this.isNewS = isNewS;
    }

    public String getIsVIP() {
        return isVIP;
    }

    public void setIsVIP(String isVIP) {
        this.isVIP = isVIP;
    }

    public void setVastTag(String VastTag) {
        this.VastTag = VastTag;
    }

    public String getVastTag() {
        return VastTag;
    }

    public String getWidevineLicence() {
        return widevineLicence;
    }

    public void setWidevineLicence(String widevineLicence) {
        this.widevineLicence = widevineLicence;
    }

    public String getGetWidevineURL() {
        return getWidevineURL;
    }

    public void setGetWidevineURL(String getWidevineURL) {
        this.getWidevineURL = getWidevineURL;
    }

    @Override
    public String toString() {
        return
                "VideosItem{" +
                        "vastTag = '" + VastTag + '\'' +
                        ",description = '" + description + '\'' +
                        ",assetKeywords = '" + assetKeywords + '\'' +
                        ",likeCount = '" + likeCount + '\'' +
                        ",title = '" + title + '\'' +
                        ",svod = '" + svod + '\'' +
                        ",contentProvider = '" + contentProvider + '\'' +
                        ",assetCast = '" + assetCast + '\'' +
                        ",premium = '" + premium + '\'' +
                        ",posterURL = '" + posterURL + '\'' +
                        ",price = '" + price + '\'' +
                        ",assetGenres = '" + assetGenres + '\'' +
                        ",season = '" + season + '\'' +
                        ",id = '" + id + '\'' +
                        ",sku = '" + sku + '\'' +
                        ",new = '" + isNew + '\'' +
                        ",tvod = '" + tvod + '\'' +
                        ",episodeNo = '" + episodeNo + '\'' +
                        ",assetType = '" + assetType + '\'' +
                        ",commentCount = '" + commentCount + '\'' +
                        ",uploadedAssetKey = '" + uploadedAssetKey + '\'' +
                        ",brightcoveVideoId = '" + brightcoveVideoId + '\'' +
                        ",series = '" + series + '\'' +
                        ",plans = '" + plans + '\'' +
                        ",publishedDate = '" + publishedDate + '\'' +
                        ",status = '" + status + '\'' +
                        "}";
    }


    public Drawable getVipImageDrawable() {
        PanteaoApplication application = PanteaoApplication.getInstance();
        try {
            if (KsPreferenceKeys.getInstance().getAppLanguage().equalsIgnoreCase("English")) {
                return ContextCompat.getDrawable(application, R.drawable.vip_icon_120);
            } else if (KsPreferenceKeys.getInstance().getAppLanguage().equalsIgnoreCase("Thai")) {
                return ContextCompat.getDrawable(application, R.drawable.vip_icon_120);
            } else {
                return ContextCompat.getDrawable(application, R.drawable.vip_icon_120);
            }
        } catch (Exception e) {
            return ContextCompat.getDrawable(application, R.drawable.vip_icon_120);
        }
    }

    public Drawable getNewSeriesImageDrawable() {
        PanteaoApplication application = PanteaoApplication.getInstance();
        try {
            if (KsPreferenceKeys.getInstance().getAppLanguage().equalsIgnoreCase("English")) {
                return ContextCompat.getDrawable(application, R.drawable.series_icon_120);
            } else if (KsPreferenceKeys.getInstance().getAppLanguage().equalsIgnoreCase("Thai")) {
                return ContextCompat.getDrawable(application, R.drawable.series_thai_icon);
            } else {
                return ContextCompat.getDrawable(application, R.drawable.series_icon_120);
            }
        } catch (Exception e) {
            return ContextCompat.getDrawable(application, R.drawable.series_icon_120);
        }
    }

    public Drawable getEpisodeImageDrawable() {
        PanteaoApplication application = PanteaoApplication.getInstance();
        try {
            if (KsPreferenceKeys.getInstance().getAppLanguage().equalsIgnoreCase("English")) {
                return ContextCompat.getDrawable(application, R.drawable.episode_icon_120);
            } else if (KsPreferenceKeys.getInstance().getAppLanguage().equalsIgnoreCase("Thai")) {
                return ContextCompat.getDrawable(application, R.drawable.episode_thai_icon);
            } else {
                return ContextCompat.getDrawable(application, R.drawable.episode_icon_120);
            }
        } catch (Exception e) {
            return ContextCompat.getDrawable(application, R.drawable.episode_icon_120);
        }
    }

    public Drawable getNewMoviesDrawable() {
        PanteaoApplication application = PanteaoApplication.getInstance();
        try {
            if (KsPreferenceKeys.getInstance().getAppLanguage().equalsIgnoreCase("English")) {
                return ContextCompat.getDrawable(application, R.drawable.new_movie_120);
            } else if (KsPreferenceKeys.getInstance().getAppLanguage().equalsIgnoreCase("Thai")) {
                return ContextCompat.getDrawable(application, R.drawable.new_movie_thai120);
            } else {
                return ContextCompat.getDrawable(application, R.drawable.new_movie_120);
            }
        } catch (Exception e) {
            return ContextCompat.getDrawable(application, R.drawable.new_movie_120);
        }
    }
}