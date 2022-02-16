package panteao.make.ready.adapters.commonRails;

import android.content.Context;
import android.os.SystemClock;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.make.baseCollection.baseCategoryModel.BaseCategory;
import com.make.enums.RailCardSize;
import panteao.make.ready.beanModel.enveuCommonRailData.RailCommonData;
import panteao.make.ready.callbacks.commonCallbacks.CommonRailtItemClickListner;
import panteao.make.ready.R;
import panteao.make.ready.beanModelV3.uiConnectorModelV2.EnveuVideoItemBean;
import panteao.make.ready.databinding.SquareItemBinding;
import panteao.make.ready.databinding.SquareItemLargeBinding;
import panteao.make.ready.databinding.SquareItemSmallBinding;
import panteao.make.ready.utils.commonMethods.AppCommonMethod;
import panteao.make.ready.utils.cropImage.helpers.Logger;
import panteao.make.ready.utils.helpers.ImageHelper;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.annotations.NonNull;

public class CommonSquareRailAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private long mLastClickTime = 0;
    private final RailCommonData railCommonData;
    private List<EnveuVideoItemBean> videos;
    private final CommonRailtItemClickListner listner;
    private final Context mContext;
    final BaseCategory baseCategory;
    public CommonSquareRailAdapter(Context context, RailCommonData railCommonData, CommonRailtItemClickListner listner, BaseCategory baseCat) {
        this.mContext = context;
        this.railCommonData = railCommonData;
        this.videos = new ArrayList<>();
        this.videos = railCommonData.getEnveuVideoItemBeans();
        this.listner = listner;
        this.baseCategory=baseCat;
    }


    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int i) {
        if (baseCategory!=null && baseCategory.getRailCardSize()!=null) {
            if (baseCategory.getRailCardSize().equalsIgnoreCase(RailCardSize.NORMAL.name())) {
                SquareItemBinding binding = DataBindingUtil.inflate(
                        LayoutInflater.from(parent.getContext()),
                        R.layout.square_item, parent, false);
                return new NormalHolder(binding);
            }
            else  if (baseCategory.getRailCardSize().equalsIgnoreCase(RailCardSize.SMALL.name())) {
                SquareItemSmallBinding binding = DataBindingUtil.inflate(
                        LayoutInflater.from(parent.getContext()),
                        R.layout.square_item_small, parent, false);
                return new SmallHolder(binding);
            }
            else  if (baseCategory.getRailCardSize().equalsIgnoreCase(RailCardSize.LARGE.name())) {
                SquareItemLargeBinding binding = DataBindingUtil.inflate(
                        LayoutInflater.from(parent.getContext()),
                        R.layout.square_item_large, parent, false);
                return new LargeHolder(binding);
            }
            else {
                SquareItemBinding binding = DataBindingUtil.inflate(
                        LayoutInflater.from(parent.getContext()),
                        R.layout.square_item, parent, false);
                return new NormalHolder(binding);
            }
        }else {
            SquareItemBinding binding = DataBindingUtil.inflate(
                    LayoutInflater.from(parent.getContext()),
                    R.layout.square_item, parent, false);
            return new NormalHolder(binding);
        }

    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int i) {
        if (holder instanceof NormalHolder) {
            setNomalValues(((NormalHolder) holder).circularItemBinding,i);
        }
        else if (holder instanceof SmallHolder) {
            setSmallValues(((SmallHolder) holder).circularItemBinding,i);
        }
        else if (holder instanceof LargeHolder) {
            setLargeValues(((LargeHolder) holder).circularItemBinding,i);
        }
        else {
            setNomalValues(((NormalHolder) holder).circularItemBinding,i);
        }

    }

    private void setLargeValues(SquareItemLargeBinding itemBinding, int i) {
        itemBinding.setPlaylistItem(videos.get(i));
        itemBinding.rippleView.setOnClickListener(v -> {
            itemClick(i);
        });
        try {
            AppCommonMethod.handleTags(videos.get(i).getIsVIP(),videos.get(i).getIsNewS(),
                    itemBinding.flExclusive,itemBinding.flNew,itemBinding.flEpisode,itemBinding.flNewMovie,videos.get(i).getAssetType());

        }catch (Exception ignored){
            Logger.w("crashonTags",ignored.toString());
        }
        try {
            if (videos.get(i).getPosterURL() != null && !videos.get(i).getPosterURL().equalsIgnoreCase("")) {
                ImageHelper.getInstance(mContext).loadListSQRImage(itemBinding.itemImage, AppCommonMethod.getListSQRImage(videos.get(i).getPosterURL(), mContext));
            }
        }catch (Exception ignored){

        }

        try {
            AppCommonMethod.handleTitleDesc(itemBinding.titleLayout,itemBinding.tvTitle,itemBinding.tvDescription,baseCategory);
            itemBinding.tvTitle.setText(videos.get(i).getTitle());
            itemBinding.tvDescription.setText(videos.get(i).getDescription());
        }catch (Exception ignored){

        }
    }

    private void setSmallValues(SquareItemSmallBinding itemBinding, int i) {
        itemBinding.setPlaylistItem(videos.get(i));
        itemBinding.rippleView.setOnClickListener(v -> {
            itemClick(i);
        });
        try {
            AppCommonMethod.handleTags(videos.get(i).getIsVIP(),videos.get(i).getIsNewS(),
                    itemBinding.flExclusive,itemBinding.flNew,itemBinding.flEpisode,itemBinding.flNewMovie,videos.get(i).getAssetType());

        }catch (Exception ignored){
            Logger.w("crashonTags",ignored.toString());
        }
        try {
            if (videos.get(i).getPosterURL() != null && !videos.get(i).getPosterURL().equalsIgnoreCase("")) {
                ImageHelper.getInstance(mContext).loadListSQRImage(itemBinding.itemImage, AppCommonMethod.getListSQRImage(videos.get(i).getPosterURL(), mContext));
            }
        }catch (Exception ignored){

        }

        try {
            AppCommonMethod.handleTitleDesc(itemBinding.titleLayout,itemBinding.tvTitle,itemBinding.tvDescription,baseCategory);
            itemBinding.tvTitle.setText(videos.get(i).getTitle());
            itemBinding.tvDescription.setText(videos.get(i).getDescription());
        }catch (Exception ignored){

        }
    }

    private void setNomalValues(SquareItemBinding itemBinding, int i) {
        itemBinding.setPlaylistItem(videos.get(i));
        itemBinding.rippleView.setOnClickListener(v -> {
            itemClick(i);
        });
        try {
            AppCommonMethod.handleTags(videos.get(i).getIsVIP(),videos.get(i).getIsNewS(),
                    itemBinding.flExclusive,itemBinding.flNew,itemBinding.flEpisode,itemBinding.flNewMovie,videos.get(i).getAssetType());

        }catch (Exception ignored){
            Logger.w("crashonTags",ignored.toString());
        }
        try {
            if (videos.get(i).getPosterURL() != null && !videos.get(i).getPosterURL().equalsIgnoreCase("")) {
                ImageHelper.getInstance(mContext).loadListSQRImage(itemBinding.itemImage, AppCommonMethod.getListSQRImage(videos.get(i).getPosterURL(), mContext));
            }
        }catch (Exception ignored){

        }

        try {
            AppCommonMethod.handleTitleDesc(itemBinding.titleLayout,itemBinding.tvTitle,itemBinding.tvDescription,baseCategory);
            itemBinding.tvTitle.setText(videos.get(i).getTitle());
            itemBinding.tvDescription.setText(videos.get(i).getDescription());
        }catch (Exception ignored){

        }
    }

    public void itemClick(int position) {

        if (SystemClock.elapsedRealtime() - mLastClickTime < 1200) {
            return;
        }
        mLastClickTime = SystemClock.elapsedRealtime();
        listner.railItemClick();

    }

    @Override
    public int getItemCount() {
        return videos.size();
    }

    public class CustomViewHolder extends RecyclerView.ViewHolder {
        final SquareItemBinding itemBinding;

        CustomViewHolder(SquareItemBinding itemBinding) {
            super(itemBinding.getRoot());
            this.itemBinding = itemBinding;
        }

    }

    public class NormalHolder extends RecyclerView.ViewHolder {

        final SquareItemBinding circularItemBinding;

        NormalHolder(SquareItemBinding circularItemBind) {
            super(circularItemBind.getRoot());
            this.circularItemBinding = circularItemBind;

        }

    }

    public class SmallHolder extends RecyclerView.ViewHolder {

        final SquareItemSmallBinding circularItemBinding;

        SmallHolder(SquareItemSmallBinding circularItemBind) {
            super(circularItemBind.getRoot());
            this.circularItemBinding = circularItemBind;

        }

    }

    public class LargeHolder extends RecyclerView.ViewHolder {

        final SquareItemLargeBinding circularItemBinding;

        LargeHolder(SquareItemLargeBinding circularItemBind) {
            super(circularItemBind.getRoot());
            this.circularItemBinding = circularItemBind;

        }

    }


}
