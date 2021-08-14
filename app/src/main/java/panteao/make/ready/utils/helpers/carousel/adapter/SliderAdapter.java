package panteao.make.ready.utils.helpers.carousel.adapter;

import android.content.Context;
import android.os.SystemClock;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.databinding.ViewDataBinding;
import androidx.viewpager.widget.PagerAdapter;

import com.google.gson.Gson;

import panteao.make.ready.beanModel.enveuCommonRailData.RailCommonData;
import panteao.make.ready.beanModelV3.playListModelV2.Thumbnail;
import panteao.make.ready.callbacks.commonCallbacks.CommonRailtItemClickListner;
import panteao.make.ready.enums.KalturaImageType;
import panteao.make.ready.utils.Utils;
import panteao.make.ready.utils.commonMethods.AppCommonMethod;
import panteao.make.ready.utils.config.ImageLayer;
import panteao.make.ready.utils.constants.AppConstants;
import panteao.make.ready.utils.cropImage.helpers.Logger;
import panteao.make.ready.BR;
import panteao.make.ready.R;
import panteao.make.ready.beanModelV3.uiConnectorModelV2.EnveuVideoItemBean;

import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class SliderAdapter extends PagerAdapter {

    private final LayoutInflater layoutInflater;
    private final Context context;
    private RailCommonData items;
    private long mLastClickTime = 0;
    private CommonRailtItemClickListner listner;
    private int viewType;
    private List<EnveuVideoItemBean> videos;
    private int pos;

    public SliderAdapter(@NonNull Context context, RailCommonData items, int viewType, CommonRailtItemClickListner listner, int position) {
        this.context = context;
        this.items = items;
        notifyDataSetChanged();
        layoutInflater = LayoutInflater.from(context);
        this.listner = listner;
        this.viewType = viewType;
        this.videos = items.getEnveuVideoItemBeans();
        this.pos = position;
    }

    @Override
    public int getCount() {
        return items.getEnveuVideoItemBeans().size();
    }

    @Override
    public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
        // The object returned by instantiateItem() is a key/identifier. This method checks whether
        // the View passed to it (representing the page) is associated with that key or not.
        // It is required by a PagerAdapter to function properly.
        return view == object;
    }


    @NonNull
    @Override
    public Object instantiateItem(@NonNull ViewGroup container, final int position) {

        ViewDataBinding viewDataBinding;

        switch (viewType) {
            case AppConstants.CAROUSEL_LDS_LANDSCAPE:
                viewDataBinding = DataBindingUtil.inflate(layoutInflater, R.layout.layout_carousal_landscape_item, container, false);
                break;
            case AppConstants.CAROUSEL_PR_POTRAIT:
                viewDataBinding = DataBindingUtil.inflate(layoutInflater, R.layout.layout_carousal_potrait_item, container, false);
                break;
            case AppConstants.CAROUSEL_SQR_SQUARE:
                viewDataBinding = DataBindingUtil.inflate(layoutInflater, R.layout.layout_carousel_square_item, container, false);
                break;
            case AppConstants.CAROUSEL_CIR_CIRCLE:
                viewDataBinding = DataBindingUtil.inflate(layoutInflater, R.layout.layout_carousel_circle_item, container, false);
                break;
            case AppConstants.CAROUSEL_CST_CUSTOM:
                viewDataBinding = DataBindingUtil.inflate(layoutInflater, R.layout.row_slider_live, container, false);
                break;
            default:
                viewDataBinding = DataBindingUtil.inflate(layoutInflater, R.layout.layout_carousal_landscape_item, container, false);
                break;
        }

        HashMap<String, Thumbnail> crousalImages = videos.get(position).getImages();
        KalturaImageType imageType=KalturaImageType.LANDSCAPE;
        videos.get(position).setPosterURL(ImageLayer.getInstance().getFilteredImage(crousalImages, imageType, 800, 450));

        viewDataBinding.setVariable(BR.assetItem, videos.get(position));
        viewDataBinding.setVariable(BR.adapter, this);
        viewDataBinding.setVariable(BR.position, position);
        container.addView(viewDataBinding.getRoot());
        return viewDataBinding.getRoot();
    }

    public void itemClick(int position) {
        if (SystemClock.elapsedRealtime() - mLastClickTime < 1200) {
            return;
        }
        mLastClickTime = SystemClock.elapsedRealtime();
        listner.railItemClick(items, position);

        AppCommonMethod.trackFcmEvent("Content Screen", "", context, 0);
        AppCommonMethod.trackFcmCustomEvent(context, AppConstants.CONTENT_SELECT, videos.get(position).getAssetType(), items.getScreenWidget().getContentID(), items.getScreenWidget().getName() + "", pos, videos.get(position).getTitle(), position, videos.get(position).getId() + "", 0, 0, "", "", "", "");


    }


    @Override
    public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
        // Removes the page from the container for the given position. We simply removed object using removeView()
        // but could’ve also used removeViewAt() by passing it the position.
        try {
            // Remove the view from the container
            container.removeView((View) object);
            // Invalidate the object
            object = null;
        } catch (Exception e) {
            Logger.e("SliderAdapter", "" + e.toString());
        }
    }

    /**
     * Display the gallery image into the image view provided.
     */
    private void loadImage(ImageView imageView, String url, int corner) {
        if (!TextUtils.isEmpty(url)) {
           /* Glide.with(imageView.getContext()) // Bind it with the context of the actual view used
                    .load(url) // Load the image
                    .bitmapTransform(new CenterCrop(imageView.getContext()), new RoundedCornersTransformations(imageView.getContext(), corner, 0, RoundedCornersTransformations.CornerType.ALL))
                    .animate(R.anim.fade_in) // need to manually set the animation as bitmap cannot use cross fade
                    .into(imageView);*/
        }
    }
}
