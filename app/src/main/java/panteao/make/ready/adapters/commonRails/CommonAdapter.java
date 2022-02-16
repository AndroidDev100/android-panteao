package panteao.make.ready.adapters.commonRails;

import android.app.Activity;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.SnapHelper;

import panteao.make.ready.beanModel.ContinueRailModel.CommonContinueRail;
import panteao.make.ready.beanModel.responseModels.landingTabResponses.CommonRailData;
import panteao.make.ready.R;
import panteao.make.ready.databinding.BannerLayoutBinding;
import panteao.make.ready.databinding.CircleRecyclerItemBinding;
import panteao.make.ready.databinding.HeaderRecyclerItemBinding;
import panteao.make.ready.databinding.LandscapeRecyclerItemBinding;
import panteao.make.ready.databinding.PosterLandscapeRecyclerItemBinding;
import panteao.make.ready.databinding.PosterPotraitRecyclerItemBinding;
import panteao.make.ready.databinding.PotraitRecyclerItemBinding;
import panteao.make.ready.databinding.SquareRecyclerItemBinding;
import panteao.make.ready.utils.helpers.GravitySnapHelper;
import panteao.make.ready.utils.helpers.carousel.model.Slide;
import com.google.ads.interactivemedia.v3.api.AdsLoader;
import com.google.ads.interactivemedia.v3.api.AdsManagerLoadedEvent;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import static panteao.make.ready.utils.constants.AppConstants.APP_CONTINUE_WATCHING;

public class CommonAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> implements AdsLoader.AdsLoadedListener {
    private final Activity activity;
    private final List<CommonRailData> dataList;
    private ArrayList<Slide> slides;


    public CommonAdapter(Activity activity, List<CommonRailData> demoList, ArrayList<Slide> slides) {
        this.activity = activity;
        this.dataList = demoList;
        this.slides = slides;
    }

    @Override
    public int getItemViewType(int position) {
        return dataList.get(position).getType();
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        switch (viewType) {
            case 0: {
                //TYPE_HEADER
                HeaderRecyclerItemBinding binding = DataBindingUtil.inflate(LayoutInflater.from(parent.getContext()), R.layout.header_recycler_item, parent, false);
                return new HeaderHolder(binding);
            }
            case 1: {
                //TYPE_SQUARE
                SquareRecyclerItemBinding binding = DataBindingUtil.inflate(LayoutInflater.from(parent.getContext()), R.layout.square_recycler_item, parent, false);
                return new SqureHolder(binding);
            }
            case 2: {
                //TYPE_CIRCLE
                CircleRecyclerItemBinding binding = DataBindingUtil.inflate(LayoutInflater.from(parent.getContext()), R.layout.circle_recycler_item, parent, false);
                return new CircleHolder(binding);
            }
            case 3: {
                //TYPE_LANDSCAPE
                LandscapeRecyclerItemBinding binding = DataBindingUtil.inflate(LayoutInflater.from(parent.getContext()), R.layout.landscape_recycler_item, parent, false);
                return new LandscapeHolder(binding);
            }
            case 4: {
                //TYPE_BANNER
                BannerLayoutBinding bannerLayoutBinding = DataBindingUtil.inflate(LayoutInflater.from(parent.getContext()), R.layout.banner_layout, parent, false);
                return new BannerHolder(bannerLayoutBinding);
            }
            case 5: {
                //TYPE_POSTER_LANDSCAPE
                PosterLandscapeRecyclerItemBinding itemBinding = DataBindingUtil.inflate(LayoutInflater.from(parent.getContext()), R.layout.poster_landscape_recycler_item, parent, false);
                return new PosterLandscapeHolder(itemBinding);
            }
            case 6: {
                //TYPE_POSTER_POTRAIT
                PosterPotraitRecyclerItemBinding itemBinding = DataBindingUtil.inflate(LayoutInflater.from(parent.getContext()), R.layout.poster_potrait_recycler_item, parent, false);
                return new PosterPotraitHolder(itemBinding);
            }
            default: {
                PotraitRecyclerItemBinding binding = DataBindingUtil.inflate(LayoutInflater.from(parent.getContext()), R.layout.potrait_recycler_item, parent, false);
                return new PortrateHolder(binding);
            }
        }

    }

    public void onClear() {
        dataList.clear();
        notifyDataSetChanged();
    }

    @Override
    public void onBindViewHolder(@NonNull final RecyclerView.ViewHolder holder, final int position) {

    }


    private void setMoreVisibilty(View view, int count) {
        if (count > 10)
            view.setVisibility(View.VISIBLE);
        else
            view.setVisibility(View.GONE);
    }

    private void setDivider(View mView) {
        mView.setVisibility(View.GONE);
       /* AppCommonMethod.adsRail.size();
        int check = position / 5;
        if (position % 5 == 0 && check < AppCommonMethod.adsRail.size()) {
            Logger.e("check", "check" + check);
            Logger.e("adsRail", "adsRail" + AppCommonMethod.adsRail.size());
            mView.setVisibility(View.GONE);
        } else {
            mView.setVisibility(View.VISIBLE);
        }*/
    }

    private void setRecylerProperties(RecyclerView recyclerView, TextView header, String text) {
        recyclerView.setNestedScrollingEnabled(false);
        recyclerView.setHasFixedSize(true);
        header.setText(text);
        // recyclerView.addItemDecoration(new SpacingItemDecoration(12, SpacingItemDecoration.HORIZONTAL));
        recyclerView.setLayoutManager(new LinearLayoutManager(activity, LinearLayoutManager.HORIZONTAL, false));
        SnapHelper snapHelperStart = new GravitySnapHelper(Gravity.START);
        snapHelperStart.attachToRecyclerView(recyclerView);
    }

    @Override
    public int getItemCount() {
        return (null != dataList ? dataList.size() : 0);
    }

    public void notifyAdapter(List<CommonRailData> commonRailData, ArrayList<Slide> slides) {
        if (this.dataList != null) {
            this.dataList.clear();
            this.dataList.addAll(commonRailData);
        }
        if (this.slides != null) {
            this.slides.clear();
            this.slides = slides;
        }
        notifyDataSetChanged();
    }

    public boolean hasContinueRail() {
        boolean hasContinueRailInList = false;
        Iterator itr = dataList.iterator();
        while (itr.hasNext()) {
            CommonRailData x = (CommonRailData) itr.next();
            if (x.getRailData() != null && x.getRailData().getData().getDisplayName().toUpperCase().contains(APP_CONTINUE_WATCHING)) {
                hasContinueRailInList = true;
                return hasContinueRailInList;
            }
        }
        return hasContinueRailInList;
    }

    public void addDataToList(int index, CommonRailData newRail) {
        dataList.add(index, newRail);
        notifyItemInserted(index);
    }

    public void notifyAdapterContinue(ArrayList<CommonContinueRail> continueRailAdapter) {
        for (int i = 0; i < dataList.size(); i++) {
            if (dataList.get(i).getRailData() != null && dataList.get(i).getRailData().getData().getDisplayName().toUpperCase().contains(APP_CONTINUE_WATCHING)) {
                if (continueRailAdapter.isEmpty()) {
                    removeItem();
                    notifyItemRemoved(i);
                } else {
                    addItem(continueRailAdapter);
                    notifyItemChanged(i);
                }
            }
        }
     /*   if(dataList.size()>3)
        notifyItemRangeChanged(2, (null != dataList ? dataList.size() : 0));
        else
            notifyItemRangeChanged(0, (null != dataList ? dataList.size() : 0));
        notifyDataSetChanged();*/

    }

    public void addItem(ArrayList<CommonContinueRail> continueRailAdapter) {
        Iterator itr = dataList.iterator();
        while (itr.hasNext()) {
            CommonRailData x = (CommonRailData) itr.next();
            if (x.getRailData() != null && x.getRailData().getData().getDisplayName().toUpperCase().contains(APP_CONTINUE_WATCHING)) {
                if (x.getContinueRailAdapter() != null) {
                    x.getContinueRailAdapter().clear();
                    x.getContinueRailAdapter().addAll(continueRailAdapter);
                }
            }
        }
    }

    public void removeItem() {
        Iterator itr = dataList.iterator();
        while (itr.hasNext()) {
            CommonRailData x = (CommonRailData) itr.next();
            if (x.getRailData() != null && x.getRailData().getData().getDisplayName().toUpperCase().contains(APP_CONTINUE_WATCHING)) {
                itr.remove();
            }
        }
    }

    @Override
    public void onAdsManagerLoaded(AdsManagerLoadedEvent adsManagerLoadedEvent) {

    }

    public class HeaderHolder extends RecyclerView.ViewHolder {

        final HeaderRecyclerItemBinding headerRecyclerItemBinding;

        HeaderHolder(HeaderRecyclerItemBinding flightItemLayoutBinding) {
            super(flightItemLayoutBinding.getRoot());
            headerRecyclerItemBinding = flightItemLayoutBinding;

        }
    }

    public class CircleHolder extends RecyclerView.ViewHolder {

        final CircleRecyclerItemBinding circularRecyclerItemBinding;

        CircleHolder(CircleRecyclerItemBinding flightItemLayoutBinding) {
            super(flightItemLayoutBinding.getRoot());
            circularRecyclerItemBinding = flightItemLayoutBinding;
        }
    }

    public class PosterPotraitHolder extends RecyclerView.ViewHolder {

        final PosterPotraitRecyclerItemBinding itemBinding;

        PosterPotraitHolder(PosterPotraitRecyclerItemBinding itemBinding) {
            super(itemBinding.getRoot());
            this.itemBinding = itemBinding;
        }
    }

    public class PosterLandscapeHolder extends RecyclerView.ViewHolder {

        final PosterLandscapeRecyclerItemBinding itemBinding;

        PosterLandscapeHolder(PosterLandscapeRecyclerItemBinding itemBinding) {
            super(itemBinding.getRoot());
            this.itemBinding = itemBinding;
        }
    }

    public class SqureHolder extends RecyclerView.ViewHolder {
        final SquareRecyclerItemBinding squareRecyclerItemBinding;

        SqureHolder(SquareRecyclerItemBinding flightItemLayoutBinding) {
            super(flightItemLayoutBinding.getRoot());
            squareRecyclerItemBinding = flightItemLayoutBinding;

        }
    }

    public class LandscapeHolder extends RecyclerView.ViewHolder {
        final LandscapeRecyclerItemBinding landscapeRecyclerItemBinding;

        LandscapeHolder(LandscapeRecyclerItemBinding flightItemLayoutBinding) {
            super(flightItemLayoutBinding.getRoot());
            landscapeRecyclerItemBinding = flightItemLayoutBinding;
        }
    }

    class BannerHolder extends RecyclerView.ViewHolder {
        final BannerLayoutBinding bannerLayoutBinding;

        BannerHolder(BannerLayoutBinding flightItemLayoutBinding) {
            super(flightItemLayoutBinding.getRoot());
            bannerLayoutBinding = flightItemLayoutBinding;
        }
    }

    public class PortrateHolder extends RecyclerView.ViewHolder {
        final PotraitRecyclerItemBinding potraitRecyclerItemBinding;

        PortrateHolder(PotraitRecyclerItemBinding flightItemLayoutBinding) {
            super(flightItemLayoutBinding.getRoot());
            potraitRecyclerItemBinding = flightItemLayoutBinding;
        }
    }
}

