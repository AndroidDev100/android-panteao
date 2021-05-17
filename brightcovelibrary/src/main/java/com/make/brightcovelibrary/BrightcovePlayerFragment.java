package com.make.brightcovelibrary;

import android.annotation.TargetApi;
import android.app.Activity;
import android.app.ActivityManager;
import android.app.AlertDialog;
import android.content.ComponentName;
import android.content.Context;
import android.content.IntentFilter;
import android.content.pm.ActivityInfo;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.graphics.Color;
import android.media.AudioAttributes;
import android.media.AudioFocusRequest;
import android.media.AudioManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.telephony.PhoneStateListener;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.util.Pair;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.MediaController;
import android.widget.ProgressBar;
import android.annotation.SuppressLint;
import androidx.annotation.NonNull;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.brightcove.cast.GoogleCastComponent;
import com.brightcove.cast.GoogleCastEventType;
import com.brightcove.cast.model.BrightcoveCastCustomData;
import com.brightcove.cast.model.CustomData;
import com.brightcove.ima.GoogleIMAComponent;
import com.brightcove.ima.GoogleIMAEventType;
import com.brightcove.player.captioning.BrightcoveCaptionFormat;
import com.brightcove.player.captioning.BrightcoveCaptionStyle;
import com.brightcove.player.captioning.LoadCaptionsService;
import com.brightcove.player.captioning.preferences.CaptionConstants;
import com.brightcove.player.display.ExoPlayerVideoDisplayComponent;
import com.brightcove.player.display.VideoDisplayComponent;
import com.brightcove.player.edge.Catalog;
import com.brightcove.player.edge.VideoListener;
import com.brightcove.player.event.Event;
import com.brightcove.player.event.EventEmitter;
import com.brightcove.player.event.EventListener;
import com.brightcove.player.event.EventType;
import com.brightcove.player.management.BrightcoveClosedCaptioningManager;
import com.brightcove.player.model.CuePoint;
import com.brightcove.player.model.DeliveryType;
import com.brightcove.player.model.Source;
import com.brightcove.player.model.SourceCollection;
import com.brightcove.player.model.Video;
import com.brightcove.player.model.VideoFields;
import com.brightcove.player.pictureinpicture.PictureInPictureManager;
import com.brightcove.player.util.StringUtil;
import com.brightcove.player.view.BaseVideoView;
import com.make.brightcovelibrary.callBacks.BackPressCallBack;
import com.make.brightcovelibrary.callBacks.PhoneListenerCallBack;
import com.make.brightcovelibrary.callBacks.PlayerCallbacks;
import com.make.brightcovelibrary.chromecast.ChromeCastCallback;
import com.make.brightcovelibrary.chromecast.ChromecastManager;
import com.google.ads.interactivemedia.v3.api.AdDisplayContainer;
import com.google.ads.interactivemedia.v3.api.AdError;
import com.google.ads.interactivemedia.v3.api.AdsRequest;
import com.google.ads.interactivemedia.v3.api.ImaSdkFactory;
import com.google.android.exoplayer2.ExoPlayer;
import com.google.android.exoplayer2.Format;
import com.google.android.exoplayer2.SimpleExoPlayer;
import com.google.android.exoplayer2.source.TrackGroupArray;
import com.google.android.exoplayer2.trackselection.DefaultTrackSelector;
import com.google.android.exoplayer2.trackselection.MappingTrackSelector;
import com.google.android.exoplayer2.upstream.DefaultBandwidthMeter;
import com.google.android.exoplayer2.source.TrackGroup;
import com.google.android.gms.cast.AdBreakClipInfo;
import com.google.android.gms.cast.AdBreakInfo;
import com.google.android.gms.cast.MediaInfo;
import com.google.android.gms.cast.MediaMetadata;
import com.google.android.gms.cast.VastAdsRequest;
import com.google.android.gms.common.images.WebImage;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;

import static android.content.Context.TELEPHONY_SERVICE;
import static android.media.AudioManager.AUDIOFOCUS_LOSS;

public class BrightcovePlayerFragment extends Fragment {
//        extends com.brightcove.player.appcompat.BrightcovePlayerFragment implements PlayerControlsFragment.OnFragmentInteractionListener, PlayerCallbacks, NetworkChangeReceiver.ConnectivityReceiverListener, PhoneListenerCallBack, BackPressCallBack, ChromeCastCallback {

//    private static final String TAG = "BrightcovePlayer";
//    private OnPlayerInteractionListener mListener;
//    private ChromeCastStartedCallBack chromeCastStartedListener;
//    private EventEmitter eventEmitter;
//    private Video currentVideo;
//    private NetworkChangeReceiver receiver = null;
//    private Activity mActivity;
//    private boolean enable = true;
//    private boolean imaEnable = true;
//    private boolean bingeWatch = false;
//    private int bingeWatchTimer = 0;
//    private GoogleIMAComponent googleIMAComponent;
//    String videoId = "";
//    String assetType = "";
//    String selected_track = "";
//    String selected_lang = "";
//    private String adRulesURL = "";
//    private String poster_image = "";
//    private int bottomMargin = 0;
//    private int currentCaptionLanguage = 0;
//    private int currentAudioLanguage = 0;
//    private CharSequence[] availableCapitonsList;
//    private CharSequence[] availableCapitonsListDisplay;
//    private List<String> tracks;
//    private CharSequence[] audioTracks;
//    private Handler handler;
//    private Runnable runnable;
//    private long bookmarkPosition = 0l;
//    private ProgressBar progressBar;
//    private String brightcoveAccountId;
//    private String brightcovePolicyKey;
//    private PlayerControlsFragment playerControlsFragment;
//    private String videoType = "0";
//    List<CuePoint> cuePointsList;
//
//    private boolean isOfflineVideo = false;
//    private boolean isPictureinPictureEnabled = false;
//    private boolean isInPictureinPicture = false;
//    private FrameLayout container;
//    private boolean isAdShowingToUser=true;
//    private boolean isBingeWatchTimeCalculate=false;
//    private static final String PROPERTY_APPLICATION_ID = "panteao.make.ready";
//    private boolean isCastConnected=false;
//    int from=0;


    public BrightcovePlayerFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        ChromecastManager.getInstance().setCallBacks(this);
//        Bundle bundle = getArguments();
//        if (bundle != null) {
//            videoId = bundle.getString("videoId");
////            Log.w("IMATAG", videoId);
//            assetType = bundle.getString("assetType");
//            selected_track = bundle.getString("selected_track");
////            Log.e("Selectedtrack",selected_track);
//            //Log.d("asasasasas",selected_track);
//            selected_lang = bundle.getString("selected_lang");
//            adRulesURL = bundle.getString("config_vast_tag");
//            bingeWatch = bundle.getBoolean("binge_watch");
//            bingeWatchTimer = bundle.getInt("binge_watch_timer");
//
//
//            isAdShowingToUser = bundle.getBoolean("ads_visibility");
//            // Log.w("config_vast_tag", adRulesURL);
//
////            Log.w("IMATAG ",bundle.getString("vast_tag"));
//            if (bundle.getString("vast_tag") != null && !bundle.getString("vast_tag").equalsIgnoreCase("")) {
//                adRulesURL = bundle.getString("vast_tag");
//            }
//            if (bundle.getString("poster_image") != null && !bundle.getString("poster_image").equalsIgnoreCase("")) {
//                poster_image = bundle.getString("poster_image");
//            }
//
//
//            bookmarkPosition = bundle.getLong("bookmarkPosition");
//            if (bundle.containsKey("isOffline")) {
//                isOfflineVideo = bundle.getBoolean("isOffline");
//                currentVideo = bundle.getParcelable("videoId");
//                from=bundle.getInt("from");
//                if (isOfflineVideo && mActivity != null) {
//                    if (from==1){
//                        mActivity.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_SENSOR_LANDSCAPE);
//                    }else {
//                        if (mActivity != null) {
//                            mActivity.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_NOSENSOR);
//                        }
//                    }
//                }
//            } else {
//                if (mActivity != null) {
//                    mActivity.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_NOSENSOR);
//                }
//            }
//
//        }
    }

//    private void requestAudioFocus() {
//
//        AudioManager mAudioManager = (AudioManager) getActivity().getSystemService(Context.AUDIO_SERVICE);
//        AudioAttributes mAudioAttributes =
//                null;
//        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP) {
//            mAudioAttributes = new AudioAttributes.Builder()
//                    .setUsage(AudioAttributes.USAGE_MEDIA)
//                    .setContentType(AudioAttributes.CONTENT_TYPE_MUSIC)
//                    .build();
//        }
//        AudioFocusRequest mAudioFocusRequest =
//                null;
//        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
//            mAudioFocusRequest = new AudioFocusRequest.Builder(AudioManager.AUDIOFOCUS_GAIN)
//                    .setAudioAttributes(mAudioAttributes)
//                    .setAcceptsDelayedFocusGain(true)
//                    .setOnAudioFocusChangeListener(new AudioManager.OnAudioFocusChangeListener() {
//                        @Override
//                        public void onAudioFocusChange(int i) {
//                            if (i == AUDIOFOCUS_LOSS) {
//                                Log.w("windowFocusChanged===",AUDIOFOCUS_LOSS+"");
//                                if (!exitFromPIP){
//                                    baseVideoView.pause();
//                                }
//                            }
//                        }
//                    }) // Need to implement listener
//                    .build();
//        }
//        int focusRequest = 0;
//        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
//            focusRequest = mAudioManager.requestAudioFocus(mAudioFocusRequest);
//        }
//        switch (focusRequest) {
//            case AudioManager.AUDIOFOCUS_REQUEST_FAILED:
//                // don’t start playback
//            case AudioManager.AUDIOFOCUS_REQUEST_GRANTED:
//                // actually start playback
//        }
//
//    }
//
//    @Override
//    public View onCreateView(LayoutInflater inflater, ViewGroup container,
//                             Bundle savedInstanceState) {
//       /* if (mActivity != null) {
//            mActivity.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_NOSENSOR);
//        }*/
//        View result = inflater.inflate(R.layout.fragment_blank, container, false);
//        Log.w("IMATAG", "view");
//        findPlayerId(result);
//        super.onCreateView(inflater, container, savedInstanceState);
//        bottomMargin = (int) getResources().getDimension(R.dimen.caption_margin);
//        return result;
//    }
//
//    @Override
//    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
//        super.onViewCreated(view, savedInstanceState);
//        if (isOfflineVideo && from==1) {
//            FrameLayout.LayoutParams captionParams = (FrameLayout.LayoutParams) container.getLayoutParams();
//            captionParams.bottomMargin = (int) getResources().getDimension(R.dimen.offline_player_padding);
//            captionParams.topMargin = (int) getResources().getDimension(R.dimen.offline_player_padding);
//            container.setLayoutParams(captionParams);
//        }
//
//    }
//
//    @SuppressLint("SourceLockedOrientationActivity")
//    @Override
//    public void onActivityCreated(Bundle savedInstanceState) {
//        super.onActivityCreated(savedInstanceState);
//        mActivity = getActivity();
//        mListener = (OnPlayerInteractionListener) mActivity;
//        chromeCastStartedListener = (ChromeCastStartedCallBack) mActivity;
//
//        try {
//            ApplicationInfo ai = mActivity.getPackageManager().getApplicationInfo(mActivity.getPackageName(), PackageManager.GET_META_DATA);
//            Bundle bundle = ai.metaData;
//            if (bundle != null) {
//                brightcoveAccountId = bundle.getString("brightcove_account_id");
//                brightcovePolicyKey = bundle.getString("brightcove_policy_key");
//
//            }
//        } catch (
//                PackageManager.NameNotFoundException | NullPointerException e) {
//
//        }
//        if (isOfflineVideo) {
//            setPlayerWithCallBacks(true);
//            if (mActivity != null) {
//                if (from==1){
//                    mActivity.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
//                    mActivity.getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
//                }
//            }
//        } else {
//            setPlayerWithCallBacks(false);
//        }
//
//        try {
//            int orientation = getResources().getConfiguration().orientation;
//            if (orientation == Configuration.ORIENTATION_LANDSCAPE) {
//                mActivity.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_SENSOR_LANDSCAPE);
//                mActivity.getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
//                FrameLayout.LayoutParams captionParams = (FrameLayout.LayoutParams) container.getLayoutParams();
//                captionParams.bottomMargin = (int) getResources().getDimension(R.dimen.live_full_screen_bottom);
//                captionParams.topMargin = (int) getResources().getDimension(R.dimen.live_full_screen_bottom);
//                container.setLayoutParams(captionParams);
//
//            }
//        }catch (Exception ignored){
//
//        }
//    }
//
//    @Override
//    public BaseVideoView getBaseVideoView() {
//        if (baseVideoView != null) {
//            return baseVideoView;
//        }
//        return null;
//    }
//
//    DefaultTrackSelector trackSelector;
//    DefaultBandwidthMeter bandwidthMeter;
//    private TrackGroupArray trackGroups;
//    private MappingTrackSelector.MappedTrackInfo mappedTrackInfo;
//    boolean isContentCompleted=false;
//    boolean willBingWatchShow=false;
//    private void setPlayerWithCallBacks(boolean isOffline) {
//        bandwidthMeter = new DefaultBandwidthMeter();
//
//        PictureInPictureManager.getInstance().registerActivity(mActivity, baseVideoView);
//        baseVideoView.setMediaController((MediaController) null);
//        baseVideoView.setupClosedCaptioningRendering();
//       /* BrightcoveCaptionStyle brightcoveCaptionStyle=BrightcoveClosedCaptioningManager.getInstance(getActivity()).getStyle();
//        int greenColorValue = Color.parseColor("#9F272727");
//        brightcoveCaptionStyle.createCaptionStyle("6",
//                "sans-serif",greenColorValue,greenColorValue,greenColorValue,greenColorValue,greenColorValue,greenColorValue,greenColorValue,greenColorValue);
//        BrightcoveClosedCaptioningManager.getInstance(getActivity()).setStyle(brightcoveCaptionStyle);*/
//
//        eventEmitter = baseVideoView.getEventEmitter();
//        eventEmitter.enable();
//        if (adRulesURL==null || adRulesURL.equalsIgnoreCase("")){
//            imaEnable=false;
//        }
//        if (imaEnable && !isOffline) {
//            if (isAdShowingToUser){
//                setupGoogleIMA();
//                Map<String, String> options = new HashMap<>();
//                List<String> values = new ArrayList<String>(Arrays.asList(VideoFields.DEFAULT_FIELDS));
//                values.remove(VideoFields.HLS_URL);
//                options.put("video_fields", StringUtil.join(values, ","));
//            }
//        }
//
//
//        if (brightcoveAccountId.isEmpty() || brightcovePolicyKey.isEmpty()) {
//            showErrorDialog(getString(R.string.implement_brightcove_creds));
//            return;
//        }
//
//        if (assetType != null && assetType.equalsIgnoreCase("LIVE")) {
//            //videoId="6125432335001";
//            videoType = "1";
//        } else {
//            videoType = "0";
//        }
//
//        if (!isOffline) {
//            Log.w("IMATAG", "catalog");
//            progressBar.setVisibility(View.VISIBLE);
//            Catalog catalog = new Catalog(eventEmitter, brightcoveAccountId, brightcovePolicyKey);
//            catalog.findVideoByID(videoId, new VideoListener() {
//                @Override
//                public void onVideo(Video video) {
//                    Log.w("IMATAG", "catalogin");
//                    progressBar.setVisibility(View.VISIBLE);
//                    currentVideo = video;
//                    cuePointsList = new ArrayList<>();
//                    createCuePointList(video);
//                    baseVideoView.add(video);
//                    isContentCompleted=false;
//                    willBingWatchShow=false;
//                    baseVideoView.start();
//                    baseVideoView.seekTo((int) (bookmarkPosition * 1000));
//                    callPlayerControlsFragment();
//                    /*if (playerControlsFragment != null)
//                        playerControlsFragment.sendTapCallBack(false);*/
//                    if (playerControlsFragment != null) {
//                        playerControlsFragment.sendTapCallBack(false);
//                        playerControlsFragment.setIsOffline(false,from);
//                    }
//
//                    Log.w("videoFound", "inUp");
//                    /*if (ChromecastManager.getInstance().getRunningVideoId()!=null && !ChromecastManager.getInstance().getRunningVideoId().equalsIgnoreCase("")){
//                        if (ChromecastManager.getInstance().getRunningVideoId().equalsIgnoreCase(videoId)){
//                            onStatusUpdate();
//                        }else {
//                            castEnabling(video);
//                        }
//                    }else {
//                        castEnabling(video);
//                    }*/
//                    castEnabling(video);
//                    // cast(video);
//                }
//
//                @Override
//                public void onError(String error) {
//                    super.onError(error);
//                    if (!baseVideoView.isPlaying())
//                        showErrorDialog(error);
//                }
//            });
//        } else {
//            baseVideoView.stopPlayback();
//            baseVideoView.clear();
//            eventEmitter = baseVideoView.getEventEmitter();
//            eventEmitter.enable();
//            baseVideoView.setMediaController((MediaController) null);
//            baseVideoView.seekTo((int) (bookmarkPosition * 1000));
//
//
//            baseVideoView.add(currentVideo);
//            baseVideoView.start();
//
//            callPlayerControlsFragment();
//            if (playerControlsFragment != null) {
//                playerControlsFragment.sendTapCallBack(false);
//                playerControlsFragment.setIsOffline(true,from);
//            }
//        }
//
//        eventEmitter.on(EventType.DID_SET_VIDEO, new EventListener() {
//            @Override
//            public void processEvent(Event event) {
//                Log.w("IMATAG", "DID_SET_VIDEO");
//                if (progressBar!=null){
//                    progressBar.setVisibility(View.VISIBLE);
//                }
//
//                VideoDisplayComponent videoDisplayComponent = baseVideoView.getVideoDisplay();
//                if (videoDisplayComponent instanceof ExoPlayerVideoDisplayComponent) {
//                    //Get ExoPlayer
//                    ExoPlayer exoPlayer = ((ExoPlayerVideoDisplayComponent) videoDisplayComponent).getExoPlayer();
//                    if (exoPlayer instanceof SimpleExoPlayer) {
//                        //Get SimpleExoPlayer
//                        SimpleExoPlayer simpleExoPlayer = (SimpleExoPlayer) exoPlayer;
//                        ((SimpleExoPlayer) exoPlayer).addVideoListener(new com.google.android.exoplayer2.video.VideoListener() {
//                            @Override
//                            public void onVideoSizeChanged(int width, int height, int unappliedRotationDegrees, float pixelWidthHeightRatio) {
//
//                            }
//
//                            @Override
//                            public void onRenderedFirstFrame() {
//
//                            }
//                        });
//                        // Log.v(TAG, "onDID_SET_VIDEO: ExoPlayer = " + simpleExoPlayer+" "+trackGroupArray.get(0));
//
//
//                    }
//                }
//            }
//
//        });
//
//        //Callback for player progress
//        eventEmitter.on(EventType.PROGRESS, new EventListener() {
//            @Override
//            public void processEvent(Event event) {
//                if (baseVideoView.getDuration() != 0) {
//                    if (playerControlsFragment != null) {
//                        playerControlsFragment.setTotalDuration(baseVideoView.getDuration());
//                        playerControlsFragment.setCurrentPosition(baseVideoView.getCurrentPosition());
//                        Log.d("Progress","playerProgress");
//                        if (mListener != null) {
//                            mListener = (OnPlayerInteractionListener) mActivity;
//                            mListener.onPlayerInProgress();
//                        }
//                    }
//
//                    double currentPosition = baseVideoView.getCurrentPosition();
//                    if (currentPosition == skipHideTime || currentPosition > skipHideTime) {
//                        if (playerControlsFragment != null) {
//                            skipHideTime = 0;
//                            playerControlsFragment.hideSkipIntro();
//                        }
//                    }
//                    Log.w("progressValuess",bingeWatch+" "+currentPosition + " "+ bingeWatchTimer+"  "+baseVideoView.getDuration());
//                    if (bingeWatch && bingeWatchTimer>0){
//                        if (currentPosition>=bingeWatchTimer){
//                            willBingWatchShow=true;
//                            if (!isInPictureinPicture){
//                                playerControlsFragment.showBingeWatch();
//                            }
//                        }
//                    }
//                }
//
//            }
//        });
//
//        //Callback for player pause
//        eventEmitter.on(EventType.PAUSE, new EventListener() {
//            @Override
//            public void processEvent(Event event) {
//                if (playerControlsFragment != null) {
//                    playerControlsFragment.SendPlayerPauseState(event.getType());
//                }
//            }
//        });
//        //Callback for play
//        eventEmitter.on(EventType.PLAY, new EventListener() {
//            @Override
//            public void processEvent(Event event) {
//                Log.w("IMATAG", "PLAY");
//
//                if (progressBar!=null){
//                    progressBar.setVisibility(View.GONE);
//                }
//                if (playerControlsFragment != null) {
//                    playerControlsFragment.sendPlayerPlayState(event.getType());
//                }
//                try {
//                    if (mListener != null) {
//                        mListener = (OnPlayerInteractionListener) mActivity;
//                        if (baseVideoView!=null && !baseVideoView.isPlaying()){
//                            mListener.onPlayerStart();
//                        }
//
//                        Log.w("Playerstart", "PLAY");
//
//                        if (playerControlsFragment != null) {
//                            if (!isInPictureinPicture) {
//                                playerControlsFragment.sendTapCallBack(true);
//                                playerControlsFragment.startHandler();
//                            }
//                        }
//                    }
//                    int orientation = getResources().getConfiguration().orientation;
//                    if (orientation == Configuration.ORIENTATION_LANDSCAPE) {
//                        if (playerControlsFragment!=null && playerControlsFragment.fullscreen!=null){
//                            playerControlsFragment.fullscreen.setBackgroundResource(R.drawable.exit_full_screen);
//                        }
//                    }
//
//                } catch (Exception e) {
//
//                }
//            }
//        });
//        //Callback for buffering started
//        eventEmitter.on(EventType.BUFFERING_STARTED, new EventListener() {
//            @Override
//            public void processEvent(Event event) {
//                if (Network.HaveNetworkConnection(mActivity)) {
//                    Log.w("IMATAG", "BUFFERING_STARTED");
//                    progressBar.setVisibility(View.VISIBLE);
//                } else {
//                    showErrorDialog(getString(R.string.no_internet));
//                }
//            }
//        });
//        //Callback for buffering completed
//        eventEmitter.on(EventType.BUFFERING_COMPLETED, new EventListener() {
//            @Override
//            public void processEvent(Event event) {
//                Log.w("IMATAG", "BUFFERING_COMPLETE");
//                progressBar.setVisibility(View.GONE);
////                if (mListener != null) {
////                    mListener = (OnPlayerInteractionListener) mActivity;
////                    mListener.OnPlayerCompleted();
////                }
//                if (playerControlsFragment != null) {
//                    playerControlsFragment.setTotalDuration(baseVideoView.getDuration());
//                }
//            }
//        });
//
//        //Callback for Playback finish
//        eventEmitter.on(EventType.COMPLETED, new EventListener() {
//            @Override
//            public void processEvent(Event event) {
//                baseVideoView.stopPlayback();
//                isContentCompleted=true;
//                willBingWatchShow=false;
//                baseVideoView.seekTo(baseVideoView.getDuration());
//                if (playerControlsFragment != null) {
//                    playerControlsFragment.hideControls();
//                    if (!isInPictureinPicture){
//                        if (playerControlsFragment.bingeLay.getVisibility()==View.VISIBLE){
//                            playerControlsFragment.backArrow.setVisibility(View.VISIBLE);
//                        }else {
//                            playerControlsFragment.sendVideoCompletedState(event.getType());
//                        }
//
//                    }
//                }
//                progressBar.setVisibility(View.GONE);
//                eventEmitter.disable();
//                mListener.onBookmarkFinish();
//                handler.removeCallbacksAndMessages(runnable);
//                if (bingeWatch){
//                    isBingeWatchTimeCalculate=false;
//                    mListener.bingeWatchCall(brightcoveAccountId);
//                }
//
//            }
//        });
//
//        //Callback for Playback error
//        eventEmitter.on(EventType.ERROR, new EventListener() {
//            @Override
//            public void processEvent(Event event) {
//                Exception exception = (Exception) event.properties.get(Event.ERROR);
//                Log.w("IMATAG", exception + "");
//                if (assetType != null && assetType.equalsIgnoreCase("LIVE")) {
//
//                } else {
//                    if (exception != null) {
//                        if (exception instanceof AdError) {
//
//                            /*if (((AdError) exception).getErrorCodeNumber() == 402){
//
//                            }
//                            else if (((AdError) exception).getErrorCodeNumber() == 1005){
//
//                            }else if (((AdError) exception).getErrorCodeNumber() == 900){
//
//                            }
//                            else {
//                                showErrorDialog("");
//                            }*/
//
//                        } else {
//                            showErrorDialog("");
//                        }
//                    } else {
//                        showErrorDialog("");
//                    }
//                }
//
//
//                String message = (String) event.properties.get(Event.ERROR_MESSAGE);
//                Video video = (Video) event.properties.get(Event.VIDEO);
//                Source source = (Source) event.properties.get(Event.SOURCE);
//                if (event.properties.containsKey("error_code")) {
//                    String errorCode = (String) event.properties.get("error_code");
//                }
//            }
//        });
//
//        eventEmitter.on(EventType.DID_REMOVE_VIDEO_STILL, new EventListener() {
//            @Override
//            public void processEvent(Event event) {
//                if (playerControlsFragment != null) {
//                    //progressBar.setVisibility(View.VISIBLE);
//                    playerControlsFragment.setFragmenmtVisibility();
//                }
//                //progressBar.setVisibility(View.GONE);
//            }
//        });
//        eventEmitter.on(EventType.CAPTIONS_AVAILABLE, new EventListener() {
//            @Override
//            public void processEvent(Event event) {
//                Log.w("captionHide", "CAPTIONS_AVAILABLE");
//                if (baseVideoView.getClosedCaptioningController().checkIfCaptionsExist(currentVideo)) {
//                    Log.w("captionHide", "in");
//                    captionStyleUpdate();
//                    if (playerControlsFragment != null) {
//                        playerControlsFragment.sendCaptionAvailable(event.getType());
//                    }
//                }
//            }
//        });
//        eventEmitter.on(EventType.CAPTIONS_LANGUAGES, new EventListener() {
//            @Override
//            public void processEvent(Event event) {
//                Log.w("captionHide", "CAPTIONS_LANGUAGES");
//                List captions = (List) event.properties.get("languages");
//                if (captions.size() >= 1)
//                    captionStyleUpdate();
//                if (playerControlsFragment != null) {
//                    playerControlsFragment.sendCaptionAvailable(event.getType());
//                }
//
//            }
//        });
//        eventEmitter.on(EventType.CAPTIONS_DIALOG_SETTINGS, new EventListener() {
//            @Override
//            public void processEvent(Event event) {
//            }
//        });
//
//        //Checking Audio Availabilty
//        eventEmitter.on(EventType.AUDIO_TRACKS, event -> {
//
//            tracks = (List) event.properties.get("tracks");
//
//
//            if (tracks.size() > 1) {
//                if (playerControlsFragment != null) {
//                    playerControlsFragment.sendAudioAvailable(event.getType());
//                }
//            }
//
//            String selectedTrack = (String) event.properties.get("track");
//            if (selectedTrack != null) {
//                for (int i = 0; i < tracks.size(); ++i) {
//                    if (tracks.get(i).equals(selectedTrack)) {
//                        break;
//                    }
//                }
//            }
//            audioTracks = new CharSequence[tracks.size()];
//            for (int i = 0; i < this.tracks.size(); ++i) {
//                audioTracks[i] = this.tracks.get(i);
//            }
//
//        });
//
//        eventEmitter.on(EventType.DID_PLAY, new EventListener() {
//            @Override
//            public void processEvent(Event event) {
//                if (playerControlsFragment != null) {
//                    if (!isBingeWatchTimeCalculate){
//                        Log.w("totalDuartion",baseVideoView.getDuration()+"");
//                        Log.w("totalDuartion",bingeWatchTimer*1000+"");
//                        Log.w("totalDuartion",baseVideoView.getDuration()-bingeWatchTimer*1000+"");
//                        int timeCalculation=baseVideoView.getDuration()-bingeWatchTimer*1000;
//                        if (timeCalculation>bingeWatchTimer){
//                            isBingeWatchTimeCalculate=true;
//                            bingeWatchTimer=baseVideoView.getDuration()-bingeWatchTimer*1000;
//                        }
//
//                    }
//
//                    playerControlsFragment.setTotalDuration(baseVideoView.getDuration());
//                    playerControlsFragment.setCurrentPosition(baseVideoView.getCurrentPosition());
//                    playerControlsFragment.sendTapCallBack(true);
//                }
//                startBookmarking();
//
//            }
//        });
//
//        //Check if media is loaded
//        eventEmitter.on(EventType.READY_TO_PLAY, new EventListener() {
//            @Override
//            public void processEvent(Event event) {
//                if (!isOffline) {
//                    getSelectedTrack();
//                    mActivity.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_SENSOR);
//                }
//            }
//        });
//
//
//        eventEmitter.on(EventType.DID_ENTER_PICTURE_IN_PICTURE_MODE, new EventListener() {
//            @Override
//            public void processEvent(Event event) {
//
//                if (playerControlsFragment != null) {
//                    // playerControlsFragment.sendTapCallBack(false);
//                    if (!baseVideoView.isPlaying()) {
//                        baseVideoView.start();
//                        /*if (mListener!=null && playerControlsFragment!=null){
//                            mListener.isInPip(true);
//                            playerControlsFragment.hideControls();
//                            playerControlsFragment.isInPip(true);
//                        }*/
//                    }
//                }
//            }
//        });
//        eventEmitter.on(EventType.DID_EXIT_PICTURE_IN_PICTURE_MODE, new EventListener() {
//            @Override
//            public void processEvent(Event event) {
//
//                if (playerControlsFragment != null) {
//                    //  baseVideoView.stopPlayback();
//                }
//            }
//        });
//
//        eventEmitter.on(GoogleCastEventType.CAST_SESSION_STARTED, new EventListener() {
//            @Override
//            public void processEvent(Event event) {
//                boolean playing = ChromecastManager.getInstance().updatePlayingId(videoId);
//                Log.w("videoFound", "inDown");
////                ChromecastManager.getInstance().loadRemoteMediaOtt(videoId);
//                if (baseVideoView != null) {
//                    baseVideoView.pause();
//                }
//                progressBar.setVisibility(View.GONE);
//            }
//        });
//
//        eventEmitter.on(GoogleCastEventType.LOAD_MEDIA_INFO, new EventListener() {
//            @Override
//            public void processEvent(Event event) {
//                Log.w("mediaInfo", event.toString());
//                //googleCastComponent.loadMediaInfo(mediaInfo);
//            }
//        });
//
//
//        eventEmitter.on(GoogleCastEventType.CAST_SESSION_ENDED, new EventListener() {
//            @Override
//            public void processEvent(Event event) {
//
//            }
//        });
//        CustomData customData = new BrightcoveCastCustomData.Builder(getActivity())
//                .setAccountId(getString(R.string.brightcove_account_id))
//                // Set your account’s policy key
//                .setPolicyKey(getString(R.string.brightcove_policy_key))
//                // Optional: Set your Edge Playback Authorization (EPA) JWT token here
//                // Note that if you set the EPA token, you will not need to set the Policy Key
//                .setBrightcoveAuthorizationToken(null)
//                // Optional: For SSAI videos, set your adConfigId here
//                // Set your Analytics application ID here
//                .setApplicationId(PROPERTY_APPLICATION_ID)
//                .build();
//
//        googleCastComponent = new GoogleCastComponent.Builder(eventEmitter, getActivity())
//                .setAutoPlay(true)
//                .setEnableCustomData(true)
//                .setCustomData(customData)
//                .build();
//        googleCastComponent.isSessionAvailable();
//    }
//
//    private void captionStyleUpdate() {
//        BrightcoveClosedCaptioningManager manager = BrightcoveClosedCaptioningManager.getInstance(getActivity());
//        BrightcoveCaptionStyle captionStyle = BrightcoveCaptionStyle.builder()
//                .preset(CaptionConstants.PRESET_CUSTOM) // building a custom preset
//                .fontSize("0.6") // or your desired font size
//                .typeface("Arial") // or your desired typeFace
//                .foregroundColor(Color.WHITE) // or your desired text foreground color
//                .foregroundOpacity(CaptionConstants.DEFAULT_FOREGROUND_OPACITY) // or your desired text foreground opacity
//                .edgeColor(Color.GREEN) // or your desired text edge color
//                .edgeType(CaptionConstants.DEFAULT_EDGE_TYPE) // or your desired text edge type
//                .backgroundColor(Color.TRANSPARENT) // or your desired text background color
//                .backgroundOpacity(Color.TRANSPARENT) // or your desired text foreground opacity
//                .windowColor(Color.MAGENTA) // or your desired text window color)
//                .windowOpacity(CaptionConstants.DEFAULT_WINDOW_OPACITY) // or your desired text window opacity
//                .build();
//        manager.setStyle(captionStyle);
//    }
//
//    GoogleCastComponent googleCastComponent;
//
//    private void cast(Video video) {
//        ChromecastManager.getInstance().setupCastListener();
//    }
//
//    MediaInfo mediaInfo;
//
//    private void castEnabling(Video video) {
//        try {
//            Map<DeliveryType, SourceCollection> map = video.getSourceCollections();
//            SourceCollection sor = map.get(DeliveryType.valueOf("DASH"));
//            Set<Source> se = sor.getSources();
//            Object[] ar = se.toArray();
//            Source hp = (Source) ar[0];
//            Map<String, Object> ma = hp.getProperties();
//            String url = (String) ma.get("url");
//
//            MediaMetadata movieMetadata = new MediaMetadata(MediaMetadata.MEDIA_TYPE_MOVIE);
//            movieMetadata.putString(MediaMetadata.KEY_TITLE, video.getName());
//            movieMetadata.addImage(new WebImage(Uri.parse(video.getPosterImage().toString())));
//            //movieMetadata.addImage(new WebImage(Uri.parse(mSelectedMedia.getImage(1))));
//
//            JSONObject jsonObject = new JSONObject();
//            jsonObject.put("adTagUrl", adRulesURL);
//
//            Log.w("chromecastUrl", url);
//
//            List<AdBreakClipInfo> adBreakClipInfoList = new ArrayList<>();
//            VastAdsRequest vastRequest = new VastAdsRequest.Builder().setAdTagUrl("https://pubads.g.doubleclick.net/gampad/ads?sz=640x480&iu=/124319096/external/ad_rule_samples&ciu_szs=300x250&ad_rule=1&impl=s&gdfp_req=1&env=vp&output=vmap&unviewed_position_start=1&cust_params=deployment%3Ddevsite%26sample_ar%3Dpreonly&cmsid=496&vid=short_onecue&correlator=").build();
//            AdBreakClipInfo clipInfo1 = new AdBreakClipInfo.Builder("100").setVastAdsRequest(vastRequest).build();
//            adBreakClipInfoList.add(clipInfo1);
//
//            List<AdBreakInfo> adBreakInfoList = new ArrayList<>();
//            final String[] breakClipIds = new String[]{"100"};
//            AdBreakInfo adBreakInfo1 = new AdBreakInfo.Builder(0).setBreakClipIds(breakClipIds).setId("101").build();
//            adBreakInfoList.add(adBreakInfo1);
//
//            mediaInfo = new MediaInfo.Builder(url)
//                    .setStreamType(MediaInfo.STREAM_TYPE_BUFFERED)
//                    .setContentType("videos/mp4")
//                    .setMetadata(movieMetadata)
//                    .setStreamDuration(video.getDuration() * 1000)
//                    .build();
//
//            Log.w("mediaInfo", mediaInfo.toString());
//            if (mediaInfo != null) {
//                loadMediaInfo(eventEmitter, video);
//            }
//        } catch (Exception e) {
//            if (ChromecastManager.getInstance().getmCastContext(getActivity()) != null && ChromecastManager.getInstance().getmCastContext(getActivity()).getSessionManager() != null) {
//                ChromecastManager.getInstance().getmCastContext(getActivity()).getSessionManager().endCurrentSession(true);
//            }
//        }
//
//    }
//
//    private void loadMediaInfo(EventEmitter emitter, Video video) {
//        new Handler().postDelayed(new Runnable() {
//            @Override
//            public void run() {
//                Map<String, Object> properties = new HashMap<>();
//                properties.put(GoogleCastComponent.CAST_MEDIA_INFO, mediaInfo);
//                properties.put(GoogleCastComponent.CAST_MEDIA_PLAY_POSITION, 0);
//                emitter.emit(GoogleCastEventType.LOAD_MEDIA_INFO, properties);
//
//                Map<String, Object> properties2 = new HashMap<>();
//                properties2.put("value", brightcoveAccountId);
//                emitter.emit("account", properties2);
//            }
//        }, 2000);
//
//    }
//
//
//    private void hideProgres() {
//        getActivity().runOnUiThread(new Runnable() {
//            @Override
//            public void run() {
//                new Handler().postDelayed(new Runnable() {
//                    @Override
//                    public void run() {
//                        Log.w("IMATAG", "BUFFERING_COMPLETED");
//                        progressBar.setVisibility(View.GONE);
//                    }
//                }, 1400);
//            }
//        });
//    }
//
//    private void startBookmarking() {
//        handler = new Handler();
//        runnable = new Runnable() {
//            @Override
//            public void run() {
//                if (baseVideoView != null) {
//                    double totalDuration = baseVideoView.getDuration();
//                    double currentPosition = baseVideoView.getCurrentPosition();
//                    double percentagePlayed = ((currentPosition / totalDuration) * 100L);
//                    if (percentagePlayed > 10 && percentagePlayed <= 95) {
//                        if (mListener != null) {
//                            mListener = (OnPlayerInteractionListener) mActivity;
//                            mListener.onBookmarkCall(baseVideoView.getCurrentPosition());
//                        }
//                        if (handler != null) {
//                            handler.postDelayed(this, 10000);
//                        }
//                    } else if (percentagePlayed > 95) {
//                        if (mListener != null) {
//                            mListener = (OnPlayerInteractionListener) mActivity;
//                            mListener.onBookmarkFinish();
//                        }
//                        Log.d("PercentagePlayed",percentagePlayed+"");
//                    } else {
//                        if (handler != null) {
//                            handler.postDelayed(this, 10000);
//                        }
//                    }
//                }
//            }
//        };
//        handler.postDelayed(runnable, 10000);
//    }
//
//    private void showAudioTracksDialog() {
//        AlertDialog.Builder alertDialog = new AlertDialog.Builder(mActivity, R.style.CustomDialogTheme);
//        alertDialog.setTitle(mActivity.getResources().getString(R.string.audio_track_selection)).setSingleChoiceItems(audioTracks, currentAudioLanguage, (dialog, which) -> {
//            currentAudioLanguage = which;
//            selectAudioTrack(which);
//
//        }).setPositiveButton(mActivity.getResources().getString(R.string.ok), (dialog, which) -> dialog.dismiss()).show();
//    }
//
//
//    protected void selectAudioTrack(int trackIndex) {
//        String track = this.tracks.get(trackIndex);
//        Map<String, Object> properties = new HashMap();
//        properties.put("track", track);
//        this.eventEmitter.emit("selectAudioTrack", properties);
//    }
//
//    FrameLayout playerRoot;
//
//    private void findPlayerId(View view) {
//        baseVideoView = view.findViewById(R.id.brightcove_video_view);
//        progressBar = view.findViewById(R.id.pBar);
//        Log.w("IMATAG", "viewFind");
//        progressBar.setVisibility(View.VISIBLE);
//        container = view.findViewById(R.id.container);
//       /* progressBar.setOnSystemUiVisibilityChangeListener(new View.OnSystemUiVisibilityChangeListener() {
//            @Override
//            public void onSystemUiVisibilityChange(int visibility) {
//                if (visibility == View.VISIBLE) {
//                    if (playerControlsFragment!=null) {
//                        playerControlsFragment.hideControls();
//                    }
//                } else {
//                    if (playerControlsFragment!=null) {
//                        playerControlsFragment.showControls();
//                    }
//                }
//            }
//        });*/
//    }
//
//    // TODO: Rename method, update argument and hook method into UI event
//    public void onButtonPressed(Uri uri) {
//        if (mListener != null) {
//            mListener.onFragmentInteraction(uri);
//        }
//    }
//
//    @Override
//    public void onAttach(Context context) {
//        super.onAttach(context);
//        if (!(mActivity instanceof OnPlayerInteractionListener)) {
//            try {
//                throw new Exception("Activity must implement OnPlayerInteractionListener");
//            } catch (Exception e) {
//                e.printStackTrace();
//            }
//        }
//    }
//
//    @Override
//    public void onDetach() {
//        super.onDetach();
//        mListener = null;
//    }
//
//    private void showErrorDialog(String exception) {
//        if (mListener != null){
//            mListener.onPlayerError(exception);
//        }
//        try {
//            if (baseVideoView!=null){
//                if(!isOfflineVideo){
//                    baseVideoView.stopPlayback();
//                    baseVideoView.removeListeners();
//                }
//            }
//        }catch (Exception ignored){
//            Log.w("IMATAG", ignored.toString());
//        }
//
//    }
//
//    private void callPlayerControlsFragment() {
//        if (!isAdded()) return;
//
//        FragmentTransaction transaction = getChildFragmentManager().beginTransaction();
//        if (playerControlsFragment == null) {
//            try {
//                playerControlsFragment = new PlayerControlsFragment();
//                transaction.add(R.id.container, playerControlsFragment, "PlayerControlsFragment");
//                transaction.addToBackStack(null);
//                transaction.commit();
//                playerControlsFragment.setVideoType(videoType);
//                playerControlsFragment.setPlayerCallBacks(this);
//            } catch (Exception ignored) {
//
//            }
//
//        }
//    }
//
//    @Override
//    public void playPause() {
//        Log.w("windowFocusChanged=3",exitFromPIP+"");
//        if (baseVideoView != null) {
//            if (baseVideoView.isPlaying()) {
//                baseVideoView.pause();
//            } else {
//                baseVideoView.start();
//            }
//
//        }
//
//    }
//
//    public boolean isPlaying() {
//        if (baseVideoView != null) {
//            return baseVideoView.isPlaying();
//        } else {
//            return false;
//        }
//    }
//
//    @Override
//    public void Forward() {
//        if (baseVideoView != null) {
//            baseVideoView.seekTo(baseVideoView.getCurrentPosition() + 10000);
//            if (playerControlsFragment != null) {
//                playerControlsFragment.sendPlayerCurrentPosition(baseVideoView.getCurrentPosition());
//            }
//        }
//    }
//
//    @Override
//    public void Rewind() {
//        if (baseVideoView != null) {
//            baseVideoView.seekTo(baseVideoView.getCurrentPosition() - 10000);
//            if (playerControlsFragment != null) {
//                playerControlsFragment.sendPlayerCurrentPosition(baseVideoView.getCurrentPosition());
//            }
//        }
//    }
//
//    @Override
//    public void skipIntro() {
//        if (baseVideoView != null) {
//            baseVideoView.seekTo(skipIntroPosition);
//            if (playerControlsFragment != null) {
//                skipIntroPosition = 0;
//                playerControlsFragment.skipIntro(titleOnButton);
//                playerControlsFragment.sendPlayerCurrentPosition(baseVideoView.getCurrentPosition());
//            }
//        }
//    }
//
//    @Override
//    public void bingeWatch() {
//        mListener.bingeWatchCall(brightcoveAccountId);
//    }
//
//    @Override
//    public void SeekbarLastPosition(long position) {
//        if (baseVideoView != null) {
//            baseVideoView.seekTo((int) position);
//        }
//    }
//
//    @Override
//    public void subtitlePopup() {
//        if (baseVideoView != null) {
//            if (baseVideoView.getClosedCaptioningController().checkIfCaptionsExist(currentVideo)) {
//                showCaptionDialog();
//            }
//        }
//    }
//
//    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
//    private void showCaptionDialog() {
//        baseVideoView.setClosedCaptioningEnabled(true);
//        List<Pair<Uri, BrightcoveCaptionFormat>> pairs = (List) baseVideoView.getCurrentVideo().getProperties().get("captionSources");
//        availableCapitonsList = new CharSequence[pairs.size() + 1];
//        availableCapitonsListDisplay = new CharSequence[pairs.size() + 1];
//
//        int i = 0;
//        availableCapitonsList[i] = "None";
//        availableCapitonsListDisplay[i] = "None";
//        for (Pair<Uri, BrightcoveCaptionFormat> pair : pairs
//        ) {
//            i++;
//            availableCapitonsList[i] = pair.second.language();
//            availableCapitonsListDisplay[i] = new Locale(pair.second.language()).getDisplayLanguage();
//        }
//        AlertDialog.Builder alertDialog = new AlertDialog.Builder(mActivity, R.style.CustomDialogTheme);
//        alertDialog.setTitle(mActivity.getResources().getString(R.string.caption_selection)).setSingleChoiceItems(availableCapitonsListDisplay, currentCaptionLanguage, (dialog, which) -> {
//            currentCaptionLanguage = which;
//            selectCaptions(currentCaptionLanguage);
//        }).setPositiveButton(mActivity.getResources().getString(R.string.ok), (dialog, which) -> dialog.dismiss()).show();
//    }
//
//    void selectCaptions(int trackIndex) {
//        if (trackIndex != 0 && this.availableCapitonsList != null && this.availableCapitonsList.length > 0) {
//            baseVideoView.getClosedCaptioningController().saveClosedCaptioningState(true);
//            baseVideoView.getClosedCaptioningController().setShouldImportSystemSettings(true);
//            String selectedLanguageCode = (String) this.availableCapitonsList[trackIndex];
//            LoadCaptionsService loadCaptionsService = baseVideoView.getClosedCaptioningController().getLoadCaptionsService();
//            if (baseVideoView.getClosedCaptioningController().getLoadCaptionsService() != null) {
//                Pair<Uri, BrightcoveCaptionFormat> pair = getCaptionsForLanguageCode(selectedLanguageCode);
//                HashMap properties;
//                if (!((Uri) pair.first).equals(Uri.EMPTY)) {
//                    if (!((Uri) pair.first).toString().startsWith("brightcove://")) {
//                        loadCaptionsService.loadCaptions(pair.first, pair.second.type());
//                    }
//
//                    properties = new HashMap();
//                    properties.put("captionFormat", pair.second);
//                    properties.put("captionUri", pair.first);
//
//                    this.eventEmitter.emit("selectClosedCaptionTrack", properties);
//                } else {
//                    properties = new HashMap();
//                    properties.put("boolean", true);
//                    this.eventEmitter.emit("toggleClosedCaptions", properties);
//                }
//            }
//        } else {
//            baseVideoView.getClosedCaptioningController().saveClosedCaptioningState(false);
//        }
//
//    }
//
//    private Pair<Uri, BrightcoveCaptionFormat> getCaptionsForLanguageCode(String languageCode) {
//        List pairs = (List) baseVideoView.getCurrentVideo().getProperties().get("captionSources");
//        if (pairs != null) {
//            Iterator var3 = pairs.iterator();
//
//            while (var3.hasNext()) {
//                Pair<Uri, BrightcoveCaptionFormat> pair = (Pair) var3.next();
//                if (pair.second.language().equals(languageCode)) {
//                    return pair;
//                }
//            }
//        }
//        return null;
//    }
//
//
//    @Override
//    public void audioTrackPopup() {
//        if (baseVideoView != null) {
////            baseVideoView.getAudioTracksController().showAudioTracksDialog();
//            if (selected_lang.equalsIgnoreCase("Thai")){
//                Utils.updateLanguage("th", getActivity());
//            } else if (selected_lang.equalsIgnoreCase("English")){
//                Utils.updateLanguage("en", getActivity());
//            }
//            showAudioTracksDialog();
//        }
//    }
//
//    @Override
//    public void finishPlayer() {
//        checkBackButtonClickOrientation();
//    }
//
//    public void stopPlayer() {
//        if (baseVideoView != null) {
//            baseVideoView.stopPlayback();
//        }
//    }
//
//    private void checkBackButtonClickOrientation() {
//
//        int orientation = getResources().getConfiguration().orientation;
//        if (orientation == Configuration.ORIENTATION_LANDSCAPE) {
//            mActivity.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_SENSOR_PORTRAIT);
//        } else {
//
//            if (baseVideoView != null) {
//                baseVideoView.stopPlayback();
//            }
//        }
//    }
//
//    @Override
//    public void checkOrientation(ImageView id) {
//        FrameLayout.LayoutParams captionParams = (FrameLayout.LayoutParams) container.getLayoutParams();
//        captionParams.bottomMargin = (int) 0;
//        captionParams.topMargin = (int) 0;
//        container.setLayoutParams(captionParams);
//        if (id.getId() == R.id.backArrow) {
//            _isOrienataion(1, id);
//        } else {
//            int orientation = getResources().getConfiguration().orientation;
//            if (orientation == Configuration.ORIENTATION_LANDSCAPE) {
//                mActivity.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_SENSOR_PORTRAIT);
//                id.setBackgroundResource(R.drawable.full_screen);
//            } else {
//                mActivity.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_SENSOR_LANDSCAPE);
//                id.setBackgroundResource(R.drawable.exit_full_screen);
//            }
//        }
//    }
//
//    private void _isOrienataion(int i, ImageView id) {
//        int orientation = mActivity.getResources().getConfiguration().orientation;
//        if (isOfflineVideo && from==1 ) {
//            mActivity.finish();
//        }
//        {
//            if (orientation == Configuration.ORIENTATION_PORTRAIT) {
//                mActivity.finish();
//            } else {
//                mActivity.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_SENSOR_PORTRAIT);
//                //id.setBackgroundResource(R.drawable.full_screen);
//            }
//        }
//    }
//
//    @Override
//    public void replay() {
//        isContentCompleted=false;
//        eventEmitter.enable();
//        baseVideoView.seekTo(0);
//        baseVideoView.start();
//    }
//
//
//    @Override
//    public void showPlayerController(boolean isVisible) {
//
//        if (baseVideoView.getClosedCaptioningView() != null) {
//            FrameLayout.LayoutParams captionParams = (FrameLayout.LayoutParams) baseVideoView.getClosedCaptioningView().getLayoutParams();
//            if (isVisible)
//                captionParams.bottomMargin = (int) getResources().getDimension(R.dimen.caption_margin_default);
//            else
//                captionParams.bottomMargin = (int) getResources().getDimension(R.dimen.caption_margin_default);
//            //  baseVideoView.getClosedCaptioningView().setLayoutParams(captionParams);
//        }
//
//        try {
//            if (baseVideoView!=null){
//                playerControlsFragment.currentPosition.setText(Utils.stringForTime(baseVideoView.getCurrentPosition()));
//            }
//        }catch (Exception ignored){
//
//        }
//
//    }
//
//
//    @Override
//    public void onPause() {
//        super.onPause();
//
////        if (mActivity != null) {
////            if (receiver != null)
////                mActivity.unregisterReceiver(receiver);
////        }
//
//
//        if (handler != null) {
//            handler.removeCallbacksAndMessages(runnable);
//            handler = null;
//        }
////        if (isApplicationSentToBackground(mActivity)) {
////            // Do what you want to do on detecting Home Key being Pressed
////            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
////                if (enable) {
////
////                    if (playerControlsFragment!=null) {
////                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
////                            if(!baseVideoView.isPlaying()){
////                                baseVideoView.start();
////                            }
////                            // playerControlsFragment.sendTapCallBack(false);
////                            //  playerControlsFragment.hideControls();
////
////                        }
////                    }
////
////               //     PictureInPictureManager.getInstance().enterPictureInPictureMode();
////                    // PictureInPictureManager.getInstance().setOnUserLeaveEnabled(true);
//////                    PictureInPictureManager.getInstance().onUserLeaveHint();
////                } else {
////                    baseVideoView.pause();
////                }
////            }
////        } else {
////            baseVideoView.pause();
////        }
//    }
//
//
//    public boolean isApplicationSentToBackground(final Context context) {
//        ActivityManager am = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
//        List<ActivityManager.RunningTaskInfo> tasks = am.getRunningTasks(1);
//        if (!tasks.isEmpty()) {
//            ComponentName topActivity = tasks.get(0).topActivity;
//            if (!topActivity.getPackageName().equals(context.getPackageName())) {
//                return true;
//            }
//        }
//        return false;
//    }
//
//    @Override
//    public void onResume() {
//        super.onResume();
//        requestAudioFocus();
////        registerBroadcast();
//    }
//
//    private void registerBroadcast() {
//        receiver = NetworkChangeReceiver.getInstance();
//        IntentFilter filter = new IntentFilter();
//        filter.addAction("android.net.conn.CONNECTIVITY_CHANGE");
//        filter.addAction("android.net.wifi.WIFI_STATE_CHANGED");
//        filter.addAction("android.net.wifi.STATE_CHANGE");
//        mActivity.registerReceiver(receiver, filter);
//        receiver.setConnectivityReceiverListener(this);
//    }
//
//    @Override
//    public void onNetworkConnectionChanged(boolean isConnected) {
//        if (!isConnected) {
//            if (baseVideoView != null) {
//                baseVideoView.pause();
//                showErrorDialog(getString(R.string.no_internet));
//            }
//        }
//    }
//
//    @Override
//    public void onCallStateRinging() {
//        if (baseVideoView != null) {
//            baseVideoView.pause();
//        }
//    }
//
//    @Override
//    public void onCallStateIdle(int state) {
//
//        if (baseVideoView != null) {
//            baseVideoView.start();
//        }
//
//    }
//
//    @Override
//    public void onStart() {
//        super.onStart();
//        try {
//            TelephonyManager mgr = (TelephonyManager) mActivity.getApplicationContext().getSystemService(TELEPHONY_SERVICE);
//            if (mgr != null) {
//                mgr.listen(PhoneStateListenerHelper.getInstance(this), PhoneStateListener.LISTEN_CALL_STATE);
//            }
//        } catch (Exception e) {
//        }
//    }
//
//    @Override
//    public void onStop() {
//        super.onStop();
//        TelephonyManager mgr = (TelephonyManager) mActivity.getApplicationContext().getSystemService(TELEPHONY_SERVICE);
//        if (mgr != null) {
//            mgr.listen(PhoneStateListenerHelper.getInstance(this), PhoneStateListener.LISTEN_NONE);
//        }
//        if (handler != null) {
//            handler.removeCallbacksAndMessages(runnable);
//            handler = null;
//        }
//        baseVideoView.stopPlayback();
//        if (mActivity!=null && !mActivity.isFinishing()){
//            if (assetType.equalsIgnoreCase("EPISODES") || assetType.equalsIgnoreCase("EPISODE")){
//
//            }else {
//                finishPlayer();
//            }
//        }
//
//
//    }
//
//
//    Configuration currentConfig = null;
//
//    @Override
//    public void onConfigurationChanged(Configuration newConfig) {
//        if(!isCastConnected) {
//            super.onConfigurationChanged(newConfig);
//            if (!isOfflineVideo) {
//                if (newConfig.orientation == Configuration.ORIENTATION_LANDSCAPE) {
//                    mActivity.getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
//                    if (playerControlsFragment != null) {
//                        playerControlsFragment.sendLandscapeCallback();
//                    }
//                    bottomMargin = (int) getResources().getDimension(R.dimen.caption_margin_landscape);
//                    currentConfig = newConfig;
//                    if (adStarted) {
//                        baseVideoView.setPadding(0, 15, 10, 15);
//                    }
//
//                } else if (newConfig.orientation == Configuration.ORIENTATION_PORTRAIT) {
//                    if (playerControlsFragment != null) {
//                        playerControlsFragment.sendPortraitCallback();
//                    }
//                    bottomMargin = (int) getResources().getDimension(R.dimen.caption_margin);
//                    mActivity.getWindow().clearFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
//                    currentConfig = newConfig;
//                    baseVideoView.setPadding(0, 0, 0, 0);
//                }
//            }else {
//                if (from!=1){
//                    if (newConfig.orientation == Configuration.ORIENTATION_LANDSCAPE) {
//                        mActivity.getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
//                        if (playerControlsFragment != null) {
//                            playerControlsFragment.sendLandscapeCallback();
//                        }
//                        bottomMargin = (int) getResources().getDimension(R.dimen.caption_margin_landscape);
//                        currentConfig = newConfig;
//                        if (adStarted) {
//                            baseVideoView.setPadding(0, 15, 10, 15);
//                        }
//
//                    } else if (newConfig.orientation == Configuration.ORIENTATION_PORTRAIT) {
//                        if (playerControlsFragment != null) {
//                            playerControlsFragment.sendPortraitCallback();
//                        }
//                        bottomMargin = (int) getResources().getDimension(R.dimen.caption_margin);
//                        mActivity.getWindow().clearFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
//                        currentConfig = newConfig;
//                        baseVideoView.setPadding(0, 0, 0, 0);
//                    }
//                }
//            }
//        }
//    }
//
//    @Override
//    public void onDestroy() {
//        super.onDestroy();
//
//    }
//
//    boolean exitFromPIP=false;
//    public void onPictureInPictureModeChanged(boolean isInPictureInPictureMode, Configuration newConfig) {
//        super.onPictureInPictureModeChanged(isInPictureInPictureMode);
//
//
//        this.isInPictureinPicture = isInPictureInPictureMode;
//        Log.w("windowFocusChanged=2",isInPictureInPictureMode+"");
//        if (isInPictureInPictureMode) {
//            exitFromPIP=true;
//            if (playerControlsFragment != null) {
//                mListener.isInPip(true);
//                playerControlsFragment.removeTimer();
//                playerControlsFragment.hideControls();
//                playerControlsFragment.hideBingeWatch();
//                playerControlsFragment.isInPip(true);
//
//            }
//        } else {
//            if (playerControlsFragment != null) {
//
//                final Handler handler = new Handler();
//                mListener.isInPip(false);
//                handler.postDelayed(new Runnable() {
//                    @Override
//                    public void run() {
//                        Log.w("windowFocusChanged=4",isInPictureInPictureMode+"");
//
//                        //Do something after 100ms
//
//                       /* Video video=baseVideoView.getCurrentVideo();
//                        Log.w("currentVideo",video+"");*/
//                        if (!adRunning){
//                            Log.w("conditionCheck",willBingWatchShow+" "+isContentCompleted+" "+adRunning);
//                            adRunning=false;
//                            if (isContentCompleted){
//                                playerControlsFragment.sendVideoCompletedState(EventType.COMPLETED);
//                            }else {
//                                if (willBingWatchShow){
//                                    playerControlsFragment.showBingeWatch();
//                                }else {
//                                    playerControlsFragment.sendTapCallBack(true);
//                                    playerControlsFragment.startHandler();
//                                    playerControlsFragment.showControls();
//                                }
//                            }
//
//                        }
//                        playerControlsFragment.isInPip(false);
//                    }
//                }, 500);
//
//                new Handler().postDelayed(new Runnable() {
//                    @Override
//                    public void run() {
//                        exitFromPIP=false;
//                    }
//                },1000);
//
//
//            }
//        }
//
//    }
//
//    @Override
//    public void onFragmentInteraction(Uri uri) {
//
//    }
//
//    @Override
//    public void BackPressClicked(int value) {
//        checkBackButtonOrientation(value);
//    }
//
//    private void checkBackButtonOrientation(int value) {
//        FrameLayout.LayoutParams captionParams = (FrameLayout.LayoutParams) container.getLayoutParams();
//        captionParams.bottomMargin = (int) 0;
//        captionParams.topMargin = (int) 0;
//        container.setLayoutParams(captionParams);
//        if (value == 2) {
//            mActivity.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_SENSOR_PORTRAIT);
//        } else {
//            if (baseVideoView != null) {
//                baseVideoView.stopPlayback();
//                mActivity.finish();
//            }
//        }
//    }
//
//    public void hideControls() {
//        if (playerControlsFragment != null) {
//            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
//
//                PictureInPictureManager.getInstance().enterPictureInPictureMode();
//
//            }           Log.d("Progress","playerProgress");
//
//        }
//    }
//
//    @Override
//    public void onChromeCastConnecting() {
//        if (baseVideoView != null) {
//            baseVideoView.pause();
//            progressBar.setVisibility(View.VISIBLE);
//        }
//    }
//
//    @Override
//    public void onChromeCastConnected() {
//        if (getActivity() != null) {
//            progressBar.setVisibility(View.GONE);
//            chromeCastStartedListener.chromeCastViewConnected(true);
//        }
//    }
//
//    @Override
//    public void onChromeCastDisconnected() {
//        if (chromeCastStartedListener != null)
//            chromeCastStartedListener.chromeCastViewConnected(false);
//        isCastConnected = false;
//    }
//
//    @Override
//    public void onVideoEnded() {
//
//    }
//
//    @Override
//    public void onStatusUpdate() {
//        if (chromeCastStartedListener != null)
//            chromeCastStartedListener.chromeCastViewConnected(true);
//    }
//
//    public void updateEpisodesList(String seasonEpisodes) {
//
//    }
//
//    int totalEpisodes=0;
//    public void totalEpisodes(int size) {
//        totalEpisodes=size;
//    }
//    int runningEpisodes=0;
//    public void currentEpisodes(int i) {
//        runningEpisodes=i;
//        Log.w("totalZies",totalEpisodes+" "+runningEpisodes);
//        if (runningEpisodes<totalEpisodes){
//            bingeWatch=true;
//        }else {
//            bingeWatch=false;
//        }
//    }
//
//    public void bingeWatchStatus(boolean b) {
//        bingeWatch=b;
//    }
//
//    public String getRunningAssetId() {
//        return brightcoveAccountId;
//    }
    public interface OnPlayerInteractionListener {

        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);

        void onPlayerError(String error);

        void onBookmarkCall(int currentPosition);

        void onBookmarkFinish();

        void onPlayerStart();

        void onPlayerInProgress();

        void onAdStarted();


        default void isInPip(boolean status) {

        }

        default void bingeWatchCall(String brightcoveId) {

        }


    }
//
//
//
//    AdDisplayContainer containers;
//    boolean adStarted = false;
//    boolean adRunning=false;
//    private void setupGoogleIMA() {
//
//        // Defer adding cue points until the set video event is triggered.
//        eventEmitter.on(EventType.DID_SET_SOURCE, new EventListener() {
//            @Override
//            public void processEvent(Event event) {
//                Log.w("IMATAG", (Source) event.properties.get(Event.SOURCE) + "");
//                Log.w("IMATAG", event.properties + "");
//                // setupCuePoints((Source) event.properties.get(Event.SOURCE),0);
//            }
//        });
//
//        // Establish the Google IMA SDK factory instance.
//        final ImaSdkFactory sdkFactory = ImaSdkFactory.getInstance();
//
//        // Enable logging of ad starts
//        eventEmitter.on(EventType.AD_STARTED, new EventListener() {
//            @Override
//            public void processEvent(Event event) {
//                Log.w("IMATAG", "AD_STARTED");
//                if (playerControlsFragment != null) {
//                    progressBar.setVisibility(View.GONE);
//                    adStarted = true;
//                    adRunning=true;
//                    if (mListener!=null){
//                        mListener.onAdStarted();
//                    }
//
//                    if (currentConfig != null && currentConfig.orientation == Configuration.ORIENTATION_LANDSCAPE) {
//                        baseVideoView.setPadding(0, 15, 10, 15);
//                    }
//                    if (playerControlsFragment != null) {
//                        playerControlsFragment.sendTapCallBack(false);
//                        playerControlsFragment.removeTimer();
//                        playerControlsFragment.hideControls();
//                    }
//                }
//            }
//        });
//
//        eventEmitter.on(EventType.AD_ERROR, new EventListener() {
//            @Override
//            public void processEvent(Event event) {
//                Log.w("IMATAG", "AD_ERROR");
//                if (playerControlsFragment != null) {
//                    progressBar.setVisibility(View.GONE);
//                    adRunning=false;
//                    playerControlsFragment.showControls();
//                }
//            }
//        });
//
//        // Enable logging of any failed attempts to play an ad.
//        eventEmitter.on(GoogleIMAEventType.DID_FAIL_TO_PLAY_AD, new EventListener() {
//            @Override
//            public void processEvent(Event event) {
//                Log.w("IMATAG", "DID_FAIL_TO_PLAY_AD");
////                 playerControlsFragment.showControls();
//            }
//        });
//
//        // Enable logging of ad completions.
//        eventEmitter.on(EventType.AD_COMPLETED, new EventListener() {
//            @Override
//            public void processEvent(Event event) {
//                Log.w("IMATAG", "AD_COMPLETED");
//                adRunning=false;
////                 playerControlsFragment.showControls();
//            }
//        });
//
//        // Set up a listener for initializing AdsRequests. The Google IMA plugin emits an ad
//        // request event in response to each cue point event.  The event processor (handler)
//        // illustrates how to play ads back to back.
//        eventEmitter.on(GoogleIMAEventType.ADS_REQUEST_FOR_VIDEO, new EventListener() {
//            @Override
//            public void processEvent(Event event) {
//                Log.w("IMATAG", event.properties + "");
//                Log.w("IMATAG", "ADS_REQUEST_FOR_VIDEO");
//                progressBar.setVisibility(View.VISIBLE);
//                if (playerControlsFragment != null) {
//                    playerControlsFragment.removeTimer();
//                    playerControlsFragment.hideControls();
//
//                }
//                containers = sdkFactory.createAdDisplayContainer();
//                containers.setPlayer(googleIMAComponent.getVideoAdPlayer());
//                containers.setAdContainer(baseVideoView);
//                AdsRequest adsRequest = sdkFactory.createAdsRequest();
//                adsRequest.setAdTagUrl(adRulesURL);
//                adsRequest.setAdDisplayContainer(containers);
//                ArrayList<AdsRequest> adsRequests = new ArrayList<AdsRequest>(1);
//                adsRequests.add(adsRequest);
//                event.properties.put(GoogleIMAComponent.ADS_REQUESTS, adsRequests);
//                eventEmitter.respond(event);
//            }
//        });
//
//        // Create the Brightcove IMA Plugin and register the event emitter so that the plugin
//        // can deal with video events.
//        googleIMAComponent = new GoogleIMAComponent(baseVideoView, eventEmitter,true);
//    }
//
//    private void setupCuePoints(Source source, int type) {
//        String cuePointType = "ad";
//        Map<String, Object> properties = new HashMap<String, Object>();
//        Map<String, Object> details = new HashMap<String, Object>();
//
//
//        try {
//            CuePoint cuePoint = new CuePoint(CuePoint.PositionType.POINT_IN_TIME, cuePointType, properties);
//            details.put(Event.CUE_POINT, cuePoint);
//            eventEmitter.emit(EventType.SET_CUE_POINT, details);
//        } catch (Exception e) {
//            Log.d("EXCEPTION", e.getMessage());
//        }
//
//        // preroll
//        CuePoint cuePoint = new CuePoint(CuePoint.PositionType.BEFORE, cuePointType, properties);
//        details.put(Event.CUE_POINT, cuePoint);
//        eventEmitter.emit(EventType.SET_CUE_POINT, details);
//
////        // postroll
//        cuePoint = new CuePoint(CuePoint.PositionType.AFTER, cuePointType, properties);
//        details.put(Event.CUE_POINT, cuePoint);
//        eventEmitter.emit(EventType.SET_CUE_POINT, details);
//    }
//
//    /**
//     * Provide a sample illustrative ad.
//     */
//    private String[] googleAds = {
//            // Honda Pilot
//            "https://pubads.g.doubleclick.net/gampad/ads?sz=640x480&iu=/124319096/external/single_ad_samples&ciu_szs=300x250&impl=s&gdfp_req=1&env=vp&output=vast&unviewed_position_start=1&cust_params=deployment%3Ddevsite%26sample_ct%3Dskippablelinear&correlator=[timestamp]"
//    };
//
//    private void createCuePointList(Video video) {
//        if (video.getCuePoints().size() > 0) {
//            // new CuePointPlugin(baseVideoView.getEventEmitter(), getActivity(), baseVideoView);
//            setUpDataCuePoint();
//            for (CuePoint cuePoint : video.getCuePoints()) {
//                cuePointsList.add(cuePoint);
//            }
//
//        }
//    }
//
//
//    int skipIntroPosition = 0;
//    int skipHideTime = 0;
//    String titleOnButton = "";
//
//    private void setUpDataCuePoint() {
//
//        eventEmitter.on(EventType.DID_SET_SOURCE, new EventListener() {
//            @Override
//            public void processEvent(Event event) {
//                setupCuePoints((Source) event.properties.get(Event.SOURCE), 1);
//            }
//        });
//
//        eventEmitter.on(EventType.CUE_POINT, new EventListener() {
//            @Override
//            public void processEvent(Event event) {
//
//                int startTime = (int) event.getProperties().get("startTime");
//                if (startTime > 0) {
//                    if (cuePointsList.size() > 0) {
//                        int value;
//                        for (int i = 0; i < cuePointsList.size(); i++) {
//                            if (baseVideoView.getCurrentPosition() > cuePointsList.get(i).getPosition()) {
//                                value = baseVideoView.getCurrentPosition() - cuePointsList.get(i).getPosition();
//                            } else {
//                                value = cuePointsList.get(i).getPosition() - baseVideoView.getCurrentPosition();
//                            }
//
//                            if (value < 1000) {
//                                if (cuePointsList.get(i).getProperties().get(Constants.META_DATA).toString().contains(Constants.SKIPINTRO)) {
//
//                                    skipIntroPosition = Utils.getSkipPosition(cuePointsList.get(i).getProperties().get(Constants.META_DATA).toString());
//                                    skipHideTime = Utils.getSkipHideTime(cuePointsList.get(i).getProperties().get(Constants.META_DATA).toString());
//                                    titleOnButton = Utils.getSkipBtnText(cuePointsList.get(i).getProperties().get(Constants.META_DATA).toString());
//                                    int currentPosition = baseVideoView.getCurrentPosition();
//                                    skipHideTime = skipHideTime + currentPosition;
//
//                                    if (playerControlsFragment != null) {
//                                        playerControlsFragment.skipIntro(titleOnButton);
//                                    }
//                                    break;
//                                }
//                            }
//                        }
//                    }
//                }
//            }
//        });
//
//
//    }
//
//    @Override
//    public void changeBitRateRequest(String title, int position) {
//        selected_track = title;
//        VideoDisplayComponent videoDisplayComponent = baseVideoView.getVideoDisplay();
//        if (videoDisplayComponent instanceof ExoPlayerVideoDisplayComponent) {
//            Log.d("SelectedTrackName -->>", title + "  " + position);
//            if (title.equalsIgnoreCase(getActivity().getResources().getString(R.string.auto)) || title.equalsIgnoreCase(Constants.AUTO)) {
//                Log.d("SelectedTrackName -->>", title + "  " + Constants.AUTO + "  " + trackSelector);
//                updateVideoTrack(position);
//
//            } else {
//                updateVideoTrack(position);
//                // ((ExoPlayerVideoDisplayComponent) baseVideoView.getVideoDisplay()).setPeakBitrate(videoTrackArray.get(position).bitrate);
//
//            }
//
//        }
//
//    }
//
//    private void updateVideoTrack(int position) {
//        try {
//            VideoDisplayComponent component = baseVideoView.getVideoDisplay();
//            if (component instanceof ExoPlayerVideoDisplayComponent) {
//                ExoPlayerVideoDisplayComponent exoComponent = (ExoPlayerVideoDisplayComponent) component;
//
//                ExoPlayer exoPlayer = exoComponent.getExoPlayer();
//                MappingTrackSelector mappingTrackSelector = exoComponent.getTrackSelector();
//                if (mappingTrackSelector instanceof DefaultTrackSelector) {
//                    trackSelector = (DefaultTrackSelector) mappingTrackSelector;
//
//                    mappedTrackInfo = trackSelector.getCurrentMappedTrackInfo();
//                    if (mappedTrackInfo.length > 0) {
//                        trackGroups = mappedTrackInfo.getTrackGroups(0);
//                        DefaultTrackSelector.Parameters pp = trackSelector.getParameters();
//                        trackSelectedPosition = position;
//                        DefaultTrackSelector.SelectionOverride selectiveBitRateOverride = null;
//                        if (position > 0) {
//                            Log.d("selectedPosition", position + "  " + videoTrackArray.get(position - 1).height + "  " + videoTrackArray.get(position - 1).bitrate);
//                            Log.w("", videoTrackArray.get(position - 1).bitrate + "");
//                            selectiveBitRateOverride = new DefaultTrackSelector.SelectionOverride(0, position - 1);
//
//                        } else {
//                            int[] trackIndices = new int[trackGroups.get(0).length];
//
//                            for (int idx = 0; idx < trackGroups.get(0).length; idx++) {
//                                trackIndices[idx] = idx;
//                            }
//                            selectiveBitRateOverride = new DefaultTrackSelector.SelectionOverride(0, trackIndices);
//                        }
//
//                        trackSelector.setParameters(trackSelector.buildUponParameters()
//                                .setSelectionOverride(0, trackGroups, selectiveBitRateOverride));
//
//                    }
//                }
//            }
//        } catch (Exception ignored) {
//
//        }
//
//    }
//
//    @Override
//    public void bitRateRequest() {
//        if (selected_lang.equalsIgnoreCase("Thai")){
//            Utils.updateLanguage("th", getActivity());
//        } else if (selected_lang.equalsIgnoreCase("English")){
//            Utils.updateLanguage("en", getActivity());
//        }
//        gettingVideoTracks("");
//    }
//
//    private void getSelectedTrack() {
//
//        new Handler().postDelayed(new Runnable() {
//            @Override
//            public void run() {
//                int position = Utils.getSelectedTrackPosition(selected_track,getActivity());
//                Log.w("selectedPositionNew",position+""+" "+selected_track);
//
//                getVideoTracks();
//                if (videoTrackArray != null) {
//                    ArrayList<TrackItem> arrayList = Utils.createTrackList(videoTrackArray, getActivity());
//                    if (position > 0) {
//                        if (position < arrayList.size()) {
//                            updateVideoTrack(position);
//                        } else {
//                            selected_track = getActivity().getResources().getString(R.string.auto);
//                            // Toast.makeText(getActivity(), "Streaming setting not available", Toast.LENGTH_LONG).show();
//                        }
//                    }
//
//                }
//            }
//        },3000);
//
//
//
//    }
//
//    private void getVideoTracks() {
//        try {
//            if (videoTrackArray != null || videoTrackArray == null) {
//                videoTrackArray = new ArrayList<>();
//                VideoDisplayComponent component = baseVideoView.getVideoDisplay();
//                if (component instanceof ExoPlayerVideoDisplayComponent) {
//                    ExoPlayerVideoDisplayComponent exoComponent = (ExoPlayerVideoDisplayComponent) component;
//
//                    ExoPlayer exoPlayer = exoComponent.getExoPlayer();
//                    MappingTrackSelector mappingTrackSelector = exoComponent.getTrackSelector();
//                    if (mappingTrackSelector instanceof DefaultTrackSelector) {
//                        trackSelector = (DefaultTrackSelector) mappingTrackSelector;
//
//                        mappedTrackInfo = trackSelector.getCurrentMappedTrackInfo();
//                        if (mappedTrackInfo.length > 0) {
//                            trackGroups = mappedTrackInfo.getTrackGroups(0);
//
//                            for (int i = 0; i < trackGroups.length; i++) {
//                                group = trackGroups.get(i);
//
//                                for (int j = 0; j < trackGroups.get(i).length; j++) {
//                                    Log.d("selectedPosition", trackGroups.get(i).getFormat(j).height + " " + trackGroups.get(i).getFormat(j).width + "  " + trackGroups.get(i).getFormat(j).bitrate);
//                                    videoTrackArray.add(trackGroups.get(i).getFormat(j));
//                                }
//                            }
//
//
//                        }
//
//                    }
//                }
//            }
//
//        }catch (Exception ignored){
//
//        }
//
//    }
//
//    TrackGroup group;
//    int position = 1;
//    List<Format> videoTrackArray;
//
//    private void gettingVideoTracks(String msg) {
//        if (videoTrackArray != null || videoTrackArray == null) {
//            videoTrackArray = new ArrayList<>();
//            Log.d("DID_SET_VIDEO", msg);
//            VideoDisplayComponent component = baseVideoView.getVideoDisplay();
//            if (component instanceof ExoPlayerVideoDisplayComponent) {
//                ExoPlayerVideoDisplayComponent exoComponent = (ExoPlayerVideoDisplayComponent) component;
//
//                ExoPlayer exoPlayer = exoComponent.getExoPlayer();
//                MappingTrackSelector mappingTrackSelector = exoComponent.getTrackSelector();
//                if (mappingTrackSelector instanceof DefaultTrackSelector) {
//                    trackSelector = (DefaultTrackSelector) mappingTrackSelector;
//
//                    mappedTrackInfo = trackSelector.getCurrentMappedTrackInfo();
//                    if (mappedTrackInfo.length > 0) {
//                        trackGroups = mappedTrackInfo.getTrackGroups(0);
//
//                        for (int i = 0; i < trackGroups.length; i++) {
//                            group = trackGroups.get(i);
//
//                            for (int j = 0; j < trackGroups.get(i).length; j++) {
//                                Log.d("selectedPosition", trackGroups.get(i).getFormat(j).height + " " + trackGroups.get(i).getFormat(j).width + "  " + trackGroups.get(i).getFormat(j).bitrate);
//                                videoTrackArray.add(trackGroups.get(i).getFormat(j));
//                            }
//                        }
//                        /*270P - 360P = Low
//                        540P - 720P = Medium
//                        1080P = High*/
//                        DefaultTrackSelector.Parameters pp = trackSelector.getParameters();
//                        trackSelectedPosition = position;
//                        if (playerControlsFragment != null) {
//                            playerControlsFragment.setVideoFormate(videoTrackArray, selected_track,selected_lang);
//                        }
//                    }
//
//                }
//            }
//        }
//
//
//    }
//    int trackSelectedPosition = 0;
    public interface ChromeCastStartedCallBack {
        void chromeCastViewConnected(boolean status);
    }

}