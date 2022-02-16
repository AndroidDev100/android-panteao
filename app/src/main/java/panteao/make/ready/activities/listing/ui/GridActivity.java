package panteao.make.ready.activities.listing.ui;


import android.os.Bundle;
import android.os.PersistableBundle;
import android.util.Log;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.make.baseCollection.baseCategoryModel.BaseCategory;
import com.make.bookmarking.bean.continuewatching.ContinueWatchingBookmark;
import com.make.enums.ImageType;

import panteao.make.ready.Bookmarking.BookmarkingViewModel;
import panteao.make.ready.activities.listing.callback.ItemClickListener;
import panteao.make.ready.activities.listing.viewmodel.ListingViewModel;
import panteao.make.ready.baseModels.BaseBindingActivity;
import panteao.make.ready.beanModelV3.continueWatching.DataItem;
import panteao.make.ready.fragments.dialog.AlertDialogFragment;
import panteao.make.ready.fragments.dialog.AlertDialogSingleButtonFragment;
import panteao.make.ready.networking.apistatus.APIStatus;
import panteao.make.ready.R;
import panteao.make.ready.adapters.CommonListingAdapter;
import panteao.make.ready.adapters.commonRails.CommonCircleAdapter;
import panteao.make.ready.adapters.commonRails.CommonPosterLandscapeAdapter;
import panteao.make.ready.adapters.commonRails.CommonPosterPotraitAdapter;
import panteao.make.ready.adapters.commonRails.CommonPotraitAdapter;
import panteao.make.ready.adapters.commonRails.CommonPotraitTwoAdapter;
import panteao.make.ready.adapters.commonRails.LandscapeListingAdapter;
import panteao.make.ready.adapters.commonRails.SquareListingAdapter;
import panteao.make.ready.beanModel.enveuCommonRailData.RailCommonData;
import panteao.make.ready.beanModel.responseModels.landingTabResponses.railData.ContentsItem;
import panteao.make.ready.callbacks.commonCallbacks.CommonApiCallBack;
import panteao.make.ready.databinding.ListingActivityBinding;
import panteao.make.ready.layersV2.ContinueWatchingLayer;
import panteao.make.ready.utils.MediaTypeConstants;
import panteao.make.ready.utils.commonMethods.AppCommonMethod;
import panteao.make.ready.utils.constants.AppConstants;
import panteao.make.ready.utils.cropImage.helpers.Logger;
import panteao.make.ready.utils.cropImage.helpers.NetworkConnectivity;
import panteao.make.ready.utils.cropImage.helpers.PrintLogging;
import panteao.make.ready.utils.cropImage.helpers.ShimmerDataModel;
import panteao.make.ready.utils.helpers.GridSpacingItemDecoration;
import panteao.make.ready.utils.helpers.RailInjectionHelper;
import panteao.make.ready.utils.helpers.RecyclerAnimator;

import panteao.make.ready.utils.helpers.ksPreferenceKeys.KsPreferenceKeys;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;


public class GridActivity extends BaseBindingActivity<ListingActivityBinding> implements ItemClickListener, AlertDialogFragment.AlertDialogListener {
    String playListId;
    BaseCategory baseCategory;
    int enveuLayoutType = 0;
    LinearLayoutManager linearLayoutManager;
    int spacing;
    private ListingViewModel listingViewModel;
    private int counter = 0, flag, firstVisiblePosition, pastVisiblesItems, visibleItemCount, totalItemCount;
    private CommonCircleAdapter commonCircleAdapter;
    private LandscapeListingAdapter commonLandscapeAdapter;
    private CommonPotraitAdapter commonPotraitAdapter;
    private CommonPotraitTwoAdapter commonPotraitTwoAdapter;
    private CommonPosterLandscapeAdapter commonPosterLandscapeAdapter;
    private CommonPosterPotraitAdapter commonPosterPotraitAdapter;
    private GridLayoutManager gridLayoutManager;
    private List<ContentsItem> contentsItems;
    private SquareListingAdapter squareCommonAdapter;
    private String title;
    private boolean mIsLoading = true, isScrolling = false;
    private int mScrollY;
    private int shimmerType;
    private RailCommonData listData;
    private final long mLastClickTime = 0;
    private int pos;

    @Override
    public ListingActivityBinding inflateBindingLayout() {
        return ListingActivityBinding.inflate(inflater);
    }


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        title = getIntent().getStringExtra("title");
        playListId = getIntent().getStringExtra("playListId");
        Log.e("testData", "playListId: "+playListId );
        flag = getIntent().getIntExtra("flag", 0);
        shimmerType = getIntent().getIntExtra("shimmerType", 0);
        baseCategory = getIntent().getExtras().getParcelable("baseCategory");
        pos = getIntent().getIntExtra("railPosition", 0);

        modelCall();
        UiIntilization();
        connectionObserver();
    }

    private void connectionObserver() {
        if (NetworkConnectivity.isOnline(GridActivity.this)) {
            getBinding().noConnectionLayout.setVisibility(View.GONE);
            connectionValidation();
        } else {
            connectionValidation();
        }
    }

    private void connectionValidation() {
        if (aBoolean) {
            if (counter == 0)
                callShimmer();
            setClicks();
            getRailData();
        } else {
            noConnectionLayout();
        }
    }

    private void callShimmer() {
        CommonListingAdapter shimmerAdapter = new CommonListingAdapter(GridActivity.this);
        getBinding().listRecyclerview.hasFixedSize();
        getBinding().listRecyclerview.setNestedScrollingEnabled(false);
        int num = 2;
        if (baseCategory.getContentImageType().equalsIgnoreCase(ImageType.CIR.name()) || baseCategory.getContentImageType().equalsIgnoreCase(ImageType.PR1.name())
                || baseCategory.getContentImageType().equalsIgnoreCase(ImageType.PR2.name())
                || baseCategory.getContentImageType().equalsIgnoreCase(ImageType.SQR.name())) {
            boolean tabletSize = GridActivity.this.getResources().getBoolean(R.bool.isTablet);
            num = 3;
            if (tabletSize) {
                if (GridActivity.this.getResources().getConfiguration().orientation == 2)
                    num = 5;
                else
                    num = 4;
            }
            shimmerAdapter.setDataList(new ShimmerDataModel().getList(5));
        } else if (baseCategory.getContentImageType().equalsIgnoreCase(ImageType.LDS.name())
                || baseCategory.getContentImageType().equalsIgnoreCase(ImageType.LDS2.name())) {
            boolean tabletSize = GridActivity.this.getResources().getBoolean(R.bool.isTablet);
            num = 2;
            if (tabletSize) {
                if (GridActivity.this.getResources().getConfiguration().orientation == 2)
                    num = 4;
                else
                    num = 3;
            }
            shimmerAdapter.setDataList(new ShimmerDataModel().getList(4));
        }

        getBinding().listRecyclerview.addItemDecoration(new GridSpacingItemDecoration(num, 6, true));
        gridLayoutManager = new GridLayoutManager(GridActivity.this, num);

        getBinding().listRecyclerview.setLayoutManager(gridLayoutManager);
        getBinding().listRecyclerview.setAdapter(shimmerAdapter);
        getBinding().listRecyclerview.setVisibility(View.VISIBLE);

    }

    private void setRecyclerProperties() {
        if (enveuLayoutType == 0) {
            gridLayoutManager = new GridLayoutManager(this, num);
            getBinding().listRecyclerview.setItemAnimator(new DefaultItemAnimator());
            getBinding().listRecyclerview.setLayoutManager(gridLayoutManager);

        } else {
            linearLayoutManager = new LinearLayoutManager(this);
            getBinding().listRecyclerview.setItemAnimator(new DefaultItemAnimator());
            getBinding().listRecyclerview.setLayoutManager(linearLayoutManager);
        }
    }

    private void UiIntilization() {
        getBinding().toolbar.backLayout.setOnClickListener(view -> GridActivity.this.finish());
        getBinding().toolbar.titleText.setVisibility(View.VISIBLE);
        getBinding().toolbar.screenText.setText(title);
        getBinding().toolbar.homeIcon.setVisibility(View.GONE);
        getBinding().toolbar.mediaRouteButton.setVisibility(View.GONE);
        getBinding().toolbar.backLayout.setVisibility(View.VISIBLE);
        getBinding().toolbar.searchIcon.setVisibility(View.GONE);
        getBinding().toolbar.screenText.setVisibility(View.VISIBLE);
        getBinding().listRecyclerview.hasFixedSize();
        getBinding().listRecyclerview.setNestedScrollingEnabled(false);
        getBinding().listRecyclerview.addItemDecoration(new GridSpacingItemDecoration(3, 5, true));

    }

    private void setClicks() {

        getBinding().listRecyclerview.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                try {
                    if (enveuLayoutType == 0) {
                        GridLayoutManager layoutManager = ((GridLayoutManager) recyclerView.getLayoutManager());
                        firstVisiblePosition = Objects.requireNonNull(layoutManager).findFirstVisibleItemPosition();
                        if (dy > 0) {
                            visibleItemCount = layoutManager.getChildCount();
                            totalItemCount = layoutManager.getItemCount();
                            pastVisiblesItems = layoutManager.findFirstVisibleItemPosition();
                            if (mIsLoading) {
                                if ((visibleItemCount + pastVisiblesItems) >= totalItemCount) {
                                    // PrintLogging.printLog("","slidingValues"+getBinding().listRecyclerview.getAdapter().getItemCount()+" "+counter);
                                    int adapterSize = getBinding().listRecyclerview.getAdapter().getItemCount();
                                    if (adapterSize > 8) {
                                        mIsLoading = false;
                                        counter++;
                                        isScrolling = true;
                                        mScrollY += dy;
                                        connectionObserver();
                                    }
                                }
                            }
                        }
                    } else {
                        LinearLayoutManager layoutManager = ((LinearLayoutManager) recyclerView.getLayoutManager());
                        firstVisiblePosition = Objects.requireNonNull(layoutManager).findFirstVisibleItemPosition();
                        if (dy > 0) {
                            visibleItemCount = layoutManager.getChildCount();
                            totalItemCount = layoutManager.getItemCount();
                            pastVisiblesItems = layoutManager.findFirstVisibleItemPosition();
                            if (mIsLoading) {
                                if ((visibleItemCount + pastVisiblesItems) >= totalItemCount) {
                                    // PrintLogging.printLog("","slidingValues"+getBinding().listRecyclerview.getAdapter().getItemCount()+" "+counter);
                                    int adapterSize = getBinding().listRecyclerview.getAdapter().getItemCount();
                                    if (adapterSize > 8) {
                                        mIsLoading = false;
                                        counter++;
                                        isScrolling = true;
                                        mScrollY += dy;
                                        connectionObserver();
                                    }
                                }
                            }
                        }
                    }

                } catch (Exception e) {
                    Logger.e("ListingActivity", "" + e.toString());

                }
            }
        });

        getBinding().transparentLayout.setVisibility(View.VISIBLE);
        getBinding().transparentLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });

    }

    private void getRailData() {
        if (flag == 0) {
            if (baseCategory.getReferenceName() != null && (baseCategory.getReferenceName().equalsIgnoreCase(AppConstants.ContentType.CONTINUE_WATCHING.name()) || baseCategory.getReferenceName().equalsIgnoreCase("special_playlist"))) {
                KsPreferenceKeys preference = KsPreferenceKeys.getInstance();
                if (preference.getAppPrefLoginStatus()) {
                    String token = preference.getAppPrefAccessToken();
                    BookmarkingViewModel bookmarkingViewModel =new ViewModelProvider(this).get(BookmarkingViewModel.class);
                    bookmarkingViewModel.getContinueWatchingData(token, counter, AppConstants.PAGE_SIZE).observe(this, getContinueWatchingBean -> {
                        getBinding().transparentLayout.setVisibility(View.GONE);
                        String videoIds = "";
                        List<ContinueWatchingBookmark> continueWatchingBookmarkLists = new ArrayList<>();
                        if (getContinueWatchingBean != null) {
                            continueWatchingBookmarkLists = getContinueWatchingBean.getData().getContinueWatchingBookmarks();
                        }

                        List<ContinueWatchingBookmark> continueWatchingBookmarkList = removeDuplicates();
                        for (ContinueWatchingBookmark continueWatchingBookmark : continueWatchingBookmarkList
                        ) {
                            videoIds = videoIds.concat(String.valueOf(continueWatchingBookmark.getAssetId())).concat(",");
                        }
                        Logger.w("assetIds", videoIds);

                        ContinueWatchingLayer.getInstance().getContinueWatchingVideos(continueWatchingBookmarkList, videoIds, new CommonApiCallBack() {
                            @Override
                            public void onSuccess(Object item) {
                                getBinding().transparentLayout.setVisibility(View.GONE);
                                if (item instanceof List) {
                                    ArrayList<DataItem> enveuVideoDetails = (ArrayList<DataItem>) item;
                                    RailCommonData railCommonData = new RailCommonData();
                                    railCommonData.setContinueWatchingData(baseCategory, enveuVideoDetails, new CommonApiCallBack() {
                                        @Override
                                        public void onSuccess(Object item) {
                                            setRail();
                                        }

                                        @Override
                                        public void onFailure() {
                                            showDialog();
                                        }

                                        @Override
                                        public void onFinish() {

                                        }
                                    });
                                }
                            }

                            @Override
                            public void onFailure() {
                            }

                            @Override
                            public void onFinish() {

                            }
                        });
                    });
                }
            } else {
                RailInjectionHelper railInjectionHelper = new ViewModelProvider(this).get(RailInjectionHelper.class);
                /*railInjectionHelper.getPlayListDetailsWithPagination(this, playListId, counter, AppConstants.PAGE_SIZE, baseCategory).observe(this, playlistRailData -> {
                    getBinding().transparentLayout.setVisibility(View.GONE);
                    if (Objects.requireNonNull(playlistRailData) != null) {
                        try {
                            if (title == null || title.equalsIgnoreCase("")) {
                                getBinding().toolbar.screenText.setText(playlistRailData.getDisplayName());
                            }
                        } catch (Exception e) {

                        }
                        listData = playlistRailData;
                        setRail(playlistRailData);
                    }
                });*/
                railInjectionHelper.getPlayListDetailsWithPaginationV2(playListId, counter, AppConstants.PAGE_SIZE, baseCategory).observe(this, playlistRailData -> {
                    Log.e("testData", "getPlayListByWithPagination: "+playListId+"\n"+counter+"\n"+ AppConstants.PAGE_SIZE+"\n"+baseCategory );
                    if (playlistRailData.getStatus().equalsIgnoreCase(APIStatus.START.name())) {

                    } else if (playlistRailData.getStatus().equalsIgnoreCase(APIStatus.SUCCESS.name())) {
                        if (Objects.requireNonNull(playlistRailData) != null) {
                            if (playlistRailData.getBaseCategory() != null) {
                                RailCommonData railCommonData = (RailCommonData) playlistRailData.getBaseCategory();
                                try {
                                    if (title == null || title.equalsIgnoreCase("")) {
                                        getBinding().toolbar.screenText.setText(railCommonData.getDisplayName());
                                    }
                                } catch (Exception e) {
                                    e.printStackTrace();

                                }
                                listData = railCommonData;
                                setRail();
                                Logger.e("RAIL DATA", String.valueOf(listData.isSeries()));
                            }
                        }
                    } else if (playlistRailData.getStatus().equalsIgnoreCase(APIStatus.ERROR.name())) {
                        showDialog();


                    } else if (playlistRailData.getStatus().equalsIgnoreCase(APIStatus.FAILURE.name())) {
                        showDialog();
                    }

                });

            }

        } else {

        }
    }

    private List<ContinueWatchingBookmark> removeDuplicates() {
        List<ContinueWatchingBookmark> noRepeat = new ArrayList<ContinueWatchingBookmark>();
        try {
            for (ContinueWatchingBookmark event : continueWatchingBookmarkList) {
                boolean isFound = false;
                // check if the event name exists in noRepeat
                for (ContinueWatchingBookmark e : noRepeat) {
                    if (e.getAssetId().equals(event.getAssetId()) || (e.equals(event))) {
                        isFound = true;
                        break;
                    }
                }
                if (!isFound) noRepeat.add(event);
            }
        } catch (Exception ignored) {

        }


        return noRepeat;
    }

    private void setSeasonData() {
        if (!isScrolling) {
            PrintLogging.printLog(isScrolling + "isScrolling");
            commonLandscapeAdapter = new LandscapeListingAdapter(this, new ArrayList<>(), seasonResponse.getData().getItems(), AppConstants.VOD, this, baseCategory);
            int num = 2;
            boolean tabletSize = GridActivity.this.getResources().getBoolean(R.bool.isTablet);
            if (tabletSize) {
                //landscape
                if (GridActivity.this.getResources().getConfiguration().orientation == 2)
                    num = 4;
                else
                    num = 3;
            }
            itemDecoration();


            getBinding().listRecyclerview.setAdapter(commonLandscapeAdapter);
            getBinding().progressBar.setVisibility(View.GONE);
        } else {
            commonLandscapeAdapter.notifyEpisodedata(seasonResponse.getData().getItems());
            mIsLoading = seasonResponse.getData().getPageInfo().getTotal() != commonLandscapeAdapter.getItemCount();
            getBinding().listRecyclerview.scrollToPosition(mScrollY);
        }
    }

    private void setRail() {
        getBinding().transparentLayout.setVisibility(View.GONE);
        if (isScrolling) {
            setUiComponents();
            getBinding().progressBar.setVisibility(View.GONE);
        } else {
            getBinding().progressBar.setVisibility(View.GONE);
            mIsLoading = true;
            int num = 3;
            if (playlistRailData != null) {
                if (baseCategory.getReferenceName() != null && (baseCategory.getReferenceName().equalsIgnoreCase(AppConstants.ContentType.CONTINUE_WATCHING.name()) || baseCategory.getReferenceName().equalsIgnoreCase("special_playlist"))) {
                    if (commonPosterLandscapeAdapter == null) {
                        new RecyclerAnimator(this).animate(getBinding().listRecyclerview);
                        commonPosterLandscapeAdapter = new CommonPosterLandscapeAdapter(this, playlistRailData.getEnveuVideoItemBeans(), new ArrayList<>(), "VIDEO", new ArrayList<>(), baseCategory);
                        num = 2;
                        boolean tabletSize = GridActivity.this.getResources().getBoolean(R.bool.isTablet);
                        if (tabletSize) {
                            //landscape
                            if (GridActivity.this.getResources().getConfiguration().orientation == 2)
                                num = 4;
                            else
                                num = 3;
                        }
                        itemDecoration();

                        //  getBinding().listRecyclerview.addItemDecoration(new SpacingItemDecoration(2, SpacingItemDecoration.HORIZONTAL));
                        getBinding().listRecyclerview.setAdapter(commonPosterLandscapeAdapter);
                    }
                } else {
                    if (baseCategory.getContentImageType().equalsIgnoreCase(ImageType.CIR.name())) {
                        if (commonCircleAdapter == null) {
                            new RecyclerAnimator(this).animate(getBinding().listRecyclerview);
                            commonCircleAdapter = new CommonCircleAdapter(this, playlistRailData.getEnveuVideoItemBeans(), "VIDEO", new ArrayList<>(), this);

                            num = 3;
                            boolean tabletSize = GridActivity.this.getResources().getBoolean(R.bool.isTablet);
                            if (tabletSize) {
                                //landscape
                                if (GridActivity.this.getResources().getConfiguration().orientation == 2)
                                    num = 5;
                                else
                                    num = 4;
                            }
                            itemDecoration();
                            getBinding().listRecyclerview.setAdapter(commonCircleAdapter);
                        } else
                            commonCircleAdapter.notifydata(playlistRailData.getEnveuVideoItemBeans());

                        mIsLoading = playlistRailData.getMaxContent() != commonCircleAdapter.getItemCount();
                    } else if (baseCategory.getContentImageType().equalsIgnoreCase(ImageType.SQR.name())) {
                        if (squareCommonAdapter == null) {
                            new RecyclerAnimator(this).animate(getBinding().listRecyclerview);
                            squareCommonAdapter = new SquareListingAdapter(this, playlistRailData.getEnveuVideoItemBeans(), "VIDEO", this);

                            num = 3;
                            boolean tabletSize = GridActivity.this.getResources().getBoolean(R.bool.isTablet);
                            if (tabletSize) {
                                //landscape
                                if (GridActivity.this.getResources().getConfiguration().orientation == 2)
                                    num = 5;
                                else
                                    num = 4;
                            }
                            itemDecoration();
                            getBinding().listRecyclerview.setAdapter(squareCommonAdapter);
                        } else
                            squareCommonAdapter.notifydata(playlistRailData.getEnveuVideoItemBeans());

                        mIsLoading = playlistRailData.getMaxContent() != squareCommonAdapter.getItemCount();
                    } else if (baseCategory.getContentImageType().equalsIgnoreCase(ImageType.LDS.name())) {

                        if (commonLandscapeAdapter == null) {
                            new RecyclerAnimator(this).animate(getBinding().listRecyclerview);
                            commonLandscapeAdapter = new LandscapeListingAdapter(this, playlistRailData.getEnveuVideoItemBeans(), new ArrayList<>(), "VIDEO", this, baseCategory);

                            num = 2;
                            boolean tabletSize = GridActivity.this.getResources().getBoolean(R.bool.isTablet);
                            if (tabletSize) {
                                //landscape
                                if (GridActivity.this.getResources().getConfiguration().orientation == 2)
                                    num = 4;
                                else
                                    num = 3;
                            }
                            //itemDecoration(num,baseCategory.getContentImageType());

                            getBinding().listRecyclerview.setAdapter(commonLandscapeAdapter);
                        } else
                            commonLandscapeAdapter.notifydata(playlistRailData.getEnveuVideoItemBeans());

                        mIsLoading = playlistRailData.getMaxContent() != commonLandscapeAdapter.getItemCount();
                    } else if (baseCategory.getContentImageType().equalsIgnoreCase(ImageType.LDS2.name())) {

                        if (commonLandscapeAdapter == null) {
                            new RecyclerAnimator(this).animate(getBinding().listRecyclerview);
                            commonLandscapeAdapter = new LandscapeListingAdapter(this, playlistRailData.getEnveuVideoItemBeans(), new ArrayList<>(), "VIDEO", this, baseCategory);

                            num = 2;
                            boolean tabletSize = GridActivity.this.getResources().getBoolean(R.bool.isTablet);
                            if (tabletSize) {
                                //landscape
                                if (GridActivity.this.getResources().getConfiguration().orientation == 2)
                                    num = 4;
                                else
                                    num = 3;
                            }
                            itemDecoration();

                            getBinding().listRecyclerview.setAdapter(commonLandscapeAdapter);
                        } else
                            commonLandscapeAdapter.notifydata(playlistRailData.getEnveuVideoItemBeans());

                        mIsLoading = playlistRailData.getMaxContent() != commonLandscapeAdapter.getItemCount();
                    } else if (baseCategory.getContentImageType().equalsIgnoreCase(ImageType.PR1.name())) {

                        if (commonPotraitAdapter == null) {
                            new RecyclerAnimator(this).animate(getBinding().listRecyclerview);
                            commonPotraitAdapter = new CommonPotraitAdapter(this, playlistRailData.getEnveuVideoItemBeans(), "VIDEO", new ArrayList<>(), this, baseCategory);

                            num = 3;
                            boolean tabletSize = GridActivity.this.getResources().getBoolean(R.bool.isTablet);
                            if (tabletSize) {
                                //landscape
                                if (GridActivity.this.getResources().getConfiguration().orientation == 2)
                                    num = 5;
                                else
                                    num = 4;
                            }
                            //  itemDecoration(num,baseCategory.getContentImageType());


                            getBinding().listRecyclerview.setAdapter(commonPotraitAdapter);
                        } else
                            commonPotraitAdapter.notifydata(playlistRailData.getEnveuVideoItemBeans());

                        mIsLoading = playlistRailData.getMaxContent() != commonPotraitAdapter.getItemCount();
                    } else if (baseCategory.getContentImageType().equalsIgnoreCase(ImageType.PR2.name())) {

                        if (commonPotraitTwoAdapter == null) {
                            new RecyclerAnimator(this).animate(getBinding().listRecyclerview);
                            commonPotraitTwoAdapter = new CommonPotraitTwoAdapter(this, playlistRailData.getEnveuVideoItemBeans(), "VIDEO", new ArrayList<>(), this, baseCategory);

                            num = 3;
                            boolean tabletSize = GridActivity.this.getResources().getBoolean(R.bool.isTablet);
                            if (tabletSize) {
                                //landscape
                                if (GridActivity.this.getResources().getConfiguration().orientation == 2)
                                    num = 5;
                                else
                                    num = 4;
                            }
                            itemDecoration();


                            getBinding().listRecyclerview.setAdapter(commonPotraitTwoAdapter);
                        } else
                            commonPotraitTwoAdapter.notifydata(playlistRailData.getEnveuVideoItemBeans());

                        mIsLoading = playlistRailData.getMaxContent() != commonPotraitTwoAdapter.getItemCount();
                    } else if (baseCategory.getContentImageType().equalsIgnoreCase(ImageType.LDS.name())) {

                        if (commonPosterLandscapeAdapter == null) {
                            new RecyclerAnimator(this).animate(getBinding().listRecyclerview);
                            commonPosterLandscapeAdapter = new CommonPosterLandscapeAdapter(this, playlistRailData.getEnveuVideoItemBeans(), new ArrayList<>(), "VIDEO", new ArrayList<>(), baseCategory);

                            num = 2;
                            boolean tabletSize = GridActivity.this.getResources().getBoolean(R.bool.isTablet);
                            if (tabletSize) {
                                //landscape
                                if (GridActivity.this.getResources().getConfiguration().orientation == 2)
                                    num = 4;
                                else
                                    num = 3;
                            }
                            itemDecoration();

                            //  getBinding().listRecyclerview.addItemDecoration(new SpacingItemDecoration(2, SpacingItemDecoration.HORIZONTAL));
                            getBinding().listRecyclerview.setAdapter(commonPosterLandscapeAdapter);
                        } else
                            commonPosterLandscapeAdapter.notifydata(playlistRailData.getEnveuVideoItemBeans());

                        mIsLoading = playlistRailData.getMaxContent() != commonPosterLandscapeAdapter.getItemCount();
                    } else if (baseCategory.getContentImageType().equalsIgnoreCase(ImageType.PR1.name())) {
                        if (commonPosterPotraitAdapter == null) {
                            new RecyclerAnimator(this).animate(getBinding().listRecyclerview);
                            commonPosterPotraitAdapter = new CommonPosterPotraitAdapter(this, playlistRailData.getEnveuVideoItemBeans(), new ArrayList<>(), "VIDEO", new ArrayList<>(), this, baseCategory);
                            num = 3;
                            boolean tabletSize = GridActivity.this.getResources().getBoolean(R.bool.isTablet);
                            if (tabletSize) {
                                //landscape
                                if (GridActivity.this.getResources().getConfiguration().orientation == 2)
                                    num = 5;
                                else
                                    num = 4;
                            }
                            itemDecoration();
                            //     getBinding().listRecyclerview.addItemDecoration(new SpacingItemDecoration(12, SpacingItemDecoration.HORIZONTAL));
                            getBinding().listRecyclerview.setAdapter(commonPosterPotraitAdapter);
                        } else
                            commonPosterPotraitAdapter.notifydata(playlistRailData.getEnveuVideoItemBeans());

                        mIsLoading = playlistRailData.getMaxContent() != commonPosterPotraitAdapter.getItemCount();
                    }

                    getBinding().listRecyclerview.scrollToPosition(mScrollY);
                }
            }
        }
    }

    private void setUiComponents() {
        if (playlistRailData.getEnveuVideoItemBeans().size() > 0) {
            if (baseCategory.getContentImageType().equalsIgnoreCase(ImageType.CIR.name())) {
                commonCircleAdapter.notifydata(playlistRailData.getEnveuVideoItemBeans());
                mIsLoading = playlistRailData.getMaxContent() != commonCircleAdapter.getItemCount();

            } else if (baseCategory.getContentImageType().equalsIgnoreCase(ImageType.SQR.name())) {
                if (squareCommonAdapter != null) {
                    squareCommonAdapter.notifydata(playlistRailData.getEnveuVideoItemBeans());
                    mIsLoading = playlistRailData.getMaxContent() != squareCommonAdapter.getItemCount();
                }

            } else if (baseCategory.getContentImageType().equalsIgnoreCase(ImageType.PR1.name())) {

                commonPotraitAdapter.notifydata(playlistRailData.getEnveuVideoItemBeans());
                mIsLoading = playlistRailData.getMaxContent() != commonPotraitAdapter.getItemCount();

            } else if (baseCategory.getContentImageType().equalsIgnoreCase(ImageType.PR2.name())) {

                commonPotraitTwoAdapter.notifydata(playlistRailData.getEnveuVideoItemBeans());
                mIsLoading = playlistRailData.getMaxContent() != commonPotraitTwoAdapter.getItemCount();

            } else if (baseCategory.getContentImageType().equalsIgnoreCase(ImageType.LDS.name())) {

                commonLandscapeAdapter.notifydata(playlistRailData.getEnveuVideoItemBeans());
                mIsLoading = playlistRailData.getMaxContent() != commonLandscapeAdapter.getItemCount();

            } else if (baseCategory.getContentImageType().equalsIgnoreCase(ImageType.LDS2.name())) {

                commonLandscapeAdapter.notifydata(playlistRailData.getEnveuVideoItemBeans());
                mIsLoading = playlistRailData.getMaxContent() != commonLandscapeAdapter.getItemCount();

            } else if (baseCategory.getContentImageType().equalsIgnoreCase(ImageType.PR1.name())) {
                commonPosterPotraitAdapter.notifydata(playlistRailData.getEnveuVideoItemBeans());
                mIsLoading = playlistRailData.getMaxContent() != commonPosterPotraitAdapter.getItemCount();

            } else {
                commonLandscapeAdapter.notifydata(playlistRailData.getEnveuVideoItemBeans());
                mIsLoading = playlistRailData.getMaxContent() != commonLandscapeAdapter.getItemCount();

            }
        } else {
            mIsLoading = false;
        }
    }

    private void itemDecoration() {
        /*if (baseCategory.getContentImageType().equalsIgnoreCase(ImageType.LDS.name())){
            spacing= (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 8, getResources().getDisplayMetrics());
        }else {
             spacing= (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 6, getResources().getDisplayMetrics());
        }


        getBinding().listRecyclerview.addItemDecoration(new EqualSpacingItemDecoration(spacing, EqualSpacingItemDecoration.GRID));
        if (enveuLayoutType == 0) {
            gridLayoutManager = new GridLayoutManager(this, i);
            getBinding().listRecyclerview.setLayoutManager(gridLayoutManager);
        } else {
            linearLayoutManager = new LinearLayoutManager(this);
            getBinding().listRecyclerview.setLayoutManager(linearLayoutManager);
        }*/

    }

    private void noConnectionLayout() {
        getBinding().noConnectionLayout.setVisibility(View.VISIBLE);
        getBinding().connection.retryTxt.setOnClickListener(view -> connectionObserver());
    }

    private void modelCall() {
        listingViewModel =new ViewModelProvider(this).get(ListingViewModel.class);

    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    public void onRowItemClicked() {
        Log.d("clickedItem", "list");
        try {
            AppCommonMethod.trackFcmEvent("Content Screen", "", getApplicationContext());
            AppCommonMethod.trackFcmCustomEvent(getApplicationContext(), AppConstants.CONTENT_SELECT, listData.getEnveuVideoItemBeans().get(position).getAssetType(), listData.getScreenWidget().getContentID(), title, pos, listData.getEnveuVideoItemBeans().get(position).getTitle(), position, listData.getEnveuVideoItemBeans().get(position).getId() + "", 0, 0, "", "", "", "");
        } catch (Exception ignored) {

        }
        if (listData.isSeries() && AppCommonMethod.getCheckKEntryId(itemValue.getkEntryId())) {
            String getVideoId = itemValue.getkEntryId();
            AppCommonMethod.launchDetailScreen(this, getVideoId, MediaTypeConstants.getInstance().getSeries(), itemValue.getId(), "0", false, itemValue);
        } else {
            if (AppCommonMethod.getCheckBCID(itemValue.getBrightcoveVideoId())) {
                String getVideoId = itemValue.getkEntryId();
                if (itemValue.getAssetType() != null) {
                    AppCommonMethod.launchDetailScreen(this, getVideoId, itemValue.getAssetType(), itemValue.getId(), "0", false, itemValue);
                } else {
                    AppCommonMethod.launchDetailScreen(this, getVideoId, AppConstants.Video, itemValue.getId(), "0", false, itemValue);
                }
            } else {
                if (itemValue.getAssetType() != null) {
                    AppCommonMethod.launchDetailScreen(this, "", itemValue.getAssetType(), itemValue.getId(), "0", false, itemValue);
                } else {
                    AppCommonMethod.launchDetailScreen(this, "", AppConstants.Video, itemValue.getId(), "0", false, itemValue);
                }
            }
        }
    }

    @Override
    public void onSaveInstanceState(Bundle outState, PersistableBundle outPersistentState) {
        super.onSaveInstanceState(outState, outPersistentState);

    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);

    }

    private void showDialog() {
        FragmentManager fm = getSupportFragmentManager();
        AlertDialogSingleButtonFragment alertDialog = AlertDialogSingleButtonFragment.newInstance(title, message, getResources().getString(R.string.ok));
        alertDialog.setCancelable(false);
        alertDialog.setAlertDialogCallBack(this);
        alertDialog.show(fm, "fragment_alert");
    }

    @Override
    public void onFinishDialog() {
        onBackPressed();
    }
}



