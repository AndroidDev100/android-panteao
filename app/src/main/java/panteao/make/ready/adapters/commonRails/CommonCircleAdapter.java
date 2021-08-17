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

import panteao.make.ready.activities.listing.callback.ItemClickListener;
import panteao.make.ready.activities.series.ui.SeriesDetailActivity;
import panteao.make.ready.beanModel.ContinueRailModel.CommonContinueRail;
import panteao.make.ready.beanModel.responseModels.landingTabResponses.railData.ContentsItem;
import panteao.make.ready.R;
import panteao.make.ready.beanModelV3.uiConnectorModelV2.EnveuVideoItemBean;
import panteao.make.ready.databinding.GridCircleItemBinding;
import panteao.make.ready.utils.commonMethods.AppCommonMethod;
import panteao.make.ready.utils.constants.AppConstants;
import panteao.make.ready.utils.helpers.ImageHelper;

import panteao.make.ready.utils.helpers.intentlaunchers.ActivityLauncher;
import panteao.make.ready.utils.helpers.ksPreferenceKeys.KsPreferenceKeys;

import java.util.ArrayList;
import java.util.List;

public class CommonCircleAdapter extends RecyclerView.Adapter<CommonCircleAdapter.SingleItemRowHolder> {

    private final String contentType;
    private final List<EnveuVideoItemBean> itemsList;
    private final Activity mContext;
    private ItemClickListener listener;
    private long mLastClickTime = 0;
    private ArrayList<CommonContinueRail> continuelist;
    private boolean isContinueList;
    private boolean isLogin;
    private KsPreferenceKeys preference;
    private int itemWidth;
    private int itemHeight;

    public CommonCircleAdapter(Activity context, List<EnveuVideoItemBean> itemsList, String contentType, ArrayList<CommonContinueRail> continuelist, ItemClickListener callback) {
        this.itemsList = itemsList;
        this.mContext = context;
        this.contentType = contentType;
        this.continuelist = continuelist;
        listener = callback;
        if (this.continuelist != null) {
            if (this.continuelist.size() > 0)
                isContinueList = true;
            else
                isContinueList = false;
        }
        // isContinueList = null != continuelist ? false : true;
        preference = KsPreferenceKeys.getInstance();
        isLogin = preference.getAppPrefLoginStatus();
        int num = 4;
        boolean tabletSize = mContext.getResources().getBoolean(R.bool.isTablet);
        if (tabletSize) {
            //landscape
            if (mContext.getResources().getConfiguration().orientation == 2)
                num = 6;
            else
                num = 5;
        }
        DisplayMetrics displaymetrics = new DisplayMetrics();
        (mContext).getWindowManager().getDefaultDisplay().getMetrics(displaymetrics);

        //if you need three fix imageview in width
        itemWidth = (displaymetrics.widthPixels) / num;
        itemHeight = itemWidth;
        //this.listener=listener;
    }

    public void notifydata(List<EnveuVideoItemBean> i) {

        this.itemsList.addAll(i);
        notifyDataSetChanged();

    }

    @NonNull
    @Override
    public SingleItemRowHolder onCreateViewHolder(@NonNull ViewGroup parent, int i) {
        GridCircleItemBinding binding = DataBindingUtil.inflate(
                LayoutInflater.from(parent.getContext()),
                R.layout.grid_circle_item, parent, false);
        return new SingleItemRowHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull SingleItemRowHolder holder, int i) {
        // holder.circularItemBinding.itemImage.getLayoutParams().width = itemWidth;
        // holder.circularItemBinding.itemImage.getLayoutParams().height = itemHeight;

        if (itemsList.size() > 0) {
            EnveuVideoItemBean contentsItem = itemsList.get(i);
            if (contentsItem != null) {
                holder.circularItemBinding.llContinueProgress.setVisibility(View.GONE);
                holder.circularItemBinding.ivContinuePlay.setVisibility(View.GONE);
                holder.circularItemBinding.tvTitle.setText(itemsList.get(i).getTitle());
                if (contentType.equalsIgnoreCase(AppConstants.VOD)) {

                    if (contentsItem.getPosterURL() != null && !contentsItem.getPosterURL().equalsIgnoreCase("")) {
                        ImageHelper.getInstance(mContext)
                                .loadCIRImage(holder.circularItemBinding.itemImage, AppCommonMethod.getListCIRCLEImage(contentsItem.getPosterURL(), mContext),null);
                    }

                    holder.circularItemBinding.itemImage.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            listener.onRowItemClicked(itemsList.get(i), i);
                        }
                    });
/*
                    holder.circularItemBinding.itemImage.setOnClickListener(view -> {
                        if (SystemClock.elapsedRealtime() - mLastClickTime < 1200) {
                            return;
                        }
                        mLastClickTime = SystemClock.elapsedRealtime();

                        try {
                            if ((itemsList.get(i).getAssetType()) != null) {
                                if (itemsList.get(i).getAssetType().equalsIgnoreCase("EPISODE")) {
                                    new ActivityLauncher(mContext).episodeScreen(mContext, EpisodeActivity.class, itemsList.get(i).getId(), "0", contentsItem.isPremium());
                                } else {
                                    new ActivityLauncher(mContext).detailScreen(mContext, DetailActivity.class, itemsList.get(i).getId(), "0", contentsItem.isPremium());
                                }
                            }
                        } catch (Exception e) {
                            new ActivityLauncher(mContext).detailScreen(mContext, DetailActivity.class, itemsList.get(i).getId(), "0", contentsItem.isPremium());
                        }

                    });
*/

                } else if (contentType.equalsIgnoreCase(AppConstants.SERIES)) {
                    holder.circularItemBinding.itemImage.setOnClickListener(view -> {
                        if (SystemClock.elapsedRealtime() - mLastClickTime < 1200) {
                            return;
                        }
                        mLastClickTime = SystemClock.elapsedRealtime();
                        new ActivityLauncher(mContext).seriesDetailScreen(mContext, SeriesDetailActivity.class, itemsList.get(i).getId());

                    });
                    ImageHelper.getInstance(mContext)
                            .loadImageTo(holder.circularItemBinding.itemImage, AppCommonMethod.getImageUrl(AppConstants.SERIES, "CIRCLE") + itemsList.get(i).getPosterURL());

                } else if (contentType.equalsIgnoreCase(AppConstants.CAST_AND_CREW)) {

                    ImageHelper.getInstance(mContext)
                            .loadImageTo(holder.circularItemBinding.itemImage, AppCommonMethod.getImageUrl(AppConstants.CAST_AND_CREW, "CIRCLE") + itemsList.get(i).getPosterURL());
                } else if (contentType.equalsIgnoreCase(AppConstants.GENRE)) {

                    ImageHelper.getInstance(mContext)
                            .loadImageTo(holder.circularItemBinding.itemImage, AppCommonMethod.getImageUrl(AppConstants.GENRE, "CIRCLE") + itemsList.get(i).getPosterURL());

                }


            }
        } else if (continuelist.size() > 0) {

            if (isLogin) {
                holder.circularItemBinding.llContinueProgress.setVisibility(View.VISIBLE);
                holder.circularItemBinding.ivContinuePlay.setVisibility(View.VISIBLE);
                holder.circularItemBinding.flNew.setVisibility(View.GONE);

                if (continuelist.get(i).getUserAssetDetail().isPremium()) {
                    holder.circularItemBinding.flExclusive.setVisibility(View.VISIBLE);
                } else {
                    holder.circularItemBinding.flExclusive.setVisibility(View.GONE);
                }
                ImageHelper.getInstance(mContext)
                        .loadImageTo(holder.circularItemBinding.itemImage, AppCommonMethod.getImageUrl(AppConstants.VOD, "CIRCLE") + continuelist.get(i).getUserAssetDetail().getPortraitImage());

                holder.circularItemBinding.itemImage.setOnClickListener(v -> {
                    if (isLogin) {
                        if (continuelist.size() > 0 && (continuelist.get(i).getUserAssetDetail().getAssetType()) != null) {
                            /*if (continuelist.get(i).getUserAssetDetail().getAssetType().equalsIgnoreCase(MediaTypeConstants.getInstance().getEpisode())) {
                                AppCommonMethod.launchDetailScreen(mContext,0l, MediaTypeConstants.getInstance().getEpisode(), continuelist.get(i).getUserAssetDetail().getId(), String.valueOf(continuelist.get(i).getUserAssetStatus().getPosition()), continuelist.get(i).getUserAssetDetail().isPremium());
                            } else {
                                AppCommonMethod.launchDetailScreen(mContext,0l,continuelist.get(i).getUserAssetDetail().getAssetType(), continuelist.get(i).getUserAssetDetail().getId(), String.valueOf(continuelist.get(i).getUserAssetStatus().getPosition()), continuelist.get(i).getUserAssetDetail().isPremium());
                            }*/
                            AppCommonMethod.launchDetailScreen(mContext,"", continuelist.get(i).getUserAssetDetail().getAssetType(), continuelist.get(i).getUserAssetDetail().getId(), String.valueOf(continuelist.get(i).getUserAssetStatus().getPosition()), continuelist.get(i).getUserAssetDetail().isPremium(),null);
                        }
                    }
                });

            } else {
                holder.circularItemBinding.llContinueProgress.setVisibility(View.GONE);
                holder.circularItemBinding.ivContinuePlay.setVisibility(View.GONE);
            }
        }
    }


    @Override
    public int getItemCount() {

        if (isContinueList)
            return (null != continuelist ? continuelist.size() : 0);
        else
            return (null != itemsList ? itemsList.size() : 0);
    }

    public interface CommonCircleListener {
        void onItemClicked(ContentsItem itemValue);
    }

    public class SingleItemRowHolder extends RecyclerView.ViewHolder {

        final GridCircleItemBinding circularItemBinding;

        SingleItemRowHolder(GridCircleItemBinding circularItemBind) {
            super(circularItemBind.getRoot());
            circularItemBinding = circularItemBind;
        }

    }


}
