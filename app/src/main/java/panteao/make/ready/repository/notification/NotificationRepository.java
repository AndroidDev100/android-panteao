package panteao.make.ready.repository.notification;

import android.app.Activity;

import androidx.recyclerview.widget.LinearLayoutManager;

import panteao.make.ready.activities.notification.adapter.NotificationListAdapter;
import panteao.make.ready.beanModel.beanModel.SingleItemModel;
import panteao.make.ready.databinding.NotificationBinding;

import java.util.ArrayList;

import panteao.make.ready.activities.notification.adapter.NotificationListAdapter;

public class NotificationRepository {
    private static NotificationRepository notificationRepository;

    public synchronized static NotificationRepository getInstance() {

        if (notificationRepository == null) {
            notificationRepository = new NotificationRepository();
        }
        return notificationRepository;
    }

    public void setAdapter(Activity context, NotificationBinding binding) {
        loadData(context, binding);
    }

    private void loadData(Activity activity, NotificationBinding binding) {
        ArrayList<SingleItemModel> singleItem = new ArrayList<>();
        for (int j = 0; j <= 10; j++) {
            singleItem.add(new SingleItemModel("Item " + j, "URL " + j));
        }
        //binding.recyclerView.setHasFixedSize(true);
        NotificationListAdapter notificationListAdapter = new NotificationListAdapter(activity, singleItem);
        binding.recyclerView.setLayoutManager(new LinearLayoutManager(activity));
        binding.recyclerView.setAdapter(notificationListAdapter);
    }

}
