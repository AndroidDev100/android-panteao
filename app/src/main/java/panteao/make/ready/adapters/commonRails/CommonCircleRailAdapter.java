package panteao.make.ready.adapters.commonRails;

import android.content.Context;
import android.os.SystemClock;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.make.baseCollection.baseCategoryModel.BaseCategory;
import com.make.enums.RailCardSize;
import panteao.make.ready.beanModel.enveuCommonRailData.RailCommonData;
import panteao.make.ready.beanModel.responseModels.landingTabResponses.railData.ContentsItem;
import panteao.make.ready.callbacks.commonCallbacks.CommonRailtItemClickListner;
import panteao.make.ready.R;
import panteao.make.ready.beanModelV3.uiConnectorModelV2.EnveuVideoItemBean;
import panteao.make.ready.databinding.CircleItemBinding;
import panteao.make.ready.databinding.CircleItemLargeBinding;
import panteao.make.ready.databinding.CircleItemSmallBinding;
import panteao.make.ready.utils.commonMethods.AppCommonMethod;
import panteao.make.ready.utils.constants.AppConstants;
import panteao.make.ready.utils.helpers.ImageHelper;
import panteao.make.ready.utils.helpers.ksPreferenceKeys.KsPreferenceKeys;

import java.util.ArrayList;
import java.util.List;

public class CommonCircleRailAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private long mLastClickTime = 0;
    private RailCommonData railCommonData;
    private List<EnveuVideoItemBean> videos;
    private CommonRailtItemClickListner listner;
    private Context mContext;
    private int pos;

    BaseCategory baseCategory;
    public CommonCircleRailAdapter(Context context, RailCommonData railCommonData, int position, CommonRailtItemClickListner listner, BaseCategory baseCat) {
        this.mContext = context;
        this.railCommonData = railCommonData;
        this.videos = new ArrayList<>();
        this.videos = railCommonData.getEnveuVideoItemBeans();
        this.listner = listner;
        this.pos=position;
        this.baseCategory=baseCat;

    }

    public void notifydata(List<ContentsItem> i) {

        notifyDataSetChanged();

    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int i) {
        if (baseCategory!=null && baseCategory.getRailCardSize()!=null) {
            if (baseCategory.getRailCardSize().equalsIgnoreCase(RailCardSize.NORMAL.name())) {
                CircleItemBinding binding = DataBindingUtil.inflate(
                        LayoutInflater.from(parent.getContext()),
                        R.layout.circle_item, parent, false);
                return new NormalHolder(binding);
            }else if (baseCategory.getRailCardSize().equalsIgnoreCase(RailCardSize.SMALL.name())) {
                CircleItemSmallBinding binding = DataBindingUtil.inflate(
                        LayoutInflater.from(parent.getContext()),
                        R.layout.circle_item_small, parent, false);
                return new SmallHolder(binding);
            }
            else if (baseCategory.getRailCardSize().equalsIgnoreCase(RailCardSize.LARGE.name())) {
                CircleItemLargeBinding binding = DataBindingUtil.inflate(
                        LayoutInflater.from(parent.getContext()),
                        R.layout.circle_item_large, parent, false);
                return new LargeHolder(binding);
            }

            else {
                CircleItemBinding binding = DataBindingUtil.inflate(
                        LayoutInflater.from(parent.getContext()),
                        R.layout.circle_item, parent, false);
                return new NormalHolder(binding);
            }
        }else {
            CircleItemBinding binding = DataBindingUtil.inflate(
                    LayoutInflater.from(parent.getContext()),
                    R.layout.circle_item, parent, false);
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

    private void setLargeValues(CircleItemLargeBinding itemBinding, int i) {
        itemBinding.setPlaylistItem(videos.get(i));
        itemBinding.rippleView.setOnClickListener(v -> {
            itemClick(i);

        });

        try {
            if (videos.get(i).getPosterURL() != null && !videos.get(i).getPosterURL().equalsIgnoreCase("")) {
                ImageHelper.getInstance(mContext).loadCircleImageTo(itemBinding.itemImage, AppCommonMethod.getListCIRCLEImage(videos.get(i).getPosterURL(), mContext));
            }
        }catch (Exception ignored){

        }
    }

    private void setSmallValues(CircleItemSmallBinding itemBinding, int i) {
        itemBinding.setPlaylistItem(videos.get(i));
        itemBinding.rippleView.setOnClickListener(v -> {
            itemClick(i);

        });

        try {
            if (videos.get(i).getPosterURL() != null && !videos.get(i).getPosterURL().equalsIgnoreCase("")) {
                ImageHelper.getInstance(mContext).loadCircleImageTo(itemBinding.itemImage, AppCommonMethod.getListCIRCLEImage(videos.get(i).getPosterURL(), mContext));
            }
        }catch (Exception ignored){

        }

    }

    private void setNomalValues(CircleItemBinding itemBinding, int i) {
        itemBinding.setPlaylistItem(videos.get(i));
        itemBinding.rippleView.setOnClickListener(v -> {
            itemClick(i);

        });

        try {
            if (videos.get(i).getPosterURL() != null && !videos.get(i).getPosterURL().equalsIgnoreCase("")) {
                ImageHelper.getInstance(mContext).loadCircleImageTo(itemBinding.itemImage, AppCommonMethod.getListCIRCLEImage(videos.get(i).getPosterURL(), mContext));
            }
        }catch (Exception ignored){

        }

    }

    public void itemClick(int position) {
        Log.d("clickedfrom", KsPreferenceKeys.getInstance().getScreenName());

        if (SystemClock.elapsedRealtime() - mLastClickTime < 1200) {
            return;
        }
        mLastClickTime = SystemClock.elapsedRealtime();
        listner.railItemClick(railCommonData, position);

        if(KsPreferenceKeys.getInstance().getScreenName().equalsIgnoreCase(AppConstants.MAIN_HOME) ) {

            AppCommonMethod.trackFcmEvent("Content Screen","", mContext,0);
            AppCommonMethod.trackFcmCustomEvent(mContext, AppConstants.CONTENT_SELECT, videos.get(position).getAssetType(), railCommonData.getScreenWidget().getContentID(), railCommonData.getScreenWidget().getName() + "", pos, videos.get(position).getTitle(), position, videos.get(position).getId() + "", 0, 0, "", "","","");
        }else if(KsPreferenceKeys.getInstance().getScreenName().equalsIgnoreCase(AppConstants.MAIN_TOPHITS) ) {
            AppCommonMethod.trackFcmEvent("Content Screen","", mContext,0);

            AppCommonMethod.trackFcmCustomEvent(mContext, AppConstants.CONTENT_SELECT, videos.get(position).getAssetType(), railCommonData.getScreenWidget().getContentID(), railCommonData.getScreenWidget().getName() + "", pos, videos.get(position).getTitle(), position, videos.get(position).getId() + "", 0, 0, "", "","","");
        }        else if(KsPreferenceKeys.getInstance().getScreenName().equalsIgnoreCase("Content Screen") ) {
            AppCommonMethod.trackFcmEvent("Content Screen","", mContext,0);

            AppCommonMethod.trackFcmCustomEvent(mContext, AppConstants.CONTENT_SELECT, videos.get(position).getAssetType(), railCommonData.getScreenWidget().getContentID(), railCommonData.getScreenWidget().getName() + "", pos, videos.get(position).getTitle(), position, videos.get(position).getId() + "", 0, 0, "", "","","");

        }         else if(KsPreferenceKeys.getInstance().getScreenName().equalsIgnoreCase(AppConstants.MAIN_COMINGSOON)) {
            AppCommonMethod.trackFcmEvent("Content Screen","", mContext,0);

            AppCommonMethod.trackFcmCustomEvent(mContext, AppConstants.CONTENT_SELECT, videos.get(position).getAssetType(), railCommonData.getScreenWidget().getContentID(), railCommonData.getScreenWidget().getName() + "", pos, videos.get(position).getTitle(), position, videos.get(position).getId() + "", 0, 0, "", "","","");

        }        else if(KsPreferenceKeys.getInstance().getScreenName().equalsIgnoreCase(AppConstants.MAIN_LIVETV)) {
            AppCommonMethod.trackFcmEvent("Content Screen","", mContext,0);

            AppCommonMethod.trackFcmCustomEvent(mContext, AppConstants.CONTENT_SELECT, videos.get(position).getAssetType(), railCommonData.getScreenWidget().getContentID(), railCommonData.getScreenWidget().getName() + "", pos, videos.get(position).getTitle(), position, videos.get(position).getId() + "", 0, 0, "", "","","");

        }
//        else if(KsPreferenceKeys.getInstance().getScreenName() == "Search") {
//            AppCommonMethod.trackFcmEvent("Content Screen","", mContext,0);
//
//            AppCommonMethod.trackFcmCustomEvent(mContext, AppConstants.CONTENT_SELECT, "Search", videos.get(position).getAssetType(), railCommonData.getScreenWidget().getContentID(), railCommonData.getScreenWidget().getName() + "", pos, videos.get(position).getTitle(), position, videos.get(position).getId() + "", 0, 0, "", "");
//
//        }

    }

    @Override
    public int getItemCount() {

        return videos.size();
    }

    public class SingleItemRowHolder extends RecyclerView.ViewHolder {

        final CircleItemBinding circularItemBinding;

        SingleItemRowHolder(CircleItemBinding circularItemBind) {
            super(circularItemBind.getRoot());
            this.circularItemBinding = circularItemBind;
        }

    }

    public class NormalHolder extends RecyclerView.ViewHolder {

        final CircleItemBinding circularItemBinding;

        NormalHolder(CircleItemBinding circularItemBind) {
            super(circularItemBind.getRoot());
            this.circularItemBinding = circularItemBind;

        }

    }

    public class SmallHolder extends RecyclerView.ViewHolder {

        final CircleItemSmallBinding circularItemBinding;

        SmallHolder(CircleItemSmallBinding circularItemBind) {
            super(circularItemBind.getRoot());
            this.circularItemBinding = circularItemBind;

        }

    }

    public class LargeHolder extends RecyclerView.ViewHolder {

        final CircleItemLargeBinding circularItemBinding;

        LargeHolder(CircleItemLargeBinding circularItemBind) {
            super(circularItemBind.getRoot());
            this.circularItemBinding = circularItemBind;

        }

    }


}
