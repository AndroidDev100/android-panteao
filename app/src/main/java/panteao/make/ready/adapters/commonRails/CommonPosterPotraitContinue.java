package panteao.make.ready.adapters.commonRails;

import android.app.Activity;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import panteao.make.ready.beanModel.ContinueRailModel.CommonContinueRail;
import panteao.make.ready.R;
import panteao.make.ready.databinding.PosterPotraitItemBinding;
import panteao.make.ready.utils.commonMethods.AppCommonMethod;
import panteao.make.ready.utils.constants.AppConstants;
import panteao.make.ready.utils.helpers.ImageHelper;
import panteao.make.ready.utils.helpers.ksPreferenceKeys.KsPreferenceKeys;


import java.util.ArrayList;

public class CommonPosterPotraitContinue extends RecyclerView.Adapter<CommonPosterPotraitContinue.SingleItemRowHolder> {

    private final String contentType;
    private final Activity mContext;
    private long mLastClickTime = 0;
    private int itemWidth;
    private int itemHeight;
    private ArrayList<CommonContinueRail> continuelist;
    private boolean isLogin;
    private KsPreferenceKeys preference;

    public CommonPosterPotraitContinue(Activity context, String contentType, ArrayList<CommonContinueRail> continuelist) {
        this.mContext = context;
        this.contentType = contentType;
        this.continuelist = continuelist;
        preference = KsPreferenceKeys.getInstance();
        isLogin = preference.getAppPrefLoginStatus();
        int num = 4;
        boolean tabletSize = mContext.getResources().getBoolean(R.bool.isTablet);
        if (tabletSize) {
            if (mContext.getResources().getConfiguration().orientation == 2)
                num = 6;
        }
        DisplayMetrics displaymetrics = new DisplayMetrics();
        (mContext).getWindowManager().getDefaultDisplay().getMetrics(displaymetrics);
        //if you need three fix imageview in width
        itemWidth = (displaymetrics.widthPixels - 60) / num;
        itemHeight = (int) (itemWidth * 3) / 2;
    }


    @NonNull
    @Override
    public CommonPosterPotraitContinue.SingleItemRowHolder onCreateViewHolder(@NonNull ViewGroup parent, int i) {
        PosterPotraitItemBinding binding = DataBindingUtil.inflate(
                LayoutInflater.from(parent.getContext()),
                R.layout.poster_potrait_item, parent, false);
        return new CommonPosterPotraitContinue.SingleItemRowHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull CommonPosterPotraitContinue.SingleItemRowHolder holder, int i) {

        holder.itemBinding.itemImage.getLayoutParams().width = itemWidth;
        holder.itemBinding.itemImage.getLayoutParams().height = itemHeight;
        holder.itemBinding.llContinueProgress.setVisibility(View.GONE);
        holder.itemBinding.ivContinuePlay.setVisibility(View.GONE);
        if (continuelist.size() > 0) {
            if (isLogin) {
                holder.itemBinding.llContinueProgress.setVisibility(View.VISIBLE);
                holder.itemBinding.ivContinuePlay.setVisibility(View.VISIBLE);
                holder.itemBinding.flNew.setVisibility(View.GONE);

//                if (continuelist.get(i).getUserAssetDetail().isPremium()) {
//                    holder.itemBinding.flExclusive.setVisibility(View.VISIBLE);
//                } else {
//                    holder.itemBinding.flExclusive.setVisibility(View.GONE);
//                }

                ImageHelper.getInstance(holder.itemBinding.itemImage.getContext())
                        .loadImageTo(holder.itemBinding.itemImage, AppCommonMethod.getImageUrl(AppConstants.VOD, "POTRIAT") + continuelist.get(i).getUserAssetDetail().getPosterPortraitImage());


                holder.itemBinding.itemImage.setOnClickListener(v -> {
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
                holder.itemBinding.llContinueProgress.setVisibility(View.GONE);
                holder.itemBinding.ivContinuePlay.setVisibility(View.GONE);
            }
        }

    }



    @Override
    public int getItemCount() {
        int itemCount = 0;
        itemCount = continuelist.size();
        return itemCount;
    }

    public class SingleItemRowHolder extends RecyclerView.ViewHolder {

        PosterPotraitItemBinding itemBinding;

        SingleItemRowHolder(PosterPotraitItemBinding itemBinding) {
            super(itemBinding.getRoot());
            this.itemBinding = itemBinding;
        }
    }

}