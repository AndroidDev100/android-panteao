package panteao.make.ready.activities.search.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.google.gson.Gson;

import panteao.make.ready.beanModelV3.playListModelV2.Thumbnail;
import panteao.make.ready.databinding.NewCommonSearchAdapterBinding;
import panteao.make.ready.enums.KalturaImageType;
import panteao.make.ready.utils.Utils;
import panteao.make.ready.utils.config.ImageLayer;
import panteao.make.ready.utils.cropImage.helpers.Logger;
import panteao.make.ready.utils.helpers.ksPreferenceKeys.KsPreferenceKeys;
import panteao.make.ready.R;
import panteao.make.ready.activities.search.ui.ActivitySearch;
import panteao.make.ready.beanModelV3.uiConnectorModelV2.EnveuVideoItemBean;
import panteao.make.ready.utils.helpers.ImageHelper;

import java.util.HashMap;
import java.util.List;

public class RowSearchAdapter extends RecyclerView.Adapter<RowSearchAdapter.SingleItemRowHolder> {
    private static final int ITEM = 0;
    private static final int LOADING = 1;
    private final Context context;
    private final KsPreferenceKeys preference;
    private final List<EnveuVideoItemBean> list;
    private final RowSearchListener listener;
    private final int limitView = 5;
    private ActivitySearch itemListener;

    public RowSearchAdapter(Context context, List<EnveuVideoItemBean> list, RowSearchListener listener) {
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

    public void notifyAdapter(List<EnveuVideoItemBean> list) {
        this.list.addAll(list);
        notifyDataSetChanged();
    }


    public int allCount() {
        return list.size();
    }

    @NonNull
    @Override
    public RowSearchAdapter.SingleItemRowHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {

        NewCommonSearchAdapterBinding itemBinding;
        itemBinding = DataBindingUtil.inflate(
                LayoutInflater.from(viewGroup.getContext()),
                R.layout.new_common_search_adapter, viewGroup, false);

        return new SingleItemRowHolder(itemBinding);
    }

    @Override
    public void onBindViewHolder(@NonNull RowSearchAdapter.SingleItemRowHolder viewHolder, final int position) {
        HashMap<String, Thumbnail> crousalImages = list.get(position).getImages();
        KalturaImageType imageType = KalturaImageType.LANDSCAPE;
        if (list.get(position).getImages() != null && list.get(position).getImages().size() > 0)
            list.get(position).setPosterURL(ImageLayer.getInstance().getFilteredImage(crousalImages, imageType, (int) Utils.INSTANCE.dpToPx(128), (int) Utils.INSTANCE.dpToPx(72)));

        viewHolder.itemBinding.tvTitle.setText(list.get(position).getTitle());
        viewHolder.itemBinding.clRoot.setOnClickListener(view -> listener.onRowItemClicked(list.get(position)));

        if (list.get(position) != null) {
            viewHolder.itemBinding.setPlaylistItem(list.get(position));
        }
        try {
            if (list.get(position).getPosterURL() != null && !list.get(position).getPosterURL().equalsIgnoreCase("")) {
                ImageHelper.getInstance(context).loadListImage(viewHolder.itemBinding.itemImage, list.get(position).getPosterURL());
            }
        } catch (Exception ignored) {

        }
    }

    private void setSeasonEpisodeValue(EnveuVideoItemBean enveuVideoItemBean, TextView tvEpisode) {
        Logger.e("Seasons", new Gson().toJson(enveuVideoItemBean));
        if (enveuVideoItemBean != null) {
            if (enveuVideoItemBean.getSeasonCount() > 0 && enveuVideoItemBean.getVodCount() > 0) {
                tvEpisode.setText(context.getResources().getString(R.string.seasons) + " " + enveuVideoItemBean.getSeasonCount() + "  " + context.getResources().getString(R.string.episodes) + " " + enveuVideoItemBean.getVodCount() + "");
            } else {
                if (enveuVideoItemBean.getSeasonCount() == 0 && enveuVideoItemBean.getVodCount() == 0) {
                    tvEpisode.setText("");

                } else if (enveuVideoItemBean.getSeasonCount() == 0) {
                    if (enveuVideoItemBean.getVodCount() > 1) {
                        tvEpisode.setText(context.getResources().getString(R.string.episodes) + " " + enveuVideoItemBean.getVodCount() + "");
                    } else {
                        tvEpisode.setText(context.getResources().getString(R.string.episode) + " " + enveuVideoItemBean.getVodCount() + "");
                    }
                } else if (enveuVideoItemBean.getVodCount() == 0) {
                    tvEpisode.setText(enveuVideoItemBean.getSeasonCount() + "");
                    if (enveuVideoItemBean.getSeasonCount() > 1) {
                        tvEpisode.setText(context.getResources().getString(R.string.seasons) + " " + enveuVideoItemBean.getSeasonCount() + "");
                    } else {
                        tvEpisode.setText(context.getResources().getString(R.string.season) + " " + enveuVideoItemBean.getSeasonCount() + "");
                    }
                }
            }
        }
    }

    @Override
    public int getItemCount() {
       /* if (isLimit)
            return 4;
        else*/
        return list.size();
    }

    public interface RowSearchListener {
        void onRowItemClicked(EnveuVideoItemBean itemValue);
    }

    public class SingleItemRowHolder extends RecyclerView.ViewHolder {

        final NewCommonSearchAdapterBinding itemBinding;

        public SingleItemRowHolder(NewCommonSearchAdapterBinding binding) {
            super(binding.getRoot());
            this.itemBinding = binding;
        }
    }

}


