package panteao.make.ready.adapters;

import android.app.Activity;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import panteao.make.ready.beanModel.beanModel.SectionDataModel;
import panteao.make.ready.R;
import panteao.make.ready.databinding.LandscapeShimmerBinding;
import panteao.make.ready.databinding.PotraitShimmerBinding;
import panteao.make.ready.databinding.SquareShimmerBinding;
import com.facebook.shimmer.ShimmerFrameLayout;

import java.util.ArrayList;

public class CommonListingAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private ArrayList<SectionDataModel> dataList;
    private int itemWidth;
    private int itemHeight;

    public CommonListingAdapter(Activity activity) {
        Log.w("common-->","CommonListingAdapter");
        DisplayMetrics displaymetrics = new DisplayMetrics();
        (activity).getWindowManager().getDefaultDisplay().getMetrics(displaymetrics);
        //if you need three fix imageview in width
        int num = 3;
        boolean tabletSize = activity.getResources().getBoolean(R.bool.isTablet);
        if (tabletSize) {
            //landscape
            if (activity.getResources().getConfiguration().orientation == 2)
                num = 6;
            else
                num = 5;
        }

        //if you need three fix imageview in width
        itemWidth = (displaymetrics.widthPixels - 60) / num;
        itemHeight = (int) (itemWidth * 16) / 9;
    }


    public void setDataList(ArrayList<SectionDataModel> demoList) {
        this.dataList = demoList;
    }

    @Override
    public int getItemViewType(int position)
    {
        return dataList.get(0).getType();
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if (viewType == 1) {
            SquareShimmerBinding squareShimmerBinding = DataBindingUtil.inflate(LayoutInflater.from(parent.getContext()), R.layout.square_shimmer, parent, false);
            return new SquareHolder(squareShimmerBinding);
        } else if (viewType == 2) {
            PotraitShimmerBinding potraitShimmerBinding = DataBindingUtil.inflate(LayoutInflater.from(parent.getContext()), R.layout.potrait_shimmer, parent, false);
            return new PortrateHolder(potraitShimmerBinding);
        } else if (viewType == 3) {
            LandscapeShimmerBinding landscapeShimmerBinding = DataBindingUtil.inflate(LayoutInflater.from(parent.getContext()), R.layout.landscape_shimmer, parent, false);
            return new LandscapeHolder(landscapeShimmerBinding);
        } else {
            SquareShimmerBinding squareShimmerBinding = DataBindingUtil.inflate(LayoutInflater.from(parent.getContext()), R.layout.square_shimmer, parent, false);
            return new SquareHolder(squareShimmerBinding);
        }

    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder viewHolder, int i) {
        if (viewHolder instanceof SquareHolder) {
            ((SquareHolder) viewHolder).shimmerFrameLayout.startShimmer();
        } else if (viewHolder instanceof LandscapeHolder) {
            ((LandscapeHolder) viewHolder).shimmerFrameLayout.startShimmer();
        } else if (viewHolder instanceof PortrateHolder) {
            ((PortrateHolder) viewHolder).shimmerFrameLayout.startShimmer();
            ((PortrateHolder) viewHolder).shimmerImage.getLayoutParams().width = itemWidth;
            ((PortrateHolder) viewHolder).shimmerImage.getLayoutParams().height = itemHeight;

        }
    }

    @Override
    public int getItemCount() {
        return 18;
    }

    class SquareHolder extends RecyclerView.ViewHolder {
        final SquareShimmerBinding squareRecyclerItemBinding;
        final ShimmerFrameLayout shimmerFrameLayout;

        SquareHolder(SquareShimmerBinding flightItemLayoutBinding) {
            super(flightItemLayoutBinding.getRoot());
            squareRecyclerItemBinding = flightItemLayoutBinding;
            shimmerFrameLayout = flightItemLayoutBinding.sfShimmer1;

        }
    }

    class LandscapeHolder extends RecyclerView.ViewHolder {
        final LandscapeShimmerBinding landscapeRecyclerItemBinding;
        final ShimmerFrameLayout shimmerFrameLayout;

        LandscapeHolder(LandscapeShimmerBinding flightItemLayoutBinding) {
            super(flightItemLayoutBinding.getRoot());
            landscapeRecyclerItemBinding = flightItemLayoutBinding;
            shimmerFrameLayout = flightItemLayoutBinding.sfShimmer1;

        }
    }

    class PortrateHolder extends RecyclerView.ViewHolder {
        final PotraitShimmerBinding potraitRecyclerItemBinding;
        final ShimmerFrameLayout shimmerFrameLayout;
        final View shimmerImage;

        PortrateHolder(PotraitShimmerBinding flightItemLayoutBinding) {
            super(flightItemLayoutBinding.getRoot());
            potraitRecyclerItemBinding = flightItemLayoutBinding;
            shimmerFrameLayout = flightItemLayoutBinding.sfShimmer1;
            shimmerImage = flightItemLayoutBinding.shimmerViewPotrait;
        }
    }
}
