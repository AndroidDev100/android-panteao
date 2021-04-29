package panteao.make.ready.beanModelV2.searchV2;

public class CuePointsItem{
	private String metadata;
	private String brightCoveId;
	private String name;
	private int id;
	private double time;
	private String type;
	private boolean forceStop;

	public void setMetadata(String metadata){
		this.metadata = metadata;
	}

	public String getMetadata(){
		return metadata;
	}

	public void setBrightCoveId(String brightCoveId){
		this.brightCoveId = brightCoveId;
	}

	public String getBrightCoveId(){
		return brightCoveId;
	}

	public void setName(String name){
		this.name = name;
	}

	public String getName(){
		return name;
	}

	public void setId(int id){
		this.id = id;
	}

	public int getId(){
		return id;
	}

	public void setTime(double time){
		this.time = time;
	}

	public double getTime(){
		return time;
	}

	public void setType(String type){
		this.type = type;
	}

	public String getType(){
		return type;
	}

	public void setForceStop(boolean forceStop){
		this.forceStop = forceStop;
	}

	public boolean isForceStop(){
		return forceStop;
	}

	@Override
 	public String toString(){
		return 
			"CuePointsItem{" + 
			"metadata = '" + metadata + '\'' + 
			",brightCoveId = '" + brightCoveId + '\'' + 
			",name = '" + name + '\'' + 
			",id = '" + id + '\'' + 
			",time = '" + time + '\'' + 
			",type = '" + type + '\'' + 
			",forceStop = '" + forceStop + '\'' + 
			"}";
		}
}
