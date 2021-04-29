package panteao.make.ready.utils.BindingUtils;

import android.graphics.drawable.Drawable;
import android.widget.ImageView;

import androidx.databinding.BindingAdapter;

import com.make.enums.ImageType;
import panteao.make.ready.utils.helpers.ImageHelper;

public class ImageBindingUtils {
    @BindingAdapter(value = {"imageUrl", "placeholder", "errorImage", "type"}, requireAll = false)
    public static void addSrc(ImageView imageView, String url, Drawable placeholder, Drawable errorImage, String type) {
        if (url != null) {
            if (placeholder == null) {
                loadImage(imageView, url);
            } else {
                if (type != null && type.equalsIgnoreCase(ImageType.CIR.name())) {
                    loadCIRImage(imageView, url, placeholder);
                } else {
                    loadImage(imageView, url, placeholder);
                }
            }
        }
    }

    private static void loadCIRImage(ImageView imageView, String url, Drawable placeholder) {
        ImageHelper.getInstance(imageView.getContext()).loadCIRImage(imageView, url, placeholder);
    }

    private static void loadImage(ImageView imageView, String url, Drawable placeholder) {
        ImageHelper.getInstance(imageView.getContext()).tabsloadImage(imageView, url, placeholder);
    }


    private static void loadImage(ImageView imageView, String url) {
        /*Glide.with(imageView.getContext())
                .load(url)
                .apply(new RequestOptions().fitCenter())
                .into(imageView);*/
        ImageHelper.getInstance(imageView.getContext()).tabsloadImage(imageView, url, null);
    }
}
