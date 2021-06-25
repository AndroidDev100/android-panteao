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

import panteao.make.ready.activities.instructor.ui.InstructorActivity;
import panteao.make.ready.activities.show.ui.EpisodeActivity;
import panteao.make.ready.activities.series.ui.SeriesDetailActivity;
import panteao.make.ready.beanModel.ContinueRailModel.CommonContinueRail;
import panteao.make.ready.beanModel.responseModels.landingTabResponses.railData.ContentsItem;
import panteao.make.ready.R;
import panteao.make.ready.databinding.SquareItemBinding;
import panteao.make.ready.utils.commonMethods.AppCommonMethod;
import panteao.make.ready.utils.constants.AppConstants;
import panteao.make.ready.utils.cropImage.helpers.Logger;
import panteao.make.ready.utils.helpers.ImageHelper;

import panteao.make.ready.utils.helpers.intentlaunchers.ActivityLauncher;
import panteao.make.ready.utils.helpers.ksPreferenceKeys.KsPreferenceKeys;

import java.util.ArrayList;
import java.util.List;


public class SquareCommonAdapter extends RecyclerView.Adapter<SquareCommonAdapter.SingleItemRowHolder> {

    private final List<ContentsItem> itemsList;
    private final Activity mContext;
    private final String contentType;
    private long mLastClickTime = 0;
    private ArrayList<CommonContinueRail> continuelist;
    private KsPreferenceKeys preference;
    private boolean isLogin;
    private boolean isContinueList;
    private int itemWidth;
    private int itemHeight;

    public SquareCommonAdapter(Activity context, List<ContentsItem> itemsList, String contentType, ArrayList<CommonContinueRail> continuelist) {
        this.itemsList = itemsList;
        this.mContext = context;
        this.contentType = contentType;
        this.continuelist = continuelist;
        if (this.continuelist != null) {
            if (this.continuelist.size() > 0)
                isContinueList = true;
            else
                isContinueList = false;
        }
        preference = KsPreferenceKeys.getInstance();
        isLogin = preference.getAppPrefLoginStatus();
        int num = 3;
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
        itemWidth = (displaymetrics.widthPixels - 20) / num;
        itemHeight = itemWidth;

    }

    @NonNull
    @Override
    public SingleItemRowHolder onCreateViewHolder(@NonNull ViewGroup parent, int i) {
        SquareItemBinding binding = DataBindingUtil.inflate(
                LayoutInflater.from(parent.getContext()),
                R.layout.square_item, parent, false);
        return new SingleItemRowHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull SingleItemRowHolder holder, int i) {
        holder.squareItemBinding.itemImage.getLayoutParams().width = itemWidth;
        holder.squareItemBinding.itemImage.getLayoutParams().height = itemHeight;

        if (itemsList.size() > 0) {
            ContentsItem contentsItem = itemsList.get(i);
            if (contentsItem != null) {
                holder.squareItemBinding.llContinueProgress.setVisibility(View.GONE);
                holder.squareItemBinding.ivContinuePlay.setVisibility(View.GONE);

                if (contentsItem.isPremium()) {
                    holder.squareItemBinding.flExclusive.setVisibility(View.VISIBLE);
                } else {
                    holder.squareItemBinding.flExclusive.setVisibility(View.GONE);
                }
                if (contentsItem.isNew()) {
                    holder.squareItemBinding.flNew.setVisibility(View.VISIBLE);
                } else {
                    holder.squareItemBinding.flNew.setVisibility(View.GONE);
                }


                if (contentType.equalsIgnoreCase(AppConstants.VOD)) {
                    holder.squareItemBinding.tvTitle.setText(itemsList.get(i).getTitle());

                    ImageHelper.getInstance(mContext)
                            .loadImageTo(holder.squareItemBinding.itemImage, AppCommonMethod.getImageUrl(AppConstants.VOD, "SQUARE") + itemsList.get(i).getPortraitImage());

                    Logger.e("imageUrl", "imageUrl" + AppCommonMethod.getImageUrl(AppConstants.VOD, "SQUARE") + itemsList.get(i).getPortraitImage());
                    holder.squareItemBinding.itemImage.setOnClickListener(view -> {
                        if (SystemClock.elapsedRealtime() - mLastClickTime < 1200) {
                            return;
                        }
                        mLastClickTime = SystemClock.elapsedRealtime();
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

                    holder.squareItemBinding.itemImage.setOnClickListener(view -> {
                        if (SystemClock.elapsedRealtime() - mLastClickTime < 1200) {
                            return;
                        }
                        mLastClickTime = SystemClock.elapsedRealtime();
                        new ActivityLauncher(mContext).seriesDetailScreen(mContext, SeriesDetailActivity.class, itemsList.get(i).getId());

                    });
                    holder.squareItemBinding.tvTitle.setText(itemsList.get(i).getName());

                    ImageHelper.getInstance(mContext)
                            .loadImageTo(holder.squareItemBinding.itemImage, AppCommonMethod.getImageUrl(AppConstants.SERIES, "SQUARE") + itemsList.get(i).getPicture());
                    Logger.e("imageUrl", "imageUrl" + AppCommonMethod.getImageUrl(AppConstants.VOD, "SQUARE") + itemsList.get(i).getPortraitImage());


                } else if (contentType.equalsIgnoreCase(AppConstants.CAST_AND_CREW)) {
                    holder.squareItemBinding.tvTitle.setText(itemsList.get(i).getName());
                    Logger.e("imageUrl", "imageUrl" + AppCommonMethod.getImageUrl(AppConstants.VOD, "SQUARE") + itemsList.get(i).getPortraitImage());

                    ImageHelper.getInstance(mContext)
                            .loadImageTo(holder.squareItemBinding.itemImage, AppCommonMethod.getImageUrl(AppConstants.CAST_AND_CREW, "SQUARE") + itemsList.get(i).getPicture());

                } else if (contentType.equalsIgnoreCase(AppConstants.GENRE)) {

                    ImageHelper.getInstance(mContext)
                            .loadImageTo(holder.squareItemBinding.itemImage, AppCommonMethod.getImageUrl(AppConstants.GENRE, "SQUARE") + itemsList.get(i).getPicture());
                    Logger.e("imageUrl", "imageUrl" + AppCommonMethod.getImageUrl(AppConstants.VOD, "SQUARE") + itemsList.get(i).getPortraitImage());


                }
            }
        } else if (continuelist.size() > 0) {
            if (isLogin) {
                holder.squareItemBinding.llContinueProgress.setVisibility(View.VISIBLE);
                holder.squareItemBinding.ivContinuePlay.setVisibility(View.VISIBLE);
                holder.squareItemBinding.flNew.setVisibility(View.GONE);

                if (continuelist.get(i).getUserAssetDetail().isPremium()) {
                    holder.squareItemBinding.flExclusive.setVisibility(View.VISIBLE);
                } else {
                    holder.squareItemBinding.flExclusive.setVisibility(View.GONE);
                }
                ImageHelper.getInstance(mContext)
                        .loadImageTo(holder.squareItemBinding.itemImage, AppCommonMethod.getImageUrl(AppConstants.VOD, "SQUARE") + continuelist.get(i).getUserAssetDetail().getPortraitImage());
                holder.squareItemBinding.itemImage.setOnClickListener(v -> {
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
                holder.squareItemBinding.llContinueProgress.setVisibility(View.GONE);
                holder.squareItemBinding.ivContinuePlay.setVisibility(View.GONE);
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

        final SquareItemBinding squareItemBinding;

        SingleItemRowHolder(SquareItemBinding squareItemBind) {
            super(squareItemBind.getRoot());
            squareItemBinding = squareItemBind;

        }

    }

}

