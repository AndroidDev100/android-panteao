package panteao.make.ready.activities.listing.listadapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import panteao.make.ready.beanModelV3.playListModelV2.Thumbnail;
import panteao.make.ready.enums.KalturaImageType;
import panteao.make.ready.modelClasses.ItemsItem;
import panteao.make.ready.R;
import panteao.make.ready.activities.listing.callback.ItemClickListener;
import panteao.make.ready.activities.search.ui.ActivitySearch;
import panteao.make.ready.beanModelV3.uiConnectorModelV2.EnveuVideoItemBean;
import panteao.make.ready.databinding.ListCircleItemBinding;
import panteao.make.ready.databinding.ListCommonItemBinding;
import panteao.make.ready.databinding.ListLdsItemBinding;
import panteao.make.ready.databinding.ListPrItemBinding;
import panteao.make.ready.databinding.ListPrtwoItemBinding;
import panteao.make.ready.databinding.ListSquareItemBinding;
import panteao.make.ready.utils.commonMethods.AppCommonMethod;
import panteao.make.ready.utils.config.ImageLayer;
import panteao.make.ready.utils.cropImage.helpers.Logger;
import panteao.make.ready.utils.cropImage.helpers.PrintLogging;
import panteao.make.ready.utils.helpers.ImageHelper;

import panteao.make.ready.utils.helpers.StringUtils;
import panteao.make.ready.utils.helpers.ksPreferenceKeys.KsPreferenceKeys;

import java.util.HashMap;
import java.util.List;

public class ListAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private final Context context;
    private int limitView = 5;
    private ActivitySearch itemListener;
    private final KsPreferenceKeys preference;
    private final List<EnveuVideoItemBean> list;
    private static final int ITEM = 0;
    private static final int LOADING = 1;
    private final ItemClickListener listener;
    private boolean isWatchList = false;
    private boolean isWatchHistory = false;

    public ListAdapter(Context context, List<EnveuVideoItemBean> list, ItemClickListener listener, int viewType) {
        this.context = context;
        this.list = list;
        this.listener = listener;
        limitView = viewType;
        preference = KsPreferenceKeys.getInstance();
    }

    @Override
    public int getItemViewType(int position) {
        /*boolean isLoadingAdded = false;
        return (position == list.size() - 1 && isLoadingAdded) ? LOADING : ITEM;*/
        return limitView;
    }

    public void notifyAdapter(List<EnveuVideoItemBean> list) {
        this.list.addAll(list);
        notifyDataSetChanged();
    }

    public void notifyEpisodedata(List<panteao.make.ready.beanModel.responseModels.series.season.ItemsItem> i) {

        //list.addAll(i);
        notifyDataSetChanged();

    }

    public void clear() {
        this.list.clear();
        notifyDataSetChanged();
    }

    public void deleteItem(int position) {
        this.list.remove(position);
        notifyDataSetChanged();
    }

    public int allCount() {
        return list.size();
    }


    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        switch (i) {
            case 0:
                ListCircleItemBinding itemBinding;
                itemBinding = DataBindingUtil.inflate(
                        LayoutInflater.from(viewGroup.getContext()),
                        R.layout.list_circle_item, viewGroup, false);

                return new CircleItemRowHolder(itemBinding);


            case 1:
                ListLdsItemBinding listLdsItemBinding;
                listLdsItemBinding = DataBindingUtil.inflate(
                        LayoutInflater.from(viewGroup.getContext()),
                        R.layout.list_lds_item, viewGroup, false);

                return new LandscapeItemRowHolder(listLdsItemBinding);

            case 2:
                ListPrItemBinding listPrItemBinding;
                listPrItemBinding = DataBindingUtil.inflate(
                        LayoutInflater.from(viewGroup.getContext()),
                        R.layout.list_pr_item, viewGroup, false);

                return new PortraiteItemRowHolder(listPrItemBinding);
            case 3:

                ListPrtwoItemBinding listPrtwoItemBinding;
                listPrtwoItemBinding = DataBindingUtil.inflate(
                        LayoutInflater.from(viewGroup.getContext()),
                        R.layout.list_prtwo_item, viewGroup, false);

                return new PortraiteTwoItemRowHolder(listPrtwoItemBinding);

            case 4:
                ListSquareItemBinding listSquareItemBinding;
                listSquareItemBinding = DataBindingUtil.inflate(
                        LayoutInflater.from(viewGroup.getContext()),
                        R.layout.list_square_item, viewGroup, false);

                return new SquareItemRowHolder(listSquareItemBinding);

            default:
                ListLdsItemBinding listLdsItemBinding2;
                listLdsItemBinding2 = DataBindingUtil.inflate(
                        LayoutInflater.from(viewGroup.getContext()),
                        R.layout.list_lds_item, viewGroup, false);

                return new LandscapeItemRowHolder(listLdsItemBinding2);
        }
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder viewHolder, final int position) {

        if (viewHolder instanceof CircleItemRowHolder) {
            setCircleData(((CircleItemRowHolder) viewHolder), position);
        } else if (viewHolder instanceof LandscapeItemRowHolder) {
            setLandscapeData(((LandscapeItemRowHolder) viewHolder), position);
        } else if (viewHolder instanceof PortraiteItemRowHolder) {
            setPortraiteData(((PortraiteItemRowHolder) viewHolder), position);
        } else if (viewHolder instanceof PortraiteTwoItemRowHolder) {
            setPortraiteTwoData(((PortraiteTwoItemRowHolder) viewHolder), position);
        } else if (viewHolder instanceof SquareItemRowHolder) {
            setSquareData(((SquareItemRowHolder) viewHolder), position);
        } else {
            setLandscapeData(((LandscapeItemRowHolder) viewHolder), position);
        }


    }


    private void setCircleData(CircleItemRowHolder viewHolder, int position) {
        viewHolder.itemBinding.tvTitle.setText(list.get(position).getTitle());


        viewHolder.itemBinding.clRoot.setOnClickListener(view -> listener.onRowItemClicked(list.get(position), position));

        if (list.get(position).getPosterURL() != null && !list.get(position).getPosterURL().equalsIgnoreCase("")) {
            //ImageHelper.getInstance(context).loadListSQRImage(viewHolder.itemBinding.itemImage, AppCommonMethod.getListCIRCLEImage(list.get(position).getPosterURL(), context));
            Logger.w("valuesFinal",AppCommonMethod.getListCIRCLEImage(list.get(position).getPosterURL(),context));
            ImageHelper.getInstance(context).loadCIRImage(viewHolder.itemBinding.itemImage, AppCommonMethod.getListCIRCLEImage(list.get(position).getPosterURL(), context), null);

        }
        viewHolder.itemBinding.tvGenre.setVisibility(View.VISIBLE);
        viewHolder.itemBinding.tvGenre.setText(AppCommonMethod.getGenre(list.get(position)));
        if (list.get(position).getDescription() != null && !list.get(position).getDescription().equalsIgnoreCase("")) {
            viewHolder.itemBinding.descriptionTxt.setText(list.get(position).getDescription());
        } else {
            viewHolder.itemBinding.descriptionTxt.setVisibility(View.GONE);
        }
    }

    private void setLandscapeData(LandscapeItemRowHolder viewHolder, int position) {
        HashMap<String, Thumbnail> crousalImages = list.get(position).getImages();
        KalturaImageType imageType = KalturaImageType.LANDSCAPE;
        list.get(position).setPosterURL(ImageLayer.getInstance().getFilteredImage(crousalImages, imageType, 640, 360));

        viewHolder.itemBinding.setPlaylistItem(list.get(position));
        viewHolder.itemBinding.tvTitle.setText(list.get(position).getTitle());

        viewHolder.itemBinding.clRoot.setOnClickListener(view -> {
            listener.onRowItemClicked(list.get(position), position);
        });
        viewHolder.itemBinding.clRoot.setOnLongClickListener(v -> {
            if (isWatchHistory)
                listener.onDeleteWatchHistoryClicked(list.get(position).getId(), position);

            if (isWatchList)
                listener.onDeleteWatchListClicked(list.get(position).getId(), position);
            return true;
        });
        viewHolder.itemBinding.flDeleteWatchlist.setOnClickListener(view -> {

            if (isWatchHistory)
                listener.onDeleteWatchHistoryClicked(list.get(position).getId(), position);

            if (isWatchList)
                listener.onDeleteWatchListClicked(list.get(position).getId(), position);
        });

        if (list.get(position).getPosterURL() != null && !list.get(position).getPosterURL().equalsIgnoreCase("")) {
            Logger.w("valuesFinal",AppCommonMethod.getListCIRCLEImage(list.get(position).getPosterURL(),context)+" "+list.get(position).getTitle());
            ImageHelper.getInstance(context).loadListImage(viewHolder.itemBinding.itemImage, AppCommonMethod.getListLDSImage(list.get(position).getPosterURL(), context));
        }
        viewHolder.itemBinding.tvGenre.setText(AppCommonMethod.getGenre(list.get(position)));
        if (list.get(position).getDescription() != null && !list.get(position).getDescription().equalsIgnoreCase("")) {
            viewHolder.itemBinding.descriptionTxt.setText(list.get(position).getDescription());
        } else {
            viewHolder.itemBinding.descriptionTxt.setVisibility(View.GONE);
        }
        if (isWatchList || isWatchHistory) {
            viewHolder.itemBinding.flDeleteWatchlist.setVisibility(View.GONE);
        } else {
            viewHolder.itemBinding.flDeleteWatchlist.setVisibility(View.GONE);
        }

        try {
            AppCommonMethod.handleTags(list.get(position).getIsVIP(),list.get(position).getIsNewS(),
                    viewHolder.itemBinding.flExclusive,viewHolder.itemBinding.flNew,viewHolder.itemBinding.flEpisode,viewHolder.itemBinding.flNewMovie,list.get(position).getAssetType());

        }catch (Exception ignored){

        }
    }

    private void setPortraiteData(PortraiteItemRowHolder viewHolder, int position) {
        viewHolder.itemBinding.setPlaylistItem(list.get(position));
        viewHolder.itemBinding.tvTitle.setText(list.get(position).getTitle());
        viewHolder.itemBinding.clRoot.setOnClickListener(view -> listener.onRowItemClicked(list.get(position), position));
        //PrintLogging.printLog("","PRPosterImage-->>IN"+AppCommonMethod.getListPRImage(list.get(position).getThumbnailImage()));
        if (list.get(position).getPosterURL() != null && !list.get(position).getPosterURL().equalsIgnoreCase("")) {
            ImageHelper.getInstance(context).loadImageTo(viewHolder.itemBinding.itemImage, AppCommonMethod.getListPRImage(list.get(position).getPosterURL(), context));
        }
        viewHolder.itemBinding.tvGenre.setText(AppCommonMethod.getGenre(list.get(position)));
        if (list.get(position).getDescription() != null && !list.get(position).getDescription().equalsIgnoreCase("")) {
            viewHolder.itemBinding.descriptionTxt.setText(list.get(position).getDescription());
        } else {
            viewHolder.itemBinding.descriptionTxt.setVisibility(View.GONE);
        }

        try {
            AppCommonMethod.handleTags(list.get(position).getIsVIP(),list.get(position).getIsNewS(),
                    viewHolder.itemBinding.flExclusive,viewHolder.itemBinding.flNew,viewHolder.itemBinding.flEpisode,viewHolder.itemBinding.flNewMovie,list.get(position).getAssetType());

        }catch (Exception ignored){

        }
    }

    private void setPortraiteTwoData(PortraiteTwoItemRowHolder viewHolder, int position) {
        viewHolder.itemBinding.setPlaylistItem(list.get(position));
        viewHolder.itemBinding.tvTitle.setText(list.get(position).getTitle());
        viewHolder.itemBinding.clRoot.setOnClickListener(view -> listener.onRowItemClicked(list.get(position), position));

        if (list.get(position).getPosterURL() != null && !list.get(position).getPosterURL().equalsIgnoreCase("")) {
            ImageHelper.getInstance(context).loadImageTo(viewHolder.itemBinding.itemImage, AppCommonMethod.getListPRTwoImage(list.get(position).getPosterURL(), context));
        }
        viewHolder.itemBinding.tvGenre.setText(AppCommonMethod.getGenre(list.get(position)));
        if (list.get(position).getDescription() != null && !list.get(position).getDescription().equalsIgnoreCase("")) {
            viewHolder.itemBinding.descriptionTxt.setText(list.get(position).getDescription());
        } else {
            viewHolder.itemBinding.descriptionTxt.setVisibility(View.GONE);
        }
        try {
            AppCommonMethod.handleTags(list.get(position).getIsVIP(),list.get(position).getIsNewS(),
                    viewHolder.itemBinding.flExclusive,viewHolder.itemBinding.flNew,viewHolder.itemBinding.flEpisode,viewHolder.itemBinding.flNewMovie,list.get(position).getAssetType());

        }catch (Exception ignored){

        }
    }


    private void setSquareData(SquareItemRowHolder viewHolder, int position) {
        viewHolder.itemBinding.setPlaylistItem(list.get(position));
        viewHolder.itemBinding.tvTitle.setText(list.get(position).getTitle());
        viewHolder.itemBinding.clRoot.setOnClickListener(view -> listener.onRowItemClicked(list.get(position), position));

        if (list.get(position).getPosterURL() != null && !list.get(position).getPosterURL().equalsIgnoreCase("")) {
            PrintLogging.printLog("", "imageUrl-->>" + list.get(position).getName() + " --->>" + list.get(position).getPosterURL());
            ImageHelper.getInstance(context).loadListSQRImage(viewHolder.itemBinding.itemImage, AppCommonMethod.getListSQRImage(list.get(position).getPosterURL(), context));
        }
        viewHolder.itemBinding.tvGenre.setText(AppCommonMethod.getGenre(list.get(position)));
        if (list.get(position).getDescription() != null && !list.get(position).getDescription().equalsIgnoreCase("")) {
            viewHolder.itemBinding.descriptionTxt.setText(list.get(position).getDescription());
        } else {
            viewHolder.itemBinding.descriptionTxt.setVisibility(View.GONE);
        }
        try {
            AppCommonMethod.handleTags(list.get(position).getIsVIP(),list.get(position).getIsNewS(),
                    viewHolder.itemBinding.flExclusive,viewHolder.itemBinding.flNew,viewHolder.itemBinding.flEpisode,viewHolder.itemBinding.flNewMovie,list.get(position).getAssetType());

        }catch (Exception ignored){

        }
    }


    public void setImage(String imageKey, String imageUrl, ImageView view) {
        try {

            String url1 = preference.getAppPrefCfep();
            if (StringUtils.isNullOrEmpty(url1)) {
                url1 = AppCommonMethod.urlPoints;
                preference.setAppPrefCfep(url1);
            }
            String tranform = "/fit-in/160x90/filters:quality(100):max_bytes(400)";

            StringBuilder stringBuilder = new StringBuilder(url1 + tranform + imageUrl + imageKey);
            Logger.e("", "imageURL" + stringBuilder.toString());
            Glide.with(context)
                    .asBitmap()
                    .load(stringBuilder.toString())
                    .apply(AppCommonMethod.optionsSearch)
                    .into(view);
        } catch (Exception e) {
            Logger.e("", "" + e.toString());
        }

    }


    @Override
    public int getItemCount() {
       /* if (isLimit)
            return 4;
        else*/
        return list.size();
    }

    public void setWatchList() {
        isWatchList = true;
    }

    public void setWatchHistory() {
        isWatchHistory = true;
    }

    public class CircleItemRowHolder extends RecyclerView.ViewHolder {

        final ListCircleItemBinding itemBinding;

        public CircleItemRowHolder(ListCircleItemBinding binding) {
            super(binding.getRoot());
            this.itemBinding = binding;
        }
    }

    public class LandscapeItemRowHolder extends RecyclerView.ViewHolder {

        final ListLdsItemBinding itemBinding;

        public LandscapeItemRowHolder(ListLdsItemBinding binding) {
            super(binding.getRoot());
            this.itemBinding = binding;
        }
    }

    public class PortraiteItemRowHolder extends RecyclerView.ViewHolder {

        final ListPrItemBinding itemBinding;

        public PortraiteItemRowHolder(ListPrItemBinding binding) {
            super(binding.getRoot());
            this.itemBinding = binding;
        }
    }

    public class PortraiteTwoItemRowHolder extends RecyclerView.ViewHolder {

        final ListPrtwoItemBinding itemBinding;

        public PortraiteTwoItemRowHolder(ListPrtwoItemBinding binding) {
            super(binding.getRoot());
            this.itemBinding = binding;
        }
    }

    public class SquareItemRowHolder extends RecyclerView.ViewHolder {

        final ListSquareItemBinding itemBinding;

        public SquareItemRowHolder(ListSquareItemBinding binding) {
            super(binding.getRoot());
            this.itemBinding = binding;
        }
    }

    public class SingleItemRowHolder extends RecyclerView.ViewHolder {

        final ListCommonItemBinding itemBinding;

        public SingleItemRowHolder(ListCommonItemBinding binding) {
            super(binding.getRoot());
            this.itemBinding = binding;
        }
    }

    public interface RowSearchListener {
        void onRowItemClicked(ItemsItem itemValue);
    }

}


