
package com.make.bookmarking.bean;

import com.google.gson.annotations.Expose;

public class BookmarkingResponse {

    @Expose
    private Object data;
    @Expose
    private Object debugMessage;
    @Expose
    private Long responseCode;

    public Object getData() {
        return data;
    }

    public void setData(Object data) {
        this.data = data;
    }

    public Object getDebugMessage() {
        return debugMessage;
    }

    public void setDebugMessage(Object debugMessage) {
        this.debugMessage = debugMessage;
    }

    public Long getResponseCode() {
        return responseCode;
    }

    public void setResponseCode(Long responseCode) {
        this.responseCode = responseCode;
    }

}
