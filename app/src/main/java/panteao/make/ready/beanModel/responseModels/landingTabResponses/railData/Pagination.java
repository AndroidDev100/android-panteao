package panteao.make.ready.beanModel.responseModels.landingTabResponses.railData;

public class Pagination {
    private int pageNo;
    private int totalPages;
    private int pageSize;
    private int totalElements;

    public int getPageNo() {
        return pageNo;
    }

    public void setPageNo(int pageNo) {
        this.pageNo = pageNo;
    }

    public int getTotalPages() {
        return totalPages;
    }

    public void setTotalPages(int totalPages) {
        this.totalPages = totalPages;
    }

    public int getPageSize() {
        return pageSize;
    }

    public void setPageSize(int pageSize) {
        this.pageSize = pageSize;
    }

    public int getTotalElements() {
        return totalElements;
    }

    public void setTotalElements(int totalElements) {
        this.totalElements = totalElements;
    }

    @Override
    public String toString() {
        return
                "Pagination{" +
                        "pageNo = '" + pageNo + '\'' +
                        ",totalPages = '" + totalPages + '\'' +
                        ",pageSize = '" + pageSize + '\'' +
                        ",totalElements = '" + totalElements + '\'' +
                        "}";
    }
}
