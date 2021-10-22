package panteao.make.ready.activities.watchList.ui;

import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.SystemClock;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.make.bookmarking.bean.BookmarkingResponse;
import com.make.enums.ImageType;
import panteao.make.ready.Bookmarking.BookmarkingViewModel;
import panteao.make.ready.activities.instructor.ui.InstructorActivity;
import panteao.make.ready.activities.show.ui.EpisodeActivity;
import panteao.make.ready.activities.watchList.adapter.WatchHistoryAdapter;
import panteao.make.ready.activities.watchList.adapter.WatchListAdapter;
import panteao.make.ready.activities.watchList.viewModel.WatchListViewModel;
import panteao.make.ready.baseModels.BaseBindingActivity;
import panteao.make.ready.R;
import panteao.make.ready.activities.listing.callback.ItemClickListener;
import panteao.make.ready.activities.listing.listadapter.ListAdapter;
import panteao.make.ready.activities.series.ui.SeriesDetailActivity;
import panteao.make.ready.adapters.CommonShimmerAdapter;
import panteao.make.ready.beanModel.AssetHistoryContinueWatching.ResponseAssetHistory;
import panteao.make.ready.beanModel.ContinueRailModel.CommonContinueRail;
import panteao.make.ready.beanModel.emptyResponse.ResponseEmpty;
import panteao.make.ready.beanModel.enveuCommonRailData.RailCommonData;
import panteao.make.ready.beanModelV3.uiConnectorModelV2.EnveuVideoItemBean;
import panteao.make.ready.beanModel.watchHistory.ItemsItem;
import panteao.make.ready.databinding.WatchListActivityBinding;
import panteao.make.ready.fragments.dialog.AlertDialogFragment;
import panteao.make.ready.fragments.dialog.AlertDialogSingleButtonFragment;
import panteao.make.ready.utils.commonMethods.AppCommonMethod;
import panteao.make.ready.utils.constants.AppConstants;
import panteao.make.ready.utils.cropImage.helpers.Logger;
import panteao.make.ready.utils.cropImage.helpers.NetworkConnectivity;
import panteao.make.ready.utils.helpers.CheckInternetConnection;
import panteao.make.ready.utils.helpers.RailInjectionHelper;
import panteao.make.ready.utils.helpers.RecyclerAnimator;

import panteao.make.ready.utils.helpers.ToastHandler;
import panteao.make.ready.utils.helpers.intentlaunchers.ActivityLauncher;
import com.google.gson.JsonObject;
import panteao.make.ready.utils.helpers.ksPreferenceKeys.KsPreferenceKeys;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;


public class WatchListActivity extends BaseBindingActivity<WatchListActivityBinding> implements WatchListAdapter.WatchListAdaperListener, WatchHistoryAdapter.WatchHistoryAdaperListener, WatchListAdapter.DeleteWatchList, AlertDialogFragment.AlertDialogListener, ItemClickListener, View.OnClickListener {
    private int counter = 0;
    private WatchListViewModel viewModel;
    private KsPreferenceKeys preference;
    private String token;
    private String viewType;
    private WatchListAdapter listAdapter;
    private WatchHistoryAdapter historyAdapter;
    private LinearLayoutManager mLayoutManager;
    private long mLastClickTime = 0;
    private boolean loading = true;
    private boolean deleteItem;
    private panteao.make.ready.beanModel.allWatchList.ItemsItem itemValue;
    private CommonShimmerAdapter adapterPurchase;
    private boolean isloggedout = false;
    private int totalCount;
    private boolean isRailData = false;
    private int counterLimit = 1;
    private boolean isCounterLimit = true;
    private BookmarkingViewModel bookmarkingViewModel;
    private RailInjectionHelper railInjectionHelper;
    private ListAdapter commonLandscapeAdapter;
    private boolean mIsLoading = true, isScrolling = false;
    private int mScrollY;
    private int flag, firstVisiblePosition, pastVisiblesItems, visibleItemCount, totalItemCount;
    private boolean isWatchHistory;

    private void callBinding() {
        viewModel = new ViewModelProvider(WatchListActivity.this).get(WatchListViewModel.class);
        bookmarkingViewModel = new ViewModelProvider(this).get(BookmarkingViewModel.class);
        railInjectionHelper = new ViewModelProvider(this).get(RailInjectionHelper.class);
        getBinding().connection.retryTxt.setOnClickListener(view -> connectionObserver());
        getBinding().retryLoadData.setOnClickListener(this);
        modelCall();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        callBinding();
    }

    private void modelCall() {
        totalCount = 0;
        loading = true;
        listAdapter = new WatchListAdapter(WatchListActivity.this, new ArrayList<>(), WatchListActivity.this, WatchListActivity.this);
        historyAdapter = new WatchHistoryAdapter(WatchListActivity.this, new ArrayList<>(), WatchListActivity.this);
        setRecyclerProperties(getBinding().watchListRecycler);
        getBinding().toolbar.llSearchIcon.setVisibility(View.GONE);
        getBinding().toolbar.backLayout.setVisibility(View.VISIBLE);
        getBinding().toolbar.homeIcon.setVisibility(View.GONE);
        getBinding().toolbar.mediaRouteButton.setVisibility(View.GONE);
        getBinding().toolbar.titleText.setVisibility(View.VISIBLE);
        connectionObserver();
    }

    private void setRecyclerProperties(RecyclerView recyclerView) {
        recyclerView.setNestedScrollingEnabled(false);
        recyclerView.setHasFixedSize(true);
        mLayoutManager = new LinearLayoutManager(this, RecyclerView.VERTICAL, false);
        recyclerView.setLayoutManager(mLayoutManager);
    }

    private void connectionObserver() {
        if (NetworkConnectivity.isOnline(this)) {
            connectionValidation(true);
        } else {
            connectionValidation(false);
        }
    }

    private void connectionValidation(Boolean aBoolean) {
        if (aBoolean) {
            getBundleValue();
        } else {
            noConnectionLayout();
        }
    }

    private void getBundleValue() {

        if (getIntent().hasExtra("bundleId")) {
            Bundle extras = getIntent().getExtras();
            if (extras != null) {
                extras = extras.getBundle("bundleId");
                viewType = Objects.requireNonNull(extras).getString("viewType");
                isWatchHistory = extras.getBoolean("isWatchHistory", false);
            }
        } else {
            throw new IllegalArgumentException("Activity cannot find  extras " + "Search_Show_All");
        }
        UIinitialization();
    }

    private void UIinitialization() {
        counter = 0;
        loading = true;
        preference = KsPreferenceKeys.getInstance();
        token = preference.getAppPrefAccessToken();
        getBinding().noConnectionLayout.setVisibility(View.GONE);
        getBinding().nodatafounmd.setVisibility(View.GONE);
        getBinding().toolbar.screenText.setText(viewType);
        if (viewType.equalsIgnoreCase(getResources().getString(R.string.my_history))) {
            callShimmer(WatchListActivity.this, getBinding().watchListRecycler);
            getWatchHistoryList();
        } else {
            callShimmer(WatchListActivity.this, getBinding().watchListRecycler);
            getWatchListData();
        }

        getBinding().watchListRecycler.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                LinearLayoutManager layoutManager = ((LinearLayoutManager) recyclerView.getLayoutManager());
                firstVisiblePosition = Objects.requireNonNull(layoutManager).findFirstVisibleItemPosition();
                if (dy > 0) {
                    visibleItemCount = layoutManager.getChildCount();
                    totalItemCount = layoutManager.getItemCount();
                    pastVisiblesItems = layoutManager.findFirstVisibleItemPosition();
                    if (mIsLoading) {
                        if ((visibleItemCount + pastVisiblesItems) >= totalItemCount) {
                            int adapterSize = getBinding().watchListRecycler.getAdapter().getItemCount();
                            if (adapterSize > 8) {
                                mIsLoading = false;
                                counter++;
                                isScrolling = true;
                                mScrollY += dy;
                                getWatchHistoryList();
                            }
                        }
                    }
                }
            }
        });
        getBinding().toolbar.backLayout.setOnClickListener(view -> onBackPressed());

    }

    private void getWatchListData() {
        try {
            token = preference.getAppPrefAccessToken();
            if (counter <= counterLimit - 1) {
                bookmarkingViewModel.getMywatchListData(token, counter, AppConstants.PAGE_SIZE).observe(this, responseWatchHistoryAssetList -> {
                    if (responseWatchHistoryAssetList.isStatus()) {
                        if (responseWatchHistoryAssetList.getData() != null) {
                            counterLimit = responseWatchHistoryAssetList.getData().getTotalPages();
                            String videoIds = "";
                            if (responseWatchHistoryAssetList.getData() != null) {
                                List<com.make.watchHistory.beans.ItemsItem> watchHistoryList = responseWatchHistoryAssetList.getData().getItems();
                                for (com.make.watchHistory.beans.ItemsItem historyItem : watchHistoryList) {
                                    videoIds = videoIds.concat(String.valueOf(historyItem.getAssetId())).concat(",");
                                }
                                railInjectionHelper.getWatchHistoryAssets(watchHistoryList, videoIds).observe(this, new Observer<RailCommonData>() {
                                    @Override
                                    public void onChanged(RailCommonData railCommonData) {
                                        if (railCommonData != null) {
                                            getBinding().watchListRecycler.setVisibility(View.VISIBLE);
                                            setRail(railCommonData);
                                        } else {
                                            getBinding().watchListRecycler.setVisibility(View.GONE);
                                            getBinding().nodatafounmd.setVisibility(View.VISIBLE);
                                            getBinding().progressBar.setVisibility(View.GONE);
                                            //  getBinding().noData.setBackgroundResource(R.drawable.watchhistoryblankimg);
                                        }
                                    }
                                });
                            } else {
                                if (historyAdapter.getItemCount() <= 0) {
                                    getBinding().watchListRecycler.setVisibility(View.GONE);
                                    getBinding().nodatafounmd.setVisibility(View.VISIBLE);
                                    getBinding().progressBar.setVisibility(View.GONE);
                                    // getBinding().noData.setBackgroundResource(R.drawable.watchhistoryblankimg);
                                }
                            }
                        } else {
                            //
                        }
                    } else {
                        if (responseWatchHistoryAssetList != null && responseWatchHistoryAssetList.getResponseCode() == 4302) {
                            getBinding().watchListRecycler.setVisibility(View.GONE);
                            getBinding().nodatafounmd.setVisibility(View.VISIBLE);
                            getBinding().progressBar.setVisibility(View.GONE);
                            logoutCall();
                        } else {
                            getBinding().watchListRecycler.setVisibility(View.GONE);
                            getBinding().progressBar.setVisibility(View.GONE);
                            getBinding().nodatafounmd.setVisibility(View.VISIBLE);
                        }
                    }

                });
            }
        } catch (Exception e) {
        }
    }

    private void logoutCall() {
        if (CheckInternetConnection.isOnline(Objects.requireNonNull(this))) {
            clearCredientials(preference);
            hitApiLogout(this, preference.getAppPrefAccessToken());
        } else {
            new ToastHandler(this).show(getResources().getString(R.string.no_internet_connection));
        }
    }


    private void noConnectionLayout() {
        getBinding().noConnectionLayout.setVisibility(View.VISIBLE);
    }

    private void showDialog(String title, String message) {
        FragmentManager fm = getSupportFragmentManager();
        AlertDialogSingleButtonFragment alertDialog = AlertDialogSingleButtonFragment.newInstance(title, message, getResources().getString(R.string.ok));
        alertDialog.setCancelable(false);
        alertDialog.setAlertDialogCallBack(this);
        alertDialog.show(fm, "fragment_alert");
    }

    @Override
    public WatchListActivityBinding inflateBindingLayout(@NonNull LayoutInflater inflater) {
        return WatchListActivityBinding.inflate(inflater);
    }

    @Override
    public void onWatchHistoryItemClicked(ItemsItem itemValue) {
           Log.d("watchHistory","itemclick");
        if (SystemClock.elapsedRealtime() - mLastClickTime < 1200) {
            return;
        }
        mLastClickTime = SystemClock.elapsedRealtime();
        try {
            if (itemValue.getVideoType().equalsIgnoreCase("SERIES")) {
                new ActivityLauncher(this).detailScreen(this, InstructorActivity.class, itemValue.getId(), "0", false);

            } else if (itemValue.getVideoType().equalsIgnoreCase("EPISODE")) {
                new ActivityLauncher(this).episodeScreen(this, EpisodeActivity.class, itemValue.getId(), "0", false);
            } else {
                new ActivityLauncher(this).detailScreen(this, InstructorActivity.class, itemValue.getId(), "0", false);
            }
        } catch (Exception e) {

        }


    }

    @Override
    public void onWatchListItemClicked(panteao.make.ready.beanModel.allWatchList.ItemsItem itemValue) {
        Log.d("watchHistory","itemclick");

        if (SystemClock.elapsedRealtime() - mLastClickTime < 1200) {
            return;
        }
        mLastClickTime = SystemClock.elapsedRealtime();

        if (itemValue.getContentType().equalsIgnoreCase("SERIES")) {
            new ActivityLauncher(this).seriesDetailScreen(this, SeriesDetailActivity.class, itemValue.getContentId());
        } else {
            if (itemValue.getAssetType().equalsIgnoreCase("EPISODE")) {
                new ActivityLauncher(this).episodeScreen(this, EpisodeActivity.class, itemValue.getContentId(), "0", false);
            } else {
                new ActivityLauncher(this).detailScreen(this, InstructorActivity.class, itemValue.getContentId(), "0", false);
            }
        }


    }

    @Override
    public void onDeleteClick(panteao.make.ready.beanModel.allWatchList.ItemsItem itemValue) {
        try {
            deleteItem = true;
            isloggedout = false;
            this.itemValue = itemValue;
            showDialog(WatchListActivity.this.getResources().getString(R.string.error), getResources().getString(R.string.delete_from_watchlist));

        } catch (Exception e) {
            Logger.i("WatchListAcivity", "less");
        }
    }

    private void hitApiRemoveList(int watchlistId) {
        JsonObject requestParam = new JsonObject();
        requestParam.addProperty(AppConstants.API_PARAM_WATCHLIST_ID, watchlistId);
        getBinding().progressBar.setVisibility(View.VISIBLE);
        viewModel.hitApiRemoveWatchList(token, String.valueOf(watchlistId)).observe(WatchListActivity.this, responseWatchList -> {
            getBinding().progressBar.setVisibility(View.GONE);
            deleteItem = false;
            if (Objects.requireNonNull(responseWatchList).isStatus()) {

                listAdapter.deleteComment(itemValue.getId());
                if (listAdapter.getItemCount() > 0) {

                } else {
                    if (viewType.equalsIgnoreCase(getResources().getString(R.string.my_watchlist))) {
                        getBinding().watchListRecycler.setVisibility(View.GONE);
                        getBinding().nodatafounmd.setVisibility(View.VISIBLE);
                        //   getBinding().noData.setBackgroundResource(R.drawable.watchlistblankimg);

                    } else {
                        getBinding().watchListRecycler.setVisibility(View.GONE);
                        getBinding().nodatafounmd.setVisibility(View.VISIBLE);
                        //    getBinding().noData.setBackgroundResource(R.drawable.watchhistoryblankimg);

                    }
                }
                // getWatchListData();
                Logger.e("", "hitApiAddWatchList");
            } else {
                if (responseWatchList.getResponseCode() == 401) {
                    isloggedout = true;
                    deleteItem = false;
                    showDialog(WatchListActivity.this.getResources().getString(R.string.logged_out), getResources().getString(R.string.you_are_logged_out));
                }
            }
        });
    }

    @Override
    public void onFinishDialog() {
        if (deleteItem) {
            hitApiRemoveList(itemValue.getId());
        } else if (isloggedout) {
            forceLogout();
        }
    }

    private void callShimmer(Context context, RecyclerView recyclerView) {
        adapterPurchase = new CommonShimmerAdapter(context);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(context);
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(adapterPurchase);
    }

    public void forceLogout() {
        if (isloggedout) {
            isloggedout = false;
            hitApiLogout(WatchListActivity.this, preference.getAppPrefAccessToken());
        }

    }


    @Override
    protected void onPause() {
        super.onPause();
    }


    public void getWatchHistoryList() {
        try {
            token = preference.getAppPrefAccessToken();
            if (counter <= counterLimit - 1) {
                bookmarkingViewModel.getWatchHistory(token, counter, AppConstants.PAGE_SIZE).observe(this, responseWatchHistoryAssetList -> {
                    if (responseWatchHistoryAssetList.isStatus()) {
                        if (responseWatchHistoryAssetList.getData() != null) {
                            counterLimit = responseWatchHistoryAssetList.getData().getTotalPages();
                            String videoIds = "";
                            if (responseWatchHistoryAssetList.getData() != null) {
                                List<com.make.watchHistory.beans.ItemsItem> watchHistoryList = responseWatchHistoryAssetList.getData().getItems();
                                for (com.make.watchHistory.beans.ItemsItem historyItem : watchHistoryList
                                ) {
                                    videoIds = videoIds.concat(String.valueOf(historyItem.getAssetId())).concat(",");
                                }
                                railInjectionHelper.getWatchHistoryAssets(watchHistoryList, videoIds).observe(this, new Observer<RailCommonData>() {
                                    @Override
                                    public void onChanged(RailCommonData railCommonData) {
                                        if (railCommonData != null) {
                                            getBinding().watchListRecycler.setVisibility(View.VISIBLE);
                                            setRail(railCommonData);
                                        } else {
                                            getBinding().watchListRecycler.setVisibility(View.GONE);
                                            getBinding().nodatafounmd.setVisibility(View.VISIBLE);
                                            getBinding().progressBar.setVisibility(View.GONE);
                                            //      getBinding().noData.setBackgroundResource(R.drawable.watchhistoryblankimg);
                                        }
                                    }
                                });
                            } else {
                                if (historyAdapter.getItemCount() <= 0) {
                                    getBinding().watchListRecycler.setVisibility(View.GONE);
                                    getBinding().nodatafounmd.setVisibility(View.VISIBLE);
                                    getBinding().progressBar.setVisibility(View.GONE);
                                    //  getBinding().noData.setBackgroundResource(R.drawable.watchhistoryblankimg);
                                }
                            }
                        }

                    } else {
                        if (responseWatchHistoryAssetList != null && responseWatchHistoryAssetList.getResponseCode() == 4302) {
                            getBinding().watchListRecycler.setVisibility(View.GONE);
                            getBinding().nodatafounmd.setVisibility(View.VISIBLE);
                            getBinding().progressBar.setVisibility(View.GONE);
                            logoutCall();
                        } else {
                            getBinding().watchListRecycler.setVisibility(View.GONE);
                            getBinding().progressBar.setVisibility(View.GONE);
                            getBinding().nodatafounmd.setVisibility(View.VISIBLE);
                        }
                    }
                });
            }
        } catch (Exception e) {
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
                new RecyclerAnimator(this).animate(getBinding().watchListRecycler);
                commonLandscapeAdapter = new ListAdapter(this, playlistRailData.getEnveuVideoItemBeans(), this, AppCommonMethod.getListViewType(ImageType.LDS.name()));
                if (viewType.equalsIgnoreCase(getResources().getString(R.string.my_history)))
                    commonLandscapeAdapter.setWatchHistory();
                else
                    commonLandscapeAdapter.setWatchList();
                getBinding().watchListRecycler.setAdapter(commonLandscapeAdapter);
            } else {
                commonLandscapeAdapter.notifyAdapter(playlistRailData.getEnveuVideoItemBeans());
            }
            mIsLoading = playlistRailData.getMaxContent() != commonLandscapeAdapter.getItemCount();

            getBinding().watchListRecycler.scrollToPosition(mScrollY);
            getBinding().progressBar.setVisibility(View.GONE);

        }
    }

    private void setUiComponents(RailCommonData playlistRailData) {
        commonLandscapeAdapter.notifyAdapter(playlistRailData.getEnveuVideoItemBeans());
        mIsLoading = playlistRailData.getMaxContent() != commonLandscapeAdapter.getItemCount();
    }


    public void getHistoryAssetData(ResponseAssetHistory responseAssetHistory) {
        try {
            ArrayList<CommonContinueRail> tempAdapter = new ArrayList<>();
            viewModel.getContinueList(token, responseAssetHistory, true).observe(this, responseContinueList -> {

                if (responseContinueList.isStatus()) {
                    List<ItemsItem> items;
                    if (responseAssetHistory.getData() != null && responseContinueList.getData() != null) {
                        items = new ArrayList<>();
                        for (int i = 0; i < responseAssetHistory.getData().getItems().size(); i++) {
                            for (int j = 0; j < responseContinueList.getData().getContents().size(); j++) {
                                if (responseContinueList.getData().getContents().get(j) != null && responseAssetHistory.getData().getItems().get(i).getId() == responseContinueList.getData().getContents().get(j).getId() && responseAssetHistory.getData().getItems().get(i).isFinishedWatching()) {
                                    if (responseContinueList.getData().getContents().get(j) != null) {
                                        ItemsItem tempValue = new ItemsItem();

                                        tempValue.setId(responseContinueList.getData().getContents().get(j).getId());
                                        if (responseContinueList.getData().getContents().get(j).getAssetGenres() != null) {
                                            ArrayList<String> gener = new ArrayList<>();
                                            gener.add((String) responseContinueList.getData().getContents().get(j).getAssetGenres());
                                            tempValue.setGenres(gener);
                                        } else {

                                        }
                                        tempValue.setStatus(responseContinueList.getData().getContents().get(j).getStatus());
                                        tempValue.setVideoType(AppConstants.Video);
                                        tempValue.setGenres(responseContinueList.getData().getContents().get(j).getGenres());
                                        tempValue.setImageLandscape(responseContinueList.getData().getContents().get(j).getLandscapeImage());
                                        tempValue.setTitle(responseContinueList.getData().getContents().get(j).getTitle());
                                        Logger.e("", "tempValue" + tempValue.toString());
                                        items.add(tempValue);
                                    }
                                }
                            }
                        }

                        new Handler().postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                if (items.size() > 0) {
                                    getBinding().watchListRecycler.setVisibility(View.VISIBLE);
                                    getBinding().nodatafounmd.setVisibility(View.GONE);
                                    getBinding().watchListRecycler.setLayoutManager(mLayoutManager);
                                    historyAdapter.notifyAdapter(items);
                                    getBinding().watchListRecycler.setAdapter(historyAdapter);
                                }

                                if (counterLimit == counter) {
                                    if (historyAdapter.getItemCount() <= 0) {
                                        getBinding().watchListRecycler.setVisibility(View.GONE);
                                        getBinding().nodatafounmd.setVisibility(View.VISIBLE);
                                        // getBinding().noData.setBackgroundResource(R.drawable.watchhistoryblankimg);
                                    }
                                } else {
                                    if (historyAdapter.getItemCount() <= 0) {
                                        //      Toast.makeText(WatchListActivity.this, "autoCall", Toast.LENGTH_SHORT).show();
                                        getWatchHistoryList();
                                    }
                                }
                                setLoaderValue();

                            }
                        }, 100);


                    } else {
                        setLoaderValue();

                    }

                } else {
                    setLoaderValue();
                }

            });
        } catch (Exception e) {
        }

    }


    public void setLoaderValue() {
        isRailData = true;
        if (counterLimit != counter) {
            loading = true;
        } else {
            loading = false;
        }
        counter = counter + 1;
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        if (viewModel != null) {
            viewModel.callCleared();
        }
    }


    @Override
    public void onRowItemClicked(EnveuVideoItemBean itemValue, int position) {
        AppCommonMethod.trackFcmEvent("Content Screen","", getApplicationContext(),0);

        if (AppCommonMethod.getCheckBCID(itemValue.getkEntryId())) {
            String getVideoId = itemValue.getkEntryId();
            if (itemValue.getAssetType() != null) {
                AppCommonMethod.launchDetailScreen(this, getVideoId, itemValue.getAssetType(), itemValue.getId(), "0", false,itemValue);
            } else {
                AppCommonMethod.launchDetailScreen(this, getVideoId, AppConstants.Video, itemValue.getId(), "0", false,itemValue);
            }
        }else {
            AppCommonMethod.launchDetailScreen(this, "", itemValue.getAssetType(), itemValue.getId(), "0", false,itemValue);
        }
    }

    @Override
    public void onDeleteWatchHistoryClicked(int assetId, int position) {
        FragmentManager fm = getSupportFragmentManager();
        AlertDialogFragment alertDialog = AlertDialogFragment.newInstance("", getResources().getString(R.string.delete_from_watchhistory_message), getResources().getString(R.string.ok), getResources().getString(R.string.cancel));
        alertDialog.setAlertDialogCallBack(new AlertDialogFragment.AlertDialogListener() {
            @Override
            public void onFinishDialog() {
                getBinding().progressBar.setVisibility(View.VISIBLE);
                getBinding().watchListRecycler.setVisibility(View.GONE);
                bookmarkingViewModel.deleteFromWatchHistory(token, assetId).observe(WatchListActivity.this, new Observer<BookmarkingResponse>() {
                    @Override
                    public void onChanged(BookmarkingResponse bookmarkingResponse) {
                        if (bookmarkingResponse != null) {
                            Toast.makeText(WatchListActivity.this, getString(R.string.delete_from_watchhistory), Toast.LENGTH_LONG).show();
                            counter = 0;
                            commonLandscapeAdapter.clear();
                            getWatchHistoryList();
                        } else {
                            Toast.makeText(WatchListActivity.this, getString(R.string.delete_from_watchhistory_error), Toast.LENGTH_LONG).show();
                            getBinding().progressBar.setVisibility(View.GONE);
                        }
                    }
                });
            }
        });
        alertDialog.show(Objects.requireNonNull(fm), "fragment_alert");
    }

    @Override
    public void onDeleteWatchListClicked(int assetId, int position) {
        FragmentManager fm = getSupportFragmentManager();
        AlertDialogFragment alertDialog = AlertDialogFragment.newInstance("", getResources().getString(R.string.delete_from_watchlist), getResources().getString(R.string.ok), getResources().getString(R.string.cancel));
        alertDialog.setAlertDialogCallBack(new AlertDialogFragment.AlertDialogListener() {
            @Override
            public void onFinishDialog() {
                getBinding().progressBar.setVisibility(View.VISIBLE);
                bookmarkingViewModel.hitRemoveWatchlist(token, assetId).observe(WatchListActivity.this, new Observer<ResponseEmpty>() {
                    @Override
                    public void onChanged(ResponseEmpty responseEmpty) {
                        if (responseEmpty != null && responseEmpty.isStatus()) {
                            Toast.makeText(WatchListActivity.this, getString(R.string.delete_from_watch_list_message), Toast.LENGTH_LONG).show();
                            counter = 0;
                            commonLandscapeAdapter.clear();
                            getWatchListData();
                        } else {
                            Toast.makeText(WatchListActivity.this, getString(R.string.delete_from_watch_list_message_error), Toast.LENGTH_LONG).show();
                            getBinding().progressBar.setVisibility(View.GONE);
                        }
                    }
                });
            }
        });
        alertDialog.show(Objects.requireNonNull(fm), "fragment_alert");
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.retry_load_data: {
                counter = 0;
                counterLimit = 1;
                getBinding().progressBar.setVisibility(View.VISIBLE);
                connectionObserver();
            }
            break;
        }
    }
}
