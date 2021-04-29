package panteao.make.ready.beanModel.enveuCommonRailData;

import com.google.gson.annotations.SerializedName;

public class SeriesItem {

    @SerializedName("brightcoveSeriesId")
    private String mBrightcoveSeriesId;
    @SerializedName("description")
    private String mDescription;
    @SerializedName("id")
    private Long mId;
    @SerializedName("title")
    private String mName;
    @SerializedName("posterURL")
    private String mPosterImage;
    @SerializedName("seasonCount")
    private Long mSeasonCount;
    @SerializedName("status")
    private String mStatus;
    @SerializedName("thumbnailURL")
    private String mThumbnailImage;
    @SerializedName("vodCount")
    private Long mVodCount;

    public String getBrightcoveSeriesId() {
        return mBrightcoveSeriesId;
    }

    public void setBrightcoveSeriesId(String brightcoveSeriesId) {
        mBrightcoveSeriesId = brightcoveSeriesId;
    }

    public String getDescription() {
        return mDescription;
    }

    public void setDescription(String description) {
        mDescription = description;
    }

    public Long getId() {
        return mId;
    }

    public void setId(Long id) {
        mId = id;
    }

    public String getName() {
        return mName;
    }

    public void setName(String name) {
        mName = name;
    }

    public String getPosterImage() {
        return mPosterImage;
    }

    public void setPosterImage(String posterImage) {
        mPosterImage = posterImage;
    }

    public Long getSeasonCount() {
        return mSeasonCount;
    }

    public void setSeasonCount(Long seasonCount) {
        mSeasonCount = seasonCount;
    }

    public String getStatus() {
        return mStatus;
    }

    public void setStatus(String status) {
        mStatus = status;
    }

    public String getThumbnailImage() {
        return mThumbnailImage;
    }

    public void setThumbnailImage(String thumbnailImage) {
        mThumbnailImage = thumbnailImage;
    }

    public Long getVodCount() {
        return mVodCount;
    }

    public void setVodCount(Long vodCount) {
        mVodCount = vodCount;
    }

}
