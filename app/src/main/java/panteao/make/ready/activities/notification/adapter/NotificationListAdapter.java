package panteao.make.ready.activities.notification.adapter;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import panteao.make.ready.R;
import panteao.make.ready.beanModel.beanModel.SingleItemModel;

import java.util.ArrayList;

public class NotificationListAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    final Activity activity;
    private final ArrayList<SingleItemModel> itemsList;

    public NotificationListAdapter(Activity activity, ArrayList<SingleItemModel> itemsList) {
        this.activity = activity;
        this.itemsList = itemsList;

    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.notification_item, null);
        return new SingleItemRowHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder viewHolder, int i) {

    }

    @Override
    public int getItemCount() {
        return (null != itemsList ? itemsList.size() : 0);
    }

    public class SingleItemRowHolder extends RecyclerView.ViewHolder {

        public SingleItemRowHolder(View view) {
            super(view);


        }


    }

}

