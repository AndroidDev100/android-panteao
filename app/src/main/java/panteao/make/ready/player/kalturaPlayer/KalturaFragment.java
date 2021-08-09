package panteao.make.ready.player.kalturaPlayer;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Context;
import android.content.pm.ActivityInfo;
import android.content.res.Configuration;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.LiveData;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.CountDownTimer;
import android.os.Handler;
import android.os.Looper;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

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
public class KalturaFragment extends Fragment implements PlayerCallbacks, PKEvent.Listener<PlayerEvent.StateChanged>, AlertDialogFragment.AlertDialogListener {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private OnPlayerInteractionListener mListener;
    private KalturaOvpPlayer player;
    private FrameLayout playerLayout;
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
        }

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_kaltura, container, false);


        findViewById(view);
        player = AppCommonMethod.loadPlayer(getActivity(), playerLayout);
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
                            Toast toast =  Toast.makeText(getActivity(), loadError.getMessage(), Toast.LENGTH_LONG);
                            toast.show();
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
                if (IsbingeWatch && bingeWatchTimer > 0) {
                    int currentPosition = (int) player.getCurrentPosition();
                    if (currentPosition >= bingeWatchTimer) {
                        showBingeWatchControls = true;
                        playerControlsFragment.showBingeWatch();
                        countDownTimer.cancel();
                    }
                }
            }
        });
        player.addListener(this, PlayerEvent.playing, new PKEvent.Listener() {
            @Override
            public void onEvent(PKEvent event) {
                if (playerControlsFragment != null) {

                    if (!isBingeWatchTimeCalculate) {
                        int timeCalculation = (int) (player.getDuration() - bingeWatchTimer * 1000);
                        if (timeCalculation > bingeWatchTimer) {
                            isBingeWatchTimeCalculate = true;
                            bingeWatchTimer = (int) (player.getDuration() - bingeWatchTimer * 1000);
                        }
                    }

                }
            }
        });
        player.addListener(this, PlayerEvent.canPlay, new PKEvent.Listener() {
            @Override
            public void onEvent(PKEvent event) {
                playerLayout.setVisibility(View.VISIBLE);
                mListener.onPlayerStart();
                countDownTimer.start();
                playerControlsFragment.showControls();
            }
        });
        player.addListener(this, PlayerEvent.ended, new PKEvent.Listener() {
            @Override
            public void onEvent(PKEvent event) {
                if (playerControlsFragment != null) {
                    player.stop();
                    showBingeWatchControls = false;
                    playerControlsFragment.hideControls();
                    if (playerControlsFragment.bingeBtn.getVisibility() == View.VISIBLE) {
                        playerControlsFragment.backArrow.setVisibility(View.VISIBLE);
                    }
                }
                if (mHandler != null) {
                    finishPlayer();
                }
                if (IsbingeWatch) {
                    isBingeWatchTimeCalculate = false;
                    mListener.bingeWatchCall(entryID);
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
            } else {
                countDownTimer.start();
                if (AppCommonMethod.isTV(requireActivity()))
                    id.setImageDrawable(requireActivity().getDrawable(R.drawable.exo_icon_play));
                else
                    id.setImageDrawable(requireActivity().getDrawable(R.drawable.ic_baseline_pause_24));
                player.play();
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
        Log.d("playyyyy", "for");

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
        mListener.bingeWatchCall(entryID);
    }

    @Override
    public void checkOrientation(ImageView id) {
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
            } else {
                getActivity().setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_SENSOR_LANDSCAPE);
                id.setBackgroundResource(R.drawable.exit_full_screen);
            }

        }
    }

    private void _isOrientation(int i, ImageView id) {
        int orientation = getActivity().getResources().getConfiguration().orientation;

        {
            if (orientation == Configuration.ORIENTATION_PORTRAIT) {
                getActivity().finish();
            } else {
                getActivity().setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_SENSOR_PORTRAIT);
            }
        }
    }

    @Override
    public void replay() {

    }

    @Override
    public void QualitySettings() {
        isDialogShowing = true;
        chooseVideoquality();
    }

    private void chooseVideoquality() {
        final RecyclerView recycleview;
        videodialog = new Dialog(getActivity(), R.style.AppAlertTheme);
        videodialog.setContentView(R.layout.list_layout);
        videodialog.setTitle(getString(R.string.title_video_quality));
        int width = (int) (getResources().getDisplayMetrics().widthPixels * 0.47);
        int height = (int) (getResources().getDisplayMetrics().heightPixels * 0.65);
        videodialog.getWindow().setLayout(width, height);
        videodialog.show();
        recycleview = videodialog.findViewById(R.id.recycler_view_quality);
        Button closeButton = videodialog.findViewById(R.id.close);
        closeButton.setOnClickListener(v -> videodialog.cancel());
        if (recycleview != null) {
            VideoTracksAdapter trackItemAdapter = new VideoTracksAdapter(trackItemList);
            recycleview.setAdapter(trackItemAdapter);
            recycleview.setLayoutManager(new LinearLayoutManager(getActivity()));
            trackItemAdapter.notifyDataSetChanged();
        } else {
//            ToastHandler.show(getActivity().getResources().getString(R.string.no_tracks_available), getActivity());
        }
        trackItemList.clear();
        if (tracks.getVideoTracks().size() > 0) {
            for (int i = 0; i < tracks.getVideoTracks().size(); i++) {

                VideoTrack videoTrackInfo = tracks.getVideoTracks().get(i);
                setVideoQuality();
                if (videoTrackInfo.isAdaptive()) {
                    trackItemList.add(new TracksItem(getActivity().getResources().getString(R.string.auto), videoTrackInfo.getUniqueId()));
                } else if (videoTrackInfo.getBitrate() > 100000 && videoTrackInfo.getBitrate() < 450000) {
                    trackItemList.add(new TracksItem(getActivity().getResources().getString(R.string.low), videoTrackInfo.getUniqueId()));
                } else if ((videoTrackInfo.getBitrate() > 450001 && videoTrackInfo.getBitrate() < 600000) || (videoTrackInfo.getBitrate() > 400000 && videoTrackInfo.getBitrate() < 620000)) {
                    trackItemList.add(new TracksItem(getActivity().getResources().getString(R.string.medium), videoTrackInfo.getUniqueId()));
                } else if (videoTrackInfo.getBitrate() > 600001 && videoTrackInfo.getBitrate() < 1000000) {

                    trackItemList.add(new TracksItem(getActivity().getResources().getString(R.string.high), videoTrackInfo.getUniqueId()));
                }
            }
        } else {
            Logger.d("tracksSize", tracks.getVideoTracks().size() + "");
        }

    }

    private void setVideoQuality() {
        String selectedTrack = KsPreferenceKeys.getInstance().getQualityName();
        if (!TextUtils.isEmpty(selectedTrack)) {
            trackName = selectedTrack;

            Log.d("TrackNameIs", trackName);
            Log.d("TrackNameIs", selectedTrack);

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

    public interface OnPlayerInteractionListener {
        void bingeWatchCall(String entryID);

        void onPlayerStart();
    }

    @Override
    public void onPause() {
        super.onPause();
        if (player != null) {
            stopPosition = (int) player.getCurrentPosition();
            player.pause();
        }
    }

    @Override
    public void onResume() {

        if (player != null) {
            player.seekTo(stopPosition);
            player.play();
        }
        super.onResume();

    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    @Override
    public void onActivityCreated(@Nullable @org.jetbrains.annotations.Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        try {
            mListener = (OnPlayerInteractionListener) getActivity();
        } catch (Exception ignored) {
            ignored.printStackTrace();
        }

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


        private VideoTracksAdapter(ArrayList<TracksItem> videoTracks) {
            this.tracks = videoTracks;


        }

        @NonNull
        @Override
        public ViewHolder1 onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.tracks_item_list_row, parent, false);
            return new ViewHolder1(view);
        }

        @Override
        public void onBindViewHolder(@NonNull final ViewHolder1 holder, @SuppressLint("RecyclerView") final int position) {

            if (KsPreferenceKeys.getInstance().getQualityName().equalsIgnoreCase(trackItemList.get(position).getTrackName())) {
                holder.tick.setBackgroundResource(R.drawable.tick);
            } else {
                holder.tick.setBackgroundResource(0);
            }
            holder.qualityText.setText(tracks.get(position).getTrackName());
            holder.qualityText.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    KsPreferenceKeys.getInstance().setQualityPosition(position);
                    KsPreferenceKeys.getInstance().setQualityName(trackItemList.get(position).getTrackName());

                    trackName = trackItemList.get(position).getTrackName();
                    player.changeTrack(tracks.get(position).getUniqueId());

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
    }
}