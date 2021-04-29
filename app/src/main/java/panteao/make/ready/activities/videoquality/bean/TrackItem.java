package panteao.make.ready.activities.videoquality.bean;

public class TrackItem {


    private final String trackName; //Readable name of the track.
    private final String uniqueId; //Unique id, which should be passed to player in order to change track.

    public TrackItem(String trackName, String uniqueId) {
        this.trackName = trackName;
        this.uniqueId = uniqueId;
    }

    public String getTrackName() {
        return trackName;
    }

    public String getUniqueId() {
        return uniqueId;
    }
}

