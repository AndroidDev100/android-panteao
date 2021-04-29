package panteao.make.ready.beanModel.seriesResponse;

public class Data{
	private int vodCount;
	private int seasonCount;
	private String name;
	private String brightcoveSeriesId;
	private String description;
	private int id;
	private String posterURL;
	private String thumbnailURL;
	private String status;
	private String title;

	public void setVodCount(int vodCount){
		this.vodCount = vodCount;
	}

	public int getVodCount(){
		return vodCount;
	}

	public void setSeasonCount(int seasonCount){
		this.seasonCount = seasonCount;
	}

	public int getSeasonCount(){
		return seasonCount;
	}

	public void setName(String name){
		this.name = name;
	}

	public String getName(){
		return name;
	}

	public void setBrightcoveSeriesId(String brightcoveSeriesId){
		this.brightcoveSeriesId = brightcoveSeriesId;
	}

	public String getBrightcoveSeriesId(){
		return brightcoveSeriesId;
	}

	public void setDescription(String description){
		this.description = description;
	}

	public String getDescription(){
		return description;
	}

	public void setId(int id){
		this.id = id;
	}

	public int getId(){
		return id;
	}

	public void setStatus(String status){
		this.status = status;
	}

	public String getStatus(){
		return status;
	}

	public String getPosterURL() {
		return posterURL;
	}

	public void setPosterURL(String posterURL) {
		this.posterURL = posterURL;
	}

	public String getThumbnailURL() {
		return thumbnailURL;
	}

	public void setThumbnailURL(String thumbnailURL) {
		this.thumbnailURL = thumbnailURL;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	@Override
	public String toString() {
		return "Data{" +
				"vodCount=" + vodCount +
				", seasonCount=" + seasonCount +
				", name='" + name + '\'' +
				", brightcoveSeriesId='" + brightcoveSeriesId + '\'' +
				", description='" + description + '\'' +
				", id=" + id +
				", posterURL='" + posterURL + '\'' +
				", thumbnailURL='" + thumbnailURL + '\'' +
				", status='" + status + '\'' +
				", title='" + title + '\'' +
				'}';
	}
}
