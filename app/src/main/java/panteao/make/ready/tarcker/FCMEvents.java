package panteao.make.ready.tarcker;

import android.content.Context;

import static panteao.make.ready.utils.constants.AppConstants.TRACK_EVENT_ADD_TO_WATCHLIST;
import static panteao.make.ready.utils.constants.AppConstants.TRACK_EVENT_CONTENT_COMPLETED;
import static panteao.make.ready.utils.constants.AppConstants.TRACK_EVENT_CONTENT_EXIT;
import static panteao.make.ready.utils.constants.AppConstants.TRACK_EVENT_CONTENT_PLAY;
import static panteao.make.ready.utils.constants.AppConstants.TRACK_EVENT_CONTENT_SELECT;
import static panteao.make.ready.utils.constants.AppConstants.TRACK_EVENT_FIREBASE_SCREEN;
import static panteao.make.ready.utils.constants.AppConstants.TRACK_EVENT_GALLERY_SELECT;
import static panteao.make.ready.utils.constants.AppConstants.TRACK_EVENT_LOGOUT;
import static panteao.make.ready.utils.constants.AppConstants.TRACK_EVENT_REMOVE_WATCHLIST;
import static panteao.make.ready.utils.constants.AppConstants.TRACK_EVENT_SEARCH;
import static panteao.make.ready.utils.constants.AppConstants.TRACK_EVENT_SETTINGS_VIDEO_QUALITY;
import static panteao.make.ready.utils.constants.AppConstants.TRACK_EVENT_SHARE_CONTENT;
import static panteao.make.ready.utils.constants.AppConstants.TRACK_EVENT_SIGN_IN_SUCCESS;
import static panteao.make.ready.utils.constants.AppConstants.TRACK_EVENT_SIGN_UP_SUCCESS;

public class FCMEvents {

    private static FCMEvents instance;
    Context context;

    private FCMEvents() {
    }

    public static FCMEvents getInstance() {
        if (instance == null) {
            instance = new FCMEvents();
        }
        return (instance);
    }

    public void trackEvent(int eventType) {
        switch (eventType) {
            case 1:
//    TrackerUtil.getInstance(context).track(TrackerEvent.REGISTER, requestParam,
//                       Arrays.asList(PlatformType.FCM));
                break;
            case 2:
                /*TrackerUtil.getInstance(context).track(TrackerEvent.CLICK_CONTENT, requestParam,
                        Arrays.asList(PlatformType.FCM));*/
                break;
            case 3:
               /* TrackerUtil.getInstance(context).track(TrackerEvent.PLAY_CONTENT, requestParam,
                        Arrays.asList(PlatformType.FCM));*/
                break;

            case 4:
                /*TrackerUtil.getInstance(context).track(TrackerEvent.SEARCH, requestParam,
                        Arrays.asList(PlatformType.FCM));*/
            case 5:
                /*TrackerUtil.getInstance(context).track(TrackerEvent.LOGIN, requestParam,
                        Arrays.asList(PlatformType.FCM));*/
                break;
            case TRACK_EVENT_FIREBASE_SCREEN:
                /*TrackerUtil.getInstance(context).track(TrackerEvent.screen_track, requestParam,
                        Arrays.asList(PlatformType.FCM));*/
                break;
            case TRACK_EVENT_GALLERY_SELECT:
               /* TrackerUtil.getInstance(context).track(TrackerEvent.gallery_select, requestParam,
                        Arrays.asList(PlatformType.FCM));*/
                break;
            case TRACK_EVENT_CONTENT_SELECT:
                /*TrackerUtil.getInstance(context).track(TrackerEvent.content_select, requestParam,
                        Arrays.asList(PlatformType.FCM));*/
                break;
            case TRACK_EVENT_CONTENT_PLAY:
                /*TrackerUtil.getInstance(context).track(TrackerEvent.content_play, requestParam,
                        Arrays.asList(PlatformType.FCM));*/
                break;
            case TRACK_EVENT_CONTENT_COMPLETED:
                /*TrackerUtil.getInstance(context).track(TrackerEvent.content_completed, requestParam,
                        Arrays.asList(PlatformType.FCM));*/
                break;
            case TRACK_EVENT_CONTENT_EXIT:
                /*TrackerUtil.getInstance(context).track(TrackerEvent.content_exit, requestParam,
                        Arrays.asList(PlatformType.FCM));*/
                break;
            case TRACK_EVENT_SHARE_CONTENT:
                /*TrackerUtil.getInstance(context).track(TrackerEvent.share_content, requestParam,
                        Arrays.asList(PlatformType.FCM));*/
                break;
            case TRACK_EVENT_ADD_TO_WATCHLIST:
                /*TrackerUtil.getInstance(context).track(TrackerEvent.add_to_watchlist, requestParam,
                        Arrays.asList(PlatformType.FCM));*/
                break;
            case TRACK_EVENT_REMOVE_WATCHLIST:
                /*TrackerUtil.getInstance(context).track(TrackerEvent.remove_watchlist, requestParam,
                        Arrays.asList(PlatformType.FCM));*/
                break;
            case TRACK_EVENT_SEARCH:
                /*TrackerUtil.getInstance(context).track(TrackerEvent.search, requestParam,
                        Arrays.asList(PlatformType.FCM));*/
                break;
            case TRACK_EVENT_SIGN_IN_SUCCESS:
                /*TrackerUtil.getInstance(context).track(TrackerEvent.sign_in_success, requestParam,
                        Arrays.asList(PlatformType.FCM));*/
                break;
            case TRACK_EVENT_SIGN_UP_SUCCESS:
                /*TrackerUtil.getInstance(context).track(TrackerEvent.sign_up_success, requestParam,
                        Arrays.asList(PlatformType.FCM));*/
                break;
            case TRACK_EVENT_LOGOUT:
                /*TrackerUtil.getInstance(context).track(TrackerEvent.logout, requestParam,
                        Arrays.asList(PlatformType.FCM));*/
                break;
            case TRACK_EVENT_SETTINGS_VIDEO_QUALITY:
                /*TrackerUtil.getInstance(context).track(TrackerEvent.settings_video_quality, requestParam,
                        Arrays.asList(PlatformType.FCM));*/
                break;


            default:
                break;
        }
    }

    public FCMEvents setContext(Context context) {
        this.context=context;
        return this;
    }
}