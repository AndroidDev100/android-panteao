package panteao.make.ready.beanModelV3.playListModelV2;

public class ItemsItem{
	private int contentOrder;
	private VideosItem content;

	public void setContentOrder(int contentOrder){
		this.contentOrder = contentOrder;
	}

	public int getContentOrder(){
		return contentOrder;
	}

	public void setContent(VideosItem content){
		this.content = content;
	}

	public VideosItem getContent(){
		return content;
	}

	@Override
 	public String toString(){
		return 
			"ItemsItem{" + 
			"contentOrder = '" + contentOrder + '\'' + 
			",content = '" + content + '\'' + 
			"}";
		}
}
