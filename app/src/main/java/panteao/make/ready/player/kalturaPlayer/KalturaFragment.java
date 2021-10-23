package panteao.make.ready.player.kalturaPlayer;

import static android.content.Context.TELEPHONY_SERVICE;
import static android.media.AudioManager.AUDIOFOCUS_LOSS;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.IntentFilter;
import android.content.pm.ActivityInfo;
import android.content.res.Configuration;
import android.media.AudioAttributes;
import android.media.AudioFocusRequest;
import android.media.AudioManager;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.constraintlayout.widget.ConstraintSet;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.CountDownTimer;
import android.os.Handler;
import android.os.Looper;
import android.provider.Settings;
import android.telephony.PhoneStateListener;
import android.telephony.TelephonyManager;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.kaltura.netkit.utils.ErrorElement;
import com.kaltura.playkit.PKEvent;
import com.kaltura.playkit.PKMediaEntry;
import com.kaltura.playkit.PlayerEvent;
import com.kaltura.playkit.PlayerState;
import com.kaltura.playkit.player.PKTracks;
import com.kaltura.playkit.player.VideoTrack;
import com.kaltura.tvplayer.KalturaOvpPlayer;
import com.kaltura.tvplayer.KalturaPlayer;
import com.kaltura.tvplayer.OVPMediaOptions;

import java.util.ArrayList;

import panteao.make.ready.R;
import panteao.make.ready.activities.show.ui.EpisodeActivity;
import panteao.make.ready.callbacks.commonCallbacks.NetworkChangeReceiver;
import panteao.make.ready.callbacks.commonCallbacks.PhoneListenerCallBack;
import panteao.make.ready.fragments.dialog.AlertDialogSingleButtonFragment;
import panteao.make.ready.player.BackPressCallBack;
import panteao.make.ready.player.PhoneStateListenerHelper;
import panteao.make.ready.utils.helpers.ToastHandler;
import panteao.make.ready.utils.helpers.ksPreferenceKeys.KsPreferenceKeys;
import panteao.make.ready.fragments.dialog.AlertDialogFragment;
import panteao.make.ready.fragments.player.ui.PlayerCallbacks;
import panteao.make.ready.fragments.player.ui.PlayerControlsFragment;
import panteao.make.ready.player.tracks.TracksItem;
import panteao.make.ready.utils.commonMethods.AppCommonMethod;
import panteao.make.ready.utils.constants.AppConstants;
import panteao.make.ready.utils.cropImage.helpers.Logger;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link KalturaFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class KalturaFragment extends Fragment implements PlayerCallbacks, PKEvent.Listener<PlayerEvent.StateChanged>, AlertDialogFragment.AlertDialogListener, PhoneListenerCallBack, NetworkChangeReceiver.ConnectivityReceiverListener, BackPressCallBack {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private OnPlayerInteractionListener mListener;
    private KalturaOvpPlayer player;
    private FrameLayout playerLayout,playerRootMain;
    private ConstraintLayout constraintMain;
    private Context mcontext;
    private View tracksSelectionMenu;
    private ProgressBar progressbar;
    private boolean isSelected = false;
    private boolean isDialogShowing = false;
    private PlayerCallbacks playerCallbacks;
    private Boolean skipIntroEnable = false;
    private boolean IsbingeWatch = false;
    private int bingeWatchTimer = 0;
    ArrayList<TracksItem> trackItemList = new ArrayList<TracksItem>();
    ArrayList<VideoTrack> videoTracks = new ArrayList<VideoTrack>();
    private PKTracks tracks;
    private String trackName = "";
    private String entryID = "";
    private int stopPosition = 0;
    private Dialog videodialog;
    private boolean showBingeWatchControls = false;
    private boolean isBingeWatchTimeCalculate = false;
    private int pos;
    private int bottomMargin = 0;
    private boolean isOfflineVideo = false;
    private PlayerControlsFragment playerControlsFragment;
    private Handler mHandler = new Handler();
    private ImageView play_pause;
    private AlertDialogSingleButtonFragment errorDialog;
    private NetworkChangeReceiver receiver = null;
    private boolean isFirstCalled = true;
    private boolean canPlay = false;
    private long bookmarkPosition = 0l;
    private boolean fromTrailor = false;
    private long playerCurrentPosition = 0l;
    private boolean fromOnstart = false;
    String typeofTVOD="";
    private String fromActivity = "";
    private boolean isBingeWatchRunning = false;

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    private FrameLayout container;
    CountDownTimer countDownTimer = new CountDownTimer(5000, 1000) {
        public void onTick(long millisUntilFinished) {
            Logger.e("TICKING", "TRUE");
        }

        public void onFinish() {
            Logger.e("TICKING", "FINISH");
            playerControlsFragment.hideControls();
            countDownTimer.start();
        }
    };

    public KalturaFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment KalturaFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static KalturaFragment newInstance(String param1, String param2) {
        KalturaFragment fragment = new KalturaFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }

        Bundle bundle = getArguments();
        if (bundle != null) {
            IsbingeWatch = bundle.getBoolean("binge_watch");
            bingeWatchTimer = bundle.getInt("binge_watch_timer");
            bookmarkPosition = bundle.getLong("bookmark_position");
            fromActivity = bundle.getString("from_chapter");
            if (bundle.getString("tvod_type")!=null){
                typeofTVOD = bundle.getString("tvod_type");
            }
            fromTrailor = bundle.getBoolean("from_trailor", false);
//            Log.d("gtgtgtgt",fromTrailor);
        }

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_kaltura, container, false);


        findViewById(view);
        player = AppCommonMethod.loadPlayer(getActivity(), playerLayout);
        setVideoQuality();
        callPlayerControlsFragment();
        startPlayer();
        setPlayerListner();
        performClick();
        return view;

    }

    private void performClick() {
        if (!AppCommonMethod.isTV(requireContext())) {
            playerLayout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (playerControlsFragment != null) {
                        playerControlsFragment.sendTapCallBack(true);
                    }
                }
            });
        }
    }

    private void callPlayerControlsFragment() {
        FragmentTransaction transaction = getChildFragmentManager().beginTransaction();
        if (playerControlsFragment == null) {
            try {

                playerControlsFragment = new PlayerControlsFragment();
                transaction.add(R.id.playerRoot, playerControlsFragment, "PlayerControlsFragment");
                transaction.addToBackStack(null);
                transaction.commit();
                playerControlsFragment.setPlayerCallBacks(this);
                playerControlsFragment.IsFromTrailor(fromTrailor);
//                if (fromTrailor.equalsIgnoreCase("true")){
//                    getActivity().setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
//                    playerControlsFragment.sendLandscapeCallback();
//                }else {
                int orientation = getResources().getConfiguration().orientation;
                if (orientation == Configuration.ORIENTATION_LANDSCAPE) {
                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            playerControlsFragment.sendLandscapeCallback();
                        }
                    }, 1500);

                } else {
                    playerControlsFragment.sendPortraitCallback();
                }
//                }
            } catch (Exception ignored) {

            }
        }
    }

    private void startPlayer() {
        if (player != null) {
            player.stop();
        }
        if (getArguments().containsKey(AppConstants.ENTRY_ID)) {
            entryID = getArguments().getString(AppConstants.ENTRY_ID);
        }
        OVPMediaOptions ovpMediaOptions = AppCommonMethod.buildOvpMediaOptions(entryID, 0L);
        player.loadMedia(ovpMediaOptions, new KalturaPlayer.OnEntryLoadListener() {
            @Override
            public void onEntryLoadComplete(PKMediaEntry entry, ErrorElement loadError) {
                if (loadError != null) {
                    new Handler(Looper.getMainLooper()).post(new Runnable() {
                        @Override
                        public void run() {
//                            Toast toast =  Toast.makeText(getActivity(), loadError.getMessage(), Toast.LENGTH_LONG);
//                            toast.show();
                            showErrorDialog(loadError.getMessage());
                        }
                    });

                } else {
                    Logger.d("OVPMedia onEntryLoadComplete  entry = ", entry.getId());
                }
            }
        });
    }

    private void setPlayerListner() {
        player.addListener(this, PlayerEvent.stateChanged, this::onEvent);
        player.addListener(this, PlayerEvent.playheadUpdated, new PKEvent.Listener<PlayerEvent.PlayheadUpdated>() {
            @Override
            public void onEvent(PlayerEvent.PlayheadUpdated event) {
                playerControlsFragment.setCurrentPosition((int) player.getCurrentPosition(), (int) player.getDuration());
                if (player.getCurrentPosition() >= 15000 && skipIntroEnable) {
                    playerControlsFragment.showControls();
                    playerControlsFragment.hideSkipIntro();
                    skipIntroEnable = false;
                } else {
                    if (skipIntroEnable) {
                        playerControlsFragment.showSkipButton();
                        playerControlsFragment.hideControls();
                    }
                }
                int currentPosition = (int) player.getCurrentPosition();
                Log.w("progressValuess", IsbingeWatch + " " + currentPosition + " " + bingeWatchTimer + "  " + player.getDuration());
                if (IsbingeWatch && bingeWatchTimer > 0) {

                    if (currentPosition >= bingeWatchTimer) {
                        showBingeWatchControls = true;
                        playerControlsFragment.showBingeWatch(player.getDuration() - player.getCurrentPosition(), isFirstCalled, totalEpisodes, runningEpisodes,fromActivity);
                        countDownTimer.cancel();
                    }
                }
            }
        });
        player.addListener(this, PlayerEvent.playing, new PKEvent.Listener() {
            @Override
            public void onEvent(PKEvent event) {

            }
        });

        player.addListener(this, PlayerEvent.loadedMetadata, new PKEvent.Listener() {
            @Override
            public void onEvent(PKEvent event) {
                if (player != null) {
                    if (KsPreferenceKeys.getInstance().getAppPrefLoginStatus()) {
                        if (fromOnstart){
                            player.seekTo(playerCurrentPosition);
                        }else {
                            player.seekTo(bookmarkPosition * 1000);
                        }
                    }else {
                        if (fromOnstart){
                            player.seekTo(playerCurrentPosition);
                        }
                    }
                }
            }
        });
        player.addListener(this, PlayerEvent.canPlay, new PKEvent.Listener() {
            @Override
            public void onEvent(PKEvent event) {
                playerLayout.setVisibility(View.VISIBLE);
                isFirstCalled = true;
                mListener.onPlayerStart();
                countDownTimer.start();
                playerControlsFragment.showControls();
                if (playerControlsFragment != null) {
                    if (!isBingeWatchTimeCalculate) {
                        int timeCalculation = (int) (player.getDuration() - bingeWatchTimer * 1000);
                        if (timeCalculation > bingeWatchTimer) {
                            isBingeWatchTimeCalculate = true;
                            bingeWatchTimer = (int) (player.getDuration() - bingeWatchTimer * 1000);
                        }
                    }

                }
                canPlay = true;
                bookmarking(player);
            }
        });
        player.addListener(this, PlayerEvent.ended, new PKEvent.Listener() {
            @Override
            public void onEvent(PKEvent event) {
                if (playerControlsFragment != null) {
                    if (!IsbingeWatch) {
                        //  player.stop();
                        showBingeWatchControls = false;
                        //  playerControlsFragment.hideControls();
                        countDownTimer.cancel();
                        new Handler().postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                playerControlsFragment.showReplayVisibility();
                            }
                        }, 1000);

                    }
                    if (mListener != null) {
                        mListener.onBookmarkFinish();
                    }
                }
//                if (mHandler != null) {
//                    finishPlayer();
//                }
                if (IsbingeWatch) {
                    if (totalEpisodes == runningEpisodes) {
                        //  player.stop();
                        showBingeWatchControls = false;
                        // playerControlsFragment.hideControls();
                        countDownTimer.cancel();
                        new Handler().postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                playerControlsFragment.showReplayVisibility();
                            }
                        }, 1200);
                        // playerControlsFragment.showReplayVisibility();

                    } else {
                        player.stop();
                        isFirstCalled = true;
                        isBingeWatchTimeCalculate = false;
                        playerControlsFragment.hideBingeWatch();
                        mListener.bingeWatchCall(entryID);
                    }
                }

            }
        });
        player.addListener(this, PlayerEvent.tracksAvailable, new PKEvent.Listener<PlayerEvent.TracksAvailable>() {
            @Override
            public void onEvent(PlayerEvent.TracksAvailable event) {
                PlayerEvent.TracksAvailable tracksAvailable = (PlayerEvent.TracksAvailable) event;
                tracks = tracksAvailable.tracksInfo;
            }
        });

        player.addListener(this, PlayerEvent.videoTrackChanged, new PKEvent.Listener<PlayerEvent.VideoTrackChanged>() {
            @Override
            public void onEvent(PlayerEvent.VideoTrackChanged event) {

            }
        });

    }

    private void findViewById(View view) {

        playerLayout = (FrameLayout) view.findViewById(R.id.playerRoot);
        playerRootMain = (FrameLayout) view.findViewById(R.id.playerRootMain);
        constraintMain = (ConstraintLayout) view.findViewById(R.id.constraintMain);
        progressbar = (ProgressBar) view.findViewById(R.id.pBar);
        container = view.findViewById(R.id.container);
        playerLayout.setVisibility(View.GONE);
        progressbar.setVisibility(View.VISIBLE);
    }

    @Override
    public void onDetach() {
        super.onDetach();


    }

    @Override
    public void onEvent(PlayerEvent.StateChanged event) {

        if (event.newState == PlayerState.READY) {

//            getBinding().playerImage.setVisibility(View.GONE);
            if (getActivity()!=null && !getActivity().isFinishing()){
                if (android.provider.Settings.System.getInt(getActivity().getContentResolver(), Settings.System.ACCELEROMETER_ROTATION, 0) == 1) {
                    getActivity().setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_SENSOR);
                }

            }
            progressbar.setVisibility(View.GONE);
        } else if (event.newState == PlayerState.BUFFERING) {
            progressbar.setVisibility(View.VISIBLE);
        } else if (event.newState == PlayerState.LOADING) {
        }

        Logger.e("PLAYER_STATE", "State changed from " + event.oldState + " to " + event.newState);
    }

    @Override
    public void playPause(ImageView id) {
        if (player != null) {
            if (player.isPlaying()) {
                countDownTimer.cancel();
                if (AppCommonMethod.isTV(requireActivity()))
                    id.setImageDrawable(requireActivity().getDrawable(R.drawable.exo_icon_pause));
                else
                    id.setImageDrawable(requireActivity().getDrawable(R.drawable.ic_baseline_play_arrow_24));
                player.pause();
                pausedFromBtn=true;
            } else {
                countDownTimer.start();
                if (AppCommonMethod.isTV(requireActivity()))
                    id.setImageDrawable(requireActivity().getDrawable(R.drawable.exo_icon_play));
                else
                    id.setImageDrawable(requireActivity().getDrawable(R.drawable.ic_baseline_pause_24));
                player.play();
                pausedFromBtn=false;
            }
        }
    }

    @Override
    public void Forward() {
        if (player != null) {
            player.seekTo(player.getCurrentPosition() + 10000);
            if (playerControlsFragment != null) {
                playerControlsFragment.sendPlayerCurrentPosition((int) player.getCurrentPosition());
            }
        }

    }

    @Override
    public void Rewind() {
        if (player != null) {
            player.seekTo(player.getCurrentPosition() - 10000);
            if (playerControlsFragment != null) {
                playerControlsFragment.sendPlayerCurrentPosition((int) player.getCurrentPosition());
            }
        }

    }

    Configuration currentConfig = null;

    @Override
    public void onConfigurationChanged(Configuration newConfig) {

        super.onConfigurationChanged(newConfig);
            int orientation = getActivity().getResources().getConfiguration().orientation;
            Log.w("_orientation-->>",orientation+"");
            if (orientation == Configuration.ORIENTATION_LANDSCAPE) {
                ConstraintSet constraintSet = new ConstraintSet();
                constraintSet.clone(constraintMain);
                constraintSet.setDimensionRatio(playerRootMain.getId(), heightRatio+":"+widthRatio);
                constraintSet.applyTo(constraintMain);
            }else {
                ConstraintSet constraintSet = new ConstraintSet();
                constraintSet.clone(constraintMain);
                constraintSet.setDimensionRatio(playerRootMain.getId(), "16:9");
                constraintSet.applyTo(constraintMain);
            }

        if (newConfig.orientation == Configuration.ORIENTATION_LANDSCAPE) {
            getActivity().getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
            if (playerControlsFragment != null) {
                playerControlsFragment.sendLandscapeCallback();
            }
            currentConfig = newConfig;
        } else if (newConfig.orientation == Configuration.ORIENTATION_PORTRAIT) {
            if (playerControlsFragment != null) {
                playerControlsFragment.sendPortraitCallback();
            }
            getActivity().getWindow().clearFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
            currentConfig = newConfig;
            playerLayout.setPadding(0, 0, 0, 0);
        }


    }


    @Override
    public void finishPlayer() {

        int orientation = getResources().getConfiguration().orientation;
        if (orientation == Configuration.ORIENTATION_LANDSCAPE) {
            getActivity().setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_SENSOR_PORTRAIT);
        } else {

            if (player != null) {
                player.stop();
                player.destroy();
            }
        }
    }


    @Override
    public void skipIntro() {
        player.seekTo(15000);
        playerControlsFragment.hideSkipIntro();
    }

    int totalEpisodes = 0;

    public void totalEpisodes(int size) {
        totalEpisodes = size;
    }

    int runningEpisodes = 0;

    public void currentEpisodes(int i) {
        runningEpisodes = i;
//        Log.w("totalZies", totalEpisodes + " " + runningEpisodes);
//        if (runningEpisodes < totalEpisodes) {
//            IsbingeWatch = true;
//        } else {
//            IsbingeWatch = false;
//        }
    }

    public void bingeWatchStatus(boolean b) {
        IsbingeWatch = b;
    }

    @Override
    public void bingeWatch() {
        if (playerControlsFragment != null) {
            player.stop();
            player.destroy();
            showBingeWatchControls = false;
            playerControlsFragment.hideControls();
        }
        isBingeWatchTimeCalculate = false;
        isFirstCalled = true;
        mListener.bingeWatchCall(entryID);
    }

    boolean fullScreenFromBtnClick=false;
    @Override
    public void checkOrientation(ImageView id) {
        //  Log.d("gtgtgtgtg",fromTrailor);
        fullScreenFromBtnClick=true;
        if (fromTrailor) {
            if (player != null) {
                player.stop();
                player.destroy();
            }
            getActivity().finish();
        } else {

            FrameLayout.LayoutParams captionParams = (FrameLayout.LayoutParams) playerLayout.getLayoutParams();
            captionParams.bottomMargin = (int) 0;
            captionParams.topMargin = (int) 0;
            playerLayout.setLayoutParams(captionParams);
            if (id.getId() == R.id.backArrow) {
                _isOrientation(1, id);
            } else {
                int orientation = getActivity().getResources().getConfiguration().orientation;
                if (orientation == Configuration.ORIENTATION_LANDSCAPE) {
                    getActivity().setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_SENSOR_PORTRAIT);
                    id.setBackgroundResource(R.drawable.full_screen);
                    ConstraintSet constraintSet = new ConstraintSet();
                    constraintSet.clone(constraintMain);
                    constraintSet.setDimensionRatio(playerRootMain.getId(), "16:9");
                    constraintSet.applyTo(constraintMain);
                } else {
                    getActivity().setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_SENSOR_LANDSCAPE);
                    id.setBackgroundResource(R.drawable.exit_full_screen);
                    ConstraintSet constraintSet = new ConstraintSet();
                    constraintSet.clone(constraintMain);
                    constraintSet.setDimensionRatio(playerRootMain.getId(), heightRatio+":"+widthRatio);
                    constraintSet.applyTo(constraintMain);

                }

            }
        }
    }

    private void _isOrientation(int i, ImageView id) {
        int orientation = getActivity().getResources().getConfiguration().orientation;

        {
            if (orientation == Configuration.ORIENTATION_PORTRAIT) {
                if (player!=null){
                    player.stop();
                    player.destroy();
                }
                getActivity().finish();
            } else {
                getActivity().setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_SENSOR_PORTRAIT);
            }
        }
    }

    @Override
    public void replay() {
        if (player != null) {
            player.replay();
            countDownTimer.start();
            playerControlsFragment.showControls();
        }
    }

    @Override
    public void IsBingeWatchRunning(boolean b) {
        this.isBingeWatchRunning = b;
    }

    @Override
    public void QualitySettings() {

        chooseVideoquality();
    }

    private void chooseVideoquality() {
        trackItemList.clear();
        if (tracks.getVideoTracks().size() > 0) {
            trackItemList = AppCommonMethod.createTrackList(tracks, getActivity(),typeofTVOD);
        } else {
            ToastHandler.show(getActivity().getResources().getString(R.string.no_tracks_available), getActivity());
        }

        final RecyclerView recycleview;
        videodialog = new Dialog(getActivity());
        videodialog.setContentView(R.layout.list_layout);
        videodialog.setTitle(getString(R.string.title_video_quality));
        recycleview = videodialog.findViewById(R.id.recycler_view_quality);
        Button closeButton = videodialog.findViewById(R.id.close);
        closeButton.setOnClickListener(v -> {
            videodialog.cancel();
            isDialogShowing = false;
        });
        if (trackItemList.size() > 0) {
            VideoTracksAdapter trackItemAdapter = new VideoTracksAdapter(trackItemList, videodialog);
            recycleview.setAdapter(trackItemAdapter);
            recycleview.setLayoutManager(new LinearLayoutManager(getActivity()));
            videodialog.show();
            isDialogShowing = true;
            //trackItemAdapter.notifyDataSetChanged();
        } else {
            ToastHandler.show(getActivity().getResources().getString(R.string.no_tracks_available), getActivity());
        }


    }

    private void setVideoQuality() {
        String selectedTrack = KsPreferenceKeys.getInstance().getQualityName();
        if (!TextUtils.isEmpty(selectedTrack)) {
            trackName = selectedTrack;
        }
    }


    @Override
    public void SeekbarLastPosition(long position) {
        if (player != null) {
            player.seekTo((int) position);
        }

    }

    @Override
    public void showPlayerController(boolean isVisible) {

    }

    @Override
    public void sendPlayPauseId(ImageView id) {
        this.play_pause = id;
    }

    @Override
    public void changeBitRateRequest(String title, int position) {

    }

    @Override
    public void bitRateRequest() {
    }

    @Override
    public void onFinishDialog() {

    }

    public void skipIntroStatus(boolean skipIntro) {
        skipIntroEnable = skipIntro;
    }

    public void onKeyDown(int keyCode) {
        countDownTimer.cancel();
        countDownTimer.start();
        if (playerControlsFragment != null) {
            playerControlsFragment.onKeyDown(keyCode);
        } else {
            player.stop();
            player.destroy();
            requireActivity().finish();
        }
    }

    @Override
    public void onCallStateRinging() {
        if (player != null) {
            player.pause();
        }

    }

    @Override
    public void onCallStateIdle(int state) {
        if (player != null) {
            player.play();
        }
    }

    @Override
    public void onNetworkConnectionChanged(boolean isConnected) {
        if (!isConnected) {
            if (player != null) {
                player.pause();
                showErrorDialog(getActivity().getResources().getString(R.string.no_internet));
            }
        }
    }

    private void showErrorDialog(String message) {
        playerControlsFragment.hideControls();
        FragmentManager fm = getActivity().getSupportFragmentManager();
        errorDialog = AlertDialogSingleButtonFragment.newInstance("", message, getResources().getString(R.string.ok));
        errorDialog.setCancelable(false);

        errorDialog.setAlertDialogCallBack(new AlertDialogFragment.AlertDialogListener() {
            @Override
            public void onFinishDialog() {
                if (player != null) {
                    player.stop();
                    player.destroy();
                    requireActivity().finish();
                }
            }
        });

        errorDialog.show(fm, "fragment_alert");
    }

    @Override
    public void BackPressClicked(int value) {
        checkBackButtonOrientation(value);

    }

    private void checkBackButtonOrientation(int value) {
        if (fromTrailor) {
            if (player != null) {
                player.stop();
                player.destroy();
            }
            getActivity().finish();
        } else {
            FrameLayout.LayoutParams captionParams = (FrameLayout.LayoutParams) container.getLayoutParams();
            captionParams.bottomMargin = (int) 0;
            captionParams.topMargin = (int) 0;
            container.setLayoutParams(captionParams);
            if (value == 2) {
                getActivity().setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_SENSOR_PORTRAIT);
            } else {
                if (player != null) {
                    player.stop();
                    player.destroy();
                    finishPlayer();
                    getActivity().finish();
                }
            }
        }
    }

    public void stopPlayback() {
        try {
            if (player != null) {
            player.stop();
            player.destroy();
           // finishPlayer();
        }
        }catch (Exception exception){
            Log.e("ErrorIs",exception.getMessage());
        }
    }

    public void pausePlayer() {
        if (player!=null){
            mListener.onCurrentPosition(player.getCurrentPosition());
            player.stop();
            player.destroy();
        }
    }

    public void passCurrentPosition(long playerCurrentPosition, boolean fromOnStart) {
        this.playerCurrentPosition = playerCurrentPosition;
        this.fromOnstart = fromOnStart;
    }

    public void checkPlayerState(Activity episodeActivity, int i) {
        try {
            if (player!=null){
                if (i==1){
                    if (player!=null && player.isPlaying()){
                        player.pause();
                    }
                }else {
                    if (!pausedFromBtn && player!=null){
                        player.play();
                        pausedFromBtn=false;
                    }
                    if (getActivity()!=null && !getActivity().isFinishing()){
                        if (android.provider.Settings.System.getInt(getActivity().getContentResolver(), Settings.System.ACCELEROMETER_ROTATION, 0) == 1) {
                            getActivity().setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_SENSOR);
                        }else {
                            int orientation = getActivity().getResources().getConfiguration().orientation;
                            if (orientation == Configuration.ORIENTATION_LANDSCAPE) {
                                getActivity().setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_SENSOR_LANDSCAPE);
                            }else {
                                getActivity().setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_SENSOR_PORTRAIT);
                            }
                        }

                    }
                }

            }
        }catch (Exception ignored){

        }
    }


    public interface OnPlayerInteractionListener {
        void bingeWatchCall(String entryID);

        void onPlayerStart();

        void onBookmarkCall(int currentPosition);

        void onBookmarkFinish();

        void onCurrentPosition(long currentPosition);
    }

    boolean pausedFromBtn=false;
    @Override
    public void onPause() {
        super.onPause();
        if (receiver != null) {
            getActivity().unregisterReceiver(receiver);
            if (NetworkChangeReceiver.connectivityReceiverListener != null)
                NetworkChangeReceiver.connectivityReceiverListener = null;
        }

        if (player != null) {
            stopPosition = (int) player.getCurrentPosition();
            if (play_pause != null) {
                countDownTimer.cancel();
                if (AppCommonMethod.isTV(requireActivity()))
                    play_pause.setImageDrawable(requireActivity().getDrawable(R.drawable.exo_icon_pause));
                else
                    play_pause.setImageDrawable(requireActivity().getDrawable(R.drawable.ic_baseline_play_arrow_24));
            }
            pausedFromBtn=true;
            player.pause();
            Log.d("ftyshhdh","EnterOnPause");

        }
        if (handler != null) {
            handler.removeCallbacksAndMessages(runnable);
            handler = null;
        }

        if (isBingeWatchRunning){
            if (playerControlsFragment!=null){
                playerControlsFragment.stopTimer();
            }
        }
    }

    @Override
    public void onResume() {

        if (player != null) {
            Log.d("ftyshhdh","EnterResume");
            player.seekTo(stopPosition);
            if (play_pause != null) {

                countDownTimer.start();
                Log.d("ftyshhdh","EnterResume1");
                if (AppCommonMethod.isTV(requireActivity()))
                    play_pause.setImageDrawable(requireActivity().getDrawable(R.drawable.exo_icon_play));
                else
                    play_pause.setImageDrawable(requireActivity().getDrawable(R.drawable.ic_baseline_pause_24));
            }
            player.play();


        }
        super.onResume();
        requestAudioFocus();
        setBroadcast();
        if (canPlay == true && handler == null) {
            bookmarking(player);
        }
    }


    private void setBroadcast() {
        receiver = new NetworkChangeReceiver();
        IntentFilter filter = new IntentFilter();
        filter.addAction("android.net.conn.CONNECTIVITY_CHANGE");
        filter.addAction("android.net.wifi.WIFI_STATE_CHANGED");
        filter.addAction("android.net.wifi.STATE_CHANGE");
        getActivity().registerReceiver(receiver, filter);
        setConnectivityListener(this);
    }

    private void setConnectivityListener(NetworkChangeReceiver.ConnectivityReceiverListener listener) {
        NetworkChangeReceiver.connectivityReceiverListener = listener;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    int widthRatio=9;
    int heightRatio=16;
    @Override
    public void onActivityCreated(@Nullable @org.jetbrains.annotations.Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        try {
            mListener = (OnPlayerInteractionListener) getActivity();


        } catch (Exception ignored) {
            ignored.printStackTrace();
        }
        try {
            DisplayMetrics metrics = getActivity().getResources().getDisplayMetrics();
            float ratio = ((float)metrics.heightPixels / (float)metrics.widthPixels);

            int factor = greatestCommonFactor(metrics.widthPixels, metrics.heightPixels);

            widthRatio = metrics.widthPixels / factor;
            heightRatio = metrics.heightPixels / factor;

            Log.w("phoneration-->>",heightRatio+":"+widthRatio);
            Log.w("phoneration-->>",ratio+"");
        }catch (Exception e){

        }

    }

    public int greatestCommonFactor(int width, int height) {
        return (height == 0) ? width : greatestCommonFactor(height, width % height);
    }

    static class ViewHolder1 extends RecyclerView.ViewHolder {

        ImageView tick;
        private TextView qualityText;

        private ViewHolder1(View itemView) {
            super(itemView);
            tick = itemView.findViewById(R.id.tick_image);
            qualityText = itemView.findViewById(R.id.tvTrackName);

        }
    }

    class VideoTracksAdapter extends RecyclerView.Adapter<ViewHolder1> {
        final ArrayList<TracksItem> tracks;
        final Dialog videoDialog;


        private VideoTracksAdapter(ArrayList<TracksItem> videoTracks, Dialog videodialog) {
            this.tracks = videoTracks;
            this.videoDialog = videodialog;


        }

        @NonNull
        @Override
        public ViewHolder1 onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.tracks_item_list_row, parent, false);
            return new ViewHolder1(view);
        }

        @Override
        public void onBindViewHolder(@NonNull final ViewHolder1 holder, @SuppressLint("RecyclerView") final int position) {

            if (trackName.equalsIgnoreCase(trackItemList.get(position).getTrackName())) {
                holder.tick.setBackgroundResource(R.drawable.tick);
            } else {
                holder.tick.setBackgroundResource(0);
            }
            holder.qualityText.setText(tracks.get(position).getTrackName());
            holder.qualityText.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

//                    KsPreferenceKeys.getInstance().setQualityPosition(position);
//                    KsPreferenceKeys.getInstance().setQualityName(trackItemList.get(position).getTrackName());

                    trackName = trackItemList.get(position).getTrackName();
                    player.changeTrack(tracks.get(position).getUniqueId());
                    if (videoDialog != null) {
                        videodialog.cancel();
                    }

                    notifyDataSetChanged();

                }
            });


        }

        @Override
        public int getItemCount() {
            return trackItemList.size();
        }
    }

    @Override
    public void onStop() {
        super.onStop();
        countDownTimer.cancel();

        TelephonyManager mgr = (TelephonyManager) getActivity().getApplicationContext().getSystemService(TELEPHONY_SERVICE);
        if (mgr != null) {
            mgr.listen(PhoneStateListenerHelper.getInstance(this), PhoneStateListener.LISTEN_NONE);
        }
        Log.d("ftyshhdh","EnterOnStop");
        if (handler != null) {
            handler.removeCallbacksAndMessages(runnable);
            handler = null;
        }
    }

    @Override
    public void onStart() {
        super.onStart();
        if (isBingeWatchRunning){
            if (player!=null){
                playerControlsFragment.setBingeTrue(true);
            }

        }

        try {
            TelephonyManager mgr = (TelephonyManager) getActivity().getApplicationContext().getSystemService(TELEPHONY_SERVICE);
            if (mgr != null) {
                mgr.listen(PhoneStateListenerHelper.getInstance(this), PhoneStateListener.LISTEN_CALL_STATE);
            }
        } catch (Exception e) {
        }
    }

    private void requestAudioFocus() {

        AudioManager mAudioManager = (AudioManager) getActivity().getSystemService(Context.AUDIO_SERVICE);
        AudioAttributes mAudioAttributes =
                null;
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP) {
            mAudioAttributes = new AudioAttributes.Builder()
                    .setUsage(AudioAttributes.USAGE_MEDIA)
                    .setContentType(AudioAttributes.CONTENT_TYPE_MUSIC)
                    .build();
        }
        AudioFocusRequest mAudioFocusRequest =
                null;
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            mAudioFocusRequest = new AudioFocusRequest.Builder(AudioManager.AUDIOFOCUS_GAIN)
                    .setAudioAttributes(mAudioAttributes)
                    .setAcceptsDelayedFocusGain(true)
                    .setOnAudioFocusChangeListener(new AudioManager.OnAudioFocusChangeListener() {
                        @Override
                        public void onAudioFocusChange(int i) {
                            if (i == AUDIOFOCUS_LOSS) {
//                                if (player!=null) {
//                                    player.pause();
//                                }
                            }
                        }
                    }) // Need to implement listener
                    .build();
        }
        int focusRequest = 0;
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            focusRequest = mAudioManager.requestAudioFocus(mAudioFocusRequest);
        }
        switch (focusRequest) {
            case AudioManager.AUDIOFOCUS_REQUEST_FAILED:
                // don’t start playback
            case AudioManager.AUDIOFOCUS_REQUEST_GRANTED:
                // actually start playback
        }


    }



    private Handler handler;
    private Runnable runnable;

    public void bookmarking(KalturaOvpPlayer player) {
        handler = new Handler();
        runnable = new Runnable() {
            @Override
            public void run() {
                try {
                    if (player != null) {
                        double totalDuration = player.getDuration();
                        double currentPosition = player.getCurrentPosition();
                        double percentagePlayed = ((currentPosition / totalDuration) * 100L);
                        if (percentagePlayed > 10 && percentagePlayed <= 95) {
                            if (mListener != null) {
                                mListener = (OnPlayerInteractionListener) getActivity();
                                mListener.onBookmarkCall((int) player.getCurrentPosition());
                            }
                            if (handler != null) {
                                handler.postDelayed(this, 10000);
                            }
                        } else if (percentagePlayed > 95) {
                            if (mListener != null) {
                                mListener = (OnPlayerInteractionListener) getActivity();
                                mListener.onBookmarkFinish();
                            }
                            Log.d("PercentagePlayed", percentagePlayed + "");
                        } else {
                            if (handler != null) {
                                handler.postDelayed(this, 10000);
                            }
                        }
                    }
                } catch (Exception e) {

                }

            }
        };
        handler.postDelayed(runnable, 10000);
    }

}