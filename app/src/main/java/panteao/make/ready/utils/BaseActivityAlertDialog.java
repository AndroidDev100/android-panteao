package panteao.make.ready.utils;

import android.app.Dialog;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.DialogFragment;

import panteao.make.ready.R;

import java.util.Objects;

public class BaseActivityAlertDialog extends DialogFragment {


    private BaseActivityAlertDialog.AlertDialogListener alertDialogListener;


    public BaseActivityAlertDialog() {
    }

    public static BaseActivityAlertDialog newInstance(String title, String message, String positiveButtonText) {
        BaseActivityAlertDialog frag = new BaseActivityAlertDialog();

        Bundle args = new Bundle();
        args.putString("title", title);
        args.putString("message", message);
        args.putString("positiveButtonText", positiveButtonText);
        frag.setArguments(args);
        return frag;
    }

    public void setAlertDialogCallBack(BaseActivityAlertDialog.AlertDialogListener alertDialogListener) {
        this.alertDialogListener = alertDialogListener;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
//        return super.onCreateDialog(savedInstanceState);
        AlertDialog.Builder alertDialogBuilder;
        String title = Objects.requireNonNull(getArguments()).getString("title");
        String message = getArguments().getString("message");
        String positiveButtonText = getArguments().getString("positiveButtonText");

        alertDialogBuilder = new AlertDialog.Builder(Objects.requireNonNull(getActivity()), R.style.AppAlertTheme);

        if (Objects.requireNonNull(title).equalsIgnoreCase("Error")) {
            alertDialogBuilder.setTitle("");
        } else {
            alertDialogBuilder.setTitle(title);
        }


        alertDialogBuilder.setMessage("" + message);
        alertDialogBuilder.setPositiveButton("" + positiveButtonText, (dialog, which) -> {
            // on success
//                AlertDialogListener alertDialogListener = (AlertDialogListener) getActivity();
            alertDialogListener.onDialogFinished();
            dialog.dismiss();
        });

        if (Objects.requireNonNull(message).contains("remove video")) {
            alertDialogBuilder.setNegativeButton("" + "Cancel", (dialog, which) -> {
                // on success
                dialog.dismiss();
            });
        }
        return alertDialogBuilder.create();
    }


    // 1. Defines the listener interface with a method passing back data result.
    public interface AlertDialogListener {
        void onDialogFinished();
    }
}
