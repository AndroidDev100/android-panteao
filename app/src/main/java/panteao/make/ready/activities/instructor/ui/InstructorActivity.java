package panteao.make.ready.activities.instructor.ui;

import android.app.AlertDialog;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.graphics.Typeface;
import android.os.Bundle;
import android.os.Handler;
import android.os.SystemClock;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.widget.ImageViewCompat;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.ViewPager;
import panteao.make.ready.activities.downloads.NetworkHelper;
import panteao.make.ready.activities.instructor.IRecommendationRailFragment;
import panteao.make.ready.activities.instructor.ISeasonTabFragment;
import panteao.make.ready.activities.series.viewmodel.SeriesViewModel;
import panteao.make.ready.activities.usermanagment.ui.LoginActivity;
import panteao.make.ready.baseModels.BaseBindingActivity;
import panteao.make.ready.beanModel.AssetHistoryContinueWatching.ItemsItem;
import panteao.make.ready.callbacks.commonCallbacks.TrailorCallBack;
import panteao.make.ready.enums.KalturaImageType;
import panteao.make.ready.networking.apistatus.APIStatus;
import panteao.make.ready.SDKConfig;
import panteao.make.ready.beanModel.enveuCommonRailData.RailCommonData;
import panteao.make.ready.utils.MediaTypeConstants;
import panteao.make.ready.utils.config.ImageLayer;
import panteao.make.ready.utils.constants.SharedPrefesConstants;
import panteao.make.ready.utils.helpers.ImageHelper;
import panteao.make.ready.utils.helpers.SharedPrefHelper;
import panteao.make.ready.utils.helpers.downloads.OnDownloadClickInteraction;
import panteao.make.ready.utils.helpers.downloads.VideoListListener;
import panteao.make.ready.R;
import panteao.make.ready.adapters.player.EpisodeTabAdapter;
import panteao.make.ready.beanModel.AppUserModel;
import panteao.make.ready.beanModelV3.uiConnectorModelV2.EnveuVideoItemBean;
import panteao.make.ready.beanModel.selectedSeason.SelectedSeasonModel;
import panteao.make.ready.databinding.ActivitySeriesDetailBinding;
import panteao.make.ready.fragments.dialog.AlertDialogFragment;
import panteao.make.ready.fragments.dialog.AlertDialogSingleButtonFragment;
import panteao.make.ready.fragments.player.ui.CommentsFragment;
import panteao.make.ready.fragments.player.ui.UserInteractionFragment;
import panteao.make.ready.utils.commonMethods.AppCommonMethod;
import panteao.make.ready.utils.constants.AppConstants;
import panteao.make.ready.utils.cropImage.helpers.Logger;
import panteao.make.ready.utils.cropImage.helpers.NetworkConnectivity;
import panteao.make.ready.utils.helpers.RailInjectionHelper;

import panteao.make.ready.utils.helpers.ToolBarHandler;
import panteao.make.ready.utils.helpers.intentlaunchers.ActivityLauncher;
import com.google.android.material.tabs.TabLayout;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.kaltura.tvplayer.OfflineManager;

import panteao.make.ready.utils.helpers.ksPreferenceKeys.KsPreferenceKeys;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;


import static com.google.android.material.tabs.TabLayout.INDICATOR_GRAVITY_BOTTOM;

public class InstructorActivity extends BaseBindingActivity<ActivitySeriesDetailBinding> implements AlertDialogFragment.AlertDialogListener, OnDownloadClickInteraction, VideoListListener, TrailorCallBack {
    private final String TAG = this.getClass().getSimpleName();
    public String userName = "";
    public boolean isloggedout = false;
    private int seriesId, count,assestId;
    private long mLastClickTime = 0;
    private int watchListId = 0;
    private SeriesViewModel viewModel;
    private KsPreferenceKeys preference;
    private String token;
    private int watchListCounter = 0;
    private int likeCounter = 0;
    private boolean isLogin;
    private int shimmerCounter = 0;
    RailInjectionHelper railInjectionHelper;
    EnveuVideoItemBean seriesDetailBean;
    List<ItemsItem> assetListContinue;
    private CommentsFragment commentsFragment;
    private ISeasonTabFragment seasonTabFragment;
    private AlertDialog alertDialog;
    private AlertDialog.Builder builder;
    private IRecommendationRailFragment railFragment;
    private EpisodeTabAdapter episodeTabAdapter;
    private boolean newIntentCall = false;
    public boolean isSeasonData = false;
    public boolean isRailData = false;
    private UserInteractionFragment userInteractionFragment;
    private final boolean mFlag = false;

    @Override
    public ActivitySeriesDetailBinding inflateBindingLayout() {
        return ActivitySeriesDetailBinding.inflate(inflater);
    }

    private String tabId;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setBackgroundDrawableResource(R.color.black);
       /* getWindow().setFlags(WindowManager.LayoutParams.FLAG_SECURE,
                WindowManager.LayoutParams.FLAG_SECURE);*/
        shimmerCounter = 0;

        if (SDKConfig.getInstance().getInstructorDetaildId().equalsIgnoreCase("")) {
            tabId = "10000";
        } else {
            tabId = SDKConfig.getInstance().getInstructorDetaildId();
        }

        setupUI(getBinding().llParent);
        seriesId = getIntent().getIntExtra("seriesId", 0);
        if (getIntent().hasExtra(AppConstants.BUNDLE_ASSET_BUNDLE)) {
            Bundle extras = getIntent().getExtras();
            if (extras != null) {
                extras = extras.getBundle(AppConstants.BUNDLE_ASSET_BUNDLE);
                assestId = Objects.requireNonNull(extras).getInt(AppConstants.BUNDLE_ASSET_ID);
               // tabId = extras.getString(AppConstants.BUNDLE_DETAIL_TYPE, AppConstants.MOVIE_ENVEU);
//                downloadHelper = new DownloadHelper(this, this, AppConstants.ContentType.VIDEO.name());
                //  downloadHelper.findVideo(String.valueOf(brightCoveVideoId));
            }
        } else {
            throw new IllegalArgumentException("Activity cannot find  extras " + "Search_Show_All");
        }
        new ToolBarHandler(this).setSeriesAction();

        onSeriesCreate();
    }

    @Override
    protected void onPause() {
        dismissLoading(getBinding().progressBar);
        super.onPause();
    }

    public void onSeriesCreate() {
        if (shimmerCounter == 0) {
            callShimmer();
        }
        connectionObserver();
    }


    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        shimmerCounter = 0;
        setupUI(getBinding().llParent);
        seriesId = intent.getIntExtra("seriesId", 0);
        new ToolBarHandler(this).setSeriesAction();
        newIntentCall = true;
        onSeriesCreate();
    }

    private void callShimmer() {
        shimmerCounter = 1;
        getBinding().seriesShimmer.setVisibility(View.VISIBLE);
        getBinding().mShimmer.seriesShimmerScroll1.setEnabled(false);
        getBinding().mShimmer.seriesShimmerScroll2.setEnabled(false);
        getBinding().mShimmer.seriesShimmerScroll2.setEnabled(false);
        getBinding().llParent.setVisibility(View.GONE);
        getBinding().noConnectionLayout.setVisibility(View.GONE);
        getBinding().mShimmer.sfShimmer1.startShimmer();
        getBinding().mShimmer.sfShimmer2.startShimmer();
        getBinding().mShimmer.sfShimmer3.startShimmer();
        getBinding().mShimmer.flBackIconImage.bringToFront();
        getBinding().mShimmer.flBackIconImage.setOnClickListener(v -> onBackPressed());


    }

    public void stopShimmer() {
        if (isSeasonData && isRailData) {
            isSeasonData = false;
            isRailData = false;
            getBinding().seriesShimmer.setVisibility(View.GONE);
            getBinding().llParent.setVisibility(View.VISIBLE);
            getBinding().noConnectionLayout.setVisibility(View.GONE);
            getBinding().mShimmer.sfShimmer1.startShimmer();
            getBinding().mShimmer.sfShimmer2.startShimmer();
            getBinding().mShimmer.sfShimmer3.startShimmer();
            Animation aniFade = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.fade_in);
            getBinding().llParent.startAnimation(aniFade);
            setExpandable();
        }

    }

    public void numberOfEpisodes() {
        if (size == 1) {
            setCustomeFields();
            // getBinding().vodCount.setText(size + " " + getResources().getString(R.string.episode));
        } else {
            setCustomeFields();
            //getBinding().vodCount.setText(size + " " + getResources().getString(R.string.episodes));
        }
    }

    private void setCustomeFields() {
        try {
            getBinding().vodCount.setText("");
            if (responseDetailPlayer.getParentalRating() != null && !responseDetailPlayer.getParentalRating().equalsIgnoreCase("")) {
                getBinding().vodCount.setText(responseDetailPlayer.getParentalRating() + " \u2022");
            }

            if (size > 0) {
                getBinding().vodCount.setText(getBinding().vodCount.getText().toString() + " " + size + " " + episode + " \u2022");
            }


            if (responseDetailPlayer.getCountry() != null && !responseDetailPlayer.getCountry().equalsIgnoreCase("")) {
                getBinding().vodCount.setText(getBinding().vodCount.getText().toString() + " " + responseDetailPlayer.getCountry() + " \u2022");
            }

            if (responseDetailPlayer.getCompany() != null && !responseDetailPlayer.getCompany().equalsIgnoreCase("")) {
                getBinding().vodCount.setText(getBinding().vodCount.getText().toString() + " " + responseDetailPlayer.getCompany() + " \u2022");
            }

            if (responseDetailPlayer.getYear() != null && !responseDetailPlayer.getYear().equalsIgnoreCase("")) {
                getBinding().vodCount.setText(getBinding().vodCount.getText().toString() + " " + responseDetailPlayer.getYear() + " \u2022");
            }

            if (getBinding().vodCount.getText().toString().trim().endsWith("\u2022")) {
                String customeF = getBinding().vodCount.getText().toString().substring(0, getBinding().vodCount.getText().toString().length() - 1);
                getBinding().vodCount.setText(customeF);
            }
            if (getBinding().vodCount.getText().toString().trim().equalsIgnoreCase("")) {
                // getBinding().customeFieldView.setVisibility(View.GONE);
            }
        } catch (Exception ignored) {

        }

    }


    private void modelCall() {
        railInjectionHelper = new ViewModelProvider(this).get(RailInjectionHelper.class);
        assetListContinue = new ArrayList<>();
        getBinding().noConnectionLayout.setVisibility(View.GONE);
        getBinding().player.setVisibility(View.VISIBLE);
        // getBinding().playerFooter.setSmoothScrollingEnabled(true);
        getBinding().playerFooter.setVisibility(View.VISIBLE);
        getBinding().flBackIconImage.setVisibility(View.VISIBLE);
        getBinding().bannerFrame.bringToFront();
        getBinding().backImage.bringToFront();
        getBinding().flBackIconImage.bringToFront();
        preference = KsPreferenceKeys.getInstance();
        isLogin = preference.getAppPrefLoginStatus();
        token = preference.getAppPrefAccessToken();
        viewModel = new ViewModelProvider(this).get(SeriesViewModel.class);
        if (isLogin) {
            AppUserModel signInResponseModel = AppUserModel.getInstance();
            if (signInResponseModel != null) {
                {
                    userName = signInResponseModel.getName();
                }

            }
        }
    }

    private void connectionObserver() {
        if (NetworkConnectivity.isOnline(this)) {
            connectionValidation();
        } else {
            connectionValidation();
        }
    }

    private void connectionValidation() {
        if (aBoolean) {
            getSeriesDetail();
        } else {
            noConnectionLayout();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        dismissLoading(getBinding().progressBar);
        watchListCounter = 0;
        likeCounter = 0;
        AppCommonMethod.isSeriesPage = true;
        if (NetworkConnectivity.isOnline(this)) {
            Logger.e("SeriesDetailActivity", "isOnline");
        } else {
            noConnectionLayout();
        }

        if (preference != null && userInteractionFragment != null) {
            AppCommonMethod.callSocialAction(preference, userInteractionFragment);
        }
    }

    private void getSeriesDetail() {
        modelCall();
        postCommentClick();
        RailInjectionHelper railInjectionHelper = new ViewModelProvider(this).get(RailInjectionHelper.class);
        railInjectionHelper.getAssetDetailsV2(String.valueOf(assestId)).observe(InstructorActivity.this, assetResponse -> {
            if (assetResponse != null) {
                if (assetResponse.getStatus().equalsIgnoreCase(APIStatus.START.name())) {

                } else if (assetResponse.getStatus().equalsIgnoreCase(APIStatus.SUCCESS.name())) {
                    RailCommonData enveuCommonResponse = (RailCommonData) assetResponse.getBaseCategory();
                    parseSeriesData();
                } else if (assetResponse.getStatus().equalsIgnoreCase(APIStatus.ERROR.name())) {
                    if (assetResponse.getErrorModel() != null && assetResponse.getErrorModel().getErrorCode() != 0) {
                        showDialog();
                    }

                } else if (assetResponse.getStatus().equalsIgnoreCase(APIStatus.FAILURE.name())) {
                    showDialog();
                }
            }

        });
        /*railInjectionHelper.getSeriesDetails(String.valueOf(seriesId)).observe(SeriesDetailActivity.this, enveuCommonResponse -> {
            if (enveuCommonResponse != null && enveuCommonResponse.getEnveuVideoItemBeans().size() > 0) {
                if (enveuCommonResponse.getEnveuVideoItemBeans().get(0).getResponseCode() == AppConstants.RESPONSE_CODE_SUCCESS) {
                    Logger.e("enveuCommonResponse", "" + enveuCommonResponse.getEnveuVideoItemBeans().get(0).toString());
                    seriesDetailBean = enveuCommonResponse.getEnveuVideoItemBeans().get(0);
                    seriesId = seriesDetailBean.getId();
                    setUserInteractionFragment(seriesId);
                    setTabs();
                    setUiComponents(seriesDetailBean);
                    downloadHelper = new DownloadHelper(this, this);
                    downloadHelper.setAssetType(AppConstants.ContentType.EPISODE);

//                    downloadHelper.findVideo(seriesDetailBean.getBrightcoveVideoId());
                } else {
                    if (enveuCommonResponse.getEnveuVideoItemBeans().get(0).getResponseCode() == AppConstants.RESPONSE_CODE_LOGOUT) {
                        showDialog(SeriesDetailActivity.this.getResources().getString(R.string.error), getResources().getString(R.string.logged_out));
                    } else {
                        showDialog(SeriesDetailActivity.this.getResources().getString(R.string.error), getResources().getString(R.string.something_went_wrong));
                    }
                }
            }
        });*/

        getBinding().flBackIconImage.setOnClickListener(view -> onBackPressed());


    }

    private void parseSeriesData() {
        if (enveuCommonResponse != null && enveuCommonResponse.getEnveuVideoItemBeans().size() > 0) {
            Logger.e("enveuCommonResponse", "" + enveuCommonResponse.getEnveuVideoItemBeans().get(0).toString());
            seriesDetailBean = enveuCommonResponse.getEnveuVideoItemBeans().get(0);
            seriesId = seriesDetailBean.getId();
            setUserInteractionFragment();
            setTabs();
            setUiComponents();
//            downloadHelper = new DownloadHelper(this, this);
//            downloadHelper.setAssetType(MediaTypeConstants.getInstance().getEpisode());
//            downloadHelper.setSeriesName(seriesDetailBean.getTitle());

            //downloadHelper.findVideo(seriesDetailBean.getBrightcoveVideoId());
        } else {
            if (enveuCommonResponse.getEnveuVideoItemBeans().get(0).getResponseCode() == AppConstants.RESPONSE_CODE_LOGOUT) {
                if (isLogin) {
                    hitApiLogout();
                }
                // showDialog(SeriesDetailActivity.this.getResources().getString(R.string.error), getResources().getString(R.string.logged_out));
            } else {
                showDialog();
            }
        }
    }


    public void setUserInteractionFragment() {
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        Bundle args = new Bundle();
        args.putInt(AppConstants.BUNDLE_ASSET_ID, id);
        args.putParcelable(AppConstants.BUNDLE_SERIES_DETAIL, seriesDetailBean);

        userInteractionFragment = new UserInteractionFragment();
        userInteractionFragment.setArguments(args);
        transaction.replace(R.id.fragment_user_interaction, userInteractionFragment);
        transaction.addToBackStack(null);
        transaction.commit();
//        downloadHelper = new DownloadHelper(this, this, String.valueOf(seriesId), seriesDetailBean.getTitle(), MediaTypeConstants.getInstance().getEpisode(), seriesDetailBean);
        userInteractionFragment.setDownloadable();
    }

    public void setTabs() {
        if (newIntentCall) {
            newIntentCall = false;
            Bundle args = new Bundle();
            args.putString(AppConstants.BUNDLE_TAB_ID, tabId);
            railFragment.setArguments(args);


            Bundle bundleSeason = new Bundle();
            bundleSeason.putInt(AppConstants.BUNDLE_ASSET_ID, seriesId);
            bundleSeason.putParcelableArrayList(AppConstants.BUNDLE_SEASON_ARRAY, seriesDetailBean.getSeasons());

            bundleSeason.putInt(AppConstants.BUNDLE_SEASON_COUNT, seriesDetailBean.getSeasonCount());
            seasonTabFragment.setArguments(bundleSeason);

            getSupportFragmentManager().beginTransaction().detach(seasonTabFragment).attach(seasonTabFragment).commit();
            getSupportFragmentManager().beginTransaction().detach(railFragment).attach(railFragment).commit();

            TabLayout.Tab tab = getBinding().tabLayout.getTabAt(0);
            tab.select();
        } else {
            railFragment = new IRecommendationRailFragment();
            seasonTabFragment = new ISeasonTabFragment();
            getBinding().tabLayout.setSelectedTabIndicatorGravity(INDICATOR_GRAVITY_BOTTOM);
            episodeTabAdapter = new EpisodeTabAdapter(getSupportFragmentManager());

            Bundle args = new Bundle();
            args.putString(AppConstants.BUNDLE_TAB_ID, tabId);
            railFragment.setArguments(args);

            Bundle bundleSeason = new Bundle();
            bundleSeason.putInt(AppConstants.BUNDLE_ASSET_ID, seriesId);
            bundleSeason.putParcelableArrayList(AppConstants.BUNDLE_SEASON_ARRAY, seriesDetailBean.getSeasons());
            bundleSeason.putInt(AppConstants.BUNDLE_SEASON_COUNT, seriesDetailBean.getSeasonCount());
            seasonTabFragment.setArguments(bundleSeason);

//            episodeTabAdapter.addFragment(seasonTabFragment, getString(R.string.tab_heading_episodes));
            episodeTabAdapter.addFragment(seasonTabFragment, getString(R.string.tab_heading_related_content));

            episodeTabAdapter.addFragment(railFragment, getString(R.string.tab_heading_other));
            getBinding().viewPager.setAdapter(episodeTabAdapter);
            getBinding().viewPager.setOffscreenPageLimit(10);
            getBinding().tabLayout.setupWithViewPager(getBinding().viewPager);
            //AppCommonMethod.customTabWidth(getBinding().tabLayout);
            //AppCommonMethod.customTabWidth2(getBinding().tabLayout);

//            downloadHelper.getAllEpisodesOfSeries(seriesDetailBean.getBrightcoveVideoId(), String.valueOf(seasonTabFragment.getSelectedSeason())).observe(this, new Observer<ArrayList<DownloadedEpisodes>>() {
//                @Override
//                public void onChanged(ArrayList<DownloadedEpisodes> downloadedEpisodes) {
//                    downloadableEpisodes = downloadedEpisodes;
//
//                    if (downloadedEpisodes.size() > 0) {
//                        userInteractionFragment.setDownloadable(true);
//                        setStatus();
//                    } else
//                        userInteractionFragment.setDownloadable(false);
//                }
//            });
        }

        getBinding().tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                showLoading(getBinding().progressBar);
                new Handler().postDelayed(() -> dismissLoading(getBinding().progressBar), 1500);
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });

        getBinding().viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
            }

            @Override
            public void onPageSelected(int position) {
                getBinding().viewPager.measure(getBinding().viewPager.getMeasuredWidth(), getBinding().viewPager.getMeasuredHeight());
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });


    }

    private void setStatus() {
//        for (DownloadedEpisodes episodes : downloadableEpisodes) {
//            downloadHelper.getDownloadStatus(episodes.getVideoId(), new OfflineCallback<DownloadStatus>() {
//                @Override
//                public void onSuccess(DownloadStatus downloadStatus) {
//                    if (!mFlag) {
//                        switch (downloadStatus.getCode()) {
//                            case DownloadStatus.STATUS_DOWNLOADING: {
//                                userInteractionFragment.setDownloadStatus(panteao.make.ready.enums.DownloadStatus.DOWNLOADING);
//                                mFlag = true;
//                            }
//                            break;
//                        }

//                    }
//                }
//
//                @Override
//                public void onFailure(Throwable throwable) {
//
//                }
//            });
//        }
    }


    public void seriesLoader() {
        showLoading(getBinding().progressBar);
    }

    public void removeTab() {
        if (getBinding().tabLayout.getTabCount() >= 1 && position <= getBinding().tabLayout.getTabCount()) {
            episodeTabAdapter.removeTabPage(position);
            ViewGroup.LayoutParams params = getBinding().tabLayout.getLayoutParams();
            params.width = WindowManager.LayoutParams.MATCH_PARENT;
            getBinding().tabLayout.setLayoutParams(params);

        }
    }


    private void setUiComponents() {
        if (seriesResponse != null) {
            setCustomeFields();
            if (seriesResponse.getAssetCast().size() > 0) {
                StringBuilder stringBuilder = new StringBuilder();
                for (int i = 0; i < seriesResponse.getAssetCast().size(); i++) {
                    if (i == seriesResponse.getAssetCast().size() - 1) {
                        stringBuilder = stringBuilder.append(seriesResponse.getAssetCast().get(i));

                    } else
                        stringBuilder = stringBuilder.append(seriesResponse.getAssetCast().get(i)).append(", ");
                }
                getBinding().setCasttext(" " + stringBuilder);
            } else {
                getBinding().llCastView.setVisibility(View.GONE);
            }
            if (seriesResponse.getAssetGenres().size() > 0) {
                StringBuilder stringBuilder = new StringBuilder();
                for (int i = 0; i < seriesResponse.getAssetGenres().size(); i++) {
                    if (i == seriesResponse.getAssetGenres().size() - 1) {
                        stringBuilder = stringBuilder.append(seriesResponse.getAssetGenres().get(i));
                    } else
                        stringBuilder = stringBuilder.append(seriesResponse.getAssetGenres().get(i)).append(", ");
                }
                getBinding().setCrewtext(" " + stringBuilder);
            } else {
                getBinding().llCrewView.setVisibility(View.GONE);
            }
            Logger.e("SeriesResponse", new Gson().toJson(seriesResponse));

            getBinding().setPlaylistItem(seriesResponse);
            if (seriesResponse.getImages()!=null && seriesResponse.getImages().size()>0){
                ImageHelper.getInstance(InstructorActivity.this).loadListImage(getBinding().sliderImage, ImageLayer.getInstance().getFilteredImage(seriesResponse.getImages(),  KalturaImageType.LANDSCAPE, 800, 450));
            }

            getBinding().bannerlabel.setText(seriesResponse.getName());
            getBinding().bannerlabel.setVisibility(View.INVISIBLE);
            getBinding().seriesTitle.setText(seriesResponse.getTitle());
           /* if (seriesResponse.getSeasonCount() == 0) {
                if (seriesResponse.getVodCount() == 1) {
                    getBinding().vodCount.setText(seriesResponse.getVodCount() + " " + getResources().getString(R.string.episode));
                } else {
                    getBinding().vodCount.setText(seriesResponse.getVodCount() + " " + getResources().getString(R.string.episodes));
                }
            } else {
                if (seriesResponse.getSeasonCount() == 1) {
                    getBinding().vodCount.setText(+seriesResponse.getSeasonCount() + " " + getResources().getString(R.string.season));
                } else {

                    getBinding().vodCount.setText(+seriesResponse.getSeasonCount() + " " + getResources().getString(R.string.seasons));
                }
            }*/
//            getBinding().seriesCount.setVisibility(View.GONE);
            if (seriesResponse.getDescription()!=null){
                if (seriesResponse.getDescription().equalsIgnoreCase("")){
                    getBinding().descriptionText.setVisibility(View.GONE);
                    getBinding().textExpandable.setVisibility(View.GONE);
                }
            }else {
                getBinding().descriptionText.setVisibility(View.GONE);
                getBinding().textExpandable.setVisibility(View.GONE);
            }
            getBinding().setResponseApi(seriesResponse.getDescription().trim());
            count = 0;

            getBinding().interactionSection.llLike.setOnClickListener(view -> {
                if (preference.getAppPrefLoginStatus()) {
                    showLoading(getBinding().progressBar);

                    if (likeCounter == 0)
                        hitApiAddLike();
                    else
                        hitApiRemoveLike();
                } else {
                    openLogin();
                }
            });

            getBinding().interactionSection.watchList.setOnClickListener(view -> {
                if (preference.getAppPrefLoginStatus()) {
                    if (watchListCounter == 0) {
                        hitApiAddWatchList();
                    }
                    else
                        hitApiRemoveList();
                } else {
                    openLogin();
                }
            });

            getBinding().interactionSection.shareWith.setOnClickListener(view -> {
                Logger.e("share","share1");
                showLoading(getBinding().progressBar);
                if (SystemClock.elapsedRealtime() - mLastClickTime < 900) {
                    Logger.e("share","share2");
                    return;
                }
                mLastClickTime = SystemClock.elapsedRealtime();



                openShareDialogue();
            });
            if (seriesResponse!=null && seriesResponse.getTrailerReferenceId()!=null && seriesResponse.getTrailerReferenceId()!=""){
             //   getBinding().trailor.setVisibility(View.VISIBLE);
                Logger.e("TrailerReferenceId",seriesResponse.getTrailerReferenceId());
            }else {
               // getBinding().trailor.setVisibility(View.GONE);
            }

        }
    }

    private void setExpandable() {
        getBinding().expandableLayout.collapse();
        if (getBinding().descriptionText.isExpanded()) {
            getBinding().descriptionText.setEllipsize(null);
        } else {
            getBinding().descriptionText.setEllipsize(TextUtils.TruncateAt.END);
        }
        getBinding().setExpandabletext(getResources().getString(R.string.more));
        getBinding().expandableLayout.setOnExpansionUpdateListener(expansionFraction -> getBinding().lessButton.setRotation(0 * expansionFraction));
        getBinding().lessButton.setOnClickListener(view -> clickExpandable());
    }

    public void clickExpandable() {
        getBinding().descriptionText.toggle();
        getBinding().descriptionText.setEllipsis("...");
        if (getBinding().descriptionText.isExpanded()) {
            getBinding().descriptionText.setEllipsize(null);
        } else {
            getBinding().descriptionText.setEllipsize(TextUtils.TruncateAt.END);
        }

        if (getBinding().expandableLayout.isExpanded()) {
            getBinding().setExpandabletext(getResources().getString(R.string.more));

        } else {
            getBinding().setExpandabletext(getResources().getString(R.string.less));
        }
        if (view != null) {
            getBinding().expandableLayout.expand();
        }
        getBinding().expandableLayout.collapse();

    }

    public void hitApiIsWatchList() {
        JsonObject requestParam = new JsonObject();
        requestParam.addProperty(AppConstants.API_PARAM_CONTENT_ID, seriesId);
        requestParam.addProperty(AppConstants.API_PARAM_CONTENT_TYPE, "SERIES");

        viewModel.hitApiIsWatchList(token, requestParam).observe(InstructorActivity.this, responseContentInWatchlist -> {
            if (responseContentInWatchlist.isStatus()) {
                watchListId = responseContentInWatchlist.getData().getId();
                setWatchList();
            } else {
                resetWatchList();
                if (responseContentInWatchlist.getResponseCode() == 401) {
                    isloggedout = true;
                    if (isLogin) {
                        hitApiLogout();
                    }
                    //   showDialog(SeriesDetailActivity.this.getResources().getString(R.string.logged_out), getResources().getString(R.string.you_are_logged_out));

                }
            }
        });

    }

    @Override
    protected void onStop() {
        super.onStop();
        AppCommonMethod.isSeriesPage = false;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        AppCommonMethod.seasonId = -1;
        preference.setAppPrefAssetId(0);
        preference.setAppPrefJumpTo("");
        preference.setAppPrefBranchIo(false);
    }


    public void hitApiAddLike() {

        JsonObject requestParam = new JsonObject();
        requestParam.addProperty(AppConstants.API_PARAM_LIKE_ID, seriesId);
        requestParam.addProperty(AppConstants.API_PARAM_LIKE_TYPE, MediaTypeConstants.getInstance().getSeries());
        viewModel.hitApiAddLike(token, requestParam).observe(InstructorActivity.this, responseContentInWatchlist -> {
            dismissLoading(getBinding().progressBar);
            if (responseContentInWatchlist.isStatus()) {
                setLike();
            } else {
                if (responseContentInWatchlist.getResponseCode() == 401) {
                    isloggedout = true;
                    if (isLogin) {
                        hitApiLogout();
                    }
                    // showDialog(SeriesDetailActivity.this.getResources().getString(R.string.logged_out), getResources().getString(R.string.you_are_logged_out));
                }
            }
        });

    }

    public void hitApiIsLike() {
        JsonObject requestParam = new JsonObject();
        requestParam.addProperty(AppConstants.API_PARAM_LIKE_ID, seriesId);
        requestParam.addProperty(AppConstants.API_PARAM_LIKE_TYPE, MediaTypeConstants.getInstance().getSeries());
        viewModel.hitApiIsLike(token, requestParam).observe(InstructorActivity.this, responseContentInWatchlist -> {
            if (responseContentInWatchlist.isStatus()) {
                if (responseContentInWatchlist.getData().isIsLike()) {
                    setLike();
                } else {
                    resetLike();
                }
            } else {
               /* if (responseContentInWatchlist.getResponseCode() == 401) {
                    isloggedout = true;
                    showDialog(SeriesDetailActivity.this.getResources().getString(R.string.logged_out), getResources().getString(R.string.you_are_logged_out));
                }*/
            }
        });
    }

    public void setLike() {
        likeCounter = 1;


        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP) {
            // Do something for lollipop and above versions
            ImageViewCompat.setImageTintList(getBinding().interactionSection.likeIcon, ColorStateList.valueOf(getResources().getColor(R.color.dialog_green_color)));
            getBinding().interactionSection.tvLike.setTextColor(ContextCompat.getColor(InstructorActivity.this, R.color.dialog_green_color));
        } else {
            // do something for phones running an SDK before lollipop
            ImageViewCompat.setImageTintList(getBinding().interactionSection.likeIcon, ColorStateList.valueOf(getResources().getColor(R.color.dialog_green_color)));
            getBinding().interactionSection.tvLike.setTextColor(getResources().getColor(R.color.dialog_green_color));
        }

    }

    public void resetLike() {
        likeCounter = 0;
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP) {
            // Do something for lollipop and above versions
            getBinding().interactionSection.tvLike.setTextColor(ContextCompat.getColor(InstructorActivity.this, R.color.white));

            ImageViewCompat.setImageTintList(getBinding().interactionSection.likeIcon, ColorStateList.valueOf(getResources().getColor(R.color.white)));
        } else {
            // do something for phones running an SDK before lollipop
            getBinding().interactionSection.tvLike.setTextColor(getResources().getColor(R.color.white));
            ImageViewCompat.setImageTintList(getBinding().interactionSection.likeIcon, ColorStateList.valueOf(getResources().getColor(R.color.white)));
        }

    }

    private void openShareDialogue() {
        Logger.e("share","share3");
        //   String imgUrl = AppCommonMethod.urlPoints + AppConstants.SERIES_IMAGES_BASE_KEY + seriesDetailBean..getPicture();
        String imgUrl = seriesDetailBean.getPosterURL();
        int id = seriesDetailBean.getId();
        String title = seriesDetailBean.getTitle();
        Logger.e("openShareDialogue", new Gson().toJson(seriesDetailBean));
        AppCommonMethod.openShareDialog(InstructorActivity.this, title, id, MediaTypeConstants.getInstance().getInstructor(), imgUrl, seriesDetailBean.getSeason());
        new Handler().postDelayed(() -> dismissLoading(getBinding().progressBar), 2000);
    }

    private void noConnectionLayout() {
        stopShimmer();
        getBinding().llParent.setVisibility(View.GONE);
        getBinding().noConnectionLayout.setVisibility(View.VISIBLE);
        getBinding().connection.retryTxt.setOnClickListener(view -> {
            callShimmer();
            connectionObserver();
        });
    }


    public void hitApiRemoveList() {
        showLoading(getBinding().progressBar);
        JsonObject requestParam = new JsonObject();
        requestParam.addProperty(AppConstants.API_PARAM_WATCHLIST_ID, watchListId);
        viewModel.hitApiRemoveWatchList(token, String.valueOf(watchListId)).observe(InstructorActivity.this, responseWatchList -> {
            dismissLoading(getBinding().progressBar);
            if (responseWatchList.isStatus()) {
                getBinding().interactionSection.addIcon.setImageResource(R.drawable.add_to_watchlist);
                resetWatchList();
                Logger.e("", "hitApiAddWatchList");
            } else {
                if (responseWatchList.getResponseCode() == 401) {
                    isloggedout = true;
                    if (isLogin) {
                        hitApiLogout();
                    }
                    //   showDialog(SeriesDetailActivity.this.getResources().getString(R.string.logged_out), getResources().getString(R.string.you_are_logged_out));
                }

                Logger.e("", "something went wrong");
                // new ToastHandler(SeriesDetailActivity.this).show(SeriesDetailActivity.this.getResources().getString(R.string.something_went_wrong));
            }
        });

    }

    public void hitApiAddWatchList() {
        showLoading(getBinding().progressBar);
        JsonObject requestParam = new JsonObject();
        requestParam.addProperty(AppConstants.API_PARAM_CONTENT_ID, seriesId);
        requestParam.addProperty(AppConstants.API_PARAM_CONTENT_TYPE, "SERIES");
        viewModel.hitApiAddWatchList(token, requestParam).observe(InstructorActivity.this, responseWatchList -> {
            dismissLoading(getBinding().progressBar);
            if (responseWatchList.isStatus()) {
                watchListId = responseWatchList.getData().getId();
                setWatchList();
            } else {
                if (responseWatchList.getResponseCode() == 401) {
                    isloggedout = true;
                    if (isLogin) {
                        hitApiLogout();
                    }
                    //  showDialog(SeriesDetailActivity.this.getResources().getString(R.string.logged_out), getResources().getString(R.string.you_are_logged_out));
                }
                Logger.e("", "hitApiAddWatchList");
            }
        });

    }

    private void showDialog() {
        FragmentManager fm = getSupportFragmentManager();
        AlertDialogSingleButtonFragment alertDialog = AlertDialogSingleButtonFragment.newInstance(title, message, getResources().getString(R.string.ok));
        alertDialog.setCancelable(false);
        alertDialog.setAlertDialogCallBack(this);
        alertDialog.show(fm, "fragment_alert");
    }

    public void setWatchList() {
        watchListCounter = 1;
        getBinding().interactionSection.addIcon.setImageResource(R.drawable.check_icon);
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP) {
            // Do something for lollipop and above versions
            ImageViewCompat.setImageTintList(getBinding().interactionSection.addIcon, ColorStateList.valueOf(getResources().getColor(R.color.dialog_green_color)));
            getBinding().interactionSection.tvWatch.setTextColor(ContextCompat.getColor(InstructorActivity.this, R.color.dialog_green_color));
        } else {
            // do something for phones running an SDK before lollipop
            ImageViewCompat.setImageTintList(getBinding().interactionSection.addIcon, ColorStateList.valueOf(getResources().getColor(R.color.dialog_green_color)));
            getBinding().interactionSection.tvWatch.setTextColor(getResources().getColor(R.color.dialog_green_color));
        }

    }

    public void resetWatchList() {
        watchListCounter = 0;
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP) {
            // Do something for lollipop and above versions
            getBinding().interactionSection.tvWatch.setTextColor(ContextCompat.getColor(InstructorActivity.this, R.color.white));
            ImageViewCompat.setImageTintList(getBinding().interactionSection.addIcon, ColorStateList.valueOf(getResources().getColor(R.color.white)));
        } else {
            // do something for phones running an SDK before lollipop
            getBinding().interactionSection.tvWatch.setTextColor(getResources().getColor(R.color.white));
            ImageViewCompat.setImageTintList(getBinding().interactionSection.addIcon, ColorStateList.valueOf(getResources().getColor(R.color.white)));
        }
    }

    public void hitApiRemoveLike() {
        JsonObject requestParam = new JsonObject();
        requestParam.addProperty(AppConstants.API_PARAM_LIKE_ID, seriesId);
        requestParam.addProperty(AppConstants.API_PARAM_LIKE_TYPE, MediaTypeConstants.getInstance().getSeries());
        viewModel.hitApiUnLike(token, requestParam).observe(InstructorActivity.this, responseContentInWatchlist -> {
            dismissLoading(getBinding().progressBar);
            if (responseContentInWatchlist.isStatus()) {
                resetLike();
            } else {
                if (responseContentInWatchlist.getResponseCode() == 401) {
                    isloggedout = true;
                    if (isLogin) {
                        hitApiLogout();
                    }
                    //    showDialog(SeriesDetailActivity.this.getResources().getString(R.string.logged_out), getResources().getString(R.string.you_are_logged_out));
                }
                Logger.e("", "hitApiAddWatchList");
            }
        });
    }


    @Override
    public void onBackPressed() {
        super.onBackPressed();

        if (commentsFragment != null) {
            removeCommentFragment();
        } else {
            if (preference == null)
                preference = KsPreferenceKeys.getInstance();
            if (preference.getAppPrefJumpBack()) {
                preference.setAppPrefJumpBackId(0);
                preference.setAppPrefJumpBack(false);
            }
            // fragment.releasePlayer();
            AppCommonMethod.seasonId = -1;
            AppCommonMethod.isSeasonCount = false;
            this.finish();
        }

    }


    public void commentFragment() {
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        commentsFragment = new CommentsFragment();
        Bundle args = new Bundle();
        args.putInt(AppConstants.BUNDLE_ID_FOR_COMMENTS, id);
        args.putString(AppConstants.BUNDLE_TYPE_FOR_COMMENTS, MediaTypeConstants.getInstance().getSeries());
        commentsFragment.setArguments(args);
        transaction.replace(R.id.fragment_comment, commentsFragment);
        transaction.addToBackStack(null);
        transaction.commit();

    }

    public void removeCommentFragment() {
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        if (commentsFragment != null) {
            transaction.remove(commentsFragment);
            transaction.commit();
            transaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_CLOSE);
            commentsFragment = null;
            getBinding().playerFooter.setVisibility(View.VISIBLE);
            getBinding().fragmentComment.setVisibility(View.GONE);
            getBinding().interactionSection.showComments.setVisibility(View.VISIBLE);
        }
    }


    public void postCommentClick() {
     /*   getBinding().interactionSection.showComments.setOnClickListener(v -> {
            openCommentSection();
        });*/
    }

    public void openCommentSection() {
        commentFragment();
        getBinding().interactionSection.showComments.setVisibility(View.GONE);
        getBinding().playerFooter.setVisibility(View.GONE);
        getBinding().fragmentComment.setVisibility(View.VISIBLE);
    }


    public void openLoginPage() {
        preference.setAppPrefJumpTo(MediaTypeConstants.getInstance().getSeries());
        preference.setAppPrefJumpBack(true);
        preference.setAppPrefJumpBackId(seriesId);
        new ActivityLauncher(InstructorActivity.this).loginActivity(InstructorActivity.this, LoginActivity.class);

    }


    @Override
    public void onFinishDialog() {
        isloggedout = false;
        if (isLogin) {
            hitApiLogout();
        }
        finish();
    }


    public void hitApiLogout() {
        isloggedout = false;
        if (isLogin) {
            hitApiLogout(preference.getAppPrefAccessToken());
        }
    }


    public void openLogin() {
        preference.setAppPrefJumpTo(getResources().getString(R.string.series));
        preference.setAppPrefJumpBack(true);
        preference.setAppPrefJumpBackId(seriesId);
        new ActivityLauncher(InstructorActivity.this).loginActivity(InstructorActivity.this, LoginActivity.class);

    }

    public void showSeasonList() {
        getBinding().transparentLayout.setVisibility(View.VISIBLE);

        InstructorActivity.SeasonListAdapter listAdapter = new InstructorActivity.SeasonListAdapter(list, selectedSeasonId);
        builder = new AlertDialog.Builder(InstructorActivity.this);
        LayoutInflater inflater = LayoutInflater.from(InstructorActivity.this);
        View content = inflater.inflate(R.layout.season_custom_dialog, null);
        builder.setView(content);
        RecyclerView mRecyclerView = content.findViewById(R.id.my_recycler_view);
        ImageView imageView = content.findViewById(R.id.close);
        imageView.setOnClickListener(v -> {
            alertDialog.cancel();
            getBinding().transparentLayout.setVisibility(View.GONE);
        });

        //Creating Adapter to fill data in Dialog
        mRecyclerView.setLayoutManager(new LinearLayoutManager(InstructorActivity.this));
        mRecyclerView.setAdapter(listAdapter);
        alertDialog = builder.create();
        alertDialog.getWindow().setBackgroundDrawable(ActivityCompat.getDrawable(InstructorActivity.this, R.color.transparent_series));
        alertDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);

        if (alertDialog.getWindow() != null)
            alertDialog.getWindow().getAttributes().windowAnimations = R.style.SlidingDialogAnimation;

        alertDialog.show();
        WindowManager.LayoutParams lWindowParams = new WindowManager.LayoutParams();
        lWindowParams.copyFrom(alertDialog.getWindow().getAttributes());
        lWindowParams.width = WindowManager.LayoutParams.MATCH_PARENT; // this is where the magic happens
        lWindowParams.height = WindowManager.LayoutParams.MATCH_PARENT;
        alertDialog.getWindow().setAttributes(lWindowParams);

    }

    @Override
    public void onDownloadClicked(String videoId, Object position, Object source) {
        try {
            boolean loginStatus = preference.getAppPrefLoginStatus();
            if (!loginStatus)
                new ActivityLauncher(this).loginActivity(this, LoginActivity.class);
            else {
                int videoQuality = new SharedPrefHelper(this).getInt(SharedPrefesConstants.DOWNLOAD_QUALITY_INDEX, 3);
                if (source instanceof UserInteractionFragment) {
                    if (KsPreferenceKeys.getInstance().getDownloadOverWifi() == 1 && NetworkHelper.INSTANCE.isWifiEnabled(this)) {
//                        downloadHelper.findVideo(seriesDetailBean.getBrightcoveVideoId(), new VideoListener() {
//                            @Override
//                            public void onVideo(Video video) {
//                                downloadHelper.startSeriesDownload(seriesDetailBean.getBrightcoveVideoId(), seasonTabFragment.getSelectedSeason(), seasonTabFragment.getSeasonAdapter().getSeasonEpisodes(), videoQuality);
//                            }
//
//                            @Override
//                            public void onError(String error) {
//                                super.onError(error);
//                                Logger.e(TAG, error);
//                            }
//                        });
                    } else {
                        Toast.makeText(InstructorActivity.this, "NoWifi", Toast.LENGTH_LONG).show();
                    }

                } else {
                    if (KsPreferenceKeys.getInstance().getDownloadOverWifi() == 1 && NetworkHelper.INSTANCE.isWifiEnabled(this)) {
                    } else {
                        if (KsPreferenceKeys.getInstance().getDownloadOverWifi() == 1) {
                            showWifiSettings();
                        } else {
//                            downloadHelper.findVideo(String.valueOf(videoId), new VideoListener() {
//                                @Override
//                                public void onVideo(Video video) {
//                                    if (videoQuality != 4) {
//                                        if (downloadHelper.getCatalog() != null) {
//                                            downloadHelper.allowedMobileDownload();
//                                            if (seasonTabFragment != null) {
//                                                seasonTabFragment.updateStatus();
//                                            }
//                                            downloadHelper.startEpisodeDownload(video, String.valueOf(seriesDetailBean.getBrightcoveVideoId()), seasonTabFragment.getSelectedSeason(), seasonTabFragment.getSeasonAdapter().getEpisodeNumber(videoId), videoQuality);
//                                        }
//                                    } else {
//                                        if (downloadHelper.getCatalog() != null) {
//                                            downloadHelper.allowedMobileDownload();
//                                            selectDownloadVideoQuality(video, videoId);
//                                        }
//
//                                    }
//
//                                }
//
//                                @Override
//                                public void onError(String error) {
//                                    super.onError(error);
//                                    Logger.e(TAG, error);
//                                }
//                            });

                        }

                    }

                }
            }
        }catch (Exception ignored){

        }
    }

    private void showWifiSettings() {
//        downloadHelper.changeWifiSetting(new WifiPreferenceListener() {
//            @Override
//            public void actionP(int value) {
//                if (value == 0) {
//                    if (downloadHelper.getCatalog() != null) {
//                        downloadHelper.allowedMobileDownload();
//                        downloadHelper.findVideo(String.valueOf(videoId), new VideoListener() {
//                            @Override
//                            public void onVideo(Video video) {
//                                if ((KsPreferenceKeys.getInstance().getDownloadOverWifi() == 1)) {
//                                    if (NetworkHelper.INSTANCE.isWifiEnabled(SeriesDetailActivity.this)) {
//                                        if (videoQuality != 4) {
//                                            downloadHelper.startEpisodeDownload(video, String.valueOf(seriesDetailBean.getBrightcoveVideoId()), seasonTabFragment.getSelectedSeason(), seasonTabFragment.getSeasonAdapter().getEpisodeNumber(videoId), videoQuality);
//                                        } else {
//                                            selectDownloadVideoQuality(video, videoId);
//                                        }
//                                    } else {
//                                        Toast.makeText(SeriesDetailActivity.this, "NoWifi", Toast.LENGTH_LONG).show();
//                                    }
//                                } else {
//                                    if (videoQuality != 4) {
//                                        downloadHelper.startEpisodeDownload(video, String.valueOf(seriesDetailBean.getBrightcoveVideoId()), seasonTabFragment.getSelectedSeason(), seasonTabFragment.getSeasonAdapter().getEpisodeNumber(videoId), videoQuality);
//                                    } else {
//                                        selectDownloadVideoQuality(video, videoId);
//                                    }
//                                }
//                            }
//
//                            @Override
//                            public void onError(String error) {
//                                super.onError(error);
//                                Logger.e(TAG, error);
//                            }
//                        });
//                    } else {
//                        Toast.makeText(SeriesDetailActivity.this, SeriesDetailActivity.this.getResources().getString(R.string.something_went_wrong), Toast.LENGTH_LONG).show();
//                    }
//                }
//            }
//        });
    }


    private void selectDownloadVideoQuality() {
//        downloadHelper.selectVideoQuality(position -> {
//            if (seasonTabFragment!=null){
//                seasonTabFragment.updateStatus();
//            }
//            downloadHelper.startEpisodeDownload(video, String.valueOf(seriesDetailBean.getBrightcoveVideoId()), seasonTabFragment.getSelectedSeason(), seasonTabFragment.getSeasonAdapter().getEpisodeNumber(videoId), position);
//        });
    }

    private List<EnveuVideoItemBean> getDownloadableEpisodes
            () {
        List<EnveuVideoItemBean> downloadableEpisodes = new ArrayList<>();
        for (EnveuVideoItemBean enveuVideoItemBean : seasonEpisodes) {
//            downloadHelper.findVideo(enveuVideoItemBean.getBrightcoveVideoId(), new VideoListener() {
//                @Override
//                public void onVideo(Video video) {
//                    if (video.isOfflinePlaybackAllowed()) {
//                        Logger.e(TAG, String.valueOf(video.isOfflinePlaybackAllowed()));
//                        downloadableEpisodes.add(enveuVideoItemBean);
//                    }
//                }
//            });
        }
        return downloadableEpisodes;
    }

    @Override
    public void onProgressbarClicked(View view, Object source, String videoId) {
        if (source instanceof UserInteractionFragment) {
            AppCommonMethod.showPopupMenu(this, view, R.menu.download_menu, item -> {
                switch (item.getItemId()) {
                    case R.id.cancel_download:
//                        downloadHelper.cancelVideo(downloadAbleVideo.getId());
                        break;
                    case R.id.pause_download:
//                        downloadHelper.pauseVideo();
                        break;
                }
                return false;
            });
        } else {
            AppCommonMethod.showPopupMenu(this, view, R.menu.download_menu, item -> {
                switch (item.getItemId()) {
                    case R.id.cancel_download:
//                        downloadHelper.cancelVideo(videoId);
                        if (videoId.equals(String.valueOf(seriesId)))
                            userInteractionFragment.setDownloadStatus();
                        break;
                    case R.id.pause_download:
//                        downloadHelper.pauseVideo(videoId);
                        if (videoId.equals(String.valueOf(seriesId)))
                            userInteractionFragment.setDownloadStatus();
                        break;
                }
                return false;
            });
        }
    }

    @Override
    public void onDownloadCompleteClicked(View view, Object source, String videoId) {
        if (source instanceof UserInteractionFragment) {
            AppCommonMethod.showPopupMenu(this, view, R.menu.delete_menu, item -> {
                switch (item.getItemId()) {
                    case R.id.delete_download:
//                        downloadHelper.deleteVideo(downloadAbleVideo);
                        break;

                }
                return false;
            });
        } else {
            if (videoId.equals(String.valueOf(seriesId)))
                userInteractionFragment.setDownloadStatus();
        }
    }

    @Override
    public void onPauseClicked(String videoId, Object source) {
        if (source instanceof UserInteractionFragment) {
//            downloadHelper.resumeDownload(downloadAbleVideo.getId());
        } else {
//            downloadHelper.resumeDownload(videoId);
        }
    }

    @Override
    public void onDownloadDeleted(@NotNull String videoId, @NotNull Object source) {

    }

    @Override
    public void onClick() {

    }

    @Override
    public void onPointerCaptureChanged(boolean hasCapture) {

    }

    @Override
    public void fromAdapterDownloadProgress(float process, @NonNull String assetID) {

    }

    @Override
    public void fromAdapterStatusChanged(@NonNull OfflineManager.AssetDownloadState state, @NonNull String assetID) {

    }

    @Override
    public void fromAdapterPaused(@NonNull String assetID) {

    }

    @Override
    public void fromAdapterStatus(@NonNull OfflineManager.AssetDownloadState state, @NonNull String assetID) {

    }

    @Override
    public void onSeriesDownloadClicked(@NotNull View view, @NotNull Object source, @NotNull String videoId) {

    }

//    @Override
//    public void onDownloadRequested(@androidx.annotation.NonNull Video video) {
//        Logger.i(TAG, String.format(
//                "Starting to process '%s' video download request", video.getName()));
//        if (seasonTabFragment.getSeasonAdapter() != null) {
//            seasonTabFragment.getSeasonAdapter().onDownloadRequested(video);
//        }
//    }
//
//    @Override
//    public void onDownloadStarted(@androidx.annotation.NonNull Video video, long l,
//                                  @androidx.annotation.NonNull Map<String, Serializable> map) {
//        Logger.e(TAG, "onDownloadStarted");
//        if (downloadAbleVideo != null && video.getId().equals(downloadAbleVideo.getId()))
//            userInteractionFragment.setDownloadStatus(panteao.make.ready.enums.DownloadStatus.DOWNLOADING);
//        if (seasonTabFragment.getSeasonAdapter() != null) {
//            seasonTabFragment.getSeasonAdapter().onDownloadStarted(video, l, map);
//        }
//        if (seasonTabFragment!=null){
//            seasonTabFragment.updateStatus();
//        }
//    }
//
//    @Override
//    public void onDownloadProgress(@androidx.annotation.NonNull Video
//                                           video, @androidx.annotation.NonNull com.brightcove.player.network.DownloadStatus
//                                           downloadStatus) {
//        Logger.e(TAG, "onDownloadProgress" + downloadStatus.getProgress());
//        if (downloadAbleVideo != null && video.getId().equals(downloadAbleVideo.getId())) {
//            userInteractionFragment.setDownloadStatus(panteao.make.ready.enums.DownloadStatus.DOWNLOADING);
//            userInteractionFragment.setDownloadProgress((float) downloadStatus.getProgress());
//        }
//        if (seasonTabFragment.getSeasonAdapter() != null) {
//            seasonTabFragment.getSeasonAdapter().onDownloadProgress(video, downloadStatus);
//        }
//    }
//
//    @Override
//    public void onDownloadPaused(@androidx.annotation.NonNull Video
//                                         video, @androidx.annotation.NonNull com.brightcove.player.network.DownloadStatus
//                                         downloadStatus) {
//        Logger.e(TAG, "onDownloadPaused");
//        if (downloadAbleVideo != null && video.getId().equals(downloadAbleVideo.getId()))
//            userInteractionFragment.setDownloadStatus(panteao.make.ready.enums.DownloadStatus.PAUSE);
//        if (seasonTabFragment.getSeasonAdapter() != null) {
//            seasonTabFragment.getSeasonAdapter().onDownloadPaused(video, downloadStatus);
//        }
//    }
//
//    @Override
//    public void onDownloadCompleted(@androidx.annotation.NonNull Video
//                                            video, @androidx.annotation.NonNull com.brightcove.player.network.DownloadStatus
//                                            downloadStatus) {
//        Logger.e(TAG, "onDownloadCompleted");
//        if (downloadAbleVideo != null && video.getId().equals(downloadAbleVideo.getId()))
//            userInteractionFragment.setDownloadStatus(panteao.make.ready.enums.DownloadStatus.DOWNLOADED);
//        seasonTabFragment.getSeasonAdapter().onDownloadCompleted(video, downloadStatus);
//        downloadHelper.updateVideoStatus(com.brightcove.player.network.DownloadStatus.STATUS_COMPLETE, video.getId());
//    }
//
//    @Override
//    public void onDownloadCanceled(@androidx.annotation.NonNull Video video) {
//        Logger.e(TAG, "onDownloadCanceled");
//        if (downloadAbleVideo != null && video.getId().equals(downloadAbleVideo.getId())) {
//            userInteractionFragment.setDownloadStatus(panteao.make.ready.enums.DownloadStatus.START);
//            userInteractionFragment.setDownloadProgress(0);
//        }
//        if (seasonTabFragment.getSeasonAdapter() != null) {
//            seasonTabFragment.getSeasonAdapter().onDownloadCanceled(video);
//        }
//    }
//
//    @Override
//    public void onDownloadDeleted(@androidx.annotation.NonNull Video video) {
//        Logger.e(TAG, "onDownloadDeleted");
//        if (downloadAbleVideo != null && video.getId().equals(downloadAbleVideo.getId()))
//            userInteractionFragment.setDownloadStatus(panteao.make.ready.enums.DownloadStatus.START);
//        seasonTabFragment.getSeasonAdapter().onDownloadDeleted(video);
//    }
//
//    @Override
//    public void onDownloadFailed(@androidx.annotation.NonNull Video
//                                         video, @androidx.annotation.NonNull com.brightcove.player.network.DownloadStatus
//                                         downloadStatus) {
//        Logger.e(TAG, "onDownloadFailed");
//
//    }
//
//    @Override
//    public void downloadVideo(@androidx.annotation.NonNull Video video) {
//
//    }
//
//    @Override
//    public void pauseVideoDownload(Video video) {
//        if (downloadAbleVideo != null && video.getId().equals(downloadAbleVideo.getId()))
//            userInteractionFragment.setDownloadStatus(panteao.make.ready.enums.DownloadStatus.DOWNLOADING);
//
//    }
//
//    @Override
//    public void resumeVideoDownload(Video video) {
//        if (downloadAbleVideo != null && video.getId().equals(downloadAbleVideo.getId()))
//            userInteractionFragment.setDownloadStatus(panteao.make.ready.enums.DownloadStatus.PAUSE);
//    }
//
//    @Override
//    public void deleteVideo(@androidx.annotation.NonNull Video video) {
//        downloadHelper = new DownloadHelper(this, this);
//        downloadHelper.setAssetType(MediaTypeConstants.getInstance().getEpisode());
//        downloadHelper.setSeriesName(seriesDetailBean.getTitle());
//    }
//
//    @Override
//    public void alreadyDownloaded(@androidx.annotation.NonNull Video video) {
//        if (downloadAbleVideo != null && video.getId().equals(downloadAbleVideo.getId()))
//            userInteractionFragment.setDownloadStatus(panteao.make.ready.enums.DownloadStatus.DOWNLOADED);
//    }
//
//    @Override
//    public void downloadedVideos(@org.jetbrains.annotations.Nullable List<? extends
//            Video> p0) {
//
//    }
//
//    @Override
//    public void videoFound(Video video) {
////        this.downloadAbleVideo = video;
////        userInteractionFragment.setDownloadable(downloadAbleVideo.isOfflinePlaybackAllowed());
//    }
//
//    @Override
//    public void downloadStatus(String videoId, DownloadStatus downloadStatus) {
//        Logger.e(TAG, "onDownloadFailed" + downloadStatus);
//    }

//    @Override
//    public void onDownloadDeleted(@NotNull String videoId, @NotNull Object source) {
//        Logger.e(TAG, "onDownloadDeleted" + videoId);
//        try {
//            downloadHelper = new DownloadHelper(this, this);
//            downloadHelper.setAssetType(MediaTypeConstants.getInstance().getEpisode());
//            downloadHelper.setSeriesName(seriesDetailBean.getTitle());
//        } catch (Exception ignored) {
//
//        }
//
//    }

    class SeasonListAdapter extends RecyclerView.Adapter<InstructorActivity.SeasonListAdapter.ViewHolder> {
        private final ArrayList<SelectedSeasonModel> list;
        private final int selectedPos;

        //TrackGroup list;
        public SeasonListAdapter(ArrayList<SelectedSeasonModel> list, int selectedPos) {
            this.list = list;
            this.selectedPos = selectedPos;
        }

        @NonNull
        @Override
        public InstructorActivity.SeasonListAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.all_season_listing, parent, false);

            return new InstructorActivity.SeasonListAdapter.ViewHolder(view);
        }

        @Override
        public void onBindViewHolder(@NonNull final InstructorActivity.SeasonListAdapter.ViewHolder holder, final int position) {
            holder.season.setText(list.get(position).getList());
            if (list.get(position).isSelected()) {
                holder.season.setTextColor(getResources().getColor(R.color.moretitlecolor));
                Typeface boldTypeface = Typeface.defaultFromStyle(Typeface.BOLD);
                holder.season.setTypeface(boldTypeface);

            } else {
                holder.season.setTextColor(getResources().getColor(R.color.bottom_nav_color_f));
                Typeface boldTypeface = Typeface.defaultFromStyle(Typeface.NORMAL);
                holder.season.setTypeface(boldTypeface);

            }

            holder.season.setOnClickListener(v -> {
                alertDialog.cancel();
                getBinding().transparentLayout.setVisibility(View.GONE);
                if (seasonTabFragment != null) {
                    seasonTabFragment.updateTotalPages();
                    seasonTabFragment.setSeasonAdapter();
                    seasonTabFragment.setSelectedSeason(list.get(position).getSelectedId());
                    showLoading(getBinding().progressBar);
                    seasonTabFragment.getSeasonEpisodes();

                }

            });
        }

        @Override
        public int getItemCount() {
            return list.size();
        }


        class ViewHolder extends RecyclerView.ViewHolder {
            TextView season;

            public ViewHolder(View itemView) {
                super(itemView);
                season = itemView.findViewById(R.id.season_name);
            }
        }
    }
}

