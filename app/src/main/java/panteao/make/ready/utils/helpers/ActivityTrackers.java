package panteao.make.ready.utils.helpers;


public class ActivityTrackers {

    private static ActivityTrackers activityTrackersInstance;
    public String launcherActivity;
    public String action;

    public static String WATCHLIST="watchlist";
    public static String LIKE="like";

    private ActivityTrackers() {

    }

    public static ActivityTrackers getInstance() {
        if (activityTrackersInstance == null) {
            activityTrackersInstance = new ActivityTrackers();
        }
        return (activityTrackersInstance);
    }

    public void setLauncherActivity(String activity) {
        this.launcherActivity=activity;
    }

    public void setAction(String actionTaken) {
        this.action=actionTaken;
    }
}
