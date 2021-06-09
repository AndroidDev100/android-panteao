package panteao.make.ready.player.kalturaPlayer;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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

import org.jetbrains.annotations.NotNull;

import panteao.make.ready.R;
import panteao.make.ready.activities.KalturaPlayerActivity;
import panteao.make.ready.activities.instructor.ui.InstructorActivity;
import panteao.make.ready.fragments.player.ui.PlayerCallbacks;
import panteao.make.ready.fragments.player.ui.PlayerControlsFragment;
import panteao.make.ready.utils.commonMethods.AppCommonMethod;
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
    private KalturaOvpPlayer player;
    private FrameLayout playerLayout;
    private Context mcontext;
    private ProgressBar progressbar;
    private PlayerCallbacks playerCallbacks;

    private PlayerControlsFragment playerControlsFragment;
    private final Handler mHandler = new Handler();

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

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
        OVPMediaOptions ovpMediaOptions = AppCommonMethod.buildOvpMediaOptions(KalturaPlayerActivity.Companion.getENTRY_ID(), 0L);
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
                    playerControlsFragment.sendTapCallBack(true);
                    playerControlsFragment.startHandler();

                }

            }
        });
        player.addListener(this, PlayerEvent.ended, new PKEvent.Listener() {
            @Override
            public void onEvent(PKEvent event) {
                if (playerControlsFragment != null) {
                    Log.d("ndhfdm", "playing");
                    player.stop();
                    if (mHandler != null && updateTimeTask != null)
                    {
                        //                    onBackPressed();
                        finishPlayer();
                    mHandler.removeCallbacks(updateTimeTask);
                }
                }
            }
        });

                player.addListener(this, PlayerState.READY, new PKEvent.Listener() {
                    @Override
                    public void onEvent(PKEvent event) {
                        if (playerControlsFragment != null) {

                            Log.d("ndhfdm", "playing");

                        }
                    }
                });

    }

    private void findViewById(View view) {
        playerLayout=(FrameLayout)view.findViewById(R.id.playerRoot);
        progressbar=(ProgressBar)view.findViewById(R.id.pBar);

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

    @Override
    public void finishPlayer() {

    }

    @Override
    public void checkOrientation(ImageView id) {

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
    }

    @Override
    public void onPause() {
        super.onPause();
        player.pause();
    }

    @Override
    public void onResume() {
        super.onResume();
        player.play();
    }
}