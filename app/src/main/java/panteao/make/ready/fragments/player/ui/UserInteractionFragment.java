package panteao.make.ready.fragments.player.ui;

import android.content.Context;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.os.Bundle;
import android.os.Handler;
import android.os.SystemClock;
import android.util.Log;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;

import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.core.widget.ImageViewCompat;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.ViewModelProvider;

import com.google.gson.JsonObject;
import com.kaltura.tvplayer.OfflineManager;

import panteao.make.ready.Bookmarking.BookmarkingViewModel;
import panteao.make.ready.activities.article.ArticleActivity;
import panteao.make.ready.activities.instructor.ui.InstructorActivity;
import panteao.make.ready.activities.show.ui.EpisodeActivity;
import panteao.make.ready.activities.live.LiveActivity;
import panteao.make.ready.activities.series.ui.SeriesDetailActivity;
import panteao.make.ready.activities.show.ui.ShowActivity;
import panteao.make.ready.activities.tutorial.ui.ChapterActivity;
import panteao.make.ready.activities.tutorial.ui.TutorialActivity;
import panteao.make.ready.baseModels.BaseBindingFragment;
import panteao.make.ready.callbacks.commonCallbacks.TrailorCallBack;
import panteao.make.ready.enums.DownloadStatus;
import panteao.make.ready.player.trailor.PlayerActivity;
import panteao.make.ready.utils.MediaTypeConstants;
import panteao.make.ready.utils.cropImage.helpers.Logger;
import panteao.make.ready.utils.helpers.ActivityTrackers;
import panteao.make.ready.utils.helpers.downloads.OnDownloadClickInteraction;
import panteao.make.ready.R;
import panteao.make.ready.beanModelV3.uiConnectorModelV2.EnveuVideoItemBean;
import panteao.make.ready.databinding.DetailWatchlistLikeShareViewBinding;
import panteao.make.ready.fragments.dialog.AlertDialogFragment;
import panteao.make.ready.fragments.dialog.AlertDialogSingleButtonFragment;
import panteao.make.ready.utils.commonMethods.AppCommonMethod;
import panteao.make.ready.utils.constants.AppConstants;
import panteao.make.ready.utils.helpers.CheckInternetConnection;

import panteao.make.ready.utils.helpers.StringUtils;
import panteao.make.ready.utils.helpers.ToastHandler;
import panteao.make.ready.utils.helpers.ksPreferenceKeys.KsPreferenceKeys;


import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Objects;

public class UserInteractionFragment extends BaseBindingFragment<DetailWatchlistLikeShareViewBinding> implements AlertDialogFragment.AlertDialogListener, View.OnClickListener {
    private final String TAG = this.getClass().getSimpleName();
    private int assestId;
    private Context context;
    private KsPreferenceKeys preference;
    private String token;
    private int watchListCounter = 0;
    private int likeCounter = 0;
    private boolean loginStatus;
    private EnveuVideoItemBean seriesDetailBean;
    private BookmarkingViewModel bookmarkingViewModel;
    private long mLastClickTime = 0;
    private boolean isloggedout = false;
    private String videoId = "6081937244001";
    private final boolean resetWatchlist =false;
    private final boolean addToWatchlist=false;
    private OnDownloadClickInteraction onDownloadClickInteraction;
    private TrailorCallBack trailorCallBack;
    /**
     * The policy key for the video cloud account.
     */
    private String seriesId;
    private String entryid="";


    public UserInteractionFragment() {

    }

    @Override
    protected DetailWatchlistLikeShareViewBinding inflateBindingLayout() {
        return DetailWatchlistLikeShareViewBinding.inflate(inflater);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        this.context = context;
        if (!(context instanceof OnDownloadClickInteraction))
            try {
                throw new Throwable("Activity doesnot implement OnDownloadClickInteraction");
            } catch (Throwable throwable) {
                throwable.printStackTrace();
            }
    }


    @Override
    public void onViewCreated(@NotNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        onDownloadClickInteraction = (OnDownloadClickInteraction) getActivity();
        trailorCallBack = (TrailorCallBack) getActivity();
        try {
            getAssetId();
            hitApiIsLike();
            hitApiIsWatchList();
            isloggedout = false;
            if (context instanceof SeriesDetailActivity || context instanceof InstructorActivity  || context instanceof TutorialActivity) {
                getBinding().watchList.setVisibility(View.GONE);
            }

            if (context instanceof LiveActivity) {
                getBinding().watchList.setVisibility(View.GONE);
                getBinding().llLike.setVisibility(View.GONE);
            }

        } catch (Exception e) {

        }
        setClickListeners();

    }

//    @Override
//    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
//        super.onActivityCreated(savedInstanceState);
//        onDownloadClickInteraction = (OnDownloadClickInteraction) getActivity();
//        trailorCallBack = (TrailorCallBack) getActivity();
//    }




    private void setClickListeners() {
        // getBinding().llLike.setOnClickListener(this);
        getBinding().shareWith.setOnClickListener(this);
        getBinding().showComments.setOnClickListener(this);
        //  getBinding().watchList.setOnClickListener(this);
        getBinding().downloadVideo.setOnClickListener(this);
        getBinding().videoDownloaded.setOnClickListener(this);
        getBinding().seriesDownloaded.setOnClickListener(this);
        getBinding().videoDownloading.setProgress(0);
        getBinding().videoDownloading.setOnClickListener(this);
        getBinding().pauseDownload.setOnClickListener(this);
        getBinding().trailor.setOnClickListener(this);
        getBinding().setDownloadStatus(DownloadStatus.START);

    }

    private void getAssetId() {
        Bundle bundle = getArguments();
        if (bundle != null) {
            this.likeCounter = 0;
            this.assestId = bundle.getInt(AppConstants.BUNDLE_ASSET_ID);
            seriesDetailBean = bundle.getParcelable(AppConstants.BUNDLE_SERIES_DETAIL);
            videoId = seriesDetailBean.getBrightcoveVideoId();
            seriesId = bundle.getString(AppConstants.BUNDLE_SERIES_ID);
            entryid = bundle.getString(AppConstants.BUNDLE_KENTRY_ID);
        }

        if (seriesDetailBean!=null && seriesDetailBean.getTrailerReferenceId()!=null && seriesDetailBean.getTrailerReferenceId()!=""){
            getBinding().trailor.setVisibility(View.VISIBLE);
            Logger.e("TrailerReferenceId",seriesDetailBean.getTrailerReferenceId());
        }else {
            getBinding().trailor.setVisibility(View.GONE);
        }


        if (getActivity() != null && preference == null)
            preference = KsPreferenceKeys.getInstance();
        bookmarkingViewModel = new ViewModelProvider(this).get(BookmarkingViewModel.class);

        if (preference.getAppPrefLoginStatus()) {
            token = preference.getAppPrefAccessToken();
        } else {
            resetLike();
            resetWatchList();

        }
        UIintialization();
    }


    public void UIintialization() {
        likeClick();
        watchListClick();
        //  Toast.makeText(getActivity(), "UI Initiala", Toast.LENGTH_LONG).show();

       /* getBinding().shareWith.setOnClickListener(v -> {

        });*/

        getBinding().shareWith.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                return gestureDetector.onTouchEvent(motionEvent);
            }
        });

        getBinding().showComments.setOnClickListener(v -> {
            //new ToastHandler(getActivity()).show(getActivity().getResources().getString(R.string.coming_soon));
            /*if(context instanceof  SeriesDetailActivity){
                ((SeriesDetailActivity) context).openCommentSection();
            }*/
        });

    }

    GestureDetector gestureDetector = new GestureDetector(context, new GestureDetector.SimpleOnGestureListener() {
        @Override
        public void onLongPress(MotionEvent event) {
            super.onLongPress(event);
           // copyShareURL();
        }

        @Override
        public boolean onSingleTapConfirmed(MotionEvent e) {
            openShareDialogue();
            return true;

        }

        @Override
        public boolean onSingleTapUp(MotionEvent e) {
            return true;
        }
    });

    private void copyShareURL() {
        String imgUrl = seriesDetailBean.getThumbnailImage();
        int id = seriesDetailBean.getId();
        String title = seriesDetailBean.getName();
        String assetType = "";
        if (context instanceof SeriesDetailActivity) {
            imgUrl = seriesDetailBean.getPosterURL();
            id = seriesDetailBean.getId();
            title = seriesDetailBean.getTitle();
            assetType = MediaTypeConstants.getInstance().getSeries();
            ((SeriesDetailActivity) context).seriesLoader();
        } else if (context instanceof EpisodeActivity) {
            imgUrl = seriesDetailBean.getPosterURL();
            id = seriesDetailBean.getId();
            title = seriesDetailBean.getTitle();
            assetType = MediaTypeConstants.getInstance().getEpisode();

        } else if (context instanceof TutorialActivity) {
            imgUrl = seriesDetailBean.getPosterURL();
            id = seriesDetailBean.getId();
            title = seriesDetailBean.getTitle();
            assetType = MediaTypeConstants.getInstance().getTutorial();

        } else if (context instanceof ChapterActivity) {
            imgUrl = seriesDetailBean.getPosterURL();
            id = seriesDetailBean.getId();
            title = seriesDetailBean.getTitle();
            assetType = MediaTypeConstants.getInstance().getChapter();

        } else if (context instanceof InstructorActivity) {
            imgUrl = seriesDetailBean.getPosterURL();
            id = seriesDetailBean.getId();
            title = seriesDetailBean.getTitle();
            assetType = MediaTypeConstants.getInstance().getInstructor();

        } else if (context instanceof ArticleActivity) {
            imgUrl = seriesDetailBean.getPosterURL();
            id = seriesDetailBean.getId();
            title = seriesDetailBean.getTitle();
            assetType = MediaTypeConstants.getInstance().getMovie();

        }
        else if (context instanceof LiveActivity) {
            imgUrl = seriesDetailBean.getPosterURL();
            id = seriesDetailBean.getId();
            title = seriesDetailBean.getTitle();
            assetType = MediaTypeConstants.getInstance().getLive();

        }
        if (SystemClock.elapsedRealtime() - mLastClickTime < 1000) {
            return;
        }
        mLastClickTime = SystemClock.elapsedRealtime();

        AppCommonMethod.copyShareURL(getActivity(), title, id, assetType, imgUrl, seriesDetailBean.getSeason());

        new Handler().postDelayed(() -> {
            if (context instanceof SeriesDetailActivity) {
                ((SeriesDetailActivity) context).dismissLoading(((SeriesDetailActivity) context).getBinding().progressBar);
            }
        }, 2000);
    }


    private void goToLogin() {
        if (context instanceof SeriesDetailActivity) {
            ActivityTrackers.getInstance().setLauncherActivity("SeriesDetailActivity");
            ((SeriesDetailActivity) context).openLogin();
        } else if (context instanceof EpisodeActivity) {
            ((EpisodeActivity) context).openLoginPage();
            ActivityTrackers.getInstance().setLauncherActivity("EpisodeActivity");
        } else if (context instanceof InstructorActivity) {
            ActivityTrackers.getInstance().setLauncherActivity("DetailActivity");
            ((InstructorActivity) context).openLoginPage();
        }
        else if (context instanceof ShowActivity) {
            ActivityTrackers.getInstance().setLauncherActivity("DetailActivity");
            ((ShowActivity) context).openLoginPage();
        }
        else if (context instanceof ChapterActivity) {
            ActivityTrackers.getInstance().setLauncherActivity("DetailActivity");
            ((ChapterActivity) context).openLoginPage();
        }
        else if (context instanceof TutorialActivity) {
            ActivityTrackers.getInstance().setLauncherActivity("DetailActivity");
            ((TutorialActivity) context).openLoginPage();
        }
    }

    public void watchListClick() {
        getBinding().watchList.setOnClickListener(v -> {
            if (getBinding().wProgressBar.getVisibility() != View.VISIBLE) {
                if (preference.getAppPrefLoginStatus()) {
                    setWatchListForAsset();

                } else {
                    ActivityTrackers.getInstance().setAction(ActivityTrackers.WATCHLIST);
                    goToLogin();
                }
            }
        });
    }

    public void setWatchListForAsset() {
        getBinding().wProgressBar.setVisibility(View.VISIBLE);
        getBinding().addIcon.setVisibility(View.GONE);
        if (watchListCounter == 0)
            hitApiAddWatchList();
        else {
            hitApiRemoveList();
        }
    }

    private void hitApiRemoveList() {
        bookmarkingViewModel.hitRemoveWatchlist(token, assestId).observe(getActivity(), responseEmpty -> {
            getBinding().wProgressBar.setVisibility(View.GONE);
            getBinding().addIcon.setVisibility(View.VISIBLE);
            if (Objects.requireNonNull(responseEmpty).isStatus()) {
                resetWatchList();
                AppCommonMethod.trackFcmCustomEvent(getActivity(), AppConstants.REMOVE_WATCHLIST, seriesDetailBean.getAssetType(), "", "", 0, seriesDetailBean.getTitle(), 0, seriesDetailBean.getId() + "", 0, 0, "", "","","");

            } else {
                if (responseEmpty.getResponseCode() == 4302) {
                    isloggedout = true;
                    logoutCall();
                   // showDialog(getActivity().getResources().getString(R.string.logged_out), getResources().getString(R.string.you_are_logged_out));
                } else if (responseEmpty.getResponseCode() == 500) {
                    showDialog();
                }
            }
        });

    }

    public void likeClick() {
        getBinding().llLike.setOnClickListener(view -> {
            setLikeForAsset();
        });

    }

    public void setToken() {
        this.token = token;
    }

    public void setLikeForAsset() {
        if (getBinding().lProgressBar.getVisibility() != View.VISIBLE) {
            if (SystemClock.elapsedRealtime() - mLastClickTime < 500) {
                return;
            }
            mLastClickTime = SystemClock.elapsedRealtime();

            if (preference.getAppPrefLoginStatus()) {
                getBinding().lProgressBar.setVisibility(View.VISIBLE);
                getBinding().likeIcon.setVisibility(View.GONE);
                if (likeCounter == 0)
                    hitApiAddLike();
                else
                    hitApiRemoveLike();
            } else {
                ActivityTrackers.getInstance().setAction(ActivityTrackers.LIKE);
                goToLogin();
            }
        }
    }


    public void hitApiAddLike() {
        bookmarkingViewModel.hitApiAddLike(token, assestId).observe(getActivity(), responseEmpty -> {
            getBinding().lProgressBar.setVisibility(View.GONE);
            getBinding().likeIcon.setVisibility(View.VISIBLE);
            if (Objects.requireNonNull(responseEmpty).isStatus()) {
                setLike();
            } else {
                if (responseEmpty.getResponseCode() == 4302) {
                    isloggedout = true;
                    logoutCall();
                  //  showDialog(getActivity().getResources().getString(R.string.logged_out), getResources().getString(R.string.you_are_logged_out));
                } else if (responseEmpty.getResponseCode() == 4902) {
                    setLike();
                    String debugMessage = responseEmpty.getDebugMessage();
                    //from value will bedepends on how user click of watchlist icon-->>if loggedout=2 else=2
                    if (from == 1) {
                        showDialog();
                    }

                } else if (responseEmpty.getResponseCode() == 500) {
                    showDialog();
                }
            }

        });
    }

    public void hitApiRemoveLike() {

        bookmarkingViewModel.hitApiDeleteLike(token, assestId).observe(getActivity(), responseEmpty -> {
            getBinding().lProgressBar.setVisibility(View.GONE);
            getBinding().likeIcon.setVisibility(View.VISIBLE);
            if (Objects.requireNonNull(responseEmpty).isStatus()) {
                resetLike();
            } else {
                if (responseEmpty.getResponseCode() == 4302) {
                    isloggedout = true;
                    showDialog();
                } else if (responseEmpty.getResponseCode() == 500) {
                    showDialog();
                }
            }


        });


    }


    public void hitApiIsLike() {
        if (preference.getAppPrefLoginStatus()) {
            JsonObject requestParam = new JsonObject();
            requestParam.addProperty(AppConstants.API_PARAM_LIKE_ID, assestId);
            requestParam.addProperty(AppConstants.API_PARAM_LIKE_TYPE, MediaTypeConstants.getInstance().getSeries());
            bookmarkingViewModel.hitApiIsLike(token, assestId).observe(getActivity(), responseEmpty -> {

                if (Objects.requireNonNull(responseEmpty).isStatus()) {
                    if (StringUtils.isNullOrEmptyOrZero(responseEmpty.getData().getId())) {
                        resetLike();
                    } else {
                        setLike();
                    }
                } else {
                    if (responseEmpty.getResponseCode() == 4302) {
                        isloggedout = true;
                        logoutCall();
                       // showDialog(getActivity().getResources().getString(R.string.logged_out), getResources().getString(R.string.you_are_logged_out));
                    } else if (responseEmpty.getResponseCode() == 500) {
                        showDialog();
                    }
                }

            });
        }
    }


    public void hitApiIsWatchList() {
        if (preference.getAppPrefLoginStatus()) {
            bookmarkingViewModel.hitApiIsWatchList(token, assestId).observe(getActivity(), responseEmpty -> {

                if (Objects.requireNonNull(responseEmpty).isStatus()) {
                    setWatchList();
                } else {
                    if (responseEmpty.getResponseCode() == 4302) {
                        isloggedout = true;
                        logoutCall();
                       // showDialog(getActivity().getResources().getString(R.string.logged_out), responseEmpty.getDebugMessage() + "");
                    } else if (responseEmpty.getResponseCode() == 500) {
                        showDialog();
                    }
                }
            });
        }


    }

    public void hitApiAddWatchList() {
        if (context instanceof SeriesDetailActivity) {
            ((SeriesDetailActivity) context).seriesLoader();
        }
        bookmarkingViewModel.hitApiAddWatchList(token, assestId).observe(getActivity(), responseEmpty -> {
            getBinding().wProgressBar.setVisibility(View.GONE);
            getBinding().addIcon.setVisibility(View.VISIBLE);
            if (Objects.requireNonNull(responseEmpty).isStatus()) {
                setWatchList();
                AppCommonMethod.trackFcmCustomEvent(getActivity(), AppConstants.ADD_TO_WATCHLIST, seriesDetailBean.getAssetType(), "", "", 0, seriesDetailBean.getTitle(), 0, seriesDetailBean.getId()+"", 0, 0, "", "","","");

            } else {
                if (responseEmpty.getResponseCode() == 4302) {
                    isloggedout = true;
                    logoutCall();
                    //showDialog(getActivity().getResources().getString(R.string.logged_out), getResources().getString(R.string.you_are_logged_out));
                } else if (responseEmpty.getResponseCode() == 4904) {
                    setWatchList();
                    String debugMessage = responseEmpty.getDebugMessage();
                    //from value will bedepends on how user click of watchlist icon-->>if loggedout=2 else=2
                    if (from == 1) {
                        showDialog();
                    }
                } else if (responseEmpty.getResponseCode() == 500) {
                    showDialog();
                }
            }

        });


    }


    private void openShareDialogue() {
        String imgUrl = seriesDetailBean.getThumbnailImage();
        int id = seriesDetailBean.getId();
        String title = seriesDetailBean.getName();
        String assetType = "";
        if (context instanceof SeriesDetailActivity) {
            imgUrl = seriesDetailBean.getPosterURL();
            id = seriesDetailBean.getId();
            title = seriesDetailBean.getTitle();
            assetType = MediaTypeConstants.getInstance().getSeries();
            ((SeriesDetailActivity) context).seriesLoader();
        } else if (context instanceof EpisodeActivity) {
            imgUrl = seriesDetailBean.getPosterURL();
            id = seriesDetailBean.getId();
            title = seriesDetailBean.getTitle();
            assetType = MediaTypeConstants.getInstance().getEpisode();

        } else if (context instanceof InstructorActivity) {
            imgUrl = seriesDetailBean.getPosterURL();
            id = seriesDetailBean.getId();
            title = seriesDetailBean.getTitle();
            assetType = MediaTypeConstants.getInstance().getInstructor();

        } else if (context instanceof TutorialActivity) {
            imgUrl = seriesDetailBean.getPosterURL();
            id = seriesDetailBean.getId();
            title = seriesDetailBean.getTitle();
            assetType = MediaTypeConstants.getInstance().getTutorial();

        } else if (context instanceof ChapterActivity) {
            imgUrl = seriesDetailBean.getPosterURL();
            id = seriesDetailBean.getId();
            title = seriesDetailBean.getTitle();
            assetType = MediaTypeConstants.getInstance().getChapter();

        } else if (context instanceof ArticleActivity) {
            imgUrl = seriesDetailBean.getPosterURL();
            id = seriesDetailBean.getId();
            title = seriesDetailBean.getTitle();
            assetType = MediaTypeConstants.getInstance().getMovie();

        }
        else if (context instanceof LiveActivity) {
            imgUrl = seriesDetailBean.getPosterURL();
            id = seriesDetailBean.getId();
            title = seriesDetailBean.getTitle();
            assetType = MediaTypeConstants.getInstance().getLive();

        }
        else if (context instanceof ShowActivity) {
            imgUrl = seriesDetailBean.getPosterURL();
            id = seriesDetailBean.getId();
            title = seriesDetailBean.getTitle();
            assetType = MediaTypeConstants.getInstance().getShow();

        }
        if (SystemClock.elapsedRealtime() - mLastClickTime < 1000) {
            return;
        }
        mLastClickTime = SystemClock.elapsedRealtime();

        AppCommonMethod.openShareDialog(getActivity(), title, id, assetType, imgUrl, seriesDetailBean.getSeason());
        AppCommonMethod.trackFcmCustomEvent(getActivity(),AppConstants.SHARE_CONTENT, seriesDetailBean.getAssetType(), "", "", 0,seriesDetailBean.getTitle(), 0,seriesDetailBean.getId()+"", 0, 0, "", "","","");

        new Handler().postDelayed(() -> {
            if (context instanceof SeriesDetailActivity) {
                ((SeriesDetailActivity) context).dismissLoading(((SeriesDetailActivity) context).getBinding().progressBar);
            }
        }, 2000);
    }

   /* private AppConstants.ContentType getAssetType() {
        if (context instanceof SeriesDetailActivity) {
            return AppConstants.ContentType.SERIES;
        } else if (context instanceof EpisodeActivity) {
            return AppConstants.ContentType.EPISODE;
        } else {
            return AppConstants.ContentType.VIDEO;
        }
    }*/

    public boolean isLogin() {
        loginStatus = preference.getAppPrefLoginStatus();
        return loginStatus;
    }


    public void setLike() {
        getBinding().lProgressBar.setVisibility(View.GONE);
        getBinding().likeIcon.setVisibility(View.VISIBLE);
        likeCounter = 1;
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP) {
            // Do something for lollipop and above versions


            setLikeProperty();

        } else {

            setLikeProperty();
        }

    }

    private void setLikeProperty() {
        if (KsPreferenceKeys.getInstance().getCurrentTheme().equalsIgnoreCase(AppConstants.LIGHT_THEME)) {
            getBinding().tvLike.setTextColor(ContextCompat.getColor(getActivity(), R.color.more_text_color_dark));
            ImageViewCompat.setImageTintList(getBinding().likeIcon, ColorStateList.valueOf(getResources().getColor(R.color.more_text_color_dark)));
        } else {
            getBinding().tvLike.setTextColor(ContextCompat.getColor(getActivity(), R.color.more_text_color_dark));
            ImageViewCompat.setImageTintList(getBinding().likeIcon, ColorStateList.valueOf(getResources().getColor(R.color.more_text_color_dark)));

        }
    }

    public void resetLike() {
        getBinding().lProgressBar.setVisibility(View.GONE);
        getBinding().likeIcon.setVisibility(View.VISIBLE);
        likeCounter = 0;
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP) {
            if (KsPreferenceKeys.getInstance().getCurrentTheme().equalsIgnoreCase(AppConstants.LIGHT_THEME)) {
                getBinding().tvLike.setTextColor(ContextCompat.getColor(getActivity(), R.color.navy_blue));
                ImageViewCompat.setImageTintList(getBinding().likeIcon, ColorStateList.valueOf(getResources().getColor(R.color.navy_blue)));
            } else {
                getBinding().tvLike.setTextColor(ContextCompat.getColor(getActivity(), R.color.description_grey));
                ImageViewCompat.setImageTintList(getBinding().likeIcon, ColorStateList.valueOf(getResources().getColor(R.color.description_grey)));

            }

        } else {
            if (KsPreferenceKeys.getInstance().getCurrentTheme().equalsIgnoreCase(AppConstants.LIGHT_THEME)) {
                getBinding().tvLike.setTextColor(ContextCompat.getColor(getActivity(), R.color.navy_blue));
                ImageViewCompat.setImageTintList(getBinding().likeIcon, ColorStateList.valueOf(getResources().getColor(R.color.navy_blue)));
            } else {
                getBinding().tvLike.setTextColor(ContextCompat.getColor(getActivity(), R.color.description_grey));
                ImageViewCompat.setImageTintList(getBinding().likeIcon, ColorStateList.valueOf(getResources().getColor(R.color.description_grey)));

            }
        }

    }


    public void setWatchList() {
        getBinding().wProgressBar.setVisibility(View.GONE);
        getBinding().addIcon.setVisibility(View.VISIBLE);
        watchListCounter = 1;


        if (KsPreferenceKeys.getInstance().getCurrentTheme().equalsIgnoreCase(AppConstants.LIGHT_THEME)) {
            getBinding().addIcon.setImageResource(R.drawable.check_icon_navy_blue);

        } else {
            getBinding().addIcon.setImageResource(R.drawable.check_icon_navy_blue);

        }

        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP) {

            setTextColor();
            // Do something for lollipop and above versions

        } else {
            setTextColor();
            // do something for phones running an SDK before lollipop
            //  ImageViewCompat.setImageTintList(getBinding().addIcon, ColorStateList.valueOf(getResources().getColor(R.color.dialog_green_color)));
        }

    }

    private void setTextColor() {
        if (KsPreferenceKeys.getInstance().getCurrentTheme().equalsIgnoreCase(AppConstants.LIGHT_THEME)) {
            //  ImageViewCompat.setImageTintList(getBinding().addIcon, ColorStateList.valueOf(getResources().getColor(R.color.dialog_green_color)));
            getBinding().tvWatch.setTextColor(ContextCompat.getColor(getActivity(), R.color.more_text_color_dark));
        } else {
            getBinding().tvWatch.setTextColor(ContextCompat.getColor(getActivity(), R.color.more_text_color_dark));
        }
    }

    public void resetWatchList() {
        getBinding().wProgressBar.setVisibility(View.GONE);
        getBinding().addIcon.setVisibility(View.VISIBLE);
        watchListCounter = 0;

            if (KsPreferenceKeys.getInstance().getCurrentTheme().equalsIgnoreCase(AppConstants.LIGHT_THEME)) {
            getBinding().addIcon.setImageResource(R.drawable.add_to_watchlist_navy_blue);

        } else {
            getBinding().addIcon.setImageResource(R.drawable.add_to_watchlist);

        }


       /* if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP) {
            // Do something for lollipop and above versions
            setTextColor();
        } else {
            setTextColor();
        }*/

        if (KsPreferenceKeys.getInstance().getCurrentTheme().equalsIgnoreCase(AppConstants.LIGHT_THEME)) {
            //  ImageViewCompat.setImageTintList(getBinding().addIcon, ColorStateList.valueOf(getResources().getColor(R.color.dialog_green_color)));
            getBinding().tvWatch.setTextColor(ContextCompat.getColor(getActivity(), R.color.navy_blue));
        } else {
            getBinding().tvWatch.setTextColor(ContextCompat.getColor(getActivity(), R.color.description_grey));
        }

    }

    public void hideProgressBar() {
        if (context instanceof SeriesDetailActivity) {
            ((SeriesDetailActivity) context).isRailData = true;
            ((SeriesDetailActivity) context).stopShimmer();
            ((SeriesDetailActivity) context).dismissLoading(((SeriesDetailActivity) context).getBinding().progressBar);
        } else if (context instanceof EpisodeActivity) {
            ((EpisodeActivity)
                    context).dismissLoading(((EpisodeActivity) context).getBinding().progressBar);
            ((EpisodeActivity) context).isRailData = true;
            ((EpisodeActivity) context).stopShimmercheck();
        }
    }

    private void showDialog() {
        FragmentManager fm = getActivity().getSupportFragmentManager();
        AlertDialogSingleButtonFragment alertDialog = AlertDialogSingleButtonFragment.newInstance(title, message, getResources().getString(R.string.ok));
        alertDialog.setCancelable(false);
        alertDialog.setAlertDialogCallBack(this);
        alertDialog.show(fm, "fragment_alert");
    }

    @Override
    public void onFinishDialog() {
        if (isloggedout) {
            logoutCall();
        }
    }

    private void logoutCall() {
        if (CheckInternetConnection.isOnline(requireActivity())) {
            clearCredientials(preference);
            hitApiLogout(preference.getAppPrefAccessToken());
        } else {
            new ToastHandler(getActivity()).show(getActivity().getResources().getString(R.string.no_internet_connection));
        }
    }


    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.shareWith: {
                openShareDialogue();

            }
            break;
            case R.id.show_comments: {
            }
            break;
            case R.id.llLike: {
                likeClick();
            }
            break;
            case R.id.watchList: {
                watchListClick();
            }
            break;
            case R.id.download_video: {
                onDownloadClickInteraction.onDownloadClicked(entryid+"", 0, this);
            }
            break;
            case R.id.video_downloaded: {
                onDownloadClickInteraction.onDownloadCompleteClicked(view, this, entryid+"");
            }
            break;
            case R.id.series_downloaded: {
                onDownloadClickInteraction.onSeriesDownloadClicked(view, this, entryid+"");
            }
            break;
            case R.id.video_downloading: {
                onDownloadClickInteraction.onProgressbarClicked(view, this, entryid+"");
            }
            break;
            case R.id.pause_download: {
                onDownloadClickInteraction.onPauseClicked(entryid+"", this);
            }
            break;
            case R.id.trailor: {
                trailorCallBack.onClick();
                Intent intent = new Intent(getActivity(), PlayerActivity.class);
                intent.putExtra(AppConstants.ENTRY_ID, seriesDetailBean.getTrailerReferenceId());
                startActivity(intent);
            }
        }
    }

    public void setDownloadStatus() {
        if (getActivity()!=null && !getActivity().isFinishing()){
            if (KsPreferenceKeys.getInstance().getAppPrefLoginStatus()){
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        if (getBinding() != null)
                            getBinding().setDownloadStatus(downloadStatus);
                        try {
                            Log.w("userInteraction","in6"+downloadStatus);
                            if (downloadStatus==DownloadStatus.PAUSE){
                                getBinding().downloadText.setText(getActivity().getResources().getString(R.string.Resume));
                                getBinding().downloadText.setTextColor(getActivity().getResources().getColor(R.color.subtitlecolor));
                            }else if (downloadStatus==DownloadStatus.DOWNLOADING){
                                getBinding().downloadText.setText(getActivity().getResources().getString(R.string.Downloading));
                                getBinding().downloadText.setTextColor(getActivity().getResources().getColor(R.color.subtitlecolor));
                            }else if (downloadStatus==DownloadStatus.started){
                                getBinding().downloadText.setText(getActivity().getResources().getString(R.string.Downloading));
                                getBinding().downloadText.setTextColor(getActivity().getResources().getColor(R.color.subtitlecolor));
                            }
                            else if (downloadStatus==DownloadStatus.DOWNLOADED){
                                Log.w("userInteraction","in5");
                                getBinding().downloadText.setText(getActivity().getResources().getString(R.string.Downloaded));
                                getBinding().downloadText.setTextColor(getActivity().getResources().getColor(R.color.more_text_color_dark));
                            }
                            else if (downloadStatus==DownloadStatus.REQUESTED){

                            }
                            else if (downloadStatus==DownloadStatus.SERIES_DOWNLOADING){
                                getBinding().downloadText.setText(getActivity().getResources().getString(R.string.Downloading));
                                getBinding().downloadText.setTextColor(getActivity().getResources().getColor(R.color.subtitlecolor));
                            }
                            else {
                                if (getActivity()!=null && getActivity() instanceof SeriesDetailActivity){
                                    getBinding().downloadText.setText(getActivity().getResources().getString(R.string.download_all));
                                }
                                else if (getActivity()!=null && getActivity() instanceof TutorialActivity){
                                    getBinding().downloadText.setText(getActivity().getResources().getString(R.string.download_all));
                                }else {
                                    getBinding().downloadText.setText(getActivity().getResources().getString(R.string.download));
                                }

                                getBinding().downloadText.setTextColor(getActivity().getResources().getColor(R.color.subtitlecolor));
                            }

                        }catch (Exception e){
                            Log.e("crashhappen",e.toString());
                        }
                    }
                });

            }else {
                getBinding().downloadText.setText(getActivity().getResources().getString(R.string.download));
                getBinding().downloadText.setTextColor(getActivity().getResources().getColor(R.color.subtitlecolor));
            }
        }
    }

    public void setDownloadable() {
        if (getBinding() != null){
            getBinding().setIsDownloadable(isDownloadable);
        }

    }

    public void setDownloadProgress() {
        try {
            if (getBinding() != null){
                Log.w("userInteraction","in2");
                if (getActivity()!=null && !getActivity().isFinishing()){
                    Log.w("userInteraction","in3");
                    getActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            if (getActivity() instanceof SeriesDetailActivity || getActivity() instanceof TutorialActivity){
                                Log.w("userInteraction","in4");
                                getBinding().videoDownloaded.setImageResource(R.drawable.series_downloading_icon);
                                getBinding().downloadText.setText(getActivity().getResources().getString(R.string.Downloading));
                                getBinding().downloadText.setTextColor(getActivity().getResources().getColor(R.color.subtitlecolor));
                            }else {
                                Log.w("userInteraction","in5");
                                getBinding().videoDownloading.setProgress(progress);
                            }
                        }
                    });

                }else {
                    Log.w("userInteraction","in6");
                    getBinding().videoDownloading.setProgress(progress);
                }
            }

        }catch (Exception ignored){

        }
    }

    public boolean itemFound=false;
    ArrayList list;
    public void checkDownloadStatus() {
        Log.w("itemFoundValue-->",itemFound+"");
        if (adapterList!=null && adapterList.size()>0){
            if (!itemFound) {
                list=new ArrayList();
                for (int i = 0; i < adapterList.size(); i++) {
                    EnveuVideoItemBean videoItemBean = adapterList.get(i);
                    String entryid = videoItemBean.getkEntryId();
                    if (downloadHelper != null) {
                        OfflineManager.AssetInfo info = downloadHelper.getManager().getAssetInfo(entryid);
                        Log.w("itemFoundValue-->",info+"");
                        if (info != null) {
                            list.add(info);
                        }
                    }
                }

                itemFound=true;
                Log.w("adapterListSize",adapterList.size() +" "+list.size());
                if (list.size()==adapterList.size()){
                    setDownloadStatus();
                }else {
                    setDownloadStatus();
                }
            }
        }
    }

    public boolean itemCheck=false;
    ArrayList itemList;
    ArrayList completeItemList;
    public void checkSeriesDownloadStatus() {
        Log.w("itemChecked 1",adapterList.size()+"");
        if (adapterList!=null && adapterList.size()>0){
            Log.w("itemChecked 2",itemCheck+"");
            if (!itemCheck) {
                itemList=new ArrayList();
                completeItemList=new ArrayList();
                for (int i = 0; i < adapterList.size(); i++) {
                    EnveuVideoItemBean videoItemBean = adapterList.get(i);
                    String entryid = videoItemBean.getkEntryId();
                   // Log.w("itemChecked 3",entryid+" "+downloadHelper);
                    if (downloadHelper != null) {
                        OfflineManager.AssetInfo info = downloadHelper.getManager().getAssetInfo(entryid);
                       // Log.w("itemChecked 4",info+"");
                        if (info!=null){
                            Log.w("itemChecked 4",info.getState()+"");
                            if(info.getState().name().equalsIgnoreCase("none")){

                            }
                            else if (info.getState().name().equalsIgnoreCase("completed")){
                                completeItemList.add(info);
                                itemList.add(info);
                            }
                            else if (info.getState().name().equalsIgnoreCase("failed")){

                            }else {
                                itemList.add(info);

                            }
                        }

                    }
                }

                Log.w("itemChecked 4",itemList.size()+" "+adapterList.size()+"  "+completeItemList.size());
                if (completeItemList.size()==adapterList.size()){
                    setDownloadStatus();
                }else if (itemList.size()==adapterList.size()){
                    setDownloadStatus();
                }else {
                    setDownloadStatus();
                }
                /*if (!itemCheck){
                    setDownloadStatus(DownloadStatus.START);
                }*/
            }
        }
    }

    public void setItemFound() {
        this.itemFound=false;
    }

    public boolean isItemCheck() {
        return itemCheck=false;
    }
}
