
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

    public ThumbnailEn getThumbnailEn() {
        return thumbnailEn;
    }

    public void setThumbnailEn(ThumbnailEn thumbnailEn) {
        this.thumbnailEn = thumbnailEn;
    }

    public Thumbnail getThumbnail() {
        return thumbnail;
    }

    public void setThumbnail(Thumbnail thumbnail) {
        this.thumbnail = thumbnail;
    }

    public Poster getPoster() {
        return poster;
    }

    public void setPoster(Poster poster) {
        this.poster = poster;
    }

    public PosterEn getPosterEn() {
        return posterEn;
    }

    public void setPosterEn(PosterEn posterEn) {
        this.posterEn = posterEn;
    }

}
