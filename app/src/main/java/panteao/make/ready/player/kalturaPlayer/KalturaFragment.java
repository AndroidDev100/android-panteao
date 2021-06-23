package panteao.make.ready.player.kalturaPlayer;

import android.content.Context;
import android.content.pm.ActivityInfo;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.kaltura.netkit.utils.ErrorElement;
import com.kaltura.playkit.PKEvent;
import com.kaltura.playkit.PKMediaEntry;
import com.kaltura.playkit.PlayerEvent;
import com.kaltura.playkit.PlayerState;
import com.kaltura.tvplayer.KalturaOvpPlayer;
import com.kaltura.tvplayer.KalturaPlayer;
import com.kaltura.tvplayer.OVPMediaOptions;

import panteao.make.ready.R;
import panteao.make.ready.activities.KalturaPlayerActivity;
import panteao.make.ready.fragments.player.ui.PlayerCallbacks;
import panteao.make.ready.fragments.player.ui.PlayerControlsFragment;
import panteao.make.ready.utils.commonMethods.AppCommonMethod;
import panteao.make.ready.utils.constants.AppConstants;
import panteao.make.ready.utils.cropImage.helpers.Logger;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link KalturaFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class KalturaFragment extends Fragment implements  PlayerCallbacks,PKEvent.Listener<PlayerEvent.StateChanged> {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private OnPlayerInteractionListener mListener;
    private KalturaOvpPlayer player;
    private FrameLayout playerLayout;
    private Context mcontext;
    private ProgressBar progressbar;
    private PlayerCallbacks playerCallbacks;
    private boolean IsbingeWatch = false;
    private int bingeWatchTimer = 0;
    private  String entryID="";

    private boolean showBingeWatchControls =false;
    private boolean isBingeWatchTimeCalculate=false;

    private int bottomMargin = 0;
    private boolean isOfflineVideo = false;
    private PlayerControlsFragment playerControlsFragment;
    private  Handler mHandler = new Handler();

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    private FrameLayout container;

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
    private final Runnable updateTimeTask = new Runnable() {
        public void run() {
//            Log.d("ndhfdm", "playing");
            Log.d("ndhfdm", player.getCurrentPosition()+"");
            playerControlsFragment.setCurrentPosition((int) player.getCurrentPosition(),(int) player.getDuration());
//            seekBar1.setProgress(((int) player.getCurrentPosition()));
//            seekBar1.setMax(((int) player.getDuration()));
            mHandler.postDelayed(this, 100);


        }
    };
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_kaltura, container, false);


        findViewById(view);
        // Inflate the layout for this fragment
        player = AppCommonMethod.loadPlayer(getActivity(), playerLayout);
        callPlayerControlsFragment();
        startPlayer();
       setPlayerListner();
       performClick();
        bottomMargin = (int) getResources().getDimension(R.dimen.caption_margin);

        return view;

    }

    private void performClick() {
        playerLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (playerControlsFragment != null) {
                    playerControlsFragment.sendTapCallBack(true);
                    playerControlsFragment.callAnimation();
                    Log.d("bnjm", "visible");
//
                }
            }
        });
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
//                playerControlsFragment.setVideoType(videoType);
//                playerControlsFragment.setPlayerCallBacks(this);
            } catch (Exception ignored) {

            }

        }
    }
    private void startPlayer() {
        if (player!=null) {
            player.stop();
        }
        if(getArguments().containsKey(AppConstants.ENTRY_ID)){
        entryID=getArguments().getString(AppConstants.ENTRY_ID);
        }
        OVPMediaOptions ovpMediaOptions = AppCommonMethod.buildOvpMediaOptions(entryID, 0L);
        player.loadMedia(ovpMediaOptions, new KalturaPlayer.OnEntryLoadListener() {
            @Override
            public void onEntryLoadComplete(PKMediaEntry entry, ErrorElement loadError) {
                if (loadError != null) {
                    Toast.makeText(getActivity(), loadError.getMessage(), Toast.LENGTH_LONG).show();
                } else {
                    Logger.d("OVPMedia onEntryLoadComplete  entry = ", entry.getId());
                }
            }
        });
    }
    private void setPlayerListner() {

        player.addListener(this, PlayerEvent.stateChanged, this::onEvent);
        player.addListener(this, PlayerEvent.playing, new PKEvent.Listener() {
            @Override
            public void onEvent(PKEvent event) {
                Log.d("ndhfdm", "playing");
                mHandler.postDelayed(updateTimeTask, 100);
                if (playerControlsFragment != null) {
                    if (!isBingeWatchTimeCalculate){
                        Log.w("totalDuartion",player.getDuration()+"");
                        Log.w("totalDuartion",bingeWatchTimer*1000+"");
                        Log.w("totalDuartion",player.getDuration()-bingeWatchTimer*1000+"");
                        int timeCalculation= (int) (player.getDuration()-bingeWatchTimer*1000);
                        if (timeCalculation>bingeWatchTimer){
                            isBingeWatchTimeCalculate=true;
                            bingeWatchTimer= (int) (player.getDuration()-bingeWatchTimer*1000);
                        }

                    }
                    playerControlsFragment.sendTapCallBack(true);
                    playerControlsFragment.startHandler();
                    if (IsbingeWatch && bingeWatchTimer>0){
                        int currentPosition = (int) player.getCurrentPosition();
                        if (currentPosition >= bingeWatchTimer){
                            showBingeWatchControls=true;

                            playerControlsFragment.showBingeWatch();

                        }
                    }

                }

            }
        });
        player.addListener(this, PlayerEvent.ended, new PKEvent.Listener() {
            @Override
            public void onEvent(PKEvent event) {
                if (playerControlsFragment != null) {
                    Log.d("ndhfdm", "playing");
                    player.stop();
                    showBingeWatchControls = false;
                    playerControlsFragment.hideControls();
                    if (playerControlsFragment.bingeLay.getVisibility() == View.VISIBLE) {
                        playerControlsFragment.backArrow.setVisibility(View.VISIBLE);
                    }
//                        else {
//                            playerControlsFragment.sendVideoCompletedState(event.getType());
//                        }

                }
                    if (mHandler != null && updateTimeTask != null)
                    {
                        //                    onBackPressed();
                        finishPlayer();
                    mHandler.removeCallbacks(updateTimeTask);
                }
                    if (IsbingeWatch){
                        isBingeWatchTimeCalculate=false;
                        Logger.d("bingewatcher","ytirrrr");
                        mListener.bingeWatchCall(entryID);
                    }

            }
        });

                player.addListener(this, PlayerState.READY, new PKEvent.Listener() {
                    @Override
                    public void onEvent(PKEvent event) {
                        if (playerControlsFragment != null) {


                        }
                    }
                });

    }

    private void findViewById(View view) {
        playerLayout=(FrameLayout)view.findViewById(R.id.playerRoot);
        progressbar=(ProgressBar)view.findViewById(R.id.pBar);
        container = view.findViewById(R.id.container);


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
                id.setBackgroundResource(R.color.transparent);
                id.setBackgroundResource(R.drawable.ic_baseline_play_arrow_24);

                player.pause();
//                playerControlsFragment.showControls();
                Log.d("chgytfgh", "pause");

            } else {
                id.setBackgroundResource(R.color.transparent);

                id.setBackgroundResource(R.drawable.ic_baseline_pause_24);

                player.play();

                Log.d("chgytfgh", "play");
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
        Log.d("playyyyy","for");

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
//                    bottomMargin = (int) getResources().getDimension(R.dimen.caption_margin_landscape);
                    currentConfig = newConfig;
//
                } else if (newConfig.orientation == Configuration.ORIENTATION_PORTRAIT) {
                    if (playerControlsFragment != null) {
                        playerControlsFragment.sendPortraitCallback();
                    }
//                    bottomMargin = (int) getResources().getDimension(R.dimen.caption_margin);
                    getActivity().getWindow().clearFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
                    currentConfig = newConfig;
                   playerLayout.setPadding(0, 0, 0, 0);
                }


    }


    @Override
    public void finishPlayer() {
        checkBackButtonClickOrientation();

    }

    @Override
    public void skipIntro() {

    }
    int totalEpisodes=0;
    public void totalEpisodes(int size) {
        totalEpisodes=size;
    }
    int runningEpisodes=0;
    public void currentEpisodes(int i) {
        runningEpisodes=i;
        Log.w("totalZies",totalEpisodes+" "+runningEpisodes);
        if (runningEpisodes<totalEpisodes){
            IsbingeWatch=true;
        }else {
            IsbingeWatch=false;
        }
    }

    public void bingeWatchStatus(boolean b) {
        IsbingeWatch=b;
    }

    @Override
    public void bingeWatch() {
        mListener.bingeWatchCall(entryID);


    }

    private void checkBackButtonClickOrientation() {

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

    public interface OnPlayerInteractionListener {
        default void bingeWatchCall(String entryID) {

        }
        void onPlayerStart();    }

    @Override
    public void onPause() {
        super.onPause();
        if (player != null) {
           player.stop();
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        player.play();
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
//
//            int orientation = getResources().getConfiguration().orientation;
//            if (orientation == Configuration.ORIENTATION_LANDSCAPE) {
//                getActivity().setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_SENSOR_LANDSCAPE);
//               getActivity().getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
//                FrameLayout.LayoutParams captionParams = (FrameLayout.LayoutParams)playerLayout.getLayoutParams();
//                captionParams.bottomMargin = (int) getResources().getDimension(R.dimen.live_full_screen_bottom);
//                captionParams.topMargin = (int) getResources().getDimension(R.dimen.live_full_screen_bottom);
//               playerLayout.setLayoutParams(captionParams);
//
//            }
        }catch (Exception ignored){

        }

    }
}