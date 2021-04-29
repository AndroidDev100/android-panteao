package panteao.make.ready.beanModelV3.searchV2;

public class PageInfo{
	private int perpage;
	private int total;
	private int pages;
	private Object field;
	private Object page;
	private Object sort;

	public void setPerpage(int perpage){
		this.perpage = perpage;
	}

	public int getPerpage(){
		return perpage;
	}

	public void setTotal(int total){
		this.total = total;
	}

	public int getTotal(){
		return total;
	}

	public void setPages(int pages){
		this.pages = pages;
	}

	public int getPages(){
		return pages;
	}

	public void setField(Object field){
		this.field = field;
	}

	public Object getField(){
		return field;
	}

	public void setPage(Object page){
		this.page = page;
	}

	public Object getPage(){
		return page;
	}

	public void setSort(Object sort){
		this.sort = sort;
	}

	public Object getSort(){
		return sort;
	}

	@Override
 	public String toString(){
		return 
			"PageInfo{" + 
			"perpage = '" + perpage + '\'' + 
			",total = '" + total + '\'' + 
			",pages = '" + pages + '\'' + 
			",field = '" + field + '\'' + 
			",page = '" + page + '\'' + 
			",sort = '" + sort + '\'' + 
			"}";
		}
}
