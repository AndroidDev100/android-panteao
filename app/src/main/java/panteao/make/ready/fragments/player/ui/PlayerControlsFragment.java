package panteao.make.ready.fragments.player.ui;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.os.Bundle;

import android.os.CountDownTimer;
import android.os.Handler;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;


import panteao.make.ready.R;
import panteao.make.ready.baseModels.BaseBindingFragment;
import panteao.make.ready.player.kalturaPlayer.KalturaFragment;
import panteao.make.ready.utils.commonMethods.AppCommonMethod;
import panteao.make.ready.utils.cropImage.helpers.Logger;

import android.content.Context;
import android.util.AttributeSet;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.view.GravityCompat;
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
    private boolean timer = true;
    private boolean mFlag = false;
    public ImageView qualitySettings;
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
    private View btnback;
    public ImageView backArrow;
    private DefaultTimeBar seekBar;
    private long playbackDuration, playbackCurrentPosition;
    private boolean isOffline = false;
    private LinearLayout seekbarLayout;
    public ViewGroup childControls;
    public Button skipBtn;
    public LinearLayout bingeBtn;
    private boolean dragging = false;
    public ConstraintLayout bingeLay;
    private CountDownTimer mTimer;
    private TextView skipduration;
    private FrameLayout replay;


    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    private boolean isPipEnabled = false;
    private boolean seeking = false;

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
      // controlRewindAndForwardImageVisibility(playbackCurrentPosition, playbackDuration);
    }


    private void updateSeekbar(int currentposition) {

        seekBar.setPosition(currentposition);
    }


    public void sendPlayerCurrentPosition(int playerCurrentPosition) {
        seekBar.setPosition(playerCurrentPosition);
        if (playerCurrentPosition > playbackDuration) {
            currentPosition.setText(stringForTime(playbackDuration));
//            if (videoType.equalsIgnoreCase("1")) {
//                hideControlsForLive();
//            } else {
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

    public void sendTapCallBack(boolean b) {
        mFlag = b;
        if (childControls.getVisibility() == View.VISIBLE) {
            hideControls();
        } else {
            showControls();
        }
    }

    private void performClick() {
        if (AppCommonMethod.isTV(requireContext())) {
            seekBar.setKeyCountIncrement(100);
        }
        seekBar.addListener(new TimeBar.OnScrubListener() {
            @Override
            public void onScrubStart(TimeBar timeBar, long position) {
                seeking = true;

            }

            @Override
            public void onScrubMove(TimeBar timeBar, long position) {
                //  seekBar.setPosition(position);
                currentPosition.setText(stringForTime(position));
            }

            @Override
            public void onScrubStop(TimeBar timeBar, long position, boolean canceled) {
                if (playerCallbacks != null) {
                    seekBar.setPosition(position);
                    playerCallbacks.SeekbarLastPosition(position);
                    seeking = false;
                }
            }
        });

        //Play pause control for player

        btnPause.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (playerCallbacks != null) {
                    playerCallbacks.playPause(btnPause);
                }
            }
        });
        qualitySettings.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (playerCallbacks != null) {
                    playerCallbacks.QualitySettings();
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
                if (!AppCommonMethod.isTV(requireContext())) {
                    if (playerCallbacks != null) {
                        playerCallbacks.finishPlayer();
                    }
                    playerCallbacks.checkOrientation(backArrow);
                } else {
                    getActivity().finish();
                }
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
                    bingeLay.setVisibility(View.GONE);
                    bingeBtn.setVisibility(View.GONE);
                    backArrow.setVisibility(View.GONE);
                    qualitySettings.setVisibility(View.GONE);
                    playerCallbacks.bingeWatch();
                }
            }
        });

        skipBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (playerCallbacks != null) {
                    playerCallbacks.skipIntro();
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
//                    currentPosition.setText("00:00");
//                    playerCallbacks.replay();
//                }
//            }
//        });


    }

    public void showControls() {
        Log.d("ffrrrfrrfr", childControls.getVisibility() + "");
        Log.d("ffrrrfrrfr", bingeLay.getVisibility() + "");
        if (replay.getVisibility() == View.VISIBLE) {
            backArrow.setVisibility(View.VISIBLE);
        } else {
            if (childControls.getVisibility() == View.GONE && bingeLay.getVisibility() != View.VISIBLE) {
                childControls.animate().alpha(1.0f).setDuration(1000)
                        .setListener(new AnimatorListenerAdapter() {
                            @Override
                            public void onAnimationEnd(Animator animation) {
                                super.onAnimationEnd(animation);
                                if (AppCommonMethod.isTV(requireContext())) {
                                    btnPause.requestFocus();
                                }

                            }

                            @Override
                            public void onAnimationStart(Animator animation) {
                                super.onAnimationStart(animation);
                                childControls.setVisibility(View.VISIBLE);
                                seekbarLayout.setVisibility(View.VISIBLE);
                                backArrow.setVisibility(View.VISIBLE);
                                qualitySettings.setVisibility(View.VISIBLE);
                                if (childControls.getFocusedChild() != null) {
                                    childControls.getFocusedChild().requestFocus();
                                }
                            }
                        });
            }
        }
    }

    //
    public void hideControls() {
        if (seeking) {
            return;
        }
        childControls.animate().alpha(0.0f).setDuration(500)
                .setStartDelay(200)
                .setListener(new AnimatorListenerAdapter() {
                    @Override
                    public void onAnimationEnd(Animator animation) {
                        super.onAnimationEnd(animation);
                        childControls.setVisibility(View.GONE);
                        seekbarLayout.setVisibility(View.GONE);
                        backArrow.setVisibility(View.GONE);
                        qualitySettings.setVisibility(View.GONE);
                        if (childControls.getFocusedChild() != null) {
                            childControls.getFocusedChild().clearFocus();
                        }
                    }
                });
    }

    public void showBingeWatch(long position, boolean isFirstCalled, int totalEpisodes, int runningEpisodes) {
//        try {
//            if (timer) {
//                if (viewHideShowTimeHandler != null) {
//                    viewHideShowTimeHandler.removeCallbacks(viewHideShowRunnable);
//                }
//            }
//        } catch (Exception ignored) {
//            ignored.printStackTrace();
//        }
//        childControls.setVisibility(View.VISIBLE);
//        seekbarLayout.setVisibility(View.VISIBLE);
//        bingeBtn.setVisibility(View.VISIBLE);
//        backArrow.setVisibility(View.VISIBLE);
        if (totalEpisodes == runningEpisodes) {

        } else {
            childControls.setVisibility(View.GONE);
            seekbarLayout.setVisibility(View.GONE);
            qualitySettings.setVisibility(View.GONE);
            bingeLay.setVisibility(View.VISIBLE);
            bingeBtn.setVisibility(View.VISIBLE);
            skipduration.setVisibility(View.VISIBLE);
            backArrow.setVisibility(View.VISIBLE);
            if (isFirstCalled) {
                mTimer = new CountDownTimer(position, 1000) {
                    public void onTick(long millisUntilFinished) {
                        skipduration.setText(Long.toString(millisUntilFinished / 1000));
                    }

                    public void onFinish() {
                        skipduration.setText("");
                    }
                };

                mTimer.start();
            }
        }

    }

    public void hideBingeWatch() {
        try {
            childControls.setVisibility(View.GONE);
            seekbarLayout.setVisibility(View.GONE);
            bingeLay.setVisibility(View.GONE);
            bingeBtn.setVisibility(View.GONE);
            skipduration.setVisibility(View.GONE);
            backArrow.setVisibility(View.GONE);
        } catch (Exception e) {

        }

    }

    public void sendPortraitCallback() {

        qualitySettings.setVisibility(View.VISIBLE);

        fullscreen.setBackgroundResource(R.drawable.full_screen);
        setParamstoQualityinPortrait(qualitySettings);
        setParamstoBackbuttoninProtrait(backArrow);
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
        backArrow.setLayoutParams(params1);


        try {
            RelativeLayout.LayoutParams params2 = new RelativeLayout.LayoutParams(
                    LinearLayout.LayoutParams.WRAP_CONTENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT
            );
            params2.setMargins(0, 0, 0, 0);
            params2.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
        } catch (Exception ignored) {

        }


    }

    public void sendLandscapeCallback() {
        try {
            qualitySettings.setVisibility(View.VISIBLE);
            fullscreen.setBackgroundResource(R.drawable.exit_full_screen);
            Log.d("frfjghjghj","EnterHere5");
            if (!getResources().getBoolean(R.bool.isTablet)) {
                Log.d("frfjghjghj","EnterHere4");
                setParamstoSeekBarControl(seekBarControl);
                setParamstoBackbutton(backArrow);
                setParamstoQuality(qualitySettings);
                //Utils.setParamstoSetingButton(skipBtn);
            }
        } catch (Exception e) {

        }

    }

    public static void setParamstoBackbutton(View btnback) {
        RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(
                RelativeLayout.LayoutParams.WRAP_CONTENT,
                RelativeLayout.LayoutParams.WRAP_CONTENT
        );
        params.setMargins(30, 65, 0, 0);


        btnback.setLayoutParams(params);

    }

    public static void setParamstoBackbuttoninProtrait(View btnback) {
        RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(
                RelativeLayout.LayoutParams.WRAP_CONTENT,
                RelativeLayout.LayoutParams.WRAP_CONTENT
        );
        params.setMargins(35, 0, 0, 0);


        btnback.setLayoutParams(params);

    }

    public static void setParamstoQuality(View quality) {
        RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(
                RelativeLayout.LayoutParams.WRAP_CONTENT,
                RelativeLayout.LayoutParams.WRAP_CONTENT
        );
        params.setMargins(0, 65, 15, 0);
        params.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);

        quality.setLayoutParams(params);

    }

    public static void setParamstoQualityinPortrait(View quality) {
        RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(
                RelativeLayout.LayoutParams.WRAP_CONTENT,
                RelativeLayout.LayoutParams.WRAP_CONTENT
        );
        params.setMargins(0, 8, 15, 0);
        params.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);

        quality.setLayoutParams(params);

    }

    public static void setParamstoSeekBarControl(View seekBarControl) {
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.WRAP_CONTENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
        );
        params.setMargins(0, 0, 0, 110);
        seekBarControl.setLayoutParams(params);
    }

    private void findId(View view) {
        btnPause = (ImageView) view.findViewById(R.id.pause);
        btnForward = (ImageView) view.findViewById(R.id.forward);
        btnRewind = (ImageView) view.findViewById(R.id.rew);
        bingeLay = (ConstraintLayout) view.findViewById(R.id.bingeLay);
        skipduration = (TextView) view.findViewById(R.id.skip_duration);
        replay = (FrameLayout) view.findViewById(R.id.replay);
        seekBar = view.findViewById(R.id.exo_progress);
        currentPosition = view.findViewById(R.id.exo_position);
        totalDuration = view.findViewById(R.id.exo_duration);
        seekbarLayout = view.findViewById(R.id.seekbar_ll);
        childControls = view.findViewById(R.id.control_layout);
        seekBarControl = (View) view.findViewById(R.id.seekbarLayout);
        backArrow = (ImageView) view.findViewById(R.id.backArrow);
        fullscreen = (ImageView) view.findViewById(R.id.fullscreen);
        bingeBtn = view.findViewById(R.id.bingeBtn);
        skipBtn = view.findViewById(R.id.skipBtn);
        qualitySettings = (ImageView) view.findViewById(R.id.iv_quality);
        bingeLay.setVisibility(View.GONE);
        replay.setVisibility(View.GONE);
        bingeBtn.setVisibility(View.GONE);
        if (AppCommonMethod.isTV(getActivity())) {
            Logger.e("IS_TV", "TRUE");
            fullscreen.setVisibility(View.GONE);
        } else {
            Logger.e("IS_TV", "FALSE");
        }
        if (playerCallbacks != null) {
            playerCallbacks.sendPlayPauseId(btnPause);
        }

        childControls.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                //  Toast.makeText(getActivity(),"playerViewClicked",Toast.LENGTH_LONG).show();

                if (!mFlag)
                    return false;
                if (replay.getVisibility() == View.VISIBLE) {

                } else {

                    if (childControls.getVisibility() == View.VISIBLE) {
                        hideControls();
                    } else {
                        showControls();
                    }
                }
//                if (replay.getVisibility() == View.VISIBLE) {
//                    childControl.setVisibility(View.GONE);
//                } else {
//                    callAnimation();
//                }

                return false;
            }
        });
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        btnPause.setVisibility(View.VISIBLE);
        seekBar.setEnabled(true);
    }


    public void setPlayerCallBacks(PlayerCallbacks playerCallBacks) {
        this.playerCallbacks = playerCallBacks;
    }

    public void showSkipButton() {
        skipBtn.setVisibility(View.VISIBLE);
        skipBtn.requestFocus();
    }

    public void hideSkipIntro() {
        if (skipBtn.getVisibility() == View.VISIBLE)
            skipBtn.setVisibility(View.GONE);
    }

    public void onKeyDown(int keyCode) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            if (childControls.getVisibility() == View.VISIBLE) {
                hideControls();
            } else {
                getActivity().finish();
            }
        } else {
            if (childControls.getVisibility() == View.GONE) {
                showControls();
            }
            switch (keyCode) {
                case KeyEvent.KEYCODE_MEDIA_PLAY_PAUSE:
                    btnPause.requestFocus();
                    if (playerCallbacks != null) {
                        playerCallbacks.playPause(btnPause);
                    }
                    break;
                case KeyEvent.KEYCODE_MEDIA_FAST_FORWARD:
                    btnForward.requestFocus();
                    btnForward.performClick();
                    break;
                case KeyEvent.KEYCODE_MEDIA_REWIND:
                    btnRewind.requestFocus();
                    btnRewind.performClick();
                    break;
                case KeyEvent.KEYCODE_MEDIA_NEXT:
//                    btnForward.requestFocus();
//                    btnForward.performClick();
                    break;
                case KeyEvent.KEYCODE_MEDIA_PREVIOUS:
//                    btnRewind.requestFocus();
//                    btnRewind.performClick();
                    break;
                case KeyEvent.KEYCODE_PROG_GREEN:
//                    playerOptions.requestFocus();
//                    playerOptions.performClick();
                    break;
                case KeyEvent.KEYCODE_DPAD_CENTER:
                    if (bingeBtn.hasFocus()) {
                        bingeBtn.performClick();
                    }
                    break;
            }
        }
    }

    public void showReplayVisibility() {
        childControls.setVisibility(View.GONE);
        seekbarLayout.setVisibility(View.GONE);
        qualitySettings.setVisibility(View.GONE);
        replay.setVisibility(View.VISIBLE);
        backArrow.setVisibility(View.VISIBLE);
    }
}