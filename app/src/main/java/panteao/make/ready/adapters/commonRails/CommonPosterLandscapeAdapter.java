package panteao.make.ready.adapters.commonRails;

import android.app.Activity;
import android.os.SystemClock;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.make.baseCollection.baseCategoryModel.BaseCategory;

import panteao.make.ready.activities.instructor.ui.InstructorActivity;
import panteao.make.ready.activities.show.ui.EpisodeActivity;
import panteao.make.ready.activities.series.ui.SeriesDetailActivity;
import panteao.make.ready.beanModel.ContinueRailModel.CommonContinueRail;
import panteao.make.ready.beanModel.responseModels.series.season.ItemsItem;
import panteao.make.ready.R;
import panteao.make.ready.beanModelV3.uiConnectorModelV2.EnveuVideoItemBean;
import panteao.make.ready.databinding.PosterLandscapeItemBinding;
import panteao.make.ready.utils.commonMethods.AppCommonMethod;
import panteao.make.ready.utils.constants.AppConstants;
import panteao.make.ready.utils.cropImage.helpers.PrintLogging;
import panteao.make.ready.utils.helpers.ImageHelper;

import panteao.make.ready.utils.helpers.StringUtils;
import panteao.make.ready.utils.helpers.intentlaunchers.ActivityLauncher;
import panteao.make.ready.utils.helpers.ksPreferenceKeys.KsPreferenceKeys;

import java.util.ArrayList;
import java.util.List;

public class CommonPosterLandscapeAdapter extends RecyclerView.Adapter<CommonPosterLandscapeAdapter.SingleItemRowHolder> {

    private final List<ItemsItem> seasonItems;
    private final String contentType;
    private final List<EnveuVideoItemBean> itemsList;
    private final Activity mContext;
    private long mLastClickTime = 0;
    private int itemWidth;
    private int itemHeight;
    private ArrayList<CommonContinueRail> continuelist;
    private boolean isLogin;
    private KsPreferenceKeys preference;
    BaseCategory baseCategory;
    public CommonPosterLandscapeAdapter(Activity context, List<EnveuVideoItemBean> itemsList, List<ItemsItem> itemsItems, String contentType, ArrayList<CommonContinueRail> continuelist, BaseCategory baseCat) {
        this.itemsList = itemsList;
        this.mContext = context;
        this.seasonItems = itemsItems;
        this.contentType = contentType;
        this.continuelist = continuelist;
        this.baseCategory=baseCat;
        preference = KsPreferenceKeys.getInstance();
        isLogin = preference.getAppPrefLoginStatus();
        int num = 2;
        boolean tabletSize = mContext.getResources().getBoolean(R.bool.isTablet);
        if (tabletSize) {
            if (mContext.getResources().getConfiguration().orientation == 2)
                num = 4;
            else
                num = 3;
        }
        DisplayMetrics displaymetrics = new DisplayMetrics();
        (mContext).getWindowManager().getDefaultDisplay().getMetrics(displaymetrics);
        //if you need two fix imageview in width
        itemWidth = (displaymetrics.widthPixels - 80) / num;
        itemHeight = (int) (itemWidth * 2) / 3;


    }

    public void notifydata(List<EnveuVideoItemBean> i) {
        this.itemsList.addAll(i);
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public SingleItemRowHolder onCreateViewHolder(@NonNull ViewGroup parent, int i) {
        PosterLandscapeItemBinding binding = DataBindingUtil.inflate(
                LayoutInflater.from(parent.getContext()),
                R.layout.poster_landscape_item, parent, false);
        return new SingleItemRowHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull SingleItemRowHolder holder, int i) {
        holder.itemBinding.itemImage.getLayoutParams().width = itemWidth;
        holder.itemBinding.itemImage.getLayoutParams().height = itemHeight;
        holder.itemBinding.llContinueProgress.setVisibility(View.GONE);
        holder.itemBinding.ivContinuePlay.setVisibility(View.GONE);

        if (itemsList.size() > 0) {

            try {
                AppCommonMethod.handleTags(itemsList.get(i).getIsVIP(),itemsList.get(i).getIsNewS(),
                        holder.itemBinding.flExclusive,holder.itemBinding.flNew,holder.itemBinding.flEpisode,holder.itemBinding.flNewMovie,itemsList.get(i).getAssetType());

            }catch (Exception ignored){

            }

            try {
                AppCommonMethod.handleTitleDesc(holder.itemBinding.titleLayout,holder.itemBinding.tvTitle,holder.itemBinding.tvDescription,baseCategory);
                holder.itemBinding.tvTitle.setText(itemsList.get(i).getTitle());
                holder.itemBinding.tvDescription.setText(itemsList.get(i).getDescription());
            }catch (Exception ignored){

            }

            PrintLogging.printLog("", "CommonPosterLandscapeAdapter");
            EnveuVideoItemBean contentsItem = itemsList.get(i);
            if (contentsItem != null) {

                holder.itemBinding.setPlaylistItem(contentsItem);
//                if (contentsItem.isPremium()) {
//                    holder.itemBinding.flExclusive.setVisibility(View.VISIBLE);
//                } else {
//                    holder.itemBinding.flExclusive.setVisibility(View.GONE);
//                }
//
//                if (contentsItem.isNew()) {
//                    holder.itemBinding.flNew.setVisibility(View.VISIBLE);
//                } else {
//                    holder.itemBinding.flNew.setVisibility(View.GONE);
//                }
                holder.itemBinding.tvTitle.setText(itemsList.get(i).getTitle());

                if (contentsItem.getVideoPosition() > 0) {
                    AppCommonMethod.railBadgeVisibility(holder.itemBinding.llContinueProgress, true);
                    double totalDuration = contentsItem.getDuration();
                    double currentPosition = contentsItem.getVideoPosition() * 1000;
                    double percentagePlayed = ((currentPosition / totalDuration) * 100L);
                    holder.itemBinding.setProgress((int) percentagePlayed);
                } else {
                    AppCommonMethod.railBadgeVisibility(holder.itemBinding.llContinueProgress, false);
                }

                if (contentType.equalsIgnoreCase(AppConstants.VOD)) {

                    if (contentsItem.getPosterURL() != null && !contentsItem.getPosterURL().equalsIgnoreCase("")) {
                        ImageHelper.getInstance(mContext)
                                .loadImageTo(holder.itemBinding.itemImage, AppCommonMethod.getListLDSImage(contentsItem.getPosterURL(), mContext));
                    }

                    holder.itemBinding.itemImage.setOnClickListener(view -> {
                        if (SystemClock.elapsedRealtime() - mLastClickTime < 2000) {
                            return;
                        }
                        mLastClickTime = SystemClock.elapsedRealtime();

                        //    showHideProgress(holder.itemBinding.itemImage, holder.itemBinding.progressBar);
                        try {
                            if ((itemsList.get(i).getAssetType()) != null) {
                                if (itemsList.get(i).getAssetType().equalsIgnoreCase("EPISODE")) {
                                    new ActivityLauncher(mContext).episodeScreen(mContext, EpisodeActivity.class, itemsList.get(i).getId(), "0", contentsItem.isPremium());
                                } else {
                                    new ActivityLauncher(mContext).detailScreen(mContext, InstructorActivity.class, itemsList.get(i).getId(), "0", contentsItem.isPremium());
                                }
                            }
                        } catch (Exception e) {
                            new ActivityLauncher(mContext).detailScreen(mContext, InstructorActivity.class, itemsList.get(i).getId(), "0", contentsItem.isPremium());
                        }

                    });

                } else if (contentType.equalsIgnoreCase(AppConstants.SERIES)) {
                    holder.itemBinding.itemImage.setOnClickListener(view -> {
                        if (SystemClock.elapsedRealtime() - mLastClickTime < 2000) {
                            return;
                        }
                        mLastClickTime = SystemClock.elapsedRealtime();
                        //   showHideProgress( holder.itemBinding.itemImage,holder.itemBinding.progressBar);
                        new ActivityLauncher(mContext).seriesDetailScreen(mContext, SeriesDetailActivity.class, itemsList.get(i).getId());

                    });

                    ImageHelper.getInstance(holder.itemBinding.itemImage.getContext())
                            .loadImageTo(holder.itemBinding.itemImage, AppCommonMethod.getImageUrl(AppConstants.SERIES, "POSTER_LANDSCAPE") + itemsList.get(i).getPosterURL());


                } else if (contentType.equalsIgnoreCase(AppConstants.CAST_AND_CREW)) {

                    ImageHelper.getInstance(holder.itemBinding.itemImage.getContext())
                            .loadImageTo(holder.itemBinding.itemImage, AppCommonMethod.getImageUrl(AppConstants.CAST_AND_CREW, "POSTER_LANDSCAPE") + itemsList.get(i).getPosterURL());

                } else if (contentType.equalsIgnoreCase(AppConstants.GENRE)) {

                    ImageHelper.getInstance(holder.itemBinding.itemImage.getContext())
                            .loadImageTo(holder.itemBinding.itemImage, AppCommonMethod.getImageUrl(AppConstants.GENRE, "POSTER_LANDSCAPE") + seasonItems.get(i).getLandscapeImage());

                }

            }
        } else if (seasonItems.size() > 0) {

            ItemsItem sItem = seasonItems.get(i);
//            if (sItem.isPremium()) {
//                holder.itemBinding.flExclusive.setVisibility(View.VISIBLE);
//            } else {
//                holder.itemBinding.flExclusive.setVisibility(View.GONE);
//            }
//
//            if (sItem.isNew()) {
//                holder.itemBinding.flNew.setVisibility(View.VISIBLE);
//            } else {
//                holder.itemBinding.flNew.setVisibility(View.GONE);
//            }
            ImageHelper.getInstance(holder.itemBinding.itemImage.getContext())
                    .loadImageTo(holder.itemBinding.itemImage, AppCommonMethod.urlPoints + AppConstants.FILTER + AppConstants.QUALITY + "(100):format(webP):maxbytes(800)" + AppConstants.VIDEO_IMAGE_BASE_KEY + seasonItems.get(i).getLandscapeImage());


            holder.itemBinding.itemImage.setOnClickListener(view -> {
                PrintLogging.printLog("", seasonItems.get(i).isPremium() + "seasonItems");
                if (SystemClock.elapsedRealtime() - mLastClickTime < 2000) {
                    return;
                }
                mLastClickTime = SystemClock.elapsedRealtime();

                // showHideProgress( holder.itemBinding.itemImage, holder.itemBinding.progressBar);
                if (!StringUtils.isNullOrEmptyOrZero(sItem.getVideoType())) {

                    if (sItem.getVideoType().equalsIgnoreCase("EPISODE")) {
                        new ActivityLauncher(mContext).episodeScreen(mContext, EpisodeActivity.class, sItem.getId(), "", sItem.isPremium());
                    } else {
                        new ActivityLauncher(mContext).detailScreen(mContext, InstructorActivity.class, sItem.getId(), "", sItem.isPremium());
                    }
                }

            });

        }
    }

    @Override
    public int getItemCount() {
        int itemCount = 0;
        if (itemsList.size() > 0) {
            itemCount = itemsList.size();
        } else if (seasonItems.size() > 0) {
            itemCount = seasonItems.size();
        } else if (continuelist.size() > 0) {
            itemCount = continuelist.size();
        }
        return itemCount;
    }

    public class SingleItemRowHolder extends RecyclerView.ViewHolder {

        PosterLandscapeItemBinding itemBinding;

        SingleItemRowHolder(PosterLandscapeItemBinding itemBinding) {
            super(itemBinding.getRoot());
            this.itemBinding = itemBinding;
        }
    }

}

