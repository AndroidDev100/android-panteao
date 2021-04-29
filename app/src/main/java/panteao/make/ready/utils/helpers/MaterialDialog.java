package panteao.make.ready.utils.helpers;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Intent;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import panteao.make.ready.callbacks.commonCallbacks.DialogInterface;
import panteao.make.ready.utils.cropImage.helpers.Logger;
import panteao.make.ready.R;

import panteao.make.ready.callbacks.commonCallbacks.DialogInterface;
import panteao.make.ready.utils.cropImage.helpers.Logger;

public class MaterialDialog {
    final Activity activity;
    DialogInterface dialogInterface;
    AlertDialog.Builder alertdialog;
    Dialog dialog;

    public MaterialDialog(Activity context) {
        this.activity = context;
    }

    public void showDialog(String title, String message, Activity context, DialogInterface listner) {
        try {

            dialogInterface = listner;
            alertdialog = new AlertDialog.Builder(activity);
            AlertDialog.Builder alert = new AlertDialog.Builder(activity);
            LayoutInflater li = LayoutInflater.from(activity);
            View view = li.inflate(R.layout.version_update_dialog, null);
            alert.setView(view);

            if (dialog == null) {
                dialog = alert.create();
            }
            dialog.setCanceledOnTouchOutside(false);
            dialog.setCancelable(false);
            TextView description_txt=view.findViewById(R.id.des_txt);
            description_txt.setText(activity.getResources().getString(R.string.please_update_your_app));
            LinearLayout negative_button = view.findViewById(R.id.negative_button);
            LinearLayout positive_button = view.findViewById(R.id.positive_button);


            if (title.equals(ForceUpdateHandler.FORCE)) {
                positive_button.setVisibility(View.GONE);
            }

            negative_button.setOnClickListener(view12 -> {

                // dialog.dismiss();
                final String appPackageName = activity.getPackageName();
                try {
                 //   activity.startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://play.google.com/store/apps/details?id=panteao.make.ready")));
                    Intent intent = new Intent(Intent.ACTION_VIEW);
                    intent.setData(Uri.parse("market://details?id=panteao.make.ready"));
                    activity.startActivity(intent);
                } catch (android.content.ActivityNotFoundException anfe) {
                    activity.startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://play.google.com/store/apps/details?id=panteao.make.ready")));
                }

            });

            positive_button.setOnClickListener(view1 -> {
                Logger.w("positivebutton", "cliii");
                dialogInterface.positiveAction();

            });

            dialog.show();

        } catch (Exception e) {
            Logger.e("MaterialDialog", "" + e.toString());

        }

    }

    public void hide() {
        if (dialog.isShowing()) {
            dialog.hide();
        }
    }
}
