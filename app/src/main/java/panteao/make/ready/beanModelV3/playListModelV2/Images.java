
package panteao.make.ready.beanModelV3.playListModelV2;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Images {

    @SerializedName("thumbnail.en")
    @Expose
    private ThumbnailEn thumbnailEn;
    @SerializedName("thumbnail")
    @Expose
    private Thumbnail thumbnail;
    @SerializedName("poster")
    @Expose
    private Poster poster;
    @SerializedName("poster.en")
    @Expose
    private PosterEn posterEn;

    @SerializedName("1920x1080")
    @Expose
    private ThumbnailEn hdBanner;

    @SerializedName("3840x1260")
    @Expose
    private Thumbnail banner;

    @SerializedName("1280x720")
    @Expose
    private Poster newPoster;

    public ThumbnailEn getThumbnailEn() {
        return hdBanner;
    }

    public void setThumbnailEn(ThumbnailEn thumbnailEn) {
        this.thumbnailEn = thumbnailEn;
    }

    public ThumbnailEn getThumbnail() {
        return hdBanner;
    }

    public void setThumbnail(Thumbnail thumbnail) {
        this.thumbnail = thumbnail;
    }

    public Poster getPoster() {
        return newPoster;
    }

    public void setPoster(Poster poster) {
        this.poster = poster;
    }

    public Poster getPosterEn() {
        return newPoster;
    }

    public void setPosterEn(PosterEn posterEn) {
        this.posterEn = posterEn;
    }

}
