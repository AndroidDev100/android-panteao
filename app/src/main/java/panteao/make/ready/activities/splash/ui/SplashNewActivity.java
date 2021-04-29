package panteao.make.ready.activities.splash.ui;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.LayoutInflater;

import panteao.make.ready.baseModels.BaseActivity;
import panteao.make.ready.baseModels.BaseBindingActivity;
import panteao.make.ready.databinding.ActivitySplashNewBinding;

public class SplashNewActivity extends BaseBindingActivity<ActivitySplashNewBinding> {

    @Override
    public ActivitySplashNewBinding inflateBindingLayout(@NonNull LayoutInflater inflater) {
        return ActivitySplashNewBinding.inflate(inflater);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }
}