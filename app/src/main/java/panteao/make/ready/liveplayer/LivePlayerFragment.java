package panteao.make.ready.liveplayer;

import android.app.Activity;
import android.app.Dialog;
import android.app.PendingIntent;
import android.app.PictureInPictureParams;
import android.app.RemoteAction;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.ActivityInfo;
import android.content.res.Configuration;
import android.graphics.drawable.Icon;
import android.media.AudioAttributes;
import android.media.AudioFocusRequest;
import android.media.AudioManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.util.Rational;
import android.telephony.PhoneStateListener;
import android.telephony.TelephonyManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;

import com.make.brightcovelibrary.BrightcovePlayerFragment;
import com.make.brightcovelibrary.NetworkChangeReceiver;
import com.google.android.exoplayer2.C;
import com.google.android.exoplayer2.DefaultLoadControl;
import com.google.android.exoplayer2.DefaultRenderersFactory;
import com.google.android.exoplayer2.ExoPlaybackException;
import com.google.android.exoplayer2.ExoPlayerFactory;
import com.google.android.exoplayer2.LoadControl;
import com.google.android.exoplayer2.PlaybackParameters;
import com.google.android.exoplayer2.Player;
import com.google.android.exoplayer2.SimpleExoPlayer;
import com.google.android.exoplayer2.Timeline;
import com.google.android.exoplayer2.drm.DefaultDrmSessionManager;
import com.google.android.exoplayer2.drm.FrameworkMediaDrm;
import com.google.android.exoplayer2.drm.HttpMediaDrmCallback;
import com.google.android.exoplayer2.source.MediaSource;
import com.google.android.exoplayer2.source.TrackGroupArray;
import com.google.android.exoplayer2.source.dash.DashMediaSource;
import com.google.android.exoplayer2.source.dash.DefaultDashChunkSource;
import com.google.android.exoplayer2.trackselection.AdaptiveTrackSelection;
import com.google.android.exoplayer2.trackselection.DefaultTrackSelector;
import com.google.android.exoplayer2.trackselection.TrackSelection;
import com.google.android.exoplayer2.trackselection.TrackSelectionArray;
import com.google.android.exoplayer2.trackselection.TrackSelector;
import com.google.android.exoplayer2.ui.PlayerView;
import com.google.android.exoplayer2.upstream.DataSource;
import com.google.android.exoplayer2.upstream.DefaultBandwidthMeter;
import com.google.android.exoplayer2.upstream.DefaultDataSourceFactory;
import com.google.android.exoplayer2.upstream.DefaultHttpDataSourceFactory;
import com.google.android.exoplayer2.util.Log;
import com.google.android.exoplayer2.util.Util;
import panteao.make.ready.R;
import panteao.make.ready.callbacks.commonCallbacks.PhoneListenerCallBack;
import panteao.make.ready.utils.cropImage.helpers.Logger;
import panteao.make.ready.utils.helpers.PhoneStateHelper;

import java.util.ArrayList;

import static android.content.Context.TELEPHONY_SERVICE;
import static android.media.AudioManager.AUDIOFOCUS_LOSS;


public class LivePlayerFragment extends Fragment implements Player.EventListener, NetworkChangeReceiver.ConnectivityReceiverListener, PhoneListenerCallBack {
    private BrightcovePlayerFragment.OnPlayerInteractionListener mListener;
    private PlayerView playerView;
    //  private String DRM_DASH_URL = "https://edge6a.v2h-cdn.com/drmlive/asia_action/manifest.mpd";
    private String DRM_DASH_URL = "";
    private String IS_LIVE_DRM = "";
    // private String DRM_HLS_URL = "https://load1.v2h-cdn.com/redirect/live_mvhub_v2/smil:asia_action_abr.smil?type=m3u8";
    // private String hlsvideouri="https://cdnapisec.kaltura.com/p/2215841/playManifest/entryId/1_w9zx2eti/format/applehttp/protocol/https/a.m3u8";
    private static Boolean mExoPlayerFullscreen = false;
    private Dialog mFullScreenDialog;
    ImageView mFullScreenIcon;
    FrameLayout liveLayer;
    private FrameLayout mFullScreenButton;
    private SimpleExoPlayer player;
    private ProgressBar progressBar;
    //String DRM_LICENSE_URL="https://wv.miratech.io/wvproxy/clicense?contentid=asia_action";
    String DRM_LICENSE_URL = "";
    String USER_AGENT = "user-agent";
    boolean isDrm = true;

    private int bottomMargin = 0;
    private int currentLanguage = 0;

    DefaultBandwidthMeter bandwidthMeter = new DefaultBandwidthMeter();
    HttpMediaDrmCallback drmCallback;
    com.google.android.exoplayer2.drm.DefaultDrmSessionManager DefaultDrmSessionManager;
    DefaultRenderersFactory defaultRenderersFactory;
    private boolean isInPictureinPicture = false;

    public void playPause() {
        if (player != null) {
            if (player.isPlaying()) {
                pausePlayer();
            } else {
                startPlayer();
            }
        }
    }

    public void BackPressClicked(int i) {
        checkBackButtonOrientation(i);

    }

    private void checkBackButtonOrientation(int value) {
        if (value == 2) {
            mActivity.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_SENSOR_PORTRAIT);
        } else {
            if (player != null) {
                player.stop();
                mActivity.finish();
            }
        }
    }

    public void hideControls() {

    }

    public boolean isPlaying() {
        if (player != null) {
            return player.isPlaying();
        } else {
            return false;
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public void pictureInPictureMode(PictureInPictureParams.Builder pictureInPictureParamsBuilder) {
        Rational aspectRatio = new Rational(playerView.getWidth(), playerView.getHeight());
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            pictureInPictureParamsBuilder.setAspectRatio(aspectRatio).build();
        }
    }

    @Override
    public void onNetworkConnectionChanged(boolean isConnected) {
        if (!isConnected) {
            if (playerView != null && player != null) {
                pausePlayer();
                showErrorDialog(getString(R.string.no_internet));
            }
        }
    }

    private void showErrorDialog(String exception) {
        if (mListener != null)
            mListener.onPlayerError(exception);
    }

    enum StreamingType {
        DASH, HLS
    }


    View result;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        if (mActivity != null) {
            mActivity.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_SENSOR);
        }
        result = inflater.inflate(R.layout.liveplayer_fragment, container, false);
        findPlayerId(result);
        super.onCreateView(inflater, container, savedInstanceState);
        bottomMargin = (int) getResources().getDimension(com.make.brightcovelibrary.R.dimen.caption_margin);
        return result;
    }

    private void findPlayerId(View result) {
        Bundle bundle = getArguments();
        if (bundle != null) {
            DRM_LICENSE_URL = bundle.getString("widevine_licence");
            DRM_DASH_URL = bundle.getString("widevine_url");
            IS_LIVE_DRM = bundle.getString("isLivedrm");

            if (IS_LIVE_DRM.equalsIgnoreCase("true")) {
                isDrm = true;
            } else {
                isDrm = false;
            }


        }


        initializePlayer();
    }

    private void initializePlayer() {
        if (player == null) {
            Logger.w("playerFragmentcal  ", "playPlayerWhenShimmer");
            drmCallback = new HttpMediaDrmCallback(DRM_LICENSE_URL, new DefaultHttpDataSourceFactory(USER_AGENT));
            player = initPlayer(isDrm, StreamingType.DASH, result);
            player.setPlayWhenReady(true);
            //createPipAction();

            initFullscreenButton();
            initFullscreenDialog();
            callAnimation();
        }
    }
    private boolean timer = true;
    private void callAnimation() {
        if (timer && viewHideShowTimeHandler!=null && viewHideShowRunnable!=null) {
            viewHideShowTimeHandler.removeCallbacks(viewHideShowRunnable);
        }
        ShowAndHideView();
    }

    Activity mActivity;

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        PhoneStateHelper.getInstance().setListener(this);
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        mActivity = getActivity();
        mListener = (BrightcovePlayerFragment.OnPlayerInteractionListener) mActivity;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

    }

    private SimpleExoPlayer initPlayer(boolean isDrm, StreamingType mediaType, View result) {
        progressBar = result.findViewById(R.id.progressBar);
        progressBar.setVisibility(View.VISIBLE);
        try {
            DefaultDrmSessionManager = new DefaultDrmSessionManager(C.WIDEVINE_UUID,
                    FrameworkMediaDrm.newInstance(C.WIDEVINE_UUID), drmCallback, null, true, 1);

            TrackSelection.Factory videotrackselectionfactory = new AdaptiveTrackSelection.Factory(bandwidthMeter);
            TrackSelector trackSelector = new DefaultTrackSelector(videotrackselectionfactory);
            LoadControl loadControl = new DefaultLoadControl();

            if (isDrm) {
                defaultRenderersFactory = new DefaultRenderersFactory(mActivity, DefaultDrmSessionManager);
            } else {
                defaultRenderersFactory = new DefaultRenderersFactory(mActivity);
            }

            player = ExoPlayerFactory.newSimpleInstance(mActivity, defaultRenderersFactory, trackSelector, loadControl);
            playerView = result.findViewById(R.id.exoplayer);
            playerView.setPlayer(player);
            DataSource.Factory datasourcefactory = new DefaultDataSourceFactory(mActivity, Util.getUserAgent(mActivity, "ExohlsDemo"), bandwidthMeter);

            MediaSource mediaSource = createMediaSource(isDrm, mediaType, datasourcefactory);
            player.addListener(this);
            player.prepare(mediaSource);
            playerView.requestFocus();
        } catch (Exception ignored) {
            progressBar.setVisibility(View.GONE);
        }
        return player;
    }

    MediaSource mediaSource;

    private MediaSource createMediaSource(boolean isDrm, StreamingType mediaType, DataSource.Factory datasourcefactory) {
        if (mediaType.equals(StreamingType.DASH)) {
            mediaSource = new DashMediaSource.Factory(
                    new DefaultDashChunkSource.Factory(datasourcefactory),
                    datasourcefactory
            ).createMediaSource(Uri.parse(DRM_DASH_URL));
        } else {
            //mediaSource=new HlsMediaSource.Factory(datasourcefactory).createMediaSource(Uri.parse(DRM_HLS_URL));
        }
        return mediaSource;
    }

    @Override
    public void onTimelineChanged(Timeline timeline, Object manifest, int reason) {

    }

    @Override
    public void onTracksChanged(TrackGroupArray trackGroups, TrackSelectionArray trackSelections) {

    }

    @Override
    public void onLoadingChanged(boolean isLoading) {

    }

    @Override
    public void onPlayerStateChanged(boolean playWhenReady, int playbackState) {
        Logger.e("PlaybackState", String.valueOf(playbackState));
        switch (playbackState) {
            case Player.STATE_BUFFERING:
                //You can use progress dialog to show user that video is preparing or buffering so please wait
                progressBar.setVisibility(View.VISIBLE);
                break;
            case Player.STATE_IDLE:
                //idle state
                break;
            case Player.STATE_READY:
                // dismiss your dialog here because our video is ready to play now
                callAnimation();
                progressBar.setVisibility(View.GONE);
                // pauseButton.setBackgroundResource(R.drawable.pause);
                Log.w("playerStart", "STATE_READY");
                if (mListener != null) {
                    mListener.onPlayerStart();
                }
                break;
            case Player.STATE_ENDED:
                // do your processing after ending of video
                break;
        }
    }

    @Override
    public void onRepeatModeChanged(int repeatMode) {

    }

    @Override
    public void onShuffleModeEnabledChanged(boolean shuffleModeEnabled) {

    }

    @Override
    public void onPlayerError(ExoPlaybackException error) {
        if (mListener != null) {
            mListener.onPlayerError("");
            progressBar.setVisibility(View.GONE);
        }
    }

    @Override
    public void onPositionDiscontinuity(int reason) {

    }

    @Override
    public void onPlaybackParametersChanged(PlaybackParameters playbackParameters) {

    }

    @Override
    public void onSeekProcessed() {

    }


    ImageView pauseButton;
    FrameLayout fullScreenLayout,main_media_frame,full_lay,live_lay;
    ImageView backArrow;
    RelativeLayout backArrowLay;

    private void initFullscreenButton() {
        backArrowLay=(RelativeLayout)result.findViewById(R.id.backArrowLay);
        main_media_frame = (FrameLayout) result.findViewById(R.id.main_media_frame);
        full_lay = (FrameLayout) result.findViewById(R.id.full_lay);
        live_lay = (FrameLayout) result.findViewById(R.id.live_lay);

        mFullScreenIcon = (ImageView) result.findViewById(R.id.full_screen);
        pauseButton = (ImageView) result.findViewById(R.id.pause_button);
        fullScreenLayout = (FrameLayout) result.findViewById(R.id.full_lay);
        backArrow = (ImageView) result.findViewById(R.id.back_arrow);
        liveLayer = result.findViewById(R.id.live_lay);
        mFullScreenIcon.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                if (!mExoPlayerFullscreen)
                    openFullscreenDialog();
                else
                    closeFullscreenDialog();

            }
        });

        pauseButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (player != null) {
                    player.release();
                    player = null;
                    progressBar.setVisibility(View.VISIBLE);
                    pauseButton.setVisibility(View.GONE);
                    initializePlayer();
                }
            }
        });

        backArrow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int orientation = getResources().getConfiguration().orientation;
                if (orientation == Configuration.ORIENTATION_LANDSCAPE) {
                    mActivity.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_SENSOR_PORTRAIT);
                    resetFullScreenMargin();
                } else {
                    mActivity.onBackPressed();
                }
            }
        });

        main_media_frame.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                    callAnimation();
            }
        });
    }

    private void pausePlayer() {
        if (player != null) {
            player.setPlayWhenReady(false);
            player.getPlaybackState();
            //createPipAction();
            pauseButton.setBackgroundResource(R.drawable.play);
        }
    }

    private void startPlayer() {
        player.setPlayWhenReady(true);
        player.getPlaybackState();
        //createPipAction();
        //  pauseButton.setBackgroundResource(R.drawable.pause);
    }

    private void openFullscreenDialog() {

        int orientation = getResources().getConfiguration().orientation;
        if (orientation == Configuration.ORIENTATION_LANDSCAPE) {
            mActivity.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_SENSOR_PORTRAIT);
            resetFullScreenMargin();
        } else {
            mActivity.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_SENSOR_LANDSCAPE);
            RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(
                    RelativeLayout.LayoutParams.WRAP_CONTENT,
                    RelativeLayout.LayoutParams.WRAP_CONTENT
            );
            params.setMargins(60, 60, 0, 0);
            backArrow.setLayoutParams(params);
            fullScreenMargin();
        }
    }

    private void resetFullScreenMargin() {
        mFullScreenIcon.setBackgroundResource(com.make.brightcovelibrary.R.drawable.full_screen);
        FrameLayout.LayoutParams captionParams = (FrameLayout.LayoutParams) fullScreenLayout.getLayoutParams();
        captionParams.bottomMargin = (int) getResources().getDimension(R.dimen.fullscreen_margin_default);
        RelativeLayout.LayoutParams params1 = new RelativeLayout.LayoutParams(
                RelativeLayout.LayoutParams.WRAP_CONTENT,
                RelativeLayout.LayoutParams.WRAP_CONTENT
        );
        params1.setMargins(0, 0, 0, 0);
        backArrow.setLayoutParams(params1);
    }

    private void fullScreenMargin() {
        mFullScreenIcon.setBackgroundResource(com.make.brightcovelibrary.R.drawable.exit_full_screen);
        FrameLayout.LayoutParams captionParams = (FrameLayout.LayoutParams) fullScreenLayout.getLayoutParams();
        captionParams.bottomMargin = bottomMargin;
    }

    private void closeFullscreenDialog() {
        int orientation = getResources().getConfiguration().orientation;
        if (orientation == Configuration.ORIENTATION_LANDSCAPE) {
            mActivity.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_SENSOR_PORTRAIT);
            RelativeLayout.LayoutParams params1 = new RelativeLayout.LayoutParams(
                    RelativeLayout.LayoutParams.WRAP_CONTENT,
                    RelativeLayout.LayoutParams.WRAP_CONTENT
            );
            params1.setMargins(0, 0, 0, 0);
            backArrow.setLayoutParams(params1);
            resetFullScreenMargin();
        } else {
            mActivity.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_SENSOR_LANDSCAPE);
            RelativeLayout.LayoutParams params1 = new RelativeLayout.LayoutParams(
                    RelativeLayout.LayoutParams.WRAP_CONTENT,
                    RelativeLayout.LayoutParams.WRAP_CONTENT
            );
            params1.setMargins(0, 0, 0, 0);
            backArrow.setLayoutParams(params1);
            fullScreenMargin();
        }
    }

    private void initFullscreenDialog() {

        mFullScreenDialog = new Dialog(mActivity, android.R.style.Theme_Black_NoTitleBar_Fullscreen) {
            public void onBackPressed() {
                if (mExoPlayerFullscreen)
                    closeFullscreenDialog();
                super.onBackPressed();
            }
        };
    }


    @Override
    public void onDestroyView() {
        super.onDestroyView();
        if (player != null)
            player.release();
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        if (newConfig.orientation == Configuration.ORIENTATION_LANDSCAPE) {
            mActivity.getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
            bottomMargin = (int) getResources().getDimension(com.make.brightcovelibrary.R.dimen.caption_margin);
            mFullScreenIcon.setBackgroundResource(com.make.brightcovelibrary.R.drawable.exit_full_screen);

            FrameLayout.LayoutParams captionParams = (FrameLayout.LayoutParams) fullScreenLayout.getLayoutParams();
            captionParams.bottomMargin = (int) getResources().getDimension(R.dimen.live_full_screen_bottom);
            fullScreenLayout.setLayoutParams(captionParams);

            captionParams = (FrameLayout.LayoutParams) liveLayer.getLayoutParams();
            captionParams.bottomMargin = (int) getResources().getDimension(R.dimen.live_full_screen_bottom);
            liveLayer.setLayoutParams(captionParams);

            RelativeLayout.LayoutParams params1 = new RelativeLayout.LayoutParams(
                    RelativeLayout.LayoutParams.WRAP_CONTENT,
                    RelativeLayout.LayoutParams.WRAP_CONTENT
            );
            params1.topMargin= (int) getResources().getDimension(R.dimen.live_full_screen_bottom);
            backArrow.setLayoutParams(params1);

        } else if (newConfig.orientation == Configuration.ORIENTATION_PORTRAIT) {
            bottomMargin = (int) getResources().getDimension(com.make.brightcovelibrary.R.dimen.caption_margin);
            mActivity.getWindow().clearFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
            mFullScreenIcon.setBackgroundResource(com.make.brightcovelibrary.R.drawable.full_screen);

            FrameLayout.LayoutParams captionParams = (FrameLayout.LayoutParams) fullScreenLayout.getLayoutParams();
            captionParams.bottomMargin = (int) getResources().getDimension(R.dimen.fullscreen_margin_bottom);
            fullScreenLayout.setLayoutParams(captionParams);

            captionParams = (FrameLayout.LayoutParams) liveLayer.getLayoutParams();
            captionParams.bottomMargin = (int) getResources().getDimension(R.dimen.fullscreen_margin_bottom);
            liveLayer.setLayoutParams(captionParams);

            RelativeLayout.LayoutParams params1 = new RelativeLayout.LayoutParams(
                    RelativeLayout.LayoutParams.WRAP_CONTENT,
                    RelativeLayout.LayoutParams.WRAP_CONTENT
            );
            params1.topMargin=0;
            backArrow.setLayoutParams(params1);
        }
    }

    private BroadcastReceiver receiver;
    int count = 1;

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    public void onPictureInPictureModeChanged(boolean isInPictureInPictureMode) {
        super.onPictureInPictureModeChanged(isInPictureInPictureMode);
        this.isInPictureinPicture = isInPictureInPictureMode;
        if (isInPictureInPictureMode) {
            backArrow.setVisibility(View.GONE);
            mFullScreenIcon.setVisibility(View.GONE);
            pauseButton.setVisibility(View.GONE);
            mListener.isInPip(true);
            if (count == 1) {
                IntentFilter filter = new IntentFilter();
                filter.addAction("media_control");

                receiver = new BroadcastReceiver() {
                    @Override
                    public void onReceive(Context context,
                                          Intent intent) {
                        if (player != null && player.isPlaying()) {
                            pausePlayer();
                        } else {
                            startPlayer();
                        }
                    }
                };

                mActivity.registerReceiver(receiver, filter);
                count++;
            }


            if (playerView != null && player != null) {
                startPlayer();
            }
        } else {
            backArrow.setVisibility(View.VISIBLE);
            mFullScreenIcon.setVisibility(View.VISIBLE);
            pauseButton.setVisibility(View.VISIBLE);
            mListener.isInPip(false);
            if (receiver != null) {
                mActivity.unregisterReceiver(receiver);
                receiver = null;
                count = 1;
            }
        }
    }

    private static int REQUEST_CODE = 101;
    int iconId = 0;

    @RequiresApi(api = Build.VERSION_CODES.O)
    private void createPipAction() {

        final ArrayList<RemoteAction> actions = new ArrayList<>();

        Intent actionIntent =
                new Intent("media_control").putExtra("control_type", 1);

        if (player != null && player.isPlaying()) {
            REQUEST_CODE = 1;
        } else {
            REQUEST_CODE = 2;
        }

        final PendingIntent pendingIntent =
                PendingIntent.getBroadcast(mActivity,
                        REQUEST_CODE, actionIntent, 0);

        if (player != null && player.isPlaying()) {
            iconId = R.drawable.ic_pause_24dp;
        } else {
            iconId = R.drawable.ic_play_arrow_24dp;
        }
        final Icon icon =
                Icon.createWithResource(mActivity,
                        iconId);
        RemoteAction remoteAction = new RemoteAction(icon, "Info",
                "Video Info", pendingIntent);

        actions.add(remoteAction);

        PictureInPictureParams params =
                new PictureInPictureParams.Builder()
                        .setActions(actions)
                        .build();

        mActivity.setPictureInPictureParams(params);
    }

    ///////////////////////////////////////////////////////////////////////////////////////////////

    @Override
    public void onPause() {
        super.onPause();
        if (player != null) {
            pauseButton.setVisibility(View.VISIBLE);
            pauseButton.setBackgroundResource(com.make.brightcovelibrary.R.drawable.play);
            player.setPlayWhenReady(false);
        }
    }

    @Override
    public void onStart() {
        super.onStart();
        try {
            TelephonyManager mgr = (TelephonyManager) mActivity.getApplicationContext().getSystemService(TELEPHONY_SERVICE);
            if (mgr != null) {
                mgr.listen(PhoneStateHelper.getInstance(), PhoneStateListener.LISTEN_CALL_STATE);
            }
        } catch (Exception e) {
            Log.d("jhjgjgjg", e.toString());
        }
    }

    @Override
    public void onStop() {
        super.onStop();
        try {
            TelephonyManager mgr = (TelephonyManager) mActivity.getApplicationContext().getSystemService(TELEPHONY_SERVICE);
            if (mgr != null) {
                mgr.listen(PhoneStateHelper.getInstance(), PhoneStateListener.LISTEN_NONE);
            }

            if (mActivity != null) {
                mActivity.unregisterReceiver(headsetRecicer);
            }
        } catch (Exception ignored) {

        }
        pausePlayer();
        if (player != null)
            player.release();
    }

    @Override
    public void onResume() {
        super.onResume();
        pauseButton.setBackgroundResource(com.make.brightcovelibrary.R.drawable.play);
        requestAudioFocus();
        if (mActivity != null) {
            IntentFilter receiverFilter = new IntentFilter(Intent.ACTION_HEADSET_PLUG);
            mActivity.registerReceiver(headsetRecicer, receiverFilter);

        }
    }

    @Override
    public void onCallStateRinging() {
        if (player != null) {
            pausePlayer();
        }
    }

    @Override
    public void onCallStateIdle(int state) {
        if (player != null) {
            startPlayer();
        }
    }

    private void requestAudioFocus() {

        if (getActivity() == null) {
            return;
        }

        AudioManager mAudioManager = (AudioManager) getActivity().getSystemService(Context.AUDIO_SERVICE);
        AudioAttributes mAudioAttributes =
                null;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            mAudioAttributes = new AudioAttributes.Builder().setUsage(AudioAttributes.USAGE_MEDIA).setContentType(AudioAttributes.CONTENT_TYPE_MUSIC).build();
        }
        AudioFocusRequest mAudioFocusRequest = null;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            mAudioFocusRequest = new AudioFocusRequest.Builder(AudioManager.AUDIOFOCUS_GAIN)
                    .setAudioAttributes(mAudioAttributes)
                    .setAcceptsDelayedFocusGain(true)
                    .setOnAudioFocusChangeListener(new AudioManager.OnAudioFocusChangeListener() {
                        @Override
                        public void onAudioFocusChange(int i) {
                            if (i == AUDIOFOCUS_LOSS) {
                                pausePlayer();
                            }
                        }
                    }) // Need to implement listener
                    .build();
        }
        int focusRequest = 0;
        if (mAudioManager == null) {
            return;
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            focusRequest = mAudioManager.requestAudioFocus(mAudioFocusRequest);
        }

        switch (focusRequest) {
            case AudioManager.AUDIOFOCUS_REQUEST_FAILED:
                // don’t start playback
            case AudioManager.AUDIOFOCUS_REQUEST_GRANTED:
                // actually start playback
        }

    }

    private final BroadcastReceiver headsetRecicer = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            if (intent.getAction().equals(Intent.ACTION_HEADSET_PLUG)) {
                int headsetState = intent.getExtras().getInt("state");
                if (player != null && player.isPlaying() && headsetState == 0) {
                    pausePlayer();
                }
            }
        }
    };


    public void ShowAndHideView() {
        try {

            Animation animationFadeOut = AnimationUtils.loadAnimation(getActivity(), com.make.brightcovelibrary.R.anim.fade_out);
            Animation animationFadeIn = AnimationUtils.loadAnimation(getActivity(), com.make.brightcovelibrary.R.anim.fade_in);

            animationFadeIn.setAnimationListener(new Animation.AnimationListener() {
                @Override
                public void onAnimationStart(Animation animation) {

                }

                @Override
                public void onAnimationEnd(Animation animation) {
                }

                @Override
                public void onAnimationRepeat(Animation animation) {

                }
            });

            animationFadeOut.setAnimationListener(new Animation.AnimationListener() {
                @Override
                public void onAnimationStart(Animation animation) {

                }

                @Override
                public void onAnimationEnd(Animation animation) {

                }

                @Override
                public void onAnimationRepeat(Animation animation) {

                }
            });
            if (backArrowLay.getVisibility() == View.VISIBLE) {
                backArrowLay.startAnimation(animationFadeOut);
                live_lay.startAnimation(animationFadeOut);
                full_lay.startAnimation(animationFadeOut);

                backArrowLay.setVisibility(View.GONE);
                live_lay.setVisibility(View.GONE);
                full_lay.setVisibility(View.GONE);
                timer = true;

            } else {

                android.util.Log.w("IMATAG", "handler");
                backArrowLay.setVisibility(View.VISIBLE);
                live_lay.setVisibility(View.VISIBLE);
                full_lay.setVisibility(View.VISIBLE);

                backArrowLay.startAnimation(animationFadeIn);
                live_lay.startAnimation(animationFadeIn);
                full_lay.startAnimation(animationFadeIn);


                callHandler();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private Runnable viewHideShowRunnable;
    private Handler viewHideShowTimeHandler;
    private void callHandler() {
        timer = true;
        viewHideShowRunnable = () -> ShowAndHideView();

        viewHideShowTimeHandler = new Handler();
        viewHideShowTimeHandler.postDelayed(viewHideShowRunnable, 4000);
    }


}
