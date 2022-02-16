package panteao.make.ready.adapters.commonRails;


import android.app.Activity;
import android.os.SystemClock;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import panteao.make.ready.activities.listing.callback.ItemClickListener;
import panteao.make.ready.activities.series.ui.SeriesDetailActivity;
import panteao.make.ready.beanModel.responseModels.landingTabResponses.railData.ContentsItem;
import panteao.make.ready.R;
import panteao.make.ready.beanModelV3.uiConnectorModelV2.EnveuVideoItemBean;
import panteao.make.ready.databinding.SquareListingItemBinding;
import panteao.make.ready.utils.commonMethods.AppCommonMethod;
import panteao.make.ready.utils.constants.AppConstants;
import panteao.make.ready.utils.helpers.ImageHelper;
import panteao.make.ready.utils.helpers.intentlaunchers.ActivityLauncher;

import java.util.List;


public class SquareListingAdapter extends RecyclerView.Adapter<SquareListingAdapter.SingleItemRowHolder> {

    private final String contentType;
    private final List<EnveuVideoItemBean> itemsList;
    private final Activity mContext;
    final ItemClickListener listener;
    private long mLastClickTime = 0;

    public SquareListingAdapter(Activity context, List<EnveuVideoItemBean> itemsList, String contentType, ItemClickListener callBack) {
        this.itemsList = itemsList;
        this.mContext = context;
        this.contentType = contentType;
        listener = callBack;
    }

    public void notifydata(List<EnveuVideoItemBean> i) {

        this.itemsList.addAll(i);
        notifyDataSetChanged();

    }

    @NonNull
    @Override
    public SingleItemRowHolder onCreateViewHolder(@NonNull ViewGroup parent, int i) {
        SquareListingItemBinding binding = DataBindingUtil.inflate(
                LayoutInflater.from(parent.getContext()),
                R.layout.square_listing_item, parent, false);
        return new SingleItemRowHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull SingleItemRowHolder holder, int i) {
        if (itemsList.size() > 0) {
            EnveuVideoItemBean contentsItem = itemsList.get(i);
            if (contentsItem != null) {
                holder.squareItemBinding.setPlaylistItem(contentsItem);

                try {
                    AppCommonMethod.handleTags(itemsList.get(i).getIsVIP(),itemsList.get(i).getIsNewS(),
                            holder.squareItemBinding.flExclusive,holder.squareItemBinding.flNew,holder.squareItemBinding.flEpisode,holder.squareItemBinding.flNewMovie,itemsList.get(i).getAssetType());

                }catch (Exception ignored){

                }

//                if (contentsItem.isPremium()) {
//                    holder.squareItemBinding.flExclusive.setVisibility(View.VISIBLE);
//                } else {
//                    holder.squareItemBinding.flExclusive.setVisibility(View.GONE);
//                }
//
//                if (contentsItem.isNew()) {
//                    holder.squareItemBinding.flNew.setVisibility(View.VISIBLE);
//                } else {
//                    holder.squareItemBinding.flNew.setVisibility(View.GONE);
//                }

                holder.squareItemBinding.tvTitle.setText(itemsList.get(i).getTitle());
                if (contentType.equalsIgnoreCase(AppConstants.VOD)) {
                    holder.squareItemBinding.itemImage.setOnClickListener(view -> {
                        listener.onRowItemClicked();
                    });

                    if (itemsList.get(i).getPosterURL() != null && !itemsList.get(i).getPosterURL().equalsIgnoreCase("")) {
                        ImageHelper.getInstance(mContext).loadImageTo(holder.squareItemBinding.itemImage, AppCommonMethod.getListSQRImage(itemsList.get(i).getPosterURL(), mContext));
                    }
                    //Glide.with(mContext).load(itemsList.get(i).getPosterURL()).into(holder.squareItemBinding.itemImage);
                } else if (contentType.equalsIgnoreCase(AppConstants.SERIES)) {
                    Glide.with(mContext).load(AppCommonMethod.getImageUrl(AppConstants.SERIES, "SQUARE") + itemsList.get(i).getPosterURL()).into(holder.squareItemBinding.itemImage);
                    holder.squareItemBinding.itemImage.setOnClickListener(view -> {
                        if (SystemClock.elapsedRealtime() - mLastClickTime < 1200) {
                            return;
                        }
                        mLastClickTime = SystemClock.elapsedRealtime();
                        new ActivityLauncher(mContext).seriesDetailScreen(mContext, SeriesDetailActivity.class, itemsList.get(i).getId());

                    });

                } else if (contentType.equalsIgnoreCase(AppConstants.CAST_AND_CREW)) {
                    Glide.with(mContext).load(AppCommonMethod.getImageUrl(AppConstants.CAST_AND_CREW, "SQUARE") + itemsList.get(i).getPosterURL()).into(holder.squareItemBinding.itemImage);

                } else if (contentType.equalsIgnoreCase(AppConstants.GENRE)) {
                    Glide.with(mContext).load(AppCommonMethod.getImageUrl(AppConstants.GENRE, "SQUARE") + itemsList.get(i).getPosterURL()).into(holder.squareItemBinding.itemImage);
                }
            }
        }
    }


    @Override
    public int getItemCount() {
        return (null != itemsList ? itemsList.size() : 0);
    }

    public interface SquareListingListener {
        void onItemClicked(ContentsItem itemValue);
    }

    public class SingleItemRowHolder extends RecyclerView.ViewHolder {

        final SquareListingItemBinding squareItemBinding;

        SingleItemRowHolder(SquareListingItemBinding squareItemBind) {
            super(squareItemBind.getRoot());
            squareItemBinding = squareItemBind;

        }

    }


}

