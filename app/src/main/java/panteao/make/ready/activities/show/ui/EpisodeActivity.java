package panteao.make.ready.activities.show.ui;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.res.Configuration;
import android.graphics.Color;
import android.graphics.Typeface;
import android.media.AudioAttributes;
import android.media.AudioFocusRequest;
import android.media.AudioManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.provider.MediaStore;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.TextUtils;
import android.text.style.ForegroundColorSpan;
import android.util.Log;
import android.view.DisplayCutout;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowInsets;
import android.view.WindowInsetsController;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.ViewPager;

import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.android.material.tabs.TabLayout;
import com.kaltura.tvplayer.OfflineManager;
import com.make.bookmarking.bean.GetBookmarkResponse;
import com.make.enums.Layouts;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.TimeUnit;

import panteao.make.ready.Bookmarking.BookmarkingViewModel;
import panteao.make.ready.R;
import panteao.make.ready.SDKConfig;
import panteao.make.ready.activities.downloads.WifiPreferenceListener;
import panteao.make.ready.activities.purchase.TVODENUMS;
import panteao.make.ready.activities.show.adapter.AllCommentAdapter;
import panteao.make.ready.activities.show.viewModel.DetailViewModel;
import panteao.make.ready.activities.downloads.NetworkHelper;
import panteao.make.ready.activities.listing.listui.ListActivity;
import panteao.make.ready.activities.purchase.callBack.EntitlementStatus;
import panteao.make.ready.activities.purchase.planslayer.GetPlansLayer;
import panteao.make.ready.activities.purchase.ui.PurchaseActivity;
import panteao.make.ready.activities.purchase.ui.VodOfferType;
import panteao.make.ready.activities.usermanagment.ui.LoginActivity;
import panteao.make.ready.adapters.player.EpisodeTabAdapter;
import panteao.make.ready.baseModels.BaseBindingActivity;
import panteao.make.ready.beanModel.AppUserModel;
import panteao.make.ready.beanModel.allComments.ItemsItem;
import panteao.make.ready.beanModel.entitle.EntitledAs;
import panteao.make.ready.beanModel.entitle.ResponseEntitle;
import panteao.make.ready.beanModel.enveuCommonRailData.RailCommonData;
import panteao.make.ready.beanModel.responseModels.detailPlayer.Data;
import panteao.make.ready.beanModel.responseModels.detailPlayer.ResponseDetailPlayer;
import panteao.make.ready.beanModel.responseModels.series.SeriesResponse;
import panteao.make.ready.beanModel.responseModels.series.season.SeasonResponse;
import panteao.make.ready.beanModel.selectedSeason.SelectedSeasonModel;
import panteao.make.ready.beanModelV3.uiConnectorModelV2.EnveuVideoItemBean;
import panteao.make.ready.callbacks.commonCallbacks.CommonRailtItemClickListner;
import panteao.make.ready.callbacks.commonCallbacks.MoreClickListner;
import panteao.make.ready.callbacks.commonCallbacks.NetworkChangeReceiver;
import panteao.make.ready.callbacks.commonCallbacks.TrailorCallBack;
import panteao.make.ready.databinding.ActivityEpisodeBinding;
import panteao.make.ready.fragments.dialog.AlertDialogFragment;
import panteao.make.ready.fragments.dialog.AlertDialogSingleButtonFragment;
import panteao.make.ready.fragments.dialog.PremiumDownloadPopup;
import panteao.make.ready.fragments.dialog.LoginPopupDialog;
import panteao.make.ready.fragments.dialog.PremiumDialog;
import panteao.make.ready.fragments.player.ui.CommentsFragment;
import panteao.make.ready.fragments.player.ui.RecommendationRailFragment;
import panteao.make.ready.fragments.player.ui.SeasonTabFragment;
import panteao.make.ready.fragments.player.ui.UserInteractionFragment;
import panteao.make.ready.networking.apistatus.APIStatus;
import panteao.make.ready.networking.responsehandler.ResponseModel;
import panteao.make.ready.player.kalturaPlayer.KalturaFragment;
import panteao.make.ready.utils.MediaTypeConstants;
import panteao.make.ready.utils.commonMethods.AppCommonMethod;
import panteao.make.ready.utils.constants.AppConstants;
import panteao.make.ready.utils.constants.SharedPrefesConstants;
import panteao.make.ready.utils.cropImage.helpers.Logger;
import panteao.make.ready.utils.cropImage.helpers.NetworkConnectivity;
import panteao.make.ready.utils.helpers.ADHelper;
import panteao.make.ready.utils.helpers.CheckInternetConnection;
import panteao.make.ready.utils.helpers.ImageHelper;
import panteao.make.ready.utils.helpers.RailInjectionHelper;
import panteao.make.ready.utils.helpers.SharedPrefHelper;
import panteao.make.ready.utils.helpers.StringUtils;
import panteao.make.ready.utils.helpers.ToastHandler;
import panteao.make.ready.utils.helpers.ToolBarHandler;
import panteao.make.ready.utils.helpers.downloads.KTDownloadEvents;
import panteao.make.ready.utils.helpers.downloads.KTDownloadHelper;
import panteao.make.ready.utils.helpers.downloads.OnDownloadClickInteraction;
import panteao.make.ready.utils.helpers.downloads.VideoListListener;
import panteao.make.ready.utils.helpers.intentlaunchers.ActivityLauncher;
import panteao.make.ready.utils.helpers.ksPreferenceKeys.KsPreferenceKeys;

import static android.media.AudioManager.AUDIOFOCUS_LOSS;
import static com.google.android.material.tabs.TabLayout.INDICATOR_GRAVITY_BOTTOM;

public class EpisodeActivity extends BaseBindingActivity<ActivityEpisodeBinding> implements AlertDialogFragment.AlertDialogListener, NetworkChangeReceiver.ConnectivityReceiverListener, AudioManager.OnAudioFocusChangeListener, CommonRailtItemClickListner, MoreClickListner, OnDownloadClickInteraction, VideoListListener, KalturaFragment.OnPlayerInteractionListener, KTDownloadEvents, TrailorCallBack {
    public static boolean isActive = false;
    private final long mLastClickTime = 0;
    private DetailViewModel viewModel;
    private KsPreferenceKeys preference;
    private int assestId;
    private int seriesId;
    private final String videoUrl = "";
    private final String vastUrl = "";
    private String token;
    private ResponseDetailPlayer response;
    private boolean isLogin;
    private final String bannerImage = "";
    private AllCommentAdapter commentAdapter;
    private String profilePicKey;
    public long videoPos = 0;
    public boolean isloggedout = false;
    private BottomSheetDialog dialog;
    public String userName = "";
    public int commentCounter = 0;
    private final boolean commentLastPage = false;
    private boolean loadingComment = true;
    private List<ItemsItem> commentsList;
    private boolean isHitPlayerApi = false;
    private int selectedIdIntent = -1;
    private String tabId;
    private String Entryid;
    private String brightCoveVideoId;
    private RailInjectionHelper railInjectionHelper;
    private FragmentTransaction transaction;
    private String sharingUrl;
    private AudioManager audioManager;
    private AudioFocusRequest focusRequest;
    private SeasonTabFragment seasonTabFragment;
    private EnveuVideoItemBean seriesDetailBean;
    private CommentsFragment commentsFragment;
    private Dialog seasonDialog;
    private static SeriesResponse mSeriesResponse;
    private static List<SeasonResponse> railSeriesResponse;
    private boolean isVOD;
    private int selectedSeasonId;
    private int totalSeasonCount;

    private AlertDialog alertDialog;
    private AlertDialog.Builder builder;
    private final String TAG = "EpisodeActivity";
    private boolean hasSelectedSeasonId;
    private boolean isDataUpdated;
    private EpisodeTabAdapter episodeTabAdapter;
    public boolean isSeasonData = false;
    public boolean isRailData = false;
    private AlertDialogSingleButtonFragment errorDialog;
    private final boolean errorDialogShown = false;
    private EnveuVideoItemBean videoDetails;
    private MediaStore.Video downloadAbleVideo;
    private UserInteractionFragment userInteractionFragment;
    Bundle extras;
    private final boolean isOfflineAvailable = false;
    private final boolean hitEvent = false;
    private final boolean isCastConnected = false;
    private KalturaFragment playerfragment;
    private final boolean skipIntro=true;
    long bookmarkPosition = 0l;
    private long playerCurrentPosition = 0l;
    private boolean isClickedTrailor = false;
    private boolean fromOnStart = false;


    public static void closeActivity() {
    }


    boolean isLoggedIn = false;

    private void setFullScreen() {
        Log.e("Tag", "Inset: " + Build.VERSION.SDK_INT);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            final WindowInsetsController insetsController = getWindow().getInsetsController();
            if (insetsController != null) {
                insetsController.hide(WindowInsets.Type.statusBars());
            }
            WindowManager.LayoutParams attribs = getWindow().getAttributes();
            attribs.layoutInDisplayCutoutMode = WindowManager.LayoutParams.LAYOUT_IN_DISPLAY_CUTOUT_MODE_ALWAYS;
            getWindow().getDecorView().setOnApplyWindowInsetsListener((view, windowInsets) -> {
                        DisplayCutout inset = windowInsets.getDisplayCutout();
                        Log.d("Tag", "Inset: " + inset);
                        return windowInsets;
                    }
            );
        } else {
            getWindow().setFlags(
                    WindowManager.LayoutParams.FLAG_FULLSCREEN,
                    WindowManager.LayoutParams.FLAG_FULLSCREEN
            );
        }

    }

    @Override
    public ActivityEpisodeBinding inflateBindingLayout() {
        return ActivityEpisodeBinding.inflate(inflater);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        isActive = true;
        getWindow().setBackgroundDrawableResource(R.color.black);
        /*getWindow().setFlags(WindowManager.LayoutParams.FLAG_SECURE,
                WindowManager.LayoutParams.FLAG_SECURE);*/

        if (SDKConfig.getInstance().getEpisodeDetailId().equalsIgnoreCase("")) {
            //tabId = AppConstants.EPISODE_ENVEU;
            tabId = "10000";
        } else {
            tabId = SDKConfig.getInstance().getEpisodeDetailId();
        }

        preference = KsPreferenceKeys.getInstance();
        if (preference.getAppPrefLoginStatus()) {
            isLoggedIn = true;
        }
        viewModel = ViewModelProviders.of(EpisodeActivity.this).get(DetailViewModel.class);
        setupUI(getBinding().llParent);
        commentCounter = 0;
        isHitPlayerApi = false;
        AppCommonMethod.isPurchase = false;

        if (getIntent().hasExtra(AppConstants.BUNDLE_ASSET_BUNDLE)) {
            extras = getIntent().getExtras();
            if (extras != null) {
                extras = extras.getBundle(AppConstants.BUNDLE_ASSET_BUNDLE);
                assestId = Objects.requireNonNull(extras).getInt(AppConstants.BUNDLE_ASSET_ID);
                videoPos = TimeUnit.SECONDS.toMillis(Long.parseLong(extras.getString(AppConstants.BUNDLE_DURATION)));
                Entryid = Objects.requireNonNull(extras).getString(AppConstants.ENTRY_ID);

            }
        } else {
            throw new IllegalArgumentException("Activity cannot find  extras " + "Search_Show_All");
        }
        //end basic settings
        callBinding();
    }

    private void callBinding() {
        modelCall();
    }

    private void modelCall() {

        new ToolBarHandler(this).setEpisodeAction(getBinding());
        getBinding().connection.retryTxt.setOnClickListener(view -> {
            getBinding().llParent.setVisibility(View.VISIBLE);
            getBinding().noConnectionLayout.setVisibility(View.GONE);
            connectionObserver();
        });

        getBinding().backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });
        connectionObserver();
    }


    private void connectionObserver() {
        if (NetworkConnectivity.isOnline(this)) {
            connectionValidation();
        } else {
            connectionValidation();
        }
    }

    private void connectionValidation() {
        if (aBoolean) {
            UIinitialization();
        } else {
            noConnectionLayout();
        }
    }

    private void setPlayerFragment() {
        Bundle args = new Bundle();
        if (videoDetails != null) {
            args.putString(AppConstants.ENTRY_ID, Entryid);
            args.putBoolean("binge_watch", SDKConfig.getInstance().getBingeWatchingEnabled());
            args.putInt("binge_watch_timer", SDKConfig.getInstance().getTimer());
            args.putBoolean("from_binge", fromBingWatch);
            args.putLong("bookmark_position",bookmarkPosition);
            args.putString("tvod_type",typeofTVOD);
            args.putString("from_chapter","Episode");
            Logger.d("ENTRY_ID", Entryid + "");

        }
        if (Entryid!=null && !Entryid.equalsIgnoreCase("")){
            FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
            playerfragment = new KalturaFragment();
            playerfragment.setArguments(args);
            transaction.replace(R.id.player_frame, playerfragment);
            transaction.addToBackStack(null);
            transaction.commit();
        }
    }

    boolean loginClicked=false;
    private void showloginPopup() {
        if(!this.isFinishing()) {
            try {
        FragmentManager fm = getSupportFragmentManager();
        LoginPopupDialog alertDialog = LoginPopupDialog.newInstance("", getResources().getString(R.string.login_popup_message));
        alertDialog.setCancelable(false);
        alertDialog.setAlertDialogCallBack(new LoginPopupDialog.AlertDialogListener() {
            @Override
            public void onFinishDialog() {
                loginClicked=true;
                new ActivityLauncher(EpisodeActivity.this).loginActivity(EpisodeActivity.this, LoginActivity.class);
            }
        });
        alertDialog.show(fm, "fragment_alert");
            } catch (Exception e) {

            }
        }

    }

    public void postCommentClick() {
      /*  getBinding().interactionSection.showComments.setOnClickListener(v -> {
            commentFragment(163);
            getBinding().interactionSection.showComments.setVisibility(View.GONE);
            getBinding().rootScroll.setVisibility(View.GONE);
            getBinding().fragmentComment.setVisibility(View.VISIBLE);
        });*/
    }

    public void commentFragment() {
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        commentsFragment = new CommentsFragment();
        Bundle args = new Bundle();
        args.putInt(AppConstants.BUNDLE_ID_FOR_COMMENTS, id);
        args.putString(AppConstants.BUNDLE_TYPE_FOR_COMMENTS, MediaTypeConstants.getInstance().getSeries());
        commentsFragment.setArguments(args);
        transaction.replace(R.id.fragment_comment, commentsFragment);
        transaction.addToBackStack(null);
        transaction.commit();

    }

    public void removeCommentFragment() {
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        if (commentsFragment != null) {
            transaction.remove(commentsFragment);
            transaction.commit();
            transaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_CLOSE);
            commentsFragment = null;
            getBinding().rootScroll.setVisibility(View.VISIBLE);
            getBinding().fragmentComment.setVisibility(View.GONE);
        }
    }

    RecommendationRailFragment railFragment;

    public void setTabs() {
        if (seriesDetailBean != null) {
//            downloadHelper = new DownloadHelper(this, this, seriesDetailBean.getEntryid(), seriesDetailBean.getTitle(), MediaTypeConstants.getInstance().getEpisode(), videoDetails);
//            downloadHelper.findVideo(videoDetails.getBrightcoveVideoId());
        }
        getBinding().tabLayout.setSelectedTabIndicatorGravity(INDICATOR_GRAVITY_BOTTOM);
        if (episodeTabAdapter == null) {
            episodeTabAdapter = new EpisodeTabAdapter(getSupportFragmentManager());
            railFragment = new RecommendationRailFragment();
            Bundle args = new Bundle();
            args.putString(AppConstants.BUNDLE_TAB_ID, tabId);
            railFragment.setArguments(args);
            seasonTabFragment = new SeasonTabFragment();
            Bundle bundleSeason = new Bundle();

            if (noEpisode) {
                bundleSeason.putInt(AppConstants.BUNDLE_ASSET_ID, seriesId);
                bundleSeason.putInt(AppConstants.BUNDLE_CURRENT_ASSET_ID, assestId);
                bundleSeason.putParcelableArrayList(AppConstants.BUNDLE_SEASON_ARRAY, seriesDetailBean.getSeasons());
                bundleSeason.putInt(AppConstants.BUNDLE_SEASON_COUNT, seriesDetailBean.getSeasonCount());
                if (seriesDetailBean.getSeasonCount() > 0)
                    bundleSeason.putInt(AppConstants.BUNDLE_SELECTED_SEASON, Integer.parseInt(seasonNumber));

            } else {
                bundleSeason.putInt(AppConstants.BUNDLE_ASSET_ID, -1);
            }

            seasonTabFragment.setArguments(bundleSeason);
            episodeTabAdapter.addFragment(seasonTabFragment, getString(R.string.tab_heading_episodes));
            episodeTabAdapter.addFragment(railFragment, getString(R.string.tab_heading_other));
            getBinding().viewPager.setAdapter(episodeTabAdapter);
            getBinding().viewPager.setOffscreenPageLimit(2);
            getBinding().tabLayout.setupWithViewPager(getBinding().viewPager);
            //AppCommonMethod.customTabWidth(getBinding().tabLayout);
            //AppCommonMethod.customTabWidth2(getBinding().tabLayout);
            getBinding().tabLayout.addOnTabSelectedListener(new TabLayout.BaseOnTabSelectedListener() {
                @Override
                public void onTabSelected(TabLayout.Tab tab) {
                    showLoading(getBinding().progressBar);
                    new Handler().postDelayed(() -> dismissLoading(getBinding().progressBar), 1500);
                }

                @Override
                public void onTabUnselected(TabLayout.Tab tab) {

                }

                @Override
                public void onTabReselected(TabLayout.Tab tab) {

                }
            });

            getBinding().viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
                @Override
                public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                }

                @Override
                public void onPageSelected(int position) {
                    getBinding().viewPager.measure(getBinding().viewPager.getMeasuredWidth(), getBinding().viewPager.getMeasuredHeight());
                }

                @Override
                public void onPageScrollStateChanged(int state) {

                }
            });

        } else {
            Bundle bundleSeason = new Bundle();

            if (noEpisode) {
                bundleSeason.putInt(AppConstants.BUNDLE_ASSET_ID, seriesId);
                bundleSeason.putInt(AppConstants.BUNDLE_CURRENT_ASSET_ID, assestId);
                bundleSeason.putParcelableArrayList(AppConstants.BUNDLE_SEASON_ARRAY, seriesDetailBean.getSeasons());

                bundleSeason.putInt(AppConstants.BUNDLE_SEASON_COUNT, seriesDetailBean.getSeasonCount());
                if (seriesDetailBean.getSeasonCount() > 0)
                    bundleSeason.putInt(AppConstants.BUNDLE_SELECTED_SEASON, Integer.parseInt(seasonNumber));

            } else {
                bundleSeason.putInt(AppConstants.BUNDLE_ASSET_ID, -1);
            }

            Bundle args = new Bundle();
            args.putString(AppConstants.BUNDLE_TAB_ID, tabId);
            railFragment.getVideoRails();

            seasonTabFragment.getVideoRails();
        }

    }


    public void stopShimmercheck() {
        if (isSeasonData && isRailData) {
            isSeasonData = false;
            isRailData = false;
            try {
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        stopShimmer();
                    }
                }, 1000);

            } catch (Exception ignored) {

            }
        }
    }

    public void numberOfEpisodes() {

    }


    public void removeTab() {
        if (getBinding().tabLayout.getTabCount() >= 1 && position <= getBinding().tabLayout.getTabCount()) {
            episodeTabAdapter.removeTabPage(position);
            ViewGroup.LayoutParams params = getBinding().tabLayout.getLayoutParams();
            params.width = WindowManager.LayoutParams.MATCH_PARENT;
            //(int) getResources().getDimension(R.dimen.tab_layout_single);
            getBinding().tabLayout.setLayoutParams(params);
            //  getBinding().tabLayout.setLayoutParams(new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,height));


        }
    }


    private void callShimmer() {
        getBinding().seriesShimmer.setVisibility(View.VISIBLE);
        getBinding().mShimmer.seriesShimmerScroll1.setEnabled(false);
        getBinding().mShimmer.seriesShimmerScroll2.setEnabled(false);
        getBinding().llParent.setVisibility(View.GONE);
        getBinding().noConnectionLayout.setVisibility(View.GONE);
        getBinding().mShimmer.sfShimmer1.startShimmer();
        getBinding().mShimmer.sfShimmer2.startShimmer();
        getBinding().mShimmer.sfShimmer3.startShimmer();
        getBinding().mShimmer.flBackIconImage.bringToFront();
        getBinding().mShimmer.flBackIconImage.setOnClickListener(v -> onBackPressed());

    }


    private void stopShimmer() {
        getBinding().seriesShimmer.setVisibility(View.GONE);
        getBinding().llParent.setVisibility(View.VISIBLE);
        getBinding().noConnectionLayout.setVisibility(View.GONE);
        getBinding().mShimmer.sfShimmer1.startShimmer();
        getBinding().mShimmer.sfShimmer2.startShimmer();
        getBinding().mShimmer.sfShimmer3.startShimmer();
        if (isPremium) {
            showPremiumPopup();
        }

        if (isPremium){
            if (!KsPreferenceKeys.getInstance().getAppPrefLoginStatus()){
            showloginPopup();
            }else {
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        showPremiumDialog();
                    }
                },400);
            }
        }
    }

    public void playPlayerWhenShimmer() {
        viewModel.getBookMarkByVideoId(token, videoDetails.getId()).observe(this, new Observer<GetBookmarkResponse>() {
            @Override
            public void onChanged(GetBookmarkResponse getBookmarkResponse) {
                getBinding().backButton.setVisibility(View.GONE);

                if (getBookmarkResponse != null && getBookmarkResponse.getBookmarks() != null) {
                    bookmarkPosition = getBookmarkResponse.getBookmarks().get(0).getPosition();
                }
                Log.w("bookmarkingPosition",bookmarkPosition+"");
                transaction = getSupportFragmentManager().beginTransaction();
                setPlayerFragment();
            }
        });
    }


    private void setArgsForEvent() {
        try {
            if (videoDetails != null) {
                if (videoDetails.getName() != null) {
                    args.putString(AppConstants.PLAYER_ASSET_TITLE, videoDetails.getName());
                }
                if (videoDetails.getAssetType() != null) {
                    args.putString(AppConstants.PLAYER_ASSET_MEDIATYPE, videoDetails.getAssetType());
                }
            }
        } catch (Exception e) {

        }

    }


    @Override
    protected void onResume() {
        super.onResume();
        requestAudioFocus();
        boolean isTablet = EpisodeActivity.this.getResources().getBoolean(R.bool.isTablet);
        if (isTablet) {
            AppCommonMethod.isResumeDetail = true;
        }
        isloggedout = false;
        dismissLoading(getBinding().progressBar);
        if (AppCommonMethod.isPurchase) {
            AppCommonMethod.isPurchase = false;
            seriesId = AppCommonMethod.seriesId;
            isHitPlayerApi = false;
            refreshDetailPage();
        }

        if (!isLoggedIn) {
            if (preference.getAppPrefLoginStatus()) {
                isLoggedIn = true;
                AppCommonMethod.isPurchase = false;
                seriesId = AppCommonMethod.seriesId;
                isHitPlayerApi = false;
                fromBingWatch = false;
                //  seasonTabFragment=null;
                seasonTabFragment.setSeasonAdapter();
                token = preference.getAppPrefAccessToken();
                refreshDetailPage();
            }else {

            }
        }
        setBroadcast();
        if (preference != null && userInteractionFragment != null) {
            AppCommonMethod.callSocialAction(preference, userInteractionFragment);
        }

       // Log.w("loginClicked",loginClicked+" "+isPremium+" "+preference.getAppPrefLoginStatus());

        try {
             downloadHelper = new KTDownloadHelper(this,this);

        if (KsPreferenceKeys.getInstance().getVideoDownloadAction()==3){
            videoDeletedFromList();
        }
        }catch (Exception ignored){

        }

    }

    private void showPremiumDialog() {
        if(!this.isFinishing()) {
            try {
                FragmentManager fm = getSupportFragmentManager();
            PremiumDialog alertDialog = PremiumDialog.newInstance("", getResources().getString(R.string.premium_popup_message_new));
            alertDialog.setCancelable(false);
            alertDialog.setAlertDialogCallBack(new PremiumDialog.AlertDialogListener() {
                @Override
                public void onFinishDialog() {
                    loginClicked = false;
                    comingSoon();
                    // new ActivityLauncher(EpisodeActivity.this).loginActivity(EpisodeActivity.this, LoginActivity.class);
                }
            });
            alertDialog.show(fm, "fragment_alert");
            }catch (Exception e){

            }

        }
    }

    public void requestAudioFocus() {
        audioManager = (AudioManager) getSystemService(Context.AUDIO_SERVICE);
        int negativeVal = -1;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            AudioAttributes playbackAttributes = new AudioAttributes.Builder()
                    .setUsage(AudioAttributes.USAGE_VOICE_COMMUNICATION)
                    .setContentType(AudioAttributes.CONTENT_TYPE_MUSIC)
                    .build();

            focusRequest =
                    new AudioFocusRequest.Builder(AudioManager.STREAM_MUSIC)
                            .setAudioAttributes(playbackAttributes)
                            .setAcceptsDelayedFocusGain(true)
                            .setOnAudioFocusChangeListener(
                                    new AudioManager.OnAudioFocusChangeListener() {
                                        @Override
                                        public void onAudioFocusChange(int i) {

                                        }
                                    })
                            .build();
            // audioManager.requestAudioFocus(focusRequest);

            audioManager.requestAudioFocus(this, AudioManager.STREAM_MUSIC, AudioManager.AUDIOFOCUS_GAIN);
            switch (audioManager.requestAudioFocus(focusRequest)) {
                case AudioManager.AUDIOFOCUS_REQUEST_FAILED:
                    // don???t start playback
                {

                }
                break;
                case AudioManager.AUDIOFOCUS_REQUEST_GRANTED:
                    // actually start playback
            }


        } else {
            audioManager.requestAudioFocus(null, AudioManager.STREAM_VOICE_CALL,
                    AudioManager.AUDIOFOCUS_GAIN_TRANSIENT);
            //    Toast.makeText(EpisodeActivity.this,"AUDIOFOCUS_GAIN_TRANSIENT" ,Toast.LENGTH_SHORT).show();

        }
    }


    void releaseAudioFocusForMyApp() {
        // Toast.makeText(EpisodeActivity.this,"releaseAudioFocusForMyApp" ,Toast.LENGTH_SHORT).show();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            audioManager.abandonAudioFocusRequest(focusRequest);
        }
    }

    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);
        try {
            if (playerfragment != null) {
            if (!hasFocus) {
                playerfragment.checkPlayerState(1);
            }else {
                playerfragment.checkPlayerState(2);
            }
        }
        }catch (Exception e){

        }
    }

    public void setBroadcast() {
        receiver = new NetworkChangeReceiver();
        IntentFilter filter = new IntentFilter();
        filter.addAction("android.net.conn.CONNECTIVITY_CHANGE");
        filter.addAction("android.net.wifi.WIFI_STATE_CHANGED");
        filter.addAction("android.net.wifi.STATE_CHANGE");
        EpisodeActivity.this.registerReceiver(receiver, filter);
        setConnectivityListener();
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        if (intent.hasExtra(AppConstants.BUNDLE_ASSET_BUNDLE)) {
            isHitPlayerApi = false;
            Bundle extras = intent.getExtras();
            if (extras != null) {
                assestId = Objects.requireNonNull(intent.getExtras().getBundle(AppConstants.BUNDLE_ASSET_BUNDLE)).getInt(AppConstants.BUNDLE_ASSET_ID);
                AppCommonMethod.seasonId = -1;
                if (commentAdapter != null) {
                    commentAdapter.clearList();
                    if (commentsList != null) {
                        commentsList.clear();
                    }
                }
                refreshDetailPage();
            }
        } else {
            throw new IllegalArgumentException("Activity cannot find  extras " + "Search_Show_All");
        }
    }


    public void refreshDetailPage() {
        loginClicked=false;
        this.assestId = assestId;
//        if (playerFragment != null)
//            playerFragment.stopPlayer();
        if (preference.getAppPrefHasSelectedId()) {
            preference.setAppPrefHasSelectedId(false);
            int tempId = preference.getAppPrefSelectodSeasonId();
            if (tempId != -1) {
                hasSelectedSeasonId = true;
                selectedIdIntent = tempId;
                preference.setAppPrefSelectodSeasonId(-1);
            }
        }

        if (getBinding().expandableLayout.isExpanded())
            clickExpandable();

        callBinding();
    }


    public void comingSoon() {
       // if (isLogin) {
            //showDialog(EpisodeActivity.this.getResources().getString(R.string.error), getResources().getString(R.string.you_are_not_entitled));
          if (getBinding().tvPurchased.getText().toString().equalsIgnoreCase(getResources().getString(R.string.subscribed)) || getBinding().tvPurchased.getText().toString().equalsIgnoreCase(getResources().getString(R.string.purchased)) ||
                  getBinding().tvPurchased.getText().toString().equalsIgnoreCase(getResources().getString(R.string.rented))){

        }else {
              AppCommonMethod.assetId = assestId;
              AppCommonMethod.seriesId = seriesId;
              if (responseEntitlementModel != null && responseEntitlementModel.getStatus()) {
                  Intent intent = new Intent(EpisodeActivity.this, PurchaseActivity.class);
                  intent.putExtra("response", videoDetails);
                  intent.putExtra("assestId", assestId);
                  intent.putExtra("contentType", MediaTypeConstants.getInstance().getEpisode());
                  intent.putExtra("responseEntitlement", responseEntitlementModel);
                  if (responseEntitlementModel != null) {
                      startActivity(intent);
                  }
              }
          }

    }

    public void openLoginPage() {

        AppCommonMethod.seasonId = selectedSeasonId;
        preference.setAppPrefJumpTo(MediaTypeConstants.getInstance().getEpisode());
        preference.setAppPrefJumpBack(true);
        preference.setAppPrefIsEpisode(true);
        preference.setAppPrefJumpBackId(assestId);
        //   preference.setString(AppConstants.APP_PREF_VIDEO_POSITION, String.valueOf(TimeUnit.MILLISECONDS.toSeconds(fragment.getCurrentPosition())));

        preference.setAppPrefHasSelectedId(true);
        preference.setAppPrefSelectodSeasonId(selectedSeasonId);
        loginClicked=true;
        new ActivityLauncher(EpisodeActivity.this).loginActivity(EpisodeActivity.this, LoginActivity.class);
    }

    public void UIinitialization() {
        if (!fromBingWatch) {
            callShimmer();
        }
        isPremium = false;
        railInjectionHelper = ViewModelProviders.of(this).get(RailInjectionHelper.class);
        loadingComment = true;
        commentCounter = 0;
        getBinding().tvBuyNow.setVisibility(View.GONE);
        postCommentClick();
        getBinding().tvPurchased.setVisibility(View.GONE);
        getBinding().tvPremium.setVisibility(View.GONE);
        getBinding().setDuration("");
        getBinding().setCasttext("");
        getBinding().setCrewtext("");
        EnveuVideoItemBean player = new EnveuVideoItemBean();
        Data data = new Data();
        data.setContentTitle("");
        getBinding().setResponseApi(player);
        setupUI(getBinding().llParent);

        response = new ResponseDetailPlayer();
        preference.setAppPrefAssetId(assestId);
        isLogin = preference.getAppPrefLoginStatus();
        token = preference.getAppPrefAccessToken();
        Logger.e("", "APP_PREF_ACCESS_TOKEN" + token);


        showLoading(getBinding().progressBar);

        if (isLogin) {
            AppUserModel signInResponseModel = AppUserModel.getInstance();
            if (signInResponseModel != null) {
                userName = signInResponseModel.getName();
                profilePicKey = signInResponseModel.getProfilePicURL();
            }
        }

        getBinding().noConnectionLayout.setVisibility(View.GONE);
        getBinding().header.setVisibility(View.VISIBLE);

        // clickSharing();
        downloadHelper = new KTDownloadHelper(this,this);
        if (!isHitPlayerApi) {
            getEpisodeDetails();
           /* if (isLogin) {
                hitApiWatchHistory();

            }*/
        }
        BuyNowClick();
    }

    //***********************call episode data from below API***************************************//
    boolean isAdShowingToUser = true;

    public void getEpisodeDetails() {

        isHitPlayerApi = true;
        if (fromBingWatch) {
            parseVideoDetails();
        } else {
            railInjectionHelper.getSeriesDetailsV2(String.valueOf(assestId)).observe(EpisodeActivity.this, new Observer<ResponseModel>() {
                @Override
                public void onChanged(ResponseModel response) {
                    if (response != null) {
                        if (response.getStatus().equalsIgnoreCase(APIStatus.START.name())) {

                        } else if (response.getStatus().equalsIgnoreCase(APIStatus.SUCCESS.name())) {
                            if (response.getBaseCategory() != null) {
                                RailCommonData enveuCommonResponse = (RailCommonData) response.getBaseCategory();

                                videoDetails = enveuCommonResponse.getEnveuVideoItemBeans().get(0);
                               // Log.w("store_seriesid 2",videoDetails.getSeriesId());
                                parseVideoDetails();


                            }
                        } else if (response.getStatus().equalsIgnoreCase(APIStatus.ERROR.name())) {
                            if (response.getErrorModel().getErrorCode() != 0) {
                                stopShimmer();
                            }

                        } else if (response.getStatus().equalsIgnoreCase(APIStatus.FAILURE.name())) {
                            stopShimmer();
                        }

                    }
                }
            });

        }


    }

    private void parseVideoDetails() {
        dismissLoading(getBinding().progressBar);
        sharingClick();
        ImageHelper.getInstance(EpisodeActivity.this).loadListImage(getBinding().playerImage, videoDetails.getPosterURL());

        if (videoDetails.isSeriesPremium()) {
            isPremium = true;
            try {
                if (fromBingWatch) {
                    showPremiumPopup();
                }
            } catch (Exception ignored) {

            }

            getBinding().tvPurchased.setVisibility(View.GONE);
            getBinding().tvPremium.setVisibility(View.GONE);
            getBinding().tvBuyNow.setVisibility(View.VISIBLE);
            getBinding().mPremiumStatus.setVisibility(View.VISIBLE);
            getBinding().backButton.setVisibility(View.VISIBLE);
            if (videoDetails.isPremium()) {
                Entryid="";
            }else {
                Entryid=videoDetails.getkEntryId();
            }
            if (videoDetails.getSeriesSku()!=null && !videoDetails.getSeriesSku().equalsIgnoreCase("")){
                hitApiEntitlement();
            }

        } else {
            if (AppCommonMethod.getCheckBCID(videoDetails.getkEntryId())) {
                Entryid=videoDetails.getkEntryId();
                isLogin = preference.getAppPrefLoginStatus();
                if (isLogin) {
                    Log.d("tgtgtgtgtt",isLogin+"");
                    Log.d("tgtgtgtgtt",preference.getEntitlementStatus()+"");
                    if (!preference.getEntitlementStatus()) {
                        GetPlansLayer.getInstance().getEntitlementStatus(preference, token, new EntitlementStatus() {
                            @Override
                            public void entitlementStatus(boolean entitlementStatus, boolean apiStatus) {
                                if (entitlementStatus && apiStatus) {
                                    isAdShowingToUser = false;
                                }
                                Entryid = (videoDetails.getkEntryId());
                                Log.d("tgtgtgtgtt",Entryid);
                                playPlayerWhenShimmer();
                            }
                        });
                    } else {
                        getBinding().pBar.setVisibility(View.GONE);
                        Entryid = (videoDetails.getkEntryId());
                        Log.d("tgtgtgtgtt>>>>",Entryid);

                        playPlayerWhenShimmer();
                    }

                } else {
                    Entryid = (videoDetails.getkEntryId());
                    playPlayerWhenShimmer();
                }

            }

        }

        if (StringUtils.isNullOrEmptyOrZero(videoDetails.getSeries())) {
            seriesId = -1;
        } else {
            if (videoDetails.getSeriesId() != null) {
                seriesId = Integer.parseInt(videoDetails.getSeriesId());
            } else {
                seriesId = -1;
            }
        }
        if (!fromBingWatch) {
            if (videoDetails.getSeasonNumber() != null && !videoDetails.getSeasonNumber().equalsIgnoreCase("")) {
                getSeriesDetail();
            } else {
                getSeriesDetail();
            }
        } else {
            if (seasonTabFragment != null) {
                seasonTabFragment.updateCurrentAsset();
            }

        }

        setUI();
    }
    //***********************^||||call episode data from above API\\\\\^***************************************//


    //***********************call series data from below API***************************************//
    public void getSeriesDetail() {
        if (seriesId == -1) {
            setUserInteractionFragment();
            setTabs();
        } else {

            railInjectionHelper.getSeriesDetailsV2(String.valueOf(seriesId)).observe(EpisodeActivity.this, new Observer<ResponseModel>() {
                @Override
                public void onChanged(ResponseModel response) {
                    if (response != null) {
                        if (response.getStatus().equalsIgnoreCase(APIStatus.START.name())) {

                        } else if (response.getStatus().equalsIgnoreCase(APIStatus.SUCCESS.name())) {
                            if (response.getBaseCategory() != null) {
                                RailCommonData enveuCommonResponse = (RailCommonData) response.getBaseCategory();
                                //parseSeriesData(enveuCommonResponse);
                                seriesDetailBean = enveuCommonResponse.getEnveuVideoItemBeans().get(0);
                                setTabs();
                                setUserInteractionFragment();
                            }
                        } else if (response.getStatus().equalsIgnoreCase(APIStatus.ERROR.name())) {
                            if (response.getErrorModel().getErrorCode() != 0) {
                                /*if (response.getErrorModel().getErrorCode() == AppConstants.RESPONSE_CODE_LOGOUT) {
                                    showDialog(EpisodeActivity.this.getResources().getString(R.string.error), getResources().getString(R.string.logged_out));
                                } else {
                                    showDialog(EpisodeActivity.this.getResources().getString(R.string.error), getResources().getString(R.string.something_went_wrong));
                                }*/
                                stopShimmer();
                                showDialog();
                            }

                        } else if (response.getStatus().equalsIgnoreCase(APIStatus.FAILURE.name())) {
                            stopShimmer();
                            showDialog();
                            // showDialog(EpisodeActivity.this.getResources().getString(R.string.error), getResources().getString(R.string.something_went_wrong));
                        }

                    }
                }
            });

        }
    }
    //***********************^||||call series data from above API\\\\\^***************************************//


    public void sharingClick() {
        String imgUrl = videoDetails.getPosterURL();
        int id = videoDetails.getId();
        String title = videoDetails.getTitle();
        sharingUrl = AppCommonMethod.getSharingURL(this, title, id, "VIDEO", imgUrl, getApplicationContext());
    }


    public void hitApiWatchHistory() {
        //  token = "eyJhbGciOiJIUzUxMiJ9.eyJpZCI6IjE5NDciLCJleHBpcnlUaW1lIjoxNTY4ODA4Mjk4MTMzLCJlbWFpbCI6InBhcnZlZW4ubWFuaUBnbWFpbC5jb20iLCJhY2NvdW50SWQiOm51bGwsIm5hbWUiOiJNYW5pIn0.lNTYXsMw3YQfADXNcwOOmYK-JlTrPhEs7WJD5_2mWiiHDxVke8JtzhEe6303E4v552L7XtAo8Yxirnw8J6EUdQ";
        railInjectionHelper.hitApiAddWatchHistory(token, assestId).observe(this, responseEmpty ->
        {
            if (responseEmpty.isStatus()) {
                if (response.getResponseCode() == AppConstants.RESPONSE_CODE_REGISTER) {

                } else if (response.getResponseCode() == AppConstants.RESPONSE_CODE_ERROR) {


                }
            }

        });


    }


    ResponseEntitle responseEntitlementModel;

    public void hitApiEntitlement() {

        viewModel.hitApiEntitlement(token, sku).observe(EpisodeActivity.this, responseEntitlement -> {
            responseEntitlementModel = responseEntitlement;
            if (responseEntitlement.getStatus()) {
                if (responseEntitlement.getData().getEntitled()) {
                    isPremium = false;
                    getBinding().tvBuyNow.setVisibility(View.GONE);
                    if (responseEntitlement.getData() != null) {
                        updateBuyNowText();
                    }
                } else {
                    if (responseEntitlement.getData() != null) {
                        updateBuyNowText();
                    }
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            try {
                                if (fromBingWatch) {
                                    showPremiumPopup();
                                } else {
                                    isPremium = true;
                                }

                            } catch (Exception ignored) {

                            }
                        }
                    });
                }
            } else {
                if (responseEntitlementModel != null && responseEntitlementModel.getResponseCode() != null && responseEntitlementModel.getResponseCode() > 0 && responseEntitlementModel.getResponseCode() == 4302) {
                    isloggedout = true;
                    logoutUser();
                    // showDialog(EpisodeActivity.this.getResources().getString(R.string.logged_out), responseEntitlementModel.getDebugMessage() == null ? "" : responseEntitlementModel.getDebugMessage().toString());
                }
            }
        });
    }

    boolean isPremium = false;

    private void showPremiumPopup() {
        try {
            isPremium = true;
            if (KsPreferenceKeys.getInstance().getAppLanguage().equalsIgnoreCase("Thai") || KsPreferenceKeys.getInstance().getAppLanguage().equalsIgnoreCase("???????????????")) {
                AppCommonMethod.resetLanguage("th", EpisodeActivity.this);
            } else if (KsPreferenceKeys.getInstance().getAppLanguage().equalsIgnoreCase("English")) {
                AppCommonMethod.resetLanguage("en", EpisodeActivity.this);
            }
            //showDialog("", getResources().getString(R.string.premium_popup_message));
        } catch (Exception ignored) {

        }
    }

    String typeofTVOD="";
    private void updateBuyNowText() {
        try {
            typeofTVOD="";
            String subscriptionOfferPeriod = null;
            if (type == 1) {
                if (responseEntitlement.getData().getEntitledAs() != null) {
                    List<EntitledAs> alpurchaseas = responseEntitlement.getData().getEntitledAs();
                    for (int i = 0 ; i<alpurchaseas.size();i++){
                        String vodOfferType = alpurchaseas.get(i).getVoDOfferType();
                        if (alpurchaseas.get(i).getOfferType() != null) {
                            subscriptionOfferPeriod = alpurchaseas.get(i).getOfferType();
                        }
                        if (vodOfferType!=null){
                             if (vodOfferType.contains(VodOfferType.RENTAL.name())) {
                                 AppCommonMethod.setDownloadExpiry(alpurchaseas.get(i).getRentalPeriod());
                                if (alpurchaseas.get(i).getIdentifier().contains(TVODENUMS.___sd.name())){
                                    typeofTVOD=TVODENUMS.___sd.name();
                                }else if (alpurchaseas.get(i).getIdentifier().contains(TVODENUMS.___hd.name())){
                                    typeofTVOD=TVODENUMS.___hd.name();
                                }else if (alpurchaseas.get(i).getIdentifier().contains(TVODENUMS.___uhd.name())){
                                    typeofTVOD=TVODENUMS.___uhd.name();
                                }

                        }
                             if (vodOfferType.contains(VodOfferType.PERPETUAL.name())) {
                                 AppCommonMethod.setDownloadExpiry(null);
                                 if (alpurchaseas.get(i).getIdentifier().contains(TVODENUMS.___sd.name())){
                                     typeofTVOD=TVODENUMS.___sd.name();
                                 }else if (alpurchaseas.get(i).getIdentifier().contains(TVODENUMS.___hd.name())){
                                     typeofTVOD=TVODENUMS.___hd.name();
                                 }else if (alpurchaseas.get(i).getIdentifier().contains(TVODENUMS.___uhd.name())){
                                     typeofTVOD=TVODENUMS.___uhd.name();
                                 }

                             } else {

                        }
                        }

                    }
                    String vodOfferType = alpurchaseas.get(0).getVoDOfferType();

                    if (vodOfferType != null) {
                        if (vodOfferType.contains(VodOfferType.RENTAL.name())) {
                            getBinding().tvPurchased.setVisibility(View.VISIBLE);
                            getBinding().tvPurchased.setText("" + getResources().getString(R.string.rented));
                        }
                        if (vodOfferType.contains(VodOfferType.PERPETUAL.name())) {
                            getBinding().tvPurchased.setVisibility(View.VISIBLE);
                            getBinding().tvPurchased.setText("" + getResources().getString(R.string.purchased));
                        }
                        else {

                        }
                    } else {
                        AppCommonMethod.setDownloadExpiry(null);
                    }
                    if (subscriptionOfferPeriod != null) {
                        getBinding().tvPurchased.setVisibility(View.VISIBLE);
                        getBinding().tvPurchased.setText("" + getResources().getString(R.string.subscribed));
                        typeofTVOD="";
                    }
                    if (responseEntitlement.getData().getBrightcoveVideoId() != null) {
                       // Entryid = (responseEntitlement.getData().getBrightcoveVideoId());
                        if(userInteractionFragment!=null) {
                            userInteractionFragment.setDownloadable();
                            initDownload();
                        }
                    }
                    isAdShowingToUser = false;
                    preference.setEntitlementState(true);
                    playPlayerWhenShimmer();

                }
            } else {

            }

        } catch (Exception e) {

        }
    }


    private void BuyNowClick() {
        getBinding().tvBuyNow.setOnClickListener(view -> comingSoon());
        getBinding().tvPurchased.setOnClickListener(view -> comingSoon());
    }
    private KTDownloadHelper downloadHelper;
    public void setUserInteractionFragment() {
        String seriesId = "";
        if (seriesDetailBean != null) {
            seriesId = seriesDetailBean.getBrightcoveVideoId();
        }

        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();

        Bundle args = new Bundle();
        args.putInt(AppConstants.BUNDLE_ASSET_ID, id);
        if (Entryid!=null && !Entryid.equalsIgnoreCase("")){
            args.putString(AppConstants.BUNDLE_KENTRY_ID, Entryid);
        }
        args.putParcelable(AppConstants.BUNDLE_SERIES_DETAIL, videoDetails);
        args.putString(AppConstants.BUNDLE_SERIES_ID, seriesId);

        userInteractionFragment = new UserInteractionFragment();
        userInteractionFragment.setArguments(args);
        transaction.replace(R.id.fragment_user_interaction, userInteractionFragment);
        transaction.addToBackStack(null);
        transaction.commit();
        if (seriesDetailBean != null) {
            if(userInteractionFragment!=null && !isVideoPremium) {
                initDownload();
            }

//          downloadHelper.findVideo(videoDetails.getBrightcoveVideoId());
        }

        if (userInteractionFragment!=null){
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    userInteractionFragment.setDownloadable();
                }
            },1000);

        }


    }

    private void initDownload() {
        if (kEntryId!=null && !kEntryId.equalsIgnoreCase("")) {
            downloadHelper.getAssetInfo(kEntryId);
        }
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        userInteractionFragment.setDownloadable();
                        new Handler().postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                Logger.e(TAG, "onDownloadProgress" +"  ------ "+"initDownload");
                                userInteractionFragment.setDownloadStatus();
                            }
                        },50);

                    }
                });
            }
        },1500);

    }


    public void setUI() {
        if (responseDetailPlayer.getAssetCast().size() > 0) {
            StringBuilder stringBuilder = new StringBuilder();
            for (int i = 0; i < responseDetailPlayer.getAssetCast().size(); i++) {
                if (i == responseDetailPlayer.getAssetCast().size() - 1) {
                    stringBuilder = stringBuilder.append(responseDetailPlayer.getAssetCast().get(i));

                } else
                    stringBuilder = stringBuilder.append(responseDetailPlayer.getAssetCast().get(i)).append(", ");
            }
            getBinding().setCasttext(" " + stringBuilder);
        } else {
            getBinding().llCastView.setVisibility(View.GONE);
        }
        if (responseDetailPlayer.getAssetGenres().size() > 0) {
            StringBuilder stringBuilder = new StringBuilder();
            for (int i = 0; i < responseDetailPlayer.getAssetGenres().size(); i++) {
                if (i == responseDetailPlayer.getAssetGenres().size() - 1) {
                    stringBuilder = stringBuilder.append(responseDetailPlayer.getAssetGenres().get(i));
                } else
                    stringBuilder = stringBuilder.append(responseDetailPlayer.getAssetGenres().get(i)).append(", ");
            }
            getBinding().setCrewtext(" " + stringBuilder);
        } else {
            getBinding().llCrewView.setVisibility(View.GONE);
        }
        setDetails();
    }

    public void logoutUser() {
        isloggedout = false;
        if (isLogin) {
            if (CheckInternetConnection.isOnline(EpisodeActivity.this)) {
                clearCredientials(preference);
                hitApiLogout(preference.getAppPrefAccessToken());
            }
        }
    }


    private void showDialog() {
        FragmentManager fm = getSupportFragmentManager();
        AlertDialogSingleButtonFragment alertDialog = AlertDialogSingleButtonFragment.newInstance(title, message, getResources().getString(R.string.ok));
        alertDialog.setCancelable(false);
        alertDialog.setAlertDialogCallBack(this);
        alertDialog.show(fm, "fragment_alert");
    }

    public void setDetails() {

        if (responseDetailPlayer.getAssetType() != null && responseDetailPlayer.getDuration() > 0) {
            String tempTag1 = responseDetailPlayer.getAssetType();
            String bullet = "\u2022";

            String tempTag2 = AppCommonMethod.calculateTimeinMinutes(responseDetailPlayer.getDuration());

            Spannable WordtoSpan = new SpannableString(bullet);
            WordtoSpan.setSpan(new ForegroundColorSpan(Color.BLUE), 0, WordtoSpan.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
            // StringBuilder stringBuilder = new StringBuilder(tempTag1 + "  " + WordtoSpan + " " + tempTag2);

            setCustomeFields();
        } else {
            setCustomeFields();
            new ToastHandler(EpisodeActivity.this).show(EpisodeActivity.this.getResources().getString(R.string.can_not_play_error));
        }
        if (responseDetailPlayer.getDescription()!=null){
            if (responseDetailPlayer.getDescription().equalsIgnoreCase("")){
                getBinding().descriptionText.setVisibility(View.GONE);
                getBinding().textExpandable.setVisibility(View.GONE);
            }
        }else {
            getBinding().descriptionText.setVisibility(View.GONE);
            getBinding().textExpandable.setVisibility(View.GONE);
        }
        getBinding().setResponseApi(responseDetailPlayer);
        setExpandable();
        if (isLogin)
            addToWatchHistory();
    }

    private void setCustomeFields() {
        try {
            getBinding().tag.setText("");
            if (responseDetailPlayer.getParentalRating() != null && !responseDetailPlayer.getParentalRating().equalsIgnoreCase("")) {
                getBinding().tag.setText(responseDetailPlayer.getParentalRating() + " \u2022");
            }

            getBinding().tag.setText(getBinding().tag.getText().toString() + " " + duration + " \u2022");

            if (responseDetailPlayer.getCountry() != null && !responseDetailPlayer.getCountry().equalsIgnoreCase("")) {
                getBinding().tag.setText(getBinding().tag.getText().toString() + " " + responseDetailPlayer.getCountry() + " \u2022");
            }

            if (responseDetailPlayer.getCompany() != null && !responseDetailPlayer.getCompany().equalsIgnoreCase("")) {
                getBinding().tag.setText(getBinding().tag.getText().toString() + " " + responseDetailPlayer.getCompany() + " \u2022");
            }

            if (responseDetailPlayer.getYear() != null && !responseDetailPlayer.getYear().equalsIgnoreCase("")) {
                getBinding().tag.setText(getBinding().tag.getText().toString() + " " + responseDetailPlayer.getYear() + " \u2022");
            }

            if (getBinding().tag.getText().toString().trim().endsWith("\u2022")) {
                String customeF = getBinding().tag.getText().toString().substring(0, getBinding().tag.getText().toString().length() - 1);
                getBinding().tag.setText(customeF);
            }
            if (getBinding().tag.getText().toString().trim().equalsIgnoreCase("")) {
                // getBinding().customeFieldView.setVisibility(View.GONE);
            }
        } catch (Exception ignored) {

        }

    }


    private void addToWatchHistory() {
        BookmarkingViewModel bookmarkingViewModel = ViewModelProviders.of(this).get(BookmarkingViewModel.class);
        bookmarkingViewModel.addToWatchHistory(token, assestId);
    }

    private void noConnectionLayout() {
        getBinding().llParent.setVisibility(View.GONE);
        getBinding().noConnectionLayout.setVisibility(View.VISIBLE);


    }


    private void setExpandable() {
        getBinding().expandableLayout.collapse();
        if (getBinding().descriptionText.isExpanded()) {
            getBinding().descriptionText.toggle();
            getBinding().descriptionText.setEllipsize(null);
        } else {
            getBinding().descriptionText.setEllipsize(TextUtils.TruncateAt.END);
        }

        getBinding().setExpandabletext(getResources().getString(R.string.more));
        getBinding().expandableLayout.setOnExpansionUpdateListener(expansionFraction -> getBinding().lessButton.setRotation(0 * expansionFraction));
        getBinding().lessButton.setOnClickListener(view -> clickExpandable());
    }

    public void clickExpandable() {
        getBinding().descriptionText.toggle();
        getBinding().descriptionText.setEllipsis("...");
        if (getBinding().descriptionText.isExpanded()) {
            getBinding().descriptionText.setEllipsize(null);
        } else {
            getBinding().descriptionText.setEllipsize(TextUtils.TruncateAt.END);
        }

        if (getBinding().expandableLayout.isExpanded()) {
            getBinding().setExpandabletext(getResources().getString(R.string.more));

        } else {
            getBinding().setExpandabletext(getResources().getString(R.string.less));
        }
        if (view != null) {
            getBinding().expandableLayout.expand();
        }
        getBinding().expandableLayout.collapse();
    }

    private NetworkChangeReceiver receiver = null;


    @Override
    public void onBackPressed() {
        try {
//            if (playerFragment != null) {
//                AppCommonMethod.trackFcmCustomEvent(EpisodeActivity.this, AppConstants.CONTENT_EXIT, videoDetails.getAssetType(), "", "", 0, videoDetails.getTitle(), 0, videoDetails.getSeriesId(), playerFragment.getBaseVideoView().getCurrentPosition(), videoDetails.getDuration(), "", "", "", "");
//            }

        } catch (Exception ignored) {

        }


        if (commentsFragment != null) {
            removeCommentFragment();
        } else {
            //AppCommonMethod.isPurchase = true;
            if (preference.getAppPrefJumpBack()) {
                preference.setAppPrefJumpBackId(0);
                preference.setAppPrefVideoPosition(String.valueOf(0));
                preference.setAppPrefJumpBack(false);
                preference.setAppPrefGotoPurchase(false);
                preference.setAppPrefIsEpisode(false);
            }
            preference.setAppPrefAssetId(0);
            AppCommonMethod.seasonId = -1;
            int orientation = this.getResources().getConfiguration().orientation;
            if (orientation == Configuration.ORIENTATION_PORTRAIT) {
              //  playerfragment.BackPressClicked(1);
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        if(playerfragment!=null) {
                            playerfragment.stopPlayback();
                        }
                    }
                },1500);

                finish();
            } else {
                if (playerfragment != null) {
                    playerfragment.BackPressClicked(2);
                }
            }
        }
    }


    @Override
    protected void onPause() {
        super.onPause();
//        if (fragment != null)
//            fragment.onPause();
        if (receiver != null) {
            this.unregisterReceiver(receiver);
            if (NetworkChangeReceiver.connectivityReceiverListener != null)
                NetworkChangeReceiver.connectivityReceiverListener = null;
        }
        releaseAudioFocusForMyApp();


    }

    @Override
    protected void onStop() {
        super.onStop();
    }

    @Override
    protected void onDestroy() {
        Logger.e("EpisodeActivity", "onDestroy");
        super.onDestroy();

        preference.setAppPrefAssetId(0);
        preference.setAppPrefJumpTo("");
        preference.setAppPrefBranchIo(false);
        AppCommonMethod.seasonId = -1;
        isActive = false;
    }


    @Override
    public void onFinishDialog() {
        Logger.w("onfinishdialog", "episode");
        if (isPremium) {
            isPremium = false;
            int orientation = this.getResources().getConfiguration().orientation;
            if (orientation == Configuration.ORIENTATION_LANDSCAPE) {
//                if (playerFragment != null) {
//                    playerFragment.BackPressClicked(2);
//                }
            }
            return;
        }
        if (isloggedout)
            logoutUser();

        if (isPlayerError) {
            getBinding().playerImage.setVisibility(View.VISIBLE);
            ImageHelper.getInstance(EpisodeActivity.this).loadListImage(getBinding().playerImage, videoDetails.getPosterURL());
            isPlayerError = false;

        } else {
            finish();
        }
    }

    public void setConnectivityListener() {
        NetworkChangeReceiver.connectivityReceiverListener = listener;
    }

    @Override
    public void onNetworkConnectionChanged(boolean isConnected) {
        //fragment.playVideo();
        //AppCommonMethod.isInternet = fragment != null;

    }

    @Override
    public void onAudioFocusChange(int focusChange) {
        AudioManager manager = (AudioManager) this.getSystemService(Context.AUDIO_SERVICE);
        if (manager.isMusicActive()) {

            switch (focusChange) {
                case AudioManager.AUDIOFOCUS_GAIN:
                    Logger.i(TAG, "AUDIOFOCUS_GAIN");
                    //restart/resume your sound
                    break;
                case AUDIOFOCUS_LOSS:
                    Logger.e(TAG, "AUDIOFOCUS_LOSS");
                    //Loss of audio focus for a long time
                    //Stop playing the sound
//                    if (fragment != null)

                    break;
                case AudioManager.AUDIOFOCUS_LOSS_TRANSIENT:
                    Logger.e(TAG, "AUDIOFOCUS_LOSS_TRANSIENT");
                    //Loss of audio focus for a short time
                    //Pause playing the sound
                    break;
                case AudioManager.AUDIOFOCUS_LOSS_TRANSIENT_CAN_DUCK:
                    Logger.e(TAG, "AUDIOFOCUS_LOSS_TRANSIENT_CAN_DUCK");
                    //Loss of audio focus for a short time.
                    //But one can duck. Lower the volume of playing the sound
                    break;

                default:
                    //
            }

            // do something - or do it not
        }
    }


    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        if (!isCastConnected) {
            super.onConfigurationChanged(newConfig);
            boolean isTablet = EpisodeActivity.this.getResources().getBoolean(R.bool.isTablet);
            AppCommonMethod.isOrientationChanged = true;
            //if (isTablet) {
            if (newConfig.orientation == 2) {
                hideVideoDetail();
            } else {
                showVideoDetail();
            }
        }
    }

    public void showVideoDetail() {
        getBinding().rootScroll.setVisibility(View.VISIBLE);
//
        ViewGroup.LayoutParams layoutParams = getBinding().header.getLayoutParams();
        layoutParams.height = AppCommonMethod.convertDpToPixel(0);
        layoutParams.width = ViewGroup.LayoutParams.MATCH_PARENT;
        getBinding().header.setLayoutParams(layoutParams);
        getBinding().header.requestLayout();
    }


    public void hideVideoDetail() {
        // getBinding().rootScroll.setBackgroundColor(Color.BLACK);
        getBinding().rootScroll.setVisibility(View.GONE);

        ViewGroup.LayoutParams layoutParams = getBinding().header.getLayoutParams();
        layoutParams.height = ViewGroup.LayoutParams.MATCH_PARENT;
        layoutParams.width = ViewGroup.LayoutParams.MATCH_PARENT;
        getBinding().header.setLayoutParams(layoutParams);
        getBinding().header.requestLayout();
    }

    @Override
    public void railItemClick() {
        if (item.getScreenWidget().getType() != null && item.getScreenWidget().getLayout().equalsIgnoreCase(Layouts.HRO.name())) {
            Toast.makeText(EpisodeActivity.this, item.getScreenWidget().getLandingPageType(), Toast.LENGTH_LONG).show();
        } else {
            if (AppCommonMethod.getCheckBCID(item.getEnveuVideoItemBeans().get(position).getkEntryId())) {
                Long getVideoId = Long.parseLong(item.getEnveuVideoItemBeans().get(position).getkEntryId());
                AppCommonMethod.launchDetailScreen(this, getVideoId.toString(), AppConstants.Video, item.getEnveuVideoItemBeans().get(position).getId(), "0", false,item.getEnveuVideoItemBeans().get(position));
            }
        }
    }

    @Override
    public void moreRailClick() {
        if (data.getScreenWidget() != null && data.getScreenWidget().getContentID() != null) {
            String playListId = data.getScreenWidget().getContentID();
            String screenName = "";
            if (data.getScreenWidget().getName() != null) {
                screenName = (String) data.getScreenWidget().getName();
            }
            Intent intent = new Intent(this, ListActivity.class);
            intent.putExtra("playListId", playListId);
            intent.putExtra("title", screenName);
            intent.putExtra("flag", 0);
            intent.putExtra("shimmerType", 0);
            intent.putExtra("baseCategory", data.getScreenWidget());
            startActivityForResult(intent, 1001);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
    }

    boolean fromBingWatch = false;
    int playerOrientation = -1;
    EnveuVideoItemBean nextEpisode = null;
//


    //
    @Override
    public void bingeWatchCall() {
        int nextEpisdoeId = 0;
        int iValue = -1;
        if (seasonEpisodesList != null && seasonEpisodesList.size() > 0) {
            for (int i = 0; i < seasonEpisodesList.size(); i++) {
                int id = seasonEpisodesList.get(i).getId();
                if (id == assestId) {
                    nextEpisdoeId = seasonEpisodesList.get(i + 1).getId();
                    iValue = i + 1;
                    break;
                }
            }
            if (iValue > -1) {
                nextEpisode = seasonEpisodesList.get(iValue);
                getBinding().backButton.setVisibility(View.VISIBLE);
                getBinding().playerImage.setVisibility(View.VISIBLE);
                ImageHelper.getInstance(EpisodeActivity.this).loadListImage(getBinding().playerImage, seasonEpisodesList.get(iValue).getPosterURL());
                AppCommonMethod.isPurchase = false;
                seriesId = AppCommonMethod.seriesId;
                isHitPlayerApi = false;
                fromBingWatch = true;
                //playerOrientation=playerFragment.getCurrentOrientation();
                refreshDetailPage();

            }
        }
    }

    List<EnveuVideoItemBean> seasonEpisodesList;

    public void episodesList() {
        try {
//            if (Entryid != null) {
            seasonEpisodesList = new ArrayList<>();
            if (seasonEpisodes != null && seasonEpisodes.size() > 0) {
                for (int i = 0; i < seasonEpisodes.size(); i++) {
                    if (seasonEpisodes.get(i).getkEntryId() != null) {
                        String id = seasonEpisodes.get(i).getkEntryId();
                        Log.d("rtrtrtrtrt",id);
                        Log.d("rtrtrtrtrt",Entryid);
                        if (id.equalsIgnoreCase(String.valueOf(Entryid))) {
                            seasonEpisodesList.addAll(seasonEpisodes);
                            if (playerfragment != null) {
                                playerfragment.totalEpisodes(seasonEpisodesList.size());
                                playerfragment.currentEpisodes(i + 1);
                            }

                            break;
                        }
                    }
                }

            }
//            }

        } catch (Exception ignored) {

        }


    }

    public void showSeasonList() {
        getBinding().transparentLayout.setVisibility(View.VISIBLE);

        EpisodeActivity.SeasonListAdapter listAdapter = new EpisodeActivity.SeasonListAdapter(list, selectedSeasonId);
        builder = new AlertDialog.Builder(EpisodeActivity.this);
        LayoutInflater inflater = LayoutInflater.from(EpisodeActivity.this);
        View content = inflater.inflate(R.layout.season_custom_dialog, null);
        builder.setView(content);
        RecyclerView mRecyclerView = content.findViewById(R.id.my_recycler_view);
        ImageView imageView = content.findViewById(R.id.close);
        imageView.setOnClickListener(v -> {
            alertDialog.cancel();
            getBinding().transparentLayout.setVisibility(View.GONE);
        });

        //Creating Adapter to fill data in Dialog
        mRecyclerView.setLayoutManager(new LinearLayoutManager(EpisodeActivity.this));
        mRecyclerView.setAdapter(listAdapter);
        alertDialog = builder.create();
        alertDialog.getWindow().setBackgroundDrawable(ActivityCompat.getDrawable(EpisodeActivity.this, R.color.transparent_series));
        alertDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);

        if (alertDialog.getWindow() != null)
            alertDialog.getWindow().getAttributes().windowAnimations = R.style.SlidingDialogAnimation;


        alertDialog.show();
        WindowManager.LayoutParams lWindowParams = new WindowManager.LayoutParams();
        lWindowParams.copyFrom(alertDialog.getWindow().getAttributes());
        lWindowParams.width = WindowManager.LayoutParams.MATCH_PARENT; // this is where the magic happens
        lWindowParams.height = WindowManager.LayoutParams.MATCH_PARENT;
        alertDialog.getWindow().setAttributes(lWindowParams);

    }

    @Override
    public void onClick() {
        this.isClickedTrailor  = isClicked;
        if (playerfragment!=null){
            playerfragment.pausePlayer();
        }
    }


    @Override
    public void fromAdapterStatusChanged(@NonNull OfflineManager.AssetDownloadState state, @NonNull String assetID) {

    }

    @Override
    public void onSeriesDownloadClicked(@NotNull View view, @NotNull Object source, @NotNull String videoId) {

    }

    class SeasonListAdapter extends RecyclerView.Adapter<EpisodeActivity.SeasonListAdapter.ViewHolder> {
        private final ArrayList<SelectedSeasonModel> list;
        private final int selectedPos;

        public SeasonListAdapter(ArrayList<SelectedSeasonModel> list, int selectedPos) {
            this.list = list;
            this.selectedPos = selectedPos;
        }

        @NonNull
        @Override
        public EpisodeActivity.SeasonListAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.all_season_listing, parent, false);

            return new EpisodeActivity.SeasonListAdapter.ViewHolder(view);
        }

        @Override
        public void onBindViewHolder(@NonNull final EpisodeActivity.SeasonListAdapter.ViewHolder holder, final int position) {
            holder.season.setText(list.get(position).getList());
            if (list.get(position).isSelected()) {
                holder.season.setTextColor(getResources().getColor(R.color.moretitlecolor));
                Typeface boldTypeface = Typeface.defaultFromStyle(Typeface.BOLD);
                holder.season.setTypeface(boldTypeface);

            } else {
                holder.season.setTextColor(getResources().getColor(R.color.bottom_nav_color_f));
                Typeface boldTypeface = Typeface.defaultFromStyle(Typeface.NORMAL);
                holder.season.setTypeface(boldTypeface);

            }

            holder.season.setOnClickListener(v -> {
                alertDialog.cancel();
                getBinding().transparentLayout.setVisibility(View.GONE);
                if (seasonTabFragment != null) {
                    seasonTabFragment.updateTotalPages();
                    seasonTabFragment.setSeasonAdapter();
                    seasonTabFragment.setSelectedSeason(list.get(position).getSelectedId());
                    showLoading(getBinding().progressBar);
                    seasonTabFragment.updateTotalPages();
                    seasonTabFragment.getSeasonEpisodes();

                }

            });
            //TODO TrackerEvent Selection implementation

        }

        @Override
        public int getItemCount() {
            return list.size();
        }


        class ViewHolder extends RecyclerView.ViewHolder {
            TextView season;

            public ViewHolder(View itemView) {
                super(itemView);
                season = itemView.findViewById(R.id.season_name);
            }
        }

    }

//    @Override
//    public void onFragmentInteraction(Uri uri) {
//
//    }

    @Override
    protected void onUserLeaveHint() {
        super.onUserLeaveHint();
        if (supportsPiPMode()) {
            try {
//                PictureInPictureManager.getInstance().onUserLeaveHint();
//                if (playerFragment != null) {
//                    playerFragment.hideControls();
//                }
            } catch (Exception ignored) {

            }

        }
    }

//    @Override
//    public void isInPip(boolean status) {
//        DetailActivity.isBackStacklost = status;
//    }

    @Override
    public void onPictureInPictureModeChanged(boolean isInPictureInPictureMode, Configuration newConfig) {
        super.onPictureInPictureModeChanged(isInPictureInPictureMode, newConfig);
        if (supportsPiPMode()) {
//            PictureInPictureManager.getInstance().onPictureInPictureModeChanged(isInPictureInPictureMode, newConfig);
            ADHelper.getInstance(EpisodeActivity.this).pipActivity(EpisodeActivity.this);
//            playerFragment.onPictureInPictureModeChanged(isInPictureInPictureMode, newConfig);
        }
    }

    public boolean supportsPiPMode() {
//        return Build.VERSION.SDK_INT >= Build.VERSION_CODES.O;
        boolean isPipSupported = false;
//        return Build.VERSION.SDK_INT >= Build.VERSION_CODES.O;
        isPipSupported = Build.VERSION.SDK_INT >= Build.VERSION_CODES.O;
        return isPipSupported;

    }

    boolean isPlayerError = false;

    //    @Override
//    public void onPlayerError(String error) {
//        try {
//            getBinding().backButton.setVisibility(View.VISIBLE);
//            getBinding().pBar.setVisibility(View.GONE);
//            String errorMessage = getString(R.string.player_error);
//            if (!NetworkConnectivity.isOnline(this)) {
//                if (!isOfflineAvailable) {
//                    playerFragment.getBaseVideoView().pause();
//                }
//            } else {
//                if (!errorDialogShown) {
//                    isPlayerError = true;
//                    errorDialogShown = true;
//                    FragmentManager fm = getSupportFragmentManager();
//                    errorDialog = AlertDialogSingleButtonFragment.newInstance("", errorMessage, getResources().getString(R.string.ok));
//                    errorDialog.setCancelable(false);
//                    errorDialog.setAlertDialogCallBack(new AlertDialogFragment.AlertDialogListener() {
//                        @Override
//                        public void onFinishDialog() {
//                            getBinding().backButton.setVisibility(View.VISIBLE);
//                            getBinding().playerImage.setVisibility(View.VISIBLE);
//                            ImageHelper.getInstance(EpisodeActivity.this).loadListImage(getBinding().playerImage, videoDetails.getPosterURL());
//                            isPlayerError = false;
//
//                        }
//                    });
//                    errorDialog.show(fm, "fragment_alert");
//                }
//            }
//        } catch (Exception e) {
//
//        }
//
//    }
//
//
    @Override
    public void onBookmarkCall() {
        if (isLogin) {
            BookmarkingViewModel bookmarkingViewModel = ViewModelProviders.of(this).get(BookmarkingViewModel.class);
            bookmarkingViewModel.bookmarkVideo(token, assestId, (currentPosition / 1000));
        }
    }

    @Override
    public void onBookmarkFinish() {
        if (isLogin) {
            BookmarkingViewModel bookmarkingViewModel = ViewModelProviders.of(this).get(BookmarkingViewModel.class);
            bookmarkingViewModel.finishBookmark(token, assestId);
        }
    }

    @Override
    public void onCurrentPosition() {
        this.playerCurrentPosition = currentPosition;
    }

    //
//    @Override
//    public void onPlayerInProgress() {
//        double totalDuration = videoDetails.getDuration();
//        double currentPosition = playerFragment.getBaseVideoView().getCurrentPosition();
//        double percentagePlayed = ((currentPosition / totalDuration) * 100L);
//        Log.d("PercentagePlayed", percentagePlayed + "");
//
//        if (percentagePlayed >= 95) {
//            if (!hitEvent) {
//                try {
//                    AppCommonMethod.trackFcmCustomEvent(EpisodeActivity.this, AppConstants.CONTENT_COMPLETED, videoDetails.getAssetType(), "", "", 0, videoDetails.getTitle(), 0, videoDetails.getSeriesId(), playerFragment.getBaseVideoView().getCurrentPosition(), videoDetails.getDuration(), "", "", "", "");
//                    hitEvent = true;
//                    Log.d("PercentagePlayed", "success");
//                } catch (Exception ignored) {
//
//                }
//            }
//
//        }
//
//    }
//
    @Override
    public void onPlayerStart() {
        try {
            getBinding().backButton.setVisibility(View.GONE);
            getBinding().playerImage.setVisibility(View.GONE);
            getBinding().pBar.setVisibility(View.GONE);
            if (playerfragment != null && seasonEpisodesList != null && seasonEpisodesList.size() > 0) {
                playerfragment.totalEpisodes(seasonEpisodesList.size());
                for (int i = 0; i < seasonEpisodesList.size(); i++) {
                    int id = seasonEpisodesList.get(i).getId();
                    if (id == assestId) {
                        playerfragment.currentEpisodes(i + 1);
                        break;
                    } else {
                        playerfragment.bingeWatchStatus(true);
                    }
                }
            }

            if (seasonEpisodesList == null || seasonEpisodesList.size() <= 0) {
                if (playerfragment != null) {
                    playerfragment.bingeWatchStatus(false);
                }
            }
            playerfragment.skipIntroStatus(false);

            if (playerfragment!=null){
                if (seasonTabFragment!=null){
                    seasonTabFragment.isPlayerStart();
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);
//        GoogleCastComponent.setUpMediaRouteButton(this, menu);
        return true;
    }


    String downloadId="";
    @Override
    public void onDownloadClicked(String videoId, Object position, Object source) {
        if (getBinding().tvBuyNow.getVisibility()==View.VISIBLE){
            showDownloadPopup();
            return;
        }
        if (source instanceof UserInteractionFragment){
            downloadId=Entryid;
        }else {
            downloadId=videoId;
        }
        if (source instanceof UserInteractionFragment || source instanceof EpisodeActivity) {
            boolean loginStatus = preference.getAppPrefLoginStatus();
            if (!loginStatus) {
                loginClicked = true;
                new ActivityLauncher(this).loginActivity(this, LoginActivity.class);
            }else {
                int videoQuality = new SharedPrefHelper(this).getInt(SharedPrefesConstants.DOWNLOAD_QUALITY_INDEX, 3);
                if (KsPreferenceKeys.getInstance().getDownloadOverWifi() == 1) {
                    if (NetworkHelper.INSTANCE.isWifiEnabled(this)) {
                        if (videoQuality != 3) {
                            userInteractionFragment.setDownloadStatus();
                            if (videoDetails!=null && videoId!=null && !videoId.equalsIgnoreCase("")){
                                String[] array = getResources().getStringArray(R.array.download_quality);
                                userInteractionFragment.setDownloadStatus();
                                int pos =  new SharedPrefHelper(EpisodeActivity.this).getInt(SharedPrefesConstants.DOWNLOAD_QUALITY_INDEX, 3);
                                startDownload();
                            }
                        } else {
                            selectDownloadVideoQuality();
                        }
                    } else {
                        showWifiSettings();
//                        downloadHelper.checkDownloadStatus(downloadAbleVideo);
                        //Toast.makeText(this, "NoWifi", Toast.LENGTH_LONG).show();
                    }
                } else {
                    if (videoQuality != 3) {
                        userInteractionFragment.setDownloadStatus();
                        if (videoDetails!=null && videoId!=null && !videoId.equalsIgnoreCase("")){
                            String[] array = getResources().getStringArray(R.array.download_quality);
                            userInteractionFragment.setDownloadStatus();
                            int pos =  new SharedPrefHelper(EpisodeActivity.this).getInt(SharedPrefesConstants.DOWNLOAD_QUALITY_INDEX, 3);
                            startDownload();
                        }
                    } else {
                        selectDownloadVideoQuality();
                    }
                }
            }
        }
    }

    private void showDownloadPopup() {
        try {
            if (KsPreferenceKeys.getInstance().getAppLanguage().equalsIgnoreCase("Thai") || KsPreferenceKeys.getInstance().getAppLanguage().equalsIgnoreCase("???????????????")) {
                AppCommonMethod.resetLanguage("th", EpisodeActivity.this);
            } else if (KsPreferenceKeys.getInstance().getAppLanguage().equalsIgnoreCase("English")) {
                AppCommonMethod.resetLanguage("en", EpisodeActivity.this);
            }
            if (KsPreferenceKeys.getInstance().getAppPrefLoginStatus()){
                showDownloadDialog();
            }else {
                showDownloadDialog();
            }
        }catch (Exception ignored){

        }
    }

    private void showDownloadDialog() {
        FragmentManager fm = getSupportFragmentManager();
        PremiumDownloadPopup alertDialog = PremiumDownloadPopup.newInstance(title, message, buttonText);
        alertDialog.setCancelable(false);
        alertDialog.setAlertDialogCallBack(new PremiumDownloadPopup.AlertDialogListener() {
            @Override
            public void onFinishDialog() {
                if (type==1){
                    comingSoon();
                }else {
                    new ActivityLauncher(EpisodeActivity.this).loginActivity(EpisodeActivity.this, LoginActivity.class);
                }
            }
        });
        alertDialog.show(fm, "fragment_alert");
    }

    private void startDownload() {
        if (seasonTabFragment!=null && seasonTabFragment.getSeasonAdapter()!=null){
            if (seasonTabFragment.getSeasonAdapter().getAdapterList()!=null && seasonTabFragment.getSeasonAdapter().getAdapterList().size()>0){
                Log.w("instanceof","2");
                int position=AppCommonMethod.getDownloadPosition(seasonTabFragment.getSeasonAdapter().getAdapterList(),videoId);
                EnveuVideoItemBean videoDetails=seasonTabFragment.getSeasonAdapter().getAdapterList().get(position);
                downloadHelper.startDownload(pos,videoDetails.getkEntryId(),videoDetails.getTitle(),videoDetails.getAssetType(),videoDetails.getSeriesId(),videoDetails.getName(),videoDetails.getPosterURL(),String.valueOf(videoDetails.getEpisodeNo()),seasonTabFragment.getSelectedSeason(),videoDetails.getSeriesImageURL());
                if (seasonTabFragment!=null){
                    seasonTabFragment.notifySingleItem();
                }
            }

        }
    }


    private void showWifiSettings() {
        downloadHelper.changeWifiSetting(new WifiPreferenceListener() {
            @Override
            public void actionP(int value) {
                if (value == 0) {
                        if (videoQuality != 3) {
                            if (videoDetails!=null && downloadId!=null && !downloadId.equalsIgnoreCase("")){
                                String[] array = getResources().getStringArray(R.array.download_quality);
                                userInteractionFragment.setDownloadStatus();
                                int pos =  new SharedPrefHelper(EpisodeActivity.this).getInt(SharedPrefesConstants.DOWNLOAD_QUALITY_INDEX, 3);
                                startDownload();
                            }

                        } else {
                            selectDownloadVideoQuality();
                        }
                }
            }
        });
    }

    private void selectDownloadVideoQuality() {
        try {

                downloadHelper.selectVideoQuality(typeofTVOD,position -> {
                    if (videoDetails!=null && downloadId!=null && !downloadId.equalsIgnoreCase("")){
                        String[] array = getResources().getStringArray(R.array.download_quality);
                        if (Entryid.equalsIgnoreCase(downloadId)){
                            userInteractionFragment.setDownloadStatus();
                        }
                        //Log.w("store_seriesid 3",videoDetails.getSeriesId());
                        int pos =  new SharedPrefHelper(EpisodeActivity.this).getInt(SharedPrefesConstants.DOWNLOAD_QUALITY_INDEX, 3);
                        startDownload();
                    }
                });


        }catch (Exception ignored){

        }

    }

    @Override
    public void onProgressbarClicked(View view, Object source, String videoId) {
        AppCommonMethod.showPopupMenu(this, view, R.menu.download_menu, item -> {
            if (source instanceof UserInteractionFragment){
                switch (item.getItemId()) {
                    case R.id.cancel_download:
                        userInteractionFragment.setDownloadStatus();
                        downloadHelper.cancelVideo(videoId);

                        break;
                    case R.id.pause_download:
                        Log.w("pauseVideo", "pop");
                        userInteractionFragment.setDownloadStatus();
                        downloadHelper.pauseVideo(videoId);

                        break;
                }
            }else {
                switch (item.getItemId()) {
                case R.id.cancel_download:
                    downloadHelper.cancelVideo(videoId);
                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            if (seasonTabFragment!=null){
                                seasonTabFragment.cancelDownload();
                            }
                        }
                    },500);

                    break;
                case R.id.pause_download:
                    Log.w("pauseVideo", "pop");
                   // userInteractionFragment.setDownloadStatus(panteao.make.ready.enums.DownloadStatus.REQUESTED);
                    downloadHelper.pauseVideo(videoId);
                    break;
            }
            }

            return false;
        });
    }

    @Override
    public void onDownloadCompleteClicked(View view, Object source, String videoId) {
        AppCommonMethod.showPopupMenu(this, view, R.menu.delete_menu, item -> {
            switch (item.getItemId()) {
                case R.id.delete_download:
                    Log.w("onDownloadCompleteClik",videoId);
                    if (userInteractionFragment!=null){
                        userInteractionFragment.setDownloadStatus();
                    }
                    downloadHelper.cancelVideo(videoId);
                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            if (seasonTabFragment!=null){
                                seasonTabFragment.cancelDownload();
                            }
                        }
                    },500);

                    break;
            }
            return false;
        });
    }

    @Override
    public void onPauseClicked(String videoId, Object source) {
        Log.w("pauseClicked", "in2");
        if (NetworkConnectivity.isOnline(this)) {
            if (KsPreferenceKeys.getInstance().getDownloadOverWifi() == 1) {
                if (NetworkHelper.INSTANCE.isWifiEnabled(this)) {
                    Log.w("pauseClicked", "in3"+videoId);
                    if (source instanceof UserInteractionFragment){
                        userInteractionFragment.setDownloadStatus();
                        downloadHelper.resumeDownload(videoId);
                    }else {
                        //userInteractionFragment.setDownloadStatus(panteao.make.ready.enums.DownloadStatus.REQUESTED);
                        downloadHelper.resumeDownload(videoId);
                    }

                } else {
                    //Toast.makeText(this, "NoWifi", Toast.LENGTH_LONG).show();
                }
            } else {
                if (source instanceof UserInteractionFragment){
                    userInteractionFragment.setDownloadStatus();
                    Log.w("pauseClicked", "in4");
                    downloadHelper.resumeDownload(videoId);

                }else {
                    //userInteractionFragment.setDownloadStatus(panteao.make.ready.enums.DownloadStatus.REQUESTED);
                    Log.w("pauseClicked", "in4");
                    downloadHelper.resumeDownload(videoId);
                }

            }
        } else {
            Toast.makeText(this, getResources().getString(R.string.no_internet_connection), Toast.LENGTH_LONG).show();
        }

    }


    @Override
    public void onDownloadDeleted(@NotNull String videoId, @NotNull Object source) {
        Logger.e(TAG, "onDownloadDeleted--->>" + 3);
        try {
            Log.w("cancelVideo","-->onDownloadDeleted");
            if (videoId!=null && !videoId.equalsIgnoreCase("")){
                downloadHelper.cancelVideo(videoId);
                seasonTabFragment.cancelDownload();
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        if (seasonTabFragment!=null){
                            seasonTabFragment.cancelDownload();
                            if (userInteractionFragment!=null && userInteractionFragment.getBinding()!=null){
                                if (seasonTabFragment.getSeasonAdapter()!=null && seasonTabFragment.getSeasonAdapter().getAdapterList()!=null){
                                    if (seasonTabFragment.getSeasonAdapter().getAdapterList().size()>0){
                                        userInteractionFragment.checkDownloadStatus();
                                    }
                                }
                            }

                        }
                    }
                },500);
            }
        }catch (Exception ignored){

        }

    }

//    @Override
//    public void chromeCastViewConnected(boolean status) {
//        if (status) {
//            Intent intent = new Intent(this, DefaultExpandedControllerActivity.class);
//            startActivity(intent);
//            finish();
//            isCastConnected = true;
//        }
//    }



//    @Override
    @Override
    public void setDownloadProgressListener(float progress,String assetId) {
        Logger.e(TAG, "onDownloadProgress" + progress+"  ------ "+(int)progress+"   "+assetId);
        if (userInteractionFragment != null) {
          //  String string = String.format(Locale.ROOT, "%.1f", progress);
           // Log.e("finalPer",string);
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    if (Entryid!=null && !Entryid.equalsIgnoreCase("") && Entryid.equalsIgnoreCase(assetId)){
                        userInteractionFragment.setDownloadStatus();
                        userInteractionFragment.setDownloadProgress();
                    }
                }
            });

        }
    }

    @Override
    public void onDownloadPaused(@NonNull @NotNull String assetId) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        Logger.e(TAG, "onDownloadProgress" +"  ------ "+"paused");
                        OfflineManager.AssetInfo info=downloadHelper.getManager().getAssetInfo(assetId);
                        if (info!=null){
                            userInteractionFragment.setDownloadStatus();
                        }else {
                            userInteractionFragment.setDownloadStatus();
                        }

                    }
                },1000);

            }
        });

    }

    OfflineManager.AssetDownloadState downloadState;
    @Override
    public void initialStatus() {
        this.downloadState=state;
    }

    @Override
    public void onStateChanged(@NonNull @NotNull OfflineManager.AssetDownloadState state,String assetId) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                try {
                    userInteractionFragment.setDownloadStatus();
                    userInteractionFragment.setDownloadProgress();
                }catch (Exception ignored){

                }
            }
        });
    }

    @Override
    public void onAssetDownloadComplete(@NonNull @NotNull String assetId) {
        if (assetId.equalsIgnoreCase(Entryid)){
            OfflineManager.AssetInfo info=downloadHelper.getManager().getAssetInfo(assetId);
            if (info!=null){
                userInteractionFragment.setDownloadStatus();
            }else {
                userInteractionFragment.setDownloadStatus();
            }
        }

    }

    @Override
    public void onAssetDownloadFailed() {
        if (NetworkConnectivity.isOnline(this)) {
            userInteractionFragment.setDownloadStatus();
            userInteractionFragment.setDownloadProgress();
        }else {
            OfflineManager.AssetInfo info=downloadHelper.getManager().getAssetInfo(assetId);
            if (info!=null){
                downloadHelper.pauseVideo(assetId);
               // userInteractionFragment.setDownloadStatus(AppCommonMethod.getDownloadStatus(OfflineManager.AssetDownloadState.paused));
            }else {
                userInteractionFragment.setDownloadStatus();
            }
        }
    }

    private void videoDeletedFromList() {
        if (userInteractionFragment!=null){
            userInteractionFragment.setDownloadStatus();
            KsPreferenceKeys.getInstance().setVideoDownloadAction(-1);
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        try {
            if (isClickedTrailor) {
                if (playerfragment != null) {
                    fromOnStart = true;
                    playerfragment = null;
                    setPlayerFragment();

                    playerfragment.passCurrentPosition(playerCurrentPosition, fromOnStart);
                    isClickedTrailor = false;
                    fromOnStart = false;
                }
            }
        }catch (Exception e){
            Log.e("ErrorIs",e.getLocalizedMessage());
        }
    }

    int countCall = 0;
    int countCall2 = 2;
    @Override
    public void fromAdapterDownloadProgress(float progress, @NonNull String assetId) {
        if (userInteractionFragment != null) {
            //  String string = String.format(Locale.ROOT, "%.1f", progress);
            // Log.e("finalPer",string);
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    if (Entryid!=null && !Entryid.equalsIgnoreCase("") && Entryid.equalsIgnoreCase(assetId)){
                        userInteractionFragment.setDownloadStatus();
                        userInteractionFragment.setDownloadProgress();
                    }
                }
            });

        }
        if (seasonTabFragment!=null){
            seasonTabFragment.updateAdapter();
        }
    }

    @Override
    public void fromAdapterPaused(@NonNull String assetID) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        Logger.e(TAG, "onDownloadProgress" +"  ------ "+"paused");
                        if (assetID.equalsIgnoreCase(Entryid)){
                            OfflineManager.AssetInfo info=downloadHelper.getManager().getAssetInfo(assetID);
                            if (info!=null){
                                userInteractionFragment.setDownloadStatus();
                            }else {
                                userInteractionFragment.setDownloadStatus();
                            }
                        }
                    }
                },1000);

            }
        });

    }

    @Override
    public void fromAdapterStatus(@NonNull OfflineManager.AssetDownloadState state, @NonNull String assetID) {
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                if (seasonTabFragment!=null && downloadHelper!=null){
                    OfflineManager.AssetInfo info=downloadHelper.getManager().getAssetInfo(assetID);
                    if (info!=null){
                        userInteractionFragment.setDownloadStatus();
                    }else {
                        userInteractionFragment.setDownloadStatus();
                    }
                }
            }
        },500);

    }
}
