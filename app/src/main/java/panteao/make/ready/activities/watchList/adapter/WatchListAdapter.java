package panteao.make.ready.activities.watchList.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import panteao.make.ready.R;
import panteao.make.ready.activities.search.ui.ActivitySearch;
import panteao.make.ready.beanModel.allWatchList.ItemsItem;
import panteao.make.ready.databinding.CommonSearchAdapterBinding;
import panteao.make.ready.utils.MediaTypeConstants;
import panteao.make.ready.utils.commonMethods.AppCommonMethod;
import panteao.make.ready.utils.constants.AppConstants;
import panteao.make.ready.utils.cropImage.helpers.Logger;

import panteao.make.ready.utils.helpers.StringUtils;
import panteao.make.ready.utils.helpers.ksPreferenceKeys.KsPreferenceKeys;

import java.util.List;

public class WatchListAdapter extends RecyclerView.Adapter<WatchListAdapter.SingleItemRowHolder> {

    private static final int ITEM = 0;
    private static final int LOADING = 1;
    private final Context context;
    private final KsPreferenceKeys preference;
    private final List<ItemsItem> list;
    private final WatchListAdaperListener listener;
    private final DeleteWatchList deleteWatchList;
    private ActivitySearch itemListener;

    public WatchListAdapter(Context context, List<ItemsItem> list, WatchListAdaperListener listener, DeleteWatchList deleteWatchList) {
        this.context = context;
        this.list = list;
        this.listener = listener;
        this.deleteWatchList = deleteWatchList;
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

    public void deleteItem(int position) {
        this.list.remove(position);
        notifyDataSetChanged();
    }

    public void deleteComment(int id) {
        int posDelete = 0;
        for (int i = 0; i < list.size(); i++) {
            if (list.get(i).getId() == id) {
                posDelete = i;
                break;
            }
        }
        if (posDelete < list.size())
            list.remove(posDelete);
        notifyItemRemoved(posDelete);
        notifyItemRangeChanged(posDelete, list.size());
        notifyDataSetChanged();
    }


    @NonNull
    @Override
    public WatchListAdapter.SingleItemRowHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {

        CommonSearchAdapterBinding itemBinding;
        itemBinding = DataBindingUtil.inflate(
                LayoutInflater.from(viewGroup.getContext()),
                R.layout.common_search_adapter, viewGroup, false);

        return new SingleItemRowHolder(itemBinding);
    }

    @Override
    public void onBindViewHolder(@NonNull SingleItemRowHolder viewHolder, int position) {
        if (list.size() > 0) {
            viewHolder.itemBinding.tvTitle.setText(list.get(position).getTitle());

//            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
//                viewHolder.itemBinding.tvEpisode.setTextColor(ContextCompat.getColor(context, R.color.greyTextColor));
//            } else {
//                viewHolder.itemBinding.tvEpisode.setTextColor(context.getResources().getColor(R.color.greyTextColor));
//            }

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
            viewHolder.itemBinding.clRoot.setOnClickListener(view -> onItemClicked(position));
            viewHolder.itemBinding.flDeleteWatchlist.setVisibility(View.VISIBLE);
            viewHolder.itemBinding.flDeleteWatchlist.setOnClickListener(view -> {
                if (list.size() > 0)
                    deleteWatchList.onDeleteClick(list.get(position));
            });


            /*if (list.get(position).getStatus().equalsIgnoreCase(AppConstants.UNPUBLISHED)) {
                viewHolder.itemBinding.itemImage.setAlpha(.5f);
                viewHolder.itemBinding.tvEpisode.setAlpha(.5f);
                viewHolder.itemBinding.tvTitle.setAlpha(.5f);
            }*/
        /*   viewHolder.itemBinding.clRoot.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View view) {
                    if(list.size()>0)
                    deleteWatchList.onDeleteClick(list.get(position));
                    return false;
                }
            });*/
           if (list.get(position).getContentType().equalsIgnoreCase(MediaTypeConstants.getInstance().getMovie())){
              // setImage(list.get(position).getImageUrl(), AppConstants.VIDEO_IMAGE_BASE_KEY, viewHolder.itemBinding.itemImage);
           }
           else if (list.get(position).getContentType().equalsIgnoreCase(MediaTypeConstants.getInstance().getEpisode())){
             //  setImage(list.get(position).getImageUrl(), AppConstants.VIDEO_IMAGE_BASE_KEY, viewHolder.itemBinding.itemImage);
           }
           else if (list.get(position).getContentType().equalsIgnoreCase(MediaTypeConstants.getInstance().getSeries())){
             //  setImage(list.get(position).getImageUrl(), AppConstants.SERIES_IMAGES_BASE_KEY, viewHolder.itemBinding.itemImage);
           }
/*
            switch (list.get(position).getContentType()) {
                case AppConstants.Video:
                    setImage(list.get(position).getImageUrl(), AppConstants.VIDEO_IMAGE_BASE_KEY, viewHolder.itemBinding.itemImage);
                    break;
                case AppConstants.Movie:
                    setImage(list.get(position).getImageUrl(), AppConstants.VIDEO_IMAGE_BASE_KEY, viewHolder.itemBinding.itemImage);
                    break;
                case AppConstants.Episode:
                    setImage(list.get(position).getImageUrl(), AppConstants.VIDEO_IMAGE_BASE_KEY, viewHolder.itemBinding.itemImage);
                    break;
                case AppConstants.Series:
                    setImage(list.get(position).getImageUrl(), AppConstants.SERIES_IMAGES_BASE_KEY, viewHolder.itemBinding.itemImage);

                    break;
                default:
                    break;
            }
*/
        }
    }


    private void onItemClicked(int position) {

        try {
            if (!list.get(position).getStatus().equalsIgnoreCase(AppConstants.UNPUBLISHED))
                listener.onWatchListItemClicked(list.get(position));
        } catch (NullPointerException e) {
            Logger.e("WatchlistAdapter", "NullPointerException");
        }


    }

    private void setImage(String imageKey, String imageUrl, ImageView view) {
        try {

            String url1 = preference.getAppPrefCfep();
            if (StringUtils.isNullOrEmpty(url1)) {
                url1 = AppCommonMethod.urlPoints;
                preference.setAppPrefCfep(url1);
            }
            String tranform = "/fit-in/160x90/filters:quality(100):max_bytes(400)";

            StringBuilder stringBuilder = new StringBuilder(url1 + tranform + imageUrl + imageKey);
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

    public interface WatchListAdaperListener {
        void onWatchListItemClicked(ItemsItem itemValue);
    }

    public interface DeleteWatchList {
        void onDeleteClick(ItemsItem itemValue);
    }

    public class SingleItemRowHolder extends RecyclerView.ViewHolder {

        final CommonSearchAdapterBinding itemBinding;

        public SingleItemRowHolder(CommonSearchAdapterBinding binding) {
            super(binding.getRoot());
            this.itemBinding = binding;
        }
    }


}


