package panteao.make.ready.adapters.commonRails;

import android.content.Context;
import android.os.SystemClock;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import panteao.make.ready.beanModel.enveuCommonRailData.RailCommonData;
import panteao.make.ready.callbacks.commonCallbacks.CommonRailtItemClickListner;
import panteao.make.ready.R;
import panteao.make.ready.databinding.LayoutHeroCircularItemBinding;
import panteao.make.ready.databinding.LayoutHeroLandscapeItemBinding;
import panteao.make.ready.databinding.LayoutHeroPosterItemBinding;
import panteao.make.ready.databinding.LayoutHeroPotraitItemBinding;
import panteao.make.ready.databinding.LayoutHeroSquareItemBinding;
import panteao.make.ready.utils.constants.AppConstants;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.admanager.AdManagerAdRequest;

import panteao.make.ready.beanModel.enveuCommonRailData.RailCommonData;
import panteao.make.ready.callbacks.commonCallbacks.CommonRailtItemClickListner;

public class CommonHeroRailAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private final Context mContext;
    private RailCommonData item;
    private String deviceId;
    private AdManagerAdRequest adRequest;
    private int viewType;
    private RailCommonData railCommonData;
    private CommonRailtItemClickListner listner;
    private long mLastClickTime = 0;

    public CommonHeroRailAdapter(Context context, RailCommonData item, CommonRailtItemClickListner listner) {
        this.mContext = context;
        this.item = item;
        this.viewType = item.getRailType();
        this.listner = listner;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        switch (viewType) {
            case AppConstants
                    .HERO_CIR_CIRCLE:
                LayoutHeroCircularItemBinding itemBinding = DataBindingUtil.inflate(LayoutInflater.from(parent.getContext()), R.layout.layout_hero_circular_item, parent, false);
                return new CircularHeroHolder(itemBinding);
            case AppConstants
                    .HERO_LDS_LANDSCAPE:
                LayoutHeroLandscapeItemBinding itemBinding1 = DataBindingUtil.inflate(LayoutInflater.from(parent.getContext()), R.layout.layout_hero_landscape_item, parent, false);
                return new LandscapeHeroHolder(itemBinding1);
            case AppConstants
                    .HERO_PR_POTRAIT:
                LayoutHeroPotraitItemBinding itemBinding2 = DataBindingUtil.inflate(LayoutInflater.from(parent.getContext()), R.layout.layout_hero_potrait_item, parent, false);
                return new PotraitHeroHolder(itemBinding2);
            case AppConstants
                    .HERO_SQR_SQUARE:
                LayoutHeroSquareItemBinding itemBinding5 = DataBindingUtil.inflate(LayoutInflater.from(parent.getContext()), R.layout.layout_hero_square_item, parent, false);
                return new SquareHeroHolder(itemBinding5);
            case AppConstants
                    .HERO_PR_POSTER:
                LayoutHeroPosterItemBinding itemBinding6 = DataBindingUtil.inflate(LayoutInflater.from(parent.getContext()), R.layout.layout_hero_poster_item, parent, false);
                return new PosterHeroHolder(itemBinding6);
            default:
                LayoutHeroSquareItemBinding itemBinding7 = DataBindingUtil.inflate(LayoutInflater.from(parent.getContext()), R.layout.layout_hero_square_item, parent, false);
                return new SquareHeroHolder(itemBinding7);

        }
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {

        if (holder instanceof SquareHeroHolder) {
            ((SquareHeroHolder) holder).itemBinding.setPlaylistItem(item.getEnveuVideoItemBeans().get(0));
            ((SquareHeroHolder) holder).itemBinding.heroImage.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    itemClick(position);
                }
            });
        } else if (holder instanceof PosterHeroHolder) {
            ((PosterHeroHolder) holder).itemBinding.setPlaylistItem(item.getEnveuVideoItemBeans().get(0));
            ((PosterHeroHolder) holder).itemBinding.heroImage.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    itemClick(position);
                }
            });
        } else if (holder instanceof PotraitHeroHolder) {
            ((PotraitHeroHolder) holder).itemBinding.setPlaylistItem(item.getEnveuVideoItemBeans().get(0));
            ((PotraitHeroHolder) holder).itemBinding.heroImage.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    itemClick(position);
                }
            });

        } else if (holder instanceof CircularHeroHolder) {
            ((CircularHeroHolder) holder).itemBinding.setPlaylistItem(item.getEnveuVideoItemBeans().get(0));
            ((CircularHeroHolder) holder).itemBinding.heroImage.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    itemClick(position);
                }
            });
        } else if (holder instanceof LandscapeHeroHolder) {
            ((LandscapeHeroHolder) holder).itemBinding.setPlaylistItem(item.getEnveuVideoItemBeans().get(0));
            ((LandscapeHeroHolder) holder).itemBinding.heroImage.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    itemClick(position);
                }
            });
        }
    }


    public void itemClick(int position) {

        if (SystemClock.elapsedRealtime() - mLastClickTime < 1200) {
            return;
        }
        mLastClickTime = SystemClock.elapsedRealtime();
        listner.railItemClick(item, position);

    }

    @Override
    public int getItemViewType(int position) {
        return item.getRailType();
    }


    @Override
    public int getItemCount() {
        return 1;
    }

    public class PotraitHeroHolder extends RecyclerView.ViewHolder {
        LayoutHeroPotraitItemBinding itemBinding;

        PotraitHeroHolder(LayoutHeroPotraitItemBinding itemBinding) {
            super(itemBinding.getRoot());
            this.itemBinding = itemBinding;
        }
    }

    public class LandscapeHeroHolder extends RecyclerView.ViewHolder {
        LayoutHeroLandscapeItemBinding itemBinding;

        LandscapeHeroHolder(LayoutHeroLandscapeItemBinding itemBinding) {
            super(itemBinding.getRoot());
            this.itemBinding = itemBinding;
        }
    }

    public class CircularHeroHolder extends RecyclerView.ViewHolder {
        LayoutHeroCircularItemBinding itemBinding;

        CircularHeroHolder(LayoutHeroCircularItemBinding itemBinding) {
            super(itemBinding.getRoot());
            this.itemBinding = itemBinding;
        }
    }

    public class SquareHeroHolder extends RecyclerView.ViewHolder {
        LayoutHeroSquareItemBinding itemBinding;

        SquareHeroHolder(LayoutHeroSquareItemBinding itemBinding) {
            super(itemBinding.getRoot());
            this.itemBinding = itemBinding;
        }
    }

    public class PosterHeroHolder extends RecyclerView.ViewHolder {
        LayoutHeroPosterItemBinding itemBinding;

        PosterHeroHolder(LayoutHeroPosterItemBinding itemBinding) {
            super(itemBinding.getRoot());
            this.itemBinding = itemBinding;
        }
    }


}
