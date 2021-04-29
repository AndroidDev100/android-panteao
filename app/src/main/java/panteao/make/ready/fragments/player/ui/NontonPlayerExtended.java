package panteao.make.ready.fragments.player.ui;


public class NontonPlayerExtended /*extends BaseBindingFragment<FragmentPlayerBinding> implements View.OnClickListener*//*, ExoPlayerListener, ExoAdListener, ExoplayerOnInternet, ExoThumbListener, ExoPlayerHelper.NoInternetListner*/ {

   /* private Bundle bundle;
    private Context context;
    private ExoPlayerHelper mExoPlayerHelper;
    public boolean pauseAds = false;
    public boolean isBanner = false;
    public boolean mExoPlayerFullscreen;
    private Dialog mFullScreenDialog;
    private ImageView mFullScreenIcon;
    int currentWindowIndex;
    private String url;
    private String vastUrl;
    private String TAG = "";
    private long currentPosition;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        this.context = context;
    }


    public long getCurrentPosition() {
        long pos = 0;
        if (isBanner)
            return pos;
        else {
            if (mExoPlayerHelper == null)
                return 0;
            else
                return mExoPlayerHelper.getCurrentPosition();
        }
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        connectionObserver();
        clickListners();
        this.bundle = savedInstanceState;
    }

    private void connectionObserver() {

        if (NetworkConnectivity.isOnline(context)) {
            connectionValidation(true);
        } else {
            connectionValidation(false);
        }

    }

    private void connectionValidation(Boolean aBoolean) {
        if (aBoolean) {
            UIinitialization();
        } else {
            Logger.e("NontonPlayerExtended", "noInternet");
            //noConnectionLayout();
        }
    }

    private void UIinitialization() {
        getBinding().noInternet.setVisibility(View.GONE);
        getBinding().playerRoot.setVisibility(View.VISIBLE);
    }

    private void noConnectionLayout() {
        getBinding().noInternet.setVisibility(View.VISIBLE);
    }


  *//*  public void setPlayerSetting(String url, String vastURl, String image) {
        getBinding().exoPlayerProgressBar.setVisibility(View.VISIBLE);

        if (StringUtils.isNullOrEmpty(url)) {
            isBanner = true;
            setShutter();
            setImage(image, getBinding().bannerView);
        } else {
            isBanner = false;
            initFullscreenButton();
            initFullscreenDialog();
            getBinding().exoPlayerProgressBar.setVisibility(View.VISIBLE);
            getBinding().ivRetryPlay.setVisibility(View.GONE);
            getBinding().playerView.setVisibility(View.VISIBLE);
            if (StringUtils.isNullOrEmpty(vastURl)) {
                typePlayerPlaying = 1;
                this.url = url;
                mExoPlayerHelper = new ExoPlayerHelper.Builder(context, getBinding().playerView)
                        .addMuteButton(false, false)
                        .setUiControllersVisibility(isVisible())
                        .setRepeatModeOn(false)
                        .setAutoPlayOn(true)
                        .setExoplayerOnInternet(this)
                        .setVideoUrls(url)
                        .setExoPlayerEventsListener(this)
                        .setInternetListener(this)
                        .setExoAdEventsListener(this)
                        .addSavedInstanceState(bundle)
                        .createAndPrepare();
                mExoPlayerHelper.setmExoplayerOnInternet(this);
                if (context instanceof DetailActivity) {
                    if (((DetailActivity) context).getPositonVideo() > 0) {
                        mExoPlayerHelper.mSeekTo(((DetailActivity) context).getPositonVideo());
                    }
                }
                if (context instanceof EpisodeActivity) {
                    if (((EpisodeActivity) context).getPositonVideo() > 0) {
                        mExoPlayerHelper.mSeekTo(((EpisodeActivity) context).getPositonVideo());
                    }
                }
            } else {
                this.url = url;
                this.vastUrl = vastURl;

                typePlayerPlaying = 2;
                pauseAds = false;
                mExoPlayerHelper = new ExoPlayerHelper.Builder(context, getBinding().playerView)
                        .addMuteButton(false, false)
                        .setUiControllersVisibility(isVisible())
                        .addSavedInstanceState(bundle)
                        .setRepeatModeOn(false)
                        .setAutoPlayOn(false)
                        .setVideoUrls(url)
                        .setTagUrl(vastUrl)
                        .setExoplayerOnInternet(this)
                        .setExoPlayerEventsListener(this)
                        .setInternetListener(this)
                        .setExoAdEventsListener(this)
                        .createAndPrepare();

                mExoPlayerHelper.setmExoplayerOnInternet(this);


                if (context instanceof DetailActivity) {
                    if (((DetailActivity) context).getPositonVideo() > 0) {
                        mExoPlayerHelper.mSeekTo(((DetailActivity) context).getPositonVideo());
                    }
                }
                if (context instanceof EpisodeActivity) {
                    if (((EpisodeActivity) context).getPositonVideo() > 0) {
                        mExoPlayerHelper.mSeekTo(((EpisodeActivity) context).getPositonVideo());

                    }
                }

                getBinding().exoPlayerProgressBar.setVisibility(View.GONE);
            }
            mExoPlayerHelper.playerPlay();
            currentPosition = mExoPlayerHelper.mPlayer.getCurrentPosition();
            Logger.e("Current player", "current position is " + currentPosition);
        }
    }

 *//*   *//*public void pauseNontonPlayer() {
        if (mExoPlayerHelper != null)
            mExoPlayerHelper.playerPause();
    }*//*

     *//*public void playVideoAfterShimmer() {
        if (mExoPlayerHelper != null)
            mExoPlayerHelper.playerPlay();
    }
*//*

    private void setShutter() {
        getBinding().playerView.setVisibility(View.GONE);
        getBinding().bannerFrame.setVisibility(View.VISIBLE);
    }

    public void resetShutter() {
        getBinding().playerView.setVisibility(View.VISIBLE);
        getBinding().bannerFrame.setVisibility(View.GONE);
        releaseNontonPlayer();
    }


    private void setImage(String imageKey, ImageView view) {
        try {
            getBinding().playerError.setVisibility(View.VISIBLE);
            getBinding().playerError.bringToFront();
            getBinding().ivPlayPremium.setVisibility(View.VISIBLE);
            getBinding().ivPlayPremium.bringToFront();
            getBinding().ivRetryPlay.setVisibility(View.GONE);
            getBinding().ivRetryPlay.bringToFront();
            getBinding().flBackIconImage.setVisibility(View.VISIBLE);
            getBinding().backImage.bringToFront();
            getBinding().flBackIconImage.bringToFront();
            typePlayerPlaying = 0;
            KsPreferenceKeys preference = KsPreferenceKeys.getInstance();
            String url1 = preference.getString(AppConstants.APP_PREF_CFEP, "");
            if (StringUtils.isNullOrEmpty(url1)) {
                url1 = AppCommonMethod.urlPoints;
                preference.setString(AppConstants.APP_PREF_CFEP, url1);
            }
            String url2 = AppConstants.VIDEO_IMAGE_BASE_KEY;
            StringBuilder stringBuilder = new StringBuilder(url1 + AppConstants.FILTER_PLAYER_BANNER + url2 + imageKey);
            Logger.e("", "" + stringBuilder.toString());

            PrintLogging.printLog("", "dsfsdfsfds" + stringBuilder.toString());

            ImageHelper.getInstance(context)
                    .loadImageTo(view, stringBuilder.toString(), new RequestOptions());


        } catch (Exception e) {
            Logger.e("", "" + e.toString());
        }
        getBinding().exoPlayerProgressBar.setVisibility(View.GONE);
    }


    private void clickListners() {

        getBinding().ivRetryPlay.setOnClickListener(view -> {
            // ((DetailActivity) getActivity()).openLoginPage(getActivity().getResources().getString(R.string.please_login_play).toString());
            // ((DetailActivity) getActivity()).comingSoon();
        });


        getBinding().playerView.findViewById(R.id.flBackIcon).setOnClickListener(view -> onBackIconClick());

        getBinding().playerView.findViewById(R.id.exo_no_internet).setOnClickListener(view -> mExoPlayerHelper.checkInternetAndResume());
        getBinding().playerView.findViewById(R.id.exo_internet).setOnClickListener(view -> mExoPlayerHelper.checkInternetAndResume());


        getBinding().flBackIconImage.setOnClickListener(view -> onBackIconClick());

        getBinding().noInternet.setOnClickListener(view -> connectionObserver());
    }

    public void onBackIconClick() {
        if (mExoPlayerFullscreen)
            closeFullscreenDialog();
        else {
            // ((AppCompatActivity) context).finish();
            if (context instanceof DetailActivity) {
                ((DetailActivity) context).onBackPressed();
            } else if (context instanceof EpisodeActivity) {
                ((EpisodeActivity) context).onBackPressed();
            }
        }
    }


  *//*  @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        try {
            mExoPlayerHelper.onSaveInstanceState(outState);
        } catch (Exception e) {
            Logger.e("NontonPlayerExtended", "" + e.toString());
        }
        super.onSaveInstanceState(outState);
    }*//*

    @Override
    public FragmentPlayerBinding inflateBindingLayout(@NonNull LayoutInflater inflater) {
        return FragmentPlayerBinding.inflate(inflater);
    }


  *//*  @Override
    public void onResume() {
        super.onResume();
        if (mExoPlayerHelper != null) {
            if (mExoPlayerHelper.isPlaying())
                mExoPlayerHelper.playerPlay();

            if(mExoPlayerHelper.isPlayingAd())
                mExoPlayerHelper.playerPlay();
        }
    }*//*


    public void pauseOnOtherAudio() {
        if (mExoPlayerHelper != null) {
            Toast.makeText(context, "pauseOnOtherAudio", Toast.LENGTH_SHORT).show();
            if (mExoPlayerHelper.isPlaying())
                mExoPlayerHelper.playerPause();
            if (mExoPlayerHelper.isPlayingAd())
                mExoPlayerHelper.mImaAdsLoader.pauseAd();
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        if (context != null) {
            context = null;
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        if (mExoPlayerHelper != null) {
            mExoPlayerHelper.playerPause();
            if (mExoPlayerHelper.isPlayingAd()) {
                pauseAds = true;
                mExoPlayerHelper.mImaAdsLoader.pauseAd();
            }
        }
    }

    public void pauseAdsShare() {
        if (mExoPlayerHelper != null) {
            if (mExoPlayerHelper.isPlayingAd()) {
                pauseAds = true;
                mExoPlayerHelper.mImaAdsLoader.pauseAd();
            }
        }
    }

    @Override
    public void onStop() {
        super.onStop();
        if (mExoPlayerHelper != null) {
            mExoPlayerHelper.playerPause();
            if (mExoPlayerHelper.isPlayingAd()) {
                mExoPlayerHelper.mImaAdsLoader.pauseAd();
            }
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (mExoPlayerHelper != null) {
            mExoPlayerHelper.onActivityDestroy();
            mExoPlayerHelper.releasePlayer();
        }
    }

    public void showPremiumPlay(boolean val, String message) {
        getBinding().noInternet.setVisibility(View.GONE);
        getBinding().ivPlayPremium.setVisibility(View.VISIBLE);
        getBinding().exoPlayerProgressBar.setVisibility(View.GONE);
        if (val) {
            //  getBinding().debugTextView.setText(message);
            getBinding().ivPlayPremium.setVisibility(View.VISIBLE);
            getBinding().ivPlayPremium.bringToFront();
        } else {
            getBinding().ivPlayPremium.setVisibility(View.GONE);
        }
    }

    public HeartbeatModel playerDurationWhilePlaying() {
        HeartbeatModel model = new HeartbeatModel();
        model.setStatus(false);
        //2 for pause
        model.setState(2);
        if (mExoPlayerHelper != null && mExoPlayerHelper.mPlayer!=null) {
            if (mExoPlayerHelper.mPlayer.getPlayWhenReady() && mExoPlayerHelper.mPlayer.getPlaybackState() == Player.STATE_READY) {
                model.setStatus(true);
                // 1 for playing
                model.setState(1);
                model.setDuration((int) mExoPlayerHelper.mPlayer.getDuration());
                model.setPosition((int) mExoPlayerHelper.mPlayer.getCurrentPosition());
            }
        }
        return model;
    }

    public void releaseNontonPlayer() {
        if (mExoPlayerHelper != null) {
            if (mExoPlayerHelper.isPlayingAd()) {
                mExoPlayerHelper.releaseAdsLoader();
            }
            mExoPlayerHelper.playerPause();
            mExoPlayerHelper.onActivityStop();
            mExoPlayerHelper.releasePlayer();
        }
    }

    public void hideReplayImage() {
        if (mExoPlayerHelper != null)
            mExoPlayerHelper.showAllControls(false);
    }

    public void showPlayIcon() {
        getBinding().noInternet.setVisibility(View.GONE);
        getBinding().ivPlayPremium.setVisibility(View.GONE);
        getBinding().exoPlayerProgressBar.setVisibility(View.GONE);
    }


  *//*  @Override
    public void onAdPlay() {
        mExoPlayerHelper.disableControls();


    }
*//*
    public void hitApiHeartBeat() {
        if (mExoPlayerHelper != null && mExoPlayerHelper.mPlayer != null) {
            if (context instanceof DetailActivity) {
                int duration = (int) mExoPlayerHelper.mPlayer.getDuration();
                int pos = (int) mExoPlayerHelper.mPlayer.getCurrentPosition();
                ((DetailActivity) context).hitHeartBeatAPI(pos, duration);
            } else if (context instanceof EpisodeActivity) {
                // ((EpisodeActivity) context).onBackPressed();
            }
        }
    }

 *//*   @Override
    public void onAdPause() {
        if (!pauseAds)
            mExoPlayerHelper.playerPlay();

        hitApiHeartBeat();
    }

    @Override
    public void onAdResume() {

    }

    @Override
    public void onAdEnded() {
        mExoPlayerHelper.enableControls();
        hitApiHeartBeat();
        Logger.e("", "");
    }

    @Override
    public void onAdError() {

    }

    @Override
    public void onAdClicked() {

    }

    @Override
    public void onAdTapped() {

    }

    @Override
    public void onLoadingStatusChanged(boolean isLoading, long bufferedPosition, int bufferedPercentage) {

    }

    @Override
    public void onPlayerPlaying(int currentWindowIndex) {
        getBinding().exoPlayerProgressBar.setVisibility(View.GONE);

        Logger.e("", "currentWindowIndex" + currentWindowIndex);
        Logger.e("", "currentWindowIndex long" + mExoPlayerHelper.mPlayer.getCurrentPosition());
        hitApiHeartBeat();


      *//**//*  if(mExoPlayerHelper.mPlayer.getCurrentPosition()>currentPosition+20)
        {
            Logger.e("",""+currentPosition);
            currentPosition=currentPosition+20;
        }
        *//**//*

     *//**//*  if (context instanceof DetailActivity) {
            ((DetailActivity) context).requestAudioFocus();
        } else {
            ((EpisodeActivity) context).requestAudioFocus();
        }
*//**//*
    }*//*

     *//*   @Override
    public void onPlayerPaused(int currentWindowIndex) {
        hitApiHeartBeat();
    }

    @Override
    public void onPlayerBuffering(int currentWindowIndex) {
        hitApiHeartBeat();
        //getBinding().exoPlayerProgressBar.setVisibility(View.VISIBLE);
    }

    @Override
    public void onPlayerStateEnded(int currentWindowIndex) {
        getBinding().exoPlayerProgressBar.setVisibility(View.GONE);
        hitApiHeartBeat();


    }

    @Override
    public void onPlayerStateIdle(int currentWindowIndex) {
        if (!AppCommonMethod.isInternet)
            this.currentWindowIndex = currentWindowIndex;
        hitApiHeartBeat();
    }

    @Override
    public void onPlayerError(String errorString) {

    }

    @Override
    public void createExoPlayerCalled(boolean isToPrepare) {

    }

    @Override
    public void releaseExoPlayerCalled() {

    }

    @Override
    public void onVideoResumeDataLoaded(int window, long position, boolean isResumeWhenReady) {

    }

    @Override
    public void onTracksChanged(int currentWindowIndex, int nextWindowIndex, boolean isPlayBackStateReady) {

    }

    @Override
    public void onMuteStateChanged(boolean isMuted) {

    }

    @Override
    public void onVideoTapped() {

    }

    @Override
    public boolean onPlayBtnTap() {
        if (mExoPlayerHelper != null && AppCommonMethod.isInternet) {
            hitApiHeartBeat();
            if (context instanceof DetailActivity) {
                ((DetailActivity) context).requestAudioFocus();
            } else if (context instanceof EpisodeActivity) {
                ((EpisodeActivity) context).requestAudioFocus();
            }
            mExoPlayerHelper.playWhenInternetAvailable(currentWindowIndex);
            return true;
        }
        return false;

    }


    @Override
    public boolean onPauseBtnTap() {
        return false;
    }

    @Override
    public void onFullScreenBtnTap() {


    }
*//*
    private void initFullscreenButton() {
        PlayerControlView controlView = getBinding().playerView.findViewById(R.id.exo_controller);
        mFullScreenIcon = controlView.findViewById(R.id.exo_fullscreen_icon);
        FrameLayout mFullScreenButton = controlView.findViewById(R.id.exo_fullscreen_button);
        mFullScreenButton.setOnClickListener(view -> {
            if (!mExoPlayerFullscreen)
                openFullscreenDialog();
            else
                closeFullscreenDialog();

        });
    }


    private void initFullscreenDialog() {
        try {
            mFullScreenDialog = new Dialog(context, android.R.style.Theme_Black_NoTitleBar_Fullscreen) {
                public void onBackPressed() {
                    if (mExoPlayerFullscreen)
                        closeFullscreenDialog();
                    super.onBackPressed();
                }
            };
        } catch (Exception e) {

        }
    }


    public void openFullscreenDialog() {
        ((FrameLayout) getBinding().playerView.getParent()).removeView(getBinding().playerView);
        if (context instanceof DetailActivity) {
            ((DetailActivity) context).setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
        } else {
            ((EpisodeActivity) context).setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
        }

        mFullScreenDialog.addContentView(getBinding().playerView, new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));

        if (context instanceof DetailActivity) {
            mFullScreenIcon.setImageDrawable(ContextCompat.getDrawable((DetailActivity) context, R.drawable.outline_icon));
            ((DetailActivity) context).hideVideoDetail();

        } else {
            mFullScreenIcon.setImageDrawable(ContextCompat.getDrawable((EpisodeActivity) context, R.drawable.outline_icon));
            ((EpisodeActivity) context).hideVideoDetail();

        }
        mExoPlayerFullscreen = true;
        mFullScreenDialog.show();
        restorePlayerState();

    }

    public void restorePlayerState() {
        if (mExoPlayerHelper != null) {
            if (mExoPlayerHelper.isPlaying())
                mExoPlayerHelper.playerPlay();
            else
                mExoPlayerHelper.playerPause();
            Logger.e("openFullscreenDialog", "openFullscreenDialog");
        }
    }

    public void closeFullscreenDialog() {
        ((FrameLayout) getBinding().playerView.getParent()).removeView(getBinding().playerView);

        if (context instanceof DetailActivity) {
            ((DetailActivity) context).setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        } else {
            ((EpisodeActivity) context).setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        }


        getBinding().playerRoot.addView(getBinding().playerView);
        mExoPlayerFullscreen = false;
        mFullScreenDialog.dismiss();
        restorePlayerState();
        if (context instanceof DetailActivity) {
            mFullScreenIcon.setImageDrawable(ContextCompat.getDrawable((DetailActivity) context, R.drawable.outline_fullscreen));
           ((DetailActivity) context).showVideoDetail();

        } else {
            mFullScreenIcon.setImageDrawable(ContextCompat.getDrawable((EpisodeActivity) context, R.drawable.outline_fullscreen));
            ((EpisodeActivity) context).showVideoDetail();

        }

    }


    @Override
    public void onClick(View view) {

    }

   *//* @Override
    public void onInternetResume(boolean check, long duration) {
        if (check) {
            mExoPlayerHelper.releasePlayer();
            switch (typePlayerPlaying) {
                case 1:
                    typePlayerPlaying = 1;
                    mExoPlayerHelper = new ExoPlayerHelper.Builder(context, getBinding().playerView)
                            .addMuteButton(false, false)
                            .setUiControllersVisibility(isVisible())
                            .setRepeatModeOn(false)
                            .setAutoPlayOn(true)
                            .setExoplayerOnInternet(this)
                            .setVideoUrls(url)
                            .setExoPlayerEventsListener(this)
                            .setExoAdEventsListener(this)
                            .addSavedInstanceState(bundle)
                            .createAndPrepare();

                    break;

                case 2:
                    typePlayerPlaying = 2;
                    mExoPlayerHelper = new ExoPlayerHelper.Builder(context, getBinding().playerView)
                            .addMuteButton(false, false)
                            .setUiControllersVisibility(isVisible())
                            .addSavedInstanceState(bundle)
                            .setRepeatModeOn(false)
                            .setAutoPlayOn(false)
                            .setVideoUrls(url)
                            .setTagUrl(vastUrl)
                            .setExoplayerOnInternet(this)
                            .setExoPlayerEventsListener(this)
                            .setExoAdEventsListener(this)
                            .createAndPrepare();
                    break;

                default:
                    break;


            }
            mExoPlayerHelper.mSeekTo(duration);
            // mExoPlayerHelper.showBottomProgress();
            mExoPlayerHelper.playerPlay();
            hitApiHeartBeat();
        }
    }

    @Override
    public void onThumbImageViewReady(ImageView imageView) {

    }


    @Override
    public void onNoInternet(boolean val) {
        if (val)
            getBinding().exoPlayerProgressBar.setVisibility(View.GONE);
    }
*//*


    public void fullScreenIcon()
    {
        PlayerControlView controlView = getBinding().playerView.findViewById(R.id.exo_controller);
        mFullScreenIcon = controlView.findViewById(R.id.exo_fullscreen_icon);
        if (context instanceof DetailActivity) {
            mFullScreenIcon.setImageDrawable(ContextCompat.getDrawable((DetailActivity) context, R.drawable.outline_fullscreen));
        } else {
            mFullScreenIcon.setImageDrawable(ContextCompat.getDrawable((EpisodeActivity) context, R.drawable.outline_fullscreen));
        }
    }


    public void smallScreenIcon()
    {
        PlayerControlView controlView = getBinding().playerView.findViewById(R.id.exo_controller);
        mFullScreenIcon = controlView.findViewById(R.id.exo_fullscreen_icon);
        if (context instanceof DetailActivity) {
            mFullScreenIcon.setImageDrawable(ContextCompat.getDrawable((DetailActivity) context, R.drawable.outline_icon));
        } else {
            mFullScreenIcon.setImageDrawable(ContextCompat.getDrawable((EpisodeActivity) context, R.drawable.outline_icon));
        }
    }*/
}
