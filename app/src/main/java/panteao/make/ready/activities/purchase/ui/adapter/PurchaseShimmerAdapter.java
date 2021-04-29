package panteao.make.ready.activities.purchase.ui.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import panteao.make.ready.R;
import com.facebook.shimmer.ShimmerFrameLayout;

public class PurchaseShimmerAdapter extends RecyclerView.Adapter<PurchaseShimmerAdapter.PurchaseViewHolder> {


    private final Context context;
    private boolean isResetCall;

    public PurchaseShimmerAdapter(Context context, boolean callreset) {
        this.context = context;
        this.isResetCall = callreset;

    }

    @NonNull
    @Override
    public PurchaseShimmerAdapter.PurchaseViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.list_row_purchase_shimmer, parent, false);

        return new PurchaseShimmerAdapter.PurchaseViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull PurchaseViewHolder holder, int i) {
        holder.shimmerFrameLayout.startShimmer();

       /*if(isResetCall) {
           new Handler().postDelayed(() -> {
               holder.shimmerFrameLayout.stopShimmerAnimation();
               holder.shimmerFrameLayout.setVisibility(View.GONE);
               holder.cardView.setVisibility(View.GONE);
               ((PurchaseActivity) context).resetpurchaseAdapter();
               // holder.relativeLayout.setVisibility(View.VISIBLE);
           }, 8000);
       }*/

    }

    @Override
    public int getItemCount() {
        return 4;
    }

    public class PurchaseViewHolder extends RecyclerView.ViewHolder {

        public final ShimmerFrameLayout shimmerFrameLayout;
        public final RelativeLayout relativeLayout;
        public final RelativeLayout cardView;


        public PurchaseViewHolder(View view) {
            super(view);
            relativeLayout = view.findViewById(R.id.main_lay);
            shimmerFrameLayout = view.findViewById(R.id.sflMockContent);
            cardView = view.findViewById(R.id.cv_tvod);

        }
    }

}
