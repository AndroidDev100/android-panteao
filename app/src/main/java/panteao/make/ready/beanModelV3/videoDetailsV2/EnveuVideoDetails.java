
package panteao.make.ready.beanModelV3.videoDetailsV2;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import panteao.make.ready.beanModelV3.playListModelV2.Images;

import java.util.List;

import panteao.make.ready.beanModelV3.playListModelV2.Images;

public class EnveuVideoDetails {

    @SerializedName("dateCreated")
    @Expose
    private long dateCreated;
    @SerializedName("lastUpdated")
    @Expose
    private long lastUpdated;
    @SerializedName("contentSource")
    @Expose
    private String contentSource;
    @SerializedName("id")
    @Expose
    private Integer id;
    @SerializedName("adKeys")
    @Expose
    private Object adKeys;
    @SerializedName("cuePoints")
    @Expose
    private Object cuePoints;
    @SerializedName("customFields")
    @Expose
    private Object customFields;
    @SerializedName("deliveryType")
    @Expose
    private String deliveryType;
    @SerializedName("description")
    @Expose
    private String description;
    @SerializedName("duration")
    @Expose
    private int duration;
    @SerializedName("economics")
    @Expose
    private String economics;
    @SerializedName("brightcoveContentId")
    @Expose
    private String brightcoveContentId;
    @SerializedName("images")
    @Expose
    private Images images;
    @SerializedName("longDescription")
    @Expose
    private Object longDescription;
    @SerializedName("title")
    @Expose
    private String title;
    @SerializedName("kEntryId")
    @Expose
    private String kEntryId;
    @SerializedName("status")
    @Expose
    private String status;
    @SerializedName("keywords")
    @Expose
    private List<Object> keywords;
    @SerializedName("textTracks")
    @Expose
    private List<Object> textTracks;
    @SerializedName("audioTracks")
    @Expose
    private Object audioTracks;
    @SerializedName("contentType")
    @Expose
    private String contentType;
    @SerializedName("genres")
    @Expose
    private List<String> genres;
    @SerializedName("cast")
    @Expose
    private List<String> cast;
    @SerializedName("sku")
    @Expose
    private String sku;
    @SerializedName("seasonNumber")
    @Expose
    private Object seasonNumber;
    @SerializedName("episodeNumber")
    @Expose
    private Object episodeNumber;
    @SerializedName("publishedDate")
    @Expose
    private long publishedDate;
    @SerializedName("seasons")
    @Expose
    private List<Integer> seasons = null;
    @SerializedName("contentMonetization")
    @Expose
    private Object contentMonetization;
    @SerializedName("linkedContent")
    @Expose
    private LinkedContent linkedContent;
    @SerializedName("contentProvider")
    @Expose
    private String contentProvider;
    @SerializedName("complete")
    @Expose
    private Object complete;
    @SerializedName("drmdisabled")
    @Expose
    private Boolean drmdisabled;
    @SerializedName("premium")
    @Expose
    private Boolean premium;
    @SerializedName("offlineEnabled")
    @Expose
    private Boolean offlineEnabled;

    public long getDateCreated() {
        return dateCreated;
    }

    public void setDateCreated(long dateCreated) {
        this.dateCreated = dateCreated;
    }

    public long getLastUpdated() {
        return lastUpdated;
    }

    public void setLastUpdated(long lastUpdated) {
        this.lastUpdated = lastUpdated;
    }

    public String getContentSource() {
        return contentSource;
    }

    public void setContentSource(String contentSource) {
        this.contentSource = contentSource;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Object getAdKeys() {
        return adKeys;
    }

    public void setAdKeys(Object adKeys) {
        this.adKeys = adKeys;
    }

    public Object getCuePoints() {
        return cuePoints;
    }

    public void setCuePoints(Object cuePoints) {
        this.cuePoints = cuePoints;
    }

    public Object getCustomFields() {
        return customFields;
    }

    public void setCustomFields(Object customFields) {
        this.customFields = customFields;
    }

    public String getDeliveryType() {
        return deliveryType;
    }

    public void setDeliveryType(String deliveryType) {
        this.deliveryType = deliveryType;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getDuration() {
        return duration;
    }

    public void setDuration(int duration) {
        this.duration = duration;
    }

    public String getEconomics() {
        return economics;
    }

    public void setEconomics(String economics) {
        this.economics = economics;
    }

    public String getBrightcoveContentId() {
        return brightcoveContentId;
    }

    public void setBrightcoveContentId(String brightcoveContentId) {
        this.brightcoveContentId = brightcoveContentId;
    }

    public Images getImages() {
        return images;
    }

    public void setImages(Images images) {
        this.images = images;
    }

    public Object getLongDescription() {
        return longDescription;
    }

    public void setLongDescription(Object longDescription) {
        this.longDescription = longDescription;
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

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public List<Object> getKeywords() {
        return keywords;
    }

    public void setKeywords(List<Object> keywords) {
        this.keywords = keywords;
    }

    public List<Object> getTextTracks() {
        return textTracks;
    }

    public void setTextTracks(List<Object> textTracks) {
        this.textTracks = textTracks;
    }

    public Object getAudioTracks() {
        return audioTracks;
    }

    public void setAudioTracks(Object audioTracks) {
        this.audioTracks = audioTracks;
    }

    public String getContentType() {
        return contentType;
    }

    public void setContentType(String contentType) {
        this.contentType = contentType;
    }

    public List<String> getGenres() {
        return genres;
    }

    public void setGenres(List<String> genres) {
        this.genres = genres;
    }

    public List<String> getCast() {
        return cast;
    }

    public void setCast(List<String> cast) {
        this.cast = cast;
    }

    public String getSku() {
        return sku;
    }

    public void setSku(String sku) {
        this.sku = sku;
    }

    public Object getSeasonNumber() {
        return seasonNumber;
    }

    public void setSeasonNumber(Object seasonNumber) {
        this.seasonNumber = seasonNumber;
    }

    public Object getEpisodeNumber() {
        return episodeNumber;
    }

    public void setEpisodeNumber(Object episodeNumber) {
        this.episodeNumber = episodeNumber;
    }

    public long getPublishedDate() {
        return publishedDate;
    }

    public void setPublishedDate(long publishedDate) {
        this.publishedDate = publishedDate;
    }

    public List<Integer> getSeasons() {
        return seasons;
    }

    public void setSeasons(List<Integer> seasons) {
        this.seasons = seasons;
    }

    public Object getContentMonetization() {
        return contentMonetization;
    }

    public void setContentMonetization(Object contentMonetization) {
        this.contentMonetization = contentMonetization;
    }

    public LinkedContent getLinkedContent() {
        return linkedContent;
    }

    public void setLinkedContent(LinkedContent linkedContent) {
        this.linkedContent = linkedContent;
    }

    public String getContentProvider() {
        return contentProvider;
    }

    public void setContentProvider(String contentProvider) {
        this.contentProvider = contentProvider;
    }

    public Object getComplete() {
        return complete;
    }

    public void setComplete(Object complete) {
        this.complete = complete;
    }

    public Boolean getDrmdisabled() {
        return drmdisabled;
    }

    public void setDrmdisabled(Boolean drmdisabled) {
        this.drmdisabled = drmdisabled;
    }

    public Boolean getPremium() {
        return premium;
    }

    public void setPremium(Boolean premium) {
        this.premium = premium;
    }

    public Boolean getOfflineEnabled() {
        return offlineEnabled;
    }

    public void setOfflineEnabled(Boolean offlineEnabled) {
        this.offlineEnabled = offlineEnabled;
    }

}
