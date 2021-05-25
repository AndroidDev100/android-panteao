package panteao.make.ready.fragments.player.ui;

import android.os.Bundle;

import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;


import panteao.make.ready.R;
import panteao.make.ready.baseModels.BaseBindingFragment;
import android.content.Context;
import android.util.AttributeSet;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.kaltura.playkit.PKLog;
import com.kaltura.playkit.Player;
import com.kaltura.playkit.PlayerState;
import com.kaltura.playkit.ads.AdController;
import com.kaltura.playkit.utils.Consts;

import org.jetbrains.annotations.NotNull;

import java.util.Formatter;
import java.util.Locale;
/**
 * A simple {@link Fragment} subclass.
 * Use the {@link PlayerControlsFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class PlayerControlsFragment extends Fragment {


    private Runnable viewHideShowRunnable;
    private Handler viewHideShowTimeHandler;
    private boolean timer = true;

    private Formatter formatter;
    private StringBuilder formatBuilder;
    private PlayerCallbacks playerCallbacks;
    private SeekBar seekBar;
    private ImageView btnPlay;
    private ImageView btnPause;
    private ImageView btnForward;
    private ImageView btnRewind;
    private ImageView btnback;
    private RelativeLayout relativeLayout;

    private boolean dragging = false;



    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public PlayerControlsFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment PlayerControlsFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static PlayerControlsFragment newInstance(String param1, String param2) {
        PlayerControlsFragment fragment = new PlayerControlsFragment();
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

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_player_controls, container, false);

        findId(view);
        performClick();

        return view;
    }

    public void startHandler() {
        callHandler();

    }


    private void performClick() {
         relativeLayout.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                //  Toast.makeText(getActivity(),"playerViewClicked",Toast.LENGTH_LONG).show();

//                if (!mFlag)
//                    return false;
//                if (replay.getVisibility() == View.VISIBLE) {
//                    childControl.setVisibility(View.GONE);
//                } else {
                  ShowAndHideView();
//                }

                return false;
            }
        });
        //Play pause control for player

        btnPause.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                    if (playerCallbacks != null) {
                        playerCallbacks.playPause(btnPause);
                        Log.d("mmmmm", "btnpause");

                    } else {
                        Log.d("mmmmm", "btnpauseelse");

                    }

            }
        });


        //Seek player for 10 seconds from currentPosition
        btnForward.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (playerCallbacks != null) {
                    playerCallbacks.Forward();
                }
            }
        });
        //Rewind player for 10 seconds from currentPosition
        btnRewind.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (playerCallbacks != null) {
                    playerCallbacks.Rewind();
                }
            }
        });

        //Back button click
//        btnback.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                if (playerCallbacks != null) {
//                    playerCallbacks.finishPlayer();
//                }
//                playerCallbacks.checkOrientation(btnback);
//            }
//        });



        //Replay video event
//        replay.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                if (playerCallbacks != null) {
//                    replay.setVisibility(View.GONE);
//                    seekBar.setPosition(0);
//                    playerCallbacks.replay();
//                }
//            }
//        });

        //Seekbar callbacks
//        seekBar.addListener(new TimeBar.OnScrubListener() {
//            @Override
//            public void onScrubStart(TimeBar timeBar, long position) {
//
//            }
//
//            @Override
//            public void onScrubMove(TimeBar timeBar, long position) {
//                currentPosition.setText(Utils.stringForTime(position));
//                hideSkipIntro();
//            }
//
//            @Override
//            public void onScrubStop(TimeBar timeBar, long position, boolean canceled) {
//                if (playerCallbacks != null) {
//                    seekBar.setPosition(position);
//                    playerCallbacks.SeekbarLastPosition(position);
//                }
//            }
//        });
    }



    private void callHandler() {
        Log.w("conditionCheck-->>","in");
        timer = true;
        viewHideShowRunnable = () -> ShowAndHideView();

        viewHideShowTimeHandler = new Handler();
        viewHideShowTimeHandler.postDelayed(viewHideShowRunnable, 3000);
    }
    private void callAnimation() {

        if (timer) {
            viewHideShowTimeHandler.removeCallbacks(viewHideShowRunnable);
        }
        ShowAndHideView();
    }
    private void ShowAndHideView() {
        try {

            Animation animationFadeOut = AnimationUtils.loadAnimation(getActivity(), R.anim.fade_out);
            Animation animationFadeIn = AnimationUtils.loadAnimation(getActivity(), R.anim.fade_in);

            animationFadeIn.setAnimationListener(new Animation.AnimationListener() {
                @Override
                public void onAnimationStart(Animation animation) {
                    if (playerCallbacks != null)
                        playerCallbacks.showPlayerController(true);
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
                    if (playerCallbacks != null)
                        playerCallbacks.showPlayerController(false);
                }

                @Override
                public void onAnimationRepeat(Animation animation) {

                }
            });

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void findId(View view) {
        btnPause=(ImageView)view.findViewById(R.id.pause);
        btnForward=(ImageView)view.findViewById(R.id.forward);
        btnRewind=(ImageView)view.findViewById(R.id.rew);
        btnback=(ImageView)view.findViewById(R.id.back_arrow);
       relativeLayout=(RelativeLayout)view.findViewById(R.id.control_layout);

    }


    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        if (getActivity() instanceof PlayerCallbacks)
           playerCallbacks = (PlayerCallbacks) getActivity();
        btnPause.setVisibility(View.VISIBLE);
        btnPause.setBackgroundResource(R.drawable.ic_baseline_pause_24);

    }
}