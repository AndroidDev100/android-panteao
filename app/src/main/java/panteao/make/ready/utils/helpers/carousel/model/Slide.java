package panteao.make.ready.utils.helpers.carousel.model;


import panteao.make.ready.beanModel.responseModels.landingTabResponses.railData.ContentsItem;
import panteao.make.ready.utils.helpers.carousel.entity.CategoryData;
import panteao.make.ready.utils.helpers.carousel.entity.UserData;

import java.io.Serializable;
import java.util.List;

import panteao.make.ready.beanModel.responseModels.landingTabResponses.railData.ContentsItem;


public class Slide implements Serializable {

    private Integer beamId;
    private String videoBeamType;
    private List<CategoryData> categories = null;
    private String orientation;
    private String startDateTime;
    private String title;
    private String description;
    private String thumbnail;
    private UserData userDTO;
    private String imageFromUrl;
    private String playerHlsPlaybackUrl;
    private String duration;
    private String vodPublish;
    private String status;
    private int imageResource;
    private int assetId;
    private ContentsItem contents;
    private int type;
    private String beamCycle;

    public ContentsItem getContents() {
        return contents;
    }

    public void setContents(ContentsItem contents) {
        this.contents = contents;
    }

    public int getAssetId() {
        return assetId;
    }

    public void setAssetId(int assetId) {
        this.assetId = assetId;
    }

    public String getImageFromUrl() {
        return imageFromUrl;
    }

    public void setImageFromUrl(String imageFromUrl) {
        this.imageFromUrl = imageFromUrl;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public Integer getBeamId() {
        return beamId;
    }

    public void setBeamId(Integer beamId) {
        this.beamId = beamId;
    }

    public String getVideoBeamType() {
        return videoBeamType;
    }

    public void setVideoBeamType(String videoBeamType) {
        this.videoBeamType = videoBeamType;
    }

    public List<CategoryData> getCategories() {
        return categories;
    }

    public void setCategories(List<CategoryData> categories) {
        this.categories = categories;
    }

    public String getOrientation() {
        return orientation;
    }

    public void setOrientation(String orientation) {
        this.orientation = orientation;
    }

    public String getStartDateTime() {
        return startDateTime;
    }

    public void setStartDateTime(String startDateTime) {
        this.startDateTime = startDateTime;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getThumbnail() {
        return thumbnail;
    }

    public void setThumbnail(String thumbnail) {
        this.thumbnail = thumbnail;
    }

    public UserData getUserDTO() {
        return userDTO;
    }

    public void setUserDTO(UserData userDTO) {
        this.userDTO = userDTO;
    }

    public String getPlayerHlsPlaybackUrl() {
        return playerHlsPlaybackUrl;
    }

    public void setPlayerHlsPlaybackUrl(String playerHlsPlaybackUrl) {
        this.playerHlsPlaybackUrl = playerHlsPlaybackUrl;
    }

    public String getDuration() {
        return duration;
    }

    public void setDuration(String duration) {
        this.duration = duration;
    }

    public String getVodPublish() {
        return vodPublish;
    }

    public void setVodPublish(String vodPublish) {
        this.vodPublish = vodPublish;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getBeamCycle() {
        return beamCycle;
    }

    public void setBeamCycle(String beamCycle) {
        this.beamCycle = beamCycle;
    }

    public int getImageResource() {
        return imageResource;
    }

    public void setImageResource(int imageResource) {
        this.imageResource = imageResource;
    }
}
