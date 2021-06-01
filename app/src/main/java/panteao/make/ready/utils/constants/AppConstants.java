package panteao.make.ready.utils.constants;

public interface AppConstants {
    String MY_MVHUB_ENCRYPTION_KEY="MYMVHUB$KEY";
    String ENVEU_CONFIG = "enveuConfig";
    String IMAGE_TYPE_THUMBNAIL = "THUMBNAIL";
    String IMAGE_TYPE_POSTER = "POSTER";
    String APP_CONTINUE_WATCHING = "CONTINUE WATCHING";

    String EMAIL_REGEX = "[A-Z0-9a-z._%+-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,64}";
    String PLATFORM = "MOBILE";
    String PROFILE_FOLDER = "/180x180/filters:format(jpeg):max_bytes(400):quality(100)/app/user/profilePicture/";
    String PROFILE_URL = "https://";
    String GENRE_IMAGES_BASE_KEY = "/cms/images/taxonomy/genre/";
    String CAST_CREW_IMAGES_BASE_KEY = "/cms/images/taxonomy/castCrew/";
    String SERIES_IMAGES_BASE_KEY = "/cms/images/library/series/";
    String VIDEO_IMAGE_BASE_KEY = "/cms/images/library/video/";
    String SEASON_IMAGES_BASE_KEY = "/cms/images/library/season/";
    String VIDEO_CLOUD_FRONT_URL = "https://d228i5jh7v5l96.cloudfront.net/";
    String FILTER_PLAYER_BANNER = "/640x360/filters:format(jpeg):quality(100)";
    String FILTER = "/filters:";
    String LINE_URI = "https://line.me/R/oaMessage/@mvhub";


    //  private static String hlsvideouri = "https://d228i5jh7v5l96.cloudfront.net/deval_dabeli/d5369610-7f07-dc54-71c3-56ca1b972a25.m3u8";
    String QUALITY = "quality";
    String VOD = "VIDEO";
    String SERIES = "SERIES";
    String CAST_AND_CREW = "CAST_AND_CREW";
    String GENRE = "GENRE";
    String SQUARE = "SQUARE";
    String CIRCLE = "CIRCLE";
    String LANDSCAPE = "WIDE_SCREEN_LANDSCAPE";
    String POTRAIT = "WIDE_SCREEN_PORTRAIT";
    String POSTER_LANDSCAPE = "POSTER_LANDSCAPE";
    String POSTER_POTRAIT = "POSTER_PORTRAIT";

    String Movie = "MOVIE";
    String Show = "SHOW";
    String Season = "SEASON";
    String Episode = "EPISODE";
    String Series = "SERIES";
    String Video = "VIDEO";
    String Article = "ARTICLE";

    /***************
     * API PARAM KEYS
     ******/
    String API_PARAM_NAME = "name";
    String API_PARAM_EMAIL = "email";
    String API_PARAM_PASSWORD = "password";
    String API_PARAM_STATUS = "status";
    String API_PARAM_PROFILE_PIC = "profilePicURL";
    String API_PARAM_GENDER = "gender";
    String API_PARAM_DOB = "dateOfBirth";
    String API_PARAM_IS_VERIFIED = "verified";
    String API_PARAM_VERIFICATION_DATE = "verificationDate";
    String API_PARAM_PHONE_NUMBER = "phoneNumber";
    String API_PARAM_EXPIRY_DATE = "expiryDate";
    String API_PARAM_PROFILE_STEP = "profileStep";
    String API_PARAM_NEW_PWD = "newPassword";
    String API_PARAM_FB_ID = "fbId";
    String API_PARAM_EMAIL_ID = "emailId";
    String API_PARAM_FB_TOKEN = "accessToken";
    String API_PARAM_IS_FB_EMAIL = "fbMail";
    String API_PARAM_FB_PIC = "profilePicUrl";
    String API_PARAM_CONTENT_ID = "contentId";
    String API_PARAM_CONTENT_TYPE = "contentType";
    String API_PARAM_WATCHLIST_ID = "watchListItemId";
    String API_PARAM_LIKE_ID = "id";
    String API_PARAM_LIKE_TYPE = "type";
    String API_PARAM_COMMENT_TEXT = "commentText";
    String API_PARAM_PAGE_NO = "pageNo";
    String API_PARAM_PAGE = "page";
    String API_PARAM_SIZE = "size";
    String API_PARAM_PAGE_SIZE = "pageSize";
    String API_PARAM_SERIES_ID = "seriesId";
    String API_PARAM_SEASON_ID = "seasonId";
    String API_PARAM_DURATION = "duration";
    String API_PARAM_POSITION = "position";
    String API_PARAM_TYPE = "type";
    String API_PARAM_ID = "id";
    String API_PARAM_FETCH_DATA = "fetchData";
    String API_RESPONSE_CODE = "responseCode";
    String SOMETHING_WENT_WRONG = "serverError";
    String API_PARAM_USER_ASSET_LIST_DTO = "userAssetListDTOList";
    /***************
     * API RESPONSES
     ******/
    String RESPONSE_CODE_SIGNUP = "2001";
    String RESPONSE_CODE_SIGNUP_ALREADY = "409";
    int RESPONSE_CODE_REGISTER = 200;
    int RESPONSE_CODE_LOGOUT=401;
    int RESPONSE_CODE_ERROR = 500;
    int REQUEST_PICK_IMAGE = 1003;
    int REQUEST_CODE_CAMERA = 1002;
    int CAMERA_INTENT = 1;
    int SELECT_PIC = 2;
    int RESPONSE_CODE_SUCCESS = 2000;
    /***************
     * INTENT CONSTANTS
     ******/
    String STRING_USER_DETAIL = "registeredUserDetail";
    String EXTRA_REGISTER_USER = "extraRegisterUser";
    String EXTRA_SAVED_LAYOUT_MANAGER = "recyclerSavedState";
    /***************
     * OTHER STRINGS
     ******/

    String GENDER_MALE = "MALE";
    String GENDER_FEMALE = "FEMALE";
    String PROFILE_SETUP = "STEP_1";
    String UNPUBLISHED = "UNPUBLISHED";
    String PUBLISHED = "PUBLISHED";
    /***************
     * PREF STRINGS
     ******/
    String APP_PREF_PROFILE = "profileDetail";
    String APP_PREF_CONFIG = "appConfig";
    String APP_PREF_ACCESS_TOKEN = "accessToken";
    String APP_PREF_LOGIN_STATUS = "loginStatus";
    String APP_PREF_GET_USER = "geLtUser";
    String APP_PREF_LOGIN_TYPE = "userLoginType";
    String APP_PREF_USER_ID = "userId";
    String APP_PREF_AVAILABLE_VERSION = "availableVersion";
    String APP_PREF_CFEP = "cloudFrontEndpoint";
    String APP_PREF_VIDEO_URL = "cloudFrontVideoUrl";
    String APP_PREF_CONFIG_VERSION = "configVersion";
    String APP_PREF_SERVER_BASE_URL = "serverBaseURL";
    String APP_PREF_IS_VERIFIED = "isVerified";
    String APP_PREF_CONFIG_RESPONSE = "Config_Response";
    String APP_PREF_LAST_CONFIG_HIT = "LastConfigTime";
    String APP_PREF_JUMP_BACK = "returnBack";
    String APP_PREF_IS_EPISODE = "isEpisode";
    String APP_PREF_JUMP_BACK_ID = "returnBackId";
    String APP_PREF_JUMP_TO = "returnTo";
    String APP_PREF_VIDEO_POSITION = "videoPosition";
    String APP_PREF_GOTO_PURCHASE = "returnPurchase";
    String APP_PREF_BRANCH_IO = "returnBack";
    String APP_PREF_IS_PLAYING = "playerPlaying";
    String APP_PREF_ASSET_ID = "assetId";
    String APP_PREF_SELECTED_SEASON_ID = "seasonId";
    String APP_PREF_HAS_SELECTED_ID = "hasSelectedId";
    String APP_PREF_IS_RESTORE_STATE = "isRestoreState";
    /***************
     * EnveuSDK Keys
     ******/

    //String OVP_BASE_URL = "https://app.beta.enveu.com/app/api/v1/";


    String HOME_ENVEU = "0";
    String ORIGINAL_ENVEU = "1";
    String PREMIUM_ENVEU = "2";
    String SINETRON_ENVEU = "3";
    String EPISODE_ENVEU = "4";
    String SHOW_ENVEU = "5";
    String MOVIE_ENVEU = "6";
    String SERIES_ENVEU = "7";
    /*******
     *Layout Constants
     ******/
    String WIDGET_TYPE_AD = "ADS";
    String WIDGET_TYPE_CONTENT = "CNT";
    String KEY_MREC = "MREC";
    String KEY_BANNER = "BANNER";
    String KEY_CUS = "CUS";
    int CAROUSEL_LDS_LANDSCAPE = 10;
    int CAROUSEL_LDS_BANNER = 11;
    int CAROUSEL_PR_POTRAIT = 12;
    int CAROUSEL_PR_POSTER = 13;
    int CAROUSEL_SQR_SQUARE = 14;
    int CAROUSEL_CIR_CIRCLE = 15;
    int CAROUSEL_CST_CUSTOM = 16;
    int HERO_LDS_LANDSCAPE = 20;
    int HERO_LDS_BANNER = 21;
    int HERO_PR_POTRAIT = 22;
    int HERO_PR_POSTER = 23;
    int HERO_SQR_SQUARE = 24;
    int HERO_CIR_CIRCLE = 25;
    int HERO_CST_CUSTOM = 26;
    int HORIZONTAL_LDS_LANDSCAPE = 31;
    int HORIZONTAL_LDS_BANNER = 32;
    int HORIZONTAL_PR_POTRAIT = 33;
    int HORIZONTAL_PR_POSTER = 34;
    int HORIZONTAL_SQR_SQUARE = 35;
    int HORIZONTAL_CIR_CIRCLE = 36;
    int HORIZONTAL_CST_CUSTOM = 37;

    int ADS_BANNER = 41;
    int ADS_MREC = 42;
    int ADS_CUS = 43;
    String BUNDLE_VIDEO_ID_BRIGHTCOVE = "videoId";
    String BUNDLE_TAB_ID = "tabId";
    String BUNDLE_ASSET_ID = "assestId";
    String BUNDLE_SERIES_ID = "seriesId";
    String BUNDLE_IS_PREMIUM = "isPremium";
    String BUNDLE_DURATION = "duration";
    String BUNDLE_ASSET_BUNDLE = "assestIdBundle";
    String WEB_VIEW_HEADING = "WebViewHeading";
    String WEB_VIEW_URL = "WebVieweURl";
    String BUNDLE_ID_FOR_COMMENTS = "commentId";
    String BUNDLE_BANNER_IMAGE = "bannerImage";
    String BUNDLE_TYPE_FOR_COMMENTS = "type";
    String BUNDLE_SEASON_COUNT="seasonCount";
    String BUNDLE_SEASON_ARRAY="seasonArray";

    String BUNDLE_SELECTED_SEASON="selectedSeasonId";
    String BUNDLE_DETAIL_TYPE = "detailType";
    String BUNDLE_SERIES_DETAIL = "seriesDetail";
    String BOOKMARK_POSITION = "bookmarkPosition";
    String ASSETTYPE = "assetType";
    String PLAYER_ASSET_TITLE = "playerAssetTitle";
    String PLAYER_ASSET_MEDIATYPE = "playerMediaType";
    int PAGE_SIZE = 20;
    String BUNDLE_ASSET_TYPE = "assetType";
    String BUNDLE_CURRENT_ASSET_ID = "currentAssetId";

    enum UserLoginType {
        Manual,
        FbLogin,
        GoogleLogin
    }

    enum ContentType {
        VIDEO,
        MOVIE,
        SHOW,
        EPISODE,
        SERIES,
        SEASON,
        VOD,
        CONTINUE_WATCHING,
        MY_WATCHLIST,
        LIVE,
        ARTICLE;
    }

    enum ApiManagerType {
        OVP,
        SUBSCRIPTION
    }
    int BOOKMARK_INTERVAL= 30000; // 30 sec


    String USER_NAME="name";
    String USER_EMAIL="email";

    public static String LIGHT_THEME = "LightTheme";
    public static String DARK_THEME = "DarkTheme";
    public static final String THEME = "Theme";


    String FCM_TOKEN = "fcm_token";


  String GALLERY_SELECT="gallery_select";
  String CONTENT_SELECT="content_select";
  String CONTENT_PLAY="content_play";
  String CONTENT_COMPLETED="content_completed";
  String CONTENT_EXIT="content_exit";
  String SHARE_CONTENT="share_content";
  String ADD_TO_WATCHLIST="add_to_watchlist";
  String REMOVE_WATCHLIST="remove_watchlist";
  String SEARCH ="search";
  String SIGN_IN_SUCCESS ="sign_in_success";
  String SIGN_UP_SUCCESS ="sign_up_success";
  String LOGOUT="logout";
  String SETTINGS_VIDEO_QUALITY="settings_video_quality";

    int TRACK_EVENT_FIREBASE_SCREEN = 6;
   int TRACK_EVENT_GALLERY_SELECT = 7;
    int TRACK_EVENT_CONTENT_SELECT = 8;
    int TRACK_EVENT_CONTENT_PLAY = 9;
    int TRACK_EVENT_CONTENT_COMPLETED = 10;
    int TRACK_EVENT_CONTENT_EXIT=11;
    int TRACK_EVENT_SHARE_CONTENT=12;
    int TRACK_EVENT_ADD_TO_WATCHLIST=13;
    int TRACK_EVENT_REMOVE_WATCHLIST=14;
    int TRACK_EVENT_SEARCH =15;
    int TRACK_EVENT_SIGN_IN_SUCCESS =16;
    int TRACK_EVENT_SIGN_UP_SUCCESS =17;
    int TRACK_EVENT_LOGOUT=18;
    int TRACK_EVENT_SETTINGS_VIDEO_QUALITY=19;

    String MAIN_HOME="Main - Home";
    String MAIN_TOPHITS="Main – Top Hits";
    String MAIN_COMINGSOON="Main – Coming Soon";
    String MAIN_LIVETV="Main – Live TV";
    String MAIN_MORE="Main - More";


// FireBaseLinks
String FIREBASE_DPLNK_URL = "https://www.panteao.com/";
    String FIREBASE_DPLNK_PREFIX = "https://panteaoandroid.page.link/";
   String FIREBASE_IOS_PACKAGE = "com.panteaoproductions.mobile.dev";
  String FIREBASE_ANDROID_PACKAGE = "panteao.make.ready.dev";

    String WIDTH = "/width/";
    String HEIGHT = "/height/";
    String QUALITY_IMAGE = "/quality/100";

    String TAB_ID = "tabId";


}
