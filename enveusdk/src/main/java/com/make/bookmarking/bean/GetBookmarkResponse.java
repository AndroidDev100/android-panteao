
package com.make.bookmarking.bean;

import java.util.List;
import com.google.gson.annotations.SerializedName;

public class GetBookmarkResponse {

    @SerializedName("data")
    private List<Bookmarks> mData;
    @SerializedName("debugMessage")
    private Object mDebugMessage;
    @SerializedName("responseCode")
    private Long mResponseCode;

    public List<Bookmarks> getBookmarks() {
        return mData;
    }

    public void setData(List<Bookmarks> data) {
        mData = data;
    }

    public Object getDebugMessage() {
        return mDebugMessage;
    }

    public void setDebugMessage(Object debugMessage) {
        mDebugMessage = debugMessage;
    }

    public Long getResponseCode() {
        return mResponseCode;
    }

    public void setResponseCode(Long responseCode) {
        mResponseCode = responseCode;
    }

}
