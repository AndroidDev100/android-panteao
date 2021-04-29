package com.make.brightcovelibrary;

import java.util.Objects;

public class TrackItem {


    private final String trackName; //Readable name of the track.
    private final String uniqueId; //Unique id, which should be passed to player in order to change track.
    private String trackDescription;
    private boolean isSelected;
    private int bitrate;
    private int bitRatePosition;

    public TrackItem(String trackName, String uniqueId) {
        this.trackName = trackName;
        this.uniqueId = uniqueId;

    }

    public TrackItem(String trackName, int bitrate, String desc,int selectedPos,String uniqueId) {
        this.trackName = trackName;
        this.bitrate = bitrate;
        this.trackDescription=desc;
        this.bitRatePosition=selectedPos;
        this.uniqueId=uniqueId;

    }

    public TrackItem(String trackName, String uniqueId, String trackDescription) {
        this.trackName = trackName;
        this.uniqueId = uniqueId;
        this.trackDescription = trackDescription;
    }

    public TrackItem(String trackName, String uniqueId, boolean isSelected) {
        this.trackName = trackName;
        this.uniqueId = uniqueId;
        this.isSelected = isSelected;
    }

    public String getTrackDescription() {
        return trackDescription;
    }

    public void setTrackDescription(String trackdescription) {
        trackDescription = trackdescription;
    }

    public String getTrackName() {
        return trackName;
    }

    public String getUniqueId() {
        return uniqueId;
    }


    public boolean isSelected() {
        return isSelected;
    }

    public void setSelected(boolean selected) {
        isSelected = selected;
    }

    public int getBitrate() {
        return bitrate;
    }

    public void setBitrate(int bitrate) {
        this.bitrate = bitrate;
    }

    public void setBitRatePosition(int bitRatePosition) {
        this.bitRatePosition = bitRatePosition;
    }

    public int getBitRatePosition() {
        return bitRatePosition;
    }

    @Override
    public boolean equals(Object obj) {
        TrackItem purchaseModel = (TrackItem) obj;
        return String.valueOf(this.isSelected).equals(String.valueOf(purchaseModel.isSelected));
    }

    @Override
    public int hashCode() {
        return Objects.hash(trackName, uniqueId, isSelected);
    }
}
