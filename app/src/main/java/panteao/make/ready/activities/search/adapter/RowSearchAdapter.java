package panteao.make.ready.activities.search.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.google.gson.Gson;
import panteao.make.ready.utils.constants.AppConstants;
import panteao.make.ready.utils.cropImage.helpers.Logger;
import panteao.make.ready.utils.helpers.ksPreferenceKeys.KsPreferenceKeys;
import panteao.make.ready.R;
import panteao.make.ready.activities.search.ui.ActivitySearch;
import panteao.make.ready.beanModelV3.uiConnectorModelV2.EnveuVideoItemBean;
import panteao.make.ready.databinding.CommonSearchAdapterBinding;
import panteao.make.ready.utils.MediaTypeConstants;
import panteao.make.ready.utils.commonMethods.AppCommonMethod;
import panteao.make.ready.utils.helpers.ImageHelper;

import panteao.make.ready.utils.helpers.StringUtils;

import java.util.List;

import panteao.make.ready.activities.search.ui.ActivitySearch;
import panteao.make.ready.utils.MediaTypeConstants;

public class RowSearchAdapter extends RecyclerView.Adapter<RowSearchAdapter.SingleItemRowHolder> {
    private static final int ITEM = 0;
    private static final int LOADING = 1;
    private final Context context;
    private final KsPreferenceKeys preference;
    private final List<EnveuVideoItemBean> list;
    private final RowSearchListener listener;
    private int limitView = 5;
    private ActivitySearch itemListener;

    public RowSearchAdapter(Context context, List<EnveuVideoItemBean> list, boolean isLimit, RowSearchListener listener) {
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

        CommonSearchAdapterBinding itemBinding;
        itemBinding = DataBindingUtil.inflate(
                LayoutInflater.from(viewGroup.getContext()),
                R.layout.common_search_adapter, viewGroup, false);

        return new SingleItemRowHolder(itemBinding);
    }

    @Override
    public void onBindViewHolder(@NonNull RowSearchAdapter.SingleItemRowHolder viewHolder, final int position) {

        viewHolder.itemBinding.tvTitle.setText(list.get(position).getTitle());
        viewHolder.itemBinding.clRoot.setOnClickListener(view -> listener.onRowItemClicked(list.get(position)));

        if (list.get(position) != null) {
            viewHolder.itemBinding.setPlaylistItem(list.get(position));
        }
        try {

            if (list.get(position).getAssetType().equalsIgnoreCase(MediaTypeConstants.getInstance().getShow())) {
                if (list.get(position).getPosterURL() != null && !list.get(position).getPosterURL().equalsIgnoreCase("")) {
                    ImageHelper.getInstance(context).loadListImage(viewHolder.itemBinding.itemImage, AppCommonMethod.getListLDSImage(list.get(position).getPosterURL(), context));
                }
            } else if (list.get(position).getAssetType().equalsIgnoreCase(MediaTypeConstants.getInstance().getMovie())) {
                if (list.get(position).getPosterURL() != null && !list.get(position).getPosterURL().equalsIgnoreCase("")) {
                    ImageHelper.getInstance(context).loadListImage(viewHolder.itemBinding.itemImage, AppCommonMethod.getListLDSImage(list.get(position).getPosterURL(), context));
                }
                viewHolder.itemBinding.tvEpisode.setText(AppCommonMethod.getGenre(list.get(position)));
            } else if (list.get(position).getAssetType().equalsIgnoreCase(MediaTypeConstants.getInstance().getEpisode())) {
                if (list.get(position).getPosterURL() != null && !list.get(position).getPosterURL().equalsIgnoreCase("")) {
                    ImageHelper.getInstance(context).loadListImage(viewHolder.itemBinding.itemImage, AppCommonMethod.getListLDSImage(list.get(position).getPosterURL(), context));
                }
                viewHolder.itemBinding.tvEpisode.setText(AppCommonMethod.getGenre(list.get(position)));
            } else if (list.get(position).getAssetType().equalsIgnoreCase(MediaTypeConstants.getInstance().getSeries())) {
                if (list.get(position).getPosterURL() != null && !list.get(position).getPosterURL().equalsIgnoreCase("")) {
                    ImageHelper.getInstance(context).loadListImage(viewHolder.itemBinding.itemImage, AppCommonMethod.getListLDSImage(list.get(position).getPosterURL(), context));
                }
                setSeasonEpisodeValue(list.get(position), viewHolder.itemBinding.tvEpisode);
            } else if (list.get(position).getAssetType().equalsIgnoreCase(MediaTypeConstants.getInstance().getLive())) {
                if (list.get(position).getPosterURL() != null && !list.get(position).getPosterURL().equalsIgnoreCase("")) {
                    ImageHelper.getInstance(context).loadListImage(viewHolder.itemBinding.itemImage, AppCommonMethod.getListLDSImage(list.get(position).getPosterURL(), context));
                }
                viewHolder.itemBinding.tvEpisode.setText(AppCommonMethod.getGenre(list.get(position)));
            } else if (list.get(position).getAssetType().equalsIgnoreCase(AppConstants.Season)) {
                if (list.get(position).getPosterURL() != null && !list.get(position).getPosterURL().equalsIgnoreCase("")) {
                    ImageHelper.getInstance(context).loadListImage(viewHolder.itemBinding.itemImage, AppCommonMethod.getListLDSImage(list.get(position).getPosterURL(), context));
                }
            }

/*
            switch (list.get(position).getAssetType()) {
                case AppConstants.Show:
                    //ImageHelper.getInstance(context).loadListImage(viewHolder.itemBinding.itemImage, list.get(position).getPosterURL());
                    if (list.get(position).getPosterURL()!=null && !list.get(position).getPosterURL().equalsIgnoreCase("")){
                        ImageHelper.getInstance(context).loadListImage(viewHolder.itemBinding.itemImage, AppCommonMethod.getListLDSImage(list.get(position).getPosterURL(),context));
                    }
                    //setImage(list.get(position).getLandscapeImage(), AppConstants.VIDEO_IMAGE_BASE_KEY, viewHolder.itemBinding.itemImage);
                    // viewHolder.itemBinding.tvEpisode.setText(list.get(position).getGenreSearchDTOList().get(0).getGenreName() + " | " + AppCommonMethod.calculateTime(list.get(position).getDuration()));
                    break;
                case AppConstants.Movie:
                    if (list.get(position).getPosterURL()!=null && !list.get(position).getPosterURL().equalsIgnoreCase("")){
                        ImageHelper.getInstance(context).loadListImage(viewHolder.itemBinding.itemImage, AppCommonMethod.getListLDSImage(list.get(position).getPosterURL(),context));
                    }
                    // ImageHelper.getInstance(context).loadListImage(viewHolder.itemBinding.itemImage, list.get(position).getPosterURL());
                    //viewHolder.itemBinding.tvEpisode.setText(AppCommonMethod.getGenre(list.get(position)) + " | " + AppCommonMethod.calculateTime(45116));
                    viewHolder.itemBinding.tvEpisode.setText(AppCommonMethod.getGenre(list.get(position)));
                    break;
                case AppConstants.Episode:
                    if (list.get(position).getPosterURL()!=null && !list.get(position).getPosterURL().equalsIgnoreCase("")){
                        ImageHelper.getInstance(context).loadListImage(viewHolder.itemBinding.itemImage, AppCommonMethod.getListLDSImage(list.get(position).getPosterURL(),context));
                    }
                    //ImageHelper.getInstance(context).loadListImage(viewHolder.itemBinding.itemImage, list.get(position).getPosterURL());
                    viewHolder.itemBinding.tvEpisode.setText(AppCommonMethod.getGenre(list.get(position)));
                    // viewHolder.itemBinding.tvEpisode.setText(list.get(position).getGenreSearchDTOList().get(0).getGenreName() + " | " + AppCommonMethod.calculateTime(list.get(position).getDuration()));
                    break;
                case AppConstants.Series:
                    if (list.get(position).getPosterURL()!=null && !list.get(position).getPosterURL().equalsIgnoreCase("")){
                        ImageHelper.getInstance(context).loadListImage(viewHolder.itemBinding.itemImage, AppCommonMethod.getListLDSImage(list.get(position).getPosterURL(),context));
                    }
                    setSeasonEpisodeValue(list.get(position),viewHolder.itemBinding.tvEpisode);

                    // PrintLogging.printLog("","valuessForSeries"+list.get(position).getSeason().toString());
                    // ImageHelper.getInstance(context).loadListImage(viewHolder.itemBinding.itemImage, list.get(position).getPosterURL());
               */
/* if (list.get(position).getSeason() == 0)
                    viewHolder.itemBinding.tvEpisode.setText(list.get(position).getEpisodesCount() + " " + context.getResources().getString(R.string.episodes));
                else
                    viewHolder.itemBinding.tvEpisode.setText(list.get(position).getSeasonCount() + " " + context.getResources().getString(R.string.seasons));
*//*

                    break;
                case AppConstants.Season:

                    if (list.get(position).getPosterURL()!=null && !list.get(position).getPosterURL().equalsIgnoreCase("")){
                        ImageHelper.getInstance(context).loadListImage(viewHolder.itemBinding.itemImage, AppCommonMethod.getListLDSImage(list.get(position).getPosterURL(),context));
                    }

                    break;

                case AppConstants.Article:

                    if (list.get(position).getPosterURL()!=null && !list.get(position).getPosterURL().equalsIgnoreCase("")){
                        ImageHelper.getInstance(context).loadListImage(viewHolder.itemBinding.itemImage, AppCommonMethod.getListLDSImage(list.get(position).getPosterURL(),context));
                    }

                    viewHolder.itemBinding.tvEpisode.setText(AppCommonMethod.getGenre(list.get(position)));

                    break;

                default:
                    break;
            }
*/


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


    public void setImage(String imageKey, String imageUrl, ImageView view) {
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
       /* if (isLimit)
            return 4;
        else*/
        return list.size();
    }

    public interface RowSearchListener {
        void onRowItemClicked(EnveuVideoItemBean itemValue);
    }

    public class SingleItemRowHolder extends RecyclerView.ViewHolder {

        final CommonSearchAdapterBinding itemBinding;

        public SingleItemRowHolder(CommonSearchAdapterBinding binding) {
            super(binding.getRoot());
            this.itemBinding = binding;
        }
    }

}


