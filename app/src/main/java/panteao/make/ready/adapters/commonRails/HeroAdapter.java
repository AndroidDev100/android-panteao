package panteao.make.ready.adapters.commonRails;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import panteao.make.ready.beanModelV3.playListModelV2.PlayListDetailsResponse;
import panteao.make.ready.R;
import panteao.make.ready.databinding.RowHeroLayoutBinding;
import panteao.make.ready.utils.config.ImageLayer;
import panteao.make.ready.utils.helpers.ImageHelper;

import com.google.android.gms.ads.admanager.AdManagerAdRequest;

import panteao.make.ready.beanModelV3.playListModelV2.PlayListDetailsResponse;

public class HeroAdapter extends RecyclerView.Adapter<HeroAdapter.ViewHolder> {

    private final Context mContext;
    private PlayListDetailsResponse item;
    private String deviceId;
    private AdManagerAdRequest adRequest;
    private String adsType;

    public HeroAdapter(Context context, PlayListDetailsResponse item, String adsType) {
        this.mContext = context;
        this.item = item;

    }

    @NonNull
    @Override
    public HeroAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int i) {
        RowHeroLayoutBinding binding = DataBindingUtil.inflate(
                LayoutInflater.from(parent.getContext()),
                R.layout.row_hero_layout, parent, false);
        return new HeroAdapter.ViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull HeroAdapter.ViewHolder holder, int i) {
        try {
            String imageUrl= ImageLayer.getInstance().getHeroImageUrl(item);
            ImageHelper.getInstance(holder.layoutBinding.sliderImage.getContext())
                    .loadImageTo(holder.layoutBinding.sliderImage, imageUrl);

            /*if (item.getItems().get(0).getContent().getImages()!=null && item.getItems().get(0).getContent().getImages().getPoster()!=null && item.getItems().get(0).getContent().getImages().getPoster().getSources()!=null
                    && item.getItems().get(0).getContent().getImages().getPoster().getSources().size()>0){
                ImageHelper.getInstance(holder.layoutBinding.sliderImage.getContext())
                        .loadImageTo(holder.layoutBinding.sliderImage, item.getItems().get(0).getContent().getImages().getPoster().getSources().get(0).getSrc());
            }*/


        } catch (Exception e) {


        }
    }


    @Override
    public int getItemCount() {
        return 1;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        RowHeroLayoutBinding layoutBinding;

        ViewHolder(RowHeroLayoutBinding layoutBinding) {
            super(layoutBinding.getRoot());
            layoutBinding = layoutBinding;

        }

    }


}

