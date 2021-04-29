package panteao.make.ready.fragments.dialog;

import android.app.Dialog;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.DialogFragment;

import panteao.make.ready.R;

import java.util.Objects;

public class AlertDialogFragment extends DialogFragment {


    private AlertDialogListener alertDialogListener;

    public AlertDialogFragment() {
    }

    public static AlertDialogFragment newInstance(String title, String message,String positiveButtonText,String negativeButtonText) {
        AlertDialogFragment frag = new AlertDialogFragment();
        Bundle args = new Bundle();
        args.putString("title", title);
        args.putString("message", message);
        args.putString("positiveButtonText", positiveButtonText);
        args.putString("negativeButtonText", negativeButtonText);
        frag.setArguments(args);
        return frag;
    }

    public void setAlertDialogCallBack(AlertDialogListener alertDialogListener) {
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
        alertDialogBuilder.setPositiveButton(getArguments().getString("positiveButtonText"), (dialog, which) -> {
            alertDialogListener.onFinishDialog();
            dialog.dismiss();
        });
        alertDialogBuilder.setNegativeButton(getArguments().getString("negativeButtonText"), (dialog, which) -> {
            dialog.dismiss();
        });

        return alertDialogBuilder.create();

    }


    // 1. Defines the listener interface with a method passing back data result.
    public interface AlertDialogListener {
        void onFinishDialog();
    }
}
