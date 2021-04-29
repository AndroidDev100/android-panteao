package panteao.make.ready.networking.bean;

public class CustomFields{
	private String isPremium;
	private String year;
	private String rating;
	private String series;

	public void setIsPremium(String isPremium){
		this.isPremium = isPremium;
	}

	public String getIsPremium(){
		return isPremium;
	}

	public void setYear(String year){
		this.year = year;
	}

	public String getYear(){
		return year;
	}

	public void setRating(String rating){
		this.rating = rating;
	}

	public String getRating(){
		return rating;
	}

	public void setSeries(String series){
		this.series = series;
	}

	public String getSeries(){
		return series;
	}

	@Override
 	public String toString(){
		return 
			"CustomFields{" + 
			"is_premium = '" + isPremium + '\'' + 
			",year = '" + year + '\'' + 
			",rating = '" + rating + '\'' + 
			",series = '" + series + '\'' + 
			"}";
		}
}
