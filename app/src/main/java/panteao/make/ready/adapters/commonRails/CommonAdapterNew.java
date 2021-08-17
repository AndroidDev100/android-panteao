package panteao.make.ready.adapters.commonRails;

import android.app.Activity;
import android.content.Context;
import android.content.res.Configuration;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AlphaAnimation;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.SnapHelper;

import panteao.make.ready.beanModel.enveuCommonRailData.RailCommonData;
import panteao.make.ready.callbacks.commonCallbacks.CommonRailtItemClickListner;
import panteao.make.ready.callbacks.commonCallbacks.MoreClickListner;
import panteao.make.ready.R;
import panteao.make.ready.databinding.CircleRecyclerItemBinding;
import panteao.make.ready.databinding.DfpBannerLayoutBinding;
import panteao.make.ready.databinding.HeaderRecyclerItemBinding;
import panteao.make.ready.databinding.HeadingRailsBinding;
import panteao.make.ready.databinding.HeroAdsLayoutBinding;
import panteao.make.ready.databinding.LandscapeRecyclerItemBinding;
import panteao.make.ready.databinding.PosterPotraitRecyclerItemBinding;
import panteao.make.ready.databinding.PotraitRecyclerItemBinding;
import panteao.make.ready.databinding.SquareRecyclerItemBinding;
import panteao.make.ready.utils.CustomLayoutManager;
import panteao.make.ready.utils.commonMethods.AppCommonMethod;
import panteao.make.ready.utils.constants.AppConstants;
import panteao.make.ready.utils.cropImage.helpers.Logger;
import panteao.make.ready.utils.helpers.GravitySnapHelper;
import panteao.make.ready.utils.helpers.SpacingItemDecoration;
import panteao.make.ready.utils.helpers.ksPreferenceKeys.KsPreferenceKeys;

import java.util.List;

import static panteao.make.ready.utils.constants.AppConstants.ADS_BANNER;
import static panteao.make.ready.utils.constants.AppConstants.ADS_CUS;
import static panteao.make.ready.utils.constants.AppConstants.ADS_MREC;
import static panteao.make.ready.utils.constants.AppConstants.CAROUSEL_CIR_CIRCLE;
import static panteao.make.ready.utils.constants.AppConstants.CAROUSEL_CST_CUSTOM;
import static panteao.make.ready.utils.constants.AppConstants.CAROUSEL_LDS_BANNER;
import static panteao.make.ready.utils.constants.AppConstants.CAROUSEL_LDS_LANDSCAPE;
import static panteao.make.ready.utils.constants.AppConstants.CAROUSEL_PR_POSTER;
import static panteao.make.ready.utils.constants.AppConstants.CAROUSEL_PR_POTRAIT;
import static panteao.make.ready.utils.constants.AppConstants.CAROUSEL_SQR_SQUARE;
import static panteao.make.ready.utils.constants.AppConstants.HERO_CIR_CIRCLE;
import static panteao.make.ready.utils.constants.AppConstants.HERO_CST_CUSTOM;
import static panteao.make.ready.utils.constants.AppConstants.HERO_LDS_BANNER;
import static panteao.make.ready.utils.constants.AppConstants.HERO_LDS_LANDSCAPE;
import static panteao.make.ready.utils.constants.AppConstants.HERO_PR_POSTER;
import static panteao.make.ready.utils.constants.AppConstants.HERO_PR_POTRAIT;
import static panteao.make.ready.utils.constants.AppConstants.HERO_SQR_SQUARE;
import static panteao.make.ready.utils.constants.AppConstants.HORIZONTAL_CIR_CIRCLE;
import static panteao.make.ready.utils.constants.AppConstants.HORIZONTAL_LDS_LANDSCAPE;
import static panteao.make.ready.utils.constants.AppConstants.HORIZONTAL_PR_POSTER;
import static panteao.make.ready.utils.constants.AppConstants.HORIZONTAL_PR_POTRAIT;
import static panteao.make.ready.utils.constants.AppConstants.HORIZONTAL_SQR_SQUARE;

public class CommonAdapterNew extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private Context mContext;
    private List<RailCommonData> mList;
    private CommonRailtItemClickListner listner;
    private MoreClickListner moreClickListner;

    public CommonAdapterNew(Context context, List<RailCommonData> mList, CommonRailtItemClickListner listner, MoreClickListner moreClickListner) {
        Logger.e("PLAYLIST_ID", "ON_BIND_1");
        this.mContext = context;
        this.mList = mList;
        this.listner = listner;
        this.moreClickListner = moreClickListner;


    }

    public void notifyAdapter(RailCommonData item) {
        this.mList.add(item);
        notifyItemChanged(this.mList.size() - 1);
//        notifyDataSetChanged();
    }


    @Override
    public int getItemViewType(int position) {
        return mList.get(position).getRailType();
    }


    @Override
    public int getItemCount() {
        return mList.size();
    }


    @Override
    public void onBindViewHolder(@NonNull final RecyclerView.ViewHolder holder, final int position) {
        Logger.e("position bind in", position + " ==>" + holder.getClass().getSimpleName());
        setFadeAnimation(holder.itemView);
        if (holder instanceof CarouselViewHolder) {
            carouselLandscape((CarouselViewHolder) holder, position);
//            AppCommonMethod.trackFcmEvent("Content_screen","",mContext,0);

        } else if (holder instanceof DfpBannerHolder) {
            dfpAdsLayout((DfpBannerHolder) holder, position);
        } else if (holder instanceof HeroAdsHolder) {
            heroAdsLayout((HeroAdsHolder) holder, position);
        } else if (holder instanceof CircleHolder) {
            circularRail((CircleHolder) holder, position);
        } else if (holder instanceof SquareHolder) {
            squareRail((SquareHolder) holder, position);
        } else if (holder instanceof PortraitHolder) {
            potraitRail((PortraitHolder) holder, position);
        } else if (holder instanceof LandscapeHolder) {
            LandscapeRail((LandscapeHolder) holder, position);
        } else {
            posterPotraitRail((PosterPotraitHolder) holder, position);
        }
    }

    public void setFadeAnimation(View view) {
        AlphaAnimation anim = new AlphaAnimation(0.0f, 1.0f);
        anim.setDuration(500);
        view.startAnimation(anim);
    }

    private void posterPotraitRail(PosterPotraitHolder viewHolder, int position) {
        Logger.e("RAIL_TYPE", "PosterPortrait");
        RecyclerView recyclerView = viewHolder.itemBinding.recyclerViewList2;
        CommonPosterPotrailRailAdapter adapter = new CommonPosterPotrailRailAdapter(mContext, mList.get(position), position, listner, mList.get(position).getScreenWidget());
        setCommonRailAdapter(viewHolder.itemBinding.titleHeading, recyclerView, position, adapter);
    }

    private void LandscapeRail(LandscapeHolder viewHolder, int position) {
        Logger.e("RAIL_TYPE", "Landscape");
        RecyclerView recyclerView = viewHolder.landscapeRecyclerItemBinding.recyclerViewList3;
        CommonLandscapeRailAdapter adapter = new CommonLandscapeRailAdapter(mContext, mList.get(position), position, listner, mList.get(position).getScreenWidget());
        setCommonRailAdapter(viewHolder.landscapeRecyclerItemBinding.titleHeading, recyclerView, position, adapter);
    }

    private void potraitRail(PortraitHolder viewHolder, int position) {
        Logger.e("RAIL_TYPE", "Portrait");
        RecyclerView recyclerView = viewHolder.potraitRecyclerItemBinding.recyclerViewList4;
        CommonPotraitRailAdapter adapter = new CommonPotraitRailAdapter(mContext, mList.get(position), position, listner, mList.get(position).getScreenWidget());
        setCommonRailAdapter(viewHolder.potraitRecyclerItemBinding.titleHeading, recyclerView, position, adapter);
    }

    private void squareRail(SquareHolder viewHolder, int position) {
        Logger.e("RAIL_TYPE", "Square");
        RecyclerView recyclerView = viewHolder.squareRecyclerItemBinding.recyclerViewList2;
        CommonSquareRailAdapter commonSquareRailAdapter = new CommonSquareRailAdapter(mContext, mList.get(position), listner, mList.get(position).getScreenWidget());
        setCommonRailAdapter(viewHolder.squareRecyclerItemBinding.titleHeading, recyclerView, position, commonSquareRailAdapter);
    }

    private void circularRail(CircleHolder viewHolder, int position) {
        Logger.e("RAIL_TYPE", "Circle");
        RecyclerView recyclerView = viewHolder.circularRecyclerItemBinding.recyclerViewList1;
        CommonCircleRailAdapter commonCircleAdapter = new CommonCircleRailAdapter(mContext, mList.get(position), position, listner, mList.get(position).getScreenWidget());
        setCommonRailAdapter(viewHolder.circularRecyclerItemBinding.titleHeading, recyclerView, position, commonCircleAdapter);
    }


    private void setCommonRailAdapter(HeadingRailsBinding headingRailsBinding, RecyclerView recyclerView, int position, RecyclerView.Adapter adapter) {
        try {
            setTitle(headingRailsBinding, mList.get(position), position);
            if (mList.get(position).getEnveuVideoItemBeans().size() > 0) {
                recyclerView.setNestedScrollingEnabled(false);
                recyclerView.setLayoutManager(new LinearLayoutManager(mContext, LinearLayoutManager.HORIZONTAL, false));
                recyclerView.setHasFixedSize(true);
                recyclerView.setAdapter(adapter);
            } else {
                recyclerView.setAdapter(null);
            }
        } catch (ClassCastException e) {
            Logger.e("CommonAdapter", "" + e.toString());
        }
    }

    private void setRecylerProperties(RecyclerView recyclerView) {
        recyclerView.setNestedScrollingEnabled(false);
        recyclerView.addItemDecoration(new SpacingItemDecoration(8, SpacingItemDecoration.HORIZONTAL));
        recyclerView.setLayoutManager(new LinearLayoutManager(mContext, LinearLayoutManager.HORIZONTAL, false));
        recyclerView.setHasFixedSize(true);
        SnapHelper snapHelperStart = new GravitySnapHelper(Gravity.START);
        snapHelperStart.attachToRecyclerView(recyclerView);
    }


    private void carouselLandscape(CarouselViewHolder viewHolder, int position) {
        setTitle(viewHolder.itemBinding.titleHeading, mList.get(position), position);
        KsPreferenceKeys.getInstance().setAutoDuration(mList.get(position).getScreenWidget().getAutoRotateDuration() == null ? 0 : mList.get(position).getScreenWidget().getAutoRotateDuration());
        KsPreferenceKeys.getInstance().setAutoRotation(mList.get(position).getScreenWidget().getAutoRotate() == null ? true : mList.get(position).getScreenWidget().getAutoRotate());
        viewHolder.itemBinding.slider.addSlides(mList.get(position), listner, position, mList.get(position).getRailType(), mList.get(position).getScreenWidget().getContentIndicator(), mList.get(position).getScreenWidget().getAutoRotate() == null ? true : mList.get(position).getScreenWidget().getAutoRotate(), mList.get(position).getScreenWidget().getAutoRotateDuration() == null ? 0 : mList.get(position).getScreenWidget().getAutoRotateDuration());
    }


    private void heroAdsLayout(HeroAdsHolder holder, int position) {
        CommonHeroRailAdapter adapter = new CommonHeroRailAdapter(mContext, mList.get(position), listner);
        holder.heroAdsHolder.rvHeroBanner.setNestedScrollingEnabled(false);
        holder.heroAdsHolder.rvHeroBanner.setHasFixedSize(true);
        holder.heroAdsHolder.rvHeroBanner.setLayoutManager(new LinearLayoutManager(mContext, LinearLayoutManager.HORIZONTAL, false));
        holder.heroAdsHolder.rvHeroBanner.setAdapter(adapter);

    }


    private void dfpAdsLayout(DfpBannerHolder viewHolder, int position) {
        int viewType = (mList.get(position).getRailType()) % 10;
        try {
            DfpBannerAdapter adapter;
            if (viewType == 1) {
                adapter = new DfpBannerAdapter(mContext, mList.get(position), AppConstants.KEY_BANNER, mList.get(position).getScreenWidget().getHeight() == null ? 0 : (int) ((double) mList.get(position).getScreenWidget().getHeight()), mList.get(position).getScreenWidget().getWidth() == null ? 0 : (int) ((double) mList.get(position).getScreenWidget().getWidth()));
            } else if (viewType == 3) {
                adapter = new DfpBannerAdapter(mContext, mList.get(position), AppConstants.KEY_CUS, mList.get(position).getScreenWidget().getHeight() == null ? 0 : (int) ((double) mList.get(position).getScreenWidget().getHeight()), mList.get(position).getScreenWidget().getWidth() == null ? 0 : (int) ((double) mList.get(position).getScreenWidget().getWidth()));
            } else {
                adapter = new DfpBannerAdapter(mContext, mList.get(position), AppConstants.KEY_MREC, mList.get(position).getScreenWidget().getHeight() == null ? 0 : (int) ((double) mList.get(position).getScreenWidget().getHeight()), mList.get(position).getScreenWidget().getWidth() == null ? 0 : (int) ((double) mList.get(position).getScreenWidget().getWidth()));
            }

            viewHolder.dfpBannerLayoutBinding.rvDfpBanner.setNestedScrollingEnabled(false);
            viewHolder.dfpBannerLayoutBinding.rvDfpBanner.setHasFixedSize(true);
            viewHolder.dfpBannerLayoutBinding.rvDfpBanner.setLayoutManager(new CustomLayoutManager(mContext, LinearLayoutManager.HORIZONTAL, false));
            SnapHelper snapHelperStart = new GravitySnapHelper(Gravity.START);
            snapHelperStart.attachToRecyclerView(viewHolder.dfpBannerLayoutBinding.rvDfpBanner);
            viewHolder.dfpBannerLayoutBinding.rvDfpBanner.setAdapter(adapter);
        } catch (ClassCastException ea) {
            Logger.e("CommonAdapter", "" + ea.toString());
        }

    }


    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Logger.e("PLAYLIST_ID", String.valueOf(viewType));
        switch (viewType) {
            case CAROUSEL_CIR_CIRCLE:
            case CAROUSEL_LDS_LANDSCAPE:
            case CAROUSEL_SQR_SQUARE:
            case CAROUSEL_CST_CUSTOM:
            case CAROUSEL_LDS_BANNER:
            case CAROUSEL_PR_POSTER:
            case CAROUSEL_PR_POTRAIT:
                HeaderRecyclerItemBinding headerRecyclerItemBinding = DataBindingUtil.inflate(LayoutInflater.from(parent.getContext()), R.layout.header_recycler_item, parent, false);
                return new CarouselViewHolder(headerRecyclerItemBinding, viewType);
            case HERO_CIR_CIRCLE:
            case HERO_CST_CUSTOM:
            case HERO_LDS_BANNER:
            case HERO_LDS_LANDSCAPE:
            case HERO_PR_POSTER:
            case HERO_PR_POTRAIT:
            case HERO_SQR_SQUARE:
                HeroAdsLayoutBinding heroAdsLayoutBinding = DataBindingUtil.inflate(LayoutInflater.from(parent.getContext()), R.layout.hero_ads_layout, parent, false);
                return new HeroAdsHolder(heroAdsLayoutBinding);
            case HORIZONTAL_LDS_LANDSCAPE:
                LandscapeRecyclerItemBinding landscapeRecyclerItemBinding = DataBindingUtil.inflate(LayoutInflater.from(parent.getContext()), R.layout.landscape_recycler_item, parent, false);
                LandscapeHolder landscapeHolder = new LandscapeHolder(landscapeRecyclerItemBinding);
                setRecylerProperties(landscapeHolder.landscapeRecyclerItemBinding.recyclerViewList3);
                return landscapeHolder;

            case HORIZONTAL_PR_POTRAIT:
                PotraitRecyclerItemBinding potraitRecyclerItemBinding = DataBindingUtil.inflate(LayoutInflater.from(parent.getContext()), R.layout.potrait_recycler_item, parent, false);
                PortraitHolder portraitHolder = new PortraitHolder(potraitRecyclerItemBinding);
                setRecylerProperties(portraitHolder.potraitRecyclerItemBinding.recyclerViewList4);
                return portraitHolder;

            case HORIZONTAL_PR_POSTER:
                PosterPotraitRecyclerItemBinding potraitRecyclerItemBinding1 = DataBindingUtil.inflate(LayoutInflater.from(parent.getContext()), R.layout.poster_potrait_recycler_item, parent, false);
                PosterPotraitHolder posterPotraitHolder = new PosterPotraitHolder(potraitRecyclerItemBinding1);
                setRecylerProperties(posterPotraitHolder.itemBinding.recyclerViewList2);
                return posterPotraitHolder;

            case HORIZONTAL_SQR_SQUARE:
                SquareRecyclerItemBinding binding2 = DataBindingUtil.inflate(LayoutInflater.from(parent.getContext()), R.layout.square_recycler_item, parent, false);
                SquareHolder squareHolder = new SquareHolder(binding2);
                setRecylerProperties(squareHolder.squareRecyclerItemBinding.recyclerViewList2);
                return squareHolder;

            case HORIZONTAL_CIR_CIRCLE:
                CircleRecyclerItemBinding binding3 = DataBindingUtil.inflate(LayoutInflater.from(parent.getContext()), R.layout.circle_recycler_item, parent, false);
                CircleHolder circleHolder = new CircleHolder(binding3);
                setRecylerProperties(circleHolder.circularRecyclerItemBinding.recyclerViewList1);
                return circleHolder;

            case ADS_BANNER:
            case ADS_MREC:
            case ADS_CUS:
                DfpBannerLayoutBinding dfpBannerLayoutBinding = DataBindingUtil.inflate(LayoutInflater.from(parent.getContext()), R.layout.dfp_banner_layout, parent, false);
                return new DfpBannerHolder(dfpBannerLayoutBinding);
            default:
                DfpBannerLayoutBinding binding = DataBindingUtil.inflate(LayoutInflater.from(parent.getContext()), R.layout.dfp_banner_layout, parent, false);
                return new DfpBannerHolder(binding);

        }
    }


    public void setTitle(HeadingRailsBinding headingRailsBinding, RailCommonData item, int position) {
        headingRailsBinding.shimmerTitleLayout.setVisibility(View.GONE);

        if (item.getScreenWidget().getShowHeader() != null && item.getScreenWidget().getShowHeader() && item.getEnveuVideoItemBeans().size() > 0) {
            headingRailsBinding.headerTitleLayout.setVisibility(View.VISIBLE);
            headingRailsBinding.headingTitle.bringToFront();
            headingRailsBinding.headingTitle.setText((String) item.getScreenWidget().getName());
        } else {
            headingRailsBinding.headerTitleLayout.setVisibility(View.GONE);
        }
        if (item.getScreenWidget().getContentShowMoreButton() != null && item.getScreenWidget().getContentShowMoreButton() && item.getEnveuVideoItemBeans().size() > 0) {
            headingRailsBinding.headerTitleLayout.setVisibility(View.VISIBLE);
            headingRailsBinding.headingTitle.bringToFront();
            headingRailsBinding.moreText.setVisibility(View.VISIBLE);
            headingRailsBinding.moreText.setOnClickListener(v -> {
                moreClickListner.moreRailClick(item, position);

                AppCommonMethod.trackFcmEvent("Video Gallery", "", mContext, 0);
                AppCommonMethod.trackFcmCustomEvent(mContext, AppConstants.GALLERY_SELECT, item.getEnveuVideoItemBeans().get(0).getAssetType(), item.getScreenWidget().getContentID(), item.getScreenWidget().getName() + "", position, "", 0, "", 0, 0, "", "", "", "");
            });
        } else {
            headingRailsBinding.moreText.setVisibility(View.GONE);
        }
    }

    public class HeroAdsHolder extends RecyclerView.ViewHolder {
        HeroAdsLayoutBinding heroAdsHolder;

        HeroAdsHolder(@NonNull HeroAdsLayoutBinding itemHolder) {
            super(itemHolder.getRoot());
            this.heroAdsHolder = itemHolder;
        }
    }

    public class DfpBannerHolder extends RecyclerView.ViewHolder {
        DfpBannerLayoutBinding dfpBannerLayoutBinding;

        DfpBannerHolder(@NonNull DfpBannerLayoutBinding itemView) {
            super(itemView.getRoot());
            dfpBannerLayoutBinding = itemView;
        }
    }

    public class CarouselViewHolder extends RecyclerView.ViewHolder {
        HeaderRecyclerItemBinding itemBinding;

        CarouselViewHolder(HeaderRecyclerItemBinding flightItemLayoutBinding, int viewType) {
            super(flightItemLayoutBinding.getRoot());
            itemBinding = flightItemLayoutBinding;
            LinearLayout.LayoutParams layoutParams = null;
            DisplayMetrics displayMetrics = new DisplayMetrics();
            ((Activity) itemBinding.slider.getContext()).getWindowManager()
                    .getDefaultDisplay()
                    .getMetrics(displayMetrics);
            int width = displayMetrics.widthPixels;
            boolean isTablet = false;

            if (itemBinding.constraintLayout.getResources().getBoolean(R.bool.isTablet))
                isTablet = true;

            switch (viewType) {
                case CAROUSEL_LDS_LANDSCAPE: {
                    int height = (int) (width / 1.80);
                    if (isTablet) {
                        if (itemBinding.slider.getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE) {
                            height = (int) (height / 1.70);
                        } else {
                            height = height + AppCommonMethod.convertDpToPixel(10);
                        }
                    }

                    layoutParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, height);
                    layoutParams.height = (int) (height + (itemBinding.constraintLayout.getContext().getResources().getDimension(R.dimen.carousal_landscape_indicator_padding)));
                }
                break;
                case CAROUSEL_PR_POTRAIT: {
                    int height = (int) (width * 1.77);
                    if (isTablet) {
                        if (itemBinding.slider.getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT) {
                            height = width + AppCommonMethod.convertDpToPixel(10);
                        } else {
                            height = (int) (width / 2.4);
                        }
                    }
                    layoutParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, height);
                    layoutParams.height = (int) (height + itemBinding.constraintLayout.getContext().getResources().getDimension(R.dimen.carousal_potrait_indicator_padding));
                    break;
                }
                case CAROUSEL_SQR_SQUARE: {
                    int height = width;
                    if (isTablet)
                        height = (int) (height / 2);
                    layoutParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, height);
                    layoutParams.height = (int) (height + itemBinding.constraintLayout.getContext().getResources().getDimension(R.dimen.carousal_square_indicator_padding));
                    break;
                }
                case CAROUSEL_CIR_CIRCLE: {
                    layoutParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, (int) itemBinding.constraintLayout.getContext().getResources().getDimension(R.dimen.carousal_square_height));
                    break;
                }

            }
            if (layoutParams != null)
                itemBinding.constraintLayout.setLayoutParams(layoutParams);

        }

    }

    public class PortraitHolder extends RecyclerView.ViewHolder {
        final PotraitRecyclerItemBinding potraitRecyclerItemBinding;

        PortraitHolder(PotraitRecyclerItemBinding flightItemLayoutBinding) {
            super(flightItemLayoutBinding.getRoot());
            potraitRecyclerItemBinding = flightItemLayoutBinding;
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

        PosterPotraitRecyclerItemBinding itemBinding;

        PosterPotraitHolder(PosterPotraitRecyclerItemBinding itemBinding) {
            super(itemBinding.getRoot());
            this.itemBinding = itemBinding;
        }
    }

    public class SquareHolder extends RecyclerView.ViewHolder {
        final SquareRecyclerItemBinding squareRecyclerItemBinding;

        SquareHolder(SquareRecyclerItemBinding flightItemLayoutBinding) {
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

}
