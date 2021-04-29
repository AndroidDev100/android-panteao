package panteao.make.ready.utils.helpers;

import android.app.Activity;
import android.content.Context;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.RequiresApi;

import panteao.make.ready.R;


/**
 * Created by AnkurYadav on 11/18/2016.
 */

public class MaterialToast {

    final Activity activity;

    public MaterialToast(Activity activity) {
        this.activity = activity;
    }


    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    public void show(String message, Drawable drawable, int color, int gravity) {
        LayoutInflater layoutInflater = (LayoutInflater) activity.getApplicationContext().getSystemService
                (Context.LAYOUT_INFLATER_SERVICE);
        View viewHolder = View.inflate(activity, R.layout.material_toast, null);

        LinearLayout outerLayout = viewHolder.findViewById(R.id.outerLayout);
        outerLayout.setBackground(drawable);

        TextView toastTitle = viewHolder.findViewById(R.id.toastTitle);
        toastTitle.setText(message);
        //CustomeText.setFontRegular(toastTitle);

        Toast toastObject = new Toast(activity);
        toastObject.setDuration(Toast.LENGTH_SHORT);
        toastObject.setGravity(Gravity.BOTTOM, 0, 0);
        toastObject.setView(viewHolder);
        toastObject.show();

    }

}
