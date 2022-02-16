package panteao.make.ready.activities.series.ui;

import android.app.AlertDialog;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.res.ColorStateList;
import android.graphics.Typeface;
import android.os.Bundle;
import android.os.Handler;
import android.os.SystemClock;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.widget.ImageViewCompat;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.ViewPager;
import panteao.make.ready.activities.downloads.NetworkHelper;
import panteao.make.ready.activities.downloads.WifiPreferenceListener;
import panteao.make.ready.activities.purchase.TVODENUMS;
import panteao.make.ready.activities.purchase.ui.PurchaseActivity;
import panteao.make.ready.activities.purchase.ui.VodOfferType;
import panteao.make.ready.activities.series.viewmodel.SeriesViewModel;
import panteao.make.ready.activities.usermanagment.ui.LoginActivity;
import panteao.make.ready.baseModels.BaseBindingActivity;
import panteao.make.ready.beanModel.AssetHistoryContinueWatching.ItemsItem;
import panteao.make.ready.beanModel.entitle.EntitledAs;
import panteao.make.ready.beanModel.entitle.ResponseEntitle;
import panteao.make.ready.callbacks.commonCallbacks.NetworkChangeReceiver;
import panteao.make.ready.callbacks.commonCallbacks.TrailorCallBack;
import panteao.make.ready.enums.KalturaImageType;
import panteao.make.ready.fragments.dialog.PremiumDownloadPopup;
import panteao.make.ready.networking.apistatus.APIStatus;
import panteao.make.ready.networking.responsehandler.ResponseModel;
import panteao.make.ready.SDKConfig;
import panteao.make.ready.beanModel.enveuCommonRailData.RailCommonData;
import panteao.make.ready.utils.MediaTypeConstants;
import panteao.make.ready.utils.config.ImageLayer;
import panteao.make.ready.utils.constants.SharedPrefesConstants;
import panteao.make.ready.utils.helpers.CheckInternetConnection;
import panteao.make.ready.utils.helpers.ImageHelper;
import panteao.make.ready.utils.helpers.SharedPrefHelper;
import panteao.make.ready.utils.helpers.downloads.KTDownloadEvents;
import panteao.make.ready.utils.helpers.downloads.KTDownloadHelper;
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
import panteao.make.ready.fragments.player.ui.RecommendationRailFragment;
import panteao.make.ready.fragments.player.ui.SeasonTabFragment;
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
import panteao.make.ready.utils.inAppBilling.CancelCallBack;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;


import static com.google.android.material.tabs.TabLayout.INDICATOR_GRAVITY_BOTTOM;

public class SeriesDetailActivity extends BaseBindingActivity<ActivitySeriesDetailBinding> implements AlertDialogFragment.AlertDialogListener, OnDownloadClickInteraction, TrailorCallBack ,VideoListListener, KTDownloadEvents,NetworkChangeReceiver.ConnectivityReceiverListener {
    private final String TAG = this.getClass().getSimpleName();
    public String userName = "";
    public boolean isloggedout = false;
    private int seriesId, count;
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
    private SeasonTabFragment seasonTabFragment;
    private AlertDialog alertDialog;
    private AlertDialog.Builder builder;
    private RecommendationRailFragment railFragment;
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

        if (SDKConfig.getInstance().getSeriesDetailId().equalsIgnoreCase("")) {
            tabId = "10000";
        } else {
            tabId = SDKConfig.getInstance().getSeriesDetailId();
        }
        isHitPlayerApi = false;
        setupUI(getBinding().llParent);
        seriesId = getIntent().getIntExtra("seriesId", 0);
        new ToolBarHandler(this).setSeriesAction();

        onSeriesCreate();
    }

    @Override
    protected void onPause() {
        dismissLoading(getBinding().progressBar);
        if (userInteractionFragment!=null){
            userInteractionFragment.setItemFound();
        }
        super.onPause();
        try {
            if (receiver != null) {
            this.unregisterReceiver(receiver);
            if (NetworkChangeReceiver.connectivityReceiverListener != null)
                NetworkChangeReceiver.connectivityReceiverListener = null;
        }
        }catch (Exception e){

        }

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

            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    if (isPremium){
                        showPremiumPopup();
                    }
                }
            },500);

        }

    }

    private void showPremiumPopup() {
        try {
            isPremium=true;
            if (KsPreferenceKeys.getInstance().getAppLanguage().equalsIgnoreCase("Thai") || KsPreferenceKeys.getInstance().getAppLanguage().equalsIgnoreCase("हिंदी")) {
                AppCommonMethod.resetLanguage("th", SeriesDetailActivity.this);
            } else if (KsPreferenceKeys.getInstance().getAppLanguage().equalsIgnoreCase("English")) {
                AppCommonMethod.resetLanguage("en", SeriesDetailActivity.this);
            }
          //  showDialog("", getResources().getString(R.string.premium_popup_message));
        }catch (Exception ignored){

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
            if (!isHitPlayerApi) {
                getSeriesDetail();
            }
        } else {
            noConnectionLayout();
        }
    }

    private boolean isHitPlayerApi = false;
    boolean refreshEntitlement=false;
    private KTDownloadHelper downloadHelper;
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

        downloadHelper = new KTDownloadHelper(this,this);
        if (seasonTabFragment!=null){
            //seasonTabFragment.refreshAdapter()
        }

        if (preference != null && userInteractionFragment != null) {
            AppCommonMethod.callSocialAction(preference, userInteractionFragment);
        }

        if (AppCommonMethod.isPurchase) {
            AppCommonMethod.isPurchase = false;
            if (getBinding()!=null){
                getBinding().tvBuyNow.setVisibility(View.GONE);
            }
           // seriesId = AppCommonMethod.seriesId;
            isHitPlayerApi = false;
            refreshDetailPage();
        }

        if (preference!=null){
            isLogin = preference.getAppPrefLoginStatus();
        }
        if (isLogin) {
            AppCommonMethod.seriesId = seriesId;
            if (responseEntitlementModel != null && !responseEntitlementModel.getStatus()) {
                if (!refreshEntitlement){
                    AppCommonMethod.isPurchase = false;
                    refreshEntitlement=true;
                    isHitPlayerApi = false;
                    refreshDetailPage();
                }
            }
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
        SeriesDetailActivity.this.registerReceiver(receiver, filter);
        setConnectivityListener();
    }

    public void setConnectivityListener() {
        NetworkChangeReceiver.connectivityReceiverListener = listener;
    }

    @Override
    public void onNetworkConnectionChanged(boolean isConnected) {
        if (seasonTabFragment!=null){
            seasonTabFragment.notifyAdapter();
        }
    }

    private void refreshDetailPage() {
        this.seriesId=seriesId;
        Log.w("seriesID--->>",seriesId+"");
        onSeriesCreate();
    }

    private void getSeriesDetail() {
        modelCall();
        postCommentClick();
        isHitPlayerApi = true;
        RailInjectionHelper railInjectionHelper = new ViewModelProvider(this).get(RailInjectionHelper.class);
        railInjectionHelper.getSeriesDetailsV2(String.valueOf(seriesId)).observe(SeriesDetailActivity.this, new Observer<ResponseModel>() {
            @Override
            public void onChanged(ResponseModel response) {
                if (response != null) {
                    if (response.getStatus().equalsIgnoreCase(APIStatus.START.name())) {

                    } else if (response.getStatus().equalsIgnoreCase(APIStatus.SUCCESS.name())) {
                        if (response.getBaseCategory() != null) {
                            RailCommonData enveuCommonResponse = (RailCommonData) response.getBaseCategory();
                            parseSeriesData();
                        }
                    } else if (response.getStatus().equalsIgnoreCase(APIStatus.ERROR.name())) {
                        if (response.getErrorModel().getErrorCode() != 0) {
                            if (response.getErrorModel().getErrorCode() == AppConstants.RESPONSE_CODE_LOGOUT) {
                                if (isLogin) {
                                    hitApiLogout();
                                }
                                // showDialog(SeriesDetailActivity.this.getResources().getString(R.string.error), getResources().getString(R.string.logged_out));
                            } else {
                                showDialog();
                            }
                        }

                    } else if (response.getStatus().equalsIgnoreCase(APIStatus.FAILURE.name())) {
                        showDialog();
                    }

                }
            }
        });

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
            if (seriesDetailBean.isPremium()) {
                getBinding().tvPurchased.setVisibility(View.GONE);
                getBinding().mPremiumStatus.setVisibility(View.VISIBLE);
                hitApiEntitlement();
            }else {
                if (userInteractionFragment!=null){
                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            userInteractionFragment.setDownloadable();
                        }
                    },1000);

                }
            }
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

        BuyNowClick();
    }

    private void BuyNowClick() {
        getBinding().tvBuyNow.setOnClickListener(view -> comingSoon());
        getBinding().tvPurchased.setOnClickListener(view -> comingSoon());
    }

    public void comingSoon() {
            //showDialog(EpisodeActivity.this.getResources().getString(R.string.error), getResources().getString(R.string.you_are_not_entitled));
        if (getBinding().tvPurchased.getText().toString().equalsIgnoreCase(getResources().getString(R.string.subscribed)) || getBinding().tvPurchased.getText().toString().equalsIgnoreCase(getResources().getString(R.string.purchased)) ||
                getBinding().tvPurchased.getText().toString().equalsIgnoreCase(getResources().getString(R.string.rented))){

        }else {
            AppCommonMethod.seriesId = seriesId;
            if (responseEntitlementModel != null && responseEntitlementModel.getStatus()) {
                Intent intent = new Intent(SeriesDetailActivity.this, PurchaseActivity.class);
                intent.putExtra("response", seriesDetailBean);
                intent.putExtra("contentType", MediaTypeConstants.getInstance().getSeries());
                intent.putExtra("responseEntitlement", responseEntitlementModel);
                if (responseEntitlementModel!=null){
                    startActivity(intent);
                }
            }

        }

    }


    boolean isPremium = false;
    ResponseEntitle responseEntitlementModel;
    private void hitApiEntitlement() {
        viewModel.hitApiEntitlement(token, sku).observe(SeriesDetailActivity.this, responseEntitlement -> {
            responseEntitlementModel = responseEntitlement;
            if (responseEntitlement.getStatus()) {
                if (responseEntitlement.getData().getEntitled()) {
                    isPremium = false;
                    getBinding().tvBuyNow.setVisibility(View.GONE);
                    if (responseEntitlement.getData() != null) {
                        updateBuyNowText();
                    }
                } else {
                    getBinding().tvBuyNow.setVisibility(View.VISIBLE);
                    if (responseEntitlement.getData() != null) {
                        updateBuyNowText();
                    }
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            try {
                                isPremium = true;

                            } catch (Exception ignored) {

                            }
                        }
                    });
                }
                if (userInteractionFragment!=null){
                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            userInteractionFragment.setDownloadable();
                        }
                    },1000);

                }

            } else {
                if (!isLogin){
                    getBinding().tvBuyNow.setVisibility(View.VISIBLE);
                }
                if (responseEntitlementModel != null && responseEntitlementModel.getResponseCode() != null && responseEntitlementModel.getResponseCode() > 0 && responseEntitlementModel.getResponseCode() == 4302) {
                    isloggedout = true;
                    logoutUser();
                }

                if (userInteractionFragment!=null){
                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            userInteractionFragment.setDownloadable();
                        }
                    },1000);

                }

            }
        });

    }

    public void logoutUser() {
        isloggedout = false;
        if (isLogin) {
            if (CheckInternetConnection.isOnline(SeriesDetailActivity.this)) {
                clearCredientials(preference);
                hitApiLogout(preference.getAppPrefAccessToken());
            }
        }
    }

    String typeofTVOD="";
    private void updateBuyNowText() {
        try {
            typeofTVOD="";
            String subscriptionOfferPeriod = null;
            if (type == 1) {
                if (responseEntitlement.getData().getEntitledAs() != null) {
                    List<EntitledAs> alpurchaseas = responseEntitlement.getData().getEntitledAs();
                    for (int i = 0 ; i<alpurchaseas.size();i++){
                        String vodOfferType = alpurchaseas.get(i).getVoDOfferType();
                        if (alpurchaseas.get(i).getOfferType() != null) {
                            subscriptionOfferPeriod = alpurchaseas.get(i).getOfferType();
                        }
                        if (vodOfferType!=null){
                            if (vodOfferType.contains(VodOfferType.PERPETUAL.name())) {

                            } else if (vodOfferType.contains(VodOfferType.RENTAL.name())) {
                                if (alpurchaseas.get(i).getIdentifier().contains(TVODENUMS.___sd.name())){
                                    typeofTVOD=TVODENUMS.___sd.name();
                                }else if (alpurchaseas.get(i).getIdentifier().contains(TVODENUMS.___hd.name())){
                                    typeofTVOD=TVODENUMS.___hd.name();
                                }else if (alpurchaseas.get(i).getIdentifier().contains(TVODENUMS.___uhd.name())){
                                    typeofTVOD=TVODENUMS.___uhd.name();
                                }

                            } else {

                            }
                        }

                    }
                    String vodOfferType = alpurchaseas.get(0).getVoDOfferType();



                    if (vodOfferType != null) {
                        if (vodOfferType.contains(VodOfferType.PERPETUAL.name())) {
                            getBinding().tvPurchased.setVisibility(View.VISIBLE);
                            getBinding().tvPurchased.setText("" + getResources().getString(R.string.purchased));
                        } else if (vodOfferType.contains(VodOfferType.RENTAL.name())) {
                            getBinding().tvPurchased.setVisibility(View.VISIBLE);
                            getBinding().tvPurchased.setText("" + getResources().getString(R.string.rented));
                        } else {

                        }
                    }

                    if (subscriptionOfferPeriod != null) {
                        getBinding().tvPurchased.setVisibility(View.VISIBLE);
                        getBinding().tvPurchased.setText("" + getResources().getString(R.string.subscribed));
                        typeofTVOD="";
                    } else {

                    }
                    if (responseEntitlement.getData().getBrightcoveVideoId() != null) {

                        preference.setEntitlementState(true);
                    }
                    if(userInteractionFragment!=null) {
                        userInteractionFragment.setDownloadable();
                       // initDownload(Entryid);
                    }

                }
            } else {

            }

        } catch (Exception e) {

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
       // userInteractionFragment.setDownloadable(false);
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
            if (downloadHelper!=null){
                seasonTabFragment.setDownloadHelper();
            }


            getSupportFragmentManager().beginTransaction().detach(seasonTabFragment).attach(seasonTabFragment).commit();
            getSupportFragmentManager().beginTransaction().detach(railFragment).attach(railFragment).commit();

            TabLayout.Tab tab = getBinding().tabLayout.getTabAt(0);
            tab.select();
        } else {
            if (episodeTabAdapter == null) {
                 railFragment = new RecommendationRailFragment();
            seasonTabFragment = new SeasonTabFragment();
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
                if (downloadHelper!=null){
                    seasonTabFragment.setDownloadHelper();
                }

            episodeTabAdapter.addFragment(seasonTabFragment, getString(R.string.tab_heading_episodes));
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
            }else {
                Bundle args = new Bundle();
                args.putString(AppConstants.BUNDLE_TAB_ID, tabId);
                railFragment.getVideoRails();

                Bundle bundleSeason = new Bundle();
                bundleSeason.putInt(AppConstants.BUNDLE_ASSET_ID, seriesId);
                bundleSeason.putParcelableArrayList(AppConstants.BUNDLE_SEASON_ARRAY, seriesDetailBean.getSeasons());
                bundleSeason.putInt(AppConstants.BUNDLE_SEASON_COUNT, seriesDetailBean.getSeasonCount());
                seasonTabFragment.getVideoRails();
            }

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
                ImageHelper.getInstance(SeriesDetailActivity.this).loadListImage(getBinding().sliderImage, ImageLayer.getInstance().getFilteredImage(seriesResponse.getImages(),  KalturaImageType.LANDSCAPE, 800, 450));
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
                showLoading(getBinding().progressBar);
                if (SystemClock.elapsedRealtime() - mLastClickTime < 900) {
                    return;
                }
                mLastClickTime = SystemClock.elapsedRealtime();

                openShareDialogue();
            });
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

        viewModel.hitApiIsWatchList(token, requestParam).observe(SeriesDetailActivity.this, responseContentInWatchlist -> {
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
        viewModel.hitApiAddLike(token, requestParam).observe(SeriesDetailActivity.this, responseContentInWatchlist -> {
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
        viewModel.hitApiIsLike(token, requestParam).observe(SeriesDetailActivity.this, responseContentInWatchlist -> {
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
            getBinding().interactionSection.tvLike.setTextColor(ContextCompat.getColor(SeriesDetailActivity.this, R.color.dialog_green_color));
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
            getBinding().interactionSection.tvLike.setTextColor(ContextCompat.getColor(SeriesDetailActivity.this, R.color.white));

            ImageViewCompat.setImageTintList(getBinding().interactionSection.likeIcon, ColorStateList.valueOf(getResources().getColor(R.color.white)));
        } else {
            // do something for phones running an SDK before lollipop
            getBinding().interactionSection.tvLike.setTextColor(getResources().getColor(R.color.white));
            ImageViewCompat.setImageTintList(getBinding().interactionSection.likeIcon, ColorStateList.valueOf(getResources().getColor(R.color.white)));
        }

    }

    private void openShareDialogue() {
        //   String imgUrl = AppCommonMethod.urlPoints + AppConstants.SERIES_IMAGES_BASE_KEY + seriesDetailBean..getPicture();
        String imgUrl = seriesDetailBean.getPosterURL();
        int id = seriesDetailBean.getId();
        String title = seriesDetailBean.getTitle();
        Logger.e("openShareDialogue", new Gson().toJson(seriesDetailBean));
        AppCommonMethod.openShareDialog(SeriesDetailActivity.this, title, id, MediaTypeConstants.getInstance().getSeries(), imgUrl, seriesDetailBean.getSeason());
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
        viewModel.hitApiRemoveWatchList(token, String.valueOf(watchListId)).observe(SeriesDetailActivity.this, responseWatchList -> {
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
        viewModel.hitApiAddWatchList(token, requestParam).observe(SeriesDetailActivity.this, responseWatchList -> {
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
            getBinding().interactionSection.tvWatch.setTextColor(ContextCompat.getColor(SeriesDetailActivity.this, R.color.dialog_green_color));
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
            getBinding().interactionSection.tvWatch.setTextColor(ContextCompat.getColor(SeriesDetailActivity.this, R.color.white));
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
        viewModel.hitApiUnLike(token, requestParam).observe(SeriesDetailActivity.this, responseContentInWatchlist -> {
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
        new ActivityLauncher(SeriesDetailActivity.this).loginActivity(SeriesDetailActivity.this, LoginActivity.class);

    }


    @Override
    public void onFinishDialog() {
        if (isPremium){
            isPremium=false;

            return;
        }
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
        new ActivityLauncher(SeriesDetailActivity.this).loginActivity(SeriesDetailActivity.this, LoginActivity.class);

    }

    public void showSeasonList() {
        getBinding().transparentLayout.setVisibility(View.VISIBLE);

        SeasonListAdapter listAdapter = new SeasonListAdapter(list, selectedSeasonId);
        builder = new AlertDialog.Builder(SeriesDetailActivity.this);
        LayoutInflater inflater = LayoutInflater.from(SeriesDetailActivity.this);
        View content = inflater.inflate(R.layout.season_custom_dialog, null);
        builder.setView(content);
        RecyclerView mRecyclerView = content.findViewById(R.id.my_recycler_view);
        ImageView imageView = content.findViewById(R.id.close);
        imageView.setOnClickListener(v -> {
            alertDialog.cancel();
            getBinding().transparentLayout.setVisibility(View.GONE);
        });

        //Creating Adapter to fill data in Dialog
        mRecyclerView.setLayoutManager(new LinearLayoutManager(SeriesDetailActivity.this));
        mRecyclerView.setAdapter(listAdapter);
        alertDialog = builder.create();
        alertDialog.getWindow().setBackgroundDrawable(ActivityCompat.getDrawable(SeriesDetailActivity.this, R.color.transparent_series));
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

    Object clickSource=null;
    String downloadId="";
    @Override
    public void onDownloadClicked(String videoId, Object position, Object source) {
        if (getBinding().tvBuyNow.getVisibility()==View.VISIBLE){
            showDownloadPopup();
            return;
        }
        clickSource=source;
        downloadId=videoId;
        Log.w("instanceof",clickSource+"");
        if (source instanceof UserInteractionFragment) {
            boolean loginStatus = preference.getAppPrefLoginStatus();
            if (!loginStatus)
                new ActivityLauncher(this).loginActivity(this, LoginActivity.class);
            else {
                int videoQuality = new SharedPrefHelper(this).getInt(SharedPrefesConstants.DOWNLOAD_QUALITY_INDEX, 3);
                if (KsPreferenceKeys.getInstance().getDownloadOverWifi() == 1) {
                    if (NetworkHelper.INSTANCE.isWifiEnabled(this)) {
                        if (videoQuality != 3) {
                            //userInteractionFragment.setDownloadStatus(panteao.make.ready.enums.DownloadStatus.REQUESTED);
                            if (seasonEpisodesList!=null){
                                String[] array = getResources().getStringArray(R.array.download_quality);
                                userInteractionFragment.setDownloadStatus();
                                int pos =  new SharedPrefHelper(SeriesDetailActivity.this).getInt(SharedPrefesConstants.DOWNLOAD_QUALITY_INDEX, 3);
                                startDownload();

                            }
                        } else {
                            selectDownloadVideoQuality();
                        }
                    } else {
                        showWifiSettings();
//                        downloadHelper.checkDownloadStatus(downloadAbleVideo);
                        //Toast.makeText(this, "NoWifi", Toast.LENGTH_LONG).show();
                    }
                } else {
                    if (videoQuality != 3) {
                        userInteractionFragment.setDownloadStatus();
                        if (seasonEpisodesList!=null){
                            String[] array = getResources().getStringArray(R.array.download_quality);
                            userInteractionFragment.setDownloadStatus();
                            int pos =  new SharedPrefHelper(SeriesDetailActivity.this).getInt(SharedPrefesConstants.DOWNLOAD_QUALITY_INDEX, 3);
                            startDownload();
                        }
                    } else {
                        selectDownloadVideoQuality();
                    }
                }
            }
        }else {
            boolean loginStatus = preference.getAppPrefLoginStatus();
            if (!loginStatus)
                new ActivityLauncher(this).loginActivity(this, LoginActivity.class);
            else {
                int videoQuality = new SharedPrefHelper(this).getInt(SharedPrefesConstants.DOWNLOAD_QUALITY_INDEX, 3);
                if (KsPreferenceKeys.getInstance().getDownloadOverWifi() == 1) {
                    if (NetworkHelper.INSTANCE.isWifiEnabled(this)) {
                        if (videoQuality != 3) {
                            userInteractionFragment.setDownloadStatus();
                            if (seasonEpisodesList!=null){
                                String[] array = getResources().getStringArray(R.array.download_quality);
                                userInteractionFragment.setDownloadStatus();
                                int pos =  new SharedPrefHelper(SeriesDetailActivity.this).getInt(SharedPrefesConstants.DOWNLOAD_QUALITY_INDEX, 3);
                                startDownload();

                            }
                        } else {
                            selectDownloadVideoQuality();
                        }
                    } else {
                        showWifiSettings();
//                        downloadHelper.checkDownloadStatus(downloadAbleVideo);
                        //Toast.makeText(this, "NoWifi", Toast.LENGTH_LONG).show();
                    }
                } else {
                    if (videoQuality != 3) {
                        userInteractionFragment.setDownloadStatus();
                        if (seasonEpisodesList!=null){
                            String[] array = getResources().getStringArray(R.array.download_quality);
                            userInteractionFragment.setDownloadStatus();
                            int pos =  new SharedPrefHelper(SeriesDetailActivity.this).getInt(SharedPrefesConstants.DOWNLOAD_QUALITY_INDEX, 3);
                            startDownload();
                        }
                    } else {
                        selectDownloadVideoQuality();
                    }
                }
            }
        }

    }

    private void showDownloadPopup() {
        try {
            if (KsPreferenceKeys.getInstance().getAppLanguage().equalsIgnoreCase("Thai") || KsPreferenceKeys.getInstance().getAppLanguage().equalsIgnoreCase("हिंदी")) {
                AppCommonMethod.resetLanguage("th", SeriesDetailActivity.this);
            } else if (KsPreferenceKeys.getInstance().getAppLanguage().equalsIgnoreCase("English")) {
                AppCommonMethod.resetLanguage("en", SeriesDetailActivity.this);
            }
            if (KsPreferenceKeys.getInstance().getAppPrefLoginStatus()){
                showDownloadDialog();
            }else {
                showDownloadDialog();
            }


        }catch (Exception ignored){

        }
    }

    private void showDownloadDialog() {
        FragmentManager fm = getSupportFragmentManager();
        PremiumDownloadPopup alertDialog = PremiumDownloadPopup.newInstance(title, message, buttonText);
        alertDialog.setCancelable(false);
        alertDialog.setAlertDialogCallBack(new PremiumDownloadPopup.AlertDialogListener() {
            @Override
            public void onFinishDialog() {
                if (type==1){
                    comingSoon();
                }else {
                    new ActivityLauncher(SeriesDetailActivity.this).loginActivity(SeriesDetailActivity.this, LoginActivity.class);
                }

            }
        });
        alertDialog.show(fm, "fragment_alert");
    }

    private void startDownload() {
        Log.w("instanceof",clickSource+"");
        if (clickSource!=null){
            if (clickSource instanceof SeriesDetailActivity){
                Log.w("instanceof",clickSource+" "+1);
                if (seasonTabFragment!=null && seasonTabFragment.getSeasonAdapter()!=null){
                    if (seasonTabFragment.getSeasonAdapter().getAdapterList()!=null && seasonTabFragment.getSeasonAdapter().getAdapterList().size()>0){
                        Log.w("instanceof",clickSource+" "+2);
                        int position=AppCommonMethod.getDownloadPosition(seasonTabFragment.getSeasonAdapter().getAdapterList(),videoId);
                        EnveuVideoItemBean videoDetails=seasonTabFragment.getSeasonAdapter().getAdapterList().get(position);
                        seasonTabFragment.notifySingleItem();
                        countCall = 0;
                        countCall2 = 2;
                        if (userInteractionFragment!=null){
                            userInteractionFragment.setItemFound();
                        }
                        downloadHelper.startDownload(pos,videoDetails.getkEntryId(),videoDetails.getTitle(),videoDetails.getAssetType(),videoDetails.getSeriesId(),videoDetails.getName(),videoDetails.getPosterURL(),String.valueOf(videoDetails.getEpisodeNo()),seasonTabFragment.getSelectedSeason(),videoDetails.getSeriesImageURL());
                    }

                }else {

                }

            }else {
                countCall = 0;
                countCall2 = 2;
                if (userInteractionFragment!=null){
                    userInteractionFragment.setItemFound();
                }
                downloadHelper.startSeriesDownload(pos,seasonTabFragment.getSelectedSeason(),seasonEpisodesList, new CancelCallBack() {
                    @Override
                    public void startDownload() {

                    }
                });
                if (seasonTabFragment!=null){
                    seasonTabFragment.setProgressStatus();
                }
            }
        }else {
            countCall = 0;
            countCall2 = 2;
            if (userInteractionFragment!=null){
                userInteractionFragment.setItemFound();
            }
            downloadHelper.startSeriesDownload(pos, seasonTabFragment.getSelectedSeason(), seasonEpisodesList, new CancelCallBack() {
                @Override
                public void startDownload() {

                }
            });
            if (seasonTabFragment!=null){
                seasonTabFragment.setProgressStatus();
            }
        }
    }

    private void showWifiSettings() {
        downloadHelper.changeWifiSetting(new WifiPreferenceListener() {
            @Override
            public void actionP(int value) {
                if (value == 0) {
                    if (videoQuality != 3) {
                        if (seasonEpisodesList!=null){
                            String[] array = getResources().getStringArray(R.array.download_quality);
                            userInteractionFragment.setDownloadStatus();
                            int pos =  new SharedPrefHelper(SeriesDetailActivity.this).getInt(SharedPrefesConstants.DOWNLOAD_QUALITY_INDEX, 3);
                            startDownload();
                        }

                    } else {
                        selectDownloadVideoQuality();
                    }
                }
            }
        });
    }

    private void selectDownloadVideoQuality() {
        try {
            downloadHelper.selectVideoQuality(typeofTVOD,position -> {
                if (seasonEpisodesList!=null){
                    String[] array = getResources().getStringArray(R.array.download_quality);
                    userInteractionFragment.setDownloadStatus();
                    int pos =  new SharedPrefHelper(SeriesDetailActivity.this).getInt(SharedPrefesConstants.DOWNLOAD_QUALITY_INDEX, 3);
                    startDownload();
                }
            });
        }catch (Exception ignored){

        }

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
                        Log.w("cancelVideo","3");
                        downloadHelper.cancelVideo(videoId);
                        break;
                    case R.id.pause_download:
                        Log.w("cancelVideo","4");
                        downloadHelper.pauseVideo(videoId);
                        break;
                }
                return false;
            });
        } else {
            AppCommonMethod.showPopupMenu(this, view, R.menu.download_menu, item -> {
                switch (item.getItemId()) {
                    case R.id.cancel_download:
                        Log.w("cancelVideo","1");
                        downloadHelper.cancelVideo(videoId);
                        new Handler().postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                if (seasonTabFragment!=null){
                                    seasonTabFragment.cancelDownload();
                                    if (userInteractionFragment!=null && seasonTabFragment!=null && downloadHelper!=null){
                                        userInteractionFragment.isItemCheck();
                                        userInteractionFragment.checkSeriesDownloadStatus();
                                    }
                                }
                            }
                        },500);

                       // if (videoId.equals(String.valueOf(seriesId)))
                          //  userInteractionFragment.setDownloadStatus(panteao.make.ready.enums.DownloadStatus.START);
                        break;
                    case R.id.pause_download:
                        Log.w("cancelVideo","2");
                        downloadHelper.pauseVideo(videoId);
                       // if (videoId.equals(String.valueOf(seriesId)))
                         //   userInteractionFragment.setDownloadStatus(panteao.make.ready.enums.DownloadStatus.PAUSE);
                        break;
                }
                return false;
            });
        }
    }

    @Override
    public void onDownloadCompleteClicked(View view, Object source, String videoId) {
        AppCommonMethod.showPopupMenu(this, view, R.menu.remove_downloads, item -> {
            switch (item.getItemId()) {
                case R.id.delete_download:
                    if (seasonTabFragment!=null){
                        if (seasonTabFragment.getSeasonAdapter()!=null && seasonTabFragment.getSeasonAdapter().getAdapterList()!=null && seasonTabFragment.getSeasonAdapter().getAdapterList().size()>0){
                            if (userInteractionFragment!=null){
                                userInteractionFragment.setDownloadStatus();
                                userInteractionFragment.getBinding().downloadText.setText(getResources().getString(R.string.removing_download));
                                userInteractionFragment.getBinding().downloadText.setTextColor(getResources().getColor(R.color.subtitlecolor));
                            }
                            downloadHelper.removeAllVideo(seasonTabFragment.getSeasonAdapter().getAdapterList(), new CancelCallBack() {
                                @Override
                                public void cancelVideos() {
                                    userInteractionFragment.setDownloadStatus();
                                    if (seasonTabFragment!=null){
                                        seasonTabFragment.notifyAdapter();
                                        userInteractionFragment.setDownloadStatus();
                                    }
                                }
                            });
                        }
                    }

                    break;
            }
            return false;
        });

    }

    @Override
    public void onPauseClicked(String videoId, Object source) {
        if (source instanceof UserInteractionFragment) {
            downloadHelper.resumeDownload(videoId);
        } else {
            downloadHelper.resumeDownload(videoId);
        }
    }

    @Override
    public void fromAdapterPaused(@NonNull String assetID) {

    }

    @Override
    public void onSeriesDownloadClicked(@NotNull View view, @NotNull Object source, @NotNull String videoId) {
            if (seasonTabFragment!=null){
               // int status=seasonTabFragment.checkProgressStatus(downloadHelper);
                AppCommonMethod.showPopupMenu(this, view, R.menu.series_cancel_downloads, item -> {
                switch (item.getItemId()) {
                    case R.id.delete_download:
                        if (seasonTabFragment!=null){
                            if (seasonTabFragment.getSeasonAdapter()!=null && seasonTabFragment.getSeasonAdapter().getAdapterList()!=null && seasonTabFragment.getSeasonAdapter().getAdapterList().size()>0){
                                if (userInteractionFragment!=null){
                                    userInteractionFragment.setDownloadStatus();
                                    userInteractionFragment.getBinding().downloadText.setText(getResources().getString(R.string.canceling_download));
                                }
                                downloadHelper.cancelAllVideo(seasonTabFragment.getSeasonAdapter().getAdapterList(), new CancelCallBack() {
                                    @Override
                                    public void cancelVideos() {
                                        userInteractionFragment.setDownloadStatus();
                                        if (seasonTabFragment!=null){
                                            seasonTabFragment.notifyAdapter();
                                            userInteractionFragment.setDownloadStatus();
                                        }
                                    }
                                });
                            }
                        }

                        break;
                }
                return false;
            });

            }


    }


    class SeasonListAdapter extends RecyclerView.Adapter<SeasonListAdapter.ViewHolder> {
        private final ArrayList<SelectedSeasonModel> list;
        private final int selectedPos;

        //TrackGroup list;
        public SeasonListAdapter(ArrayList<SelectedSeasonModel> list, int selectedPos) {
            this.list = list;
            this.selectedPos = selectedPos;
        }

        @NonNull
        @Override
        public SeasonListAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.all_season_listing, parent, false);

            return new ViewHolder(view);
        }

        @Override
        public void onBindViewHolder(@NonNull final ViewHolder holder, final int position) {
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

    @Override
    public void onDownloadDeleted(@NotNull String videoId, @NotNull Object source) {
         try {
              Log.w("cancelVideo","-->onDownloadDeleted");
              if (videoId!=null && !videoId.equalsIgnoreCase("")){
                   downloadHelper.cancelVideo(videoId);
                   userInteractionFragment.setItemFound();
                        new Handler().postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                if (seasonTabFragment!=null){
                                    seasonTabFragment.cancelDownload();
                                    if (userInteractionFragment!=null && userInteractionFragment.getBinding()!=null){
                                        if (seasonTabFragment.getSeasonAdapter()!=null && seasonTabFragment.getSeasonAdapter().getAdapterList()!=null){
                                            if (seasonTabFragment.getSeasonAdapter().getAdapterList().size()>0){
                                                userInteractionFragment.checkSeriesDownloadStatus();
                                            }
                                        }
                                    }

                                }
                            }
                        },500);
              }
         }catch (Exception ignored){

         }
    }

    @Override
    public void onClick() {

    }

    @Override
    public void onPointerCaptureChanged(boolean hasCapture) {

    }

    @Override
    public void setDownloadProgressListener(float progress, String assetId) {


    }

    @Override
    public void onDownloadPaused(@NonNull @NotNull String assetId) {
        if (seasonTabFragment!=null){
            if (downloadHelper!=null){
              // seasonTabFragment.pauseDownload(downloadHelper,assetId);
            }
        }
    }

    @Override
    public void initialStatus() {

    }

    @Override
    public void onStateChanged(@NonNull @NotNull OfflineManager.AssetDownloadState state,String assetId) {
        if (seasonTabFragment!=null){
            if (downloadHelper!=null){
               // seasonTabFragment.pauseDownload(downloadHelper,assetId);
            }
        }
    }

    @Override
    public void onAssetDownloadComplete(@NonNull @NotNull String assetId) {
         if (seasonTabFragment!=null){
             runOnUiThread(new Runnable() {
                 @Override
                 public void run() {
                     if (userInteractionFragment!=null){
                         try {
                             userInteractionFragment.isItemCheck();
                             userInteractionFragment.checkSeriesDownloadStatus();
                         }catch (Exception e){

                         }
                     }
                   //  seasonTabFragment.downloadComplete(assetId);
                 }
             });

         }
    }

    @Override
    public void onAssetDownloadFailed() {

    }
    List<EnveuVideoItemBean> seasonEpisodesList;
    public void episodesList() {
        try {
            seasonEpisodesList = new ArrayList<>();
            if (seasonEpisodes != null && seasonEpisodes.size() > 0) {
                for (int i = 0; i < seasonEpisodes.size(); i++) {
                    seasonEpisodesList.add(seasonEpisodes.get(i));
                }
                if (userInteractionFragment!=null && seasonTabFragment!=null && downloadHelper!=null){
                    userInteractionFragment.isItemCheck();
                    userInteractionFragment.checkSeriesDownloadStatus();
                }

            }

        } catch (Exception ignored) {

        }

    }

    int countCall = 0;
    int countCall2 = 2;
    @Override
    public void fromAdapterDownloadProgress(float progress, @NonNull String assetId) {
        Log.w("countValues-->>",countCall+"  "+countCall2);
        if (countCall==0 && countCall2==2){
            if (seasonTabFragment!=null){
                countCall=1;
                seasonTabFragment.downloadStatusChanged();
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        countCall2=2;
                        countCall=2;
                    }
                },500);
            }
            if (userInteractionFragment!=null){
                Log.w("userInteraction","in1");
                //userInteractionFragment.setDownloadStatus(DownloadStatus.SERIES_DOWNLOADING);
               // userInteractionFragment.setDownloadProgress((int)progress);
            }
        }else {
            if (countCall==2 && countCall2==2){
                if (seasonTabFragment!=null){
                    if (userInteractionFragment!=null && userInteractionFragment.getBinding()!=null){
                        if (seasonTabFragment.getSeasonAdapter()!=null && seasonTabFragment.getSeasonAdapter().getAdapterList()!=null){
                            if (seasonTabFragment.getSeasonAdapter().getAdapterList().size()>0){
                                userInteractionFragment.checkDownloadStatus();
                            }
                        }
                    }
                   // userInteractionFragment.setDownloadStatus(DownloadStatus.SERIES_DOWNLOADING);
                    try {
                       // Thread.sleep(60);
                        seasonTabFragment.updateAdapter();
                    }catch (Exception e){

                    }

                }
            }
        }
    }

    @Override
    public void fromAdapterStatusChanged(@NonNull OfflineManager.AssetDownloadState state, @NonNull String assetID) {
        Log.w("adapterStaus",state+" 2 "+assetID);
        if (seasonTabFragment!=null){
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    if (userInteractionFragment!=null){
                        try {
                            userInteractionFragment.isItemCheck();
                            userInteractionFragment.checkSeriesDownloadStatus();
                        }catch (Exception e){

                        }
                    }
                    //  seasonTabFragment.downloadComplete(assetId);
                }
            });

        }


    }

    @Override
    public void fromAdapterStatus(@NonNull OfflineManager.AssetDownloadState state, @NonNull String assetID) {
        Log.w("adapterStaus",state+" 1 "+assetID);
        if (userInteractionFragment!=null){
            if (userInteractionFragment!=null && userInteractionFragment.getBinding()!=null){
                if (userInteractionFragment.getBinding().seriesDownloaded.getVisibility()==View.GONE){
                  //  userInteractionFragment.getBinding().seriesDownloaded.setVisibility(View.VISIBLE);
                   // userInteractionFragment.setDownloadStatus(DownloadStatus.SERIES_DOWNLOADING);
                }
            }
           /* OfflineManager.AssetInfo info=downloadHelper.getManager().getAssetInfo(assetID);
            if (info!=null){
                userInteractionFragment.setDownloadStatus(AppCommonMethod.getDownloadStatus(info.getState()));
            }else {
                userInteractionFragment.setDownloadStatus(AppCommonMethod.getDownloadStatus(null));
            }*/

            try {
                if (userInteractionFragment!=null && seasonTabFragment!=null && downloadHelper!=null){
                    userInteractionFragment.isItemCheck();
                    userInteractionFragment.checkSeriesDownloadStatus();
                }
            }catch (Exception e){

            }


        }
    }
}
