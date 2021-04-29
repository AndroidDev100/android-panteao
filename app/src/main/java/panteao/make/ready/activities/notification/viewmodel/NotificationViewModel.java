package panteao.make.ready.activities.notification.viewmodel;

import android.app.Activity;
import android.app.Application;

import androidx.lifecycle.AndroidViewModel;

import panteao.make.ready.repository.notification.NotificationRepository;
import panteao.make.ready.databinding.NotificationBinding;

public class NotificationViewModel extends AndroidViewModel {
    public NotificationViewModel(Application application, Activity activity, NotificationBinding binding) {
        super(application);
        NotificationRepository.getInstance().setAdapter(activity, binding);
    }
}
