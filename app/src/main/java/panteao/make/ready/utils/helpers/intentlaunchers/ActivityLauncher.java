package panteao.make.ready.utils.helpers.intentlaunchers;

import android.app.Activity;
import android.app.TaskStackBuilder;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import com.make.baseCollection.baseCategoryModel.BaseCategory;
import panteao.make.ready.activities.article.ArticleActivity;
import panteao.make.ready.activities.instructor.ui.InstructorActivity;
import panteao.make.ready.activities.show.ui.EpisodeActivity;
import panteao.make.ready.activities.homeactivity.ui.HomeActivity;
import panteao.make.ready.activities.show.ui.ShowActivity;
import panteao.make.ready.activities.listing.listui.ListActivity;
import panteao.make.ready.activities.listing.ui.GridActivity;
import panteao.make.ready.activities.live.LiveActivity;
import panteao.make.ready.activities.notification.ui.NotificationActivity;
import panteao.make.ready.activities.profile.ui.ProfileActivity;
import panteao.make.ready.activities.profile.ui.ProfileActivityNew;
import panteao.make.ready.activities.search.ui.ActivityResults;
import panteao.make.ready.activities.series.ui.SeriesDetailActivity;
import panteao.make.ready.activities.tutorial.ui.ChapterActivity;
import panteao.make.ready.activities.tutorial.ui.TutorialActivity;
import panteao.make.ready.activities.usermanagment.ui.ChangePasswordActivity;
import panteao.make.ready.activities.usermanagment.ui.ForceLoginFbActivity;
import panteao.make.ready.activities.usermanagment.ui.ForgotPasswordActivity;
import panteao.make.ready.activities.usermanagment.ui.LoginActivity;
import panteao.make.ready.activities.usermanagment.ui.SignUpActivity;
import panteao.make.ready.activities.usermanagment.ui.SkipActivity;
import panteao.make.ready.activities.watchList.ui.WatchListActivity;
import panteao.make.ready.beanModel.responseModels.SignUp.DataModel;
import panteao.make.ready.utils.constants.AppConstants;
import panteao.make.ready.utils.cropImage.helpers.Logger;
import panteao.make.ready.utils.helpers.ADHelper;
import panteao.make.ready.utils.helpers.downloads.downloadListing.MyDownloadsNewActivity;
import panteao.make.ready.utils.helpers.downloads.offlinePlayer.OfflinePlayerActivity;
import panteao.make.ready.utils.helpers.ksPreferenceKeys.KsPreferenceKeys;
import panteao.make.ready.activities.search.ui.ActivitySearch;

import panteao.make.ready.utils.helpers.StringUtils;
import com.google.gson.Gson;

import org.jetbrains.annotations.Nullable;

import static android.content.Intent.FLAG_ACTIVITY_CLEAR_TOP;
import static android.content.Intent.FLAG_ACTIVITY_NEW_TASK;


public class ActivityLauncher {
    private final Activity activity;

    public ActivityLauncher(Activity context) {
        this.activity = context;
    }

    public void signUpActivity(Activity source, Class<SignUpActivity> destination, String from) {
        Intent intent = new Intent(source, destination);
        intent.putExtra("loginFrom",from);
        activity.startActivity(intent);
    }

    public void skipActivity(Activity source, Class<SkipActivity> destination, DataModel model) {

        Bundle args = new Bundle();
        args.putParcelable(AppConstants.STRING_USER_DETAIL, model);
        Intent intent = new Intent(source, destination);
        intent.putExtra(AppConstants.EXTRA_REGISTER_USER, args);

        activity.startActivity(intent);
    }

    public void ProfileActivity(Activity source, Class<ProfileActivity> destination) {

        Intent intent = new Intent(source, destination);
        activity.startActivity(intent);
    }

    public void ProfileActivityNew(Activity source, Class<ProfileActivityNew> destination) {

        Intent intent = new Intent(source, destination);
        activity.startActivity(intent);
    }

    public void changePassword(Activity source, Class<ChangePasswordActivity> destination) {
        Intent intent = new Intent(source, destination);
        activity.startActivity(intent);

    }

    public void loginActivity(Activity source, Class<LoginActivity> destination) {
        if (source instanceof HomeActivity){
            Intent intent = new Intent(source, destination);
            intent.putExtra("loginFrom","home");
            activity.startActivity(intent);
        }else {
            Intent intent = new Intent(source, destination);
            intent.putExtra("loginFrom","");
            activity.startActivity(intent);
        }


    }

    public void searchActivity(Activity source, Class<ActivitySearch> destination) {
        Intent intent = new Intent(source, destination);
        activity.startActivity(intent);
    }

    public void homeScreen(Activity source, Class<HomeActivity> destination) {
        Intent intent = new Intent(source, destination);
        TaskStackBuilder.create(source).addNextIntentWithParentStack(intent).startActivities();
    }

    public void showScreen(Activity source, Class<ShowActivity> destination, int id, String duration, boolean isPremium) {
        Bundle args = new Bundle();
        args.putInt(AppConstants.BUNDLE_ASSET_ID, id);
        args.putInt(AppConstants.BUNDLE_VIDEO_ID_BRIGHTCOVE, id);

        args.putBoolean(AppConstants.BUNDLE_IS_PREMIUM, isPremium);
        if (StringUtils.isNullOrEmpty(duration))
            args.putString(AppConstants.BUNDLE_DURATION, "0");
        else
            args.putString(AppConstants.BUNDLE_DURATION, duration);
        Intent intent = new Intent(source, destination);
        intent.putExtra(AppConstants.BUNDLE_ASSET_BUNDLE, args);
        intent.setFlags(FLAG_ACTIVITY_CLEAR_TOP);
        intent.setFlags(FLAG_ACTIVITY_NEW_TASK);
        KsPreferenceKeys preference = KsPreferenceKeys.getInstance();
        preference.setAppPrefAssetId(0);
        if (ADHelper.getInstance(activity).getPipAct()!=null){
            ADHelper.getInstance(activity).getPipAct().moveTaskToBack(false);
            ADHelper.getInstance(activity).getPipAct().finish();
            // intent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
        }
        activity.startActivity(intent);
    }

    public void detailScreen(Activity source, Class<InstructorActivity> destination, int id, String duration, boolean isPremium) {
        Bundle args = new Bundle();
        args.putInt(AppConstants.BUNDLE_ASSET_ID, id);
        args.putInt(AppConstants.BUNDLE_VIDEO_ID_BRIGHTCOVE, id);

        args.putBoolean(AppConstants.BUNDLE_IS_PREMIUM, isPremium);
        if (StringUtils.isNullOrEmpty(duration))
            args.putString(AppConstants.BUNDLE_DURATION, "0");
        else
            args.putString(AppConstants.BUNDLE_DURATION, duration);
        Intent intent = new Intent(source, destination);
        intent.putExtra(AppConstants.BUNDLE_ASSET_BUNDLE, args);
        intent.setFlags(FLAG_ACTIVITY_CLEAR_TOP);
        intent.setFlags(FLAG_ACTIVITY_NEW_TASK);
        KsPreferenceKeys preference = KsPreferenceKeys.getInstance();
        preference.setAppPrefAssetId(0);
        if (ADHelper.getInstance(activity).getPipAct()!=null){
            ADHelper.getInstance(activity).getPipAct().moveTaskToBack(false);
            ADHelper.getInstance(activity).getPipAct().finish();
           // intent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
        }
        activity.startActivity(intent);
    }


    public void chapterScreen(Activity source, Class<ChapterActivity> destination, int id, String duration, boolean isPremium) {
        Bundle args = new Bundle();
        args.putInt(AppConstants.BUNDLE_ASSET_ID, id);
        args.putInt(AppConstants.BUNDLE_VIDEO_ID_BRIGHTCOVE, id);

        args.putBoolean(AppConstants.BUNDLE_IS_PREMIUM, isPremium);
        if (StringUtils.isNullOrEmpty(duration))
            args.putString(AppConstants.BUNDLE_DURATION, "0");
        else
            args.putString(AppConstants.BUNDLE_DURATION, duration);
        Intent intent = new Intent(source, destination);
        intent.putExtra(AppConstants.BUNDLE_ASSET_BUNDLE, args);
        intent.setFlags(FLAG_ACTIVITY_CLEAR_TOP);
        intent.setFlags(FLAG_ACTIVITY_NEW_TASK);
        KsPreferenceKeys preference = KsPreferenceKeys.getInstance();
        preference.setAppPrefAssetId(0);
        if (ADHelper.getInstance(activity).getPipAct()!=null){
            ADHelper.getInstance(activity).getPipAct().moveTaskToBack(false);
            ADHelper.getInstance(activity).getPipAct().finish();
            // intent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
        }
        activity.startActivity(intent);
    }

    public void articleScreen(Activity source, Class<ArticleActivity> destination, int id, String duration, boolean isPremium) {
        Bundle args = new Bundle();
        args.putInt(AppConstants.BUNDLE_ASSET_ID, id);
        args.putInt(AppConstants.BUNDLE_VIDEO_ID_BRIGHTCOVE, id);

        args.putBoolean(AppConstants.BUNDLE_IS_PREMIUM, isPremium);
        if (StringUtils.isNullOrEmpty(duration))
            args.putString(AppConstants.BUNDLE_DURATION, "0");
        else
            args.putString(AppConstants.BUNDLE_DURATION, duration);
        Intent intent = new Intent(source, destination);
        intent.putExtra(AppConstants.BUNDLE_ASSET_BUNDLE, args);
        intent.setFlags(FLAG_ACTIVITY_CLEAR_TOP);
        intent.setFlags(FLAG_ACTIVITY_NEW_TASK);
        KsPreferenceKeys preference = KsPreferenceKeys.getInstance();
        preference.setAppPrefAssetId(0);
        activity.startActivity(intent);
    }



    public void detailScreenBrightCove(Activity source, Class<InstructorActivity> destination, String videoId, int id, String duration, boolean isPremium, String detailType) {
        Bundle args = new Bundle();
        args.putInt(AppConstants.BUNDLE_ASSET_ID, id);
        args.putString(AppConstants.BUNDLE_VIDEO_ID_BRIGHTCOVE, videoId);

        args.putBoolean(AppConstants.BUNDLE_IS_PREMIUM, isPremium);
        args.putString(AppConstants.BUNDLE_DETAIL_TYPE, detailType);

        //this is for non-series launcher
        if (StringUtils.isNullOrEmpty(duration))
            args.putString(AppConstants.BUNDLE_DURATION, "0");
        else
            args.putString(AppConstants.BUNDLE_DURATION, duration);

        Intent intent = new Intent(source, destination);
        intent.putExtra(AppConstants.BUNDLE_ASSET_BUNDLE, args);
        intent.setFlags(FLAG_ACTIVITY_CLEAR_TOP);
        KsPreferenceKeys preference = KsPreferenceKeys.getInstance();
        preference.setAppPrefAssetId(0);
        if (ADHelper.getInstance(activity).getPipAct()!=null){
            ADHelper.getInstance(activity).getPipAct().moveTaskToBack(false);
            ADHelper.getInstance(activity).getPipAct().finish();
        }
        activity.startActivity(intent);
    }
    public void ShowScreenBrightCove(Activity source, Class<ShowActivity> destination, String videoId, int id, String duration, boolean isPremium, String detailType) {
        Bundle args = new Bundle();
        args.putInt(AppConstants.BUNDLE_ASSET_ID, id);
        args.putString(AppConstants.BUNDLE_VIDEO_ID_BRIGHTCOVE, videoId);

        args.putBoolean(AppConstants.BUNDLE_IS_PREMIUM, isPremium);
        args.putString(AppConstants.BUNDLE_DETAIL_TYPE, detailType);

        //this is for non-series launcher
        if (StringUtils.isNullOrEmpty(duration))
            args.putString(AppConstants.BUNDLE_DURATION, "0");
        else
            args.putString(AppConstants.BUNDLE_DURATION, duration);

        Intent intent = new Intent(source, destination);
        intent.putExtra(AppConstants.BUNDLE_ASSET_BUNDLE, args);
        intent.setFlags(FLAG_ACTIVITY_CLEAR_TOP);
        KsPreferenceKeys preference = KsPreferenceKeys.getInstance();
        preference.setAppPrefAssetId(0);
        Logger.e("JSON SENT",new Gson().toJson(args));
        if (ADHelper.getInstance(activity).getPipAct()!=null){
            ADHelper.getInstance(activity).getPipAct().moveTaskToBack(false);
            ADHelper.getInstance(activity).getPipAct().finish();
            // intent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
        }
        activity.startActivity(intent);


    }

    public void chapterScreenBrightcove(Activity source, Class<ChapterActivity> destination, String videoId, int id, String duration, boolean isPremium) {
        KsPreferenceKeys preference = KsPreferenceKeys.getInstance();

        Bundle args = new Bundle();
        args.putInt(AppConstants.BUNDLE_ASSET_ID, id);
        args.putString(AppConstants.BUNDLE_VIDEO_ID_BRIGHTCOVE, videoId);
        args.putBoolean(AppConstants.BUNDLE_IS_PREMIUM, isPremium);

        if (StringUtils.isNullOrEmpty(duration))
            args.putString(AppConstants.BUNDLE_DURATION, "0");
        else
            args.putString(AppConstants.BUNDLE_DURATION, duration);
        Intent intent = new Intent(source, destination);
        intent.putExtra(AppConstants.BUNDLE_ASSET_BUNDLE, args);
        intent.setFlags(FLAG_ACTIVITY_CLEAR_TOP);
        preference.setAppPrefAssetId(0);

        if (ADHelper.getInstance(activity).getPipAct()!=null){
            ADHelper.getInstance(activity).getPipAct().moveTaskToBack(false);
            ADHelper.getInstance(activity).getPipAct().finish();
            //intent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
        }

        activity.startActivity(intent);
    }


    public void liveScreenBrightCove(Activity source, Class<LiveActivity> destination, Long videoId, int id, String duration, boolean isPremium, String detailType) {
        Bundle args = new Bundle();
        args.putInt(AppConstants.BUNDLE_ASSET_ID, id);
        args.putLong(AppConstants.BUNDLE_VIDEO_ID_BRIGHTCOVE, videoId);

        args.putBoolean(AppConstants.BUNDLE_IS_PREMIUM, isPremium);
        args.putString(AppConstants.BUNDLE_DETAIL_TYPE, detailType);

        //this is for non-series launcher
        if (StringUtils.isNullOrEmpty(duration))
            args.putString(AppConstants.BUNDLE_DURATION, "0");
        else
            args.putString(AppConstants.BUNDLE_DURATION, duration);

        Intent intent = new Intent(source, destination);
        intent.putExtra(AppConstants.BUNDLE_ASSET_BUNDLE, args);
        intent.setFlags(FLAG_ACTIVITY_CLEAR_TOP);
        KsPreferenceKeys preference = KsPreferenceKeys.getInstance();
        preference.setAppPrefAssetId(0);
        Logger.e("JSON SENT",new Gson().toJson(args));
        activity.startActivity(intent);
    }


    public void episodeScreenBrightcove(Activity source, Class<EpisodeActivity> destination, String videoId, int id, String duration, boolean isPremium) {
        KsPreferenceKeys preference = KsPreferenceKeys.getInstance();

        Bundle args = new Bundle();
        args.putInt(AppConstants.BUNDLE_ASSET_ID, id);
        args.putString(AppConstants.BUNDLE_VIDEO_ID_BRIGHTCOVE, videoId);
        args.putBoolean(AppConstants.BUNDLE_IS_PREMIUM, isPremium);

        if (StringUtils.isNullOrEmpty(duration))
            args.putString(AppConstants.BUNDLE_DURATION, "0");
        else
            args.putString(AppConstants.BUNDLE_DURATION, duration);
        Intent intent = new Intent(source, destination);
        intent.putExtra(AppConstants.BUNDLE_ASSET_BUNDLE, args);
        intent.setFlags(FLAG_ACTIVITY_CLEAR_TOP);
        preference.setAppPrefAssetId(0);

        if (ADHelper.getInstance(activity).getPipAct()!=null){
            ADHelper.getInstance(activity).getPipAct().moveTaskToBack(false);
            ADHelper.getInstance(activity).getPipAct().finish();
            //intent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
        }

        activity.startActivity(intent);
    }


    public void episodeScreen(Activity source, Class<EpisodeActivity> destination, int id, String duration, boolean isPremium) {
        KsPreferenceKeys preference = KsPreferenceKeys.getInstance();

        Bundle args = new Bundle();
        args.putInt(AppConstants.BUNDLE_ASSET_ID, id);
        args.putBoolean(AppConstants.BUNDLE_IS_PREMIUM, isPremium);
        //this is for non-series launcher
        if (StringUtils.isNullOrEmpty(duration))
            args.putString(AppConstants.BUNDLE_DURATION, "0");
        else
            args.putString(AppConstants.BUNDLE_DURATION, duration);
        Intent intent = new Intent(source, destination);
        intent.putExtra(AppConstants.BUNDLE_ASSET_BUNDLE, args);
        intent.setFlags(FLAG_ACTIVITY_CLEAR_TOP);
        preference.setAppPrefAssetId(0);
        if (ADHelper.getInstance(activity).getPipAct()!=null){
            ADHelper.getInstance(activity).getPipAct().moveTaskToBack(false);
            ADHelper.getInstance(activity).getPipAct().finish();
           // intent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
        }
        activity.startActivity(intent);
    }


    public void portraitListing(Activity source, Class<GridActivity> destination, String i, String title, int flag, int type, BaseCategory baseCategory, int position) {
        Intent intent = new Intent(source, destination);
        intent.putExtra("playListId", i);
        intent.putExtra("title", title);
        intent.putExtra("flag", flag);
        intent.putExtra("shimmerType", type);
        intent.putExtra("baseCategory", baseCategory);
        intent.putExtra("railPosition",position);

        activity.startActivity(intent);
    }

    public void listActivity(Activity source, Class<ListActivity> destination, String i, String title, int flag, int type, BaseCategory baseCategory) {
        Intent intent = new Intent(source, destination);
        intent.putExtra("playListId", i);
        intent.putExtra("title", title);
        intent.putExtra("flag", flag);
        intent.putExtra("shimmerType", type);
        intent.putExtra("baseCategory", baseCategory);
        activity.startActivity(intent);
    }

    public void notificationActivity(Activity source, Class<NotificationActivity> destination) {
        Intent intent = new Intent(source, destination);
        activity.startActivity(intent);
    }

    public void forgotPasswordActivity(Activity source, Class<ForgotPasswordActivity> destination) {
        Intent intent = new Intent(source, destination);
        activity.startActivity(intent);
    }


    public void forceLogin(Activity source, Class<ForceLoginFbActivity> destination, String token, String fid, String name, String picUrl) {

        Bundle args = new Bundle();
        args.putString("fbName", name);
        args.putString("fbToken", token);
        args.putString("fbId", fid);
        args.putString("fbProfilePic", picUrl);
        Intent intent = new Intent(source, destination);
        intent.putExtra(AppConstants.EXTRA_REGISTER_USER, args);

        activity.startActivity(intent);
    }

    public void resultActivityBundle(Activity source, Class<ActivityResults> destination, String searchType, String searchKey, int total) {
        Bundle args = new Bundle();
        args.putString("Search_Show_All", searchType);
        args.putString("Search_Key", searchKey);
        args.putInt("Total_Result", total);
        Intent intent = new Intent(source, destination);
        intent.putExtra("SearchResult", args);
        activity.startActivity(intent);
    }

    public void seriesDetailScreen(Activity source, Class<SeriesDetailActivity> destination, int seriesId) {
        Intent intent = new Intent(source, destination);
        intent.putExtra("seriesId", seriesId);
        intent.setFlags(FLAG_ACTIVITY_CLEAR_TOP);
        activity.startActivity(intent);
    }

    public void tutorialDetailScreen(Activity source, Class<TutorialActivity> destination, int seriesId) {
        Intent intent = new Intent(source, destination);
        intent.putExtra("seriesId", seriesId);
        intent.setFlags(FLAG_ACTIVITY_CLEAR_TOP);
        activity.startActivity(intent);
    }


    public void watchHistory(Activity source, Class<WatchListActivity> destination, String type, boolean isWatchHistory) {
        Bundle args = new Bundle();
        args.putString("viewType", type);
        Intent intent = new Intent(source, destination);
        intent.putExtra("bundleId", args);
        intent.putExtra("isWatchHistory", isWatchHistory);
        // activity.startActivity(intent, ActivityOptions.makeSceneTransitionAnimation(source).toBundle());
        activity.startActivity(intent);
    }
    public void launchMyDownloads(String seriesId,int seasonNumber,String title){
        Intent intent = new Intent(this.activity, MyDownloadsNewActivity.class);
        intent.putExtra("series_id", seriesId);
        intent.putExtra("season_number", seasonNumber);
        intent.putExtra("title", title);
        activity.startActivity(intent);
    }

    public void launchMyDownloads(){
        Intent intent = new Intent(this.activity, MyDownloadsNewActivity.class);
        activity.startActivity(intent);
    }

    public void launchOfflinePlayer(@Nullable String entryId) {
        Log.w("optionss","launchOfflinePlayer");
        Intent intent = new Intent(this.activity, OfflinePlayerActivity.class);
        intent.putExtra("entry_id",entryId);
        activity.startActivity(intent);
    }
}
