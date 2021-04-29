package panteao.make.ready.beanModelV3.searchV2;

import java.util.List;

public class Data{
	private PageInfo pageInfo;
	private List<ItemsItem> items;

	public void setPageInfo(PageInfo pageInfo){
		this.pageInfo = pageInfo;
	}

	public PageInfo getPageInfo(){
		return pageInfo;
	}

	public void setItems(List<ItemsItem> items){
		this.items = items;
	}

	public List<ItemsItem> getItems(){
		return items;
	}

	@Override
 	public String toString(){
		return 
			"Data{" + 
			"pageInfo = '" + pageInfo + '\'' + 
			",items = '" + items + '\'' + 
			"}";
		}
}