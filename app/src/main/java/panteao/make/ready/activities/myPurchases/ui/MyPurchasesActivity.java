package panteao.make.ready.activities.myPurchases.ui;

import android.annotation.SuppressLint;
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

import java.util.Objects;

import panteao.make.ready.PanteaoApplication;
import panteao.make.ready.R;
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


public class MyPurchasesActivity extends BaseBindingActivity<ActivityMyPurchasesBinding> implements AlertDialogFragment.AlertDialogListener {

    public Fragment active;
    private KsPreferenceKeys preference;
    private String strCurrentTheme = "";
    private BottomNavigationView navigation;
    private int initialPageSize = 20;
    private boolean mIsLoading;
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
        initRecyclerView();

    }

    MyPurchasesAdapter myRecyclerViewAdapter;

    private void initRecyclerView() {
        layoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        myRecyclerViewAdapter = new MyPurchasesAdapter(this, result.getResults());
        getBinding().setMyAdapter(myRecyclerViewAdapter);
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

    PagedList<Item> result = new PagedList<>();
    MyPurchasesResponseModel myPurchaseModelResponse;

    public void callMyPurchases(String auth, String page, String size, @Nullable String orderStatus, Boolean isFirstpage) {
        mIsLoading = true;
        mCurrentPage = mCurrentPage + 1;
        if (CheckInternetConnection.isOnline(MyPurchasesActivity.this)) {
            showLoading(getBinding().progressBar, true);
            viewModel.hitMyPurchasesAPI(MyPurchasesActivity.this, auth, page, size, orderStatus).observe(MyPurchasesActivity.this, myPurchaseModelResponse -> {
                mIsLoading = false;

                dismissLoading(getBinding().progressBar);
                this.myPurchaseModelResponse = myPurchaseModelResponse;
                if (Objects.requireNonNull(myPurchaseModelResponse).getResponseCode() == 2000) {
                    this.result = (PagedList<Item>) myPurchaseModelResponse.getData().getItems();
                    if (result == null) return;
                    if (result == null) return;
                    else if (!isFirstPage) myRecyclerViewAdapter.addAll(result.getResults());
                    else myRecyclerViewAdapter.setList(result.getResults());
                    mIsLastPage = mCurrentPage == result.getTotalPages();
                    AppCommonMethod.trackFcmCustomEvent(getApplicationContext(), AppConstants.SIGN_IN_SUCCESS, "", "", "", 0, " ", 0, "", 0, 0, "", "", myPurchaseModelResponse.getData().getPageNumber() + "", myPurchaseModelResponse.getData().getPageSize() + "");

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
                    }
                }

            });
        } else {
            mIsLoading = false;

            dismissLoading(getBinding().progressBar);
            new ToastHandler(MyPurchasesActivity.this).show(MyPurchasesActivity.this.getResources().getString(R.string.no_internet_connection));
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
}

