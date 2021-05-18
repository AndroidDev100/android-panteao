package panteao.make.ready.adapters.commonRails;


import android.app.Activity;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import panteao.make.ready.beanModel.ContinueRailModel.CommonContinueRail;
import panteao.make.ready.beanModel.responseModels.landingTabResponses.railData.ContentsItem;
import panteao.make.ready.beanModel.responseModels.series.season.ItemsItem;
import panteao.make.ready.R;
import panteao.make.ready.databinding.LandscapeShimmerItemBinding;
import panteao.make.ready.utils.helpers.ksPreferenceKeys.KsPreferenceKeys;


import java.util.ArrayList;
import java.util.List;

import panteao.make.ready.beanModel.ContinueRailModel.CommonContinueRail;
import panteao.make.ready.beanModel.responseModels.landingTabResponses.railData.ContentsItem;
import panteao.make.ready.beanModel.responseModels.series.season.ItemsItem;
import panteao.make.ready.utils.helpers.ksPreferenceKeys.KsPreferenceKeys;

public class CommonLandscapeAdapter extends RecyclerView.Adapter<CommonLandscapeAdapter.SingleItemRowHolder> {

    private final List<ItemsItem> seasonItems;
    private final String contentType;
    private final List<ContentsItem> itemsList;
    private final Activity mContext;
    private KsPreferenceKeys preference;
    private boolean isLogin;
    private ArrayList<CommonContinueRail> continuelist;
    private long mLastClickTime = 0;
    private int itemWidth;
    private int itemHeight;

    public CommonLandscapeAdapter(Activity context, List<ContentsItem> itemsList, List<ItemsItem> itemsItems, String contentType, ArrayList<CommonContinueRail> continuelist) {
        this.itemsList = itemsList;
        this.mContext = context;
        this.seasonItems = itemsItems;
        this.contentType = contentType;
        this.continuelist = continuelist;
        preference = KsPreferenceKeys.getInstance();
        isLogin = preference.getAppPrefLoginStatus();
        int num = 2;
        boolean tabletSize = mContext.getResources().getBoolean(R.bool.isTablet);
        if (tabletSize) {
            //landscape
            if (mContext.getResources().getConfiguration().orientation == 2)
                num = 4;
            else
                num = 3;
        }
        DisplayMetrics displaymetrics = new DisplayMetrics();
        (mContext).getWindowManager().getDefaultDisplay().getMetrics(displaymetrics);
        //if you need three fix imageview in width
        itemWidth = (displaymetrics.widthPixels - 80) / num;
        itemHeight = (int) (itemWidth * 9) / 16;
    }

    @NonNull
    @Override
    public SingleItemRowHolder onCreateViewHolder(@NonNull ViewGroup parent, int i) {
        LandscapeShimmerItemBinding binding = DataBindingUtil.inflate(
                LayoutInflater.from(parent.getContext()),
                R.layout.landscape_shimmer_item, parent, false);
        return new SingleItemRowHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull SingleItemRowHolder holder, int i) {
    }

    @Override
    public int getItemCount() {
        int itemCount = 0;
        if (itemsList.size() > 0) {
            itemCount = itemsList.size();
        } else if (seasonItems.size() > 0) {
            itemCount = seasonItems.size();
        } else if (continuelist.size() > 0)
            itemCount = continuelist.size();
        return itemCount;
    }

    public class SingleItemRowHolder extends RecyclerView.ViewHolder {

        final LandscapeShimmerItemBinding landscapeItemBinding;

        SingleItemRowHolder(LandscapeShimmerItemBinding flightItemLayoutBinding) {
            super(flightItemLayoutBinding.getRoot());
            landscapeItemBinding = flightItemLayoutBinding;
        }
    }

}