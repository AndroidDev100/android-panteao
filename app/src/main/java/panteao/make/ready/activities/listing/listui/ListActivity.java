package panteao.make.ready.activities.listing.listui;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.make.baseCollection.baseCategoryModel.BaseCategory;
import panteao.make.ready.activities.listing.callback.ItemClickListener;
import panteao.make.ready.activities.listing.listadapter.ListAdapter;
import panteao.make.ready.activities.listing.viewmodel.ListingViewModel;
import panteao.make.ready.baseModels.BaseBindingActivity;
import panteao.make.ready.networking.apistatus.APIStatus;
import panteao.make.ready.R;
import panteao.make.ready.adapters.CommonShimmerAdapter;
import panteao.make.ready.beanModel.enveuCommonRailData.RailCommonData;
import panteao.make.ready.beanModelV3.uiConnectorModelV2.EnveuVideoItemBean;
import panteao.make.ready.beanModel.responseModels.series.season.SeasonResponse;
import panteao.make.ready.databinding.ListingActivityBinding;
import panteao.make.ready.utils.MediaTypeConstants;
import panteao.make.ready.utils.commonMethods.AppCommonMethod;
import panteao.make.ready.utils.constants.AppConstants;
import panteao.make.ready.utils.cropImage.helpers.Logger;
import panteao.make.ready.utils.cropImage.helpers.NetworkConnectivity;
import panteao.make.ready.utils.cropImage.helpers.PrintLogging;
import panteao.make.ready.utils.helpers.RailInjectionHelper;
import panteao.make.ready.utils.helpers.RecyclerAnimator;

import java.util.Objects;

import panteao.make.ready.adapters.CommonShimmerAdapter;
import panteao.make.ready.baseModels.BaseBindingActivity;
import panteao.make.ready.beanModel.responseModels.series.season.SeasonResponse;
import panteao.make.ready.utils.MediaTypeConstants;

public class ListActivity extends BaseBindingActivity<ListingActivityBinding> implements ItemClickListener {
    String playListId;
    BaseCategory baseCategory;
    private ListingViewModel listingViewModel;
    private int counter = 0, flag, firstVisiblePosition, pastVisiblesItems, visibleItemCount, totalItemCount;
    private ListAdapter commonLandscapeAdapter;
    private LinearLayoutManager gridLayoutManager;
    private String title;
    private boolean mIsLoading = true, isScrolling = false;
    private int mScrollY;
    private int shimmerType;
    private RailCommonData listData;
    private long mLastClickTime = 0;

    @Override
    public ListingActivityBinding inflateBindingLayout(@NonNull LayoutInflater inflater) {
        return ListingActivityBinding.inflate(inflater);
    }


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        title = getIntent().getStringExtra("title");
        playListId = getIntent().getStringExtra("playListId");
        flag = getIntent().getIntExtra("flag", 0);
        shimmerType = getIntent().getIntExtra("shimmerType", 0);
        baseCategory = getIntent().getExtras().getParcelable("baseCategory");
        PrintLogging.printLog("", "ValueForImageTYpe " + baseCategory.getWidgetImageType());
        modelCall();
        UiIntilization();
        connectionObserver();
    }

    private void connectionObserver() {
        if (NetworkConnectivity.isOnline(ListActivity.this)) {
            getBinding().noConnectionLayout.setVisibility(View.GONE);
            connectionValidation(true);
        } else {
            connectionValidation(false);
        }
    }


    private void connectionValidation(Boolean aBoolean) {
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
        CommonShimmerAdapter adapterPurchase = new CommonShimmerAdapter(ListActivity.this);
        gridLayoutManager = new LinearLayoutManager(this);
        getBinding().listRecyclerview.setItemAnimator(new DefaultItemAnimator());
        getBinding().listRecyclerview.setLayoutManager(gridLayoutManager);
        getBinding().listRecyclerview.setAdapter(adapterPurchase);
        //SeparatorDecoration decoration = new SeparatorDecoration(this, getResources().getColor(R.color.home_screen_seperator), 0.9f);
        //getBinding().listRecyclerview.addItemDecoration(decoration);
        getBinding().listRecyclerview.setVisibility(View.VISIBLE);
    }


    private void UiIntilization() {
        getBinding().toolbar.backLayout.setOnClickListener(view -> onBackPressed());
        getBinding().listRecyclerview.setLayoutManager(gridLayoutManager);
        getBinding().toolbar.titleText.setVisibility(View.VISIBLE);
        getBinding().toolbar.screenText.setText(title);
        getBinding().toolbar.mediaRouteButton.setVisibility(View.GONE);
        getBinding().toolbar.homeIcon.setVisibility(View.GONE);
        getBinding().toolbar.backLayout.setVisibility(View.VISIBLE);
        getBinding().toolbar.searchIcon.setVisibility(View.GONE);
        getBinding().toolbar.screenText.setVisibility(View.VISIBLE);
        getBinding().listRecyclerview.hasFixedSize();
        getBinding().listRecyclerview.setNestedScrollingEnabled(false);

    }

    private void setClicks() {

        getBinding().listRecyclerview.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                try {
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
                } catch (Exception e) {
                    Logger.e("ListActivity", "" + e.toString());

                }
            }
        });

    }

    private void getRailData() {
        if (flag == 0) {
            RailInjectionHelper railInjectionHelper = ViewModelProviders.of(this).get(RailInjectionHelper.class);
            /*railInjectionHelper.getPlayListDetailsWithPaginationV2(this, playListId, counter, AppConstants.PAGE_SIZE, baseCategory).observe(this, playlistRailData -> {
                if (Objects.requireNonNull(playlistRailData) != null) {
                    try {
                        if (title == null || title.equalsIgnoreCase("")) {
                            getBinding().toolbar.screenText.setText(playlistRailData.getDisplayName());
                        }
                    } catch (Exception e) {

                    }
                    listData = playlistRailData;
                    setRail(playlistRailData);
                    Logger.e("RAIL DATA", String.valueOf(listData.isSeries()));
                }
            });*/

            railInjectionHelper.getPlayListDetailsWithPaginationV2(this, playListId, counter, AppConstants.PAGE_SIZE, baseCategory).observe(this, playlistRailData -> {
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

                            }
                            listData = railCommonData;
                            setRail(railCommonData);
                            Logger.e("RAIL DATA", String.valueOf(listData.isSeries()));
                        }
                    }
                } else if (playlistRailData.getStatus().equalsIgnoreCase(APIStatus.ERROR.name())) {

                } else if (playlistRailData.getStatus().equalsIgnoreCase(APIStatus.FAILURE.name())) {

                }

            });


        } else {

           /* if (AppCommonMethod.isSeasonCount) {
                listingViewModel.getVOD(playListId, counter, 20).observe(this, seasonResponse -> {
                    if (seasonResponse != null)
                        setSeasonData(seasonResponse);
                });
            } else {
                PrintLogging.printLog("", counter + "playplay");
                listingViewModel.getSeasonDetail(playListId, counter, 20).observe(this, seasonResponse -> {
                    if (seasonResponse != null)
                        setSeasonData(seasonResponse);
                });
            }*/
        }

    }

    private void setSeasonData(SeasonResponse seasonResponse) {
        if (!isScrolling) {
            PrintLogging.printLog("", isScrolling + "isScrolling");
            // commonLandscapeAdapter = new ListAdapter(this, new ArrayList<>(), seasonResponse.getData().getContinueWatchingBookmarks(), AppConstants.VOD);
            int num = 2;
            boolean tabletSize = ListActivity.this.getResources().getBoolean(R.bool.isTablet);
            if (tabletSize) {
                //landscape
                if (ListActivity.this.getResources().getConfiguration().orientation == 2)
                    num = 4;
                else
                    num = 3;
            }
            itemDecoration(num);


            getBinding().listRecyclerview.setAdapter(commonLandscapeAdapter);
            getBinding().progressBar.setVisibility(View.GONE);
        } else {
            commonLandscapeAdapter.notifyEpisodedata(seasonResponse.getData().getItems());
            mIsLoading = seasonResponse.getData().getPageInfo().getTotal() != commonLandscapeAdapter.getItemCount();
            getBinding().listRecyclerview.scrollToPosition(mScrollY);
        }
    }

    private void setRail(RailCommonData playlistRailData) {
        if (isScrolling) {
            setUiComponents(playlistRailData);
            getBinding().progressBar.setVisibility(View.GONE);
        } else {
            getBinding().progressBar.setVisibility(View.GONE);
            mIsLoading = true;

            if (commonLandscapeAdapter == null) {
                new RecyclerAnimator(this).animate(getBinding().listRecyclerview);
                commonLandscapeAdapter = new ListAdapter(this, playlistRailData.getEnveuVideoItemBeans(), this, AppCommonMethod.getListViewType(baseCategory.getContentImageType()));
                itemDecoration(0);

                getBinding().listRecyclerview.setAdapter(commonLandscapeAdapter);
            } else
                commonLandscapeAdapter.notifyAdapter(playlistRailData.getEnveuVideoItemBeans());

            mIsLoading = playlistRailData.getMaxContent() != commonLandscapeAdapter.getItemCount();

            getBinding().listRecyclerview.scrollToPosition(mScrollY);
        }
    }

    private void setUiComponents(RailCommonData playlistRailData) {
        if (playlistRailData.getEnveuVideoItemBeans().size() > 0) {
            commonLandscapeAdapter.notifyAdapter(playlistRailData.getEnveuVideoItemBeans());
            mIsLoading = playlistRailData.getMaxContent() != commonLandscapeAdapter.getItemCount();
        } else {
            mIsLoading = false;
        }
    }

    private void itemDecoration(int i) {
       /* int spacing = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 5, getResources().getDisplayMetrics());

        //  getBinding().listRecyclerview.addItemDecoration(new GridSpacingItemDecoration(i, spacing, true));

        getBinding().listRecyclerview.addItemDecoration(new EqualSpacingItemDecoration(spacing, EqualSpacingItemDecoration.VERTICAL));
        gridLayoutManager = new LinearLayoutManager(this);
        getBinding().listRecyclerview.setLayoutManager(gridLayoutManager);*/
    }

    private void noConnectionLayout() {
        getBinding().noConnectionLayout.setVisibility(View.VISIBLE);
        getBinding().connection.retryTxt.setOnClickListener(view -> connectionObserver());
    }

    private void modelCall() {
        listingViewModel = ViewModelProviders.of(this).get(ListingViewModel.class);
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
    public void onRowItemClicked(EnveuVideoItemBean itemValue, int position) {

        try {
//            AppCommonMethod.trackFcmEvent(itemValue.getTitle(), itemValue.getAssetType(), ListActivity.this, position);
        } catch (Exception e) {

        }
        if (listData.isSeries() && AppCommonMethod.getCheckBCID(itemValue.getBrightcoveVideoId())) {
            Long getVideoId = Long.parseLong(itemValue.getBrightcoveVideoId());
            AppCommonMethod.launchDetailScreen(this, getVideoId, MediaTypeConstants.getInstance().getSeries(), itemValue.getId(), "0", false);
        } else {
            if (AppCommonMethod.getCheckBCID(itemValue.getBrightcoveVideoId())) {
                Long getVideoId = Long.parseLong(itemValue.getBrightcoveVideoId());
                if (itemValue.getAssetType() != null) {
                    AppCommonMethod.launchDetailScreen(this, getVideoId, itemValue.getAssetType(), itemValue.getId(), "0", false);
                } else {
                    AppCommonMethod.launchDetailScreen(this, getVideoId, AppConstants.Video, itemValue.getId(), "0", false);
                }
            } else {
                if (itemValue.getAssetType() != null) {
                    AppCommonMethod.launchDetailScreen(this, 0l, itemValue.getAssetType(), itemValue.getId(), "0", false);
                } else {
                    AppCommonMethod.launchDetailScreen(this, 0l, AppConstants.Video, itemValue.getId(), "0", false);
                }
            }
        }
    }
}



