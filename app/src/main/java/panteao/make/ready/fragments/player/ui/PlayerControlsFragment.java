package panteao.make.ready.fragments.player.ui;

import android.os.Bundle;

import android.os.Handler;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;


import panteao.make.ready.R;
import panteao.make.ready.baseModels.BaseBindingFragment;
import panteao.make.ready.player.kalturaPlayer.KalturaFragment;

import android.content.Context;
import android.util.AttributeSet;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;

import com.kaltura.android.exoplayer2.ui.DefaultTimeBar;
import com.kaltura.android.exoplayer2.ui.TimeBar;
import com.kaltura.android.exoplayer2.util.Util;
import com.kaltura.playkit.PKLog;
import com.kaltura.playkit.Player;
import com.kaltura.playkit.PlayerState;
import com.kaltura.playkit.Utils;
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
    private boolean timer = true ;
    private boolean mFlag = false;

    private Formatter formatter;
    private StringBuilder formatBuilder;
    private PlayerCallbacks playerCallbacks;
    private ImageView btnPlay;
    private View seekBarControl;
    public ImageView fullscreen;
    public TextView currentPosition;
    private TextView totalDuration;
    private ImageView btnPause;
    private ImageView btnForward;
    private ImageView btnRewind;
    private ImageView btnback;
    public ImageView backArrow;
    private DefaultTimeBar seekBar;
    private long playbackDuration, playbackCurrentPosition;
    private boolean isOffline = false;
    private RelativeLayout relativeLayout;
    private LinearLayout seekbarLayout;
    private LinearLayout childControls;
    private LinearLayout skipBtn, bingeBtn;
    public ConstraintLayout bingeLay;

    private boolean dragging = false;



    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    private boolean isPipEnabled = false;

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


    public static String stringForTime(long timeMs) {
        StringBuilder formatBuilder = new StringBuilder();
        Formatter formatter = new Formatter(formatBuilder, Locale.getDefault());

        long totalSeconds = (timeMs + 500) / 1000;
        long seconds = totalSeconds % 60;
        long minutes = (totalSeconds / 60) % 60;
        long hours = totalSeconds / 3600;
        formatBuilder.setLength(0);
        return hours > 0 ? formatter.format("%d:%02d:%02d", hours, minutes, seconds).toString()
                : formatter.format("%02d:%02d", minutes, seconds).toString();
    }

   public void setCurrentPosition(int currentposition, int duration) {
        seekBar.setDuration(duration);
        currentPosition.setText(stringForTime(currentposition));
        totalDuration.setText(stringForTime(duration));
        updateSeekbar(currentposition);
        playbackCurrentPosition = currentposition;
//        controlRewindAndForwardImageVisibility(playbackCurrentPosition, playbackDuration);
    }

    private void updateSeekbar(int currentposition) {
        seekBar.setPosition(currentposition);
    }


   public void sendPlayerCurrentPosition(int playerCurrentPosition) {
        seekBar.setPosition(playerCurrentPosition);
        if (playerCurrentPosition > playbackDuration) {
            currentPosition.setText(stringForTime(playbackDuration));
//////            if (videoType.equalsIgnoreCase("1")) {
//////                hideControlsForLive();
////            } else {
//                forward.setVisibility(View.GONE);
//                rewind.setVisibility(View.VISIBLE);
//            }
        } else if (playerCurrentPosition <= 0) {
            currentPosition.setText(stringForTime(0));
//            if (videoType.equalsIgnoreCase("1")) {
//                hideControlsForLive();
//            } else {
//                rewind.setVisibility(View.GONE);
//                forward.setVisibility(View.VISIBLE);
//            }
        } else {
            currentPosition.setText(stringForTime(playerCurrentPosition));
        }


    }
    public void startHandler() {
        callHandler();

    }

    public void callAnimation() {

        if (timer) {
//            if (viewHideShowTimeHandler != null) {
                viewHideShowTimeHandler.removeCallbacks(viewHideShowRunnable);
//            }
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

                if (relativeLayout.getVisibility() == View.VISIBLE) {
                    relativeLayout.startAnimation(animationFadeOut);
//                    backArrow.setVisibility(View.GONE);
                    relativeLayout.setVisibility(View.GONE);


                    timer = true;


                } else {

                    Log.w("IMATAG", "handler");
                    relativeLayout.setVisibility(View.VISIBLE);
//                    backArrow.setVisibility(View.VISIBLE);
//                    if (videoType.equalsIgnoreCase("1")) {
//                        hideControlsForLive();
//                    } else {
//
//                    }
                   relativeLayout.startAnimation(animationFadeIn);

                    callHandler();
                }



        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void callHandler() {
        Log.w("conditionCheck-->>","in");
        timer = true;
        viewHideShowRunnable = () -> ShowAndHideView();

        viewHideShowTimeHandler = new Handler();
        viewHideShowTimeHandler.postDelayed(viewHideShowRunnable, 3000);
    }
    public void sendTapCallBack(boolean b) {
        mFlag = b;
    }
    private void performClick() {


        seekBar.addListener(new TimeBar.OnScrubListener() {
            @Override
            public void onScrubStart(TimeBar timeBar, long position) {

            }

            @Override
            public void onScrubMove(TimeBar timeBar, long position) {
                currentPosition.setText(stringForTime(position));
            }

            @Override
            public void onScrubStop(TimeBar timeBar, long position, boolean canceled) {
                if (playerCallbacks != null) {
                    seekBar.setPosition(position);
                    playerCallbacks.SeekbarLastPosition(position);
                }
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

        backArrow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (playerCallbacks != null) {
                    playerCallbacks.finishPlayer();
                }
                playerCallbacks.checkOrientation(backArrow);
            }
        });
        relativeLayout.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                //  Toast.makeText(getActivity(),"playerViewClicked",Toast.LENGTH_LONG).show();

                if (!mFlag)
                    return false;
//                if (replay.getVisibility() == View.VISIBLE) {
//                    childControl.setVisibility(View.GONE);
//                } else {
                    callAnimation();
//                }

                return false;
            }
        });

        fullscreen.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (playerCallbacks != null) {
                    playerCallbacks.checkOrientation(fullscreen);
                }
            }
        });

        bingeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (playerCallbacks != null) {
                    playerCallbacks.bingeWatch();
                }
            }
        });


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


    }

    public void showControls() {
        Log.w("IMATAG", "showControls");
        childControls.setVisibility(View.VISIBLE);
        seekbarLayout.setVisibility(View.VISIBLE);

    }
//
    public void hideControls() {
        Log.w("IMATAG", "hideControls");
        childControls.setVisibility(View.GONE);
        seekbarLayout.setVisibility(View.GONE);
    }
    public void showBingeWatch() {
        try {
            if (timer) {
                if (viewHideShowTimeHandler != null) {
                    viewHideShowTimeHandler.removeCallbacks(viewHideShowRunnable);
                }
            }
        } catch (Exception ignored) {

        }
        childControls.setVisibility(View.GONE);
        seekbarLayout.setVisibility(View.GONE);
        bingeLay.setVisibility(View.VISIBLE);
        bingeBtn.setVisibility(View.VISIBLE);
        backArrow.setVisibility(View.VISIBLE);
    }

    public void hideBingeWatch() {
        try {
            bingeLay.setVisibility(View.GONE);
            bingeBtn.setVisibility(View.GONE);
            backArrow.setVisibility(View.GONE);
        }catch (Exception e){

        }

    }
    public void sendPortraitCallback() {
        Log.w("captionHide", "sendPortraitCallback");

        fullscreen.setBackgroundResource(R.drawable.full_screen);
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.WRAP_CONTENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
        );
        params.setMargins(0, 0, 0, 0);
        params.gravity = Gravity.CENTER;
        seekBarControl.setLayoutParams(params);

        RelativeLayout.LayoutParams params1 = new RelativeLayout.LayoutParams(
                RelativeLayout.LayoutParams.WRAP_CONTENT,
                RelativeLayout.LayoutParams.WRAP_CONTENT
        );
        params1.setMargins(0, 0, 0, 0);

        try {
            RelativeLayout.LayoutParams params2 = new RelativeLayout.LayoutParams(
                    LinearLayout.LayoutParams.WRAP_CONTENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT
            );
            params2.setMargins(0, 10, 60, 0);
            params2.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
        } catch (Exception ignored) {

        }

    }

    public void sendLandscapeCallback() {
        try {
            fullscreen.setBackgroundResource(R.drawable.exit_full_screen);

            if (!getResources().getBoolean(R.bool.isTablet)) {
                setParamstoSeekBarControl(seekBarControl);

                //Utils.setParamstoSetingButton(skipBtn);
            }
        } catch (Exception e) {

        }

    }

    public static void setParamstoSeekBarControl(View seekBarControl) {
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.WRAP_CONTENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
        );
        params.setMargins(0, 0, 0, 60);
        seekBarControl.setLayoutParams(params);
    }

    private void findId(View view) {
        btnPause=(ImageView)view.findViewById(R.id.pause);
        btnForward=(ImageView)view.findViewById(R.id.forward);
        btnRewind=(ImageView)view.findViewById(R.id.rew);
        btnback=(ImageView)view.findViewById(R.id.back_arrow);
       relativeLayout=view.findViewById(R.id.control_layout);
       seekBar= view.findViewById(R.id.exo_progress);
       currentPosition=view.findViewById(R.id.exo_position);
       totalDuration=view.findViewById(R.id.exo_duration);
       seekbarLayout=view.findViewById(R.id.seekbar_ll);
       childControls=view.findViewById(R.id.childControls);
        seekBarControl = (View) view.findViewById(R.id.seekbarLayout);
        backArrow=(ImageView)view.findViewById(R.id.backArrow);
        fullscreen = (ImageView) view.findViewById(R.id.fullscreen);
        bingeBtn = (LinearLayout) view.findViewById(R.id.bingeBtn);
        bingeLay = (ConstraintLayout) view.findViewById(R.id.bingeLay);

//        hideControls();



    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
//        if (getActivity() instanceof PlayerCallbacks)
//           playerCallbacks = (PlayerCallbacks) getActivity();
        btnPause.setVisibility(View.VISIBLE);
        btnPause.setBackgroundResource(R.drawable.ic_baseline_pause_24);
        seekBar.setEnabled(true);


    }


    public void setPlayerCallBacks(PlayerCallbacks playerCallBacks) {
        this.playerCallbacks = playerCallBacks;
    }
}