package com.make.brightcovelibrary.chromecast;

import android.content.Context;
import android.os.Handler;
import android.util.Log;
import android.view.MenuItem;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.mediarouter.app.MediaRouteButton;

import com.brightcove.player.model.PlaybackLocation;
import com.make.brightcovelibrary.R;
import com.google.android.exoplayer2.ui.DefaultTimeBar;
import com.google.android.gms.cast.MediaInfo;
import com.google.android.gms.cast.MediaStatus;
import com.google.android.gms.cast.framework.CastContext;
import com.google.android.gms.cast.framework.CastSession;
import com.google.android.gms.cast.framework.CastState;
import com.google.android.gms.cast.framework.CastStateListener;
import com.google.android.gms.cast.framework.IntroductoryOverlay;
import com.google.android.gms.cast.framework.SessionManagerListener;
import com.google.android.gms.cast.framework.media.RemoteMediaClient;

import java.util.Formatter;
import java.util.Locale;

public class ChromecastManager {
    private static ChromecastManager mInstance;
    private IntroductoryOverlay mIntroductoryOverlay;
    private SessionManagerListener<CastSession> mSessionManagerListener;
    private CastContext mCastContext;
    private MenuItem mediaRouteMenuItem;
    private PlaybackLocation mLocation;
    private CastSession mCastSession;
    private MediaInfo mSelectedMedia;
    private String vastTagUrl = "https://pubads.g.doubleclick.net/gampad/ads?slotname=/124319096/external/ad_rule_samples&sz=640x480&ciu_szs=300x250&unviewed_position_start=1&output=xml_vast3&impl=s&env=vp&gdfp_req=1&ad_rule=0&vad_type=linear&vpos=preroll&pod=1&ppos=1&lip=true&min_ad_duration=0&max_ad_duration=30000&vrid=5776&cust_params=deployment%3Ddevsite%26sample_ar%3Dpreonly&url=https://developers.google.com/interactive-media-ads/docs/sdks/html5/tags&video_doc_id=short_onecue&cmsid=496&kfa=0&tfcd=0";
    private Context mContext;
    private MediaRouteButton mMediaRouteButton;
    private AppCompatActivity playerActivity;
    private CastStateListener mCastStateListener;
    private ChromeCastCallback chromecastCallBack;
    private ChromecastStateCallback chromecastStateCallback;
    private boolean isOnceEnded = false;
    private RemoteMediaClient remoteMediaClient;
    DefaultTimeBar seekBar;
    TextView currentTime, totalDuration;
    private final Handler mHandler = new Handler();
    private boolean isChromecastConnected, isIntentCalled;
    ChromeCastConnectionListener chromeCastConnectionListener;
    public long runningPosition = 0;


    public synchronized static ChromecastManager getInstance() {
        if (mInstance == null)
            mInstance = new ChromecastManager();
        return mInstance;
    }

    public void init(Context context, AppCompatActivity playerActivitys, MediaRouteButton mediaRouteButton, CastContextAttachedListner castContextAttachedListner, ChromeCastConnectionListener connectionListener) {
        isChromecastConnected = false;
        isIntentCalled = true;

        mContext = context;
        mMediaRouteButton = mediaRouteButton;
        playerActivity = playerActivitys;
        chromeCastConnectionListener = connectionListener;

        mCastStateListener = newState -> {
            if (newState != CastState.NO_DEVICES_AVAILABLE) {
                Log.e("Devices A===>", "true");

                showIntroductoryOverlay(context);
                // mMediaRouteButton.setVisibility(View.VISIBLE);
            } else {
                Log.e("Devices A===>", "false");
                //  mMediaRouteButton.setVisibility(View.GONE);
                castContextAttachedListner.onDevicesUnavailable();

            }
        };
        setupCastListener();

        mCastContext = CastContext.getSharedInstance(context);
        mCastContext.getSessionManager().addSessionManagerListener(
                mSessionManagerListener, CastSession.class);

        castContextAttachedListner.onDevicesAvailable();
    }

    public CastContext getmCastContext(Context context) {
        return CastContext.getSharedInstance(context);
    }

    private void showIntroductoryOverlay(Context context) {
        if (mIntroductoryOverlay != null) {
            mIntroductoryOverlay.remove();
        }
        if ((mediaRouteMenuItem != null) && mediaRouteMenuItem.isVisible()) {
            new Handler().post(new Runnable() {
                @Override
                public void run() {
                    mIntroductoryOverlay = new IntroductoryOverlay.Builder(
                            playerActivity, mediaRouteMenuItem)
                            .setTitleText("Introducing Cast")
                            .setSingleTime()
                            .setOnOverlayDismissedListener(
                                    new IntroductoryOverlay.OnOverlayDismissedListener() {
                                        @Override
                                        public void onOverlayDismissed() {
                                            mIntroductoryOverlay = null;
                                        }
                                    })
                            .build();
                    mIntroductoryOverlay.show();
                }
            });
        }
    }

    public void setupCastListener() {
        mSessionManagerListener = new SessionManagerListener<CastSession>() {
            @Override
            public void onSessionEnded(CastSession session, int error) {
                Log.w("onSessionManager-->>", "onSessionEnded");
                onApplicationDisconnected();
                playerActivity.invalidateOptionsMenu();
            }

            @Override
            public void onSessionResumed(CastSession session, boolean wasSuspended) {
                Log.w("onSessionManager-->>", "onSessionResumed");
                onApplicationConnected(session);
                mCastSession = session;
                playerActivity.invalidateOptionsMenu();

            }

            @Override
            public void onSessionResumeFailed(CastSession session, int error) {
                Log.w("onSessionManager-->>", "onSessionResumeFailed");
                onApplicationDisconnected();
            }

            @Override
            public void onSessionStarted(CastSession session, String sessionId) {
                Log.w("onSessionManager-->>", "onSessionStarted");
                mCastSession = session;
                onApplicationConnected(session);
                playerActivity.invalidateOptionsMenu();

            }

            @Override
            public void onSessionStartFailed(CastSession session, int error) {
                Log.w("onSessionManager-->>", "onSessionStartFailed");
                onApplicationDisconnected();
                playerActivity.invalidateOptionsMenu();
            }

            @Override
            public void onSessionStarting(CastSession session) {
                Log.w("onSessionManager-->>", "onSessionStarting");
                if (chromecastCallBack != null)
                    chromecastCallBack.onChromeCastConnecting();
            }

            @Override
            public void onSessionEnding(CastSession session) {
                Log.w("onSessionManager-->>", "onSessionEnding");
                runningVideoId = "";
            }

            @Override
            public void onSessionResuming(CastSession session, String sessionId) {
            }

            @Override
            public void onSessionSuspended(CastSession session, int reason) {
                runningVideoId = "";
            }

            private void onApplicationConnected(CastSession castSession) {
                Log.w("onSessionManager-->>", "onApplicationConnected");
                mCastSession = castSession;
                if (chromecastCallBack != null) {
                    chromecastCallBack.onChromeCastConnected();
                }
                chromeCastConnectionListener.onSessionStarted();

            }

            private void onApplicationDisconnected() {
                Log.w("onSessionManager-->>", "onApplicationDisconnected");
                runningVideoId = "";
                updatePlaybackLocation(PlaybackLocation.LOCAL);
                mLocation = PlaybackLocation.LOCAL;
                isIntentCalled = true;

                if (chromecastCallBack != null)
                    chromecastCallBack.onChromeCastDisconnected();

                playerActivity.supportInvalidateOptionsMenu();
                chromeCastConnectionListener.onSessionFailed();


            }
        };
    }

    private void updatePlaybackLocation(PlaybackLocation location) {
        mLocation = location;
        if (mLocation == PlaybackLocation.REMOTE) {
            Log.e("Location", "Remote");
        } else {
            Log.e("Location", "Local");
        }
    }

    public void addCastListener() {
        if (mCastContext != null && mCastStateListener != null)
            mCastContext.addCastStateListener(mCastStateListener);

    }

    public void removeCastListener() {
        if (mCastContext != null && mCastStateListener != null)
            mCastContext.removeCastStateListener(mCastStateListener);
    }

    String runningVideoId = "";
    String videoID = "";

    public void loadRemoteMediaOtt(String videoId) {
        if (mCastSession == null) {
            Log.w("CastSession-->", mCastSession.getSessionId());
            return;
        } else {
            Log.w("CastSession-->>", mCastSession.toString());
            runningVideoId = videoId;
            remoteMediaClient = mCastSession.getRemoteMediaClient();
            if (remoteMediaClient != null) {
                Log.w("mediaClient-->>", remoteMediaClient.toString());
                Log.w("runningPosition-->>", remoteMediaClient.getApproximateStreamPosition() + " " + remoteMediaClient.getMediaInfo());
                if (remoteMediaClient.getApproximateStreamPosition() > 0) {
                    // Log.w("runningPosition-->>",remoteMediaClient.getApproximateStreamPosition()+" "+remoteMediaClient.getMediaInfo());
                }
                remoteMediaClient.addListener(mRemoteMediaClientListener);

                addListeners(remoteMediaClient);
            } else {
                Log.w("mediaClient-->", remoteMediaClient.toString());
            }
        }
    }

    public String getRunningVideoId() {
        return runningVideoId;
    }

    public RemoteMediaClient getRemoteMediaClient() {
        return remoteMediaClient;
    }

    public CastSession getmCastSession() {
        return mCastSession;
    }

    public RemoteMediaClient.Listener mRemoteMediaClientListener = new RemoteMediaClient.Listener() {
        @Override
        public void onStatusUpdated() {
            Log.w("Chromecast-->>", "onStatusUpdated");
            if (remoteMediaClient == null) {
                return;
            }
            Log.w("Chromecast-->>", "onStatusUpdated" + "  " + isIntentCalled);
            if (isIntentCalled) {
                isIntentCalled = false;
                if (chromecastCallBack != null) {
                    chromecastCallBack.onStatusUpdate();
                }

            }
            MediaStatus mediaStatus = remoteMediaClient.getMediaStatus();
            int mIdleReason = MediaStatus.IDLE_REASON_NONE;

            if (mediaStatus != null) {
                int playerStatus = mediaStatus.getPlayerState();
                // Log.d("PlayerState", "onStatusUpdated() called, progress= "+mSeekBar.getProgress() +", stream duration= "+ mRemoteMediaClient.getStreamDuration()+" mSeekBar.getProgress() == mRemoteMediaClient.getStreamDuration()="+(mSeekBar.getProgress() == mRemoteMediaClient.getStreamDuration()));
                Log.d("PlayerState", "onStatusUpdated() called playerStatus=" + playerStatus + ", idleReason=" + mediaStatus.getIdleReason());

                if (playerStatus == MediaStatus.PLAYER_STATE_PLAYING) {
                    mIdleReason = MediaStatus.IDLE_REASON_FINISHED;
                   /* exo_duration.setText(stringForTime(mediaClient.getStreamDuration())+"");
                    seekBar.setDuration(mediaClient.getStreamDuration());*/
                    Log.w("mediaClient-->>>>", remoteMediaClient.getStreamDuration() + "");
                   /* new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            mediaClient.pause();
                        }
                    },2000);*/
                    if (playPauseButton != null)
                        playPauseButton.setBackgroundResource(R.drawable.pause);
                    if (chromecastStateCallback != null) {
                        chromecastStateCallback.onPlaying();
                    }

                } else if (playerStatus == MediaStatus.PLAYER_STATE_BUFFERING) {
                    if (chromecastStateCallback != null) {
                        chromecastStateCallback.onBuffering();
                    }

                } else if (playerStatus == MediaStatus.PLAYER_STATE_PAUSED) {
                    playPauseButton.setBackgroundResource(R.drawable.play);
                } else if (playerStatus == MediaStatus.IDLE_REASON_INTERRUPTED) {

                } else if (playerStatus == MediaStatus.IDLE_REASON_ERROR) {

                } else if (playerStatus == MediaStatus.PLAYER_STATE_IDLE && mediaStatus.getIdleReason() == MediaStatus.IDLE_REASON_FINISHED && mIdleReason == MediaStatus.IDLE_REASON_FINISHED) {
                    Log.d("PlayerState-->>", "onStatusUpdated() called playerStatus=" + playerStatus + ", idleReason=" + mediaStatus.getIdleReason() + "  " + mIdleReason);
                }
            }


        }

        @Override
        public void onMetadataUpdated() {
            Log.d("", "onMetadataUpdated: ");
        }

        @Override
        public void onQueueStatusUpdated() {
            Log.d("", "onQueueStatusUpdated: ");
        }

        @Override
        public void onPreloadStatusUpdated() {
            Log.d("", "onPreloadStatusUpdated: ");
        }

        @Override
        public void onSendingRemoteMediaRequest() {
            Log.d("", "onSendingRemoteMediaRequest: ");
        }

        @Override
        public void onAdBreakStatusUpdated() {
            Log.d("", "onAdBreakStatusUpdated: ");
        }
    };

    private void addListeners(RemoteMediaClient mediaClient) {
        mediaClient.addProgressListener(new RemoteMediaClient.ProgressListener() {
            @Override
            public void onProgressUpdated(long l, long l1) {
                Log.w("onProgressUpdated", l + "   " + l1);
                if (mediaClient != null && mediaClient.isPlaying()) {
                    //   exo_position.setText(stringForTime(l));
                }
                //  seekBar.setPosition(l);
            }
        }, 0);


       /* PlayerActivity.this.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if (mediaClient.isPlaying())
                {
                    Log.w("isPlaying",mediaClient.isPlaying()+"");
                }else {
                    Log.w("isPlaying",mediaClient.isPlaying()+"");
                }
            }
        });*/

    }

    public void setChromecastStateCallBacks(ChromecastStateCallback chromecastStateCallback) {
        this.chromecastStateCallback = chromecastStateCallback;
    }

    ImageButton playPauseButton;

    public void setPlaybackDuration(TextView totalDuration, TextView currentPosition, DefaultTimeBar seekBar, ImageButton button) {
        this.seekBar = seekBar;
        this.currentTime = currentPosition;
        this.totalDuration = totalDuration;
        this.playPauseButton = button;
        if (remoteMediaClient != null) {


//            Handler handler =new Handler();
//            handler.postDelayed(new Runnable() {
//                @Override
//                public void run() {
//
//                    totalDuration.setText(totalTimeOfContent(remoteMediaClient.getStreamDuration()));
//                }
//            },3000);

            updateProgressBar();
        }
    }

    private void updateProgressBar() {
        if (mHandler != null)
            mHandler.postDelayed(updateTimeTask, 100);
    }

    private final Runnable updateTimeTask = new Runnable() {
        public void run() {


            String position = stringForTime(remoteMediaClient.getApproximateStreamPosition());
            currentTime.setText(position);
            totalDuration.setText(totalTimeOfContent(remoteMediaClient.getStreamDuration()));
            Log.w("chromacastposition", remoteMediaClient.getApproximateStreamPosition() + "");
            runningPosition = remoteMediaClient.getApproximateStreamPosition();
            seekBar.setPosition((int) remoteMediaClient.getApproximateStreamPosition());
            seekBar.setDuration((int) remoteMediaClient.getStreamDuration());


            mHandler.postDelayed(this, 100);


        }
    };


    public String stringForTime(long timeMs) {
        String timeValue = "";
        try {
            long totalSeconds = (timeMs + 500) / 1000;
            long seconds = totalSeconds % 60;
            long minutes = (totalSeconds / 60) % 60;
            long hours = totalSeconds / 3600;
            formatBuilder.setLength(0);
            timeValue = hours > 0 ? formatter.format("%d:%02d:%02d", hours, minutes, seconds).toString()
                    : formatter.format("%02d:%02d", minutes, seconds).toString();
        } catch (Exception ignored) {

        }

        return timeValue;
    }

    private StringBuilder formatBuilder;
    private Formatter formatter;

    private String totalTimeOfContent(long timeMs) {
        formatBuilder = new StringBuilder();
        formatter = new Formatter(formatBuilder, Locale.getDefault());
        long totalSeconds = (timeMs + 500) / 1000;
        long seconds = totalSeconds % 60;
        long minutes = (totalSeconds / 60) % 60;
        long hours = totalSeconds / 3600;
        formatBuilder.setLength(0);
        return hours > 0 ? formatter.format("%d:%02d:%02d", hours, minutes, seconds).toString()
                : formatter.format("%02d:%02d", minutes, seconds).toString();
    }

    public void playPauseToggle(ImageButton imageButton) {

        if (remoteMediaClient != null) {
            if (remoteMediaClient.isPlaying() || remoteMediaClient.isBuffering()) {
                remoteMediaClient.pause();
                imageButton.setBackgroundResource(R.drawable.play);
            } else if (remoteMediaClient.isPaused()) {
                remoteMediaClient.play();
                imageButton.setBackgroundResource(R.drawable.pause);
            }
        }
    }

    public void setCallBacks(ChromeCastCallback chromeCastCallback) {
        this.chromecastCallBack = chromeCastCallback;
    }

    public void remoteMediClientUpdate(long i) {
        remoteMediaClient.seek(i);
    }


    public boolean updatePlayingId(String videoId) {
        return false;
    }
}

