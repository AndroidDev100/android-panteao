package panteao.make.ready.utils.commonMethods;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.net.Uri;
import android.os.SystemClock;
import android.provider.Settings;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.appcompat.widget.PopupMenu;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.google.firebase.dynamiclinks.DynamicLink;
import com.google.firebase.dynamiclinks.FirebaseDynamicLinks;
import com.google.firebase.dynamiclinks.ShortDynamicLink;
import com.kaltura.playkit.player.PKAspectRatioResizeMode;
import com.kaltura.playkit.player.PKTracks;
import com.kaltura.playkit.player.VideoTrack;
import com.kaltura.playkit.providers.ovp.OVPMediaAsset;
import com.kaltura.tvplayer.KalturaOvpPlayer;
import com.kaltura.tvplayer.OVPMediaOptions;
import com.kaltura.tvplayer.OfflineManager;
import com.kaltura.tvplayer.PlayerInitOptions;
import com.make.baseCollection.baseCategoryModel.BaseCategory;
import com.make.enums.ImageType;
import com.make.enums.RailCardType;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.InstanceIdResult;

import panteao.make.ready.FirebaseConstants;
import panteao.make.ready.activities.KalturaPlayerActivity;
import panteao.make.ready.activities.instructor.ui.InstructorActivity;
import panteao.make.ready.activities.internalpages.CustomInternalPage;
import panteao.make.ready.activities.internalpages.TVCustomInternalPage;
import panteao.make.ready.activities.purchase.TVODENUMS;
import panteao.make.ready.activities.purchase.ui.VodOfferType;
import panteao.make.ready.activities.show.ui.EpisodeActivity;
import panteao.make.ready.activities.show.ui.ShowActivity;
import panteao.make.ready.activities.series.ui.SeriesDetailActivity;
import panteao.make.ready.activities.tutorial.ui.ChapterActivity;
import panteao.make.ready.activities.tutorial.ui.TutorialActivity;
import panteao.make.ready.baseModels.BaseActivity;
import panteao.make.ready.beanModel.configBean.ResponseConfig;
import panteao.make.ready.beanModel.entitle.RentalPeriod;
import panteao.make.ready.beanModel.enveuCommonRailData.RailCommonData;
import panteao.make.ready.beanModel.membershipAndPlan.DataItem;
import panteao.make.ready.beanModel.responseModels.landingTabResponses.CommonRailData;
import panteao.make.ready.beanModel.responseModels.landingTabResponses.railData.PlaylistRailData;
import panteao.make.ready.beanModelV3.uiConnectorModelV2.EnveuVideoItemBean;
import panteao.make.ready.beanModelV3.videoDetailsV2.EnveuVideoDetails;
import panteao.make.ready.beanModelV3.videoDetailsV2.EnveuVideoDetailsBean;
import panteao.make.ready.cms.CustomExternalPageWebview;
import panteao.make.ready.enums.DownloadStatus;
import panteao.make.ready.fragments.player.ui.UserInteractionFragment;
import panteao.make.ready.networking.responsehandler.ResponseModel;
import panteao.make.ready.player.tracks.TracksItem;
import panteao.make.ready.tarcker.EventConstant;
import panteao.make.ready.tarcker.FCMEvents;
import panteao.make.ready.tvBaseModels.basemodels.TVBaseActivity;
import panteao.make.ready.utils.MediaTypeConstants;
import panteao.make.ready.utils.config.bean.ConfigBean;
import panteao.make.ready.utils.constants.AppConstants;
import panteao.make.ready.utils.cropImage.helpers.Logger;
import panteao.make.ready.utils.cropImage.helpers.PrintLogging;
import panteao.make.ready.utils.helpers.ActivityTrackers;
import panteao.make.ready.utils.helpers.ImageHelper;
import panteao.make.ready.utils.helpers.RailInjectionHelper;
import panteao.make.ready.utils.helpers.StringUtils;
import panteao.make.ready.utils.helpers.carousel.model.Slide;
import panteao.make.ready.utils.helpers.downloads.db.DownloadItemEntity;
import panteao.make.ready.utils.helpers.intentlaunchers.ActivityLauncher;
import panteao.make.ready.utils.helpers.intentlaunchers.TvActivityLauncher;
import panteao.make.ready.utils.helpers.ksPreferenceKeys.KsPreferenceKeys;
import panteao.make.ready.R;
import panteao.make.ready.SDKConfig;

import com.google.android.material.tabs.TabLayout;
import com.google.gson.Gson;
import com.google.gson.JsonObject;

import org.jetbrains.annotations.NotNull;
import org.json.JSONObject;

import java.lang.ref.WeakReference;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.StringTokenizer;
import java.util.concurrent.TimeUnit;

import de.hdodenhof.circleimageview.CircleImageView;
import io.branch.indexing.BranchUniversalObject;
import io.branch.referral.util.LinkProperties;
import retrofit2.Response;

import static android.content.pm.PackageManager.FEATURE_TOUCHSCREEN_MULTITOUCH;
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


@SuppressWarnings({"IntegerDivisionInFloatingPointContext", "StatementWithEmptyBody"})
public class AppCommonMethod {
    public static final int ScreenWidth = 0;
    public static int ScreenHeight = 0;
    public static final List<CommonRailData> adsRail = new ArrayList<>();
    public static final int multiRequestLimit = 5;
    public static final RequestOptions options = new RequestOptions()
            .centerCrop()
            .placeholder(R.drawable.default_profile_pic)
            .error(R.drawable.default_profile_pic)
            .diskCacheStrategy(DiskCacheStrategy.NONE);
    public static final RequestOptions optionsSearch = new RequestOptions()
            .fitCenter()
            .placeholder(R.drawable.placeholder_landscape)
            .error(R.drawable.placeholder_landscape)
            .diskCacheStrategy(DiskCacheStrategy.NONE);
    public static final RequestOptions castOptions = new RequestOptions()
            .placeholder(R.drawable.placeholder_landscape)
            .error(R.drawable.placeholder_landscape)
            .diskCacheStrategy(DiskCacheStrategy.NONE);
    public static final RequestOptions optionsPlayer = new RequestOptions()
            .fitCenter()
            .diskCacheStrategy(DiskCacheStrategy.NONE);
    public static final String PASSWORD_PATTERN = "/^(?=.*[!&^%$#@()\\\\_+-])[A-Za-z0-9\\\\d!&^%$#@()\\\\_+-]{8,20}$/";
    public static boolean afterLogin = false;
    public static boolean hasContinueRail = false;
    public static String urlPoints = "";
    public static boolean isPurchase = false;
    public static int landscapeRailNo;
    public static int potraitRailNo;
    public static int squareRailfNo;
    public static String userId;
    public static boolean isDownloadDeleted = false;
    public static int isDownloadIndex = -1;
    public static boolean isOrientationChanged = false;
    public static boolean isResumeDetail = false;
    public static boolean isSeasonCount = false;
    public static boolean isSeriesPage = false;
    public static boolean isInternet = false;
    public static long currentDuration = 0;
    public static int typePlayerPlaying = 0;
    public static int assetId;
    public static int seriesId;
    public static int seasonId;
    public static String ClickedValue = "";
    public static long getTimeStampDOB = 0;
    private static String sharingURL = "";
    private static WeakReference<Activity> mActivity;
    private static long mLastClickTime = 0;
    public static ArrayList<String> occuranceList;

    public static long showDatePicker(final TextView viewToUpdate, Context context) {
        DatePickerDialog datePicker;
        final Calendar c = Calendar.getInstance();
        int mYear = c.get(Calendar.YEAR); // current year
        int mMonth = c.get(Calendar.MONTH); // current month
        int mDay = c.get(Calendar.DAY_OF_MONTH); // current day
        getTimeStampDOB = 0;

        // date picker dialog
        datePicker = new DatePickerDialog(context, AlertDialog.THEME_DEVICE_DEFAULT_DARK,
                (view, year, monthOfYear, dayOfMonth) -> {
                    String monthstr = "";
                    // String[] month = {"January", "February", "March", "April", "May", "June", "July", "August", "September", "October", "November", "December"};

                    String[] month = {"Jan", "Feb", "Mar", "Apr", "May", "Jun", "Jul", "Aug", "Sep", "Oct", "Nov", "Dec"};
                    if (monthOfYear == 0) {
                        monthstr = month[0];
                    } else if (monthOfYear == 1) {
                        monthstr = month[1];
                    } else if (monthOfYear == 2) {
                        monthstr = month[2];
                    } else if (monthOfYear == 3) {
                        monthstr = month[3];
                    } else if (monthOfYear == 4) {
                        monthstr = month[4];
                    } else if (monthOfYear == 5) {
                        monthstr = month[5];
                    } else if (monthOfYear == 6) {
                        monthstr = month[6];
                    } else if (monthOfYear == 7) {
                        monthstr = month[7];
                    } else if (monthOfYear == 8) {
                        monthstr = month[8];
                    } else if (monthOfYear == 9) {
                        monthstr = month[9];
                    } else if (monthOfYear == 10) {
                        monthstr = month[10];
                    } else if (monthOfYear == 11) {
                        monthstr = month[11];
                    }
                    // set day of month , month and year value in the edit text
                    String choosedMonth = "";
                    String choosedDate;
                    // String choosedDateFormat = dayOfMonth + "/" + (monthOfYear + 1) + "/" + year;

                    if (dayOfMonth < 10) {
                        choosedDate = "0" + dayOfMonth;
                    } else {
                        choosedDate = "" + dayOfMonth;
                    }
                    String choosedDateFormat = choosedDate + "/" + monthstr + "" + "/" + year;

                    Calendar calendarNEw = new GregorianCalendar(year, monthOfYear, dayOfMonth);
                    getTimeStampDOB = calendarNEw.getTimeInMillis();
                    viewToUpdate.setText(choosedDateFormat);
                }, mYear, mMonth, mDay);
        datePicker.getDatePicker().setMaxDate(System.currentTimeMillis());
        datePicker.show();
        return getTimeStampDOB;
    }

    public static int convertDpToPixel(int dp) {
        DisplayMetrics metrics = Resources.getSystem().getDisplayMetrics();
        int px = dp * (metrics.densityDpi / 160);
        return Math.round(px);
    }

    public static void setContinueWatching(LinearLayout continueView, ImageView playImage, ProgressBar progressBar, int total, int watched) {
        continueView.setVisibility(View.VISIBLE);
        playImage.setVisibility(View.VISIBLE);
        progressBar.setMax(total);
        progressBar.setProgress(watched);

    }


    public static void setContinueViews(LinearLayout premium, LinearLayout newRelease, boolean isPremium, boolean isNew) {
        if (isPremium)
            premium.setVisibility(View.VISIBLE);
        else
            premium.setVisibility(View.GONE);

        if (isNew)
            newRelease.setVisibility(View.VISIBLE);
        else
            newRelease.setVisibility(View.GONE);

    }


    public static String getImageUrl(String contentType, String shape) {
        String url = "", ImageFrontEndUrl, resolution = "";
        ImageFrontEndUrl = AppCommonMethod.urlPoints;
        if (shape.equalsIgnoreCase("LANDSCAPE")) {
            resolution = "/fit-in/640x360";
        } else if (shape.equalsIgnoreCase("SQUARE")) {
            resolution = "/fit-in/200x200";
        } else if (shape.equalsIgnoreCase("CIRCLE")) {
            resolution = "/fit-in/200x200";
        } else if (shape.equalsIgnoreCase("POTRAIT")) {
            resolution = "/fit-in/360x640";
        } else if (shape.equalsIgnoreCase("CROUSEL")) {
            resolution = "/fit-in/640x360";
        } else if (shape.equalsIgnoreCase("POSTER_POTRAIT")) {
            resolution = "/fit-in/360x640";
        } else if (shape.equalsIgnoreCase("POSTER_LANDSCAPE")) {
            resolution = "/fit-in/640x360";
        }
        if (contentType.equalsIgnoreCase(AppConstants.VOD)) {
            url = ImageFrontEndUrl + resolution + AppConstants.FILTER + AppConstants.QUALITY + "(100):format(webP):maxbytes(400)" + AppConstants.VIDEO_IMAGE_BASE_KEY;
        } else if (contentType.equalsIgnoreCase(AppConstants.SERIES)) {
            url = ImageFrontEndUrl + resolution + AppConstants.FILTER + AppConstants.QUALITY + "(100):format(webP):maxbytes(400)" + AppConstants.SERIES_IMAGES_BASE_KEY;
        } else if (contentType.equalsIgnoreCase(AppConstants.CAST_AND_CREW)) {
            url = ImageFrontEndUrl + resolution + AppConstants.FILTER + AppConstants.QUALITY + "(100):format(webP):maxbytes(400)" + AppConstants.CAST_CREW_IMAGES_BASE_KEY;

        } else if (contentType.equalsIgnoreCase(AppConstants.GENRE)) {
            url = ImageFrontEndUrl + resolution + AppConstants.FILTER + AppConstants.QUALITY + "(100):format(webP):maxbytes(400)" + AppConstants.GENRE_IMAGES_BASE_KEY;
        }

        return url;
    }

/*    public static void openShareDialog(Activity activity, String title, int assetId, String assetType, String imgUrl, String seriesId, String seasonNumber) {
        mActivity = new WeakReference<>(activity);
        new ToastHandler(mActivity.get()).show("Loading...");
        BranchUniversalObject buo = new BranchUniversalObject()
                .setTitle(title)
                .setContentIndexingMode(BranchUniversalObject.CONTENT_INDEX_MODE.PUBLIC)
                .setLocalIndexMode(BranchUniversalObject.CONTENT_INDEX_MODE.PUBLIC)
                .setContentImageUrl(imgUrl);


        LinkProperties lp = new LinkProperties()
                .setFeature("sharing")
                .setChannel("enveusharing")
                .addControlParameter("contentType", assetType)
                .addControlParameter("id", String.valueOf(assetId))
                .addControlParameter("seriesId", String.valueOf(seriesId))
                .addControlParameter("seasonNumber", String.valueOf(seasonNumber));
        buo.generateShortUrl(activity, lp, (url, error) -> {
            if (error == null) {
                sharingURL = url;
                Intent sharingIntent = new Intent(Intent.ACTION_SEND);
                sharingIntent.setType("text/plain");
                sharingIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                sharingIntent.putExtra(Intent.EXTRA_TEXT, mActivity.get().getResources().getString(R.string.checkout) + " " + title + " " + activity.getResources().getString(R.string.on_enveu) + "\n" + sharingURL);
                Logger.e("LINK PROPS", new Gson().toJson(sharingIntent));

                PackageManager pm = PanteaoApplication.getInstance().getPackageManager();
                List<ResolveInfo> activityList = pm.queryIntentActivities(sharingIntent, 0);
                for(final ResolveInfo app : activityList) {
                    Log.i("SharingType", "app.actinfo.name: " + app.activityInfo.name);

                }
                activity.startActivity(Intent.createChooser(sharingIntent, activity.getResources().getString(R.string.share)));

                Logger.i("BRANCH SDK", "got my Branch link to share: " + sharingURL);
            } else {
                Logger.e("BRANCH ERROR", error.getMessage());
            }
        });
    }*/

    public static void copyShareURL(Activity activity, String title, int assetId, String assetType, String imgUrl, String seasonNumber) {
        try {
            //String imageURL = imgUrl + AppConstants.WIDTH + (int) activity.getResources().getDimension(R.dimen.width1) + AppConstants.HEIGHT + (int) activity.getResources().getDimension(R.dimen.height1) + AppConstants.QUALITY_IMAGE;
            //  Log.e("FinalUrl-->>in", imageURL);
            Log.e("ImageUrl-->>in", imgUrl);
            String uri = createURI(title, assetId, assetType, imgUrl);


            Task<ShortDynamicLink> shortLinkTask = FirebaseDynamicLinks.getInstance().createDynamicLink()
                    //.setDomainUriPrefix("https://stagingsott.page.link/")
                    .setLink(Uri.parse(uri))
                    .setDomainUriPrefix(FirebaseConstants.FIREBASE_DPLNK_PREFIX)
                    //.setLink(Uri.parse(uri))
                    .setNavigationInfoParameters(new DynamicLink.NavigationInfoParameters.Builder().setForcedRedirectEnabled(true)
                            .build())
                    .setAndroidParameters(new DynamicLink.AndroidParameters.Builder(FirebaseConstants.FIREBASE_ANDROID_PACKAGE)
                            .build())
                    .setIosParameters(new DynamicLink.IosParameters.Builder(FirebaseConstants.FIREBASE_IOS_PACKAGE).build())
                    .setSocialMetaTagParameters(
                            new DynamicLink.SocialMetaTagParameters.Builder()
                                    .setTitle(title)
                                    .setDescription(seasonNumber)
                                    .setImageUrl(Uri.parse(imgUrl))
                                    .build())
                    .buildShortDynamicLink(ShortDynamicLink.Suffix.SHORT)
                    .addOnCompleteListener(activity, new OnCompleteListener<ShortDynamicLink>() {
                        @Override
                        public void onComplete(@NonNull Task<ShortDynamicLink> task) {
                            if (task.isSuccessful()) {

                                dynamicLinkUri = task.getResult().getShortLink();
                                Uri flowchartLink = task.getResult().getPreviewLink();
                                Log.e("dynamicUrl", dynamicLinkUri.toString() + flowchartLink);
                                // Log.e("flowchartLink", String.valueOf(flowchartLink));
                                try {
                                    activity.runOnUiThread(new Runnable() {
                                        @Override
                                        public void run() {
                                            if (dynamicLinkUri != null) {
                                                Intent sharingIntent = new Intent(Intent.ACTION_SEND);
                                                sharingIntent.setType("text/plain");
                                                sharingIntent.putExtra(Intent.EXTRA_TEXT, activity.getResources().getString(R.string.checkout) + " " + title + " " + activity.getResources().getString(R.string.on_enveu) + "\n" + dynamicLinkUri.toString());
                                                // sharingIntent.putExtra(Intent.EXTRA_TEXT, activity.getResources().getString(R.string.checkout) + " " + asset.getName() + " " + activity.getResources().getString(R.string.on_Dialog) + "\n" + "https://stagingsott.page.link/?link="+dynamicLinkUri.toString()+"&apn=com.astro.stagingsott");


                                                activity.startActivity(Intent.createChooser(sharingIntent, activity.getResources().getString(R.string.share)));

                                            }

                                        }
                                    });
                                } catch (Exception ignored) {

                                }


                            } else {
                                // Log.w("dynamicUrl",dynamicLinkUri.toString());
                            }
                        }
                    });

            shortLinkTask.toString();

        } catch (Exception ignored) {

        }
/*        mActivity = new WeakReference<>(activity);
        // new ToastHandler(mActivity.get()).show("Loading...");
        BranchUniversalObject buo = new BranchUniversalObject()
                .setTitle(title)
                .setContentIndexingMode(BranchUniversalObject.CONTENT_INDEX_MODE.PUBLIC)
                .setLocalIndexMode(BranchUniversalObject.CONTENT_INDEX_MODE.PUBLIC)
                .setContentImageUrl(imgUrl);


        LinkProperties lp = new LinkProperties()
                .setFeature("sharing")
                .setChannel("enveusharing")
                .addControlParameter("contentType", assetType)
                .addControlParameter("id", String.valueOf(assetId))
                .addControlParameter("seriesId", String.valueOf(seriesId))
                .addControlParameter("seasonNumber", String.valueOf(seasonNumber));
        Logger.e("LINK PROPS", new Gson().toJson(lp));
        buo.generateShortUrl(activity, lp, (url, error) -> {
            if (error == null) {
                sharingURL = url;
                String completeURL = mActivity.get().getResources().getString(R.string.checkout) + " " + title + " " + mActivity.get().getResources().getString(R.string.on_enveu) + "\n" + sharingURL;
                if (android.os.Build.VERSION.SDK_INT < android.os.Build.VERSION_CODES.HONEYCOMB) {
                    android.text.ClipboardManager clipboard = (android.text.ClipboardManager) mActivity.get().getSystemService(Context.CLIPBOARD_SERVICE);
                    clipboard.setText(completeURL);
                } else {
                    android.content.ClipboardManager clipboard = (android.content.ClipboardManager) mActivity.get().getSystemService(Context.CLIPBOARD_SERVICE);
                    android.content.ClipData clip = android.content.ClipData.newPlainText("Copied Text", completeURL);
                    clipboard.setPrimaryClip(clip);
                    new ToastHandler(mActivity.get()).show("Copied");
                }
              *//*  Intent sharingIntent = new Intent(Intent.ACTION_SEND);
                sharingIntent.setType("text/plain");
                sharingIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                sharingIntent.putExtra(Intent.EXTRA_TEXT, mActivity.get().getResources().getString(R.string.checkout) + " " + title + " " + mActivity.get().getResources().getString(R.string.on_enveu) + "\n" + sharingURL);
                activity.startActivity(Intent.createChooser(sharingIntent, activity.getResources().getString(R.string.share)));
*//*
                Logger.i("BRANCH SDK", "got my Branch link to share: " + sharingURL);
            } else {
                Logger.e("BRANCH ERROR", error.getMessage());
            }
        });*/
    }


    public static void openShareDialog(Activity activity, String title, String sharingURL) {
        Intent sharingIntent = new Intent(Intent.ACTION_SEND);
        sharingIntent.setType("text/plain");
        sharingIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        sharingIntent.putExtra(Intent.EXTRA_TEXT, mActivity.get().getResources().getString(R.string.checkout) + " " + title + " " + mActivity.get().getResources().getString(R.string.on_enveu) + "\n" + sharingURL);
        activity.startActivity(Intent.createChooser(sharingIntent, activity.getResources().getString(R.string.share)));
    }

    public static String getSharingURL(Activity activity, String title, int assetId, String assetType, String imgUrl, Context context) {
        mActivity = new WeakReference<>(activity);
        Logger.e("IMAGE URL----", imgUrl);
        BranchUniversalObject buo = new BranchUniversalObject()
                .setTitle(title)
                .setContentIndexingMode(BranchUniversalObject.CONTENT_INDEX_MODE.PUBLIC)
                .setLocalIndexMode(BranchUniversalObject.CONTENT_INDEX_MODE.PUBLIC)
                .setContentImageUrl(imgUrl);


        LinkProperties lp = new LinkProperties()
                .setFeature("sharing")
                .setChannel("enveusharing")
                .addControlParameter("contentType", assetType)
                .addControlParameter("id", String.valueOf(assetId));
//              .addControlParameter("seriesId", String.valueOf(seriesId));
//              .addControlParameter("seasonNumber", String.valueOf(seasonNumber));

        buo.generateShortUrl(context, lp, (url, error) -> {
            if (error == null) {
                sharingURL = url;
                Logger.i("BRANCH SDK", "got my Branch link to share: " + sharingURL);
            } else {
                sharingURL = "";
            }
        });
        return sharingURL;
    }

    public static ArrayList<Slide> dummySlides() {
        ArrayList<Slide> slides = new ArrayList<>();

        for (int i = 0; i < 10; i++) {
            Slide tempVal = new Slide();
            tempVal.setTitle("Title" + i);
            tempVal.setType(1);
            tempVal.setImageFromUrl("https://www.gstatic.com/webp/gallery/1.jpg");
            slides.add(tempVal);
        }

        return slides;
    }

    public static List<CommonRailData> getHomeRailData(List<PlaylistRailData> commonRailData, List<CommonRailData> adsRail, int playlistsize) {
        List<CommonRailData> commonRailDataList = new ArrayList<>();
        int size = commonRailData.size();
        int adsCounter = 0;
        int listCounter = 0;
        int loopSize;


        int listAds = commonRailData.size() / 5;

        if (listAds > adsRail.size()) {
            loopSize = commonRailData.size() + adsRail.size();
        } else {

            loopSize = commonRailData.size() + listAds + 1;
        }


        for (int i = 0; i < loopSize; i++) {
            CommonRailData commonData = new CommonRailData();
            if (i == 0) {
                ArrayList<Slide> slides = new ArrayList<>();
                int slideSize;

                if (commonRailData.get(i).getData().getContents().size() >= 5) {
                    slideSize = 5;
                } else {
                    slideSize = commonRailData.get(i).getData().getContents().size();
                }
                for (int j = 0; j < slideSize; j++) {
                    Slide slide = new Slide();
                    commonData.setType(0);
                    slide.setType(1);

                    if (commonRailData.get(i).getData().getContentType().equalsIgnoreCase(AppConstants.VOD)) {
                        slide.setImageFromUrl(AppCommonMethod.getImageUrl(AppConstants.VOD, "CROUSEL") + commonRailData.get(i).getData().getContents().get(j).getLandscapeImage());
                    } else if (commonRailData.get(i).getData().getContentType().equalsIgnoreCase(AppConstants.SERIES)) {
                        slide.setImageFromUrl(AppCommonMethod.getImageUrl(AppConstants.SERIES, "CROUSEL") + commonRailData.get(i).getData().getContents().get(j).getPicture());
                    } else if (commonRailData.get(i).getData().getContentType().equalsIgnoreCase(AppConstants.CAST_AND_CREW)) {
                        slide.setImageFromUrl(AppCommonMethod.getImageUrl(AppConstants.CAST_AND_CREW, "CROUSEL") + commonRailData.get(i).getData().getContents().get(j).getPicture());
                    } else if (commonRailData.get(i).getData().getContentType().equalsIgnoreCase(AppConstants.GENRE)) {
                        slide.setImageFromUrl(AppCommonMethod.getImageUrl(AppConstants.GENRE, "CROUSEL") + commonRailData.get(i).getData().getContents().get(j).getPicture());
                    }
                    slide.setAssetId(commonRailData.get(i).getData().getContents().get(j).getId());
                    slide.setTitle(commonRailData.get(i).getData().getContents().get(j).getTitle());
                    slide.setContents(commonRailData.get(i).getData().getContents().get(j));
                    slides.add(slide);
                }

                commonData.setSlides(slides);
                commonData.setProgressType(playlistsize);
                commonData.setRailData(commonRailData.get(i));
                commonRailDataList.add(commonData);
                listCounter = listCounter + 1;
            } else {

                if (i % 5 == 1) {
                    if (adsCounter < adsRail.size()) {
                        adsRail.get(adsCounter).setType(4);
                        commonRailDataList.add(adsRail.get(adsCounter));
                        adsCounter = adsCounter + 1;
                    } else {
                        if (listCounter < size) {
                            if (commonRailData.get(listCounter).getData().getContentImageType().equalsIgnoreCase(AppConstants.SQUARE)) {
                                commonData.setType(1);
                            } else if (commonRailData.get(listCounter).getData().getContentImageType().equalsIgnoreCase(AppConstants.POTRAIT)) {
                                commonData.setType(7);//default type common adapter
                            } else if (commonRailData.get(listCounter).getData().getContentImageType().equalsIgnoreCase(AppConstants.CIRCLE)) {
                                commonData.setType(2);
                            } else if (commonRailData.get(listCounter).getData().getContentImageType().equalsIgnoreCase(AppConstants.LANDSCAPE)) {
                                commonData.setType(3);
                            } else if (commonRailData.get(listCounter).getData().getContentImageType().equalsIgnoreCase(AppConstants.POSTER_LANDSCAPE)) {
                                commonData.setType(5);
                            } else if (commonRailData.get(listCounter).getData().getContentImageType().equalsIgnoreCase(AppConstants.POSTER_POTRAIT)) {
                                commonData.setType(6);
                            } else {
                                commonData.setType(3);
                            }
                            commonData.setRailData(commonRailData.get(listCounter));
                            commonData.setProgressType(playlistsize);
                            commonRailDataList.add(commonData);
                            listCounter = listCounter + 1;
                        }
                    }

                } else {
                    if (listCounter < size) {
                        if (commonRailData.get(listCounter).getData().getContentImageType().equalsIgnoreCase(AppConstants.SQUARE)) {
                            commonData.setType(1);
                        } else if (commonRailData.get(listCounter).getData().getContentImageType().equalsIgnoreCase(AppConstants.POTRAIT)) {
                            commonData.setType(7);//default type
                        } else if (commonRailData.get(listCounter).getData().getContentImageType().equalsIgnoreCase(AppConstants.CIRCLE)) {
                            commonData.setType(2);
                        } else if (commonRailData.get(listCounter).getData().getContentImageType().equalsIgnoreCase(AppConstants.LANDSCAPE)) {
                            commonData.setType(3);
                        }//change for digital type 2:3
                        else if (commonRailData.get(listCounter).getData().getContentImageType().equalsIgnoreCase(AppConstants.POSTER_LANDSCAPE)) {
                            commonData.setType(5);
                        } else if (commonRailData.get(listCounter).getData().getContentImageType().equalsIgnoreCase(AppConstants.POSTER_POTRAIT)) {
                            commonData.setType(6);
                        } else {
                            commonData.setType(3);
                        }
                        commonData.setRailData(commonRailData.get(listCounter));
                        commonData.setProgressType(playlistsize);
                        commonRailDataList.add(commonData);
                        listCounter = listCounter + 1;
                    }
                }

            }
            //  commonRailDataList.add(commonData);
        }
        return commonRailDataList;
    }

    public static String calculateTimein_hh_mm_format(long milliseconds) {

   /* if (milliseconds % 1000 > 0) {
        milliseconds = milliseconds + (milliseconds % 1000);
    }*/

        long hours = TimeUnit.SECONDS.toHours(milliseconds);
        long minute = TimeUnit.SECONDS.toMinutes(milliseconds) % TimeUnit.HOURS.toMinutes(1);
        long second = TimeUnit.SECONDS.toSeconds(milliseconds) % TimeUnit.MINUTES.toSeconds(1);
        String strHour = String.format("%02d", hours);
        String strSecond = String.format("%02d", second);
        if (second >= 30) {
            minute = minute + 1;
        }
        String strMinute = String.format("%02d", minute);

        String showTime = minute + ":" + strSecond;

        if (hours > 0)
            showTime = strHour + ":" + strMinute + ":" + strSecond;
        else if (minute > 0)
            if (second > 0) {
                showTime =strMinute +  ":" +  strSecond;
            }
            else if (second >= 0)
                showTime = "00:" + strSecond;
        return showTime;
    }

    public static String calculateTime(long milliseconds) {
        Logger.e("MIILLSSS", String.valueOf(milliseconds));
//        if (milliseconds % 1000 > 0) {
//            milliseconds = milliseconds + (milliseconds % 1000);
//        }
        long minute = Math.round(TimeUnit.SECONDS.toMinutes(milliseconds));
//        long hours = TimeUnit.MILLISECONDS.toHours(milliseconds);
//        long minute = TimeUnit.MILLISECONDS.toMinutes(milliseconds) % TimeUnit.HOURS.toMinutes(1);
//        long second = TimeUnit.MILLISECONDS.toSeconds(milliseconds) % TimeUnit.MINUTES.toSeconds(1);
//
//
//        String strHour = String.format("%02d", hours);
//        String strMinute = String.format("%02d", minute);
//        String strSecond = String.format("%02d", second);
//
//        String showTime = minute + ":" + strSecond;
//
//        if (hours > 0)
//            showTime = strHour + ":" + strMinute + ":" + strSecond;
//        else if (minute > 0)
//            showTime = strMinute + ":" + strSecond;
//        else if (second >= 0)
//            showTime = "00:" + strSecond;
        return minute + " minutes";
    }

    public static String getDuration(long milliseconds) {

        if (milliseconds % 1000 > 0) {
            milliseconds = milliseconds + (milliseconds % 1000);
        }

        long hours = TimeUnit.MILLISECONDS.toHours(milliseconds);
        long minute = TimeUnit.MILLISECONDS.toMinutes(milliseconds) % TimeUnit.HOURS.toMinutes(1);
        long second = TimeUnit.MILLISECONDS.toSeconds(milliseconds) % TimeUnit.MINUTES.toSeconds(1);


        String strHour = String.format("%01d", hours);
        String strMinute = String.format("%01d", minute);
        String strSecond = String.format("%01d", second);

        String showTime = minute + ":" + strSecond;

        if (hours > 0)
            showTime = strHour + "h " + strMinute + "m " + strSecond + "s";
        else if (minute > 0)
            showTime = strMinute + "m " + strSecond + "s";
        else if (second >= 0)
            showTime = "00:" + strSecond + "s";
        return showTime;
    }

    public static String getURLDuration(long milliseconds) {
        long totalSecs = milliseconds;
        long hours = (totalSecs / 3600);
        long mins = (totalSecs / 60) % 60;
        long secs = totalSecs % 60;
        String minsString = (mins == 0)
                ? "00"
                : ((mins < 10)
                ? "0" + mins
                : "" + mins);
        String secsString = (secs == 0)
                ? "00"
                : ((secs < 10)
                ? "" + secs + " sec"
                : "" + secs + " sec");
        if (hours > 0)
            return hours + " hr " + minsString + " min " + secsString;
        else if (mins > 0)
            return mins + " min";
        else return secsString;
    }

    public static void railBadgeVisibility(View view, boolean isVisible) {
        if (isVisible) {
            view.setVisibility(View.VISIBLE);
        } else {
            view.setVisibility(View.INVISIBLE);
        }
    }


    public static String setDownloadImage(String oldUrl, String imageSize) {
        PrintLogging.printLog("PRPosterImage-->>" + oldUrl + " " + imageSize);
        StringBuilder stringBuilder = new StringBuilder();
        String urlImage = oldUrl.trim();
        String one = SDKConfig.getInstance().getWebPUrl()+"fit-in/";
        String two = imageSize + "/" + SDKConfig.WEBP_QUALITY;
        stringBuilder.append(one).append(two).append(urlImage);
        PrintLogging.printLog("ImageUrld-->>" + one + "  " + two + " " + urlImage);
        PrintLogging.printLog("-->>StringBilder" + stringBuilder.toString());
        return stringBuilder.toString();
    }

    public static String setImage(String oldUrl, String imageSize) {
        PrintLogging.printLog("PRPosterImage-->>" + oldUrl + " " + imageSize);
        StringBuilder stringBuilder = new StringBuilder();
        String urlImage = oldUrl.trim();
        String one = SDKConfig.getInstance().getWebPUrl();
        String two = imageSize + "/" + SDKConfig.WEBP_QUALITY;
        stringBuilder.append(one).append(two).append(urlImage);
        PrintLogging.printLog("ImageUrld-->>" + one + "  " + two + " " + urlImage);
        PrintLogging.printLog("-->>StringBilder" + stringBuilder.toString());
        return stringBuilder.toString();
    }

    public static ArrayList<String> divideDate(String temp, String delim) {

        ArrayList<String> mDetailList = new ArrayList<>();
        StringTokenizer tok = new StringTokenizer(temp, delim, true);
        boolean expectDelim = false;
        while (tok.hasMoreTokens()) {
            String token = tok.nextToken();
            if (!token.equalsIgnoreCase(delim))
                mDetailList.add(token.trim());
            if (delim.equals(token)) {
                if (expectDelim) {
                    expectDelim = false;
                    continue;
                } /*else {
                    // unexpected delim means empty token
                    token = null;
                }*/
            }
            expectDelim = true;
        }

        return mDetailList;
    }


    public static int getMonth(String mm) {
        int val = 0;
        switch (mm) {
            case "Jan":
                break;
            case "Feb":
                val = 1;
                break;
            case "Mar":
                val = 2;
                break;
            case "Apr":
                val = 3;
                break;
            case "May":
                val = 4;
                break;
            case "Jun":
                val = 5;
                break;
            case "Jul":
                val = 6;
                break;
            case "Aug":
                val = 7;
                break;
            case "Sep":
                val = 8;
                break;
            case "Oct":
                val = 9;
                break;
            case "Nov":
                val = 10;
                break;
            case "Dec":
                val = 11;
                break;
        }
        return val;

    }

    public static ResponseConfig callpreference() {
        Gson gson = new Gson();
        String json = KsPreferenceKeys.getInstance().getAppPrefConfigResponse();
        return gson.fromJson(json, ResponseConfig.class);
    }

    public static void setProfilePic(KsPreferenceKeys preference, Context context, String key, CircleImageView imageView) {
        try {
            if (StringUtils.isNullOrEmptyOrZero(key)) {
                ImageHelper.getInstance(context)
                        .loadImageToProfile(imageView, "");
            } else {

                StringBuilder stringBuilder = new StringBuilder();
                String url1 = preference.getAppPrefCfep();
                if (key.contains("http")) {
                    stringBuilder.append(url1).append("/").append(key);
                } else {
                    if (StringUtils.isNullOrEmpty(url1)) {
                        url1 = AppCommonMethod.urlPoints;
                        preference.setAppPrefCfep(url1);
                    }
                    String url2 = AppConstants.PROFILE_FOLDER;
                    stringBuilder.append(url1).append(url2).append(key);
                }

                ImageHelper.getInstance(context)
                        .loadImageToProfile(imageView, stringBuilder.toString());
            }
        } catch (Exception e) {
            Logger.e("AppCommonMEthod", "setProfilePic" + e.getMessage());
        }

    }


    public static String getDeviceId(ContentResolver contentResolver) {
        return Settings.Secure.getString(contentResolver,
                Settings.Secure.ANDROID_ID);
    }

    public static String getGenre(EnveuVideoItemBean videosItem) {
        StringBuilder stringBuilder = new StringBuilder();
        if (videosItem.getAssetGenres() != null && videosItem.getAssetGenres().size() > 0) {
            for (int i = 0; i < videosItem.getAssetGenres().size(); i++) {
                if (i == videosItem.getAssetGenres().size() - 1) {
                    stringBuilder = stringBuilder.append(videosItem.getAssetGenres().get(i));

                } else
                    stringBuilder = stringBuilder.append(videosItem.getAssetGenres().get(i)).append(", ");
            }
        }

        return stringBuilder.toString();
    }


    public static int getListViewType(String contentImageType) {
        if (contentImageType.equalsIgnoreCase(ImageType.CIR.name())) {
            return 0;
        } else if (contentImageType.equalsIgnoreCase(ImageType.LDS.name())) {
            return 1;
        } else if (contentImageType.equalsIgnoreCase(ImageType.PR1.name())) {
            return 2;
        } else if (contentImageType.equalsIgnoreCase(ImageType.PR2.name())) {
            return 3;
        } else if (contentImageType.equalsIgnoreCase(ImageType.SQR.name())) {
            return 4;
        } else {
            return 1;
        }

    }

    public static String getListPRImage(String posterURL, Context context) {
        PrintLogging.printLog("PRPosterImage-->>" + posterURL);
        int w = (int) context.getResources().getDimension(R.dimen.portrait_image_width);
        int h = (int) context.getResources().getDimension(R.dimen.portrait_image_height);
        return setImage(posterURL, w + "x" + h);
    }

    public static String getListPRTwoImage(String posterURL, Context context) {
        PrintLogging.printLog("PRPosterImage-->>" + posterURL);
        int w = (int) context.getResources().getDimension(R.dimen.portrait_image_width);
        int h = (int) context.getResources().getDimension(R.dimen.portrait_image_height);
        return setImage(posterURL, w + "x" + h);
    }

    public static String getListCIRCLEImage(String posterURL, Context context) {
        PrintLogging.printLog("PRPosterImage-->>" + posterURL);
        int w = (int) context.getResources().getDimension(R.dimen.circle_image_width);
        int h = (int) context.getResources().getDimension(R.dimen.circle_image_height);
        return setImage(posterURL, w + "x" + h);
    }

    public static String getListLDownloadImage(String posterURL, Context context) {
        PrintLogging.printLog("PRPosterImage-->>" + posterURL);
        int w = (int) context.getResources().getDimension(R.dimen.landscape_image_width);
        int h = (int) context.getResources().getDimension(R.dimen.landscape_image_height);
        return setDownloadImage(posterURL, w + "x" + h);
    }

    public static String getListLDSImage(String posterURL, Context context) {
        PrintLogging.printLog("PRPosterImage-->>" + posterURL);
        int w = (int) context.getResources().getDimension(R.dimen.landscape_image_width);
        int h = (int) context.getResources().getDimension(R.dimen.landscape_image_height);
        return setImage(posterURL, w + "x" + h);
    }

    public static String getListSQRImage(String posterURL, Context context) {
        PrintLogging.printLog("PRPosterImage-->>" + posterURL);
        int w = (int) context.getResources().getDimension(R.dimen.square_image_width);
        int h = (int) context.getResources().getDimension(R.dimen.square_image_height);
        return setImage(posterURL, w + "x" + h);
    }

    public static void launchDetailScreen(
            Context context,
            String screenType,
            int id,
            String series,
            String season,
            Boolean isPremium, EnveuVideoItemBean asset
    ) {
        if (SystemClock.elapsedRealtime() - mLastClickTime < 2000) {
            return;
        }
        mLastClickTime = SystemClock.elapsedRealtime();
        Logger.e("CLICKED_DETAILS", screenType + " " + new Gson().toJson(asset));
        if (screenType.equalsIgnoreCase(
                MediaTypeConstants.getInstance().getSeries()
        ) || screenType.equalsIgnoreCase(
                MediaTypeConstants.getInstance().getTutorial()
        )
        ) {
            TvActivityLauncher.Companion.getInstance().seriesDetailScreen(
                    (TVBaseActivity) context,
                    id
            );
        } else if (screenType.toUpperCase().equalsIgnoreCase(MediaTypeConstants.getInstance().getShow())) {
            TvActivityLauncher.Companion.getInstance().detailScreenBrightCove(
                    (TVBaseActivity) context,
                    id,
                    series,
                    season,
                    isPremium,
                    AppConstants.MOVIE_ENVEU
            );
        } else if (screenType.toUpperCase().equalsIgnoreCase(
                MediaTypeConstants.getInstance().getEpisode()
        ) || screenType.toUpperCase().equalsIgnoreCase(
                MediaTypeConstants.getInstance().getChapter()
        ) || screenType.toUpperCase().equalsIgnoreCase(
                MediaTypeConstants.getInstance().getTrailor()
        )
        ) {
            RailInjectionHelper railInjectionHelper = ViewModelProviders.of((TVBaseActivity) context).get(RailInjectionHelper.class);
            railInjectionHelper.getAssetDetailsV2(String.valueOf(id))
                    .observe((TVBaseActivity) context, new Observer<ResponseModel>() {
                        @Override
                        public void onChanged(ResponseModel responseModel) {
                            if (responseModel != null && responseModel.getBaseCategory() != null) {
                                RailCommonData railCommonData = (RailCommonData) responseModel.getBaseCategory();
                                Intent playerIntent =
                                        new Intent(context, KalturaPlayerActivity.class);
                                playerIntent.putExtra(
                                        "EntryId",
                                        railCommonData.getEnveuVideoItemBeans().get(0).getkEntryId()
                                );
                                context.startActivity(playerIntent);
                            }
                        }
                    });
        } else if (screenType.equalsIgnoreCase(MediaTypeConstants.getInstance().getInstructor())) {
            TvActivityLauncher.Companion.getInstance().instructorDetailsScreen(
                    (TVBaseActivity) context,
                    asset
            );
        } else if (screenType.equalsIgnoreCase(MediaTypeConstants.getInstance().getCustomInternalPage())) {
            Intent playerIntent =
                    new Intent(context, TVCustomInternalPage.class);
            playerIntent.putExtra(
                    "asset",
                    asset
            );
            context.startActivity(playerIntent);
        }
    }

    public static void launchDetailScreen(Context context, String videoId, String screenType, int id, String duration, boolean isPremium, EnveuVideoItemBean asset) {


            if (SystemClock.elapsedRealtime() - mLastClickTime < 2000) {
                return;
            }
            mLastClickTime = SystemClock.elapsedRealtime();
            Logger.e("CLICKED_DETAILS", screenType + " " + new Gson().toJson(asset));

            if (screenType.toUpperCase().equalsIgnoreCase(MediaTypeConstants.getInstance().getShow())) {
                if (SDKConfig.getInstance().getShowDetailId().equalsIgnoreCase("")) {
                    //new ActivityLauncher((BaseActivity) context).detailScreenBrightCove((BaseActivity) context, DetailActivity.class, videoId, id, duration, isPremium, AppConstants.SHOW_ENVEU);
                } else {
                    //new ActivityLauncher((BaseActivity) context).detailScreenBrightCove((BaseActivity) context, InstructorActivity.class, videoId, id, duration, isPremium, SDKConfig.getInstance().getShowDetailId());
                    new ActivityLauncher((BaseActivity) context).ShowScreenBrightCove((BaseActivity) context, ShowActivity.class, videoId, id, duration, isPremium, SDKConfig.getInstance().getShowDetailId());
                }
            } else if (screenType.toUpperCase().equalsIgnoreCase(MediaTypeConstants.getInstance().getEpisode())) {
                new ActivityLauncher((BaseActivity) context).episodeScreenBrightcove((BaseActivity) context, EpisodeActivity.class, videoId, id, duration, isPremium);
            } else if (screenType.toUpperCase().equalsIgnoreCase(MediaTypeConstants.getInstance().getSeries())) {
                new ActivityLauncher((BaseActivity) context).seriesDetailScreen((BaseActivity) context, SeriesDetailActivity.class, id);
            } else if (screenType.toUpperCase().equalsIgnoreCase(MediaTypeConstants.getInstance().getLive()) || screenType.equalsIgnoreCase("LIVE")) {
                if (SDKConfig.getInstance().getLiveDetailId().equalsIgnoreCase("")) {
                    // new ActivityLauncher((BaseActivity) context).detailScreenBrightCove((BaseActivity) context, DetailActivity.class, videoId, id, duration, isPremium, AppConstants.MOVIE_ENVEU);
                } else {
                    // new ActivityLauncher((BaseActivity) context).liveScreenBrightCove((BaseActivity) context, LiveActivity.class, videoId, id, duration, isPremium, SDKConfig.getInstance().getMovieDetailId());
                }
            } else if (screenType.toUpperCase().equalsIgnoreCase(MediaTypeConstants.getInstance().getInstructor()) || screenType.equalsIgnoreCase("INSTRUCTOR")) {
                if (SDKConfig.getInstance().getInstructorDetaildId().equalsIgnoreCase("")) {
                    // new ActivityLauncher((BaseActivity) context).detailScreenBrightCove((BaseActivity) context, DetailActivity.class, videoId, id, duration, isPremium, AppConstants.MOVIE_ENVEU);
                } else {
                    new ActivityLauncher((BaseActivity) context).detailScreenBrightCove((BaseActivity) context, InstructorActivity.class, videoId, id, duration, isPremium, SDKConfig.getInstance().getMovieDetailId());
                }
            } else if (screenType.toUpperCase().equalsIgnoreCase(MediaTypeConstants.getInstance().getChapter()) || screenType.equalsIgnoreCase("CHAPTER")) {
                if (SDKConfig.getInstance().getInstructorDetaildId().equalsIgnoreCase("")) {
                    // new ActivityLauncher((BaseActivity) context).detailScreenBrightCove((BaseActivity) context, DetailActivity.class, videoId, id, duration, isPremium, AppConstants.MOVIE_ENVEU);
                } else {
                    new ActivityLauncher((BaseActivity) context).chapterScreenBrightcove((BaseActivity) context, ChapterActivity.class, videoId, id, duration, isPremium);
                }
            } else if (screenType.toUpperCase().equalsIgnoreCase(MediaTypeConstants.getInstance().getTutorial())) {
                new ActivityLauncher((BaseActivity) context).tutorialDetailScreen((BaseActivity) context, TutorialActivity.class, id);
            } else if (screenType.equalsIgnoreCase(MediaTypeConstants.getInstance().getCustomInternalPage())) {
                Log.w("customeInternalPage","true");
                Intent playerIntent =
                        new Intent(context, CustomInternalPage.class);
                playerIntent.putExtra(
                        "asset",
                        asset
                );
                context.startActivity(playerIntent);
            } else if (screenType.equalsIgnoreCase(MediaTypeConstants.getInstance().getCustomExternalPage())) {
                if (asset.getCustomLinkDetails()!=null && !asset.getCustomLinkDetails().equalsIgnoreCase("\"#\"") && !asset.getCustomLinkDetails().equalsIgnoreCase("#")) {
                    try {
                        new ActivityLauncher((BaseActivity) context).customExternalPageWebview((BaseActivity) context, CustomExternalPageWebview.class, asset.getCustomLinkDetails().replace("\"", ""), asset.getTitle());
                    } catch (Exception ex) {
                        ex.printStackTrace();
                    }
                } else {
                    Logger.e("EXTERNAL_PAGE", "EMPTY");
                }
            }

    }

    public static void trackFcmEvent(String title, String assetType, Context activity) {
        try {
            final JsonObject requestParam = new JsonObject();
            requestParam.addProperty(EventConstant.ActionScreen, title);
            if (assetType.toUpperCase().equalsIgnoreCase(MediaTypeConstants.getInstance().getMovie())) {
                requestParam.addProperty(EventConstant.ContentType, assetType);
            } else if (assetType.toUpperCase().equalsIgnoreCase(MediaTypeConstants.getInstance().getShow())) {
                requestParam.addProperty(EventConstant.ContentType, assetType);
            } else if (assetType.toUpperCase().equalsIgnoreCase(MediaTypeConstants.getInstance().getEpisode())) {
                requestParam.addProperty(EventConstant.ContentType, assetType);
            } else if (assetType.toUpperCase().equalsIgnoreCase(MediaTypeConstants.getInstance().getSeries())) {
                requestParam.addProperty(EventConstant.ContentType, assetType);
            } else if (assetType.toUpperCase().equalsIgnoreCase(MediaTypeConstants.getInstance().getInstructor())) {
                requestParam.addProperty(EventConstant.ContentType, assetType);
            } else if (assetType.toUpperCase().equalsIgnoreCase(MediaTypeConstants.getInstance().getChapter())) {
                requestParam.addProperty(EventConstant.ContentType, assetType);
            } else if (assetType.toUpperCase().equalsIgnoreCase(MediaTypeConstants.getInstance().getTutorial())) {
                requestParam.addProperty(EventConstant.ContentType, assetType);
            } else {
                requestParam.addProperty(EventConstant.ContentType, "");
            }

            FCMEvents.getInstance().setContext(activity).trackEvent(TRACK_EVENT_FIREBASE_SCREEN);
        } catch (Exception e) {

        }

    }

    public static void trackFcmCustomEvent(Context activity, String eventType, String content_type, String category_id, String category_name, int row_index, String content_title, int display_index, String content_id, long content_played, long content_duration, String search_term, String video_quality, String userId, String username) {
        try {
            final JsonObject requestParam = new JsonObject();
            if (eventType.equalsIgnoreCase(AppConstants.GALLERY_SELECT)) {
//                requestParam.addProperty(EventConstant.ActionScreen,action_screen);
                requestParam.addProperty(EventConstant.Content_Type, content_type);
                requestParam.addProperty(EventConstant.CategoryId, category_id);
                requestParam.addProperty(EventConstant.CategoryName, category_name);
                requestParam.addProperty(EventConstant.RowIndex, row_index);
                FCMEvents.getInstance().setContext(activity).trackEvent(TRACK_EVENT_GALLERY_SELECT);


            } else if (eventType.equalsIgnoreCase(AppConstants.CONTENT_SELECT)) {
                requestParam.addProperty(EventConstant.DisplayIndex, display_index);
                requestParam.addProperty(EventConstant.ContentId, content_id);
                requestParam.addProperty(EventConstant.ContentTitle, content_title);
//                requestParam.addProperty(EventConstant.ActionScreen,action_screen);
                requestParam.addProperty(EventConstant.RowIndex, row_index);
                requestParam.addProperty(EventConstant.Content_Type, content_type);
                requestParam.addProperty(EventConstant.CategoryId, category_id);
                requestParam.addProperty(EventConstant.CategoryName, category_name);
                FCMEvents.getInstance().setContext(activity).trackEvent(TRACK_EVENT_CONTENT_SELECT);


            } else if (eventType.equalsIgnoreCase(AppConstants.CONTENT_PLAY)) {
                requestParam.addProperty(EventConstant.ContentPlayed, content_played);
                requestParam.addProperty(EventConstant.ContentDuration, content_duration);
                requestParam.addProperty(EventConstant.Content_Type, content_type);
                requestParam.addProperty(EventConstant.ContentTitle, content_title);
                requestParam.addProperty(EventConstant.ContentId, content_id);
//                requestParam.addProperty(EventConstant.ActionScreen,action_screen);
                FCMEvents.getInstance().setContext(activity).trackEvent(TRACK_EVENT_CONTENT_PLAY);


            } else if (eventType.equalsIgnoreCase(AppConstants.CONTENT_COMPLETED)) {
                requestParam.addProperty(EventConstant.ContentPlayed, content_played);
                requestParam.addProperty(EventConstant.ContentDuration, content_duration);
                requestParam.addProperty(EventConstant.Content_Type, content_type);
                requestParam.addProperty(EventConstant.ContentTitle, content_title);
                requestParam.addProperty(EventConstant.ContentId, content_id);
//                requestParam.addProperty(EventConstant.ActionScreen,action_screen);
                FCMEvents.getInstance().setContext(activity).trackEvent(TRACK_EVENT_CONTENT_COMPLETED);


            } else if (eventType.equalsIgnoreCase(AppConstants.CONTENT_EXIT)) {
                requestParam.addProperty(EventConstant.ContentPlayed, content_played);
                requestParam.addProperty(EventConstant.ContentDuration, content_duration);
                requestParam.addProperty(EventConstant.Content_Type, content_type);
                requestParam.addProperty(EventConstant.ContentTitle, content_title);
                requestParam.addProperty(EventConstant.ContentId, content_id);
//                requestParam.addProperty(EventConstant.ActionScreen,action_screen);
                FCMEvents.getInstance().setContext(activity).trackEvent(TRACK_EVENT_CONTENT_EXIT);


            } else if (eventType.equalsIgnoreCase(AppConstants.SHARE_CONTENT)) {
                requestParam.addProperty(EventConstant.ContentId, content_id);
//                requestParam.addProperty(EventConstant.ActionScreen,action_screen);
                requestParam.addProperty(EventConstant.ContentTitle, content_title);
                requestParam.addProperty(EventConstant.Content_Type, content_type);
                FCMEvents.getInstance().setContext(activity).trackEvent(TRACK_EVENT_SHARE_CONTENT);


            } else if (eventType.equalsIgnoreCase(AppConstants.ADD_TO_WATCHLIST)) {
                requestParam.addProperty(EventConstant.ContentId, content_id);
//                requestParam.addProperty(EventConstant.ActionScreen,action_screen);
                requestParam.addProperty(EventConstant.ContentTitle, content_title);
                requestParam.addProperty(EventConstant.Content_Type, content_type);
                FCMEvents.getInstance().setContext(activity).trackEvent(TRACK_EVENT_ADD_TO_WATCHLIST);


            } else if (eventType.equalsIgnoreCase(AppConstants.REMOVE_WATCHLIST)) {
                requestParam.addProperty(EventConstant.ContentId, content_id);
//                requestParam.addProperty(EventConstant.ActionScreen,action_screen);
                requestParam.addProperty(EventConstant.ContentTitle, content_title);
                requestParam.addProperty(EventConstant.Content_Type, content_type);
                FCMEvents.getInstance().setContext(activity).trackEvent(TRACK_EVENT_REMOVE_WATCHLIST);
            } else if (eventType.equalsIgnoreCase(AppConstants.SEARCH)) {
//                requestParam.addProperty(EventConstant.ActionScreen,action_screen);
                requestParam.addProperty(EventConstant.SearchTerm, search_term);
                FCMEvents.getInstance().setContext(activity).trackEvent(TRACK_EVENT_SEARCH);


            } else if (eventType.equalsIgnoreCase(AppConstants.SIGN_IN_SUCCESS)) {
                requestParam.addProperty(EventConstant.UserID, userId);
                requestParam.addProperty(EventConstant.Username, username);

                FCMEvents.getInstance().setContext(activity).trackEvent(TRACK_EVENT_SIGN_IN_SUCCESS);

            } else if (eventType.equalsIgnoreCase(AppConstants.SIGN_UP_SUCCESS)) {
//                requestParam.addProperty(EventConstant.ActionScreen,action_screen);
                requestParam.addProperty(EventConstant.UserID, userId);
                requestParam.addProperty(EventConstant.Username, username);
                FCMEvents.getInstance().setContext(activity).trackEvent(TRACK_EVENT_SIGN_UP_SUCCESS);

            } else if (eventType.equalsIgnoreCase(AppConstants.LOGOUT)) {
//                requestParam.addProperty(EventConstant.ActionScreen,action_screen);
                FCMEvents.getInstance().setContext(activity).trackEvent(TRACK_EVENT_LOGOUT);

            } else if (eventType.equalsIgnoreCase(AppConstants.SETTINGS_VIDEO_QUALITY)) {
//                requestParam.addProperty(EventConstant.ActionScreen,action_screen);
                requestParam.addProperty(EventConstant.VideoQuality, video_quality);
                FCMEvents.getInstance().setContext(activity).trackEvent(TRACK_EVENT_SETTINGS_VIDEO_QUALITY);


            } else {
                requestParam.addProperty(EventConstant.ContentType, "");
            }

        } catch (Exception e) {

        }

    }

    public static void customTabWidth(TabLayout tabLayout) {
        LinearLayout layout = ((LinearLayout) ((LinearLayout) tabLayout.getChildAt(0)).getChildAt(0));
        LinearLayout.LayoutParams layoutParams = (LinearLayout.LayoutParams) layout.getLayoutParams();
        layoutParams.weight = 0.5f;
        layoutParams.width = ViewGroup.LayoutParams.WRAP_CONTENT; // e.g. 0.5f
        layout.setLayoutParams(layoutParams);

    }

    public static void customTabWidth2(TabLayout tabLayout) {
        LinearLayout layout = ((LinearLayout) ((LinearLayout) tabLayout.getChildAt(0)).getChildAt(1));
        LinearLayout.LayoutParams layoutParams = (LinearLayout.LayoutParams) layout.getLayoutParams();
        layoutParams.weight = 0f;
        layoutParams.width = ViewGroup.LayoutParams.WRAP_CONTENT; // e.g. 0.5f
        layout.setLayoutParams(layoutParams);
    }


    public static String getUserName(String name) {
        String value = "";
        try {
            if (name != null) {
                if (!name.equals("")) {
                    name = name.trim().replaceAll("\\s+", " ");
                    if (name.contains(" ")) {
                        String[] words = name.split(" ");
                        if (words.length != 0) {
                            String firstWord = String.valueOf(words[0].charAt(0)).toUpperCase();
                            if (words.length == 1) {
                                value = firstWord;
                            } else {
                                String secondWord = String.valueOf(words[1].charAt(0)).toUpperCase();
                                value = firstWord + secondWord;
                            }
                        }

                    } else {
                        if (name.length() > 2) {
                            value = String.valueOf(name.charAt(0)).toUpperCase() + "" + String.valueOf(name.charAt(1)).toUpperCase();
                        } else {
                            value = String.valueOf(name.charAt(0)).toUpperCase() + "" + String.valueOf(name.charAt(1)).toUpperCase();
                        }
                    }
                }
            }

        } catch (Exception e) {

            PrintLogging.printLog("" + e);
        }

        return value;
    }

    public static void trackSearchFcmEvent(Context activity, String searchKeyword) {
        try {
            final JsonObject requestParam = new JsonObject();
            requestParam.addProperty(EventConstant.SearchTitle, searchKeyword);

            FCMEvents.getInstance().setContext(activity).trackEvent(4);
        } catch (Exception e) {

        }
    }

    public static void updateLanguage(String language, Context context) {
        Logger.w("selectedLang--in", language);
        Locale locale = new Locale(language);
        Locale.setDefault(locale);

        Resources res = context.getResources();
        Configuration config = new Configuration(res.getConfiguration());
        config.locale = locale;
        res.updateConfiguration(config, res.getDisplayMetrics());

    }


    public static void resetLanguage(String language, Context context) {
        try {
            Logger.w("selectedLang--in", language);
            Locale locale = new Locale(language);
            Locale.setDefault(locale);
            Configuration config = new Configuration();
            config.locale = locale;
            context.getResources().updateConfiguration(config, context.getResources().getDisplayMetrics());

        } catch (Exception ignored) {

        }
    }

    public static void getPushToken(Activity activity) {
        mActivity = new WeakReference<>(activity);
        FirebaseInstanceId.getInstance().getInstanceId()
                .addOnCompleteListener(new OnCompleteListener<InstanceIdResult>() {
                    @Override
                    public void onComplete(@NonNull Task<InstanceIdResult> task) {
                        if (!task.isSuccessful()) {
                            return;
                        }

                        // Get new Instance ID token
                        String token = task.getResult().getToken();
                        KsPreferenceKeys.getInstance().setAppPrefFcmToken(token);
                        Logger.w("FCM_TOKEN", KsPreferenceKeys.getInstance().getAppPrefFcmToken());

                        // Log and toast
                        // String msg = getString(R.string.msg_token_fmt, token);
                        //Log.d(TAG, msg);
                        //  Toast.makeText(MainActivity.this, msg, Toast.LENGTH_SHORT).show();
                    }
                });
    }

    public static void showPopupMenu(Context context, View view, int menuItems, PopupMenu.OnMenuItemClickListener onMenuItemClickListener) {
        PopupMenu popup = new PopupMenu(context, view);
        popup.setOnMenuItemClickListener(onMenuItemClickListener);
        popup.inflate(menuItems);
        popup.show();
    }

    public static void createManualHeroItem(EnveuVideoItemBean enveuVideoItemBean, EnveuVideoDetails enveuVideoDetails) {
        enveuVideoItemBean.setBrightcoveVideoId(enveuVideoDetails.getBrightcoveContentId());
        enveuVideoItemBean.setDescription(enveuVideoDetails.getDescription());
        enveuVideoItemBean.setAssetType(enveuVideoDetails.getContentType());
    }

    public static void createAssetHeroItem(EnveuVideoItemBean enveuVideoItemBean, EnveuVideoDetails enveuVideoDetails) {
        enveuVideoItemBean.setBrightcoveVideoId(enveuVideoDetails.getBrightcoveContentId());
        enveuVideoItemBean.setDescription(enveuVideoDetails.getDescription());
//        if (screenWidget.getWidgetImageType().equalsIgnoreCase(WidgetImageType.THUMBNAIL.toString())) {
//            Logger.e("Screen WidgetType ", screenWidget.getWidgetImageType());
//            if (enveuVideoDetails.getImages() != null && enveuVideoDetails.getImages().getThumbnail() != null && enveuVideoDetails.getImages().getThumbnail().getSources() != null
//                    && enveuVideoDetails.getImages().getThumbnail().getSources().size() > 0) {
//                enveuVideoItemBean.setPosterURL(enveuVideoDetails.getImages().getThumbnail().getSources().get(0).getSrc());
//            }
//        }
//        // enveuVideoItemBean.setPosterURL(enveuVideoDetails.getThumbnailImage());
//        else {
//            if (enveuVideoDetails.getImages() != null && enveuVideoDetails.getImages().getPoster() != null && enveuVideoDetails.getImages().getPoster().getSources() != null
//                    && enveuVideoDetails.getImages().getPoster().getSources().size() > 0) {
//                enveuVideoItemBean.setPosterURL(enveuVideoDetails.getImages().getPoster().getSources().get(0).getSrc());
//            }
//        }
        enveuVideoItemBean.setImages(enveuVideoDetails.getImages());
        enveuVideoItemBean.setAssetType(enveuVideoDetails.getContentType());
    }

    public static void heroAssetRedirections(RailCommonData railCommonData, Context activity, String videoId) {
        try {
            if (railCommonData.getEnveuVideoItemBeans().get(0).getAssetType().equalsIgnoreCase(MediaTypeConstants.getInstance().getEpisode())) {
                AppCommonMethod.launchDetailScreen(activity, videoId, MediaTypeConstants.getInstance().getEpisode(), Integer.parseInt(railCommonData.getScreenWidget().getLandingPageAssetId()), "0", false, null);
            } else if (railCommonData.getEnveuVideoItemBeans().get(0).getAssetType().equalsIgnoreCase(MediaTypeConstants.getInstance().getSeries())) {
                AppCommonMethod.launchDetailScreen(activity, videoId, MediaTypeConstants.getInstance().getSeries(), Integer.parseInt(railCommonData.getScreenWidget().getLandingPageAssetId()), "0", false, null);
            } else if (railCommonData.getEnveuVideoItemBeans().get(0).getAssetType().equalsIgnoreCase(MediaTypeConstants.getInstance().getMovie())) {
                AppCommonMethod.launchDetailScreen(activity, videoId, MediaTypeConstants.getInstance().getMovie(), Integer.parseInt(railCommonData.getScreenWidget().getLandingPageAssetId()), "0", false, null);
            } else if (railCommonData.getEnveuVideoItemBeans().get(0).getAssetType().equalsIgnoreCase(MediaTypeConstants.getInstance().getShow())) {
                AppCommonMethod.launchDetailScreen(activity, videoId, MediaTypeConstants.getInstance().getShow(), Integer.parseInt(railCommonData.getScreenWidget().getLandingPageAssetId()), "0", false, null);
            } else if (railCommonData.getEnveuVideoItemBeans().get(0).getAssetType().equalsIgnoreCase(MediaTypeConstants.getInstance().getLive())) {
                AppCommonMethod.launchDetailScreen(activity, videoId, MediaTypeConstants.getInstance().getLive(), Integer.parseInt(railCommonData.getScreenWidget().getLandingPageAssetId()), "0", false, null);
            } else if (railCommonData.getEnveuVideoItemBeans().get(0).getAssetType().equalsIgnoreCase(AppConstants.ContentType.ARTICLE.name())) {
                AppCommonMethod.launchDetailScreen(activity, videoId, MediaTypeConstants.getInstance().getLive(), Integer.parseInt(railCommonData.getScreenWidget().getLandingPageAssetId()), "0", false, null);
            }
            else if (railCommonData.getEnveuVideoItemBeans().get(0).getAssetType().equalsIgnoreCase(MediaTypeConstants.getInstance().getCustomInternalPage())) {
                String id = railCommonData.setCustomeInternalPageId(railCommonData.getScreenWidget());
                if (!id.equalsIgnoreCase("")){
                    Intent playerIntent =
                            new Intent(activity, CustomInternalPage.class);
                    playerIntent.putExtra("asset", railCommonData.getEnveuVideoItemBeans().get(0));
                    playerIntent.putExtra("images",railCommonData.getEnveuVideoItemBeans().get(0).getImages());
                    playerIntent.putExtra("asset_id", Integer.parseInt(id));
                    activity.startActivity(playerIntent);
                }

            }

        } catch (Exception ignored) {
                Log.w("clickCrash-->>","Appcommon-->>"+ignored.toString());
        }

    }

    public static void getAssetDetail(RailCommonData railCommonData, Response<EnveuVideoDetailsBean> response) {
        try {
            EnveuVideoDetailsBean enveuVideoDetailsBean = new EnveuVideoDetailsBean();
            EnveuVideoDetails enveuVideoDetails = response.body().getData();
            enveuVideoDetailsBean.setData(enveuVideoDetails);
            EnveuVideoItemBean enveuVideoItemBean = new EnveuVideoItemBean(enveuVideoDetailsBean, ImageType.LDS.name());
            railCommonData.setEnveuVideoItemBeans(new ArrayList<>());
            railCommonData.getEnveuVideoItemBeans().add(enveuVideoItemBean);
        } catch (Exception ignored) {
            Logger.e("ASSET_EXCEPTION", ignored.getMessage());
        }
    }

    public static void callSocialAction(KsPreferenceKeys preference, UserInteractionFragment userInteractionFragment) {
        try {
            if (preference.getAppPrefLoginStatus()) {
                if (userInteractionFragment != null) {
                    if (ActivityTrackers.getInstance().action.equalsIgnoreCase(ActivityTrackers.LIKE)) {
                        userInteractionFragment.setToken();
                        userInteractionFragment.setLikeForAsset();
                        ActivityTrackers.getInstance().setAction("");
                    } else if (ActivityTrackers.getInstance().action.equalsIgnoreCase(ActivityTrackers.WATCHLIST)) {
                        userInteractionFragment.setToken();
                        userInteractionFragment.setWatchListForAsset();
                        ActivityTrackers.getInstance().setAction("");
                    }
                }
            }
        } catch (Exception e) {

        }
    }

    public static void guestTitle(Context context, TextView userNameWords, TextView usernameTv, KsPreferenceKeys preference) {
        if (preference != null) {
            userNameWords.setText(AppCommonMethod.getUserName(context.getResources().getString(R.string.guest_user)));
            usernameTv.setText(context.getResources().getString(R.string.guest_user));
        }
    }

    public static String lang;

    public static String getSystemLanguage() {
        try {
            lang = Locale.getDefault().getLanguage();
            String country = Locale.getDefault().getCountry();
            Logger.w("langugedetail", lang + "  " + country + "  ");
        } catch (Exception ignored) {

        }
        return lang;
    }

    public static List<String> breakCastToArray(String cast_en) {
        List<String> castArr = new ArrayList<>();
        try {
            String[] arr = cast_en.split(",");
            for (int i = 0; i < arr.length; i++) {
                castArr.add(arr[i]);
            }
        } catch (Exception e) {

        }
        return castArr;
    }

    public static List<String> breakGenreToArray(String genre_en) {
        List<String> genreArr = new ArrayList<>();
        try {
            String[] arr = genre_en.split(",");
            for (int i = 0; i < arr.length; i++) {
                genreArr.add(arr[i]);
            }
        } catch (Exception e) {

        }
        return genreArr;
    }

    public static Long getCurrentTimeStamp() {

        Long tsLong = System.currentTimeMillis() / 1000;
        return tsLong;
    }

    public static String expiryDate(int days) {
        DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
        Date currentDate = new Date();
        System.out.println(dateFormat.format(currentDate));
        //Log.d("DTGLogs", "expiryDays-->>" + dateFormat.format(currentDate));

        // convert date to calendar
        Calendar c = Calendar.getInstance();
        c.setTime(currentDate);

        // manipulate date
        c.add(Calendar.YEAR, 0);
        c.add(Calendar.MONTH, 0);
        c.add(Calendar.DATE, days); //same with c.add(Calendar.DAY_OF_MONTH, 1);
        c.add(Calendar.HOUR, 0);
        c.add(Calendar.MINUTE, 1);
        c.add(Calendar.SECOND, 1);

        // convert calendar to date
        Date currentDatePlusOne = c.getTime();

        System.out.println(dateFormat.format(currentDatePlusOne));

        //  Log.d("DTGLogs", "expiryDays-->>" + dateFormat.format(currentDatePlusOne));
        return dateFormat.format(currentDatePlusOne);
    }

    public static int getTodaysDifference(String completionDate) {
        int diff = -1;
        try {
            String date = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss", Locale.getDefault()).format(new Date());

            Date date1;
            Date date2;

            SimpleDateFormat dates = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");

            //Setting dates
            date1 = dates.parse(date);
            date2 = dates.parse(completionDate);

            if (date1.before(date2)) {
                //Log.d("DTGLogs", "DBdayDifference-->>if" + date1 + "  " + date2);
                diff = -1;
            } else {
                // Log.d("DTGLogs", "DBdayDifference-->>else" + date1 + "  " + date2);
                diff = 1;
            }


        } catch (Exception e) {

        }

        return diff;
    }

    public static ConfigBean getConfigResponse() {
        Gson gson = new Gson();
        String json = KsPreferenceKeys.getInstance().getString("DMS_Response", "");
        return gson.fromJson(json, ConfigBean.class);
    }

    public static void setConfigConstant(ConfigBean configResponse, boolean isTablet) {
        Logger.w("configResponse", configResponse.getData().getAppConfig().getBaseUrl() + "  " + configResponse.getData().getAppConfig().getOvpBaseUrl());
        SDKConfig.getInstance().setConfigObject(configResponse);

    }

    public static String getHomeTabId(ConfigBean configBean, String name) {
        String screenId = "";
        if (configBean != null) {
            for (int i = 0; i < configBean.getData().getAppConfig().getNavScreens().size(); i++) {
                if (configBean.getData().getAppConfig().getNavScreens().get(i).getScreenName().equalsIgnoreCase(name)) {
                    screenId = String.valueOf(configBean.getData().getAppConfig().getNavScreens().get(i).getId());
                    break;
                }
            }
        }
        return screenId;
    }

    public static boolean getCheckBCID(String brightcoveVideoId) {
        return brightcoveVideoId != null && !brightcoveVideoId.equalsIgnoreCase("");

    }

    public static boolean getCheckKEntryId(String brightcoveVideoId) {
        return brightcoveVideoId != null && !brightcoveVideoId.equalsIgnoreCase("");

    }

    public static void handleTags(String isVIPTag, String isNewS, FrameLayout isVIP, FrameLayout newSeries, FrameLayout newEpisode, FrameLayout newMovie, String assetType) {
        try {
            if (isVIPTag.equalsIgnoreCase("true")) {
                isVIP.setVisibility(View.VISIBLE);
            } else {
                isVIP.setVisibility(View.GONE);
            }
            if (assetType.equalsIgnoreCase(MediaTypeConstants.getInstance().getSeries())) {
                if (isNewS.equalsIgnoreCase("true")) {
                    newSeries.setVisibility(View.VISIBLE);
                } else {
                    newSeries.setVisibility(View.GONE);
                }
            } else {
                newSeries.setVisibility(View.GONE);
            }

            if (assetType.equalsIgnoreCase(MediaTypeConstants.getInstance().getMovie())) {
                if (isNewS.equalsIgnoreCase("true")) {
                    newMovie.setVisibility(View.VISIBLE);
                } else {
                    newMovie.setVisibility(View.GONE);
                }
            } else {
                newMovie.setVisibility(View.GONE);
            }
            if (assetType.equalsIgnoreCase(MediaTypeConstants.getInstance().getEpisode())) {
                if (isNewS.equalsIgnoreCase("true")) {
                    newEpisode.setVisibility(View.VISIBLE);
                } else {
                    newEpisode.setVisibility(View.GONE);
                }
            } else {
                newEpisode.setVisibility(View.GONE);
            }
        } catch (Exception ignored) {

        }
    }

    public static JSONObject createNotificationObject(String notid, String assetType) {
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("contentType", assetType);
            jsonObject.put("id", notid);
        } catch (Exception ignored) {

        }

        return jsonObject;
    }

    ///// Create dynamic link object

    public static JSONObject createDynamicLinkObject(String id, String mediaType) {
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("contentType", mediaType);
            jsonObject.put("id", id);
        } catch (Exception ignored) {
            ignored.printStackTrace();

        }

        return jsonObject;
    }

    public static void handleTitleDesc(RelativeLayout titleLayout, TextView tvTitle, TextView tvDescription, BaseCategory baseCategory) {
        try {
            if (baseCategory != null) {
                if (baseCategory.getRailCardType().equalsIgnoreCase(RailCardType.IMAGE_ONLY.name())) {
                    titleLayout.setVisibility(View.VISIBLE);
                } else {
                    //titleLayout.setVisibility(View.VISIBLE);
                    if (baseCategory.getRailCardType().equalsIgnoreCase(RailCardType.IMAGE_TITLE.name())) {
                        titleLayout.setVisibility(View.VISIBLE);
                        tvTitle.setVisibility(View.VISIBLE);
                    } else {
                        if (baseCategory.getRailCardType().equalsIgnoreCase(RailCardType.IMAGE_TITLE_DESC.name())) {
                            titleLayout.setVisibility(View.VISIBLE);
                            tvTitle.setVisibility(View.VISIBLE);
                            tvDescription.setVisibility(View.VISIBLE);
                        } else {
                            titleLayout.setVisibility(View.GONE);
                            tvTitle.setVisibility(View.GONE);
                            tvDescription.setVisibility(View.GONE);
                        }
                    }

                }

            }
        } catch (Exception ignored) {
            titleLayout.setVisibility(View.GONE);
            tvTitle.setVisibility(View.GONE);
            tvDescription.setVisibility(View.GONE);
        }
    }

    public static String calculateTimeinMinutes(long milliseconds) {
        String minutes = "";
        int minutes1;
        int seconds;

        try {
           /* if (milliseconds % 1000 > 0) {
                milliseconds = milliseconds + (milliseconds % 1000);
            }*/


//            if (milliseconds/60 > 0){
//                minutes = String.valueOf(milliseconds/60);
//            }else {
//                minutes = String.valueOf(milliseconds).concat("s");
//            }

            int hours1 = (int) (milliseconds / 3600);
            Log.d("gygygyygyg",hours1+"");
            minutes1 = (int) ((milliseconds % 3600) / 60);
            seconds = (int) (milliseconds % 60);
            if (hours1>0){
                if (minutes1>0){
                    Log.d("gygygyygyg",minutes1+"");
                    int total = (hours1*60)+minutes1;
                    minutes = String.valueOf(total).concat(" ").concat("minutes");
                }else {
                    minutes = String.valueOf(hours1 * 60).concat(" ").concat("minutes");
                }
            }else {
                if (minutes1 > 0) {
                    if (minutes1 < 2) {
                        minutes = String.valueOf(minutes1).concat(" ").concat("minute");
                    } else {
                        minutes = String.valueOf(minutes1).concat(" ").concat("minutes");
                    }
                } else if (seconds > 0) {
                    minutes = String.valueOf(seconds).concat("s");
                }
            }

            // String timeString = String.format("%02d:%02d:%02d", hours1, minutes1, seconds);


//            long hours = TimeUnit.SECONDS.toHours(milliseconds);
//            long minute = TimeUnit.SECONDS.toMinutes(milliseconds);
//            long second = TimeUnit.SECONDS.toSeconds(milliseconds) % TimeUnit.MINUTES.toSeconds(1);
//
//            Log.w("episodeTiming", minute + "   ---   " + milliseconds);
//            minutes = String.format("%02d", minute);

        } catch (Exception ignored) {

        }
        return minutes;
    }

    public static double getPlanExpiry(DataItem dataItem) {
        double expiry = 0;
        try {
            if (dataItem != null && dataItem.getExpiryDate() != null) {
                expiry = (double) dataItem.getExpiryDate();
            }
        } catch (Exception ignored) {
            expiry = 0;
        }

        return expiry;
    }

    public static String getDateFromTimeStamp(double expiryDate) {
        String date = "";

        SimpleDateFormat formatter = new SimpleDateFormat("MM/dd/yyyy");
        date = formatter.format(new Date((long) expiryDate));
        Log.w("expiryDate", date);
        return date;
    }

    static Uri dynamicLinkUri;

    public static void openShareDialog(Activity activity, String title, int assetId, String assetType, String imgUrl, String seasonNumber) {


        try {
            //String imageURL = imgUrl + AppConstants.WIDTH + (int) activity.getResources().getDimension(R.dimen.width1) + AppConstants.HEIGHT + (int) activity.getResources().getDimension(R.dimen.height1) + AppConstants.QUALITY_IMAGE;
            //  Log.e("FinalUrl-->>in", imageURL);
            // Log.e("ImageUrl-->>in", imgUrl);
            String uri = createURI(title, assetId, assetType, imgUrl);


            Task<ShortDynamicLink> shortLinkTask = FirebaseDynamicLinks.getInstance().createDynamicLink()
                    // .setDomainUriPrefix("https://link.panteao.com")
                    .setLink(Uri.parse(uri))
                    .setDomainUriPrefix(FirebaseConstants.FIREBASE_DPLNK_PREFIX)
                    //.setLink(Uri.parse(uri))
                    .setNavigationInfoParameters(new DynamicLink.NavigationInfoParameters.Builder().setForcedRedirectEnabled(true)
                            .build())
                    .setAndroidParameters(new DynamicLink.AndroidParameters.Builder(FirebaseConstants.FIREBASE_ANDROID_PACKAGE)
                            .build())
                    .setIosParameters(new DynamicLink.IosParameters.Builder(FirebaseConstants.FIREBASE_IOS_PACKAGE).build())
                    .setSocialMetaTagParameters(
                            new DynamicLink.SocialMetaTagParameters.Builder()
                                    .setTitle(title)
                                    .setDescription(seasonNumber)
                                    .setImageUrl(Uri.parse(imgUrl))
                                    .build())
                    .buildShortDynamicLink(ShortDynamicLink.Suffix.SHORT)
                    .addOnCompleteListener(activity, new OnCompleteListener<ShortDynamicLink>() {
                        @Override
                        public void onComplete(@NonNull Task<ShortDynamicLink> task) {
                            if (task.isSuccessful()) {

                                dynamicLinkUri = task.getResult().getShortLink();
                                Uri flowchartLink = task.getResult().getPreviewLink();
                                Log.e("dynamicUrl", dynamicLinkUri.toString() + flowchartLink);
                                // Log.e("flowchartLink", String.valueOf(flowchartLink));
                                try {
                                    activity.runOnUiThread(new Runnable() {
                                        @Override
                                        public void run() {
                                            if (dynamicLinkUri != null) {
                                                Intent sharingIntent = new Intent(Intent.ACTION_SEND);
                                                sharingIntent.setType("text/plain");
                                                sharingIntent.putExtra(Intent.EXTRA_TEXT, activity.getResources().getString(R.string.checkout) + " " + title + " " + activity.getResources().getString(R.string.on_enveu) + "\n" + dynamicLinkUri.toString());
                                                // sharingIntent.putExtra(Intent.EXTRA_TEXT, activity.getResources().getString(R.string.checkout) + " " + asset.getName() + " " + activity.getResources().getString(R.string.on_Dialog) + "\n" + "https://stagingsott.page.link/?link="+dynamicLinkUri.toString()+"&apn=com.astro.stagingsott");


                                                activity.startActivity(Intent.createChooser(sharingIntent, activity.getResources().getString(R.string.share)));

                                            }

                                        }
                                    });
                                } catch (Exception ignored) {

                                }


                            } else {
                                // Log.w("dynamicUrl",dynamicLinkUri.toString());
                            }
                        }
                    });

            shortLinkTask.toString();

        } catch (Exception ignored) {
            Log.w("appcrashOnShare", ignored.getMessage());
        }
    }

    private static String createURI(String title, int assetId, String assetType, String imgUrl1) {
        String uri = "";
        try {
            String assetId1 = assetId + "";
            String assetType1 = assetType + "";
            uri = Uri.parse(FirebaseConstants.FIREBASE_DPLNK_URL)
                    .buildUpon()
                    .appendQueryParameter("id", assetId1)
                    .appendQueryParameter("mediaType", assetType1)
                    .appendQueryParameter("image", imgUrl1)
                    .appendQueryParameter("name", title)
                    .appendQueryParameter("apn", FirebaseConstants.FIREBASE_ANDROID_PACKAGE)
                    .build().toString();

        } catch (Exception ignored) {
            uri = "";
        }

        return uri;
    }

    public static boolean isTV(Context context) {
        return !context.getPackageManager().hasSystemFeature(FEATURE_TOUCHSCREEN_MULTITOUCH);
    }

    public static float dptoPx(Context context, int i) {
        return i * (((float) context.getResources().getDisplayMetrics().densityDpi) / DisplayMetrics.DENSITY_DEFAULT);

    }

    public static KalturaOvpPlayer loadPlayer(Context context, FrameLayout view) {
        PlayerInitOptions playerInitOptions = new PlayerInitOptions(KalturaPlayerActivity.Companion.getPARTNER_ID());
        playerInitOptions.setAutoPlay(true);
        playerInitOptions.setAspectRatioResizeMode(PKAspectRatioResizeMode.fill);
        KalturaOvpPlayer player = KalturaOvpPlayer.create(context, playerInitOptions);
        player.setPlayerView(FrameLayout.LayoutParams.WRAP_CONTENT, FrameLayout.LayoutParams.WRAP_CONTENT);
        view.addView(player.getPlayerView());
        return player;
    }

    public static OVPMediaOptions buildOvpMediaOptions(String entryId, long StartPosition) {
        OVPMediaAsset ovpMediaAsset = new OVPMediaAsset();
        ovpMediaAsset.setEntryId(entryId);
        ovpMediaAsset.setKs(null);
        ovpMediaAsset.setRedirectFromEntryId(true);
        OVPMediaOptions ovpMediaOptions = new OVPMediaOptions(ovpMediaAsset);
        ovpMediaOptions.startPosition = StartPosition;
        return ovpMediaOptions;
    }

    public static void launchDetailScreenWithKID(Context context, String videoId, String screenType, int id, String duration, boolean isPremium) {
        if (SystemClock.elapsedRealtime() - mLastClickTime < 2000) {
            return;
        }
        mLastClickTime = SystemClock.elapsedRealtime();

        if (screenType.toUpperCase().equalsIgnoreCase(MediaTypeConstants.getInstance().getShow())) {
            if (SDKConfig.getInstance().getShowDetailId().equalsIgnoreCase("")) {
                //new ActivityLauncher((BaseActivity) context).detailScreenBrightCove((BaseActivity) context, DetailActivity.class, videoId, id, duration, isPremium, AppConstants.SHOW_ENVEU);
            } else {
                //new ActivityLauncher((BaseActivity) context).detailScreenBrightCove((BaseActivity) context, InstructorActivity.class, videoId, id, duration, isPremium, SDKConfig.getInstance().getShowDetailId());
                new ActivityLauncher((BaseActivity) context).ShowScreenBrightCove((BaseActivity) context, ShowActivity.class, videoId, id, duration, isPremium, SDKConfig.getInstance().getShowDetailId());
            }
        } else if (screenType.toUpperCase().equalsIgnoreCase(MediaTypeConstants.getInstance().getEpisode())) {
            new ActivityLauncher((BaseActivity) context).episodeScreenBrightcove((BaseActivity) context, EpisodeActivity.class, videoId, id, duration, isPremium);
        } else if (screenType.toUpperCase().equalsIgnoreCase(MediaTypeConstants.getInstance().getSeries())) {
            new ActivityLauncher((BaseActivity) context).seriesDetailScreen((BaseActivity) context, SeriesDetailActivity.class, id);
        } else if (screenType.toUpperCase().equalsIgnoreCase(MediaTypeConstants.getInstance().getLive()) || screenType.equalsIgnoreCase("LIVE")) {
            if (SDKConfig.getInstance().getLiveDetailId().equalsIgnoreCase("")) {
                // new ActivityLauncher((BaseActivity) context).detailScreenBrightCove((BaseActivity) context, DetailActivity.class, videoId, id, duration, isPremium, AppConstants.MOVIE_ENVEU);
            } else {
                // new ActivityLauncher((BaseActivity) context).liveScreenBrightCove((BaseActivity) context, LiveActivity.class, videoId, id, duration, isPremium, SDKConfig.getInstance().getMovieDetailId());
            }
        } else if (screenType.toUpperCase().equalsIgnoreCase(MediaTypeConstants.getInstance().getInstructor()) || screenType.equalsIgnoreCase("INSTRUCTOR")) {
            if (SDKConfig.getInstance().getInstructorDetaildId().equalsIgnoreCase("")) {
                // new ActivityLauncher((BaseActivity) context).detailScreenBrightCove((BaseActivity) context, DetailActivity.class, videoId, id, duration, isPremium, AppConstants.MOVIE_ENVEU);
            } else {
                new ActivityLauncher((BaseActivity) context).detailScreenBrightCove((BaseActivity) context, InstructorActivity.class, videoId, id, duration, isPremium, SDKConfig.getInstance().getMovieDetailId());
            }
        } else if (screenType.toUpperCase().equalsIgnoreCase(MediaTypeConstants.getInstance().getChapter()) || screenType.equalsIgnoreCase("CHAPTER")) {
            if (SDKConfig.getInstance().getInstructorDetaildId().equalsIgnoreCase("")) {
                // new ActivityLauncher((BaseActivity) context).detailScreenBrightCove((BaseActivity) context, DetailActivity.class, videoId, id, duration, isPremium, AppConstants.MOVIE_ENVEU);
            } else {
                new ActivityLauncher((BaseActivity) context).chapterScreenBrightcove((BaseActivity) context, ChapterActivity.class, videoId, id, duration, isPremium);
            }
        } else if (screenType.toUpperCase().equalsIgnoreCase(MediaTypeConstants.getInstance().getTutorial())) {
            new ActivityLauncher((BaseActivity) context).tutorialDetailScreen((BaseActivity) context, TutorialActivity.class, id);
        }
        // RecoSenceManager.getInstance().sendClickEvent(screenType, id);

/*
        switch (screenType.toUpperCase()) {
            case MediaTypeConstants.Show:
                if (SDKConfig.getInstance().getShowDetailId().equalsIgnoreCase("")){
                    //new ActivityLauncher((BaseActivity) context).detailScreenBrightCove((BaseActivity) context, DetailActivity.class, videoId, id, duration, isPremium, AppConstants.SHOW_ENVEU);
                }else {
                    new ActivityLauncher((BaseActivity) context).detailScreenBrightCove((BaseActivity) context, DetailActivity.class, videoId, id, duration, isPremium, SDKConfig.getInstance().getShowDetailId());
                }

                break;
            case MediaTypeConstants.Episode:
                new ActivityLauncher((BaseActivity) context).episodeScreenBrightcove((BaseActivity) context, EpisodeActivity.class, videoId, id, duration, isPremium);
                break;
            case MediaTypeConstants.Series:
                new ActivityLauncher((BaseActivity) context).seriesDetailScreen((BaseActivity) context, SeriesDetailActivity.class, id);
                break;
            case MediaTypeConstants.Live:
                if (SDKConfig.getInstance().getMovieDetailId().equalsIgnoreCase("")){
                   // new ActivityLauncher((BaseActivity) context).detailScreenBrightCove((BaseActivity) context, DetailActivity.class, videoId, id, duration, isPremium, AppConstants.MOVIE_ENVEU);
                }else {
                    new ActivityLauncher((BaseActivity) context).liveScreenBrightCove((BaseActivity) context, LiveActivity.class, videoId, id, duration, isPremium, SDKConfig.getInstance().getMovieDetailId());
                }

                break;
            case MediaTypeConstants.Article:
                if (SDKConfig.getInstance().getMovieDetailId().equalsIgnoreCase("")){
                   // new ActivityLauncher((BaseActivity) context).detailScreenBrightCove((BaseActivity) context, DetailActivity.class, videoId, id, duration, isPremium, AppConstants.MOVIE_ENVEU);
                }else {
                    new ActivityLauncher((BaseActivity) context).articleScreen((BaseActivity) context, ArticleActivity.class, id, "0", false);
                }

                break;
            default:
                if (SDKConfig.getInstance().getMovieDetailId().equalsIgnoreCase("")){
                   // new ActivityLauncher((BaseActivity) context).detailScreenBrightCove((BaseActivity) context, DetailActivity.class, videoId, id, duration, isPremium, AppConstants.MOVIE_ENVEU);
                }else {
                    new ActivityLauncher((BaseActivity) context).detailScreenBrightCove((BaseActivity) context, DetailActivity.class, videoId, id, duration, isPremium, SDKConfig.getInstance().getMovieDetailId());
                }
                break;
        }
*/
    }


    public static DownloadStatus getDownloadStatus(OfflineManager.AssetDownloadState downloadState) {
        if (downloadState == null) {
            return DownloadStatus.START;
        }
        if (downloadState.name().equalsIgnoreCase("paused")) {
            return panteao.make.ready.enums.DownloadStatus.PAUSE;
        } else if (downloadState.name().equalsIgnoreCase("started")) {
            return DownloadStatus.DOWNLOADING;
        } else if (downloadState.name().equalsIgnoreCase("completed")) {
            return DownloadStatus.DOWNLOADED;
        }
        return DownloadStatus.START;
    }

    static boolean track1, track2, track3 = false;

    public static ArrayList<TracksItem> createTrackList(PKTracks tracks, FragmentActivity activity,String typeofTVOD) {
        track1 = false;
        track2 = false;
        track3 = false;
        ArrayList<TracksItem> trackItemList = new ArrayList<TracksItem>();
        for (int i = 0; i < tracks.getVideoTracks().size(); i++) {

            VideoTrack videoTrackInfo = tracks.getVideoTracks().get(i);
            if (videoTrackInfo.isAdaptive()) {
                trackItemList.add(new TracksItem(activity.getResources().getString(R.string.auto), videoTrackInfo.getUniqueId()));
            } else if (videoTrackInfo.getBitrate() > 100000 && videoTrackInfo.getBitrate() < 450000 && !track1) {
                track1 = true;
                trackItemList.add(new TracksItem(activity.getResources().getString(R.string.sd), videoTrackInfo.getUniqueId()));
            } else if ((videoTrackInfo.getBitrate() > 450001 && videoTrackInfo.getBitrate() < 600000) && !track2 || (videoTrackInfo.getBitrate() > 400000 && videoTrackInfo.getBitrate() < 620000) && !track2) {
                track2 = true;
                trackItemList.add(new TracksItem(activity.getResources().getString(R.string.hd), videoTrackInfo.getUniqueId()));
            } else if (videoTrackInfo.getBitrate() > 600001 && videoTrackInfo.getBitrate() < 1000000 && !track3) {
                track3 = true;
                trackItemList.add(new TracksItem(activity.getResources().getString(R.string.uhd), videoTrackInfo.getUniqueId()));
            }
        }
        if (typeofTVOD!=null && !typeofTVOD.equalsIgnoreCase("") && trackItemList.size()>0){
            trackItemList=basedOnSubscription(trackItemList,typeofTVOD);
        }
        return trackItemList;
    }

    private static ArrayList<TracksItem> basedOnSubscription(ArrayList<TracksItem> tracks,String typeofTVOD) {
        ArrayList<TracksItem> trackItemList = new ArrayList<TracksItem>();
        for (int i = 0; i < tracks.size(); i++) {
            if (typeofTVOD.equalsIgnoreCase(TVODENUMS.___sd.name())){
                if (tracks.get(i).getTrackName().equalsIgnoreCase("SD")){
                    trackItemList.add(tracks.get(i));
                }else if (tracks.get(i).getTrackName().equalsIgnoreCase("Auto")){
                    trackItemList.add(tracks.get(i));
                }
            }else if (typeofTVOD.equalsIgnoreCase(TVODENUMS.___hd.name())){
                if (tracks.get(i).getTrackName().equalsIgnoreCase("SD")){
                    trackItemList.add(tracks.get(i));
                }else if (tracks.get(i).getTrackName().equalsIgnoreCase("HD")){
                    trackItemList.add(tracks.get(i));
                }else if (tracks.get(i).getTrackName().equalsIgnoreCase("Auto")){
                    trackItemList.add(tracks.get(i));
                }
            }else if (typeofTVOD.equalsIgnoreCase(TVODENUMS.___uhd.name())){
                trackItemList.add(tracks.get(i));
            }
        }
        return trackItemList;
    }


    @NotNull
    public static ArrayList<DownloadItemEntity> getSortedList(@NotNull List<? extends DownloadItemEntity> it) {
        ArrayList<DownloadItemEntity> list=new ArrayList<>();
        ArrayList<String> str=new ArrayList<>();
        int temp = 1;
        HashMap map=new HashMap();

        for (int i=0;i<it.size();i++){
            boolean notFound=false;
            //Log.w("CheckingCondition 1","isSeries-->>"+i+"<<----->>"+it.get(i).getSeriesId());
            //Log.w("CheckingCondition 1","isSeries-->>"+it.size());
            str.add(it.get(i).getSeriesIdWithNumber());
            if (list.size()>0) {
                DownloadItemEntity value = it.get(i);
                //Log.w("CheckingCondition 1","isSeries-->>"+value.isSeries()+" "+value.getSeriesId());
                if (value.isSeries()) {
                    String seriesId = value.getSeriesId();
                   // Log.w("CheckingCondition 1","isSeries-->>"+value.isSeries()+" "+seriesId);
                    int seasonNumber = value.getSeasonNumber();
                   // Log.w("CheckingCondition 2","seriesid-->>"+value.getSeriesId());
                    for (int j = 0; j < list.size(); j++) {
                        DownloadItemEntity value2 = list.get(j);
                        String seriesId2 = value2.getSeriesId();
                        int seasonNumber2 = value2.getSeasonNumber();
                       // Log.w("CheckingCondition 3","seriesid2-->>"+value.getSeriesId()+"  "+seriesId+"<<---->>"+seriesId2+"   "+value2.isSeries());
                        // if (value2.isSeries()) {
                        if (seriesId.equalsIgnoreCase(seriesId2) && seasonNumber==seasonNumber2) {
                         //   Log.w("CheckingCondition 4","in-->>"+seriesId+"<<---->>"+seriesId2+"  "+temp);
                            notFound=true;
                        }
                        // }
                    }
                    if (!notFound){
                        list.add(value);
                    }
                }else {
                    list.add(value);
                }
            }else {
                list.add(it.get(0));
            }

        }
        AppCommonMethod.occuranceList=str;
        return getSortedList(list);
    }

    private static void checkAccurances(ArrayList<String> str, String i) {
        int occurrences = Collections.frequency(str, i);
        Log.w("occurances-->",occurrences+"");
    }


    private static ArrayList<DownloadItemEntity> getSortedListBasedOnepisodeNo(ArrayList<DownloadItemEntity> list) {
        Collections.sort(list, new Comparator<DownloadItemEntity>() {
            @Override
            public int compare(DownloadItemEntity o1, DownloadItemEntity o2) {
                return ((o1.getEpisodeNumber().charAt(1)-o2.getEpisodeNumber().charAt(1))*10 + o1.getEpisodeNumber().charAt(0)-o2.getEpisodeNumber().charAt(0));
            }
        });
        //  Collections.sort(list, (o1, o2) -> o1.getTimeStamp().compareTo(o2.getTimeStamp()));
        return list;
    }


    private static ArrayList<DownloadItemEntity> getSortedList(ArrayList<DownloadItemEntity> list) {
        Collections.sort(list, new Comparator<DownloadItemEntity>() {
            @Override
            public int compare(DownloadItemEntity o1, DownloadItemEntity o2) {
                return Long.compare(o2.getTimeStamp(), o1.getTimeStamp());
            }
        });
        //  Collections.sort(list, (o1, o2) -> o1.getTimeStamp().compareTo(o2.getTimeStamp()));
        return list;
    }

    @NotNull
    public static ArrayList<DownloadItemEntity> getEpisodesSortedList(@NotNull List<? extends DownloadItemEntity> it) {
        ArrayList<DownloadItemEntity> list=new ArrayList<>();
        for (int i=0;i<it.size();i++){
            if (list.size()>0) {
                DownloadItemEntity value = it.get(i);
                Log.d("episodeNumber",value.getEpisodeNumber());
                list.add(value);
            }else {
                Log.d("episodeNumber",it.get(0).getEpisodeNumber());
                list.add(it.get(0));
            }
        }
        return getSortedListBasedOnepisodeNo(list);
    }

    @NotNull
    public static List<DownloadItemEntity> getSortedListByTimeStamp(@NotNull List<DownloadItemEntity> list) {
        Collections.sort(list, new Comparator<DownloadItemEntity>() {
            @Override
            public int compare(DownloadItemEntity o1, DownloadItemEntity o2) {
                return Long.compare(o2.getTimeStamp(), o1.getTimeStamp());
            }
        });
        return list;
    }

    @NotNull
    public static ArrayList<DownloadItemEntity> getSortedChapters(@NotNull ArrayList<DownloadItemEntity> list) {
        Collections.sort(list, new Comparator<DownloadItemEntity>() {
            @Override
            public int compare(DownloadItemEntity o1, DownloadItemEntity o2) {
                return Long.compare(o2.getTimeStamp(), o1.getTimeStamp());
            }
        });
        return list;
    }

    @NotNull
    public static ArrayList<DownloadItemEntity> getSortChapters(@NotNull ArrayList<DownloadItemEntity> list) {
        Collections.sort(list, new Comparator<DownloadItemEntity>() {
            @Override
            public int compare(DownloadItemEntity o1, DownloadItemEntity o2) {
                return Long.compare(o1.getTimeStamp(), o2.getTimeStamp());
            }
        });
        return list;
    }

    private static String formattedDate;
    public static String getCurrentDateTimeStamp(int type) {
        Calendar calendar = Calendar.getInstance();

        Date today = calendar.getTime();

        //Log.w("downloadExpiry-->",SDKConfig.DOWNLOAD_EXPIRY_DAYS+"");
        calendar.add(Calendar.DAY_OF_YEAR, SDKConfig.DOWNLOAD_EXPIRY_DAYS);
        Date tomorrow = calendar.getTime();

        if (type == 1) {
            SimpleDateFormat df = new SimpleDateFormat("MMM dd, yyyy");
            formattedDate = df.format(today);
            PrintLogging.printLog("printDatedate" + formattedDate + "-->>");
        } else {
            SimpleDateFormat df = new SimpleDateFormat("MMM dd, yyyy");
            formattedDate = df.format(tomorrow);
            PrintLogging.printLog("printDatedate" + formattedDate + "-->>");

        }
        calendar.clear();
        return getTimeStamp(formattedDate, type);
    }
    private static final String startTime = " 00:00:00 AM";
    private static String getTimeStamp(String todayDate, int type) {
        long timestamp = 0;
        String dateStr;
        if (type == 1) {
            dateStr = todayDate + startTime;
        } else {
            dateStr = todayDate + startTime;
        }

        DateFormat readFormat = new SimpleDateFormat("MMM dd, yyyy hh:mm:ss aa");
        DateFormat writeFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date date = null;
        try {
            date = readFormat.parse(dateStr);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        String formattedDate = "";
        if (date != null) {
            formattedDate = writeFormat.format(date);
        }

        if (date == null) {

        } else {
            long output = date.getTime() / 1000L;
            String str = Long.toString(output);
            timestamp = Long.parseLong(str);
            PrintLogging.printLog("printDatedate" + formattedDate + "-->>" + timestamp);
            System.out.println(formattedDate);
        }
        return String.valueOf(timestamp);
    }

    public static int getIndex(List<String> subList,String identifier) {
        int returnIndex=-1;
        for (int i=0;i<subList.size();i++){
            String s=identifier.replace("svod_","");
            String v=s.replace("_",".");
            if (v.equalsIgnoreCase(subList.get(i))){
                returnIndex=i;
            }
        }
        return returnIndex;
    }

    public static int getProductIndex(List<String> subList,String identifier) {
        int returnIndex=-1;
        for (int i=0;i<subList.size();i++){
            if (identifier.equalsIgnoreCase(subList.get(i))){
                returnIndex=i;
            }
        }
        return returnIndex;
    }

    public static void setDownloadExpiry(RentalPeriod rentalPeriod) {
        try {
            if(rentalPeriod!=null){
                if (rentalPeriod.getPeriodType().contains(VodOfferType.DAYS.name())){
                    int periodlenth=rentalPeriod.getPeriodLength();
                    if (periodlenth>0){
                        SDKConfig.DOWNLOAD_EXPIRY_DAYS=periodlenth;
                    }
                }else if (rentalPeriod.getPeriodType().contains(VodOfferType.WEEKS.name())){
                    int periodLenth=rentalPeriod.getPeriodLength();
                    int weekDays=7;
                    int days=periodLenth*weekDays;
                    SDKConfig.DOWNLOAD_EXPIRY_DAYS=days;
                }
                else if (rentalPeriod.getPeriodType().contains(VodOfferType.MONTHS.name())){
                    int periodLenth=rentalPeriod.getPeriodLength();
                    int monthDays=30;
                    int days=periodLenth*monthDays;
                    SDKConfig.DOWNLOAD_EXPIRY_DAYS=days;
                }else {
                    SDKConfig.DOWNLOAD_EXPIRY_DAYS=30;
                }
            }else {
                SDKConfig.DOWNLOAD_EXPIRY_DAYS=30;
            }
        }catch (Exception e){
            SDKConfig.DOWNLOAD_EXPIRY_DAYS=30;
        }
    }

    public static int getDownloadPosition(List<EnveuVideoItemBean> adapterList,String videoId) {
        int position=0;
        for (int i=0;i<adapterList.size();i++){
            String kentryId=adapterList.get(i).getkEntryId();
            if (videoId!=null && !videoId.equalsIgnoreCase("")){
                if (kentryId.equalsIgnoreCase(videoId)){
                    position=i;
                    break;
                }
            }
        }
        return position;
    }

    public static String getMyPurchaseTimeTodate(double expiryDate) {
        String date = "";

        SimpleDateFormat formatter = new SimpleDateFormat("EEE dd/MM/yyyy");
        date = formatter.format(new Date((long) expiryDate));
        Log.w("expiryDate", date);
        return date;
    }

}