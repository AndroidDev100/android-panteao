package panteao.make.ready.activities.series.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import panteao.make.ready.R;
import panteao.make.ready.beanModel.responseModels.series.SeasonsItem;
import panteao.make.ready.databinding.ListSeasonSelectionBinding;
import panteao.make.ready.utils.helpers.SortSeasonNumber;

import java.util.Collections;
import java.util.List;

public class SeasonNameAdapter extends RecyclerView.Adapter<SeasonNameAdapter.SeasonViewHolder> {
    private final Context context;
    private List<SeasonsItem> response;
    private SeasonItemClick listner;
    private int selectedId;


    public SeasonNameAdapter(Context context, List<SeasonsItem> seriesResponse, SeasonItemClick listner) {
        this.context = context;
        this.response = seriesResponse;
        this.listner = listner;

    }

    public void notifyAdapterItem(int selectedId) {
        this.selectedId = selectedId;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public SeasonNameAdapter.SeasonViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {

        ListSeasonSelectionBinding itemBinding;
        itemBinding = DataBindingUtil.inflate(
                LayoutInflater.from(viewGroup.getContext()),
                R.layout.list_season_selection, viewGroup, false);

        return new SeasonNameAdapter.SeasonViewHolder(itemBinding);
    }

    @Override
    public void onBindViewHolder(@NonNull SeasonNameAdapter.SeasonViewHolder holder, int position) {
        Collections.sort(response, new SortSeasonNumber());
        holder.itemBinding.charges.setText("Season" + " " + response.get(position).getSeasonNo() + "");


        if (response.get(position).getId() == selectedId) {
            holder.itemBinding.tickImage.setVisibility(View.VISIBLE);
        } else {
            holder.itemBinding.tickImage.setVisibility(View.GONE);
        }

        holder.itemBinding.mainLay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                listner.onAdapterItemClick(response.get(position), position, response.get(position).getSeasonNo());
            }
        });

    }

    @Override
    public int getItemCount() {
        return response.size();
    }


    public interface SeasonItemClick {

        void onAdapterItemClick(SeasonsItem itemClick, int pos, int seasonNum);

    }

    public class SeasonViewHolder extends RecyclerView.ViewHolder {


        final ListSeasonSelectionBinding itemBinding;

        SeasonViewHolder(ListSeasonSelectionBinding binding) {
            super(binding.getRoot());
            this.itemBinding = binding;
        }
    }


}
