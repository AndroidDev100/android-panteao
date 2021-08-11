package panteao.make.ready.utils.helpers;

import android.app.Activity;
import android.view.Gravity;

import androidx.core.content.ContextCompat;
import androidx.fragment.app.FragmentActivity;

import panteao.make.ready.R;


public class ToastHandler {
    final Activity activity;

    public ToastHandler(Activity context) {
        this.activity = context;
    }

    public static void show(String message, FragmentActivity activity) {

        new MaterialToast(activity).show(message, ContextCompat.getDrawable(activity, R.drawable.toast_drawable),
                ContextCompat.getColor(activity, R.color.blackColor),
                Gravity.BOTTOM);
    }

    public void show(String message) {
        new MaterialToast(activity).show(message, ContextCompat.getDrawable(activity, R.drawable.toast_drawable),
                ContextCompat.getColor(activity, R.color.blackColor),
                Gravity.BOTTOM);
    }


}
