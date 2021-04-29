package panteao.make.ready.utils.helpers;

import android.content.Context;
import android.content.res.AssetManager;
import android.graphics.Typeface;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.TextView;

import androidx.collection.LruCache;


public final class CustomeText {
    public static final String FONT_THIN = "Raleway-Regular.ttf";
    public static final String FONT_LIGHT = "Raleway-Regular.ttf";
    public static final String FONT_REGULAR = "Raleway-Regular.ttf";
    public static final String FONT_MEDIUM = "Raleway-Regular.ttf";
    public static final String FONT_BOLD = "Raleway-Regular.ttf";
    public static final String FONT_BLACK = "Raleway-Regular.ttf";
    public static final String MORVA_LIGHT = "Raleway-Regular.ttf";
    public static final String RALEWAY_LIGHT = "Raleway-Regular.ttf";

    private static LruCache<String, Typeface> mCache;
    private static AssetManager mAssetManager;

    public static void initialize(Context context) {
        mAssetManager = context.getAssets();
        mCache = new LruCache<>(6);
    }

    public static Typeface getTypeface(final String filename) {
        Typeface typeface = mCache.get(filename);
        if (typeface == null) {
            typeface = Typeface.createFromAsset(mAssetManager, "fonts/" + filename);
            mCache.put(filename, typeface);
        }
        return typeface;
    }

    public static void setFont(String typefaceName, TextView... tv) {
        Typeface typeface = getTypeface(typefaceName);
        for (TextView textView : tv) {
            if (textView != null)
                textView.setTypeface(typeface);
        }
    }

    public static void setFontThin(TextView... tv) {
        Typeface typeface = getTypeface(FONT_THIN);
        for (TextView textView : tv) {
            if (textView != null)
                textView.setTypeface(typeface);
        }
    }

    public static void setFontLight(TextView... tv) {
        Typeface typeface = getTypeface(FONT_LIGHT);
        for (TextView textView : tv) {
            if (textView != null)
                textView.setTypeface(typeface);
        }
    }

    public static void setFontRegular(TextView... tv) {
        Typeface typeface = getTypeface(FONT_REGULAR);
        for (TextView textView : tv) {
            if (textView != null)
                textView.setTypeface(typeface);
        }
    }

    public static void setFontMedium(TextView... tv) {
        Typeface typeface = getTypeface(FONT_MEDIUM);
        for (TextView textView : tv) {
            if (textView != null)
                textView.setTypeface(typeface);
        }
    }

    public static void setFontRadioMedium(RadioButton... tv) {
        Typeface typeface = getTypeface(FONT_MEDIUM);
        for (TextView textView : tv) {
            if (textView != null)
                textView.setTypeface(typeface);
        }
    }

    public static void setFontBold(TextView... tv) {
        Typeface typeface = getTypeface(FONT_BOLD);
        for (TextView textView : tv) {
            if (textView != null)
                textView.setTypeface(typeface);
        }
    }

    public static void setFontBlack(TextView... tv) {
        Typeface typeface = getTypeface(FONT_BLACK);
        for (TextView textView : tv) {
            if (textView != null)
                textView.setTypeface(typeface);
        }
    }


    public static void setRalewayLight(TextView... tv) {
        Typeface typeface = getTypeface(RALEWAY_LIGHT);
        for (TextView textView : tv) {
            if (textView != null)
                textView.setTypeface(typeface);
        }
    }

    public static void setRalewayLightEdt(EditText... edt) {
        Typeface typeface = getTypeface(FONT_REGULAR);
        for (EditText editView : edt) {
            if (editView != null)
                editView.setTypeface(typeface);
        }
    }

    public static void setButtonFontLight(TextView... tv) {
        Typeface typeface = getTypeface(MORVA_LIGHT);
        for (TextView textView : tv) {
            if (textView != null)
                textView.setTypeface(typeface);
        }
    }
}
