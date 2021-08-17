package panteao.make.ready.adapters.commonRails;

import android.content.Context;
import android.os.SystemClock;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.make.baseCollection.baseCategoryModel.BaseCategory;
import com.make.enums.RailCardSize;

import panteao.make.ready.beanModel.enveuCommonRailData.RailCommonData;
import panteao.make.ready.beanModelV3.playListModelV2.Thumbnail;
import panteao.make.ready.callbacks.commonCallbacks.CommonRailtItemClickListner;
import panteao.make.ready.R;
import panteao.make.ready.beanModelV3.uiConnectorModelV2.EnveuVideoItemBean;
import panteao.make.ready.databinding.PosterPotraitItemBinding;
import panteao.make.ready.databinding.PosterPotraitItemLargeBinding;
import panteao.make.ready.databinding.PosterPotraitItemSmallBinding;
import panteao.make.ready.enums.KalturaImageType;
import panteao.make.ready.utils.commonMethods.AppCommonMethod;
import panteao.make.ready.utils.config.ImageLayer;
import panteao.make.ready.utils.constants.AppConstants;
import panteao.make.ready.utils.helpers.ImageHelper;
import panteao.make.ready.utils.helpers.ksPreferenceKeys.KsPreferenceKeys;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import io.reactivex.annotations.NonNull;

public class CommonPosterPotrailRailAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private long mLastClickTime = 0;
    private RailCommonData railCommonData;
    private List<EnveuVideoItemBean> videos;
    private CommonRailtItemClickListner listner;
    private Context mContext;
    private int pos;
    BaseCategory baseCategory;

    public CommonPosterPotrailRailAdapter(Context context, RailCommonData railCommonData, int position, CommonRailtItemClickListner listner, BaseCategory baseCat) {
        this.mContext = context;
        this.railCommonData = railCommonData;
        this.videos = new ArrayList<>();
        this.videos = railCommonData.getEnveuVideoItemBeans();
        this.listner = listner;
        this.pos = position;
        this.baseCategory = baseCat;
    }


    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int i) {
        if (baseCategory != null && baseCategory.getRailCardSize() != null) {
            if (baseCategory.getRailCardSize().equalsIgnoreCase(RailCardSize.NORMAL.name())) {
                PosterPotraitItemBinding binding = DataBindingUtil.inflate(
                        LayoutInflater.from(parent.getContext()),
                        R.layout.poster_potrait_item, parent, false);
                return new NormalHolder(binding);
            } else if (baseCategory.getRailCardSize().equalsIgnoreCase(RailCardSize.SMALL.name())) {
                PosterPotraitItemSmallBinding binding = DataBindingUtil.inflate(
                        LayoutInflater.from(parent.getContext()),
                        R.layout.poster_potrait_item_small, parent, false);
                return new SmallHolder(binding);
            } else if (baseCategory.getRailCardSize().equalsIgnoreCase(RailCardSize.LARGE.name())) {
                PosterPotraitItemLargeBinding binding = DataBindingUtil.inflate(
                        LayoutInflater.from(parent.getContext()),
                        R.layout.poster_potrait_item_large, parent, false);
                return new LargeHolder(binding);
            } else {
                PosterPotraitItemBinding binding = DataBindingUtil.inflate(
                        LayoutInflater.from(parent.getContext()),
                        R.layout.poster_potrait_item, parent, false);
                return new NormalHolder(binding);
            }
        } else {
            PosterPotraitItemBinding binding = DataBindingUtil.inflate(
                    LayoutInflater.from(parent.getContext()),
                    R.layout.poster_potrait_item, parent, false);
            return new NormalHolder(binding);
        }

    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int i) {
        //PosterPotraitItemLargeBinding itemBinding = holder.circularItemBinding;
        if (holder instanceof NormalHolder) {
            setNomalValues(((NormalHolder) holder).circularItemBinding, i);
        } else if (holder instanceof SmallHolder) {
            setSmallValues(((SmallHolder) holder).circularItemBinding, i);
        } else if (holder instanceof LargeHolder) {
            setLargeValues(((LargeHolder) holder).circularItemBinding, i);
        } else {
            setNomalValues(((NormalHolder) holder).circularItemBinding, i);
        }

    }

    private void setLargeValues(PosterPotraitItemLargeBinding itemBinding, int i) {
        HashMap<String, Thumbnail> crousalImages = videos.get(i).getImages();
        KalturaImageType imageType = KalturaImageType.PORTRAIT_2_3;
        if (railCommonData.getScreenWidget().getWidgetImageType().equalsIgnoreCase("16x9")) {
            imageType = KalturaImageType.LANDSCAPE;
        }
        videos.get(i).setPosterURL(ImageLayer.getInstance().getFilteredImage(crousalImages, imageType, 450, 800));
        ImageHelper.getInstance(mContext).loadListImage(itemBinding.itemImage, ImageLayer.getInstance().getFilteredImage(crousalImages, imageType, 450, 800));

        itemBinding.setPlaylistItem(videos.get(i));
        itemBinding.rippleView.setOnClickListener(v -> {
            itemClick(i);

        });
        try {
            AppCommonMethod.handleTags(videos.get(i).getIsVIP(), videos.get(i).getIsNewS(),
                    itemBinding.flExclusive, itemBinding.flNew, itemBinding.flEpisode, itemBinding.flNewMovie, videos.get(i).getAssetType());

        } catch (Exception ignored) {

        }

        try {
            AppCommonMethod.handleTitleDesc(itemBinding.titleLayout, itemBinding.tvTitle, itemBinding.tvDescription, baseCategory);
            itemBinding.tvTitle.setText(videos.get(i).getTitle());
            itemBinding.tvDescription.setText(videos.get(i).getDescription());
        } catch (Exception ignored) {

        }
    }

    private void setSmallValues(PosterPotraitItemSmallBinding itemBinding, int i) {
        HashMap<String, Thumbnail> crousalImages = videos.get(i).getImages();
        KalturaImageType imageType = KalturaImageType.PORTRAIT_2_3;
        if (railCommonData.getScreenWidget().getWidgetImageType().equalsIgnoreCase("16x9")) {
            imageType = KalturaImageType.LANDSCAPE;
        }
        videos.get(i).setPosterURL(ImageLayer.getInstance().getFilteredImage(crousalImages, imageType, 180, 320));
        ImageHelper.getInstance(mContext).loadListImage(itemBinding.itemImage, ImageLayer.getInstance().getFilteredImage(crousalImages, imageType, 180, 320));

        itemBinding.setPlaylistItem(videos.get(i));
        itemBinding.rippleView.setOnClickListener(v -> {
            itemClick(i);

        });
        try {
            AppCommonMethod.handleTags(videos.get(i).getIsVIP(), videos.get(i).getIsNewS(),
                    itemBinding.flExclusive, itemBinding.flNew, itemBinding.flEpisode, itemBinding.flNewMovie, videos.get(i).getAssetType());

        } catch (Exception ignored) {

        }

        try {
            AppCommonMethod.handleTitleDesc(itemBinding.titleLayout, itemBinding.tvTitle, itemBinding.tvDescription, baseCategory);
            itemBinding.tvTitle.setText(videos.get(i).getTitle());
            itemBinding.tvDescription.setText(videos.get(i).getDescription());
        } catch (Exception ignored) {

        }
    }

    private void setNomalValues(PosterPotraitItemBinding itemBinding, int i) {
        HashMap<String, Thumbnail> crousalImages = videos.get(i).getImages();
        KalturaImageType imageType = KalturaImageType.PORTRAIT_2_3;
        if (railCommonData.getScreenWidget().getWidgetImageType().equalsIgnoreCase("16x9")) {
            imageType = KalturaImageType.LANDSCAPE;
        }
        videos.get(i).setPosterURL(ImageLayer.getInstance().getFilteredImage(crousalImages, imageType, 360, 640));
        ImageHelper.getInstance(mContext).loadListImage(itemBinding.itemImage, ImageLayer.getInstance().getFilteredImage(crousalImages, imageType, 360, 640));

        itemBinding.setPlaylistItem(videos.get(i));
        itemBinding.rippleView.setOnClickListener(v -> {
            itemClick(i);

        });
        try {
            AppCommonMethod.handleTags(videos.get(i).getIsVIP(), videos.get(i).getIsNewS(),
                    itemBinding.flExclusive, itemBinding.flNew, itemBinding.flEpisode, itemBinding.flNewMovie, videos.get(i).getAssetType());

        } catch (Exception ignored) {

        }

        try {
            AppCommonMethod.handleTitleDesc(itemBinding.titleLayout, itemBinding.tvTitle, itemBinding.tvDescription, baseCategory);
            itemBinding.tvTitle.setText(videos.get(i).getTitle());
            itemBinding.tvDescription.setText(videos.get(i).getDescription());
        } catch (Exception ignored) {

        }
    }


    public void itemClick(int position) {

        if (SystemClock.elapsedRealtime() - mLastClickTime < 1200) {
            return;
        }
        mLastClickTime = SystemClock.elapsedRealtime();
        listner.railItemClick(railCommonData, position);
        Log.d("clickedfrom", "list");

        if (KsPreferenceKeys.getInstance().getScreenName().equalsIgnoreCase(AppConstants.MAIN_HOME)) {
            AppCommonMethod.trackFcmEvent("Content Screen", "", mContext, 0);

            AppCommonMethod.trackFcmCustomEvent(mContext, AppConstants.CONTENT_SELECT, videos.get(position).getAssetType(), railCommonData.getScreenWidget().getContentID(), railCommonData.getScreenWidget().getName() + "", pos, videos.get(position).getTitle(), position, videos.get(position).getId() + "", 0, 0, "", "", "", "");
        } else if (KsPreferenceKeys.getInstance().getScreenName().equalsIgnoreCase(AppConstants.MAIN_TOPHITS)) {
            AppCommonMethod.trackFcmEvent("Content Screen", "", mContext, 0);

            AppCommonMethod.trackFcmCustomEvent(mContext, AppConstants.CONTENT_SELECT, videos.get(position).getAssetType(), railCommonData.getScreenWidget().getContentID(), railCommonData.getScreenWidget().getName() + "", pos, videos.get(position).getTitle(), position, videos.get(position).getId() + "", 0, 0, "", "", "", "");
        } else if (KsPreferenceKeys.getInstance().getScreenName().equalsIgnoreCase("Content Screen")) {
            AppCommonMethod.trackFcmEvent("Content Screen", "", mContext, 0);

            AppCommonMethod.trackFcmCustomEvent(mContext, AppConstants.CONTENT_SELECT, videos.get(position).getAssetType(), railCommonData.getScreenWidget().getContentID(), railCommonData.getScreenWidget().getName() + "", pos, videos.get(position).getTitle(), position, videos.get(position).getId() + "", 0, 0, "", "", "", "");
        } else if (KsPreferenceKeys.getInstance().getScreenName().equalsIgnoreCase(AppConstants.MAIN_COMINGSOON)) {
            AppCommonMethod.trackFcmEvent("Content Screen", "", mContext, 0);

            AppCommonMethod.trackFcmCustomEvent(mContext, AppConstants.CONTENT_SELECT, videos.get(position).getAssetType(), railCommonData.getScreenWidget().getContentID(), railCommonData.getScreenWidget().getName() + "", pos, videos.get(position).getTitle(), position, videos.get(position).getId() + "", 0, 0, "", "", "", "");
        } else if (KsPreferenceKeys.getInstance().getScreenName().equalsIgnoreCase(AppConstants.MAIN_LIVETV)) {
            AppCommonMethod.trackFcmEvent("Content Screen", "", mContext, 0);

            AppCommonMethod.trackFcmCustomEvent(mContext, AppConstants.CONTENT_SELECT, videos.get(position).getAssetType(), railCommonData.getScreenWidget().getContentID(), railCommonData.getScreenWidget().getName() + "", pos, videos.get(position).getTitle(), position, videos.get(position).getId() + "", 0, 0, "", "", "", "");

        }
//         else if(KsPreferenceKeys.getInstance().getScreenName() == "Search") {
//            AppCommonMethod.trackFcmEvent("Content Screen","", mContext,0);
//            AppCommonMethod.trackFcmCustomEvent(mContext, AppConstants.CONTENT_SELECT, "Search", videos.get(position).getAssetType(), railCommonData.getScreenWidget().getContentID(), railCommonData.getScreenWidget().getName() + "", pos, videos.get(position).getTitle(), position, videos.get(position).getId() + "", 0, 0, "", "");
//
//        }
    }

    @Override
    public int getItemCount() {

        return videos.size();
    }

    public class NormalHolder extends RecyclerView.ViewHolder {

        final PosterPotraitItemBinding circularItemBinding;

        NormalHolder(PosterPotraitItemBinding circularItemBind) {
            super(circularItemBind.getRoot());
            this.circularItemBinding = circularItemBind;

        }

    }

    public class SmallHolder extends RecyclerView.ViewHolder {

        final PosterPotraitItemSmallBinding circularItemBinding;

        SmallHolder(PosterPotraitItemSmallBinding circularItemBind) {
            super(circularItemBind.getRoot());
            this.circularItemBinding = circularItemBind;

        }

    }

    public class LargeHolder extends RecyclerView.ViewHolder {

        final PosterPotraitItemLargeBinding circularItemBinding;

        LargeHolder(PosterPotraitItemLargeBinding circularItemBind) {
            super(circularItemBind.getRoot());
            this.circularItemBinding = circularItemBind;

        }

    }


}
