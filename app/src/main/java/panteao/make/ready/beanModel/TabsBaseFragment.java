package panteao.make.ready.beanModel;

import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.make.enums.LandingPageType;
import com.make.enums.Layouts;
import com.make.enums.ListingLayoutType;
import com.make.enums.PDFTarget;
import panteao.make.ready.activities.listing.listui.ListActivity;
import panteao.make.ready.activities.listing.ui.GridActivity;
import panteao.make.ready.activities.privacypolicy.ui.WebViewActivity;
import panteao.make.ready.activities.usermanagment.ui.LoginActivity;
import panteao.make.ready.activities.watchList.ui.WatchListActivity;
import panteao.make.ready.baseModels.BaseBindingFragment;
import panteao.make.ready.baseModels.HomeBaseViewModel;
import panteao.make.ready.beanModel.enveuCommonRailData.RailCommonData;
import panteao.make.ready.callbacks.commonCallbacks.CommonApiCallBack;
import panteao.make.ready.callbacks.commonCallbacks.CommonRailtItemClickListner;
import panteao.make.ready.callbacks.commonCallbacks.MoreClickListner;
import panteao.make.ready.fragments.home.viewModel.HomeFragmentViewModel;
import panteao.make.ready.fragments.movies.viewModel.MovieFragmentViewModel;
import panteao.make.ready.fragments.news.viewModel.NewsFragmentViewModel;
import panteao.make.ready.fragments.shows.viewModel.ShowsFragmentViewModel;
import panteao.make.ready.SDKConfig;
import panteao.make.ready.activities.search.ui.ActivitySearch;
import panteao.make.ready.adapters.commonRails.CommonAdapter;
import panteao.make.ready.adapters.commonRails.CommonAdapterNew;
import panteao.make.ready.adapters.shimmer.ShimmerAdapter;
import panteao.make.ready.databinding.FragmentHomeBinding;
import panteao.make.ready.utils.MediaTypeConstants;
import panteao.make.ready.utils.commonMethods.AppCommonMethod;
import panteao.make.ready.utils.constants.AppConstants;
import panteao.make.ready.utils.cropImage.helpers.Logger;
import panteao.make.ready.utils.cropImage.helpers.ShimmerDataModel;
import panteao.make.ready.utils.helpers.NetworkConnectivity;
import panteao.make.ready.utils.helpers.RailInjectionHelper;
import panteao.make.ready.utils.helpers.RecyclerAnimator;

import panteao.make.ready.utils.helpers.intentlaunchers.ActivityLauncher;
import com.google.gson.Gson;
import panteao.make.ready.utils.helpers.ksPreferenceKeys.KsPreferenceKeys;

import java.util.ArrayList;
import java.util.List;


public class TabsBaseFragment<T extends HomeBaseViewModel> extends BaseBindingFragment<FragmentHomeBinding> implements CommonRailtItemClickListner, MoreClickListner {

    String playListId;
    private T viewModel;
    private boolean mIsLoading = true, isScrolling = false;
    private int counter = 0;
    private int swipeToRefresh = 0;
    private int count = 0;
    private int counterValueApiFail = 0;
    private CommonAdapter adapter;
    private int mScrollY;
    private OnFragmentInteractionListener mListener;
    private boolean crouseInjected = false;
    private KsPreferenceKeys preference;
    private String tabId;
    private boolean isLogin;
    private List<RailCommonData> railCommonDataList = new ArrayList<>();
    private CommonAdapterNew adapterNew;
    private long mLastClickTime = 0;

    protected void setViewModel(Class<? extends HomeBaseViewModel> viewModelClass) {
       viewModel = (T) new ViewModelProvider(this).get(viewModelClass);
    }

    @Override
    public FragmentHomeBinding inflateBindingLayout(@NonNull LayoutInflater inflater) {
        return FragmentHomeBinding.inflate(inflater);
    }

    private void modelCall() {
        callShimmer();
        if (getActivity() != null) {
            preference = KsPreferenceKeys.getInstance();
        }
        setTabId();
        connectionObserver();
    }

    private void setTabId() {

        try {
            //shows=Premium
            //Movie=instructor
            if (AppCommonMethod.getConfigResponse()!=null && AppCommonMethod.getConfigResponse().getData()!=null
                    && AppCommonMethod.getConfigResponse().getData().getAppConfig()!=null
                    && AppCommonMethod.getConfigResponse().getData().getAppConfig().getNavScreens()!=null){
                if (viewModel instanceof HomeFragmentViewModel) {
                    tabId = SDKConfig.getInstance().getFirstTabId();
                }
                else if (viewModel instanceof NewsFragmentViewModel) {
                    tabId = SDKConfig.getInstance().getSecondTabId();
                } else if (viewModel instanceof ShowsFragmentViewModel) {
                    tabId = SDKConfig.getInstance().getSecondTabId();
                } else if (viewModel instanceof MovieFragmentViewModel) {
                    tabId = SDKConfig.getInstance().getThirdTabId();
                } else {
                    tabId = SDKConfig.getInstance().getFirstTabId();
                }

            }else {
                if (viewModel instanceof HomeFragmentViewModel){
                    tabId = AppConstants.HOME_ENVEU;
                }
                else if (viewModel instanceof NewsFragmentViewModel) {
                    tabId = AppConstants.ORIGINAL_ENVEU;
                } else if (viewModel instanceof ShowsFragmentViewModel) {
                    tabId = AppConstants.PREMIUM_ENVEU;
                } else if (viewModel instanceof MovieFragmentViewModel) {
                    tabId = AppConstants.SINETRON_ENVEU;
                } else {
                    tabId = AppConstants.HOME_ENVEU;
                }
            }
        }catch (Exception ignored){
            if (viewModel instanceof HomeFragmentViewModel){
                tabId = AppConstants.HOME_ENVEU;
            } else if (viewModel instanceof NewsFragmentViewModel) {
                tabId = AppConstants.ORIGINAL_ENVEU;
            } else if (viewModel instanceof ShowsFragmentViewModel) {
                tabId = AppConstants.PREMIUM_ENVEU;
            } else if (viewModel instanceof MovieFragmentViewModel) {
                tabId = AppConstants.SINETRON_ENVEU;
            } else {
                tabId = AppConstants.HOME_ENVEU;
            }
        }

    }

    private void connectionObserver() {
        if (getActivity() != null
                && NetworkConnectivity.isOnline(getActivity())) {
            getBinding().noConnectionLayout.setVisibility(View.GONE);
            adapterNew = null;
            connectionValidation(true);
        } else {
            connectionValidation(false);
        }
    }

    private void connectionValidation(Boolean aBoolean) {
        if (aBoolean) {
            getBinding().swipeContainer.setRefreshing(true);
            UIinitialization();
        } else {
            noConnectionLayout();
        }
    }

    private void UIinitialization() {
        //  dummyData();
        swipeToRefresh();
        callShimmer();
        getBinding().noResultFound.retryAgain.setOnClickListener(view -> {
            getBinding().noResultLayout.setVisibility(View.GONE);
            getBinding().noConnectionLayout.setVisibility(View.GONE);
            getBinding().myRecyclerView.setVisibility(View.VISIBLE);
            swipeToRefresh = 2;
            counter = 0;
            counterValueApiFail = 0;
            connectionObserver();
        });
        getBinding().myRecyclerView.setHasFixedSize(true);
        getBinding().myRecyclerView.setItemViewCacheSize(20);
        getBinding().myRecyclerView.setNestedScrollingEnabled(false);
        getBinding().myRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity(), RecyclerView.VERTICAL, false));
        getBinding().swipeContainer.setRefreshing(true);
        getBinding().swipeContainer.setVisibility(View.VISIBLE);
        getBinding().noResultLayout.setVisibility(View.GONE);
        getBinding().noConnectionLayout.setVisibility(View.GONE);

        getBinding().swipeContainer.setOnRefreshListener(() -> {
            if (NetworkConnectivity.isOnline(getActivity()) && swipeToRefresh == 1) {
                swipeToRefresh = 2;
                getBinding().swipeContainer.setRefreshing(true);
                getBinding().swipeContainer.setVisibility(View.VISIBLE);
                getBinding().noResultLayout.setVisibility(View.GONE);
                getBaseCategories();
            } else {
                swipeToRefreshCheck();
            }
        });


        try {
           // PerformanceTracking performanceTracking=new PerformanceTracking();

        }catch (Exception e){
            Logger.w("streamValues  --",e.toString()+"");
        }
        getBaseCategories();

    }

    private void getBaseCategories() {
        railCommonDataList = new ArrayList<>();
        adapterNew = null;
        RailInjectionHelper railInjectionHelper = new ViewModelProvider(this).get(RailInjectionHelper.class);
        railInjectionHelper.getScreenWidgets(getActivity(), tabId, new CommonApiCallBack() {
            @Override
            public void onSuccess(Object item) {
                if (item instanceof RailCommonData) {
                    RailCommonData railCommonData = (RailCommonData) item;
                    railCommonDataList.add(railCommonData);
                    getBinding().swipeContainer.setRefreshing(false);
                    if (adapterNew == null) {
                        new RecyclerAnimator(getActivity()).animate(getBinding().myRecyclerView);
                        adapterNew = new CommonAdapterNew(getContext(), railCommonDataList, TabsBaseFragment.this::railItemClick, TabsBaseFragment.this::moreRailClick);
                        getBinding().myRecyclerView.setAdapter(adapterNew);
                    } else {
                        synchronized (railCommonDataList) {
                            adapterNew.notifyItemChanged(railCommonDataList.size() - 1);
                            getBinding().myRecyclerView.scrollToPosition(mScrollY + 500);
                        }

                    }
                }
            }

            @Override
            public void onFailure(Throwable throwable) {
                if (throwable.getMessage().equalsIgnoreCase("No Data")) {
                    getBinding().swipeContainer.setRefreshing(false);
                    getBinding().myRecyclerView.setVisibility(View.GONE);
                    getBinding().noResultLayout.setVisibility(View.VISIBLE);

                }
            }

            @Override
            public void onFinish() {
                swipeToRefresh = 1;
                if (railCommonDataList.size() <= 0) {
                    getBinding().swipeContainer.setRefreshing(false);
                    getBinding().myRecyclerView.setVisibility(View.GONE);
                    getBinding().noResultLayout.setVisibility(View.VISIBLE);
                }
            }
        });


       /* railInjectionHelper.getCatData(getActivity(),tabId).observe(getActivity(), new Observer<ResponseModel>() {
            @Override
            public void onChanged(ResponseModel responseModel) {
                if (responseModel.getStatus().equalsIgnoreCase(APIStatus.START.name())){
                    PrintLogging.printLog("","apiProgress "+APIStatus.START.name());

                }else if (responseModel.getStatus().equalsIgnoreCase(APIStatus.SUCCESS.name())){
                    PrintLogging.printLog("","apiProgress "+APIStatus.SUCCESS.name());
                    railInjectionHelper.getRailCommonData(getActivity(),responseModel.getBaseCategoriesList(), new CommonApiCallBack() {
                        @Override
                        public void onSuccess(Object item) {
                            parseSuccessData(item);
                        }

                        @Override
                        public void onFailure(Throwable throwable) {
                            if (throwable.getMessage().equalsIgnoreCase("No Data")) {
                                getBinding().swipeContainer.setRefreshing(false);
                                getBinding().myRecyclerView.setVisibility(View.GONE);
                                getBinding().noResultLayout.setVisibility(View.VISIBLE);

                            }
                        }

                        @Override
                        public void onFinish() {
                            swipeToRefresh = 1;
                            if (railCommonDataList.size() <= 0) {
                                getBinding().swipeContainer.setRefreshing(false);
                                getBinding().myRecyclerView.setVisibility(View.GONE);
                                getBinding().noResultLayout.setVisibility(View.VISIBLE);
                            }
                        }
                    });
                }else if (responseModel.getStatus().equalsIgnoreCase(APIStatus.ERROR.name())){
                    PrintLogging.printLog("","apiProgress "+APIStatus.ERROR.name());
                    getBinding().swipeContainer.setRefreshing(false);
                    getBinding().myRecyclerView.setVisibility(View.GONE);
                    getBinding().noResultLayout.setVisibility(View.VISIBLE);
                }
                else if (responseModel.getStatus().equalsIgnoreCase(APIStatus.FAILURE.name())){
                    PrintLogging.printLog("","apiProgress "+APIStatus.FAILURE.name());
                    getBinding().swipeContainer.setRefreshing(false);
                    getBinding().myRecyclerView.setVisibility(View.GONE);
                    getBinding().noResultLayout.setVisibility(View.VISIBLE);
                }
            }
        });*/

    }


    private void parseSuccessData(Object item) {
        if (item instanceof RailCommonData) {
            RailCommonData railCommonData = (RailCommonData) item;
            railCommonDataList.add(railCommonData);
            getBinding().swipeContainer.setRefreshing(false);
            if (adapterNew == null) {
                adapterNew = new CommonAdapterNew(getContext(), railCommonDataList, TabsBaseFragment.this::railItemClick, TabsBaseFragment.this::moreRailClick);
                getBinding().myRecyclerView.setAdapter(adapterNew);
            } else {
                synchronized (railCommonDataList) {
                    adapterNew.notifyItemChanged(railCommonDataList.size() - 1);
                    getBinding().myRecyclerView.scrollToPosition(mScrollY + 500);
                }

            }
        }
    }


    private void callShimmer() {
        ShimmerAdapter shimmerAdapter = new ShimmerAdapter(getActivity(), new ShimmerDataModel(getActivity()).getList(0), new ShimmerDataModel(getActivity()).getSlides());
        getBinding().myRecyclerView.setAdapter(shimmerAdapter);
    }

    private void noConnectionLayout() {
        getBinding().noConnectionLayout.setVisibility(View.VISIBLE);
        getBinding().connection.retryTxt.setOnClickListener(view -> connectionObserver());

    }
    private void swipeToRefresh() {
        getBinding().swipeContainer.setOnRefreshListener(() -> {
            if (NetworkConnectivity.isOnline(getBaseActivity()) && swipeToRefresh == 1) {
                swipeToRefresh = 2;
                counter = 0;
                counterValueApiFail = 0;
                adapter = null;
                crouseInjected = false;
                railCommonDataList.clear();
                connectionObserver();
                swipeToRefreshCheck();
            } else {
                swipeToRefreshCheck();
            }
        });


    }

    private void swipeToRefreshCheck() {
        if (getBinding().swipeContainer != null) {
            if (getBinding().swipeContainer.isRefreshing()) {
                getBinding().swipeContainer.setRefreshing(false);
            }
        }
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        modelCall();
        viewModel.resetObject();

    }

    //    @Override
//    public void onViewCreated(@Nullable Bundle savedInstanceState) {
//        super.onViewCreated(savedInstanceState);
//        modelCall();
//        viewModel.resetObject();
//    }


    @Override
    public void onStart() {
        super.onStart();
    }


    @Override
    public void railItemClick(RailCommonData railCommonData, int position) {
        try {
//            AppCommonMethod.trackFcmEvent(railCommonData.getEnveuVideoItemBeans().get(position).getTitle(), railCommonData.getEnveuVideoItemBeans().get(position).getAssetType(), getActivity(), position);
        } catch (Exception e) {

        }
        if (railCommonData.getScreenWidget().getType() != null && railCommonData.getScreenWidget().getLayout().equalsIgnoreCase(Layouts.HRO.name())) {
            heroClickRedirection(railCommonData);
        } else {

            if (railCommonData.getEnveuVideoItemBeans().get(position).getAssetType() == MediaTypeConstants.getInstance().getSeries()) {
                String videoId = railCommonData.getEnveuVideoItemBeans().get(position).getkEntryId();
                AppCommonMethod.launchDetailScreen(getActivity(), videoId, MediaTypeConstants.getInstance().getSeries(), railCommonData.getEnveuVideoItemBeans().get(position).getId(), "0", false,railCommonData.getEnveuVideoItemBeans().get(position));
            } else {
                if (railCommonData.getEnveuVideoItemBeans().get(position).getAssetType() != null && railCommonData.getEnveuVideoItemBeans().get(position).getBrightcoveVideoId()!=null &&
                        !railCommonData.getEnveuVideoItemBeans().get(position).getBrightcoveVideoId().equalsIgnoreCase("")) {
                    AppCommonMethod.launchDetailScreen(getActivity(), railCommonData.getEnveuVideoItemBeans().get(position).getkEntryId(), railCommonData.getEnveuVideoItemBeans().get(position).getAssetType(), railCommonData.getEnveuVideoItemBeans().get(position).getId(), "0", railCommonData.getEnveuVideoItemBeans().get(position).isPremium(),railCommonData.getEnveuVideoItemBeans().get(position));
                } else {
                    AppCommonMethod.launchDetailScreen(getActivity(), "", railCommonData.getEnveuVideoItemBeans().get(position).getAssetType(), railCommonData.getEnveuVideoItemBeans().get(position).getId(), "0", railCommonData.getEnveuVideoItemBeans().get(position).isPremium(),railCommonData.getEnveuVideoItemBeans().get(position));
                }
            }
        }
    }

    private void heroClickRedirection(RailCommonData railCommonData) {
        try {
            AppCommonMethod.trackFcmEvent(railCommonData.getEnveuVideoItemBeans().get(0).getTitle(), railCommonData.getEnveuVideoItemBeans().get(0).getAssetType(), getActivity(), 0);
        } catch (Exception e) {

        }
        String landingPageType = railCommonData.getScreenWidget().getLandingPageType();
        if (landingPageType != null) {
            if (landingPageType.equals(LandingPageType.DEF.name()) || landingPageType.equals(LandingPageType.AST.name())) {
                String videoId = "";
                if (AppCommonMethod.getCheckKEntryId(railCommonData.getEnveuVideoItemBeans().get(0).getkEntryId())) {
                    videoId = railCommonData.getEnveuVideoItemBeans().get(0).getkEntryId();
                }
                AppCommonMethod.heroAssetRedirections(railCommonData,getActivity(),videoId,Integer.parseInt(railCommonData.getScreenWidget().getLandingPageAssetId()),"0",false);

            } else if (landingPageType.equals(LandingPageType.HTM.name())) {
                Intent webViewIntent = new Intent(getActivity(), WebViewActivity.class);
                webViewIntent.putExtra(AppConstants.WEB_VIEW_HEADING, railCommonData.getScreenWidget().getLandingPageTitle());
                webViewIntent.putExtra(AppConstants.WEB_VIEW_URL, railCommonData.getScreenWidget().getHtmlLink());
                startActivity(webViewIntent);
            } else if (landingPageType.equals(LandingPageType.PDF.name())) {
                if (railCommonData.getScreenWidget().getLandingPagetarget() != null) {
                    if (railCommonData.getScreenWidget().getLandingPagetarget().equals(PDFTarget.LGN.name())) {
                        new ActivityLauncher(getActivity()).loginActivity(getActivity(), LoginActivity.class);
                    } else if (railCommonData.getScreenWidget().getLandingPagetarget().equals(PDFTarget.SRH.name())) {
                        new ActivityLauncher(getActivity()).searchActivity(getActivity(), ActivitySearch.class);
                    }
                }
            } else if (landingPageType.equals(LandingPageType.PLT.name())) {
                Logger.e("MORE RAIL CLICK", new Gson().toJson(railCommonData));
                moreRailClick(railCommonData, 0);
            }
        }
    }

    @Override
    public void moreRailClick(RailCommonData data, int position) {
       try {
           if (data.getScreenWidget() != null) {
               if (data.getScreenWidget().getContentID() != null)
                   playListId = data.getScreenWidget().getContentID();
               else
                   playListId = data.getScreenWidget().getLandingPagePlayListId();

               if (data.getScreenWidget().getName() != null && data.getScreenWidget().getReferenceName() != null && (data.getScreenWidget().getReferenceName().equalsIgnoreCase(AppConstants.ContentType.CONTINUE_WATCHING.name()) || data.getScreenWidget().getReferenceName().equalsIgnoreCase("special_playlist"))) {
                   new ActivityLauncher(getActivity()).portraitListing(getActivity(), GridActivity.class, playListId, data.getScreenWidget().getName().toString(), 0, 0, data.getScreenWidget(),position);
               } else if (data.getScreenWidget().getName() != null && data.getScreenWidget().getReferenceName() != null && (data.getScreenWidget().getReferenceName().equalsIgnoreCase(AppConstants.ContentType.MY_WATCHLIST.name()))) {
                   new ActivityLauncher(getActivity()).watchHistory(getActivity(), WatchListActivity.class, data.getScreenWidget().getName().toString(), false);
               } else {
                   if (data.getScreenWidget().getContentListinglayout() != null && !data.getScreenWidget().getContentListinglayout().equalsIgnoreCase("") && data.getScreenWidget().getContentListinglayout().equalsIgnoreCase(ListingLayoutType.LST.name())) {
                       if (data.getScreenWidget().getName() != null) {
                           new ActivityLauncher(getActivity()).listActivity(getActivity(), ListActivity.class, playListId, data.getScreenWidget().getName().toString(), 0, 0, data.getScreenWidget());
                       } else {
                           new ActivityLauncher(getActivity()).listActivity(getActivity(), ListActivity.class, playListId, "", 0, 0, data.getScreenWidget());
                       }
                   } else if (data.getScreenWidget().getContentListinglayout() != null && !data.getScreenWidget().getContentListinglayout().equalsIgnoreCase("") && data.getScreenWidget().getContentListinglayout().equalsIgnoreCase(ListingLayoutType.GRD.name())) {
                       Logger.e("getRailData", "GRD");
                       if (data.getScreenWidget().getName() != null) {
                           new ActivityLauncher(getActivity()).portraitListing(getActivity(), GridActivity.class, playListId, data.getScreenWidget().getName().toString(), 0, 0, data.getScreenWidget(),position);
                       } else {
                           new ActivityLauncher(getActivity()).portraitListing(getActivity(), GridActivity.class, playListId, "", 0, 0, data.getScreenWidget(),position);
                       }
                   } else {
                       Logger.e("getRailData", "PDF");
                       if (data.getScreenWidget().getType() != null && data.getScreenWidget().getLayout().equalsIgnoreCase(Layouts.HRO.name())){
                           if (data.getScreenWidget().getHeroTitle() != null) {
                               new ActivityLauncher(getActivity()).portraitListing(getActivity(), GridActivity.class, playListId, data.getScreenWidget().getHeroTitle().toString(), 0, 0, data.getScreenWidget(),position);
                           } else {
                               new ActivityLauncher(getActivity()).portraitListing(getActivity(), GridActivity.class, playListId, "", 0, 0, data.getScreenWidget(),position);
                           }
                       }else {
                           if (data.getScreenWidget().getName() != null) {
                               new ActivityLauncher(getActivity()).portraitListing(getActivity(), GridActivity.class, playListId, data.getScreenWidget().getName().toString(), 0, 0, data.getScreenWidget(),position);
                           } else {
                               new ActivityLauncher(getActivity()).portraitListing(getActivity(), GridActivity.class, playListId, "", 0, 0, data.getScreenWidget(),position);
                           }
                       }

                   }
               }
           }
       }catch (Exception ignored){

       }
    }

    public interface OnFragmentInteractionListener {
        void onFragmentInteraction(String name);
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);

        if(adapterNew!=null) {
            getBinding().myRecyclerView.requestLayout();
            getBinding().myRecyclerView.setAdapter(null);
            getBinding().myRecyclerView.setLayoutManager(null);
            getBinding().myRecyclerView.setAdapter(adapterNew);
            getBinding().myRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity(), RecyclerView.VERTICAL, false));
            adapterNew.notifyDataSetChanged();
        }
    }

    public void updateList(){
        if (preference!=null && !preference.getAppPrefLoginStatus()) {
           for (int i=0;i<railCommonDataList.size();i++){
               if (railCommonDataList.get(i).isContinueWatching()){
                   railCommonDataList.remove(i);
                   adapterNew.notifyItemRemoved(i);
               }
           }
        }

    }

    public void updateAdList(){
        try {
            for (int i=0;i<railCommonDataList.size();i++){
                if (railCommonDataList.get(i).isAd()){
                    Logger.w("isAdConfigurd"+i+"  -->>",railCommonDataList.get(i).isAd()+"");
                    if (preference.getEntitlementStatus()){
                        railCommonDataList.remove(i);
                        adapterNew.notifyItemRemoved(i);
                    }
                }
            }
        }catch (Exception ignored){

        }

    }


}
