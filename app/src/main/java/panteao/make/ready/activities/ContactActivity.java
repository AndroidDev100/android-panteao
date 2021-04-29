package panteao.make.ready.activities;

import androidx.annotation.NonNull;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;

import panteao.make.ready.databinding.ActivityContactBinding;
import panteao.make.ready.utils.constants.AppConstants;

import java.net.URISyntaxException;

import panteao.make.ready.baseModels.BaseBindingActivity;

public class ContactActivity extends BaseBindingActivity<ActivityContactBinding> {

    @Override
    public ActivityContactBinding inflateBindingLayout(@NonNull LayoutInflater inflater) {
        return ActivityContactBinding.inflate(inflater);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setCLicks();
    }

    private void setCLicks() {
        getBinding().bank.setOnClickListener(v -> {
            Intent email= new Intent(Intent.ACTION_SENDTO);
            email.setData(Uri.parse("mailto:"));
            startActivity(email);
        });
        getBinding().line.setOnClickListener(v -> {
            Intent intent = new Intent();
            try {
                intent = Intent.parseUri(AppConstants.LINE_URI, Intent.URI_INTENT_SCHEME);
            } catch (URISyntaxException e) {
                e.printStackTrace();
            }
            startActivity(intent);
        });
    }
}