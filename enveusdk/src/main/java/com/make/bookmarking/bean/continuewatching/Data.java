
package com.make.bookmarking.bean.continuewatching;

import java.util.List;
import com.google.gson.annotations.SerializedName;

public class Data {

    @SerializedName("items")
    private List<ContinueWatchingBookmark> mItems;
    @SerializedName("pageNumber")
    private Long mPageNumber;
    @SerializedName("pageSize")
    private Long mPageSize;
    @SerializedName("totalElements")
    private Long mTotalElements;
    @SerializedName("totalPages")
    private Long mTotalPages;

    public List<ContinueWatchingBookmark> getContinueWatchingBookmarks() {
        return mItems;
    }

    public void setItems(List<ContinueWatchingBookmark> items) {
        mItems = items;
    }

    public Long getPageNumber() {
        return mPageNumber;
    }

    public void setPageNumber(Long pageNumber) {
        mPageNumber = pageNumber;
    }

    public Long getPageSize() {
        return mPageSize;
    }

    public void setPageSize(Long pageSize) {
        mPageSize = pageSize;
    }

    public Long getTotalElements() {
        return mTotalElements;
    }

    public void setTotalElements(Long totalElements) {
        mTotalElements = totalElements;
    }

    public long getTotalPages() {
        return mTotalPages;
    }

    public void setTotalPages(Long totalPages) {
        mTotalPages = totalPages;
    }

}
