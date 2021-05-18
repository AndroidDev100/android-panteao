package panteao.make.ready.fragments.player.ui;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.make.enums.LandingPageType;
import com.make.enums.Layouts;
import com.make.enums.ListingLayoutType;
import com.make.enums.PDFTarget;

import panteao.make.ready.activities.detail.ui.EpisodeActivity;
import panteao.make.ready.activities.listing.listui.ListActivity;
import panteao.make.ready.activities.listing.ui.GridActivity;
import panteao.make.ready.activities.privacypolicy.ui.WebViewActivity;
import panteao.make.ready.activities.series.ui.SeriesDetailActivity;
import panteao.make.ready.activities.usermanagment.ui.LoginActivity;
import panteao.make.ready.baseModels.BaseBindingFragment;
import panteao.make.ready.activities.search.ui.ActivitySearch;
import panteao.make.ready.adapters.commonRails.CommonAdapterNew;
import panteao.make.ready.beanModel.enveuCommonRailData.RailCommonData;
import panteao.make.ready.callbacks.commonCallbacks.CommonApiCallBack;
import panteao.make.ready.callbacks.commonCallbacks.CommonRailtItemClickListner;
import panteao.make.ready.callbacks.commonCallbacks.MoreClickListner;
import panteao.make.ready.databinding.DetailFooterFragmentBinding;
import panteao.make.ready.utils.MediaTypeConstants;
import panteao.make.ready.utils.commonMethods.AppCommonMethod;
import panteao.make.ready.utils.constants.AppConstants;
import panteao.make.ready.utils.cropImage.helpers.Logger;
import panteao.make.ready.utils.helpers.RailInjectionHelper;
import panteao.make.ready.utils.helpers.intentlaunchers.ActivityLauncher;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.List;

public class RecommendationRailFragment extends BaseBindingFragment<DetailFooterFragmentBinding> implements CommonRailtItemClickListner, MoreClickListner {

    private List<RailCommonData> railCommonDataList = new ArrayList<>();
    private CommonAdapterNew adapterDetailRail;
    private String tabId;
    private String playListId;
    private Context context;

    @Override
    protected DetailFooterFragmentBinding inflateBindingLayout(@NonNull LayoutInflater inflater) {
        return DetailFooterFragmentBinding.inflate(inflater);
    }


    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);


    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        try {
            getVideoRails(getArguments());
        } catch (Exception e) {
        }
    }


    public void getVideoRails(Bundle bund) {
        Bundle bundle = bund;
        if (bundle != null) {
            tabId = bundle.getString(AppConstants.BUNDLE_TAB_ID);
          //tabId = "3";
            hitApiRecommendationRail();
        }
    }

    private void hitApiRecommendationRail() {
        railCommonDataList = new ArrayList<>();
        adapterDetailRail = null;
        getBinding().progressBar.setVisibility(View.VISIBLE);
        setRecyclerProperties(getBinding().recyclerView);

        railCommonDataList.clear();
        RailInjectionHelper railInjectionHelper = ViewModelProviders.of(this).get(RailInjectionHelper.class);
        railInjectionHelper.getScreenWidgets(getActivity(), tabId, new CommonApiCallBack() {
            @Override
            public void onSuccess(Object item) {
                getBinding().progressBar.setVisibility(View.GONE);
                if (item instanceof RailCommonData) {
                    RailCommonData railCommonData = (RailCommonData) item;
                    railCommonDataList.add(railCommonData);
                    AppCommonMethod.isSeasonCount = false;
                    if (adapterDetailRail == null) {
                        getBinding().rlFooter.setVisibility(View.VISIBLE);
                        getBinding().recyclerView.setVisibility(View.VISIBLE);
                        //new RecyclerAnimator(getActivity()).animate(getBinding().recyclerView);
                        adapterDetailRail = new CommonAdapterNew(getActivity(), railCommonDataList, RecommendationRailFragment.this::railItemClick, RecommendationRailFragment.this::moreRailClick);
                        getBinding().recyclerView.setAdapter(adapterDetailRail);

                    } else {
                        synchronized (railCommonDataList) {
                            adapterDetailRail.notifyItemChanged(railCommonDataList.size() - 1);
                        }
                    }
                } else {
                    getBinding().rlFooter.setVisibility(View.GONE);
                    getBinding().recyclerView.setVisibility(View.GONE);
                }

                hideProgressBar();
            }

            @Override
            public void onFailure(Throwable throwable) {
                getBinding().progressBar.setVisibility(View.GONE);
                if (throwable.getMessage().equalsIgnoreCase("No Data")) {
                    getBinding().rlFooter.setVisibility(View.VISIBLE);
                    getBinding().recyclerView.setVisibility(View.GONE);
                    removeTab();
                }
                hideProgressBar();
            }

            @Override
            public void onFinish() {
                if (railCommonDataList.size()>0){
                }else {
                    getBinding().progressBar.setVisibility(View.GONE);
                    removeTab();
                    hideProgressBar();
                }
            }
        });

    }


    public void removeTab() {
        if (context instanceof SeriesDetailActivity) {
            ((SeriesDetailActivity) context).removeTab(1);
        } else if (context instanceof EpisodeActivity) {
            ((EpisodeActivity) context).removeTab(1);
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

    public void setRecyclerProperties(RecyclerView recyclerView) {
        recyclerView.setNestedScrollingEnabled(false);
        recyclerView.setHasFixedSize(false);
        LinearLayoutManager mLayoutManager = new LinearLayoutManager(getActivity(), RecyclerView.VERTICAL, false);
        recyclerView.setLayoutManager(mLayoutManager);
    }


    @Override
    public void railItemClick(RailCommonData railCommonData, int position) {
        Log.d("recommended rail","success");
        try {
//            AppCommonMethod.trackFcmEvent(railCommonData.getEnveuVideoItemBeans().get(position).getTitle(),railCommonData.getEnveuVideoItemBeans().get(position).getAssetType(),getActivity(),position);
        }catch (Exception e){

        }
        if (railCommonData.getScreenWidget().getType() != null && railCommonData.getScreenWidget().getLayout().equalsIgnoreCase(Layouts.HRO.name())) {
            heroClickRedirection(railCommonData);
        } else {
            if (railCommonData.isSeries() && AppCommonMethod.getCheckBCID(railCommonData.getEnveuVideoItemBeans().get(position).getBrightcoveVideoId())) {
                Long videoId = Long.parseLong(railCommonData.getEnveuVideoItemBeans().get(position).getBrightcoveVideoId());
                getActivity().finish();
                AppCommonMethod.launchDetailScreen(getActivity(), videoId, MediaTypeConstants.getInstance().getSeries(), railCommonData.getEnveuVideoItemBeans().get(position).getId(), "0", false);
            } else {

                    if (railCommonData.getEnveuVideoItemBeans().get(position).getAssetType() != null) {
                        if (AppCommonMethod.getCheckBCID(railCommonData.getEnveuVideoItemBeans().get(position).getBrightcoveVideoId())){
                            getActivity().finish();
                            AppCommonMethod.launchDetailScreen(getActivity(), Long.valueOf(railCommonData.getEnveuVideoItemBeans().get(position).getBrightcoveVideoId()), railCommonData.getEnveuVideoItemBeans().get(position).getAssetType(), railCommonData.getEnveuVideoItemBeans().get(position).getId(), "0", railCommonData.getEnveuVideoItemBeans().get(position).isPremium());
                        }else {
                            getActivity().finish();
                            AppCommonMethod.launchDetailScreen(getActivity(), 0l, railCommonData.getEnveuVideoItemBeans().get(position).getAssetType(), railCommonData.getEnveuVideoItemBeans().get(position).getId(), "0", railCommonData.getEnveuVideoItemBeans().get(position).isPremium());
                        }
                    } else {
                        if (AppCommonMethod.getCheckBCID(railCommonData.getEnveuVideoItemBeans().get(position).getBrightcoveVideoId())){
                            getActivity().finish();
                            AppCommonMethod.launchDetailScreen(getActivity(), Long.valueOf(railCommonData.getEnveuVideoItemBeans().get(position).getBrightcoveVideoId()), AppConstants.Video, railCommonData.getEnveuVideoItemBeans().get(position).getId(), "0", railCommonData.getEnveuVideoItemBeans().get(position).isPremium());
                        }else {
                            getActivity().finish();
                            AppCommonMethod.launchDetailScreen(getActivity(), 0l, AppConstants.Video, railCommonData.getEnveuVideoItemBeans().get(position).getId(), "0", railCommonData.getEnveuVideoItemBeans().get(position).isPremium());
                        }
                    }

            }
        }
    }

    private void heroClickRedirection(RailCommonData railCommonData) {
        try {
            AppCommonMethod.trackFcmEvent(railCommonData.getEnveuVideoItemBeans().get(0).getTitle(),railCommonData.getEnveuVideoItemBeans().get(0).getAssetType(),getActivity(),0);
        }catch (Exception e){

        }
        String landingPageType = railCommonData.getScreenWidget().getLandingPageType();
        if (landingPageType != null) {
            if (landingPageType.equals(LandingPageType.DEF.name()) || landingPageType.equals(LandingPageType.AST.name())) {
                Long videoId = 0l;
                if (AppCommonMethod.getCheckBCID(railCommonData.getEnveuVideoItemBeans().get(0).getBrightcoveVideoId())) {
                    videoId = Long.parseLong(railCommonData.getEnveuVideoItemBeans().get(0).getBrightcoveVideoId());
                }
                if (railCommonData.getEnveuVideoItemBeans().get(0).getAssetType().equalsIgnoreCase( MediaTypeConstants.getInstance().getEpisode())) {
                    AppCommonMethod.launchDetailScreen(getActivity(), videoId, MediaTypeConstants.getInstance().getEpisode(), Integer.parseInt(railCommonData.getScreenWidget().getLandingPageAssetId()), "0", false);
                } else if (railCommonData.getEnveuVideoItemBeans().get(0).getAssetType().equalsIgnoreCase(MediaTypeConstants.getInstance().getSeries())) {
                    AppCommonMethod.launchDetailScreen(getActivity(), videoId, MediaTypeConstants.getInstance().getSeries(), Integer.parseInt(railCommonData.getScreenWidget().getLandingPageAssetId()), "0", false);
                } else {
                    AppCommonMethod.launchDetailScreen(getActivity(), videoId, AppConstants.Video, Integer.parseInt(railCommonData.getScreenWidget().getLandingPageAssetId()), "0", false);
                }
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
        if (data.getScreenWidget() != null) {
            if (data.getScreenWidget().getContentID() != null)
                playListId = data.getScreenWidget().getContentID();
            else
                playListId = data.getScreenWidget().getLandingPagePlayListId();

            if (data.getScreenWidget().getContentListinglayout() != null && !data.getScreenWidget().getContentListinglayout().equalsIgnoreCase("") && data.getScreenWidget().getContentListinglayout().equalsIgnoreCase(ListingLayoutType.LST.name())) {
               startListingActivity(data);
            } else if (data.getScreenWidget().getContentListinglayout() != null && !data.getScreenWidget().getContentListinglayout().equalsIgnoreCase("") && data.getScreenWidget().getContentListinglayout().equalsIgnoreCase(ListingLayoutType.GRD.name())) {
                startGridActivity(data);
            } else {
                startListingActivity(data);
            }
        }
    }

    private void startListingActivity(RailCommonData data) {
        if (data.getScreenWidget() != null && data.getScreenWidget().getContentID() != null) {
            String playListId = data.getScreenWidget().getContentID();
            String screenName = "";
            if (data.getScreenWidget().getName() != null) {
                screenName = (String) data.getScreenWidget().getName();
            }
            Intent intent = new Intent(getActivity(), ListActivity.class);
            intent.putExtra("playListId", playListId);
            intent.putExtra("title", screenName);
            intent.putExtra("flag", 0);
            intent.putExtra("shimmerType", 0);
            intent.putExtra("baseCategory", data.getScreenWidget());
            startActivityForResult(intent, 1001);
        }
    }
    private void startGridActivity(RailCommonData data) {
        if (data.getScreenWidget() != null && data.getScreenWidget().getContentID() != null) {
            String playListId = data.getScreenWidget().getContentID();
            String screenName = "";
            if (data.getScreenWidget().getName() != null) {
                screenName = (String) data.getScreenWidget().getName();
            }
            Intent intent = new Intent(getActivity(), GridActivity.class);
            intent.putExtra("playListId", playListId);
            intent.putExtra("title", screenName);
            intent.putExtra("flag", 0);
            intent.putExtra("shimmerType", 0);
            intent.putExtra("baseCategory", data.getScreenWidget());
            startActivityForResult(intent, 1001);
        }
    }
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (data != null) {
            Bundle bundle = data.getBundleExtra(AppConstants.BUNDLE_ASSET_BUNDLE);
            if (bundle != null) {
                if (requestCode == 1001) {
                    if (resultCode == 10001) {
                        Logger.e("Bundle", new Gson().toJson(bundle));
                        getActivity().finish();
                        AppCommonMethod.launchDetailScreen(getActivity(), bundle.getLong(AppConstants.BUNDLE_VIDEO_ID_BRIGHTCOVE, 0l), bundle.getString(AppConstants.BUNDLE_ASSET_TYPE), bundle.getInt(AppConstants.BUNDLE_ASSET_ID, 0), bundle.getString(AppConstants.BUNDLE_DURATION), bundle.getBoolean(AppConstants.BUNDLE_IS_PREMIUM, false));
                    }
                }
            }
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        this.context = context;
    }

    public interface OnFragmentInteractionListener {
        void updateData(int id);
    }
}