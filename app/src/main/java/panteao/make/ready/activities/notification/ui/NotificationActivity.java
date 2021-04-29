package panteao.make.ready.activities.notification.ui;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import panteao.make.ready.R;
import panteao.make.ready.databinding.NotificationBinding;
import panteao.make.ready.utils.helpers.ToolBarHandler;

public class NotificationActivity extends AppCompatActivity {

    NotificationBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        callBinding();
    }

    private void callBinding() {

        binding = DataBindingUtil.setContentView(this, R.layout.notification);
        new ToolBarHandler(this).setAction(binding.toolbar, "notification");
    }


}
