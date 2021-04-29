package panteao.make.ready.activities.search.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import panteao.make.ready.beanModel.popularSearch.ItemsItem;
import panteao.make.ready.R;
import panteao.make.ready.beanModel.enveuCommonRailData.RailCommonData;
import panteao.make.ready.beanModelV3.uiConnectorModelV2.EnveuVideoItemBean;
import panteao.make.ready.callbacks.commonCallbacks.SearchClickCallbacks;
import panteao.make.ready.databinding.CommonSearchAdapterBinding;
import panteao.make.ready.utils.commonMethods.AppCommonMethod;
import panteao.make.ready.utils.cropImage.helpers.Logger;
import panteao.make.ready.utils.helpers.ImageHelper;
import panteao.make.ready.utils.helpers.StringUtils;
import panteao.make.ready.utils.helpers.ksPreferenceKeys.KsPreferenceKeys;

import java.util.List;

public class CommonSearchAdapter extends RecyclerView.Adapter<CommonSearchAdapter.SingleItemRowHolder> {
    private final Context context;
    private final RailCommonData jsonObject;
    private final KsPreferenceKeys preference;
    private final SearchClickCallbacks listener;

    public CommonSearchAdapter(Context context, RailCommonData jsonObject, SearchClickCallbacks listener) {
        this.context = context;
        this.jsonObject = jsonObject;
        preference = KsPreferenceKeys.getInstance();
        this.listener = listener;
    }


    @NonNull
    @Override
    public SingleItemRowHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        CommonSearchAdapterBinding itemBinding = DataBindingUtil.inflate(
                LayoutInflater.from(viewGroup.getContext()),
                R.layout.common_search_adapter, viewGroup, false);

        return new SingleItemRowHolder(itemBinding);
    }

    @Override
    public void onBindViewHolder(@NonNull SingleItemRowHolder viewHolder, final int position) {

        List<EnveuVideoItemBean> itemList = jsonObject.getEnveuVideoItemBeans();
        if (itemList.size() > 0) {

            viewHolder.searchItemBinding.setPlaylistItem(itemList.get(position));
            viewHolder.searchItemBinding.tvTitle.setText(itemList.get(position).getTitle());
            viewHolder.searchItemBinding.tvEpisode.setText(AppCommonMethod.getGenre(itemList.get(position)));
            if (itemList.get(position).getPosterURL()!=null && !itemList.get(position).getPosterURL().equalsIgnoreCase("")){
                ImageHelper.getInstance(context).loadListImage(viewHolder.searchItemBinding.itemImage, AppCommonMethod.getListLDSImage(itemList.get(position).getPosterURL(),context));
            }
            //ImageHelper.getInstance(context).loadListImage(viewHolder.searchItemBinding.itemImage, itemList.get(position).getPosterURL());
            viewHolder.searchItemBinding.clRoot.setOnClickListener(view -> listener.onEnveuItemClicked(itemList.get(position)));

            /*viewHolder.searchItemBinding.clRoot.setOnClickListener(view -> listener.onItemClicked(jsonObject.getData().getContinueWatchingBookmarks().get(position)));
            viewHolder.searchItemBinding.tvTitle.setText(jsonObject.getData().getContinueWatchingBookmarks().get(position).getName());
            switch (jsonObject.getData().getContinueWatchingBookmarks().get(position).getType()) {
                case AppConstants.Series:
                    setImage(jsonObject.getData().getContinueWatchingBookmarks().get(position).getPicture(), AppConstants.SERIES_IMAGES_BASE_KEY, viewHolder.searchItemBinding.itemImage);

                     if (jsonObject.getData().getContinueWatchingBookmarks().get(position).getEpisodesCount() > 0) {
                        if (jsonObject.getData().getContinueWatchingBookmarks().get(position).getEpisodesCount() == 1)
                            viewHolder.searchItemBinding.tvEpisode.setText(jsonObject.getData().getContinueWatchingBookmarks().get(position).getEpisodesCount() + " " + context.getResources().getString(R.string.episode));
                        else
                            viewHolder.searchItemBinding.tvEpisode.setText(jsonObject.getData().getContinueWatchingBookmarks().get(position).getEpisodesCount() + " " + context.getResources().getString(R.string.episodes));
                    } else {
                        if (jsonObject.getData().getContinueWatchingBookmarks().get(position).getSeasonCount() == 0)
                            viewHolder.searchItemBinding.tvEpisode.setText(context.getResources().getString(R.string.no_season));
                        else
                            viewHolder.searchItemBinding.tvEpisode.setText(jsonObject.getData().getContinueWatchingBookmarks().get(position).getSeasonCount() + " " + context.getResources().getString(R.string.seasons));
                    }
                    break;
                case AppConstants.Season:
                    setImage(jsonObject.getData().getContinueWatchingBookmarks().get(position).getPicture(), AppConstants.SEASON_IMAGES_BASE_KEY, viewHolder.searchItemBinding.itemImage);
                    viewHolder.searchItemBfinding.tvEpisode.setText(jsonObject.getData().getContinueWatchingBookmarks().get(position).getEpisodesCount() + " " + context.getResources().getString(R.string.episodes));

                    break;
                default:
                    break;
            }*/


        }


    }


    private void setImage(String imageKey, String imageUrl, ImageView view) {
        try {

            String url1 = preference.getAppPrefCfep();
            if (StringUtils.isNullOrEmpty(url1)) {
                url1 = AppCommonMethod.urlPoints;
                preference.setAppPrefCfep(url1);
            }
            String tranform = "/fit-in/70x40/filters:quality(40):max_bytes(100)/";
            StringBuilder stringBuilder = new StringBuilder(url1 + tranform + imageUrl + imageKey);
            Logger.e("", "" + stringBuilder.toString());
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
        return jsonObject.getEnveuVideoItemBeans().size();
    }

    public interface CommonSearchListener {
        void onItemClicked(ItemsItem itemValue);



        default void onEnveuItemClick(EnveuVideoItemBean itemValue) {

        }
    }

    public class SingleItemRowHolder extends RecyclerView.ViewHolder {

        final CommonSearchAdapterBinding searchItemBinding;

        SingleItemRowHolder(CommonSearchAdapterBinding binding) {
            super(binding.getRoot());
            this.searchItemBinding = binding;
        }

    }

}

