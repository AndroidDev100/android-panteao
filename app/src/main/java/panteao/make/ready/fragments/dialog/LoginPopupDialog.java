package panteao.make.ready.fragments.dialog;

import android.app.Dialog;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.DialogFragment;

import java.util.Objects;

import panteao.make.ready.R;

public class LoginPopupDialog extends DialogFragment {


    private LoginPopupDialog.AlertDialogListener alertDialogListener;

    public LoginPopupDialog() {
    }

    public static LoginPopupDialog newInstance(String title, String message) {
        LoginPopupDialog frag = new LoginPopupDialog();
        Bundle args = new Bundle();
        args.putString("title", title);
        args.putString("message", message);
        frag.setArguments(args);
        return frag;
    }

    public void setAlertDialogCallBack(LoginPopupDialog.AlertDialogListener alertDialogListener) {
        this.alertDialogListener = alertDialogListener;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        String title = Objects.requireNonNull(getArguments()).getString("title");
        //String message = getArguments().getString("message");
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(Objects.requireNonNull(getActivity()), R.style.AppAlertTheme);
        alertDialogBuilder.setTitle(title);
        alertDialogBuilder.setMessage("Please login to play this content");
        alertDialogBuilder.setPositiveButton("Login", (dialog, which) -> {
            alertDialogListener.onFinishDialog();
            dialog.dismiss();
        });
        alertDialogBuilder.setNegativeButton("Cancel", (dialog, which) -> {
            dialog.dismiss();
        });

        return alertDialogBuilder.create();

    }


    // 1. Defines the listener interface with a method passing back data result.
    public interface AlertDialogListener {
        void onFinishDialog();
    }
}
