package panteao.make.ready.activities.watchList.adapter;

import android.content.Context;
import android.os.Build;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import panteao.make.ready.R;
import panteao.make.ready.beanModel.watchHistory.ItemsItem;
import panteao.make.ready.databinding.CommonSearchAdapterBinding;
import panteao.make.ready.utils.commonMethods.AppCommonMethod;
import panteao.make.ready.utils.constants.AppConstants;
import panteao.make.ready.utils.cropImage.helpers.Logger;

import panteao.make.ready.utils.helpers.StringUtils;
import panteao.make.ready.utils.helpers.ksPreferenceKeys.KsPreferenceKeys;

import java.util.List;

public class WatchHistoryAdapter extends RecyclerView.Adapter<WatchHistoryAdapter.SingleItemRowHolder> {

    private static final int ITEM = 0;
    private static final int LOADING = 1;
    private static List<ItemsItem> list;
    private final Context context;
    private final KsPreferenceKeys preference;
    private final WatchHistoryAdaperListener listener;


    public WatchHistoryAdapter(Context context, List<ItemsItem> list, WatchHistoryAdaperListener listener) {
        this.context = context;
        this.list = list;
        this.listener = listener;

        preference = KsPreferenceKeys.getInstance();
    }

    @Override
    public int getItemViewType(int position) {
        boolean isLoadingAdded = false;
        return (position == list.size() - 1 && isLoadingAdded) ? LOADING : ITEM;
    }

    public void notifyAdapter(List<ItemsItem> list) {
        this.list.addAll(list);
        notifyDataSetChanged();
    }


   /* public int allCount() {
        return list.size();
    }*/

    @NonNull
    @Override
    public WatchHistoryAdapter.SingleItemRowHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        CommonSearchAdapterBinding itemBinding;
        itemBinding = DataBindingUtil.inflate(
                LayoutInflater.from(viewGroup.getContext()),
                R.layout.common_search_adapter, viewGroup, false);

        return new SingleItemRowHolder(itemBinding);
    }

    @Override
    public void onBindViewHolder(@NonNull SingleItemRowHolder viewHolder, int position) {
        viewHolder.itemBinding.tvTitle.setText(list.get(position).getTitle());
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            viewHolder.itemBinding.tvEpisode.setTextColor(ContextCompat.getColor(context, R.color.greyTextColor));
        } else {
            viewHolder.itemBinding.tvEpisode.setTextColor(context.getResources().getColor(R.color.greyTextColor));
        }

        StringBuilder stringBuilder = new StringBuilder();
        if (list.get(position).getGenres() != null && list.get(position).getGenres().size() > 0) {
            for (int i = 0; i < list.get(position).getGenres().size(); i++) {
                if (i == list.get(position).getGenres().size() - 1) {
                    stringBuilder = stringBuilder.append(list.get(position).getGenres().get(i));

                } else
                    stringBuilder = stringBuilder.append(list.get(position).getGenres().get(i)).append(", ");
            }
        }
        viewHolder.itemBinding.tvEpisode.setText(stringBuilder.toString());

       /* if (list.get(position).getStatus() != null) {
            if (list.get(position).getStatus().equalsIgnoreCase(AppConstants.UNPUBLISHED)) {
                viewHolder.itemBinding.itemImage.setAlpha(.5f);
                viewHolder.itemBinding.tvEpisode.setAlpha(.5f);
                viewHolder.itemBinding.tvTitle.setAlpha(.5f);
                Logger.e("WatchListAdapter", "WatchListAdapter" + list.get(position));

            } else if (list.get(position).getStatus().equalsIgnoreCase(AppConstants.PUBLISHED)) {
                viewHolder.itemBinding.itemImage.setAlpha(1.0f);
                viewHolder.itemBinding.tvEpisode.setAlpha(1.0f);
                viewHolder.itemBinding.tvTitle.setAlpha(1.0f);
            }
        }*/

       /* switch (AppConstants.Video) {
            case AppConstants.Video:
                setImage(list.get(position).getImageLandscape(), viewHolder.itemBinding.itemImage);
                break;
            default:
                break;
        }*/

        viewHolder.itemBinding.clRoot.setOnClickListener(view -> onItemClicked(position));
    }


    private void onItemClicked(int position) {
        try {
            if (list.get(position).getStatus() != null && !list.get(position).getStatus().equalsIgnoreCase(AppConstants.UNPUBLISHED))
                listener.onWatchHistoryItemClicked(list.get(position));
        } catch (Exception e) {
            Logger.e("WatchListAdapter", "WatchListAdapter" + list.get(position));

        }

    }

    private void setImage(String imageKey, ImageView view) {
        try {

            String url1 = preference.getAppPrefCfep();
            if (StringUtils.isNullOrEmpty(url1)) {
                url1 = AppCommonMethod.urlPoints;
                preference.setAppPrefCfep(url1);
            }
            String url2 = AppConstants.VIDEO_IMAGE_BASE_KEY;
            String tranform = "/fit-in/160x90/filters:quality(100):max_bytes(400)";

            StringBuilder stringBuilder = new StringBuilder(url1 + tranform + url2 + imageKey);
            Logger.e("", "imageURL" + stringBuilder.toString());
            Glide.with(context)
                    .asBitmap()
                    .load(stringBuilder.toString())
                    .apply(AppCommonMethod.optionsSearch)
                    .into(view);
        } catch (Exception e) {
            Logger.e("", "" + e.toString());
        }

    }


    @Override
    public int getItemCount() {

        return list.size();
    }

    public interface WatchHistoryAdaperListener {
        void onWatchHistoryItemClicked(ItemsItem itemValue);
    }


    public class SingleItemRowHolder extends RecyclerView.ViewHolder {
        final CommonSearchAdapterBinding itemBinding;

        public SingleItemRowHolder(CommonSearchAdapterBinding binding) {
            super(binding.getRoot());
            this.itemBinding = binding;
        }
    }

}


