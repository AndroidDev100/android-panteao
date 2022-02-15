package panteao.make.ready.activities.myPurchases.ui;

import android.annotation.SuppressLint;
import android.content.IntentFilter;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.bottomnavigation.BottomNavigationMenuView;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.bottomnavigation.LabelVisibilityMode;
import com.google.android.material.navigation.NavigationBarView;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.play.core.appupdate.AppUpdateInfo;
import com.google.android.play.core.install.InstallStateUpdatedListener;
import com.google.android.play.core.install.model.AppUpdateType;
import com.google.android.play.core.install.model.InstallStatus;
import com.google.gson.Gson;

import org.jetbrains.annotations.NotNull;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import panteao.make.ready.PanteaoApplication;
import panteao.make.ready.R;
import panteao.make.ready.activities.live.LiveActivity;
import panteao.make.ready.activities.myPurchases.PagedList;
import panteao.make.ready.activities.myPurchases.viewmodel.MyPurchaseViewModel;
import panteao.make.ready.activities.profile.ui.ProfileActivityNew;
import panteao.make.ready.activities.purchase.ui.PurchaseActivity;
import panteao.make.ready.activities.purchase.ui.viewmodel.PurchaseViewModel;
import panteao.make.ready.activities.usermanagment.ui.LoginActivity;
import panteao.make.ready.activities.usermanagment.viewmodel.RegistrationLoginViewModel;
import panteao.make.ready.adapters.MyPurchasesAdapter;
import panteao.make.ready.baseModels.BaseBindingActivity;
import panteao.make.ready.beanModel.responseModels.Item;
import panteao.make.ready.beanModel.responseModels.MyPurchasesResponseModel;
import panteao.make.ready.callbacks.commonCallbacks.FragmentClickNetwork;
import panteao.make.ready.callbacks.commonCallbacks.HomeClickNetwork;
import panteao.make.ready.callbacks.commonCallbacks.NetworkChangeReceiver;
import panteao.make.ready.callbacks.commonCallbacks.OriginalFragmentClick;
import panteao.make.ready.callbacks.commonCallbacks.PremiumClick;
import panteao.make.ready.callbacks.commonCallbacks.SinetronClick;
import panteao.make.ready.databinding.ActivityMainBinding;
import panteao.make.ready.databinding.ActivityMyPurchasesBinding;
import panteao.make.ready.fragments.dialog.AlertDialogFragment;
import panteao.make.ready.fragments.dialog.AlertDialogSingleButtonFragment;
import panteao.make.ready.fragments.home.ui.HomeFragment;
import panteao.make.ready.fragments.more.ui.MoreFragment;
import panteao.make.ready.fragments.movies.ui.MovieFragment;
import panteao.make.ready.fragments.shows.ui.ShowsFragment;
import panteao.make.ready.utils.commonMethods.AppCommonMethod;
import panteao.make.ready.utils.constants.AppConstants;
import panteao.make.ready.utils.cropImage.helpers.Logger;
import panteao.make.ready.utils.cropImage.helpers.NetworkConnectivity;
import panteao.make.ready.utils.helpers.ActivityTrackers;
import panteao.make.ready.utils.helpers.AnalyticsController;
import panteao.make.ready.utils.helpers.CheckInternetConnection;
import panteao.make.ready.utils.helpers.StringUtils;
import panteao.make.ready.utils.helpers.ToastHandler;
import panteao.make.ready.utils.helpers.ToolBarHandler;
import panteao.make.ready.utils.helpers.database.preferences.UserPreference;
import panteao.make.ready.utils.helpers.downloads.downloadListing.MyDownloadsFragment;
import panteao.make.ready.utils.helpers.intentlaunchers.ActivityLauncher;
import panteao.make.ready.utils.helpers.ksPreferenceKeys.KsPreferenceKeys;
import panteao.make.ready.utils.inAppUpdate.AppUpdateCallBack;
import panteao.make.ready.utils.inAppUpdate.ApplicationUpdateManager;


public class MyPurchasesActivity extends BaseBindingActivity<ActivityMyPurchasesBinding> implements AlertDialogFragment.AlertDialogListener, NetworkChangeReceiver.ConnectivityReceiverListener {

    public Fragment active;
    private KsPreferenceKeys preference;
    private String strCurrentTheme = "";
    private BottomNavigationView navigation;
    private int initialPageSize = 20;
    private boolean mIsLoading=false;
    private boolean mIsLastPage;
    private LinearLayoutManager layoutManager;
    private int mCurrentPage = 0;
    private Boolean isFirstPage = false;

    @SuppressLint({"RestrictedApi", "WrongConstant"})
    public static void removeNavigationShiftMode(BottomNavigationView view) {
        BottomNavigationMenuView menuView = (BottomNavigationMenuView) view.getChildAt(0);
        menuView.setLabelVisibilityMode(LabelVisibilityMode.LABEL_VISIBILITY_LABELED);
        menuView.buildMenuView();
    }

    @Override
    public ActivityMyPurchasesBinding inflateBindingLayout(@NonNull LayoutInflater inflater) {
        return ActivityMyPurchasesBinding.inflate(inflater);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        strCurrentTheme = KsPreferenceKeys.getInstance().getCurrentTheme();
        mIsLoading = false;
        mIsLastPage = false;
        Logger.d("CurrentThemeIs", strCurrentTheme);
        if (KsPreferenceKeys.getInstance().getCurrentTheme().equalsIgnoreCase(AppConstants.LIGHT_THEME)) {
            setTheme(R.style.MyMaterialTheme_Base_Light);
        } else {
            setTheme(R.style.MyMaterialTheme_Base_Dark);
        }
        preference = KsPreferenceKeys.getInstance();
        callBinding();
        initUI();
        initToolbar();
        if (KsPreferenceKeys.getInstance().getAppPrefLoginStatus()) {
            initRecyclerView();
        } else {
            showDialog(getString(R.string.user_not_logged_in), getString(R.string.please_login_to_see_my_purchases));
        }


    }

    private void initUI() {
        getBinding().nodatafounmd.setVisibility(View.GONE);
        getBinding().progressBar.setVisibility(View.GONE);
        getBinding().noConnectionLayout.setVisibility(View.GONE);
        getBinding().connection.retryTxt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loadMoreItems(true);
            }
        });
        getBinding().retryLoadData.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loadMoreItems(true);
            }
        });
    }

    private void initToolbar() {
        getBinding().toolbar.llSearchIcon.setVisibility(View.GONE);
        getBinding().toolbar.backLayout.setVisibility(View.VISIBLE);
        getBinding().toolbar.homeIcon.setVisibility(View.GONE);
        getBinding().toolbar.mediaRouteButton.setVisibility(View.GONE);
        getBinding().toolbar.titleText.setVisibility(View.VISIBLE);
        getBinding().toolbar.screenText.setText(getString(R.string.my_purchases));

        getBinding().toolbar.backLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
    }

    MyPurchasesAdapter myRecyclerViewAdapter;

    private void initRecyclerView() {
        layoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        getBinding().recyclerMyPurchases.setLayoutManager(layoutManager);
        getBinding().recyclerMyPurchases.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                // number of visible items
                int visibleItemCount = layoutManager.getChildCount();
                // number of items in layout
                int totalItemCount = layoutManager.getItemCount();
                // the position of first visible item
                int firstVisibleItemPosition = layoutManager.findFirstVisibleItemPosition();

                boolean isNotLoadingAndNotLastPage = !mIsLoading && !mIsLastPage;
                // flag if number of visible items is at the last
                boolean isAtLastItem = firstVisibleItemPosition + visibleItemCount >= totalItemCount;
                // validate non negative values
                boolean isValidFirstItem = firstVisibleItemPosition >= 0;
                // validate total items are more than possible visible items
                boolean totalIsMoreThanVisible = totalItemCount >= initialPageSize;
                // flag to know whether to load more
                boolean shouldLoadMore = isValidFirstItem && isAtLastItem && totalIsMoreThanVisible && isNotLoadingAndNotLastPage;

                if (shouldLoadMore) {
                    loadMoreItems(false);
                }

            }
        });

        // load the first page
        loadMoreItems(true);

    }

    private MyPurchaseViewModel viewModel;
    Boolean isloggedout = false;

    private void showDialog(String title, String message) {
        FragmentManager fm = getSupportFragmentManager();
        AlertDialogSingleButtonFragment alertDialog = AlertDialogSingleButtonFragment.newInstance(title, message, getResources().getString(R.string.ok));
        alertDialog.setCancelable(false);
        alertDialog.setAlertDialogCallBack(this);
        alertDialog.show(fm, "fragment_alert");
    }

    private void logoutCall() {
        if (CheckInternetConnection.isOnline(Objects.requireNonNull(this))) {
            clearCredientials(preference);
            hitApiLogout(this, preference.getAppPrefAccessToken());
        }
    }

    public void loadMoreItems(Boolean isFirstpage) {
        this.isFirstPage = isFirstpage;
        if (KsPreferenceKeys.getInstance().getAppPrefLoginStatus()) {
            callMyPurchases(KsPreferenceKeys.getInstance().getAppPrefAccessToken(), String.valueOf(mCurrentPage), String.valueOf(initialPageSize), null, isFirstpage);
        } else {
            dismissLoading(getBinding().progressBar);
        }
    }

    List<Item> result = new ArrayList<>();
    MyPurchasesResponseModel myPurchaseModelResponse;

    public int getTotalPages(int totalItemCount, int perPageItems) {
        int result = (totalItemCount / perPageItems);
        if (totalItemCount % perPageItems == 0) {
            return result = result;
        } else {
            return result = (result + 1);
        }
    }

    public void callMyPurchases(String auth, String page, String size, @Nullable String orderStatus, Boolean isFirstpage) {
        mIsLoading = true;
        if (result.size() != 0) {
            mCurrentPage = mCurrentPage + 1;
        }
        if (CheckInternetConnection.isOnline(MyPurchasesActivity.this)) {
            getBinding().nodatafounmd.setVisibility(View.GONE);
            getBinding().noConnectionLayout.setVisibility(View.GONE);
            showLoading(getBinding().progressBar, true);
            viewModel.hitMyPurchasesAPI(MyPurchasesActivity.this, auth, String.valueOf(mCurrentPage), size, orderStatus).observe(MyPurchasesActivity.this, myPurchaseModelResponse -> {
                mIsLoading = false;
                dismissLoading(getBinding().progressBar);
                this.myPurchaseModelResponse = myPurchaseModelResponse;
                Logger.e("OrderHistorydata", new Gson().toJson(myPurchaseModelResponse));
                if (this.myPurchaseModelResponse != null && this.myPurchaseModelResponse.getResponseCode() == 2000 && this.myPurchaseModelResponse.isSuccessful()) {
                    if (this.myPurchaseModelResponse.getData() != null) {
                        if (this.myPurchaseModelResponse.getData().getItems() != null) {
                            if (this.result.size() == 0 && this.myPurchaseModelResponse.getData().getItems().size() == 0) {
                                getBinding().nodatafounmd.setVisibility(View.VISIBLE);
                            } else {
                                getBinding().nodatafounmd.setVisibility(View.GONE);
                                if (result.size() != 0) {
                                    this.result.addAll(this.myPurchaseModelResponse.getData().getItems());
                                    myRecyclerViewAdapter.notifyDataSetChanged();
                                } else {
                                    this.result.clear();
                                    this.result.addAll(this.myPurchaseModelResponse.getData().getItems());
                                    myRecyclerViewAdapter = new MyPurchasesAdapter(this, result);
                                    getBinding().recyclerMyPurchases.setAdapter(myRecyclerViewAdapter);
                                }
                                mIsLastPage = mCurrentPage == getTotalPages(result.size(), AppConstants.PAGE_SIZE);
                            }
                        } else {
                            getBinding().nodatafounmd.setVisibility(View.VISIBLE);
                        }
                    } else {
                        getBinding().nodatafounmd.setVisibility(View.VISIBLE);
                    }
                } else {
                    if (this.myPurchaseModelResponse.getDebugMessage() != null && this.myPurchaseModelResponse.getDebugMessage().trim() != "") {
                        showDialog(getString(R.string.error), this.myPurchaseModelResponse.getDebugMessage());
                    } else {
                        getBinding().nodatafounmd.setVisibility(View.VISIBLE);
                    }
                }
              /*  if (Objects.requireNonNull(myPurchaseModelResponse).getResponseCode() == 2000) {
                    this.result = (PagedList<Item>) myPurchaseModelResponse.getData().getItems();
                    if (result == null) return;

                    else if (!isFirstPage) myRecyclerViewAdapter.addAll(result.getResults());
                    else myRecyclerViewAdapter.setList(result.getResults());
                    mIsLastPage = mCurrentPage == result.getTotalPages();
                   // AppCommonMethod.trackFcmCustomEvent(getApplicationContext(), AppConstants.SIGN_IN_SUCCESS, "", "", "", 0, " ", 0, "", 0, 0, "", "", myPurchaseModelResponse.getData().getPageNumber() + "", myPurchaseModelResponse.getData().getPageSize() + "");

                } else {
                    if (myPurchaseModelResponse.getResponseCode() == 4302) {
                        isloggedout = true;
                        logoutCall();
                        try {
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    onBackPressed();
                                }
                            });
                        } catch (Exception e) {

                        }

                        // showDialog(getResources().getString(R.string.logged_out), getResources().getString(R.string.you_are_logged_out));
                    }
                    if (myPurchaseModelResponse.getDebugMessage() != null) {
                        showDialog(MyPurchasesActivity.this.getResources().getString(R.string.error), myPurchaseModelResponse.getDebugMessage().toString());
                    } else {
                        showDialog(MyPurchasesActivity.this.getResources().getString(R.string.error), MyPurchasesActivity.this.getResources().getString(R.string.something_went_wrong));
                    }*/


            });
        } else {
            mIsLoading = false;
            getBinding().noConnectionLayout.setVisibility(View.VISIBLE);
            dismissLoading(getBinding().progressBar);
//            new ToastHandler(MyPurchasesActivity.this).show(MyPurchasesActivity.this.getResources().getString(R.string.no_internet_connection));
        }
    }

    private void callBinding() {
        viewModel = new ViewModelProvider(this).get(MyPurchaseViewModel.class);
        callIntials();
    }

    public void callIntials() {
        if (StringUtils.isNullOrEmptyOrZero(AppCommonMethod.urlPoints)) {
            AppCommonMethod.urlPoints = preference.getAppPrefCfep();
        }

    }

    @Override
    protected void onResume() {
        super.onResume();
        AppCommonMethod.isPurchase = false;
        if (preference == null) {
            preference = KsPreferenceKeys.getInstance();
        }
        setBroadcast();
    }

    private NetworkChangeReceiver receiver = null;

    public void setBroadcast() {
        receiver = new NetworkChangeReceiver();
        IntentFilter filter = new IntentFilter();
        filter.addAction("android.net.conn.CONNECTIVITY_CHANGE");
        filter.addAction("android.net.wifi.WIFI_STATE_CHANGED");
        filter.addAction("android.net.wifi.STATE_CHANGE");
        MyPurchasesActivity.this.registerReceiver(receiver, filter);
        setConnectivityListener(this);
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (preference.getAppPrefIsRestoreState()) {
            preference.setAppPrefIsRestoreState(false);
        }
    }


    @Override
    protected void onRestoreInstanceState(@NotNull Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        if (preference == null)
            preference = KsPreferenceKeys.getInstance();

        preference.setAppPrefIsRestoreState(true);
        for (Fragment fragment : getSupportFragmentManager().getFragments()) {
            getSupportFragmentManager().beginTransaction().remove(fragment).commit();
        }
        callBinding();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }

    @Override
    protected void onStop() {
        super.onStop();
        try {
            if (receiver != null) {
                this.unregisterReceiver(receiver);
                NetworkChangeReceiver.connectivityReceiverListener = null;
            }
        } catch (Exception e) {

        }
    }

    @Override
    protected void onStart() {
        super.onStart();
    }

    @Override
    public void onFinishDialog() {
        if (isloggedout) {
            logoutCall();
        } else {
            onBackPressed();
        }
    }

    public void setConnectivityListener(NetworkChangeReceiver.ConnectivityReceiverListener
                                                listener) {
        NetworkChangeReceiver.connectivityReceiverListener = listener;
    }

    @Override
    public void onNetworkConnectionChanged(boolean isConnected) {
        AppCommonMethod.isInternet = isConnected;
        if (isConnected) {
            getBinding().noConnectionLayout.setVisibility(View.GONE);
            if (!mIsLoading) {
                loadMoreItems(true);
            }
        } else {
            getBinding().noConnectionLayout.setVisibility(View.VISIBLE);
        }
    }

}

