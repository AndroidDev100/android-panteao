
package com.make.bookmarking.bean.continuewatching;

import com.google.gson.annotations.SerializedName;

@SuppressWarnings("unused")
public class ContinueWatchingBookmark {

    @SerializedName("assetId")
    private Long mAssetId;
    @SerializedName("completed")
    private Boolean mCompleted;
    @SerializedName("dateCreated")
    private Long mDateCreated;
    @SerializedName("id")
    private String mId;
    @SerializedName("lastUpdated")
    private Long mLastUpdated;
    @SerializedName("latest")
    private Boolean mLatest;
    @SerializedName("position")
    private Long mPosition;
    @SerializedName("projectId")
    private Object mProjectId;
    @SerializedName("userId")
    private Long mUserId;

    public Long getAssetId() {
        return mAssetId;
    }

    public void setAssetId(Long assetId) {
        mAssetId = assetId;
    }

    public Boolean getCompleted() {
        return mCompleted;
    }

    public void setCompleted(Boolean completed) {
        mCompleted = completed;
    }

    public Long getDateCreated() {
        return mDateCreated;
    }

    public void setDateCreated(Long dateCreated) {
        mDateCreated = dateCreated;
    }

    public String getId() {
        return mId;
    }

    public void setId(String id) {
        mId = id;
    }

    public Long getLastUpdated() {
        return mLastUpdated;
    }

    public void setLastUpdated(Long lastUpdated) {
        mLastUpdated = lastUpdated;
    }

    public Boolean getLatest() {
        return mLatest;
    }

    public void setLatest(Boolean latest) {
        mLatest = latest;
    }

    public Long getPosition() {
        return mPosition;
    }

    public void setPosition(Long position) {
        mPosition = position;
    }

    public Object getProjectId() {
        return mProjectId;
    }

    public void setProjectId(Object projectId) {
        mProjectId = projectId;
    }

    public Long getUserId() {
        return mUserId;
    }

    public void setUserId(Long userId) {
        mUserId = userId;
    }

    /*@Override
    public boolean equals(Object obj) {
        if (obj instanceof ContinueWatchingBookmark) {
            return ((ContinueWatchingBookmark) obj).mAssetId == mAssetId;
        }
        return false;
    }
    @Override
    public int hashCode() {
        return this.mAssetId.intValue();
    }*/

}
