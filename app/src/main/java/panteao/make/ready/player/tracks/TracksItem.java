package panteao.make.ready.player.tracks;

public class TracksItem {


        private String trackName;
        private String uniqueId;

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


}
