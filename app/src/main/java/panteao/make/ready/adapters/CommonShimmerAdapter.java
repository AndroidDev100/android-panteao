package panteao.make.ready.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import panteao.make.ready.R;
import com.facebook.shimmer.ShimmerFrameLayout;

import java.util.ArrayList;
import java.util.List;

public class CommonShimmerAdapter extends RecyclerView.Adapter<CommonShimmerAdapter.PurchaseViewHolder> {


    private final Context context;
    private boolean isHeader = false;
    private List<String> list;

    public CommonShimmerAdapter(Context context) {
        this.context = context;
        dummyList();
    }

    public CommonShimmerAdapter(Context context, boolean isHeader) {
        this.context = context;
        this.isHeader = isHeader;
    }

    private void dummyList() {
        list = new ArrayList<>();
        list.add("one");
        list.add("one");
        list.add("one");
        list.add("one");


    }

    @NonNull
    @Override
    public CommonShimmerAdapter.PurchaseViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.card_row_shimmer, parent, false);
        return new CommonShimmerAdapter.PurchaseViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull CommonShimmerAdapter.PurchaseViewHolder holder, int i) {
        holder.shimmerFrameLayout.startShimmer();
        if (i == 0) {
            if (this.isHeader) {
                holder.header.setVisibility(View.VISIBLE);
            }
        }
    }

    public void clearAdapter() {
        this.list.clear();
        this.notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        dummyList();
        return this.list.size();
    }

    public class PurchaseViewHolder extends RecyclerView.ViewHolder {

        final ShimmerFrameLayout shimmerFrameLayout;
        final View header;

        PurchaseViewHolder(View view) {
            super(view);
            shimmerFrameLayout = view.findViewById(R.id.sflMockContent);
            header = view.findViewById(R.id.header);

        }
    }

}

