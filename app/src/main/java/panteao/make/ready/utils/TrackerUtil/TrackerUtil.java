package panteao.make.ready.utils.TrackerUtil;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;

import panteao.make.ready.tarcker.EventConstant;
import panteao.make.ready.utils.cropImage.helpers.Logger;
import panteao.make.ready.utils.cropImage.helpers.PrintLogging;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.initialization.InitializationStatus;
import com.google.android.gms.ads.initialization.OnInitializationCompleteListener;
import com.google.android.gms.analytics.GoogleAnalytics;
import com.google.firebase.analytics.FirebaseAnalytics;
import com.google.gson.JsonObject;

import java.util.List;

import io.branch.referral.Branch;
import panteao.make.ready.tarcker.EventConstant;
import panteao.make.ready.utils.cropImage.helpers.Logger;
import panteao.make.ready.utils.cropImage.helpers.PrintLogging;

public class TrackerUtil {
    public static GoogleAnalytics sAnalytics;

    private static TrackerUtil trackerUtil = null;
    Context context;

    private TrackerUtil(Context context) {
        if (context != null) {
            this.context=context;

            Branch.enableLogging();

            // Branch object initialization
            Branch.getAutoInstance(context);
            sAnalytics = GoogleAnalytics.getInstance(context);
            Branch.getInstance().initSession((referringParams, error) -> Logger.d("Branch", "onInitFinished() with deep link data: " + referringParams));
//            MobileAds.initialize(context, "ca-app-pub-3940256099942544~3347511713");
            MobileAds.initialize(context, new OnInitializationCompleteListener() {
                @Override
                public void onInitializationComplete(InitializationStatus initializationStatus) {
                }
            });
            Logger.d("sdkInitialzed", "successfull");


        }
    }

    public static TrackerUtil getInstance(Context context) {
        if (trackerUtil == null) {
            trackerUtil = new TrackerUtil(context);
        }
        return trackerUtil;
    }

    public static GoogleAnalytics getAnalyticsInstance() {
        return sAnalytics;
    }

    public void track(TrackerEvent login, JsonObject name, List<PlatformType> trackingPlatforms) {
        switch (login) {
            case REGISTER:
//                trackRegisterEvent(name, trackingPlatforms);
                break;
            case CLICK_CONTENT:
//                trackClickContentEvent(name, trackingPlatforms);
                break;
            case PLAY_CONTENT:
//                trackPlayContentEvent(name, trackingPlatforms);
                break;
            case SEARCH:
//                trackSearchEvent(name, trackingPlatforms);
                break;
            case LOGIN:
//                trackLoginEvent(name, trackingPlatforms);
                break;
            case screen_track:
                trackfirebaseScreenEvent(name,trackingPlatforms);
                break;
            case gallery_select:
                trackGallerySelectEvent(name,trackingPlatforms);
                break;
            case content_select:
                trackContentSelectEvent(name,trackingPlatforms);
                break;
            case content_play:
                trackContentPlayEvent(name,trackingPlatforms);
                break;
            case content_completed:
                trackContentCompletedEvent(name,trackingPlatforms);
                break;
            case content_exit:
                trackContentExitEvent(name,trackingPlatforms);
                break;
            case share_content:
                trackShareContentEvent(name,trackingPlatforms);
                break;
            case add_to_watchlist:
                trackAddToWatchlistEvent(name,trackingPlatforms);
                break;
            case remove_watchlist:
                trackRemoveWatchlistEvent(name,trackingPlatforms);
                break;
            case search:
                trackSearchFCMEvent(name,trackingPlatforms);
                break;
            case sign_in_success:
                trackSignInSuccessEvent(name,trackingPlatforms);
                break;
            case sign_up_success:
                trackSignUpSuccessEvent(name,trackingPlatforms);
                break;
            case logout:
                tracklogoutEvent(name,trackingPlatforms);
                break;
            case settings_video_quality:
                trackVideoQualityEvent(name,trackingPlatforms);
                break;

            case DETAIL_VISIT:
                break;

            default:

        }
    }

    private void trackVideoQualityEvent(JsonObject params, List<PlatformType> trackingPlatforms) {
        for (PlatformType tracking : trackingPlatforms) {
            switch (tracking) {
                case GTM:
                    Logger.d("EventTracked", "GTM");
                    break;
                case GCM:
                    break;
                case MOENGAGE:
                    break;
                case FCM:
                    try {
//                        String action_screen = params.get(EventConstant.ActionScreen).toString();
                        String video_quality = params.get(EventConstant.VideoQuality).toString();
//                        PrintLogging.printLog("", "ValueForFcm-->>" + name+"  "+contentType);
                        FirebaseAnalytics mFirebaseAnalytics = FirebaseAnalytics.getInstance(context);
                        Bundle bundle = new Bundle();
//                        bundle.putString(EventConstant.ActionScreen,action_screen);
                        bundle.putString(EventConstant.VideoQuality, video_quality);


                        Log.d("VideoQualityEvent",bundle.toString());
//                        Log.d("Action_screen",action_screen);
                        Log.d("Video_quality",video_quality);



                        mFirebaseAnalytics.logEvent(TrackerEvent.settings_video_quality.name(), bundle);

                    } catch (Exception ignored) {

                    }
                    break;
                default:
            }
        }
    }

    private void tracklogoutEvent(JsonObject params, List<PlatformType> trackingPlatforms) {
        for (PlatformType tracking : trackingPlatforms) {
            switch (tracking) {
                case GTM:
                    Logger.d("EventTracked", "GTM");
                    break;
                case GCM:
                    break;
                case MOENGAGE:
                    break;
                case FCM:
                    try {
//                        String action_screen = params.get(EventConstant.ActionScreen).toString();

//                        PrintLogging.printLog("", "ValueForFcm-->>" + name+"  "+contentType);
                        FirebaseAnalytics mFirebaseAnalytics = FirebaseAnalytics.getInstance(context);
                        Bundle bundle = new Bundle();
//                        bundle.putString(EventConstant.ActionScreen,action_screen);

                        Log.d("logoutEvent",bundle.toString());
//                        Log.d("Action_screen",action_screen);


                        mFirebaseAnalytics.logEvent(TrackerEvent.logout.name(), bundle);

                    } catch (Exception ignored) {

                    }
                    break;
                default:
            }
        }
    }

    private void trackSignUpSuccessEvent(JsonObject params, List<PlatformType> trackingPlatforms) {
        for (PlatformType tracking : trackingPlatforms) {
            switch (tracking) {
                case GTM:
                    Logger.d("EventTracked", "GTM");
                    break;
                case GCM:
                    break;
                case MOENGAGE:
                    break;
                case FCM:
                    try {
                        String user_id = params.get(EventConstant.UserID).toString();
                        String user_name = params.get(EventConstant.Username).toString();
//                        PrintLogging.printLog("", "ValueForFcm-->>" + name+"  "+contentType);
                        FirebaseAnalytics mFirebaseAnalytics = FirebaseAnalytics.getInstance(context);
                        Bundle bundle = new Bundle();
//                        bundle.putString(EventConstant.ActionScreen,action_screen);
//                        bundle.putString(EventConstant.UserID,user_id);
//                        bundle.putString(EventConstant.Username,user_name);
                        Log.d("SignUpSuccess",bundle.toString());
//                        Log.d("Action_screen",action_screen);
                        mFirebaseAnalytics.logEvent(TrackerEvent.sign_up_success.name(), bundle);
                        mFirebaseAnalytics.setUserId(user_id);
                        mFirebaseAnalytics.setUserProperty("username",user_name);


                    } catch (Exception ignored) {

                    }
                    break;
                default:
            }
        }
    }

    private void trackSignInSuccessEvent(JsonObject params, List<PlatformType> trackingPlatforms) {
        for (PlatformType tracking : trackingPlatforms) {
            switch (tracking) {
                case GTM:
                    Logger.d("EventTracked", "GTM");
                    break;
                case GCM:
                    break;
                case MOENGAGE:
                    break;
                case FCM:
                    try {
                        String user_id = params.get(EventConstant.UserID).toString();
                        String user_name = params.get(EventConstant.Username).toString();

//                        PrintLogging.printLog("", "ValueForFcm-->>" + name+"  "+contentType);
                        FirebaseAnalytics mFirebaseAnalytics = FirebaseAnalytics.getInstance(context);
                        Bundle bundle = new Bundle();
//                        bundle.putString(EventConstant.UserID,user_id);
//                        bundle.putString(EventConstant.Username,user_name);
                        Log.d("SignInSuccess",bundle.toString());
//                        Log.d("Action_screen",action_screen);
                        mFirebaseAnalytics.logEvent(TrackerEvent.sign_in_success.name(), bundle);
                        mFirebaseAnalytics.setUserId(user_id);
                        mFirebaseAnalytics.setUserProperty("username",user_name);


                    } catch (Exception ignored) {

                    }
                    break;
                default:
            }
        }

    }

    private void trackSearchFCMEvent(JsonObject params, List<PlatformType> trackingPlatforms) {
        for (PlatformType tracking : trackingPlatforms) {
            switch (tracking) {
                case GTM:
                    Logger.d("EventTracked", "GTM");
                    break;
                case GCM:
                    break;
                case MOENGAGE:
                    break;
                case FCM:
                    try {
//                        String action_screen = params.get(EventConstant.ActionScreen).toString();
                        String search_term =params.get(EventConstant.SearchTerm).toString();

//                        PrintLogging.printLog("", "ValueForFcm-->>" + name+"  "+contentType);
                        FirebaseAnalytics mFirebaseAnalytics = FirebaseAnalytics.getInstance(context);
                        Bundle bundle = new Bundle();
//                        bundle.putString(EventConstant.ActionScreen,action_screen);

                        bundle.putString(EventConstant.SearchTerm,search_term);

                        Log.d("SearchFCMEvent",bundle.toString());
//                        Log.d("result_screen",action_screen);


                        mFirebaseAnalytics.logEvent(TrackerEvent.search.name(), bundle);

                    } catch (Exception ignored) {

                    }
                    break;
                default:
            }
        }
    }

    private void trackRemoveWatchlistEvent(JsonObject params, List<PlatformType> trackingPlatforms) {
        for (PlatformType tracking : trackingPlatforms) {
            switch (tracking) {
                case GTM:
                    Logger.d("EventTracked", "GTM");
                    break;
                case GCM:
                    break;
                case MOENGAGE:
                    break;
                case FCM:
                    try {
//                        String action_screen = params.get(EventConstant.ActionScreen).toString();
                        String content_type =params.get(EventConstant.Content_Type).toString();
                        String  content_id =params.get(EventConstant.ContentId).toString();
                        String content_title=params.get(EventConstant.ContentTitle).toString();

//                        PrintLogging.printLog("", "ValueForFcm-->>" + name+"  "+contentType);
                        FirebaseAnalytics mFirebaseAnalytics = FirebaseAnalytics.getInstance(context);
                        Bundle bundle = new Bundle();
//                        bundle.putString(EventConstant.ActionScreen,action_screen);
                        bundle.putString(EventConstant.Content_Type, content_type);
                        bundle.putString(EventConstant.ContentId,content_id);
                        bundle.putString(EventConstant.ContentTitle,content_title);

                        Log.d("RemoveWatchlist",bundle.toString());
                        Log.d("content_type",content_type);
//                        Log.d("result_screen",action_screen);


                        mFirebaseAnalytics.logEvent(TrackerEvent.remove_watchlist.name(), bundle);

                    } catch (Exception ignored) {

                    }
                    break;
                default:
            }
        }
    }

    private void trackAddToWatchlistEvent(JsonObject params, List<PlatformType> trackingPlatforms) {
        for (PlatformType tracking : trackingPlatforms) {
            switch (tracking) {
                case GTM:
                    Logger.d("EventTracked", "GTM");
                    break;
                case GCM:
                    break;
                case MOENGAGE:
                    break;
                case FCM:
                    try {
//                        String action_screen = params.get(EventConstant.ActionScreen).toString();
                        String content_type =params.get(EventConstant.Content_Type).toString();
                        String  content_id =params.get(EventConstant.ContentId).toString();
                        String content_title=params.get(EventConstant.ContentTitle).toString();

//                        PrintLogging.printLog("", "ValueForFcm-->>" + name+"  "+contentType);
                        FirebaseAnalytics mFirebaseAnalytics = FirebaseAnalytics.getInstance(context);
                        Bundle bundle = new Bundle();
//                        bundle.putString(EventConstant.ActionScreen,action_screen);
                        bundle.putString(EventConstant.Content_Type, content_type);
                        bundle.putString(EventConstant.ContentId,content_id);
                        bundle.putString(EventConstant.ContentTitle,content_title);

                        Log.d("AddToWatchlist",bundle.toString());
                        Log.d("content_type",content_type);
//                        Log.d("result_screen",action_screen);


                        mFirebaseAnalytics.logEvent(TrackerEvent.add_to_watchlist.name(), bundle);

                    } catch (Exception ignored) {

                    }
                    break;
                default:
            }
        }
    }

    private void trackShareContentEvent(JsonObject params, List<PlatformType> trackingPlatforms) {
        for (PlatformType tracking : trackingPlatforms) {
            switch (tracking) {
                case GTM:
                    Logger.d("EventTracked", "GTM");
                    break;
                case GCM:
                    break;
                case MOENGAGE:
                    break;
                case FCM:
                    try {
//                        String action_screen = params.get(EventConstant.ActionScreen).toString();
                        String content_type =params.get(EventConstant.Content_Type).toString();
                        String  content_id =params.get(EventConstant.ContentId).toString();
                        String content_title=params.get(EventConstant.ContentTitle).toString();

//                        PrintLogging.printLog("", "ValueForFcm-->>" + name+"  "+contentType);
                        FirebaseAnalytics mFirebaseAnalytics = FirebaseAnalytics.getInstance(context);
                        Bundle bundle = new Bundle();
//                        bundle.putString(EventConstant.ActionScreen,action_screen);
                        bundle.putString(EventConstant.Content_Type, content_type);
                        bundle.putString(EventConstant.ContentId,content_id);
                        bundle.putString(EventConstant.ContentTitle,content_title);

                        Log.d("ShareContent",bundle.toString());
                        Log.d("content_type",content_type);
//                        Log.d("result_screen",action_screen);


                        mFirebaseAnalytics.logEvent(TrackerEvent.share_content.name(), bundle);

                    } catch (Exception ignored) {

                    }
                    break;
                default:
            }
        }
    }

    private void trackContentExitEvent(JsonObject params, List<PlatformType> trackingPlatforms) {
        for (PlatformType tracking : trackingPlatforms) {
            switch (tracking) {
                case GTM:
                    Logger.d("EventTracked", "GTM");
                    break;
                case GCM:
                    break;
                case MOENGAGE:
                    break;
                case FCM:
                    try {
//                        String action_screen = params.get(EventConstant.ActionScreen).toString();
                        String content_type =params.get(EventConstant.Content_Type).toString();
                        String  content_id =params.get(EventConstant.ContentId).toString();
                        String content_played =params.get(EventConstant.ContentPlayed).toString();
                        String content_duration =params.get(EventConstant.ContentDuration).toString();
                        String content_title=params.get(EventConstant.ContentTitle).toString();

//                        PrintLogging.printLog("", "ValueForFcm-->>" + name+"  "+contentType);
                        FirebaseAnalytics mFirebaseAnalytics = FirebaseAnalytics.getInstance(context);
                        Bundle bundle = new Bundle();
//                        bundle.putString(EventConstant.ActionScreen,action_screen);
                        bundle.putString(EventConstant.Content_Type, content_type);
                        bundle.putString(EventConstant.ContentId,content_id);
                        bundle.putString(EventConstant.ContentPlayed, content_played);
                        bundle.putString(EventConstant.ContentDuration,content_duration);
                        bundle.putString(EventConstant.ContentTitle,content_title);

                        Log.d("ContentExit",bundle.toString());
                        Log.d("content_type",content_type);
//                        Log.d("result_screen",action_screen);


                        mFirebaseAnalytics.logEvent(TrackerEvent.content_exit.name(), bundle);

                    } catch (Exception ignored) {

                    }
                    break;
                default:
            }
        }
    }

    private void trackContentCompletedEvent(JsonObject params, List<PlatformType> trackingPlatforms) {
        for (PlatformType tracking : trackingPlatforms) {
            switch (tracking) {
                case GTM:
                    Logger.d("EventTracked", "GTM");
                    break;
                case GCM:
                    break;
                case MOENGAGE:
                    break;
                case FCM:
                    try {
//                        String action_screen = params.get(EventConstant.ActionScreen).toString();
                        String content_type =params.get(EventConstant.Content_Type).toString();
                        String  content_id =params.get(EventConstant.ContentId).toString();
                        String content_played =params.get(EventConstant.ContentPlayed).toString();
                        String content_duration =params.get(EventConstant.ContentDuration).toString();
                        String content_title=params.get(EventConstant.ContentTitle).toString();

//                        PrintLogging.printLog("", "ValueForFcm-->>" + name+"  "+contentType);
                        FirebaseAnalytics mFirebaseAnalytics = FirebaseAnalytics.getInstance(context);
                        Bundle bundle = new Bundle();
//                        bundle.putString(EventConstant.ActionScreen,action_screen);
                        bundle.putString(EventConstant.Content_Type, content_type);
                        bundle.putString(EventConstant.ContentId,content_id);
                        bundle.putString(EventConstant.ContentPlayed, content_played);
                        bundle.putString(EventConstant.ContentDuration,content_duration);
                        bundle.putString(EventConstant.ContentTitle,content_title);

                        Log.d("trackContentCompleted",bundle.toString());
                        Log.d("content_type",content_type);
//                        Log.d("result_screen",action_screen);


                        mFirebaseAnalytics.logEvent(TrackerEvent.content_completed.name(), bundle);

                    } catch (Exception ignored) {

                    }
                    break;
                default:
            }
        }
    }

    private void trackContentPlayEvent(JsonObject params, List<PlatformType> trackingPlatforms) {
        for (PlatformType tracking : trackingPlatforms) {
            switch (tracking) {
                case GTM:
                    Logger.d("EventTracked", "GTM");
                    break;
                case GCM:
                    break;
                case MOENGAGE:
                    break;
                case FCM:
                    try {
//                        String action_screen = params.get(EventConstant.ActionScreen).toString();
                        String content_type =params.get(EventConstant.Content_Type).toString();
                        String  content_id =params.get(EventConstant.ContentId).toString();
                        String content_played =params.get(EventConstant.ContentPlayed).toString();
                        String content_duration =params.get(EventConstant.ContentDuration).toString();
                        String content_title=params.get(EventConstant.ContentTitle).toString();

//                        PrintLogging.printLog("", "ValueForFcm-->>" + name+"  "+contentType);
                        FirebaseAnalytics mFirebaseAnalytics = FirebaseAnalytics.getInstance(context);
                        Bundle bundle = new Bundle();
//                        bundle.putString(EventConstant.ActionScreen,action_screen);
                        bundle.putString(EventConstant.Content_Type, content_type);
                        bundle.putString(EventConstant.ContentId,content_id);
                        bundle.putString(EventConstant.ContentPlayed, content_played);
                        bundle.putString(EventConstant.ContentDuration,content_duration);
                        bundle.putString(EventConstant.ContentTitle,content_title);

                        Log.d("ContentPlayEvent",bundle.toString());
                        Log.d("content_type",content_type);
//                        Log.d("result_screen",action_screen);
                        Log.d("Contentduration",content_duration);



                        mFirebaseAnalytics.logEvent(TrackerEvent.content_play.name(), bundle);

                    } catch (Exception ignored) {

                    }
                    break;
                default:
            }
        }
    }

    private void trackContentSelectEvent(JsonObject params, List<PlatformType> trackingPlatforms) {
        for (PlatformType tracking : trackingPlatforms) {
            switch (tracking) {
                case GTM:
                    Logger.d("EventTracked", "GTM");
                    break;
                case GCM:
                    break;
                case MOENGAGE:
                    break;
                case FCM:
                    try {
//                        String action_screen = params.get(EventConstant.ActionScreen).toString();
                        String content_type =params.get(EventConstant.Content_Type).toString();
                        String  category_id =params.get(EventConstant.CategoryId).toString();
                        String category_name =params.get(EventConstant.CategoryName).toString();
                        String row_index =params.get(EventConstant.RowIndex).toString();
                        String display_index=params.get(EventConstant.DisplayIndex).toString();
                        String content_id =params.get(EventConstant.ContentId).toString();
                        String content_title=params.get(EventConstant.ContentTitle).toString();
//                        PrintLogging.printLog("", "ValueForFcm-->>" + name+"  "+contentType);
                        FirebaseAnalytics mFirebaseAnalytics = FirebaseAnalytics.getInstance(context);
                        Bundle bundle = new Bundle();
//                        bundle.putString(EventConstant.ActionScreen,action_screen);
                        bundle.putString(EventConstant.Content_Type, content_type);
                        bundle.putString(EventConstant.CategoryId, category_id);
                        bundle.putString(EventConstant.CategoryName, category_name);
                        bundle.putString(EventConstant.RowIndex, row_index);
                        bundle.putString(EventConstant.DisplayIndex, display_index);
                        bundle.putString(EventConstant.ContentId,content_id);
                        bundle.putString(EventConstant.ContentTitle,content_title);

                        Log.d("ContentSelect",bundle.toString());
                        Log.d("content_type",content_type);
//                        Log.d("result_screen",action_screen);


                        mFirebaseAnalytics.logEvent(TrackerEvent.content_select.name(), bundle);

                    } catch (Exception ignored) {

                    }
                    break;
                default:
            }
        }
    }

    private void trackGallerySelectEvent(JsonObject params, List<PlatformType> trackingPlatforms) {
        for (PlatformType tracking : trackingPlatforms) {
            switch (tracking) {
                case GTM:
                    Logger.d("EventTracked", "GTM");
                    break;
                case GCM:
                    break;
                case MOENGAGE:
                    break;
                case FCM:
                    try {
//                        String action_screen = params.get(EventConstant.ActionScreen).toString();
                        String content_type =params.get(EventConstant.Content_Type).toString();
                        String  category_id =params.get(EventConstant.CategoryId).toString();
                        String category_name =params.get(EventConstant.CategoryName).toString();
                        String row_index =params.get(EventConstant.RowIndex).toString();
//                        PrintLogging.printLog("", "ValueForFcm-->>" + name+"  "+contentType);
                        FirebaseAnalytics mFirebaseAnalytics = FirebaseAnalytics.getInstance(context);
                        Bundle bundle = new Bundle();
//                        bundle.putString(EventConstant.ActionScreen,action_screen);
                        bundle.putString(EventConstant.Content_Type, content_type);
                        bundle.putString(EventConstant.CategoryId, category_id);
                        bundle.putString(EventConstant.CategoryName, category_name);
                        bundle.putString(EventConstant.RowIndex, row_index);

                        Log.d("GallerySelect",bundle.toString());
                        Log.d("content_type",content_type);
//                        Log.d("result_screen",action_screen);


                        mFirebaseAnalytics.logEvent(TrackerEvent.gallery_select.name(), bundle);

                    } catch (Exception ignored) {

                    }
                    break;
                default:
            }
        }
    }

    private void trackfirebaseScreenEvent(JsonObject params, List<PlatformType> trackingPlatforms) {
        for (PlatformType tracking : trackingPlatforms) {
            switch (tracking) {
                case GTM:
                    Logger.d("EventTracked", "GTM");
                    break;
                case GCM:
                    break;
                case MOENGAGE:
                    break;
                case FCM:
                    try {
                        String screen_name = params.get(EventConstant.ActionScreen).toString();
//                        String contentType = params.get(EventConstant.ContentType).toString();
//                        PrintLogging.printLog("", "ValueForFcm-->>" + name+"  "+contentType);
                        FirebaseAnalytics mFirebaseAnalytics = FirebaseAnalytics.getInstance(context);
                        Bundle bundle = new Bundle();
                        bundle.putString(EventConstant.ActionScreen,screen_name);
//                        bundle.putString(EventConstant.ContentType, contentType);
                        Log.d("firebaseScreen",bundle.toString());

                        Log.d("result_screen",screen_name);
                        mFirebaseAnalytics.logEvent(TrackerEvent.screen_track.name(), bundle);


                    } catch (Exception ignored) {

                    }
                    break;
                default:
            }
        }
    }

    private void trackPlayContentEvent(JsonObject params, List<PlatformType> trackingPlatforms) {
        for (PlatformType tracking : trackingPlatforms) {
            switch (tracking) {
                case GTM:
                    Logger.d("EventTracked", "GTM");
                    break;
                case GCM:
                    break;
                case MOENGAGE:
                    break;
                case FCM:
                    try {
                        String name = params.get(EventConstant.Name).toString();
                        String contentType = params.get(EventConstant.ContentType).toString();
                        PrintLogging.printLog("", "ValueForFcm-->>" + name+"  "+contentType);
                        FirebaseAnalytics mFirebaseAnalytics = FirebaseAnalytics.getInstance(context);
                        Bundle bundle = new Bundle();
                        bundle.putString(EventConstant.Name, name);
                        bundle.putString(EventConstant.ContentType, contentType);
//                        mFirebaseAnalytics.logEvent(TrackerEvent.PLAY_CONTENT.name(), bundle);

                    } catch (Exception ignored) {

                    }
                    break;
                default:
            }
        }
    }

    private void trackSearchEvent(JsonObject params, List<PlatformType> trackingPlatforms) {
        for (PlatformType tracking : trackingPlatforms) {
            switch (tracking) {
                case GTM:
                    Logger.d("EventTracked", "GTM");
                    break;
                case GCM:
                    break;
                case MOENGAGE:
                    break;
                case FCM:
                    try {
                        String name = params.get(EventConstant.SearchTitle).toString();
                        PrintLogging.printLog("", "ValueForFcm-->>" + name+"  ");
                        FirebaseAnalytics mFirebaseAnalytics = FirebaseAnalytics.getInstance(context);
                        Bundle bundle = new Bundle();
                        bundle.putString(EventConstant.SearchTitle, name);
//                        mFirebaseAnalytics.logEvent(TrackerEvent.SEARCH.name(), bundle);

                    } catch (Exception ignored) {

                    }
                    break;
                default:
            }
        }

    }

    private void trackClickContentEvent(JsonObject params, List<PlatformType> trackingPlatforms) {
        for (PlatformType tracking : trackingPlatforms) {
            switch (tracking) {
                case GTM:
                    Logger.d("EventTracked", "GTM");
                    break;
                case GCM:
                    break;
                case MOENGAGE:
                    break;
                case FCM:
                    try {
                        String name = params.get(EventConstant.Name).toString();
                        String contentType = params.get(EventConstant.ContentType).toString();
                        PrintLogging.printLog("", "ValueForFcm-->>" + name+"  "+contentType);
                        FirebaseAnalytics mFirebaseAnalytics = FirebaseAnalytics.getInstance(context);
                        Bundle bundle = new Bundle();
                        bundle.putString(EventConstant.Name, name);
                        bundle.putString(EventConstant.ContentType, contentType);
//                        mFirebaseAnalytics.logEvent(TrackerEvent.CLICK_CONTENT.name(), bundle);

                    } catch (Exception ignored) {

                    }
                    break;
                default:
            }
        }
    }

    private void trackRegisterEvent(JsonObject params, List<PlatformType> trackingPlatforms) {
        for (PlatformType tracking : trackingPlatforms) {
            switch (tracking) {
                case GTM:
                    Logger.d("EventTracked", "GTM");
                    break;
                case GCM:
                    break;
                case MOENGAGE:
                    break;
                case FCM:
                    try{
                        String name=params.get(EventConstant.Name).toString();
                        String platformType=params.get(EventConstant.PlatformType).toString();
                        PrintLogging.printLog("","ValueForFcm-->>"+name);
                        FirebaseAnalytics mFirebaseAnalytics = FirebaseAnalytics.getInstance(context);
                        Bundle bundle = new Bundle();
                        bundle.putString(EventConstant.Name, name);
                        bundle.putString(EventConstant.PlatformType, platformType);
//                        mFirebaseAnalytics.logEvent(TrackerEvent.REGISTER.name(), bundle);

                    }catch (Exception ignored){

                    }
                    break;
                default:
            }
        }
    }

    private void trackLoginEvent(JsonObject params, List<PlatformType> trackingPlatforms) {

        for (PlatformType tracking : trackingPlatforms) {
            switch (tracking) {
                case GTM:
                    Logger.d("EventTracked", "GTM");
                    break;
                case GCM:
                    break;
                case MOENGAGE:
                    break;
                case FCM:
                    try{
                        String name=params.get(EventConstant.Name).toString();
                        String platformType=params.get(EventConstant.PlatformType).toString();
                        PrintLogging.printLog("","ValueForFcm-->>"+name+"  "+platformType);
                        FirebaseAnalytics mFirebaseAnalytics = FirebaseAnalytics.getInstance(context);
                        Bundle bundle = new Bundle();
                        bundle.putString(EventConstant.Name, name);
                        bundle.putString(EventConstant.PlatformType, platformType);
//                        mFirebaseAnalytics.logEvent(TrackerEvent.LOGIN.name(), bundle);

                    }catch (Exception ignored){

                    }
                    break;
                default:
            }
        }
    }

}