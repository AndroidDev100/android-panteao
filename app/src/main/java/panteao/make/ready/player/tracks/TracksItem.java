package panteao.make.ready.player.tracks;

public class TracksItem {


        private String trackName;
        private String uniqueId;
        private boolean isSelected;

        public TracksItem(String trackName, String uniqueId) {
            this.trackName = trackName;
            this.uniqueId = uniqueId;

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
}
