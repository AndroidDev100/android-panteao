package panteao.make.ready.activities.series.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import panteao.make.ready.R;
import panteao.make.ready.activities.detail.ui.EpisodeActivity;
import panteao.make.ready.beanModel.responseModels.series.season.ItemsItem;
import panteao.make.ready.databinding.ListRowSeasonBinding;
import panteao.make.ready.utils.commonMethods.AppCommonMethod;
import panteao.make.ready.utils.constants.AppConstants;
import panteao.make.ready.utils.cropImage.helpers.PrintLogging;

import panteao.make.ready.utils.helpers.StringUtils;
import panteao.make.ready.utils.helpers.ksPreferenceKeys.KsPreferenceKeys;

import java.util.List;

public class SeasonAdapterEpisode extends RecyclerView.Adapter<SeasonAdapterEpisode.SeasonViewHolder> {


    private final Context context;
    private List<ItemsItem> model;
    private SeasonAdapterEpisode.EpisodeItemClick listner;
    private int id;
    private int episodeId;
    private boolean isBuyNow;
    private KsPreferenceKeys preference;
    private String isLogin;

    public SeasonAdapterEpisode(Context context, List<ItemsItem> model, int id, int episodeId, SeasonAdapterEpisode.EpisodeItemClick listner) {
        this.context = context;
        this.model = model;
        this.listner = listner;
        this.id = id;
        this.episodeId = episodeId;
        this.isBuyNow = false;
        //Collections.sort(model, new SortSeasonAdapterItems());
        preference = KsPreferenceKeys.getInstance();
        isLogin = preference.getAppPrefLoginStatus();

    }


    @NonNull
    @Override
    public SeasonAdapterEpisode.SeasonViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {

        ListRowSeasonBinding itemBinding;
        itemBinding = DataBindingUtil.inflate(
                LayoutInflater.from(viewGroup.getContext()),
                R.layout.list_row_season, viewGroup, false);

        return new SeasonAdapterEpisode.SeasonViewHolder(itemBinding);
    }

    @Override
    public void onBindViewHolder(@NonNull SeasonAdapterEpisode.SeasonViewHolder holder, int position) {

        holder.itemBinding.title.setText(model.get(position).getContentTitle());

        double episodeNum = (double) model.get(position).getEpisodeNo();
        int eNum = (int) episodeNum;
        holder.itemBinding.serialNum.setText(eNum + "");
        holder.itemBinding.description.setText(model.get(position).getDescription());


        if (isLogin.equalsIgnoreCase(AppConstants.UserStatus.Login.toString())) {

            double d1 = (double) model.get(position).getContinueDuration();
            long x1 = (long) d1; // x = 1234

            holder.itemBinding.pbProcessing.setMax((int) x1);

            double dd = (double) model.get(position).getPosition();
            long xx = (long) dd; // x = 1234
            holder.itemBinding.pbProcessing.setProgress((int) xx);

            if (xx > 0)
                holder.itemBinding.llContinueProgress.setVisibility(View.VISIBLE);
            else
                holder.itemBinding.llContinueProgress.setVisibility(View.GONE);

        } else {
            holder.itemBinding.llContinueProgress.setVisibility(View.GONE);
        }


        if (!StringUtils.isNullOrEmpty(String.valueOf(model.get(position).getDuration()))) {

            double d = (double) model.get(position).getDuration();
            long x = (long) d; // x = 1234

            holder.itemBinding.duration.setText(AppCommonMethod.calculateTime((x)));
        } else {
            holder.itemBinding.duration.setText("00:00");
        }

        if (position == model.size() - 1) {
            holder.itemBinding.seperator.setVisibility(View.GONE);
        } else {
            holder.itemBinding.seperator.setVisibility(View.VISIBLE);
        }

        if (context instanceof EpisodeActivity) {
            holder.itemBinding.duration.setVisibility(View.GONE);
            holder.itemBinding.description.setVisibility(View.GONE);

            if (model.get(position).getId() == id) {
                holder.itemBinding.duration.setVisibility(View.VISIBLE);
                holder.itemBinding.description.setVisibility(View.VISIBLE);
            } else {
                holder.itemBinding.duration.setVisibility(View.GONE);
                holder.itemBinding.description.setVisibility(View.GONE);
            }


            if (model.get(position).getId() == episodeId) {
                if (isBuyNow) {

                } else {
                    holder.itemBinding.nowPlaying.setVisibility(View.VISIBLE);
                    holder.itemBinding.playIcon.setVisibility(View.GONE);
                }
            } else {
                holder.itemBinding.playIcon.setVisibility(View.VISIBLE);
                holder.itemBinding.nowPlaying.setVisibility(View.GONE);
            }


        }


        holder.itemBinding.playIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                listner.onItemClick(model.get(position).getId(), model.get(position).isPremium());
            }
        });

        holder.itemBinding.mainLay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                PrintLogging.printLog("", "positionIs" + model.get(position));
                id = model.get(position).getId();
                notifyDataSetChanged();
            }
        });
    }


    @Override
    public int getItemCount() {
        return model.size();
    }

    public interface EpisodeItemClick {

        void onItemClick(int assetId, boolean isPremium);

    }

    public class SeasonViewHolder extends RecyclerView.ViewHolder {


        final ListRowSeasonBinding itemBinding;

        SeasonViewHolder(ListRowSeasonBinding binding) {
            super(binding.getRoot());
            this.itemBinding = binding;
        }
    }


}

