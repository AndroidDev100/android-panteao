package panteao.make.ready.beanModelV3.videoDetailsV2;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import panteao.make.ready.beanModelV3.playListModelV2.Thumbnail;
import panteao.make.ready.networking.bean.CustomFields;

import java.util.HashMap;
import java.util.List;

public class LinkedContent{
	private String longDescription;
	private List<Object> keywords;
	private CustomFields customFields;
	private String description;
	private String title;
	private String posterImage;
	private Object svod;
	private String contentProvider;
	private Object duration;
	private Object audioTracks;
	private List<String> cast;
	private long lastUpdated;
	private boolean premium;
	private long dateCreated;
	private List<String> genres;
	private Object price;
	private int id;
	private String thumbnailImage;
	private String sku;
	private String contentType;
	private Object adKeys;
	private Object linkedContent;
	private Object jsonMemberNew;
	private Object tvod;
	private List<Integer> seasons;
	private Object episodeNo;
	private List<Object> textTracks;
	private Object seasonNumber;
	private Object brightcoveContentId;
	private Object cuePoints;
	private long publishedDate;
	private String status;
	private HashMap<String, Thumbnail> images;

	public HashMap<String, Thumbnail> getImages() {
		return images;
	}

	public void setImages(HashMap<String, Thumbnail> images) {
		this.images = images;
	}

	public void setLongDescription(String longDescription){
		this.longDescription = longDescription;
	}

	public String getLongDescription(){
		return longDescription;
	}

	public void setKeywords(List<Object> keywords){
		this.keywords = keywords;
	}

	public List<Object> getKeywords(){
		return keywords;
	}

	public void setCustomFields(CustomFields customFields){
		this.customFields = customFields;
	}

	public CustomFields getCustomFields(){
		return customFields;
	}

	public void setDescription(String description){
		this.description = description;
	}

	public String getDescription(){
		return description;
	}

	public void setTitle(String title){
		this.title = title;
	}

	public String getTitle(){
		return title;
	}

	public void setPosterImage(String posterImage){
		this.posterImage = posterImage;
	}

	public String getPosterImage(){
		return posterImage;
	}

	public void setSvod(Object svod){
		this.svod = svod;
	}

	public Object getSvod(){
		return svod;
	}

	public void setContentProvider(String contentProvider){
		this.contentProvider = contentProvider;
	}

	public String getContentProvider(){
		return contentProvider;
	}

	public void setDuration(Object duration){
		this.duration = duration;
	}

	public Object getDuration(){
		return duration;
	}

	public void setAudioTracks(Object audioTracks){
		this.audioTracks = audioTracks;
	}

	public Object getAudioTracks(){
		return audioTracks;
	}

	public void setCast(List<String> cast){
		this.cast = cast;
	}

	public List<String> getCast(){
		return cast;
	}

	public void setLastUpdated(long lastUpdated){
		this.lastUpdated = lastUpdated;
	}

	public long getLastUpdated(){
		return lastUpdated;
	}

	public void setPremium(boolean premium){
		this.premium = premium;
	}

	public boolean isPremium(){
		return premium;
	}

	public void setDateCreated(long dateCreated){
		this.dateCreated = dateCreated;
	}

	public long getDateCreated(){
		return dateCreated;
	}

	public void setGenres(List<String> genres){
		this.genres = genres;
	}

	public List<String> getGenres(){
		return genres;
	}

	public void setPrice(Object price){
		this.price = price;
	}

	public Object getPrice(){
		return price;
	}

	public void setId(int id){
		this.id = id;
	}

	public int getId(){
		return id;
	}

	public void setThumbnailImage(String thumbnailImage){
		this.thumbnailImage = thumbnailImage;
	}

	public String getThumbnailImage(){
		return thumbnailImage;
	}

	public void setSku(String sku){
		this.sku = sku;
	}

	public String getSku(){
		return sku;
	}

	public void setContentType(String contentType){
		this.contentType = contentType;
	}

	public String getContentType(){
		return contentType;
	}

	public void setAdKeys(Object adKeys){
		this.adKeys = adKeys;
	}

	public Object getAdKeys(){
		return adKeys;
	}

	public void setLinkedContent(Object linkedContent){
		this.linkedContent = linkedContent;
	}

	public Object getLinkedContent(){
		return linkedContent;
	}

	public void setJsonMemberNew(Object jsonMemberNew){
		this.jsonMemberNew = jsonMemberNew;
	}

	public Object getJsonMemberNew(){
		return jsonMemberNew;
	}

	public void setTvod(Object tvod){
		this.tvod = tvod;
	}

	public Object getTvod(){
		return tvod;
	}

	public void setSeasons(List<Integer> seasons){
		this.seasons = seasons;
	}

	public List<Integer> getSeasons(){
		return seasons;
	}

	public void setEpisodeNo(Object episodeNo){
		this.episodeNo = episodeNo;
	}

	public Object getEpisodeNo(){
		return episodeNo;
	}

	public void setTextTracks(List<Object> textTracks){
		this.textTracks = textTracks;
	}

	public List<Object> getTextTracks(){
		return textTracks;
	}

	public void setSeasonNumber(Object seasonNumber){
		this.seasonNumber = seasonNumber;
	}

	public Object getSeasonNumber(){
		return seasonNumber;
	}

	public void setBrightcoveContentId(Object brightcoveContentId){
		this.brightcoveContentId = brightcoveContentId;
	}

	public Object getBrightcoveContentId(){
		return brightcoveContentId;
	}

	public void setCuePoints(Object cuePoints){
		this.cuePoints = cuePoints;
	}

	public Object getCuePoints(){
		return cuePoints;
	}

	public void setPublishedDate(long publishedDate){
		this.publishedDate = publishedDate;
	}

	public long getPublishedDate(){
		return publishedDate;
	}

	public void setStatus(String status){
		this.status = status;
	}

	public String getStatus(){
		return status;
	}

	@Override
 	public String toString(){
		return 
			"LinkedContent{" + 
			"longDescription = '" + longDescription + '\'' + 
			",keywords = '" + keywords + '\'' + 
			",customFields = '" + customFields + '\'' + 
			",description = '" + description + '\'' + 
			",title = '" + title + '\'' + 
			",posterImage = '" + posterImage + '\'' + 
			",svod = '" + svod + '\'' + 
			",contentProvider = '" + contentProvider + '\'' + 
			",duration = '" + duration + '\'' + 
			",audioTracks = '" + audioTracks + '\'' + 
			",cast = '" + cast + '\'' + 
			",lastUpdated = '" + lastUpdated + '\'' + 
			",premium = '" + premium + '\'' + 
			",dateCreated = '" + dateCreated + '\'' + 
			",genres = '" + genres + '\'' + 
			",price = '" + price + '\'' + 
			",id = '" + id + '\'' + 
			",thumbnailImage = '" + thumbnailImage + '\'' + 
			",sku = '" + sku + '\'' + 
			",contentType = '" + contentType + '\'' + 
			",adKeys = '" + adKeys + '\'' + 
			",linkedContent = '" + linkedContent + '\'' + 
			",new = '" + jsonMemberNew + '\'' + 
			",tvod = '" + tvod + '\'' + 
			",seasons = '" + seasons + '\'' + 
			",episodeNo = '" + episodeNo + '\'' + 
			",textTracks = '" + textTracks + '\'' + 
			",seasonNumber = '" + seasonNumber + '\'' + 
			",brightcoveContentId = '" + brightcoveContentId + '\'' + 
			",cuePoints = '" + cuePoints + '\'' + 
			",publishedDate = '" + publishedDate + '\'' + 
			",status = '" + status + '\'' + 
			"}";
		}
}