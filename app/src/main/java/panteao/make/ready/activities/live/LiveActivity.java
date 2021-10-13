package panteao.make.ready.activities.live;

import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.res.Configuration;
import android.media.AudioAttributes;
import android.media.AudioFocusRequest;
import android.media.AudioManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.SystemClock;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.ViewModelProvider;

import com.make.enums.Layouts;

import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.Objects;
import java.util.Timer;
import java.util.concurrent.TimeUnit;

import panteao.make.ready.Bookmarking.BookmarkingViewModel;
import panteao.make.ready.R;
import panteao.make.ready.SDKConfig;
import panteao.make.ready.activities.show.viewModel.DetailViewModel;
import panteao.make.ready.activities.listing.listui.ListActivity;
import panteao.make.ready.activities.purchase.callBack.EntitlementStatus;
import panteao.make.ready.activities.purchase.planslayer.GetPlansLayer;
import panteao.make.ready.activities.purchase.ui.PurchaseActivity;
import panteao.make.ready.activities.purchase.ui.VodOfferType;
import panteao.make.ready.activities.usermanagment.ui.LoginActivity;
import panteao.make.ready.adapters.commonRails.CommonAdapterNew;
import panteao.make.ready.baseModels.BaseBindingActivity;
import panteao.make.ready.beanModel.AppUserModel;
import panteao.make.ready.beanModel.entitle.EntitledAs;
import panteao.make.ready.beanModel.entitle.ResponseEntitle;
import panteao.make.ready.beanModel.enveuCommonRailData.RailCommonData;
import panteao.make.ready.beanModel.responseModels.detailPlayer.Data;
import panteao.make.ready.beanModel.responseModels.detailPlayer.ResponseDetailPlayer;
import panteao.make.ready.beanModelV3.uiConnectorModelV2.EnveuVideoItemBean;
import panteao.make.ready.callbacks.commonCallbacks.CommonRailtItemClickListner;
import panteao.make.ready.callbacks.commonCallbacks.MoreClickListner;
import panteao.make.ready.callbacks.commonCallbacks.NetworkChangeReceiver;
import panteao.make.ready.databinding.ActivityLiveBinding;
import panteao.make.ready.fragments.dialog.AlertDialogFragment;
import panteao.make.ready.fragments.dialog.AlertDialogSingleButtonFragment;
import panteao.make.ready.fragments.player.ui.CommentsFragment;
import panteao.make.ready.fragments.player.ui.NontonPlayerExtended;
import panteao.make.ready.fragments.player.ui.RecommendationRailFragment;
import panteao.make.ready.fragments.player.ui.UserInteractionFragment;
import panteao.make.ready.liveplayer.LivePlayerFragment;
import panteao.make.ready.networking.apistatus.APIStatus;
import panteao.make.ready.networking.responsehandler.ResponseModel;
import panteao.make.ready.utils.MediaTypeConstants;
import panteao.make.ready.utils.commonMethods.AppCommonMethod;
import panteao.make.ready.utils.constants.AppConstants;
import panteao.make.ready.utils.cropImage.helpers.Logger;
import panteao.make.ready.utils.cropImage.helpers.NetworkConnectivity;
import panteao.make.ready.utils.cropImage.helpers.PrintLogging;
import panteao.make.ready.utils.helpers.CheckInternetConnection;
import panteao.make.ready.utils.helpers.ImageHelper;
import panteao.make.ready.utils.helpers.RailInjectionHelper;
import panteao.make.ready.utils.helpers.ToolBarHandler;
import panteao.make.ready.utils.helpers.downloads.OnDownloadClickInteraction;
import panteao.make.ready.utils.helpers.downloads.VideoListListener;
import panteao.make.ready.utils.helpers.intentlaunchers.ActivityLauncher;
import panteao.make.ready.utils.helpers.ksPreferenceKeys.KsPreferenceKeys;

import static android.media.AudioManager.AUDIOFOCUS_LOSS;

public class LiveActivity extends BaseBindingActivity<ActivityLiveBinding> implements AlertDialogFragment.AlertDialogListener, NetworkChangeReceiver.ConnectivityReceiverListener, AudioManager.OnAudioFocusChangeListener, CommonRailtItemClickListner, MoreClickListner, OnDownloadClickInteraction, VideoListListener {

    public long videoPos = 0;
    public boolean isloggedout = false;
    public String userName = "";
    public int commentCounter = 0;
    String TAG = "DetailActivity";
    AudioManager audioManager;
    AudioFocusRequest focusRequest;
    EnveuVideoItemBean videoDetails;
    CommonAdapterNew adapterDetailRail;
    private Handler handler;
    private Runnable runnable;
    Timer timer = new Timer();
    private long mLastClickTime = 0;
    private DetailViewModel viewModel;
    private KsPreferenceKeys preference;
    private int assestId;
    private int seriesId;
    private int watchList = 0;
    private int watchListId = 0;
    private int likeCounter = 0;
    private String videoUrl = "";
    private String vastUrl = "";
    private String token;
    private ResponseDetailPlayer response;
    private boolean isLogin;
    private boolean loadingComment = true;
    private boolean isHitPlayerApi = false;
    private int playerApiCount = 0;
    private long brightCoveVideoId;
    private String tabId;
    private RailInjectionHelper railInjectionHelper;
    private FragmentTransaction transaction;
    private LivePlayerFragment playerFragment;
    private String sharingUrl;
    private String detailType;
    private AlertDialogSingleButtonFragment errorDialog;
    private boolean errorDialogShown = false;
    private BookmarkingViewModel bookmarkingViewModel;
    private MediaStore.Video downloadAbleVideo;
    private UserInteractionFragment userInteractionFragment;
    public static boolean isBackStacklost = false;
    private boolean isOfflineAvailable = false;

    @Override
    public ActivityLiveBinding inflateBindingLayout(@NonNull LayoutInflater inflater) {
        return ActivityLiveBinding.inflate(inflater);
    }


    public long getPositonVideo() {
        return videoPos;
    }

    boolean isLoggedIn = false;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setBackgroundDrawableResource(R.color.black);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_SECURE,
                WindowManager.LayoutParams.FLAG_SECURE);

        //change this id in future for rails in details
        tabId = SDKConfig.getInstance().getLiveDetailId();
        int orientation = this.getResources().getConfiguration().orientation;
        if (orientation == Configuration.ORIENTATION_PORTRAIT) {
            // code for portrait mode
            showVideoDetail();
        } else {
            // code for landscape mode
            hideVideoDetail();
        }

        //basic settings and do not require internet
        preference = KsPreferenceKeys.getInstance();
        if (preference.getAppPrefLoginStatus()) {
            isLoggedIn = true;
        }
        AppCommonMethod.isPurchase = false;
        viewModel = new ViewModelProvider(LiveActivity.this).get(DetailViewModel.class);
        bookmarkingViewModel =new ViewModelProvider(this).get(BookmarkingViewModel.class);

        setupUI(getBinding().llParent);
        commentCounter = 0;
        isHitPlayerApi = false;
        handler = new Handler(Looper.getMainLooper());

        if (getIntent().hasExtra(AppConstants.BUNDLE_ASSET_BUNDLE)) {
            Bundle extras = getIntent().getExtras();
            if (extras != null) {
                extras = extras.getBundle(AppConstants.BUNDLE_ASSET_BUNDLE);
                assestId = Objects.requireNonNull(extras).getInt(AppConstants.BUNDLE_ASSET_ID);
                videoPos = TimeUnit.SECONDS.toMillis(Long.parseLong(extras.getString(AppConstants.BUNDLE_DURATION)));
                brightCoveVideoId = Objects.requireNonNull(extras).getLong(AppConstants.BUNDLE_VIDEO_ID_BRIGHTCOVE);
                tabId = SDKConfig.getInstance().getLiveDetailId();//extras.getString(AppConstants.BUNDLE_DETAIL_TYPE, AppConstants.MOVIE_ENVEU);

            }
        } else {
            throw new IllegalArgumentException("Activity cannot find  extras " + "Search_Show_All");
        }
        callBinding();


    }


    private void connectionObserver() {
        if (NetworkConnectivity.isOnline(this)) {
            connectionValidation(true);
        } else {
            connectionValidation(false);
        }
    }

    private void connectionValidation(Boolean aBoolean) {
        if (aBoolean) {
            UIinitialization();
        } else {
            noConnectionLayout();
        }
    }

    private void callShimmer() {
        getBinding().seriesShimmer.setVisibility(View.VISIBLE);
        getBinding().mShimmer.seriesShimmerScroll1.setEnabled(false);
        getBinding().mShimmer.seriesShimmerScroll2.setEnabled(false);
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
        Logger.e("stopShimmer", String.valueOf(brightCoveVideoId));

        if (brightCoveVideoId != 0) {
            //  getBinding().playerImage.setVisibility(View.GONE);
        } else {
            //  getBinding().playerImage.setVisibility(View.VISIBLE);
        }

        getBinding().seriesShimmer.setVisibility(View.GONE);
        getBinding().llParent.setVisibility(View.VISIBLE);
        getBinding().noConnectionLayout.setVisibility(View.GONE);
        getBinding().mShimmer.sfShimmer1.startShimmer();
        getBinding().mShimmer.sfShimmer2.startShimmer();
        getBinding().mShimmer.sfShimmer3.startShimmer();
    }


    public void playPlayerWhenShimmer() {

        getBinding().pBar.setVisibility(View.VISIBLE);
        getBinding().backButton.setVisibility(View.GONE);
        long bookmarkPosition = 0l;

        transaction = getSupportFragmentManager().beginTransaction();
        playerFragment = new LivePlayerFragment();
        Bundle args = new Bundle();
        args.putString(AppConstants.BUNDLE_VIDEO_ID_BRIGHTCOVE, String.valueOf(brightCoveVideoId));
        args.putString(AppConstants.ASSETTYPE, MediaTypeConstants.getInstance().getLive());
        args.putLong(AppConstants.BOOKMARK_POSITION, bookmarkPosition);

        String isLiveDrm = videoDetails.getIslivedrm();
        if (isLiveDrm != null && !isLiveDrm.equalsIgnoreCase("")) {


            if (isLiveDrm.equalsIgnoreCase("true")) {
                if (videoDetails.getWidevineLicence() != null && !videoDetails.getWidevineLicence().equalsIgnoreCase("") &&
                        videoDetails.getGetWidevineURL() != null && !videoDetails.getGetWidevineURL().equalsIgnoreCase("")) {
                    args.putString("isLivedrm", videoDetails.getIslivedrm());
                    args.putString("widevine_licence", videoDetails.getWidevineLicence());
                    args.putString("widevine_url", videoDetails.getGetWidevineURL());

                    if (videoDetails.getThumbnailImage() != null) {
                        // ImageHelper.getInstance(LiveActivity.this).loadListImage(getBinding().channelLogo, videoDetails.getThumbnailImage());
                    } else if (videoDetails.getPosterURL() != null) {
                        // ImageHelper.getInstance(LiveActivity.this).loadListImage(getBinding().channelLogo, videoDetails.getPosterURL());
                    }

                    if (videoDetails.isPremium() && videoDetails.getThumbnailImage() != null) {
                        args.putString(AppConstants.BUNDLE_BANNER_IMAGE, videoDetails.getThumbnailImage());
                    }
                    playerFragment.setArguments(args);
                    transaction.replace(R.id.player_frame, playerFragment);
                    transaction.commit();
                } else {
                    getBinding().pBar.setVisibility(View.GONE);
                    getBinding().backButton.setVisibility(View.VISIBLE);
                    showErrorDialog();
                }
            } else {
                if (videoDetails.getGetWidevineURL() != null && !videoDetails.getGetWidevineURL().equalsIgnoreCase("")) {
                    // args.putString("widevine_licence",videoDetails.getWidevineLicence());
                    args.putString("isLivedrm", videoDetails.getIslivedrm());
                    args.putString("widevine_url", videoDetails.getGetWidevineURL());

                    if (videoDetails.getThumbnailImage() != null) {
                        // ImageHelper.getInstance(LiveActivity.this).loadListImage(getBinding().channelLogo, videoDetails.getThumbnailImage());
                    } else if (videoDetails.getPosterURL() != null) {
                        // ImageHelper.getInstance(LiveActivity.this).loadListImage(getBinding().channelLogo, videoDetails.getPosterURL());
                    }

                    if (videoDetails.isPremium() && videoDetails.getThumbnailImage() != null) {
                        args.putString(AppConstants.BUNDLE_BANNER_IMAGE, videoDetails.getThumbnailImage());
                    }
                    playerFragment.setArguments(args);
                    transaction.replace(R.id.player_frame, playerFragment);
                    transaction.commit();
                } else {
                    getBinding().pBar.setVisibility(View.GONE);
                    getBinding().backButton.setVisibility(View.VISIBLE);
                    showErrorDialog();
                }
            }
        } else {
            if (videoDetails.getGetWidevineURL() != null && !videoDetails.getGetWidevineURL().equalsIgnoreCase("")) {
                // args.putString("widevine_licence",videoDetails.getWidevineLicence());
                args.putString("isLivedrm", videoDetails.getIslivedrm());
                args.putString("widevine_url", videoDetails.getGetWidevineURL());

                if (videoDetails.getThumbnailImage() != null) {
                    // ImageHelper.getInstance(LiveActivity.this).loadListImage(getBinding().channelLogo, videoDetails.getThumbnailImage());
                } else if (videoDetails.getPosterURL() != null) {
                    // ImageHelper.getInstance(LiveActivity.this).loadListImage(getBinding().channelLogo, videoDetails.getPosterURL());
                }

                if (videoDetails.isPremium() && videoDetails.getThumbnailImage() != null) {
                    args.putString(AppConstants.BUNDLE_BANNER_IMAGE, videoDetails.getThumbnailImage());
                }
                playerFragment.setArguments(args);
                transaction.replace(R.id.player_frame, playerFragment);
                transaction.commit();
            } else {
                getBinding().pBar.setVisibility(View.GONE);
                getBinding().backButton.setVisibility(View.VISIBLE);
                showErrorDialog();
            }
        }


    }

    private void showErrorDialog() {
        String errorMessage = getString(R.string.player_error);
        FragmentManager fm = getSupportFragmentManager();
        AlertDialogSingleButtonFragment errorDialog = AlertDialogSingleButtonFragment.newInstance("", errorMessage, getResources().getString(R.string.ok));
        errorDialog.setCancelable(false);
        errorDialog.setAlertDialogCallBack(new AlertDialogFragment.AlertDialogListener() {
            @Override
            public void onFinishDialog() {

            }
        });
        errorDialog.show(fm, "fragment_alert");
    }


    private void setArgsForEvent(Bundle args) {
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
        boolean isTablet = LiveActivity.this.getResources().getBoolean(R.bool.isTablet);
        if (isTablet) {
            AppCommonMethod.isResumeDetail = true;
        }
        isloggedout = false;
        // if (fragment != null)
        //  fragment.pauseAds = false;
        dismissLoading(getBinding().progressBar);
        if (AppCommonMethod.isPurchase) {
            seriesId = AppCommonMethod.seriesId;
            AppCommonMethod.isPurchase = false;
            isHitPlayerApi = false;
            refreshDetailPage(assestId);
        }

        if (!isLoggedIn) {
            if (preference.getAppPrefLoginStatus()) {
                isLoggedIn = true;
                AppCommonMethod.isPurchase = false;
                seriesId = AppCommonMethod.seriesId;
                isHitPlayerApi = false;
                refreshDetailPage(assestId);
            }
        }

        setBroadcast();
        if (preference != null && userInteractionFragment != null) {
            AppCommonMethod.callSocialAction(preference, userInteractionFragment);
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

            // AudioManager.AUDIOFOCUS_GAIN_TRANSIENT
            focusRequest =
                    new AudioFocusRequest.Builder(AudioManager.STREAM_MUSIC)
                            .setAudioAttributes(playbackAttributes)
                            .setAcceptsDelayedFocusGain(true)
                            .setOnAudioFocusChangeListener(
                                    new AudioManager.OnAudioFocusChangeListener() {
                                        @Override
                                        public void onAudioFocusChange(int i) {
                                            if (i == AUDIOFOCUS_LOSS) {
                                                Logger.d("AudioFocus", "Loss");
//                                                if (playerFragment != null) {
//                                                    playerFragment.playPause();
//                                                }
                                            }
                                        }
                                    })
                            .build();

            audioManager.requestAudioFocus(focusRequest);
            switch (audioManager.requestAudioFocus(focusRequest)) {
                case AudioManager.AUDIOFOCUS_REQUEST_FAILED:
                    // don’t start playback
                    Logger.d("AudioFocus", "Failed");
                {
//                    if (fragment != null) {
//                        //    fragment.pauseOnOtherAudio();
//                    }
                }
                break;
                case AudioManager.AUDIOFOCUS_REQUEST_GRANTED:
                    Logger.d("AudioFocus", "Granted");
                    // actually start playback

            }
        } else {
            audioManager.requestAudioFocus(this, AudioManager.STREAM_VOICE_CALL,
                    AudioManager.AUDIOFOCUS_GAIN_TRANSIENT);

        }
    }

    void releaseAudioFocusForMyApp(final Context context) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            audioManager.abandonAudioFocusRequest(focusRequest);
        }
    }


    CommentsFragment commentsFragment;

    public void commentFragment(int id) {
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

    public void setBroadcast() {
        receiver = new NetworkChangeReceiver();
        IntentFilter filter = new IntentFilter();
        filter.addAction("android.net.conn.CONNECTIVITY_CHANGE");
        filter.addAction("android.net.wifi.WIFI_STATE_CHANGED");
        filter.addAction("android.net.wifi.STATE_CHANGE");
        LiveActivity.this.registerReceiver(receiver, filter);
        setConnectivityListener(this);
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        if (intent.hasExtra(AppConstants.BUNDLE_ASSET_BUNDLE)) {
            Bundle extras = intent.getExtras();
            if (extras != null) {
                //extras = extras.getBundle("assestIdBundle");
                assestId = Objects.requireNonNull(intent.getExtras().getBundle(AppConstants.BUNDLE_ASSET_BUNDLE)).getInt(AppConstants.BUNDLE_ASSET_ID);

                Logger.d("newintentCalled", assestId + "");
                refreshDetailPage(assestId);

            }
        } else {
            throw new IllegalArgumentException("Activity cannot find  extras " + "Search_Show_All");
        }
    }

    public void refreshDetailPage(int assestId) {
        callBinding();
    }


    private void callBinding() {
        modelCall();

    }

    private void modelCall() {

        new ToolBarHandler(this).setAction(getBinding());
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

    public void comingSoon() {
        if (SystemClock.elapsedRealtime() - mLastClickTime < 1000) {
            return;
        }
        mLastClickTime = SystemClock.elapsedRealtime();
        if (isLogin) {
            //showDialog(DetailActivity.this.getResources().getString(R.string.error), getResources().getString(R.string.you_are_not_entitled));
            AppCommonMethod.assetId = assestId;
            AppCommonMethod.seriesId = seriesId;
            if (responseEntitlementModel != null && responseEntitlementModel.getStatus()) {
                Intent intent = new Intent(LiveActivity.this, PurchaseActivity.class);
                intent.putExtra("response", videoDetails);
                intent.putExtra("assestId", assestId);
                intent.putExtra("contentType", AppConstants.ContentType.VIDEO.toString());
                intent.putExtra("responseEntitlement", responseEntitlementModel);
                startActivity(intent);
            }

        } else {
            preference.setAppPrefGotoPurchase(true);
            openLoginPage(getResources().getString(R.string.please_login_play));
        }
    }

    public void openLoginPage(String message) {
        preference.setReturnTo(AppConstants.ContentType.VIDEO.toString());
        // preference.setString(AppConstants.APP_PREF_JUMP_TO, AppConstants.ContentType.VIDEO.toString());
        preference.setAppPrefJumpBack(true);
        preference.setAppPrefIsEpisode(false);
        preference.setAppPrefJumpBackId(assestId);
        new ActivityLauncher(LiveActivity.this).loginActivity(LiveActivity.this, LoginActivity.class);

    }


    public void UIinitialization() {
        callShimmer();

        loadingComment = true;
        commentCounter = 0;
//        getBinding().tvBuyNow.setVisibility(View.GONE);
//        getBinding().tvPurchased.setVisibility(View.GONE);
//        getBinding().tvPremium.setVisibility(View.GONE);
        //getBinding().setDuration("");
        getBinding().setCasttext("");
        getBinding().setCrewtext("");

        EnveuVideoItemBean player = new EnveuVideoItemBean();
        Data data = new Data();
        data.setContentTitle("");
        getBinding().setResponseApi(player);

        setupUI(getBinding().llParent);
        if (getBinding().descriptionText.isExpanded())
            resetExpandable();

        response = new ResponseDetailPlayer();
        setExpandable();
        preference.setAppPrefAssetId(assestId);
        watchList = 0;
        likeCounter = 0;
        isLogin = preference.getAppPrefLoginStatus();
        token = preference.getAppPrefAccessToken();


        if (isLogin) {
            AppUserModel signInResponseModel = AppUserModel.getInstance();
            if (signInResponseModel != null) {
                userName = signInResponseModel.getName();
            }
        }

        getBinding().noConnectionLayout.setVisibility(View.GONE);

        Logger.d("newintentCalled", isHitPlayerApi + "");

        if (!isHitPlayerApi) {
            getAssetDetails();
        }
        //postCommentClick();
        BuyNowClick();
    }

    public void getAssetDetails() {
        isHitPlayerApi = true;
        railInjectionHelper =new ViewModelProvider(this).get(RailInjectionHelper.class);
        getBinding().pBar.setVisibility(View.VISIBLE);
        railInjectionHelper.getAssetDetailsV2(String.valueOf(assestId)).observe(LiveActivity.this, assetResponse -> {
            if (assetResponse != null) {
                if (assetResponse.getStatus().equalsIgnoreCase(APIStatus.START.name())) {

                } else if (assetResponse.getStatus().equalsIgnoreCase(APIStatus.SUCCESS.name())) {
                    parseAssetDetails(assetResponse);
                } else if (assetResponse.getStatus().equalsIgnoreCase(APIStatus.ERROR.name())) {
                    if (assetResponse.getErrorModel() != null && assetResponse.getErrorModel().getErrorCode() != 0) {
                        showDialog(LiveActivity.this.getResources().getString(R.string.error), getResources().getString(R.string.something_went_wrong));
                    }

                } else if (assetResponse.getStatus().equalsIgnoreCase(APIStatus.FAILURE.name())) {
                    showDialog(LiveActivity.this.getResources().getString(R.string.error), getResources().getString(R.string.something_went_wrong));
                }
            }

        });
    }

    boolean isAdShowingToUser = true;

    private void parseAssetDetails(ResponseModel assetResponse) {
        RailCommonData enveuCommonResponse = (RailCommonData) assetResponse.getBaseCategory();

        if (enveuCommonResponse != null && enveuCommonResponse.getEnveuVideoItemBeans().size() > 0) {
            videoDetails = enveuCommonResponse.getEnveuVideoItemBeans().get(0);

            getBinding().descriptionText.setEllipsize(TextUtils.TruncateAt.END);

            ImageHelper.getInstance(LiveActivity.this).loadListImage(getBinding().playerImage, videoDetails.getPosterURL());
            ImageHelper.getInstance(LiveActivity.this).loadListSQRImage(getBinding().channelLogo, videoDetails.getPosterURL());
            if (videoDetails.isPremium()) {
                getBinding().pBar.setVisibility(View.GONE);
                ImageHelper.getInstance(LiveActivity.this).loadListImage(getBinding().playerImage, videoDetails.getPosterURL());
                getBinding().tvPurchased.setVisibility(View.GONE);
                getBinding().tvPremium.setVisibility(View.GONE);

                getBinding().mPremiumStatus.setVisibility(View.VISIBLE);
                getBinding().backButton.setVisibility(View.VISIBLE);
                //hitApiEntitlement(enveuCommonResponse.getEnveuVideoItemBeans().get(0).getSku());
                if (isLogin) {
                    hitApiEntitlement(enveuCommonResponse.getEnveuVideoItemBeans().get(0).getSku());
                } else {
                    getBinding().tvBuyNow.setVisibility(View.VISIBLE);
                }
            } else {
                getBinding().pBar.setVisibility(View.VISIBLE);
                if (AppCommonMethod.getCheckBCID(videoDetails.getBrightcoveVideoId())) {
                    isLogin = preference.getAppPrefLoginStatus();


                    if (isLogin) {
                        if (!preference.getEntitlementStatus()) {
                            GetPlansLayer.getInstance().getEntitlementStatus(preference, token, new EntitlementStatus() {
                                @Override
                                public void entitlementStatus(boolean entitlementStatus, boolean apiStatus) {
                                    getBinding().pBar.setVisibility(View.GONE);
                                    if (entitlementStatus && apiStatus) {
                                        isAdShowingToUser = false;
                                    }
                                    brightCoveVideoId = Long.parseLong(videoDetails.getBrightcoveVideoId());
                                    playPlayerWhenShimmer();
                                }
                            });
                        } else {
                            getBinding().pBar.setVisibility(View.GONE);
                            brightCoveVideoId = Long.parseLong(videoDetails.getBrightcoveVideoId());
                            playPlayerWhenShimmer();
                        }

                    } else {
                        getBinding().pBar.setVisibility(View.GONE);
                        brightCoveVideoId = Long.parseLong(videoDetails.getBrightcoveVideoId());
                        playPlayerWhenShimmer();
                    }
                } else {
                    getBinding().pBar.setVisibility(View.GONE);
                }
            }
            setUserInteractionFragment(assestId);
            stopShimmer();
            setUI(videoDetails);
        }
    }

    ResponseEntitle responseEntitlementModel;

    public void hitApiEntitlement(String sku) {

        viewModel.hitApiEntitlement(token, sku).observe(LiveActivity.this, responseEntitlement -> {
            responseEntitlementModel = responseEntitlement;
            if (responseEntitlement.getStatus()) {
                if (responseEntitlement.getData().getEntitled()) {
                    getBinding().tvBuyNow.setVisibility(View.GONE);
                    if (responseEntitlement.getData() != null) {
                        updateBuyNowText(responseEntitlement, 1);
                    }
                } else {
                    getBinding().tvBuyNow.setVisibility(View.VISIBLE);
                    if (responseEntitlement.getData() != null) {
                        updateBuyNowText(responseEntitlement, 2);
                    }
                }
            } else {
                if (responseEntitlementModel != null && responseEntitlementModel.getResponseCode() != null && responseEntitlementModel.getResponseCode() > 0 && responseEntitlementModel.getResponseCode() == 4302) {
                    isloggedout = true;
                    logoutUser();
                    // showDialog(LiveActivity.this.getResources().getString(R.string.logged_out), responseEntitlementModel.getDebugMessage() == null ? "" : responseEntitlementModel.getDebugMessage().toString());
                }
            }
        });
    }

    private void updateBuyNowText(ResponseEntitle responseEntitlement, int type) {
        try {
            if (type == 1) {
                if (responseEntitlement.getData().getEntitledAs() != null) {
                    List<EntitledAs> alpurchaseas = responseEntitlement.getData().getEntitledAs();
                    String vodOfferType = alpurchaseas.get(0).getVoDOfferType();
                    String subscriptionOfferPeriod = null;
                    if (alpurchaseas.get(0).getOfferType() != null) {
                        subscriptionOfferPeriod = (String) alpurchaseas.get(0).getOfferType();
                    }

                    if (vodOfferType != null) {
                        if (vodOfferType.contains(VodOfferType.PERPETUAL.name())) {
                            getBinding().tvPurchased.setVisibility(View.VISIBLE);
                            getBinding().tvPurchased.setText("" + getResources().getString(R.string.purchased));
                        } else if (vodOfferType.contains(VodOfferType.RENTAL.name())) {
                            getBinding().tvPurchased.setVisibility(View.VISIBLE);
                            getBinding().tvPurchased.setText("" + getResources().getString(R.string.rented));
                        } else {

                        }
                    } else {
                        if (subscriptionOfferPeriod != null) {
                            getBinding().tvPurchased.setVisibility(View.VISIBLE);
                            getBinding().tvPurchased.setText("" + getResources().getString(R.string.subscribed));
                        } else {

                        }
                    }
                    if (responseEntitlement.getData().getBrightcoveVideoId() != null) {
                        brightCoveVideoId = Long.parseLong(responseEntitlement.getData().getBrightcoveVideoId());
                    }
                    isAdShowingToUser = false;
                    preference.setEntitlementState(true);
                    playPlayerWhenShimmer();

                }
            } else {
//                getBinding().tvBuyNow.setVisibility(View.VISIBLE);
//                getBinding().tvPurchased.setVisibility(View.GONE);
            }

        } catch (Exception e) {

        }
    }


    public void setUserInteractionFragment(int id) {
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        Bundle args = new Bundle();
        args.putInt(AppConstants.BUNDLE_ASSET_ID, id);
        args.putParcelable(AppConstants.BUNDLE_SERIES_DETAIL, videoDetails);
        userInteractionFragment = new UserInteractionFragment();
        userInteractionFragment.setArguments(args);
        transaction.replace(R.id.fragment_user_interaction, userInteractionFragment);
        transaction.addToBackStack(null);
        transaction.commit();
    }


    private void BuyNowClick() {
        getBinding().tvBuyNow.setOnClickListener(view -> comingSoon());
    }

    public void setUI(EnveuVideoItemBean responseDetailPlayer) {
        recommendationRailFragment();

       /* if (responseDetailPlayer.getAssetCast().size() > 0) {
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
        }*/
       /* if (responseDetailPlayer.getAssetGenres().size() > 0) {
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
        }*/
        setDetails(responseDetailPlayer);


    }

    private void setCustomeFields(EnveuVideoItemBean responseDetailPlayer, String duration) {
        try {
            getBinding().tag.setText("");
            if (responseDetailPlayer.getParentalRating() != null && !responseDetailPlayer.getParentalRating().equalsIgnoreCase("")) {
                getBinding().tag.setText(responseDetailPlayer.getParentalRating() + "");
            }

           /* getBinding().tag.setText(getBinding().tag.getText().toString() + " " + duration + " \u2022");

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
            }*/
        } catch (Exception ignored) {

        }

    }

    public void recommendationRailFragment() {
        transaction = getSupportFragmentManager().beginTransaction();
        RecommendationRailFragment railFragment = new RecommendationRailFragment();
        Bundle args = new Bundle();
        args.putString(AppConstants.BUNDLE_TAB_ID, tabId);
        railFragment.setArguments(args);
        transaction.replace(R.id.recommendation_rail, railFragment);
        transaction.addToBackStack(null);
        transaction.commit();
    }


    public void logoutUser() {
        isloggedout = false;
        if (isLogin) {
            if (CheckInternetConnection.isOnline(Objects.requireNonNull(LiveActivity.this))) {
                clearCredientials(preference);
                hitApiLogout(LiveActivity.this, preference.getAppPrefAccessToken());
            }
        }
    }

    private void showDialog(String title, String message) {
        FragmentManager fm = getSupportFragmentManager();
        AlertDialogSingleButtonFragment alertDialog = AlertDialogSingleButtonFragment.newInstance(title, message, getResources().getString(R.string.ok));
        alertDialog.setCancelable(false);
        alertDialog.setAlertDialogCallBack(this);
        alertDialog.show(fm, "fragment_alert");
    }

    public void setDetails(EnveuVideoItemBean responseDetailPlayer) {
        if (responseDetailPlayer.getAssetType() != null) {
           /* String tempTag1 = responseDetailPlayer.getAssetType();
            String bullet = "\u2022";
            String tempTag2 = AppCommonMethod.getDuration(responseDetailPlayer.getDuration());
            Spannable WordtoSpan = new SpannableString(bullet);
            WordtoSpan.setSpan(new ForegroundColorSpan(Color.BLUE), 0, WordtoSpan.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
*/            // StringBuilder stringBuilder = new StringBuilder(tempTag1 + "  " + WordtoSpan + " " + tempTag2);

            setCustomeFields(responseDetailPlayer, "");
        } else {
            setCustomeFields(responseDetailPlayer, "");
            //  new ToastHandler(LiveActivity.this).show(LiveActivity.this.getResources().getString(R.string.can_not_play_error));
        }
        getBinding().setResponseApi(responseDetailPlayer);
       /* if (isLogin.equalsIgnoreCase(AppConstants.UserStatus.Login.toString())) {
            addToWatchHistory();
        }*/
        getBinding().descriptionText.post(() -> {
            int lineCount = getBinding().descriptionText.getLineCount();
            getBinding().descriptionText.setMaxLines(2);
            if (lineCount >= 2) {
                getBinding().lessButton.setVisibility(View.VISIBLE);
            } else {

                getBinding().lessButton.setVisibility(View.INVISIBLE);
            }
            // Use lineCount here
        });
    }

    private void addToWatchHistory() {
        bookmarkingViewModel.addToWatchHistory(token, assestId);
    }


    private void noConnectionLayout() {
        getBinding().llParent.setVisibility(View.GONE);
        getBinding().noConnectionLayout.setVisibility(View.VISIBLE);
        getBinding().connection.btnMyDownloads.setOnClickListener(view -> {
            if (preference.getAppPrefLoginStatus())
                new ActivityLauncher(this).launchMyDownloads();
            else
                new ActivityLauncher(this).loginActivity(this, LoginActivity.class);
        });
    }

    private void setExpandable() {
        // getBinding().expandableLayout.collapse();
        getBinding().setExpandabletext(getResources().getString(R.string.more));
        getBinding().descriptionText.setEllipsis("...");
        //  getBinding().expandableLayout.setOnExpansionUpdateListener(expansionFraction -> getBinding().lessButton.setRotation(0 * expansionFraction));
        getBinding().lessButton.setOnClickListener(this::clickExpandable);
    }

    public void clickExpandable(View view) {
        getBinding().descriptionText.toggle();
        getBinding().descriptionText.setEllipsis("...");
        if (getBinding().descriptionText.isExpanded()) {
            getBinding().descriptionText.setEllipsize(null);
        } else {
            getBinding().descriptionText.setMaxLines(2);
            getBinding().descriptionText.setEllipsize(TextUtils.TruncateAt.END);
        }

        if (getBinding().descriptionText.isExpanded()) {
            getBinding().setExpandabletext(getResources().getString(R.string.more));

        } else {
            getBinding().setExpandabletext(getResources().getString(R.string.less));
        }


    }

    private NetworkChangeReceiver receiver = null;

    public void resetExpandable() {
        getBinding().setExpandabletext(getResources().getString(R.string.more));
        if (getBinding().descriptionText.isExpanded()) {
            getBinding().descriptionText.toggle();
            getBinding().descriptionText.setEllipsis("...");
        }
    }

    @Override
    public void onBackPressed() {
        //  super.onBackPressed();

        if (commentsFragment != null) {
            removeCommentFragment();
        } else {
            AppCommonMethod.isPurchase = true;
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
                finish();
            } else {
//                if (playerFragment != null) {
//                    playerFragment.BackPressClicked(2);
//                }

            }
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        Logger.d("DetailActivityCalled", "True");
        releaseAudioFocusForMyApp(LiveActivity.this);
        if (handler != null && runnable != null)
            handler.removeCallbacksAndMessages(runnable);

        if (timer != null)
            timer.cancel();
        dismissLoading(getBinding().progressBar);
    }

    @Override
    protected void onStop() {
        super.onStop();
        try {
            if (receiver != null) {
                this.unregisterReceiver(receiver);
                NetworkChangeReceiver.connectivityReceiverListener = null;
            }
        } catch (Exception e) {

        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        preference.setAppPrefAssetId(0);
        preference.setAppPrefJumpTo("");
        preference.setAppPrefBranchIo(false);
        AppCommonMethod.seasonId = -1;
        if (timer != null)
            timer.cancel();

    }


    @Override
    public void onFinishDialog() {
        if (isloggedout)
            logoutUser();

        if (isPlayerError) {
            getBinding().playerImage.setVisibility(View.VISIBLE);
            ImageHelper.getInstance(LiveActivity.this).loadListImage(getBinding().playerImage, videoDetails.getPosterURL());
            isPlayerError = false;

        } else {
            finish();
        }
    }

    public void setConnectivityListener(NetworkChangeReceiver.ConnectivityReceiverListener
                                                listener) {
        NetworkChangeReceiver.connectivityReceiverListener = listener;
    }

    @Override
    public void onNetworkConnectionChanged(boolean isConnected) {
        AppCommonMethod.isInternet = isConnected;
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
                        //  fragment.pauseNontonPlayer();
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
        super.onConfigurationChanged(newConfig);
        boolean isTablet = LiveActivity.this.getResources().getBoolean(R.bool.isTablet);
        AppCommonMethod.isOrientationChanged = true;

        if (newConfig.orientation == 2) {
            hideVideoDetail();
        } else {
            showVideoDetail();
        }

    }

    public void showVideoDetail() {
        getBinding().rootScroll.setVisibility(View.VISIBLE);
    }

    public void hideVideoDetail() {
        getBinding().rootScroll.setVisibility(View.GONE);
    }


    @Override
    public void railItemClick(RailCommonData item, int position) {
        if (item.getScreenWidget().getType() != null && item.getScreenWidget().getLayout().equalsIgnoreCase(Layouts.HRO.name())) {
            Toast.makeText(LiveActivity.this, item.getScreenWidget().getLandingPageType(), Toast.LENGTH_LONG).show();
        } else {
            if (AppCommonMethod.getCheckBCID(item.getEnveuVideoItemBeans().get(position).getBrightcoveVideoId())) {
                String getVideoId = item.getEnveuVideoItemBeans().get(position).getkEntryId();
                AppCommonMethod.launchDetailScreen(this, getVideoId, AppConstants.Video, item.getEnveuVideoItemBeans().get(position).getId(), "0", false,item.getEnveuVideoItemBeans().get(position));
            }
        }
    }

    @Override
    public void moreRailClick(RailCommonData data, int position) {
        PrintLogging.printLog("", data.getScreenWidget().getContentID() + "  " + data.getScreenWidget().getLandingPageTitle() + " " + 0 + " " + 0);
        if (data.getScreenWidget() != null && data.getScreenWidget().getContentID() != null) {
            String playListId = data.getScreenWidget().getContentID();
            if (data.getScreenWidget().getName() != null) {
                new ActivityLauncher(LiveActivity.this).listActivity(LiveActivity.this, ListActivity.class, playListId, data.getScreenWidget().getName().toString(), 0, 0, data.getScreenWidget());
            } else {
                new ActivityLauncher(LiveActivity.this).listActivity(LiveActivity.this, ListActivity.class, playListId, "", 0, 0, data.getScreenWidget());
            }
        }
    }

    @Override
    protected void onUserLeaveHint() {
        super.onUserLeaveHint();
        if (supportsPiPMode()) {
//            try {
//                PictureInPictureManager.getInstance().onUserLeaveHint();
//                if (playerFragment != null) {
//                    playerFragment.hideControls();
//                }
//            } catch (Exception ignored) {
//
//            }
        }
    }

//    @Override
//    public void isInPip(boolean status) {
//        isBackStacklost = status;
//    }

    @Override
    public void onPictureInPictureModeChanged(boolean isInPictureInPictureMode, Configuration
            newConfig) {
        super.onPictureInPictureModeChanged(isInPictureInPictureMode, newConfig);
        if (supportsPiPMode()) {
//            PictureInPictureManager.getInstance().onPictureInPictureModeChanged(isInPictureInPictureMode, newConfig);
            // playerFragment.onPictureInPictureModeChanged(isInPictureInPictureMode, newConfig);
        }
    }

    public boolean supportsPiPMode() {
        boolean isPipSupported = false;
//        return Build.VERSION.SDK_INT >= Build.VERSION_CODES.O;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            isPipSupported = true;
        } else {
            isPipSupported = false;
        }
        return isPipSupported;
    }

//    @Override
//    public void onFragmentInteraction(Uri uri) {
//
//    }

    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);
//        if (playerFragment != null) {
//            if (!hasFocus) {
//                if (playerFragment.isPlaying()) {
//                    playerFragment.playPause();
//                }
//            } else {
//                if (!playerFragment.isPlaying()) {
//                    playerFragment.playPause();
//                }
//            }
//        }
    }

    boolean isPlayerError = false;

//    @Override
//    public void onPlayerError(String error) {
//        try {
//            getBinding().backButton.setVisibility(View.VISIBLE);
//            getBinding().pBar.setVisibility(View.GONE);
//            getBinding().playerImage.setVisibility(View.VISIBLE);
//
//            //Toast.makeText(this, error, Toast.LENGTH_LONG).show();
//            String errorMessage = getString(R.string.player_error);
//            if (!NetworkConnectivity.isOnline(this)) {
//                errorMessage = getString(R.string.no_internet_connection);
//                new ToastHandler(this).show(errorMessage);
//                if (!isOfflineAvailable) {
//                    //playerFragment.getBaseVideoView().pause();
//                }
//            } else {
//                if (!errorDialogShown) {
//                    errorDialogShown = true;
//                    FragmentManager fm = getSupportFragmentManager();
//                    errorDialog = AlertDialogSingleButtonFragment.newInstance("", errorMessage, getResources().getString(R.string.ok));
//                    errorDialog.setCancelable(false);
//                    errorDialog.setAlertDialogCallBack(new AlertDialogFragment.AlertDialogListener() {
//                        @Override
//                        public void onFinishDialog() {
//                            getBinding().backButton.setVisibility(View.VISIBLE);
//                            getBinding().playerImage.setVisibility(View.VISIBLE);
//                            ImageHelper.getInstance(LiveActivity.this).loadListImage(getBinding().playerImage, videoDetails.getPosterURL());
//                            isPlayerError = false;
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
//    @Override
//    public void onBookmarkCall(int currentPosition) {
//        if (isLogin.equalsIgnoreCase(AppConstants.UserStatus.Login.toString())) {
//            bookmarkingViewModel.bookmarkVideo(token, assestId, (currentPosition / 1000));
//        }
//    }

//
//    @Override
//    public void onBookmarkFinish() {
//        if (isLogin.equalsIgnoreCase(AppConstants.UserStatus.Login.toString())) {
//            bookmarkingViewModel.finishBookmark(token, assestId);
//        }
//    }
//
//    @Override
//    public void onPlayerInProgress() {
//
//    }
//
//    @Override
//    public void onPlayerStart() {
//        try {
//            getBinding().backButton.setVisibility(View.GONE);
//            getBinding().playerImage.setVisibility(View.GONE);
//            getBinding().pBar.setVisibility(View.GONE);
//            String name = "";
//            String mediaType = "";
//            if (videoDetails.getTitle() != null) {
//                name = videoDetails.getTitle();
//            }
//            if (videoDetails.getAssetType() != null) {
//                mediaType = videoDetails.getAssetType();
//            }
////            AppCommonMethod.trackFcmEvent(name, mediaType, LiveActivity.this, 0);
//        } catch (Exception e) {
//
//        }
//    }
//
//    @Override
//    public void onAdStarted() {
//        try {
//            getBinding().playerImage.setVisibility(View.GONE);
//        } catch (Exception ignored) {
//
//        }
//    }


    @Override
    public void onDownloadClicked(String videoId, Object position, Object source) {

    }

    private void showWifiSettings(int videoQuality) {

    }

    private void selectDownloadVideoQuality() {

    }


    @Override
    public void onProgressbarClicked(View view, Object source, String videoId) {

    }

    @Override
    public void onDownloadCompleteClicked(View view, Object source, String videoId) {

    }

    @Override
    public void onPauseClicked(String videoId, Object source) {

    }

    @Override
    public void onDownloadDeleted(@NotNull String videoId, @NotNull Object source) {

    }

//    @Override
//    public void onDownloadRequested(@androidx.annotation.NonNull Video video) {
//
//    }
//
//
//    @Override
//    public void onDownloadStarted(@androidx.annotation.NonNull Video video, long l,
//                                  @androidx.annotation.NonNull Map<String, Serializable> map) {
//
//    }

//    @Override
//    public void onDownloadProgress(@androidx.annotation.NonNull Video
//                                           video, @androidx.annotation.NonNull com.brightcove.player.network.DownloadStatus
//                                           downloadStatus) {
//
//    }
//
//    @Override
//    public void onDownloadPaused(@androidx.annotation.NonNull Video
//                                         video, @androidx.annotation.NonNull com.brightcove.player.network.DownloadStatus
//                                         downloadStatus) {
//    }

//    @Override
//    public void onDownloadCompleted(@androidx.annotation.NonNull Video
//                                            video, @androidx.annotation.NonNull com.brightcove.player.network.DownloadStatus
//                                            downloadStatus) {
//    }
//
//    @Override
//    public void onDownloadCanceled(@androidx.annotation.NonNull Video video) {
//
//    }
//
//    @Override
//    public void onDownloadDeleted(@androidx.annotation.NonNull Video video) {
//
//    }
//
//    @Override
//    public void onDownloadFailed(@androidx.annotation.NonNull Video
//                                         video, @androidx.annotation.NonNull com.brightcove.player.network.DownloadStatus
//                                         downloadStatus) {
//        Logger.e(TAG, "onDownloadFailed");
//    }
//
//    @Override
//    public void downloadVideo(@androidx.annotation.NonNull Video video) {
//
//    }
//
//    @Override
//    public void pauseVideoDownload(Video video) {
//
//    }
//
//    @Override
//    public void resumeVideoDownload(Video video) {
//
//    }
//
//    @Override
//    public void deleteVideo(@androidx.annotation.NonNull Video video) {
//        Logger.e(TAG, "onDownloadDeleted--->>" + 2);
//    }
//
//    @Override
//    public void alreadyDownloaded(@androidx.annotation.NonNull Video video) {
//
//    }
//
//    @Override
//    public void downloadedVideos(@org.jetbrains.annotations.Nullable List<? extends
//            Video> p0) {
//
//    }
//
//    @Override
//    public void videoFound(Video video) {
//
//    }
//
//    @Override
//    public void downloadStatus(String videoId, DownloadStatus downloadStatus) {
//        Logger.e(TAG, "DownloadStatus" + downloadStatus.getCode());
//    }
//
//    @Override
//    public void onDownloadDeleted(@NotNull String videoId, @NotNull Object source) {
//        Logger.e(TAG, "onDownloadDeleted--->>" + 3);
//    }
//
//    @Override
//    public void chromeCastViewConnected(boolean status) {
//        Log.w("Chromecast-->>", "chromeCastViewConnected");
//        if (!LiveActivity.this.isFinishing()) {
//            Log.w("Chromecast-->>", "chromeCastViewConnected");
//            RailCommonData railCommonData = new RailCommonData();
//            Intent intent = new Intent(LiveActivity.this, DefaultExpandedControllerActivity.class);
//            if (videoDetails != null) {
//                intent.putExtra("image_url", videoDetails.getPosterURL());
//            }
//            /*intent.putExtra(AppConstants.RAIL_DATA_OBJECT, image_url);
//            intent.putExtra(AppConstants.ASSET, railCommonData);*/
//            startActivity(intent);
//            finish();
//        }
//    }
}
