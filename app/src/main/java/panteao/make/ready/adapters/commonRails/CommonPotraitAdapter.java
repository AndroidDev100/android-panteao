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
import panteao.make.ready.beanModelV3.playListModelV2.Thumbnail;
import panteao.make.ready.beanModelV3.uiConnectorModelV2.EnveuVideoItemBean;
import panteao.make.ready.databinding.PotraitItemBinding;
import panteao.make.ready.enums.KalturaImageType;
import panteao.make.ready.utils.commonMethods.AppCommonMethod;
import panteao.make.ready.utils.config.ImageLayer;
import panteao.make.ready.utils.constants.AppConstants;
import panteao.make.ready.utils.helpers.ImageHelper;
import panteao.make.ready.utils.helpers.intentlaunchers.ActivityLauncher;
import panteao.make.ready.utils.helpers.ksPreferenceKeys.KsPreferenceKeys;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;


public class CommonPotraitAdapter extends RecyclerView.Adapter<CommonPotraitAdapter.SingleItemRowHolder> {

    private final String contentType;
    private final List<EnveuVideoItemBean> itemsList;
    private final Activity mContext;
    private final ItemClickListener listener;
    int viewType;
    private long mLastClickTime = 0;
    private final int itemWidth;
    private final int itemHeight;
    private final ArrayList<CommonContinueRail> continuelist;
    private boolean isContinueList;
    private final boolean isLogin;
    private final KsPreferenceKeys preference;
    final BaseCategory baseCategory;

    public CommonPotraitAdapter(Activity context, List<EnveuVideoItemBean> itemsList, String contentType, ArrayList<CommonContinueRail> continuelist, ItemClickListener listener, BaseCategory baseCat) {
        this.itemsList = itemsList;
        this.mContext = context;
        this.listener = listener;
        this.contentType = contentType;
        this.continuelist = continuelist;
        this.baseCategory=baseCat;
        if (this.continuelist != null) {
            isContinueList = this.continuelist.size() > 0;
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
        itemHeight = (itemWidth * 16) / 9;
    }

    public void notifydata(List<EnveuVideoItemBean> i) {
        this.itemsList.addAll(i);
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public SingleItemRowHolder onCreateViewHolder(@NonNull ViewGroup parent, int i) {
        PotraitItemBinding binding = PotraitItemBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false);
        return new SingleItemRowHolder();
    }

    @Override
    public void onBindViewHolder(@NonNull SingleItemRowHolder holder, int position) {
        holder.potraitItemBinding.itemImage.getLayoutParams().width = itemWidth;
        holder.potraitItemBinding.itemImage.getLayoutParams().height = itemHeight;


        if (itemsList.size() > 0) {

            try {
                AppCommonMethod.handleTags(itemsList.get(position).getIsVIP(),itemsList.get(position).getIsNewS(),
                        holder.potraitItemBinding.flExclusive,holder.potraitItemBinding.flNew,holder.potraitItemBinding.flEpisode,holder.potraitItemBinding.flNewMovie,itemsList.get(position).getAssetType());

            }catch (Exception ignored){

            }

            try {
                AppCommonMethod.handleTitleDesc(holder.potraitItemBinding.titleLayout,holder.potraitItemBinding.tvTitle,holder.potraitItemBinding.tvDescription,baseCategory);
                holder.potraitItemBinding.tvTitle.setText(itemsList.get(position).getTitle());
                holder.potraitItemBinding.tvDescription.setText(itemsList.get(position).getDescription());
            }catch (Exception ignored){

            }

            holder.potraitItemBinding.llContinueProgress.setVisibility(View.GONE);
            holder.potraitItemBinding.ivContinuePlay.setVisibility(View.GONE);
            EnveuVideoItemBean contentsItem = itemsList.get(position);
            if (contentsItem != null) {
                HashMap<String, Thumbnail> crousalImages = itemsList.get(position).getImages();
                KalturaImageType imageType = KalturaImageType.LANDSCAPE;
                contentsItem.setPosterURL(ImageLayer.getInstance().getFilteredImage(crousalImages, imageType, 320, 180));

                holder.potraitItemBinding.setPlaylistItem(contentsItem);


//                if (preference.getAppLanguage().equalsIgnoreCase("English")){
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
                            listener.onRowItemClicked();
                        }
                    });
                    /*ImageHelper.getInstance(mContext)
                            .loadImageTo(holder.potraitItemBinding.itemImage, contentsItem.getPosterURL());
                    holder.potraitItemBinding.tvTitle.setText(contentsItem.getTitle());*/

                    if (contentsItem.getPosterURL() != null && !contentsItem.getPosterURL().equalsIgnoreCase("")) {

                        ImageHelper.getInstance(mContext).loadImageTo(holder.potraitItemBinding.itemImage, AppCommonMethod.getListPRImage(contentsItem.getPosterURL(), mContext));

                    }


                    /*holder.potraitItemBinding.tvTitle.setText(contentsItem.getTitle());
                    ImageHelper.getInstance(mContext)
                            .loadImageTo(holder.potraitItemBinding.itemImage, contentsItem.getPosterURL());
                    holder.potraitItemBinding.itemImage.setOnClickListener(view -> {
                        if (SystemClock.elapsedRealtime() - mLastClickTime < 1200) {
                            return;
                        }
                        mLastClickTime = SystemClock.elapsedRealtime();

                        try {
                            if ((contentsItem.getAssetType()) != null) {
                                if (contentsItem.getAssetType().equalsIgnoreCase("EPISODE")) {
                                    new ActivityLauncher(mContext).episodeScreen(mContext, EpisodeActivity.class, contentsItem.getId(), "", contentsItem.isPremium());
                                } else {
                                    new ActivityLauncher(mContext).detailScreen(mContext, DetailActivity.class, contentsItem.getId(), "", contentsItem.isPremium());
                                }
                            }
                        } catch (Exception e) {
                            new ActivityLauncher(mContext).detailScreen(mContext, DetailActivity.class, contentsItem.getId(), "", contentsItem.isPremium());

                        }
                    });*/

                } else if (contentType.equalsIgnoreCase(AppConstants.SERIES)) {

                    holder.potraitItemBinding.itemImage.setOnClickListener(view -> {
                        if (SystemClock.elapsedRealtime() - mLastClickTime < 1200) {
                            return;
                        }
                        mLastClickTime = SystemClock.elapsedRealtime();

                        new ActivityLauncher(mContext).seriesDetailScreen(mContext, SeriesDetailActivity.class, contentsItem.getId());
                    });
                    //holder.potraitItemBinding.tvTitle.setText(contentsItem.getTitle());
                    try {
                        AppCommonMethod.handleTitleDesc(holder.potraitItemBinding.titleLayout,holder.potraitItemBinding.tvTitle,holder.potraitItemBinding.tvDescription,baseCategory);
                        holder.potraitItemBinding.tvTitle.setText(itemsList.get(position).getTitle());
                        holder.potraitItemBinding.tvDescription.setText(itemsList.get(position).getDescription());
                    }catch (Exception ignored){

                    }
                    ImageHelper.getInstance(mContext)
                            .loadImageTo(holder.potraitItemBinding.itemImage, AppCommonMethod.getImageUrl(AppConstants.SERIES, "POTRAIT") + contentsItem.getPosterURL());


                } else if (contentType.equalsIgnoreCase(AppConstants.CAST_AND_CREW)) {
                   // holder.potraitItemBinding.tvTitle.setText(contentsItem.getTitle());
                    try {
                        AppCommonMethod.handleTitleDesc(holder.potraitItemBinding.titleLayout,holder.potraitItemBinding.tvTitle,holder.potraitItemBinding.tvDescription,baseCategory);
                        holder.potraitItemBinding.tvTitle.setText(itemsList.get(position).getTitle());
                        holder.potraitItemBinding.tvDescription.setText(itemsList.get(position).getDescription());
                    }catch (Exception ignored){

                    }
                    ImageHelper.getInstance(mContext)
                            .loadImageTo(holder.potraitItemBinding.itemImage, AppCommonMethod.getImageUrl(AppConstants.CAST_AND_CREW, "POTRAIT") + contentsItem.getPosterURL());

                } else if (contentType.equalsIgnoreCase(AppConstants.GENRE)) {
                    ImageHelper.getInstance(mContext)
                            .loadImageTo(holder.potraitItemBinding.itemImage, AppCommonMethod.getImageUrl(AppConstants.GENRE, "POTRAIT") + contentsItem.getPosterURL());
                }
            }
        } else if (continuelist.size() > 0) {
            if (isLogin) {
                holder.potraitItemBinding.llContinueProgress.setVisibility(View.VISIBLE);
                holder.potraitItemBinding.ivContinuePlay.setVisibility(View.VISIBLE);
                holder.potraitItemBinding.flNew.setVisibility(View.GONE);

//                if (continuelist.get(position).getUserAssetDetail().isPremium()) {
//                    holder.potraitItemBinding.flExclusive.setVisibility(View.VISIBLE);
//                } else {
//                    holder.potraitItemBinding.flExclusive.setVisibility(View.GONE);
//                }
                try {
                    ImageHelper.getInstance(mContext)
                            .loadImageTo(holder.potraitItemBinding.itemImage, AppCommonMethod.getImageUrl(AppConstants.VOD, "POTRAIT") + continuelist.get(position).getUserAssetDetail().getPortraitImage());
                } catch (Exception e) {

                }
                holder.potraitItemBinding.itemImage.setOnClickListener(v -> {
                    if (isLogin) {
                        if (continuelist.size() > 0 && (continuelist.get(position).getUserAssetDetail().getAssetType()) != null) {
                           /* if (continuelist.get(position).getUserAssetDetail().getAssetType().equalsIgnoreCase("EPISODE")) {
                                AppCommonMethod.launchDetailScreen(mContext, 0l, AppConstants.Episode, continuelist.get(position).getUserAssetDetail().getId(), String.valueOf(continuelist.get(position).getUserAssetStatus().getPosition()), continuelist.get(position).getUserAssetDetail().isPremium());
                            } else {
                                AppCommonMethod.launchDetailScreen(mContext, 0l, AppConstants.Video, continuelist.get(position).getUserAssetDetail().getId(), String.valueOf(continuelist.get(position).getUserAssetStatus().getPosition()), continuelist.get(position).getUserAssetDetail().isPremium());
                            }*/
                            AppCommonMethod.launchDetailScreen(mContext, "", continuelist.get(position).getUserAssetDetail().getAssetType(), continuelist.get(position).getUserAssetDetail().getId(), String.valueOf(continuelist.get(position).getUserAssetStatus().getPosition()), continuelist.get(position).getUserAssetDetail().isPremium(),null);

                            AppCommonMethod.trackFcmEvent("Content Screen","", mContext);

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
        final PotraitItemBinding potraitItemBinding;

        SingleItemRowHolder() {
            super(potraitItemBind.getRoot());
            potraitItemBinding = potraitItemBind;
        }
    }


}

