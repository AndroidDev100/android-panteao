package panteao.make.ready.beanModelV3.playListModelV2;

import java.util.List;

public class PlayListDetailsResponse {
    private int pageNumber;
    private int totalPages;
    private int pageSize;
    private List<ItemsItem> items;
    private int totalElements;

    public void setPageNumber(int pageNumber){
        this.pageNumber = pageNumber;
    }

    public int getPageNumber(){
        return pageNumber;
    }

    public void setTotalPages(int totalPages){
        this.totalPages = totalPages;
    }

    public int getTotalPages(){
        return totalPages;
    }

    public void setPageSize(int pageSize){
        this.pageSize = pageSize;
    }

    public int getPageSize(){
        return pageSize;
    }

    public void setItems(List<ItemsItem> items){
        this.items = items;
    }

    public List<ItemsItem> getItems(){
        return items;
    }

    public void setTotalElements(int totalElements){
        this.totalElements = totalElements;
    }

    public int getTotalElements(){
        return totalElements;
    }

    @Override
    public String toString(){
        return
                "Data{" +
                        "pageNumber = '" + pageNumber + '\'' +
                        ",totalPages = '" + totalPages + '\'' +
                        ",pageSize = '" + pageSize + '\'' +
                        ",items = '" + items + '\'' +
                        ",totalElements = '" + totalElements + '\'' +
                        "}";
    }
}