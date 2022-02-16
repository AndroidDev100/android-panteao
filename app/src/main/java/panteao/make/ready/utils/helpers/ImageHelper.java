package panteao.make.ready.utils.helpers;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.widget.ImageView;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions;
import com.bumptech.glide.request.RequestOptions;
import com.bumptech.glide.request.target.SimpleTarget;
import panteao.make.ready.utils.cropImage.helpers.Logger;
import panteao.make.ready.utils.cropImage.helpers.PrintLogging;
import panteao.make.ready.R;
import panteao.make.ready.utils.commonMethods.AppCommonMethod;


public class ImageHelper {

    private static final ImageHelper ourInstance = new ImageHelper();
    private static Glide mGlideObj;
    private static RequestOptions requestOptions;

    public static ImageHelper getInstance(Context context) {
        mGlideObj = Glide.get(context);
        requestOptions = new RequestOptions();

        return ourInstance;
    }

    public void loadImageTo(ImageView imageView, String imageUrl) {
        PrintLogging.printLog(imageView.getContext()+" : "+imageUrl);
        requestOptions.placeholder(R.drawable.placeholder_potrait);
        Glide.with(mGlideObj.getContext()).setDefaultRequestOptions(requestOptions).
                load(imageUrl).transition(DrawableTransitionOptions.withCrossFade(250)).thumbnail(0.6f).into(imageView);
    }

    public void loadListImage(ImageView imageView, String imageUrl) {
        PrintLogging.printLog("ImageURLL-->>"+imageUrl);
        requestOptions.placeholder(R.drawable.placeholder_landscape);
        Glide.with(mGlideObj.getContext()).setDefaultRequestOptions(requestOptions).
                load(imageUrl).transition(DrawableTransitionOptions.withCrossFade(250)).thumbnail(0.6f).into(imageView);
    }


    public void loadListSQRImage(ImageView imageView, String imageUrl) {
        requestOptions.placeholder(R.drawable.placeholder_square);
        Glide.with(mGlideObj.getContext()).setDefaultRequestOptions(requestOptions).
                load(imageUrl).transition(DrawableTransitionOptions.withCrossFade(250)).thumbnail(0.6f).into(imageView);
    }

    public void loadImageToProfile(ImageView imageView, String imageUrl) {
        requestOptions.placeholder(R.drawable.profile_dark);

        if (StringUtils.isNullOrEmptyOrZero(imageUrl)) {
            Glide.with(mGlideObj.getContext()).setDefaultRequestOptions(AppCommonMethod.options).
                    load(R.drawable.profile_dark).transition(DrawableTransitionOptions.withCrossFade(250)).thumbnail(0.6f).into(imageView);
        } else {
            Glide.with(mGlideObj.getContext()).setDefaultRequestOptions(AppCommonMethod.options).
                    load(imageUrl).transition(DrawableTransitionOptions.withCrossFade(250)).thumbnail(0.6f).into(imageView);
        }
    }

    public void loadCircleImageTo(ImageView imageView, String imageUrl) {
        requestOptions.placeholder(R.drawable.placeholder_circle);
        Glide.with(mGlideObj.getContext()).setDefaultRequestOptions(requestOptions).
                load(imageUrl).thumbnail(0.6f).into(imageView);

    }

    public void loadImageTo(ImageView imageView, String imageUrl, RequestOptions requestOptions) {
        requestOptions.placeholder(R.drawable.default_player_pic);

        Glide.with(mGlideObj.getContext()).setDefaultRequestOptions(requestOptions).
                load(imageUrl).transition(DrawableTransitionOptions.withCrossFade(250)).thumbnail(0.1f).into(imageView);
    }

    public void loadImageTo(ImageView imageView, Uri imageUrl) {
        Glide.with(mGlideObj.getContext()).setDefaultRequestOptions(requestOptions).
                load(imageUrl).transition(DrawableTransitionOptions.withCrossFade(250)).thumbnail(0.1f).into(imageView);
    }

    public void loadImageTo(String imageUrl, SimpleTarget<Drawable> simpleTarget) {
        Glide.with(mGlideObj.getContext()).
                load(imageUrl).transition(DrawableTransitionOptions.withCrossFade(250)).thumbnail(0.6f).into(simpleTarget);


    }


    public void tabsloadImage(ImageView imageView, String url, Drawable placeholder) {
        if (placeholder==null){
            Glide.with(imageView.getContext())
                    .load(url).transition(DrawableTransitionOptions.withCrossFade(250))
                    .apply(new RequestOptions()
                    .fitCenter())
                    .into(imageView);
        }else {
            Glide.with(imageView.getContext())
                    .load(url).transition(DrawableTransitionOptions.withCrossFade(250))
                    .apply(new RequestOptions().placeholder(placeholder)
                    .error(placeholder))
                    .into(imageView);
        }

    }

    public void loadCIRImage(ImageView imageView, String url, Drawable placeholder) {
        if (placeholder==null){
            if (requestOptions!=null){
                requestOptions.placeholder(R.drawable.placeholder_circle);
            }
            Glide.with(imageView.getContext())
                    .load(url)
                    .apply(new RequestOptions().placeholder(placeholder).override(300,300)
                            .error(placeholder))
                    .thumbnail(0.5f)
                    .into(imageView);
        }else {
            Logger.e("ImageHelper", "" + url+"  "+" ");
            Glide.with(imageView.getContext())
                    .load(url)
                    .apply(new RequestOptions().placeholder(placeholder).override(300,300)
                            .error(placeholder))
                    .thumbnail(0.5f)
                    .into(imageView);
           }
    }

    public void loadIAPImage(ImageView imageView) {
        Glide.with(imageView.getContext())
                .load(R.drawable.iap_bg)
                .into(imageView);
    }
}
