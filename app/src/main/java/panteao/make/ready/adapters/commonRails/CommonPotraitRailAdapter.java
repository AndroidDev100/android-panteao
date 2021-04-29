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
import panteao.make.ready.callbacks.commonCallbacks.CommonRailtItemClickListner;
import panteao.make.ready.R;
import panteao.make.ready.beanModelV3.uiConnectorModelV2.EnveuVideoItemBean;
import panteao.make.ready.databinding.PotraitItemBinding;
import panteao.make.ready.databinding.PotraitItemLargeBinding;
import panteao.make.ready.databinding.PotraitItemSmallBinding;
import panteao.make.ready.utils.commonMethods.AppCommonMethod;
import panteao.make.ready.utils.constants.AppConstants;
import panteao.make.ready.utils.helpers.ImageHelper;
import panteao.make.ready.utils.helpers.ksPreferenceKeys.KsPreferenceKeys;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.annotations.NonNull;
import panteao.make.ready.beanModel.enveuCommonRailData.RailCommonData;
import panteao.make.ready.beanModelV3.uiConnectorModelV2.EnveuVideoItemBean;
import panteao.make.ready.callbacks.commonCallbacks.CommonRailtItemClickListner;
import panteao.make.ready.utils.helpers.ksPreferenceKeys.KsPreferenceKeys;

public class CommonPotraitRailAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private long mLastClickTime = 0;
    private RailCommonData railCommonData;
    private List<EnveuVideoItemBean> videos;
    private CommonRailtItemClickListner listner;
    private Context mContext;
    private int pos;
    BaseCategory baseCategory;
    public CommonPotraitRailAdapter(Context context, RailCommonData railCommonData, int position, CommonRailtItemClickListner listner,BaseCategory baseCat) {
        this.mContext = context;
        this.railCommonData = railCommonData;
        this.videos = new ArrayList<>();
        this.videos = railCommonData.getEnveuVideoItemBeans();
        this.listner = listner;
        this.pos=position;
        this.baseCategory=baseCat;
    }


    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int i) {
        if (baseCategory!=null && baseCategory.getRailCardSize()!=null) {
            if (baseCategory.getRailCardSize().equalsIgnoreCase(RailCardSize.NORMAL.name())) {
                PotraitItemBinding binding = DataBindingUtil.inflate(
                        LayoutInflater.from(parent.getContext()),
                        R.layout.potrait_item, parent, false);
                return new NormalHolder(binding);
            }
            else  if (baseCategory.getRailCardSize().equalsIgnoreCase(RailCardSize.SMALL.name())) {
                PotraitItemSmallBinding binding = DataBindingUtil.inflate(
                        LayoutInflater.from(parent.getContext()),
                        R.layout.potrait_item_small, parent, false);
                return new SmallHolder(binding);
            }
            else  if (baseCategory.getRailCardSize().equalsIgnoreCase(RailCardSize.LARGE.name())) {
                PotraitItemLargeBinding binding = DataBindingUtil.inflate(
                        LayoutInflater.from(parent.getContext()),
                        R.layout.potrait_item_large, parent, false);
                return new LargeHolder(binding);
            }
            else {
                PotraitItemBinding binding = DataBindingUtil.inflate(
                        LayoutInflater.from(parent.getContext()),
                        R.layout.potrait_item, parent, false);
                return new NormalHolder(binding);
            }
        }else {
            PotraitItemBinding binding = DataBindingUtil.inflate(
                    LayoutInflater.from(parent.getContext()),
                    R.layout.potrait_item, parent, false);
            return new NormalHolder(binding);
        }

    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int i) {

        if (holder instanceof   NormalHolder) {
            setNomalValues(((  NormalHolder) holder).circularItemBinding,i);
        }
        else if (holder instanceof   SmallHolder) {
            setSmallValues(((  SmallHolder) holder).circularItemBinding,i);
        }
        else if (holder instanceof   LargeHolder) {
            setLargeValues(((LargeHolder) holder).circularItemBinding,i);
        }
        else {
            setNomalValues(((  NormalHolder) holder).circularItemBinding,i);
        }
    }

    private void setLargeValues(PotraitItemLargeBinding itemBinding, int i) {
        EnveuVideoItemBean enveuVideoItemBean=videos.get(i);
        // Logger.w("posterValue-->>",videos.get(i).getPosterURL());
        itemBinding.setPlaylistItem(videos.get(i));
        itemBinding.rippleView.setOnClickListener(v -> {
            itemClick(i);

        });

        try {
            AppCommonMethod.handleTags(enveuVideoItemBean.getIsVIP(),enveuVideoItemBean.getIsNewS(),
                    itemBinding.flExclusive,itemBinding.flNew,itemBinding.flEpisode,itemBinding.flNewMovie,enveuVideoItemBean.getAssetType());

        }catch (Exception ignored){

        }

        try {
            AppCommonMethod.handleTitleDesc(itemBinding.titleLayout,itemBinding.tvTitle,itemBinding.tvDescription,baseCategory);
            itemBinding.tvTitle.setText(videos.get(i).getTitle());
            itemBinding.tvDescription.setText(videos.get(i).getDescription());
        }catch (Exception ignored){

        }

        //AppCommonMethod.railBadgeVisibility(itemBinding.flNew, videos.get(i).isNew());
//        AppCommonMethod.railBadgeVisibility(itemBinding.flExclusive, videos.get(i).isPremium());
        try {
            if (enveuVideoItemBean.getPosterURL() != null && !enveuVideoItemBean.getPosterURL().equalsIgnoreCase("")) {
                ImageHelper.getInstance(mContext).loadImageTo(itemBinding.itemImage, AppCommonMethod.getListPRImage(enveuVideoItemBean.getPosterURL(), mContext));
            }
        }catch (Exception ignored){

        }
    }

    private void setSmallValues(PotraitItemSmallBinding itemBinding, int i) {
        EnveuVideoItemBean enveuVideoItemBean=videos.get(i);
        // Logger.w("posterValue-->>",videos.get(i).getPosterURL());
        itemBinding.setPlaylistItem(videos.get(i));
        itemBinding.rippleView.setOnClickListener(v -> {
            itemClick(i);

        });

        try {
            AppCommonMethod.handleTags(enveuVideoItemBean.getIsVIP(),enveuVideoItemBean.getIsNewS(),
                    itemBinding.flExclusive,itemBinding.flNew,itemBinding.flEpisode,itemBinding.flNewMovie,enveuVideoItemBean.getAssetType());

        }catch (Exception ignored){

        }

        try {
            AppCommonMethod.handleTitleDesc(itemBinding.titleLayout,itemBinding.tvTitle,itemBinding.tvDescription,baseCategory);
            itemBinding.tvTitle.setText(videos.get(i).getTitle());
            itemBinding.tvDescription.setText(videos.get(i).getDescription());
        }catch (Exception ignored){

        }

        //AppCommonMethod.railBadgeVisibility(itemBinding.flNew, videos.get(i).isNew());
//        AppCommonMethod.railBadgeVisibility(itemBinding.flExclusive, videos.get(i).isPremium());
        try {
            if (enveuVideoItemBean.getPosterURL() != null && !enveuVideoItemBean.getPosterURL().equalsIgnoreCase("")) {
                ImageHelper.getInstance(mContext).loadImageTo(itemBinding.itemImage, AppCommonMethod.getListPRImage(enveuVideoItemBean.getPosterURL(), mContext));
            }
        }catch (Exception ignored){

        }
    }

    private void setNomalValues(PotraitItemBinding itemBinding, int i) {
        EnveuVideoItemBean enveuVideoItemBean=videos.get(i);
        // Logger.w("posterValue-->>",videos.get(i).getPosterURL());
        itemBinding.setPlaylistItem(videos.get(i));
        itemBinding.rippleView.setOnClickListener(v -> {
            itemClick(i);

        });

        try {
            AppCommonMethod.handleTags(enveuVideoItemBean.getIsVIP(),enveuVideoItemBean.getIsNewS(),
                    itemBinding.flExclusive,itemBinding.flNew,itemBinding.flEpisode,itemBinding.flNewMovie,enveuVideoItemBean.getAssetType());

        }catch (Exception ignored){

        }

        try {
            AppCommonMethod.handleTitleDesc(itemBinding.titleLayout,itemBinding.tvTitle,itemBinding.tvDescription,baseCategory);
            itemBinding.tvTitle.setText(videos.get(i).getTitle());
            itemBinding.tvDescription.setText(videos.get(i).getDescription());
        }catch (Exception ignored){

        }

        //AppCommonMethod.railBadgeVisibility(itemBinding.flNew, videos.get(i).isNew());
//        AppCommonMethod.railBadgeVisibility(itemBinding.flExclusive, videos.get(i).isPremium());
        try {
            if (enveuVideoItemBean.getPosterURL() != null && !enveuVideoItemBean.getPosterURL().equalsIgnoreCase("")) {
                ImageHelper.getInstance(mContext).loadImageTo(itemBinding.itemImage, AppCommonMethod.getListPRImage(enveuVideoItemBean.getPosterURL(), mContext));
            }
        }catch (Exception ignored){

        }

    }

    public void itemClick(int position) {
        Log.d("clickedfrom","list");

        if (SystemClock.elapsedRealtime() - mLastClickTime < 1200) {
            return;
        }
        mLastClickTime = SystemClock.elapsedRealtime();
        listner.railItemClick(railCommonData, position);
        AppCommonMethod.trackFcmEvent("Content Screen","", mContext,0);

        if(KsPreferenceKeys.getInstance().getScreenName().equalsIgnoreCase(AppConstants.MAIN_HOME)) {
            AppCommonMethod.trackFcmCustomEvent(mContext, AppConstants.CONTENT_SELECT, videos.get(position).getAssetType(), railCommonData.getScreenWidget().getContentID(), railCommonData.getScreenWidget().getName() + "", pos, videos.get(position).getTitle(), position, videos.get(position).getId() + "", 0, 0, "", "","","");
        }else if(KsPreferenceKeys.getInstance().getScreenName().equalsIgnoreCase(AppConstants.MAIN_TOPHITS)) {
            AppCommonMethod.trackFcmCustomEvent(mContext, AppConstants.CONTENT_SELECT, videos.get(position).getAssetType(), railCommonData.getScreenWidget().getContentID(), railCommonData.getScreenWidget().getName() + "", pos, videos.get(position).getTitle(), position, videos.get(position).getId() + "", 0, 0, "", "","","");
        }        else if(KsPreferenceKeys.getInstance().getScreenName().equalsIgnoreCase("Content Screen")) {
            AppCommonMethod.trackFcmCustomEvent(mContext, AppConstants.CONTENT_SELECT, videos.get(position).getAssetType(), railCommonData.getScreenWidget().getContentID(), railCommonData.getScreenWidget().getName() + "", pos, videos.get(position).getTitle(), position, videos.get(position).getId() + "", 0, 0, "", "","","");

        }         else if(KsPreferenceKeys.getInstance().getScreenName().equalsIgnoreCase(AppConstants.MAIN_COMINGSOON)) {
            AppCommonMethod.trackFcmCustomEvent(mContext, AppConstants.CONTENT_SELECT, videos.get(position).getAssetType(), railCommonData.getScreenWidget().getContentID(), railCommonData.getScreenWidget().getName() + "", pos, videos.get(position).getTitle(), position, videos.get(position).getId() + "", 0, 0, "", "","","");

        }        else if(KsPreferenceKeys.getInstance().getScreenName().equalsIgnoreCase(AppConstants.MAIN_LIVETV) ){
            AppCommonMethod.trackFcmCustomEvent(mContext, AppConstants.CONTENT_SELECT, videos.get(position).getAssetType(), railCommonData.getScreenWidget().getContentID(), railCommonData.getScreenWidget().getName() + "", pos, videos.get(position).getTitle(), position, videos.get(position).getId() + "", 0, 0, "", "","","");

        }
//        else if(KsPreferenceKeys.getInstance().getScreenName() == "Search") {
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

        final PotraitItemBinding circularItemBinding;

        NormalHolder(PotraitItemBinding circularItemBind) {
            super(circularItemBind.getRoot());
            this.circularItemBinding = circularItemBind;

        }

    }

    public class SmallHolder extends RecyclerView.ViewHolder {

        final PotraitItemSmallBinding circularItemBinding;

        SmallHolder(PotraitItemSmallBinding circularItemBind) {
            super(circularItemBind.getRoot());
            this.circularItemBinding = circularItemBind;

        }

    }

    public class LargeHolder extends RecyclerView.ViewHolder {

        final PotraitItemLargeBinding circularItemBinding;

        LargeHolder(PotraitItemLargeBinding circularItemBind) {
            super(circularItemBind.getRoot());
            this.circularItemBinding = circularItemBind;

        }

    }


}
