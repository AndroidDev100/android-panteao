package panteao.make.ready.utils.inAppBilling;

import android.app.Dialog;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.DialogFragment;

import java.util.Objects;

import panteao.make.ready.R;
import panteao.make.ready.fragments.dialog.AlertDialogFragment;

public class RestoreSubcriptionsDialod extends DialogFragment {

    static int typeOfDialog=2;
    private RestoreSubcriptionsDialod.RestoreSubcriptionsDialodListener alertDialogListener;

    public RestoreSubcriptionsDialod() {
    }

    public static RestoreSubcriptionsDialod newInstance(String title, String message,String positiveButtonText,String negativeButtonText,int type) {
        typeOfDialog=type;
        RestoreSubcriptionsDialod frag = new RestoreSubcriptionsDialod();
        Bundle args = new Bundle();
        args.putString("title", title);
        args.putString("message", message);
        args.putString("positiveButtonText", positiveButtonText);
        args.putString("negativeButtonText", negativeButtonText);
        frag.setArguments(args);
        return frag;
    }

    public void setAlertDialogCallBack(RestoreSubcriptionsDialod.RestoreSubcriptionsDialodListener alertDialogListener) {
        this.alertDialogListener = alertDialogListener;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        String title = Objects.requireNonNull(getArguments()).getString("title");
        String message = getArguments().getString("message");
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(Objects.requireNonNull(getActivity()), R.style.AppAlertTheme);
        alertDialogBuilder.setTitle(title);
        alertDialogBuilder.setMessage("" + message);

        if (typeOfDialog==1){
            alertDialogBuilder.setPositiveButton("Continue", (dialog, which) -> {
                alertDialogListener.onFinishDialog(1);
                dialog.dismiss();
            });
        }else if (typeOfDialog==2){
            alertDialogBuilder.setPositiveButton("Ok", (dialog, which) -> {
                alertDialogListener.onFinishDialog(2);
                dialog.dismiss();
            });
        }else {
            alertDialogBuilder.setPositiveButton("Ok", (dialog, which) -> {
                alertDialogListener.onFinishDialog(3);
                dialog.dismiss();
            });
        }


        return alertDialogBuilder.create();

    }


    // 1. Defines the listener interface with a method passing back data result.
    public interface RestoreSubcriptionsDialodListener {
        void onFinishDialog(int btnType);
    }
}

