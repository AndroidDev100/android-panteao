package panteao.make.ready.activities.tutorial;

import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.SystemClock;
import android.util.Log;
import android.view.View;

import androidx.annotation.Nullable;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.SimpleItemAnimator;

import panteao.make.ready.activities.series.adapter.SeasonAdapter;
import panteao.make.ready.activities.tutorial.ui.ChapterActivity;
import panteao.make.ready.activities.tutorial.ui.TutorialActivity;
import panteao.make.ready.baseModels.BaseBindingFragment;
import panteao.make.ready.networking.apistatus.APIStatus;
import panteao.make.ready.networking.responsehandler.ResponseModel;
import panteao.make.ready.R;
import panteao.make.ready.beanModel.enveuCommonRailData.RailCommonData;
import panteao.make.ready.beanModelV3.uiConnectorModelV2.EnveuVideoItemBean;
import panteao.make.ready.beanModel.selectedSeason.SelectedSeasonModel;
import panteao.make.ready.databinding.SeasonFragmentLayoutBinding;
import panteao.make.ready.utils.MediaTypeConstants;
import panteao.make.ready.utils.commonMethods.AppCommonMethod;
import panteao.make.ready.utils.constants.AppConstants;
import panteao.make.ready.utils.cropImage.helpers.Logger;
import panteao.make.ready.utils.helpers.RailInjectionHelper;
import panteao.make.ready.utils.helpers.RecyclerAnimator;
import panteao.make.ready.utils.helpers.SpacingItemDecoration;
import panteao.make.ready.utils.helpers.StringUtils;
import panteao.make.ready.utils.helpers.downloads.KTDownloadHelper;

import java.util.ArrayList;
import java.util.List;


public class TSeasonTabFragment extends BaseBindingFragment<SeasonFragmentLayoutBinding> implements SeasonAdapter.EpisodeItemClick {

    private RailInjectionHelper railInjectionHelper;
    private int seriesId;
    private int seasonCount;
    private int selectedSeason = 0;
    private ArrayList seasonArray;
    private Bundle bundle;
    private Context context;
    private ArrayList<SelectedSeasonModel> seasonList;
    private int currentAssetId;
    private SeasonAdapter seasonAdapter;
    private List<EnveuVideoItemBean> seasonEpisodes, allEpiosdes;
    private long mLastClickTime = 0;

    public TSeasonTabFragment() {
    }

    public int getSelectedSeason() {
        return selectedSeason;
    }

    public void setSelectedSeason(int selectedSeason) {
        this.selectedSeason = selectedSeason;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        this.context = context;
    }

    @Override
    protected SeasonFragmentLayoutBinding inflateBindingLayout() {
        return SeasonFragmentLayoutBinding.inflate(inflater);
    }


    @Override
    public void onStart() {
        super.onStart();
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        Logger.e("ON VIEW CREATED-----", "TRUE");
        bundle = getArguments();

        seasonArray = bundle.getParcelableArrayList(AppConstants.BUNDLE_SEASON_ARRAY);
        Log.w("", seasonArray + "");

        try {
            getVideoRails();
        } catch (Exception e) {
        }
    }

    public void getVideoRails() {
        if (bundle != null) {
            getBinding().seasonHeader.setVisibility(View.GONE);
            seriesId = bundle.getInt(AppConstants.BUNDLE_ASSET_ID);
            seasonCount = bundle.getInt(AppConstants.BUNDLE_SEASON_COUNT);
            currentAssetId = bundle.getInt(AppConstants.BUNDLE_CURRENT_ASSET_ID);
            if (seasonCount > 0) {
                seasonList = new ArrayList<>();
                selectedSeason = 1;
                int tempSeaon = bundle.getInt(AppConstants.BUNDLE_SELECTED_SEASON);
                if (context instanceof ChapterActivity && tempSeaon > 0)
                    selectedSeason = tempSeaon;
                if (context instanceof TutorialActivity) {
                    if (seasonArray != null && !seasonArray.isEmpty()) {
                        selectedSeason = (int) seasonArray.get(0);
                    }
                }

            }
            getBinding().seasonHeader.setEnabled(false);

            if (seriesId == -1) {
                getBinding().seasonHeader.setVisibility(View.VISIBLE);
                getBinding().seasonHeader.setText(getResources().getString(R.string.all_chapters));
                getBinding().seasonHeader.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, 0);
                getBinding().comingSoon.setVisibility(View.VISIBLE);
                getBinding().seriesRecyclerView.setVisibility(View.GONE);
                getBinding().seasonMore.setVisibility(View.GONE);
                hideProgressBar();
            } else {
                getEpisodeList();
            }
        }

        getBinding().seasonMore.setOnClickListener(v -> {
            if (SystemClock.elapsedRealtime() - mLastClickTime < 1500) {
                return;
            }
            getBinding().seasonMore.setVisibility(View.GONE);
            getBinding().progressBar.setVisibility(View.VISIBLE);
            mLastClickTime = SystemClock.elapsedRealtime();
            totalPages++;
            if (seasonCount > 0) {
                getSeasonEpisodes();
            } else {
                //here -1 indicates not to send seasonNumber key
                getAllEpisodes();
            }

        });
        getBinding().seasonHeader.setOnClickListener(v -> {

            if (SystemClock.elapsedRealtime() - mLastClickTime < 1000) {
                return;
            }
            mLastClickTime = SystemClock.elapsedRealtime();
            if (seasonList != null)
                seasonList.clear();
            for (int i = 0; i < seasonArray.size(); i++) {

                if (selectedSeason == (int) seasonArray.get(i))
                    seasonList.add(new SelectedSeasonModel(getResources().getString(R.string.season) + " " + seasonArray.get(i), (int) seasonArray.get(i), true));
                else
                    seasonList.add(new SelectedSeasonModel(getResources().getString(R.string.season) + " " + seasonArray.get(i), (int) seasonArray.get(i), false));
            }
            if (context instanceof TutorialActivity) {
                ((TutorialActivity) context).showSeasonList();
            } else if (context instanceof ChapterActivity) {
                ((ChapterActivity) context).showSeasonList();
            }
        });


    }


    public void hideProgressBar() {
        if (context instanceof TutorialActivity) {
            ((TutorialActivity) context).isSeasonData = true;
            ((TutorialActivity) context).stopShimmer();
            ((TutorialActivity) context).dismissLoading(((TutorialActivity) context).getBinding().progressBar);
            if (seasonAdapter != null) {
                ((TutorialActivity) context).numberOfEpisodes();
            }
        } else if (context instanceof ChapterActivity) {
            ((ChapterActivity) context).dismissLoading(((ChapterActivity) context).getBinding().progressBar);
            ((ChapterActivity) context).isSeasonData = true;
            ((ChapterActivity) context).stopShimmercheck();
            if (seasonAdapter != null) {
                ((ChapterActivity) context).numberOfEpisodes();
            }
        }
    }

    private void getEpisodeList() {
        getBinding().seriesRecyclerView.addItemDecoration(new SpacingItemDecoration(8, SpacingItemDecoration.HORIZONTAL));
        railInjectionHelper = new ViewModelProvider(this).get(RailInjectionHelper.class);
        if (seasonCount > 0) {
            getSeasonEpisodes();
        } else {
            //here -1 indicates not to send seasonNumber key
            getAllEpisodes();
        }

    }

    public void getAllEpisodes() {
        railInjectionHelper.getEpisodeNoSeasonV2(seriesId, totalPages, 50, -1).observe(getActivity(), new Observer<ResponseModel>() {
            @Override
            public void onChanged(ResponseModel response) {
                if (response != null) {
                    if (response.getStatus().equalsIgnoreCase(APIStatus.START.name())) {

                    } else if (response.getStatus().equalsIgnoreCase(APIStatus.SUCCESS.name())) {
                        if (response.getBaseCategory() != null) {
                            RailCommonData enveuCommonResponse = (RailCommonData) response.getBaseCategory();
                            getBinding().comingSoon.setVisibility(View.GONE);
                            getBinding().progressBar.setVisibility(View.GONE);
                            if (!StringUtils.isNullOrEmptyOrZero(enveuCommonResponse.getSeasonName())) {

                            } else {
                                // all episode view to set here
                                if (enveuCommonResponse.getPageTotal() - 1 > totalPages) {
                                    getBinding().seasonMore.setVisibility(View.VISIBLE);

                                } else {
                                    getBinding().seasonMore.setVisibility(View.GONE);
                                }
                                getBinding().seasonHeader.setVisibility(View.VISIBLE);
                                getBinding().seasonHeader.setEnabled(false);

                                if (seasonAdapter == null) {
                                    allEpiosdes = enveuCommonResponse.getEnveuVideoItemBeans();
                                    getBinding().seasonHeader.setText(getResources().getString(R.string.all_chapters));
                                    getBinding().seasonHeader.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, 0);
                                    new RecyclerAnimator(getActivity()).animate(getBinding().seriesRecyclerView);
                                    seasonAdapter = new SeasonAdapter(getActivity(), allEpiosdes, seriesId, currentAssetId, TSeasonTabFragment.this);
                                    getBinding().seriesRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity(), RecyclerView.VERTICAL, false));
                                    ((SimpleItemAnimator) getBinding().seriesRecyclerView.getItemAnimator()).setSupportsChangeAnimations(false);
                                    getBinding().seriesRecyclerView.setAdapter(seasonAdapter);
                                    hideProgressBar();

                                } else {
                                    allEpiosdes.addAll(enveuCommonResponse.getEnveuVideoItemBeans());
                                    seasonAdapter.notifyDataSetChanged();
                                    hideProgressBar();
                                }

                                if (context instanceof ChapterActivity) {
                                    new Handler().postDelayed(new Runnable() {
                                        @Override
                                        public void run() {
                                            ((ChapterActivity) context).episodesList();
                                        }
                                    },1200);
                                } else if (context instanceof TutorialActivity) {
                                    new Handler().postDelayed(new Runnable() {
                                        @Override
                                        public void run() {
                                            ((TutorialActivity) context).episodesList();
                                        }
                                    }, 1200);

                                }

                            }
                        }
                    } else if (response.getStatus().equalsIgnoreCase(APIStatus.ERROR.name())) {
                        if (response.getErrorModel().getErrorCode() != 0) {
                            getBinding().seasonHeader.setVisibility(View.GONE);
                            getBinding().progressBar.setVisibility(View.GONE);
                            getBinding().comingSoon.setVisibility(View.VISIBLE);
                            getBinding().seriesRecyclerView.setVisibility(View.GONE);
                            getBinding().seasonMore.setVisibility(View.GONE);

                        }

                    } else if (response.getStatus().equalsIgnoreCase(APIStatus.FAILURE.name())) {
                        getBinding().seasonHeader.setVisibility(View.GONE);
                        getBinding().comingSoon.setVisibility(View.VISIBLE);
                        getBinding().progressBar.setVisibility(View.GONE);
                        getBinding().seriesRecyclerView.setVisibility(View.GONE);
                        getBinding().seasonMore.setVisibility(View.GONE);
                        hideProgressBar();
                    }

                }
            }
        });

    }

    public void getSeasonEpisodes() {
        getBinding().seasonHeader.setEnabled(false);
        ((SimpleItemAnimator) getBinding().seriesRecyclerView.getItemAnimator()).setSupportsChangeAnimations(false);

        railInjectionHelper.getEpisodeNoSeasonV2(seriesId, totalPages, 50, selectedSeason).observe(getActivity(), new Observer<ResponseModel>() {
            @Override
            public void onChanged(ResponseModel response) {
                if (response != null) {
                    if (response.getStatus().equalsIgnoreCase(APIStatus.START.name())) {

                    } else if (response.getStatus().equalsIgnoreCase(APIStatus.SUCCESS.name())) {
                        if (response.getBaseCategory() != null) {
                            getBinding().progressBar.setVisibility(View.GONE);
                            RailCommonData enveuCommonResponse = (RailCommonData) response.getBaseCategory();
                            parseSeriesData();
                        }
                    } else if (response.getStatus().equalsIgnoreCase(APIStatus.ERROR.name())) {
                        if (response.getErrorModel().getErrorCode() != 0) {
                            getBinding().seasonHeader.setVisibility(View.GONE);
                            getBinding().progressBar.setVisibility(View.GONE);
                            getBinding().comingSoon.setVisibility(View.VISIBLE);
                            getBinding().seriesRecyclerView.setVisibility(View.GONE);
                            getBinding().seasonMore.setVisibility(View.GONE);
                        }

                    } else if (response.getStatus().equalsIgnoreCase(APIStatus.FAILURE.name())) {
                        getBinding().seasonHeader.setVisibility(View.GONE);
                        getBinding().comingSoon.setVisibility(View.VISIBLE);
                        getBinding().progressBar.setVisibility(View.GONE);
                        getBinding().seriesRecyclerView.setVisibility(View.GONE);
                        getBinding().seasonMore.setVisibility(View.GONE);
                    }

                }
            }
        });

    }

    int totalPages = 0;

    private void parseSeriesData() {
        if (railCommonData != null) {
            if (railCommonData.getEnveuVideoItemBeans().size() > 0) {

                if (railCommonData.getPageTotal() - 1 > totalPages) {
                    getBinding().seasonMore.setVisibility(View.VISIBLE);

                } else {
                    getBinding().seasonMore.setVisibility(View.GONE);

                }
                getBinding().seasonHeader.setVisibility(View.VISIBLE);
                getBinding().seasonHeader.setEnabled(true);

                if (seasonAdapter == null) {
                    seasonEpisodes = railCommonData.getEnveuVideoItemBeans();
                    getBinding().seasonHeader.setText(getResources().getString(R.string.season) + " " + railCommonData.getSeasonNumber());
                    getBinding().seasonHeader.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.arrow_down, 0);
                    getBinding().comingSoon.setVisibility(View.GONE);
                    new RecyclerAnimator(getActivity()).animate(getBinding().seriesRecyclerView);
                    seasonAdapter = new SeasonAdapter(getActivity(), seasonEpisodes, seriesId, currentAssetId, this);
                    getBinding().seriesRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity(), RecyclerView.VERTICAL, false));
                    ((SimpleItemAnimator) getBinding().seriesRecyclerView.getItemAnimator()).setSupportsChangeAnimations(false);
                    getBinding().seriesRecyclerView.setAdapter(seasonAdapter);
                } else {

                    seasonEpisodes.addAll(railCommonData.getEnveuVideoItemBeans());
                    seasonAdapter.notifyDataSetChanged();
                }
                if (context instanceof ChapterActivity) {
                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            ((ChapterActivity) context).episodesList();
                        }
                    },1200);
                }else if (context instanceof TutorialActivity) {
                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            ((TutorialActivity) context).episodesList();
                        }
                    }, 1200);
                }
            } else {
                getBinding().seasonHeader.setVisibility(View.GONE);
                getBinding().comingSoon.setVisibility(View.VISIBLE);
                getBinding().seriesRecyclerView.setVisibility(View.GONE);
                getBinding().seasonMore.setVisibility(View.GONE);
            }
        }
        hideProgressBar();
    }

    @Override
    public void onItemClick() {
        String assetType = enveuVideoItemBean.getAssetType().toUpperCase();
        /*long brighCoveId = 0l;

        if (AppCommonMethod.getCheckBCID(enveuVideoItemBean.getBrightcoveVideoId()))
            brighCoveId = Long.parseLong(enveuVideoItemBean.getBrightcoveVideoId());

        int assetID = enveuVideoItemBean.getId();*/


        if (assetType.equalsIgnoreCase(MediaTypeConstants.getInstance().getChapter())) {
            if (AppCommonMethod.getCheckKEntryId(enveuVideoItemBean.getkEntryId())) {
                AppCommonMethod.launchDetailScreenWithKID(getActivity(), enveuVideoItemBean.getkEntryId(), MediaTypeConstants.getInstance().getChapter(), enveuVideoItemBean.getId(), "0", enveuVideoItemBean.isPremium());
            } else {
                AppCommonMethod.launchDetailScreen(getActivity(), "", MediaTypeConstants.getInstance().getChapter(), enveuVideoItemBean.getId(), "0", enveuVideoItemBean.isPremium(),enveuVideoItemBean);
            }

        }

/*
        switch (assetType) {
            case AppConstants.Episode:
                AppCommonMethod.launchDetailScreen(getActivity(), Long.valueOf(enveuVideoItemBean.getBrightcoveVideoId()), AppConstants.Episode, enveuVideoItemBean.getId(), "0", enveuVideoItemBean.isPremium());
                break;
            case AppConstants.Series:
                //  new ActivityLauncher(getActivity()).seriesDetailScreen(getActivity(), TutorialActivity.class, id);
                break;
            case AppConstants.Video:
               // new ActivityLauncher(getActivity()).detailScreenBrightCove(getActivity(), DetailActivity.class, brighCoveId, assetID, "0", isPremium, AppConstants.MOVIE_ENVEU);
                if (SDKConfig.getInstance().getMovieDetailId().equalsIgnoreCase("")){
                   // new ActivityLauncher(getActivity()).detailScreenBrightCove(getActivity(), DetailActivity.class, brighCoveId, assetID, "0", isPremium, AppConstants.MOVIE_ENVEU);
                }else {
                    new ActivityLauncher(getActivity()).detailScreenBrightCove(getActivity(), DetailActivity.class, brighCoveId, assetID, "0", isPremium, SDKConfig.getInstance().getMovieDetailId());
                }
                break;

        }
*/
    }

    public void updateFragment() {
        getVideoRails();
    }

    public SeasonAdapter getSeasonAdapter() {
        return seasonAdapter;
    }

    public void updateCurrentAsset() {
        currentAssetId = id;
        if (seasonAdapter != null) {
            seasonAdapter.updateCurrentId(currentAssetId);
            seasonAdapter.notifyDataSetChanged();
        }
    }

    public void updateTotalPages() {
        totalPages = 0;
    }

    public void setSeasonAdapter() {
        this.seasonAdapter = seasonAdapter;
    }

    public void updateStatus() {
        if (seasonAdapter!=null){
            seasonAdapter.holdHolder();
        }
    }

    String findAssetId = "";
    int position = 0;
    KTDownloadHelper downloadHelper;
    public void downloadComplete() {
        try {
            List<EnveuVideoItemBean> videoItemBeans = seasonAdapter.getAdapterList();
            if (videoItemBeans != null && videoItemBeans.size() > 0) {
                for (int i = 0; i < videoItemBeans.size(); i++) {
                    if (assetId.equalsIgnoreCase(videoItemBeans.get(i).getkEntryId())) {
                        if (getActivity() != null && !getActivity().isFinishing()) {
                            getActivity().runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    seasonAdapter.downloadCompletChanged(assetId, seasonAdapter.getAdapterList());
                                }
                            });
                        }
                    }
                }
            }
        } catch (Exception ingbored) {

        }
    }

    public void pauseDownload() {
        if (seasonAdapter != null) {
            if (seasonAdapter.getAdapterList() != null) {
                if (seasonAdapter.getAdapterList().size() > 0) {
                    for (int i = 0; i < seasonAdapter.getAdapterList().size(); i++) {
                        if (assetId.equalsIgnoreCase(seasonAdapter.getAdapterList().get(i).getkEntryId())) {
                            if (getActivity() != null && !getActivity().isFinishing()) {
                                getActivity().runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        seasonAdapter.downloadStatusChanged(assetId, seasonAdapter.getAdapterList());
                                    }
                                });
                            }
                        }
                    }
                }
            }
        }
    }

    public void setDownloadHelper() {
        if (downloadHelp!=null){
            this.downloadHelper=downloadHelp;
        }
    }

    public void updateAdapter() {
        Log.w("downlaodContext",context+"");
        try {
            if (seasonAdapter != null) {
                getBinding().seriesRecyclerView.post(new Runnable() {
                    @Override
                    public void run() {
                        if (context!=null && !context.isFinishing()){
                            context.runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    try {
                                        if (seasonAdapter!=null){
                                            seasonAdapter.itemChanged(assetId);
                                        }
                                    }catch (Exception ignored){

                                    }
                                }
                            });

                        }
                    }
                });

            }

        }catch (Exception ignored){

        }
    }


    public void downloadStatusChanged() {
        if (seasonAdapter != null) {
            getBinding().seriesRecyclerView.post(new Runnable() {
                @Override
                public void run() {
                    seasonAdapter.itemStatusChanged(assetID);
                }
            });
        }
    }

    public void cancelDownload() {
        if (seasonAdapter != null) {
            seasonAdapter.onDownloadPaused(videoId);
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        if (seasonAdapter!=null){
            seasonAdapter.refreshDownloadHelper();
            try {
                getBinding().seriesRecyclerView.post(new Runnable() {
                    @Override
                    public void run() {
                        seasonAdapter.notifyDataSetChanged();
                    }
                });
            }catch (Exception ignored){

            }
        }
    }

    public void notifyAdapter() {
        try {
            getBinding().seriesRecyclerView.post(new Runnable() {
                @Override
                public void run() {
                    if (seasonAdapter!=null){
                        seasonAdapter.notifyDataSetChanged();
                    }
                }
            });
        }catch (Exception ignored){

        }
    }


    public void isPlayerStart() {
        if (b){
            if (seasonAdapter!=null){
                seasonAdapter.isPlayerStart(b);
                if (seasonAdapter!=null){
                    getBinding().seriesRecyclerView.post(new Runnable() {
                        @Override
                        public void run() {
                            if (seasonAdapter!=null){
                                seasonAdapter.notifyDataSetChanged();
                            }
                        }
                    });
                }
            }
        }
    }

    public void notifySingleItem() {
        if (seasonAdapter!=null && getkEntryId!=null && !getkEntryId.equalsIgnoreCase("")){
            getBinding().seriesRecyclerView.post(new Runnable() {
                @Override
                public void run() {
                    if (seasonAdapter!=null){
                        seasonAdapter.notifySingleItem(getkEntryId);
                    }
                }
            });
        }
    }

    public void setProgressStatus() {
        if (seasonAdapter!=null){
            getBinding().seriesRecyclerView.post(new Runnable() {
                @Override
                public void run() {
                    if (seasonAdapter!=null){
                        seasonAdapter.downloadStatusChanged();
                    }
                }
            });
        }
    }


}