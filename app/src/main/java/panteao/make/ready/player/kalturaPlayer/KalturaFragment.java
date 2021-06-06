package panteao.make.ready.player.kalturaPlayer;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.ProgressBar;

import com.kaltura.playkit.PKEvent;
import com.kaltura.playkit.PlayerEvent;
import com.kaltura.playkit.PlayerState;
import com.kaltura.tvplayer.KalturaOvpPlayer;

import org.jetbrains.annotations.NotNull;

import panteao.make.ready.R;
import panteao.make.ready.fragments.player.ui.PlayerCallbacks;
import panteao.make.ready.fragments.player.ui.PlayerControlsFragment;
import panteao.make.ready.utils.commonMethods.AppCommonMethod;
import panteao.make.ready.utils.cropImage.helpers.Logger;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link KalturaFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class KalturaFragment extends Fragment implements PlayerControlsFragment.OnFragmentInteractionListener, PlayerCallbacks,PKEvent.Listener<PlayerEvent.StateChanged> {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private KalturaOvpPlayer player;
    private FrameLayout playerLayout;
    private Context mcontext;
    private ProgressBar progressbar;

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
        findViewById();
        // Inflate the layout for this fragment
        player = AppCommonMethod.loadPlayer(getActivity(), playerLayout);

     setPlayerListner();
        return inflater.inflate(R.layout.fragment_kaltura, container, false);

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
//                        onBackPressed();
                    mHandler.removeCallbacks(updateTimeTask);
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

    private void findViewById() {
        playerLayout=(FrameLayout)getView().findViewById(R.id.playerRoot);
        progressbar=(ProgressBar)getView().findViewById(R.id.pBar);

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

    }

    @Override
    public void Forward() {

    }

    @Override
    public void Rewind() {

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
    public void onAttach(@NonNull @NotNull Activity context) {

        super.onAttach(context);
        this.mcontext=context;
    }
}