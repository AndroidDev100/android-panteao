package panteao.make.ready.adapters.commonRails;

import android.app.Activity;
import android.os.SystemClock;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.make.baseCollection.baseCategoryModel.BaseCategory;
import panteao.make.ready.activities.listing.callback.ItemClickListener;
import panteao.make.ready.activities.series.ui.SeriesDetailActivity;
import panteao.make.ready.beanModel.ContinueRailModel.CommonContinueRail;
import panteao.make.ready.R;
import panteao.make.ready.beanModelV3.uiConnectorModelV2.EnveuVideoItemBean;
import panteao.make.ready.databinding.PotraitTwoItemBinding;
import panteao.make.ready.utils.commonMethods.AppCommonMethod;
import panteao.make.ready.utils.constants.AppConstants;
import panteao.make.ready.utils.helpers.ImageHelper;

import panteao.make.ready.utils.helpers.intentlaunchers.ActivityLauncher;
import panteao.make.ready.utils.helpers.ksPreferenceKeys.KsPreferenceKeys;

import java.util.ArrayList;
import java.util.List;

public class CommonPotraitTwoAdapter extends RecyclerView.Adapter<CommonPotraitTwoAdapter.SingleItemRowHolder> {

    private final String contentType;
    private final List<EnveuVideoItemBean> itemsList;
    private final Activity mContext;
    private final ItemClickListener listener;
    int viewType;
    private long mLastClickTime = 0;
    private int itemWidth;
    private int itemHeight;
    private ArrayList<CommonContinueRail> continuelist;
    private boolean isContinueList;
    private boolean isLogin;
    private KsPreferenceKeys preference;
    BaseCategory baseCategory;

    public CommonPotraitTwoAdapter(Activity context, List<EnveuVideoItemBean> itemsList, String contentType, ArrayList<CommonContinueRail> continuelist, int view, ItemClickListener listener, BaseCategory baseCat) {
        this.itemsList = itemsList;
        this.mContext = context;
        this.listener = listener;
        this.contentType = contentType;
        this.continuelist = continuelist;
        this.baseCategory=baseCat;
        if (this.continuelist != null) {
            if (this.continuelist.size() > 0)
                isContinueList = true;
            else
                isContinueList = false;
        }

        int num = 3;
        boolean tabletSize = mContext.getResources().getBoolean(R.bool.isTablet);
        if (tabletSize) {
            //landscape
            if (mContext.getResources().getConfiguration().orientation == 2)
                num = 6;
            else
                num = 5;
        }
        preference = KsPreferenceKeys.getInstance();
        isLogin = preference.getAppPrefLoginStatus();
        DisplayMetrics displaymetrics = new DisplayMetrics();
        (mContext).getWindowManager().getDefaultDisplay().getMetrics(displaymetrics);

        //if you need three fix imageview in width
        itemWidth = (displaymetrics.widthPixels) / num;
        itemHeight = (int) (itemWidth * 16) / 9;
    }

    public void notifydata(List<EnveuVideoItemBean> i) {
        this.itemsList.addAll(i);
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public SingleItemRowHolder onCreateViewHolder(@NonNull ViewGroup parent, int i) {
        PotraitTwoItemBinding binding = PotraitTwoItemBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false);
        return new SingleItemRowHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull SingleItemRowHolder holder, int i) {
        // holder.potraitItemBinding.itemImage.getLayoutParams().width = itemWidth;
        // holder.potraitItemBinding.itemImage.getLayoutParams().height = itemHeight;


        if (itemsList.size() > 0) {

            try {
                AppCommonMethod.handleTags(itemsList.get(i).getIsVIP(),itemsList.get(i).getIsNewS(),
                        holder.potraitItemBinding.flExclusive,holder.potraitItemBinding.flNew,holder.potraitItemBinding.flEpisode,holder.potraitItemBinding.flNewMovie,itemsList.get(i).getAssetType());

            }catch (Exception ignored){

            }

            try {
                AppCommonMethod.handleTitleDesc(holder.potraitItemBinding.titleLayout,holder.potraitItemBinding.tvTitle,holder.potraitItemBinding.tvDescription,baseCategory);
                holder.potraitItemBinding.tvTitle.setText(itemsList.get(i).getTitle());
                holder.potraitItemBinding.tvDescription.setText(itemsList.get(i).getDescription());
            }catch (Exception ignored){

            }

            holder.potraitItemBinding.llContinueProgress.setVisibility(View.GONE);
            holder.potraitItemBinding.ivContinuePlay.setVisibility(View.GONE);
            EnveuVideoItemBean contentsItem = itemsList.get(i);
            if (contentsItem != null) {

                holder.potraitItemBinding.setPlaylistItem(contentsItem);

//                if (contentsItem.isPremium()) {
//                    holder.potraitItemBinding.flExclusive.setVisibility(View.VISIBLE);
//
//                } else {
//                    holder.potraitItemBinding.flExclusive.setVisibility(View.GONE);
//                }
//
//                if (contentsItem.isNew()) {
//                    holder.potraitItemBinding.flNew.setVisibility(View.VISIBLE);
//                } else {
//                    holder.potraitItemBinding.flNew.setVisibility(View.GONE);
//                }


                if (contentType.equalsIgnoreCase(AppConstants.VOD)) {

                    holder.potraitItemBinding.itemImage.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            listener.onRowItemClicked(itemsList.get(i), i);
                        }
                    });
                    /*ImageHelper.getInstance(mContext)
                            .loadImageTo(holder.potraitItemBinding.itemImage, itemsList.get(i).getPosterURL());
                    holder.potraitItemBinding.tvTitle.setText(itemsList.get(i).getTitle());*/

                    if (itemsList.get(i).getPosterURL() != null && !itemsList.get(i).getPosterURL().equalsIgnoreCase("")) {
                        ImageHelper.getInstance(mContext).loadImageTo(holder.potraitItemBinding.itemImage, AppCommonMethod.getListPRImage(itemsList.get(i).getPosterURL(), mContext));
                    }

                    /*holder.potraitItemBinding.tvTitle.setText(itemsList.get(i).getTitle());
                    ImageHelper.getInstance(mContext)
                            .loadImageTo(holder.potraitItemBinding.itemImage, itemsList.get(i).getPosterURL());
                    holder.potraitItemBinding.itemImage.setOnClickListener(view -> {
                        if (SystemClock.elapsedRealtime() - mLastClickTime < 1200) {
                            return;
                        }
                        mLastClickTime = SystemClock.elapsedRealtime();

                        try {
                            if ((itemsList.get(i).getAssetType()) != null) {
                                if (itemsList.get(i).getAssetType().equalsIgnoreCase("EPISODE")) {
                                    new ActivityLauncher(mContext).episodeScreen(mContext, EpisodeActivity.class, itemsList.get(i).getId(), "", contentsItem.isPremium());
                                } else {
                                    new ActivityLauncher(mContext).detailScreen(mContext, DetailActivity.class, itemsList.get(i).getId(), "", contentsItem.isPremium());
                                }
                            }
                        } catch (Exception e) {
                            new ActivityLauncher(mContext).detailScreen(mContext, DetailActivity.class, itemsList.get(i).getId(), "", contentsItem.isPremium());

                        }
                    });*/

                } else if (contentType.equalsIgnoreCase(AppConstants.SERIES)) {

                    holder.potraitItemBinding.itemImage.setOnClickListener(view -> {
                        if (SystemClock.elapsedRealtime() - mLastClickTime < 1200) {
                            return;
                        }
                        mLastClickTime = SystemClock.elapsedRealtime();

                        new ActivityLauncher(mContext).seriesDetailScreen(mContext, SeriesDetailActivity.class, itemsList.get(i).getId());
                    });
                    holder.potraitItemBinding.tvTitle.setText(itemsList.get(i).getTitle());
                    ImageHelper.getInstance(mContext)
                            .loadImageTo(holder.potraitItemBinding.itemImage, AppCommonMethod.getImageUrl(AppConstants.SERIES, "POTRAIT") + itemsList.get(i).getPosterURL());


                } else if (contentType.equalsIgnoreCase(AppConstants.CAST_AND_CREW)) {
                    holder.potraitItemBinding.tvTitle.setText(itemsList.get(i).getTitle());
                    ImageHelper.getInstance(mContext)
                            .loadImageTo(holder.potraitItemBinding.itemImage, AppCommonMethod.getImageUrl(AppConstants.CAST_AND_CREW, "POTRAIT") + itemsList.get(i).getPosterURL());

                } else if (contentType.equalsIgnoreCase(AppConstants.GENRE)) {
                    ImageHelper.getInstance(mContext)
                            .loadImageTo(holder.potraitItemBinding.itemImage, AppCommonMethod.getImageUrl(AppConstants.GENRE, "POTRAIT") + itemsList.get(i).getPosterURL());
                }
            }
        } else if (continuelist.size() > 0) {
            if (isLogin) {
                holder.potraitItemBinding.llContinueProgress.setVisibility(View.VISIBLE);
                holder.potraitItemBinding.ivContinuePlay.setVisibility(View.VISIBLE);
                holder.potraitItemBinding.flNew.setVisibility(View.GONE);

//                if (continuelist.get(i).getUserAssetDetail().isPremium()) {
//                    holder.potraitItemBinding.flExclusive.setVisibility(View.VISIBLE);
//                } else {
//                    holder.potraitItemBinding.flExclusive.setVisibility(View.GONE);
//                }
                try {
                    ImageHelper.getInstance(mContext)
                            .loadImageTo(holder.potraitItemBinding.itemImage, AppCommonMethod.getImageUrl(AppConstants.VOD, "POTRAIT") + continuelist.get(i).getUserAssetDetail().getPortraitImage());
                } catch (Exception e) {

                }
                holder.potraitItemBinding.itemImage.setOnClickListener(v -> {
                    if (isLogin) {
                        if (continuelist.size() > 0 && (continuelist.get(i).getUserAssetDetail().getAssetType()) != null) {
                            /*if (continuelist.get(i).getUserAssetDetail().getAssetType().equalsIgnoreCase("EPISODE")) {
                                AppCommonMethod.launchDetailScreen(mContext,0l,AppConstants.Episode, continuelist.get(i).getUserAssetDetail().getId(), String.valueOf(continuelist.get(i).getUserAssetStatus().getPosition()), continuelist.get(i).getUserAssetDetail().isPremium());
                            } else {
                                AppCommonMethod.launchDetailScreen(mContext,0l,AppConstants.Video, continuelist.get(i).getUserAssetDetail().getId(), String.valueOf(continuelist.get(i).getUserAssetStatus().getPosition()), continuelist.get(i).getUserAssetDetail().isPremium());
                            }*/
                            AppCommonMethod.launchDetailScreen(mContext,"",continuelist.get(i).getUserAssetDetail().getAssetType(), continuelist.get(i).getUserAssetDetail().getId(), String.valueOf(continuelist.get(i).getUserAssetStatus().getPosition()), continuelist.get(i).getUserAssetDetail().isPremium());
                        }
                    }
                });

            } else {
                holder.potraitItemBinding.llContinueProgress.setVisibility(View.GONE);
                holder.potraitItemBinding.ivContinuePlay.setVisibility(View.GONE);
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

    public class SingleItemRowHolder extends RecyclerView.ViewHolder {
        final PotraitTwoItemBinding potraitItemBinding;

        SingleItemRowHolder(PotraitTwoItemBinding potraitItemBind) {
            super(potraitItemBind.getRoot());
            potraitItemBinding = potraitItemBind;
        }
    }


}

