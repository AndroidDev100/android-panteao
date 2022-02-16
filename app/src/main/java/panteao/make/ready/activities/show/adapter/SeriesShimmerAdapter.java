package panteao.make.ready.activities.show.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import panteao.make.ready.R;
import panteao.make.ready.databinding.SeasonShimmerBinding;


public class SeriesShimmerAdapter extends RecyclerView.Adapter<SeriesShimmerAdapter.ShimmerViewHolder> {


    private final Context context;

    public SeriesShimmerAdapter(Context context) {
        this.context = context;
    }


    @NonNull
    @Override
    public SeriesShimmerAdapter.ShimmerViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {

        SeasonShimmerBinding itemBinding = DataBindingUtil.inflate(
                LayoutInflater.from(viewGroup.getContext()),
                R.layout.season_shimmer, viewGroup, false);

        return new SeriesShimmerAdapter.ShimmerViewHolder(itemBinding);
    }

    @Override
    public void onBindViewHolder(@NonNull SeriesShimmerAdapter.ShimmerViewHolder holder, int position) {

        holder.itemBinding.sfShimmer1.startShimmer();


    }


    @Override
    public int getItemCount() {
        return 1;
    }


    public class ShimmerViewHolder extends RecyclerView.ViewHolder {
        final SeasonShimmerBinding itemBinding;

        public ShimmerViewHolder(@NonNull SeasonShimmerBinding itemView) {
            super(itemView.getRoot());
            this.itemBinding = itemView;
        }
    }


}

